package cc.hyperium;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonDeserializer;

public class JsonHolderTypeAdaptor implements JsonDeserializer<JsonHolder>, JsonSerializer<JsonHolder> {
    public JsonHolderTypeAdaptor() {
        super();
    }
    
    public JsonHolder deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new JsonHolder(jsonElement.getAsJsonObject());
    }
    
    public JsonElement serialize(final JsonHolder jsonHolder, final Type type, final JsonSerializationContext jsonSerializationContext) {
        return (JsonElement)jsonHolder.getObject();
    }
    
    public Object deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return this.deserialize(jsonElement, type, jsonDeserializationContext);
    }
    
    public JsonElement serialize(final Object o, final Type type, final JsonSerializationContext jsonSerializationContext) {
        return this.serialize((JsonHolder)o, type, jsonSerializationContext);
    }
}
