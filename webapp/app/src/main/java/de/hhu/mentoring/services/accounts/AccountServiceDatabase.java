package de.hhu.mentoring.services.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.files.FileService;

@Service
public class AccountServiceDatabase implements AccountService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	FileService fileService;
	
	public List<User> getAllUsers() {
		return (List<User>)userRepo.findAll();
	}
	
	public User getUserByMailAddress(String mailAddress) {
		return userRepo.findUserByMailAddress(mailAddress);
	}
	
	public List<User> getUsersByRole(Role role) {
		return userRepo.findUsersByRole(role);
	}
	
	public List<User> getAllStudents() {
		return userRepo.findUsersByRole(Role.STUDENT);
	}
	
	public List<User> getAllMentors() {
		return userRepo.findUsersByRole(Role.MENTOR);
	}
	
	public List<User> getAllOrganizers() {
		return userRepo.findUsersByRole(Role.ORGANIZER);
	}
	
	public boolean checkIfUserExists(String prename, String surname) {
		if (userRepo.findUserByPrenameAndSurname(prename, surname) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean checkIfUserExists(String mailAddress) {
		if (userRepo.findUserByMailAddress(mailAddress) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void save(User user) {
		userRepo.save(user);
		if(user.getRole().equals(Role.STUDENT)) {
			fileService.createFileForStudent(user);
		}
	}
	
	private String reminderMail = "reminder.mentoring";
	
	public User getReminderUser() {
		User reminder = getUserByMailAddress(reminderMail);
		
		// Create reminder user if needed
		if (reminder == null) {
			User r = new User("Erinnerung", "Mentoring", reminderMail, "dontuseme", Role.ORGANIZER);
			save(r);
		}
		return getUserByMailAddress(reminderMail);
	}


}
