
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Brotherhood extends Actor {

	private String				title;
	private Collection<String>	pictures;
	private Date				establishment;

	//relationships
	private Collection<Member>	members;
	private Area				area;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@ElementCollection
	@NotNull
	@NotEmpty
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Past
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	public Date getEstablishment() {
		return this.establishment;
	}

	public void setEstablishment(final Date establishment) {
		this.establishment = establishment;
	}

	@Valid
	@ManyToMany
	public Collection<Member> getMembers() {
		return this.members;
	}

	public void setMembers(final Collection<Member> members) {
		this.members = members;
	}

	@Valid
	@ManyToOne(optional = true)
	public Area getArea() {
		return this.area;
	}

	public void setArea(final Area area) {
		this.area = area;
	}

}
