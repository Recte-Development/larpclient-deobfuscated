package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.xn;
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
public abstract class q2cd6174d61a99292 {
   @Unique
   private boolean c;

   @Shadow
   public abstract RenderPass createRenderPass(
      Supplier<String> var1, GpuTextureView var2, Optional<Vector4fc> var3, @Nullable GpuTextureView var4, OptionalDouble var5, RenderArea var6
   );

   @Inject(
      method = "createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/OptionalDouble;Lcom/mojang/renderpearl/api/commands/RenderPass$RenderArea;)Lcom/mojang/renderpearl/api/commands/RenderPass;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void l(
      Supplier<String> var1,
      GpuTextureView var2,
      Optional<Vector4fc> var3,
      @Nullable GpuTextureView var4,
      OptionalDouble var5,
      RenderArea var6,
      CallbackInfoReturnable<RenderPass> var7
   ) {
      if (!this.c && (xn.j != null || xn.o != null)) {
         RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         boolean var9 = xn.j != null && var2 == var8.getColorTextureView();
         boolean var10 = xn.o != null && var4 != null && var4 == var8.getDepthTextureView();
         if (var9 || var10) {
            GpuTextureView var11 = var9 ? xn.j : var2;
            RenderArea var12 = var6.fillsTexture(var2) ? new RenderArea(0, 0, var11.getWidth(0), var11.getHeight(0)) : var6;
            this.c = true;

            try {
               var7.setReturnValue(this.createRenderPass(var1, var11, var3, var10 ? xn.o : var4, var5, var12));
            } finally {
               this.c = false;
            }
         }
      }
   }
}
