package com.umc.helper.memo;

import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.link.model.Link;
import com.umc.helper.memo.model.GetMemosResponse;
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
                                " where m.status= :status"+
                                " and f.id= :folderId",Memo.class)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderId",folderId)
                .getResultList();
    }

    public List<GetMemosResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                "select new com.umc.helper.memo.model.GetMemosResponse(m.id,m.name,m.content,m.member.username,f.folderName,bm.id,m.uploadDate,m.lastModifiedDate)"+
                        " from Memo m"+
                        " join m.folder f"+
                        " left join Bookmark bm"+
                        " on m.id=bm.memo.id"+
                        " where f.id= :folderId"+
                        " and m.status= :status",GetMemosResponse.class)
                .setParameter("folderId",folderId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
}
