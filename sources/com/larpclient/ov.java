package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class ov {
   public static final double P = 2.2;
   public static final double D = 0.18;
   public static final double y = 0.56;
   public static final double F = 0.78;
   public static final double x = 2.2;
   public static final double j = 0.035;
   public static final double w = 1.9042969F;

   private static double B(double var0) {
      return hp.R(var0);
   }

   public static double L(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? B(var0 / 0.12) * R(var0) : 0.0;
   }

   public static double R(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? B((2.2 - var0) / 0.65) : 0.0;
   }

   public static double Q(double var0) {
      return Double.isFinite(var0) ? 1.0 - 0.9 * B((var0 - 0.56) / 0.21999999999999997) : 1.0;
   }

   public static double D(double var0) {
      if (var0 >= 0.56) {
         return 0.035 + 1.9042969F * Q(var0);
      }

      double var2 = Math.clamp((var0 - 0.18) / 0.38000000000000006, 0.0, 1.0);
      return 1.939296875 + 1.495703125 * (1.0 - var2 * var2);
   }

   private static Vec3 D(Vec3 var0, Vec3 var1, double var2) {
      double var4 = Math.cos(var2);
      double var6 = Math.sin(var2);
      return var1.add(var0.x * var4 + var0.z * var6, var0.y, -var0.x * var6 + var0.z * var4);
   }

   public static ov.q s(Vec3 var0, double var1, double var3, u8 var5) {
      double var6 = L(var3);
      if (!(var6 <= 0.0) && Double.isFinite(var1) && Double.isFinite(var0.x + var0.y + var0.z)) {
         ArrayList var8 = new ArrayList(var5.z() * 2);
         if (var3 >= 0.78 && var3 < 1.48) {
            double var9 = (var3 - 0.78) / 0.7;
            double var11 = 0.28 + 2.32 * B(var9);
            double var13 = 0.1 + 0.07 * (1.0 - var9);
            int var15 = _3.A(-3046331, 0.95 * Math.pow(1.0 - var9, 1.4) * var5.w);
            int var16 = _3.A(var15, 0.0);
            int var17 = var5.z();

            for (int var18 = 0; var18 < 2; var18++) {
               double var19 = var11 + (var18 - 1) * var13;
               double var21 = var19 + var13;

               for (int var23 = 0; var23 < var17; var23++) {
                  double var24 = var23 * Math.PI * 2.0 / var17;
                  double var26 = (var23 + 1) * Math.PI * 2.0 / var17;
                  var8.add(
                     new ov.y(
                        D(new Vec3(Math.cos(var24) * var19, 0.025, Math.sin(var24) * var19), var0, var1),
                        D(new Vec3(Math.cos(var26) * var19, 0.025, Math.sin(var26) * var19), var0, var1),
                        D(new Vec3(Math.cos(var26) * var21, 0.025, Math.sin(var26) * var21), var0, var1),
                        D(new Vec3(Math.cos(var24) * var21, 0.025, Math.sin(var24) * var21), var0, var1),
                        var18 == 0 ? var16 : var15,
                        var18 == 0 ? var15 : var16
                     )
                  );
               }
            }
         }

         return new ov.q(var0, var1, new ov.x(D(var3), var6), List.copyOf(var8));
      } else {
         return ov.q.EMPTY;
      }
   }

   public static Matrix4f W(ov.q var0, Vec3 var1) {
      return new Matrix4f()
         .translate((float)(var0.origin.x - var1.x), (float)(var0.origin.y - var1.y), (float)(var0.origin.z - var1.z))
         .rotateY((float)var0.yaw)
         .translate(0.0F, (float)var0.pumpkin.bottom, 0.0F)
         .scale(2.2F);
   }

   public static ta.y[] P(ta var0, double var1) {
      ta.y[] var3 = new ta.y[6];
      double var4 = Q(var1);
      double var6 = 1.0 + 0.3 * (1.0 - var4);
      double var8 = R(var1);
      Quaternionf var10 = new Quaternionf().rotateY((float)(Math.PI + var0.v));
      Vec3 var11 = new Vec3(var6, var4, var6);

      for (int var12 = 0; var12 < var3.length; var12++) {
         double var13 = ta.Z(var12, var0.T) / 16.0 * 0.9375;
         double var15 = (24.0 - ta.Q[var12][1]) / 16.0 * 0.9375;
         Vec3 var17 = D(new Vec3(var13 * var6, 0.035 + var15 * var4, 0.0), var0.J, var0.v);
         var3[var12] = new ta.y(var17, 0.0, 0.0, 0.0, 1.0, var8, 0.0, var1 >= 0.78, var10, var11);
      }

      return var3;
   }

   public static void W(ki var0, PoseStack var1, xh var2, ov.q var3, Vec3 var4, boolean var5) {
      wu.C(var1, var2, var3, var4, var5);
      var0.blend(pf.x.S);
      double var6 = var5 ? 0.3 : 1.0;

      for (ov.y var9 : var3.wave) {
         int var10 = _3.U(var9.inner, var6);
         int var11 = _3.U(var9.outer, var6);
         var0.vertex(var9.a.x, var9.a.y, var9.a.z, var10);
         var0.vertex(var9.b.x, var9.b.y, var9.b.z, var10);
         var0.vertex(var9.c.x, var9.c.y, var9.c.z, var11);
         var0.vertex(var9.d.x, var9.d.y, var9.d.z, var11);
      }

      kx.U("Halloween shockwave vertices", var3.vertices());
   }

   private ov() {
   }

   public record q(Vec3 origin, double yaw, ov.x pumpkin, List<ov.y> wave) {
      public static final ov.q EMPTY = new ov.q(Vec3.ZERO, 0.0, null, List.of());

      public int vertices() {
         return this.wave.size() * 4;
      }
   }

   public record x(double bottom, double alpha) {
   }

   public record y(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int inner, int outer) {
   }
}
