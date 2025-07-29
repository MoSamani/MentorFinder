package de.hhu.mentoring.database.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(exclude= {"file"})
@EqualsAndHashCode(exclude={"file"})
@Data
@Entity
@Table(name="notes")
public class Note {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size(min = 1,max = 70,message ="Bitte gib einen Titel an (1-70 Zeichen)")
	private String title;
	
	@NotNull
	@Size(min = 1,max = 254,message ="Bitte gib einen Inhalt an (1-254 Zeichen)")
	private String content;
	
	private LocalDateTime date;
	
	@ManyToOne
	private File file;
	
	
	public Note() {}
	
	public Note(File file, String title, String content, LocalDateTime date) {
		this();
		this.file = file;
		this.title = title;
		this.content = content;
		this.date = date;
	}
}
