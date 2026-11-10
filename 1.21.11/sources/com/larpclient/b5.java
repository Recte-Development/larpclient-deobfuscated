package com.larpclient;

import net.minecraft.class_1011;
import net.minecraft.class_10537;
import net.minecraft.class_10539;
import net.minecraft.class_1060;
import net.minecraft.class_10799;
import net.minecraft.class_1084;
import net.minecraft.class_12253;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_332;

public final class b5 {
   private static final b5.k m = new b5.k("live");
   private static final b5.k u = new b5.k("preview");

   private b5() {
   }

   private static b5.w O(b5.k var0, f6.l var1) {
      class_1060 var2 = class_310.method_1551().method_1531();
      if (var0.Z != var2) {
         var0.N = null;
         var0.b = null;
         var0.Z = var2;
      }

      if (var1.equals(var0.b)) {
         return null;
      }

      if (var0.N == null || !var0.N.W.equals(var1)) {
         b5.w var3 = null;

         try {
            var3 = new b5.w(var0.D, var1);
            var2.method_65876(var0.D, var3);
         } catch (RuntimeException var5) {
            if (var3 != null) {
               var3.close();
            }

            var0.b = var1;
            q8.O("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
            return null;
         }

         var0.N = var3;
         var0.b = null;
         q8.v("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
      }

      return var0.N;
   }

   public static void k(class_332 var0, int var1, int var2, String var3, int var4, int var5, i_ var6, int var7, boolean var8, double var9, boolean var11) {
      if (var7 >>> 24 != 0) {
         b5.k var12 = var11 ? u : m;
         double var13 = class_310.method_1551().method_22683().method_4495();
         double var15 = f6.s(Math.hypot(var0.method_51448().m00(), var0.method_51448().m01()) * var13);
         f6.l var17 = new f6.l(var3, var4, var5, var8, var15, var6);
         b5.w var18 = O(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            gu.Z(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            gu.Z(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.method_51448().pushMatrix();

            try {
               var0.method_51448().translate(var1, var2);
               double var19 = var18.p % 2 == 0 ? 0.0 : 0.5;
               double var21 = var0.method_51448().m20() * var13;
               double var23 = var0.method_51448().m21() * var13;
               var0.method_51448()
                  .translateLocal((float)((Math.round(var21 - var19) + var19 - var21) / var13), (float)((Math.round(var23 - var19) + var19 - var23) / var13));
               if (var9 != 0.0) {
                  var0.method_51448().rotate((float)var9);
               }

               var0.method_51448().scale((float)(1.0 / var15));
               var0.method_51448().translate(-var18.p / 2.0F, -var18.x / 2.0F);
               F(var0, var12.D, var18, var7, var8);
            } finally {
               var0.method_51448().popMatrix();
            }
         }
      }
   }

   public static void S(class_332 var0, int var1, int var2, g.l var3, int var4, int var5, double var6) {
      if (var5 >>> 24 != 0) {
         double var8 = 2.0 * var4 / Math.max(var3.width(), var3.height());
         float var10 = (float)(var3.width() * var8);
         float var11 = (float)(var3.height() * var8);
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2);
            if (var6 != 0.0) {
               var0.method_51448().rotate((float)var6);
            }

            var0.method_51448().scale(var10 / 64.0F, var11 / 64.0F);
            var0.method_25293(
               class_10799.field_56883,
               var3.id(),
               -32,
               -32,
               0.0F,
               0.0F,
               64,
               64,
               var3.width(),
               var3.height(),
               var3.width(),
               var3.height(),
               fp.R(var5 | 16777215)
            );
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static int F(g.l var0, int var1, boolean var2) {
      double var3 = 2.0 * var1 / Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot(var0.width(), var0.height()) * var3 / 2.0 : var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void F(class_332 var0, class_2960 var1, b5.w var2, int var3, boolean var4) {
      if (var4) {
         var0.method_25293(class_10799.field_56883, var1, 0, 0, 0.0F, var2.x, var2.p, var2.x, var2.p, var2.x, var2.p, var2.x * 2, fp.R(var3 & 0xFF000000));
      }

      var0.method_25293(class_10799.field_56883, var1, 0, 0, 0.0F, 0.0F, var2.p, var2.x, var2.p, var2.x, var2.p, var2.x * 2, fp.R(var3));
   }

   private static final class k {
      final class_2960 D;
      b5.w N;
      class_1060 Z;
      f6.l b;

      k(String var1) {
         this.D = class_2960.method_60655("larp-client", "crosshair/" + var1);
      }
   }

   private static final class w extends class_10537 {
      final f6.l W;
      final int p;
      final int x;
      final f6.u w;

      w(class_2960 var1, f6.l var2) {
         super(var1);
         this.W = var2;
         this.w = new f6(var2).K();
         this.p = this.w.width();
         this.x = this.w.height();
      }

      public class_10539 method_65809(class_3300 var1) {
         class_1011 var2 = new class_1011(this.p, this.x * 2, false);

         try {
            for (int var3 = 0; var3 < this.x * 2; var3++) {
               for (int var4 = 0; var4 < this.p; var4++) {
                  var2.method_61941(var4, var3, this.w.pixels()[var3 * this.p + var4]);
               }
            }

            return new class_10539(var2, new class_1084(true, true, class_12253.field_64077, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }
}
