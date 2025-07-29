package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.Message;
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
import de.hhu.mentoring.services.messaging.Messenger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagingTests {
	
	@Autowired
	UserRepository urep;
	
	@Autowired
	MessageRepository mrep;
	
	@Autowired
	Messenger msg;
	
	@Autowired
	AccountService acsv;
	
	@Autowired
	AssignmentService agsv;
	
	@Autowired
	AppointmentService apsv;
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentProposalRepository appointmentPropRepo;
	
	@Autowired
	FileRepository fRepo;
	
	@Autowired
	AssignmentRepository arep;
	
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
		User sender = new User("a","b","a@b.de","pw",Role.STUDENT);
		User receiver = new User("c","d","c@d.de","pw",Role.STUDENT);
		User u3 = new User("e","f","e@f","pw",Role.STUDENT);
		User u4 = new User("g","h","g@h","pw",Role.MENTOR);
		User u5 = new User("i","j","i@j","pw",Role.MENTOR);
		acsv.save(sender);
		acsv.save(receiver);
		acsv.save(u3);
		acsv.save(u4);
		acsv.save(u5);
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
		urep.deleteAll();
		arep.deleteAll();
	}
	
	
	@Test
	public void checkReceivedMessage() {
		
		List<User> userlist = acsv.getAllUsers();
		msg.send(userlist.get(1), userlist.get(0), "checkReceivedMessage", "content");
		List<Message> message = msg.getAllReceivedMessages(userlist.get(0));
		assertEquals(message.get(0).getTitle(), "checkReceivedMessage");
	}
	
	@Test
	public void checkSentMessage() {
		
		List<User> userlist = acsv.getAllUsers();
		msg.send(userlist.get(1), userlist.get(0), "checkSentMessage", "content");
		List<Message> message = msg.getAllSentMessages(userlist.get(1));
		assertEquals(message.get(0).getTitle(), "checkSentMessage");
	}

	@Test
	public void checkGetMessageById() {
		
		List<User> userlist = acsv.getAllUsers();
		msg.send(userlist.get(1), userlist.get(0), "checkGetMessageById1", "content");
		msg.send(userlist.get(0), userlist.get(1), "checkGetMessageById2", "content2");
		
		List<Message> mlist = (List<Message>)mrep.findAll();
		
		Message message1 = msg.getMessageById(mlist.get(0).getId());
		assertEquals(message1.getTitle(), "checkGetMessageById1");

		Message message2 = msg.getMessageById(mlist.get(1).getId());
		assertEquals(message2.getTitle(), "checkGetMessageById2");
	}
	
	@Test
	public void checkMessageDate() {
		
		List<User> userlist = acsv.getAllUsers();
		LocalDateTime sentTime = LocalDateTime.now();
		msg.send(userlist.get(1), userlist.get(0), "checkMessageDate", "content");
		
		List<Message> mlist = msg.getAllReceivedMessages(userlist.get(0));
		Message m = mlist.get(0);
		
		assertEquals(m.getDate().getYear(), sentTime.getYear());
		assertEquals(m.getDate().getMonth(), sentTime.getMonth());
		assertEquals(m.getDate().getDayOfYear(), sentTime.getDayOfYear());
		assertEquals(m.getDate().getHour(), sentTime.getHour());
		assertEquals(m.getDate().getMinute(), sentTime.getMinute());
	}
	
	@Test
	public void checkReceivedMessagesByMail() {
		
		List<User> userlist = acsv.getAllUsers();
		
		msg.send(userlist.get(0), userlist.get(1), "checkReceivedMessagesByMail1", "content");
		msg.send(userlist.get(0), userlist.get(1), "checkReceivedMessagesByMail2", "content");
		msg.send(userlist.get(1), userlist.get(0), "checkReceivedMessagesByMail3", "content");
		msg.send(userlist.get(1), userlist.get(0), "checkReceivedMessagesByMail4", "content");
		
		List<Message> mlist1 = msg.getAllReceivedMessagesByMailAddress(userlist.get(1).getMailAddress());
		
		assertEquals(mlist1.get(1).getTitle(), "checkReceivedMessagesByMail1");
		assertEquals(mlist1.get(0).getTitle(), "checkReceivedMessagesByMail2");
		
		List<Message> mlist2 = msg.getAllReceivedMessagesByMailAddress(userlist.get(0).getMailAddress());
		assertEquals(mlist2.get(1).getTitle(), "checkReceivedMessagesByMail3");
		assertEquals(mlist2.get(0).getTitle(), "checkReceivedMessagesByMail4");
	}
	
	@Test
	public void checkSentMessagesByMail() {
		
		List<User> userlist = acsv.getAllUsers();
		
		msg.send(userlist.get(0), userlist.get(1), "checkSentMessagesByMail1", "content");
		msg.send(userlist.get(0), userlist.get(1), "checkSentMessagesByMail2", "content");
		msg.send(userlist.get(1), userlist.get(0), "checkSentMessagesByMail3", "content");
		msg.send(userlist.get(1), userlist.get(0), "checkSentMessagesByMail4", "content");
		
		List<Message> mlist1 = msg.getAllSentMessagesByMailAddress(userlist.get(0).getMailAddress());
		
		assertEquals(mlist1.get(1).getTitle(), "checkSentMessagesByMail1");
		assertEquals(mlist1.get(0).getTitle(), "checkSentMessagesByMail2");
		
		List<Message> mlist2 = msg.getAllSentMessagesByMailAddress(userlist.get(1).getMailAddress());
		assertEquals(mlist2.get(1).getTitle(), "checkSentMessagesByMail3");
		assertEquals(mlist2.get(0).getTitle(), "checkSentMessagesByMail4");
	}
	
	@Test
	public void checkSendingReminder() {
		
		List<User> userlist = acsv.getAllUsers();
		msg.sendReminder(userlist.get(0), "checkSendingReminder", "content");
		
		List<Message> message = msg.getAllReceivedMessages(userlist.get(0));
		assertEquals("checkSendingReminder", message.get(0).getTitle());
		assertEquals(acsv.getReminderUser(), message.get(0).getSender());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void checkGetReceivedMessagesWithAppointmentReminder() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		
		List<User> students = acsv.getAllStudents();
		List<User> mentors = acsv.getAllMentors();
		
		boolean as1Success = agsv.assignMentorToStudent(students.get(0), mentors.get(0));
		boolean as2Success = agsv.assignMentorToStudent(students.get(1), mentors.get(0));
		boolean as3Success = agsv.assignMentorToStudent(students.get(2), mentors.get(1));
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateLessThanOneDayFromNow = now.plusDays(1).minusMinutes(1);
		LocalDateTime dateMoreThanOneDayFromNow = now.plusDays(1).plusMinutes(1);
		LocalDateTime dateInThePast = now.minusDays(1);
		
		// 3 Appointments should invoke a reminder
		// 2 should not because it's too early
		// 1 should not because it's too late
		List<Assignment> assignments = agsv.getAllAssignments();
		apsv.makeAppointmentProposal(assignments.get(0), dateLessThanOneDayFromNow, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), dateMoreThanOneDayFromNow, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), dateLessThanOneDayFromNow, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(1), dateLessThanOneDayFromNow, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), dateMoreThanOneDayFromNow, LocalDateTime.now(), LocalDateTime.now());
		apsv.makeAppointmentProposal(assignments.get(2), dateInThePast, LocalDateTime.now(),LocalDateTime.now());
		
		List<AppointmentProposal> appointmentProposals = (List<AppointmentProposal>)appointmentPropRepo.findAll();
		apsv.acceptAppointmentProposal(appointmentProposals.get(0), dateLessThanOneDayFromNow);
		apsv.acceptAppointmentProposal(appointmentProposals.get(1), dateMoreThanOneDayFromNow);
		apsv.acceptAppointmentProposal(appointmentProposals.get(2), dateLessThanOneDayFromNow);
		apsv.acceptAppointmentProposal(appointmentProposals.get(3), dateLessThanOneDayFromNow);
		apsv.acceptAppointmentProposal(appointmentProposals.get(4), dateMoreThanOneDayFromNow);
		apsv.acceptAppointmentProposal(appointmentProposals.get(5), dateInThePast);
		
		List<Message> messagesS1 = msg.getAllReceivedMessages(students.get(0));
		List<Message> messagesS2 = msg.getAllReceivedMessages(students.get(1));
		List<Message> messagesS3 = msg.getAllReceivedMessages(students.get(2));
		
		// Check if correct amount of appointment reminders are displayed
		assertEquals(3, messagesS1.size());
		assertEquals(6, messagesS2.size());
		assertEquals(3, messagesS3.size());
		
		// Check if those messages are the reminders
		assertEquals("Erinnerung an ihren nächsten Termin", messagesS1.get(0).getTitle());
		assertEquals("Erinnerung an ihren nächsten Termin", messagesS2.get(0).getTitle());
		assertEquals("Erinnerung an ihren nächsten Termin", messagesS2.get(1).getTitle());
		
	}
}
