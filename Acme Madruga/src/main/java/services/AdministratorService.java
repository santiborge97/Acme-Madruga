
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Message;
import forms.RegisterAdministratorForm;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository ------------------------
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Suporting services ------------------------

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods -----------------------

	public Administrator create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		Assert.isTrue((actor.getUserAccount().getAuthorities().contains(authority)));

		Administrator result;
		result = new Administrator();

		final UserAccount userAccount = this.userAccountService.createAdmin();
		result.setUserAccount(userAccount);

		return result;

	}
	public Collection<Administrator> findAll() {

		Collection<Administrator> result;
		result = this.administratorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Administrator findOne(final int administratorId) {

		Assert.notNull(administratorId);
		Administrator result;
		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);
		return result;
	}

	public Administrator save(final Administrator administrator) {

		Assert.notNull(administrator);
		Administrator result;

		if (administrator.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.ADMIN);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == administrator.getId());

			this.actorService.checkEmail(administrator.getEmail(), true);
			this.actorService.checkPhone(administrator.getPhone());

			result = this.administratorRepository.save(administrator);

		} else {

			this.actorService.checkEmail(administrator.getEmail(), true);
			this.actorService.checkPhone(administrator.getPhone());

			String pass = administrator.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = administrator.getUserAccount();
			userAccount.setPassword(pass);

			administrator.setUserAccount(userAccount);

			result = this.administratorRepository.save(administrator);

			Box inBox, outBox, trashBox, spamBox, notificationBox;
			inBox = this.boxService.create();
			outBox = this.boxService.create();
			trashBox = this.boxService.create();
			spamBox = this.boxService.create();
			notificationBox = this.boxService.create();

			inBox.setName("in box");
			outBox.setName("out box");
			trashBox.setName("trash box");
			spamBox.setName("spam box");
			notificationBox.setName("notification box");

			inBox.setByDefault(true);
			outBox.setByDefault(true);
			trashBox.setByDefault(true);
			spamBox.setByDefault(true);
			notificationBox.setByDefault(true);

			inBox.setActor(result);
			outBox.setActor(result);
			trashBox.setActor(result);
			spamBox.setActor(result);
			notificationBox.setActor(result);

			final Collection<Box> boxes = new ArrayList<>();
			boxes.add(spamBox);
			boxes.add(trashBox);
			boxes.add(inBox);
			boxes.add(outBox);
			boxes.add(notificationBox);

			inBox = this.boxService.saveNewActor(inBox);
			outBox = this.boxService.saveNewActor(outBox);
			trashBox = this.boxService.saveNewActor(trashBox);
			spamBox = this.boxService.saveNewActor(spamBox);
			notificationBox = this.boxService.saveNewActor(notificationBox);

		}
		return result;
	}

	//	public Administrator findAdministrator() {
	//		Administrator admin;
	//		admin = this.administratorRepository.findAll().get(0);
	//		Assert.notNull(admin);
	//		return admin;
	//	}

	// Other business methods -----------------------

	public Administrator findByPrincipal() {
		Administrator admin;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		admin = this.findByUserAccount(userAccount);
		Assert.notNull(admin);

		return admin;
	}

	public Administrator findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Administrator result;

		result = this.administratorRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Administrator reconstruct(final RegisterAdministratorForm form, final BindingResult binding) {

		final Administrator admin = this.create();

		this.validator.validate(form, binding);

		admin.setId(form.getId());
		admin.setVersion(form.getVersion());
		admin.setName(form.getName());
		admin.setMiddleName(form.getMiddleName());
		admin.setSurname(form.getSurname());
		admin.setPhoto(form.getPhoto());
		admin.setEmail(form.getEmail());
		admin.setPhone(form.getPhone());
		admin.setAddress(form.getAddress());
		admin.setScore(null);
		admin.setSpammer(null);
		admin.getUserAccount().setUsername(form.getUsername());
		admin.getUserAccount().setPassword(form.getPassword());

		return admin;

	}

	private Double score(final Collection<String> messages) {
		Double res = null;
		if (messages.size() != 0) {
			Integer p = 0;
			Integer n = 0;
			Integer N = 0;
			final Collection<String> positives = this.configurationService.findConfiguration().getPositiveWords();
			final Collection<String> negatives = this.configurationService.findConfiguration().getNegativeWords();
			for (final String message : messages) {

				for (final String positive : positives)
					if (message.toLowerCase().contains(" " + positive.toLowerCase() + " ") ||
						message.toLowerCase().contains(positive.toLowerCase() + " " ) ||
						message.toLowerCase().contains(" " + positive.toLowerCase()) ||
						message.toLowerCase().contains(positive.toLowerCase() + ".") ||
						message.toLowerCase().contains(" " + positive.toLowerCase() + ".")) {
						p++;
						N++;
					}

				for (final String negative : negatives)
					if (message.toLowerCase().contains(" " + negative.toLowerCase() + " ") ||
						message.toLowerCase().contains(negative.toLowerCase() + " ") ||
						message.toLowerCase().contains(" " + negative.toLowerCase()) ||
						message.toLowerCase().contains(negative.toLowerCase() + ".") ||
						message.toLowerCase().contains(" " + negative.toLowerCase() + ".")) {
						n++;
						N++;
					}
			}

			if (n == 0 && p == 0)
				res = 0.0;
			else if (N != 0)
				res = (p - n) * 1.0 / N;
		}

		return res;
	}
	public void calculateScore() {
		Double res;
		final Collection<Actor> actors = this.actorService.findAll();

		for (final Actor actor : actors) {

			final Collection<String> bodys = new HashSet<>();
			final Collection<Message> messages = this.messageService.messagePerActor(actor.getId());

			if (messages.size() != 0)
				for (final Message message : messages) {
					final String body = message.getBody();

					bodys.add(body);
				}

			res = this.score(bodys);

			actor.setScore(res);
			this.actorService.save(actor);
		}

	}

	public void spammer() {
		final Collection<Actor> actors = this.actorService.findAll();

		for (final Actor actor : actors) {

			Collection<Message> messages = new HashSet<>();

			Boolean body = false;
			Boolean subject = false;
			Boolean tags = false;

			Integer intMesage = 0;
			Integer intSpam = 0;

			messages = this.messageService.messagePerActor(actor.getId());

			if (!messages.isEmpty())
				for (final Message message : messages) {

					if (message.getBody() != null)
						body = this.configurationService.spamContent(message.getBody());
					if (message.getSubject() != null)
						subject = this.configurationService.spamContent(message.getSubject());
					if (message.getTags() != null)
						tags = this.configurationService.spamContent(message.getTags());

					intMesage++;
					if (body || subject || tags)
						intSpam++;

				}

			if (!messages.isEmpty()) {
				if (intSpam >= 0.1 * intMesage)
					actor.setSpammer(true);
				else
					actor.setSpammer(false);

				this.actorService.save(actor);
			}

		}
	}

	public Administrator reconstruct(final Administrator admin, final BindingResult binding) {

		final Administrator result;

		final Administrator adminBBDD = this.findOne(admin.getId());

		admin.setUserAccount(adminBBDD.getUserAccount());
		admin.setScore(adminBBDD.getScore());
		admin.setSpammer(adminBBDD.getSpammer());

		this.validator.validate(admin, binding);

		result = admin;

		return result;

	}
}
