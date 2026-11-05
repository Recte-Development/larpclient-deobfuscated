package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class lu implements lg {
   private final SecureRandom O = new SecureRandom();
   private float v = 0.0F;
   private float H = 0.0F;

   @Override
   public lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = le.P(var1.z, var2.z);
         float var7 = var2.I - var1.I;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.s(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.s(35.0F, 50.0F);
         } else {
            var9 = this.s(28.0F, 42.0F);
         }

         this.v = Mth.lerp(0.35F, this.v, var9);
         this.H = Mth.lerp(0.3F, this.H, var9 * 0.75F);
         float var10 = Mth.clamp(var6, -this.v, this.v);
         float var11 = Mth.clamp(var7, -this.H, this.H);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new lx(var1.z + var10, Mth.clamp(var1.I + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float s(float var1, float var2) {
      return var1 + this.O.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.v = 0.0F;
      this.H = 0.0F;
   }
}
