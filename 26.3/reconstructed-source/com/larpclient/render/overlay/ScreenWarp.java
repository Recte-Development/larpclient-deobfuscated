package com.larpclient.render.overlay;

import com.larpclient.mixin.GuiGraphicsExtractorMixin;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class ScreenWarp {
   public static final ScreenRectangle eMPTY_RECT = new ScreenRectangle(0, 0, 1, 1);
   private static final double oFFSCREEN = -10000.0;
   private static ScreenWarp.Warp currentWarp;
   private static Object currentOwner;
   private static ScreenWarp.Warp hoverWarp;
   private static boolean hoverTracking;
   private static int warpDepth;
   private static int hoverPass = -1;
   private static final double[] currentBounds = new double[4];
   private static final double[] lastBounds = new double[4];
   private static boolean hasBounds;
   private static boolean hasLastBounds;
   private static Object lastOwner;
   private static double scale = 1.0;
   private static double originX;
   private static double originY;
   private static final ScreenWarp.WarpVertexConsumer vertexConsumer = new ScreenWarp.WarpVertexConsumer();

   private ScreenWarp() {
   }

   private static int clampToSize(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void setWarp(Object var0, ScreenWarp.Warp var1) {
      if (hasBounds && currentOwner != null) {
         System.arraycopy(currentBounds, 0, lastBounds, 0, 4);
         lastOwner = currentOwner;
         hasLastBounds = (boolean)1;
      }

      hasBounds = (boolean)0;
      if (var0 != lastOwner) {
         hasLastBounds = (boolean)0;
      }

      currentOwner = var1 == null ? null : var0;
      currentWarp = var1;
   }

   public static void warpGraphics(GuiGraphicsExtractor var0, ScreenWarp.Warp var1, boolean var2, Runnable var3) {
      ScreenWarp.WarpRenderState var4 = (ScreenWarp.WarpRenderState)((GuiGraphicsExtractorMixin)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      warpDepth++;

      try {
         var3.run();
      } finally {
         warpDepth--;
         var4.larpWarpEnd();
      }
   }

   public static boolean warpGraphics() {
      return warpDepth > 0;
   }

   public static void setViewTransform(double var0, double var2, double var4) {
      scale = Math.max(0.01, var0);
      originX = var2;
      originY = var4;
   }

   public static double[] setViewTransform(Object var0) {
      return hasLastBounds && var0 == lastOwner ? (double[])lastBounds.clone() : null;
   }

   public static ScreenWarp.Warp getWarp(Object var0) {
      return var0 != null && var0 == currentOwner ? currentWarp : null;
   }

   public static void warpGraphics(int var0, ScreenWarp.Warp var1, boolean var2) {
      hoverPass = var1 == null ? -1 : var0;
      hoverWarp = var1;
      hoverTracking = var2;
   }

   public static ScreenWarp.Warp warpGraphics() {
      return hoverPass >= 0 ? hoverWarp : null;
   }

   private static void trackVertex(float var0, float var1) {
      if (hoverTracking && hoverPass >= 0 && hoverPass <= 1) {
         double var2 = originX + (var0 - originX) / scale;
         double var4 = originY + (var1 - originY) / scale;
         if (!hasBounds) {
            currentBounds[0] = currentBounds[2] = var2;
            currentBounds[1] = currentBounds[3] = var4;
            hasBounds = (boolean)1;
         } else {
            currentBounds[0] = Math.min(currentBounds[0], var2);
            currentBounds[1] = Math.min(currentBounds[1], var4);
            currentBounds[2] = Math.max(currentBounds[2], var2);
            currentBounds[3] = Math.max(currentBounds[3], var4);
         }
      }
   }

   public static ScreenWarp.WarpVertexConsumer wrapConsumer(VertexConsumer var0, ScreenWarp.Warp var1, boolean var2) {
      return wrapConsumerClipped(var0, var1, var2, null, ScreenWarp.Mode.SUBDIVIDED);
   }

   public static ScreenWarp.WarpVertexConsumer wrapConsumerClipped(
      VertexConsumer var0, ScreenWarp.Warp var1, boolean var2, ScreenRectangle var3, ScreenWarp.Mode var4
   ) {
      return vertexConsumer.begin(var0, var1, var2, var1.clipsFlat() && var4 != ScreenWarp.Mode.SINGLE_QUAD ? var3 : null, var4);
   }

   public static final class HomographyWarp implements ScreenWarp.Warp {
      public static final double mIN_W = 0.02;
      private final double m00;
      private final double m01;
      private final double m02;
      private final double m10;
      private final double m11;
      private final double m12;
      private final double m20;
      private final double m21;
      private final double m22;
      private final double inv00;
      private final double inv01;
      private final double inv02;
      private final double inv10;
      private final double inv11;
      private final double inv12;
      private final double inv20;
      private final double inv21;
      private final double inv22;

      public HomographyWarp(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.m00 = var1;
         this.m01 = var3;
         this.m02 = var5;
         this.m10 = var7;
         this.m11 = var9;
         this.m12 = var11;
         this.m20 = var13;
         this.m21 = var15;
         this.m22 = var17;
         this.inv00 = var9 * var17 - var11 * var15;
         this.inv01 = var5 * var15 - var3 * var17;
         this.inv02 = var3 * var11 - var5 * var9;
         this.inv10 = var11 * var13 - var7 * var17;
         this.inv11 = var1 * var17 - var5 * var13;
         this.inv12 = var5 * var7 - var1 * var11;
         this.inv20 = var7 * var15 - var9 * var13;
         this.inv21 = var3 * var13 - var1 * var15;
         this.inv22 = var1 * var9 - var3 * var7;
      }

      public double depth(double var1, double var3) {
         return this.m20 * var1 + this.m21 * var3 + this.m22;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.depth(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.depth(var1, var3));
         return new double[]{(this.m00 * var1 + this.m01 * var3 + this.m02) / var5, (this.m10 * var1 + this.m11 * var3 + this.m12) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.inv00 * var1 + this.inv01 * var3 + this.inv02;
         double var7 = this.inv10 * var1 + this.inv11 * var3 + this.inv12;
         double var9 = this.inv20 * var1 + this.inv21 * var3 + this.inv22;
         if (Math.abs(var9) < 1.0E-12) {
            return new double[]{-10000.0, -10000.0};
         }

         double var11 = var5 / var9;
         double var13 = var7 / var9;
         return Double.isFinite(var11) && Double.isFinite(var13) && this.front(var11, var13) ? new double[]{var11, var13} : new double[]{-10000.0, -10000.0};
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[][] var9 = new double[][]{{var1, var3}, {var1, var7}, {var5, var7}, {var5, var3}};
         double var10 = Double.POSITIVE_INFINITY;
         double var12 = var10;
         double var14 = Double.NEGATIVE_INFINITY;
         double var16 = var14;
         byte var18 = 0;

         for (int var19 = 0; var19 < 4; var19++) {
            double[] var20 = var9[var19];
            double[] var21 = var9[var19 + 1 & 3];
            double var22 = this.depth(var20[0], var20[1]) - 0.02;
            double var24 = this.depth(var21[0], var21[1]) - 0.02;
            if (var22 >= 0.0) {
               double[] var26 = this.forward(var20[0], var20[1]);
               var18 = 1;
               var10 = Math.min(var10, var26[0]);
               var12 = Math.min(var12, var26[1]);
               var14 = Math.max(var14, var26[0]);
               var16 = Math.max(var16, var26[1]);
            }

            if ((var22 >= 0.0 ? 1 : 0) != (var24 >= 0.0 ? 1 : 0)) {
               double var29 = var22 / (var22 - var24);
               double[] var28 = this.forward(var20[0] + (var21[0] - var20[0]) * var29, var20[1] + (var21[1] - var20[1]) * var29);
               var18 = 1;
               var10 = Math.min(var10, var28[0]);
               var12 = Math.min(var12, var28[1]);
               var14 = Math.max(var14, var28[0]);
               var16 = Math.max(var16, var28[1]);
            }
         }

         return var18 != 0 ? new double[]{var10, var12, var14, var16} : new double[]{-10000.0, -10000.0, -10000.0, -10000.0};
      }
   }

   public enum Mode {
      SUBDIVIDED,
      SINGLE_QUAD,
      PACKED_UV1;
   }

   public record PerspectiveWarp(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal)
      implements ScreenWarp.Warp {
      public static ScreenWarp.PerspectiveWarp fromAngle(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new ScreenWarp.PerspectiveWarp(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = (var1 - this.pivotX) * this.scale;
         double var7 = (var3 - this.pivotY) * this.scale;
         double var9 = this.focal / Math.max(this.focal * 0.2, this.focal + var5 * this.sin);
         return new double[]{this.targetX + var5 * this.cos * var9, this.targetY + var7 * var9};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = var1 - this.targetX;
         double var7 = var3 - this.targetY;
         double var9 = this.focal * this.cos - var5 * this.sin;
         if (var9 <= this.focal * 0.001) {
            return new double[]{-10000.0, -10000.0};
         }

         double var11 = var5 * this.focal / var9;
         double var13 = this.focal / Math.max(this.focal * 0.2, this.focal + var11 * this.sin);
         return new double[]{this.pivotX + var11 / this.scale, this.pivotY + var7 / var13 / this.scale};
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[] var9 = this.forward(var1, var3);
         double[] var10 = this.forward(var1, var7);
         double[] var11 = this.forward(var5, var7);
         double[] var12 = this.forward(var5, var3);
         return new double[]{
            Math.min(Math.min(var9[0], var10[0]), Math.min(var11[0], var12[0])),
            Math.min(Math.min(var9[1], var10[1]), Math.min(var11[1], var12[1])),
            Math.max(Math.max(var9[0], var10[0]), Math.max(var11[0], var12[0])),
            Math.max(Math.max(var9[1], var10[1]), Math.max(var11[1], var12[1]))
         };
      }
   }

   public interface Warp {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRectangle screenBounds(ScreenRectangle var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.left(), var1.top(), var1.right(), var1.bottom());
         int var5 = ScreenWarp.clampToSize(Math.floor(var4[0]), var2);
         int var6 = ScreenWarp.clampToSize(Math.floor(var4[1]), var3);
         int var7 = ScreenWarp.clampToSize(Math.ceil(var4[2]), var2);
         int var8 = ScreenWarp.clampToSize(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new ScreenRectangle(var5, var6, var7 - var5, var8 - var6) : ScreenWarp.eMPTY_RECT;
      }
   }

   public interface WarpRenderState {
      void larpWarpBegin(ScreenWarp.Warp var1, boolean var2);

      void larpWarpEnd();
   }

   public static final class WarpVertexConsumer implements VertexConsumer {
      private static final float mAX_SEGMENT_SIZE = 12.0F;
      private static final int hAS_COLOR = 1;
      private static final int hAS_UV = 2;
      private static final int hAS_UV1 = 4;
      private static final int hAS_UV2 = 8;
      private static final int hAS_NORMAL = 16;
      private static final int hAS_LINE_WIDTH = 32;
      private VertexConsumer delegate;
      private ScreenWarp.Warp warp;
      private ScreenRectangle clip;
      private boolean buffering;
      private boolean singleQuad;
      private boolean packUv1;
      private int vertexIndex = -1;
      private int attributeMask;
      private int packedU;
      private int packedV;
      private final float[] xs = new float[4];
      private final float[] ys = new float[4];
      private final float[] zs = new float[4];
      private final float[] us = new float[4];
      private final float[] vs = new float[4];
      private final int[] colors = new int[4];
      private final int[] uv1Us = new int[4];
      private final int[] uv1Vs = new int[4];
      private final int[] uv2Us = new int[4];
      private final int[] uv2Vs = new int[4];
      private float normalX;
      private float normalY;
      private float normalZ;
      private float lineWidth;

      private ScreenWarp.WarpVertexConsumer begin(VertexConsumer var1, ScreenWarp.Warp var2, boolean var3, ScreenRectangle var4, ScreenWarp.Mode var5) {
         this.delegate = var1;
         this.warp = var2;
         this.buffering = var3;
         this.clip = var4;
         this.singleQuad = (boolean)(var5 == ScreenWarp.Mode.SINGLE_QUAD ? 1 : 0);
         this.packUv1 = (boolean)(var5 == ScreenWarp.Mode.PACKED_UV1 ? 1 : 0);
         this.vertexIndex = -1;
         this.attributeMask = 0;
         return this;
      }

      public void interpolate() {
         if (this.vertexIndex == 3) {
            this.emitQuad();
         } else {
            for (int var1 = 0; var1 <= this.vertexIndex; var1++) {
               this.emitVertex(var1);
            }
         }

         this.vertexIndex = -1;
         this.attributeMask = 0;
         this.delegate = null;
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         ScreenWarp.trackVertex(var1, var2);
         if (!this.buffering) {
            double[] var4 = this.warp.forward(var1, var2);
            this.delegate.addVertex((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.vertexIndex == 3) {
            this.emitQuad();
            this.vertexIndex = -1;
            this.attributeMask = 0;
         }

         this.vertexIndex++;
         this.xs[this.vertexIndex] = var1;
         this.ys[this.vertexIndex] = var2;
         this.zs[this.vertexIndex] = var3;
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public VertexConsumer setColor(int var1) {
         if (!this.buffering) {
            this.delegate.setColor(var1);
            return this;
         } else {
            this.colors[this.vertexIndex] = var1;
            this.attributeMask |= 1;
            return this;
         }
      }

      public VertexConsumer setUv(float var1, float var2) {
         if (!this.buffering) {
            this.delegate.setUv(var1, var2);
            return this;
         } else {
            this.us[this.vertexIndex] = var1;
            this.vs[this.vertexIndex] = var2;
            this.attributeMask |= 2;
            return this;
         }
      }

      public VertexConsumer setUv1(int var1, int var2) {
         if (!this.buffering) {
            this.delegate.setUv1(var1, var2);
            return this;
         } else {
            this.uv1Us[this.vertexIndex] = var1;
            this.uv1Vs[this.vertexIndex] = var2;
            this.attributeMask |= 4;
            return this;
         }
      }

      public VertexConsumer setUv3(float var1, float var2) {
         if (!this.buffering) {
            this.delegate.setUv3(var1, var2);
         }

         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         if (!this.buffering) {
            this.delegate.setUv2(var1, var2);
            return this;
         } else {
            this.uv2Us[this.vertexIndex] = var1;
            this.uv2Vs[this.vertexIndex] = var2;
            this.attributeMask |= 8;
            return this;
         }
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         if (!this.buffering) {
            this.delegate.setNormal(var1, var2, var3);
            return this;
         } else {
            this.normalX = var1;
            this.normalY = var2;
            this.normalZ = var3;
            this.attributeMask |= 16;
            return this;
         }
      }

      public VertexConsumer setLineWidth(float var1) {
         if (!this.buffering) {
            this.delegate.setLineWidth(var1);
            return this;
         } else {
            this.lineWidth = var1;
            this.attributeMask |= 32;
            return this;
         }
      }

      private void emitVertex(int var1) {
         double[] var2 = this.warp.forward(this.xs[var1], this.ys[var1]);
         this.delegate.addVertex((float)var2[0], (float)var2[1], this.zs[var1]);
         if ((this.attributeMask & 1) != 0) {
            this.delegate.setColor(this.colors[var1]);
         }

         if ((this.attributeMask & 2) != 0) {
            this.delegate.setUv(this.us[var1], this.vs[var1]);
         }

         if ((this.attributeMask & 4) != 0) {
            this.delegate.setUv1(this.uv1Us[var1], this.uv1Vs[var1]);
         }

         if ((this.attributeMask & 8) != 0) {
            this.delegate.setUv2(this.uv2Us[var1], this.uv2Vs[var1]);
         }

         if ((this.attributeMask & 16) != 0) {
            this.delegate.setNormal(this.normalX, this.normalY, this.normalZ);
         }

         if ((this.attributeMask & 32) != 0) {
            this.delegate.setLineWidth(this.lineWidth);
         }

         if (this.packUv1) {
            this.delegate.setUv1(this.packedU, this.packedV);
         }
      }

      private void emitQuad() {
         if (this.packUv1) {
            this.packedU = Math.clamp(Math.round((Math.abs(this.us[0]) - this.zs[0]) * 64.0F), 0, 32767);
            this.packedV = Math.clamp(Math.round((Math.abs(this.vs[0]) - this.zs[0]) * 64.0F), 0, 32767);
         }

         if (this.singleQuad) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.warp.front(this.xs[var14], this.ys[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.emitVertex(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.clip != null) {
               float[] var5;
               float[] var6;
               if (this.xs[0] == this.xs[1] && this.xs[3] == this.xs[2] && this.ys[0] == this.ys[3] && this.ys[1] == this.ys[2]) {
                  var5 = clipRange(this.xs[0], this.xs[3], this.clip.left(), this.clip.right());
                  var6 = clipRange(this.ys[0], this.ys[1], this.clip.top(), this.clip.bottom());
               } else if (this.ys[0] == this.ys[1] && this.ys[3] == this.ys[2] && this.xs[0] == this.xs[3] && this.xs[1] == this.xs[2]) {
                  var5 = clipRange(this.ys[0], this.ys[3], this.clip.top(), this.clip.bottom());
                  var6 = clipRange(this.xs[0], this.xs[1], this.clip.left(), this.clip.right());
               } else {
                  var5 = var6 = new float[]{0.0F, 1.0F};
               }

               if (var5 == null || var6 == null) {
                  return;
               }

               var1 = var5[0];
               var2 = var5[1];
               var3 = var6[0];
               var4 = var6[1];
            }

            int var16 = var1 == 0.0F && var2 == 1.0F && var3 == 0.0F && var4 == 1.0F ? 1 : 0;
            float var17 = 0.0F;

            for (int var7 = 0; var7 < 4; var7++) {
               int var8 = var7 + 1 & 3;
               var17 = Math.max(var17, (float)Math.hypot(this.xs[var8] - this.xs[var7], this.ys[var8] - this.ys[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.warp.front(this.xs[var21], this.ys[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.emitVertex(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.isFront(var9, var12) && this.isFront(var9, var13) && this.isFront(var10, var13) && this.isFront(var10, var12)) {
                        this.emitInterpolated(var9, var12);
                        this.emitInterpolated(var9, var13);
                        this.emitInterpolated(var10, var13);
                        this.emitInterpolated(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] clipRange(float var0, float var1, int var2, int var3) {
         if (var0 != var1) {
            float var4 = (var2 - var0) / (var1 - var0);
            float var5 = (var3 - var0) / (var1 - var0);
            float var6 = Math.max(0.0F, Math.min(var4, var5));
            float var7 = Math.min(1.0F, Math.max(var4, var5));
            return var7 > var6 ? new float[]{var6, var7} : null;
         } else {
            return var0 >= var2 && var0 <= var3 ? new float[]{0.0F, 1.0F} : null;
         }
      }

      private boolean isFront(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.warp.front(interpolate(this.xs, var3, var4, var5, var6), interpolate(this.ys, var3, var4, var5, var6));
      }

      private void emitInterpolated(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.warp.forward(interpolate(this.xs, var3, var4, var5, var6), interpolate(this.ys, var3, var4, var5, var6));
         this.delegate.addVertex((float)var7[0], (float)var7[1], interpolate(this.zs, var3, var4, var5, var6));
         if ((this.attributeMask & 1) != 0) {
            this.delegate.setColor(this.blendColor(var3, var4, var5, var6));
         }

         if ((this.attributeMask & 2) != 0) {
            this.delegate.setUv(interpolate(this.us, var3, var4, var5, var6), interpolate(this.vs, var3, var4, var5, var6));
         }

         if ((this.attributeMask & 4) != 0) {
            this.delegate
               .setUv1(Math.round(interpolateInt(this.uv1Us, var3, var4, var5, var6)), Math.round(interpolateInt(this.uv1Vs, var3, var4, var5, var6)));
         }

         if ((this.attributeMask & 8) != 0) {
            this.delegate
               .setUv2(Math.round(interpolateInt(this.uv2Us, var3, var4, var5, var6)), Math.round(interpolateInt(this.uv2Vs, var3, var4, var5, var6)));
         }

         if ((this.attributeMask & 16) != 0) {
            this.delegate.setNormal(this.normalX, this.normalY, this.normalZ);
         }

         if ((this.attributeMask & 32) != 0) {
            this.delegate.setLineWidth(this.lineWidth);
         }

         if (this.packUv1) {
            this.delegate.setUv1(this.packedU, this.packedV);
         }
      }

      private static float interpolate(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float interpolateInt(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int blendColor(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (byte var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (this.colors[0] >>> var6 & 0xFF) * var1
               + (this.colors[1] >>> var6 & 0xFF) * var2
               + (this.colors[2] >>> var6 & 0xFF) * var3
               + (this.colors[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp(Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }
}
