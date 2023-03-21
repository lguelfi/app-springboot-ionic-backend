package com.leonardo.springwebservice.services.exceptions;

public class FileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileException(String msg) {
        super(msg);
    }
}
