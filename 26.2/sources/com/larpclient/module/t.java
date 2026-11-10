package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class t implements x {
   private final SecureRandom w = new SecureRandom();
   private int d;
   private int H;
   private float k;
   private float E;

   @Override
   public u rotate(u var1, u var2, LivingEntity var3, boolean var4, boolean var5) {
      this.d++;
      this.H++;
      float var6 = tv.p(var1.R, var2.R);
      float var7 = var2.x - var1.x;
      if (this.d % 5 == 0) {
         float var8 = var5 ? this.c(-1.0F, 1.0F) : this.c(-3.0F, 3.0F);
         float var9 = var5 ? this.c(-0.5F, 0.5F) : this.c(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.H > 20 ? 0.45F : 0.25F;
         this.k = Mth.lerp(var10, this.k, var8);
         this.E = Mth.lerp(var10, this.E, var9);
      }

      float var12 = var4 ? this.c(80.0F, 130.0F) : this.c(50.0F, 80.0F);
      float var13 = var4 ? this.c(50.0F, 80.0F) : this.c(30.0F, 50.0F);
      float var14 = Mth.clamp(var6 + this.k, -var12, var12);
      float var11 = Mth.clamp(var7 + this.E, -var13, var13);
      return new u(var1.R + var14, Mth.clamp(var1.x + var11, -90.0F, 90.0F));
   }

   private float c(float var1, float var2) {
      return var1 + this.w.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.d = 0;
      this.H = 0;
      this.k = 0.0F;
      this.E = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.H = 0;
   }
}
