package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class l5 implements lg {
   private final SecureRandom U = new SecureRandom();
   private int v;
   private int J;
   private float X;
   private float g;

   @Override
   public lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5) {
      this.v++;
      this.J++;
      float var6 = le.P(var1.z, var2.z);
      float var7 = var2.I - var1.I;
      if (this.v % 5 == 0) {
         float var8 = var5 ? this.Q(-1.0F, 1.0F) : this.Q(-3.0F, 3.0F);
         float var9 = var5 ? this.Q(-0.5F, 0.5F) : this.Q(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.J > 20 ? 0.45F : 0.25F;
         this.X = Mth.lerp(var10, this.X, var8);
         this.g = Mth.lerp(var10, this.g, var9);
      }

      float var12 = var4 ? this.Q(80.0F, 130.0F) : this.Q(50.0F, 80.0F);
      float var13 = var4 ? this.Q(50.0F, 80.0F) : this.Q(30.0F, 50.0F);
      float var14 = Mth.clamp(var6 + this.X, -var12, var12);
      float var11 = Mth.clamp(var7 + this.g, -var13, var13);
      return new lx(var1.z + var14, Mth.clamp(var1.I + var11, -90.0F, 90.0F));
   }

   private float Q(float var1, float var2) {
      return var1 + this.U.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.v = 0;
      this.J = 0;
      this.X = 0.0F;
      this.g = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.J = 0;
   }
}
