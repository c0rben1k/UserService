package by.algin.userservice.exception;

public class InvalidEmailException extends RuntimeException {
  public InvalidEmailException() {
    super(UserServiceErrorCode.INVALID_EMAIL_FORMAT.getDefaultMessage());
  }

  public InvalidEmailException(String message) {
    super(message);
  }
}