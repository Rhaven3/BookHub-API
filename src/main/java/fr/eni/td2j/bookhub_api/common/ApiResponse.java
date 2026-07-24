package fr.eni.td2j.bookhub_api.common;


import java.time.Instant;

/**
 * Crée une instance d'ApiResponse avec le code, le message et les données spécifiés.
 * @param <T>
 * @param code
 * @param message
 * @param data
 * @return
 */
public record ApiResponse<T>(int code, Instant timestamp , String message, T data) {


    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, Instant.now(), message, data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, Instant.now(), message, null);
    }

}
