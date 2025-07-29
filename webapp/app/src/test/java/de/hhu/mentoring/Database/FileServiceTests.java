package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Appointment;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;
import de.hhu.mentoring.database.model.Role;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AgreementRepository;
import de.hhu.mentoring.database.repository.AppointmentRepository;
import de.hhu.mentoring.database.repository.DocumentRepository;
import de.hhu.mentoring.database.repository.FileRepository;
import de.hhu.mentoring.database.repository.NoteRepository;
import de.hhu.mentoring.database.repository.UserRepository;
import de.hhu.mentoring.services.accounts.AccountService;
import de.hhu.mentoring.services.files.FileService;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileServiceTests {
	
	@Autowired
	AccountService aS;
	
	@Autowired
	FileService fS;
	
	@Autowired
    MockMvc mvc;
	
	@Autowired
	UserRepository uR;
	
	@Autowired
	FileRepository fR;
	
	@Autowired
	AgreementRepository aR;
	
	@Autowired
	DocumentRepository dR;
	
	@Autowired
	NoteRepository nR;
	
	@Autowired
	AppointmentRepository apR;
	
	
	@Before
	public void startRoutine() {
		User s= new User("A", "B", "ab@email.com", "abcd", Role.ORGANIZER);
		User s1= new User("A2", "B2", "ab2@email.com", "abcd2", Role.STUDENT);
		User s2= new User("A2", "B2", "ab3@email.com", "abcd2", Role.STUDENT);
		User s3= new User("A3", "B3", "ab4@email.com", "abcd3", Role.MENTOR);
		User s4= new User("A3", "B3", "ab5@email.com", "abcd3", Role.MENTOR);
		User s5= new User("A3", "B3", "ab6@email.com", "abcd3", Role.MENTOR);
		Appointment appointment = new Appointment();
		appointment.setId(0);
		
		apR.save(appointment);
		aS.save(s);
		aS.save(s1);
		aS.save(s2);
		aS.save(s3);
		aS.save(s4);
		aS.save(s5);
	}
	
	@After
	public void finishRoutine() {
		fR.deleteAll();
		aR.deleteAll();
		dR.deleteAll();
		nR.deleteAll();
		uR.deleteAll();
		apR.deleteAll();
	}
	
	@Test
	public void testCreateFileForStudent() {
		int s = fS.getAllFiles().size();
		assertEquals(2, s);
	}
	
	@Test
	public void testGetFileByStudent() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		assertEquals(student.getFile(), fS.getFileByStudent(student));
	}
	
	@Test
	public void testGetAllFiles() {
		User s1 = uR.findUserByMailAddress("ab2@email.com");
		User s2 = uR.findUserByMailAddress("ab3@email.com");
		
		List<File> files = new ArrayList<File>();
		files.add(s1.getFile());
		files.add(s2.getFile());
		
		assertEquals(files, fS.getAllFiles());
	}
	
	@Test
	public void testGetFileById() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		long fileId = student.getFile().getId();
		assertEquals(student.getFile(), fS.getFileById(fileId));
	}
	
	//Agreements
	
	@Test
	public void testMakeAgreement() {
		User s1 = uR.findUserByMailAddress("ab2@email.com");
		fS.makeAgreement(s1.getFile(), "blah", null, null);
		assertEquals(1, fS.getAgreementsByStudent(s1).size());
		assertEquals("blah", fS.getAgreementsByStudent(s1).get(0).getGoal());
	}
	
	@Test
	public void testGetAgreementById() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeAgreement(student.getFile(), "blah", null, null);
		List<Agreement> agreement = aR.findAgreementsByFile(student.getFile());
		long agreementId = agreement.get(0).getId();
		assertEquals(agreement.get(0), fS.getAgreementById(agreementId));
	}
	
	@Test
	public void testGetAgreementByStudent() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeAgreement(student.getFile(), "blah", null, null);
		List<Agreement> agreement = aR.findAgreementsByFile(student.getFile());
		assertEquals(agreement, fS.getAgreementsByStudent(student));
	}
	
	@Test
	public void testGetAgreementByFile() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeAgreement(student.getFile(), "blah", null, null);
		List<Agreement> agreement = aR.findAgreementsByFile(student.getFile());
		assertEquals(agreement, fS.getAgreementsByFile(student.getFile()));
	}
	
	@Test
	public void testUpdateAgreement() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeAgreement(student.getFile(), "blah", null, null);
		Agreement agreement = fS.getAgreementsByStudent(student).get(0);
		
		agreement.setGoal("blah1");
		fS.updateAgreement(agreement);
		
		assertEquals("blah1", fS.getAgreementsByStudent(student).get(0).getGoal());
	}
	
	//Documents
	
	@Test
	public void testMakeDocument() {
		User s1 = uR.findUserByMailAddress("ab2@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(s1.getFile(), "testMakeDocument", result);
		
		assertEquals(1, fS.getDocumentsByStudent(s1).size());
		assertEquals("testMakeDocument", fS.getDocumentsByStudent(s1).get(0).getTitle());
		assertTrue(fS.getDocumentsByStudent(s1).get(0).getFilename().matches(".*test.txt"));
	}
	
	@Test
	public void testGetDocumentById() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(student.getFile(), "testGetDocumentById", result);
		List<Document> document = dR.findDocumentsByFile(student.getFile());
		long documentId = document.get(0).getId();
		
		assertEquals(document.get(0), fS.getDocumentById(documentId));
	}
	
	@Test
	public void testGetDocumentByStudent() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(student.getFile(), "testGetDocumentByStudent", result);
		List<Document> document = dR.findDocumentsByFile(student.getFile());
		
		assertEquals(document, fS.getDocumentsByStudent(student));
	}
	
	@Test
	public void testGetDocumentByFile() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(student.getFile(), "testGetDocumentByFile", result);
		List<Document> document = dR.findDocumentsByFile(student.getFile());
		
		assertEquals(document, fS.getDocumentsByFile(student.getFile()));
	}
	
	@Test
	public void testLoadDocumentData() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(student.getFile(), "testLoadDocumentData", result);
		List<Document> documents = dR.findDocumentsByFile(student.getFile());
		Document document = fS.getDocumentById(documents.get(0).getId());
		Resource data = fS.loadDocumentData(document.getFilename());
		
		assertNotEquals(null, data);
	}
	
	@Test
	public void testUpdateDocument() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		MultipartFile result = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hallo".getBytes());
		fS.makeDocument(student.getFile(), "testUpdateDocument", result);
		Document document = fS.getDocumentsByStudent(student).get(0);
		document.setTitle("blah1");
		fS.updateDocument(document);
		
		assertEquals("blah1", fS.getDocumentsByStudent(student).get(0).getTitle());
	}
	
	//Notes
	
	@Test
	public void testMakeNote() {
		User s1 = uR.findUserByMailAddress("ab2@email.com");
		fS.makeNote(s1.getFile(), "blah","etwas");
		assertEquals(1, fS.getNotesByStudent(s1).size());
		assertEquals("blah", fS.getNotesByStudent(s1).get(0).getTitle());
	}
	
	@Test
	public void testGetNoteById() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeNote(student.getFile(), "blah","etwas");
		List<Note> note = nR.findNotesByFile(student.getFile());
		long noteId = note.get(0).getId();
		assertEquals(note.get(0), fS.getNoteById(noteId));
	}
	
	@Test
	public void testGetNoteByStudent() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeNote(student.getFile(), "blah","etwas");
		List<Note> notes = nR.findNotesByFile(student.getFile());
		assertEquals(notes, fS.getNotesByStudent(student));
	}
	
	@Test
	public void testGetNoteByFile() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeNote(student.getFile(), "blah","etwas");
		List<Note> notes = nR.findNotesByFile(student.getFile());
		assertEquals(notes, fS.getNotesByFile(student.getFile()));
	}
	
	@Test
	public void testUpdateNote() {
		User student = uR.findUserByMailAddress("ab3@email.com");
		fS.makeNote(student.getFile(), "blah","etwas");
		Note note = fS.getNotesByStudent(student).get(0);
		
		note.setTitle("blah1");
		fS.updateNote(note);
		
		assertEquals("blah1", fS.getNotesByStudent(student).get(0).getTitle());
	}
}
