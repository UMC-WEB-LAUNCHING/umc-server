package com.umc.helper.folder;

import com.umc.helper.file.model.File;
import com.umc.helper.folder.model.Folder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
    public List<Folder> findAllByTeamIds(List<Long> teamIds){
        return em.createQuery(
                        "select f from Folder f"+
                                " where f.team.teamIdx in (:teamIds)"+
                                " and f.status= :status", Folder.class)
                .setParameter("teamIds",teamIds)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public List<Folder> findEveryByTeamId(Long teamId){
        return em.createQuery(
                        "select f from Folder f"+
                                " where f.team.teamIdx= :teamId", Folder.class)
                .setParameter("teamId",teamId)
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

    public Long findDuplicateTeamFolderName(Long teamId,String folderName){
        return em.createQuery(
                "select count(f.folderName) from Folder f"+
                        " where f.folderName= :folderName"+
                        " and f.team.teamIdx= :teamId",Long.class)
                .setParameter("folderName",folderName)
                .setParameter("teamId",teamId)
                .setMaxResults(1)
                .getSingleResult();
    }
    public Long findDuplicateMemberFolderName(Long memberId,String folderName){
        return em.createQuery(
                        "select count(f.folderName) from Folder f"+
                                " where f.folderName= :folderName"+
                                " and f.member.id= :memberId",Long.class)
                .setParameter("folderName",folderName)
                .setParameter("memberId",memberId)
                .setMaxResults(1)
                .getSingleResult();
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

    public int removeFolderByTeamId(Long teamId){
        return em.createQuery(
                        "delete from Folder f"+
                               " where f.team.teamIdx= :teamId")
                .setParameter("teamId",teamId)
                .executeUpdate();
    }


}
