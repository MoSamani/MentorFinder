package de.hhu.mentoring.controller;


import java.security.Principal;
import java.time.LocalDateTime;
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
import de.hhu.mentoring.database.model.ChangeRequest;
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
@RequestMapping(value = "student")
public class ControllerStudent {
	
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
	public String student() {
		return "Student/studentStart";
	}
	
	@RequestMapping(value="/postfachEingang")
	public String studentPostfachEingang(Model model ,Principal principal) {
		
	List<Message> nachrichten = messenger.getAllReceivedMessagesByMailAddress(principal.getName());
	model.addAttribute("nachrichten", nachrichten);

		return "Student/NachrichtenPostfachEingang";

	}
	
	@RequestMapping(value="/postfachAusgang")
	public String studentPostfachAusgang(Model model,Principal principal) {
		
		List<Message> nachrichten = messenger.getAllSentMessagesByMailAddress(principal.getName());
		model.addAttribute("nachrichten", nachrichten);
		
		return "Student/NachrichtenPostfachAusgang";
	}
	
	@GetMapping(value="/sendeNachricht")
	public String studentNachrichtSenden(Model model,Principal principal) {
		
		if(assignmentService.getAssignmentOfStudent(accountService.getUserByMailAddress(principal.getName()))!=null) {
			User mentor = assignmentService.getAssignmentOfStudent(accountService.getUserByMailAddress(principal.getName())).getMentor();
			String mentorName = mentor.getPrename() + " " + mentor.getSurname();
			model.addAttribute("mentor", mentorName);
			model.addAttribute(new Message());
		}
		else {
			model.addAttribute("mentor","Dir wurde noch kein mentor zugewiesen");
			model.addAttribute("NoMentor",true);
			model.addAttribute(new Message());
		}
		return "Student/sendeNachricht";
	}
	
	@PostMapping(value="/sendeNachricht")
	public String studentNachrichtAbgesendet(@ModelAttribute @Valid Message newMessage,Errors errors,Principal principal,Model model) {
		
		if(errors.hasErrors()==true) {
			model.addAttribute("mentor",assignmentService.getAssignmentOfStudent(accountService.getUserByMailAddress(principal.getName())).getMentor().getPrename());
			return "Student/sendeNachricht";
		}
		else {
		newMessage.setReceiver(assignmentService.getAssignmentOfStudent(accountService.getUserByMailAddress(principal.getName())).getMentor());
		newMessage.setSender(accountService.getUserByMailAddress(principal.getName()));
		newMessage.setDate(LocalDateTime.now());
		
		messenger.send(newMessage.getSender(),newMessage.getReceiver(),newMessage.getTitle(), newMessage.getContent());
		return "redirect:/student/postfachAusgang";
		}
	}
	
	@RequestMapping(value="/postfach/{type}/{index}")
	public String studentNachrichtEinsehen(@PathVariable int index,@PathVariable int type , Model model,Principal principal) {
		
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
		
		return "Student/NachrichtEinsehen";
		
	}
	
	
	
	
	// Change Request
	@GetMapping(value="/wechselantragSenden")
	public String studentWechselantrag(Model model,Principal principal) {
		
		// Check if student already sent changeRequest
		List<ChangeRequest> requests = requestService.getAllChangeRequests();
		for (ChangeRequest request : requests) {
			if (request.getStudent().equals(accountService.getUserByMailAddress(principal.getName()))) {
				model.addAttribute("antragSchonGestellt", true);
			}
		}
		model.addAttribute("request", new ChangeRequest());
		
		
		List<User> studentsWithoutMentor = assignmentService.getAllStudentsWithoutMentor();
		
		for(User student : studentsWithoutMentor) {
			if(student.getMailAddress().equals(principal.getName())) {
				model.addAttribute("mentorVorhanden",false);
			}
		}
		model.addAttribute(new ChangeRequest());
		return "Student/WechselantragSenden";
	}
	
	@PostMapping("/wechselantragSenden")
	public String studentWechselantragSenden(@ModelAttribute @Valid ChangeRequest changeRequest,Errors error, Principal principal, Model model) {
		User student = accountService.getUserByMailAddress(principal.getName());
		if(error.hasErrors()) {
			
			List<User> studentsWithoutMentor = assignmentService.getAllStudentsWithoutMentor();
			
			for(User oneStudent : studentsWithoutMentor) {
				if(oneStudent.getMailAddress().equals(principal.getName())) {
					model.addAttribute("mentorVorhanden",false);
				}
			}
			return "Student/WechselantragSenden";
			
			
		}
		
		
		requestService.requestChangeMentor(student, changeRequest.getReason());
		
		
		return "redirect:/student";
	}
	
	@RequestMapping(value="/termineEinsicht")
	public String studentTermineEinsehen(Model model ,Principal principal) {
		User student = accountService.getUserByMailAddress(principal.getName());
		List<Appointment> appointments = appointmentService.getAllAppointmentsByStudent(student);	
		model.addAttribute("appointments", appointments);
		User student2 = accountService.getUserByMailAddress(principal.getName());
		List<AppointmentProposal> appointmentProposals = appointmentService.getAllAppointmentProposalsByStudent(student2);
		model.addAttribute("appointmentProposals", appointmentProposals);
		return "Student/TermineEinsicht";
	}
	@GetMapping("/terminAbgesagt/{id}")
	public String studentTermineAbsagen(Principal principal, Model model,@PathVariable Long id) {
		Appointment appointment =appointmentService.getAppointmentById(id);
		
		appointmentService.cancelAppointmentStudent(appointment);
		
		return "redirect:/student/termineEinsicht";
	}

	@GetMapping("/TerminvorschlaegeEinsehen/{id}")
	public String VorschlagAblehnen(Principal principal, Model model,@PathVariable Long id) {
		AppointmentProposal appointment =appointmentService.getAppointmentProposalById(id);
		
		model.addAttribute("appointment",appointment);
		
		return "Student/TerminvorschlaegeEntscheiden";
	}
	
	@PostMapping("/TerminvorschlaegeEinsehen/{id}")
	public String VorschlagAnnehmen(Principal principal, Model model,@PathVariable Long id,@RequestParam String option) {
		AppointmentProposal appointment =appointmentService.getAppointmentProposalById(id);
		
		switch(option) {
		case "option1": appointmentService.acceptAppointmentProposal(appointment,appointment.getDate1());break;
		case "option2": appointmentService.acceptAppointmentProposal(appointment,appointment.getDate2());break;
		case "option3": appointmentService.acceptAppointmentProposal(appointment,appointment.getDate3());break;
		case "option4": appointmentService.rejectAppointmentProposal(appointment);
		
		}
		return "redirect:/student/termineEinsicht";
	}
	
	@RequestMapping(value="/convLogDetail/{id}")
	public String studentProtokollEinsehen(@PathVariable Long id,Model model,Principal principal) {
		ConversationLog convLog = appointmentService.getConversationLogById(id);
		
		model.addAttribute("convLog",convLog);
		model.addAttribute("mentor", convLog.getAppointment().getAssignment().getMentor());
		return "Student/ConvLogDetail";
		
	}
}
