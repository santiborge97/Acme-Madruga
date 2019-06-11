
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(indexes = @Index(columnList = "score, spammer"))
public abstract class Actor extends DomainEntity {

	//Atributos-----------------------------------------------------------
	private String	name;
	private String	middleName;
	private String	surname;
	private String	photo;
	private String	email;
	private String	phone;
	private String	address;
	private Double	score;
	private Boolean	spammer;


	//Getters y Setters-----------------------------------------------------
	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@SafeHtml
	public String getMiddleName() {
		return this.middleName;
	}
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	@SafeHtml
	public String getSurname() {
		return this.surname;
	}
	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	@SafeHtml
	public String getPhoto() {
		return this.photo;
	}
	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@SafeHtml
	@Pattern(regexp = "^[\\w]+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+){0,1}|(([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+){0,1}>")
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	//pattern="^\\+\\d{2}\\s\\(\\d{3}\\)\\s\\d{4}|\\+\\d{2}\\s\\d{4}|\\d{4}$" 
	@SafeHtml
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@SafeHtml
	public String getAddress() {
		return this.address;
	}
	public void setAddress(final String address) {
		this.address = address;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	public Boolean getSpammer() {
		return this.spammer;
	}
	public void setSpammer(final Boolean spammer) {
		this.spammer = spammer;
	}


	// Relationships ----------------------------------------------------------
	private UserAccount	userAccount;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}
	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
