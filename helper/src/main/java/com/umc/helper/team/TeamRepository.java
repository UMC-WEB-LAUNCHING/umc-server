package com.umc.helper.team;

import com.umc.helper.team.model.GetTeamsResponse;
import com.umc.helper.team.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {
    private final EntityManager em;

    public void save(Team team){
        em.persist(team);
    }

    public Team findById(Long teamId) { return em.find(Team.class,teamId); }

    public void remove(Team team) { em.remove(team);}

    public int removeTeamByTeamId(Long teamId){
        return em.createQuery(
                "delete from Team t"+
                        " where t.teamIdx= :teamId")
                .setParameter("teamId",teamId)
                .executeUpdate();
    }
}
