package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class db implements t {
   private final SecureRandom w = new SecureRandom();
   private float f = 0.0F;
   private float d = 0.0F;

   @Override
   public d5 rotate(d5 var1, d5 var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = dj.h(var1.M, var2.M);
         float var7 = var2.G - var1.G;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.P(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.P(35.0F, 50.0F);
         } else {
            var9 = this.P(28.0F, 42.0F);
         }

         this.f = Mth.lerp(0.35F, this.f, var9);
         this.d = Mth.lerp(0.3F, this.d, var9 * 0.75F);
         float var10 = Mth.clamp(var6, -this.f, this.f);
         float var11 = Mth.clamp(var7, -this.d, this.d);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new d5(var1.M + var10, Mth.clamp(var1.G + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float P(float var1, float var2) {
      return var1 + this.w.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.f = 0.0F;
      this.d = 0.0F;
   }
}
