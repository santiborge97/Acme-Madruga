
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.member.id = ?1")
	Collection<Enrolment> findEnrolmentsByMemberId(int id);

	@Query("select e from Enrolment e where e.position.id = ?1")
	Collection<Enrolment> findEnrolmentPerPosition(int positionId);

	@Query("select e from Enrolment e where e.brotherhood.id = ?1 and e.position = null and e.dropOutMoment = null")
	Collection<Enrolment> findEnrolmentsByBrotherhoodIdNoPosition(int id);

	@Query("select e from Enrolment e where e.brotherhood.id = ?1 and e.position != null and e.dropOutMoment = null")
	Collection<Enrolment> findEnrolmentsByBrotherhoodId(int id);

	@Query("select e from Enrolment e where e.brotherhood.id = ?2 and e.member.id = ?1 and e.dropOutMoment = null")
	Enrolment findEnrolmentsByMemberIdAndBrotherhood(int id, int brotherhoodId);

}
