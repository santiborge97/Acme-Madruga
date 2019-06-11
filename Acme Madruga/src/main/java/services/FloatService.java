
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FloatRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Service
@Transactional
public class FloatService {

	// Managed repository
	@Autowired
	private FloatRepository		floatRepository;

	//Supporting Services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods
	public Float create() {
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		final Float result = new Float();

		final Collection<String> pictures = new ArrayList<>();

		result.setPictures(pictures);
		result.setBrotherhood(brotherhood);

		return result;

	}

	public Collection<Float> findAll() {
		final Collection<Float> floats = this.floatRepository.findAll();

		Assert.notNull(floats);

		return floats;
	}

	public Float findOne(final int floatId) {
		final Float floatt = this.floatRepository.findOne(floatId);

		return floatt;
	}

	public Float save(final Float floatt) {
		Assert.notNull(floatt);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Authority br = new Authority();
		br.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(br));
		Assert.isTrue(actor.getId() == floatt.getBrotherhood().getId());

		this.checkPictures(floatt.getPictures());
		final Float result = this.floatRepository.save(floatt);

		return result;

	}

	public void delete(final Float floatt) {
		Assert.notNull(floatt);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Authority br = new Authority();
		br.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(br));
		Assert.isTrue(actor.getId() == floatt.getBrotherhood().getId());

		final Collection<Procession> processions = this.processionService.findProcessionsByFloatId(floatt.getId());
		for (final Procession procession : processions) {
			final Collection<Float> floats = procession.getFloats();
			floats.remove(floatt);
			procession.setFloats(floats);
			this.processionService.saveByEditBrotherhood(procession);
			Assert.isTrue(!procession.getFloats().contains(floatt));
		}

		this.floatRepository.delete(floatt);

	}
	public void deleteAll(final int actorId) {

		final Collection<Float> floats = this.floatRepository.findFloatsByBrotherhoodId(actorId);

		if (!floats.isEmpty())
			for (final Float f : floats)
				this.floatRepository.delete(f);
	}

	//Ohter business methods------------------------------------
	public Collection<Float> findFloatsByBrotherhoodId(final int brotherhoodId) {
		final Collection<Float> result = this.floatRepository.findFloatsByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Boolean floatBrotherhoodSecurity(final int floatId) {
		Boolean res = false;

		final Brotherhood owner = this.findOne(floatId).getBrotherhood();

		final Brotherhood login = this.brotherhoodService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public void addFloatToProcession(final Float floatt, final Procession procession) {
		final Collection<Float> floats = procession.getFloats();
		Assert.isTrue(!floats.contains(floatt));
		floats.add(floatt);

		procession.setFloats(floats);

		this.processionService.save(procession);

		Assert.isTrue(procession.getFloats().contains(floatt));

	}

	//	public Float reconstruct(final FloatForm floatForm, final BindingResult binding) {
	//
	//		final Float result = this.create();
	//
	//		result.setTitle(floatForm.getTitle());
	//		result.setDescription(floatForm.getDescription());
	//		result.setPictures(floatForm.getPictures());
	//
	//		this.validator.validate(result, binding);
	//
	//		return result;
	//	}

	public Float reconstruct(final Float floatt, final BindingResult binding) {

		Float result = floatt;
		final Float floatNew = this.create();

		if (floatt.getId() == 0 || floatt == null) {

			floatt.setBrotherhood(floatNew.getBrotherhood());
			this.validator.validate(floatt, binding);
			result = floatt;

		} else {
			final Float floatBBDD = this.floatRepository.findOne(floatt.getId());
			floatt.setBrotherhood(floatBBDD.getBrotherhood());
			this.validator.validate(floatt, binding);
		}

		return result;
	}

	public void checkPictures(final Collection<String> attachments) {

		for (final String url : attachments) {
			final boolean checkUrl = url.matches("^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:])+[a-zA-Z0-9/]+$");
			Assert.isTrue(checkUrl);

		}
	}

}
