package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class u4 {
   public static final double T = 1.7;
   public static final double S = 0.85;
   public static final double W = 1.85;
   public static final double C = 1.02;
   public static final u4.v x = new u4.v(new Vec3(1.0, 0.0, 0.0), new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, 0.0, 1.0));

   public static double f(double var0) {
      return ez.N((var0 - 0.85) / 1.0);
   }

   public static Vec3 u(ez var0, double var1) {
      double var3 = Math.sin(Math.max(0.0, var1 - 1.85) * 2.4) * 0.045;
      return var0.D.add(0.0, 2.75 + var3, 0.0);
   }

   public static u4.v g(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
      Vec3 var4 = var1.subtract(var0);
      if (var4.lengthSqr() < 1.0E-10) {
         var4 = var2.cross(var3);
      }

      if (var4.lengthSqr() < 1.0E-10) {
         var4 = x.normal;
      }

      var4 = var4.normalize();
      Vec3 var5 = var2.subtract(var4.scale(var2.dot(var4)));
      if (var5.lengthSqr() < 1.0E-10) {
         var5 = var3.cross(var4);
      }

      if (var5.lengthSqr() < 1.0E-10) {
         Vec3 var6 = Math.abs(var4.y) < 0.9 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(0.0, 0.0, 1.0);
         var5 = var6.cross(var4);
      }

      var5 = var5.normalize();
      return new u4.v(var5, var4.cross(var5).normalize(), var4);
   }

   public static u4.c U(ez var0, double var1, u4.v var3, int var4) {
      jr.l var5 = x_.a.get(var4);
      Vec3 var6 = u(var0, var1).add(var3.normal.scale(var4 < 3 ? 0.035 : -0.035));
      return new u4.c(G(var6, var3, var5.from()), G(var6, var3, var5.to()));
   }

   private static Vec3 G(Vec3 var0, u4.v var1, Vec3 var2) {
      return var0.add(var1.right.scale(var2.x * 1.02)).add(var1.up.scale(-var2.z * 1.02));
   }

   private static Quaternionf s(u4.v var0) {
      Vec3 var1 = var0.right.scale(-1.0);
      Vec3 var2 = var0.up;
      Vec3 var3 = var0.normal.scale(-1.0);
      return new Quaternionf()
         .setFromNormalized(
            new Matrix3f()
               .setColumn(0, new Vector3f((float)var1.x, (float)var1.y, (float)var1.z))
               .setColumn(1, new Vector3f((float)var2.x, (float)var2.y, (float)var2.z))
               .setColumn(2, new Vector3f((float)var3.x, (float)var3.y, (float)var3.z))
         );
   }

   public static ez.q k(ez var0, int var1, double var2, u4.v var4) {
      double var5 = f(var2);
      double var7 = 1.7 * ez.N(var2 / 0.85);
      double var9 = ez.k(var1, var0.d) / 16.0 * 0.9375;
      double var11 = (24.0 - ez.X[var1][1]) / 16.0 * 0.9375;
      Vec3 var13 = var0.D.add(0.0, 1.05 + var7, 0.0).add(var4.right.scale(var9)).add(var4.up.scale(var11 - 1.05));
      u4.c var14 = U(var0, var2, var4, var1);
      double var15 = var1 < 2 ? 0.5 : (!var0.d || var1 != 2 && var1 != 3 ? 0.25 : 0.1875);
      double var17 = var1 == 0 ? 0.5 : 0.75;
      double var19 = var1 == 0 ? 0.5 : 0.25;
      double var21 = var1 == 1 ? 0.22 : 0.18;
      Vec3 var23 = new Vec3(var21 / (var15 * 0.9375), var14.length() / (var17 * 0.9375), 0.14 / (var19 * 0.9375));
      jr.l var24 = x_.a.get(var1);
      Vec3 var25 = var24.to().subtract(var24.from()).normalize();
      double var26 = Math.atan2(-var25.x, var25.z);
      Quaternionf var28 = s(var4).rotateZ((float)(var26 * var5));
      Vec3 var29 = var13.lerp(var14.center(), var5).add(var4.normal.scale(Math.sin(var5 * Math.PI) * 0.18 * (var1 < 3 ? 1 : -1)));
      double var30 = ez.N((4.6 - var2) / 0.75);
      return new ez.q(var29, 0.0, 0.0, 0.0, 1.0, var30, 0.0, false, var28, new Vec3(1.0, 1.0, 1.0).lerp(var23, var5));
   }

   private u4() {
   }

   public record c(Vec3 from, Vec3 to) {
      public Vec3 center() {
         return this.from.lerp(this.to, 0.5);
      }

      public Vec3 tangent() {
         return this.to.subtract(this.from).normalize();
      }

      public double length() {
         return this.from.distanceTo(this.to);
      }
   }

   public record v(Vec3 right, Vec3 up, Vec3 normal) {
   }
}
