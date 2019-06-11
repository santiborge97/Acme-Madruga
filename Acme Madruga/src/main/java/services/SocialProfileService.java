
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	// Suporting services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods

	public SocialProfile create() {

		SocialProfile result;

		result = new SocialProfile();

		final Actor actor = this.actorService.findByPrincipal();
		result.setActor(actor);

		return result;

	}

	public Collection<SocialProfile> findAll() {

		final Collection<SocialProfile> socialProfiles = this.socialProfileRepository.findAll();

		Assert.notNull(socialProfiles);

		return socialProfiles;
	}

	public SocialProfile findOne(final int socialProfileID) {

		final SocialProfile socialProfile = this.socialProfileRepository.findOne(socialProfileID);

		return socialProfile;

	}

	public SocialProfile save(final SocialProfile socialProfile) {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		Assert.notNull(socialProfile);

		final Actor owner = socialProfile.getActor();

		Assert.isTrue(actor.getId() == owner.getId());

		final SocialProfile result = this.socialProfileRepository.save(socialProfile);

		return result;

	}

	public void delete(final SocialProfile socialProfile) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		Assert.notNull(socialProfile);

		final Actor owner = socialProfile.getActor();

		Assert.isTrue(actor.getId() == owner.getId());

		this.socialProfileRepository.delete(socialProfile);
	}

	public void deleteAll(final int actorId) {

		final Collection<SocialProfile> profiles = this.socialProfileRepository.findAllByActor(actorId);

		if (!profiles.isEmpty())
			for (final SocialProfile s : profiles)
				this.socialProfileRepository.delete(s);
	}

	//Other business methods

	public Collection<SocialProfile> findAllByActor(final int id) {

		final Collection<SocialProfile> socialProfiles = this.socialProfileRepository.findAllByActor(id);

		Assert.notNull(socialProfiles);

		return socialProfiles;
	}

	public Boolean socialProfileSecurity(final int socialProfileId) {
		Boolean res = false;

		final Actor owner = this.socialProfileRepository.findOne(socialProfileId).getActor();

		final Actor login = this.actorService.findByPrincipal();

		if (owner.equals(login))
			res = true;

		return res;
	}

	public SocialProfile reconstruct(final SocialProfile socialProfile, final BindingResult binding) {

		SocialProfile result;

		if (socialProfile.getId() == 0 || socialProfile == null) {

			final SocialProfile socialProfileNew = this.create();

			socialProfile.setActor(socialProfileNew.getActor());

			this.validator.validate(socialProfile, binding);

			result = socialProfile;
		} else {

			final SocialProfile socialProfileBBDD = this.findOne(socialProfile.getId());

			socialProfile.setActor(socialProfileBBDD.getActor());

			this.validator.validate(socialProfile, binding);

			result = socialProfile;
		}

		return result;

	}
}
