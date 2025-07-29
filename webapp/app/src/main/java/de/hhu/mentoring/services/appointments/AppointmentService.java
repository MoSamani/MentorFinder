package de.hhu.mentoring.services.appointments;

import java.time.LocalDateTime;
import java.util.List;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ConversationLog;
import de.hhu.mentoring.database.model.User;

public interface AppointmentService {

	public Appointment getAppointmentById(Long id);
	
	public AppointmentProposal getAppointmentProposalById(Long id);
	
	public List<Appointment> getAllAppointmentsByStudent(User student);
	
	public List<AppointmentProposal> getAllAppointmentProposalsByStudent(User student);
	
	public List <Appointment> getAllAppointmentsByAssignment(Assignment assignment);
	
	public List <AppointmentProposal> getAllAppointmentProposalsByAssignment (Assignment assignment);
	
	public List<Appointment> getAllAppointmentsByMentor(User mentor);
	
	public void makeAppointmentProposal(Assignment assignment, LocalDateTime date1, LocalDateTime date2, LocalDateTime date3);
	
	public void acceptAppointmentProposal(AppointmentProposal appointmentProposal, LocalDateTime acceptedDate);
	
	public void rejectAppointmentProposal(AppointmentProposal appointmentProposal);
	
	public void cancelAppointmentStudent(Appointment appointment);
	
	public void cancelAppointmentMentor(Appointment appointment);
	
	//ConversationLog
	
	public void makeConversationLog(Appointment appointment, String content);
	
	public ConversationLog getConversationLogById(long id);
	
	public ConversationLog getConversationLogByAppointment(Appointment appointment);
		
	
}
