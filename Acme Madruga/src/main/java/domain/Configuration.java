
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	//Atributos-----------------------------------------------------------
	private Collection<String>	spamWords;
	private String				banner;
	private Collection<String>	positiveWords;
	private Collection<String>	negativeWords;
	private String				countryCode;
	private int					finderTime;
	private int					finderResult;
	private Collection<String>	priorities;
	private String				welcomeMessage;
	private String				welcomeMessageEs;


	//Getters y Setters-----------------------------------------------------

	@ElementCollection
	@NotNull
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@SafeHtml
	public String getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@SafeHtml
	public String getWelcomeMessageEs() {
		return this.welcomeMessageEs;
	}

	public void setWelcomeMessageEs(final String welcomeMessageEs) {
		this.welcomeMessageEs = welcomeMessageEs;
	}

	@URL
	@SafeHtml
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@ElementCollection
	@NotNull
	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@ElementCollection
	@NotNull
	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@NotBlank
	@Pattern(regexp = "^\\+\\d{1,3}$")
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@Min(1)
	@Max(24)
	public int getFinderTime() {
		return this.finderTime;
	}

	public void setFinderTime(final int finderTime) {
		this.finderTime = finderTime;
	}

	@Min(1)
	@Max(100)
	public int getFinderResult() {
		return this.finderResult;
	}

	public void setFinderResult(final int finderResult) {
		this.finderResult = finderResult;
	}

	@ElementCollection
	@NotEmpty
	public Collection<String> getPriorities() {
		return this.priorities;
	}

	public void setPriorities(final Collection<String> priorities) {
		this.priorities = priorities;
	}

	// Relationships ----------------------------------------------------------
}
