package tech.seife.chatutilities.enums;

public enum ReplaceType {
    CHANNEL("%Channel%"),
    PARTY_NAME("%party%"),
    PLAYER_NAME("%player%");

    private String value;

    ReplaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
