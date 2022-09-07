package com.umc.helper.alarm;

import com.umc.helper.alarm.model.Alarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmRepository {
    private final EntityManager em;

    public void save(Alarm alarm){ em.persist(alarm);}
    public List<Alarm> findByMemberId(Long memberId){
        return em.createQuery("select a from Alarm a"+
                " where a.member.id= :memberId",Alarm.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
