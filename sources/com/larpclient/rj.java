package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
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

final class rj {
   static final int Y = 96;
   static final int i = 38;
   static final int G = 4;
   private static final int w = -((int)Math.ceil(96.0 * Math.sqrt(0.5))) - 1;
   private static final int T = -97;
   private static final int m = -w * 2;
   private static final int v = 97 - (int)Math.floor(38.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier b = Identifier.fromNamespaceAndPath("larp-client", "emote/wheel_wedge");
   private static rj.k j;
   private static TextureManager U;
   private static int d;

   private rj() {
   }

   static double E(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 38.0, 96.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static rj.k n() {
      TextureManager var0 = Minecraft.getInstance().getTextureManager();
      if (var0 != U) {
         j = null;
         d = 0;
         U = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
      if (var1 == d) {
         return null;
      }

      if (j == null || j.F != var1) {
         rj.k var2 = null;

         try {
            var2 = new rj.k(var1);
            var0.registerAndLoad(b, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            d = var1;
            r4.T("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         j = var2;
         r4.g("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return j;
   }

   static void j(GuiGraphicsExtractor var0, int var1, double var2, int var4, int var5) {
      rj.k var6 = n();
      if (var6 == null) {
         double var7 = (var1 - 1) * Math.PI / 2.0;
         byte var9 = 67;
         fr.J(var0, (int)Math.round(Math.cos(var7) * var9), (int)Math.round(Math.sin(var7) * var9), 22, var4);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().rotate((float)(var1 * Math.PI / 2.0));
            var0.pose().translate(0.0F, (float)(-var2));
            var0.blit(RenderPipelines.GUI_TEXTURED, b, w, -97, 0.0F, 0.0F, m, v, var6.G, var6.h, var6.G, var6.h * 2, r.D(var4));
            if (var5 >>> 24 != 0) {
               var0.blit(RenderPipelines.GUI_TEXTURED, b, w, -97, 0.0F, var6.h, m, v, var6.G, var6.h, var6.G, var6.h * 2, r.D(var5));
            }
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static final class k extends ReloadableTexture {
      final int F;
      final int G;
      final int h;

      k(int var1) {
         super(rj.b);
         this.F = var1;
         this.G = rj.m * var1;
         this.h = rj.v * var1;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.G, this.h * 2, false);

         try {
            for (int var3 = 0; var3 < this.h; var3++) {
               for (int var4 = 0; var4 < this.G; var4++) {
                  double var5 = rj.w + (var4 + 0.5) / this.F;
                  double var7 = -97.0 + (var3 + 0.5) / this.F;
                  double var9 = rj.E(var5, var7);
                  double var11 = this.n(var9);
                  double var13 = this.n(var9) - this.n(var9 - 1.1);
                  var2.setPixel(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.h, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var15) {
            var2.close();
            throw var15;
         }
      }

      private double n(double var1) {
         return Math.clamp(var1 * this.F + 0.5, 0.0, 1.0);
      }
   }
}
