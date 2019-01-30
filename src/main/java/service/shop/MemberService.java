package service.shop;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.shop.Member;
import repository.MemberRepository;

@Service
@Transactional
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	public Long join(Member member) {
		validateDuplicatedMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicatedMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}
