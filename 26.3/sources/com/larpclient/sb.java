package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class sb {
   public static final double q = 0.995;
   public static final float d = 0.118F;
   public static final float Z = 0.19666666F;
   private static final int[] N = new int[]{0, 1, 3, 2};
   private static final float m = 0.32F;
   private static final List<sb.r> O = v();

   private static List<sb.r> v() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < N.length; var1++) {
         float var2 = (float)Math.toRadians(90 * var1);
         var0.add(new sb.r(N[var1], 0.32F * (float)Math.sin(var2), 0.0F, -0.32F * (float)Math.cos(var2), var2));
      }

      return List.copyOf(var0);
   }

   public static List<sb.r> E(sb.q var0) {
      return var0 == sb.q.t ? O : List.of();
   }

   public static Matrix4f v(Matrix4f var0, sb.r var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.x() * var5, -0.5F - var1.rise() * var5, var1.z() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.19666666F * var5);
   }

   private sb() {
   }

   public static double t(sb.q var0, double var1) {
      return (var0 == sb.q.t ? bw.h().rise() : hm.N) * var1;
   }

   public static void l(ia var0, Matrix4f var1, sb.q var2, double var3, double var5, boolean var7, double var8, double var10) {
      if (var2 == sb.q.t) {
         bw.S(var0, var1, var3, var5, var7, var8);
      } else {
         hm.H(var0, var1, var3, var5, var10);
      }
   }

   public enum q {
      t("Crown"),
      f("Halo");

      public final String Z;

      q(String var3) {
         this.Z = var3;
      }
   }

   public record r(int coin, float x, float rise, float z, float angle) {
   }
}
