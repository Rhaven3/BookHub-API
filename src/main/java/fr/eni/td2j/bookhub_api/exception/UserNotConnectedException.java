package fr.eni.td2j.bookhub_api.exception;

public class UserNotConnectedException extends RuntimeException {
    public UserNotConnectedException(String message) {
        super(message);
    }
}
