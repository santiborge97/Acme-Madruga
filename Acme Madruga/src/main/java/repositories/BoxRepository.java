
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Box b where b.name='trash box' and b.actor.id=?1 and b.byDefault = true")
	Box findTrashBoxByActorId(int actorId);

	@Query("select b from Box b where b.name='in box' and b.actor.id=?1 and b.byDefault = true")
	Box findInBoxByActorId(int actorId);

	@Query("select b from Box b where b.name='out box' and b.actor.id=?1 and b.byDefault = true")
	Box findOutBoxByActorId(int actorId);

	@Query("select b from Box b where b.name='spam box' and b.actor.id=?1 and b.byDefault = true")
	Box findSpamBoxByActorId(int actorId);

	@Query("select b from Box b where b.name='notification box' and b.actor.id=?1 and b.byDefault = true")
	Box findNotificationBoxByActorId(int actorId);

	@Query("select b from Box b where b.actor.id=?1")
	Collection<Box> findAllBoxByActorId(int actorId);

}
