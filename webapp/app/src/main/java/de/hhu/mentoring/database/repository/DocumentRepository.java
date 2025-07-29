package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;

public interface DocumentRepository extends CrudRepository<Document, Long>{

	public List<Document> findDocumentsByFile(File file);
	
}
