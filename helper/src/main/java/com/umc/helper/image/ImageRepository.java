package com.umc.helper.image;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.file.model.File;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.Link;
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
                        "select new com.umc.helper.image.model.GetImagesResponse(i.id,i.filePath,i.originalFileName,i.member.username,f.folderName,bm.id,i.volume,i.uploadDate,i.lastModifiedDate)"+
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

    public List<Image> findTrashByMemberId(Long memberId){
        return em.createQuery(
                        "select i from Image i"+
                                " where i.status= :status"+
                                " and i.member.id= :memberId",Image.class)
                .setParameter("status",Boolean.FALSE)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public void remove(Image image){
        em.remove(image);
    }

    public int removeTrashByMemberId(Long memberId){
        return em.createQuery(
                        "delete from Image i"+
                                " where i.member.id= :memberId"+
                                " and i.status= :status")
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.FALSE)
                .executeUpdate();
    }

    public List<Image> findByWord(String word){
        return em.createQuery(
                        "select i from Image i"+
                                " where i.fileName like :word"+
                                " and i.status=:status",Image.class)
                .setParameter("word",word)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
}
