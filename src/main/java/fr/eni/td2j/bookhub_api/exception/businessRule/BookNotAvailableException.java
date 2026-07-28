package fr.eni.td2j.bookhub_api.exception.businessRule;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
