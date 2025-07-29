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
@Table(name="agreements")
public class Agreement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Size(min = 1,max = 254,message ="Bitte gib ein Ziel an(1-254 Zeichen)")
	private String goal;
	
	private LocalDateTime beginning;
	
	private LocalDateTime end;
	
	@ManyToOne
	private File file;
	
	
	public Agreement() {}
	
	public Agreement(File file, String goal, LocalDateTime beginning, LocalDateTime end) {
		this();
		this.file = file;
		this.goal = goal;
		this.beginning = beginning;
		this.end = end;
	}

}

