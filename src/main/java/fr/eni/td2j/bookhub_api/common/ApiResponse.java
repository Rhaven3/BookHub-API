package fr.eni.td2j.bookhub_api.common;


import java.time.Instant;

public record ApiResponse<T>(int code, Instant timestamp , String message, T data) {

    /**
     * Crée une instance d'ApiResponse avec le code, le message et les données spécifiés.
     * @param <T>
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return new ApiResponse<>(code, Instant.now(), message, data);
    }

}
