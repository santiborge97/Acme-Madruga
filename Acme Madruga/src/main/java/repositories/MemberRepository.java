
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.userAccount.id = ?1")
	Member findByUserAccountId(int userAccountId);

	@Query("select distinct m.name from Member m join m.requests r where r.status = 'APPROVED' group by m having count(r)>= (select max(m.requests.size)from Member m join m.requests r where r.status = 'APPROVED')*1.1")
	Collection<Member> membersTenPerCent();

}
