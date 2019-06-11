
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.Authority;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Message;
import domain.Procession;
import domain.Request;
import forms.MessageForm;

@Service
@Transactional
public class MessageService {

	//Managed Repository

	@Autowired
	private MessageRepository		messageRepository;

	//Supporting services

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Simple CRUD methods

	public MessageForm create(final int recipientId) {

		Assert.notNull(recipientId);
		Assert.isTrue(recipientId != 0);

		final MessageForm result = new MessageForm();
		Actor recipient;
		Actor sender;

		recipient = this.actorService.findOne(recipientId);
		Assert.notNull(recipient);

		result.setRecipientId(recipient.getId());

		sender = this.actorService.findByPrincipal();
		result.setSenderId(sender.getId());

		return result;

	}

	public MessageForm create2() {

		final MessageForm result = new MessageForm();

		final Actor actor = this.actorService.findByPrincipal();

		result.setSenderId(actor.getId());
		result.setRecipientId(actor.getId());

		return result;

	}

	public Message create3() {

		final Message result = new Message();

		Date momentSent;

		final Actor actor = this.actorService.findByPrincipal();

		momentSent = new Date(System.currentTimeMillis() - 1000);

		final Boolean spam = false;

		result.setSender(actor);
		result.setMoment(momentSent);
		result.setSpam(spam);
		result.setBoxes(new ArrayList<Box>());
		result.setPriority("NORMAL");

		return result;

	}

	public Collection<Message> findAll() {

		final Collection<Message> messages = this.messageRepository.findAll();

		Assert.notNull(messages);

		return messages;
	}

	public Message findOne(final int messageId) {

		final Message message = this.messageRepository.findOne(messageId);

		Assert.notNull(message);

		return message;
	}

	public Message save(final Message message) {

		if (message.getId() != 0)
			Assert.isTrue((message.getSender() == this.actorService.findByPrincipal()) || message.getRecipient() == this.actorService.findByPrincipal());

		Assert.notNull(message);

		Message result;

		final Boolean spam1 = this.configurationService.spamContent(message.getSubject());

		final Boolean spam2 = this.configurationService.spamContent(message.getBody());

		final Boolean spam3 = this.configurationService.spamContent(message.getTags());

		final Collection<Box> boxes = message.getBoxes();
		final Box inBoxReceiver = this.boxService.findInBoxByActorId(message.getRecipient().getId());

		if (spam1 || spam2 || spam3) {

			final Box spamBoxReceiver = this.boxService.findSpamBoxByActorId(message.getRecipient().getId());

			boxes.remove(inBoxReceiver);
			boxes.add(spamBoxReceiver);

			message.setBoxes(boxes);

			this.configurationService.spammerDetector(message.getSender().getId());

		} else {

		}

		result = this.messageRepository.save(message);

		return result;
	}

	public Message save2(final Message message) {

		Assert.notNull(message);

		Message result;

		result = this.messageRepository.save(message);

		return result;

	}

	public void delete(final Message message) {

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(message.getRecipient().equals(actor) || message.getSender().equals(actor));

		final Box tb = this.boxService.findTrashBoxByActorId(actor.getId());
		final Collection<Box> boxes = message.getBoxes();
		final Collection<Box> allBoxes = this.boxService.findAllBoxByActor(actor.getId());
		if (boxes.contains(tb)) {
			boxes.removeAll(allBoxes);
			message.setBoxes(boxes);
			this.messageRepository.save(message);
			if (boxes.isEmpty())
				this.messageRepository.delete(message);
		} else {
			boxes.removeAll(allBoxes);
			boxes.add(tb);
			message.setBoxes(boxes);
			this.messageRepository.save(message);
			if (boxes.isEmpty())
				this.messageRepository.delete(message);
		}
	}

	public void deleteAll(final int actorId) {

		final Collection<Message> messages = this.messageRepository.AllmessagePerActor(actorId);

		if (!messages.isEmpty())
			for (final Message m : messages)
				this.messageRepository.delete(m);

	}

	public void broadcastSystem(final Message message) {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Collection<Actor> actores = this.actorService.findAll();
		actores.remove(actor);
		final Collection<Box> boxes = message.getBoxes();

		for (final Actor a : actores) {

			final Box nb = this.boxService.findNotificationBoxByActorId(a.getId());
			boxes.add(nb);

		}
		message.setBoxes(boxes);

	}

	public Collection<Message> findMessagesByBoxId(final int boxId) {

		final Collection<Message> result = this.messageRepository.findMessagesByBoxId(boxId);

		return result;

	}

	public Boolean securityMessage(final int boxId) {

		Boolean res = false;

		final Actor ownerBox = this.boxService.findOne(boxId).getActor();

		final Actor login = this.actorService.findByPrincipal();

		if (login.equals(ownerBox))
			res = true;

		return res;
	}

	public Collection<Message> messagePerActor(final int actorId) {

		final Collection<Message> messages = this.messageRepository.messagePerActor(actorId);

		return messages;
	}

	public void NotificationNewProcession(final Procession procession) {

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final Collection<Member> members = brotherhood.getMembers();

		for (final Member member : members) {

			final Message message = this.create3();

			message.setRecipient(member);
			message.setSubject("New procession/Nueva procesión");
			message.setBody(brotherhood.getName() + " has created a new procession rigth now: " + procession.getTitle() + "\n" + brotherhood.getName() + " ha creado una nueva procesión ahora mismo: " + procession.getTitle());

			final Collection<Box> boxes = message.getBoxes();
			boxes.add(this.boxService.findNotificationBoxByActorId(member.getId()));
			message.setBoxes(boxes);

			this.save2(message);
		}
	}

	public void NotificationNewEnrolment(final Enrolment enrolment) {

		final Message message = this.create3();

		message.setRecipient(enrolment.getMember());
		message.setSubject("Enrolment/Inscripción");
		message.setBody(enrolment.getBrotherhood().getName() + " has accepted/updated your enrolment with the position: " + enrolment.getPosition().getEnglishName() + "/" + enrolment.getBrotherhood().getName()
			+ " ha aceptado/actualizado tu inscripción con la posición: " + enrolment.getPosition().getSpanishName());

		final Collection<Box> boxes = message.getBoxes();
		boxes.add(this.boxService.findNotificationBoxByActorId(enrolment.getMember().getId()));
		message.setBoxes(boxes);

		this.save2(message);

	}

	public void NotificationDropOutMember(final Enrolment enrolment) {

		final Message message = this.create3();

		message.setRecipient(enrolment.getBrotherhood());
		message.setSubject("Drop out/Abandono");
		message.setBody(enrolment.getMember().getName() + " " + enrolment.getMember().getSurname() + " has drop out your brotherhood" + "\n" + enrolment.getMember().getName() + enrolment.getMember().getSurname() + " ha abandonado su hermandad");

		final Collection<Box> boxes = message.getBoxes();
		boxes.add(this.boxService.findNotificationBoxByActorId(enrolment.getBrotherhood().getId()));
		message.setBoxes(boxes);

		this.save2(message);

	}

	public void NotificationDropOutBrotherhood(final Enrolment enrolment) {

		final Message message = this.create3();

		message.setRecipient(enrolment.getMember());
		message.setSubject("Drop out/Abandono");
		message.setBody(enrolment.getBrotherhood().getName() + " has drop out you from the brotherhood" + "\n" + enrolment.getBrotherhood().getName() + " te ha echado de la hermandad");

		final Collection<Box> boxes = message.getBoxes();
		boxes.add(this.boxService.findNotificationBoxByActorId(enrolment.getMember().getId()));
		message.setBoxes(boxes);

		this.save2(message);

	}

	public void NotificationRequestStatus(final Request request) {

		final Message message = this.create3();

		message.setRecipient(request.getMember());
		message.setSubject("Request/Solicitud");
		message.setBody("Your request for " + request.getProcession().getTitle() + " has been changed" + "\n" + "Su solicitud para " + request.getProcession().getTitle() + " ha sido modificada");

		final Collection<Box> boxes = message.getBoxes();
		boxes.add(this.boxService.findNotificationBoxByActorId(request.getMember().getId()));
		message.setBoxes(boxes);

		this.save2(message);

	}

	public boolean existId(final int messageId) {
		Boolean res = false;

		final Message message = this.messageRepository.findOne(messageId);

		if (message != null)
			res = true;

		return res;
	}

	public Message reconstruct(final MessageForm message, final BindingResult binding) {

		final Message result = new Message();

		result.setId(message.getId());
		result.setVersion(message.getVersion());
		result.setBody(message.getBody());
		result.setPriority(message.getPriority());
		result.setRecipient(this.actorService.findOne(message.getRecipientId()));
		result.setSender(this.actorService.findOne(message.getSenderId()));
		result.setSpam(false);
		result.setSubject(message.getSubject());
		result.setTags(message.getTags());

		if (message.getId() == 0) {
			final Collection<Box> boxes = new ArrayList<Box>();
			boxes.add(this.boxService.findOutBoxByActorId(message.getSenderId()));
			boxes.add(this.boxService.findInBoxByActorId(message.getRecipientId()));
			result.setBoxes(boxes);

			Date momentSent;
			momentSent = new Date(System.currentTimeMillis() - 1000);
			result.setMoment(momentSent);

		} else {
			final Collection<Box> boxes = this.messageRepository.findOne(message.getId()).getBoxes();
			result.setBoxes(boxes);
			result.setMoment(this.messageRepository.findOne(message.getId()).getMoment());
		}

		final Date momentSent = new Date(System.currentTimeMillis() - 1000);
		result.setMoment(momentSent);

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Message> AllmessagePerActor(final int actorId) {

		final Collection<Message> result = this.messageRepository.AllmessagePerActor(actorId);

		return result;
	}
}
