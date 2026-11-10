package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_3532;

public final class a implements z {
   private final Random p = new Random();
   private final float[] e = new float[30];
   private int G;
   private int q;
   private int l;

   public a() {
      this.reset();
   }

   @Override
   public l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         class_310 var6 = class_310.method_1551();
         System.arraycopy(this.e, 0, this.e, 1, 29);
         this.e[0] = var2.l;
         this.G++;
         if (var4) {
            this.l = 2;
         } else if (this.l > 0) {
            this.l--;
         }

         float var7 = (var6.field_1724 != null ? var6.field_1724.field_6012 : 0) + var6.method_61966().method_60637(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = class_3532.method_15340(10 - this.G, 0, 29);
         float var11 = this.e[var10] + var9 * 1.5F;
         float var12 = this.O(var1.l, var11, this.P(0.1F, 0.5F));
         float var13 = this.O(var1.j, var2.j + var8, this.P(0.1F, 0.4F));
         if (var4 || this.l > 0) {
            if (!var5) {
               var13 = var2.j;
               var12 = var2.l;
            }

            var8 = class_3532.method_15363(var8, -0.05F, 0.05F);
            var9 = class_3532.method_15363(var9, -0.05F, 0.05F);
         }

         if (this.G <= 4 && this.q % 2 == 0 && var6.field_1724 != null && !var4) {
            var13 = AuraModule.s();
         }

         if (var4) {
            this.q++;
            this.G = 0;
         }

         return new l4(var13 + var8, class_3532.method_15363(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float O(float var1, float var2, float var3) {
      float var4 = class_3532.method_15363(var3, 0.0F, 1.0F);
      float var5 = a_.x(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = a_.r(var1, var1 + var5 * var4);
      return Math.abs(a_.x(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float P(float var1, float var2) {
      return var1 + this.p.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(l4 var1, l4 var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(a_.x(var1.j, var2.j));
         float var4 = Math.abs(var2.l - var1.l);
         return class_3532.method_15340((int)Math.ceil(Math.max(var3 / 45.0F, var4 / 25.0F)), 0, 8);
      } else {
         return 0;
      }
   }

   @Override
   public float yawSpeed() {
      return 45.0F;
   }

   @Override
   public float pitchSpeed() {
      return 25.0F;
   }

   @Override
   public void reset() {
      Arrays.fill(this.e, z() ? AuraModule.d() : 0.0F);
      this.G = 0;
      this.q = 0;
      this.l = 0;
   }

   private static boolean z() {
      class_310 var0 = class_310.method_1551();
      return var0 != null && var0.field_1724 != null;
   }

   @Override
   public void targetChanged(class_1309 var1) {
      Arrays.fill(this.e, z() ? AuraModule.d() : 0.0F);
      this.G = 0;
   }
}
