package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseControllerTests {
	
	
	@Rule public MockitoRule rule=MockitoJUnit.rule();
	@Autowired
	public UserRepository uR;
	
	@Autowired
	MessageRepository mrep;
	
	@Autowired
	AssignmentRepository arep;
	
	@Autowired
	ChangeRequestRepository changeRepo;
	
	@Autowired
	ClosingRequestRepository closingRepo;
	
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
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentProposalRepository appointmentPropRepo;
	
	@Before
	public void startRoutine() {
		User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
		User s2 = new User("Paul","Pluto","p@p.de","pw",Role.STUDENT);
		User s3 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
		User s4 = new User("Niklas","Neptun","n@n.de","pw",Role.MENTOR);
		User s5 = new User("Jens","Jupiter","j@j.de","pw",Role.MENTOR);
		User s6 = new User("Eclipse","Erde","e@e.de","pw",Role.MENTOR);
		User s7 = new User("Ulrich","Uranus","u@u.de","pw",Role.ORGANIZER);
		
		aS.save(s1);
		aS.save(s2);
		aS.save(s3);
		aS.save(s4);
		aS.save(s5);
		aS.save(s6);
		aS.save(s7);
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
		changeRepo.deleteAll();
		closingRepo.deleteAll();
	}
	
	
	@Test
	public void DatenbankCounterTest() {
		assertEquals(uR.count(),7);
	}
	@Test
	public void DatenbankCounterStudentTest() {
		assertEquals(aS.getAllStudents().size(),3);
	}
	
	@Test
	public void DatenbankCounterMentorTest() {
		assertEquals(aS.getAllMentors().size(),3);
	}
	
	@Test
	public void DatenbankCounterOrganizerTest() {
		assertEquals(aS.getAllOrganizers().size(),1);
	}
	
	@Test
	public void DatenbankDeleteUserTest() {
		fR.deleteAll();
		uR.delete(aS.getUserByMailAddress("m@m.de"));
		assertEquals(uR.count(),6);
	}
	
	@Test
	public void findUserByPrenameAndSurnameTest() {
		assertEquals("m@m.de",uR.findUserByPrenameAndSurname("Martin", "Mars").getMailAddress());
	}
	@Test
	public void findUserByMailAddressTest() {
		assertEquals(uR.findUserByPrenameAndSurname("Sebastian", "Saturn"), uR.findUserByMailAddress("s@s.de"));
	}
	
}
