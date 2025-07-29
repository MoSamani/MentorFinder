package de.hhu.mentoring.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.ConversationLog;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.files.FileService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;


@Controller
@RequestMapping(value = "mentor")
public class ControllerMentor {
	
	@Autowired 
	Messenger messenger; 
	
	@Autowired 
	AccountService accountService;
	
	@Autowired
	AssignmentService assignmentService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	AppointmentService appointmentService;
	
	@Autowired
	FileService fileService;


	@RequestMapping(value="")
	public String mentor() {
		return "Mentor/mentorStart";
	}
	 
	@RequestMapping(value="/postfachEingang")
	public String mentorPostfachEingang(Model model ,Principal principal) {
		
	List<Message> nachrichten = messenger.getAllReceivedMessagesByMailAddress(principal.getName());
	model.addAttribute("nachrichten", nachrichten);
	
		return "Mentor/NachrichtenPostfachEingang";

	}
	
	@RequestMapping(value="/postfachAusgang")
	public String mentorPostfachAusgang(Model model,Principal principal) {
		
		List<Message> nachrichten = messenger.getAllSentMessagesByMailAddress(principal.getName());
		model.addAttribute("nachrichten", nachrichten);
		
		return "Mentor/NachrichtenPostfachAusgang";
	}
	
	@GetMapping(value="/sendeNachricht")
	public String mentorNachrichtSenden(Model model,Principal principal) {
		
		List<User> allPossibleReceiver = accountService.getAllOrganizers();
		allPossibleReceiver.remove(accountService.getReminderUser());
		List<User> allStudentsOfMentor = assignmentService.getAllStudentsOfMentor(accountService.getUserByMailAddress(principal.getName()));
		for(User oneStudent : allStudentsOfMentor) {
			allPossibleReceiver.add(oneStudent);
		}
		model.addAttribute(new Message());
		model.addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
		return "Mentor/sendeNachricht";
		
	}
	@PostMapping(value="/sendeNachricht")
	public String mentorNachrichtAbgesendet(@ModelAttribute @Valid Message newMessage, Errors errors,@RequestParam String receiverName,Model model, Principal principal) {

		if(errors.hasErrors()==true) {
			List<User> allPossibleReceiver = accountService.getAllOrganizers();
			allPossibleReceiver.remove(accountService.getReminderUser());
			List<User> allStudentsOfMentor = assignmentService.getAllStudentsOfMentor(accountService.getUserByMailAddress(principal.getName()));
			for(User oneStudent : allStudentsOfMentor) {
				allPossibleReceiver.add(oneStudent);
			}
			model.addAttribute("moeglicheEmpfaenger",allPossibleReceiver);
			return "Mentor/sendeNachricht";
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
			return "redirect:/mentor/postfachAusgang";
		}
		
	}
	
	@RequestMapping(value="/postfach/{type}/{index}")
	public String mentorNachrichtEinsehen(@PathVariable int index,@PathVariable int type,Model model,Principal principal) {
		
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
		return "Mentor/NachrichtEinsehen";
		
	}
	
	
	// Closing Request
		@RequestMapping(value="/wechselantragSenden/{mailAddress:.+}")
		public String mentorWechselantrag(Model model,Principal principal) {
			model.addAttribute(new ClosingRequest());
			
			
			List<User> allStudents = assignmentService.getAllStudentsOfMentor(accountService.getUserByMailAddress(principal.getName()));
			
			
			if(allStudents == null) {
				model.addAttribute("studentVorhanden",false);
			}
			
			return "Mentor/WechselantragSenden";
		}
		
		@PostMapping("/wechselantragSenden/{mailAddress:.+}")
		public String mentorWechselantragSenden(@ModelAttribute @Valid ClosingRequest request,Errors error, Principal principal, Model model,@PathVariable String mailAddress) {
			User student = accountService.getUserByMailAddress(mailAddress);
			
			if(error.hasErrors()) {
				List<User> allStudents = assignmentService.getAllStudentsOfMentor(accountService.getUserByMailAddress(principal.getName()));
				if(allStudents == null) {
					model.addAttribute("studentVorhanden",false);
				}
				return "Mentor/WechselantragSenden";
			}
			
			requestService.requestCloseCase(accountService.getUserByMailAddress(principal.getName()),request.getReason(),assignmentService.getAssignmentOfStudent(student));
			
			return "redirect:/mentor";
		}
		
	@RequestMapping(value="/studenten")
	public String mentorStudentenEinsehen(Model model ,Principal principal) {
		User mentor = accountService.getUserByMailAddress(principal.getName());
		List<User> students = assignmentService.getAllStudentsOfMentor(mentor);
		model.addAttribute("students", students);
		return "Mentor/StudentenEinsehen";
	}
	@RequestMapping(value="/termineEinsicht")
	public String mentorTermineEinsehen(Model model ,Principal principal) {
		User mentor = accountService.getUserByMailAddress(principal.getName());

		List<Appointment> appointments = appointmentService.getAllAppointmentsByMentor(mentor);

		List<AppointmentProposal> appointmentProposals = new ArrayList<>();
		List<User> studentsOfMentor = assignmentService.getAllStudentsOfMentor(mentor);
		for(User student : studentsOfMentor) {
			appointmentProposals.addAll(appointmentService.getAllAppointmentProposalsByStudent(student));
		}
		model.addAttribute("appointmentProposals", appointmentProposals);
		model.addAttribute("appointments", appointments);
		return "Mentor/TermineEinsicht";
	}
	
	@GetMapping("/terminAbgesagt/{id}")
	public String mentorTermineAbsagen(Principal principal, Model model,@PathVariable Long id) {
		Appointment appointment =appointmentService.getAppointmentById(id);
		
		appointmentService.cancelAppointmentMentor(appointment);
		
		return "redirect:/mentor/termineEinsicht";
	}
	
	@GetMapping("/Terminvorschlaege")
	public String mentorTerminvorschlagErstellen(Model model,Principal principal)
	{
		List<User> allStudentsOfMentor = assignmentService.getAllStudentsOfMentor(accountService.getUserByMailAddress(principal.getName()));
		model.addAttribute("StudentsOfMentor",allStudentsOfMentor);
		model.addAttribute("appProposal",new AppointmentProposal());
		
		
		return "Mentor/TerminauswahlErstellen";
	}
	
	@PostMapping("/Terminvorschlaege")
	public String mentorTerminvorschlagAbschicken(Model model,@RequestParam String receiverProposal,@RequestParam String date1,@RequestParam String time1,@RequestParam String date2,@RequestParam String time2,@RequestParam String date3,@RequestParam String time3,Principal principal) {
		
		
		String NewReceiverProposal = "";
		for (int i = 0; i < receiverProposal.length(); i++) {
			if (receiverProposal.charAt(i) != ',')
				NewReceiverProposal += receiverProposal.charAt(i);
			else
				break;
		}
		String DateAndTime1 = date1 + time1;
		String DateAndTime2 = date2 + time2;
		String DateAndTime3 = date3 + time3;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
		
		appointmentService.makeAppointmentProposal(assignmentService.getAssignmentOfStudent(accountService.getUserByMailAddress(NewReceiverProposal)), LocalDateTime.parse(DateAndTime1, formatter), LocalDateTime.parse(DateAndTime2, formatter), LocalDateTime.parse(DateAndTime3, formatter));
		
		return "redirect:/mentor/termineEinsicht";
	}

	@RequestMapping(value="/convLogDetail/{id}")
	public String mentorProtokollEinsehen(@PathVariable Long id,Model model,Principal principal) {
		ConversationLog convLog = appointmentService.getConversationLogById(id);
		
		model.addAttribute("convLog",convLog);
		model.addAttribute("mentor", convLog.getAppointment().getAssignment().getMentor());
		return "Mentor/ConvLogDetail";
		
	}
	@GetMapping(value="/convLogNew/{id}")
	public String mentorNewConversation(@PathVariable Long id, Model model,Principal principal){
		
		model.addAttribute("text",new ConversationLog());
		
		return "Mentor/NewConversationLog";
		
	}
	@PostMapping("/convLogNew/{id}")
	public String mentorCreateNewConversation(@ModelAttribute ConversationLog text, Principal principal, Model model,@PathVariable Long id) {
		Appointment appointment = appointmentService.getAppointmentById(id);
		appointmentService.makeConversationLog(appointment, text.getText());
		
		return "redirect:/mentor/termineEinsicht";
	}
	
	
}
