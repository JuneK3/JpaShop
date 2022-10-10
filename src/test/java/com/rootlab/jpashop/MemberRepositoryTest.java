package com.rootlab.jpashop;

import com.rootlab.jpashop.domain.Member;
import com.rootlab.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	@Transactional
//	@Rollback(false)
	public void testMember() {
		Member member = new Member();
		member.setName("memberA");
		Long saveId = memberRepository.save(member);
		Member findMember = memberRepository.find(saveId);
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getName()).isEqualTo(member.getName());
		//JPA 엔티티 동일성 보장: findMember와 member는 같은 객체
		// 같은 영속성 컨텍스트 안에서는 id값이 같으면 같은 엔티티로 취급
		System.out.println("member = " + member);
		System.out.println("findMember = " + findMember);
		assertThat(findMember).isEqualTo(member);
	}

}