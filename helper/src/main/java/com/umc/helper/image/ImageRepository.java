package com.umc.helper.image;

import com.umc.helper.file.model.File;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.memo.model.GetMemosResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    public void save(Image image){em.persist(image);}

    public Image findById(Long imageId){ return em.find(Image.class,imageId);}

    public List<Image> findAllByFolderId(Long folderId){
        return em.createQuery(
                        "select i from Image i"+
                                " join fetch i.folder f"+
                                " where i.status= :status"+
                                " and f.id= :folderId",Image.class)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderId",folderId)
                .getResultList();
    }

    public List<GetImagesResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                        "select new com.umc.helper.image.model.GetImagesResponse(i.id,i.filePath,i.originalFileName,i.member.username,f.folderName,bm.id,i.uploadDate)"+
                                " from Image i"+
                                " join i.folder f"+
                                " left join Bookmark bm"+
                                " on i.id=bm.image.id"+
                                " where f.id= :folderId"+
                                " and i.status= :status",GetImagesResponse.class)
                .setParameter("folderId",folderId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }


}
