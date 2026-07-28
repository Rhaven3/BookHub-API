package fr.eni.td2j.bookhub_api.exception.businessRule;

public class MaxLoanException extends RuntimeException {
    public MaxLoanException(String message) {
        super(message);
    }
}
