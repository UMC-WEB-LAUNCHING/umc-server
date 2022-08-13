package com.umc.helper.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.file.model.File;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.model.*;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.TeamMemberRepository;
import com.umc.helper.team.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final AmazonS3Client amazonS3Client;
    Logger log= LoggerFactory.getLogger(ImageService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     *  해당 폴더 모든 이미지 조회
     */
//    @Transactional
//    public List<GetImagesResponse> retrieveImages(Long folderId){
//
//        List<Image> images=imageRepository.findAllByFolderId(folderId);
//        List<GetImagesResponse> result=images.stream()
//                .map(i->new GetImagesResponse(i))
//                .collect(toList());
//
//        return result;
//    }

    /**
     *  해당 폴더 모든 이미지 조회
     */
    @Transactional
    public List<GetImagesResponse> retrieveImages(Long folderId){
        Folder folder=folderRepository.findById(folderId);
        folder.notExistFolder(); // 폴더 존재 확인

        List<GetImagesResponse> result=imageRepository.findAllInfoByFolderId(folderId);

        return result;
    }

    /**
     *  이미지 업로드
     */
    @Transactional
    public PostImageResponse uploadImage(PostImageRequest postImageReq){

        Folder folder=folderRepository.findById(postImageReq.getFolderId());
        folder.notExistFolder(); // 폴더 존재 확인

        Member member=memberRepository.findById(postImageReq.getMemberId()).get();
        member.notExistMember(); // 업로더 존재 확인

        // 폴더 내 업로드 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember().getId()!=null) folder.invalidUploader(member.getId()); // 개인 폴더
        else if(folder.getMember().getId()==null && folder.getTeam().getTeamIdx()!=null){ // 팀 폴더
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.getId());
            teamMember.notExistTeamMember();
        }

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(postImageReq.getMultipartFile().getContentType());
        objectMetadata.setContentLength(postImageReq.getMultipartFile().getSize());

        String originalFileName= postImageReq.getMultipartFile().getOriginalFilename();
        int index=originalFileName.lastIndexOf(".");
        String ext= originalFileName.substring(index+1); // 확장자

        String storeFileName= UUID.randomUUID()+"."+ext; // 저장되는 이름
        String key="images/"+storeFileName;

        try (InputStream inputStream = postImageReq.getMultipartFile().getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();

        Image image=new Image();
        image.setFileName(storeFileName);
        image.setOriginalFileName(originalFileName);
        image.setFilePath(storeFileUrl);
        image.setFolder(folder);
        image.setMember(member);
        image.setVolume(postImageReq.getMultipartFile().getSize());
        image.setStatus(Boolean.TRUE);
        image.setUploadDate(LocalDateTime.now());
        folder.setLastModifiedDate(LocalDateTime.now());
        imageRepository.save(image);

        return new PostImageResponse(imageRepository.findById(image.getId()).getFilePath());


    }

    /**
     *  이미지 상태 변경 - 쓰레기통으로,,,
     */
    @Transactional
    public PatchImageStatusResponse modifyImageStatus(Long imageId, Long memberId){

        Image image=imageRepository.findById(imageId);
        image.notExistImage(); // 이미지 존재 확인

        Folder folder=folderRepository.findById(image.getFolder().getId());

        // 파일 올린 사람과 파일 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(image.getMember().getId()==memberId) {
            image.setStatus(Boolean.FALSE);
            image.setStatusModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(image.getStatusModifiedDate());
        }

        return new PatchImageStatusResponse(image);
    }

    /**
     *  이미지 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long folderId, Long imageId, Long memberId){

        Member member=memberRepository.findById(memberId).get();
        member.notExistMember();

        Image image=imageRepository.findById(imageId);
        image.notExistImage(); // 이미지 존재 확인

        Folder folder=folderRepository.findById(folderId);
        folder.notExistFolder(); // 폴더 존재 확인

        // 파일 북마크 등록 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember().getId()!=null) folder.invalidUploader(member.getId()); // 개인 폴더 내 파일
        else if(folder.getMember().getId()==null && folder.getTeam().getTeamIdx()!=null){ // 팀 폴더 내 파일
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.getId());
            teamMember.notExistTeamMember();
        }

        Bookmark bookmark=new Bookmark();
        bookmark.setImage(image);
        bookmark.setMember(member);
        bookmark.setCategory("image");
        bookmark.setAddedDate(LocalDateTime.now());

        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("image",imageId,bookmarkRepository.findById(bookmark.getId()).getId());
    }

    /**
     * s3 버킷에서 이미지 삭제
     */
    public void deleteFromS3(String storeFileName){
        String key="images/"+storeFileName;
        amazonS3Client.deleteObject(bucket,key);
    }

    /**
     *  이미지 이름 변경
     */
    @Transactional
    public PatchImageResponse modifyImage(Long imageId,PatchImageRequest patchImageReq){
        Image image=imageRepository.findById(imageId);
        image.notExistImage(); // 이미지 존재 확인

        Folder folder=folderRepository.findById(image.getFolder().getId());

        if(image.getMember().getId()==patchImageReq.getMemberId()){
            image.setOriginalFileName(patchImageReq.getName());
            image.setLastModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(image.getLastModifiedDate());
        }

        return new PatchImageResponse(image);
    }
}
