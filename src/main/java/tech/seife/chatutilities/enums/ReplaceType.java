package tech.seife.chatutilities.enums;

public enum ReplaceType {
    CHANNEL("%channel%"),
    PARTY_NAME("%partyName%"),
    PLAYER_NAME("%player%");

    private String value;

    ReplaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
