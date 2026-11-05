package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient._b;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class q789613cfdc13e52b {
   @WrapOperation(
      method = "lambda$addMainPass$0",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/renderpearl/api/commands/RenderPass;"
      ),
      require = 1,
      allow = 1
   )
   private RenderPass O(
      CommandEncoder var1,
      Supplier<String> var2,
      GpuTextureView var3,
      Optional<Vector4fc> var4,
      GpuTextureView var5,
      OptionalDouble var6,
      Operation<RenderPass> var7
   ) {
      RenderPass var8 = (RenderPass)var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      return _b.r(var8, var2, var3, var5);
   }
}
