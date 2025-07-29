package de.hhu.mentoring.services.messaging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.MessageRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;

@Service
public class MessengerDatabase implements Messenger {
	
	@Autowired
	MessageRepository messageRepo;
	
	@Autowired
	AccountService accountService;
	
	@Autowired 
	AppointmentService appointmentService;
	
	@Autowired
	ReminderService reminderService;
	
	public void send(User sender, User receiver, String title, String content) {
		Message m = new Message(title, content, LocalDateTime.now(), sender, receiver);
		messageRepo.save(m);
	}
	
	public void sendReminder(User receiver, String title, String content) {
		User reminder = accountService.getReminderUser();
		send(reminder, receiver, title, content);
	}
	
	public List<Message> getAllReceivedMessages(User receiver) {
		List<Message> allMessages = getAllAppointmentReminders(receiver);
		List<Message> dbMessages = messageRepo.findAllMessagesByReceiver(receiver);
		Collections.reverse(dbMessages);
		allMessages.addAll(dbMessages);
		return allMessages;
	}
	
	public List<Message> getAllSentMessages(User sender) {
		List<Message> dbMessages = messageRepo.findAllMessagesBySender(sender);
		Collections.reverse(dbMessages);
		return dbMessages;
	}
	
	public List<Message> getAllReceivedMessagesByMailAddress(String mailAddress) {
		User u = accountService.getUserByMailAddress(mailAddress);
		return this.getAllReceivedMessages(u);
	}
	
	public List<Message> getAllSentMessagesByMailAddress(String mailAddress) {
		User u = accountService.getUserByMailAddress(mailAddress);
		return this.getAllSentMessages(u);
	}
	
	public Message getMessageById(Long id) {
		return messageRepo.findOne(id);
	}
	
	
	private List<Message> getAllAppointmentReminders(User user){
		
		List<Message> messages = new ArrayList<Message>();
		List<Appointment> appointments = new ArrayList<Appointment>();
		
		if(user.getRole().equals(Role.MENTOR)) {
			appointments = appointmentService.getAllAppointmentsByMentor(user);
		}else if(user.getRole().equals(Role.STUDENT)){
			appointments = appointmentService.getAllAppointmentsByStudent(user);
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		for(Appointment appointment: appointments) {
			
			LocalDateTime reminderDate = appointment.getDate().minusDays(1l);
			if(now.isAfter(reminderDate) && now.isBefore(appointment.getDate())) {
				Message m = reminderService.getAppointmentReminder(appointment, user, reminderDate);
				messages.add(m);
			}
		}
		
		return messages;
	}
}
