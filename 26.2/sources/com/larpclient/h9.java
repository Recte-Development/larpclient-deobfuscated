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

final class h9 {
   static final int o = 116;
   static final int i = 48;
   static final int b = 4;
   private static final double l = 3.5;
   private static final int G = -((int)Math.ceil(116.0 * Math.sqrt(0.5))) - 1;
   private static final int y = -117;
   private static final int D = -G * 2;
   private static final int g = 117 - (int)Math.floor(48.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier u = Identifier.fromNamespaceAndPath("larp-client", "emote/wheel_wedge");
   private static h9.l h;
   private static TextureManager x;
   private static int d;

   private h9() {
   }

   static double i(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 48.0, 116.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static h9.l B() {
      TextureManager var0 = Minecraft.getInstance().getTextureManager();
      if (var0 != x) {
         h = null;
         d = 0;
         x = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
      if (var1 == d) {
         return null;
      }

      if (h == null || h.f != var1) {
         h9.l var2 = null;

         try {
            var2 = new h9.l(var1);
            var0.registerAndLoad(u, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            d = var1;
            com.larpclient.h.w("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         h = var2;
         com.larpclient.h.U("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return h;
   }

   static void v(GuiGraphicsExtractor var0, int var1, double var2, int var4, int var5, int var6) {
      h9.l var7 = B();
      if (var7 == null) {
         double var8 = (var1 - 1) * Math.PI / 2.0;
         byte var10 = 82;
         kv.Q(var0, (int)Math.round(Math.cos(var8) * var10), (int)Math.round(Math.sin(var8) * var10), 22, var4);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().rotate((float)(var1 * Math.PI / 2.0));
            var0.pose().translate(0.0F, (float)(-var2));
            i(var0, var7, 0, var4);
            if (var5 >>> 24 != 0) {
               i(var0, var7, 1, var5);
            }

            if (var6 >>> 24 != 0) {
               i(var0, var7, 2, var6);
            }
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void i(GuiGraphicsExtractor var0, h9.l var1, int var2, int var3) {
      var0.blit(RenderPipelines.GUI_TEXTURED, u, G, -117, 0.0F, var1.j * var2, D, g, var1.n, var1.j, var1.n, var1.j * 3, ua.I(var3));
   }

   private static final class l extends ReloadableTexture {
      final int f;
      final int n;
      final int j;

      l(int var1) {
         super(h9.u);
         this.f = var1;
         this.n = h9.D * var1;
         this.j = h9.g * var1;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.n, this.j * 3, false);

         try {
            for (int var3 = 0; var3 < this.j; var3++) {
               for (int var4 = 0; var4 < this.n; var4++) {
                  double var5 = h9.G + (var4 + 0.5) / this.f;
                  double var7 = -117.0 + (var3 + 0.5) / this.f;
                  double var9 = h9.i(var5, var7);
                  double var11 = this.U(var9);
                  double var13 = this.U(var9) - this.U(var9 - 1.1);
                  double var15 = Math.min(var11, this.U(Math.hypot(var5, var7) - 112.5));
                  var2.setPixel(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.j, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.j * 2, (int)Math.round(var15 * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var17) {
            var2.close();
            throw var17;
         }
      }

      private double U(double var1) {
         return Math.clamp(var1 * this.f + 0.5, 0.0, 1.0);
      }
   }
}
