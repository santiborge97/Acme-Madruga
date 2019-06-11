
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select ((select count(r1) from Request r1 where r1.status='APPROVED')/count(r2)*1.0), ((select count(r3) from Request r3 where r3.status='PENDING')/count(r2)*1.0), ((select count(r4) from Request r4 where r4.status='REJECTED')/count(r2)*1.0) from Request r2")
	Collection<Double> ratiosRequest();

	@Query("select (select count(r1) from Request r1 where r1.status='APPROVED')/count(r2)*1.0 from Request r2")
	Double ratioRequest();

	@Query("select r from Request r where r.member.id=?1")
	Collection<Request> findRequestsByMemberId(int memberId);

	@Query("select r from Request r where r.procession.brotherhood.id=?1 and r.status='PENDING'")
	Collection<Request> findPendingRequestsByBrotherhoodId(int id);

	@Query("select r from Request r where r.procession.brotherhood.id=?1 and r.status='APPROVED' or r.status='REJECTED'")
	Collection<Request> findFinalRequestsByBrotherhoodId(int id);

	@Query("select count(r) from Request r where r.procession.id = ?1 and r.rowNumber = ?2 and r.columnNumber = ?3")
	Integer nextFreePosition(int processionId, int row, int column);

	@Query("select r from Request r where r.procession.id = ?1")
	Collection<Request> requestPerProcessionId(int processionId);

	@Query("select r from Request r where r.procession.id = ?3 and r.rowNumber = ?2 and r.columnNumber = ?1 and r.status = 'APPROVED'")
	Collection<Request> requestIn(int col, int row, int processionId);

	@Query("select r from Request r where r.status='APPROVED'")
	Collection<Request> processionApproved();

	@Query("select r from Request r where r.status='APPROVED' and r.procession.id = ?1")
	Collection<Request> processionApproved(int processionId);

	@Query("select r from Request r where r.status='PENDING' and r.procession.id = ?1")
	Collection<Request> processionPending(int processionId);

	@Query("select r from Request r where r.status='REJECTED' and r.procession.id = ?1")
	Collection<Request> processionRejected(int processionId);

	@Query("select r from Request r where r.member.id = ?1 and (r.status='APPROVED' or r.status='PENDING') and r.procession.id = ?2")
	Collection<Request> findAcceptedOrPendingRequestsOfMemberIn(int memberId, int processionId);

}
