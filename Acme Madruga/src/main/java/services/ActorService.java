
package services;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Service
@Transactional
public class ActorService {

	//Managed Repository ---------------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;

	//Supporting services --------------------------------------------------
	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private FloatService			floatService;


	//Simple CRUD methods --------------------------------------------------

	public Collection<Actor> findAll() {

		final Collection<Actor> actors = this.actorRepository.findAll();

		Assert.notNull(actors);

		return actors;
	}

	public Actor findOne(final int ActorId) {

		final Actor actor = this.actorRepository.findOne(ActorId);

		Assert.notNull(actor);

		return actor;
	}

	public Actor save(final Actor a) {

		final Actor actor = this.actorRepository.save(a);

		Assert.notNull(actor);

		return actor;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));
		this.actorRepository.delete(actor);
	}

	//Other business methods----------------------------

	public Actor findByPrincipal() {
		Actor a;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		a = this.findByUserAccount(userAccount);
		Assert.notNull(a);

		return a;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);
		UserAccount result;
		result = this.userAccountService.findByActor(actor);
		return result;
	}

	public Actor editPersonalData(final Actor actor) {
		Assert.notNull(actor);
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.isTrue(actor.getUserAccount().equals(userAccount));
		final Actor result = this.save(actor);

		return result;
	}

	public void checkEmail(final String email, final Boolean isAdmin) {

		if (!isAdmin) {
			final boolean checkEmailOthers = email.matches("^[\\w]+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+|(([\\w]\\s)*[\\w])+<\\w+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+>$");
			Assert.isTrue(checkEmailOthers);
		} else {
			final boolean checkEmailAdmin = email.matches("^[\\w]+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+){0,1}|(([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+){0,1}>$");
			Assert.isTrue(checkEmailAdmin);
		}
	}

	public String checkPhone(final String phone) {
		String res = phone;

		//Esto es para contar el número de dígitos que contiene 
		int count = 0;
		for (int i = 0, len = phone.length(); i < len; i++)
			if (Character.isDigit(phone.charAt(i)))
				count++;

		if (StringUtils.isNumericSpace(phone) && count == 4) {
			res.replaceAll("\\s+", ""); //quitar espacios
			res = this.configurationService.findConfiguration().getCountryCode() + " " + phone;

		}
		return res;

	}

	public String authorityAuthenticated() {
		String res = null;

		final Authority authority1 = new Authority();
		authority1.setAuthority(Authority.MEMBER);
		final Authority authority2 = new Authority();
		authority2.setAuthority(Authority.BROTHERHOOD);
		final Authority authority3 = new Authority();
		authority3.setAuthority(Authority.ADMIN);

		if (LoginService.getPrincipal().getAuthorities().contains(authority1))
			res = "member";
		else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
			res = "brotherhood";
		else if (LoginService.getPrincipal().getAuthorities().contains(authority3))
			res = "admin";
		return res;

	}

	public void convertToSpammerActor() {
		final Actor actor = this.findByPrincipal();
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();

		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);

		final Authority authBrotherhood = new Authority();
		authBrotherhood.setAuthority(Authority.BROTHERHOOD);

		final Authority authMember = new Authority();
		authMember.setAuthority(Authority.MEMBER);

		if (authorities.contains(authAdmin)) {
			final Administrator administrator = this.administratorService.findByPrincipal();
			administrator.setSpammer(true);
			this.administratorService.save(administrator);

		} else if (authorities.contains(authBrotherhood)) {
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			brotherhood.setSpammer(true);
			this.brotherhoodService.save(brotherhood);

		} else if (authorities.contains(authMember)) {
			final Member member = this.memberService.findByPrincipal();
			member.setSpammer(true);
			this.memberService.save(member);

		}

	}

	public void banOrUnBanActor(final Actor actor) {
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();

		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);

		final Authority authBrotherhood = new Authority();
		authBrotherhood.setAuthority(Authority.BROTHERHOOD);

		final Authority authMember = new Authority();
		authMember.setAuthority(Authority.MEMBER);

		if (authorities.contains(authAdmin)) {
			final Administrator administrator = this.administratorService.findOne(actor.getId());
			final UserAccount userAccount = administrator.getUserAccount();
			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
			this.userAccountService.save(userAccount);

		} else if (authorities.contains(authBrotherhood)) {
			final Brotherhood brotherhood = this.brotherhoodService.findOne(actor.getId());
			final UserAccount userAccount = brotherhood.getUserAccount();
			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
			this.userAccountService.save(userAccount);

		} else if (authorities.contains(authMember)) {
			final Member member = this.memberService.findOne(actor.getId());
			final UserAccount userAccount = member.getUserAccount();
			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
			this.userAccountService.save(userAccount);

		}
	}

	public Boolean existActor(final int actorId) {
		Boolean res = false;

		final Actor actor = this.actorRepository.findOne(actorId);

		if (actor != null)
			res = true;

		return res;

	}

	public Collection<Actor> actorSpammer() {
		Collection<Actor> result;

		result = this.actorRepository.actorSpammer();

		return result;
	}

	public Collection<Actor> actorNoSpammer() {
		Collection<Actor> result;

		result = this.actorRepository.actorNoSpammer();

		return result;
	}

	public Collection<Actor> actorScore1() {

		final Collection<Actor> result = this.actorRepository.actorScore1();

		return result;
	}

	public Collection<Actor> actorScore2() {

		final Collection<Actor> result = this.actorRepository.actorScore2();

		return result;
	}

	public Collection<Actor> actorScore3() {

		final Collection<Actor> result = this.actorRepository.actorScore3();

		return result;
	}

	public Collection<Actor> actorScore4() {

		final Collection<Actor> result = this.actorRepository.actorScore4();

		return result;
	}

	public Collection<Actor> actorScore5() {

		final Collection<Actor> result = this.actorRepository.actorScore5();

		return result;
	}

	public Collection<Actor> actorScore6() {

		final Collection<Actor> result = this.actorRepository.actorScore6();

		return result;
	}

	public Collection<Actor> actorScore7() {

		final Collection<Actor> result = this.actorRepository.actorScore7();

		return result;
	}

	public Collection<Actor> actorScore8() {

		final Collection<Actor> result = this.actorRepository.actorScore8();

		return result;
	}

	public void masterDelete(final int actorId) {

		final Authority member = new Authority();
		member.setAuthority(Authority.MEMBER);

		final Authority brotherhood = new Authority();
		brotherhood.setAuthority(Authority.BROTHERHOOD);

		final Actor actor = this.actorRepository.findOne(actorId);

		this.messageService.deleteAll(actorId);

		this.boxService.deleteAll(actorId);

		this.socialProfileService.deleteAll(actorId);

		if (actor.getUserAccount().getAuthorities().contains(member)) {

			this.requestService.deleteAll(actorId);

			this.finderService.deleteFinderActor(actorId);

			this.enrolmentService.deleteAllMember(actorId);

			this.memberService.deleteRelantionshipBrotherhoodMember(actorId);

		} else if (actor.getUserAccount().getAuthorities().contains(brotherhood)) {

			this.enrolmentService.deleteAllBrotherhood(actorId);

			this.areaService.deleteRelationshipAreaBrotherhood(actorId);

			this.processionService.deleteAll(actorId);

			this.floatService.deleteAll(actorId);

		}

		this.actorRepository.delete(actor);

	}
}
