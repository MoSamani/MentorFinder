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
import de.hhu.mentoring.database.model.ConversationLog;
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
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceTests {
	
	@Autowired
	AccountService acsv;
	
	@Autowired
	AssignmentService agsv;
	
	@Autowired
	AppointmentService apsv;
	
	@Autowired
	UserRepository uR;
	
	@Autowired
	MessageRepository mrep;
	
	@Autowired
	AssignmentRepository arep;
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentProposalRepository appointmentPropRepo;
	
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

	@Before
	public void setupRoutine() {
		User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
		User s2 = new User("Paul","Pluto","p@p.de","pw",Role.STUDENT);
		User s3 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
		User s4 = new User("Niklas","Neptun","n@n.de","pw",Role.STUDENT);
		User s5 = new User("Jens","Jupiter","j@j.de","pw",Role.STUDENT);
		
		User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
		User m2 = new User("Erwin","Europa","a@a.de","pw",Role.MENTOR);
		
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
	
	
	@SuppressWarnings("unused")
	@Test
	public void checkMakeAppointmentProposal() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		
		// Check if correct amount of appointment proposals are saved
		assertEquals(4, appointmentProposals.size());
		assertEquals(assignments.get(0), appointmentProposals.get(0).getAssignment());
		assertEquals(assignments.get(0), appointmentProposals.get(1).getAssignment());
		assertEquals(assignments.get(1), appointmentProposals.get(2).getAssignment());
		assertEquals(assignments.get(2), appointmentProposals.get(3).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkAcceptAppointmentProposal() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		LocalDateTime date = LocalDateTime.now();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		
		List<AppointmentProposal> appointmentProposalsRefreshed = (List<AppointmentProposal>)appointmentPropRepo.findAll();;
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointment proposals are accepted
		assertEquals(2, appointmentProposalsRefreshed.size());
		assertEquals(2, appointments.size());
		assertEquals(assignments.get(0), appointmentProposalsRefreshed.get(0).getAssignment());
		assertEquals(assignments.get(2), appointmentProposalsRefreshed.get(1).getAssignment());
		assertEquals(assignments.get(0), appointments.get(0).getAssignment());
		assertEquals(assignments.get(1), appointments.get(1).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkRejectAppointmentProposal() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.rejectAppointmentProposal(appointmentProposals.get(2));
		
		List<AppointmentProposal> appointmentProposalsRefreshed = (List<AppointmentProposal>)appointmentPropRepo.findAll();;
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointments proposals are rejected
		assertEquals(2, appointmentProposalsRefreshed.size());
		assertEquals(1, appointments.size());
		assertEquals(assignments.get(0), appointmentProposalsRefreshed.get(0).getAssignment());
		assertEquals(assignments.get(2), appointmentProposalsRefreshed.get(1).getAssignment());
		assertEquals(assignments.get(0), appointments.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkCancelAppointmentStudent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		apsv.cancelAppointmentStudent(appointments.get(1));
		
		List<AppointmentProposal> appointmentProposalsRefreshed = (List<AppointmentProposal>)appointmentPropRepo.findAll();;
		List<Appointment> appointmentsRefreshed = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointments are canceled
		assertEquals(2, appointmentProposalsRefreshed.size());
		assertEquals(2, appointments.size());
		assertEquals(false, appointments.get(0).isCanceled());
		assertEquals(true, appointments.get(1).isCanceled());
		assertEquals(assignments.get(0), appointmentProposalsRefreshed.get(0).getAssignment());
		assertEquals(assignments.get(2), appointmentProposalsRefreshed.get(1).getAssignment());
		assertEquals(assignments.get(0), appointments.get(0).getAssignment());
		assertEquals(assignments.get(1), appointments.get(1).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkCancelAppointmentMentor() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		apsv.cancelAppointmentMentor(appointments.get(1));
		
		List<AppointmentProposal> appointmentProposalsRefreshed = (List<AppointmentProposal>)appointmentPropRepo.findAll();;
		List<Appointment> appointmentsRefreshed = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointments are canceled
		assertEquals(2, appointmentProposalsRefreshed.size());
		assertEquals(2, appointments.size());
		assertEquals(false, appointments.get(0).isCanceled());
		assertEquals(true, appointments.get(1).isCanceled());
		assertEquals(assignments.get(0), appointmentProposalsRefreshed.get(0).getAssignment());
		assertEquals(assignments.get(2), appointmentProposalsRefreshed.get(1).getAssignment());
		assertEquals(assignments.get(0), appointments.get(0).getAssignment());
		assertEquals(assignments.get(1), appointments.get(1).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAppointmentProposalsByStudent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentPsS1 = apsv.getAllAppointmentProposalsByStudent(students.get(0));
		List<AppointmentProposal> appointmentPsS2 = apsv.getAllAppointmentProposalsByStudent(students.get(1));
		List<AppointmentProposal> appointmentPsS3 = apsv.getAllAppointmentProposalsByStudent(students.get(2));
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		
		// Check if correct amount of appointment proposals are loaded
		assertEquals(4, appointmentProposals.size());
		assertEquals(1, appointmentPsS1.size());
		assertEquals(2, appointmentPsS2.size());
		assertEquals(1, appointmentPsS3.size());
		assertEquals(assignments.get(0), appointmentPsS1.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentPsS2.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentPsS2.get(1).getAssignment());
		assertEquals(assignments.get(2), appointmentPsS3.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAppointmentsByStudent() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(1), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(3), date);
		
		List<Appointment> appointmentsS1 = apsv.getAllAppointmentsByStudent(students.get(0));
		List<Appointment> appointmentsS2 = apsv.getAllAppointmentsByStudent(students.get(1));
		List<Appointment> appointmentsS3 = apsv.getAllAppointmentsByStudent(students.get(2));
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointment proposals are loaded
		assertEquals(4, appointments.size());
		assertEquals(1, appointmentsS1.size());
		assertEquals(2, appointmentsS2.size());
		assertEquals(1, appointmentsS3.size());
		assertEquals(assignments.get(0), appointmentsS1.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentsS2.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentsS2.get(1).getAssignment());
		assertEquals(assignments.get(2), appointmentsS3.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAppointmentsByMentor() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(1), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(3), date);
		
		List<Appointment> appointmentsM1 = apsv.getAllAppointmentsByMentor(mentors.get(0));
		List<Appointment> appointmentsM2 = apsv.getAllAppointmentsByMentor(mentors.get(1));
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointment proposals are loaded
		assertEquals(4, appointments.size());
		assertEquals(3, appointmentsM1.size());
		assertEquals(1, appointmentsM2.size());
		assertEquals(assignments.get(0), appointmentsM1.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentsM1.get(1).getAssignment());
		assertEquals(assignments.get(1), appointmentsM1.get(2).getAssignment());
		assertEquals(assignments.get(2), appointmentsM2.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAppointmentProposalsByAssignment() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentPsA1 = apsv.getAllAppointmentProposalsByAssignment(assignments.get(0));
		List<AppointmentProposal> appointmentPsA2 = apsv.getAllAppointmentProposalsByAssignment(assignments.get(1));
		List<AppointmentProposal> appointmentPsA3 = apsv.getAllAppointmentProposalsByAssignment(assignments.get(2));
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		
		// Check if correct amount of appointment proposals are loaded
		assertEquals(4, appointmentProposals.size());
		assertEquals(1, appointmentPsA1.size());
		assertEquals(2, appointmentPsA2.size());
		assertEquals(1, appointmentPsA3.size());
		assertEquals(assignments.get(0), appointmentPsA1.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentPsA2.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentPsA2.get(1).getAssignment());
		assertEquals(assignments.get(2), appointmentPsA3.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetAllAppointmentsByAssignments() {
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime date = LocalDateTime.now();
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), date, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), date, LocalDateTime.now(), LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(1), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), date);
		apsv.acceptAppointmentProposal(appointmentProposals.get(3), date);
		
		List<Appointment> appointmentsA1 = apsv.getAllAppointmentsByAssignment(assignments.get(0));
		List<Appointment> appointmentsA2 = apsv.getAllAppointmentsByAssignment(assignments.get(1));
		List<Appointment> appointmentsA3 = apsv.getAllAppointmentsByAssignment(assignments.get(2));
		List<Appointment> appointments = (List<Appointment>)appointmentRepo.findAll();
		
		// Check if correct amount of appointment proposals are loaded
		assertEquals(4, appointments.size());
		assertEquals(1, appointmentsA1.size());
		assertEquals(2, appointmentsA2.size());
		assertEquals(1, appointmentsA3.size());
		assertEquals(assignments.get(0), appointmentsA1.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentsA2.get(0).getAssignment());
		assertEquals(assignments.get(1), appointmentsA2.get(1).getAssignment());
		assertEquals(assignments.get(2), appointmentsA3.get(0).getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetAppointmentById() {
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0),appointmentProposals.get(0).getDate1());
		apsv.acceptAppointmentProposal(appointmentProposals.get(1),appointmentProposals.get(0).getDate1());
		apsv.acceptAppointmentProposal(appointmentProposals.get(2),appointmentProposals.get(0).getDate1());

		List<Appointment> as = (List<Appointment>)appointmentRepo.findAll();
		Appointment a1 = apsv.getAppointmentById(as.get(0).getId());
		Appointment a2 = apsv.getAppointmentById(as.get(1).getId());
		Appointment a3 = apsv.getAppointmentById(as.get(2).getId());
		
		assertEquals(assignments.get(0), a1.getAssignment());
		assertEquals(assignments.get(1), a2.getAssignment());
		assertEquals(assignments.get(1), a3.getAssignment());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetAppointmentProposalById() {
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now());
		
		List<AppointmentProposal> aps = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		AppointmentProposal ap1 = apsv.getAppointmentProposalById(aps.get(0).getId());
		AppointmentProposal ap2 = apsv.getAppointmentProposalById(aps.get(1).getId());
		AppointmentProposal ap3 = apsv.getAppointmentProposalById(aps.get(2).getId());
		
		assertEquals(assignments.get(0), ap1.getAssignment());
		assertEquals(assignments.get(1), ap2.getAssignment());
		assertEquals(assignments.get(1), ap3.getAssignment());
	}
	
	//ConversationLog
	
	@Test
	public void testMakeConversationLog() {
		apsv.makeConversationLog(appointmentRepo.findOne(0l), "etwas");
		assertEquals("etwas", apsv.getConversationLogByAppointment(appointmentRepo.findOne(0l)).getText());
	}
	
	@Test
	public void testGetConversationLogById() {
		apsv.makeConversationLog(appointmentRepo.findOne(0l), "etwas");
		long cLId = apsv.getConversationLogByAppointment(appointmentRepo.findOne(0l)).getId();
		assertEquals(apsv.getConversationLogByAppointment(appointmentRepo.findOne(0l)), apsv.getConversationLogById(cLId));
	}
	
	@Test
	public void testGetConversationLogByAppointment() {
		apsv.makeConversationLog(appointmentRepo.findOne(0l), "etwas");
		ConversationLog cL = cR.findConversationLogByAppointment(appointmentRepo.findOne(0l));
		assertEquals(cL.getText(), apsv.getConversationLogByAppointment(appointmentRepo.findOne(0l)).getText());
	}
		
}
