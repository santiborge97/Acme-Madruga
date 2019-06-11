
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Area extends DomainEntity {

	private String					name;
	private Collection<String>		pictures;

	//relationship
	private Collection<Brotherhood>	brotherhoods;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@ElementCollection
	@NotEmpty
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Valid
	@OneToMany(mappedBy = "area")
	public Collection<Brotherhood> getBrotherhoods() {
		return this.brotherhoods;
	}

	public void setBrotherhoods(final Collection<Brotherhood> brotherhoods) {
		this.brotherhoods = brotherhoods;
	}

}
