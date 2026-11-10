package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class l6 implements z {
   private final SecureRandom G = new SecureRandom();
   private int M;
   private int r;
   private float U;
   private float c;

   @Override
   public l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5) {
      this.M++;
      this.r++;
      float var6 = a_.x(var1.j, var2.j);
      float var7 = var2.l - var1.l;
      if (this.M % 5 == 0) {
         float var8 = var5 ? this.a(-1.0F, 1.0F) : this.a(-3.0F, 3.0F);
         float var9 = var5 ? this.a(-0.5F, 0.5F) : this.a(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.r > 20 ? 0.45F : 0.25F;
         this.U = class_3532.method_16439(var10, this.U, var8);
         this.c = class_3532.method_16439(var10, this.c, var9);
      }

      float var12 = var4 ? this.a(80.0F, 130.0F) : this.a(50.0F, 80.0F);
      float var13 = var4 ? this.a(50.0F, 80.0F) : this.a(30.0F, 50.0F);
      float var14 = class_3532.method_15363(var6 + this.U, -var12, var12);
      float var11 = class_3532.method_15363(var7 + this.c, -var13, var13);
      return new l4(var1.j + var14, class_3532.method_15363(var1.l + var11, -90.0F, 90.0F));
   }

   private float a(float var1, float var2) {
      return var1 + this.G.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.M = 0;
      this.r = 0;
      this.U = 0.0F;
      this.c = 0.0F;
   }

   @Override
   public void targetChanged(class_1309 var1) {
      this.r = 0;
   }
}
