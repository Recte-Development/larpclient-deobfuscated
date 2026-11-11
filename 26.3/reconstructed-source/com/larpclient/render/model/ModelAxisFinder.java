package com.larpclient.render.model;

import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class ModelAxisFinder {
   private static Vec3 transformPoint(Matrix4fc var0, Vec3 var1, int var2) {
      Vector3f var3 = var0.transformPosition(new Vector3f((float)(var2 * var1.x), (float)var1.y, (float)var1.z));
      return new Vec3(var3.x, var3.y, var3.z);
   }

   public static ModelAxisFinder.Axis fromVertices(float[] var0) {
      double var1 = Double.POSITIVE_INFINITY;
      double var3 = Double.NEGATIVE_INFINITY;

      for (byte var5 = 0; var5 < var0.length; var5 += 8) {
         var1 = Math.min(var1, var0[var5 + 1]);
         var3 = Math.max(var3, var0[var5 + 1]);
      }

      if (Double.isFinite(var1 + var3) && !(var3 - var1 < 0.02)) {
         Vec3 var7 = averageBand(var0, var1, var1 + (var3 - var1) * 0.055);
         Vec3 var6 = averageBand(var0, var3 - (var3 - var1) * 0.025, var3);
         return new ModelAxisFinder.Axis(var7, var6);
      } else {
         return null;
      }
   }

   private static Vec3 averageBand(float[] var0, double var1, double var3) {
      double var5 = 0.0;
      double var7 = 0.0;
      double var9 = 0.0;
      int var11 = 0;

      for (byte var12 = 0; var12 < var0.length; var12 += 8) {
         if (var0[var12 + 1] >= var1 && var0[var12 + 1] <= var3) {
            var5 += var0[var12];
            var7 += var0[var12 + 1];
            var9 += var0[var12 + 2];
            var11++;
         }
      }

      return new Vec3(var5 / var11, var7 / var11, var9 / var11);
   }

   public static ModelAxisFinder.Axis fitPrincipalAxis(List<Vec3> var0) {
      if (var0.size() < 4) {
         return null;
      }

      Vec3 var1 = Vec3.ZERO;

      for (Vec3 var3 : var0) {
         var1 = var1.add(var3);
      }

      var1 = var1.scale(1.0 / var0.size());
      double var24 = 0.0;
      double var4 = 0.0;
      double var6 = 0.0;
      double var8 = 0.0;
      double var10 = 0.0;
      double var12 = 0.0;

      for (Vec3 var15 : var0) {
         Vec3 var16 = var15.subtract(var1);
         var24 += var16.x * var16.x;
         var4 += var16.x * var16.y;
         var6 += var16.x * var16.z;
         var8 += var16.y * var16.y;
         var10 += var16.y * var16.z;
         var12 += var16.z * var16.z;
      }

      Vec3 var25 = new Vec3(0.6, 1.0, 0.05).normalize();

      for (int var26 = 0; var26 < 12; var26++) {
         Vec3 var28 = new Vec3(
            var24 * var25.x + var4 * var25.y + var6 * var25.z,
            var4 * var25.x + var8 * var25.y + var10 * var25.z,
            var6 * var25.x + var10 * var25.y + var12 * var25.z
         );
         if (var28.lengthSqr() < 1.0E-12) {
            return null;
         }

         var25 = var28.normalize();
      }

      if (var25.y < 0.0) {
         var25 = var25.scale(-1.0);
      }

      double var27 = Double.POSITIVE_INFINITY;
      double var17 = Double.NEGATIVE_INFINITY;

      for (Vec3 var20 : var0) {
         double var21 = var20.subtract(var1).dot(var25);
         var27 = Math.min(var27, var21);
         var17 = Math.max(var17, var21);
      }

      if (var17 - var27 < 0.1) {
         return null;
      }

      Vec3 var29 = Vec3.ZERO;
      int var31 = 0;

      for (Vec3 var22 : var0) {
         if (var22.subtract(var1).dot(var25) >= var17 - (var17 - var27) * 0.025) {
            var29 = var29.add(var22);
            var31++;
         }
      }

      var29 = var29.scale(1.0 / var31);
      Vec3 var33 = var1.add(var25.scale(var27 + (var17 - var27) * 0.48));
      return new ModelAxisFinder.Axis(var33, var29);
   }

   private ModelAxisFinder() {
   }

   public record Axis(Vec3 root, Vec3 tip) {
      public ModelAxisFinder.Axis transform(Matrix4fc var1, int var2) {
         return new ModelAxisFinder.Axis(ModelAxisFinder.transformPoint(var1, this.root, var2), ModelAxisFinder.transformPoint(var1, this.tip, var2));
      }

      public double length() {
         return this.root.distanceTo(this.tip);
      }
   }
}
