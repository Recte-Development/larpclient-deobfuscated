package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class i implements t {
   private final SecureRandom D = new SecureRandom();
   private int C;
   private int V;
   private float S;
   private float Z;

   @Override
   public d5 rotate(d5 var1, d5 var2, LivingEntity var3, boolean var4, boolean var5) {
      this.C++;
      this.V++;
      float var6 = dj.h(var1.M, var2.M);
      float var7 = var2.G - var1.G;
      if (this.C % 5 == 0) {
         float var8 = var5 ? this.w(-1.0F, 1.0F) : this.w(-3.0F, 3.0F);
         float var9 = var5 ? this.w(-0.5F, 0.5F) : this.w(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.V > 20 ? 0.45F : 0.25F;
         this.S = Mth.lerp(var10, this.S, var8);
         this.Z = Mth.lerp(var10, this.Z, var9);
      }

      float var12 = var4 ? this.w(80.0F, 130.0F) : this.w(50.0F, 80.0F);
      float var13 = var4 ? this.w(50.0F, 80.0F) : this.w(30.0F, 50.0F);
      float var14 = Mth.clamp(var6 + this.S, -var12, var12);
      float var11 = Mth.clamp(var7 + this.Z, -var13, var13);
      return new d5(var1.M + var14, Mth.clamp(var1.G + var11, -90.0F, 90.0F));
   }

   private float w(float var1, float var2) {
      return var1 + this.D.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.C = 0;
      this.V = 0;
      this.S = 0.0F;
      this.Z = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.V = 0;
   }
}
