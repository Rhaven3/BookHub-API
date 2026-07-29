package fr.eni.td2j.bookhub_api.notification;

public enum NotificationEnum {
    WARNING,
    ALERT,
    INFO,
    SUCCESS;

    // Méthode pour convertir un String en Enum
    public static NotificationEnum fromString(String text) {
        try {
            return NotificationEnum.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Aucune valeur d'énumération trouvée pour : " + text);
        }
    }
}
