package com.larpclient.render.model;

import com.larpclient.render.particle.KillEffect;
import com.larpclient.util.math.HexagonGeometry;
import com.larpclient.util.world.SurfaceEdgeFinder;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ModelAssemblyAnimation {
   public static final double rISE_HEIGHT = 1.7;
   public static final double aSSEMBLY_START = 0.85;
   public static final double hOVER_START = 1.85;
   public static final double pART_SCALE = 1.02;
   public static final ModelAssemblyAnimation.Basis dEFAULT_BASIS = new ModelAssemblyAnimation.Basis(
      new Vec3(1.0, 0.0, 0.0), new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, 0.0, 1.0)
   );

   public static double assemblyProgress(double var0) {
      return KillEffect.ease((var0 - 0.85) / 1.0);
   }

   public static Vec3 getHoverPoint(KillEffect var0, double var1) {
      double var3 = Math.sin(Math.max(0.0, var1 - 1.85) * 2.4) * 0.045;
      return var0.origin.add(0.0, 2.75 + var3, 0.0);
   }

   public static ModelAssemblyAnimation.Basis buildBasis(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
      Vec3 var4 = var1.subtract(var0);
      if (var4.lengthSqr() < 1.0E-10) {
         var4 = var2.cross(var3);
      }

      if (var4.lengthSqr() < 1.0E-10) {
         var4 = dEFAULT_BASIS.normal;
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
      return new ModelAssemblyAnimation.Basis(var5, var4.cross(var5).normalize(), var4);
   }

   public static ModelAssemblyAnimation.Segment getTargetSegment(KillEffect var0, double var1, ModelAssemblyAnimation.Basis var3, int var4) {
      SurfaceEdgeFinder.Edge var5 = HexagonGeometry.hEX_FACES.get(var4);
      Vec3 var6 = getHoverPoint(var0, var1).add(var3.normal.scale(var4 < 3 ? 0.035 : -0.035));
      return new ModelAssemblyAnimation.Segment(toWorldPoint(var6, var3, var5.from()), toWorldPoint(var6, var3, var5.end()));
   }

   private static Vec3 toWorldPoint(Vec3 var0, ModelAssemblyAnimation.Basis var1, Vec3 var2) {
      return var0.add(var1.right.scale(var2.x * 1.02)).add(var1.upAxis.scale(-var2.z * 1.02));
   }

   private static Quaternionf toQuaternion(ModelAssemblyAnimation.Basis var0) {
      Vec3 var1 = var0.right.scale(-1.0);
      Vec3 var2 = var0.upAxis;
      Vec3 var3 = var0.normal.scale(-1.0);
      return new Quaternionf()
         .setFromNormalized(
            new Matrix3f()
               .setColumn(0, new Vector3f((float)var1.x, (float)var1.y, (float)var1.z))
               .setColumn(1, new Vector3f((float)var2.x, (float)var2.y, (float)var2.z))
               .setColumn(2, new Vector3f((float)var3.x, (float)var3.y, (float)var3.z))
         );
   }

   public static KillEffect.PartState computePartPose(KillEffect var0, int var1, double var2, ModelAssemblyAnimation.Basis var4) {
      double var5 = assemblyProgress(var2);
      double var7 = 1.7 * KillEffect.ease(var2 / 0.85);
      double var9 = KillEffect.getTrail(var1, var0.slimArms) / 16.0 * 0.9375;
      double var11 = (24.0 - KillEffect.pART_OFFSETS[var1][1]) / 16.0 * 0.9375;
      Vec3 var13 = var0.origin.add(0.0, 1.05 + var7, 0.0).add(var4.right.scale(var9)).add(var4.upAxis.scale(var11 - 1.05));
      ModelAssemblyAnimation.Segment var14 = getTargetSegment(var0, var2, var4, var1);
      double var15 = var1 < 2 ? 0.5 : (!var0.slimArms || var1 != 2 && var1 != 3 ? 0.25 : 0.1875);
      double var17 = var1 == 0 ? 0.5 : 0.75;
      double var19 = var1 == 0 ? 0.5 : 0.25;
      double var21 = var1 == 1 ? 0.22 : 0.18;
      Vec3 var23 = new Vec3(var21 / (var15 * 0.9375), var14.length() / (var17 * 0.9375), 0.14 / (var19 * 0.9375));
      SurfaceEdgeFinder.Edge var24 = HexagonGeometry.hEX_FACES.get(var1);
      Vec3 var25 = var24.end().subtract(var24.from()).normalize();
      double var26 = Math.atan2(-var25.x, var25.z);
      Quaternionf var28 = toQuaternion(var4).rotateZ((float)(var26 * var5));
      Vec3 var29 = var13.lerp(var14.center(), var5).add(var4.normal.scale(Math.sin(var5 * Math.PI) * 0.18 * (var1 < 3 ? 1 : -1)));
      double var30 = KillEffect.ease((4.6 - var2) / 0.75);
      return new KillEffect.PartState(var29, 0.0, 0.0, 0.0, 1.0, var30, 0.0, false, var28, new Vec3(1.0, 1.0, 1.0).lerp(var23, var5));
   }

   private ModelAssemblyAnimation() {
   }

   public record Basis(Vec3 right, Vec3 up, Vec3 normal) {
      private final Vec3 upAxis;

      public Basis(Vec3 right, Vec3 up, Vec3 normal) {
         this.right = right;
         this.upAxis = up;
         this.normal = normal;
      }
   }

   public record Segment(Vec3 from, Vec3 to) {
      private final Vec3 end;

      public Segment(Vec3 from, Vec3 to) {
         this.from = from;
         this.end = to;
      }

      public Vec3 center() {
         return this.from.lerp(this.end, 0.5);
      }

      public Vec3 tangent() {
         return this.end.subtract(this.from).normalize();
      }

      public double length() {
         return this.from.distanceTo(this.end);
      }
   }
}
