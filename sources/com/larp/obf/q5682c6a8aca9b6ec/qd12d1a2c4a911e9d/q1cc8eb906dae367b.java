package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class q1cc8eb906dae367b implements IMixinConfigPlugin {
   private static final String C = "com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d.q67c54874908524c9";
   private static final String w = "com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d.qa1bcaa5546d63caf.q34316bc46cc078a8.q89bed71bc7611763";

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d.q67c54874908524c9".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d.qa1bcaa5546d63caf.q34316bc46cc078a8.q89bed71bc7611763".equals(var2)
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
