package cc.hyperium.netty;

import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonSerializer;
import java.util.UUID;
import com.google.gson.JsonDeserializer;

public class UuidTypeAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID> {
    public UuidTypeAdapter() {
        super();
    }

    public JsonElement serialize(final UUID src, final Type typeOfSrc, final JsonSerializationContext context) {
        return (JsonElement)new JsonPrimitive(src.toString());
    }

    public UUID deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        try {
            return UUID.fromString(json.getAsString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Object deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        return this.deserialize(json, typeOfT, context);
    }

    public JsonElement serialize(final Object o, final Type typeOfSrc, final JsonSerializationContext context) {
        return this.serialize((UUID)o, typeOfSrc, context);
    }
}
