package de.hhu.mentoring.services.requests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.ChangeRequestRepository;
import de.hhu.mentoring.database.repository.ClosingRequestRepository;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.messaging.ReminderService;

@Service
public class RequestServiceDatabase implements RequestService {
	
	@Autowired
	ChangeRequestRepository changeRepo;
	
	@Autowired
	ClosingRequestRepository closingRepo;
	
	@Autowired
	AssignmentService assignmentService;
	
	@Autowired
	ReminderService reminder;
	
	public List<ClosingRequest> getAllClosingRequests() {
		return (List<ClosingRequest>)closingRepo.findAll();
	}
	
	public List<ChangeRequest> getAllChangeRequests() {
		return (List<ChangeRequest>)changeRepo.findAll();
	}
	
	public boolean requestChangeMentor(User student, String reason) {
		Assignment a = assignmentService.getAssignmentOfStudent(student);
		if (student.getRole() == Role.STUDENT && a != null) {
			ChangeRequest changeRequest = new ChangeRequest(reason, student, assignmentService.getAssignmentOfStudent(student));
			changeRepo.save(changeRequest);
			
			// Send reminder
			reminder.sendReminderChangeRequestEntered(changeRequest); 
			return true;
		} else {
			return false;
		}
	}
	
	public boolean requestCloseCase(User mentor, String reason, Assignment assignment) {
		if (assignment.getMentor().equals(mentor)) {
			ClosingRequest closingRequest = new ClosingRequest(reason, mentor, assignment);
			closingRepo.save(closingRequest);
			
			// Send reminder
			reminder.sendReminderClosingRequestEntered(closingRequest);
			return true;
		} else {
			return false;
		}
		
	}
	
	public void acceptChangeRequest(ChangeRequest changeRequest) {
		// Send reminder
		reminder.sendReminderChangeRequestAccepted(changeRequest);
		
		// No need to remove request manually because it gets removed through cascading by removing assignment
		Assignment a = changeRequest.getAssignment();
		assignmentService.removeAssignment(a);
	}
	
	public void acceptClosingRequest(ClosingRequest closingRequest) {
		// Send reminder
		reminder.sendReminderClosingRequestAccepted(closingRequest);
		
		// No need to remove request manually because it gets removed through cascading by removing assignment
		Assignment a = closingRequest.getAssignment();
		assignmentService.removeAssignment(a);
	}
	
	public void rejectChangeRequest(ChangeRequest changeRequest) {
		// Send reminder
		reminder.sendReminderChangeRequestRejected(changeRequest);
		
		removeChangeRequest(changeRequest);
	}
	
	public void rejectClosingRequest(ClosingRequest closingRequest) {
		// Send reminder
		reminder.sendReminderClosingRequestRejected(closingRequest);
		
		removeClosingRequest(closingRequest);
	}
	
	private void removeChangeRequest(ChangeRequest changeRequest) {
		if (changeRequest != null) {
			changeRequest.setAssignment(null);
			changeRepo.delete(changeRequest);
		}
	}
	
	private void removeClosingRequest(ClosingRequest closingRequest) {
		if (closingRequest != null) {
			closingRequest.setAssignment(null);
			closingRepo.delete(closingRequest);
		}
	}

	
	public ClosingRequest getClosingRequestById(Long id) {
		return closingRepo.findOne(id);
	}
	
	public ChangeRequest getChangeRequestById(Long id) {
		return changeRepo.findOne(id);
	}

}
