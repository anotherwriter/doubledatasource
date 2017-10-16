package com.example.demo.server.exception;

public class JsonDeserializingException extends JsonException {

    private static final long serialVersionUID = 1L;

    public JsonDeserializingException() {
        super();
    }

    public JsonDeserializingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonDeserializingException(String message) {
        super(message);
    }

    public JsonDeserializingException(Throwable cause) {
        super(cause);
    }

}

