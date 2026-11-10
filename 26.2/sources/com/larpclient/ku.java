package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class ku {
   private final kj U;
   private final Vec3 N;
   private final Quaternionf C;
   private final double t;
   private final boolean Z;
   private String f = "Matte";
   private Vec3 J = new Vec3(0.0, 0.3, 1.0).normalize();

   ku t(String var1, Vec3 var2) {
      this.f = var1;
      this.J = var2;
      return this;
   }

   ku(kj var1, Vec3 var2, Quaternionf var3, double var4, boolean var6) {
      this.U = var1;
      this.N = var2;
      this.C = var3;
      this.t = var4;
      this.Z = var6;
   }

   Vec3 H(double var1, double var3, double var5) {
      Vector3f var7 = this.C.transform(new Vector3f((float)(var1 * this.t), (float)(var3 * this.t), (float)(var5 * this.t)));
      return this.N.add(var7.x, var7.y, var7.z);
   }

   private void R(double var1, double var3, double var5, int var7) {
      Vec3 var8 = this.H(var1, var3, var5);
      this.U.vertex(var8.x, var8.y, var8.z, var7);
   }

   void z(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, int var5) {
      Vec3 var6 = var2.subtract(var1).cross(var3.subtract(var1)).normalize();
      double var7 = this.Z ? 1.0 : 0.68 + 0.32 * Math.max(0.0, var6.dot(new Vec3(-0.35, 0.8, 0.45).normalize()));
      int var9 = uh.q(var5, var5 & 0xFF000000, 1.0 - var7);
      Vector3f var10 = this.C.transform(new Vector3f((float)var6.x, (float)var6.y, (float)var6.z));
      Vec3 var11 = new Vec3(var10.x, var10.y, var10.z);
      double var12 = Math.abs(var11.dot(this.J));
      if (!this.Z) {
         switch (this.f) {
            case "Glass":
               var9 = uh.F(uh.q(var9, -1, 0.18 * Math.pow(1.0 - var12, 3.0)), 0.32 + 0.68 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Crystal":
               var9 = uh.q(var9, uh.H(-1, (var9 >>> 24) / 255.0), 0.35 * Math.pow(1.0 - var12, 4.0));
               break;
            case "Gold":
               var9 = uh.q(var9, uh.H(-4683, (var9 >>> 24) / 255.0), 0.65 * Math.pow(Math.max(0.0, var11.dot(new Vec3(-0.35, 0.8, 0.45).normalize())), 16.0));
               break;
            case "Obsidian":
               var9 = uh.q(var9, uh.H(-7301451, (var9 >>> 24) / 255.0), 0.3 * Math.pow(var12, 28.0));
               break;
            case "Silk":
               var9 = uh.q(var9, uh.H(-5893, (var9 >>> 24) / 255.0), 0.3 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Energy":
               var9 = uh.q(var5, uh.H(-1, (var5 >>> 24) / 255.0), 0.16 + 0.2 * Math.pow(1.0 - var12, 2.0));
         }
      }

      this.R(var1.x, var1.y, var1.z, var9);
      this.R(var2.x, var2.y, var2.z, var9);
      this.R(var3.x, var3.y, var3.z, var9);
      this.R(var4.x, var4.y, var4.z, var9);
   }

   void u(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      this.z(var1, var2, var3, var3, var4);
   }

   void s(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      byte var14 = 10;
      byte var15 = 20;

      for (int var16 = 0; var16 < var14; var16++) {
         for (int var17 = 0; var17 < var15; var17++) {
            double var18 = Math.PI * var16 / var14;
            double var20 = Math.PI * (var16 + 1) / var14;
            double var22 = (Math.PI * 2) * var17 / var15;
            double var24 = (Math.PI * 2) * (var17 + 1) / var15;
            this.z(
               G(var1, var3, var5, var7, var9, var11, var18, var22),
               G(var1, var3, var5, var7, var9, var11, var20, var22),
               G(var1, var3, var5, var7, var9, var11, var20, var24),
               G(var1, var3, var5, var7, var9, var11, var18, var24),
               var13
            );
         }
      }
   }

   private static Vec3 G(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      return new Vec3(var0 + var6 * Math.sin(var12) * Math.cos(var14), var2 + var8 * Math.cos(var12), var4 + var10 * Math.sin(var12) * Math.sin(var14));
   }

   void i(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      Vec3[] var14 = new Vec3[8];

      for (int var15 = 0; var15 < 8; var15++) {
         var14[var15] = new Vec3(var1 + ((var15 & 1) == 0 ? -var7 : var7), var3 + ((var15 & 2) == 0 ? -var9 : var9), var5 + ((var15 & 4) == 0 ? -var11 : var11));
      }

      int[][] var20 = new int[][]{{0, 2, 3, 1}, {4, 5, 7, 6}, {0, 4, 6, 2}, {1, 3, 7, 5}, {0, 1, 5, 4}, {2, 6, 7, 3}};

      for (int[] var19 : var20) {
         this.z(var14[var19[0]], var14[var19[1]], var14[var19[2]], var14[var19[3]], var13);
      }
   }

   void t(double var1, double var3, double var5, double var7, int var9) {
      for (int var10 = 0; var10 < 8; var10++) {
         double var11 = var7 + var10 * Math.PI / 4.0;
         double var13 = var7 + (var10 + 1) * Math.PI / 4.0;
         Vec3 var15 = new Vec3(Math.cos(var11) * var1, 0.0, Math.sin(var11) * var1);
         Vec3 var16 = new Vec3(Math.cos(var13) * var1, 0.0, Math.sin(var13) * var1);
         this.u(new Vec3(0.0, var3, 0.0), var16, var15, var9);
         this.u(new Vec3(0.0, -var5, 0.0), var15, var16, var9);
      }
   }

   void g(double var1, double var3, Quaternionf var5, int var6) {
      for (int var7 = 0; var7 < 48; var7++) {
         double var8 = (Math.PI * 2) * var7 / 48.0;
         double var10 = (Math.PI * 2) * (var7 + 1) / 48.0;
         this.z(z(var1 - var3, var8, var5), z(var1 + var3, var8, var5), z(var1 + var3, var10, var5), z(var1 - var3, var10, var5), var6);
      }
   }

   private static Vec3 z(double var0, double var2, Quaternionf var4) {
      Vector3f var5 = var4.transform(new Vector3f((float)(Math.cos(var2) * var0), 0.0F, (float)(Math.sin(var2) * var0)));
      return new Vec3(var5.x, var5.y, var5.z);
   }

   void k(Vec3 var1, Vec3 var2, double var3, double var5, int var7) {
      Vec3 var8 = var2.subtract(var1).normalize();
      Vec3 var9 = var8.cross(Math.abs(var8.y) > 0.9 ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0)).normalize();
      Vec3 var10 = var8.cross(var9).normalize();

      for (int var11 = 0; var11 < 8; var11++) {
         double var12 = var11 * Math.PI / 4.0;
         double var14 = (var11 + 1) * Math.PI / 4.0;
         Vec3 var16 = var9.scale(Math.cos(var12)).add(var10.scale(Math.sin(var12)));
         Vec3 var17 = var9.scale(Math.cos(var14)).add(var10.scale(Math.sin(var14)));
         this.z(var1.add(var16.scale(var3)), var2.add(var16.scale(var5)), var2.add(var17.scale(var5)), var1.add(var17.scale(var3)), var7);
      }
   }
}
