package com.umc.helper.auth2;

import com.umc.helper.auth2.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final EntityManager em;
    public void save(Token token){em.persist(token);}
    public Token findByMemberId(Long memberId){
        return em.createQuery(
                "select t from Token t"+
                        " where t.member.id= :memberId",Token.class)
                .setParameter("memberId",memberId)
                .getSingleResult();
    }

    public int removeTokenByMemberId(Long memberId){
        return em.createQuery(
                "delete from Token t"+
                        " where t.member.id =:memberId")
                .setParameter("memberId",memberId)
                .executeUpdate();
    }

}
