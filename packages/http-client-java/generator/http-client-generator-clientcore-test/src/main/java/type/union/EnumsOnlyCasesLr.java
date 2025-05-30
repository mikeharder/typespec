package type.union;

/**
 * Defines values for EnumsOnlyCasesLr.
 */
public enum EnumsOnlyCasesLr {
    /**
     * Enum value left.
     */
    LEFT("left"),

    /**
     * Enum value right.
     */
    RIGHT("right"),

    /**
     * Enum value up.
     */
    UP("up"),

    /**
     * Enum value down.
     */
    DOWN("down");

    /**
     * The actual serialized value for a EnumsOnlyCasesLr instance.
     */
    private final String value;

    EnumsOnlyCasesLr(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a EnumsOnlyCasesLr instance.
     * 
     * @param value the serialized value to parse.
     * @return the parsed EnumsOnlyCasesLr object, or null if unable to parse.
     */
    public static EnumsOnlyCasesLr fromString(String value) {
        if (value == null) {
            return null;
        }
        EnumsOnlyCasesLr[] items = EnumsOnlyCasesLr.values();
        for (EnumsOnlyCasesLr item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value;
    }
}
