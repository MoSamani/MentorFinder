package de.hhu.mentoring.services.storage;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 4383129661047682493L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}