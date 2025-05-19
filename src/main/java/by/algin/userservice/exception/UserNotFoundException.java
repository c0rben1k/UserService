package by.algin.userservice.exception;

public class UserNotFoundException extends ApiException {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}