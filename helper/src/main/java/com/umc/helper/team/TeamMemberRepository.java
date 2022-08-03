package com.umc.helper.team;

import com.umc.helper.team.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepository {
    private final EntityManager em;

    public void save(TeamMember teamMember) { em.persist(teamMember);}
    public List<TeamMember> findTeamMemberByMemberId(Long memberId){
        return em.createQuery(
                "select tm from TeamMember tm"+
                        " where tm.member.id= :memberId",TeamMember.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }



    public List<TeamMember> findTeamMembersByTeamId(Long teamId){
        return em.createQuery(
                        "select tm from TeamMember tm"+
                                " where tm.team.teamIdx= :teamId",TeamMember.class)
                .setParameter("teamId",teamId)
                .getResultList();
    }

    public TeamMember findTeamMemberByTeamId(Long teamId){
        return em.find(TeamMember.class,teamId);
    }

    public void remove(TeamMember teamMember){ em.remove(teamMember);}

    public int removeTeamMemberByTeamId(Long teamId){
        return em.createQuery(
                "delete from TeamMember tm"+
                        " where tm.team.teamIdx= :teamId")
                .setParameter("teamId",teamId)
                .executeUpdate();
    }
}
