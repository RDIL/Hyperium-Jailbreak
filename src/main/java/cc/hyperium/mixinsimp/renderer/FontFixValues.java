package cc.hyperium.mixinsimp.renderer;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class FontFixValues {
    public static FontFixValues INSTANCE;
    public static SharedDrawable drawable;
    public List<StringHash> obfuscated = new ArrayList<>();
    private Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
        .writer(new RemovalListener())
        .executor(Multithreading.POOL)
        .maximumSize(7000)
        .build();
    private Queue<Integer> glRemoval = new ConcurrentLinkedQueue<>();

    public FontFixValues() {
        Multithreading.schedule(() -> {
            try {
                if (drawable == null) return;
                if (!drawable.isCurrent()) {
                    try {
                        drawable.makeCurrent();
                    } catch (LWJGLException e) {
                        try {
                            drawable.makeCurrent();
                        } catch (LWJGLException e1) {
                            drawable = null;
                            e.printStackTrace();
                        }
                        return;
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                }
                Integer integer;
                while ((integer = glRemoval.poll()) != null) {
                    GLAllocation.deleteDisplayLists(integer);
                }
                drawable.releaseContext();
            } catch (Exception e) {
                if (drawable != null) {
                    try {
                        drawable.releaseContext();
                    } catch (LWJGLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @InvokeEvent
    public void tick(TickEvent t) {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();
        if (drawable == null) {
            try {
                drawable = new SharedDrawable(Display.getDrawable());
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {
        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) {}

        @Override
        public void delete(@Nonnull StringHash key, @Nullable CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) return;
            glRemoval.add(value.getListId());
        }
    }
}
