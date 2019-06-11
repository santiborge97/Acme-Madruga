
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProcessionRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Procession;
import domain.Request;

@Service
@Transactional
public class ProcessionService {

	// Managed repository

	@Autowired
	private ProcessionRepository	processionRepository;

	// Suporting services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private Validator				validator;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private RequestService			requestService;


	// Simple CRUD methods

	public Procession create() {

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		final Procession result = new Procession();

		final Collection<Float> floatt = new HashSet<>();
		result.setFloats(floatt);
		result.setBrotherhood(brotherhood);

		result.setFinalMode(false);

		return result;

	}
	private String generateTicker(final Date moment) {

		final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		final String dateString = dateFormat.format(moment);

		final String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		final StringBuilder salt = new StringBuilder();
		final Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			final int index = (int) (rnd.nextFloat() * alphaNumeric.length());
			salt.append(alphaNumeric.charAt(index));
		}
		final String randomAlphaNumeric = salt.toString();

		final String ticker = dateString + "-" + randomAlphaNumeric;

		final int processionSameTicker = this.processionRepository.countProcessionsWithTicker(ticker);

		//nos aseguramos que que sea único
		while (processionSameTicker > 0)
			this.generateTicker(moment);

		return ticker;

	}

	public Collection<Procession> findAll() {

		final Collection<Procession> fixUpTasks = this.processionRepository.findAll();

		Assert.notNull(fixUpTasks);

		return fixUpTasks;
	}

	public Procession findOne(final int processionId) {

		final Procession procession = this.processionRepository.findOne(processionId);

		return procession;

	}

	public Procession save(final Procession procession) {
		//hasta que no tenga el brotherhood area no pueden organizarse processions
		Assert.notNull(procession.getBrotherhood().getArea());

		Assert.notNull(procession);

		Procession result = procession;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		final Date currentMoment = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(procession.getOrganisationMoment().after(currentMoment));

		result = this.processionRepository.save(procession);

		return result;

	}

	//Esto es una solución debido a que si editamos una brotherhood, hay que editar
	//las processions. Si hay processions que ya pasaron daría fallo en el de arriba
	//por el Assert de fechas. Aqui no lo tenemos. Solo es empleado el metodo cuando se edita Brotherhood
	public Procession saveByEditBrotherhood(final Procession procession) {
		//hasta que no tenga el brotherhood area no pueden organizarse processions
		Assert.notNull(procession.getBrotherhood().getArea());

		Assert.notNull(procession);

		Procession result = procession;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));

		result = this.processionRepository.save(procession);

		return result;

	}

	public void delete(final Procession procession) {

		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0);

		final Actor brotherhood = this.actorService.findByPrincipal();
		Assert.notNull(brotherhood);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		Assert.isTrue(brotherhood.getUserAccount().getAuthorities().contains(authority));
		final Collection<Procession> processions = this.processionRepository.findProcessionByBrotherhoodId(brotherhood.getId());
		Assert.isTrue(processions.contains(procession));

		//Finder
		Collection<Finder> findersByProcession = this.finderService.findFindersByProcessionId(procession.getId());
		if (!findersByProcession.isEmpty())
			for (final Finder f : findersByProcession) {

				f.getProcessions().remove(procession);
				this.finderService.save(f);
			}
		findersByProcession = this.finderService.findFindersByProcessionId(procession.getId());
		Assert.isTrue(findersByProcession.isEmpty());

		this.processionRepository.delete(procession);
	}

	public void deleteAll(final int actorId) {

		final Collection<Procession> processions = this.processionRepository.findProcessionByBrotherhoodId(actorId);

		if (!processions.isEmpty())
			for (final Procession p : processions) {

				final Collection<Finder> findersByProcession = this.finderService.findFindersByProcessionId(p.getId());
				if (!findersByProcession.isEmpty())
					for (final Finder f : findersByProcession) {

						f.getProcessions().remove(p);
						this.finderService.save(f);
					}

				final Collection<Request> requests = this.requestService.requestPerProcessionId(p.getId());

				if (!requests.isEmpty())
					for (final Request r : requests)
						this.requestService.delete(r);

				this.processionRepository.delete(p);

			}

	}

	//Other business methods-----------------------------------

	public Collection<Procession> findProcessionByBrotherhoodId(final int brotherhoodId) {

		final Collection<Procession> processions = this.processionRepository.findProcessionByBrotherhoodId(brotherhoodId);

		return processions;
	}

	public Collection<Procession> findMemberProcessions(final int memberId) {

		final Collection<Procession> processions = this.processionRepository.findMemberProcessions(memberId);

		return processions;
	}

	public Collection<Procession> findProcessionsByFloatId(final int floatId) {

		final Collection<Procession> processions = this.processionRepository.findProcessionsByFloatId(floatId);

		return processions;
	}

	public Collection<Procession> findProcessionCanBeSeen() {

		return this.processionRepository.findProcessionCanBeSeen();
	}

	public Collection<Procession> findProcessionCannotBeSeenOfBrotherhoodId(final int brotherhoodId) {

		return this.processionRepository.findProcessionCannotBeSeenOfBrotherhoodId(brotherhoodId);
	}

	public Collection<Procession> findProcessionCanBeSeenOfBrotherhoodId(final int brotherhoodId) {

		final Collection<Procession> result = this.processionRepository.findProcessionCanBeSeenOfBrotherhoodId(brotherhoodId);

		return result;
	}

	public Collection<String> findProcessionsLessThirtyDays() {

		final Collection<String> result = new HashSet<>();

		final Collection<Procession> processions = this.processionRepository.findAll();

		if (!processions.isEmpty())
			for (final Procession p : processions) {
				final Date moment = p.getOrganisationMoment();

				final Date now = new Date(System.currentTimeMillis() - 1000);

				final Interval interval = new Interval(now.getTime(), moment.getTime());
				if (interval.toDuration().getStandardDays() <= 30)
					result.add(p.getTitle());
			}

		return result;
	}

	public Procession reconstruct(final Procession procession, final BindingResult binding) {

		Procession result = procession;
		final Procession processionNew = this.create();

		if (procession.getId() == 0 || procession == null) {

			procession.setBrotherhood(processionNew.getBrotherhood());
			procession.setFloats(processionNew.getFloats());

			if (procession.getOrganisationMoment() != null) {

				final String ticker = this.generateTicker(procession.getOrganisationMoment());
				procession.setTicker(ticker);

			} else
				procession.setTicker(null);

			this.validator.validate(procession, binding);

			result = procession;
		} else {

			final Procession processionBBDD = this.findOne(procession.getId());

			procession.setBrotherhood(processionBBDD.getBrotherhood());
			procession.setFloats(processionBBDD.getFloats());

			if (procession.getOrganisationMoment() != null) {

				final String ticker = this.generateTicker(procession.getOrganisationMoment());
				procession.setTicker(ticker);

			} else
				procession.setTicker(null);

			this.validator.validate(procession, binding);

		}

		return result;

	}

	public Boolean processionBrotherhoodSecurity(final int processionId) {
		Boolean res = false;
		final Procession procession = this.findOne(processionId);

		final Brotherhood owner = procession.getBrotherhood();

		final Brotherhood login = this.brotherhoodService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

}
