package com.tpebank.exception.message;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseError {

    private HttpStatus status;


    //@Setter(value= AccessLevel.NONE)
    private LocalDateTime timeStamp;


    private String message;


    private String requestURI;

    private  ApiResponseError(){
        timeStamp=LocalDateTime.now();
    }

    public ApiResponseError(HttpStatus status){
        this();
        this.status=status;
    }

    public ApiResponseError (HttpStatus status, String message, String requestURI){
        this(status);
        this.message=message;
        this.requestURI=requestURI;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
