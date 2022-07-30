package com.umc.helper.file;

import com.umc.helper.file.model.File;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;

    public void save(File file){em.persist(file);}

    public File findById(Long fileId){ return em.find(File.class,fileId);}
}
