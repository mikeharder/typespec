package type.property.additionalproperties;

import io.clientcore.core.annotations.Metadata;
import io.clientcore.core.annotations.MetadataProperties;
import io.clientcore.core.models.binarydata.BinaryData;
import io.clientcore.core.serialization.json.JsonReader;
import io.clientcore.core.serialization.json.JsonSerializable;
import io.clientcore.core.serialization.json.JsonToken;
import io.clientcore.core.serialization.json.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The model spread Record&lt;string&gt; and Record&lt;float32&gt;.
 */
@Metadata(properties = { MetadataProperties.FLUENT })
public final class MultipleSpreadRecord implements JsonSerializable<MultipleSpreadRecord> {
    /*
     * The name property
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private final boolean flag;

    /*
     * The model spread Record<string> and Record<float32>
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private Map<String, BinaryData> additionalProperties;

    /**
     * Creates an instance of MultipleSpreadRecord class.
     * 
     * @param flag the flag value to set.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public MultipleSpreadRecord(boolean flag) {
        this.flag = flag;
    }

    /**
     * Get the flag property: The name property.
     * 
     * @return the flag value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public boolean isFlag() {
        return this.flag;
    }

    /**
     * Get the additionalProperties property: The model spread Record&lt;string&gt; and Record&lt;float32&gt;.
     * 
     * @return the additionalProperties value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Map<String, BinaryData> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Set the additionalProperties property: The model spread Record&lt;string&gt; and Record&lt;float32&gt;.
     * 
     * @param additionalProperties the additionalProperties value to set.
     * @return the MultipleSpreadRecord object itself.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public MultipleSpreadRecord setAdditionalProperties(Map<String, BinaryData> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeBooleanField("flag", this.flag);
        if (additionalProperties != null) {
            for (Map.Entry<String, BinaryData> additionalProperty : additionalProperties.entrySet()) {
                jsonWriter.writeFieldName(additionalProperty.getKey());
                if (additionalProperty.getValue() == null) {
                    jsonWriter.writeNull();
                } else {
                    additionalProperty.getValue().writeTo(jsonWriter);
                }
            }
        }
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of MultipleSpreadRecord from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of MultipleSpreadRecord if the JsonReader was pointing to an instance of it, or null if it
     * was pointing to JSON null.
     * @throws IllegalStateException If the deserialized JSON object was missing any required properties.
     * @throws IOException If an error occurs while reading the MultipleSpreadRecord.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static MultipleSpreadRecord fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            boolean flag = false;
            Map<String, BinaryData> additionalProperties = null;
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();

                if ("flag".equals(fieldName)) {
                    flag = reader.getBoolean();
                } else {
                    if (additionalProperties == null) {
                        additionalProperties = new LinkedHashMap<>();
                    }

                    additionalProperties.put(fieldName,
                        reader.getNullable(nonNullReader -> BinaryData.fromObject(nonNullReader.readUntyped())));
                }
            }
            MultipleSpreadRecord deserializedMultipleSpreadRecord = new MultipleSpreadRecord(flag);
            deserializedMultipleSpreadRecord.additionalProperties = additionalProperties;

            return deserializedMultipleSpreadRecord;
        });
    }
}
