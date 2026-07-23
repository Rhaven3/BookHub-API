package fr.eni.td2j.bookhub_api.exception;

public class AddressCreationException extends RuntimeException {

    public AddressCreationException(String message) {
        super(message);
    }

    public AddressCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}