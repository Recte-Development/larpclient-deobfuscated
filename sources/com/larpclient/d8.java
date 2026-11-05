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

final class d8 {
   static final int J = 96;
   static final int D = 38;
   static final int B = 4;
   private static final int U = -((int)Math.ceil(96.0 * Math.sqrt(0.5))) - 1;
   private static final int I = -97;
   private static final int Z = -U * 2;
   private static final int Y = 97 - (int)Math.floor(38.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier k = Identifier.fromNamespaceAndPath("larp-client", "emote/wheel_wedge");
   private static d8.t d;
   private static TextureManager M;
   private static int E;

   private d8() {
   }

   static double x(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 38.0, 96.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static d8.t I() {
      TextureManager var0 = Minecraft.getInstance().getTextureManager();
      if (var0 != M) {
         d = null;
         E = 0;
         M = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
      if (var1 == E) {
         return null;
      }

      if (d == null || d.e != var1) {
         d8.t var2 = null;

         try {
            var2 = new d8.t(var1);
            var0.registerAndLoad(k, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            E = var1;
            mt.B("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         d = var2;
         mt.U("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return d;
   }

   static void i(GuiGraphicsExtractor var0, int var1, double var2, int var4, int var5) {
      d8.t var6 = I();
      if (var6 == null) {
         double var7 = (var1 - 1) * Math.PI / 2.0;
         byte var9 = 67;
         c7.K(var0, (int)Math.round(Math.cos(var7) * var9), (int)Math.round(Math.sin(var7) * var9), 22, var4);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().rotate((float)(var1 * Math.PI / 2.0));
            var0.pose().translate(0.0F, (float)(-var2));
            var0.blit(RenderPipelines.GUI_TEXTURED, k, U, -97, 0.0F, 0.0F, Z, Y, var6.w, var6.F, var6.w, var6.F * 2, de.h(var4));
            if (var5 >>> 24 != 0) {
               var0.blit(RenderPipelines.GUI_TEXTURED, k, U, -97, 0.0F, var6.F, Z, Y, var6.w, var6.F, var6.w, var6.F * 2, de.h(var5));
            }
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static final class t extends ReloadableTexture {
      final int e;
      final int w;
      final int F;

      t(int var1) {
         super(d8.k);
         this.e = var1;
         this.w = d8.Z * var1;
         this.F = d8.Y * var1;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.w, this.F * 2, false);

         try {
            for (int var3 = 0; var3 < this.F; var3++) {
               for (int var4 = 0; var4 < this.w; var4++) {
                  double var5 = d8.U + (var4 + 0.5) / this.e;
                  double var7 = -97.0 + (var3 + 0.5) / this.e;
                  double var9 = d8.x(var5, var7);
                  double var11 = this.a(var9);
                  double var13 = this.a(var9) - this.a(var9 - 1.1);
                  var2.setPixel(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.F, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var15) {
            var2.close();
            throw var15;
         }
      }

      private double a(double var1) {
         return Math.clamp(var1 * this.e + 0.5, 0.0, 1.0);
      }
   }
}
