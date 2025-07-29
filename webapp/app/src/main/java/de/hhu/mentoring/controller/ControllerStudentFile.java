package de.hhu.mentoring.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.appointments.AppointmentService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.files.FileService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;

@Controller
@RequestMapping(value = "student")
public class ControllerStudentFile {
	
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
	
	
	@RequestMapping("/weiterleiten")
	public String weiterleitenAkte(Principal principal) {
		String mailAddress = principal.getName();
		return "redirect:/student/VereinbarungenUebersicht/" + mailAddress;
	}
	
	
	
	@RequestMapping("/VereinbarungenUebersicht/{mailAddress:.+}")
	public String vereinbarungenUebersichtHolen(@PathVariable String mailAddress,Model model,Principal principal) {
		model.addAttribute("title","Vereinbarungenübersicht");
		model.addAttribute("student",accountService.getUserByMailAddress(mailAddress));
		
		List<Agreement> agreements = fileService.getAgreementsByStudent(accountService.getUserByMailAddress(mailAddress));		
		model.addAttribute("Vereinbarungenliste", agreements);
		return"Student/Studentenakte/VereinbarungenUebersicht";
		
	}

	
	@RequestMapping("/DokumentenUebersicht/{mailAddress:.+}")
	public String dokumenteUebersichtHolen(@PathVariable String mailAddress,Model model,Principal principal) {
		model.addAttribute("title","Dokumentenübersicht");
		model.addAttribute("student",accountService.getUserByMailAddress(mailAddress));
		
		List<Document> documents = fileService.getDocumentsByStudent(accountService.getUserByMailAddress(mailAddress));
		model.addAttribute("Dokumentenliste", documents);
		return"Student/Studentenakte/DokumentenUebersicht";
	}

		
	@RequestMapping("/DokumentAnsehen/{mailAddress:.+}/{id}")
	public String DokumentAnsehen(@PathVariable long id,@PathVariable String mailAddress,Model model) {
		
		model.addAttribute("mailAddress",mailAddress);
		model.addAttribute("document",fileService.getDocumentById(id));
		return "Student/Studentenakte/DokumentAnsehen";
	}
	
	@GetMapping("/file/{mailAddress:.+}/{id:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable long id) {

        Resource file =  fileService.loadDocumentData(fileService.getDocumentById(id).getFilename());  
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
