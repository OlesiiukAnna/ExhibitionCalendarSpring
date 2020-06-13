package ua.external.exceptions.user;

public class InvalidPhoneNumberException extends InvalidUserException {
    public InvalidPhoneNumberException() {
    }

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPhoneNumberException(Throwable cause) {
        super(cause);
    }
}
