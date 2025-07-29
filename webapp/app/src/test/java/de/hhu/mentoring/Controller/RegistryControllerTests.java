package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import de.hhu.mentoring.controller.RegistryController;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.messaging.Messenger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistryControllerTests {
	
	private User s1 = new User("a","b","b@b,de","d",Role.STUDENT);
	private User m1 = new User("a","b","b@b,de","d",Role.MENTOR);
	private User o1 = new User("a","b","b@b,de","d",Role.ORGANIZER);
	private User s2 = new User("a","b","b@b.de","d",Role.STUDENT);
	private User m2 = new User("a","b","b@b.de","d",Role.MENTOR);
	private User o2 = new User("a","b","b@b.de","d",Role.ORGANIZER);

	@Mock
	Model model;
	@Mock
	Errors error;
	@Mock
	AccountService accService;
	@Mock 
	Messenger messenger;
	
	@InjectMocks
	RegistryController MockRegControl;
	
	RegistryController regControl= new RegistryController();
	
	@Rule public MockitoRule rule=MockitoJUnit.rule();
	
	@Test
	public void loadStudentTest() {
		assertEquals("Registrierung",regControl.displayRegistryStudent(model));
		Mockito.verify(model).addAttribute("user",new User());
		Mockito.verify(model).addAttribute("EmailVergeben", false);
		Mockito.verify(model).addAttribute("Title", "Registrierung Student");
		
	}
	
	@Test
	public void CreateStudentWrongModelTest() {
		Mockito.when(error.hasErrors()).thenReturn(true);
		assertEquals("Registrierung",regControl.createStudent(s1, error,model,""));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("EmailVergeben", false);
		Mockito.verify(model).addAttribute("Title", "Registrierung Student");
		
	}
	@Test
	public void CreateStudentCorrectModelAndNotExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(s2.getMailAddress())).thenReturn(false);
		assertEquals("Startsite",MockRegControl.createStudent(s2, error,model,""));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
	}
	@Test
	public void CreateStudentCorrectModelAndNotExistAndWithMessageTest() {
		List<User> allPrganizers= new ArrayList<>();
		allPrganizers.add(o1);
		Mockito.when(accService.getAllOrganizers()).thenReturn(allPrganizers);
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(s1.getMailAddress())).thenReturn(false);
		assertEquals("Registrierung",MockRegControl.createStudent(s1, error,model,"message"));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b,de");
		Mockito.verify(model).addAttribute("Kommas",true);
		Mockito.verify(model).addAttribute("student",true);
	
	}
	@Test
	public void CreateStudentCorrectModelAndNoUserExistAndWithMessagelaengerals255Test() {
		List<User> allPrganizers= new ArrayList<>();
		allPrganizers.add(o1);
		Mockito.when(accService.getAllOrganizers()).thenReturn(allPrganizers);
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(s2.getMailAddress())).thenReturn(false);
		String message="message";
		for(int i=0;i<260;i++) {
			message=message+"a";
		}
		assertEquals("Registrierung",MockRegControl.createStudent(s2, error,model,message));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
		Mockito.verify(model).addAttribute("student",true);
		Mockito.verify(model).addAttribute("zuLang",true);
	
	}
	@Test
	public void CreateStudentCorrectModelAndNotExistAndWithMessageTestAndNoKomma() {
		List<User> allPrganizers= new ArrayList<>();
		allPrganizers.add(o1);
		Mockito.when(accService.getAllOrganizers()).thenReturn(allPrganizers);
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(s2.getMailAddress())).thenReturn(false);
		assertEquals("Startsite",MockRegControl.createStudent(s2, error,model,"message"));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
	
	}
	@Test
	public void CreateStudentCorrectModelAndExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(s1.getMailAddress())).thenReturn(true);
		assertEquals("Registrierung",MockRegControl.createStudent(s1, error,model,""));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b,de");
		Mockito.verify(model).addAttribute("EmailVergeben", true);
		Mockito.verify(model).addAttribute("Title", "Registrierung Student");
	}
	@Test
	public void CreateStudentWrongMail() {
		User wrongStudent = new User("o","o","s,s@s.s","o",Role.STUDENT);
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(wrongStudent.getMailAddress())).thenReturn(false);
		assertEquals("Registrierung",MockRegControl.createStudent(wrongStudent, error,model,""));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("Kommas",true);

	}
	@Test
	public void loadMentorTest() {
		assertEquals("Registrierung",regControl.displayRegistryMentor(model));
		Mockito.verify(model).addAttribute("user",new User());
		Mockito.verify(model).addAttribute("EmailVergeben", false);
		Mockito.verify(model).addAttribute("Title", "Registrierung Mentor");
		
	}
	@Test
	public void CreateMentorWrongModelTest() {
		Mockito.when(error.hasErrors()).thenReturn(true);
		assertEquals("Registrierung",MockRegControl.createMentor(m1, error,model));
		Mockito.verify(error).hasErrors();
	}
	@Test
	public void CreateMentorCorrectModelAndNotExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(m2.getMailAddress())).thenReturn(false);
		assertEquals("Startsite",MockRegControl.createMentor(m2, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
	}
	@Test
	public void CreateMentorCorrectModelAndExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(m2.getMailAddress())).thenReturn(true);
		assertEquals("Registrierung",MockRegControl.createMentor(m2, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
		Mockito.verify(model).addAttribute("EmailVergeben",true);
		Mockito.verify(model).addAttribute("Title","Registrierung Mentor");
	}
	@Test
	public void CreateMentorWrongMail() {
		User wrongMentor = new User("o","o","m,m@m.m","o",Role.MENTOR);
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(wrongMentor.getMailAddress())).thenReturn(false);
		assertEquals("Registrierung",MockRegControl.createMentor(wrongMentor, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("Kommas",true);

	}
	@Test
	public void loadOrganizerTest() {
		assertEquals("Registrierung",regControl.displayRegistryOrganizer(model));
		Mockito.verify(model).addAttribute("user",new User());
		Mockito.verify(model).addAttribute("EmailVergeben", false);
		Mockito.verify(model).addAttribute("Title", "Registrierung Organisation");
	}
	@Test
	public void CreateOrganizerWrongModelTest() {
		Mockito.when(error.hasErrors()).thenReturn(true);
		assertEquals("Registrierung",MockRegControl.createOrganizer(o1, error,model));
		Mockito.verify(error).hasErrors();
	}
	
	@Test
	public void CreateOrganizerCorrectModelAndNotExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(o2.getMailAddress())).thenReturn(false);
		assertEquals("Startsite",MockRegControl.createOrganizer(o2, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b.de");
	
	}
	@Test
	public void CreateOrganizerCorrectModelAndExistTest() {
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(o1.getMailAddress())).thenReturn(true);
		assertEquals("Registrierung",MockRegControl.createOrganizer(o1, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accService).checkIfUserExists("b@b,de");
		Mockito.verify(model).addAttribute("EmailVergeben",true);
		Mockito.verify(model).addAttribute("Title","Registrierung Organisation");
		
	}
	
	@Test
	public void CreateOrganizerWrongMail() {
		User wrongOrganizer = new User("o","o","o,o@o.o","o",Role.ORGANIZER);
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accService.checkIfUserExists(wrongOrganizer.getMailAddress())).thenReturn(false);
		assertEquals("Registrierung",MockRegControl.createOrganizer(wrongOrganizer, error,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("Kommas",true);

	}

}
