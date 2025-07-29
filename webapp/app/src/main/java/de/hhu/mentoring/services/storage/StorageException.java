package de.hhu.mentoring.services.storage;

public class StorageException extends RuntimeException {
	
	private static final long serialVersionUID = 4640403191664903514L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
