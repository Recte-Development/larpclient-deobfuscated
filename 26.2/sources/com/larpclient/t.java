package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class t {
   public static final double w = 1.5;
   public static final double A = 0.03;
   public static final double R = 0.99;
   public static final double J = 0.8;
   public static final double U = 0.0172275;
   public static final double m = 0.1F;
   public static final int C = 60;
   static final int E = 8;
   static final double M = 0.15625;
   static final double G = 0.15F;
   static final double h = 0.3125;
   static final double v = 0.3F;
   static final double D = 1.8F;
   private static final double t = 0.0103365;
   private static final Vec3[] g = new Vec3[]{
      Vec3.ZERO,
      new Vec3(0.0103365, 0.0, 0.0),
      new Vec3(-0.0103365, 0.0, 0.0),
      new Vec3(0.0, 0.0103365, 0.0),
      new Vec3(0.0, -0.0103365, 0.0),
      new Vec3(0.0, 0.0, 0.0103365),
      new Vec3(0.0, 0.0, -0.0103365)
   };
   public static final t.n y = new t.x();

   public static t.b m(t.i var0, t.n var1) {
      t.b var2 = new t.b();
      Vec3 var3 = var0.position();
      Vec3 var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new Vec3(var4.x, var4.y - 0.03, var4.z).scale(var6);
         Vec3 var8 = var3.add(var4);
         double var9 = var1.clip(var3, var8);
         var2.T[var5] = var3;
         var2.L[var5] = var9 < 1.0 ? var3.add(var4.scale(var9)) : var8;
         var2.p[var5] = m(var0.age() + var5 + 1);
         var2.A = var5 + 1;
         if (var9 < 1.0) {
            var2.g = var2.L[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<t.b> t(t.i var0, t.n var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(m(var0, var1));
      var2.add(m(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(m(var0.back(), var1));
      }

      return var2;
   }

   public static List<t.b> G(Vec3 var0, Vec3 var1, Vec3 var2, int var3, t.n var4) {
      List var5 = t(t.i.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < g.length; var6++) {
         var5.add(m(t.i.thrown(var0, var1.add(g[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<t.b> r(Vec3 var0, Vec3 var1, Vec3 var2, t.n var3) {
      ArrayList var4 = new ArrayList(g.length);

      for (Vec3 var8 : g) {
         var4.add(m(t.i.thrown(var0, var1.add(var8), var2), var3));
      }

      return var4;
   }

   public static t.v M(Vec3 var0, float var1, float var2, Vec3 var3, t.n var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - var8);
         Vec3 var10 = g(var1, var9);
         if (l(m(t.i.thrown(var0, var10, var3), var4), var0, var10.scale(1.5).add(var3), var4) != null) {
            t.m var11 = o(r(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new t.v(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double m(int var0) {
      return Math.max(0.0F, Math.min(0.3F, (var0 - 2) / 20.0F));
   }

   static AABB I(Vec3 var0) {
      return new AABB(var0.x - 0.15625, var0.y - 0.15F, var0.z - 0.15625, var0.x + 0.15625, var0.y - 0.15F + 0.3125, var0.z + 0.15625);
   }

   static AABB B(Vec3 var0) {
      return new AABB(var0.x - 0.3F, var0.y, var0.z - 0.3F, var0.x + 0.3F, var0.y + 1.8F, var0.z + 0.3F);
   }

   public static Vec3 g(float var0, float var1) {
      float var2 = (float) (Math.PI / 180.0);
      return new Vec3(-Mth.sin(var0 * var2) * Mth.cos(var1 * var2), -Mth.sin(var1 * var2), Mth.cos(var0 * var2) * Mth.cos(var1 * var2)).normalize();
   }

   public static t.p l(t.b var0, Vec3 var1, Vec3 var2, t.n var3) {
      for (int var4 = 0; var4 < var0.A; var4++) {
         Vec3 var5 = var1.add(var2.scale(var4));
         if (!I(var5).inflate(var0.p[var4]).clip(var0.T[var4], var0.L[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            Vec3 var6 = var0.T[var4];
            boolean var7 = B(var6).inflate(m(var4 + 1)).clip(var5, var5.add(var2)).isPresent();
            return new t.p(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static t.m o(List<t.b> var0, Vec3 var1, Vec3 var2, Vec3 var3, t.n var4) {
      Vec3 var5 = var3.normalize();
      t.p var6 = l((t.b)var0.get(0), var1, var5.scale(1.5).add(var2), var4);
      return var6 == null ? null : G(var0, var1, var2, var5, var6);
   }

   private static t.m G(List<t.b> var0, Vec3 var1, Vec3 var2, Vec3 var3, t.p var4) {
      int var5 = 0;
      int var6 = 0;

      for (t.b var8 : var0) {
         for (Vec3 var12 : g) {
            var6++;
            if (l(var8, var1, var3.add(var12).scale(1.5).add(var2), y) != null) {
               var5++;
            }
         }
      }

      return new t.m(var3, var4, var5, var6);
   }

   public static t.m I(List<t.b> var0, Vec3 var1, Vec3 var2, t.n var3) {
      t.b var4 = (t.b)var0.get(0);
      Vec3 var5 = var1.add(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.A; var7++) {
         Vec3 var8 = var5.add(var2.scale(var7));
         Vec3 var9 = var4.T[var7];
         Vec3 var10 = var4.L[var7].subtract(var9);
         double var11 = var10.lengthSqr();
         if (!(var11 < 1.0E-9)) {
            Vec3 var13 = var9.subtract(var8);
            double var14 = 1.5 * var7;
            double var16 = 2.0 * var10.dot(var13);
            double var18 = var13.lengthSqr() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.p[var7]) / Math.sqrt(var11);
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
            t.p var38 = l(var4, var1, var36.scale(1.5).add(var2), var3);
            if (var38 != null) {
               var31.add(new t.m(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      t.m var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         t.m var37 = G(var0, var1, var2, ((t.m)var31.get(var35)).direction(), ((t.m)var31.get(var35)).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private t() {
   }

   public static final class b {
      final Vec3[] T = new Vec3[60];
      final Vec3[] L = new Vec3[60];
      final double[] p = new double[60];
      int A;
      Vec3 g;

      public int y() {
         return this.A;
      }

      public Vec3 X() {
         return this.g;
      }
   }

   public record i(Vec3 position, Vec3 velocity, int age) {
      public static t.i thrown(Vec3 var0, Vec3 var1, Vec3 var2) {
         return new t.i(var0.subtract(0.0, 0.1F, 0.0), var1.scale(1.5).add(var2), 0);
      }

      public t.i step(t.n var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         Vec3 var4 = new Vec3(this.velocity.x, this.velocity.y - 0.03, this.velocity.z).scale(var2);
         return new t.i(this.position.add(var4), var4, this.age + 1);
      }

      public t.i back() {
         return new t.i(this.position.subtract(this.velocity), this.velocity.scale(1.0101010101010102).add(0.0, 0.03, 0.0), this.age - 1);
      }

      public t.i after(int var1, t.n var2) {
         t.i var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }

   public record m(Vec3 direction, t.p nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / this.samples;
      }
   }

   public interface n {
      double clip(Vec3 var1, Vec3 var2);

      boolean water(Vec3 var1);
   }

   public record p(int tick, Vec3 point, boolean burst) {
   }

   public record v(float pitch, t.m plan) {
   }

   private static final class x implements t.n {
      @Override
      public double clip(Vec3 var1, Vec3 var2) {
         return 1.0;
      }

      @Override
      public boolean water(Vec3 var1) {
         return false;
      }
   }
}
