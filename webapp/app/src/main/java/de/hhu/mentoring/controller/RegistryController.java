package de.hhu.mentoring.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.messaging.Messenger;


@Controller
@RequestMapping("registrieren")
public class RegistryController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	Messenger messengerSercive;
	
	@GetMapping("/student")
	public String displayRegistryStudent(Model model) {
		model.addAttribute("user",new User());
		model.addAttribute("EmailVergeben", false);
		model.addAttribute("Title", "Registrierung Student");
		model.addAttribute("student",true);
		return "Registrierung";
	}

	@PostMapping("/student")
	public String createStudent(@ModelAttribute @Valid User newUser,Errors errors,Model model,@RequestParam String nachricht) {
		if(errors.hasErrors()==true) {
			model.addAttribute("EmailVergeben", false);
			model.addAttribute("Title", "Registrierung Student");
			model.addAttribute("student",true);
			return "Registrierung";
		}
		
		else if(!accountService.checkIfUserExists(newUser.getMailAddress())){
			
			int mailLength = newUser.getMailAddress().length();
			for(int i=0;i<mailLength;i++) {
				if (newUser.getMailAddress().charAt(i) == ',') {
					model.addAttribute("Kommas",true);
					model.addAttribute("student",true);
					
					return "Registrierung";
				}
			}	
		
			if(!nachricht.equals("")) {
				if(nachricht.length()>254) {
					model.addAttribute("student",true);
					model.addAttribute("zuLang",true);
					return "Registrierung";
				}
				String NewStudentNachricht = "Es gibt einen neuen Student " + newUser.getPrename() + " " + newUser.getSurname();
				List<User> allOrganizer = accountService.getAllOrganizers();
				for(User organizer : allOrganizer) {
					messengerSercive.sendReminder(organizer,NewStudentNachricht, nachricht);
				}
			}
			newUser.setRole(Role.STUDENT);
			accountService.save(newUser);
			return "Startsite";
			
		}
		
		else {
			model.addAttribute("Title", "Registrierung Student");
			model.addAttribute("EmailVergeben",true);
			model.addAttribute("student",true);
			return "Registrierung";
		}
	}
	@GetMapping("/mentor")
	public String displayRegistryMentor(Model model) {
		model.addAttribute("user",new User());
		model.addAttribute("EmailVergeben", false);
		model.addAttribute("Title", "Registrierung Mentor");
		return "Registrierung";
	}
	@PostMapping("/mentor")
	public String createMentor(@ModelAttribute @Valid User newUser,Errors errors,Model model) {
		
		if(errors.hasErrors()==true) {
			model.addAttribute("EmailVergeben", false);
			model.addAttribute("Title", "Registrierung Mentor");
			return "Registrierung";
		}
		else if(!accountService.checkIfUserExists(newUser.getMailAddress())){
			
			int mailLength = newUser.getMailAddress().length();
			for(int i=0;i<mailLength;i++) {
				if (newUser.getMailAddress().charAt(i) == ',') {
					model.addAttribute("Kommas",true);
					return "Registrierung";
				}
			}
			newUser.setRole(Role.MENTOR);
			accountService.save(newUser);
			return "Startsite";
		}
		else{
			model.addAttribute("EmailVergeben",true);
			model.addAttribute("Title", "Registrierung Mentor");
			return "Registrierung";
		}
	}
	
	@GetMapping("/organizer")
	public String displayRegistryOrganizer(Model model) {
		model.addAttribute("user",new User());
		model.addAttribute("EmailVergeben", false);
		model.addAttribute("Title", "Registrierung Organisation");
		return "Registrierung";
	}
	
	@PostMapping("/organizer")
	public String createOrganizer(@ModelAttribute @Valid User newUser,Errors errors,Model model) {
		if(errors.hasErrors()==true) {
			model.addAttribute("EmailVergeben", false);
			model.addAttribute("Title", "Registrierung Organisation");
			return "Registrierung";
		}
		else if(!accountService.checkIfUserExists(newUser.getMailAddress())){
			
			int mailLength = newUser.getMailAddress().length();
			for(int i=0;i<mailLength;i++) {
				if (newUser.getMailAddress().charAt(i) == ',') {
					model.addAttribute("Kommas",true);
					return "Registrierung";
				}
			}
			newUser.setRole(Role.ORGANIZER);
			accountService.save(newUser);
			return "Startsite";
		}
		else{
			model.addAttribute("EmailVergeben",true);
			model.addAttribute("Title", "Registrierung Organisation");
			return "Registrierung";
		}
	}
		
}