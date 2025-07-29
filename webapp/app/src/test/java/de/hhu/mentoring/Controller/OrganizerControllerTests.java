package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

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

import de.hhu.mentoring.controller.ControllerOrganizer;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizerControllerTests {
	
	private User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
	private User s2 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
	private User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
	private User m2 = new User("Erwin","Europa","a@a.de","pw",Role.MENTOR);
	private User o1 = new User("Paul","Pluto","p@p.de","pw",Role.ORGANIZER);
	private User o2 = new User("Paul2","Pluto2","p2@p.de","pw",Role.ORGANIZER);
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
	@InjectMocks
	ControllerOrganizer organizerControl;

	@Test
	public void organizerTest() {
				
		assertEquals("Organizer/organizerStart",organizerControl.organizer());	
		
	}
	@Test
	public void organizerPostfachEingangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Organizer/NachrichtenPostfachEingang" , organizerControl.organizerPostfachEingang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void organizerPostfachAusgangTest() {
		List<Message> list= new ArrayList<Message>();
		list.add(message1);
		list.add(message2);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(list);
		assertEquals("Organizer/NachrichtenPostfachAusgang" , organizerControl.organizerPostfachAusgang(model,principial));
		Mockito.verify(model).addAttribute("nachrichten",list);
	}
	
	@Test
	public void organizerNachrichtSendenTest() {				
		List<User> allStudents= new ArrayList<>();
		allStudents.add(s1);
		
		List<User> allPrganizers= new ArrayList<>();
		allPrganizers.add(o1);
		allPrganizers.add(o2);
		
		List<User> allMentors= new ArrayList<>();
		allMentors.add(m1);
		
		List<User> allPossibleReceiver= new ArrayList<>();
		allPossibleReceiver.add(o2);
		allPossibleReceiver.add(s1);
		allPossibleReceiver.add(m1);
		
		Mockito.when(accountService.getAllOrganizers()).thenReturn(allPrganizers);
		Mockito.when(accountService.getAllMentors()).thenReturn(allMentors);
		Mockito.when(accountService.getAllStudents()).thenReturn(allStudents);
		Mockito.when(accountService.getReminderUser()).thenReturn(o1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		assertEquals("Organizer/sendeNachricht",organizerControl.organizerNachrichtSenden(principial, model));	
		Mockito.verify(model).addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		Mockito.verify(model).addAttribute(new Message());
		
	}
	@Test
	public void organizerNachrichtAbgesendenWrongMessageTest() {
		String name = "";
		List<User> allStudents= new ArrayList<>();
		allStudents.add(s1);
		
		List<User> allPrganizers= new ArrayList<>();
		allPrganizers.add(o1);
		allPrganizers.add(o2);

		List<User> allMentors= new ArrayList<>();
		allMentors.add(m1);
		
		List<User> allPossibleReceiver= new ArrayList<>();
		allPossibleReceiver.add(o2);
		allPossibleReceiver.add(s1);
		allPossibleReceiver.add(m1);
		Mockito.when(error.hasErrors()).thenReturn(true);
		Mockito.when(accountService.getAllOrganizers()).thenReturn(allPrganizers);
		Mockito.when(accountService.getAllMentors()).thenReturn(allMentors);
		Mockito.when(accountService.getAllStudents()).thenReturn(allStudents);
		Mockito.when(accountService.getReminderUser()).thenReturn(o1);
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		assertEquals("Organizer/sendeNachricht",organizerControl.organizerNachrichtAbgesendet(message1,error,name,principial, model));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(model).addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		
	}
	@Test
	public void organizerNachrichtAbgesendenCorrectMessageTest() {
		String reciever="jose";
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s2);
		Mockito.when(principial.getName()).thenReturn(reciever);
		Mockito.when(accountService.getUserByMailAddress(reciever)).thenReturn(s1);
		assertEquals("redirect:/organizer/postfachAusgang",organizerControl.organizerNachrichtAbgesendet(message1,error,mailAddress,principial, model));	
		Mockito.verify(error).hasErrors();
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(accountService).getUserByMailAddress(reciever);

	}

	@Test
	public void organizerNachrichtEinsehenReceiverTest() {
		message1.setSender(s1);
		List<Message> messages = new ArrayList<>();
		messages.add(message1);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllReceivedMessagesByMailAddress(mailAddress)).thenReturn(messages);
		
		assertEquals("Organizer/NachrichtEinsehen",organizerControl.organizerNachrichtEinsehen(0,0, model, principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}
	
	@Test
	public void organizerNachrichtEinsehenSenderTest() {
		message1.setSender(s1);
		List<Message> messages = new ArrayList<>();
		messages.add(message1);
		
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		Mockito.when(messenger.getAllSentMessagesByMailAddress(mailAddress)).thenReturn(messages);
		
		assertEquals("Organizer/NachrichtEinsehen",organizerControl.organizerNachrichtEinsehen(0,1, model, principial));	
		Mockito.verify(model).addAttribute("Title","title");
		Mockito.verify(model).addAttribute("Content","content");
		Mockito.verify(model).addAttribute("Sender","Martin "+"Mars");

	}
	
	@Test
	public void organizerAssignmnentsEinsehenTest() {
		
		Assignment assignment1= new Assignment();
		assignment1.setStudent(s1);
		Assignment assignment2= new Assignment();
		assignment2.setMentor(s2);
		List<Assignment> allPossibleAssignment= new ArrayList<>();
		allPossibleAssignment.add(assignment1);
		allPossibleAssignment.add(assignment2);
		Mockito.when(assignmentService.getAllAssignments()).thenReturn(allPossibleAssignment);
		assertEquals("Organizer/AssignmentsEinsehen",organizerControl.organizerAssignmnentsEinsehen( model));	
		Mockito.verify(model).addAttribute("assignments",allPossibleAssignment);
	
	}
	@Test
	public void chooseStudenToAssignMentorTest() {
		List<User> studentsWithoutMentor= new ArrayList<>();
		studentsWithoutMentor.add(s1);
		studentsWithoutMentor.add(s2);
		Mockito.when(assignmentService.getAllStudentsWithoutMentor()).thenReturn(studentsWithoutMentor);
		assertEquals("Organizer/studentOhneMentor",organizerControl.chooseStudenToAssignMentor( model));	
		Mockito.verify(model).addAttribute("studenten",studentsWithoutMentor);
	
	}
	@Test
	public void assignMentorToStudentFormTest() {
		List<User> allMentors= new ArrayList<>();
		allMentors.add(m1);
		allMentors.add(m2);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(accountService.getAllMentors()).thenReturn(allMentors);
		assertEquals("Organizer/studentMentorZuweisen",organizerControl.assignMentorToStudentForm(mailAddress, model));	
		Mockito.verify(model).addAttribute("student",s1.getPrename()+" "+s1.getSurname());
		Mockito.verify(model).addAttribute("moeglicheMentoren",allMentors);
	}

	@Test
	public void assignMentorToStudentPostTest() {
			String mentor="jose";
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		Mockito.when(accountService.getUserByMailAddress(mentor)).thenReturn(s2);
		assertEquals("redirect:",organizerControl.assignMentorToStudentPost(mailAddress, model,mentor));	
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(accountService).getUserByMailAddress(mentor);
	}
	@Test
	public void fallabschlussListeTest() {
		ClosingRequest a= new ClosingRequest();
		ClosingRequest b= new ClosingRequest();
		List<ClosingRequest> allClosingRequest= new ArrayList<>();
		allClosingRequest.add(a);
		allClosingRequest.add(b);
		Mockito.when(requestService.getAllClosingRequests()).thenReturn(allClosingRequest);
		assertEquals("Organizer/FallabschlussAntragListe",organizerControl.fallabschlussListe( model,principial));	
		Mockito.verify(model).addAttribute("closingRequest",allClosingRequest);
	}
	@Test
	public void fallabschlussDetailTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		assignment.setStudent(s1);
		ClosingRequest closingRequest= new ClosingRequest("nichtkompetent",m1,assignment);
		Long id=(long) 1;
		
		Mockito.when(requestService.getClosingRequestById(id)).thenReturn(closingRequest);
		assertEquals("Organizer/FallabschlussAntragDetail",organizerControl.fallabschlussDetail(id, model));	
		//Mockito.verify(requestService).getClosingRequestById(id);
		Mockito.verify(model).addAttribute("mentor",m1.getPrename()+ " "+m1.getSurname());
		Mockito.verify(model).addAttribute("student",s1.getPrename()+ " "+s1.getSurname());
		Mockito.verify(model).addAttribute("reason",closingRequest.getReason());
		Mockito.verify(model).addAttribute("request",closingRequest);
	
	}
	@Test
	public void fallabschlussAntragAnnehmenTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		assignment.setStudent(s1);
		ClosingRequest closingRequest= new ClosingRequest("nichtkompetent",m1,assignment);
		Long id=(long) 1;
		
		Mockito.when(requestService.getClosingRequestById(id)).thenReturn(closingRequest);
		assertEquals("redirect:/organizer/fallabschlussListe",organizerControl.fallabschlussAntragAnnehmen(id, model));	
		Mockito.verify(requestService).getClosingRequestById(id);
		
	}
	@Test
	public void fallabschlussAntragAblehnenTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		assignment.setStudent(s1);
		ClosingRequest closingRequest= new ClosingRequest("nichtkompetent",m1,assignment);
		Long id=(long) 1;
		
		Mockito.when(requestService.getClosingRequestById(id)).thenReturn(closingRequest);
		assertEquals("redirect:/organizer/fallabschlussListe",organizerControl.fallabschlussAntragAblehnen(id, model));	
		Mockito.verify(requestService).getClosingRequestById(id);
		
	}
	@Test
	public void mentorenwechselListeTest() {
		ChangeRequest changeRequest1=new ChangeRequest();
		ChangeRequest changeRequest2=new ChangeRequest();
		List<ChangeRequest> changeRequests= new ArrayList<>();
		changeRequests.add(changeRequest1);
		changeRequests.add(changeRequest2);
		Mockito.when(requestService.getAllChangeRequests()).thenReturn(changeRequests);
		assertEquals("Organizer/MentorenwechselAntragListe",organizerControl.mentorenwechselListe(model,principial));	
		Mockito.verify(model).addAttribute("changeRequest",changeRequests);
		
	}
	@Test
	public void mentorenwechselDetailTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		ChangeRequest changeRequest= new ChangeRequest("nichtkompetent",m1,assignment);
		changeRequest.setStudent(s1);
		Long id=(long) 1;
		
		Mockito.when(requestService.getChangeRequestById(id)).thenReturn(changeRequest);
		assertEquals("Organizer/MentorenwechselAntragDetail",organizerControl.mentorenwechselDetail(id, model));	
		//Mockito.verify(requestService).getClosingRequestById(id);
		Mockito.verify(model).addAttribute("mentor",m1.getPrename()+ " "+m1.getSurname());
		Mockito.verify(model).addAttribute("student",s1.getPrename()+ " "+s1.getSurname());
		Mockito.verify(model).addAttribute("reason",changeRequest.getReason());
		Mockito.verify(model).addAttribute("request",changeRequest);
	
	}
	@Test
	public void mentorenwechselAntragAnnehmenTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		ChangeRequest changeRequest= new ChangeRequest("nichtkompetent",m1,assignment);
		changeRequest.setStudent(s1);
		Long id=(long) 1;
		
		Mockito.when(requestService.getChangeRequestById(id)).thenReturn(changeRequest);
		assertEquals("redirect:/organizer/mentorenwechselListe",organizerControl.mentorenwechselAntragAnnehmen(id, model));	
		Mockito.verify(requestService).getChangeRequestById(id);
		
	}

	@Test
	public void mentorenwechselAntragAblehnenTest() {
		Assignment assignment= new Assignment();
		assignment.setMentor(m1);
		ChangeRequest changeRequest= new ChangeRequest("nichtkompetent",m1,assignment);
		changeRequest.setStudent(s1);
		Long id=(long) 1;
		
		Mockito.when(requestService.getChangeRequestById(id)).thenReturn(changeRequest);
		assertEquals("redirect:/organizer/mentorenwechselListe",organizerControl.mentorenwechselAntragAblehnen(id, model));	
		Mockito.verify(requestService).getChangeRequestById(id);
		
	}
	
	@Test
	public void assignmentLoeschenTest() {
		Long id = 3l;
		Assignment a1 = new Assignment(s1,m1);
		a1.setId(id);
		
		Mockito.when(assignmentService.getAssignmentById(id)).thenReturn(a1);
		assertEquals("redirect:/organizer/assignments", organizerControl.assignmentLoeschen(id));
		Mockito.verify(assignmentService, times(1)).removeAssignmentByOrganizer(a1);
	}

	
}
