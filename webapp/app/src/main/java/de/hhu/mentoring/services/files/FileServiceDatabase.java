package de.hhu.mentoring.services.files;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;
import de.hhu.mentoring.database.model.User;
import de.hhu.mentoring.database.repository.AgreementRepository;
import de.hhu.mentoring.database.repository.DocumentRepository;
import de.hhu.mentoring.database.repository.FileRepository;
import de.hhu.mentoring.database.repository.NoteRepository;
import de.hhu.mentoring.services.storage.StorageService;


@Service
public class FileServiceDatabase implements FileService {
	
		@Autowired
		FileRepository fileRepo;
		
		@Autowired
		AgreementRepository agreementRepo;
		
		@Autowired
		DocumentRepository documentRepo;
		
		@Autowired
		NoteRepository noteRepo;
		
		@Autowired
		StorageService storageService;
		
		
		
		// Files
	
		public void createFileForStudent(User student) {
			File file = new File();
			file.setStudent(student);
			
			fileRepo.save(file);
			
		}
		
		public File getFileByStudent(User student) {
			
			return fileRepo.findFileByStudent(student);
			
		}
		
		public List<File> getAllFiles(){
			
			return (List<File>)fileRepo.findAll();
			
		}
		
		public File getFileById(long id) {
			
			return fileRepo.findOne(id);
			
		}
		
		
		
		// Agreements
		
		public void makeAgreement(File file, String goal, LocalDateTime beginning, LocalDateTime end) {
			
			Agreement agreement = new Agreement(file,goal,beginning,end);
			
			agreementRepo.save(agreement);
			
		}
		
		public Agreement getAgreementById(long id) {
			
			return agreementRepo.findOne(id);
			
		}
		
		public List<Agreement> getAgreementsByStudent(User student) {
			
			File file = student.getFile();
			
			return agreementRepo.findAgreementsByFile(file);
			
		}
		
		public List<Agreement> getAgreementsByFile(File file) {
			
			return (List<Agreement>)agreementRepo.findAgreementsByFile(file);
			
		}
		
		public void updateAgreement(Agreement agreement) {
			agreementRepo.save(agreement);
		}
		
		
		
		// Documents
		
		public void makeDocument(File file, String title, MultipartFile data) {
			
			String filename = storageService.store(data);
			Document document = new Document(file, title, LocalDateTime.now(), filename);
			
			documentRepo.save(document);
			
		}
		
		public Document getDocumentById(long id) {
			
			return documentRepo.findOne(id);
			
		}
		
		public List<Document> getDocumentsByStudent(User student) {
			
			File file = student.getFile();
			
			return documentRepo.findDocumentsByFile(file);
			
		}
		
		public List<Document> getDocumentsByFile(File file) {
			
			return (List<Document>)documentRepo.findDocumentsByFile(file);
			
		}
		
		public Resource loadDocumentData(String filename) {
			return storageService.loadAsResource(filename);
		}
		
		public void updateDocument(Document document) {
			documentRepo.save(document);
		}
		
		
		
		// Notes
		
		public void makeNote(File file, String title, String content) {
			
			Note note = new Note(file, title,content,LocalDateTime.now());			
			noteRepo.save(note);
			
		}
		
		public Note getNoteById(long id) {
			
			return noteRepo.findOne(id);
			
		}
		
		public List<Note> getNotesByStudent(User student) {
			
			File file = student.getFile();
			
			return noteRepo.findNotesByFile(file);
			
		}
		
		public List<Note> getNotesByFile(File file) {
			
			return (List<Note>)noteRepo.findNotesByFile(file);
			
		}
		
		public void updateNote(Note note) {
			noteRepo.save(note);
		}
}
