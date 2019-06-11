
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.EnrolmentRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository

	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	// Suporting services

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ActorService		actorSercice;

	@Autowired
	private MemberService		memberSercice;


	// Simple CRUD methods

	public Enrolment create(final int brotherhoodId) {

		final Member member = this.memberSercice.findByPrincipal();
		Assert.notNull(member);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		Assert.isTrue(member.getUserAccount().getAuthorities().contains(authority));

		final Date currentMoment = new Date(System.currentTimeMillis() - 1000);

		Enrolment result;

		result = new Enrolment();

		result.setBrotherhood(this.brotherhoodService.findOne(brotherhoodId));
		result.setMember(this.memberSercice.findByPrincipal());
		result.setMoment(currentMoment);

		return result;

	}

	public Collection<Enrolment> findAll() {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findAll();

		Assert.notNull(enrolments);

		return enrolments;

	}

	public Enrolment findOne(final int enrolmentID) {

		final Enrolment enrolment = this.enrolmentRepository.findOne(enrolmentID);

		Assert.notNull(enrolment);

		return enrolment;

	}

	public Enrolment save(final Enrolment enrolment) {

		Assert.notNull(enrolment);

		Enrolment result = enrolment;
		final Actor actor = this.actorSercice.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		final Authority authority2 = new Authority();
		authority2.setAuthority(Authority.BROTHERHOOD);

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority) || actor.getUserAccount().getAuthorities().contains(authority2));
		Assert.isTrue(actor.getId() == enrolment.getBrotherhood().getId() || actor.getId() == enrolment.getMember().getId());

		if (actor.getUserAccount().getAuthorities().contains(authority2))
			Assert.notNull(this.brotherhoodService.findByPrincipal().getArea());

		result = this.enrolmentRepository.save(enrolment);

		return result;

	}
	public void deleteAllMember(final int actorId) {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findEnrolmentsByMemberId(actorId);

		if (!enrolments.isEmpty())
			for (final Enrolment r : enrolments)
				this.enrolmentRepository.delete(r);
	}

	public void deleteAllBrotherhood(final int actorId) {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findEnrolmentsByBrotherhoodId(actorId);

		if (!enrolments.isEmpty())
			for (final Enrolment r : enrolments)
				this.enrolmentRepository.delete(r);
	}

	public Boolean enrolmentMemberSecurity(final int enrolmentId) {

		Boolean res = false;
		final Enrolment enrolment = this.findOne(enrolmentId);

		final Member owner = enrolment.getMember();

		final Member login = this.memberSercice.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public Boolean enrolmentBrotherhoodSecurity(final int enrolmentId) {

		Boolean res = false;
		final Enrolment enrolment = this.findOne(enrolmentId);

		final Brotherhood owner = enrolment.getBrotherhood();

		final Brotherhood login = this.brotherhoodService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {

		final Enrolment result = enrolment;
		final Enrolment theOldOne = this.enrolmentRepository.findOne(enrolment.getId());

		result.setBrotherhood(theOldOne.getBrotherhood());
		result.setMember(theOldOne.getMember());
		result.setMoment(theOldOne.getMoment());
		theOldOne.setDropOutMoment(theOldOne.getDropOutMoment());

		return result;

	}

	public Collection<Enrolment> findEnrolmentsByMemberId(final int id) {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findEnrolmentsByMemberId(id);

		Assert.notNull(enrolments);

		return enrolments;
	}

	public Collection<Enrolment> findEnrolmentPerPosition(final int positionId) {

		final Collection<Enrolment> result = this.enrolmentRepository.findEnrolmentPerPosition(positionId);

		return result;
	}

	public Collection<Enrolment> findEnrolmentsByBrotherhoodIdNoPosition(final int id) {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findEnrolmentsByBrotherhoodIdNoPosition(id);

		Assert.notNull(enrolments);

		return enrolments;
	}

	public Collection<Enrolment> findEnrolmentsByBrotherhoodId(final int id) {

		final Collection<Enrolment> enrolments = this.enrolmentRepository.findEnrolmentsByBrotherhoodId(id);

		Assert.notNull(enrolments);

		return enrolments;
	}

	public Boolean findEnrolmentsByMemberIdAndBrotherhood(final int id, final int brotherhoodId) {

		final Enrolment enrolment = this.enrolmentRepository.findEnrolmentsByMemberIdAndBrotherhood(id, brotherhoodId);

		Boolean result = false;

		result = (enrolment == null);

		return result;

	}
}
