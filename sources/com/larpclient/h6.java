package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class h6 {
   public static final double a = 1.5;
   public static final double i = 0.03;
   public static final double W = 0.99;
   public static final double s = 0.8;
   public static final double j = 0.0172275;
   public static final double V = 0.1F;
   public static final int X = 60;
   static final int o = 8;
   static final double d = 0.15625;
   static final double v = 0.15F;
   static final double H = 0.3125;
   static final double C = 0.3F;
   static final double e = 1.8F;
   private static final double E = 0.0103365;
   private static final Vec3[] U = new Vec3[]{
      Vec3.ZERO,
      new Vec3(0.0103365, 0.0, 0.0),
      new Vec3(-0.0103365, 0.0, 0.0),
      new Vec3(0.0, 0.0103365, 0.0),
      new Vec3(0.0, -0.0103365, 0.0),
      new Vec3(0.0, 0.0, 0.0103365),
      new Vec3(0.0, 0.0, -0.0103365)
   };
   public static final h6.z R = new h6.j();

   public static h6.q k(h6.g var0, h6.z var1) {
      h6.q var2 = new h6.q();
      Vec3 var3 = var0.position();
      Vec3 var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new Vec3(var4.x, var4.y - 0.03, var4.z).scale(var6);
         Vec3 var8 = var3.add(var4);
         double var9 = var1.clip(var3, var8);
         var2.m[var5] = var3;
         var2.P[var5] = var9 < 1.0 ? var3.add(var4.scale(var9)) : var8;
         var2.i[var5] = V(var0.age() + var5 + 1);
         var2.E = var5 + 1;
         if (var9 < 1.0) {
            var2.Q = var2.P[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<h6.q> q(h6.g var0, h6.z var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(k(var0, var1));
      var2.add(k(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(k(var0.back(), var1));
      }

      return var2;
   }

   public static List<h6.q> A(Vec3 var0, Vec3 var1, Vec3 var2, int var3, h6.z var4) {
      List var5 = q(h6.g.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < U.length; var6++) {
         var5.add(k(h6.g.thrown(var0, var1.add(U[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<h6.q> j(Vec3 var0, Vec3 var1, Vec3 var2, h6.z var3) {
      ArrayList var4 = new ArrayList(U.length);

      for (Vec3 var8 : U) {
         var4.add(k(h6.g.thrown(var0, var1.add(var8), var2), var3));
      }

      return var4;
   }

   public static h6.v C(Vec3 var0, float var1, float var2, Vec3 var3, h6.z var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - var8);
         Vec3 var10 = p(var1, var9);
         if (Z(k(h6.g.thrown(var0, var10, var3), var4), var0, var10.scale(1.5).add(var3), var4) != null) {
            h6.n var11 = v(j(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new h6.v(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double V(int var0) {
      return Math.max(0.0F, Math.min(0.3F, (var0 - 2) / 20.0F));
   }

   static AABB C(Vec3 var0) {
      return new AABB(var0.x - 0.15625, var0.y - 0.15F, var0.z - 0.15625, var0.x + 0.15625, var0.y - 0.15F + 0.3125, var0.z + 0.15625);
   }

   static AABB V(Vec3 var0) {
      return new AABB(var0.x - 0.3F, var0.y, var0.z - 0.3F, var0.x + 0.3F, var0.y + 1.8F, var0.z + 0.3F);
   }

   public static Vec3 p(float var0, float var1) {
      float var2 = (float) (Math.PI / 180.0);
      return new Vec3(-Mth.sin(var0 * var2) * Mth.cos(var1 * var2), -Mth.sin(var1 * var2), Mth.cos(var0 * var2) * Mth.cos(var1 * var2)).normalize();
   }

   public static h6.b Z(h6.q var0, Vec3 var1, Vec3 var2, h6.z var3) {
      for (int var4 = 0; var4 < var0.E; var4++) {
         Vec3 var5 = var1.add(var2.scale(var4));
         if (!C(var5).inflate(var0.i[var4]).clip(var0.m[var4], var0.P[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            Vec3 var6 = var0.m[var4];
            boolean var7 = V(var6).inflate(V(var4 + 1)).clip(var5, var5.add(var2)).isPresent();
            return new h6.b(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static h6.n v(List<h6.q> var0, Vec3 var1, Vec3 var2, Vec3 var3, h6.z var4) {
      Vec3 var5 = var3.normalize();
      h6.b var6 = Z((h6.q)var0.get(0), var1, var5.scale(1.5).add(var2), var4);
      return var6 == null ? null : g(var0, var1, var2, var5, var6);
   }

   private static h6.n g(List<h6.q> var0, Vec3 var1, Vec3 var2, Vec3 var3, h6.b var4) {
      int var5 = 0;
      int var6 = 0;

      for (h6.q var8 : var0) {
         for (Vec3 var12 : U) {
            var6++;
            if (Z(var8, var1, var3.add(var12).scale(1.5).add(var2), R) != null) {
               var5++;
            }
         }
      }

      return new h6.n(var3, var4, var5, var6);
   }

   public static h6.n g(List<h6.q> var0, Vec3 var1, Vec3 var2, h6.z var3) {
      h6.q var4 = (h6.q)var0.get(0);
      Vec3 var5 = var1.add(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.E; var7++) {
         Vec3 var8 = var5.add(var2.scale(var7));
         Vec3 var9 = var4.m[var7];
         Vec3 var10 = var4.P[var7].subtract(var9);
         double var11 = var10.lengthSqr();
         if (!(var11 < 1.0E-9)) {
            Vec3 var13 = var9.subtract(var8);
            double var14 = 1.5 * var7;
            double var16 = 2.0 * var10.dot(var13);
            double var18 = var13.lengthSqr() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.i[var7]) / Math.sqrt(var11);
               double var24 = Math.sqrt(var20);

               for (double var29 : new double[]{(-var16 - var24) / (2.0 * var11), (-var16 + var24) / (2.0 * var11)}) {
                  if (var29 >= -var22 && var29 <= 1.0 + var22) {
                     var6.add(var9.add(var10.scale(var29)).subtract(var8));
                  }
               }
            }
         }
      }

      ArrayList var31 = new ArrayList();

      for (Vec3 var34 : var6) {
         if (!(var34.lengthSqr() < 1.0E-12)) {
            Vec3 var36 = var34.normalize();
            h6.b var38 = Z(var4, var1, var36.scale(1.5).add(var2), var3);
            if (var38 != null) {
               var31.add(new h6.n(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      h6.n var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         h6.n var37 = g(var0, var1, var2, ((h6.n)var31.get(var35)).direction(), ((h6.n)var31.get(var35)).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private h6() {
   }

   public record b(int tick, Vec3 point, boolean burst) {
   }

   public record g(Vec3 position, Vec3 velocity, int age) {
      public static h6.g thrown(Vec3 var0, Vec3 var1, Vec3 var2) {
         return new h6.g(var0.subtract(0.0, 0.1F, 0.0), var1.scale(1.5).add(var2), 0);
      }

      public h6.g step(h6.z var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         Vec3 var4 = new Vec3(this.velocity.x, this.velocity.y - 0.03, this.velocity.z).scale(var2);
         return new h6.g(this.position.add(var4), var4, this.age + 1);
      }

      public h6.g back() {
         return new h6.g(this.position.subtract(this.velocity), this.velocity.scale(1.0101010101010102).add(0.0, 0.03, 0.0), this.age - 1);
      }

      public h6.g after(int var1, h6.z var2) {
         h6.g var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }

   private static final class j implements h6.z {
      @Override
      public double clip(Vec3 var1, Vec3 var2) {
         return 1.0;
      }

      @Override
      public boolean water(Vec3 var1) {
         return false;
      }
   }

   public record n(Vec3 direction, h6.b nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / this.samples;
      }
   }

   public static final class q {
      final Vec3[] m = new Vec3[60];
      final Vec3[] P = new Vec3[60];
      final double[] i = new double[60];
      int E;
      Vec3 Q;

      public int u() {
         return this.E;
      }

      public Vec3 K() {
         return this.Q;
      }
   }

   public record v(float pitch, h6.n plan) {
   }

   public interface z {
      double clip(Vec3 var1, Vec3 var2);

      boolean water(Vec3 var1);
   }
}
