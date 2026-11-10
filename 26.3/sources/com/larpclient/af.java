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

final class af {
   static final int P = 116;
   static final int s = 48;
   static final int R = 4;
   private static final double W = 3.5;
   private static final int p = -((int)Math.ceil(116.0 * Math.sqrt(0.5))) - 1;
   private static final int h = -117;
   private static final int w = -p * 2;
   private static final int O = 117 - (int)Math.floor(48.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier n = Identifier.fromNamespaceAndPath("larp-client", "emote/wheel_wedge");
   private static af.d r;
   private static TextureManager d;
   private static int j;

   private af() {
   }

   static double T(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 48.0, 116.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static af.d f() {
      TextureManager var0 = Minecraft.getInstance().getTextureManager();
      if (var0 != d) {
         r = null;
         j = 0;
         d = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
      if (var1 == j) {
         return null;
      }

      if (r == null || r.u != var1) {
         af.d var2 = null;

         try {
            var2 = new af.d(var1);
            var0.registerAndLoad(n, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            j = var1;
            _q.g("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         r = var2;
         _q.c("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return r;
   }

   static void P(GuiGraphicsExtractor var0, int var1, double var2, int var4, int var5, int var6) {
      af.d var7 = f();
      if (var7 == null) {
         double var8 = (var1 - 1) * Math.PI / 2.0;
         byte var10 = 82;
         h5.M(var0, (int)Math.round(Math.cos(var8) * var10), (int)Math.round(Math.sin(var8) * var10), 22, var4);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().rotate((float)(var1 * Math.PI / 2.0));
            var0.pose().translate(0.0F, (float)(-var2));
            D(var0, var7, 0, var4);
            if (var5 >>> 24 != 0) {
               D(var0, var7, 1, var5);
            }

            if (var6 >>> 24 != 0) {
               D(var0, var7, 2, var6);
            }
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void D(GuiGraphicsExtractor var0, af.d var1, int var2, int var3) {
      var0.blit(RenderPipelines.GUI_TEXTURED, n, p, -117, 0.0F, var1.T * var2, w, O, var1.X, var1.T, var1.X, var1.T * 3, oa.e(var3));
   }

   private static final class d extends ReloadableTexture {
      final int u;
      final int X;
      final int T;

      d(int var1) {
         super(af.n);
         this.u = var1;
         this.X = af.w * var1;
         this.T = af.O * var1;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.X, this.T * 3, false);

         try {
            for (int var3 = 0; var3 < this.T; var3++) {
               for (int var4 = 0; var4 < this.X; var4++) {
                  double var5 = af.p + (var4 + 0.5) / this.u;
                  double var7 = -117.0 + (var3 + 0.5) / this.u;
                  double var9 = af.T(var5, var7);
                  double var11 = this.d(var9);
                  double var13 = this.d(var9) - this.d(var9 - 1.1);
                  double var15 = Math.min(var11, this.d(Math.hypot(var5, var7) - 112.5));
                  var2.setPixel(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.T, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.T * 2, (int)Math.round(var15 * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var17) {
            var2.close();
            throw var17;
         }
      }

      private double d(double var1) {
         return Math.clamp(var1 * this.u + 0.5, 0.0, 1.0);
      }
   }
}
