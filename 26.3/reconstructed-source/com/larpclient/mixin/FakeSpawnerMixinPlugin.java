package com.larpclient.mixin;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class FakeSpawnerMixinPlugin implements IMixinConfigPlugin {
   private static final String l = "com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d.q9d7fcb40459e7c63";
   private static final String X = "com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d.q39dd639f0a03ee4f.q8490418edb2f34cf.qb36b1084a8a09668";

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d.q9d7fcb40459e7c63".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d.q39dd639f0a03ee4f.q8490418edb2f34cf.qb36b1084a8a09668".equals(var2)
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
