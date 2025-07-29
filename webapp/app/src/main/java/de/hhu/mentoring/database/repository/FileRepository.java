package de.hhu.mentoring.database.repository;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.User;

public interface FileRepository extends CrudRepository<File, Long>{

	public File findFileByStudent(User student);
	
}
