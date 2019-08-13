package cc.hyperium.mixins;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.defaults.CommandDebug;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {
    @Shadow
    public abstract CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength);

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    public void add(CallbackInfo info) {
        CrashReportCategory category = this.makeCategoryDepth("Affected level", 1);
        category.addCrashSection("Hyperium Version", Hyperium.modid);
        category.addCrashSection("Everything else", CommandDebug.get());
    }
}
