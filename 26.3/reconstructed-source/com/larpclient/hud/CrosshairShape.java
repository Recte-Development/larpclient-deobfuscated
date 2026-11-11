package com.larpclient.hud;

import com.larpclient.gui.misc.DrawingGrid;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D.Double;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CrosshairShape {
   public static final String sTYLE_DRAW_YOUR_OWN = "Draw Your Own";
   public static final List<String> sTYLES = List.of("Star of David", "67", "Draw Your Own", "Cross", "Dot", "Heart", "Star", "Crown");
   private static final List<String> oDD_THICKNESS_STYLES = List.of("Star of David", "Cross");
   private static final double dRAWING_GRID_DIVISOR = 7.0;
   public static final double oUTLINE_WIDTH = 0.65;
   public static final double mIN_SCALE_STEP = 1.0;
   public static final double mIN_SCALE = 0.5;
   public static final double mAX_SCALE = 8.0;
   private static final int sCALE_STEPS_PER_UNIT = 8;
   static final String fONT_RESOURCE = "/assets/larp-client/font/semibold.ttf";
   private static Shape sixtySevenGlyph;
   private final CrosshairShape.CrosshairGeometry geometry;
   private final double thickness;
   private final double outlineWidth;
   private final double cellSize;
   private final boolean halfPixelOffset;
   private final Area area;

   public CrosshairShape(CrosshairShape.CrosshairGeometry var1) {
      this.geometry = var1;
      double var2 = var1.scale;
      long var4 = Math.max(1L, Math.round(var1.thickness * var2));
      this.thickness = var2 >= 1.0 ? var4 / var2 : var1.thickness;
      this.outlineWidth = var1.outline ? Math.min(1.0, Math.max(1L, Math.round(0.65 * var2)) / var2) : 0.0;
      double var6 = var1.size / 7.0;
      long var8 = (long)Math.floor(var6 * var2 + 1.0E-9);
      this.cellSize = var2 >= 1.0 && var8 >= 1L ? var8 / var2 : var6;
      this.halfPixelOffset = (boolean)((!snapToPixel(var1.style) || var4 % 2L != 1L)
            && (!"Draw Your Own".equals(var1.style) || !(var2 >= 1.0) || var8 % 2L != 1L)
         ? 0
         : 1);
      this.area = this.normalizeShape();
   }

   public static boolean snapToPixel(String var0) {
      return oDD_THICKNESS_STYLES.contains(var0);
   }

   public static double clampScale(double var0) {
      return Math.clamp(Math.round(var0 * 8.0) / 8.0, 0.5, 8.0);
   }

   public static int rasterize(int var0, int var1, boolean var2) {
      return (int)Math.ceil(Math.max(var0 + var1 / 2.0, var0 / 2.0 + var1) + (var2 ? 1.0 : 0.0) + 1.0);
   }

   public static int extent(String var0, int var1, int var2, boolean var3) {
      return !"Draw Your Own".equals(var0) ? rasterize(var1, var2, var3) : (int)Math.ceil(7.5 * var1 / 7.0 * Math.sqrt(2.0) + (var3 ? 1.0 : 0.0) + 1.0);
   }

   public boolean contains(double var1, double var3) {
      return this.area.contains(var1, var3);
   }

   private double snapToPixel(double var1) {
      if (this.geometry.scale < 1.0) {
         return var1;
      }

      double var3 = this.halfPixelOffset ? 0.5 : 0.0;
      return (Math.round(var1 * this.geometry.scale - var3) + var3) / this.geometry.scale;
   }

   private Area normalizeShape() {
      double var1 = this.geometry.size;

      return switch (this.geometry.style) {
         case "Star of David" -> {
            double var16 = Math.max(var1 - this.thickness, var1 / 2.0) / 2.0;
            double var17 = Math.floor(var16 * this.geometry.scale) / this.geometry.scale;
            double var18 = !(this.geometry.scale < 1.0) && !(var17 <= 0.0) ? var17 : var16;
            double var19 = 2.0 * var18;
            double var13 = var19 * Math.sqrt(3.0) / 2.0;
            Double var15 = new Double();
            var15.moveTo(0.0, -var19);
            var15.lineTo(var13, var18);
            var15.lineTo(-var13, var18);
            var15.closePath();
            var15.moveTo(0.0, var19);
            var15.lineTo(-var13, -var18);
            var15.lineTo(var13, -var18);
            var15.closePath();
            yield new Area(new BasicStroke((float)this.thickness, 0, 0, 10.0F).createStrokedShape(var15));
         }
         case "67" -> normalizeShape(sixtySevenShape(), var1);
         case "Draw Your Own" -> this.extent();
         case "Cross" -> {
            double var5 = this.snapToPixel(Math.max(1.0, var1 * 0.3));
            double var7 = this.thickness / 2.0;
            double var9 = this.snapToPixel(var1);
            Area var11 = new Area(new java.awt.geom.Rectangle2D.Double(-var9, -var7, var9 - var5, this.thickness));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(var5, -var7, var9 - var5, this.thickness)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, var5, this.thickness, var9 - var5)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, -var9, this.thickness, var9 - var5)));
            yield var11;
         }
         case "Dot" -> crownShape(Math.max(1.0, var1 / 3.0));
         case "Heart" -> normalizeShape(heartShape(), var1);
         case "Star" -> normalizeShape(clampScale(), var1);
         case "Crown" -> normalizeShape(crownShape(), var1);
         default -> throw new IllegalStateException(this.geometry.style);
      };
   }

   private Area extent() {
      Area var1 = new Area();
      double var2 = -15.0 * this.cellSize / 2.0;

      for (int var4 = 0; var4 < 15; var4++) {
         for (int var5 = 0; var5 < 15; var5++) {
            if (this.geometry.drawing.isSet(var5, var4)) {
               int var6 = var5;

               while (var6 + 1 < 15 && this.geometry.drawing.isSet(var6 + 1, var4)) {
                  var6++;
               }

               var1.add(
                  new Area(
                     new java.awt.geom.Rectangle2D.Double(
                        var2 + var5 * this.cellSize, var2 + var4 * this.cellSize, (var6 - var5 + 1) * this.cellSize, this.cellSize
                     )
                  )
               );
               var5 = var6;
            }
         }
      }

      return var1;
   }

   private static Area crownShape(double var0) {
      return new Area(new java.awt.geom.Ellipse2D.Double(-var0, -var0, 2.0 * var0, 2.0 * var0));
   }

   private static Double polygon(double... var0) {
      Double var1 = new Double();
      var1.moveTo(var0[0], var0[1]);

      for (byte var2 = 2; var2 < var0.length; var2 += 2) {
         var1.lineTo(var0[var2], var0[var2 + 1]);
      }

      var1.closePath();
      return var1;
   }

   private static Area normalizeShape(Shape var0, double var1) {
      Rectangle2D var3 = var0.getBounds2D();
      Area var4 = new Area(var0).createTransformedArea(AffineTransform.getTranslateInstance(-var3.getCenterX(), -var3.getCenterY()));
      double var5 = 0.0;
      double[] var7 = new double[6];

      for (PathIterator var8 = var4.getPathIterator(null, 0.001); !var8.isDone(); var8.next()) {
         if (var8.currentSegment(var7) != 4) {
            var5 = Math.max(var5, Math.hypot(var7[0], var7[1]));
         }
      }

      return var4.createTransformedArea(AffineTransform.getScaleInstance(var1 / var5, var1 / var5));
   }

   private static Shape heartShape() {
      Double var0 = new Double();

      for (int var1 = 0; var1 < 160; var1++) {
         double var2 = var1 * 2 * Math.PI / 160.0;
         double var4 = Math.sin(var2);
         double var6 = 16.0 * var4 * var4 * var4;
         double var8 = -(13.0 * Math.cos(var2) - 5.0 * Math.cos(2.0 * var2) - 2.0 * Math.cos(3.0 * var2) - Math.cos(4.0 * var2));
         if (var1 == 0) {
            var0.moveTo(var6, var8);
         } else {
            var0.lineTo(var6, var8);
         }
      }

      var0.closePath();
      return var0;
   }

   private static Shape clampScale() {
      double[] var0 = new double[20];

      for (int var1 = 0; var1 < 10; var1++) {
         double var2 = (-Math.PI / 2) + var1 * Math.PI / 5.0;
         double var4 = var1 % 2 == 0 ? 1.0 : 0.46;
         var0[var1 * 2] = Math.cos(var2) * var4;
         var0[var1 * 2 + 1] = Math.sin(var2) * var4;
      }

      return polygon(var0);
   }

   private static Shape crownShape() {
      Area var0 = new Area(polygon(-0.9, 0.45, -0.9, -0.4, -0.45, 0.05, 0.0, -0.62, 0.45, 0.05, 0.9, -0.4, 0.9, 0.45));
      var0.add(new Area(new java.awt.geom.Rectangle2D.Double(-0.9, 0.55, 1.8, 0.32)));

      for (double[] var4 : new double[][]{{-0.9, -0.5}, {0.0, -0.72}, {0.9, -0.5}}) {
         var0.add(new Area(new java.awt.geom.Ellipse2D.Double(var4[0] - 0.14, var4[1] - 0.14, 0.28, 0.28)));
      }

      return var0;
   }

   static synchronized Shape sixtySevenShape() {
      if (sixtySevenGlyph != null) {
         return sixtySevenGlyph;
      }

      try (InputStream var0 = CrosshairShape.class.getResourceAsStream("/assets/larp-client/font/semibold.ttf")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing /assets/larp-client/font/semibold.ttf");
         }

         Font var1 = Font.createFont(0, var0).deriveFont(100.0F);
         return sixtySevenGlyph = var1.createGlyphVector(new FontRenderContext(null, true, true), "67").getOutline();
      } catch (IOException | FontFormatException var5) {
         throw new IllegalStateException("Could not read the 67 font", var5);
      }
   }

   public CrosshairShape.RenderedCrosshair render() {
      double var1 = this.geometry.scale;
      double var3 = (
            "Draw Your Own".equals(this.geometry.style)
               ? 15.0 * this.cellSize / 2.0
               : Math.max(this.geometry.size + this.thickness / 2.0, this.geometry.size / 2.0 + this.thickness)
         )
         + this.outlineWidth;
      int var5 = (int)Math.ceil(var3 * var1) + 2;
      int var6 = 2 * var5 + (this.halfPixelOffset ? 1 : 0);
      int var7 = var6;
      float[] var8 = this.rasterize(this.area, var6, var7);
      float[] var9 = var8;
      if (this.outlineWidth > 0.0) {
         Area var10 = new Area(this.area);
         var10.add(new Area(new BasicStroke((float)(2.0 * this.outlineWidth), 1, 1).createStrokedShape(this.area)));
         var9 = this.rasterize(var10, var6, var7);
      }

      int[] var14 = new int[var6 * var7 * 2];

      for (int var11 = 0; var11 < var6 * var7; var11++) {
         int var12 = extent(var8[var11]);
         int var13 = Math.max(var12, extent(var9[var11]));
         var14[var11] = var12 << 24 | 16777215;
         var14[var6 * var7 + var11] = var13 - var12 << 24 | 16777215;
      }

      return new CrosshairShape.RenderedCrosshair(var6, var7, var1, var14);
   }

   private static int extent(float var0) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
   }

   private float[] rasterize(Area var1, int var2, int var3) {
      AffineTransform var4 = new AffineTransform();
      var4.translate(var2 / 2.0, var3 / 2.0);
      var4.scale(this.geometry.scale, this.geometry.scale);
      PathIterator var5 = var1.getPathIterator(var4, 0.02);
      int var6 = var5.getWindingRule() == 0 ? 1 : 0;
      ArrayList var7 = new ArrayList();
      double[] var8 = new double[6];
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;

      while (!var5.isDone()) {
         switch (var5.currentSegment(var8)) {
            case 0:
               var9 = var13 = var8[0];
               var11 = var15 = var8[1];
               break;
            case 1:
               addEdge(var7, var13, var15, var8[0], var8[1]);
               var13 = var8[0];
               var15 = var8[1];
               break;
            case 2:
            case 3:
            default:
               throw new IllegalStateException("Unflattened crosshair segment");
            case 4:
               addEdge(var7, var13, var15, var9, var11);
               var13 = var9;
               var15 = var11;
         }

         var5.next();
      }

      float[] var17 = new float[var2 * var3];
      double[] var18 = new double[var7.size()];
      int[] var19 = new int[var7.size()];
      Integer[] var20 = new Integer[var7.size()];

      for (int var21 = 0; var21 < var3 * 8; var21++) {
         double var22 = (var21 + 0.5) / 8.0;
         int var24 = 0;

         for (double[] var26 : var7) {
            if (!(var22 < var26[1]) && !(var22 >= var26[3])) {
               var18[var24] = var26[0] + (var22 - var26[1]) / (var26[3] - var26[1]) * (var26[2] - var26[0]);
               var19[var24] = (int)var26[4];
               var20[var24] = var24++;
            }
         }

         if (var24 >= 2) {
            Arrays.sort(var20, 0, var24, (var1x, var2x) -> java.lang.Double.compare(var18[var1x], var18[var2x]));
            int var29 = 0;
            int var30 = var21 / 8 * var2;

            for (int var27 = 0; var27 < var24 - 1; var27++) {
               var29 += var19[var20[var27]];
               int var28 = var6 != 0 ? ((var29 & 1) != 0 ? 1 : 0) : (var29 != 0 ? 1 : 0);
               if (var28 != 0) {
                  accumulateSpan(var17, var30, var2, var18[var20[var27]], var18[var20[var27 + 1]]);
               }
            }
         }
      }

      return var17;
   }

   private static void addEdge(List<double[]> var0, double var1, double var3, double var5, double var7) {
      if (var3 != var7) {
         var0.add(var3 < var7 ? new double[]{var1, var3, var5, var7, 1.0} : new double[]{var5, var7, var1, var3, -1.0});
      }
   }

   private static void accumulateSpan(float[] var0, int var1, int var2, double var3, double var5) {
      var3 = Math.max(0.0, var3);
      var5 = Math.min(var2, var5);
      if (!(var5 <= var3)) {
         float var7 = 0.125F;
         int var8 = (int)var3;
         int var9 = Math.min(var2 - 1, (int)Math.ceil(var5) - 1);

         for (int var10 = var8; var10 <= var9; var10++) {
            var0[var1 + var10] = var0[var1 + var10] + (float)(Math.min(var5, var10 + 1) - Math.max(var3, var10)) * var7;
         }
      }
   }

   public record CrosshairGeometry(String style, int size, int thickness, boolean outline, double scale, DrawingGrid drawing) {
      public CrosshairGeometry(String style, int size, int thickness, boolean outline, double scale, DrawingGrid drawing) {
         if (CrosshairShape.sTYLES.contains(style)
            && size >= 2
            && size <= 24
            && thickness >= 1
            && thickness <= 5
            && scale >= 0.5
            && scale <= 8.0
            && (!"Draw Your Own".equals(style) || drawing != null)) {
            if (!"Draw Your Own".equals(style)) {
               drawing = null;
            }

            this.style = style;
            this.size = size;
            this.thickness = thickness;
            this.outline = outline;
            this.scale = scale;
            this.drawing = drawing;
         } else {
            throw new IllegalArgumentException("Invalid crosshair geometry");
         }
      }

      public CrosshairGeometry(String var1, int var2, int var3, boolean var4, double var5) {
         this(var1, var2, var3, var4, var5, "Draw Your Own".equals(var1) ? DrawingGrid.dEFAULT_CROSSHAIR : null);
      }
   }

   public record RenderedCrosshair(int width, int height, double scale, int[] pixels) {
   }
}
