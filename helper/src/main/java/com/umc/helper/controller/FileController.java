package com.umc.helper.controller;

import com.umc.helper.file.MD5Generator;
import com.umc.helper.file.FileDto;
import com.umc.helper.file.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class FileController {

    private Logger logger= LoggerFactory.getLogger(FileController.class);

    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile files) throws IOException, NoSuchAlgorithmException {
        String origFilename = files.getOriginalFilename();
        String filename = new MD5Generator(origFilename).toString();
        /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
        String savePath = System.getProperty("user.dir") + "\\files";
        /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
        if (!new File(savePath).exists()) {
            try {
                new File(savePath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        String filePath = savePath + "\\" + filename;
        logger.info("filePath: {}",filePath);
        files.transferTo(new File(filePath));

        FileDto fileDto = new FileDto();
        fileDto.setOrigFilename(origFilename);
        logger.info("origFilename: {}",origFilename);

        fileDto.setFilename(filename);
        logger.info("filename: {}",filename);

        fileDto.setFilePath(filePath);

        Long fileId= fileService.saveFile(fileDto);
        return "redirect:/";
    }

}
