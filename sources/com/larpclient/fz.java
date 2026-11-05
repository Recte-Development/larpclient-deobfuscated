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

public final class fz {
   public static final Identifier e = Identifier.fromNamespaceAndPath("larp-client", "crosshair/crosshair.png");
   static final int J = 4096;
   static final int b = 512;
   static final int S = 16777216;
   private static final fz.y r = new fz.y("live");
   private static final fz.y F = new fz.y("preview");
   private static fz.z G;
   private static boolean O;
   private static TextureManager Y;

   private fz() {
   }

   public static fz.j m(int var0, boolean var1) {
      if (!O) {
         O = (boolean)1;
         G = f();
      }

      if (G == null) {
         return null;
      }

      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var2 != Y) {
         r.c = F.c = null;
         r.m = F.m = -1;
         Y = var2;
      }

      fz.y var3 = var1 ? F : r;
      int var4 = Math.clamp(var0, 1, 512);
      int var5 = W(G, var4);
      if (var3.c == null || var5 != var3.m) {
         fz.e var6 = new fz.e(var3.G, d(G, var4));

         try {
            Y.registerAndLoad(var3.G, var6);
         } catch (RuntimeException var8) {
            var6.close();
            G = null;
            r.r(Y);
            F.r(Y);
            r4.r("Crosshair", "Image upload failed; using Star of David until the module is re-enabled", var8);
            return null;
         }

         var3.c = var6;
         var3.m = var5;
         r4.g("Crosshair", "Prepared image at {}x{} for {} pixels", var6.P.width(), var6.P.height(), var4);
      }

      return new fz.j(var3.G, var3.c.P.width(), var3.c.P.height());
   }

   public static void I() {
      O = (boolean)0;
      G = null;
      if (r.c != null || F.c != null) {
         TextureManager var0 = Minecraft.getInstance().getTextureManager();
         r.r(var0);
         F.r(var0);
      }
   }

   private static fz.z f() {
      Optional var0 = Minecraft.getInstance().getResourceManager().getResource(e);
      if (var0.isEmpty()) {
         r4.m("Crosshair", "{} is not in the mod; using Star of David", e);
         return null;
      }

      try (InputStream var1 = ((Resource)var0.get()).open()) {
         fz.b var2 = E(var1);
         r4.V("Crosshair", "Loaded {} ({}), kept at {}x{}", e, var2.original(), var2.picture().width(), var2.picture().height());
         return var2.picture();
      } catch (IOException | RuntimeException var6) {
         r4.m("Crosshair", "Could not read {}; using Star of David: {}", e, var6.toString());
         return null;
      }
   }

   public static fz.b E(InputStream var0) throws IOException {
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
      fz.z var17 = new fz.z(var15, var16, var2.getRGB(0, 0, var15, var16, null, 0, var15));

      while (Math.max(var17.width(), var17.height()) > 512) {
         var17 = y(var17);
      }

      return new fz.b(w(var17), var15 + "x" + var16);
   }

   public static int W(fz.z var0, int var1) {
      int var2 = 0;
      int var3 = Math.max(var0.width(), var0.height());

      for (int var4 = Math.min(var0.width(), var0.height()); var3 >= var1 * 2 && var4 > 1; var4 = Math.max(1, var4 / 2)) {
         var2++;
         var3 = Math.max(1, var3 / 2);
      }

      return var2;
   }

   public static fz.z d(fz.z var0, int var1) {
      fz.z var2 = var0;

      for (int var3 = W(var0, var1); var3 > 0; var3--) {
         var2 = y(var2);
      }

      return var2 == var0 ? var0 : w(var2);
   }

   private static fz.z y(fz.z var0) {
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

      return new fz.z(var1, var2, var3);
   }

   private static fz.z w(fz.z var0) {
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

      return new fz.z(var1, var2, var3);
   }

   public record b(fz.z picture, String original) {
   }

   private static final class e extends ReloadableTexture {
      final fz.z P;

      e(Identifier var1, fz.z var2) {
         super(var1);
         this.P = var2;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.P.width(), this.P.height(), false);

         try {
            for (int var3 = 0; var3 < this.P.height(); var3++) {
               for (int var4 = 0; var4 < this.P.width(); var4++) {
                  var2.setPixel(var4, var3, this.P.pixels()[var3 * this.P.width() + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   public record j(Identifier id, int width, int height) {
   }

   private static final class y {
      final Identifier G;
      fz.e c;
      int m = -1;

      y(String var1) {
         this.G = Identifier.fromNamespaceAndPath("larp-client", "crosshair/image-" + var1);
      }

      void r(TextureManager var1) {
         if (this.c != null) {
            var1.release(this.G);
            this.c = null;
         }

         this.m = -1;
      }
   }

   public record z(int width, int height, int[] pixels) {
   }
}
