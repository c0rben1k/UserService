package by.algin.userservice.exception;

public class AccountAlreadyConfirmedException extends ApiException {
  public AccountAlreadyConfirmedException() {
    super(ErrorCode.ACCOUNT_ALREADY_CONFIRMED);
  }
}