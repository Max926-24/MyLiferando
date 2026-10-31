package de.ait.myliferando.domain.enums;

public enum LegalType {
    LEGAL_ENTITY("Юридическое лицо"),
    INDIVIDUAL_ENTREPRENEUR("Индивидуальный предприниматель"),
    INDIVIDUAL("Физическое лицо");

    private final String displayName;

    LegalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
