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

package cc.hyperium.mods.chromahud;

import cc.hyperium.mods.chromahud.api.ButtonConfig;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.api.StringConfig;
import cc.hyperium.mods.chromahud.api.TextConfig;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChromaHUDApi {
    private static ChromaHUDApi instance;
    private final List<ChromaHUDParser> parsers = new ArrayList<>();
    private final Map<String, String> names = new HashMap<>();
    private final List<DisplayElement> elements = new ArrayList<>();
    private final Map<String, ArrayList<ButtonConfig>> buttonConfigs = new HashMap<>();
    private final Map<String, ArrayList<TextConfig>> textConfigs = new HashMap<>();
    private final Map<String, ArrayList<StringConfig>> stringConfigs = new HashMap<>();
    private boolean posted = false;
    private JsonHolder config = new JsonHolder();

    private ChromaHUDApi() {
        instance = this;
    }

    public static ChromaHUDApi getInstance() {
        if (instance == null)
            instance = new ChromaHUDApi();
        return instance;
    }

    public List<ButtonConfig> getButtonConfigs(String type) {
        type = type.toLowerCase();
        List<ButtonConfig> configs = buttonConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<TextConfig> getTextConfigs(String type) {
        type = type.toLowerCase();
        List<TextConfig> configs = this.textConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<StringConfig> getStringConfigs(String type) {
        type = type.toLowerCase();
        List<StringConfig> configs = this.stringConfigs.get(type);
        if (configs != null)
            return new ArrayList<>(configs);
        return new ArrayList<>();
    }

    public List<DisplayElement> getElements() {
        return elements;
    }

    public void registerTextConfig(String type, TextConfig config) {
        type = type.toLowerCase();
        if (!textConfigs.containsKey(type))
            textConfigs.put(type, new ArrayList<>());
        textConfigs.get(type).add(config);
    }

    public void registerStringConfig(String type, StringConfig config) {
        type = type.toLowerCase();
        if (!stringConfigs.containsKey(type))
            stringConfigs.put(type, new ArrayList<>());
        stringConfigs.get(type).add(config);
    }

    public void registerButtonConfig(String type, ButtonConfig config) {
        type = type.toLowerCase();
        if (!buttonConfigs.containsKey(type))
            buttonConfigs.put(type, new ArrayList<>());
        buttonConfigs.get(type).add(config);
    }

    public void register(ChromaHUDParser parser) {
        if (posted) throw new IllegalStateException("Cannot register parser after FMLPostInitialization event");
        parsers.add(parser);
        names.putAll(parser.getNames());
    }

    public void post(JsonHolder config) {
        this.config = config;
        this.posted = true;
        elements.clear();
        JsonArray displayElements = config.optJSONArray("elements");
        for (int i = 0; i < displayElements.size(); i++) {
            JsonHolder object = new JsonHolder(displayElements.get(i).getAsJsonObject());
            try {
                DisplayElement e = new DisplayElement(object);
                if (e.getDisplayItems().size() > 0)
                    elements.add(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DisplayItem parse(String type, int ord, JsonHolder item) {
        for (ChromaHUDParser parser : parsers) {
            DisplayItem parsed = parser.parse(type, ord, item);
            if (parsed != null) return parsed;
        }
        return null;
    }

    public String getName(String type) {
        return names.getOrDefault(type, type);
    }

    public List<ChromaHUDParser> getParsers() {
        return new ArrayList<>(parsers);
    }

    public JsonHolder getConfig() {
        return config;
    }
}
