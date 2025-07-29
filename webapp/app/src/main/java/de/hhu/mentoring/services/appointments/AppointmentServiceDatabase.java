package de.hhu.mentoring.services.appointments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.AppointmentProposal;
import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ConversationLog;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AppointmentProposalRepository;
import de.hhu.mentoring.database.repository.AppointmentRepository;
import de.hhu.mentoring.database.repository.ConversationLogRepository;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.messaging.ReminderService;

@Service
public class AppointmentServiceDatabase implements AppointmentService {

	@Autowired
	AssignmentService as;
	
	@Autowired
	AppointmentRepository ar;
	
	@Autowired
	AppointmentProposalRepository apr;
	
	@Autowired
	ReminderService rs;
	
	@Autowired
	ConversationLogRepository convLogRepo;
	
	public List<Appointment> getAllAppointmentsByStudent(User student){
		
		Assignment assignment= as.getAssignmentOfStudent(student);
		return this.getAllAppointmentsByAssignment(assignment);
		
	}
	
	public List<AppointmentProposal> getAllAppointmentProposalsByStudent(User student){
		
		Assignment assignment= as.getAssignmentOfStudent(student);
		return this.getAllAppointmentProposalsByAssignment(assignment);
		
	}
	
	public List <Appointment> getAllAppointmentsByAssignment(Assignment assignment){
		
		return ar.findAppointmentsByAssignment(assignment);	
				
	}
	
	public List <AppointmentProposal> getAllAppointmentProposalsByAssignment (Assignment assignment){
		
		return apr.findAppointmentProposalsByAssignment(assignment);
		
	}
	
	public List<Appointment> getAllAppointmentsByMentor(User mentor) {
		List<Appointment> appointments = new ArrayList<Appointment>();
		List<Assignment> assignments = as.getAllAssignmentsOfMentor(mentor);
		for(Assignment assignment: assignments) {
			appointments.addAll(getAllAppointmentsByAssignment(assignment));
		}
		return appointments;
	}
	
	public void cancelAppointmentStudent(Appointment appointment) {
		
		appointment.setCanceled(true);
		ar.save(appointment);
		rs.sendReminderAppoinmentCanceledByStudent(appointment);
		
	}
	
	public void cancelAppointmentMentor(Appointment appointment) {
		
		appointment.setCanceled(true);
		ar.save(appointment);
		rs.sendReminderAppoinmentCanceledByMentor(appointment);
		
	}
	
	public void makeAppointmentProposal(Assignment assignment, LocalDateTime date1, LocalDateTime date2, LocalDateTime date3) {
		
		AppointmentProposal appointmentProposal = new AppointmentProposal(date1, date2, date3, assignment);
		rs.sendReminderAppointmentProposalSubmitted(appointmentProposal);
		apr.save(appointmentProposal);
		
	}
	
	public void acceptAppointmentProposal(AppointmentProposal appointmentProposal, LocalDateTime acceptedDate) {
		
		rs.sendReminderAppointmentProposalAccepted(appointmentProposal, acceptedDate);
		
		Appointment appointment = new Appointment(acceptedDate, appointmentProposal.getAssignment());
		ar.save(appointment);
		
		removeAppointmentProposal(appointmentProposal);
		
	}
	
	public void rejectAppointmentProposal(AppointmentProposal appointmentProposal) {
		
		rs.sendReminderAppointmentProposalRejected(appointmentProposal);
		removeAppointmentProposal(appointmentProposal);
		
	}
	
	private void removeAppointmentProposal(AppointmentProposal appointmentProposal) {
		
		if (appointmentProposal != null) {
			appointmentProposal.setAssignment(null);
			apr.delete(appointmentProposal);
		}
		
	}
	
	public Appointment getAppointmentById(Long id) {
		
		return ar.findOne(id);
		
	}
	public AppointmentProposal getAppointmentProposalById(Long id) {
		
		return apr.findOne(id);
		
	}
	
	//ConversationLog
	
	public void makeConversationLog(Appointment appointment, String content) {
		
		ConversationLog convLog = new ConversationLog(content, appointment);
		convLogRepo.save(convLog);
		
	}
	
	public ConversationLog getConversationLogById(long id) {
		
		return convLogRepo.findOne(id);
		
	}
	
	public ConversationLog getConversationLogByAppointment(Appointment appointment) {
		
		return convLogRepo.findConversationLogByAppointment(appointment);
		
	}
			
}
