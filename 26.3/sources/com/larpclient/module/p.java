package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class p implements k {
   private final SecureRandom n = new SecureRandom();
   private float e;
   private float V;
   private float G = 0.0F;
   private float Q = 0.0F;

   @Override
   public c rotate(c var1, c var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.e += 0.065F;
         this.V += 0.045F;
         float var6 = (float)(Math.sin(this.e) * 0.8F + Math.cos(this.e * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.V) * 0.5);
         float var8 = w.P(var1.V, var2.V + var6);
         float var9 = Mth.clamp(var2.T + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.V;
         float var11 = var9 - var1.T;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.N(40.0F, 60.0F) : this.N(55.0F, 85.0F);
         float var14 = var5 ? this.N(25.0F, 40.0F) : this.N(35.0F, 55.0F);
         this.G = Mth.lerp(0.3F, this.G, var13);
         this.Q = Mth.lerp(0.3F, this.Q, var14);
         float var15 = Mth.clamp(var10, -this.G, this.G);
         float var16 = Mth.clamp(var11, -this.Q, this.Q);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new c(var1.V + var15, Mth.clamp(var1.T + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float N(float var1, float var2) {
      return var1 + this.n.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.e = this.V = this.G = this.Q = 0.0F;
   }
}
