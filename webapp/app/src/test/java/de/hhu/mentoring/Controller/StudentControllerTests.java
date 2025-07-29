package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import de.hhu.mentoring.controller.ControllerStudent;
import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
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
public class StudentControllerTests {
	
	private User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
	private User s2 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
	private User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
	
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
	ControllerStudent studentControl;
	
	@Test
	public void studentTest() {
		assertEquals("Student/studentStart",studentControl.student());	
		
	}
	@Test
	public void studentPostfachEingangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Student/NachrichtenPostfachEingang" , studentControl.studentPostfachEingang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void studentPostfachAusgangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Student/NachrichtenPostfachAusgang" , studentControl.studentPostfachAusgang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void studentNachrichtSendenTestAssignmentExist() {
		Assignment a1 = new Assignment(s1,m1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(assignmentService.getAssignmentOfStudent(s1)).thenReturn(a1);
		assertEquals("Student/sendeNachricht",studentControl.studentNachrichtSenden(model, principial));	
		Mockito.verify(model).addAttribute("mentor","Alex"+" "+"Afrika");
		
		
	}
	@Test
	public void studentNachrichtSendenTestAssignmentNotExist() {
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(assignmentService.getAssignmentOfStudent(s1)).thenReturn(null);
		assertEquals("Student/sendeNachricht",studentControl.studentNachrichtSenden(model, principial));	
		Mockito.verify(model).addAttribute("mentor","Dir wurde noch kein mentor zugewiesen");
		Mockito.verify(model).addAttribute("NoMentor",true);
		Mockito.verify(model).addAttribute(new Message());
		
	}
	@Test
	public void studentNachrichtAbgesendetWrongMessageTest() {
		Assignment a1 = new Assignment(s1,m1);
		
		Mockito.when(error.hasErrors()).thenReturn(true);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(assignmentService.getAssignmentOfStudent(s1)).thenReturn(a1);
		
		assertEquals("Student/sendeNachricht",studentControl.studentNachrichtAbgesendet(message1, error, principial, model));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("mentor","Alex");
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(assignmentService).getAssignmentOfStudent(s1);
		
	}

	@Test
	public void studentNachrichtAbgesendetCorrectMessageTest() {
		Assignment a1 = new Assignment(s1,m1);
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(assignmentService.getAssignmentOfStudent(s1)).thenReturn(a1);
		
		assertEquals("redirect:/student/postfachAusgang",studentControl.studentNachrichtAbgesendet(message1, error, principial, model));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
		Mockito.verify(assignmentService).getAssignmentOfStudent(s1);
		
	}
	@Test
	public void studentNachrichtEinsehenReceiverTest() {
		List<Message> messages= new ArrayList<>();
		messages.add(message1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(messages);
	
		assertEquals("Student/NachrichtEinsehen",studentControl.studentNachrichtEinsehen(0,0, model,principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}
	
	@Test
	public void studentNachrichtEinsehenSenderTest() {
		List<Message> messages= new ArrayList<>();
		messages.add(message1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(messages);
	
		assertEquals("Student/NachrichtEinsehen",studentControl.studentNachrichtEinsehen(0,1, model,principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}

	@Test
	public void studentWechselantragWithMentorTest() {
		List<User> students= new ArrayList<>();
		students.add(s1);
		students.add(s2);
		Mockito.when(assignmentService.getAllStudentsWithoutMentor()).thenReturn(students);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		assertEquals("Student/WechselantragSenden",studentControl.studentWechselantrag( model,principial));	
		Mockito.verify(model).addAttribute("request",new ChangeRequest());
	
	}
	@Test
	public void studentWechselantragWithoutMentorTest() {
		List<User> students= new ArrayList<>();
		students.add(s1);
		students.add(s2);
		Mockito.when(assignmentService.getAllStudentsWithoutMentor()).thenReturn(students);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		assertEquals("Student/WechselantragSenden",studentControl.studentWechselantrag( model,principial));	
		Mockito.verify(model).addAttribute("request",new ChangeRequest());
		Mockito.verify(model).addAttribute("mentorVorhanden",false);
	
	}
	
	@Test
	public void studentWechselantragSendenTest() {
		ChangeRequest changeRequest = Mockito.mock(ChangeRequest.class);
		changeRequest.setReason("reas");

		Mockito.when(error.hasErrors()).thenReturn(false);
		assertEquals("redirect:/student", studentControl.studentWechselantragSenden(changeRequest,error,principial,model));
		Mockito.verify(error).hasErrors();

	}
	@Test
	public void studentWechselantragSendenTestHasError() {
		ChangeRequest request = new ChangeRequest();
		List<User> students= new ArrayList<>();
		students.add(s1);
		s1.setMailAddress(mailAddress);
		Mockito.when(error.hasErrors()).thenReturn(true);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(m1);
		Mockito.when(assignmentService.getAllStudentsWithoutMentor()).thenReturn(students);
		assertEquals("Student/WechselantragSenden",studentControl.studentWechselantragSenden(request,error,principial,model));
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("mentorVorhanden", false);
	}
	@Test
	public void studentWechselantragSendenTestAntragDoppelt() {
		Assignment assignment = new Assignment(s2, m1);
		ChangeRequest changeRequest = new ChangeRequest("reason", s2, assignment);
		List<ChangeRequest> requests = new ArrayList<ChangeRequest>();
		requests.add(changeRequest);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s2);
		Mockito.when(requestService.getAllChangeRequests()).thenReturn(requests);
		
		assertEquals("Student/WechselantragSenden", studentControl.studentWechselantrag(model,principial));
		Mockito.verify(model).addAttribute("antragSchonGestellt", true);
	}
	
	@Test
	public void studentTermineEinsehenTest() {
		Appointment appointment1= new Appointment();
		Appointment appointment2= new Appointment();
		List<Appointment> appointments = new ArrayList<>();
		appointments.add(appointment1);
		appointments.add(appointment2);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(appointmentService.getAllAppointmentsByStudent(s1)).thenReturn(appointments);
	
		assertEquals("Student/TermineEinsicht",studentControl.studentTermineEinsehen(model, principial));	
		Mockito.verify(model).addAttribute("appointments",appointments);
		
	}

	@Test
	public void studentTermineAbsagenTest() {
		Appointment appointment1= new Appointment();
		long id=1;
		Mockito.when(appointmentService.getAppointmentById(id)).thenReturn(appointment1);
	
		assertEquals("redirect:/student/termineEinsicht",studentControl.studentTermineAbsagen(principial,model, id));	
		
	}
	
	@Test
	public void VorschlagAblehnenTest() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
	
		assertEquals("Student/TerminvorschlaegeEntscheiden",studentControl.VorschlagAblehnen(principial,model, id));	
		
	}

	@Test
	public void VorschlagAnnehmenOptionTest() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		String option = ""; 
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
		
		assertEquals("redirect:/student/termineEinsicht",studentControl.VorschlagAnnehmen(principial,model, id, option));	
		
	}
	@Test
	public void VorschlagAnnehmenOption1Test() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		String date="2000-01-0120:20";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
		String option = "option1"; 
		appointmentProposal1.setDate1(LocalDateTime.parse(date,formatter));
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
		
		assertEquals("redirect:/student/termineEinsicht",studentControl.VorschlagAnnehmen(principial,model, id, option));	
		
	}
	@Test
	public void VorschlagAnnehmenOption2Test() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		String date="2000-01-0120:20";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
		String option = "option2"; 
		appointmentProposal1.setDate2(LocalDateTime.parse(date,formatter));
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
		
		assertEquals("redirect:/student/termineEinsicht",studentControl.VorschlagAnnehmen(principial,model, id, option));	
		
	}
	@Test
	public void VorschlagAnnehmenOption3Test() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		String date="2000-01-0120:20";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
		String option = "option3"; 
		appointmentProposal1.setDate3(LocalDateTime.parse(date,formatter));
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
		
		assertEquals("redirect:/student/termineEinsicht",studentControl.VorschlagAnnehmen(principial,model, id, option));	
		
	}
	@Test
	public void VorschlagAnnehmenOption4Test() {
		AppointmentProposal appointmentProposal1= new AppointmentProposal();
		long id=1;
		String option = "option4"; 
		Mockito.when(appointmentService.getAppointmentProposalById(id)).thenReturn(appointmentProposal1);
		
		assertEquals("redirect:/student/termineEinsicht",studentControl.VorschlagAnnehmen(principial,model, id, option));	
		
	}
	@Test
	public void studentProtokollEinsehenTest() {
		ConversationLog conversationLog= new ConversationLog();
		Assignment a1 = new Assignment(s1,m1);
		Appointment appointment1= new Appointment();
		conversationLog.setAppointment(appointment1);
		appointment1.setAssignment(a1);
		long id=1;
		Mockito.when(appointmentService.getConversationLogById(id)).thenReturn(conversationLog);
		assertEquals("Student/ConvLogDetail",studentControl.studentProtokollEinsehen(id,model,principial));	
		Mockito.verify(model).addAttribute("convLog",conversationLog);
		Mockito.verify(model).addAttribute("mentor",m1);
	}
	
	
}
