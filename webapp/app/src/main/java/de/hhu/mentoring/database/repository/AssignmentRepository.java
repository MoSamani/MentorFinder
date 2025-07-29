package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.User;

public interface AssignmentRepository extends CrudRepository<Assignment, Long> {

	public Assignment findAssignmentByStudent(User student);
	
	public List<Assignment> findAssignmentsByMentor(User mentor);
	
	public Assignment findAssignmentByStudentAndMentor(User student, User mentor);
}
