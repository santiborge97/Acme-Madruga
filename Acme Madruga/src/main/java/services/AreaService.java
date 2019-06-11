
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AreaRepository;
import domain.Area;
import domain.Brotherhood;

@Service
@Transactional
public class AreaService {

	// Managed repository

	@Autowired
	private AreaRepository		areaRepository;

	// Suporting services

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods

	public Area create() {

		Area result;

		result = new Area();
		final Collection<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();
		result.setBrotherhoods(brotherhoods);

		return result;

	}

	public Collection<Area> findAll() {
		final Collection<Area> areas = this.areaRepository.findAll();

		Assert.notNull(areas);

		return areas;
	}

	public Area findOne(final int areaId) {
		final Area area = this.areaRepository.findOne(areaId);

		Assert.notNull(area);

		return area;
	}

	public Area save(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(area.getName() != "");
		Assert.notEmpty(area.getPictures());
		if (area.getId() == 0) {

		}

		this.checkPictures(area.getPictures());
		final Area result = this.areaRepository.save(area);
		return result;
	}

	public void delete(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(area.getBrotherhoods().isEmpty());
		this.areaRepository.delete(area);
	}

	public void deleteRelationshipAreaBrotherhood(final int actorId) {

		final Collection<Area> areas = this.findAll();

		final Brotherhood brotherhood = this.brotherhoodService.findOne(actorId);
		for (final Area a : areas)
			if (a.getBrotherhoods().contains(brotherhood)) {
				a.getBrotherhoods().remove(brotherhood);
				this.save(a);
			}

		brotherhood.setArea(null);
		this.brotherhoodService.saveAdmin(brotherhood);
	}

	public Area reconstruct(final Area area, final BindingResult binding) {
		Area areaNew;
		final Area areaBBDD;

		if (area.getId() == 0) {
			areaNew = this.create();
			area.setBrotherhoods(areaNew.getBrotherhoods());
		} else {
			areaBBDD = this.findOne(area.getId());
			area.setBrotherhoods(areaBBDD.getBrotherhoods());
		}

		this.validator.validate(area, binding);

		return area;
	}
	// Other business methods

	public Collection<Double> statsOfBrotherhoodPerArea() {

		Collection<Double> result;

		result = this.areaRepository.statsOfBrotherhoodPerArea();

		return result;
	}

	public String ratioBrotherhoodPerArea() {

		String res = "N/A";
		Double ratio = 0.0;
		Integer brotherhood = 0;
		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			try {
				brotherhood = this.brotherhoodService.findAll().size();
			} catch (final Throwable oops) {

			}

			if (brotherhood != 0)
				ratio = (brotherhood * 1.0) / areas.size();

			res = Double.toString(ratio);
		}

		return res;

	}

	public String countBrotherhoodPerArea() {

		String res = "0";
		Integer count = 0;

		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			count = areas.size();
			res = Integer.toString(count);
		}

		return res;

	}

	public String minBrotherhoodPerArea() {

		String res = "N/A";
		Integer min = 999999999;
		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			for (final Area a : areas) {
				final Collection<Brotherhood> b = a.getBrotherhoods();
				if (!b.isEmpty() && (b.size() < min))
					min = b.size();
				else if (b.isEmpty())
					min = 0;

			}

			res = Integer.toString(min);

		}

		return res;
	}

	public String maxBrotherhoodPerArea() {

		String res = "N/A";
		Integer max = 0;
		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			for (final Area a : areas) {
				final Collection<Brotherhood> b = a.getBrotherhoods();
				if (!b.isEmpty() && (b.size() > max))
					max = b.size();
			}

			res = Integer.toString(max);

		}

		return res;
	}

	public String avgBrotherhoodPerArea() {

		String res = "N/A";
		Double avg = 0.0;
		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			final Integer total = areas.size();
			Integer sum = 0;
			for (final Area a : areas) {
				final Collection<Brotherhood> b = a.getBrotherhoods();
				if (!b.isEmpty())
					sum = sum + b.size();
			}

			if (sum > 0)
				avg = (sum * 1.0) / total;

			res = Double.toString(avg);

		}

		return res;
	}

	public String stddevBrotherhoodPerArea() {

		String res = "N/A";
		Double avg = 0.0;
		Integer count = 0;
		Integer sum2 = 0;
		Double op = 0.0;
		final Collection<Area> areas = this.areaRepository.findAll();

		if (!areas.isEmpty()) {
			final Integer total = areas.size();
			Integer sum = 0;
			for (final Area a : areas) {
				final Collection<Brotherhood> b = a.getBrotherhoods();
				if (!b.isEmpty()) {
					sum = sum + b.size();
					sum2 = sum2 + (b.size() * b.size());
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

	public Double ratioArea() {

		Double result;

		result = this.areaRepository.ratioArea();

		return result;
	}

	public void checkPictures(final Collection<String> attachments) {

		for (final String url : attachments) {
			final boolean checkUrl = url.matches("^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:])+[a-zA-Z0-9/]+$");
			Assert.isTrue(checkUrl);

		}
	}

}
