package com.umc.helper.repository;

//import com.umc.helper.domain.Site;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SiteRepository {

    /*private EntityManager em;

    public void upload(Site site){
        if(site.getId()==null){
            em.persist(site);
        }
        else{
            em.merge(site);
        }
    }

    public Site findOne(Long id){
       return em.find(Site.class,id);
    }

    public List<Site> findAll(SiteSearch siteSearch){
        return em.createQuery("select s from Site s join s.user u"+
                " where s.category=:category"+
                " and u.id=:id",
                Site.class)
                .setParameter("category",siteSearch.getCategory())
                .setParameter("id",siteSearch.getUser_id())
                .getResultList();
    }*/



}
