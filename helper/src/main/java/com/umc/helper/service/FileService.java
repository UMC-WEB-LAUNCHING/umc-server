package com.umc.helper.service;

import com.umc.helper.domain.Files;
import com.umc.helper.dto.FileDto;
import com.umc.helper.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {

    private FileRepository fileRepository;
    Logger logger= LoggerFactory.getLogger(FileService.class);
    @Transactional
    public int saveFile(FileDto fileDto) {
        logger.info(">>fileDto:{}",fileDto);

        return fileRepository.save(fileDto.toEntity()).getFileIdx();
    }

    @Transactional
    public FileDto getFile(int id) {
        Files file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(file.getOriginalFileName())
                .filename(file.getFileName())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}
