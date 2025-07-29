package de.hhu.mentoring.services.requests;

import java.util.List;

import de.hhu.mentoring.database.model.Assignment;
import de.hhu.mentoring.database.model.ChangeRequest;
import de.hhu.mentoring.database.model.ClosingRequest;
import de.hhu.mentoring.database.model.User;

public interface RequestService {
	
	public ClosingRequest getClosingRequestById(Long id);
	
	public ChangeRequest getChangeRequestById(Long id);
	
	public List<ClosingRequest> getAllClosingRequests();
	
	public List<ChangeRequest> getAllChangeRequests();
	
	public boolean requestChangeMentor(User student, String reason);
	
	public boolean requestCloseCase(User mentor, String reason, Assignment assignment);
	
	public void acceptChangeRequest(ChangeRequest changeRequest);
	
	public void acceptClosingRequest(ClosingRequest closingRequest);
	
	public void rejectChangeRequest(ChangeRequest changeRequest);
	
	public void rejectClosingRequest(ClosingRequest closingRequest);
}
