package de.hhu.mentoring.database.repository;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.ChangeRequest;

public interface ChangeRequestRepository extends CrudRepository<ChangeRequest, Long>{

}
