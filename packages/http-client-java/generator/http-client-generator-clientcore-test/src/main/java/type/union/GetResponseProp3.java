package type.union;

/**
 * Defines values for GetResponseProp3.
 */
public enum GetResponseProp3 {
    /**
     * Enum value 1.1.
     */
    ONE_ONE(1.1),

    /**
     * Enum value 2.2.
     */
    TWO_TWO(2.2),

    /**
     * Enum value 3.3.
     */
    THREE_THREE(3.3);

    /**
     * The actual serialized value for a GetResponseProp3 instance.
     */
    private final double value;

    GetResponseProp3(double value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a GetResponseProp3 instance.
     * 
     * @param value the serialized value to parse.
     * @return the parsed GetResponseProp3 object, or null if unable to parse.
     */
    public static GetResponseProp3 fromDouble(double value) {
        GetResponseProp3[] items = GetResponseProp3.values();
        for (GetResponseProp3 item : items) {
            if (Double.doubleToLongBits(item.toDouble()) == Double.doubleToLongBits(value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * De-serializes the instance to double value.
     * 
     * @return the double value.
     */
    public double toDouble() {
        return this.value;
    }
}
