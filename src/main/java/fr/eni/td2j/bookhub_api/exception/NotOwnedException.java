package fr.eni.td2j.bookhub_api.exception;

public class NotOwnedException extends RuntimeException {
    public NotOwnedException(String message) {
        super(message);
    }
}
