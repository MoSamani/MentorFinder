package de.hhu.mentoring.database.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"assignment"})
@EqualsAndHashCode(exclude={"assignment"})
@Data
@Entity
@Table(name="appointment_proposals")
public class AppointmentProposal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private LocalDateTime Date1;
	
	private LocalDateTime Date2;
	
	private LocalDateTime Date3;
	
	@ManyToOne
	private Assignment assignment;

	
	
	
	// Constructor
	
	public AppointmentProposal() {}
		
	public AppointmentProposal(LocalDateTime Date1, LocalDateTime Date2, LocalDateTime Date3, Assignment assignment) {
		this.Date1 = Date1;
		this.Date2 = Date2;
		this.Date3 = Date3;
		this.assignment = assignment;
	}
}
