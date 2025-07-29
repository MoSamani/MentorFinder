package de.hhu.mentoring.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"student","assignment"})
@EqualsAndHashCode(exclude={"student","assignment"})
@Data
@Entity
@Table(name="changeRequests")
public class ChangeRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size(min = 1,max = 254,message = "Gib eine Begründung an (1-254 Zeichen)")
	private String reason;
	
	@OneToOne
	private User student;
	
	@OneToOne
	private Assignment assignment;
	
	
	
	
	//Constructor
	
	public ChangeRequest() {}
	
	public ChangeRequest(String reason, User student, Assignment assignment) {
		this();
		this.reason = reason;
		this.student = student;
		this.assignment = assignment;
	}
}
