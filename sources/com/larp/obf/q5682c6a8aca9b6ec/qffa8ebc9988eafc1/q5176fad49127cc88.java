package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient._m;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.commands.RenderPass.RenderArea;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrontendCommandEncoder.class)
public abstract class q5176fad49127cc88 {
   @Unique
   private boolean y;

   @Shadow
   public abstract RenderPass createRenderPass(
      Supplier<String> var1, GpuTextureView var2, Optional<Vector4fc> var3, @Nullable GpuTextureView var4, OptionalDouble var5, RenderArea var6
   );

   @Inject(
      method = "createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/OptionalDouble;Lcom/mojang/renderpearl/api/commands/RenderPass$RenderArea;)Lcom/mojang/renderpearl/api/commands/RenderPass;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void N(
      Supplier<String> var1,
      GpuTextureView var2,
      Optional<Vector4fc> var3,
      @Nullable GpuTextureView var4,
      OptionalDouble var5,
      RenderArea var6,
      CallbackInfoReturnable<RenderPass> var7
   ) {
      if (!this.y && (_m.M != null || _m.U != null)) {
         RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         boolean var9 = _m.M != null && var2 == var8.getColorTextureView();
         boolean var10 = _m.U != null && var4 != null && var4 == var8.getDepthTextureView();
         if (var9 || var10) {
            GpuTextureView var11 = var9 ? _m.M : var2;
            RenderArea var12 = var6.fillsTexture(var2) ? new RenderArea(0, 0, var11.getWidth(0), var11.getHeight(0)) : var6;
            this.y = true;

            try {
               var7.setReturnValue(this.createRenderPass(var1, var11, var3, var10 ? _m.U : var4, var5, var12));
            } finally {
               this.y = false;
            }
         }
      }
   }
}
