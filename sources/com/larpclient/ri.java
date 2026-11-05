package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class ri {
   public static final double O = 2.2;
   public static final double j = 0.18;
   public static final double K = 0.56;
   public static final double l = 0.78;
   public static final double y = 2.2;
   public static final double Q = 0.035;
   public static final double U = 1.9042969F;

   private static double s(double var0) {
      return yc.H(var0);
   }

   public static double X(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? s(var0 / 0.12) * R(var0) : 0.0;
   }

   public static double R(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? s((2.2 - var0) / 0.65) : 0.0;
   }

   public static double M(double var0) {
      return Double.isFinite(var0) ? 1.0 - 0.9 * s((var0 - 0.56) / 0.21999999999999997) : 1.0;
   }

   public static double O(double var0) {
      if (var0 >= 0.56) {
         return 0.035 + 1.9042969F * M(var0);
      }

      double var2 = Math.clamp((var0 - 0.18) / 0.38000000000000006, 0.0, 1.0);
      return 1.939296875 + 1.495703125 * (1.0 - var2 * var2);
   }

   private static Vec3 K(Vec3 var0, Vec3 var1, double var2) {
      double var4 = Math.cos(var2);
      double var6 = Math.sin(var2);
      return var1.add(var0.x * var4 + var0.z * var6, var0.y, -var0.x * var6 + var0.z * var4);
   }

   public static ri.z t(Vec3 var0, double var1, double var3, ol var5) {
      double var6 = X(var3);
      if (!(var6 <= 0.0) && Double.isFinite(var1) && Double.isFinite(var0.x + var0.y + var0.z)) {
         ArrayList var8 = new ArrayList(var5.d() * 2);
         if (var3 >= 0.78 && var3 < 1.48) {
            double var9 = (var3 - 0.78) / 0.7;
            double var11 = 0.28 + 2.32 * s(var9);
            double var13 = 0.1 + 0.07 * (1.0 - var9);
            int var15 = oh.i(-3046331, 0.95 * Math.pow(1.0 - var9, 1.4) * var5.D);
            int var16 = oh.i(var15, 0.0);
            int var17 = var5.d();

            for (int var18 = 0; var18 < 2; var18++) {
               double var19 = var11 + (var18 - 1) * var13;
               double var21 = var19 + var13;

               for (int var23 = 0; var23 < var17; var23++) {
                  double var24 = var23 * Math.PI * 2.0 / var17;
                  double var26 = (var23 + 1) * Math.PI * 2.0 / var17;
                  var8.add(
                     new ri.x(
                        K(new Vec3(Math.cos(var24) * var19, 0.025, Math.sin(var24) * var19), var0, var1),
                        K(new Vec3(Math.cos(var26) * var19, 0.025, Math.sin(var26) * var19), var0, var1),
                        K(new Vec3(Math.cos(var26) * var21, 0.025, Math.sin(var26) * var21), var0, var1),
                        K(new Vec3(Math.cos(var24) * var21, 0.025, Math.sin(var24) * var21), var0, var1),
                        var18 == 0 ? var16 : var15,
                        var18 == 0 ? var15 : var16
                     )
                  );
               }
            }
         }

         return new ri.z(var0, var1, new ri.y(O(var3), var6), List.copyOf(var8));
      } else {
         return ri.z.EMPTY;
      }
   }

   public static Matrix4f J(ri.z var0, Vec3 var1) {
      return new Matrix4f()
         .translate((float)(var0.origin.x - var1.x), (float)(var0.origin.y - var1.y), (float)(var0.origin.z - var1.z))
         .rotateY((float)var0.yaw)
         .translate(0.0F, (float)var0.pumpkin.bottom, 0.0F)
         .scale(2.2F);
   }

   public static ez.q[] r(ez var0, double var1) {
      ez.q[] var3 = new ez.q[6];
      double var4 = M(var1);
      double var6 = 1.0 + 0.3 * (1.0 - var4);
      double var8 = R(var1);
      Quaternionf var10 = new Quaternionf().rotateY((float)(Math.PI + var0.A));
      Vec3 var11 = new Vec3(var6, var4, var6);

      for (int var12 = 0; var12 < var3.length; var12++) {
         double var13 = ez.k(var12, var0.d) / 16.0 * 0.9375;
         double var15 = (24.0 - ez.X[var12][1]) / 16.0 * 0.9375;
         Vec3 var17 = K(new Vec3(var13 * var6, 0.035 + var15 * var4, 0.0), var0.D, var0.A);
         var3[var12] = new ez.q(var17, 0.0, 0.0, 0.0, 1.0, var8, 0.0, var1 >= 0.78, var10, var11);
      }

      return var3;
   }

   public static void U(r6 var0, PoseStack var1, y2 var2, ri.z var3, Vec3 var4, boolean var5) {
      xy.c(var1, var2, var3, var4, var5);
      var0.blend(fk.h.z);
      double var6 = var5 ? 0.3 : 1.0;

      for (ri.x var9 : var3.wave) {
         int var10 = oh.S(var9.inner, var6);
         int var11 = oh.S(var9.outer, var6);
         var0.vertex(var9.a.x, var9.a.y, var9.a.z, var10);
         var0.vertex(var9.b.x, var9.b.y, var9.b.z, var10);
         var0.vertex(var9.c.x, var9.c.y, var9.c.z, var11);
         var0.vertex(var9.d.x, var9.d.y, var9.d.z, var11);
      }

      h_.A("Halloween shockwave vertices", var3.vertices());
   }

   private ri() {
   }

   public record x(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int inner, int outer) {
   }

   public record y(double bottom, double alpha) {
   }

   public record z(Vec3 origin, double yaw, ri.y pumpkin, List<ri.x> wave) {
      public static final ri.z EMPTY = new ri.z(Vec3.ZERO, 0.0, null, List.of());

      public int vertices() {
         return this.wave.size() * 4;
      }
   }
}
