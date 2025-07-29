package de.hhu.mentoring.services.messaging;

import java.util.List;

import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.User;

public interface Messenger {
	
	public void send(User sender, User receiver, String title, String content);
	
	public void sendReminder(User receiver, String title, String content);
	
	public List<Message> getAllReceivedMessages(User receiver);
	
	public List<Message> getAllSentMessages(User sender);
	
	public List<Message> getAllReceivedMessagesByMailAddress(String mailAddress);
	
	public List<Message> getAllSentMessagesByMailAddress(String mailAddress);
	
	public Message getMessageById(Long id);

}
