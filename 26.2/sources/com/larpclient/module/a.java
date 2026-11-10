package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class a implements x {
   private final SecureRandom F = new SecureRandom();
   private float Q;
   private float S;
   private float a = 0.0F;
   private float L = 0.0F;

   @Override
   public u rotate(u var1, u var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.Q += 0.065F;
         this.S += 0.045F;
         float var6 = (float)(Math.sin(this.Q) * 0.8F + Math.cos(this.Q * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.S) * 0.5);
         float var8 = tv.X(var1.R, var2.R + var6);
         float var9 = Mth.clamp(var2.x + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.R;
         float var11 = var9 - var1.x;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.k(40.0F, 60.0F) : this.k(55.0F, 85.0F);
         float var14 = var5 ? this.k(25.0F, 40.0F) : this.k(35.0F, 55.0F);
         this.a = Mth.lerp(0.3F, this.a, var13);
         this.L = Mth.lerp(0.3F, this.L, var14);
         float var15 = Mth.clamp(var10, -this.a, this.a);
         float var16 = Mth.clamp(var11, -this.L, this.L);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new u(var1.R + var15, Mth.clamp(var1.x + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float k(float var1, float var2) {
      return var1 + this.F.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.Q = this.S = this.a = this.L = 0.0F;
   }
}
