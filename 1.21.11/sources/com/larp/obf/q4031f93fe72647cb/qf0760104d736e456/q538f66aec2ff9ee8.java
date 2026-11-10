package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class q538f66aec2ff9ee8 implements IMixinConfigPlugin {
   private static final String N = "com.larp.obf.q4031f93fe72647cb.qf0760104d736e456.qb55a49573996a04b";
   private static final String J = "com.larp.obf.q4031f93fe72647cb.qf0760104d736e456.q27f5790f37297af8.qc4048a9d804d2441.q6cf5a9a6fb7f97cc";

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larp.obf.q4031f93fe72647cb.qf0760104d736e456.qb55a49573996a04b".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larp.obf.q4031f93fe72647cb.qf0760104d736e456.q27f5790f37297af8.qc4048a9d804d2441.q6cf5a9a6fb7f97cc".equals(var2)
            ? FabricLoader.getInstance()
               .getModContainer("sodium")
               .map(var0 -> var0.getMetadata().getVersion().getFriendlyString().startsWith("0.8."))
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
