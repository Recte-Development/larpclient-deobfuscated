package com.larpclient.render.entity;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.util.misc.PreviewSink;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class ChinaHatRenderer {
   private static final int sEGMENTS = 64;
   private static final int rINGS = 3;
   public static final List<ChinaHatRenderer.Face> faces = buildFaces();
   private static final double[] cosTable = new double[65];
   private static final double[] sinTable = new double[65];

   private static ChinaHatRenderer.Vertex createVertex(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new ChinaHatRenderer.Vertex(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<ChinaHatRenderer.Face> buildFaces() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = var1 / 3.0;
         double var4 = (var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(
               new ChinaHatRenderer.Face(
                  createVertex(var2, var6, 1.0 - var2),
                  createVertex(var4, var6, 1.0 - var4),
                  createVertex(var4, var6 + 1, 1.0 - var4),
                  createVertex(var2, var6 + 1, 1.0 - var2),
                  false
               )
            );
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(
            new ChinaHatRenderer.Face(
               createVertex(0.96, var7, -0.022), createVertex(1.0, var7, 0.0), createVertex(1.0, var7 + 1, 0.0), createVertex(0.96, var7 + 1, -0.022), true
            )
         );
      }

      return List.copyOf(var0);
   }

   public static int scaleBrightness(int var0, double var1) {
      return var0 & 0xFF000000 | (int)((var0 >>> 16 & 0xFF) * var1) << 16 | (int)((var0 >>> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public static void render(PreviewSink var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(HitEffectConfig.BlendMode.NORMAL);

      for (ChinaHatRenderer.Face var12 : faces) {
         addFaceVertex(var0, var1, var12.innerStart, var12.brim, var2, var4, var6, var8, var9);
         addFaceVertex(var0, var1, var12.outerStart, var12.brim, var2, var4, var6, var8, var9);
         addFaceVertex(var0, var1, var12.outerEnd, var12.brim, var2, var4, var6, var8, var9);
         addFaceVertex(var0, var1, var12.innerEnd, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
         drawOutline(var0, var1, var2, var6, 0.014, ColorUtil.multiplyAlpha(var9, 0.22));
         drawOutline(var0, var1, var2, var6, 0.004, ColorUtil.lerpColor(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(HitEffectConfig.BlendMode.NORMAL);
      }
   }

   private static void drawOutline(PreviewSink var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(cosTable[var10] * (var2 - var6)), var9, (float)(sinTable[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(cosTable[var10] * (var2 + var6)), var9, (float)(sinTable[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(cosTable[var10 + 1] * (var2 + var6)), var9, (float)(sinTable[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(cosTable[var10 + 1] * (var2 - var6)), var9, (float)(sinTable[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void addFaceVertex(
      PreviewSink var0, Matrix4f var1, ChinaHatRenderer.Vertex var2, boolean var3, double var4, double var6, double var8, int var10, int var11
   ) {
      int var12 = var3 ? scaleBrightness(var11, 0.58) : scaleBrightness(ColorUtil.lerpColor(var10, var11, var2.radius), var2.light);
      emitVertex(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void emitVertex(PreviewSink var0, Matrix4f var1, ChinaHatRenderer.Vertex var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x2 * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z2 * var3), var9);
   }

   private ChinaHatRenderer() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         cosTable[var0] = Math.cos(var0 * Math.PI * 2.0 / 64.0);
         sinTable[var0] = Math.sin(var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public record Face(ChinaHatRenderer.Vertex a, ChinaHatRenderer.Vertex b, ChinaHatRenderer.Vertex c, ChinaHatRenderer.Vertex d, boolean brim) {
      private final ChinaHatRenderer.Vertex innerStart;
      private final ChinaHatRenderer.Vertex outerStart;
      private final ChinaHatRenderer.Vertex outerEnd;
      private final ChinaHatRenderer.Vertex innerEnd;

      public Face(ChinaHatRenderer.Vertex a, ChinaHatRenderer.Vertex b, ChinaHatRenderer.Vertex c, ChinaHatRenderer.Vertex d, boolean brim) {
         this.innerStart = a;
         this.outerStart = b;
         this.outerEnd = c;
         this.innerEnd = d;
         this.brim = brim;
      }
   }

   public record Vertex(double x, double rise, double z, double radius, double light) {
      private final double x2;
      private final double z2;

      public Vertex(double x, double rise, double z, double radius, double light) {
         this.x2 = x;
         this.rise = rise;
         this.z2 = z;
         this.radius = radius;
         this.light = light;
      }
   }
}
