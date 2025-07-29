package de.hhu.mentoring.services.accounts;

import java.util.List;

import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;

public interface AccountService {
	
	public List<User> getAllUsers();
	
	public User getUserByMailAddress(String mailAddress);
	
	public List<User> getUsersByRole(Role role);
	
	public List<User> getAllStudents();
	
	public List<User> getAllMentors();
	
	public List<User> getAllOrganizers();
	
	public boolean checkIfUserExists(String prename, String surname);
	
	public boolean checkIfUserExists(String mailAddress);
	
	public void save(User user);
	
	
	// Reminder User
	
	public User getReminderUser();

}
