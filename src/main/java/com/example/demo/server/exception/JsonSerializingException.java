package com.example.demo.server.exception;

public class JsonSerializingException extends JsonException {

    private static final long serialVersionUID = 1L;

    public JsonSerializingException() {
        super();
    }

    public JsonSerializingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSerializingException(String message) {
        super(message);
    }

    public JsonSerializingException(Throwable cause) {
        super(cause);
    }

}
