package com.umc.helper;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberType;
import com.umc.helper.team.Team;
import com.umc.helper.team.TeamMember;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@Component
@RequiredArgsConstructor
public class InitDB {


    private final InitService initService;
    private static final Logger log= LoggerFactory.getLogger(InitDB.class);

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1(){

            // set members and teams
            Member member1=createMember("111@gmail.com","aaa","aaapw",MemberType.일반회원);
            em.persist(member1);

            Member member2=createMember("222@gmail.com","bbb","bbbpw",MemberType.일반회원);
            em.persist(member2);

            Member member3=createMember("333@gmail.com","ccc","cccpw",MemberType.일반회원);
            em.persist(member3);

            Member member4=createMember("444@gmail.com","ddd","dddpw",MemberType.일반회원);
            em.persist(member3);

            Team team1=createTeam("teamA"); // member - aaa,bbb,ccc
            em.persist(team1);

            Team team2=createTeam("teamB"); // member - ccc,ddd
            em.persist(team2);

            TeamMember teamMember1=createTeamMember(team1,member1);
            em.persist(teamMember1);

            TeamMember teamMember2=createTeamMember(team1,member2);
            em.persist(teamMember2);

            TeamMember teamMember3=createTeamMember(team1,member3);
            em.persist(teamMember3);

            TeamMember teamMember4=createTeamMember(team2,member4);
            em.persist(teamMember4);

            // set private folders and public(team) folders
            Folder folder1=createPrivateFolder("aaa private folder1",member1); // aaa private folder1
            em.persist(folder1);

            Folder folder2=createPublicFolder("teamA public folder",team1); // teamA(aaa,bbb,ccc) public folder
            em.persist(folder2);

            Folder folder3=createPrivateFolder("aaa private folder2",member1); // aaa private folder2;
            em.persist(folder3);


        }

        private Folder createPrivateFolder(String name,Member member){
            Folder folder=new Folder();
            folder.setFolderName(name);
            folder.setMember(member);

            return folder;
        }

        private Folder createPublicFolder(String name,Team team){
            Folder folder=new Folder();
            folder.setFolderName(name);
            folder.setTeam(team);

            return folder;
        }

        private Member createMember(String email, String username, String password, MemberType type){
            Member member=new Member();
            member.setEmail(email);
            member.setUsername(username);
            member.setPassword(password);
            member.setType(type);
            log.info("member email: {}",member.getEmail());
            return member;
        }

        private Team createTeam(String name){
            Team team=new Team();
            team.setName(name);

            return team;
        }

        private TeamMember createTeamMember(Team team,Member member){
            TeamMember teamMember=new TeamMember();
            teamMember.setTeam(team);
            teamMember.setMember(member);

            return teamMember;
        }

    }
}
