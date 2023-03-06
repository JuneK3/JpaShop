package com.rootlab.jpashop.repository;

import com.rootlab.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void testMember() {
        // given
        Member member = new Member();
        member.setName("memberA");
        // when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.findOne(savedId);
        // then
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }
}