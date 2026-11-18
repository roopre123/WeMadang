package org.example.wemadang.common;

public class ChzzkApiException extends RuntimeException{

    public ChzzkApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChzzkApiException(String message) {
        super(message);
    }
}
