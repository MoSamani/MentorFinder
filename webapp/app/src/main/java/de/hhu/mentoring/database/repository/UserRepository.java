package de.hhu.mentoring.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public List<User> findUsersByRole(Role role);
	
	public User findUserByPrenameAndSurname(String prename, String surname);
	
	public User findUserByMailAddress(String mailAddress);
	
}

