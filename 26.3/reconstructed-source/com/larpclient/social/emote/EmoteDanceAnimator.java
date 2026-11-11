package com.larpclient.social.emote;

import com.larpclient.render.model.SahurMesh;
import com.larpclient.render.model.SahurMeshSplitter;
import com.larpclient.render.particle.KillEffect;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class EmoteDanceAnimator {
   public static final double mODEL_HEIGHT = 3.6;
   public static final double cROUCH_END = 0.78;
   public static final double jUMP_START = 1.62;
   public static final double dANCE_START = 2.16;
   public static final double dANCE_CYCLE = 1.12;
   public static final double dANCE_END = 6.640000000000001;
   public static final double tOTAL_DURATION = 7.5;
   public static final double lEAP_HEIGHT = 0.42;
   public static final double lEAP_DEPTH = 0.84;
   private static final double pIVOT_X = 0.028;
   private static final double pIVOT_Z = -0.062;
   private static final double pIVOT_Y = -0.105;
   private static final Vec3 pivot = new Vec3(0.028, -0.105, -0.062);
   private static final Vec3[] shoulders = new Vec3[]{new Vec3(-0.031, 0.08, -0.067), new Vec3(0.088, 0.08, -0.067)};
   private static final Vec3[] elbows = new Vec3[]{new Vec3(-0.038, 0.002, -0.068), new Vec3(0.096, 0.002, -0.068)};
   private static final Vec3[] hands = new Vec3[]{new Vec3(-0.027, -0.077, -0.05), new Vec3(0.077, -0.077, -0.05)};
   private static final Vec3[] hips = new Vec3[]{new Vec3(0.001, -0.105, -0.062), new Vec3(0.055, -0.105, -0.062)};
   private static final Vec3[] knees = new Vec3[]{new Vec3(-0.001, -0.194, -0.067), new Vec3(0.057, -0.194, -0.067)};
   private static final Vec3[] feet = new Vec3[]{new Vec3(-0.003, -0.28, -0.068), new Vec3(0.057, -0.28, -0.068)};
   private static final double[] stepSide = new double[]{1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0};
   private static final double[] stepLift = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
   private static final Vec3 legBendDirection = new Vec3(0.0, 0.0, 1.0);
   private static final Vec3 armBendDirection = new Vec3(0.0, 0.0, -1.0);
   private final SahurMesh.Mesh mesh;
   private final int[] joints;
   private final float[] vertices;
   private final double scale;
   private final Matrix4f bodyTransform = new Matrix4f();
   private final Matrix4f upperBodyTransform = new Matrix4f();
   private final EmoteDanceAnimator.LimbChain[] armChains = new EmoteDanceAnimator.LimbChain[]{
      new EmoteDanceAnimator.LimbChain(), new EmoteDanceAnimator.LimbChain()
   };
   private final EmoteDanceAnimator.LimbChain[] legChains = new EmoteDanceAnimator.LimbChain[]{
      new EmoteDanceAnimator.LimbChain(), new EmoteDanceAnimator.LimbChain()
   };
   private final Vector3f scratchVertex = new Vector3f();
   private final Vector3f scratchUpperVertex = new Vector3f();

   public EmoteDanceAnimator(SahurMesh.Mesh var1) {
      SahurMeshSplitter.JointSplit var2 = SahurMeshSplitter.splitArticulated(var1);
      this.mesh = var2.mesh();
      this.joints = var2.joints();
      this.vertices = (float[])this.mesh.vertices().clone();
      this.scale = 3.6 / var1.height();
   }

   private static double smoothstep(double var0) {
      return KillEffect.ease(var0);
   }

   private static double lerp(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static EmoteDanceAnimator.Pose poseAt(double var0) {
      double var2 = Math.max(0.0, var0);
      double var4 = Math.clamp((var2 - 0.78) / 0.8400000000000001, 0.0, 1.0);
      double var6 = smoothstep(var4);
      double var8 = 4.0 * var4 * (1.0 - var4);
      double var10 = var2 < 0.78 ? smoothstep((var2 - 0.32) / 0.34) : 0.0;
      double var12 = 1.0 - smoothstep((var2 - 0.78) / 0.14);
      double var14 = smoothstep((var2 - 1.62) / 0.085) * (1.0 - smoothstep((var2 - 1.62 - 0.085) / 0.31));
      double var16 = smoothstep((var2 - 2.16) / 0.22) * (1.0 - smoothstep((var2 - 6.640000000000001) / 0.3));
      double var18 = Math.max(0.0, var2 - 2.16) / 1.12;
      double var20 = (var18 - Math.floor(var18)) * 6.0;
      int var22 = (int)var20;
      double var23 = smoothstep(var20 - var22);
      double var25 = lerp(stepSide[var22], stepSide[var22 + 1], var23);
      double var27 = lerp(stepLift[var22], stepLift[var22 + 1], var23);
      double var29 = -var25 * 0.018 * var16;
      double var31 = 0.008
         + 0.043 * var10
         + 0.043 * (var2 >= 0.78 && var2 < 1.62 ? var12 : 0.0)
         + 0.05 * var14
         + 0.007 * var16 * (0.5 + 0.5 * Math.cos(var18 * Math.PI * 4.0));
      double var33 = 0.18 * var10 + (var2 >= 0.78 && var2 < 1.62 ? 0.18 * var12 - 0.09 * var8 : 0.0) + 0.15 * var14;
      double var35 = var2 >= 0.78 && var2 < 1.62 ? 0.05 * var8 : 0.0;
      Vec3[] var37 = new Vec3[2];

      for (int var38 = 0; var38 < 2; var38++) {
         double var39 = var38 == 0 ? -1.0 : 1.0;
         double var41 = (1.0 + var39 * var25) * 0.5;
         Vec3 var43 = new Vec3(0.028 + var25 * 0.102 + var39 * 0.017, -0.02, lerp(0.025, -0.173, var27 * var41));
         double var44 = var2 >= 0.78 && var2 < 1.62 ? var8 : 0.0;
         double var46 = var2 < 0.78 ? var10 : (var2 < 1.62 ? var12 : 0.0);
         Vec3 var48 = hands[var38].add(var39 * 0.028 * var44, 0.105 * var44, -0.055 * var46 + 0.095 * var44);
         var37[var38] = var48.lerp(var43, var16);
      }

      return new EmoteDanceAnimator.Pose(
         new Vec3(0.0, 0.42 * var6 + 1.75 * var8, lerp(-1.48, 0.84, var6)), var31, var29, var33, -var25 * 0.045 * var16, var35, var37[0], var37[1], var16
      );
   }

   private static Vec3 transformPoint(Matrix4f var0, Vec3 var1) {
      Vector3f var2 = var0.transformPosition(new Vector3f((float)var1.x, (float)var1.y, (float)var1.z));
      return new Vec3(var2.x, var2.y, var2.z);
   }

   public EmoteDanceAnimator.Frame animate(KillEffect var1, double var2, Vec3 var4) {
      EmoteDanceAnimator.Pose var5 = poseAt(var2);
      this.bodyTransform
         .identity()
         .translate((float)(pivot.x + var5.hipShift), (float)(pivot.y - var5.compression), (float)pivot.z)
         .rotateY((float)(var5.roll * 2.2))
         .rotateX((float)var5.lean)
         .rotateZ((float)var5.roll)
         .translate((float)(-pivot.x), (float)(-pivot.y), (float)(-pivot.z));
      this.upperBodyTransform
         .set(this.bodyTransform)
         .translate(0.028F, 0.135F, -0.062F)
         .rotateY((float)(-var5.roll * 1.5))
         .rotateZ((float)(-var5.roll * 0.7))
         .rotateX((float)(-var5.lean * 0.55))
         .translate(-0.028F, -0.135F, 0.062F);

      for (int var6 = 0; var6 < 2; var6++) {
         this.armChains[var6]
            .solve(
               shoulders[var6],
               elbows[var6],
               hands[var6],
               transformPoint(this.bodyTransform, shoulders[var6]),
               var6 == 0 ? var5.rightHand : var5.leftHand,
               armBendDirection
            );
         Vec3 var7 = feet[var6].add(var6 == 0 ? 0.011 : -0.011, var5.tuck, -0.007 + var5.tuck * 0.42);
         this.legChains[var6].solve(hips[var6], knees[var6], feet[var6], transformPoint(this.bodyTransform, hips[var6]), var7, legBendDirection);
      }

      float[] var15 = this.mesh.vertices();

      for (int var16 = 0; var16 < this.joints.length; var16++) {
         int var8 = var16 * 5;
         int var9 = this.joints[var16];
         float var10 = var15[var8];
         float var11 = var15[var8 + 1];
         float var12 = var15[var8 + 2];
         this.scratchVertex.set(var10, var11, var12);
         this.scratchUpperVertex.set(this.scratchVertex);
         if (var9 == 0) {
            this.bodyTransform.transformPosition(this.scratchVertex);
            this.upperBodyTransform.transformPosition(this.scratchUpperVertex);
            this.scratchVertex.lerp(this.scratchUpperVertex, (float)smoothstep((var11 - 0.105) / 0.07));
         } else {
            int var13 = var9 >= 3 ? 1 : 0;
            EmoteDanceAnimator.LimbChain var14 = (var13 != 0 ? this.legChains : this.armChains)[(var9 - 1) % 2];
            var14.rootTransform.transformPosition(this.scratchVertex);
            var14.upperTransform.transformPosition(this.scratchUpperVertex);
            this.scratchVertex.lerp(this.scratchUpperVertex, (float)smoothstep(((var13 != 0 ? -0.194 : 0.002) + 0.013 - var11) / 0.026));
            if (var13 != 0) {
               this.scratchUpperVertex.set(var10, var11, var12);
               var14.endTransform.transformPosition(this.scratchUpperVertex);
               this.scratchVertex.lerp(this.scratchUpperVertex, (float)smoothstep((-0.257 - var11) / 0.018));
            }
         }

         this.vertices[var8] = this.scratchVertex.x;
         this.vertices[var8 + 1] = this.scratchVertex.y;
         this.vertices[var8 + 2] = this.scratchVertex.z;
      }

      double var17 = Math.cos(var1.yaw);
      double var18 = Math.sin(var1.yaw);
      Vec3 var19 = var1.origin.add(var5.root.x * var17 - var5.root.z * var18, var5.root.y, var5.root.x * var18 + var5.root.z * var17);
      Matrix4f var20 = new Matrix4f()
         .translation((float)(var19.x - var4.x), (float)(var19.y - var4.y), (float)(var19.z - var4.z))
         .rotateY((float)(-var1.yaw))
         .scale((float)this.scale)
         .translate(-0.028F, -this.mesh.minimumY(), 0.062F);
      double var21 = smoothstep(var2 / 0.3) * smoothstep((7.5 - var2) / 0.65);
      return new EmoteDanceAnimator.Frame(
         this.vertices,
         this.mesh.colors(),
         var20,
         var21,
         var19,
         toWorld(var20, this.armChains[0].tip, var4),
         toWorld(var20, this.armChains[1].tip, var4),
         toWorld(var20, this.legChains[0].tip, var4),
         toWorld(var20, this.legChains[1].tip, var4)
      );
   }

   private static Vec3 toWorld(Matrix4f var0, Vec3 var1, Vec3 var2) {
      return transformPoint(var0, var1).add(var2);
   }

   public static KillEffect.PartState risePose(KillEffect var0, int var1, double var2) {
      double var4 = smoothstep((var2 - 0.22) / 0.48);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = KillEffect.getTrail(var1, var0.slimArms) / 16.0 * 0.9375;
      double var10 = (24.0 - KillEffect.pART_OFFSETS[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.yaw);
      double var16 = Math.sin(var0.yaw);
      Vec3 var18 = var0.origin.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = smoothstep((var2 - 6.790000000000001) / 0.65);
      double var21 = smoothstep((7.5 - var2) / 0.65);
      return new KillEffect.PartState(var18, -var6, 0.0, 0.0, 1.0, var21, var19, var4 >= 1.0);
   }

   public record Frame(
      float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 rightHand, Vec3 leftHand, Vec3 rightFoot, Vec3 leftFoot
   ) {
   }

   private static final class LimbChain {
      final Matrix4f rootTransform = new Matrix4f();
      final Matrix4f upperTransform = new Matrix4f();
      final Matrix4f endTransform = new Matrix4f();
      Vec3 tip = Vec3.ZERO;

      void solve(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, Vec3 var6) {
         double var7 = var1.distanceTo(var2);
         double var9 = var2.distanceTo(var3);
         Vec3 var11 = var5.subtract(var4);
         double var12 = Math.clamp(var11.length(), 0.001, var7 + var9 - 1.0E-5);
         Vec3 var14 = var11.lengthSqr() < 1.0E-12 ? new Vec3(0.0, -1.0, 0.0) : var11.normalize();
         this.tip = var4.add(var14.scale(var12));
         Vec3 var15 = var6.subtract(var14.scale(var6.dot(var14)));
         if (var15.lengthSqr() < 1.0E-10) {
            var15 = new Vec3(1.0, 0.0, 0.0).cross(var14);
         }

         var15 = var15.normalize();
         double var16 = (var7 * var7 - var9 * var9 + var12 * var12) / (2.0 * var12);
         Vec3 var18 = var4.add(var14.scale(var16)).add(var15.scale(Math.sqrt(Math.max(0.0, var7 * var7 - var16 * var16))));
         buildTransform(this.rootTransform, var1, var2, var4, var18);
         buildTransform(this.upperTransform, var2, var3, var18, this.tip);
         this.endTransform.translation((float)(this.tip.x - var3.x), (float)(this.tip.y - var3.y), (float)(this.tip.z - var3.z));
      }

      private static void buildTransform(Matrix4f var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         Vector3f var5 = new Vector3f((float)(var2.x - var1.x), (float)(var2.y - var1.y), (float)(var2.z - var1.z)).normalize();
         Vector3f var6 = new Vector3f((float)(var4.x - var3.x), (float)(var4.y - var3.y), (float)(var4.z - var3.z)).normalize();
         var0.translation((float)var3.x, (float)var3.y, (float)var3.z)
            .rotate(new Quaternionf().rotationTo(var5, var6))
            .translate((float)(-var1.x), (float)(-var1.y), (float)(-var1.z));
      }
   }

   public record Pose(Vec3 root, double compression, double hipShift, double lean, double roll, double tuck, Vec3 rightHand, Vec3 leftHand, double dance) {
   }
}
