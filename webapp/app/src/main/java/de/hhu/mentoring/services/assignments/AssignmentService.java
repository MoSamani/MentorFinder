package de.hhu.mentoring.services.assignments;

import java.util.List;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.User;

public interface AssignmentService {
	
	public void removeAssignmentByOrganizer(Assignment assignment);
	
	public void removeAssignment(Assignment assignment);
	
	public boolean assignMentorToStudent(User student, User mentor);
	
	public List<Assignment> getAllAssignments();
	
	public Assignment getAssignmentOfStudent(User student);
	
	public User getMentorOfStudent(User student);
	
	public List<Assignment> getAllAssignmentsOfMentor(User mentor);
	
	public List<User> getAllStudentsOfMentor(User mentor);
	
	public List<User> getAllStudentsWithoutMentor();
	
	public Assignment getAssignmentById(Long id);

}
