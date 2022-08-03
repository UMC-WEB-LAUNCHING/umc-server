package com.umc.helper.folder;

import com.umc.helper.folder.model.Folder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FolderRepository {

    private final EntityManager em;

    public void save(Folder folder){ em.persist(folder);}

    public Folder findById(Long folderId){ return em.find(Folder.class,folderId);}

    public List<Folder> findAllByTeamId(Long teamId){
        return em.createQuery(
                "select f from Folder f"+
                        " where f.team.teamIdx= :teamId"+
                        " and f.status= :status", Folder.class)
                .setParameter("teamId",teamId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }

    public List<Folder> findAllByMemberId(Long memberId){
        return em.createQuery(
                "select f from Folder f"+
                         " where f.member.id= :memberId"+
                        " and f.status= :status",Folder.class)
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }

    public void remove(Folder folder){
        em.remove(folder);
    }

    public List<Folder> findTrashByMemberId(Long memberId){
        return em.createQuery(
                "select f from Folder f"+
                        " where f.status= :status"+
                        " and f.creatorId= :memberId",Folder.class)
                .setParameter("status",Boolean.FALSE)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public int removeTrashByMemberId(Long memberId){
        return em.createQuery(
                        "delete from Folder f"+
                                " where f.creatorId= :memberId"+
                                " and f.status= :status")
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.FALSE)
                .executeUpdate();

    }
}
