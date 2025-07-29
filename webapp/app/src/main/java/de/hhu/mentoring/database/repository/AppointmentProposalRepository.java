package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;

public interface AppointmentProposalRepository extends CrudRepository<AppointmentProposal, Long>{

	public List<AppointmentProposal> findAppointmentProposalsByAssignment(Assignment assignment);
	
}
