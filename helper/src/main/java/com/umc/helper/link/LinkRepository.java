package com.umc.helper.link;

import com.umc.helper.link.model.Link;
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
                            " where l.status= :status",Link.class)
                .setParameter("status",Boolean.TRUE)
                .getResultList();
    }

}
