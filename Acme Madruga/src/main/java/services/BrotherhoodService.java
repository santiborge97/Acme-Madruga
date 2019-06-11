
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

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;
import forms.RegisterBrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	// Suporting services

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods

	public Brotherhood create() {

		Brotherhood result;
		result = new Brotherhood();

		final UserAccount userAccount = this.userAccountService.createBrotherhood();
		result.setUserAccount(userAccount);

		final Collection<Member> members = new HashSet<Member>();
		result.setMembers(members);

		final Collection<String> pictures = new HashSet<String>();
		result.setPictures(pictures);

		result.setScore(null);
		result.setSpammer(null);
		result.setArea(null);

		return result;

	}

	public Collection<Brotherhood> findAll() {

		Collection<Brotherhood> result;
		result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {

		Assert.notNull(brotherhoodId);
		Brotherhood result;
		result = this.brotherhoodRepository.findOne(brotherhoodId);
		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Brotherhood result;

		if (brotherhood.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.ADMIN);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == brotherhood.getId() || actor.getUserAccount().getAuthorities().contains(admin));

			this.actorService.checkEmail(brotherhood.getEmail(), false);
			this.actorService.checkPhone(brotherhood.getPhone());

			final String phone = this.actorService.checkPhone(brotherhood.getPhone());
			brotherhood.setPhone(phone);

			this.checkPictures(brotherhood.getPictures());
			result = this.brotherhoodRepository.save(brotherhood);
			if (brotherhood.getArea() != null) {
				final Collection<Procession> processions = this.processionService.findProcessionByBrotherhoodId(brotherhood.getId());
				this.editBrotherhoodInProcessions(processions, brotherhood);
			}

		} else {

			String pass = brotherhood.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = brotherhood.getUserAccount();
			userAccount.setPassword(pass);

			brotherhood.setUserAccount(userAccount);

			this.actorService.checkEmail(brotherhood.getEmail(), false);
			this.actorService.checkPhone(brotherhood.getPhone());

			final String phone = this.actorService.checkPhone(brotherhood.getPhone());
			brotherhood.setPhone(phone);

			this.checkPictures(brotherhood.getPictures());
			result = this.brotherhoodRepository.save(brotherhood);

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

	public Brotherhood saveAdmin(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Brotherhood result;

		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(admin));

		result = this.brotherhoodRepository.save(brotherhood);

		return result;

	}

	public Brotherhood editMemberList(final Brotherhood brotherhood) {

		Assert.notNull(brotherhood);
		final Brotherhood result = this.brotherhoodRepository.save(brotherhood);

		return result;
	}

	//Other business methods ----------------------------

	public Brotherhood findByPrincipal() {
		Brotherhood brotherhood;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		brotherhood = this.findByUserAccount(userAccount);
		Assert.notNull(brotherhood);

		return brotherhood;
	}

	public Brotherhood findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Brotherhood result;

		result = this.brotherhoodRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Collection<Double> statsOfMembersPerBrotherhood() {

		Collection<Double> result;

		result = this.brotherhoodRepository.statsOfMembersPerBrotherhood();

		return result;
	}

	public String avgMemberPerBrotherhood() {

		String res = "N/A";
		Double avg = 0.0;
		final Collection<Brotherhood> brotherhoods = this.brotherhoodRepository.findAll();

		if (!brotherhoods.isEmpty()) {
			final Integer total = brotherhoods.size();
			Integer sum = 0;
			for (final Brotherhood b : brotherhoods) {
				final Collection<Member> m = b.getMembers();
				if (!m.isEmpty())
					sum = sum + m.size();
			}

			if (sum > 0)
				avg = (sum * 1.0) / total;

			res = Double.toString(avg);

		}

		return res;
	}

	public String minMemberPerBrotherhood() {

		String res = "N/A";
		Integer min = 999999999;
		final Collection<Brotherhood> brotherhoods = this.brotherhoodRepository.findAll();

		if (!brotherhoods.isEmpty()) {
			for (final Brotherhood b : brotherhoods) {
				final Collection<Member> m = b.getMembers();
				if (!m.isEmpty() && (m.size() < min))
					min = m.size();
				else if (m.isEmpty())
					min = 0;

			}

			res = Integer.toString(min);

		}

		return res;
	}

	public String stddevMemberPerBrotherhood() {

		String res = "N/A";
		Double avg = 0.0;
		Integer count = 0;
		Integer sum2 = 0;
		Double op = 0.0;
		final Collection<Brotherhood> brotherhoods = this.brotherhoodRepository.findAll();

		if (!brotherhoods.isEmpty()) {
			final Integer total = brotherhoods.size();
			Integer sum = 0;
			for (final Brotherhood b : brotherhoods) {
				final Collection<Member> m = b.getMembers();
				if (!m.isEmpty()) {
					sum = sum + m.size();
					sum2 = sum2 + (m.size() * m.size());
				}

			}

			count = total;

			if (sum > 0)
				avg = (sum * 1.0) / total;

			op = Math.sqrt(((sum2 * 1.0) / count) - (avg * avg));
			res = Double.toString(op);

		}

		return res;
	}

	public String maxMemberPerBrotherhood() {

		String res = "N/A";
		Integer max = 0;
		final Collection<Brotherhood> brotherhoods = this.brotherhoodRepository.findAll();

		if (!brotherhoods.isEmpty()) {
			for (final Brotherhood b : brotherhoods) {
				final Collection<Member> m = b.getMembers();
				if (!m.isEmpty() && (m.size() > max))
					max = m.size();
			}

			res = Integer.toString(max);

		}

		return res;
	}

	public Collection<Brotherhood> theLargestBrotherhoods() {

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.theLargestBrotherhoods();

		return result;
	}

	public Collection<Brotherhood> theSmallestBrotherhoods() {

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.theSmallestBrotherhoods();

		return result;
	}

	private void editBrotherhoodInProcessions(final Collection<Procession> processions, final Brotherhood brotherhood) {

		for (final Procession p : processions) {
			p.setBrotherhood(brotherhood);
			this.processionService.saveByEditBrotherhood(p);
		}

	}

	public Brotherhood reconstruct(final RegisterBrotherhoodForm form, final BindingResult binding) {

		this.validator.validate(form, binding);

		final Brotherhood brotherhood = this.create();

		brotherhood.setTitle(form.getTitle());
		brotherhood.setPictures(form.getPictures());
		brotherhood.setEstablishment(form.getEstablishment());
		brotherhood.setName(form.getName());
		brotherhood.setMiddleName(form.getMiddleName());
		brotherhood.setSurname(form.getSurname());
		brotherhood.setPhoto(form.getPhoto());
		brotherhood.setEmail(form.getEmail());
		brotherhood.setPhone(form.getPhone());
		brotherhood.setAddress(form.getAddress());
		brotherhood.getUserAccount().setUsername(form.getUsername());
		brotherhood.getUserAccount().setPassword(form.getPassword());

		return brotherhood;

	}

	public Brotherhood reconstruct(final Brotherhood brotherhood, final BindingResult binding) {

		final Brotherhood result;

		final Brotherhood brotherhoodBBDD = this.findOne(brotherhood.getId());

		brotherhood.setUserAccount(brotherhoodBBDD.getUserAccount());
		brotherhood.setArea(brotherhoodBBDD.getArea());
		brotherhood.setScore(brotherhoodBBDD.getScore());
		brotherhood.setSpammer(brotherhoodBBDD.getSpammer());

		this.validator.validate(brotherhood, binding);

		result = brotherhood;

		return result;

	}

	public void checkPictures(final Collection<String> attachments) {

		for (final String url : attachments) {
			final boolean checkUrl = url.matches("^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:])+[a-zA-Z0-9/]+$");
			Assert.isTrue(checkUrl);

		}
	}
}
