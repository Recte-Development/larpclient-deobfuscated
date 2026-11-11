package com.larpclient.render.model;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.PreviewSink;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class WingGeometry {
   public static final float wING_THICKNESS = 0.12F;
   public static final int pOINT_COUNT = 73;
   private static final int[] fINGER_POINT_INDICES = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] fINGER_BULGE = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<WingGeometry.WingPoint> bASE_POINTS = buildBasePoints();
   public static final List<WingGeometry.MembraneQuad> mEMBRANE_QUADS;
   public static final List<WingGeometry.BoneSegment> bONE_SEGMENTS = List.of(
      new WingGeometry.BoneSegment(0, 1, 0.028F, true),
      new WingGeometry.BoneSegment(1, 2, 0.024F, true),
      new WingGeometry.BoneSegment(2, 3, 0.019F, true),
      new WingGeometry.BoneSegment(3, 4, 0.012F, false),
      new WingGeometry.BoneSegment(2, 19, 0.013F, false),
      new WingGeometry.BoneSegment(19, 7, 0.008F, false),
      new WingGeometry.BoneSegment(2, 22, 0.012F, false),
      new WingGeometry.BoneSegment(22, 10, 0.007F, false),
      new WingGeometry.BoneSegment(2, 25, 0.01F, false),
      new WingGeometry.BoneSegment(25, 13, 0.005F, false)
   );
   public static final float aNCHOR_Y = 1.38F;
   public static final float aNCHOR_Z = 0.16F;
   public static final float mAX_SCALE = 2.0F;

   private static List<WingGeometry.WingPoint> buildBasePoints() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.21F, 0.29F, -0.015F),
            new WingGeometry.WingPoint(0.48F, 0.47F, 0.045F),
            new WingGeometry.WingPoint(1.14F, 0.49F, 0.05F),
            new WingGeometry.WingPoint(1.52F, 0.12F, 0.08F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(1.18F, -0.45F, 0.06F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.68F, -0.55F, 0.03F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.1F, -0.7F, 0.005F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F),
            new WingGeometry.WingPoint(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         WingGeometry.WingPoint var2 = (WingGeometry.WingPoint)var0.get(fINGER_POINT_INDICES[var1 * 3]);
         WingGeometry.WingPoint var3 = (WingGeometry.WingPoint)var0.get(fINGER_POINT_INDICES[var1 * 3 + 3]);
         WingGeometry.WingPoint var4 = lerp(lerp(var2, var3, 0.5F), (WingGeometry.WingPoint)var0.get(2), fINGER_BULGE[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = var5 / 3.0F;
            var0.set(fINGER_POINT_INDICES[var1 * 3 + var5], lerp(lerp(var2, var4, var6), lerp(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static WingGeometry.WingPose poseFromFlags(boolean var0, boolean var1, float var2, float var3) {
      return poseFromBlend(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static WingGeometry.WingPose poseFromBlend(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new WingGeometry.WingPose(
         0.34F * var5,
         0.46F * var5,
         (1.38F - 0.42F * var4) * (1.0F - var5),
         0.16F * (1.0F - var5),
         18.0F * var4 * (1.0F - var5) + var6 * var5,
         1.0F - 0.24F * var5,
         1.0F - 0.08F * var5,
         7.0F * (1.0F - var5) + 2.0F * var5,
         11.0F * (1.0F - var5) + 1.5F * var5,
         0.09F,
         0.025F,
         -6.0F - 3.0F * var4,
         -3.0F,
         0.12F
      );
   }

   public static Matrix4f entityWingMatrix(WingGeometry.WingPose var0, float var1, float var2, float var3, float var4, int var5) {
      return applyFlapTransform(
         new Matrix4f()
            .rotateY(toRadians(180.0F - var1))
            .translate(0.0F, var0.preY, var0.preZ)
            .rotateX(toRadians(var0.pitch))
            .translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f chestWingMatrix(WingGeometry.WingPose var0, float var1, float var2, float var3, int var4, boolean var5) {
      return applyFlapTransform(
         new Matrix4f()
            .translate(0.0F, var5 ? 0.125F : 0.0F, 0.0F)
            .translate(0.0F, 1.501F, 0.0F)
            .scale(1.0666667F)
            .scale(-1.0F, -1.0F, 1.0F)
            .translate(0.0F, 1.38F, 0.16F),
         var0,
         var1,
         var2,
         var3,
         var4
      );
   }

   private static Matrix4f applyFlapTransform(Matrix4f var0, WingGeometry.WingPose var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin(var6) + 0.16 * Math.sin(var6 * 2.0F)) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate(var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(toRadians(-var5 * var8))
         .rotateZ(toRadians(var5 * (var1.splay + 3.5F * (float)Math.cos(var6))))
         .rotateX(toRadians(var1.wingPitch));
   }

   public static WingGeometry.AnimatedWing animatePoints(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(bASE_POINTS);
      WingGeometry.WingPoint var5 = (WingGeometry.WingPoint)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin(var6 - 0.7F);

      for (int var8 = 0; var8 < 13; var8++) {
         WingGeometry.WingPoint var9 = lerp(var5, (WingGeometry.WingPoint)var4.get(fINGER_POINT_INDICES[var8]), 0.5F);
         var4.add(new WingGeometry.WingPoint(var9.posX, var9.posY, var9.posZ + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = toRadians(8.0F + 7.0F * (float)Math.sin(var6 - 0.65F) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = toRadians(-4.0F * var2 + 2.0F * (float)Math.sin(var6 - 0.95F));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            WingGeometry.WingPoint var12 = (WingGeometry.WingPoint)var4.get(var11);
            float var13 = Math.clamp(var12.posX / 0.48F, 0.0F, 1.0F);
            float var14 = var21 * var13 * var10;
            float var15 = var22 * var13 * var10;
            float var16 = var12.posX - var5.posX;
            float var17 = var12.posY - var5.posY;
            float var18 = var12.posZ - var5.posZ;
            float var19 = var16 * (float)Math.cos(var14) + var18 * (float)Math.sin(var14);
            float var20 = -var16 * (float)Math.sin(var14) + var18 * (float)Math.cos(var14);
            var4.set(
               var11,
               new WingGeometry.WingPoint(
                  var5.posX + var19 * (float)Math.cos(var15) - var17 * (float)Math.sin(var15),
                  var5.posY + var19 * (float)Math.sin(var15) + var17 * (float)Math.cos(var15),
                  var5.posZ + var20
               )
            );
         }
      }

      return new WingGeometry.AnimatedWing(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin(var6 - 0.25F));
   }

   public static Vec3 animatePoints(Vec3 var0, Matrix4f var1, int var2, WingGeometry.WingPoint var3) {
      float var4 = var2 * var3.posX;
      float var5 = var3.posY;
      float var6 = var3.posZ;
      return var0.add(
         var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30(),
         var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31(),
         var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32()
      );
   }

   public static WingGeometry.WingInstance buildWing(Vec3 var0, Matrix4f var1, int var2, WingGeometry.AnimatedWing var3, int var4, int var5, float var6) {
      return new WingGeometry.WingInstance(
         var3.points.stream().map(var3x -> animatePoints(var0, var1, var2, var3x)).toList(),
         new Vec3(var1.m20(), var1.m21(), var1.m22()).normalize(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int membraneColor(WingGeometry.WingInstance var0, int var1) {
      float var2 = var1 < bASE_POINTS.size() ? bASE_POINTS.get(var1).posX / 1.52F : 0.4F;
      int var3 = ColorUtil.lerpColor(var0.primary, var0.secondary, Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= bASE_POINTS.size() && (var1 - bASE_POINTS.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= bASE_POINTS.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= bASE_POINTS.size() ? 212 : (var1 < 3 ? 225 : 145);
      return poseFromBlend(ColorUtil.lerpColor(-15526620, var3, var5), Math.round(var7 * (var3 >>> 24) / 255.0F));
   }

   public static int boneColor(WingGeometry.WingInstance var0, int var1, boolean var2) {
      double var3 = var1 >= bASE_POINTS.size() ? 0.4 : Math.clamp(bASE_POINTS.get(var1).posX / 1.52F, 0.0F, 1.0F);
      int var5 = ColorUtil.lerpColor(var0.primary, var0.secondary, var3);
      return poseFromBlend(ColorUtil.lerpColor(var5, -1, var2 ? 0.22 : 0.38), Math.round((var2 ? 88 : 215) * var0.pulse * (var5 >>> 24) / 255.0F));
   }

   public static boolean membraneColor(WingGeometry.WingInstance var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void renderWing(PreviewSink var0, WingGeometry.WingInstance var1, WingGeometry.RenderMode var2) {
      List var3 = var1.points;
      if (var2 != WingGeometry.RenderMode.FILL) {
         var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
         double var9 = (var2 == WingGeometry.RenderMode.GLOW ? 0.047 : 0.008) * var1.size;

         for (int var11 = 0; var11 < bASE_POINTS.size(); var11++) {
            var0.ribbon(
               (Vec3)var3.get(var11),
               (Vec3)var3.get((var11 + 1) % bASE_POINTS.size()),
               var9,
               boneColor(var1, var11, var2 == WingGeometry.RenderMode.GLOW),
               boneColor(var1, (var11 + 1) % bASE_POINTS.size(), var2 == WingGeometry.RenderMode.GLOW)
            );
         }
      } else {
         var0.blend(HitEffectConfig.BlendMode.NORMAL);

         for (WingGeometry.MembraneQuad var5 : mEMBRANE_QUADS) {
            addVertex(var0, (Vec3)var3.get(var5.indexA), membraneColor(var1, var5.indexA));
            addVertex(var0, (Vec3)var3.get(var5.indexB), membraneColor(var1, var5.indexB));
            addVertex(var0, (Vec3)var3.get(var5.indexC), membraneColor(var1, var5.indexC));
            addVertex(var0, (Vec3)var3.get(var5.indexD), membraneColor(var1, var5.indexD));
         }

         for (WingGeometry.BoneSegment var10 : bONE_SEGMENTS) {
            int var6 = boneColor(var1, var10.startIndex, false);
            int var7 = boneColor(var1, var10.endIndex, false);
            var0.ribbon((Vec3)var3.get(var10.startIndex), (Vec3)var3.get(var10.endIndex), var10.width * var1.size, var6, var7);
            if (var10.ridge) {
               addRidge(var0, (Vec3)var3.get(var10.startIndex), (Vec3)var3.get(var10.endIndex), var1.normal, var10.width * var1.size, var6, var7);
            }
         }

         buildBasePoints(var0, (Vec3)var3.get(1), (Vec3)var3.get(0), (Vec3)var3.get(2), var1.normal, 0.045 * var1.size, boneColor(var1, 1, false));
         buildBasePoints(var0, (Vec3)var3.get(2), (Vec3)var3.get(1), (Vec3)var3.get(3), var1.normal, 0.036 * var1.size, boneColor(var1, 2, false));
      }
   }

   public static void renderOutline(EffectsRenderer var0, WingGeometry.WingInstance var1) {
      if (membraneColor(var1)) {
         var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
         List var2 = var1.points;

         for (int var3 = 0; var3 < bASE_POINTS.size(); var3++) {
            var0.ribbon(
               (Vec3)var2.get(var3),
               (Vec3)var2.get((var3 + 1) % bASE_POINTS.size()),
               0.016 * var1.size,
               boneColor(var1, var3, false),
               boneColor(var1, (var3 + 1) % bASE_POINTS.size(), false)
            );
         }

         for (WingGeometry.BoneSegment var4 : bONE_SEGMENTS) {
            if (var4.startIndex == 2) {
               var0.ribbon(
                  (Vec3)var2.get(var4.startIndex),
                  (Vec3)var2.get(var4.endIndex),
                  0.01 * var1.size,
                  boneColor(var1, var4.startIndex, false),
                  boneColor(var1, var4.endIndex, false)
               );
            }
         }
      }
   }

   private static void addRidge(PreviewSink var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, int var6, int var7) {
      Vec3 var8 = var3.scale(var4 * 0.4);
      addVertex(var0, var1.add(var8), ColorUtil.multiplyAlpha(var6, 0.72));
      addVertex(var0, var2.add(var8), ColorUtil.multiplyAlpha(var7, 0.72));
      addVertex(var0, var2.subtract(var8), ColorUtil.multiplyAlpha(var7, 0.45));
      addVertex(var0, var1.subtract(var8), ColorUtil.multiplyAlpha(var6, 0.45));
   }

   private static void buildBasePoints(PreviewSink var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, double var5, int var7) {
      Vec3 var8 = var3.subtract(var2).normalize().scale(var5);
      Vec3 var9 = var8.cross(var4).normalize().scale(var5);
      addVertex(var0, var1.add(var8), var7);
      addVertex(var0, var1.add(var9), ColorUtil.multiplyAlpha(var7, 0.75));
      addVertex(var0, var1.subtract(var8), ColorUtil.multiplyAlpha(var7, 0.55));
      addVertex(var0, var1.subtract(var9), var7);
   }

   private static WingGeometry.WingPoint lerp(WingGeometry.WingPoint var0, WingGeometry.WingPoint var1, float var2) {
      return new WingGeometry.WingPoint(
         var0.posX + (var1.posX - var0.posX) * var2, var0.posY + (var1.posY - var0.posY) * var2, var0.posZ + (var1.posZ - var0.posZ) * var2
      );
   }

   private static void addVertex(PreviewSink var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int poseFromBlend(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float toRadians(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private WingGeometry() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new WingGeometry.MembraneQuad(2, 16 + var1, 17 + var1, 2));
         var0.add(new WingGeometry.MembraneQuad(16 + var1, fINGER_POINT_INDICES[var1], fINGER_POINT_INDICES[var1 + 1], 17 + var1));
      }

      var0.add(new WingGeometry.MembraneQuad(0, 1, 2, 28));
      var0.add(new WingGeometry.MembraneQuad(2, 3, 4, 16));
      mEMBRANE_QUADS = List.copyOf(var0);
   }

   public record AnimatedWing(List<WingGeometry.WingPoint> points, float pulse) {
   }

   public record BoneSegment(int a, int b, float width, boolean ridge) {
      private final int startIndex;
      private final int endIndex;

      public BoneSegment(int a, int b, float width, boolean ridge) {
         this.startIndex = a;
         this.endIndex = b;
         this.width = width;
         this.ridge = ridge;
      }
   }

   public record MembraneQuad(int a, int b, int c, int d) {
      private final int indexA;
      private final int indexB;
      private final int indexC;
      private final int indexD;

      public MembraneQuad(int a, int b, int c, int d) {
         this.indexA = a;
         this.indexB = b;
         this.indexC = c;
         this.indexD = d;
      }
   }

   public enum RenderMode {
      GLOW,
      FILL,
      OUTLINE;
   }

   public record WingInstance(List<Vec3> points, Vec3 normal, int primary, int secondary, float size, float pulse) {
   }

   public record WingPoint(float x, float y, float z) {
      private final float posX;
      private final float posY;
      private final float posZ;

      public WingPoint(float x, float y, float z) {
         this.posX = x;
         this.posY = y;
         this.posZ = z;
      }
   }

   public record WingPose(
      float preY,
      float preZ,
      float anchorY,
      float anchorZ,
      float pitch,
      float openScale,
      float scale,
      float walkSpread,
      float amplitude,
      float hingeX,
      float hingeZ,
      float splay,
      float wingPitch,
      float frequency
   ) {
   }

   public static final class WingPreviewBuffer {
      private boolean vertexBuffer;
      private float vertexCount;
      private float indexBuffer;
      private float blendMode;
      private float colorBuffer;
      private float lineWidth;
      private float capacity;
      private float currentColor;
      private Vec3 building;

      public void beginWith(float var1, boolean var2, float var3, boolean var4, Vec3 var5) {
         this.beginWith(var1, var2, var3, var4, false, var5);
      }

      public void beginWith(float var1, boolean var2, float var3, boolean var4, boolean var5, Vec3 var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = this.vertexBuffer
               && !(var1 < this.vertexCount)
               && !(var1 - this.vertexCount > 10.0F)
               && (this.building == null || !(var6.distanceToSqr(this.building) > 64.0))
            ? 0
            : 1;
         if (var8 != 0) {
            this.indexBuffer = var2 ? 1.0F : 0.0F;
            this.blendMode = var7;
            this.colorBuffer = var4 ? 1.0F : 0.0F;
            this.lineWidth = var5 ? 1.0F : 0.0F;
            this.capacity = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.vertexCount) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp(-var9 / 0.13F);
            float var11 = 1.0F - (float)Math.exp(-var9 / 0.19F);
            this.indexBuffer = this.indexBuffer + ((var2 ? 1 : 0) - this.indexBuffer) * var10;
            this.colorBuffer = this.colorBuffer + ((var4 ? 1 : 0) - this.colorBuffer) * var10;
            this.lineWidth = this.lineWidth + ((var5 ? 1 : 0) - this.lineWidth) * var10;
            this.blendMode = this.blendMode + (var7 - this.blendMode) * var11;
            this.capacity = var9 * 20.0F;
         }

         this.vertexCount = var1;
         this.building = var6;
         this.vertexBuffer = (boolean)1;
      }

      public void end(float var1) {
         this.currentColor = (float)((this.currentColor + this.capacity * var1) % (Math.PI * 2));
      }

      public float vertex() {
         return this.indexBuffer;
      }

      public float ribbon() {
         return this.blendMode;
      }

      public float blend() {
         return this.colorBuffer;
      }

      public float flush() {
         return this.lineWidth;
      }

      public float reset() {
         return this.currentColor;
      }
   }
}
