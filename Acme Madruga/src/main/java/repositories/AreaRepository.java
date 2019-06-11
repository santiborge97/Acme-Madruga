
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	//	@Query("select a from Area a where  a.id=?1")
	//	Collection<Float> findFloattsByBrotherhoodId(int areaId);

	@Query("select count(a.brotherhoods.size), avg(a.brotherhoods.size), min(a.brotherhoods.size), max(a.brotherhoods.size), stddev(a.brotherhoods.size) from Area a")
	Collection<Double> statsOfBrotherhoodPerArea();

	@Query("select (select count(a1) from Area a1 where a1.brotherhoods.size=0)/count(a2)*1.0 from Area a2")
	Double ratioArea();
}
