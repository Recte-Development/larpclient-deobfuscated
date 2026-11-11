package com.larpclient.render.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class LegRig {
   private static final Vector3f[] frontUpperPivots = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] frontLowerPivots = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] rearUpperPivots = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] rearLowerPivots = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float fRONT_JOINT_Y = 0.002F;
   private static final float rEAR_JOINT_Y = -0.194F;
   private static final float rEAR_SWING_SCALE = 0.9F;
   private static final float fRONT_SWING_SCALE = 0.7F;
   private final SahurMesh.Mesh sourceModel;
   private final SahurMesh.Mesh mesh;
   private final int[] jointIds;
   private final float[] animatedVertices;
   private final Matrix4f[] upperMatrices = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] lowerMatrices = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f position = new Vector3f();
   private final Vector3f bentPosition = new Vector3f();
   private float groundOffset;

   public LegRig(SahurMesh.Mesh var1) {
      SahurMeshSplitter.JointSplit var2 = SahurMeshSplitter.splitLegs(var1);
      this.sourceModel = var1;
      this.mesh = var2.mesh();
      this.jointIds = var2.joints();
      this.animatedVertices = (float[])this.mesh.vertices().clone();
   }

   public SahurMesh.Mesh getSourceModel() {
      return this.sourceModel;
   }

   public SahurMesh.Mesh getMesh() {
      return this.mesh;
   }

   public float[] getVertices() {
      return this.animatedVertices;
   }

   private static void rotateAround(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float smoothstep(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] animate(float var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      float var4 = var1 * 0.6662F;
      float var5 = (float)Math.cos(var4) * var3;
      float var6 = (float)Math.sin(var4) * var3;
      float[] var7 = new float[]{var5 * 0.9F, -var5 * 0.9F};
      float[] var8 = new float[]{-var5 * 0.7F, var5 * 0.7F};
      float[] var9 = new float[]{Math.max(0.0F, var6) * 0.9F, Math.max(0.0F, -var6) * 0.9F};
      Matrix4f var10 = new Matrix4f();

      for (int var11 = 0; var11 < 2; var11++) {
         float var12 = -0.3F * var3 - (var11 == 0 ? Math.max(0.0F, var8[var11]) : 0.0F);
         rotateAround(this.upperMatrices[var11], var10, frontUpperPivots[var11], var8[var11]);
         rotateAround(this.lowerMatrices[var11], this.upperMatrices[var11], frontLowerPivots[var11], var12);
         rotateAround(this.upperMatrices[var11 + 2], var10, rearUpperPivots[var11], var7[var11]);
         rotateAround(this.lowerMatrices[var11 + 2], this.upperMatrices[var11 + 2], rearLowerPivots[var11], var9[var11]);
      }

      float[] var21 = this.mesh.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.jointIds.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.jointIds[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.position.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.bentPosition.set(this.position);
            this.upperMatrices[var20].transformPosition(this.position);
            this.lowerMatrices[var20].transformPosition(this.bentPosition);
            this.position.lerp(this.bentPosition, smoothstep(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.position.y);
            }
         }

         this.animatedVertices[var14] = this.position.x;
         this.animatedVertices[var14 + 1] = this.position.y;
         this.animatedVertices[var14 + 2] = this.position.z;
      }

      this.groundOffset = var22 - this.mesh.minimumY();

      for (byte var23 = 1; var23 < this.animatedVertices.length; var23 += 5) {
         this.animatedVertices[var23] = this.animatedVertices[var23] - this.groundOffset;
      }

      return this.animatedVertices;
   }

   public Vector3f transformPoint(boolean var1, Vector3f var2) {
      return this.lowerMatrices[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.groundOffset, 0.0F);
   }
}
