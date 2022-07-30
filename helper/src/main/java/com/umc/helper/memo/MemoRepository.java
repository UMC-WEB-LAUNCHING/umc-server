package com.umc.helper.memo;

import com.umc.helper.link.model.Link;
import com.umc.helper.memo.model.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemoRepository {

    private final EntityManager em;

    public void save(Memo memo){
        em.persist(memo);
    }

    public Memo findById(Long memoId){
        return em.find(Memo.class,memoId);
    }

    public List<Memo> findAllByFolderId(Long folderId){
        return em.createQuery(
                        "select m from Memo m"+
                                " join fetch m.folder f"+
                                " where m.status= :status",Memo.class)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
}
