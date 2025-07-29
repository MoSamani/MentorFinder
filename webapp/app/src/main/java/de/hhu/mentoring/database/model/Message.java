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

@ToString(exclude= {"sender","receiver"})
@EqualsAndHashCode(exclude={"sender","receiver"})
@Data
@Entity
@Table(name="messages")
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size(min=1,max = 70,message ="Bitte gib einen Titel an (1-70 Zeichen)")
	private String title;
	@NotNull
	@Size(min=1,max = 254,message ="Bitte gib einen Inhalt an (1-254 Zeichen)")
	private String content;
	
	private LocalDateTime date;
	
	@ManyToOne
	private User sender;
	
	@ManyToOne
	private User receiver;
	
	
	
	
	// Constructor
	
	public Message() {}
	
	public Message(String title, String content, LocalDateTime date, User sender, User receiver) {
		this();
		this.title = title;
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
		this.date = date;
		
	}
}

