package com.larp.obf.qa300483f54d804a3;

import com.larpclient.uv;
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
public abstract class qa52c1e90ca924fd5 {
   @Shadow
   private boolean needsUpdate;
   @Unique
   private int N = -1;
   @Unique
   private float R;
   @Unique
   private Object y;

   @Inject(method = "extract", at = @At("HEAD"))
   private void V(LightmapRenderState var1, float var2, CallbackInfo var3) {
      WorldColorModule var4 = WorldColorModule.p();
      boolean var5 = var4 != null && var4.i();
      int var6 = var5 ? var4.T.getValue() : -1;
      float var7 = var5 ? var4.N.getValue().floatValue() : 0.0F;
      ClientLevel var8 = Minecraft.getInstance().level;
      if (var8 != this.y || var6 != this.N || var7 != this.R) {
         this.needsUpdate = true;
         this.y = var8;
         this.N = var6;
         this.R = var7;
      }
   }

   @Inject(method = "extract", at = @At("TAIL"))
   private void C(LightmapRenderState var1, float var2, CallbackInfo var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level != null && var4.player != null) {
         uv.y(var1, this.N, this.R);
      }
   }
}
