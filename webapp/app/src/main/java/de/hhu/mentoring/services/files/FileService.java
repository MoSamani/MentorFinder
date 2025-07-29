package de.hhu.mentoring.services.files;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.mentoring.database.model.Agreement;
import de.hhu.mentoring.database.model.Document;
import de.hhu.mentoring.database.model.File;
import de.hhu.mentoring.database.model.Note;
import de.hhu.mentoring.database.model.User;

public interface FileService {

	//Files
	
	public void createFileForStudent(User student);
	
	public File getFileByStudent(User student);
	
	public List<File> getAllFiles();
	
	public File getFileById(long id);
	
	
	//Agreements
	
	public void makeAgreement(File file, String goal, LocalDateTime beginning, LocalDateTime end);
	
	public Agreement getAgreementById(long id);
	
	public List<Agreement> getAgreementsByStudent(User student); 
	
	public List<Agreement> getAgreementsByFile(File file);
	
	public void updateAgreement(Agreement agreement);
	
	
	//Documents
	
	public void makeDocument(File file, String title, MultipartFile data);
	
	public Document getDocumentById(long id);
	
	public List<Document> getDocumentsByStudent(User student); 
	
	public List<Document> getDocumentsByFile(File file);
	
	public Resource loadDocumentData(String filename);
	
	public void updateDocument(Document document);
	
	
	//Note
	
	public void makeNote(File file, String title, String content);
	
	public Note getNoteById(long id);
	
	public List<Note> getNotesByStudent(User student); 
	
	public List<Note> getNotesByFile(File file);
	
	public void updateNote(Note note);
	
}
