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

package cc.hyperium.mods.sk1ercommon;

import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Sk1erMod {
    private static Sk1erMod instance;
    public Sk1erMod() {
        instance = this;
    }

    public static Sk1erMod getInstance() {
        return instance;
    }

    public String rawWithAgent(String url) {
        url = url.replace(" ", "%20");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Hyperium V1.0.5)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
