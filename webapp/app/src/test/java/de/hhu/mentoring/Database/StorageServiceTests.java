package de.hhu.mentoring.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import de.hhu.mentoring.services.storage.StorageProperties;
import de.hhu.mentoring.services.storage.StorageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageServiceTests {
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	StorageProperties properties;
	
	@Before
	public void setupRoutine() {
		storageService.deleteAll();
		storageService.init();
	}
	
	@After
	public void finishRoutine() {
		storageService.deleteAll();
	}

	@Test
	public void checkStoreFile() {
		String content = "checkStoreFile";
		String filename = "test.txt";
		MultipartFile data = new MockMultipartFile(filename, filename, "text/plain", content.getBytes());
		String newFilename = storageService.store(data);
		
		Path location = Paths.get(properties.getLocation(), newFilename);
		File savedFile = new File(location.toString());
		assertTrue(savedFile.exists());
		
		byte[] readFile = null;
    	try {
    		readFile = Files.readAllBytes(location);
    	} catch (IOException exc) {
    		fail("Reading file threw exception: " + exc.getMessage());
    	}
    	
		assertNotNull(readFile);
		assertEquals(content, new String(readFile));
    	
	}
	
	@Test
	public void checkLoadPath() {
		String filename = "test.txt";
		Path location = Paths.get(properties.getLocation(), filename);
		Path loadedLocation = storageService.load(filename);
		assertEquals(location, loadedLocation);
	}
	
	@Test
	public void checkLoadAsResource() {
		String content = "checkLoadAsResource";
		String filename = "test.txt";
		MultipartFile data = new MockMultipartFile(filename, filename, "text/plain", content.getBytes());
		String newFilename = storageService.store(data);
		
		Resource resource = storageService.loadAsResource(newFilename);
    	
		assertNotNull(resource);
		assertEquals(newFilename, resource.getFilename());
	}
	
	@Test
	public void checkInitDirectoryDoesNotExist() {
		storageService.init();
		Path rootLocation = Paths.get(properties.getLocation());
		File docDir = new File(rootLocation.toString());
		assertTrue(docDir.exists());
	}
	
	@Test
	public void checkInitDirectoryDoesExist() {
		Path rootLocation = Paths.get(properties.getLocation());
		File docDir = new File(rootLocation.toString());
		storageService.deleteAll();
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
            fail("Could not create directory: " + e);
        }
		storageService.init();
		assertTrue(docDir.exists());
	}
	
	@Test
	public void checkDelete() {
		storageService.init();
		storageService.deleteAll();
		Path rootLocation = Paths.get(properties.getLocation());
		File docDir = new File(rootLocation.toString());
		assertFalse(docDir.exists());
	}
}
