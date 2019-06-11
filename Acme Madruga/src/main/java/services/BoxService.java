
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	// Managed repository

	@Autowired
	private BoxRepository	boxRepository;

	// Suporting services

	@Autowired
	private ActorService	actorService;

	@Autowired
	private MessageService	messageService;

	@Autowired
	private Validator		validator;


	// Simple CRUD methods

	public Box create() {

		Box result;

		result = new Box();

		return result;

	}

	public Collection<Box> findAll() {

		final Collection<Box> boxes = this.boxRepository.findAll();

		Assert.notNull(boxes);

		return boxes;

	}

	public Box findOne(final int boxID) {

		final Box box = this.boxRepository.findOne(boxID);

		Assert.notNull(box);

		return box;

	}

	public Box save(final Box box) {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Actor owner = box.getActor();
		final String name = box.getName();

		if (box.getId() == 0)
			box.setActor(actor);
		else
			Assert.isTrue(actor.getId() == owner.getId());

		Assert.isTrue(name != "in box");
		Assert.isTrue(name != "out box");
		Assert.isTrue(name != "trash box");
		Assert.isTrue(name != "spam box");
		Assert.isTrue(name != "notification box");

		Assert.isTrue(!box.getByDefault());

		Assert.notNull(box);

		final Box result = this.boxRepository.save(box);

		return result;

	}

	public Box saveNewActor(final Box box) {

		Assert.notNull(box);

		final Box result = this.boxRepository.save(box);

		return result;

	}

	public void delete(final Box box) {

		Assert.notNull(box);
		Assert.isTrue(box.getId() != 0);
		Assert.isTrue(box.getByDefault() == false);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Actor owner = box.getActor();

		Assert.isTrue(actor.getId() == owner.getId());

		final Collection<Message> messages = this.messageService.findMessagesByBoxId(box.getId());

		for (final Message m : messages) {
			final Collection<Box> boxes = m.getBoxes();
			boxes.remove(box);
			m.setBoxes(boxes);
		}

		this.boxRepository.delete(box);

	}

	public void deleteAll(final int actorId) {

		final Collection<Box> boxes = this.findAllBoxByActor(actorId);

		if (!boxes.isEmpty())
			for (final Box b : boxes) {
				final Collection<Message> messages = this.messageService.findMessagesByBoxId(b.getId());

				if (!messages.isEmpty())
					for (final Message m : messages) {
						final Collection<Box> boxesPerMessage = m.getBoxes();
						boxesPerMessage.remove(b);
						m.setBoxes(boxesPerMessage);
					}

				this.boxRepository.delete(b);
			}

	}
	// Other business methods

	public Box findTrashBoxByActorId(final int actorId) {
		Box result;
		result = this.boxRepository.findTrashBoxByActorId(actorId);
		return result;
	}

	public Box findInBoxByActorId(final int actorId) {
		Box result;
		result = this.boxRepository.findInBoxByActorId(actorId);
		return result;
	}

	public Box findOutBoxByActorId(final int actorId) {
		Box result;
		result = this.boxRepository.findOutBoxByActorId(actorId);
		return result;
	}

	public Box findSpamBoxByActorId(final int actorId) {
		Box result;
		result = this.boxRepository.findSpamBoxByActorId(actorId);
		return result;
	}

	public Box findNotificationBoxByActorId(final int actorId) {
		Box result;
		result = this.boxRepository.findNotificationBoxByActorId(actorId);
		return result;
	}

	public Collection<Box> findAllBoxByActor(final int actorId) {
		Collection<Box> boxes = new ArrayList<Box>();
		boxes = this.boxRepository.findAllBoxByActorId(actorId);
		return boxes;
	}

	public Boolean boxSecurity(final int boxId) {

		Boolean res = false;

		final Actor owner = this.boxRepository.findOne(boxId).getActor();

		final Actor login = this.actorService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public Box reconstruct(final Box box, final BindingResult binding) {

		final Box result = box;

		if (box.getId() == 0) {

			result.setByDefault(false);
			result.setActor(this.actorService.findByPrincipal());

		} else {

			final Box theOldOne = this.findOne(box.getId());

			result.setActor(theOldOne.getActor());
			result.setByDefault(theOldOne.getByDefault());

		}

		this.validator.validate(result, binding);

		return result;
	}

	public boolean existId(final int boxId) {
		Boolean res = false;

		final Box box = this.boxRepository.findOne(boxId);

		if (box != null)
			res = true;

		return res;
	}
}
