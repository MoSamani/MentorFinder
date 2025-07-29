package de.hhu.mentoring.database.repository;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.ConversationLog;

public interface ConversationLogRepository extends CrudRepository<ConversationLog, Long>{
	
	public ConversationLog findConversationLogByAppointment(Appointment appointment);
	
}
