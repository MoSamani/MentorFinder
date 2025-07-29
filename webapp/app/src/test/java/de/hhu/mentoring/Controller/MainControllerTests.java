package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import de.hhu.mentoring.controller.MainController;
import de.hhu.mentoring.database.repository.MessageRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.messaging.ReminderService;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainControllerTests {

	@Mock
	Model model;
	@Mock
	Errors error;
	@Mock
	UserRepository uRepo;
	@Mock
	MessageRepository messageRepo;
	@Mock
	ReminderService reminderService;
	@Mock
	GrantedAuthority grantedAuthority;
	@InjectMocks
	MainController re;
	@Rule public MockitoRule rule=MockitoJUnit.rule();
	
	
	@InjectMocks
	MainController mainController;
	
	public UserRepository u;
	
	@Test
	public void displayStartsiteTest() {
		assertEquals("Startsite" , mainController.displayStartsite());
	}
	@Test
	public void loginTest() {
		assertEquals("login" , mainController.login());
	}
	@Test
	public void loginErrorTest() {
		assertEquals("login" , mainController.loginError(model));
		Mockito.verify(model).addAttribute("error",true);
	}
	
	@Test
	public void auasVorschlagEntgegennehmenTest() {
		assertEquals("redirect:/" , mainController.auasVorschlagEntgegennehmen("username","password"));
	}
}
