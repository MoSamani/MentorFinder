package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Message;
import de.hhu.mentoring.database.model.User;

public interface MessageRepository extends CrudRepository<Message, Long> {
	
	public List<Message> findAllMessagesBySender(User user);  
	
	public List<Message> findAllMessagesByReceiver(User user);

}
