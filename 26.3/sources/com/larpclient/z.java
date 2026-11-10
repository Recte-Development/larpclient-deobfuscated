package com.larpclient;

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

public final class z {
   public static final String A = "Draw Your Own";
   public static final List<String> G = List.of("Star of David", "67", "Draw Your Own", "Cross", "Dot", "Heart", "Star", "Crown");
   private static final List<String> f = List.of("Star of David", "Cross");
   private static final double h = 7.0;
   public static final double l = 0.65;
   public static final double X = 1.0;
   public static final double R = 0.5;
   public static final double Y = 8.0;
   private static final int p = 8;
   static final String P = "/assets/larp-client/font/semibold.ttf";
   private static Shape O;
   private final z.q E;
   private final double k;
   private final double U;
   private final double j;
   private final boolean W;
   private final Area o;

   public z(z.q var1) {
      this.E = var1;
      double var2 = var1.scale;
      long var4 = Math.max(1L, Math.round(var1.thickness * var2));
      this.k = var2 >= 1.0 ? var4 / var2 : var1.thickness;
      this.U = var1.outline ? Math.min(1.0, Math.max(1L, Math.round(0.65 * var2)) / var2) : 0.0;
      double var6 = var1.size / 7.0;
      long var8 = (long)Math.floor(var6 * var2 + 1.0E-9);
      this.j = var2 >= 1.0 && var8 >= 1L ? var8 / var2 : var6;
      this.W = (boolean)((!a(var1.style) || var4 % 2L != 1L) && (!"Draw Your Own".equals(var1.style) || !(var2 >= 1.0) || var8 % 2L != 1L) ? 0 : 1);
      this.o = this.l();
   }

   public static boolean a(String var0) {
      return f.contains(var0);
   }

   public static double X(double var0) {
      return Math.clamp(Math.round(var0 * 8.0) / 8.0, 0.5, 8.0);
   }

   public static int N(int var0, int var1, boolean var2) {
      return (int)Math.ceil(Math.max(var0 + var1 / 2.0, var0 / 2.0 + var1) + (var2 ? 1.0 : 0.0) + 1.0);
   }

   public static int U(String var0, int var1, int var2, boolean var3) {
      return !"Draw Your Own".equals(var0) ? N(var1, var2, var3) : (int)Math.ceil(7.5 * var1 / 7.0 * Math.sqrt(2.0) + (var3 ? 1.0 : 0.0) + 1.0);
   }

   public boolean T(double var1, double var3) {
      return this.o.contains(var1, var3);
   }

   private double a(double var1) {
      if (this.E.scale < 1.0) {
         return var1;
      }

      double var3 = this.W ? 0.5 : 0.0;
      return (Math.round(var1 * this.E.scale - var3) + var3) / this.E.scale;
   }

   private Area l() {
      double var1 = this.E.size;

      return switch (this.E.style) {
         case "Star of David" -> {
            double var16 = Math.max(var1 - this.k, var1 / 2.0) / 2.0;
            double var17 = Math.floor(var16 * this.E.scale) / this.E.scale;
            double var18 = !(this.E.scale < 1.0) && !(var17 <= 0.0) ? var17 : var16;
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
            yield new Area(new BasicStroke((float)this.k, 0, 0, 10.0F).createStrokedShape(var15));
         }
         case "67" -> l(x(), var1);
         case "Draw Your Own" -> this.U();
         case "Cross" -> {
            double var5 = this.a(Math.max(1.0, var1 * 0.3));
            double var7 = this.k / 2.0;
            double var9 = this.a(var1);
            Area var11 = new Area(new java.awt.geom.Rectangle2D.Double(-var9, -var7, var9 - var5, this.k));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(var5, -var7, var9 - var5, this.k)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, var5, this.k, var9 - var5)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, -var9, this.k, var9 - var5)));
            yield var11;
         }
         case "Dot" -> S(Math.max(1.0, var1 / 3.0));
         case "Heart" -> l(v(), var1);
         case "Star" -> l(X(), var1);
         case "Crown" -> l(S(), var1);
         default -> throw new IllegalStateException(this.E.style);
      };
   }

   private Area U() {
      Area var1 = new Area();
      double var2 = -15.0 * this.j / 2.0;

      for (int var4 = 0; var4 < 15; var4++) {
         for (int var5 = 0; var5 < 15; var5++) {
            if (this.E.drawing.k(var5, var4)) {
               int var6 = var5;

               while (var6 + 1 < 15 && this.E.drawing.k(var6 + 1, var4)) {
                  var6++;
               }

               var1.add(new Area(new java.awt.geom.Rectangle2D.Double(var2 + var5 * this.j, var2 + var4 * this.j, (var6 - var5 + 1) * this.j, this.j)));
               var5 = var6;
            }
         }
      }

      return var1;
   }

   private static Area S(double var0) {
      return new Area(new java.awt.geom.Ellipse2D.Double(-var0, -var0, 2.0 * var0, 2.0 * var0));
   }

   private static Double n(double... var0) {
      Double var1 = new Double();
      var1.moveTo(var0[0], var0[1]);

      for (byte var2 = 2; var2 < var0.length; var2 += 2) {
         var1.lineTo(var0[var2], var0[var2 + 1]);
      }

      var1.closePath();
      return var1;
   }

   private static Area l(Shape var0, double var1) {
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

   private static Shape v() {
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

   private static Shape X() {
      double[] var0 = new double[20];

      for (int var1 = 0; var1 < 10; var1++) {
         double var2 = (-Math.PI / 2) + var1 * Math.PI / 5.0;
         double var4 = var1 % 2 == 0 ? 1.0 : 0.46;
         var0[var1 * 2] = Math.cos(var2) * var4;
         var0[var1 * 2 + 1] = Math.sin(var2) * var4;
      }

      return n(var0);
   }

   private static Shape S() {
      Area var0 = new Area(n(-0.9, 0.45, -0.9, -0.4, -0.45, 0.05, 0.0, -0.62, 0.45, 0.05, 0.9, -0.4, 0.9, 0.45));
      var0.add(new Area(new java.awt.geom.Rectangle2D.Double(-0.9, 0.55, 1.8, 0.32)));

      for (double[] var4 : new double[][]{{-0.9, -0.5}, {0.0, -0.72}, {0.9, -0.5}}) {
         var0.add(new Area(new java.awt.geom.Ellipse2D.Double(var4[0] - 0.14, var4[1] - 0.14, 0.28, 0.28)));
      }

      return var0;
   }

   static synchronized Shape x() {
      if (O != null) {
         return O;
      }

      try (InputStream var0 = z.class.getResourceAsStream("/assets/larp-client/font/semibold.ttf")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing /assets/larp-client/font/semibold.ttf");
         }

         Font var1 = Font.createFont(0, var0).deriveFont(100.0F);
         return O = var1.createGlyphVector(new FontRenderContext(null, true, true), "67").getOutline();
      } catch (IOException | FontFormatException var5) {
         throw new IllegalStateException("Could not read the 67 font", var5);
      }
   }

   public z.l I() {
      double var1 = this.E.scale;
      double var3 = ("Draw Your Own".equals(this.E.style) ? 15.0 * this.j / 2.0 : Math.max(this.E.size + this.k / 2.0, this.E.size / 2.0 + this.k)) + this.U;
      int var5 = (int)Math.ceil(var3 * var1) + 2;
      int var6 = 2 * var5 + (this.W ? 1 : 0);
      int var7 = var6;
      float[] var8 = this.N(this.o, var6, var7);
      float[] var9 = var8;
      if (this.U > 0.0) {
         Area var10 = new Area(this.o);
         var10.add(new Area(new BasicStroke((float)(2.0 * this.U), 1, 1).createStrokedShape(this.o)));
         var9 = this.N(var10, var6, var7);
      }

      int[] var14 = new int[var6 * var7 * 2];

      for (int var11 = 0; var11 < var6 * var7; var11++) {
         int var12 = U(var8[var11]);
         int var13 = Math.max(var12, U(var9[var11]));
         var14[var11] = var12 << 24 | 16777215;
         var14[var6 * var7 + var11] = var13 - var12 << 24 | 16777215;
      }

      return new z.l(var6, var7, var1, var14);
   }

   private static int U(float var0) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
   }

   private float[] N(Area var1, int var2, int var3) {
      AffineTransform var4 = new AffineTransform();
      var4.translate(var2 / 2.0, var3 / 2.0);
      var4.scale(this.E.scale, this.E.scale);
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
               B(var7, var13, var15, var8[0], var8[1]);
               var13 = var8[0];
               var15 = var8[1];
               break;
            case 2:
            case 3:
            default:
               throw new IllegalStateException("Unflattened crosshair segment");
            case 4:
               B(var7, var13, var15, var9, var11);
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
                  Y(var17, var30, var2, var18[var20[var27]], var18[var20[var27 + 1]]);
               }
            }
         }
      }

      return var17;
   }

   private static void B(List<double[]> var0, double var1, double var3, double var5, double var7) {
      if (var3 != var7) {
         var0.add(var3 < var7 ? new double[]{var1, var3, var5, var7, 1.0} : new double[]{var5, var7, var1, var3, -1.0});
      }
   }

   private static void Y(float[] var0, int var1, int var2, double var3, double var5) {
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

   public record l(int width, int height, double scale, int[] pixels) {
   }

   public record q(String style, int size, int thickness, boolean outline, double scale, qj drawing) {
      public q(String style, int size, int thickness, boolean outline, double scale, qj drawing) {
         if (z.G.contains(style)
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

      public q(String var1, int var2, int var3, boolean var4, double var5) {
         this(var1, var2, var3, var4, var5, "Draw Your Own".equals(var1) ? qj.K : null);
      }
   }
}
