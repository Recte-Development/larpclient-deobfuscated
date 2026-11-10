package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p2;
import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class q3b8ddfb33b8f408e {
   @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
   private void W(FrameGraphBuilder var1, CloudStatus var2, Vec3 var3, long var4, float var6, int var7, float var8, int var9, CallbackInfo var10) {
      if (CustomSkyModule.i() && !CustomSkyModule.K().q() && !p2.p()) {
         var10.cancel();
      }
   }
}
