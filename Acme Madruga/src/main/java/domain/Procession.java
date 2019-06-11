
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = "ticker")
})
public class Procession extends DomainEntity {

	private String				title;
	private String				description;
	private Date				organisationMoment;
	private String				ticker;
	private Boolean				finalMode;
	private int					maxColumn;
	private int					maxRow;

	//Relationships----------------------------------------
	private Brotherhood			brotherhood;
	private Collection<Float>	floats;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getOrganisationMoment() {
		return this.organisationMoment;
	}

	public void setOrganisationMoment(final Date organisationMoment) {
		this.organisationMoment = organisationMoment;
	}

	@SafeHtml
	@NotEmpty
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotNull
	public Boolean getFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final Boolean finalMode) {
		this.finalMode = finalMode;
	}

	//Relationships-------------------------------------
	@Valid
	@ManyToOne(optional = false)
	@NotNull
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@Valid
	@ManyToMany
	@NotNull
	public Collection<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final Collection<Float> floats) {
		this.floats = floats;
	}

	@Min(0)
	@Max(5000)
	public int getMaxRow() {
		return this.maxRow;
	}

	public void setMaxRow(final int maxRow) {
		this.maxRow = maxRow;
	}

	@Min(0)
	@Max(10)
	public int getMaxColumn() {
		return this.maxColumn;
	}

	public void setMaxColumn(final int maxColumn) {
		this.maxColumn = maxColumn;
	}

}
