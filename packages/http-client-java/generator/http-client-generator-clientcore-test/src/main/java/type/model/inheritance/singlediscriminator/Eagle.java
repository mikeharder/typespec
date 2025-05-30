package type.model.inheritance.singlediscriminator;

import io.clientcore.core.annotations.Metadata;
import io.clientcore.core.annotations.MetadataProperties;
import io.clientcore.core.serialization.json.JsonReader;
import io.clientcore.core.serialization.json.JsonToken;
import io.clientcore.core.serialization.json.JsonWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The second level model in polymorphic single levels inheritance which contains references to other polymorphic
 * instances.
 */
@Metadata(properties = { MetadataProperties.FLUENT })
public final class Eagle extends Bird {
    /*
     * The kind property.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private String kind = "eagle";

    /*
     * The friends property.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private List<Bird> friends;

    /*
     * The hate property.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private Map<String, Bird> hate;

    /*
     * The partner property.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    private Bird partner;

    /**
     * Creates an instance of Eagle class.
     * 
     * @param wingspan the wingspan value to set.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Eagle(int wingspan) {
        super(wingspan);
    }

    /**
     * Get the kind property: The kind property.
     * 
     * @return the kind value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public String getKind() {
        return this.kind;
    }

    /**
     * Get the friends property: The friends property.
     * 
     * @return the friends value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public List<Bird> getFriends() {
        return this.friends;
    }

    /**
     * Set the friends property: The friends property.
     * 
     * @param friends the friends value to set.
     * @return the Eagle object itself.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Eagle setFriends(List<Bird> friends) {
        this.friends = friends;
        return this;
    }

    /**
     * Get the hate property: The hate property.
     * 
     * @return the hate value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Map<String, Bird> getHate() {
        return this.hate;
    }

    /**
     * Set the hate property: The hate property.
     * 
     * @param hate the hate value to set.
     * @return the Eagle object itself.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Eagle setHate(Map<String, Bird> hate) {
        this.hate = hate;
        return this;
    }

    /**
     * Get the partner property: The partner property.
     * 
     * @return the partner value.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Bird getPartner() {
        return this.partner;
    }

    /**
     * Set the partner property: The partner property.
     * 
     * @param partner the partner value to set.
     * @return the Eagle object itself.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public Eagle setPartner(Bird partner) {
        this.partner = partner;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeIntField("wingspan", getWingspan());
        jsonWriter.writeStringField("kind", this.kind);
        jsonWriter.writeArrayField("friends", this.friends, (writer, element) -> writer.writeJson(element));
        jsonWriter.writeMapField("hate", this.hate, (writer, element) -> writer.writeJson(element));
        jsonWriter.writeJsonField("partner", this.partner);
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of Eagle from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of Eagle if the JsonReader was pointing to an instance of it, or null if it was pointing to
     * JSON null.
     * @throws IllegalStateException If the deserialized JSON object was missing any required properties.
     * @throws IOException If an error occurs while reading the Eagle.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    public static Eagle fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            int wingspan = 0;
            String kind = "eagle";
            List<Bird> friends = null;
            Map<String, Bird> hate = null;
            Bird partner = null;
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();

                if ("wingspan".equals(fieldName)) {
                    wingspan = reader.getInt();
                } else if ("kind".equals(fieldName)) {
                    kind = reader.getString();
                } else if ("friends".equals(fieldName)) {
                    friends = reader.readArray(reader1 -> Bird.fromJson(reader1));
                } else if ("hate".equals(fieldName)) {
                    hate = reader.readMap(reader1 -> Bird.fromJson(reader1));
                } else if ("partner".equals(fieldName)) {
                    partner = Bird.fromJson(reader);
                } else {
                    reader.skipChildren();
                }
            }
            Eagle deserializedEagle = new Eagle(wingspan);
            deserializedEagle.kind = kind;
            deserializedEagle.friends = friends;
            deserializedEagle.hate = hate;
            deserializedEagle.partner = partner;

            return deserializedEagle;
        });
    }
}
