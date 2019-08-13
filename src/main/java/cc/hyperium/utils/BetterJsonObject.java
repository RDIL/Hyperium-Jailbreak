/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BetterJsonObject {
    private final Gson prettyPrinter = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject data;
    public BetterJsonObject() {
        this.data = new JsonObject();
    }

    public BetterJsonObject(String jsonIn) {
        if (jsonIn == null || jsonIn.isEmpty()) {
            this.data = new JsonObject();
            return;
        }
        try {
            this.data = new JsonParser().parse(jsonIn).getAsJsonObject();
        } catch (JsonSyntaxException | JsonIOException ex) {
            ex.printStackTrace();
        }
    }

    public String optString(String key) {
        return optString(key, "");
    }

    public String optString(String key, String value) {
        if (key == null || key.isEmpty() || !has(key)) return value;
        JsonPrimitive primitive = asPrimitive(get(key));

        if (primitive != null && primitive.isString()) return primitive.getAsString();
        return value;
    }

    public int optInt(String key) {
        return optInt(key, 0);
    }

    public int optInt(String key, int value) {
        if (key == null || key.isEmpty() || !has(key)) return value;

        JsonPrimitive primitive = asPrimitive(get(key));

        try {
            if (primitive != null && primitive.isNumber()) {
                return primitive.getAsInt();
            }
        } catch (NumberFormatException ignored) {}
        return value;
    }

    public double optDouble(String key) {
        return optDouble(key, 0);
    }

    public double optDouble(String key, double value) {
        if (key == null || key.isEmpty() || !has(key)) return value;

        JsonPrimitive primitive = asPrimitive(get(key));

        try {
            if (primitive != null && primitive.isNumber()) return primitive.getAsDouble();
        } catch (NumberFormatException ignored) {
        }
        return value;
    }

    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean value) {
        if (key == null || key.isEmpty() || !has(key)) return value;
        JsonPrimitive primitive = asPrimitive(get(key));
        if (primitive != null && primitive.isBoolean()) return primitive.getAsBoolean();
        return value;
    }

    public boolean has(String key) {
        return this.data.has(key);
    }

    public JsonElement get(String key) {
        return this.data.get(key);
    }

    public JsonObject getData() {
        return this.data;
    }

    public void addProperty(String key, String value) {
        if (key != null) this.data.addProperty(key, value);
    }

    public void addProperty(String key, Number value) {
        if (key != null) this.data.addProperty(key, value);
    }

    public void addProperty(String key, Boolean value) {
        if (key != null) this.data.addProperty(key, value);
    }

    public BetterJsonObject add(String key, BetterJsonObject object) {
        if (key != null) this.data.add(key, object.getData());
        return this;
    }

    public void writeToFile(File file) {
        if (file == null || (file.exists() && file.isDirectory())) return;

        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(toPrettyString()); // Use our pretty printer
            bufferedWriter.close(); // Close the BufferedWriter
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonPrimitive asPrimitive(JsonElement element) {
        return element instanceof JsonPrimitive ? (JsonPrimitive) element : null;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    private String toPrettyString() {
        return this.prettyPrinter.toJson(this.data);
    }
}
