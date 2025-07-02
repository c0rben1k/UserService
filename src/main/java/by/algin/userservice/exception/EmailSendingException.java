package by.algin.userservice.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException() {
        super(UserServiceErrorCode.EMAIL_SENDING_ERROR.getDefaultMessage());
    }

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
