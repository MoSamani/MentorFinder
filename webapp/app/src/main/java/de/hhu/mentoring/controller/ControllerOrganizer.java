package de.hhu.mentoring.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;


@Controller
@RequestMapping(value = "organizer")
public class ControllerOrganizer {
	
	@Autowired
	Messenger messenger;
	
	@Autowired 
	AccountService accountService;
	
	@Autowired
	AssignmentService assignmentService;

	@Autowired
	RequestService requestService;

	@RequestMapping(value="")
	public String organizer() {
		return "Organizer/organizerStart";
	}
	
	@RequestMapping(value="/postfachEingang")
	public String organizerPostfachEingang(Model model ,Principal principal) {
		
		List<Message> nachrichten = messenger.getAllReceivedMessagesByMailAddress(principal.getName());
	model.addAttribute("nachrichten", nachrichten);

		return "Organizer/NachrichtenPostfachEingang";

	}
	
	@RequestMapping(value="/postfachAusgang")
	public String organizerPostfachAusgang(Model model,Principal principal) {

		List<Message> nachrichten = messenger.getAllSentMessagesByMailAddress(principal.getName());
		model.addAttribute("nachrichten", nachrichten);
		return "Organizer/NachrichtenPostfachAusgang";
	}
	
	@GetMapping(value="/sendeNachricht")
	public String organizerNachrichtSenden(Principal principal,Model model) {
		List<User> allPossibleReceiver = new ArrayList<User>();
		List<User> allOrganizer = accountService.getAllOrganizers();
		for(User organizer : allOrganizer) {
			if(!organizer.getMailAddress().equals(principal.getName())&&!organizer.getMailAddress().equals(accountService.getReminderUser().getMailAddress())) {
				allPossibleReceiver.add(organizer);
			}
		}
		
		List<User> allStudents = accountService.getAllStudents();
		for(User oneStudent : allStudents) {
			allPossibleReceiver.add(oneStudent);
		}
		List<User> allMentors = accountService.getAllMentors();
		for(User oneMentor : allMentors) {
			allPossibleReceiver.add(oneMentor);
		}
		
		model.addAttribute(new Message());
		model.addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		return "Organizer/sendeNachricht";
	}
	
	@PostMapping(value="/sendeNachricht")
	public String organizerNachrichtAbgesendet(@ModelAttribute @Valid Message newMessage, Errors errors, @RequestParam String receiverName, Principal principal, Model model) {
		if(errors.hasErrors()==true) {
			List<User> allPossibleReceiver = new ArrayList<User>();
			List<User> allOrganizer = accountService.getAllOrganizers();
			for(User organizer : allOrganizer) {
				if(!organizer.getMailAddress().equals(principal.getName())&&!organizer.getMailAddress().equals(accountService.getReminderUser().getMailAddress())) {
					allPossibleReceiver.add(organizer);
				}
			}

			List<User> allStudents = accountService.getAllStudents();
			for(User oneStudent : allStudents) {
				allPossibleReceiver.add(oneStudent);
			}
			List<User> allMentors = accountService.getAllMentors();
			for(User oneMentor : allMentors) {
				allPossibleReceiver.add(oneMentor);
			}

			model.addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
			return "Organizer/sendeNachricht";
		}
		else {
			String mailAddress = "";
			for (int i = 0; i < receiverName.length(); i++) {
				if (receiverName.charAt(i) != ',')
					mailAddress += receiverName.charAt(i);
				else
					break;
			}
			newMessage.setReceiver(accountService.getUserByMailAddress(mailAddress));
			newMessage.setSender(accountService.getUserByMailAddress(principal.getName()));
			newMessage.setDate(LocalDateTime.now());

			messenger.send(newMessage.getSender(), newMessage.getReceiver(), newMessage.getTitle(), newMessage.getContent());
			return "redirect:/organizer/postfachAusgang";
		}
	}
	
	@RequestMapping(value="/postfach/{type}/{index}")
	public String organizerNachrichtEinsehen(@PathVariable int index,@PathVariable int type,Model model,Principal principal) {
		List<Message> nachrichten;
		
		if(type == 0) {
			
			nachrichten = messenger.getAllReceivedMessagesByMailAddress(principal.getName());
			model.addAttribute("posteingang",true);
		}
		else {
			
			nachrichten = messenger.getAllSentMessagesByMailAddress(principal.getName());
		}
		nachrichten.get(index);
		
		model.addAttribute("Title", nachrichten.get(index).getTitle());
		model.addAttribute("Content",nachrichten.get(index).getContent());
		model.addAttribute("Sender", nachrichten.get(index).getSender().getPrename() +" " + nachrichten.get(index).getSender().getSurname());
		return "Organizer/NachrichtEinsehen";
		
	}
	
	@RequestMapping(value="/assignments")
	public String organizerAssignmnentsEinsehen(Model model) {
		
		List<Assignment> assignments = assignmentService.getAllAssignments();
		model.addAttribute("assignments", assignments);
			
		return "Organizer/AssignmentsEinsehen";
	}
	
	@RequestMapping(value="/assignmentLoeschen/{id}")
	public String assignmentLoeschen(@PathVariable Long id) {
		Assignment assignment = assignmentService.getAssignmentById(id);
		assignmentService.removeAssignmentByOrganizer(assignment);
		return "redirect:/organizer/assignments";
	}
	
	
	
	// Assigning
	
	@RequestMapping(value="/mentorZuweisen")
	public String chooseStudenToAssignMentor(Model model){
		

		List<User> studentsWithoutMentor = assignmentService.getAllStudentsWithoutMentor();
		model.addAttribute("studenten",studentsWithoutMentor);
		return "Organizer/studentOhneMentor";
	}
	
	@GetMapping(value="/mentorZuweisen/{mailAddress:.+}")
	public String assignMentorToStudentForm(@PathVariable String mailAddress,Model model) {
		
		User student = accountService.getUserByMailAddress(mailAddress);
		String studentName = student.getPrename() + " " + student.getSurname();
		model.addAttribute("student", studentName);
		model.addAttribute("moeglicheMentoren", accountService.getAllMentors());
		return "Organizer/studentMentorZuweisen";
	}
	
	@PostMapping(value="/mentorZuweisen/{mailAddress:.+}")
	public String assignMentorToStudentPost(@PathVariable String mailAddress,Model model,@RequestParam String MentorSelected) {
		
	
		String mentor = "";
		for (int i = 0; i < MentorSelected.length(); i++) {
			if (MentorSelected.charAt(i) != ',')
				mentor += MentorSelected.charAt(i);
			else
				break;
		}
		assignmentService.assignMentorToStudent(accountService.getUserByMailAddress(mailAddress), accountService.getUserByMailAddress(mentor));
		
		return "redirect:";
	}
	
	
	
	
	// Closing Requests
	
	@RequestMapping(value="/fallabschlussListe")
	public String fallabschlussListe(Model model ,Principal principal) {
		
		List<ClosingRequest> closingRequest = requestService.getAllClosingRequests();
		
		model.addAttribute("closingRequest", closingRequest);

		return "Organizer/FallabschlussAntragListe";

	}
	
	@RequestMapping(value="/fallabschlussDetail/{id}")
	public String fallabschlussDetail(@PathVariable long id ,Model model) {		
		
		model.addAttribute("mentor", requestService.getClosingRequestById(id).getMentor().getPrename()+ " "+requestService.getClosingRequestById(id).getMentor().getSurname() );
		model.addAttribute("student", requestService.getClosingRequestById(id).getAssignment().getStudent().getPrename() + " "+requestService.getClosingRequestById(id).getAssignment().getStudent().getSurname());
		model.addAttribute("reason", requestService.getClosingRequestById(id).getReason());
		model.addAttribute("request", requestService.getClosingRequestById(id));

		
		return "Organizer/FallabschlussAntragDetail";

	}
	
	@RequestMapping(value="/fallabschlussAntragAnnehmen/{id}")
	public String fallabschlussAntragAnnehmen(@PathVariable long id ,Model model) {
		
		requestService.acceptClosingRequest(requestService.getClosingRequestById(id));
		return "redirect:/organizer/fallabschlussListe";

	}
	
	@RequestMapping(value="/fallabschlussAntragAblehnen/{id}")
	public String fallabschlussAntragAblehnen(@PathVariable long id ,Model model) {
		
		requestService.rejectClosingRequest(requestService.getClosingRequestById(id));
		return "redirect:/organizer/fallabschlussListe";

	}
	
	
	
	
	// Change Requests
	
	@RequestMapping(value="/mentorenwechselListe")
	public String mentorenwechselListe(Model model ,Principal principal) {
		
		List<ChangeRequest> changeRequest = requestService.getAllChangeRequests();
		
		model.addAttribute("changeRequest", changeRequest);

		return "Organizer/MentorenwechselAntragListe";

	}
	
	@RequestMapping(value="/mentorenwechselDetail/{id}")
	public String mentorenwechselDetail(@PathVariable long id ,Model model) {		
		
		model.addAttribute("mentor", requestService.getChangeRequestById(id).getAssignment().getMentor().getPrename()+ " "+requestService.getChangeRequestById(id).getAssignment().getMentor().getSurname() );
		model.addAttribute("student", requestService.getChangeRequestById(id).getStudent().getPrename() + " "+requestService.getChangeRequestById(id).getStudent().getSurname());
		model.addAttribute("reason", requestService.getChangeRequestById(id).getReason());
		model.addAttribute("request", requestService.getChangeRequestById(id));

		
		return "Organizer/MentorenwechselAntragDetail";

	}
	
	@RequestMapping(value="/mentorenwechselAntragAnnehmen/{id}")
	public String mentorenwechselAntragAnnehmen(@PathVariable long id ,Model model) {
		
		requestService.acceptChangeRequest(requestService.getChangeRequestById(id));
		return "redirect:/organizer/mentorenwechselListe";

	}
	
	@RequestMapping(value="/mentorenwechselAntragAblehnen/{id}")
	public String mentorenwechselAntragAblehnen(@PathVariable long id ,Model model) {
		
		requestService.rejectChangeRequest(requestService.getChangeRequestById(id));
		return "redirect:/organizer/mentorenwechselListe";

	}
	
}
