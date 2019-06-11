
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Box extends DomainEntity {

	//Atributos-----------------------------------------------------------
	private String	name;
	private Boolean	byDefault;


	//Getters y Setters-----------------------------------------------------
	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public Boolean getByDefault() {
		return this.byDefault;
	}

	public void setByDefault(final Boolean byDefault) {
		this.byDefault = byDefault;
	}


	// Relationships ----------------------------------------------------------
	private Box		parent;
	private Actor	actor;


	@ManyToOne(optional = true)
	@Valid
	public Box getParent() {
		return this.parent;
	}

	public void setParent(final Box parent) {
		this.parent = parent;
	}

	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
