package de.hhu.mentoring.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hhu.mentoring.controller.ControllerOrganizerFile;
import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.files.FileService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizerControllerFileTests {
	
	private User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
	private User s2 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
	private String mailAddress = "m@m.de";
	
	@Mock
	Resource resource;
	@Mock
	MultipartFile file;
	@Mock
	RedirectAttributes re;
	@Mock
	FileService fileService;
	@Mock
	Model model;
	@Mock
	Errors error;
	@Mock
	Principal principial;
	@Mock 
	Messenger messenger;
	@Mock 
	AccountService accountService;
	@Mock 
	AssignmentService assignmentService;
	@Mock 
	RequestService requestService;
	@InjectMocks
	ControllerOrganizerFile organizerControlFile;
	
	@Test
	public void notizenUebersichtTest() {
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		assertEquals("Organizer/Studentenakte/NotizenUebersicht" , organizerControlFile.notizenUebersicht(model,principial));
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("title","Notizenübersicht");
	}
	@Test
	public void PostNotizenUebersichtTest() {
		assertEquals("redirect:/organizer/NotizenUebersicht/"+mailAddress , organizerControlFile.PostNotizenUebersicht(model,principial,mailAddress));
		Mockito.verify(model).addAttribute("title","Notizenübersicht");
	}
	@Test
	public void notizenUebersichtHolenTest() {
		
		Note note = new Note(new File(),"Titel", "Inhalt", LocalDateTime.now());
		List<Note> notes = new ArrayList<Note>();
		notes.add(note);
		
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		
		Mockito.when(fileService.getNotesByStudent(s1)).thenReturn(notes);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("Organizer/Studentenakte/NotizenUebersicht" , organizerControlFile.notizenUebersichtHolen(mailAddress,model,principial));
		Mockito.verify(model).addAttribute("title","Notizenübersicht");
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("student",s1);
		Mockito.verify(model).addAttribute("Notizenliste",notes);
		Mockito.verify(model).addAttribute("StudentVorhanden",true);
		Mockito.verify(accountService).getAllStudents();
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
		
	}
	@Test
	public void PostNotizenUebersichtHolenTest() {
		assertEquals("redirect:/organizer/NotizenUebersicht/"+mailAddress , organizerControlFile.PostNotizenUebersichtHolen(model,principial,mailAddress));
		
	}
	@Test
	public void vereinbarungenUebersichtTest() {
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		assertEquals("Organizer/Studentenakte/VereinbarungenUebersicht" , organizerControlFile.vereinbarungenUebersicht(model,principial));
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("title","Vereinbarungenübersicht");
	}
	@Test
	public void PostVereinbarungenUebersichtTest() {
		assertEquals("redirect:/organizer/VereinbarungenUebersicht/"+mailAddress , organizerControlFile.PostVereinbarungenUebersicht(model,principial,mailAddress));
		Mockito.verify(model).addAttribute("title","Vereinbarungenübersicht");
	}
	@Test
	public void vereinbarungenUebersichtHolenTest() {
		Agreement agr = new Agreement(new File(),"Ziel", LocalDateTime.now(), LocalDateTime.now());
		List<Agreement> agrs = new ArrayList<Agreement>();
		agrs.add(agr);
		
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		
		Mockito.when(fileService.getAgreementsByStudent(s1)).thenReturn(agrs);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("Organizer/Studentenakte/VereinbarungenUebersicht" , organizerControlFile.vereinbarungenUebersichtHolen(mailAddress,model,principial));
		Mockito.verify(model).addAttribute("title","Vereinbarungenübersicht");
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("student",s1);
		Mockito.verify(model).addAttribute("Vereinbarungenliste",agrs);
		Mockito.verify(model).addAttribute("StudentVorhanden",true);
		Mockito.verify(accountService).getAllStudents();
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
	}
	@Test
	public void PostVereinbarungenUebersichtHolenTest() {
		assertEquals("redirect:/organizer/VereinbarungenUebersicht/"+mailAddress , organizerControlFile.PostVereinbarungenUebersichtHolen(model,principial,mailAddress));
		
	}
	@Test
	public void dokumenteUebersichtTest() {
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		assertEquals("Organizer/Studentenakte/DokumentenUebersicht" , organizerControlFile.dokumenteUebersicht(model,principial));
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("title","Dokumentenübersicht");
	}
	@Test
	public void PostDokumenteUebersichtTest() {
		assertEquals("redirect:/organizer/DokumentenUebersicht/"+mailAddress , organizerControlFile.PostDokumenteUebersicht(model,principial,mailAddress));
		Mockito.verify(model).addAttribute("title","Dokumentenübersicht");
	}

	@Test
	public void dokumenteUebersichtHolenTest() {
		
		Document document = new Document(new File(),"Titel",LocalDateTime.now(),"data");
		List<Document> documents = new ArrayList<Document>();
		documents.add(document);
		
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		
		Mockito.when(fileService.getDocumentsByStudent(s1)).thenReturn(documents);
		Mockito.when(accountService.getAllStudents()).thenReturn(list);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("Organizer/Studentenakte/DokumentenUebersicht" , organizerControlFile.dokumenteUebersichtHolen(mailAddress,model,principial));
		Mockito.verify(model).addAttribute("title","Dokumentenübersicht");
		Mockito.verify(model).addAttribute("studenten",list);
		Mockito.verify(model).addAttribute("student",s1);
		Mockito.verify(model).addAttribute("Dokumentenliste",documents);
		Mockito.verify(model).addAttribute("StudentVorhanden",true);
		Mockito.verify(accountService).getAllStudents();
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
		
	}
	@Test
	public void PostDokumenteUebersichtHolenTest() {
		assertEquals("redirect:/organizer/DokumentenUebersicht/"+mailAddress , organizerControlFile.PostDokumenteUebersichtHolen(model,principial,mailAddress));
		
	}
	@Test
	public void notizenHinzufuegenTest() {
		assertEquals("Organizer/Studentenakte/NotizHinzufuegen" , organizerControlFile.notizenHinzufuegen(model));
		Mockito.verify(model).addAttribute(new Note());
	}
	@Test
	public void PostNotizenHinzufuegenWrongModelTest() {
		Note note = new Note(new File(),"Titel", "Inhalt", LocalDateTime.now());
		Mockito.when(error.hasErrors()).thenReturn(true);
		assertEquals("Organizer/Studentenakte/NotizHinzufuegen" , organizerControlFile.PostNotizenHinzufuegen(note,error,model,mailAddress));
		Mockito.verify(error).hasErrors();
	}
	@Test
	public void PostNotizenHinzufuegenRightModelTest() {
		Note note = new Note(new File(),"Titel", "Inhalt", LocalDateTime.now());
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("redirect:/organizer/NotizenUebersicht/"+mailAddress , organizerControlFile.PostNotizenHinzufuegen(note,error,model,mailAddress));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(fileService, times(1)).getFileByStudent(s1);
		
	}
	@Test
	public void dokumenteHinzufuegenTest() {
		assertEquals("Organizer/Studentenakte/DokumentHinzufuegen" , organizerControlFile.dokumenteHinzufuegen(model));
		Mockito.verify(model).addAttribute(new Document());
	}
	@Test
	public void PostDokumenteHinzufuegenWrongModelTest() {
		MultipartFile data = new MockMultipartFile("file", "test.txt", "text/plain", "PostDokumenteHinzufuegenWrongModelTest".getBytes());
		assertEquals("Organizer/Studentenakte/DokumentHinzufuegen" , organizerControlFile.PostDokumenteHinzufuegen("",model,mailAddress,data,  re));
		Mockito.verify(model).addAttribute("titelNichtAngegeben",true);
	}
	
	@Test
	public void PostDokumenteHinzufuegenRightModelTest() {
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("redirect:/organizer/DokumentenUebersicht/"+mailAddress , organizerControlFile.PostDokumenteHinzufuegen("titel",model,mailAddress,file,  re));
		Mockito.verify(accountService).getUserByMailAddress(mailAddress);
		Mockito.verify(fileService).getFileByStudent(s1);
		
	}
	@Test
	public void vereinbarungenHinzufuegenTest() {
		assertEquals("Organizer/Studentenakte/VereinbarungHinzufuegen" , organizerControlFile.vereinbarungenHinzufuegen(model));
		Mockito.verify(model).addAttribute(new Agreement());
	}
	@Test
	public void PostVereinbarungenHinzufuegenWrongModelTest() {
		Agreement agreement = new Agreement(new File(),"goal",LocalDateTime.now(),LocalDateTime.now());
		Mockito.when(error.hasErrors()).thenReturn(true);
		assertEquals("Organizer/Studentenakte/VereinbarungHinzufuegen" , organizerControlFile.PostVereinbarungenHinzufuegen(agreement,error,model,mailAddress,"2000-10-10","22:22"));
		Mockito.verify(error).hasErrors();
	}
	@Test
	public void PostVereinbarungenHinzufuegenRightModelTest() {
		Agreement agreement = new Agreement(new File(),"goal",LocalDateTime.now(),LocalDateTime.now());
		
		Mockito.when(error.hasErrors()).thenReturn(false);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("redirect:/organizer/VereinbarungenUebersicht/"+mailAddress , organizerControlFile.PostVereinbarungenHinzufuegen(agreement,error,model,mailAddress,"2000-10-10","22:22"));
		Mockito.verify(error).hasErrors();
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
		Mockito.verify(fileService, times(2)).getFileByStudent(s1);
		
	}
	@Test
	public void notizAnsehenTest() {
		File file = new File();
		file.setStudent(s1);
		Note note = new Note(file, "title", "content", LocalDateTime.now());
		Long id = 3l;
		note.setId(id);
		
		Mockito.when(fileService.getNoteById(id)).thenReturn(note);
		
		assertEquals("Organizer/Studentenakte/NotizAnsehen",organizerControlFile.notizAnsehen(id, mailAddress, model));
		Mockito.verify(model).addAttribute("note",note);
		Mockito.verify(model).addAttribute("mailAddress",mailAddress);
	}
	@Test
	public void DokumentAnsehenTest() {
		File file = new File();
		file.setStudent(s1);
		Document document = new Document(file, "doc", LocalDateTime.now(), "myfile");
		Long id = 3l;
		document.setId(id);
		
		Mockito.when(fileService.getDocumentById(id)).thenReturn(document);
		
		assertEquals("Organizer/Studentenakte/DokumentAnsehen",organizerControlFile.DokumentAnsehen(id, mailAddress, model));
		Mockito.verify(model).addAttribute("mailAddress",mailAddress);
		Mockito.verify(model).addAttribute("document",document);
	}
	@Test
	public void serveFileTest() {
		File file = new File();
		file.setStudent(s1);
		Document document = new Document(file, "doc", LocalDateTime.now(), "myfile");
		Long id = 3l;
		document.setId(id);
		
		Mockito.when(fileService.getDocumentById(id)).thenReturn(document);
		Mockito.when(fileService.loadDocumentData("myfile")).thenReturn(resource);
		Mockito.when(resource.getFilename()).thenReturn("myfile");
		
		String result = "attachment; filename=\"" + resource.getFilename() + "\"";
		assertEquals(organizerControlFile.serveFile(id).getHeaders().getValuesAsList(HttpHeaders.CONTENT_DISPOSITION).get(0), result);
	}
	
}
