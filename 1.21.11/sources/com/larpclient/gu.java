package com.larpclient;

import net.minecraft.class_10799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.joml.Matrix3x2fStack;

public final class gu {
   public static void Z(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      if (var3 > 0 && var4 > 0) {
         var0.method_25294(var1, var2, var1 + var3, var2 + var4, fp.R(var5));
      }
   }

   public static void Z(class_332 var0, float var1, float var2, float var3, float var4, int var5) {
      if (var3 > var1 && var4 > var2 && var5 >>> 24 != 0) {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2);
            var0.method_51448().scale(var3 - var1, var4 - var2);
            var0.method_25294(0, 0, 1, 1, fp.R(var5));
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static void i(class_332 var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      if (var3 > var1 && var4 > var2 && (var5 | var6) >>> 24 != 0) {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2);
            var0.method_51448().scale(var3 - var1, var4 - var2);
            var0.method_25296(0, 0, 1, 1, fp.R(var5), fp.R(var6));
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static void o(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      float var7 = var3 - var1;
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (!(var5 <= 0.0F) && var6 >>> 24 != 0 && Float.isFinite(var9)) {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2);
            var0.method_51448().rotate((float)Math.atan2(var8, var7));
            var0.method_51448().translate(-var5 / 2.0F, -var5 / 2.0F);
            var0.method_51448().scale(var9 + var5, var5);
            var0.method_25294(0, 0, 1, 1, fp.R(var6));
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static void q(class_332 var0, int var1, int var2, int var3, boolean var4, int var5) {
      if (var3 > 0 && var5 >>> 24 != 0) {
         Matrix3x2fStack var6 = var0.method_51448();
         float var7 = var4 ? var6.m00() : var6.m11();
         float var8 = var7 * class_310.method_1551().method_22683().method_4495();
         if (!(var8 <= 1.001F) && var6.m01() == 0.0F && var6.m10() == 0.0F) {
            float var9 = var4 ? var6.m20() : var6.m21();
            float var10 = (float)Math.floor((var7 * (var4 ? var1 : var2) + var9) * var8 / var7 + 0.001F);
            float var11 = (var10 * var7 / var8 - var9) / var7;
            var6.pushMatrix();

            try {
               if (var4) {
                  var6.translate(var11, var2).scale(1.0F / var8, var3);
               } else {
                  var6.translate(var1, var11).scale(var3, 1.0F / var8);
               }

               var0.method_25294(0, 0, 1, 1, fp.R(var5));
            } finally {
               var6.popMatrix();
            }
         } else {
            Z(var0, var1, var2, var4 ? 1 : var3, var4 ? var3 : 1, var5);
         }
      }
   }

   public static void N(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      ws.r(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void a(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      Z(var0, var1, var2, var3, 1, var5);
      Z(var0, var1, var2 + var4 - 1, var3, 1, var5);
      Z(var0, var1, var2, 1, var4, var5);
      Z(var0, var1 + var3 - 1, var2, 1, var4, var5);
   }

   public static void R(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var0.method_25296(var1, var2, var1 + var3, var2 + var4, fp.R(var5), fp.R(var6));
   }

   public static void Q(class_332 var0, int var1, int var2, int var3, int var4) {
      if (var3 >= 0) {
         ws.r(var0, var1 - var3, var2 - var3, var3 * 2 + 1, var3 * 2 + 1, var3 + 0.5F, var4);
      }
   }

   public static void m(class_332 var0, int var1, int var2, int var3, double var4, double var6, int var8) {
      int var9 = Math.max(4, Math.min(96, var3 * 4));

      for (int var10 = 0; var10 <= var9; var10++) {
         double var11 = var4 + (var6 - var4) * var10 / var9;
         Z(var0, var1 + (int)(Math.cos(var11) * var3), var2 + (int)(Math.sin(var11) * var3), 1, 1, var8);
      }
   }

   public static void W(class_332 var0, class_2960 var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var0.method_25302(class_10799.field_56883, var1, var2, var3, 0.0F, 0.0F, var4, var5, var6, var7, var6, var7);
   }

   public static void c(class_332 var0, int var1, int var2, int var3, int var4) {
      p.o(var0, var1, var2, var3, var4);
      p.W(var0, var1, var2, var3, var4, ez.V, ez.M, 0.8);
   }

   public static boolean X(double var0, double var2, int var4, int var5, int var6, int var7) {
      return var0 >= var4 && var0 < var4 + var6 && var2 >= var5 && var2 < var5 + var7;
   }
}
