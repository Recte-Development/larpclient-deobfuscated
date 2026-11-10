package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.qs;
import com.larpclient.yf;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.class_11532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_11532.class)
public abstract class qafc5d0811f623831 {
   @Redirect(
      method = "method_72170",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V")
   )
   private void T(RenderPass var1, RenderPipeline var2) {
      RenderPipeline var3 = qs.l(var2);
      yf.l(var1, var3);
      qs.e(var1, var3);
   }
}
