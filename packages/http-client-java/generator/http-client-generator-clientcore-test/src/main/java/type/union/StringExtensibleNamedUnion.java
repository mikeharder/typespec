package type.union;

import io.clientcore.core.annotations.Metadata;
import io.clientcore.core.annotations.MetadataProperties;
import io.clientcore.core.serialization.json.JsonReader;
import io.clientcore.core.serialization.json.JsonSerializable;
import io.clientcore.core.serialization.json.JsonToken;
import io.clientcore.core.serialization.json.JsonWriter;
import io.clientcore.core.utils.ExpandableEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Defines values for StringExtensibleNamedUnion.
 */
public final class StringExtensibleNamedUnion
    implements ExpandableEnum<String>, JsonSerializable<StringExtensibleNamedUnion> {
    private static final Map<String, StringExtensibleNamedUnion> VALUES = new ConcurrentHashMap<>();

    private static final Function<String, StringExtensibleNamedUnion> NEW_INSTANCE = StringExtensibleNamedUnion::new;

    /**
     * Static value b for StringExtensibleNamedUnion.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static final StringExtensibleNamedUnion OPTIONB = fromValue("b");

    /**
     * Static value c for StringExtensibleNamedUnion.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static final StringExtensibleNamedUnion C = fromValue("c");

    private final String value;

    private StringExtensibleNamedUnion(String value) {
        this.value = value;
    }

    /**
     * Creates or finds a StringExtensibleNamedUnion.
     * 
     * @param value a value to look for.
     * @return the corresponding StringExtensibleNamedUnion.
     * @throws IllegalArgumentException if value is null.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static StringExtensibleNamedUnion fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("'value' cannot be null.");
        }
        return VALUES.computeIfAbsent(value, NEW_INSTANCE);
    }

    /**
     * Gets known StringExtensibleNamedUnion values.
     * 
     * @return Known StringExtensibleNamedUnion values.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static Collection<StringExtensibleNamedUnion> values() {
        return new ArrayList<>(VALUES.values());
    }

    /**
     * Gets the value of the StringExtensibleNamedUnion instance.
     * 
     * @return the value of the StringExtensibleNamedUnion instance.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        return jsonWriter.writeString(getValue());
    }

    /**
     * Reads an instance of StringExtensibleNamedUnion from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of StringExtensibleNamedUnion if the JsonReader was pointing to an instance of it, or null if
     * the JsonReader was pointing to JSON null.
     * @throws IOException If an error occurs while reading the StringExtensibleNamedUnion.
     * @throws IllegalStateException If unexpected JSON token is found.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static StringExtensibleNamedUnion fromJson(JsonReader jsonReader) throws IOException {
        JsonToken nextToken = jsonReader.nextToken();
        if (nextToken == JsonToken.NULL) {
            return null;
        }
        if (nextToken != JsonToken.STRING) {
            throw new IllegalStateException(
                String.format("Unexpected JSON token for %s deserialization: %s", JsonToken.STRING, nextToken));
        }
        return StringExtensibleNamedUnion.fromValue(jsonReader.getString());
    }

    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public String toString() {
        return Objects.toString(this.value);
    }

    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
