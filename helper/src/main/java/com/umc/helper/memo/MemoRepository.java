package com.umc.helper.memo;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.file.model.File;
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

    public Long findDuplicateMemoName(Long folderId,String memoName){
        return em.createQuery(
                        "select count(m.name) from Memo m"+
                                " where m.name= :memoName"+
                                " and m.folder.id= :folderId",Long.class)
                .setParameter("memoName",memoName)
                .setParameter("folderId",folderId)
                .setMaxResults(1)
                .getSingleResult();
    }
    public List<GetMemosResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                "select new com.umc.helper.memo.model.GetMemosResponse(m.id,m.name,m.content,m.member.username,f.folderName,f.id,bm.id,m.uploadDate,m.lastModifiedDate,m.member.profileImage)"+
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
    public List<GetMemosResponse> findAllInfoByFolderIds(List<Long> folderIds){
        return em.createQuery(
                        "select new com.umc.helper.memo.model.GetMemosResponse(m.id,m.name,m.content,m.member.username,f.folderName,f.id,bm.id,m.uploadDate,m.lastModifiedDate,m.member.profileImage)"+
                                " from Memo m"+
                                " join m.folder f"+
                                " left join Bookmark bm"+
                                " on m.id=bm.memo.id"+
                                " where f.id in (:folderIds)"+
                                " and m.status= :status",GetMemosResponse.class)
                .setParameter("folderIds",folderIds)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public List<Memo> findTrashByMemberId(Long memberId){
        return em.createQuery(
                "select m from Memo m"+
                        " where m.status= :status"+
                        " and m.member.id= :memberId",Memo.class)
                .setParameter("status",Boolean.FALSE)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public void remove(Memo memo){
        em.remove(memo);
    }

    public int removeTrashByMemberId(Long memberId){
        return em.createQuery(
                "delete from Memo m"+
                        " where m.member.id= :memberId"+
                        " and m.status= :status")
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.FALSE)
                 .executeUpdate();
    }

    public List<Memo> findByWord(String word){
        return em.createQuery(
                        "select m from Memo m"+
                                " where m.name like :word"+
                                " and m.status=:status",Memo.class)
                .setParameter("word",word)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public int removeEveryByFolderId(Long folderId){
        return em.createQuery(
                        "delete from Memo m"+
                                " where m.folder.id= :folderId")
                .setParameter("folderId",folderId)
                .executeUpdate();
    }

    public int removeByMemberId(Long memberId){
        return em.createQuery(
                        "delete from Memo m"+
                                " where m.member.id= :memberId")
                .setParameter("memberId",memberId)
                .executeUpdate();
    }
}
