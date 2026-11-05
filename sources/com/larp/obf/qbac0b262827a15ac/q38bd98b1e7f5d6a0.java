package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.o1;
import com.larpclient.module.WorldColorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class q38bd98b1e7f5d6a0 {
   @Shadow
   private boolean needsUpdate;
   @Unique
   private int X = -1;
   @Unique
   private float E;
   @Unique
   private Object D;

   @Inject(method = "extract", at = @At("HEAD"))
   private void h(LightmapRenderState var1, float var2, CallbackInfo var3) {
      WorldColorModule var4 = WorldColorModule.N();
      boolean var5 = var4 != null && var4.isRuntimeActive();
      int var6 = var5 ? var4.T.P() : -1;
      float var7 = var5 ? var4.B.P().floatValue() : 0.0F;
      ClientLevel var8 = Minecraft.getInstance().level;
      if (var8 != this.D || var6 != this.X || var7 != this.E) {
         this.needsUpdate = true;
         this.D = var8;
         this.X = var6;
         this.E = var7;
      }
   }

   @Inject(method = "extract", at = @At("TAIL"))
   private void A(LightmapRenderState var1, float var2, CallbackInfo var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level != null && var4.player != null) {
         o1.d(var1, this.X, this.E);
      }
   }
}
