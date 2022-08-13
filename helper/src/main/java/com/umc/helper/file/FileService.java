package com.umc.helper.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.file.model.*;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.exception.FolderNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.TeamMemberRepository;
import com.umc.helper.team.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    private final TeamMemberRepository teamMemberRepository;
    private final AmazonS3Client amazonS3Client;
    Logger log= LoggerFactory.getLogger(FileService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     *  해당 폴더 모든 파일 조회
     */
//    @Transactional
//    public List<GetFilesResponse> retrieveFiles(Long folderId){
//
//        List<File> files=fileRepository.findAllByFolderId(folderId);
//        List<GetFilesResponse> result=files.stream()
//                .map(f->new GetFilesResponse(f))
//                .collect(toList());
//
//        return result;
//    }

    /**
     *  해당 폴더 모든 파일 조회 - 모든 정보(즐겨찾기 여부 등)
     */
    @Transactional
    public List<GetFilesResponse> retrieveFiles(Long folderId){
        Folder folder=folderRepository.findById(folderId);
        folder.notExistFolder();

        List<GetFilesResponse> result=fileRepository.findAllInfoByFolderId(folderId);

        return result;
    }

    /**
     *  파일 업로드
     */
    @Transactional
    public PostFileResponse uploadFile(PostFileRequest postFileReq){

        Folder folder=folderRepository.findById(postFileReq.getFolderId());
        folder.notExistFolder(); // 폴더 존재 확인

        Member member=memberRepository.findById(postFileReq.getMemberId()).get();
        member.notExistMember(); // 업로더 존재 확인

        // 폴더 내 업로드 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember().getId()!=null) folder.invalidUploader(member.getId()); // 개인 폴더
        else if(folder.getMember().getId()==null && folder.getTeam().getTeamIdx()!=null){ // 팀 폴더
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.getId());
            teamMember.notExistTeamMember();
        }

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(postFileReq.getMultipartFile().getContentType());
        objectMetadata.setContentLength(postFileReq.getMultipartFile().getSize());

        String originalFileName= postFileReq.getMultipartFile().getOriginalFilename();
        int index=originalFileName.lastIndexOf(".");
        String ext= originalFileName.substring(index+1); // 확장자

        String storeFileName= UUID.randomUUID()+"."+ext; // 저장되는 이름
        String key="files/"+storeFileName;

        try (InputStream inputStream = postFileReq.getMultipartFile().getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();

        File file=new File();
        file.setFileName(storeFileName);
        file.setOriginalFileName(originalFileName);
        file.setFilePath(storeFileUrl);
        file.setFolder(folder);
        file.setMember(member);
        file.setVolume(postFileReq.getMultipartFile().getSize());
        file.setStatus(Boolean.TRUE);
        file.setUploadDate(LocalDateTime.now());
        file.setLastModifiedDate(LocalDateTime.now());
        folder.setLastModifiedDate(LocalDateTime.now());
        fileRepository.save(file);

        return new PostFileResponse(fileRepository.findById(file.getId()).getFilePath());


    }

    /**
     *  파일 상태 변경 - 쓰레기통으로,,,
     */
    @Transactional
    public PatchFileStatusResponse modifyFileStatus(Long fileId, Long memberId){

        File file=fileRepository.findById(fileId);
        file.notExistFile(); // 파일 존재 확인

        Folder folder=folderRepository.findById(file.getFolder().getId());

        // 파일 올린 사람과 파일 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(file.getMember().getId()==memberId) {
            file.setStatus(Boolean.FALSE);
            file.setStatusModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(file.getStatusModifiedDate());
        }

        return new PatchFileStatusResponse(file);
    }

    /**
     *  파일 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long folderId, Long fileId, Long memberId){

        Member member=memberRepository.findById(memberId).get();
        member.notExistMember();

        File file=fileRepository.findById(fileId);
        file.notExistFile();

        Folder folder=folderRepository.findById(folderId);
        folder.notExistFolder(); // 폴더 존재 확인


        // 파일 북마크 등록 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember().getId()!=null) folder.invalidUploader(member.getId()); // 개인 폴더 내 파일
        else if(folder.getMember().getId()==null && folder.getTeam().getTeamIdx()!=null){ // 팀 폴더 내 파일
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.getId());
            teamMember.notExistTeamMember();
        }

        Bookmark bookmark=new Bookmark();
        bookmark.setFile(file);
        bookmark.setMember(member);
        bookmark.setCategory("file");
        bookmark.setAddedDate(LocalDateTime.now());
        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("file",fileId,bookmarkRepository.findById(bookmark.getId()).getId());
    }

    /**
     * s3 버킷에서 파일 삭제
     */
    public void deleteFromS3(String storeFileName){
        String key="files/"+storeFileName;
        amazonS3Client.deleteObject(bucket,key);
    }

    /**
     *  파일 이름 변경
     */
    @Transactional
    public PatchFileResponse modifyFile(Long fileId, PatchFileRequest patchFileReq){
        File file=fileRepository.findById(fileId);
        file.notExistFile();

        Folder folder=folderRepository.findById(file.getFolder().getId());

        if(file.getMember().getId()==patchFileReq.getMemberId()){
            file.setOriginalFileName(patchFileReq.getName());
            file.setLastModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(file.getLastModifiedDate());
        }

        return new PatchFileResponse(file);
    }

}
