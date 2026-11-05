package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class l8 implements lg {
   private final SecureRandom o = new SecureRandom();
   private float s;
   private float u;
   private float f = 0.0F;
   private float h = 0.0F;

   @Override
   public lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.s += 0.065F;
         this.u += 0.045F;
         float var6 = (float)(Math.sin(this.s) * 0.8F + Math.cos(this.s * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.u) * 0.5);
         float var8 = le.l(var1.z, var2.z + var6);
         float var9 = Mth.clamp(var2.I + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.z;
         float var11 = var9 - var1.I;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.g(40.0F, 60.0F) : this.g(55.0F, 85.0F);
         float var14 = var5 ? this.g(25.0F, 40.0F) : this.g(35.0F, 55.0F);
         this.f = Mth.lerp(0.3F, this.f, var13);
         this.h = Mth.lerp(0.3F, this.h, var14);
         float var15 = Mth.clamp(var10, -this.f, this.f);
         float var16 = Mth.clamp(var11, -this.h, this.h);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new lx(var1.z + var15, Mth.clamp(var1.I + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float g(float var1, float var2) {
      return var1 + this.o.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.s = this.u = this.f = this.h = 0.0F;
   }
}
