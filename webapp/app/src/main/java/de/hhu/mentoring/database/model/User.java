package de.hhu.mentoring.database.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString(exclude= {"studentInAssignment", "mentorInAssignments", "file"})
@EqualsAndHashCode(exclude={"studentInAssignment", "mentorInAssignments", "file"})
@Data
@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Size(min = 1, max=69, message = "Gib bitte deinen Vornamen an (1-69 Zeichen)")
	private String prename;
	
	@Size(min=1, max=69,message = "Gib bitte deinen Nachnamen an (1-69 Zeichen)")
	private String surname;
	
	@Size(min = 1, max=69,message = "Gib bitte deine mail Addresse an (1-69 Zeichen)")
	private String mailAddress;
	
	@Size(min = 1, max= 69,message = "Gib bitte dein Passwort an (1-69 Zeichen)")
	private String password;
	
	private Role role;
	
	private boolean enabled = true;
	
	
	
	
	// Assignment Relations
	
	@OneToOne(mappedBy = "student")
	private Assignment studentInAssignment;
	
	
	@OneToMany(mappedBy = "mentor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Assignment> mentorInAssignments;
	
	
	public void removeFromAssignment(Assignment assignment) {		
		if (studentInAssignment != null) {
			studentInAssignment = null;
		}
		if (mentorInAssignments != null && mentorInAssignments.size() > 0) {
			mentorInAssignments.remove(assignment);
		}
	}
	
	//File Relation
	
	@OneToOne(mappedBy = "student")
	private File file;
	
	
	
	// Constructor
	
	public User() {}
	
	public User(String prename, String surname, String mailAddress, String password, Role role) {
		this();
		this.prename = prename;
		this.surname = surname;
		this.mailAddress = mailAddress;
		this.password = password;
		this.role = role;
	}
}