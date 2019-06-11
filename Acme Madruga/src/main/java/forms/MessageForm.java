
package forms;


public class MessageForm {

	// Constructors -----------------------------------------------------------
	public MessageForm() {
		super();
	}


	// Properties -------------------------------------------------------------

	private int		id;
	private int		version;
	private int		recipientId;
	private int		senderId;
	private String	subject;
	private String	body;
	private String	priority;
	private String	tags;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public int getRecipientId() {
		return this.recipientId;
	}

	public void setRecipientId(final int recipientId) {
		this.recipientId = recipientId;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId(final int senderId) {
		this.senderId = senderId;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(final String tags) {
		this.tags = tags;
	}

}
