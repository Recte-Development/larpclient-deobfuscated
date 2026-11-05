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

public final class x5 {
   public static final String A = "Draw Your Own";
   public static final List<String> F = List.of("Star of David", "67", "Draw Your Own", "Cross", "Dot", "Heart", "Star", "Crown");
   private static final List<String> n = List.of("Star of David", "Cross");
   private static final double d = 7.0;
   public static final double E = 0.65;
   public static final double O = 1.0;
   public static final double I = 0.5;
   public static final double e = 8.0;
   private static final int w = 8;
   static final String u = "/assets/larp-client/font/semibold.ttf";
   private static Shape L;
   private final x5.p f;
   private final double j;
   private final double J;
   private final double U;
   private final boolean h;
   private final Area s;

   public x5(x5.p var1) {
      this.f = var1;
      double var2 = var1.scale;
      long var4 = Math.max(1L, Math.round(var1.thickness * var2));
      this.j = var2 >= 1.0 ? var4 / var2 : var1.thickness;
      this.J = var1.outline ? Math.min(1.0, Math.max(1L, Math.round(0.65 * var2)) / var2) : 0.0;
      double var6 = var1.size / 7.0;
      long var8 = (long)Math.floor(var6 * var2 + 1.0E-9);
      this.U = var2 >= 1.0 && var8 >= 1L ? var8 / var2 : var6;
      this.h = (boolean)((!Z(var1.style) || var4 % 2L != 1L) && (!"Draw Your Own".equals(var1.style) || !(var2 >= 1.0) || var8 % 2L != 1L) ? 0 : 1);
      this.s = this.M();
   }

   public static boolean Z(String var0) {
      return n.contains(var0);
   }

   public static double R(double var0) {
      return Math.clamp(Math.round(var0 * 8.0) / 8.0, 0.5, 8.0);
   }

   public static int D(int var0, int var1, boolean var2) {
      return (int)Math.ceil(Math.max(var0 + var1 / 2.0, var0 / 2.0 + var1) + (var2 ? 1.0 : 0.0) + 1.0);
   }

   public static int V(String var0, int var1, int var2, boolean var3) {
      return !"Draw Your Own".equals(var0) ? D(var1, var2, var3) : (int)Math.ceil(7.5 * var1 / 7.0 * Math.sqrt(2.0) + (var3 ? 1.0 : 0.0) + 1.0);
   }

   public boolean V(double var1, double var3) {
      return this.s.contains(var1, var3);
   }

   private double m(double var1) {
      if (this.f.scale < 1.0) {
         return var1;
      }

      double var3 = this.h ? 0.5 : 0.0;
      return (Math.round(var1 * this.f.scale - var3) + var3) / this.f.scale;
   }

   private Area M() {
      double var1 = this.f.size;

      return switch (this.f.style) {
         case "Star of David" -> {
            double var16 = Math.max(var1 - this.j, var1 / 2.0) / 2.0;
            double var17 = Math.floor(var16 * this.f.scale) / this.f.scale;
            double var18 = !(this.f.scale < 1.0) && !(var17 <= 0.0) ? var17 : var16;
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
            yield new Area(new BasicStroke((float)this.j, 0, 0, 10.0F).createStrokedShape(var15));
         }
         case "67" -> J(Y(), var1);
         case "Draw Your Own" -> this.v();
         case "Cross" -> {
            double var5 = this.m(Math.max(1.0, var1 * 0.3));
            double var7 = this.j / 2.0;
            double var9 = this.m(var1);
            Area var11 = new Area(new java.awt.geom.Rectangle2D.Double(-var9, -var7, var9 - var5, this.j));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(var5, -var7, var9 - var5, this.j)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, var5, this.j, var9 - var5)));
            var11.add(new Area(new java.awt.geom.Rectangle2D.Double(-var7, -var9, this.j, var9 - var5)));
            yield var11;
         }
         case "Dot" -> D(Math.max(1.0, var1 / 3.0));
         case "Heart" -> J(F(), var1);
         case "Star" -> J(x(), var1);
         case "Crown" -> J(V(), var1);
         default -> throw new IllegalStateException(this.f.style);
      };
   }

   private Area v() {
      Area var1 = new Area();
      double var2 = -15.0 * this.U / 2.0;

      for (int var4 = 0; var4 < 15; var4++) {
         for (int var5 = 0; var5 < 15; var5++) {
            if (this.f.drawing.x(var5, var4)) {
               int var6 = var5;

               while (var6 + 1 < 15 && this.f.drawing.x(var6 + 1, var4)) {
                  var6++;
               }

               var1.add(new Area(new java.awt.geom.Rectangle2D.Double(var2 + var5 * this.U, var2 + var4 * this.U, (var6 - var5 + 1) * this.U, this.U)));
               var5 = var6;
            }
         }
      }

      return var1;
   }

   private static Area D(double var0) {
      return new Area(new java.awt.geom.Ellipse2D.Double(-var0, -var0, 2.0 * var0, 2.0 * var0));
   }

   private static Double w(double... var0) {
      Double var1 = new Double();
      var1.moveTo(var0[0], var0[1]);

      for (byte var2 = 2; var2 < var0.length; var2 += 2) {
         var1.lineTo(var0[var2], var0[var2 + 1]);
      }

      var1.closePath();
      return var1;
   }

   private static Area J(Shape var0, double var1) {
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

   private static Shape F() {
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

   private static Shape x() {
      double[] var0 = new double[20];

      for (int var1 = 0; var1 < 10; var1++) {
         double var2 = (-Math.PI / 2) + var1 * Math.PI / 5.0;
         double var4 = var1 % 2 == 0 ? 1.0 : 0.46;
         var0[var1 * 2] = Math.cos(var2) * var4;
         var0[var1 * 2 + 1] = Math.sin(var2) * var4;
      }

      return w(var0);
   }

   private static Shape V() {
      Area var0 = new Area(w(-0.9, 0.45, -0.9, -0.4, -0.45, 0.05, 0.0, -0.62, 0.45, 0.05, 0.9, -0.4, 0.9, 0.45));
      var0.add(new Area(new java.awt.geom.Rectangle2D.Double(-0.9, 0.55, 1.8, 0.32)));

      for (double[] var4 : new double[][]{{-0.9, -0.5}, {0.0, -0.72}, {0.9, -0.5}}) {
         var0.add(new Area(new java.awt.geom.Ellipse2D.Double(var4[0] - 0.14, var4[1] - 0.14, 0.28, 0.28)));
      }

      return var0;
   }

   static synchronized Shape Y() {
      if (L != null) {
         return L;
      }

      try (InputStream var0 = x5.class.getResourceAsStream("/assets/larp-client/font/semibold.ttf")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing /assets/larp-client/font/semibold.ttf");
         }

         Font var1 = Font.createFont(0, var0).deriveFont(100.0F);
         return L = var1.createGlyphVector(new FontRenderContext(null, true, true), "67").getOutline();
      } catch (IOException | FontFormatException var5) {
         throw new IllegalStateException("Could not read the 67 font", var5);
      }
   }

   public x5.l b() {
      double var1 = this.f.scale;
      double var3 = ("Draw Your Own".equals(this.f.style) ? 15.0 * this.U / 2.0 : Math.max(this.f.size + this.j / 2.0, this.f.size / 2.0 + this.j)) + this.J;
      int var5 = (int)Math.ceil(var3 * var1) + 2;
      int var6 = 2 * var5 + (this.h ? 1 : 0);
      int var7 = var6;
      float[] var8 = this.w(this.s, var6, var7);
      float[] var9 = var8;
      if (this.J > 0.0) {
         Area var10 = new Area(this.s);
         var10.add(new Area(new BasicStroke((float)(2.0 * this.J), 1, 1).createStrokedShape(this.s)));
         var9 = this.w(var10, var6, var7);
      }

      int[] var14 = new int[var6 * var7 * 2];

      for (int var11 = 0; var11 < var6 * var7; var11++) {
         int var12 = E(var8[var11]);
         int var13 = Math.max(var12, E(var9[var11]));
         var14[var11] = var12 << 24 | 16777215;
         var14[var6 * var7 + var11] = var13 - var12 << 24 | 16777215;
      }

      return new x5.l(var6, var7, var1, var14);
   }

   private static int E(float var0) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
   }

   private float[] w(Area var1, int var2, int var3) {
      AffineTransform var4 = new AffineTransform();
      var4.translate(var2 / 2.0, var3 / 2.0);
      var4.scale(this.f.scale, this.f.scale);
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
               l(var7, var13, var15, var8[0], var8[1]);
               var13 = var8[0];
               var15 = var8[1];
               break;
            case 2:
            case 3:
            default:
               throw new IllegalStateException("Unflattened crosshair segment");
            case 4:
               l(var7, var13, var15, var9, var11);
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
                  r(var17, var30, var2, var18[var20[var27]], var18[var20[var27 + 1]]);
               }
            }
         }
      }

      return var17;
   }

   private static void l(List<double[]> var0, double var1, double var3, double var5, double var7) {
      if (var3 != var7) {
         var0.add(var3 < var7 ? new double[]{var1, var3, var5, var7, 1.0} : new double[]{var5, var7, var1, var3, -1.0});
      }
   }

   private static void r(float[] var0, int var1, int var2, double var3, double var5) {
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

   public record p(String style, int size, int thickness, boolean outline, double scale, dp drawing) {
      public p(String style, int size, int thickness, boolean outline, double scale, dp drawing) {
         if (x5.F.contains(style)
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

      public p(String var1, int var2, int var3, boolean var4, double var5) {
         this(var1, var2, var3, var4, var5, "Draw Your Own".equals(var1) ? dp.h : null);
      }
   }
}
