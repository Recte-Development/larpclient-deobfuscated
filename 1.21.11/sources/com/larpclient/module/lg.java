package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class lg implements z {
   private final SecureRandom h = new SecureRandom();
   private float R;
   private float F;
   private float b = 0.0F;
   private float B = 0.0F;

   @Override
   public l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.R += 0.065F;
         this.F += 0.045F;
         float var6 = (float)(Math.sin(this.R) * 0.8F + Math.cos(this.R * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.F) * 0.5);
         float var8 = a_.H(var1.j, var2.j + var6);
         float var9 = class_3532.method_15363(var2.l + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.j;
         float var11 = var9 - var1.l;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.s(40.0F, 60.0F) : this.s(55.0F, 85.0F);
         float var14 = var5 ? this.s(25.0F, 40.0F) : this.s(35.0F, 55.0F);
         this.b = class_3532.method_16439(0.3F, this.b, var13);
         this.B = class_3532.method_16439(0.3F, this.B, var14);
         float var15 = class_3532.method_15363(var10, -this.b, this.b);
         float var16 = class_3532.method_15363(var11, -this.B, this.B);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new l4(var1.j + var15, class_3532.method_15363(var1.l + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float s(float var1, float var2) {
      return var1 + this.h.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.R = this.F = this.b = this.B = 0.0F;
   }
}
