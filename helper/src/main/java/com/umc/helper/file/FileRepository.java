package com.umc.helper.file;

import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.memo.model.Memo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;
    public void save(File file){em.persist(file);}

    public File findById(Long fileId){ return em.find(File.class,fileId);}

    public List<File> findAllByFolderId(Long folderId){
        return em.createQuery(
                "select f from File f"+
                                " join fetch f.folder ff"+
                                " where f.status= :status"+
                                " and ff.id= :folderId",File.class)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderId",folderId)
                .getResultList();
    }

    public List<GetFilesResponse> findAllInfoByFolderId(Long folderId){
        return em.createQuery(
                        "select new com.umc.helper.file.model.GetFilesResponse(f.id,f.filePath,f.originalFileName,f.member.username,ff.folderName,ff.id,bm.id,f.uploadDate,f.lastModifiedDate,f.volume,f.member.profileImage)"+
                                " from File f"+
                                " join f.folder ff"+
                                " left join Bookmark bm"+
                                " on f.id=bm.file.id"+
                                " where ff.id= :folderId"+
                                " and f.status= :status",GetFilesResponse.class)
                .setParameter("folderId",folderId)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
    public List<GetFilesResponse> findAllInfoByFolderIds(List<Long> folderIds){
        return em.createQuery(
                        "select new com.umc.helper.file.model.GetFilesResponse(f.id,f.filePath,f.originalFileName,f.member.username,ff.folderName,ff.id,bm.id,f.uploadDate,f.lastModifiedDate,f.volume,f.member.profileImage)"+
                                " from File f"+
                                " join f.folder ff"+
                                " left join Bookmark bm"+
                                " on f.id=bm.file.id"+
                                " where ff.id in (:folderIds)"+
                                " and f.status= :status",GetFilesResponse.class)
                .setParameter("folderIds",folderIds)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }
//    public List<File> findAllInfoByFolderId2(Long folderId){
//        return em.createQuery(
//                "select f"
//        )
//    }
    public Long findDuplicateFileName(Long folderId,String fileName){
        return em.createQuery(
                        "select count(f.fileName) from File f"+
                                " where f.fileName= :fileName"+
                                " and f.folder.id= :folderId",Long.class)
                .setParameter("fileName",fileName)
                .setParameter("folderId",folderId)
                .setMaxResults(1)
                .getSingleResult();
    }

    public List<File> findTrashByMemberId(Long memberId){
        return em.createQuery(
                        "select f from File f"+
                                " where f.status= :status"+
                                " and f.member.id= :memberId",File.class)
                .setParameter("status",Boolean.FALSE)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public void remove(File file){
        em.remove(file);
    }

    public int removeTrashByMemberId(Long memberId){
        return em.createQuery(
                        "delete from File f"+
                                " where f.member.id= :memberId"+
                                " and f.status= :status")
                .setParameter("memberId",memberId)
                .setParameter("status",Boolean.FALSE)
                .executeUpdate();
    }
    public int removeEveryByFolderId(Long folderId){
        return em.createQuery(
                        "delete from File f"+
                                " where f.folder.id= :folderId")
                .setParameter("folderId",folderId)
                .executeUpdate();
    }

    public List<File> findByWord(String word,List<Long> folderIds){
        word="%"+word+"%";
        return em.createQuery(
                "select f from File f"+
                        " where f.originalFileName like :word"+
                        " and f.status=:status"+
                        " and f.folder.id in (:folderIds)",File.class)
                .setParameter("word",word)
                .setParameter("status",Boolean.TRUE)
                .setParameter("folderIds",folderIds)
                .getResultList();
    }


}
