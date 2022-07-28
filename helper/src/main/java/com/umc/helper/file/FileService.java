package com.umc.helper.file;

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
    public Long saveFile(FileDto fileDto) {
        logger.info(">>fileDto:{}",fileDto);

        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id) {
        File file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(file.getOriginalFileName())
                .filename(file.getFileName())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}
