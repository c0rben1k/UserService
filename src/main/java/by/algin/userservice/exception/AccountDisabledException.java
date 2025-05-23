package by.algin.userservice.exception;

public class AccountDisabledException extends ApiException {
  public AccountDisabledException() {
    super(ErrorCode.ACCOUNT_DISABLED);
  }
}
