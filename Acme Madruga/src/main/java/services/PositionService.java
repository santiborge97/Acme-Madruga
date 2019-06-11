
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import domain.Enrolment;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed repository

	@Autowired
	private PositionRepository	positionRepository;

	// Suporting services

	@Autowired
	private EnrolmentService	enrolmentService;


	// Simple CRUD methods

	public Position create() {

		Position result;

		result = new Position();

		return result;

	}

	public Collection<Position> findAll() {

		Assert.notNull(this.positionRepository);

		final Collection<Position> res = this.positionRepository.findAll();

		Assert.notNull(res);

		return res;

	}

	public Position findOne(final int positionId) {

		Assert.notNull(positionId);
		Position result;
		result = this.positionRepository.findOne(positionId);
		return result;
	}

	public Position save(final Position position) {
		Assert.notNull(position);

		final Position result = this.positionRepository.save(position);

		return result;

	}

	public void delete(final Position position) {
		Assert.notNull(position);

		Assert.isTrue(position.getId() != 0);

		final Collection<Enrolment> enrols = this.enrolmentService.findEnrolmentPerPosition(position.getId());
		Assert.isTrue(enrols.isEmpty());
		this.positionRepository.delete(position);

	}

	//Other business methods

	public boolean existId(final int positionId) {
		Boolean res = false;

		final Position position = this.findOne(positionId);

		if (position != null)
			res = true;

		return res;
	}
}
