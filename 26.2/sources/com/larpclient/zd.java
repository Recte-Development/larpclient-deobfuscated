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

public final class zd {
   public static final Identifier G = Identifier.fromNamespaceAndPath("larp-client", "crosshair/crosshair.png");
   static final int u = 4096;
   static final int h = 512;
   static final int K = 16777216;
   private static final zd.f Q = new zd.f("live");
   private static final zd.f j = new zd.f("preview");
   private static zd.v c;
   private static boolean g;
   private static TextureManager I;

   private zd() {
   }

   public static zd.e L(int var0, boolean var1) {
      if (!g) {
         g = (boolean)1;
         c = i();
      }

      if (c == null) {
         return null;
      }

      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var2 != I) {
         Q.J = j.J = null;
         Q.z = j.z = -1;
         I = var2;
      }

      zd.f var3 = var1 ? j : Q;
      int var4 = Math.clamp(var0, 1, 512);
      int var5 = J(c, var4);
      if (var3.J == null || var5 != var3.z) {
         zd.l var6 = new zd.l(var3.B, l(c, var4));

         try {
            I.registerAndLoad(var3.B, var6);
         } catch (RuntimeException var8) {
            var6.close();
            c = null;
            Q.O(I);
            j.O(I);
            com.larpclient.h.m("Crosshair", "Image upload failed; using Star of David until the module is re-enabled", var8);
            return null;
         }

         var3.J = var6;
         var3.z = var5;
         com.larpclient.h.U("Crosshair", "Prepared image at {}x{} for {} pixels", var6.H.width(), var6.H.height(), var4);
      }

      return new zd.e(var3.B, var3.J.H.width(), var3.J.H.height());
   }

   public static void B() {
      g = (boolean)0;
      c = null;
      if (Q.J != null || j.J != null) {
         TextureManager var0 = Minecraft.getInstance().getTextureManager();
         Q.O(var0);
         j.O(var0);
      }
   }

   private static zd.v i() {
      Optional var0 = Minecraft.getInstance().getResourceManager().getResource(G);
      if (var0.isEmpty()) {
         com.larpclient.h.g("Crosshair", "{} is not in the mod; using Star of David", G);
         return null;
      }

      try (InputStream var1 = ((Resource)var0.get()).open()) {
         zd.s var2 = g(var1);
         com.larpclient.h.A("Crosshair", "Loaded {} ({}), kept at {}x{}", G, var2.original(), var2.picture().width(), var2.picture().height());
         return var2.picture();
      } catch (IOException | RuntimeException var6) {
         com.larpclient.h.g("Crosshair", "Could not read {}; using Star of David: {}", G, var6.toString());
         return null;
      }
   }

   public static zd.s g(InputStream var0) throws IOException {
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
      zd.v var17 = new zd.v(var15, var16, var2.getRGB(0, 0, var15, var16, null, 0, var15));

      while (Math.max(var17.width(), var17.height()) > 512) {
         var17 = f(var17);
      }

      return new zd.s(j(var17), var15 + "x" + var16);
   }

   public static int J(zd.v var0, int var1) {
      int var2 = 0;
      int var3 = Math.max(var0.width(), var0.height());

      for (int var4 = Math.min(var0.width(), var0.height()); var3 >= var1 * 2 && var4 > 1; var4 = Math.max(1, var4 / 2)) {
         var2++;
         var3 = Math.max(1, var3 / 2);
      }

      return var2;
   }

   public static zd.v l(zd.v var0, int var1) {
      zd.v var2 = var0;

      for (int var3 = J(var0, var1); var3 > 0; var3--) {
         var2 = f(var2);
      }

      return var2 == var0 ? var0 : j(var2);
   }

   private static zd.v f(zd.v var0) {
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

      return new zd.v(var1, var2, var3);
   }

   private static zd.v j(zd.v var0) {
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

      return new zd.v(var1, var2, var3);
   }

   public record e(Identifier id, int width, int height) {
   }

   private static final class f {
      final Identifier B;
      zd.l J;
      int z = -1;

      f(String var1) {
         this.B = Identifier.fromNamespaceAndPath("larp-client", "crosshair/image-" + var1);
      }

      void O(TextureManager var1) {
         if (this.J != null) {
            var1.release(this.B);
            this.J = null;
         }

         this.z = -1;
      }
   }

   private static final class l extends ReloadableTexture {
      final zd.v H;

      l(Identifier var1, zd.v var2) {
         super(var1);
         this.H = var2;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.H.width(), this.H.height(), false);

         try {
            for (int var3 = 0; var3 < this.H.height(); var3++) {
               for (int var4 = 0; var4 < this.H.width(); var4++) {
                  var2.setPixel(var4, var3, this.H.pixels()[var3 * this.H.width() + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   public record s(zd.v picture, String original) {
   }

   public record v(int width, int height, int[] pixels) {
   }
}
