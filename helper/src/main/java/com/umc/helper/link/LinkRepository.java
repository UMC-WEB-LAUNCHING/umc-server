package com.umc.helper.link;

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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LinkRepository {

    private final EntityManager em;

    public void save(Link link){
        em.persist(link);
    }

    public Link findById(Long linkId){
        return em.find(Link.class,linkId);
    }

    public List<Link> findAllByFolderId(Long folderId){
        return em.createQuery(
                "select l from Link l"+
                            " join fetch l.folder f"+
                            " where l.status= :status"+
                            " and f.id= :folderId",Link.class)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderId",folderId)
                .getResultList();
    }

    public List<GetLinksResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                        "select new com.umc.helper.link.model.GetLinksResponse(l.id,l.url,l.name,l.member.username,f.folderName,bm.id,l.uploadDate,l.lastModifiedDate,l.member.profileImage)"+
                                " from Link l"+
                                " join l.folder f"+
                                " left join Bookmark bm"+
                                " on l.id=bm.link.id"+
                                " where f.id= :folderId"+
                                " and l.status= :status",GetLinksResponse.class)
                .setParameter("folderId",folderId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public List<GetLinksResponse> findAllInfoByFolderIds(List<Long> folderIds){
        return em.createQuery(
                        "select new com.umc.helper.link.model.GetLinksResponse(l.id,l.url,l.name,l.member.username,f.folderName,bm.id,l.uploadDate,l.lastModifiedDate,l.member.profileImage)"+
                                " from Link l"+
                                " join l.folder f"+
                                " left join Bookmark bm"+
                                " on l.id=bm.link.id"+
                                " where f.id in (:folderIds)"+
                                " and l.status= :status",GetLinksResponse.class)
                .setParameter("folderIds",folderIds)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }

    public List<Link> findTrashByMemberId(Long memberId){
        return em.createQuery(
                        "select l from Link l"+
                                " where l.status= :status"+
                                " and l.member.id= :memberId",Link.class)
                .setParameter("status",Boolean.FALSE)
                .setParameter("memberId",memberId)
                .getResultList();
    }
    public Long findDuplicateLinkName(Long folderId,String linkName){
        return em.createQuery(
                        "select count(l.name) from Link l"+
                                " where l.name= :linkName"+
                                " and l.folder.id= :folderId",Long.class)
                .setParameter("linkName",linkName)
                .setParameter("folderId",folderId)
                .setMaxResults(1)
                .getSingleResult();
    }
    public void remove(Link link){
        em.remove(link);
    }

    public int removeTrashByMemberId(Long memberId){
        return em.createQuery(
                        "delete from Link l"+
                                " where l.member.id= :memberId"+
                                " and l.status= :status")
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.FALSE)
                .executeUpdate();
    }

    public List<Link> findByWord(String word){
        return em.createQuery(
                        "select l from Link l"+
                                " where l.name like :word"+
                                " and l.status=:status",Link.class)
                .setParameter("word",word)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public int removeEveryByFolderId(Long folderId){
        return em.createQuery(
                        "delete from Link l"+
                                " where l.folder.id= :folderId")
                .setParameter("folderId",folderId)
                .executeUpdate();
    }
}
