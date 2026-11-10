package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class q4 {
   public static final double Z = 0.995;
   public static final float B = 0.118F;
   public static final float q = 0.19666666F;
   private static final int[] m = new int[]{0, 1, 3, 2};
   private static final float o = 0.32F;
   private static final List<q4.y> h = i();

   private static List<q4.y> i() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < m.length; var1++) {
         float var2 = (float)Math.toRadians(90 * var1);
         var0.add(new q4.y(m[var1], 0.32F * (float)Math.sin(var2), 0.0F, -0.32F * (float)Math.cos(var2), var2));
      }

      return List.copyOf(var0);
   }

   public static List<q4.y> Z(q4.r var0) {
      return var0 == q4.r.g ? h : List.of();
   }

   public static Matrix4f Y(Matrix4f var0, q4.y var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.x() * var5, -0.5F - var1.rise() * var5, var1.z() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.19666666F * var5);
   }

   private q4() {
   }

   public static double a(q4.r var0, double var1) {
      return (var0 == q4.r.g ? w_.i().rise() : ut.S) * var1;
   }

   public static void L(gq var0, Matrix4f var1, q4.r var2, double var3, double var5, boolean var7, double var8, double var10) {
      if (var2 == q4.r.g) {
         w_.U(var0, var1, var3, var5, var7, var8);
      } else {
         ut.b(var0, var1, var3, var5, var10);
      }
   }

   public enum r {
      g("Crown"),
      s("Halo");

      public final String X;

      r(String var3) {
         this.X = var3;
      }
   }

   public record y(int coin, float x, float rise, float z, float angle) {
   }
}
