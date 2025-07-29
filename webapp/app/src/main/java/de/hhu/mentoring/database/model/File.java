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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"agreements", "documents", "notes", "student"})
@EqualsAndHashCode(exclude={"agreements", "documents", "notes", "student"})
@Data
@Entity
@Table(name="files")
public class File {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@OneToMany(mappedBy = "file", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Agreement> agreements;
	
	@OneToMany(mappedBy = "file", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Document> documents;
	
	@OneToMany(mappedBy = "file", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Note> notes;
	
	@OneToOne
	private User student;
	
	
	public File() {}

}
