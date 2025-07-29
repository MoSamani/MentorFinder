package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;

public interface NoteRepository extends CrudRepository<Note, Long>{

	public List<Note> findNotesByFile(File file);
	
}
