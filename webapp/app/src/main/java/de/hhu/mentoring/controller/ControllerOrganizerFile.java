package de.hhu.mentoring.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.files.FileService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;


@Controller
@RequestMapping(value = "organizer")
public class ControllerOrganizerFile {
	
	@Autowired 
	Messenger messenger; 
	
	@Autowired 
	AccountService accountService;
	
	@Autowired
	AssignmentService assignmentService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	AppointmentService appointmentService;
	
	@Autowired
	FileService fileService;
	
	@GetMapping("/NotizenUebersicht")
	public String notizenUebersicht(Model model,Principal principal) {
		model.addAttribute("title","Notizenübersicht");
		
		model.addAttribute("studenten",accountService.getAllStudents());
		return"Organizer/Studentenakte/NotizenUebersicht";
	}
	@PostMapping("/NotizenUebersicht")
	public String PostNotizenUebersicht(Model model,Principal principal,@RequestParam String studentName) {
		model.addAttribute("title","Notizenübersicht");
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/NotizenUebersicht/" + mailAddress;
	}
	
	
	
	@GetMapping("/NotizenUebersicht/{mailAddress:.+}")
	public String notizenUebersichtHolen(@PathVariable String mailAddress,Model model,Principal principal) {
		model.addAttribute("title","Notizenübersicht");
		
		model.addAttribute("student",accountService.getUserByMailAddress(mailAddress));
		
		List<User> students = accountService.getAllStudents();
		for(User student : students) {
			if(student.getMailAddress().equals(mailAddress)) {
				students.remove(student);
				students.add(0,student);
				break;
			}
		}
		model.addAttribute("studenten",students);
		List<Note> notes = fileService.getNotesByStudent(accountService.getUserByMailAddress(mailAddress));
		model.addAttribute("Notizenliste", notes);		
		model.addAttribute("StudentVorhanden",true);
		return"Organizer/Studentenakte/NotizenUebersicht";
	}
	@PostMapping("/NotizenUebersicht/{mailAddress:.+}")
	public String PostNotizenUebersichtHolen(Model model,Principal principal,@RequestParam String studentName) {
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/NotizenUebersicht/" + mailAddress;
		
	}
	
	@GetMapping("/VereinbarungenUebersicht")
	public String vereinbarungenUebersicht(Model model,Principal principal) {
		model.addAttribute("title","Vereinbarungenübersicht");
		
		model.addAttribute("studenten",accountService.getAllStudents());
		return"Organizer/Studentenakte/VereinbarungenUebersicht";
	}
	@PostMapping("/VereinbarungenUebersicht")
	public String PostVereinbarungenUebersicht(Model model,Principal principal,@RequestParam String studentName) {
		model.addAttribute("title","Vereinbarungenübersicht");
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/VereinbarungenUebersicht/" + mailAddress;
		
	}
	
	
	@GetMapping("/VereinbarungenUebersicht/{mailAddress:.+}")
	public String vereinbarungenUebersichtHolen(@PathVariable String mailAddress,Model model,Principal principal) {
		model.addAttribute("title","Vereinbarungenübersicht");
		model.addAttribute("student",accountService.getUserByMailAddress(mailAddress));
		
		List<User> students = accountService.getAllStudents();
		for(User student : students) {
			if(student.getMailAddress().equals(mailAddress)) {
				students.remove(student);
				students.add(0,student);
				break;
			}
		}
		model.addAttribute("studenten",students);
		
		List<Agreement> agreements = fileService.getAgreementsByStudent(accountService.getUserByMailAddress(mailAddress));		
		model.addAttribute("Vereinbarungenliste", agreements);
		
		model.addAttribute("StudentVorhanden",true);
		return"Organizer/Studentenakte/VereinbarungenUebersicht";
		
	}
	
	@PostMapping("/VereinbarungenUebersicht/{mailAddress:.+}")
	public String PostVereinbarungenUebersichtHolen(Model model,Principal principal,@RequestParam String studentName) {
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/VereinbarungenUebersicht/" + mailAddress;
		
	}
	
	@GetMapping("/DokumentenUebersicht")
	public String dokumenteUebersicht(Model model,Principal principal) {
		model.addAttribute("title","Dokumentenübersicht");
		
		model.addAttribute("studenten",accountService.getAllStudents());
		return"Organizer/Studentenakte/DokumentenUebersicht";
	}
	@PostMapping("/DokumentenUebersicht")
	public String PostDokumenteUebersicht(Model model,Principal principal,@RequestParam String studentName) {
		model.addAttribute("title","Dokumentenübersicht");
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/DokumentenUebersicht/" + mailAddress;
	}
	
	
	
	@GetMapping("/DokumentenUebersicht/{mailAddress:.+}")
	public String dokumenteUebersichtHolen(@PathVariable String mailAddress,Model model,Principal principal) {
		model.addAttribute("title","Dokumentenübersicht");
		model.addAttribute("student",accountService.getUserByMailAddress(mailAddress));
		
		
		List<User> students = accountService.getAllStudents();
		for(User student : students) {
			if(student.getMailAddress().equals(mailAddress)) {
				students.remove(student);
				students.add(0,student);
				break;
			}
		}
		model.addAttribute("studenten",students);
		List<Document> documents = fileService.getDocumentsByStudent(accountService.getUserByMailAddress(mailAddress));
		model.addAttribute("Dokumentenliste", documents);
		
		model.addAttribute("StudentVorhanden",true);
		return"Organizer/Studentenakte/DokumentenUebersicht";
	}
	@PostMapping("/DokumentenUebersicht/{mailAddress:.+}")
	public String PostDokumenteUebersichtHolen(Model model,Principal principal,@RequestParam String studentName) {
		String mailAddress = "";
		for (int i = 0; i < studentName.length(); i++) {
			if (studentName.charAt(i) != ',')
				mailAddress += studentName.charAt(i);
			else
				break;
		}
		return "redirect:/organizer/DokumentenUebersicht/" + mailAddress;
		
	}
	
	
	
	
	@GetMapping("/NotizHinzufuegen/{mailAddress:.+}")
	public String notizenHinzufuegen(Model model) {
		model.addAttribute(new Note());
		
		return "Organizer/Studentenakte/NotizHinzufuegen";
	}
	
	@PostMapping("/NotizHinzufuegen/{mailAddress:.+}")
	public String PostNotizenHinzufuegen(@ModelAttribute @Valid Note note,Errors error,Model model,@PathVariable String mailAddress) {
		
		if(error.hasErrors()) {
			
			return "Organizer/Studentenakte/NotizHinzufuegen";
		}
		
		note.setFile(fileService.getFileByStudent(accountService.getUserByMailAddress(mailAddress)));
		fileService.makeNote(note.getFile(), note.getTitle(), note.getContent());
		return "redirect:/organizer/NotizenUebersicht/" + mailAddress;
	}
	
	
	@GetMapping("/DokumentHinzufuegen/{mailAddress:.+}")
	public String dokumenteHinzufuegen(Model model) {
		model.addAttribute(new Document());
		
		return "Organizer/Studentenakte/DokumentHinzufuegen";
	}
	
	@PostMapping("/DokumentHinzufuegen/{mailAddress:.+}")
	public String PostDokumenteHinzufuegen(@RequestParam String title,Model model,@PathVariable String mailAddress,@RequestParam("file") MultipartFile file,RedirectAttributes redirect) {
		if(title.equals("")||title.length()>70) {
			model.addAttribute("titelNichtAngegeben",true);
			return "Organizer/Studentenakte/DokumentHinzufuegen";
		}
		fileService.makeDocument(fileService.getFileByStudent(accountService.getUserByMailAddress(mailAddress)), title, file);
		redirect.addFlashAttribute("message", "Du hast erfolgreich" + file.getOriginalFilename() + "hochgeladen !");
		
		return "redirect:/organizer/DokumentenUebersicht/"+mailAddress;
	}
	
	@GetMapping("/VereinbarungHinzufuegen/{mailAddress:.+}")
	public String vereinbarungenHinzufuegen(Model model) {
		model.addAttribute(new Agreement());
		
		return "Organizer/Studentenakte/VereinbarungHinzufuegen";
	}
	
	@PostMapping("/VereinbarungHinzufuegen/{mailAddress:.+}")
	public String PostVereinbarungenHinzufuegen(@ModelAttribute @Valid Agreement agreement,Errors error,Model model,@PathVariable String mailAddress,@RequestParam String DeadlineDate,@RequestParam String DeadlineTime) {
		if(error.hasErrors()) {
			return "Organizer/Studentenakte/VereinbarungHinzufuegen";
		}
		agreement.setFile(fileService.getFileByStudent(accountService.getUserByMailAddress(mailAddress)));
		
		String DateAndTime = DeadlineDate + DeadlineTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
		
		File file = fileService.getFileByStudent(accountService.getUserByMailAddress(mailAddress));
		
		fileService.makeAgreement(file, agreement.getGoal(), LocalDateTime.now(), LocalDateTime.parse(DateAndTime, formatter));
		
		return "redirect:/organizer/VereinbarungenUebersicht/"+mailAddress;
	}
	
	
	@RequestMapping("/NotizAnsehen/{mailAddress:.+}/{id}")
	public String notizAnsehen(@PathVariable long id,@PathVariable String mailAddress,Model model) {
		
		model.addAttribute("note",fileService.getNoteById(id));
		model.addAttribute("mailAddress",mailAddress);
		
		return "Organizer/Studentenakte/NotizAnsehen";
	}
	@RequestMapping("/DokumentAnsehen/{mailAddress:.+}/{id}")
	public String DokumentAnsehen(@PathVariable long id,@PathVariable String mailAddress,Model model) {
		
		model.addAttribute("mailAddress",mailAddress);
		model.addAttribute("document",fileService.getDocumentById(id));
		return "Organizer/Studentenakte/DokumentAnsehen";
	}
	
	@GetMapping("/file/{mailAddress:.+}/{id:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable long id) {

        Resource file =  fileService.loadDocumentData(fileService.getDocumentById(id).getFilename());  
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
