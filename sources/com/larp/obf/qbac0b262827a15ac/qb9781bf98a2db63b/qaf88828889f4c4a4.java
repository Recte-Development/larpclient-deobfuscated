package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class qaf88828889f4c4a4 implements IMixinConfigPlugin {
   private static final String z = "com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b.q1bbd321d2217789d";
   private static final String W = "com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b.q553828617d007346.q2817641209a0c459.qda3f7c20ff4b19d4";

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b.q1bbd321d2217789d".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b.q553828617d007346.q2817641209a0c459.qda3f7c20ff4b19d4".equals(var2)
            ? FabricLoader.getInstance()
               .getModContainer("sodium")
               .map(var0 -> var0.getMetadata().getVersion().getFriendlyString().startsWith("0.9."))
               .orElse(false)
            : true;
      }
   }

   public void acceptTargets(Set<String> var1, Set<String> var2) {
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }

   public void postApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }
}
