package de.hhu.mentoring.database.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"student", "mentor", "changeRequest", "closingRequests", "appointments", "appointmentProposals"})
@EqualsAndHashCode(exclude={"student", "mentor", "changeRequest", "closingRequests", "appointments", "appointmentProposals"})
@Data
@Entity
@Table(name="assignments")
public class Assignment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@OneToOne
	private User student;
	
	@ManyToOne
	private User mentor;
	
	
	
	
	// Appointment Relations
	
	@OneToMany(mappedBy="assignment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Appointment> appointments;
	
	@OneToMany(mappedBy="assignment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<AppointmentProposal> appointmentProposals;
	
	
	
	
	// Request Relations
	
	@OneToOne(mappedBy="assignment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private ChangeRequest changeRequest;
	
	@OneToMany(mappedBy="assignment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<ClosingRequest> closingRequests;
	
	
	
	
	// Constructor
	
	public Assignment() {}
	
	public Assignment(User student, User mentor) {
		this.student = student;
		this.mentor = mentor;
	}
}
