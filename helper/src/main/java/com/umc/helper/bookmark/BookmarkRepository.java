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

    public void remove(Bookmark bookmark){
        em.remove(bookmark);
    }
}
