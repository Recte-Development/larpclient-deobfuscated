package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public final class oc {
   public static final Identifier w = Identifier.fromNamespaceAndPath("larp-client", "crosshair/crosshair.png");
   static final int e = 4096;
   static final int M = 512;
   static final int F = 16777216;
   private static final oc.h H = new oc.h("live");
   private static final oc.h d = new oc.h("preview");
   private static oc.n Y;
   private static boolean y;
   private static TextureManager r;

   private oc() {
   }

   public static oc.j o(int var0, boolean var1) {
      if (!y) {
         y = (boolean)1;
         Y = y();
      }

      if (Y == null) {
         return null;
      }

      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var2 != r) {
         H.m = d.m = null;
         H.k = d.k = -1;
         r = var2;
      }

      oc.h var3 = var1 ? d : H;
      int var4 = Math.clamp(var0, 1, 512);
      int var5 = g(Y, var4);
      if (var3.m == null || var5 != var3.k) {
         oc.f var6 = new oc.f(var3.u, I(Y, var4));

         try {
            r.registerAndLoad(var3.u, var6);
         } catch (RuntimeException var8) {
            var6.close();
            Y = null;
            H.R(r);
            d.R(r);
            _q.o("Crosshair", "Image upload failed; using Star of David until the module is re-enabled", var8);
            return null;
         }

         var3.m = var6;
         var3.k = var5;
         _q.c("Crosshair", "Prepared image at {}x{} for {} pixels", var6.a.width(), var6.a.height(), var4);
      }

      return new oc.j(var3.u, var3.m.a.width(), var3.m.a.height());
   }

   public static void s() {
      y = (boolean)0;
      Y = null;
      if (H.m != null || d.m != null) {
         TextureManager var0 = Minecraft.getInstance().getTextureManager();
         H.R(var0);
         d.R(var0);
      }
   }

   private static oc.n y() {
      Optional var0 = Minecraft.getInstance().getResourceManager().getResource(w);
      if (var0.isEmpty()) {
         _q.R("Crosshair", "{} is not in the mod; using Star of David", w);
         return null;
      }

      try (InputStream var1 = ((Resource)var0.get()).open()) {
         oc.c var2 = Y(var1);
         _q.z("Crosshair", "Loaded {} ({}), kept at {}x{}", w, var2.original(), var2.picture().width(), var2.picture().height());
         return var2.picture();
      } catch (IOException | RuntimeException var6) {
         _q.R("Crosshair", "Could not read {}; using Star of David: {}", w, var6.toString());
         return null;
      }
   }

   public static oc.c Y(InputStream var0) throws IOException {
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
      oc.n var17 = new oc.n(var15, var16, var2.getRGB(0, 0, var15, var16, null, 0, var15));

      while (Math.max(var17.width(), var17.height()) > 512) {
         var17 = I(var17);
      }

      return new oc.c(j(var17), var15 + "x" + var16);
   }

   public static int g(oc.n var0, int var1) {
      int var2 = 0;
      int var3 = Math.max(var0.width(), var0.height());

      for (int var4 = Math.min(var0.width(), var0.height()); var3 >= var1 * 2 && var4 > 1; var4 = Math.max(1, var4 / 2)) {
         var2++;
         var3 = Math.max(1, var3 / 2);
      }

      return var2;
   }

   public static oc.n I(oc.n var0, int var1) {
      oc.n var2 = var0;

      for (int var3 = g(var0, var1); var3 > 0; var3--) {
         var2 = I(var2);
      }

      return var2 == var0 ? var0 : j(var2);
   }

   private static oc.n I(oc.n var0) {
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

      return new oc.n(var1, var2, var3);
   }

   private static oc.n j(oc.n var0) {
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

      return new oc.n(var1, var2, var3);
   }

   public record c(oc.n picture, String original) {
   }

   private static final class f extends ReloadableTexture {
      final oc.n a;

      f(Identifier var1, oc.n var2) {
         super(var1);
         this.a = var2;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.a.width(), this.a.height(), false);

         try {
            for (int var3 = 0; var3 < this.a.height(); var3++) {
               for (int var4 = 0; var4 < this.a.width(); var4++) {
                  var2.setPixel(var4, var3, this.a.pixels()[var3 * this.a.width() + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   private static final class h {
      final Identifier u;
      oc.f m;
      int k = -1;

      h(String var1) {
         this.u = Identifier.fromNamespaceAndPath("larp-client", "crosshair/image-" + var1);
      }

      void R(TextureManager var1) {
         if (this.m != null) {
            var1.release(this.u);
            this.m = null;
         }

         this.k = -1;
      }
   }

   public record j(Identifier id, int width, int height) {
   }

   public record n(int width, int height, int[] pixels) {
   }
}
