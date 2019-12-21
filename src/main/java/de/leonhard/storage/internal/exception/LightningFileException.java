package de.leonhard.storage.internal.exception;

public class LightningFileException extends RuntimeException {

    public LightningFileException(String... message) {
        for (String part : message) {
            System.err.println(part);
        }
    }
}
