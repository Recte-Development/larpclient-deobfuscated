package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class j implements x {
   private final SecureRandom D = new SecureRandom();
   private float M = 0.0F;
   private float i = 0.0F;

   @Override
   public u rotate(u var1, u var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = tv.p(var1.R, var2.R);
         float var7 = var2.x - var1.x;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.r(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.r(35.0F, 50.0F);
         } else {
            var9 = this.r(28.0F, 42.0F);
         }

         this.M = Mth.lerp(0.35F, this.M, var9);
         this.i = Mth.lerp(0.3F, this.i, var9 * 0.75F);
         float var10 = Mth.clamp(var6, -this.M, this.M);
         float var11 = Mth.clamp(var7, -this.i, this.i);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new u(var1.R + var10, Mth.clamp(var1.x + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float r(float var1, float var2) {
      return var1 + this.D.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.M = 0.0F;
      this.i = 0.0F;
   }
}
