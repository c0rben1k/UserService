package by.algin.userservice.exception;

public class AccountAlreadyConfirmedException extends RuntimeException {
  public AccountAlreadyConfirmedException() {
    super(UserServiceErrorCode.ACCOUNT_ALREADY_CONFIRMED.getDefaultMessage());
  }

  public AccountAlreadyConfirmedException(String message) {
    super(message);
  }
}