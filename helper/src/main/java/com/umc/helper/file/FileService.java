package com.umc.helper.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.file.model.File;
import com.umc.helper.file.model.PostFileRequest;
import com.umc.helper.file.model.PostFileResponse;
import com.umc.helper.folder.Folder;
import com.umc.helper.folder.FolderRepository;
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
import java.util.Optional;
import java.util.UUID;

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
        fileRepository.save(file);
        //File foundFile=fileRepository.findById(file.getId());

        //return new PostFileResponse(foundFile.getFilePath());

        return new PostFileResponse(fileRepository.findById(file.getId()).getFilePath());


    }

}
