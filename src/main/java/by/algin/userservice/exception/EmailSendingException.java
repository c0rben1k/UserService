package by.algin.userservice.exception;

public class EmailSendingException extends ApiException {
    public EmailSendingException(String message) {
        super(ErrorCode.EMAIL_SENDING_ERROR, message);
    }
}
