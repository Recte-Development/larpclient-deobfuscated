package com.larpclient;

import net.minecraft.class_12249;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_625;
import net.minecraft.class_630;

public final class i9 {
   public static final class_2960 k = class_2960.method_60656("textures/entity/enderdragon/dragon.png");
   public static final float A = 0.15F;
   public static final float L = 2.25F;
   public static final float x = 3.4F;
   public static final float l = 4.2F;
   public static final int a = 48;
   public static final float h = 50.0F;
   public static final float M = 64.0F;

   public static float C(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float z(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float v(float var0, float var1, float var2, float var3) {
      float var4 = l(var0);
      float var5 = l(var1);
      float var6 = l(var2);
      float var7 = l(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return L(var8, 0.09817477F, var7);
   }

   public static i9.m y(float var0, float var1, float var2, float var3, float var4) {
      float var5 = l(var1);
      float var6 = l(var2);
      float var7 = l(var3);
      float var8 = l(var4);
      float var9 = L(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = L(var9, -85.0F, var8);
      var10 = L(var10, -8.0F, var8);
      var11 = L(var11, -4.0F, var8);
      var12 = L(var12, -5.0F, var8);
      var13 = L(var13, 0.1F, var8);
      return new i9.m(
         H(var9) - (float)Math.cos(var0) * 0.2F * var13,
         H(var10) + (float)Math.sin(var0 - 0.6F) * 0.24F * var13,
         H(var11) - (float)(Math.sin(var0) + 0.125) * 0.8F * var13,
         H(var12) - (float)(Math.sin(var0 + 2.0F) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float s(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float P(int var0) {
      double var1 = var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static class_1921 z(int var0) {
      return var0 >>> 24 == 255 ? class_12249.method_75994(k) : class_12249.method_76000(k);
   }

   private static float l(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float L(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float H(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private i9() {
   }

   public static final class g {
      private final class_630 g;
      private final class_630 l;
      private final class_630 v;
      private final class_630 L;

      public g(class_630 var1) {
         class_630 var2 = var1.method_32086("body");
         this.g = var2.method_32086("left_wing");
         this.l = this.g.method_32086("left_wing_tip");
         this.v = var2.method_32086("right_wing");
         this.L = this.v.method_32086("right_wing_tip");
      }

      public static i9.g U() {
         return new i9.g(class_625.method_62128().method_32109());
      }

      public class_630 h(int var1) {
         return var1 > 0 ? this.g : this.v;
      }

      public void d(i9.m var1) {
         this.g.method_2851(0.0F, 0.0F, 0.0F);
         this.v.method_2851(0.0F, 0.0F, 0.0F);
         this.g.method_33425(var1.wingX(), var1.wingY(), var1.wingZ());
         this.v.method_33425(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.l.method_33425(0.0F, 0.0F, var1.tipZ());
         this.L.method_33425(0.0F, 0.0F, -var1.tipZ());
      }

      public static void d(class_4587 var0, int var1, float var2, boolean var3) {
         var0.method_46416(var1 * 2.25F / 16.0F, i9.C(var3) / 16.0F, i9.z(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.method_22905(var4, var4, var4);
      }

      public void t(class_4587 var1, class_4588 var2, float var3, boolean var4, int var5, int var6) {
         for (byte var7 = -1; var7 <= 1; var7 += 2) {
            var1.method_22903();

            try {
               d(var1, var7, var3, var4);
               this.h(var7).method_22699(var1, var2, var5, class_4608.field_21444, var6);
            } finally {
               var1.method_22909();
            }
         }
      }
   }

   public record m(float wingX, float wingY, float wingZ, float tipZ) {
   }
}
