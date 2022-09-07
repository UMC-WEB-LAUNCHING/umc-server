package com.umc.helper.bookmark;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.memo.model.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class BookmarkRepository {
    private final EntityManager em;

    public void save(Bookmark bookMark){
        em.persist(bookMark);
    }

    public Bookmark findById(Long bookmarkId){
        return em.find(Bookmark.class,bookmarkId);
    }

    public List<Bookmark> findAllByMemberId(Long memberId){
        return em.createQuery(
                        "select bm from Bookmark bm"+
                                " join fetch bm.member m"+
                                " where m.id= :id", Bookmark.class)
                .setParameter("id",memberId)
                .getResultList();
    }
    public List<Bookmark> findBookmarkByMemberIdWithCategoryItemId(Long memberId,Long itemId,String category){
        return em.createQuery(
                "select bm from Bookmark bm"+
                        " where bm.member.id= :memberId"+
                        " and bm.category= :category",Bookmark.class)
                .setParameter("memberId",memberId)
                .setParameter("category",category)
                .getResultList();
    }
    public int removeBookmarkMemo(Long memoId,Long memberId){
        return em.createQuery(
                "delete from Bookmark bm"+
                        " where bm.memo.id= :memoId")
                .setParameter("memoId",memoId)
                .executeUpdate();
    }

    public int removeBookmarkLink(Long linkId,Long memberId){
        return em.createQuery(
                        "delete from Bookmark bm"+
                                " where bm.link.id= :linkId")
                .setParameter("linkId",linkId)
                .executeUpdate();
//        return em.createQuery(
//                        "delete from Bookmark bm"+
//                                " where bm.member.id= :memberId"+
//                                " and bm.link.id= :linkId")
//                .setParameter("memberId",memberId)
//                .setParameter("linkId",linkId)
//                .executeUpdate();
    }

    public int removeBookmarkFile(Long fileId,Long memberId){
        return em.createQuery(
                        "delete from Bookmark bm"+
                                " where bm.file.id= :fileId")
                .setParameter("fileId",fileId)
                .executeUpdate();
    }

    public void removeBookmarkImage(Long imageId,Long memberId){
         em.createQuery(
                        "delete from Bookmark bm"+
                                " where bm.image.id= :imageId")
                .setParameter("imageId",imageId)
                .executeUpdate();
    }

    public void removeBookmarkFolder(Long folderId,Long memberId){
        em.createQuery(
                        "delete from Bookmark bm"+
                                " where bm.folder.id= :folderId")
                .setParameter("folderId",folderId)
                .executeUpdate();
    }
    public void remove(Bookmark bookmark){
        em.remove(bookmark);
    }
}
