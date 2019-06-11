
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends JpaRepository<Procession, Integer> {

	@Query("select p from Procession p join p.floats f where f.id = ?1")
	Collection<Procession> findProcessionsByFloatId(int floatId);

	@Query("select count(p) from Procession p where p.ticker = ?1")
	int countProcessionsWithTicker(String ticker);

	@Query("select p from Procession p where p.brotherhood.id = ?1")
	Collection<Procession> findProcessionByBrotherhoodId(int brotherhoodId);

	@Query("select p from Procession p where p.finalMode = true")
	Collection<Procession> findProcessionCanBeSeen();

	@Query("select p from Procession p where p.finalMode = false and p.brotherhood.id = ?1")
	Collection<Procession> findProcessionCannotBeSeenOfBrotherhoodId(int brotherhoodId);

	@Query("select p from Procession p where p.finalMode = true and p.brotherhood.id = ?1")
	Collection<Procession> findProcessionCanBeSeenOfBrotherhoodId(int brotherhoodId);

	@Query("select p from Procession p join p.brotherhood.members m where m.id = ?1")
	Collection<Procession> findMemberProcessions(int memberId);
}
