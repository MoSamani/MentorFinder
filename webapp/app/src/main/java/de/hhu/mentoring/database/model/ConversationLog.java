package de.hhu.mentoring.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"appointment"})
@EqualsAndHashCode(exclude={"appointment"})
@Data
@Entity
@Table(name="conversation_logs")
public class ConversationLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String text;
	
	@OneToOne
	private Appointment appointment;
	
	
	public ConversationLog() {}
	
	public ConversationLog(String text, Appointment appointment) {
		this();
		this.appointment = appointment;
		this.text = text;
	}

}
