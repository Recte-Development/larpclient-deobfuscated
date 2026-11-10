package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class ll implements z {
   private final SecureRandom I = new SecureRandom();
   private float F = 0.0F;
   private float A = 0.0F;

   @Override
   public l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = a_.x(var1.j, var2.j);
         float var7 = var2.l - var1.l;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.U(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.U(35.0F, 50.0F);
         } else {
            var9 = this.U(28.0F, 42.0F);
         }

         this.F = class_3532.method_16439(0.35F, this.F, var9);
         this.A = class_3532.method_16439(0.3F, this.A, var9 * 0.75F);
         float var10 = class_3532.method_15363(var6, -this.F, this.F);
         float var11 = class_3532.method_15363(var7, -this.A, this.A);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new l4(var1.j + var10, class_3532.method_15363(var1.l + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float U(float var1, float var2) {
      return var1 + this.I.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.F = 0.0F;
      this.A = 0.0F;
   }
}
