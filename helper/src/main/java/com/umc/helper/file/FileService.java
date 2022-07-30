package com.umc.helper.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.file.model.*;
import com.umc.helper.folder.Folder;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.link.model.Link;
import com.umc.helper.link.model.PatchLinkStatusResponse;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Client amazonS3Client;
    Logger log= LoggerFactory.getLogger(FileService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     *  해당 폴더 모든 파일 조회
     */
    @Transactional
    public List<GetFilesResponse> retrieveFiles(Long folderId){

        List<File> files=fileRepository.findAllByFolderId(folderId);
        List<GetFilesResponse> result=files.stream()
                .map(f->new GetFilesResponse(f))
                .collect(toList());

        return result;
    }
    /**
     *  파일 업로드
     */
    @Transactional
    public PostFileResponse uploadFile(PostFileRequest postFileReq){

        Optional<Folder> folder=folderRepository.findById(postFileReq.getFolderId());
        Optional<Member> member=memberRepository.findById(postFileReq.getMemberId());

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
        file.setFolder(folder.get());
        file.setMember(member.get());
        file.setVolume(postFileReq.getMultipartFile().getSize());
        file.setStatus(Boolean.TRUE);
        file.setUploadDate(LocalDateTime.now());
        fileRepository.save(file);

        return new PostFileResponse(fileRepository.findById(file.getId()).getFilePath());


    }

    /**
     *  파일 상태 변경
     */
    @Transactional
    public PatchFileStatusResponse modifyFileStatus(Long fileId, Long memberId){

        File file=fileRepository.findById(fileId);
        // 파일 올린 사람과 파일 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(file.getMember().getId()==memberId) {
            file.setStatus(Boolean.FALSE);
            //file.setId(file.getId());
        }

        return new PatchFileStatusResponse(file);
    }
}
