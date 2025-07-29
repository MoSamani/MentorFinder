package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AgreementRepository;
import de.hhu.mentoring.database.repository.AppointmentProposalRepository;
import de.hhu.mentoring.database.repository.AppointmentRepository;
import de.hhu.mentoring.database.repository.AssignmentRepository;
import de.hhu.mentoring.database.repository.ConversationLogRepository;
import de.hhu.mentoring.database.repository.DocumentRepository;
import de.hhu.mentoring.database.repository.FileRepository;
import de.hhu.mentoring.database.repository.MessageRepository;
import de.hhu.mentoring.database.repository.NoteRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.assignments.AssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssignmentServiceTests {
	
	@Autowired
	UserRepository uR;
	
	@Autowired
	AssignmentRepository arep;
	
	@Autowired
	AssignmentService agsv;
	
	@Autowired
	AccountService accs;
	
	@Autowired
	FileRepository fR;
	
	@Autowired
	AgreementRepository aR;
	
	@Autowired
	DocumentRepository dR;
	
	@Autowired
	NoteRepository nR;
	
	@Autowired
	ConversationLogRepository cR;
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentProposalRepository appointmentPropRepo;
	
	@Autowired
	MessageRepository mrep;
	
	@Before
	public void setupRoutine() {
		User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
		User s2 = new User("Paul","Pluto","p@p.de","pw",Role.STUDENT);
		User s3 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
		User s4 = new User("Niklas","Neptun","n@n.de","pw",Role.STUDENT);
		User s5 = new User("Jens","Jupiter","j@j.de","pw",Role.STUDENT);
		
		User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
		User m2 = new User("Erwin","Europa","a@a.de","pw",Role.MENTOR);
		
		accs.save(s1);
		accs.save(s2);
		accs.save(s3);
		accs.save(s4);
		accs.save(s5);
		
		accs.save(m1);
		accs.save(m2);
	}
	
	@After
	public void finishRoutine() {
		appointmentRepo.deleteAll();
		appointmentPropRepo.deleteAll();
		mrep.deleteAll();
		fR.deleteAll();
		aR.deleteAll();
		dR.deleteAll();
		nR.deleteAll();
		cR.deleteAll();
		uR.deleteAll();
		arep.deleteAll();
	}
	
	
	@Test
	public void checkAssignSuccess() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		// All assignments should succeed
		assertEquals(true, as1Success);
		assertEquals(true, as2Success);
		assertEquals(true, as3Success);
		
		List<Assignment> assignments = agsv.getAllAssignments();
		assertEquals(3, assignments.size());
		assertEquals(students.get(0), assignments.get(0).getStudent());
		assertEquals(students.get(1), assignments.get(1).getStudent());
		assertEquals(students.get(2), assignments.get(2).getStudent());
	}
	
	@Test
	public void checkAssignFailure() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(1));
		boolean as3Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		
		// One assignments should fail
		assertEquals(true, as1Success);
		assertEquals(true, as2Success);
		assertEquals(false, as3Success);
		
		// There should be only 2 assignments saved
		List<Assignment> assignments = agsv.getAllAssignments();
		assertEquals(2, assignments.size());
		assertEquals(students.get(0), assignments.get(0).getStudent());
		assertEquals(students.get(1), assignments.get(1).getStudent());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAssignmentOfStudent() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		assertEquals(3, assignments.size());
		assertEquals(assignments.get(0), agsv.getAssignmentOfStudent(students.get(0)));
		assertEquals(assignments.get(1), agsv.getAssignmentOfStudent(students.get(1)));
		assertEquals(assignments.get(2), agsv.getAssignmentOfStudent(students.get(2)));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAssignmentOfStudentShouldReturnNull() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		assertEquals(2, assignments.size());
		assertEquals(assignments.get(0), agsv.getAssignmentOfStudent(students.get(0))) ;
		assertEquals(assignments.get(1), agsv.getAssignmentOfStudent(students.get(1)));
		assertEquals(null, agsv.getAssignmentOfStudent(students.get(2)));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAssignmentsOfMentor() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(0));
		boolean as4Success = agsv.assignMentorToStudent(students.get(3), mentors.get(1));
		boolean as5Success = agsv.assignMentorToStudent(students.get(4), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		List<Assignment> mentorAsg1 = agsv.getAllAssignmentsOfMentor(mentors.get(0));
		List<Assignment> mentorAsg2 = agsv.getAllAssignmentsOfMentor(mentors.get(1));
		
		// First check if size is correct and then check if assignments are correct
		assertEquals(5, assignments.size());
		assertEquals(3, mentorAsg1.size());
		assertEquals(2, mentorAsg2.size());
		assertEquals(true, assignments.contains(mentorAsg1.get(0)));
		assertEquals(true, assignments.contains(mentorAsg1.get(1)));
		assertEquals(true, assignments.contains(mentorAsg1.get(2)));
		assertEquals(true, assignments.contains(mentorAsg2.get(0)));
		assertEquals(true, assignments.contains(mentorAsg2.get(1)));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllStudentsOfMentor() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(0));
		boolean as4Success = agsv.assignMentorToStudent(students.get(3), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		List<User> mentorStuds1 = agsv.getAllStudentsOfMentor(mentors.get(0));
		List<User> mentorStuds2 = agsv.getAllStudentsOfMentor(mentors.get(1));
		
		// Check if size is correct
		assertEquals(5, students.size());
		assertEquals(4, assignments.size());
		assertEquals(3, mentorStuds1.size());
		assertEquals(1, mentorStuds2.size());
		
		// Check if all students are really saved
		assertEquals(true, students.contains(mentorStuds1.get(0)));
		assertEquals(true, students.contains(mentorStuds1.get(1)));
		assertEquals(true, students.contains(mentorStuds1.get(2)));
		assertEquals(true, students.contains(mentorStuds2.get(0)));
		
		// Check if mentor is really mentor of this student (use student to get his assignment to get the mentor again)
		assertEquals(mentors.get(0), agsv.getAssignmentOfStudent(mentorStuds1.get(0)).getMentor());
		assertEquals(mentors.get(0), agsv.getAssignmentOfStudent(mentorStuds1.get(1)).getMentor());
		assertEquals(mentors.get(0), agsv.getAssignmentOfStudent(mentorStuds1.get(2)).getMentor());
		assertEquals(mentors.get(1), agsv.getAssignmentOfStudent(mentorStuds2.get(0)).getMentor());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllStudentsWithoutMentor() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(2), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(4), mentors.get(1));
		
		List<User> singleStudents = agsv.getAllStudentsWithoutMentor();
		
        // First check number of single students, then check if correct students are single
		assertEquals(2, singleStudents.size());
		assertEquals(students.get(1), singleStudents.get(0));
		assertEquals(students.get(3), singleStudents.get(1));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRemoveAssignments() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(0));
		boolean as4Success = agsv.assignMentorToStudent(students.get(3), mentors.get(1));
		boolean as5Success = agsv.assignMentorToStudent(students.get(4), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		agsv.removeAssignment(assignments.get(1));
		agsv.removeAssignment(assignments.get(3));
		
		// First check size, then check if correct assignments were removed
		List<Assignment> assignmentsAfterRemove = agsv.getAllAssignments();
		assertEquals(3, assignmentsAfterRemove.size());
		assertEquals(students.get(0), assignmentsAfterRemove.get(0).getStudent());
		assertEquals(students.get(2), assignmentsAfterRemove.get(1).getStudent());
		assertEquals(students.get(4), assignmentsAfterRemove.get(2).getStudent());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRemoveAssignmentByOrganizer() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(0));
		boolean as4Success = agsv.assignMentorToStudent(students.get(3), mentors.get(1));
		boolean as5Success = agsv.assignMentorToStudent(students.get(4), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		agsv.removeAssignmentByOrganizer(assignments.get(1));
		agsv.removeAssignmentByOrganizer(assignments.get(3));
		
		// First check size, then check if correct assignments were removed
		List<Assignment> assignmentsAfterRemove = agsv.getAllAssignments();
		assertEquals(3, assignmentsAfterRemove.size());
		assertEquals(students.get(0), assignmentsAfterRemove.get(0).getStudent());
		assertEquals(students.get(2), assignmentsAfterRemove.get(1).getStudent());
		assertEquals(students.get(4), assignmentsAfterRemove.get(2).getStudent());
		
		// Checking if reminders are sent is done in ReminderService tests
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetMentorOfStudent() {
		
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		
		assertEquals(mentors.get(0), agsv.getMentorOfStudent(students.get(0)));
		assertEquals(mentors.get(0), agsv.getMentorOfStudent(students.get(1)));
		assertEquals(mentors.get(1), agsv.getMentorOfStudent(students.get(2)));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAssignmentById() {
		List<User> students = accs.getAllStudents();
		List<User> mentors = accs.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		Long as1id = assignments.get(0).getId();
		Long as2id = assignments.get(1).getId();
		Long as3id = assignments.get(2).getId();
		
		assertEquals(assignments.get(0), agsv.getAssignmentById(as1id));
		assertEquals(assignments.get(1), agsv.getAssignmentById(as2id));
		assertEquals(assignments.get(2), agsv.getAssignmentById(as3id));
	}
}
