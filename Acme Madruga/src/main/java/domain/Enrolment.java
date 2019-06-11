
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Enrolment extends DomainEntity {

	//Atributos-----------------------------------------------------------
	private Date	moment;
	private Date	dropOutMoment;


	//Getters y Setters-----------------------------------------------------
	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@Past
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	public Date getDropOutMoment() {
		return this.dropOutMoment;
	}

	public void setDropOutMoment(final Date dropOutMoment) {
		this.dropOutMoment = dropOutMoment;
	}


	// Relationships ----------------------------------------------------------

	private Member		member;

	private Position	position;		//cargo
	private Brotherhood	brotherhood;


	@ManyToOne(optional = false)
	@NotNull
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@ManyToOne(optional = false)
	@NotNull
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@ManyToOne(optional = true)
	public Position getPosition() {
		return this.position;
	}
	public void setPosition(final Position position) {
		this.position = position;
	}

}
