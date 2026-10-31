package de.ait.myliferando.domain.enums;

public enum CourierStatus {

    OFF_DUTY("Не на смене"),
    AVAILABLE("Свободен"),
    BUSY("Везет заказ");

    private final String displayName;

    CourierStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
