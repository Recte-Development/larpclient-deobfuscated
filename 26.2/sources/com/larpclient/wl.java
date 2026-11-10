package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public final class wl {
   private static final int s = 128;
   private static final int u = 262144;
   private static final int U = 256;
   private static final int X = 512;
   private static final long L = TimeUnit.MINUTES.toNanos(1L);
   private static final AtomicInteger g = new AtomicInteger();
   private final Identifier R = Identifier.fromNamespaceAndPath("larp-client", "discord/avatar-" + g.getAndIncrement());
   private volatile String O = "";
   private volatile zd.v n;
   private volatile boolean H;
   private volatile long b;
   private wl.v Q;
   private TextureManager h;
   private int d;

   public boolean D(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      ui var5 = bq.current().account();
      if (var5 != null && var4 > 0) {
         String var6 = var5.id() + "/" + var5.avatar();
         if (!var6.equals(this.O)) {
            this.C();
            this.O = var6;
         }

         zd.v var7 = this.n;
         if (var7 == null) {
            if (!this.H && System.nanoTime() >= this.b) {
               this.M(var5, var6);
            }

            return false;
         } else {
            int var8 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
            int var9 = Math.min(256, var4 * var8);
            if (!this.R(var7, var9)) {
               return false;
            }

            var1.blit(RenderPipelines.GUI_TEXTURED, this.R, var2, var3, 0.0F, 0.0F, var4, var4, this.d, this.d, this.d, this.d, ua.I(-1));
            return true;
         }
      } else {
         this.C();
         return false;
      }
   }

   public void C() {
      if (this.Q != null || !this.O.isEmpty()) {
         if (this.Q != null) {
            Minecraft.getInstance().getTextureManager().release(this.R);
            this.Q = null;
         }

         this.O = "";
         this.n = null;
         this.d = 0;
         this.b = 0L;
      }
   }

   private boolean R(zd.v var1, int var2) {
      TextureManager var3 = Minecraft.getInstance().getTextureManager();
      if (var3 != this.h) {
         this.Q = null;
         this.d = 0;
         this.h = var3;
      }

      if (this.Q != null && this.d == var2) {
         return true;
      }

      wl.v var4 = new wl.v(this.R, var1, var2);

      try {
         var3.registerAndLoad(this.R, var4);
      } catch (RuntimeException var6) {
         var4.close();
         this.n = null;
         this.Q = null;
         this.d = 0;
         this.b = System.nanoTime() + L;
         com.larpclient.h.w("Discord", "Avatar upload failed; the account card keeps the game head", var6);
         return false;
      }

      this.Q = var4;
      this.d = var2;
      com.larpclient.h.U("Discord", "Prepared the linked avatar at {} pixels", var2);
      return true;
   }

   private void M(ui var1, String var2) {
      this.H = (boolean)1;
      String var3 = var1.avatarUrl(128);
      Thread.ofPlatform().daemon().name("Larp-Discord-Avatar").start(() -> {
         try {
            byte[] var3x = w2.f(var3, 262144, "image/png");
            w(var3x);
            zd.s var4 = zd.g(new ByteArrayInputStream(var3x));
            if (var2.equals(this.O)) {
               this.n = var4.picture();
            }
         } catch (Exception var8) {
            this.b = System.nanoTime() + L;
            com.larpclient.h.w("Discord", "Avatar download failed; the account card keeps the game head", var8);
         } finally {
            this.H = (boolean)0;
         }
      });
   }

   private static void w(byte[] var0) throws IOException {
      if (var0.length < 24) {
         throw new IOException("Avatar response is not a PNG");
      }

      ByteBuffer var1 = ByteBuffer.wrap(var0);
      if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
         int var2 = var1.getInt();
         int var3 = var1.getInt();
         if (var2 < 1 || var3 < 1 || var2 > 512 || var3 > 512) {
            throw new IOException("Avatar is " + var2 + "x" + var3 + "; at most 512 per side");
         }
      } else {
         throw new IOException("Avatar response is not a PNG");
      }
   }

   private static int[] G(zd.v var0, int var1) {
      int var2 = var0.width();
      int var3 = var0.height();
      int[] var4 = new int[var1 * var1];

      for (int var5 = 0; var5 < var1; var5++) {
         int var6 = var5 * var3 / var1;
         int var7 = Math.max(var6 + 1, (var5 + 1) * var3 / var1);

         for (int var8 = 0; var8 < var1; var8++) {
            int var9 = var8 * var2 / var1;
            int var10 = Math.max(var9 + 1, (var8 + 1) * var2 / var1);
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;
            long var17 = 0L;
            int var19 = 0;

            for (int var20 = var6; var20 < var7; var20++) {
               for (int var21 = var9; var21 < var10; var21++) {
                  int var22 = var0.pixels()[var20 * var2 + var21];
                  int var23 = var22 >>> 24;
                  var11 += var23;
                  var13 += (var22 >> 16 & 0xFF) * var23;
                  var15 += (var22 >> 8 & 0xFF) * var23;
                  var17 += (var22 & 0xFF) * var23;
                  var19++;
               }
            }

            var4[var5 * var1 + var8] = var11 == 0L
               ? 0
               : (int)(var11 / var19) << 24 | (int)(var13 / var11) << 16 | (int)(var15 / var11) << 8 | (int)(var17 / var11);
         }
      }

      return var4;
   }

   private static final class v extends ReloadableTexture {
      private final zd.v b;
      private final int t;

      v(Identifier var1, zd.v var2, int var3) {
         super(var1);
         this.b = var2;
         this.t = var3;
      }

      public TextureContents loadContents(ResourceManager var1) {
         int[] var2 = wl.G(this.b, this.t);
         NativeImage var3 = new NativeImage(this.t, this.t, false);

         try {
            double var4 = this.t / 2.0;

            for (int var6 = 0; var6 < this.t; var6++) {
               for (int var7 = 0; var7 < this.t; var7++) {
                  double var8 = var4 - Math.hypot(var7 + 0.5 - var4, var6 + 0.5 - var4);
                  int var10 = var2[var6 * this.t + var7];
                  int var11 = (int)Math.round((var10 >>> 24) * Math.clamp(var8 + 0.5, 0.0, 1.0));
                  var3.setPixel(var7, var6, var11 << 24 | var10 & 16777215);
               }
            }

            return new TextureContents(var3, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var12) {
            var3.close();
            throw var12;
         }
      }
   }
}
