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

final class qe {
   static final int x = 116;
   static final int B = 48;
   static final int R = 4;
   private static final double I = 3.5;
   private static final int S = -((int)Math.ceil(116.0 * Math.sqrt(0.5))) - 1;
   private static final int G = -117;
   private static final int A = -S * 2;
   private static final int e = 117 - (int)Math.floor(48.0 * Math.sqrt(0.5) - 2.0);
   private static final class_2960 K = class_2960.method_60655("larp-client", "emote/wheel_wedge");
   private static qe.g b;
   private static class_1060 Q;
   private static int L;

   private qe() {
   }

   static double K(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 48.0, 116.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static qe.g L() {
      class_1060 var0 = class_310.method_1551().method_1531();
      if (var0 != Q) {
         b = null;
         L = 0;
         Q = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(class_310.method_1551().method_22683().method_4495()), 1.0, 6.0);
      if (var1 == L) {
         return null;
      }

      if (b == null || b.m != var1) {
         qe.g var2 = null;

         try {
            var2 = new qe.g(var1);
            var0.method_65876(K, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            L = var1;
            q8.O("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         b = var2;
         q8.v("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return b;
   }

   static void S(class_332 var0, int var1, double var2, int var4, int var5, int var6) {
      qe.g var7 = L();
      if (var7 == null) {
         double var8 = (var1 - 1) * Math.PI / 2.0;
         byte var10 = 82;
         gu.Q(var0, (int)Math.round(Math.cos(var8) * var10), (int)Math.round(Math.sin(var8) * var10), 22, var4);
      } else {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().rotate((float)(var1 * Math.PI / 2.0));
            var0.method_51448().translate(0.0F, (float)(-var2));
            H(var0, var7, 0, var4);
            if (var5 >>> 24 != 0) {
               H(var0, var7, 1, var5);
            }

            if (var6 >>> 24 != 0) {
               H(var0, var7, 2, var6);
            }
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   private static void H(class_332 var0, qe.g var1, int var2, int var3) {
      var0.method_25293(class_10799.field_56883, K, S, -117, 0.0F, var1.S * var2, A, e, var1.f, var1.S, var1.f, var1.S * 3, fp.R(var3));
   }

   private static final class g extends class_10537 {
      final int m;
      final int f;
      final int S;

      g(int var1) {
         super(qe.K);
         this.m = var1;
         this.f = qe.A * var1;
         this.S = qe.e * var1;
      }

      public class_10539 method_65809(class_3300 var1) {
         class_1011 var2 = new class_1011(this.f, this.S * 3, false);

         try {
            for (int var3 = 0; var3 < this.S; var3++) {
               for (int var4 = 0; var4 < this.f; var4++) {
                  double var5 = qe.S + (var4 + 0.5) / this.m;
                  double var7 = -117.0 + (var3 + 0.5) / this.m;
                  double var9 = qe.K(var5, var7);
                  double var11 = this.r(var9);
                  double var13 = this.r(var9) - this.r(var9 - 1.1);
                  double var15 = Math.min(var11, this.r(Math.hypot(var5, var7) - 112.5));
                  var2.method_61941(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.method_61941(var4, var3 + this.S, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
                  var2.method_61941(var4, var3 + this.S * 2, (int)Math.round(var15 * 255.0) << 24 | 16777215);
               }
            }

            return new class_10539(var2, new class_1084(true, true, class_12253.field_64077, 0.0F));
         } catch (RuntimeException var17) {
            var2.close();
            throw var17;
         }
      }

      private double r(double var1) {
         return Math.clamp(var1 * this.m + 0.5, 0.0, 1.0);
      }
   }
}
