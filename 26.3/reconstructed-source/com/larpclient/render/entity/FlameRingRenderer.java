package com.larpclient.render.entity;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.util.misc.PreviewSink;
import com.larpclient.util.render.ColorUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class FlameRingRenderer {
   public static final float rING_RADIUS = 0.262F;
   public static final float tUBE_RADIUS = 0.0115F;
   public static final float rING_HEIGHT = 0.155F;
   private static final float rING_Z_OFFSET = 0.012F;
   private static final float tILT_SLOPE = (float)Math.tan(Math.toRadians(8.0));
   public static final float fLAME_BASE_LENGTH = 0.15F;
   public static final float tOP_EXTENT = 0.155F + 0.262F * tILT_SLOPE + 0.0115F + 0.09F;
   private static final int rING_SEGMENTS = 96;
   private static final int tUBE_SEGMENTS = 10;
   private static final int fLAME_COUNT = 48;
   private static final int fLAME_SIDES = 12;
   private static final int cOLOR_PALE = 16776168;
   private static final int cOLOR_TUBE_DARK = 16765034;
   private static final int cOLOR_FLAME_TIP = 16764757;
   private static final int cOLOR_FLAME_MID = 16757294;
   private static final int cOLOR_FLAME_BASE = 16748316;
   private static final float[] glowOffsets = new float[]{0.0F, 0.012F, 0.026F, 0.046F};
   private static final float[] glowAlphas = new float[]{0.62F, 0.36F, 0.12F, 0.0F};
   private static final float[] flameProfile = new float[]{0.0F, 0.22F, 0.48F, 0.75F, 1.0F};
   private static final float[] flameAlphas = new float[]{1.0F, 0.72F, 0.32F, 0.08F, 0.0F};
   private static final float[] ringPoints = new float[291];
   private static final float[] ringTangents = new float[291];
   private static final float[] torusVertices = new float[3201];
   private static final int[] torusColors = new int[1067];
   private static final float[] circleCos = new float[13];
   private static final float[] circleSin = new float[13];
   private static final float[] glowOffsetDirs = new float[291];

   private FlameRingRenderer() {
   }

   public static void renderFlameRing(PreviewSink var0, Matrix4f var1, double var2, double var4, double var6) {
      float var8 = (float)var2;
      float var9 = (float)Math.clamp(var4, 0.0, 1.0);
      drawTorus(var0, var1, var8, var9);
      Vector3f var10 = var0.viewer(var1, new Vector3f());
      float var11 = 1.0F + 0.07F * (float)Math.sin(var6 * 1.5);
      var0.blend(HitEffectConfig.BlendMode.NORMAL);
      drawFlames(var0, var1, var8, var9 * var11, var6, var10);
      var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
      drawGlowBand(var0, var1, var8, var9 * var11, var6, var10);
      var0.blend(HitEffectConfig.BlendMode.NORMAL);
   }

   public static void renderGlowRing(PreviewSink var0, Matrix4f var1, double var2, double var4, double var6) {
      float var8 = (float)var2;
      float var9 = (float)Math.clamp(var4, 0.0, 1.0);
      drawTorus(var0, var1, var8, var9);
      var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
      drawGlowBand(var0, var1, var8, var9, var6, var0.viewer(var1, new Vector3f()));
      var0.blend(HitEffectConfig.BlendMode.NORMAL);
   }

   private static void drawTorus(PreviewSink var0, Matrix4f var1, float var2, float var3) {
      int var4 = (int)(var3 * 255.0F) << 24;
      var0.blend(HitEffectConfig.BlendMode.NORMAL);
      var0.solid(var3 >= 0.995);

      try {
         for (int var5 = 0; var5 < 96; var5++) {
            for (int var6 = 0; var6 < 10; var6++) {
               torusVertex(var0, var1, var2, var4, var5, var6);
               torusVertex(var0, var1, var2, var4, var5 + 1, var6);
               torusVertex(var0, var1, var2, var4, var5 + 1, var6 + 1);
               torusVertex(var0, var1, var2, var4, var5, var6 + 1);
            }
         }
      } finally {
         var0.solid(false);
      }
   }

   private static void torusVertex(PreviewSink var0, Matrix4f var1, float var2, int var3, int var4, int var5) {
      int var6 = var4 * 11 + var5;
      int var7 = var6 * 3;
      var0.modelVertex(var1, torusVertices[var7] * var2, -0.5F - torusVertices[var7 + 1] * var2, torusVertices[var7 + 2] * var2, var3 | torusColors[var6]);
   }

   private static float renderFlameRing(int var0, double var1) {
      double var3 = var0 * Math.PI * 2.0 / 96.0;
      return 1.0F + 0.2F * (float)Math.sin(var3 * 3.0 - var1 * 0.9) * (float)Math.sin(var3 * 2.0 + var1 * 0.55);
   }

   private static void drawGlowBand(PreviewSink var0, Matrix4f var1, float var2, float var3, double var4, Vector3f var6) {
      for (int var7 = 0; var7 <= 96; var7++) {
         float var8 = ringPoints[var7 * 3] * var2;
         float var9 = -0.5F - ringPoints[var7 * 3 + 1] * var2;
         float var10 = ringPoints[var7 * 3 + 2] * var2;
         float var11 = var6.x - var8;
         float var12 = var6.y - var9;
         float var13 = var6.z - var10;
         float var14 = ringTangents[var7 * 3];
         float var15 = -ringTangents[var7 * 3 + 1];
         float var16 = ringTangents[var7 * 3 + 2];
         float var17 = var15 * var13 - var16 * var12;
         float var18 = var16 * var11 - var14 * var13;
         float var19 = var14 * var12 - var15 * var11;
         float var20 = (float)Math.sqrt(var17 * var17 + var18 * var18 + var19 * var19);
         if (var20 < 1.0E-6F) {
            var17 = 0.0F;
            var18 = 1.0F;
            var19 = 0.0F;
            var20 = 1.0F;
         }

         glowOffsetDirs[var7 * 3] = var17 / var20;
         glowOffsetDirs[var7 * 3 + 1] = var18 / var20;
         glowOffsetDirs[var7 * 3 + 2] = var19 / var20;
      }

      for (int var21 = 0; var21 < 96; var21++) {
         float var22 = glowOffsetDirs[var21 * 3] * glowOffsetDirs[var21 * 3 + 3]
                  + glowOffsetDirs[var21 * 3 + 1] * glowOffsetDirs[var21 * 3 + 4]
                  + glowOffsetDirs[var21 * 3 + 2] * glowOffsetDirs[var21 * 3 + 5]
               < 0.0F
            ? -1.0F
            : 1.0F;
         float var23 = var3 * renderFlameRing(var21, var4);

         for (int var24 = 0; var24 < glowOffsets.length - 1; var24++) {
            float var25 = glowOffsets[var24] * var2;
            float var26 = glowOffsets[var24 + 1] * var2;
            int var27 = packColor(16776168, 16748316, var24 / 3.0F, glowAlphas[var24] * var23);
            int var28 = packColor(16776168, 16748316, (var24 + 1) / 3.0F, glowAlphas[var24 + 1] * var23);

            for (byte var29 = -1; var29 <= 1; var29 += 2) {
               glowVertex(var0, var1, var2, var21, var29 * var25, 1.0F, var27);
               glowVertex(var0, var1, var2, var21, var29 * var26, 1.0F, var28);
               glowVertex(var0, var1, var2, var21 + 1, var29 * var26, var22, var28);
               glowVertex(var0, var1, var2, var21 + 1, var29 * var25, var22, var27);
            }
         }
      }
   }

   private static void glowVertex(PreviewSink var0, Matrix4f var1, float var2, int var3, float var4, float var5, int var6) {
      float var7 = var4 * var5;
      var0.modelVertex(
         var1,
         ringPoints[var3 * 3] * var2 + glowOffsetDirs[var3 * 3] * var7,
         -0.5F - ringPoints[var3 * 3 + 1] * var2 + glowOffsetDirs[var3 * 3 + 1] * var7,
         ringPoints[var3 * 3 + 2] * var2 + glowOffsetDirs[var3 * 3 + 2] * var7,
         var6
      );
   }

   private static void drawFlames(PreviewSink var0, Matrix4f var1, float var2, float var3, double var4, Vector3f var6) {
      float var7 = 0.0F;
      float var8 = -0.5F - 0.155F * var2;
      float var9 = 0.012F * var2;
      float var10 = var6.x - var7;
      float var11 = var6.y - var8;
      float var12 = var6.z - var9;
      float var13 = (float)Math.sqrt(var10 * var10 + var11 * var11 + var12 * var12);
      if (!(var13 < 1.0E-5F)) {
         var10 /= var13;
         var11 /= var13;
         var12 /= var13;
         float var14 = -var12;
         float var15 = var10;
         float var16 = (float)Math.sqrt(var14 * var14 + var15 * var15);
         if (var16 < 1.0E-4F) {
            var14 = 1.0F;
            var15 = 0.0F;
            var16 = 1.0F;
         }

         var14 /= var16;
         var15 /= var16;
         float var17 = -var11 * var15;
         float var18 = var12 * var14 - var10 * var15;
         float var19 = var11 * var14;

         for (int var20 = 0; var20 < 48; var20++) {
            int var21 = var20 * 96 / 48;
            float var22 = ringPoints[var21 * 3] * var2;
            float var23 = -0.5F - ringPoints[var21 * 3 + 1] * var2;
            float var24 = ringPoints[var21 * 3 + 2] * var2;
            float var25 = var3 * renderFlameRing(var21, var4);
            renderFlameRing(var0, var1, var22, var23, var24, 0.15F * var2, 0.085F * var25, 16757294, 16748316, var14, var15, var17, var18, var19);
            renderFlameRing(var0, var1, var22, var23, var24, 0.0675F * var2, 0.2F * var25, 16764757, 16757294, var14, var15, var17, var18, var19);
         }
      }
   }

   private static void renderFlameRing(
      PreviewSink var0,
      Matrix4f var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      int var7,
      int var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13
   ) {
      for (int var14 = 0; var14 < flameProfile.length - 1; var14++) {
         float var15 = flameProfile[var14] * var5;
         float var16 = flameProfile[var14 + 1] * var5;
         int var17 = packColor(var7, var8, flameProfile[var14], var6 * flameAlphas[var14]);
         int var18 = packColor(var7, var8, flameProfile[var14 + 1], var6 * flameAlphas[var14 + 1]);

         for (int var19 = 0; var19 < 12; var19++) {
            flameVertex(var0, var1, var2, var3, var4, var15, var19, var9, var10, var11, var12, var13, var17);
            flameVertex(var0, var1, var2, var3, var4, var16, var19, var9, var10, var11, var12, var13, var18);
            flameVertex(var0, var1, var2, var3, var4, var16, var19 + 1, var9, var10, var11, var12, var13, var18);
            flameVertex(var0, var1, var2, var3, var4, var15, var19 + 1, var9, var10, var11, var12, var13, var17);
         }
      }
   }

   private static void flameVertex(
      PreviewSink var0,
      Matrix4f var1,
      float var2,
      float var3,
      float var4,
      float var5,
      int var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      int var12
   ) {
      float var13 = circleCos[var6] * var5;
      float var14 = circleSin[var6] * var5;
      var0.modelVertex(var1, var2 + var7 * var13 + var9 * var14, var3 + var10 * var14, var4 + var8 * var13 + var11 * var14, var12);
   }

   private static int packColor(int var0, int var1, float var2, float var3) {
      int var4 = Math.clamp((int)(var3 * 255.0F), 0, 255);
      return var4 << 24 | ColorUtil.lerpColor(var0, var1, var2) & 16777215;
   }

   static {
      for (int var0 = 0; var0 <= 96; var0++) {
         double var1 = var0 * Math.PI * 2.0 / 96.0;
         float var3 = (float)Math.sin(var1);
         float var4 = (float)(-Math.cos(var1));
         float var5 = var3 * 0.262F;
         float var6 = var4 * 0.262F;
         float var7 = 0.155F - var6 * tILT_SLOPE;
         ringPoints[var0 * 3] = var5;
         ringPoints[var0 * 3 + 1] = var7;
         ringPoints[var0 * 3 + 2] = var6 + 0.012F;
         float var8 = (float)Math.cos(var1);
         float var9 = (float)Math.sin(var1);
         float var10 = -var9 * tILT_SLOPE;
         float var11 = (float)Math.sqrt(var8 * var8 + var10 * var10 + var9 * var9);
         ringTangents[var0 * 3] = var8 / var11;
         ringTangents[var0 * 3 + 1] = var10 / var11;
         ringTangents[var0 * 3 + 2] = var9 / var11;

         for (int var12 = 0; var12 <= 10; var12++) {
            double var13 = var12 * Math.PI * 2.0 / 10.0;
            float var15 = (float)Math.cos(var13) * 0.0115F;
            float var16 = (float)Math.sin(var13) * 0.0115F;
            int var17 = (var0 * 11 + var12) * 3;
            torusVertices[var17] = var5 + var3 * var15;
            torusVertices[var17 + 1] = var7 + var16;
            torusVertices[var17 + 2] = var6 + 0.012F + var4 * var15;
            torusColors[var17 / 3] = ColorUtil.lerpColor(16765034, 16776168, 0.5 + 0.5 * Math.sin(var13));
         }
      }

      for (int var18 = 0; var18 <= 12; var18++) {
         double var19 = var18 * Math.PI * 2.0 / 12.0;
         circleCos[var18] = (float)Math.cos(var19);
         circleSin[var18] = (float)Math.sin(var19);
      }
   }
}
