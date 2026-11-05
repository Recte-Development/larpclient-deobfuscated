package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.m8;
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
public abstract class qbdaecac5c342da7f {
   @Shadow
   private boolean needsUpdate;
   @Unique
   private int l = -1;
   @Unique
   private float e;
   @Unique
   private Object I;

   @Inject(method = "extract", at = @At("HEAD"))
   private void q(LightmapRenderState var1, float var2, CallbackInfo var3) {
      WorldColorModule var4 = WorldColorModule.S();
      boolean var5 = var4 != null && var4.isRuntimeActive();
      int var6 = var5 ? var4.z.getValue() : -1;
      float var7 = var5 ? var4.g.getValue().floatValue() : 0.0F;
      ClientLevel var8 = Minecraft.getInstance().level;
      if (var8 != this.I || var6 != this.l || var7 != this.e) {
         this.needsUpdate = true;
         this.I = var8;
         this.l = var6;
         this.e = var7;
      }
   }

   @Inject(method = "extract", at = @At("TAIL"))
   private void M(LightmapRenderState var1, float var2, CallbackInfo var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level != null && var4.player != null) {
         m8.s(var1, this.l, this.e);
      }
   }
}
