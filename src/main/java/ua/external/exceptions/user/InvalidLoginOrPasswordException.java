package ua.external.exceptions.user;

public class InvalidLoginOrPasswordException extends InvalidUserException {
    public InvalidLoginOrPasswordException() {
    }

    public InvalidLoginOrPasswordException(String message) {
        super(message);
    }

    public InvalidLoginOrPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLoginOrPasswordException(Throwable cause) {
        super(cause);
    }
}
