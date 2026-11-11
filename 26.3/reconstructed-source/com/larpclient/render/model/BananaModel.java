package com.larpclient.render.model;

import com.larpclient.social.emote.EmoteAnimation;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class BananaModel {
   public static final int mAGIC = 1112424753;
   public static final int bONE_COUNT = 11;
   public static final int mAX_VERTICES = 8000;
   public static final int mAX_TRIANGLES = 6000;
   public static final int bONE_ROOT = 0;
   public static final int bONE_TORSO = 1;
   public static final int bONE_HEAD = 2;
   public static final int bONE_LEFT_ARM = 3;
   public static final int bONE_LEFT_HAND = 4;
   public static final int bONE_RIGHT_ARM = 5;
   public static final int bONE_RIGHT_HAND = 6;
   public static final int bONE_LEFT_LEG = 7;
   public static final int bONE_LEFT_FOOT = 8;
   public static final int bONE_RIGHT_LEG = 9;
   public static final int bONE_RIGHT_FOOT = 10;
   private static final byte[] bONE_PARENTS = new byte[]{-1, 0, 1, 1, 3, 1, 5, 0, 7, 0, 9};
   private static final int pOSE_STRIDE = 10;
   private static final float[][] rEST_OFFSETS = new float[][]{
      {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {-5.0F, 2.0F, 0.0F}, {5.0F, 2.0F, 0.0F}, {-1.9F, 12.0F, 0.1F}, {1.9F, 12.0F, 0.1F}
   };
   private static final float pIXEL_SCALE = 0.05859375F;
   private static final float lIGHT_X = 0.35F;
   private static final float lIGHT_Y = 0.8F;
   private static final float lIGHT_Z = 0.48F;
   private final BananaModel.Mesh mesh;
   private final float[] pose = new float[EmoteAnimation.totalChannels];
   private final float[] lastPose = new float[EmoteAnimation.totalChannels];
   private final float[] boneMatrixData = new float[176];
   private final Matrix4f[] boneTransforms = new Matrix4f[11];
   private final float[] skinnedPositions;
   private final float[] vertexLight;
   private final float[] vertexData;
   private final int[] vertexColors;
   private final int[] footVertices;
   private final Matrix4f scratchMatrix = new Matrix4f();
   private final Matrix4f scratchRotation = new Matrix4f();
   private final Vector3f scratchVector = new Vector3f();
   private final float modelScale;
   private float lastScale = -1.0F;
   private float groundOffset;
   private boolean hasPose;

   public static BananaModel.Mesh loadMesh(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Banana mesh missing");
      }

      DataInputStream var1 = new DataInputStream(var0);
      if (var1.readInt() != 1112424753) {
         throw new IOException("Invalid Banana mesh header");
      }

      if (var1.readInt() != 11) {
         throw new IOException("Unexpected Banana bone count");
      }

      float[] var2 = new float[33];

      for (int var3 = 0; var3 < 11; var3++) {
         if (var1.readByte() != bONE_PARENTS[var3]) {
            throw new IOException("Unexpected Banana bone hierarchy");
         }

         for (int var4 = 0; var4 < 3; var4++) {
            var2[var3 * 3 + var4] = checkCoordinate(var1.readFloat());
         }
      }

      float[] var17 = new float[6];

      for (int var18 = 0; var18 < var17.length; var18++) {
         var17[var18] = checkCoordinate(var1.readFloat());
      }

      int var19 = var1.readInt();
      if (var19 >= 3 && var19 <= 8000) {
         float[] var5 = new float[var19 * 3];
         float[] var6 = new float[var19 * 3];
         float[] var7 = new float[var19 * 2];
         float[] var8 = new float[var19 * 4];
         byte[] var9 = new byte[var19 * 4];
         float var10 = Float.POSITIVE_INFINITY;
         float var11 = Float.NEGATIVE_INFINITY;

         for (int var12 = 0; var12 < var19; var12++) {
            for (int var13 = 0; var13 < 3; var13++) {
               var5[var12 * 3 + var13] = checkCoordinate(var1.readFloat());
            }

            var10 = Math.min(var10, var5[var12 * 3 + 1]);
            var11 = Math.max(var11, var5[var12 * 3 + 1]);
            float var21 = 0.0F;

            for (int var14 = 0; var14 < 3; var14++) {
               float var15 = var1.readFloat();
               if (!Float.isFinite(var15)) {
                  throw new IOException("Invalid Banana normal");
               }

               var6[var12 * 3 + var14] = var15;
               var21 += var15 * var15;
            }

            if (Math.abs(var21 - 1.0F) > 0.01F) {
               throw new IOException("Banana normal is not unit length");
            }

            for (int var23 = 0; var23 < 2; var23++) {
               float var27 = var1.readFloat();
               if (!Float.isFinite(var27) || var27 < 0.0F || var27 > 1.0F) {
                  throw new IOException("Invalid Banana texture coordinate");
               }

               var7[var12 * 2 + var23] = var27;
            }

            for (int var24 = 0; var24 < 4; var24++) {
               int var28 = var1.readUnsignedByte();
               if (var28 >= 11) {
                  throw new IOException("Invalid Banana bone index");
               }

               var9[var12 * 4 + var24] = (byte)var28;
            }

            int var25 = 0;

            for (int var29 = 0; var29 < 4; var29++) {
               int var16 = var1.readUnsignedByte();
               var25 += var16;
               var8[var12 * 4 + var29] = var16 / 255.0F;
            }

            if (var25 != 255) {
               throw new IOException("Banana weights must total 255");
            }
         }

         int var20 = var1.readInt();
         if (var20 >= 3 && var20 % 3 == 0 && var20 <= 18000) {
            int[] var22 = new int[var20];

            for (int var26 = 0; var26 < var20; var26++) {
               int var30 = var1.readUnsignedShort();
               if (var30 >= var19) {
                  throw new IOException("Banana index out of range");
               }

               var22[var26] = var30;
            }

            if (var1.read() != -1) {
               throw new IOException("Unexpected Banana mesh trailer");
            } else if (!(Math.abs(var10) > 0.001F) && !(var11 - var10 < 0.5F)) {
               return new BananaModel.Mesh(var2, var17, var5, var6, var7, var9, var8, var22, var11 - var10);
            } else {
               throw new IOException("Banana mesh must stand on y=0");
            }
         } else {
            throw new IOException("Invalid Banana triangle budget");
         }
      } else {
         throw new IOException("Invalid Banana vertex budget");
      }
   }

   private static float checkCoordinate(float var0) throws IOException {
      if (Float.isFinite(var0) && !(Math.abs(var0) > 3.0F)) {
         return var0;
      } else {
         throw new IOException("Invalid Banana coordinate");
      }
   }

   public static float[] buildPose(float[] var0, float var1, float var2, float var3, float var4, float var5, boolean var6) {
      for (int var7 = 0; var7 < rEST_OFFSETS.length; var7++) {
         int var8 = var7 * 10;
         Arrays.fill(var0, var8, var8 + 10, 0.0F);
         var0[var8] = rEST_OFFSETS[var7][0];
         var0[var8 + 1] = rEST_OFFSETS[var7][1];
         var0[var8 + 2] = rEST_OFFSETS[var7][2];
      }

      float var13 = Math.clamp(var2, 0.0F, 1.0F);
      float var14 = var1 * 0.6662F;
      float var9 = (float)Math.cos(var14) * var13;
      float var10 = (float)Math.sin(var14) * var13;
      setPose(var0, EmoteAnimation.Part.HEAD, 3, Math.clamp((float)Math.toRadians(var5) * 0.5F, -0.45F, 0.45F));
      setPose(var0, EmoteAnimation.Part.HEAD, 4, Math.clamp((float)Math.toRadians(var4), -1.3F, 1.3F));
      setPose(var0, EmoteAnimation.Part.RIGHT_ARM, 3, -var9 * 0.7F);
      setPose(var0, EmoteAnimation.Part.LEFT_ARM, 3, var9 * 0.7F);
      setPose(var0, EmoteAnimation.Part.RIGHT_LEG, 3, var9 * 0.9F);
      setPose(var0, EmoteAnimation.Part.LEFT_LEG, 3, -var9 * 0.9F);
      setPose(var0, EmoteAnimation.Part.RIGHT_LEG, 6, Math.max(0.0F, var10) * 0.9F);
      setPose(var0, EmoteAnimation.Part.LEFT_LEG, 6, Math.max(0.0F, -var10) * 0.9F);
      float var11 = (float)Math.cos(var3 * 0.09F) * 0.05F + 0.05F;
      float var12 = (float)Math.sin(var3 * 0.067F) * 0.05F;
      addPose(var0, EmoteAnimation.Part.RIGHT_ARM, 5, var11);
      addPose(var0, EmoteAnimation.Part.RIGHT_ARM, 3, var12);
      addPose(var0, EmoteAnimation.Part.LEFT_ARM, 5, -var11);
      addPose(var0, EmoteAnimation.Part.LEFT_ARM, 3, -var12);
      if (var6) {
         setPose(var0, EmoteAnimation.Part.TORSO, 3, 0.5F);
         addPose(var0, EmoteAnimation.Part.RIGHT_ARM, 3, 0.4F);
         addPose(var0, EmoteAnimation.Part.LEFT_ARM, 3, 0.4F);
         addPose(var0, EmoteAnimation.Part.RIGHT_LEG, 3, -0.8F);
         addPose(var0, EmoteAnimation.Part.LEFT_LEG, 3, -0.8F);
         addPose(var0, EmoteAnimation.Part.RIGHT_LEG, 6, 0.8F);
         addPose(var0, EmoteAnimation.Part.LEFT_LEG, 6, 0.8F);
      }

      return var0;
   }

   public static void blendPose(float[] var0, float[] var1, float var2) {
      if (!(var2 <= 0.0F)) {
         for (int var3 = 0; var3 < rEST_OFFSETS.length; var3++) {
            for (int var4 = 0; var4 <= 6; var4++) {
               int var5 = var3 * 10 + var4;
               float var6 = var1[var5];
               if (Float.isNaN(var6)) {
                  if (var4 == 6 && EmoteAnimation.isLimb(EmoteAnimation.Part.values()[var3]) && hasLegRotation(var1)) {
                     var0[var5] -= var0[var5] * var2;
                  }
               } else {
                  var0[var5] += (var6 - var0[var5]) * var2;
               }
            }
         }
      }
   }

   private static boolean hasLegRotation(float[] var0) {
      for (EmoteAnimation.Part var4 : new EmoteAnimation.Part[]{
         EmoteAnimation.Part.RIGHT_ARM, EmoteAnimation.Part.LEFT_ARM, EmoteAnimation.Part.RIGHT_LEG, EmoteAnimation.Part.LEFT_LEG
      }) {
         if (!Float.isNaN(var0[var4.ordinal() * 10 + 6])) {
            return true;
         }
      }

      return false;
   }

   public static Matrix4f applyRootTransform(Matrix4f var0, float[] var1, float var2) {
      int var3 = EmoteAnimation.Part.BODY.ordinal() * 10;
      float var4 = scaleOrZero(var1[var3], var2);
      float var5 = scaleOrZero(var1[var3 + 1], var2);
      float var6 = scaleOrZero(var1[var3 + 2], var2);
      return var0.translate(-var4, var5 + 0.7F, -var6)
         .rotateZ(-scaleOrZero(var1[var3 + 5], var2))
         .rotateY(scaleOrZero(var1[var3 + 4], var2))
         .rotateX(-scaleOrZero(var1[var3 + 3], var2))
         .translate(0.0F, -0.7F, 0.0F);
   }

   private static float scaleOrZero(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }

   private static void setPose(float[] var0, EmoteAnimation.Part var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] = var3;
   }

   private static void addPose(float[] var0, EmoteAnimation.Part var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] += var3;
   }

   public BananaModel(BananaModel.Mesh var1) {
      this.mesh = var1;

      for (int var2 = 0; var2 < 11; var2++) {
         this.boneTransforms[var2] = new Matrix4f();
      }

      this.skinnedPositions = new float[var1.vertices() * 3];
      this.vertexLight = new float[var1.vertices()];
      this.vertexData = new float[var1.indices().length * 5];
      this.vertexColors = new int[var1.indices().length];
      this.modelScale = 0.05859375F * var1.height() / 1.8F;
      int var6 = 0;
      int[] var3 = new int[var1.vertices()];

      for (int var4 = 0; var4 < var1.vertices(); var4++) {
         byte var5 = var1.bones()[var4 * 4];
         if ((var5 == 8 || var5 == 10) && var1.weights()[var4 * 4] > 0.5F) {
            var3[var6++] = var4;
         }
      }

      this.footVertices = Arrays.copyOf(var3, var6);
   }

   public BananaModel.Mesh getMesh() {
      return this.mesh;
   }

   public float[] getVertexData() {
      return this.vertexData;
   }

   public int[] getVertexColors() {
      return this.vertexColors;
   }

   public float getGroundOffset() {
      return this.groundOffset;
   }

   public void updatePose(float[] var1, float var2) {
      if (!this.hasPose || var2 != this.lastScale || !Arrays.equals(var1, 0, EmoteAnimation.totalChannels, this.lastPose, 0, EmoteAnimation.totalChannels)) {
         System.arraycopy(var1, 0, this.pose, 0, EmoteAnimation.totalChannels);
         System.arraycopy(var1, 0, this.lastPose, 0, EmoteAnimation.totalChannels);
         this.lastScale = var2;
         this.updateBones();
         this.skinMesh();
         this.hasPose = (boolean)1;
      }
   }

   private void updateBones() {
      float var1 = (this.poseDelta(EmoteAnimation.Part.RIGHT_LEG, 0) + this.poseDelta(EmoteAnimation.Part.LEFT_LEG, 0)) / 2.0F;
      float var2 = (this.poseDelta(EmoteAnimation.Part.RIGHT_LEG, 1) + this.poseDelta(EmoteAnimation.Part.LEFT_LEG, 1)) / 2.0F;
      float var3 = (this.poseDelta(EmoteAnimation.Part.RIGHT_LEG, 2) + this.poseDelta(EmoteAnimation.Part.LEFT_LEG, 2)) / 2.0F;
      this.boneTransforms[0].translation(this.applyRootTransform(var1), this.hasLegRotation(var2), this.scaleZ(var3));
      this.boneTransforms[1]
         .set(this.boneTransforms[0])
         .translate(
            this.applyRootTransform(this.poseDelta(EmoteAnimation.Part.TORSO, 0)),
            this.hasLegRotation(this.poseDelta(EmoteAnimation.Part.TORSO, 1)),
            this.scaleZ(this.poseDelta(EmoteAnimation.Part.TORSO, 2))
         );
      this.applyPivotedTransform(this.boneTransforms[1], 1, this.rotationMatrix(EmoteAnimation.Part.TORSO));
      this.updateLimbBone(2, EmoteAnimation.Part.HEAD, 0.0F, 0.0F, 0.0F);
      this.updateLimbBone(3, EmoteAnimation.Part.RIGHT_ARM, rEST_OFFSETS[2][0], rEST_OFFSETS[2][1], rEST_OFFSETS[2][2]);
      this.updateLimbBone(5, EmoteAnimation.Part.LEFT_ARM, rEST_OFFSETS[3][0], rEST_OFFSETS[3][1], rEST_OFFSETS[3][2]);
      this.buildPose(4, 3, EmoteAnimation.Part.RIGHT_ARM);
      this.buildPose(6, 5, EmoteAnimation.Part.LEFT_ARM);

      for (int var4 = 0; var4 < 2; var4++) {
         EmoteAnimation.Part var5 = var4 == 0 ? EmoteAnimation.Part.RIGHT_LEG : EmoteAnimation.Part.LEFT_LEG;
         int var6 = var4 == 0 ? 7 : 9;
         this.boneTransforms[var6]
            .set(this.boneTransforms[0])
            .translate(
               this.applyRootTransform(this.poseDelta(var5, 0) - var1),
               this.hasLegRotation(this.poseDelta(var5, 1) - var2),
               this.scaleZ(this.poseDelta(var5, 2) - var3)
            );
         this.applyPivotedTransform(this.boneTransforms[var6], var6, this.rotationMatrix(var5));
         this.buildPose(var6 + 1, var6, var5);
      }

      for (int var7 = 0; var7 < 11; var7++) {
         this.boneTransforms[var7].get(this.boneMatrixData, var7 * 16);
      }
   }

   private void applyPivotedTransform(Matrix4f var1, int var2, Matrix4f var3) {
      float var4 = this.mesh.pivots()[var2 * 3];
      float var5 = this.mesh.pivots()[var2 * 3 + 1];
      float var6 = this.mesh.pivots()[var2 * 3 + 2];
      var1.translate(var4, var5, var6).mul(var3).translate(-var4, -var5, -var6);
   }

   private void updateLimbBone(int var1, EmoteAnimation.Part var2, float var3, float var4, float var5) {
      int var6 = EmoteAnimation.Part.TORSO.ordinal() * 10;
      int var7 = var2.ordinal() * 10;
      this.scratchVector.set(var3, var4, var5);
      this.scratchMatrix.identity().rotateZ(this.pose[var6 + 5]).rotateY(this.pose[var6 + 4]).rotateX(this.pose[var6 + 3]);
      this.scratchMatrix.transformDirection(this.scratchVector).add(this.pose[var6], this.pose[var6 + 1], this.pose[var6 + 2]);
      float var8 = this.pose[var7] - this.scratchVector.x;
      float var9 = this.pose[var7 + 1] - this.scratchVector.y;
      float var10 = this.pose[var7 + 2] - this.scratchVector.z;
      this.scratchVector.set(this.mesh.pivots()[var1 * 3], this.mesh.pivots()[var1 * 3 + 1], this.mesh.pivots()[var1 * 3 + 2]);
      this.boneTransforms[1].transformPosition(this.scratchVector).add(this.applyRootTransform(var8), this.hasLegRotation(var9), this.scaleZ(var10));
      this.boneTransforms[var1].translation(this.scratchVector).mul(this.rotationMatrix(var2));
      this.boneTransforms[var1].translate(-this.mesh.pivots()[var1 * 3], -this.mesh.pivots()[var1 * 3 + 1], -this.mesh.pivots()[var1 * 3 + 2]);
   }

   private void buildPose(int var1, int var2, EmoteAnimation.Part var3) {
      this.boneTransforms[var1].set(this.boneTransforms[var2]);
      this.applyPivotedTransform(this.boneTransforms[var1], var1, this.scratchRotation.identity().rotateX(this.pose[var3.ordinal() * 10 + 6]));
   }

   private Matrix4f rotationMatrix(EmoteAnimation.Part var1) {
      int var2 = var1.ordinal() * 10;
      return this.scratchMatrix.identity().rotateZ(-this.pose[var2 + 5]).rotateY(-this.pose[var2 + 4]).rotateX(this.pose[var2 + 3]);
   }

   private float poseDelta(EmoteAnimation.Part var1, int var2) {
      return this.pose[var1.ordinal() * 10 + var2] - rEST_OFFSETS[var1.ordinal()][var2];
   }

   private float applyRootTransform(float var1) {
      return var1 * this.modelScale;
   }

   private float hasLegRotation(float var1) {
      return -var1 * this.modelScale;
   }

   private float scaleZ(float var1) {
      return -var1 * this.modelScale;
   }

   private void skinMesh() {
      float[] var1 = this.mesh.positions();
      float[] var2 = this.mesh.normals();
      float[] var3 = this.mesh.weights();
      float[] var4 = this.boneMatrixData;
      byte[] var5 = this.mesh.bones();
      float var6 = (float)Math.sqrt(0.9929F);
      int var7 = 0;

      for (int var8 = this.mesh.vertices(); var7 < var8; var7++) {
         float var9 = var1[var7 * 3];
         float var10 = var1[var7 * 3 + 1];
         float var11 = var1[var7 * 3 + 2];
         float var12 = var2[var7 * 3];
         float var13 = var2[var7 * 3 + 1];
         float var14 = var2[var7 * 3 + 2];
         float var15 = 0.0F;
         float var16 = 0.0F;
         float var17 = 0.0F;
         float var18 = 0.0F;
         float var19 = 0.0F;
         float var20 = 0.0F;

         for (int var21 = 0; var21 < 4; var21++) {
            float var22 = var3[var7 * 4 + var21];
            if (var22 != 0.0F) {
               int var23 = var5[var7 * 4 + var21] * 16;
               var15 += var22 * (var4[var23] * var9 + var4[var23 + 4] * var10 + var4[var23 + 8] * var11 + var4[var23 + 12]);
               var16 += var22 * (var4[var23 + 1] * var9 + var4[var23 + 5] * var10 + var4[var23 + 9] * var11 + var4[var23 + 13]);
               var17 += var22 * (var4[var23 + 2] * var9 + var4[var23 + 6] * var10 + var4[var23 + 10] * var11 + var4[var23 + 14]);
               var18 += var22 * (var4[var23] * var12 + var4[var23 + 4] * var13 + var4[var23 + 8] * var14);
               var19 += var22 * (var4[var23 + 1] * var12 + var4[var23 + 5] * var13 + var4[var23 + 9] * var14);
               var20 += var22 * (var4[var23 + 2] * var12 + var4[var23 + 6] * var13 + var4[var23 + 10] * var14);
            }
         }

         this.skinnedPositions[var7 * 3] = var15;
         this.skinnedPositions[var7 * 3 + 1] = var16;
         this.skinnedPositions[var7 * 3 + 2] = var17;
         float var35 = (float)Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
         float var36 = var35 < 1.0E-6F ? 0.0F : (var18 * 0.35F + var19 * 0.8F + var20 * 0.48F) / (var35 * var6);
         this.vertexLight[var7] = 0.7F + 0.3F * Math.max(0.0F, var36);
      }

      float var24 = Float.POSITIVE_INFINITY;

      for (int var31 : this.footVertices) {
         var24 = Math.min(var24, this.skinnedPositions[var31 * 3 + 1]);
      }

      this.groundOffset = this.footVertices.length == 0 ? 0.0F : var24 * this.lastScale;
      int[] var26 = this.mesh.indices();
      float[] var28 = this.mesh.uvs();

      for (int var30 = 0; var30 < var26.length; var30++) {
         int var32 = var26[var30];
         int var33 = var30 * 5;
         this.vertexData[var33] = this.skinnedPositions[var32 * 3];
         this.vertexData[var33 + 1] = this.skinnedPositions[var32 * 3 + 1] - this.groundOffset;
         this.vertexData[var33 + 2] = this.skinnedPositions[var32 * 3 + 2];
         this.vertexData[var33 + 3] = var28[var32 * 2];
         this.vertexData[var33 + 4] = var28[var32 * 2 + 1];
         int var34 = Math.round(this.vertexLight[var32] * 255.0F);
         this.vertexColors[var30] = 0xFF000000 | var34 << 16 | var34 << 8 | var34;
      }
   }

   public Vector3f applyRootTransform(int var1, Vector3f var2) {
      return this.boneTransforms[var1].transformPosition(var2).sub(0.0F, this.groundOffset, 0.0F);
   }

   public Vector3f blendPose(boolean var1) {
      return this.applyRootTransform(var1 ? 4 : 6, this.mesh.palm(var1));
   }

   public record Mesh(
      float[] pivots, float[] palms, float[] positions, float[] normals, float[] uvs, byte[] bones, float[] weights, int[] indices, float height
   ) {
      public int vertices() {
         return this.positions.length / 3;
      }

      public int triangles() {
         return this.indices.length / 3;
      }

      public Vector3f pivot(int var1) {
         return new Vector3f(this.pivots[var1 * 3], this.pivots[var1 * 3 + 1], this.pivots[var1 * 3 + 2]);
      }

      public Vector3f palm(boolean var1) {
         int var2 = var1 ? 0 : 3;
         return new Vector3f(this.palms[var2], this.palms[var2 + 1], this.palms[var2 + 2]);
      }
   }
}
