package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.an;
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
public abstract class qfca1ee1c420f7da7 {
   @Shadow
   private boolean needsUpdate;
   @Unique
   private int Q = -1;
   @Unique
   private float x;
   @Unique
   private Object B;

   @Inject(method = "extract", at = @At("HEAD"))
   private void F(LightmapRenderState var1, float var2, CallbackInfo var3) {
      WorldColorModule var4 = WorldColorModule.x();
      boolean var5 = var4 != null && var4.D();
      int var6 = var5 ? var4.o.getValue() : -1;
      float var7 = var5 ? var4.u.getValue().floatValue() : 0.0F;
      ClientLevel var8 = Minecraft.getInstance().level;
      if (var8 != this.B || var6 != this.Q || var7 != this.x) {
         this.needsUpdate = true;
         this.B = var8;
         this.Q = var6;
         this.x = var7;
      }
   }

   @Inject(method = "extract", at = @At("TAIL"))
   private void g(LightmapRenderState var1, float var2, CallbackInfo var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level != null && var4.player != null) {
         an.Z(var1, this.Q, this.x);
      }
   }
}
