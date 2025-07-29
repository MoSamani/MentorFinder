package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AgreementRepository;
import de.hhu.mentoring.database.repository.AppointmentRepository;
import de.hhu.mentoring.database.repository.ConversationLogRepository;
import de.hhu.mentoring.database.repository.DocumentRepository;
import de.hhu.mentoring.database.repository.FileRepository;
import de.hhu.mentoring.database.repository.MessageRepository;
import de.hhu.mentoring.database.repository.NoteRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.messaging.Messenger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTests {
	
	@Mock
	Model model;
	@Mock
	Errors error;
	@Rule public MockitoRule rule=MockitoJUnit.rule();

	@Autowired
	Messenger msg;
	
	@Autowired
	AccountService accs;
	
	@Autowired
	public UserRepository uR;
	
	@Autowired
	public MessageRepository mR;

	@Autowired
	public AccountService aS;
	
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
	public void startRoutine() {
		User s= new User("A", "B", "ab@email.com", "abcd", Role.ORGANIZER);
		User s1= new User("A2", "B2", "ab2@email.com", "abcd2", Role.STUDENT);
		User s2= new User("A2", "B2", "ab2@email.com", "abcd2", Role.STUDENT);
		User s3= new User("A3", "B3", "ab3@email.com", "abcd3", Role.MENTOR);
		User s4= new User("A3", "B3", "ab3@email.com", "abcd3", Role.MENTOR);
		aS.save(s);
		aS.save(s1);
		aS.save(s2);
		aS.save(s3);
		aS.save(s4);
	}
	@After
	public void finishRoutine() {
		fR.deleteAll();
		aR.deleteAll();
		dR.deleteAll();
		nR.deleteAll();
		cR.deleteAll();
		uR.deleteAll();
		apR.deleteAll();
	}

	
	@Test
	public void getUsersByRoleStudentTest() {
		int size = aS.getUsersByRole(Role.STUDENT).size();
		assertEquals(size,2);
	}
	
	@Test
	public void getUsersByRoleMentorTest() {
		int size = aS.getUsersByRole(Role.MENTOR).size();
		assertEquals(size, 2);
	}
	
	@Test
	public void getUsersByRoleOrganizerTest() {
		int size = aS.getUsersByRole(Role.ORGANIZER).size();
		assertEquals(size,1);
	}
	
	@Test
	public void getAllStudentsTest() {
		int size = aS.getAllStudents().size();
		assertEquals(size,2);
	}
	
	@Test
	public void getAllMentorsTest() {
		int size = aS.getAllMentors().size();
		assertEquals(size,2);
	}
	
	@Test
	public void getAllOrganizersTest() {
		int size = aS.getAllOrganizers().size();
		assertEquals(size,1);
	}
	
	@Test
	public void checkIfUserExistsNameTrueTest() {
		assertEquals(aS.checkIfUserExists("A", "B"), true);
	}
	
	@Test
	public void checkIfUserExistsNameFalseTest() {
		assertEquals(aS.checkIfUserExists("A", "C"), false);
	}
	
	@Test
	public void checkIfUserExistsMailTrueTest() {
		assertEquals(aS.checkIfUserExists("ab@email.com"), true);
	}
	
	@Test
	public void checkIfUserExistsMailFalseTest() {
		assertEquals(aS.checkIfUserExists("ac@email.com"), false);
	}
	
	@Test
	public void getUsersByEmailAdressTest() {
		User s5= new User("A", "B", "ab5@email.com", "abcd", Role.STUDENT);
		aS.save(s5);
		User user = aS.getUserByMailAddress("ab5@email.com");
		assertEquals(s5,user);

	}
	
	@Test
	public void checkIfReminderUserIsCreated() {
		User reminder = aS.getReminderUser();
		List<User> users = aS.getAllUsers();
		assertEquals("reminder.mentoring", reminder.getMailAddress());
		assertEquals(Role.ORGANIZER, reminder.getRole());
		assertEquals(6, users.size());
		assertEquals(reminder, users.get(5));
		

	}
	
	@Test
	public void checkIfReminderUserIsCreatedTwice() {
		User reminder = aS.getReminderUser();
		reminder = aS.getReminderUser();
		List<User> users = aS.getAllUsers();
		assertEquals("reminder.mentoring", reminder.getMailAddress());
		assertEquals(Role.ORGANIZER, reminder.getRole());
		assertEquals(6, users.size());
		assertEquals(reminder, users.get(5));
		

	}
}
