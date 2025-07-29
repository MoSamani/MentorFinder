package de.hhu.mentoring.services.assignments;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AssignmentRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.messaging.ReminderService;

@Service
public class AssignmentServiceDatabase implements AssignmentService {
	
	@Autowired
	AssignmentRepository assignmentRepo;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ReminderService reminderService;
	
	@Autowired
	UserRepository userRepo;
	
	public void removeAssignmentByOrganizer(Assignment assignment) {
		reminderService.sendReminderAssignmentDeletedByOrganizer(assignment);
		removeAssignment(assignment);
	}
	
	public void removeAssignment(Assignment assignment) {
		if (assignment != null) {			
			assignment.getStudent().removeFromAssignment(assignment);
			assignment.getMentor().removeFromAssignment(assignment);			
			assignment.setStudent(null);
			assignment.setMentor(null);

			assignmentRepo.delete(assignment);
		}
	}
	
	public Assignment getAssignmentById(Long id) {
		return assignmentRepo.findOne(id);
	}
	
	public boolean assignMentorToStudent(User student, User mentor) {
		if (getAssignmentOfStudent(student) == null) {
			Assignment a = new Assignment(student, mentor);
			assignmentRepo.save(a);
			reminderService.sendReminderNewAssignment(a);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Assignment> getAllAssignments() {
		return (List<Assignment>)assignmentRepo.findAll();
	}
	
	public Assignment getAssignmentOfStudent(User student) {
		return assignmentRepo.findAssignmentByStudent(student);
	}
	
	public User getMentorOfStudent(User student) {
		return getAssignmentOfStudent(student).getMentor();
	}
	
	public List<Assignment> getAllAssignmentsOfMentor(User mentor) {
		User mentorRefreshed = userRepo.findOne(mentor.getId());
		List<Assignment> assignmentsOfMentor = new ArrayList<Assignment>();
		if (mentorRefreshed.getMentorInAssignments() != null) {
			assignmentsOfMentor.addAll(mentorRefreshed.getMentorInAssignments());
		}
		return assignmentsOfMentor;
	}
	
	public List<User> getAllStudentsOfMentor(User mentor) {
		List<Assignment> assignmentsOfMentor = getAllAssignmentsOfMentor(mentor);
		List<User> studentsOfMentor = new ArrayList<User>();
		
		
			for (Assignment a : assignmentsOfMentor) {
				studentsOfMentor.add(a.getStudent());
			}
		
		
		
		return studentsOfMentor;
	}
	
	public List<User> getAllStudentsWithoutMentor() {
		List<User> students = accountService.getAllStudents();
		
		List<User> singleStudents = new ArrayList<User>();
		for (User student : students) {
			if (student.getStudentInAssignment() == null) {
				singleStudents.add(student);
			}
		}
		
		return singleStudents;
	}

}
