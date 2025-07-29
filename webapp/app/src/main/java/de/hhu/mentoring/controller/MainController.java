package de.hhu.mentoring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hhu.mentoring.services.messaging.ReminderService;


@Controller
@RequestMapping(value = "")
public class MainController {
	
	@Autowired
	ReminderService reminderService;

	
	@RequestMapping(value="")
	public String displayStartsite() {
		return "Startsite";
	}
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
	@RequestMapping(value="/loginError")
	public String loginError(Model model) {
		model.addAttribute("error", true);
		return "login";
	}
	
	@RequestMapping(value="weiterleiten")
	public String connectTo(Authentication auth) {
		
		if(auth.getAuthorities().toString().equals("[1]")) {
			return "redirect:/mentor";
			
		}
		else if(auth.getAuthorities().toString().equals("[0]")) {
			 
			return "redirect:/student";
		}
			else {
					return "redirect:/organizer";
			}
			
	}
	
	@RequestMapping("/AuasSchnittstelle/{prename}/{surname}")
	public String auasVorschlagEntgegennehmen(@PathVariable String prename, @PathVariable String surname) {
		reminderService.sendStudentSuggestion(prename, surname);
		return "redirect:/";
	}

}
