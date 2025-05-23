package by.algin.userservice.exception;

public class UsernameAlreadyExistsException extends ApiException {
  public UsernameAlreadyExistsException() {
    super(ErrorCode.USERNAME_ALREADY_EXISTS);
  }
}