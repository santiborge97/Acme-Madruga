
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select avg(f.processions.size), min(f.processions.size), max(f.processions.size), stddev(f.processions.size) from Finder f")
	Collection<Double> statsOfFinderResults();

	@Query("select (select count(f1) from Finder f1 where f1.processions.size=0)/count(f2)*1.0 from Finder f2")
	Double ratioEmptyFinders();

	@Query("select f from Finder f join f.processions t where t.id = ?1")
	Collection<Finder> findFindersByProcessionId(int fixUpTaskId);

	@Query("select f from Finder f where f.member.id = ?1")
	Finder findFinderByMember(int actorId);
}
