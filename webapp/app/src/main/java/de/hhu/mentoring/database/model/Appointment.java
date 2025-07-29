package de.hhu.mentoring.database.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"assignment","conversationLog"})
@EqualsAndHashCode(exclude={"assignment","conversationLog"})
@Data
@Entity
@Table(name="appointments")
public class Appointment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	
	private LocalDateTime date;
	
	
	private boolean isCanceled;
	
	@ManyToOne
	private Assignment assignment;
	
	@OneToOne(mappedBy = "appointment", cascade = CascadeType.REMOVE)
	private ConversationLog conversationLog;
	
	
	public Appointment() {
		
	}
	
	public Appointment(LocalDateTime date, Assignment assignment) {
		this();
		this.date = date;
		this.isCanceled = false;
		this.assignment = assignment;
	}
	
	public boolean isInPast() {
		
		if(LocalDateTime.now().isAfter(this.date)){
			return true;
		}
		return false;
	}
}
	