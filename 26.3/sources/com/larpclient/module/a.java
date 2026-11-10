package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class a implements k {
   private final SecureRandom W = new SecureRandom();
   private float j = 0.0F;
   private float I = 0.0F;

   @Override
   public c rotate(c var1, c var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = w.J(var1.V, var2.V);
         float var7 = var2.T - var1.T;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.U(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.U(35.0F, 50.0F);
         } else {
            var9 = this.U(28.0F, 42.0F);
         }

         this.j = Mth.lerp(0.35F, this.j, var9);
         this.I = Mth.lerp(0.3F, this.I, var9 * 0.75F);
         float var10 = Mth.clamp(var6, -this.j, this.j);
         float var11 = Mth.clamp(var7, -this.I, this.I);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new c(var1.V + var10, Mth.clamp(var1.T + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float U(float var1, float var2) {
      return var1 + this.W.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.j = 0.0F;
      this.I = 0.0F;
   }
}
