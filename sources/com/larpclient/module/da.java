package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class da implements t {
   private final SecureRandom s = new SecureRandom();
   private float t;
   private float P;
   private float u = 0.0F;
   private float N = 0.0F;

   @Override
   public d5 rotate(d5 var1, d5 var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.t += 0.065F;
         this.P += 0.045F;
         float var6 = (float)(Math.sin(this.t) * 0.8F + Math.cos(this.t * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.P) * 0.5);
         float var8 = dj.A(var1.M, var2.M + var6);
         float var9 = Mth.clamp(var2.G + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.M;
         float var11 = var9 - var1.G;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.h(40.0F, 60.0F) : this.h(55.0F, 85.0F);
         float var14 = var5 ? this.h(25.0F, 40.0F) : this.h(35.0F, 55.0F);
         this.u = Mth.lerp(0.3F, this.u, var13);
         this.N = Mth.lerp(0.3F, this.N, var14);
         float var15 = Mth.clamp(var10, -this.u, this.u);
         float var16 = Mth.clamp(var11, -this.N, this.N);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new d5(var1.M + var15, Mth.clamp(var1.G + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float h(float var1, float var2) {
      return var1 + this.s.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.t = this.P = this.u = this.N = 0.0F;
   }
}
