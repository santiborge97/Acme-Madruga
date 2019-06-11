
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	//Atributos-----------------------------------------------------------------------
	private String	keyWord;
	private String	area;
	private String	minDate;
	private String	maxDate;
	private Date	lastUpdate;


	//Getters y Setters----------------------------------------------------------------

	@SafeHtml
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	@SafeHtml
	public String getArea() {
		return this.area;
	}

	public void setArea(final String area) {
		this.area = area;
	}

	@Pattern(regexp = "((?:19|20)\\d\\d)/(0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])|| ")
	@SafeHtml
	public String getMinDate() {
		return this.minDate;
	}
	public void setMinDate(final String minDate) {
		this.minDate = minDate;
	}

	@Pattern(regexp = "((?:19|20)\\d\\d)/(0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])|| ")
	@SafeHtml
	public String getMaxDate() {
		return this.maxDate;
	}
	public void setMaxDate(final String maxDate) {
		this.maxDate = maxDate;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}
	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Procession>	processions;
	private Member					member;


	@ManyToMany
	@Valid
	public Collection<Procession> getProcessions() {
		return this.processions;
	}

	public void setProcessions(final Collection<Procession> processions) {
		this.processions = processions;
	}

	@OneToOne(optional = false)
	@Valid
	public Member getMember() {
		return this.member;
	}
	public void setMember(final Member member) {
		this.member = member;
	}

}
