package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AgreementRepository;
import de.hhu.mentoring.database.repository.AppointmentProposalRepository;
import de.hhu.mentoring.database.repository.AppointmentRepository;
import de.hhu.mentoring.database.repository.AssignmentRepository;
import de.hhu.mentoring.database.repository.ChangeRequestRepository;
import de.hhu.mentoring.database.repository.ClosingRequestRepository;
import de.hhu.mentoring.database.repository.ConversationLogRepository;
import de.hhu.mentoring.database.repository.DocumentRepository;
import de.hhu.mentoring.database.repository.FileRepository;
import de.hhu.mentoring.database.repository.MessageRepository;
import de.hhu.mentoring.database.repository.NoteRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestServiceTests {
	
	@Autowired
	RequestService rqsv;
	
	@Autowired
	AccountService acsv;
	
	@Autowired
	AssignmentService agsv;
	
	@Autowired
	AppointmentService apsv;
	
	@Autowired
	UserRepository urep;
	
	@Autowired
	AssignmentRepository arep;
	
	@Autowired
	MessageRepository mrep;
	
	@Autowired
	ChangeRequestRepository changeRepo;
	
	@Autowired
	ClosingRequestRepository closingRepo;
	
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
	AppointmentRepository appointmentR;
	
	@Autowired
	AppointmentProposalRepository appointmentPropR;
	
	@Before
	public void setupRoutine() {
		User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
		User s2 = new User("Paul","Pluto","p@p.de","pw",Role.STUDENT);
		User s3 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
		User s4 = new User("Niklas","Neptun","n@n.de","pw",Role.STUDENT);
		User s5 = new User("Jens","Jupiter","j@j.de","pw",Role.STUDENT);
		
		User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
		User m2 = new User("Erwin","Europa","e@es.de","pw",Role.MENTOR);
		
		acsv.save(s1);
		acsv.save(s2);
		acsv.save(s3);
		acsv.save(s4);
		acsv.save(s5);
		
		acsv.save(m1);
		acsv.save(m2);
	}
	
	@After
	public void finishRoutine() {
		appointmentR.deleteAll();
		appointmentPropR.deleteAll();
		mrep.deleteAll();
		fR.deleteAll();
		aR.deleteAll();
		dR.deleteAll();
		nR.deleteAll();
		cR.deleteAll();
		urep.deleteAll();
		arep.deleteAll();
		changeRepo.deleteAll();
		closingRepo.deleteAll();
		
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void checkRequestChangeMentor() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		boolean req1Success = rqsv.requestChangeMentor(students.get(0), "checkRequestChangeMentor1");
		boolean req2Success = rqsv.requestChangeMentor(students.get(2), "checkRequestChangeMentor2");
		boolean req3Success = rqsv.requestChangeMentor(mentors.get(0), "checkRequestChangeMentor3");
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		
		assertEquals(true, req1Success);
		assertEquals(true, req2Success);
		assertEquals(false, req3Success);
		assertEquals(2, creq.size());
		assertEquals("checkRequestChangeMentor1", creq.get(0).getReason());
		assertEquals("checkRequestChangeMentor2", creq.get(1).getReason());
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void checkRequestCloseCase() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		boolean req1Success = rqsv.requestCloseCase(mentors.get(0), "checkRequestCloseCase1", assignments.get(0));
		boolean req2Success = rqsv.requestCloseCase(mentors.get(1), "checkRequestCloseCase2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		
		assertEquals(2, creq.size());
		assertEquals(true, req1Success);
		assertEquals(true, req2Success);
		assertEquals("checkRequestCloseCase1", creq.get(0).getReason());
		assertEquals("checkRequestCloseCase2", creq.get(1).getReason());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRequestCloseCaseOneMentorMultipleRequests() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		boolean req1Success = rqsv.requestCloseCase(mentors.get(0), "checkRequestCloseCaseOneMentorMultipleRequests1", assignments.get(0));
		boolean req2Success = rqsv.requestCloseCase(mentors.get(0), "checkRequestCloseCaseOneMentorMultipleRequests2", assignments.get(1));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		
		assertEquals(2, creq.size());
		assertEquals(true, req1Success);
		assertEquals(true, req2Success);
		assertEquals("checkRequestCloseCaseOneMentorMultipleRequests1", creq.get(0).getReason());
		assertEquals("checkRequestCloseCaseOneMentorMultipleRequests2", creq.get(1).getReason());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRequestCloseCaseShouldFail() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		boolean req1Success = rqsv.requestCloseCase(mentors.get(0), "checkRequestCloseCaseShouldFail1", assignments.get(0));
		boolean req2Success = rqsv.requestCloseCase(mentors.get(0), "checkRequestCloseCaseShouldFail2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		
		assertEquals(1, creq.size());
		assertEquals(true, req1Success);
		assertEquals(false, req2Success);
		assertEquals("checkRequestCloseCaseShouldFail1", creq.get(0).getReason());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkAcceptChangeRequest() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestChangeMentor(students.get(0), "checkAcceptChangeRequest1");
		rqsv.requestChangeMentor(students.get(2), "checkAcceptChangeRequest2");
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.acceptChangeRequest(creq.get(0));
		
		List<ChangeRequest> creqRefreshed = rqsv.getAllChangeRequests();
		List<Assignment> assignmentsRefreshed = agsv.getAllAssignments();
		
		assertEquals(1, creqRefreshed.size());
		assertEquals(3, assignments.size());
		assertEquals(2, assignmentsRefreshed.size());
		assertEquals("checkAcceptChangeRequest2", creqRefreshed.get(0).getReason());
		assertEquals(assignments.get(1), assignmentsRefreshed.get(0));
		assertEquals(assignments.get(2), assignmentsRefreshed.get(1));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkAcceptClosingRequest() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		boolean req1Success = rqsv.requestCloseCase(mentors.get(0), "checkAcceptClosingRequest1", assignments.get(0));
		boolean req2Success = rqsv.requestCloseCase(mentors.get(1), "checkAcceptClosingRequest2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		rqsv.acceptClosingRequest(creq.get(0));
		
		List<ClosingRequest> creqRefreshed = rqsv.getAllClosingRequests();
		List<Assignment> assignmentsRefreshed = agsv.getAllAssignments();
		
		assertEquals(1, creqRefreshed.size());
		assertEquals(3, assignments.size());
		assertEquals(2, assignmentsRefreshed.size());
		assertEquals("checkAcceptClosingRequest2", creqRefreshed.get(0).getReason());
		assertEquals(assignments.get(1), assignmentsRefreshed.get(0));
		assertEquals(assignments.get(2), assignmentsRefreshed.get(1));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRejectChangeRequest() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestChangeMentor(students.get(0), "checkRejectChangeRequest1");
		rqsv.requestChangeMentor(students.get(2), "checkRejectChangeRequest2");
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.rejectChangeRequest(creq.get(0));
		
		List<ChangeRequest> creqRefreshed = rqsv.getAllChangeRequests();
		List<Assignment> assignmentsRefreshed = agsv.getAllAssignments();
		
		assertEquals(1, creqRefreshed.size());
		assertEquals(3, assignments.size());
		assertEquals(3, assignmentsRefreshed.size());
		assertEquals("checkRejectChangeRequest2", creqRefreshed.get(0).getReason());
		assertEquals(assignments, assignmentsRefreshed);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRejectClosingRequest() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		boolean req1Success = rqsv.requestCloseCase(mentors.get(0), "checkRejectChangeRequest1", assignments.get(0));
		boolean req2Success = rqsv.requestCloseCase(mentors.get(1), "checkRejectChangeRequest2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		rqsv.rejectClosingRequest(creq.get(0));
		
		List<ClosingRequest> creqRefreshed = rqsv.getAllClosingRequests();
		List<Assignment> assignmentsRefreshed = agsv.getAllAssignments();
		
		assertEquals(1, creqRefreshed.size());
		assertEquals(3, assignments.size());
		assertEquals(3, assignmentsRefreshed.size());
		assertEquals("checkRejectChangeRequest2", creqRefreshed.get(0).getReason());
		assertEquals(assignments, assignmentsRefreshed);
	}
	
	@Test
	public void checkGetClosingRequestById() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		Assignment assignment = new Assignment(students.get(0), mentors.get(0));
		Assignment assignment2 = new Assignment(students.get(1), mentors.get(0));
		arep.save(assignment);
		arep.save(assignment2);
		
		ClosingRequest closingRequest = new ClosingRequest ("Grund", mentors.get(0), assignment);
		ClosingRequest closingRequest2 = new ClosingRequest ("Grund2", mentors.get(0), assignment2);
		closingRepo.save(closingRequest);
		closingRepo.save(closingRequest2);
		
		long id1 = closingRequest.getId();
		long id2 = closingRequest2.getId();
		
		assertEquals("Grund", rqsv.getClosingRequestById(id1).getReason());
		assertEquals("Grund2", rqsv.getClosingRequestById(id2).getReason());
	}
	
	@Test
	public void checkGetChangeRequestById() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		Assignment assignment = new Assignment(students.get(0), mentors.get(0));
		Assignment assignment2 = new Assignment(students.get(1), mentors.get(0));
		arep.save(assignment);
		arep.save(assignment2);
		
		ChangeRequest changeRequest = new ChangeRequest ("Grund", students.get(0), assignment);
		ChangeRequest changeRequest2 = new ChangeRequest ("Grund2", students.get(0), assignment2);
		changeRepo.save(changeRequest);
		changeRepo.save(changeRequest2);
		
		long id1 = changeRequest.getId();
		long id2 = changeRequest2.getId();
		
		assertEquals("Grund", rqsv.getChangeRequestById(id1).getReason());
		assertEquals("Grund2", rqsv.getChangeRequestById(id2).getReason());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkAcceptChangeRequestWhenBothSidesRequested() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		rqsv.requestChangeMentor(students.get(0), "checkAcceptChangeRequestWhenBothSidesRequested11");
		rqsv.requestChangeMentor(students.get(2), "checkAcceptChangeRequestWhenBothSidesRequested12");
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestCloseCase(mentors.get(0), "checkAcceptChangeRequestWhenBothSidesRequested21", assignments.get(0));
		rqsv.requestCloseCase(mentors.get(1), "checkAcceptChangeRequestWhenBothSidesRequested22", assignments.get(2));
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.acceptChangeRequest(creq.get(0));
		
		List<ChangeRequest> chreqRefreshed = rqsv.getAllChangeRequests();
		List<ClosingRequest> clreqRefreshed = rqsv.getAllClosingRequests();
		List<Assignment> assignmentsRefreshed = agsv.getAllAssignments();
		
		assertEquals(1, chreqRefreshed.size());
		assertEquals(1, clreqRefreshed.size());
		assertEquals(2, assignmentsRefreshed.size());
		assertEquals("checkAcceptChangeRequestWhenBothSidesRequested12", chreqRefreshed.get(0).getReason());
		assertEquals("checkAcceptChangeRequestWhenBothSidesRequested22", clreqRefreshed.get(0).getReason());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkAcceptChangeRequestWhenAppointmentIsSaved() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		rqsv.requestChangeMentor(students.get(0), "checkAcceptChangeRequestWhenAppointmentIsSaved1");
		rqsv.requestChangeMentor(students.get(2), "checkAcceptChangeRequestWhenAppointmentIsSaved2");
		
		
		// Add Appointments
		List<Assignment> assignments = agsv.getAllAssignments();
		LocalDateTime date = LocalDateTime.now();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropR.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		
		
		// Accept changeRequest
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.acceptChangeRequest(creq.get(0));
		
		List<Appointment> appointmentsRefreshed = (List<Appointment>)appointmentR.findAll();
		List<AppointmentProposal> appointmentPropsRefreshed = (List<AppointmentProposal>)appointmentPropR.findAll();
		assertEquals(1, appointmentsRefreshed.size());
		assertEquals(1, appointmentPropsRefreshed.size());
	}

}
