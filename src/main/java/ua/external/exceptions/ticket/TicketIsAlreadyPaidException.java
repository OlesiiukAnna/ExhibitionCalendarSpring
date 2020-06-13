package ua.external.exceptions.ticket;

public class TicketIsAlreadyPaidException extends Throwable {

    public TicketIsAlreadyPaidException() {
    }

    public TicketIsAlreadyPaidException(String message) {
        super(message);
    }

    public TicketIsAlreadyPaidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketIsAlreadyPaidException(Throwable cause) {
        super(cause);
    }
}
