package com.larpclient.render.entity;

import com.larpclient.render.model.SahurMesh;
import com.larpclient.render.model.SahurMeshSplitter;
import com.larpclient.render.particle.KillEffect;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ItemSwingAnimator {
   public static final double tARGET_HEIGHT = 3.6;
   public static final double sTRIKE_TIME = 1.56;
   public static final double wINDUP_END = 1.28;
   public static final double rECOIL_START = 1.12;
   public static final Vec3 sHOULDER_PIVOT = new Vec3(-0.031, 0.08, -0.067);
   public static final Vec3 tIP_LOCAL = new Vec3(-0.02649, -0.064244, -0.046604);
   public static final Vec3 gRIP_LOCAL = new Vec3(-0.088521, -0.283297, 0.098837);
   private static final double aRM_PIVOT_Y = -0.105;
   private static final double gROUND_CLEARANCE = 1.65;
   private final SahurMesh.Mesh mesh;
   private final float[] posedVertices;
   private final float[] armWeights;
   private final float[] bodyWeights;
   private final double scale;
   private final double strikePitch;
   private final Vec3 gripOffset;
   private final Matrix4f armMatrix = new Matrix4f();
   private final Matrix4f bodyMatrix = new Matrix4f();
   private final Vector3f scratchVector = new Vector3f();

   public ItemSwingAnimator(SahurMesh.Mesh var1) {
      SahurMeshSplitter.BodySplit var2 = SahurMeshSplitter.splitMesh(var1);
      this.mesh = var2.mesh();
      this.posedVertices = (float[])this.mesh.vertices().clone();
      this.armWeights = new float[this.mesh.colors().length];
      this.bodyWeights = new float[this.armWeights.length];
      this.scale = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.armWeights.length; var3++) {
         double var4 = this.posedVertices[var3 * 5 + 1];
         this.armWeights[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.bodyWeights[var3] = (float)Math.max(this.armWeights[var3], KillEffect.ease((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.tipHeight(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.strikePitch = (var10 + var5) * 0.5;
      Vec3 var11 = this.transformPoint(gRIP_LOCAL, 1.56);
      this.gripOffset = new Vec3(-var11.x * this.scale, 0.0, -var11.z * this.scale);
   }

   private double tipHeight(double var1) {
      this.applyPose(var1, bodyTilt(1.56), 0.0);
      this.scratchVector.set((float)gRIP_LOCAL.x, (float)gRIP_LOCAL.y, (float)gRIP_LOCAL.z);
      this.armMatrix.transformPosition(this.scratchVector);
      this.bodyMatrix.transformPosition(this.scratchVector);
      return (this.scratchVector.y - this.mesh.minimumY()) * this.scale;
   }

   private void applyPose(double var1, double var3, double var5) {
      this.armMatrix
         .identity()
         .translate((float)sHOULDER_PIVOT.x, (float)sHOULDER_PIVOT.y, (float)sHOULDER_PIVOT.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-sHOULDER_PIVOT.x), (float)(-sHOULDER_PIVOT.y), (float)(-sHOULDER_PIVOT.z));
      this.bodyMatrix.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double armPitch(double var1) {
      if (var1 < 1.28) {
         return -2.65 * KillEffect.ease((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.strikePitch + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.strikePitch + 0.13 * KillEffect.ease((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12
            ? this.strikePitch + 0.13 - 0.27 * KillEffect.ease((var1 - 1.56 - 0.12) / 0.44000000000000006)
            : (this.strikePitch - 0.14) * (1.0 - KillEffect.ease((var1 - 2.35) / 0.8));
      }
   }

   private static double armYaw(double var0) {
      return 0.7 * Math.sin(Math.PI * KillEffect.ease((var0 - 0.36) / 0.7600000000000001));
   }

   public static double bodyTilt(double var0) {
      double var2 = -0.055 * KillEffect.ease((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * KillEffect.ease((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * KillEffect.ease((var0 - 1.56) / 0.56) : 0.06 * (1.0 - KillEffect.ease((var0 - 2.35) / 0.8));
      }
   }

   private Vec3 transformPoint(Vec3 var1, double var2) {
      this.applyPose(this.armPitch(var2), bodyTilt(var2), armYaw(var2));
      this.scratchVector.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.armMatrix.transformPosition(this.scratchVector);
      this.bodyMatrix.transformPosition(this.scratchVector);
      return new Vec3(this.scratchVector.x, this.scratchVector.y, this.scratchVector.z);
   }

   private Vec3 transformPoint(KillEffect var1, Vec3 var2) {
      Vec3 var3 = var2.add(0.0, -this.mesh.minimumY(), 0.0).scale(this.scale).add(this.gripOffset);
      double var4 = Math.cos(var1.yaw);
      double var6 = Math.sin(var1.yaw);
      return var1.origin.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3 bodyTilt(KillEffect var1, double var2) {
      return this.transformPoint(var1, this.transformPoint(gRIP_LOCAL, var2));
   }

   public Vec3 tipPosition(KillEffect var1, double var2) {
      return this.transformPoint(var1, this.transformPoint(tIP_LOCAL, var2));
   }

   public ItemSwingAnimator.SwingFrame buildFrame(KillEffect var1, double var2, Vec3 var4) {
      this.applyPose(this.armPitch(var2), bodyTilt(var2), armYaw(var2));
      float[] var5 = this.mesh.vertices();

      for (int var6 = 0; var6 < this.armWeights.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.scratchVector.set(var8, var9, var10);
         this.armMatrix.transformPosition(this.scratchVector);
         float var11 = this.armWeights[var6];
         float var12 = this.bodyWeights[var6];
         float var13 = var8 + (this.scratchVector.x - var8) * var11;
         float var14 = var9 + (this.scratchVector.y - var9) * var11;
         float var15 = var10 + (this.scratchVector.z - var10) * var11;
         this.scratchVector.set(var13, var14, var15);
         this.bodyMatrix.transformPosition(this.scratchVector);
         this.posedVertices[var7] = var13 + (this.scratchVector.x - var13) * var12;
         this.posedVertices[var7 + 1] = var14 + (this.scratchVector.y - var14) * var12;
         this.posedVertices[var7 + 2] = var15 + (this.scratchVector.z - var15) * var12;
      }

      double var16 = Math.cos(var1.yaw);
      double var17 = Math.sin(var1.yaw);
      Vec3 var18 = var1.origin.add(this.gripOffset.x * var16 - this.gripOffset.z * var17, 0.0, this.gripOffset.x * var17 + this.gripOffset.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.yaw))
         .scale((float)this.scale)
         .translate(0.0F, -this.mesh.minimumY(), 0.0F);
      Vec3[] var20 = new Vec3[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.bodyTilt(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = KillEffect.ease(var2 / 0.42) * KillEffect.ease((4.6 - var2) / 1.15);
      return new ItemSwingAnimator.SwingFrame(
         this.posedVertices, this.mesh.colors(), var19, var22, var18, this.tipPosition(var1, var2), this.bodyTilt(var1, var2), var20
      );
   }

   public static KillEffect.PartState heldItemPose(KillEffect var0, int var1, double var2) {
      double var4 = KillEffect.ease((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = KillEffect.getTrail(var1, var0.slimArms) / 16.0 * 0.9375;
      double var10 = (24.0 - KillEffect.pART_OFFSETS[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.yaw);
      double var16 = Math.sin(var0.yaw);
      Vec3 var18 = var0.origin.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = KillEffect.ease((3.65 - var2) / 0.9);
      double var21 = KillEffect.ease((var2 - 2.6) / 1.05);
      return new KillEffect.PartState(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record SwingFrame(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 grip, Vec3 tip, Vec3[] trail) {
   }
}
