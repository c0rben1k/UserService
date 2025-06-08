package by.algin.userservice.exception;

public class InvalidEmailException extends ApiException {
  public InvalidEmailException() {
    super(ErrorCode.INVALID_EMAIL);
  }

  public InvalidEmailException(String message) {
    super(ErrorCode.INVALID_EMAIL, message);
  }
}