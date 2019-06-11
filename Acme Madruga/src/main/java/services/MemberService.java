
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

import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Finder;
import domain.Member;
import domain.Request;
import forms.RegisterMemberForm;

@Service
@Transactional
public class MemberService {

	// Managed repository

	@Autowired
	private MemberRepository	memberRepository;

	// Supporting services
	@Autowired
	private BoxService			boxService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private Validator			validator;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Simple CRUD methods 

	public Member create() {
		final Member member = new Member();

		final UserAccount userAccount = this.userAccountService.createMember();
		member.setUserAccount(userAccount);

		member.setScore(null);
		member.setSpammer(null);

		return member;

	}

	public Member findOne(final int memberId) {

		Member member;
		member = this.memberRepository.findOne(memberId);
		Assert.notNull(member);
		return member;

	}

	public Collection<Member> findAll() {

		Collection<Member> result;
		result = this.memberRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Member save(final Member member) {

		Member result = null;
		Assert.notNull(member);

		if (member.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.ADMIN);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == member.getId());

			this.actorService.checkEmail(member.getEmail(), false);
			this.actorService.checkPhone(member.getPhone());

			final String phone = this.actorService.checkPhone(member.getPhone());
			member.setPhone(phone);

			result = this.memberRepository.save(member);
		} else {
			String pass = member.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = member.getUserAccount();
			userAccount.setPassword(pass);

			member.setUserAccount(userAccount);

			this.actorService.checkEmail(member.getEmail(), false);
			this.actorService.checkPhone(member.getPhone());

			final String phone = this.actorService.checkPhone(member.getPhone());
			member.setPhone(phone);

			result = this.memberRepository.save(member);

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

			final Finder finder = this.finderService.create();
			finder.setMember(result);
			this.finderService.save(finder);
		}

		return result;

	}

	public void deleteRelantionshipBrotherhoodMember(final int actorId) {

		final Member member = this.memberRepository.findOne(actorId);

		final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findAll();

		if (!brotherhoods.isEmpty())
			for (final Brotherhood b : brotherhoods)
				if (b.getMembers().contains(member)) {
					b.getMembers().remove(member);
					this.brotherhoodService.saveAdmin(b);
				}

	}

	// Other business methods 

	public Member findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final Member member = this.memberRepository.findByUserAccountId(userAccount.getId());

		return member;
	}

	public Member findByPrincipal() {
		Member member;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		member = this.findByUserAccount(userAccount);
		Assert.notNull(member);

		return member;
	}

	public Integer maxApprovedRequest() {
		Integer result = 0;

		final Collection<Member> members = this.memberRepository.findAll();

		if (!members.isEmpty()) {

			Integer max = 0;

			for (final Member m : members) {

				final Collection<Request> requestsOfAMemeber = m.getRequests();
				Integer requestsAccepted = 0;
				for (final Request r : requestsOfAMemeber)
					if (r.getStatus().equals("APPROVED"))
						requestsAccepted++;

				if (requestsAccepted > max)
					max = requestsAccepted;
			}

			result = max;
		}
		return result;
	}

	public Collection<String> membersTenPerCent() {

		final Collection<String> result = new HashSet<>();

		final Collection<Member> members = this.memberRepository.findAll();

		if (!members.isEmpty()) {

			final Integer max = this.maxApprovedRequest();

			for (final Member m : members) {

				final Collection<Request> requestsOfAMemeber = m.getRequests();
				Integer requestsAccepted = 0;
				for (final Request r : requestsOfAMemeber)
					if (r.getStatus().equals("APPROVED"))
						requestsAccepted++;

				if (requestsAccepted * 1.0 >= max)
					result.add(m.getUserAccount().getUsername());
			}

		}
		return result;
	}

	public Member reconstruct(final RegisterMemberForm form, final BindingResult binding) {

		this.validator.validate(form, binding);

		final Member member = this.create();

		member.setName(form.getName());
		member.setMiddleName(form.getMiddleName());
		member.setSurname(form.getSurname());
		member.setPhoto(form.getPhoto());
		member.setEmail(form.getEmail());
		member.setPhone(form.getPhone());
		member.setAddress(form.getAddress());
		member.getUserAccount().setUsername(form.getUsername());
		member.getUserAccount().setPassword(form.getPassword());

		return member;

	}

	public Member reconstruct(final Member member, final BindingResult binding) {

		final Member result;

		final Member memberBBDD = this.findOne(member.getId());

		member.setUserAccount(memberBBDD.getUserAccount());
		member.setScore(memberBBDD.getScore());
		member.setSpammer(memberBBDD.getSpammer());

		this.validator.validate(member, binding);

		result = member;

		return result;

	}

	public boolean existId(final int memberId) {
		Boolean res = false;

		final Member member = this.memberRepository.findOne(memberId);

		if (member != null)
			res = true;

		return res;
	}

}
