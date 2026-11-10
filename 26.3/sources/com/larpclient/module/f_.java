package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class f_ implements k {
   private final SecureRandom P = new SecureRandom();
   private int n;
   private int L;
   private float l;
   private float b;

   @Override
   public c rotate(c var1, c var2, LivingEntity var3, boolean var4, boolean var5) {
      this.n++;
      this.L++;
      float var6 = w.J(var1.V, var2.V);
      float var7 = var2.T - var1.T;
      if (this.n % 5 == 0) {
         float var8 = var5 ? this.L(-1.0F, 1.0F) : this.L(-3.0F, 3.0F);
         float var9 = var5 ? this.L(-0.5F, 0.5F) : this.L(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.L > 20 ? 0.45F : 0.25F;
         this.l = Mth.lerp(var10, this.l, var8);
         this.b = Mth.lerp(var10, this.b, var9);
      }

      float var12 = var4 ? this.L(80.0F, 130.0F) : this.L(50.0F, 80.0F);
      float var13 = var4 ? this.L(50.0F, 80.0F) : this.L(30.0F, 50.0F);
      float var14 = Mth.clamp(var6 + this.l, -var12, var12);
      float var11 = Mth.clamp(var7 + this.b, -var13, var13);
      return new c(var1.V + var14, Mth.clamp(var1.T + var11, -90.0F, 90.0F));
   }

   private float L(float var1, float var2) {
      return var1 + this.P.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.n = 0;
      this.L = 0;
      this.l = 0.0F;
      this.b = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.L = 0;
   }
}
