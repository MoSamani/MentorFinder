package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
import de.hhu.mentoring.database.model.Message;
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
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.messaging.ReminderService;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReminderServiceTests {
	
	@Autowired
	ReminderService resv;
	
	@Autowired
	RequestService rqsv;
	
	@Autowired
	AccountService acsv;
	
	@Autowired
	AssignmentService agsv;
	
	@Autowired
	Messenger msg;
	
	@Autowired
	AppointmentService apsv;
	
	@Autowired
	UserRepository urep;
	
	@Autowired
	MessageRepository mrep;
	
	@Autowired
	AssignmentRepository arep;
	
	@Autowired
	ChangeRequestRepository changeRepo;
	
	@Autowired
	ClosingRequestRepository closingRepo;
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentProposalRepository appointmentPRepo;
	
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
	AppointmentRepository apR;
	
	@Before
	public void setupRoutine() {
		User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
		User s2 = new User("Paul","Pluto","p@p.de","pw",Role.STUDENT);
		User s3 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
		User s4 = new User("Niklas","Neptun","n@n.de","pw",Role.STUDENT);
		User s5 = new User("Jens","Jupiter","j@j.de","pw",Role.STUDENT);
		
		User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
		User m2 = new User("Erwin","Europa","a@a.de","pw",Role.MENTOR);
		
		User o1 = new User("Alex2","Afrika2","a@a2.de","pw2",Role.ORGANIZER);
		User o2 = new User("Alex3","Afrika3","a@a3.de","pw3",Role.ORGANIZER);
		
		acsv.save(s1);
		acsv.save(s2);
		acsv.save(s3);
		acsv.save(s4);
		acsv.save(s5);
		
		acsv.save(m1);
		acsv.save(m2);
		
		acsv.save(o1);
		acsv.save(o2);
	}
	
	@After
	public void finishRoutine() {
		changeRepo.deleteAll();
		closingRepo.deleteAll();
		appointmentRepo.deleteAll();
		appointmentPRepo.deleteAll();
		mrep.deleteAll();
		fR.deleteAll();
		aR.deleteAll();
		dR.deleteAll();
		nR.deleteAll();
		cR.deleteAll();
		urep.deleteAll();
		arep.deleteAll();
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfNewAssignmentRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesS3 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(6, allMessages.size());
		assertEquals(1, messagesS1.size());
		assertEquals(1, messagesS2.size());
		assertEquals(1, messagesS3.size());
		assertEquals(2, messagesM1.size());
		assertEquals(1, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAssignmentDeletedByOrganizerRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		agsv.removeAssignmentByOrganizer(assignments.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesS3 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(8, allMessages.size());
		assertEquals(2, messagesS1.size());
		assertEquals(1, messagesS2.size());
		assertEquals(1, messagesS3.size());
		assertEquals(3, messagesM1.size());
		assertEquals(1, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfChangeRequestEnteredRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		rqsv.requestChangeMentor(students.get(0), "checkIfChangeRequestEnteredRemindersAreSent1");
		rqsv.requestChangeMentor(students.get(2), "checkIfChangeRequestEnteredRemindersAreSent2");
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(10, allMessages.size());
		assertEquals(2, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(3, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfChangeRequestAcceptedRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		rqsv.requestChangeMentor(students.get(0), "checkIfChangeRequestAcceptedRemindersAreSent1");
		rqsv.requestChangeMentor(students.get(2), "checkIfChangeRequestAcceptedRemindersAreSent2");
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.acceptChangeRequest(creq.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages = (List<Message>)mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(12, allMessages.size());
		assertEquals(3, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(4, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfChangeRequestRejectedRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		rqsv.requestChangeMentor(students.get(0), "checkIfChangeRequestRejectedRemindersAreSent1");
		rqsv.requestChangeMentor(students.get(2), "checkIfChangeRequestRejectedRemindersAreSent2");
		
		List<ChangeRequest> creq = rqsv.getAllChangeRequests();
		rqsv.rejectChangeRequest(creq.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages = (List<Message>)mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(12, allMessages.size());
		assertEquals(3, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(4, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfClosingRequestEnteredRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestCloseCase(mentors.get(0), "checkIfClosingRequestEnteredRemindersAreSent1", assignments.get(0));
		rqsv.requestCloseCase(mentors.get(1), "checkIfClosingRequestEnteredRemindersAreSent2", assignments.get(2));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(10, allMessages.size());
		assertEquals(2, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(3, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfClosingRequestAcceptedRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestCloseCase(mentors.get(0), "checkIfClosingRequestAcceptedRemindersAreSent1", assignments.get(0));
		rqsv.requestCloseCase(mentors.get(1), "checkIfClosingRequestAcceptedRemindersAreSent2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		rqsv.acceptClosingRequest(creq.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(12, allMessages.size());
		assertEquals(3, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(4, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfClosingRequestRejectedRemindersAreSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		rqsv.requestCloseCase(mentors.get(0), "checkIfClosingRequestRejectedRemindersAreSent1", assignments.get(0));
		rqsv.requestCloseCase(mentors.get(1), "checkIfClosingRequestRejectedRemindersAreSent2", assignments.get(2));
		
		List<ClosingRequest> creq = rqsv.getAllClosingRequests();
		rqsv.rejectClosingRequest(creq.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(2));
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> messagesM2 = msg.getAllReceivedMessages(mentors.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(12, allMessages.size());
		assertEquals(3, messagesS1.size());
		assertEquals(2, messagesS2.size());
		assertEquals(4, messagesM1.size());
		assertEquals(2, messagesM2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAppointmentCanceledByStudentReminderWasSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		
		List<Appointment> appointments = apsv.getAllAppointmentsByAssignment(assignments.get(0));
		apsv.cancelAppointmentStudent(appointments.get(0));
		
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(9, allMessages.size());
		assertEquals(4, messagesM1.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAppointmentCanceledByMentorReminderWasSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		
		List<Appointment> appointments = apsv.getAllAppointmentsByAssignment(assignments.get(0));
		apsv.cancelAppointmentMentor(appointments.get(0));
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(9, allMessages.size());
		assertEquals(3, messagesS1.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAppointmentProposalSubmittedReminderWasSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(1));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(8, allMessages.size());
		assertEquals(2, messagesS1.size());
		assertEquals(2, messagesS2.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAppointmentProposalAcceptedReminderWasSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(8, allMessages.size());
		assertEquals(3, messagesM1.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkIfAppointmentProposalRejectedReminderWasSent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.rejectAppointmentProposal(appointmentProposals.get(0));
		
		List<Message> messagesM1 = msg.getAllReceivedMessages(mentors.get(0));
		List<Message> allMessages =(List<Message>) mrep.findAll();
		
		// Check if correct amount of messages were sent
		assertEquals(8, allMessages.size());
		assertEquals(3, messagesM1.size());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAppointmentReminderForMentor() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime appointmentDate = LocalDateTime.of(2017, Month.JULY, 29, 19, 30, 40);
		LocalDateTime reminderDate = appointmentDate.minusDays(1l);
		
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), LocalDateTime.now(), LocalDateTime.now(), appointmentDate);
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), appointmentDate);
		
		List<Appointment> appointments = apsv.getAllAppointmentsByAssignment(assignments.get(0));
		Message reminder = resv.getAppointmentReminder(appointments.get(0), mentors.get(0), reminderDate);
		
		
		// Check if correct reminder message was generated
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		assertEquals(acsv.getReminderUser(), reminder.getSender());
		assertEquals(mentors.get(0), reminder.getReceiver());
		assertEquals(reminderDate, reminder.getDate());
		assertEquals("Erinnerung an ihren nächsten Termin", reminder.getTitle());
		assertEquals("Ihr nächster Termin findet am "+appointmentDate.format(formatter)+" mit Ihrem Studenten "+students.get(0).getPrename()+" "+students.get(0).getSurname()+" statt.", reminder.getContent());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAppointmentReminderForStudent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime appointmentDate = LocalDateTime.now();
		LocalDateTime reminderDate = appointmentDate.minusDays(1l);
		
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), appointmentDate, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), appointmentDate);
		
		List<Appointment> appointments = apsv.getAllAppointmentsByAssignment(assignments.get(0));
		Message reminder = resv.getAppointmentReminder(appointments.get(0), students.get(0), reminderDate);
		
		
		// Check if correct reminder message was generated
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		assertEquals(acsv.getReminderUser(), reminder.getSender());
		assertEquals(students.get(0), reminder.getReceiver());
		assertEquals(reminderDate, reminder.getDate());
		assertEquals("Erinnerung an ihren nächsten Termin", reminder.getTitle());
		assertEquals("Ihr nächster Termin findet am "+appointmentDate.format(formatter)+" mit Ihrem Mentor "+mentors.get(0).getPrename()+" "+mentors.get(0).getSurname()+" statt.", reminder.getContent());
		
	}
	
	@Test
	public void checkSendStudentSuggestion() {
		resv.sendStudentSuggestion("TestVorname", "TestNachname");
		
		List<User> organizers = acsv.getAllOrganizers();
		
		assertEquals(1, msg.getAllReceivedMessages(organizers.get(0)).size());
		assertEquals(1, msg.getAllReceivedMessages(organizers.get(1)).size());
	}
	

}
