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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hhu.mentoring.controller.ControllerStudentFile;
import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.assignments.AssignmentService;
import de.hhu.mentoring.services.files.FileService;
import de.hhu.mentoring.services.messaging.Messenger;
import de.hhu.mentoring.services.requests.RequestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerFileTests {

	private User s1 = new User("Martin","Mars","m@m.de","pw",Role.STUDENT);
	private User s2 = new User("Sebastian","Saturn","s@s.de","pw",Role.STUDENT);
	private User m1 = new User("Alex","Afrika","a@a.de","pw",Role.MENTOR);
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
	ControllerStudentFile studentControlFile;
	
	@Test
	public void WeiterleitenTest() {
		Mockito.when(principial.getName()).thenReturn(mailAddress);
		assertEquals("redirect:/student/VereinbarungenUebersicht/"+mailAddress , studentControlFile.weiterleitenAkte(principial));
	}
	
	@Test
	public void vereinbarungenUebersichtHolenTest() {
		Agreement agr = new Agreement(new File(),"Ziel", LocalDateTime.now(), LocalDateTime.now());
		List<Agreement> agrs = new ArrayList<Agreement>();
		agrs.add(agr);
		
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		Mockito.when(principial.getName()).thenReturn("principalMail");
		Mockito.when(fileService.getAgreementsByStudent(s1)).thenReturn(agrs);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		
		assertEquals("Student/Studentenakte/VereinbarungenUebersicht" , studentControlFile.vereinbarungenUebersichtHolen(mailAddress,model,principial));
		Mockito.verify(model).addAttribute("title","Vereinbarungenübersicht");
		Mockito.verify(model).addAttribute("student",s1);
		Mockito.verify(model).addAttribute("Vereinbarungenliste",agrs);
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
	}
	
	@Test
	public void dokumenteUebersichtHolenTest() {
		
		Document document = new Document(new File(),"Titel",LocalDateTime.now(),"data");
		List<Document> documents = new ArrayList<Document>();
		documents.add(document);
		
		List<User> list= new ArrayList<User>();
		list.add(s1);
		list.add(s2);
		Mockito.when(principial.getName()).thenReturn("principalMail");
		Mockito.when(accountService.getUserByMailAddress("principalMail")).thenReturn(m1);
		
		Mockito.when(fileService.getDocumentsByStudent(s1)).thenReturn(documents);
		Mockito.when(accountService.getUserByMailAddress(mailAddress)).thenReturn(s1);
		assertEquals("Student/Studentenakte/DokumentenUebersicht" ,studentControlFile.dokumenteUebersichtHolen(mailAddress,model,principial));
		Mockito.verify(model).addAttribute("title","Dokumentenübersicht");
		Mockito.verify(model).addAttribute("student",s1);
		Mockito.verify(model).addAttribute("Dokumentenliste",documents);
		Mockito.verify(accountService, times(2)).getUserByMailAddress(mailAddress);
		
	}
	
	@Test
	public void DokumentAnsehenTest() {
		File file = new File();
		file.setStudent(s1);
		Document document = new Document(file, "doc", LocalDateTime.now(), "myfile");
		Long id = 3l;
		document.setId(id);
		
		Mockito.when(fileService.getDocumentById(id)).thenReturn(document);
		
		assertEquals("Student/Studentenakte/DokumentAnsehen",studentControlFile.DokumentAnsehen(id, mailAddress, model));
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
		assertEquals(studentControlFile.serveFile(id).getHeaders().getValuesAsList(HttpHeaders.CONTENT_DISPOSITION).get(0), result);
	}
}
