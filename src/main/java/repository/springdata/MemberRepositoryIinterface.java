package repository.springdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.shop.Member;

public interface MemberRepositoryIinterface extends JpaRepository<Member, Long> {

	public List<Member> findByName(String name);
}
