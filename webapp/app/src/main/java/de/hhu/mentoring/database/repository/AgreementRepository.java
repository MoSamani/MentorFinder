package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.File;

public interface AgreementRepository extends CrudRepository<Agreement, Long>{

	public List<Agreement> findAgreementsByFile(File file);
	
}

