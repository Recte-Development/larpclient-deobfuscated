package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4185;

public final class wn extends class_4185 {
   private final wn.c C;
   private final qb c = new qb(0.0);
   private x.h e = x.h.REST;
   private double O;
   private float D;
   private float z;
   private long N;
   public Consumer<class_332> G;
   public static final int q = -16777216;
   public static final float S = 0.55F;
   public static final uw.r r = uw.r.x;
   public static final float m = 1.375F;
   public static final float a = 4.6F;
   public static final float U = 10.2F;
   public static final float T = 2.6F;
   public static final float w = 56.0F;
   public static final float W = 3.0F;
   public static final float s = 56.0F;
   public static final float Z = 90.0F;

   public wn(class_2561 var1, wn.c var2, Runnable var3, ue.c var4) {
      super(var4.x(), var4.y(), var4.width(), var4.height(), var1, var1x -> var3.run(), field_40754);
      this.C = var2;
   }

   public static int O(wn.c var0, double var1) {
      int var3 = switch (var0) {
         case H -> -2171170;
         case i -> -5592406;
         case j -> -6513508;
         case J -> -3618616;
      };
      return ei.W(var3, -657931, var1);
   }

   public static int O(double var0, boolean var2) {
      return var2 ? -657931 : ei.D(-3092272, var0 * 0.65);
   }

   public static float l(wn.c var0) {
      return switch (var0) {
         case H -> 1.2F;
         case i -> 1.12F;
         case j, J -> 1.05F;
      };
   }

   public static double B(double var0, double var2) {
      double var4 = Math.clamp(1.0 - var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static void I(class_332 var0, float var1, int var2, float var3, int var4) {
      if (!(var3 <= 0.0F) && var4 >>> 24 != 0) {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2);
            var0.method_51448().scale(var3 / 2.0F, 1.0F);
            var0.method_25294(-1, 0, 1, 1, var4);
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public void d(x.h var1) {
      this.e = var1;
   }

   public double G() {
      return this.e.alpha();
   }

   public double s() {
      return this.O;
   }

   protected void method_75752(class_332 var1, int var2, int var3, float var4) {
      if (!(this.e.alpha() <= 0.0)) {
         var1.method_51448().pushMatrix();

         try {
            float var5 = this.method_46426() + this.method_25368() / 2.0F;
            float var6 = this.method_46427() + this.method_25364() / 2.0F;
            var1.method_51448().translate((float)(var5 + this.e.x()), (float)(var6 + this.e.y()));
            var1.method_51448().rotate((float)this.e.rotation());
            var1.method_51448().scale((float)this.e.scaleX(), (float)this.e.scaleY());
            var1.method_51448().translate(-var5, -var6);
            this.A(var1, var2, var3);
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }

   private void A(class_332 var1, int var2, int var3) {
      boolean var4 = LarpClient.b().e().F().k;
      boolean var5 = class_310.method_1551().method_48186().method_48183();
      int var6 = this.field_22763 && this.method_25370() && var5 ? 1 : 0;
      double var7 = this.field_22763 && this.method_49606() && !var5 ? 1.0 : 0.0;
      double var9 = var4 ? this.c.a(var7, 140) : var7;
      if (var6 != 0) {
         var9 = 1.0;
      }

      this.O = var9;
      if (this.G != null) {
         this.G.accept(var1);
      } else {
         int var11 = this.method_46426();
         int var12 = this.method_46427();
         int var13 = this.method_25368();
         int var14 = this.method_25364();
         double var15 = this.e.alpha();
         float var17 = Math.min(l(this.C), (var14 - 4) / 9.0F) * 1.375F;
         String var18 = uw.n(this.method_25369().getString(), (int)((var13 - 40) / var17), r);
         float var19 = uw.z(var18, r) * var17;
         float var20 = var11 + (var13 - var19) / 2.0F;
         float var21 = var12 + var14 / 2.0F - 4.6F * var17;
         int var22 = this.field_22763 && var2 >= 0 && var3 >= 0 && !var5 ? 1 : 0;
         float var23 = var20 + var19 / 2.0F;
         float var24 = var12 + var14 / 2.0F;
         this.i(var22 != 0 && var4, var2, var3, var20, var21, var19, 11.0F * var17, var23, var24);
         var20 += this.D;
         var21 += this.z;
         V(var1, var18, var20, var21, var17, var6 != 0 ? 1.0 : 0.0, var22 != 0 ? var2 : Float.NaN, var22 != 0 ? var3 : Float.NaN, var15);
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(var20, var21);
            var1.method_51448().scale(var17, var17);
            uw.S(var1, var18, 0, 0, ei.R(O(this.C, var9), var15), ei.R(-16777216, var15), 0.55F, r);
         } finally {
            var1.method_51448().popMatrix();
         }

         if (var9 > 0.0) {
            I(
               var1,
               var11 + var13 / 2.0F + this.D,
               (int)Math.min(var21 + 10.2F * var17, var12 + var14 - 1),
               (float)(var19 * var9),
               ei.R(O(var9, (boolean)var6), var15)
            );
         }
      }
   }

   private void i(boolean var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var1) {
         double var12 = Math.max(0.0F, Math.max(var4 - var2, var2 - (var4 + var6)));
         double var14 = Math.max(0.0F, Math.max(var5 - var3, var3 - (var5 + var7)));
         double var16 = B(Math.hypot(var12, var14), 56.0);
         double var18 = var2 - var8;
         double var20 = var3 - var9;
         double var22 = Math.hypot(var18, var20);
         if (var16 > 0.0 && var22 > 0.001) {
            double var24 = 3.0 * var16 * Math.min(1.0, var22 / 24.0);
            var10 = (float)(var18 / var22 * var24);
            var11 = (float)(var20 / var22 * var24 * 0.6);
         }
      }

      long var26 = System.nanoTime();
      float var27 = this.N == 0L ? 1.0F : (float)(1.0 - Math.exp(-(var26 - this.N) / 1000000.0 / 90.0));
      this.N = var26;
      this.D = this.D + (var10 - this.D) * var27;
      this.z = this.z + (var11 - this.z) * var27;
      if (Math.abs(this.D) < 0.01F && Math.abs(this.z) < 0.01F && var10 == 0.0F && var11 == 0.0F) {
         this.D = this.z = 0.0F;
      }
   }

   private static void V(class_332 var0, String var1, float var2, float var3, float var4, double var5, float var7, float var8, double var9) {
      if (!(var5 <= 0.0) || !Float.isNaN(var7)) {
         int var11 = ez.a | 0xFF000000;
         float var12 = var3 + 4.6F * var4;
         float var13 = 0.0F;
         int var14 = 0;

         while (var14 < var1.length()) {
            int var15 = var1.codePointAt(var14);
            String var16 = var1.substring(var14, var14 + Character.charCount(var15));
            var14 += var16.length();
            float var17 = uw.t(var16, r);
            double var18 = Float.isNaN(var7) ? 0.0 : B(Math.hypot(var7 - (var2 + (var13 + var17 / 2.0F) * var4), var8 - var12), 56.0);
            double var20 = Math.max(var5 * 0.8, var18) * var9;
            if (var20 > 0.01 && !var16.isBlank()) {
               var0.method_51448().pushMatrix();

               try {
                  var0.method_51448().translate(var2 + var13 * var4, var3);
                  var0.method_51448().scale(var4, var4);
                  uw.A(var0, var16, 0, 0, ei.D(var11, var20), 2.6F, r);
               } finally {
                  var0.method_51448().popMatrix();
               }
            }

            var13 += var17;
         }
      }
   }

   public enum c {
      H,
      i,
      j,
      J;
   }
}
