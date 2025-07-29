package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import de.hhu.mentoring.controller.ControllerMentor;
import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.ConversationLog;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MentorControllerTests {
	
	private User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
	private User s2 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
	private User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
	private User o1 = new User("Paul","Pluto","p@p.de","pw",Role.ORGANIZER);

	private String mailAddress = "m@m.de";
	
	private Message message1 = new Message("title","content", LocalDateTime.now(), s1, s2);
	private Message message2 = new Message("title2","content2", LocalDateTime.now(), s2, s1);
	
	@Mock
	Model model;
	@Mock
	Errors error;
	@Mock
	Principal principial;
	@Mock 
	Messenger messenger;
	@Mock 
	AccountService accountService;
	@Mock 
	AssignmentService assignmentService;
	@Mock 
	RequestService requestService;
	@Mock
	AppointmentService appointmentService;
	@InjectMocks
	ControllerMentor mentorControl;
	

	@Test
	public void mentorTest() {
				
		assertEquals("Mentor/mentorStart",mentorControl.mentor());	
		
	}
	@Test
	public void mentorPostfachEingangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Mentor/NachrichtenPostfachEingang" , mentorControl.mentorPostfachEingang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void mentorPostfachAusgangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Mentor/NachrichtenPostfachAusgang" , mentorControl.mentorPostfachAusgang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void mentorNachrichtSendenTest() {				
		List<User> allStudents= new ArrayList<>();
		allStudents.add(s1);
		
		List<User> allOrganizers= new ArrayList<>();
		allOrganizers.add(o1);
		
		List<User> allMentors= new ArrayList<>();
		allMentors.add(m1);
		
		List<User> allPossibleReceiver= new ArrayList<>();
		allPossibleReceiver.add(o1);
		allPossibleReceiver.add(s1);
		
		Mockito.when(accountService.getAllOrganizers()).thenReturn(allOrganizers);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(allStudents);
		assertEquals("Mentor/sendeNachricht",mentorControl.mentorNachrichtSenden(model, principial));	
		Mockito.verify(model).addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		Mockito.verify(model).addAttribute(new Message());
			
	}
	@Test
	public void mentorNachrichtAbgesendenWrongMessageTest() {
		String name = "";
		
		List<User> allStudents= new ArrayList<>();
		allStudents.add(s1);
		
		List<User> allOrganizers= new ArrayList<>();
		allOrganizers.add(o1);
		
		List<User> allMentors= new ArrayList<>();
		allMentors.add(m1);
		
		List<User> allPossibleReceiver= new ArrayList<>();
		allPossibleReceiver.add(o1);
		allPossibleReceiver.add(s1);
		
		Mockito.when(error.hasErrors()).thenReturn(true);
		Mockito.when(accountService.getAllOrganizers()).thenReturn(allOrganizers);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(allStudents);
		Mockito.when(accountService.getAllStudents()).thenReturn(allStudents);
		assertEquals("Mentor/sendeNachricht",mentorControl.mentorNachrichtAbgesendet(message1,error,name,model, principial));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		
	}
	@Test
	public void mentorNachrichtAbgesendenCorrectMessageTest() {
		String sender="jose";
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s2);
		Mockito.when(principial.getName()).thenReturn(sender);
		Mockito.when(accountService.getUserByMailAddress(sender)).thenReturn(s1);
		assertEquals("redirect:/mentor/postfachAusgang",mentorControl.mentorNachrichtAbgesendet(message1,error,mailAddress,model, principial));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(accountService).getUserByMailAddress(sender);

	}
	@Test
	public void mentorNachrichtEinsehenReceiverTest() {
		message1.setSender(s1);
		List<Message> messages = new ArrayList<>();
		messages.add(message1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(messages);
		
		assertEquals("Mentor/NachrichtEinsehen",mentorControl.mentorNachrichtEinsehen(0,0, model, principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}
	
	@Test
	public void mentorNachrichtEinsehenSenderTest() {
		message1.setSender(s1);
		List<Message> messages = new ArrayList<>();
		messages.add(message1);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(messages);
		
		assertEquals("Mentor/NachrichtEinsehen",mentorControl.mentorNachrichtEinsehen(0,1, model, principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}
	
	@Test
	public void mentorStudentenEinsehenTest() {
		List<User> allStudents= new ArrayList<>();
		allStudents.add(s1);
		allStudents.add(s2);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(allStudents);
		assertEquals("Mentor/StudentenEinsehen",mentorControl.mentorStudentenEinsehen(model, principial));	
		Mockito.verify(model).addAttribute("students",allStudents);
		
	}
	
	@Test
	public void mentorWechselantragStudentsNotNullTest() {

		assertEquals("Mentor/WechselantragSenden",mentorControl.mentorWechselantrag(model, principial));
		Mockito.verify(model).addAttribute(new ClosingRequest());
	}
	
	@Test
	public void mentorWechselantragStudentsNullTest() {		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(null);
		
		assertEquals("Mentor/WechselantragSenden",mentorControl.mentorWechselantrag(model, principial));
		Mockito.verify(model).addAttribute(new ClosingRequest());
		Mockito.verify(model).addAttribute("studentVorhanden",false);
	}
	
	@Test
	public void mentorWechselantragSendenTest() {
		ClosingRequest request = new ClosingRequest();
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		assertEquals("redirect:/mentor",mentorControl.mentorWechselantragSenden(request,error,principial,model,mailAddress));
		Mockito.verify(error).hasErrors();
	}
	
	@Test
	public void mentorWechselantragSendenTestHasError() {
		ClosingRequest request = new ClosingRequest();
		Mockito.when(error.hasErrors()).thenReturn(true);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(null);
		assertEquals("Mentor/WechselantragSenden",mentorControl.mentorWechselantragSenden(request,error,principial,model,mailAddress));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("studentVorhanden", false);
	}
	
	@Test
	public void mentorTermineAbsagenTest() {
		
		assertEquals("redirect:/mentor/termineEinsicht", mentorControl.mentorTermineAbsagen(principial, model, (long)0));
	}
	
	@Test
	public void mentorTermineEinsehenTest() {
		List<Appointment> app= new ArrayList<>();
		
		List<User> students = new ArrayList<User>();
		students.add(s1);
		students.add(s2);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(students);
		Mockito.when(appointmentService.getAllAppointmentsByMentor(m1)).thenReturn(app);
			
		assertEquals("Mentor/TermineEinsicht", mentorControl.mentorTermineEinsehen(model, principial));
		Mockito.verify(model).addAttribute("appointments",app);
		
	}
	@Test
	public void mentorTerminvorschlagErstellenTest() {
		List<User> students= new ArrayList<>();
		students.add(s1);
		students.add(s2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsOfMentor(m1)).thenReturn(students);
		
		assertEquals("Mentor/TerminauswahlErstellen",mentorControl.mentorTerminvorschlagErstellen(model, principial));
		Mockito.verify(model).addAttribute("appProposal",new AppointmentProposal());
		Mockito.verify(model).addAttribute("StudentsOfMentor",students);
	}

	@Test
	public void mentorTerminvorschlagAbschickenTest() {
		String time="20:20";
		String date="2000-01-01";
		String reciveProposal="reciveProposal";
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		assertEquals("redirect:/mentor/termineEinsicht",mentorControl.mentorTerminvorschlagAbschicken(model,reciveProposal,date,time,date,time,date,time, principial));
	}
	@Test
	public void mentorProtokollEinsehenTest() {
		ConversationLog conversationLog= new ConversationLog();
		Assignment a1 = new Assignment(s1,m1);
		Appointment appointment1= new Appointment();
		conversationLog.setAppointment(appointment1);
		appointment1.setAssignment(a1);
		long id=1;
		Mockito.when(appointmentService.getConversationLogById(id)).thenReturn(conversationLog);
		assertEquals("Mentor/ConvLogDetail",mentorControl.mentorProtokollEinsehen(id,model,principial));	
		Mockito.verify(model).addAttribute("convLog",conversationLog);
		Mockito.verify(model).addAttribute("mentor",m1);
	}
	@Test
	public void mentorNewConversationTest() {
		long id=1;
		assertEquals("Mentor/NewConversationLog",mentorControl.mentorNewConversation(id,model,principial));	
		Mockito.verify(model).addAttribute("text",new ConversationLog());
	}
	
	@Test
	public void mentorCreateNewConversationTest() {
		ConversationLog conversationLog= new ConversationLog();
		Assignment a1 = new Assignment(s1,m1);
		Appointment appointment1= new Appointment();
		conversationLog.setAppointment(appointment1);
		appointment1.setAssignment(a1);
		long id=1;
		Mockito.when(appointmentService.getConversationLogById(id)).thenReturn(conversationLog);
		assertEquals("redirect:/mentor/termineEinsicht",mentorControl.mentorCreateNewConversation(conversationLog,principial,model,id));	
		
	}
	
}
