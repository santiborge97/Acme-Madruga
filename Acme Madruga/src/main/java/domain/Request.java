
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = @Index(columnList = "status"))
public class Request extends DomainEntity {

	private String		status;
	private Integer		rowNumber;
	private Integer		columnNumber;
	private String		comment;

	//Relationships----------------------------------------
	private Procession	procession;
	private Member		member;


	@NotBlank
	@Pattern(regexp = "\\APENDING\\z|\\AREJECTED\\z|\\AAPPROVED\\z")
	@SafeHtml
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Min(0)
	@Max(5000)
	public Integer getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(final Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Min(0)
	@Max(10)
	public Integer getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(final Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@SafeHtml
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	//Relationships-------------------------------------
	@ManyToOne(optional = false)
	@NotNull
	public Procession getProcession() {
		return this.procession;
	}

	public void setProcession(final Procession procession) {
		this.procession = procession;
	}

	@ManyToOne(optional = false)
	@NotNull
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}
}
