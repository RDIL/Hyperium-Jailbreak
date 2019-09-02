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

package cc.hyperium.mixinsimp.client;

import me.semx11.autotip.universal.ReflectionUtil;
import net.minecraft.client.renderer.GlStateManager;
import java.lang.reflect.Field;

public class GlStateModifier implements IGlStateModifier {
    private Object[] theArray;
    private Field textureNamefield;
    private Object colorStateObject = null;
    private Field activeTextureUnitField = null;
    public static final IGlStateModifier INSTANCE = new GlStateModifier();

    public void setTexture(int id) {
        if (theArray == null) {
            try {
                theArray = (Object[]) ReflectionUtil.findField(GlStateManager.class, "textureState", "field_179174_p", "p").get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (textureNamefield == null) {
            Class<?> aClass = ReflectionUtil.findClazz("net.minecraft.client.renderer.GlStateManager$TextureState", "bfl$r");
            try {
                textureNamefield = aClass.getDeclaredField("textureName");
            } catch (NoSuchFieldException e) {
                try {
                    textureNamefield = aClass.getDeclaredField("field_179059_b");
                } catch (NoSuchFieldException e1) {
                    try {
                        textureNamefield = aClass.getDeclaredField("b");
                    } catch (NoSuchFieldException e2) {
                        e2.printStackTrace();
                    }
                }
            }

            if (textureNamefield != null) {
                textureNamefield.setAccessible(true);
            }
        }

        int activeTextureUnit = -1;
        if (activeTextureUnitField == null) {
            activeTextureUnitField = ReflectionUtil.findField(GlStateManager.class, "activeTextureUnit", "field_179162_o", "o");
        }
        try {
            if (activeTextureUnitField != null)
                activeTextureUnit = ((int) activeTextureUnitField.get(null));
        } catch (ReflectionUtil.UnableToAccessFieldException | IllegalAccessException ignored) {}

        if (theArray == null || textureNamefield == null || activeTextureUnit == -1) {
            return;
        }
        Object o = theArray[activeTextureUnit];

        try {
            textureNamefield.set(o, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        setTexture(-1);
    }
}
