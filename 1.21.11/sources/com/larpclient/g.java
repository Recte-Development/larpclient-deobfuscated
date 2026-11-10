package com.larpclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.class_1011;
import net.minecraft.class_10537;
import net.minecraft.class_10539;
import net.minecraft.class_1060;
import net.minecraft.class_1084;
import net.minecraft.class_12253;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_3300;

public final class g {
   public static final class_2960 g = class_2960.method_60655("larp-client", "crosshair/crosshair.png");
   static final int J = 4096;
   static final int P = 512;
   static final int t = 16777216;
   private static final g.r K = new g.r("live");
   private static final g.r m = new g.r("preview");
   private static g.h A;
   private static boolean f;
   private static class_1060 q;

   private g() {
   }

   public static g.l X(int var0, boolean var1) {
      if (!f) {
         f = (boolean)1;
         A = E();
      }

      if (A == null) {
         return null;
      }

      class_1060 var2 = class_310.method_1551().method_1531();
      if (var2 != q) {
         K.h = m.h = null;
         K.r = m.r = -1;
         q = var2;
      }

      g.r var3 = var1 ? m : K;
      int var4 = Math.clamp(var0, 1, 512);
      int var5 = l(A, var4);
      if (var3.h == null || var5 != var3.r) {
         g.n var6 = new g.n(var3.V, b(A, var4));

         try {
            q.method_65876(var3.V, var6);
         } catch (RuntimeException var8) {
            var6.close();
            A = null;
            K.k(q);
            m.k(q);
            q8.u("Crosshair", "Image upload failed; using Star of David until the module is re-enabled", var8);
            return null;
         }

         var3.h = var6;
         var3.r = var5;
         q8.v("Crosshair", "Prepared image at {}x{} for {} pixels", var6.c.width(), var6.c.height(), var4);
      }

      return new g.l(var3.V, var3.h.c.width(), var3.h.c.height());
   }

   public static void E() {
      f = (boolean)0;
      A = null;
      if (K.h != null || m.h != null) {
         class_1060 var0 = class_310.method_1551().method_1531();
         K.k(var0);
         m.k(var0);
      }
   }

   private static g.h E() {
      Optional var0 = class_310.method_1551().method_1478().method_14486(g);
      if (var0.isEmpty()) {
         q8.L("Crosshair", "{} is not in the mod; using Star of David", g);
         return null;
      }

      try (InputStream var1 = ((class_3298)var0.get()).method_14482()) {
         g.e var2 = H(var1);
         q8.G("Crosshair", "Loaded {} ({}), kept at {}x{}", g, var2.original(), var2.picture().width(), var2.picture().height());
         return var2.picture();
      } catch (IOException | RuntimeException var6) {
         q8.L("Crosshair", "Could not read {}; using Star of David: {}", g, var6.toString());
         return null;
      }
   }

   public static g.e H(InputStream var0) throws IOException {
      byte[] var1 = var0.readNBytes(16777217);
      if (var1.length > 16777216) {
         throw new IOException("file is larger than 16 MB");
      }

      BufferedImage var2;
      try (ImageInputStream var3 = ImageIO.createImageInputStream(new ByteArrayInputStream(var1))) {
         Iterator var4 = ImageIO.getImageReaders(var3);
         if (!var4.hasNext()) {
            throw new IOException("not a PNG image");
         }

         ImageReader var5 = (ImageReader)var4.next();

         try {
            var5.setInput(var3);
            if (!var5.getFormatName().equalsIgnoreCase("png")) {
               throw new IOException("expected PNG, found " + var5.getFormatName());
            }

            int var6 = var5.getWidth(0);
            int var7 = var5.getHeight(0);
            if (var6 > 4096 || var7 > 4096) {
               throw new IOException(var6 + "x" + var7 + " is too large; use at most 4096 pixels per side");
            }

            var2 = var5.read(0);
         } finally {
            var5.dispose();
         }
      }

      int var15 = var2.getWidth();
      int var16 = var2.getHeight();
      g.h var17 = new g.h(var15, var16, var2.getRGB(0, 0, var15, var16, null, 0, var15));

      while (Math.max(var17.width(), var17.height()) > 512) {
         var17 = s(var17);
      }

      return new g.e(F(var17), var15 + "x" + var16);
   }

   public static int l(g.h var0, int var1) {
      int var2 = 0;
      int var3 = Math.max(var0.width(), var0.height());

      for (int var4 = Math.min(var0.width(), var0.height()); var3 >= var1 * 2 && var4 > 1; var4 = Math.max(1, var4 / 2)) {
         var2++;
         var3 = Math.max(1, var3 / 2);
      }

      return var2;
   }

   public static g.h b(g.h var0, int var1) {
      g.h var2 = var0;

      for (int var3 = l(var0, var1); var3 > 0; var3--) {
         var2 = s(var2);
      }

      return var2 == var0 ? var0 : F(var2);
   }

   private static g.h s(g.h var0) {
      int var1 = Math.max(1, var0.width() / 2);
      int var2 = Math.max(1, var0.height() / 2);
      int[] var3 = new int[var1 * var2];

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            long var6 = 0L;
            long var8 = 0L;
            long var10 = 0L;
            long var12 = 0L;

            for (int var14 = 0; var14 < 2; var14++) {
               for (int var15 = 0; var15 < 2; var15++) {
                  int var16 = var0.pixels()[Math.min(var4 * 2 + var14, var0.height() - 1) * var0.width() + Math.min(var5 * 2 + var15, var0.width() - 1)];
                  int var17 = var16 >>> 24;
                  var6 += var17;
                  var8 += (var16 >> 16 & 0xFF) * var17;
                  var10 += (var16 >> 8 & 0xFF) * var17;
                  var12 += (var16 & 0xFF) * var17;
               }
            }

            var3[var4 * var1 + var5] = var6 == 0L
               ? 0
               : (int)Math.round(var6 / 4.0) << 24 | (int)(var8 / var6) << 16 | (int)(var10 / var6) << 8 | (int)(var12 / var6);
         }
      }

      return new g.h(var1, var2, var3);
   }

   private static g.h F(g.h var0) {
      int var1 = var0.width();
      int var2 = var0.height();
      int[] var3 = (int[])var0.pixels().clone();

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            if (var0.pixels()[var4 * var1 + var5] >>> 24 == 0) {
               for (int var6 = 0; var6 < 4; var6++) {
                  int var7 = var5 + (var6 == 0 ? -1 : (var6 == 1 ? 1 : 0));
                  int var8 = var4 + (var6 == 2 ? -1 : (var6 == 3 ? 1 : 0));
                  if (var7 >= 0 && var8 >= 0 && var7 < var1 && var8 < var2) {
                     int var9 = var0.pixels()[var8 * var1 + var7];
                     if (var9 >>> 24 != 0) {
                        var3[var4 * var1 + var5] = var9 & 16777215;
                        break;
                     }
                  }
               }
            }
         }
      }

      return new g.h(var1, var2, var3);
   }

   public record e(g.h picture, String original) {
   }

   public record h(int width, int height, int[] pixels) {
   }

   public record l(class_2960 id, int width, int height) {
   }

   private static final class n extends class_10537 {
      final g.h c;

      n(class_2960 var1, g.h var2) {
         super(var1);
         this.c = var2;
      }

      public class_10539 method_65809(class_3300 var1) {
         class_1011 var2 = new class_1011(this.c.width(), this.c.height(), false);

         try {
            for (int var3 = 0; var3 < this.c.height(); var3++) {
               for (int var4 = 0; var4 < this.c.width(); var4++) {
                  var2.method_61941(var4, var3, this.c.pixels()[var3 * this.c.width() + var4]);
               }
            }

            return new class_10539(var2, new class_1084(true, true, class_12253.field_64077, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   private static final class r {
      final class_2960 V;
      g.n h;
      int r = -1;

      r(String var1) {
         this.V = class_2960.method_60655("larp-client", "crosshair/image-" + var1);
      }

      void k(class_1060 var1) {
         if (this.h != null) {
            var1.method_4615(this.V);
            this.h = null;
         }

         this.r = -1;
      }
   }
}
