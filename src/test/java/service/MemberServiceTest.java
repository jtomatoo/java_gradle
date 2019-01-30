package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.shop.Member;
import repository.MemberRepository;
import service.shop.MemberService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:WEB-INF/applicationContext.xml")
@Transactional
public class MemberServiceTest {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void register() throws Exception {
		
		
		// given
		Member member = new Member();
		member.setName("kim");
		
		// when
		Long savedId = memberService.join(member);
		
		// then
		assertEquals(member, memberRepository.findOne(savedId));
	}
	
	@Test(expected=IllegalStateException.class)
	public void duplicatedException() throws Exception {
		
		// given
		Member member1 = new Member();
		member1.setName("Kim");
		
		Member member2 = new Member();
		member2.setName("kim");
		
		// when
		memberService.join(member1);
		memberService.join(member2); // occur exception
		
		// then
		fail("must be occured exception");
		
	}
	
}
