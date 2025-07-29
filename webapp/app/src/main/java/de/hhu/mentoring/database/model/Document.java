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

@ToString(exclude= {"file"})
@EqualsAndHashCode(exclude={"file"})
@Data
@Entity
@Table(name="documents")
public class Document {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String title;
	
	private LocalDateTime date;
	
	private String filename;
	
	@ManyToOne
	private File file;
	
	
	public Document() {}
	
	public Document(File file, String title, LocalDateTime date, String filename) {
		this();
		this.file = file;
		this.title = title;
		this.date = date;
		this.filename = filename;
	}
	
}
