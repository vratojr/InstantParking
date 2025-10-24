package com.example.parking.application.exceptions;

/**
 * This is a generic application error. In prod I would subclass it with specific situations (EntityNotFound,InternalServerError ect)
 * that would be automatically mapped by the ControllerAdvice to predefined http response codes.
 */
public class ApplicationError extends RuntimeException {

  public ApplicationError(String message) {
    super(message);
  }

}
