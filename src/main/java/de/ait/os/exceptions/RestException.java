package de.ait.os.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 6/29/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */
public class RestException extends RuntimeException{ // какие ошибки б. перехватывать

    private final HttpStatus status;

    public RestException(HttpStatus status, String message){

        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
