package by.algin.userservice.exception;

public class InvalidTokenException extends ApiException {
  public InvalidTokenException() {
    super(ErrorCode.INVALID_TOKEN);
  }
}
