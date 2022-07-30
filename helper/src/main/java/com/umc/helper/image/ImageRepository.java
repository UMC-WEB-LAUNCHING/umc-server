package com.umc.helper.image;

import com.umc.helper.file.model.File;
import com.umc.helper.image.model.Image;
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
                                " join fetch i.folder ff"+
                                " where i.status= :status",Image.class)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
}
