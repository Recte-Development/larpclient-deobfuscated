package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.y7;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.class_11231;
import net.minecraft.class_11243;
import net.minecraft.class_11767;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_11243.class)
public abstract class q71f8f7acd43ac23d {
   @Shadow
   @Final
   private class_11767 comp_4637;

   @Inject(method = "comp_4056()Lnet/minecraft/class_11231;", at = @At("HEAD"), cancellable = true)
   private void W(CallbackInfoReturnable<class_11231> var1) {
      GpuTextureView var2 = this.comp_4637.comp_4629();
      GpuTexture var3 = var2.texture();
      if (var3.getFormat() == TextureFormat.RED8 && y7.Z(var3)) {
         var1.setReturnValue(class_11231.method_70902(var2, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR)));
      }
   }
}
