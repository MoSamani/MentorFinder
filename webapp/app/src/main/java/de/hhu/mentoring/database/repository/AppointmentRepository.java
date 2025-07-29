package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.Assignment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>{

	public List<Appointment> findAppointmentsByAssignment(Assignment assignment);
	
}
