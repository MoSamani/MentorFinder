package de.hhu.mentoring.services.messaging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;

@Service
public class ReminderService {
	
	@Autowired
	Messenger messenger;
	
	@Autowired
	AccountService accountService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	
	
	public void sendReminderNewAssignment(Assignment assignment) {
		User student = assignment.getStudent();
		User mentor = assignment.getMentor();
		
		String titleStudent = "Ihnen wurde ein neuer Mentor zugewiesen";
		String titleMentor = "Ihnen wurde ein neuer Student zugewiesen";
		String contentStudent = "Ihnen wurde ein Mentor zugewiesen! Ab sofort ist " + mentor.getPrename() + " " + mentor.getSurname() + " für Sie zuständig.";
		String contentMentor = "Ihnen wurde ein Student zugewiesen! Ab sofort sind Sie für " + student.getPrename() + " " + student.getSurname() + " zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, titleStudent, contentStudent);
		messenger.sendReminder(mentor, titleMentor, contentMentor);
	}
	
	public void sendReminderAssignmentDeletedByOrganizer(Assignment assignment) {
		User student = assignment.getStudent();
		User mentor = assignment.getMentor();
		
		String titleStudent = "Die Beziehung zu Ihrem Mentor wurde aufgelöst";
		String titleMentor = "Die Beziehung zu einem Ihrer Studenten wurde aufgelöst";
		String contentStudent = "Ein Organisator hat die Beziehung zu Ihrem Mentor aufgelöst. Ab sofort ist " + mentor.getPrename() + " " + mentor.getSurname() + " nicht mehr für Sie zuständig.";
		String contentMentor = "Ein Organisator hat die Beziehung zu Ihrem Studenten " + student.getPrename() + " " + student.getSurname() + " aufgelöst. Ab sofort sind Sie nicht mehr für Ihn zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, titleStudent, contentStudent);
		messenger.sendReminder(mentor, titleMentor, contentMentor);
	}
	
	public void sendReminderChangeRequestEntered(ChangeRequest changeRequest) {
		User student = changeRequest.getStudent();
		User mentor = changeRequest.getAssignment().getMentor();
		
		String title = "Antrag auf Mentorenwechsel wurde gestellt";
		String contentStudent = "Ihr Antrag auf Mentorenwechsel für Ihren Mentor " + mentor.getPrename() + " " + mentor.getSurname() + " wurde eingereicht. Sie werden in den nächsten Tagen erfahren ob dieser angenommen oder abgelehnt wurde.";
		String contentMentor = "Ihr Student " + student.getPrename() + " " + student.getSurname() + " hat einen Antrag auf Mentorenwechsel gestellt. Sie werden in den nächsten Tagen erfahren ob dieser angenommen oder abgelehnt wurde.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	public void sendReminderChangeRequestAccepted(ChangeRequest changeRequest) {
		User student = changeRequest.getStudent();
		User mentor = changeRequest.getAssignment().getMentor();
		
		String title = "Antrag auf Mentorenwechsel wurde angenommen";
		String contentStudent = "Ihr Antrag auf Mentorenwechsel wurde angenommen. Ihr aktuelle Mentor wurde von Ihrem Fall abgezogen und es wird Ihnen in den nächsten Tagen ein neuer zugewiesen.";
		String contentMentor = "Der Antrag auf Mentorenwechsel des Studenten " + student.getPrename() + " " + student.getSurname() + " wurde angenommen. Sie sind also nicht weiter für diesen Studenten zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	public void sendReminderChangeRequestRejected(ChangeRequest changeRequest) {
		User student = changeRequest.getStudent();
		User mentor = changeRequest.getAssignment().getMentor();
		
		String title = "Antrag auf Mentorenwechsel wurde abgelehnt";
		String contentStudent = "Ihr Antrag auf Mentorenwechsel wurde abgelehnt. Ihr Mentor ist also weiterhin für Sie zuständig.";
		String contentMentor = "Der Antrag auf Mentorenwechsel des Studenten " + student.getPrename() + " " + student.getSurname() + " wurde abgehlent. Sie sind also weiterhin für diesen Studenten zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	
	
	public void sendReminderClosingRequestEntered(ClosingRequest closingRequest) {
		User student = closingRequest.getAssignment().getStudent();
		User mentor = closingRequest.getMentor();
		
		String title = "Antrag auf Fallabschluss wurde gestellt";
		String contentStudent = "Ihr Mentor " + mentor.getPrename() + " " + mentor.getSurname() + " hat einen Antrag auf Fallabschluss gestellt. Sie werden in den nächsten Tagen erfahren ob dieser angenommen oder abgelehnt wurde.";
		String contentMentor = "Ihr Antrag auf Fallabschluss für den Studenten " + student.getPrename() + " " + student.getSurname() + " wurde eingereicht. Sie werden in den nächsten Tagen erfahren ob dieser angenommen oder abgelehnt wurde.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	public void sendReminderClosingRequestAccepted(ClosingRequest closingRequest) {
		User student = closingRequest.getAssignment().getStudent();
		User mentor = closingRequest.getMentor();
		
		String title = "Antrag auf Fallabschluss wurde angenommen";
		String contentStudent = "Der Antrag auf Fallabschluss Ihres Mentors " + mentor.getPrename() + " " + mentor.getSurname() + " wurde angenommen, ihr Mentor wurde von Ihrem Fall abgezogen. Ggf. wird Ihnen in den nächsten Tagen ein neuer Mentor zugewiesen.";
		String contentMentor = "Ihr Antrag auf Fallabschluss wurde angenommen. Sie sind also nicht mehr für den Studenten " + student.getPrename() + " " + student.getSurname() + " zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	public void sendReminderClosingRequestRejected(ClosingRequest closingRequest) {
		User student = closingRequest.getAssignment().getStudent();
		User mentor = closingRequest.getMentor();
		
		String title = "Antrag auf Fallabschluss wurde abgelehnt";
		String contentStudent = "Der Antrag auf Fallabschluss Ihres Mentors " + mentor.getPrename() + " " + mentor.getSurname() + " wurde abgelehnt, ihr Mentor ist also weiterhin für Sie zuständig.";
		String contentMentor = "Ihr Antrag auf Fallabschluss wurde abgelehnt. Sie sind also weiterhin für diesen Studenten " + student.getPrename() + " " + student.getSurname() + " zuständig.";
		
		// Send reminder to student
		messenger.sendReminder(student, title, contentStudent);
		messenger.sendReminder(mentor, title, contentMentor);
	}
	
	public void sendReminderAppointmentProposalSubmitted(AppointmentProposal appointmentProposal) {
		User student = appointmentProposal.getAssignment().getStudent();
		User mentor = appointmentProposal.getAssignment().getMentor();
		
		String title = "Sie haben neue Terminvorschläge";
		String content = "Ihr Mentor "+mentor.getPrename()+" "+mentor.getSurname()+" hat ihnen 3 neue Termine vorgeschlagen, Sie können diese in der Terminansicht einsehen.";
		
		
		// Send reminder to mentor
		messenger.sendReminder(student, title, content);
	}
	
	public void sendReminderAppoinmentCanceledByStudent(Appointment appointment) {
		
		User student = appointment.getAssignment().getStudent();
		User mentor = appointment.getAssignment().getMentor();
		
		String title = student.getPrename()+" "+student.getSurname()+" hat einen Termin abgesagt";
		String content = "Ihr Termin mit dem Studenten "+student.getPrename()+" "+student.getSurname()+" am "+appointment.getDate().format(formatter)+" wurde durch den Studenten abgesagt.";
		
		
		// Send reminder to mentor
		messenger.sendReminder(mentor, title, content);
		
	}
	
	public void sendReminderAppoinmentCanceledByMentor(Appointment appointment) {
		
		User student = appointment.getAssignment().getStudent();
		User mentor = appointment.getAssignment().getMentor();
		
		String title = mentor.getPrename()+" "+mentor.getSurname()+" hat einen Termin abgesagt";
		String content = "Ihr Termin mit ihrem Mentor "+mentor.getPrename()+" "+mentor.getSurname()+" am "+appointment.getDate().format(formatter)+" wurde durch den Mentor abgesagt.";
		
		
		// Send reminder to student
		messenger.sendReminder(student, title, content);
		
		
	}
	
	public void sendReminderAppointmentProposalAccepted(AppointmentProposal appointmentProposal, LocalDateTime acceptedDate) {
		
		User student = appointmentProposal.getAssignment().getStudent();
		User mentor = appointmentProposal.getAssignment().getMentor();
		
		String title = student.getPrename()+" "+student.getSurname()+" hat ihren Terminvorschlag angenommen";
		String content = "Ihr Terminvorschlag für ihren Studenten "+student.getPrename()+" "+student.getSurname()+" am "+acceptedDate.format(formatter)+" wurde angenommen.";
		
		
		// Send reminder to mentor
		messenger.sendReminder(mentor, title, content);
		
	}
	
	public void sendReminderAppointmentProposalRejected(AppointmentProposal appointmentProposal) {
		
		User student = appointmentProposal.getAssignment().getStudent();
		User mentor = appointmentProposal.getAssignment().getMentor();
		
		String title = student.getPrename()+" "+student.getSurname()+" hat ihren Terminvorschlag abgelehnt";
		String content = "Ihre Terminvorschläge für ihren Studenten "+student.getPrename()+" "+student.getSurname()+" wurden abgelehnt.";
		
		
		// Send reminder to mentor
		messenger.sendReminder(mentor, title, content);
		
	}
	
	public Message getAppointmentReminder(Appointment appointment, User receiver, LocalDateTime reminderDate) {
		User reminder = accountService.getReminderUser();
		User mentor = appointment.getAssignment().getMentor();
		User student = appointment.getAssignment().getStudent();
		
		String title = "Erinnerung an ihren nächsten Termin";
		
		String content;
		
		if(receiver.equals(student)) {
			content="Ihr nächster Termin findet am "+appointment.getDate().format(formatter)+" mit Ihrem Mentor "+mentor.getPrename()+" "+mentor.getSurname()+" statt.";	
		}else {
			content="Ihr nächster Termin findet am "+appointment.getDate().format(formatter)+" mit Ihrem Studenten "+student.getPrename()+" "+student.getSurname()+" statt.";
		}
		
		return new Message(title, content, reminderDate, reminder, receiver);
	}
	
	public void sendStudentSuggestion(String prename, String surname) {
		List<User> organizers = new ArrayList<User>();
		organizers.addAll(accountService.getAllOrganizers());
		String title = "Neuer Student";
		String content = prename +" "+ surname + " wurde von AUAS als Student für das Mentoring System vorgeschlagen.";
	 
		for(User organizer: organizers) {
			messenger.sendReminder(organizer, title, content);
		}
	}
}
