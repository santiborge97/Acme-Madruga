
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findByUserAccountId(int userAccountId);

	@Query("select a from Actor a where a.spammer = true")
	Collection<Actor> actorSpammer();

	@Query("select a from Actor a where a.spammer = false")
	Collection<Actor> actorNoSpammer();

	@Query("select a from Actor a where a.score <= -0.75")
	Collection<Actor> actorScore1();

	@Query("select a from Actor a where a.score > -0.75 and a.score <= -0.5")
	Collection<Actor> actorScore2();

	@Query("select a from Actor a where a.score > -0.5 and a.score <= -0.25")
	Collection<Actor> actorScore3();

	@Query("select a from Actor a where a.score > -0.25 and a.score <= 0")
	Collection<Actor> actorScore4();

	@Query("select a from Actor a where a.score > 0 and a.score <= 0.25")
	Collection<Actor> actorScore5();

	@Query("select a from Actor a where a.score > 0.25 and a.score <= 0.5")
	Collection<Actor> actorScore6();

	@Query("select a from Actor a where a.score > 0.5 and a.score <=0.75")
	Collection<Actor> actorScore7();

	@Query("select a from Actor a where a.score > 0.75")
	Collection<Actor> actorScore8();
}
