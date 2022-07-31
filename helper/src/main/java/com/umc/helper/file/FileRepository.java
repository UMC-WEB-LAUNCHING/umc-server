package com.umc.helper.file;

import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.memo.model.GetMemosResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;

    public void save(File file){em.persist(file);}

    public File findById(Long fileId){ return em.find(File.class,fileId);}

    public List<File> findAllByFolderId(Long folderId){
        return em.createQuery(
                "select f from File f"+
                                " join fetch f.folder ff"+
                                " where f.status= :status"+
                                " and ff.id= :folderId",File.class)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderId",folderId)
                .getResultList();
    }

    public List<GetFilesResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                        "select new com.umc.helper.file.model.GetFilesResponse(f.id,f.filePath,f.originalFileName,f.member.username,ff.folderName,bm.id,f.uploadDate)"+
                                " from File f"+
                                " join f.folder ff"+
                                " left join Bookmark bm"+
                                " on f.id=bm.file.id"+
                                " where ff.id= :folderId"+
                                " and f.status= :status",GetFilesResponse.class)
                .setParameter("folderId",folderId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
}
