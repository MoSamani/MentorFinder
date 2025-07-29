package de.hhu.mentoring.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"mentor","assignment"})
@EqualsAndHashCode(exclude={"mentor","assignment"})
@Data
@Entity
@Table(name="closingRequests")
public class ClosingRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size(min=1,max=254,message = "Gib eine Begründung an (1-254 Zeichen)")
	private String reason;
	
	@ManyToOne
	private User mentor;
	
	@OneToOne
	private Assignment assignment;
	
	
	
	
	//Constructor
	
	public ClosingRequest() {}
	
	public ClosingRequest(String reason, User mentor, Assignment assignment) {
		this();
		this.reason = reason;
		this.mentor = mentor;
		this.assignment = assignment;
	}
	

}
