package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class q9eeca5cceae4e695 implements IMixinConfigPlugin {
   private static final String E = "com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45.qf40e0a74ded3bd75";
   private static final String U = "com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45.q0fb4251e754f8bdc.qafc129abd0a99010.q32768bbb0bd140cd";

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45.qf40e0a74ded3bd75".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45.q0fb4251e754f8bdc.qafc129abd0a99010.q32768bbb0bd140cd".equals(var2)
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
