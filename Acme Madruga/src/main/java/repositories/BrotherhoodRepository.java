
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where b.userAccount.id = ?1")
	Brotherhood findByUserAccountId(int userAccountId);

	@Query("select avg(b.members.size), min(b.members.size), max(b.members.size), stddev(b.members.size) from Brotherhood b")
	Collection<Double> statsOfMembersPerBrotherhood();

	@Query("select b.title from Brotherhood b where b.members.size = (select max(b.members.size) from Brotherhood b)")
	Collection<Brotherhood> theLargestBrotherhoods();

	@Query("select b.title from Brotherhood b where b.members.size = (select min(b.members.size) from Brotherhood b)")
	Collection<Brotherhood> theSmallestBrotherhoods();

}
