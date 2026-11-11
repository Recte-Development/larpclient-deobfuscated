package com.larpclient.social.emote;

import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.DrawUtil;
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

final class EmoteWheelWedge {
   static final int oUTER_RADIUS = 116;
   static final int iNNER_RADIUS = 48;
   static final int gAP = 4;
   private static final double eDGE_SOFTNESS = 3.5;
   private static final int minX = -((int)Math.ceil(116.0 * Math.sqrt(0.5))) - 1;
   private static final int minY = -117;
   private static final int width = -minX * 2;
   private static final int height = 117 - (int)Math.floor(48.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier tEXTURE_ID = Identifier.fromNamespaceAndPath("larp-client", "emote/wheel_wedge");
   private static EmoteWheelWedge.WedgeTexture texture;
   private static TextureManager textureManager;
   private static int failedScale;

   private EmoteWheelWedge() {
   }

   static double wedgeDistance(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 48.0, 116.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static EmoteWheelWedge.WedgeTexture ensureTexture() {
      TextureManager var0 = Minecraft.getInstance().getTextureManager();
      if (var0 != textureManager) {
         texture = null;
         failedScale = 0;
         textureManager = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
      if (var1 == failedScale) {
         return null;
      }

      if (texture == null || texture.scale != var1) {
         EmoteWheelWedge.WedgeTexture var2 = null;

         try {
            var2 = new EmoteWheelWedge.WedgeTexture(var1);
            var0.registerAndLoad(tEXTURE_ID, var2);
         } catch (RuntimeException var4) {
            if (var2 != null) {
               var2.close();
            }

            failedScale = var1;
            LarpLog.errorOnce("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
            return null;
         }

         texture = var2;
         LarpLog.debug("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
      }

      return texture;
   }

   static void drawWedge(GuiGraphicsExtractor var0, int var1, double var2, int var4, int var5, int var6) {
      EmoteWheelWedge.WedgeTexture var7 = ensureTexture();
      if (var7 == null) {
         double var8 = (var1 - 1) * Math.PI / 2.0;
         byte var10 = 82;
         DrawUtil.fillCircle(var0, (int)Math.round(Math.cos(var8) * var10), (int)Math.round(Math.sin(var8) * var10), 22, var4);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().rotate((float)(var1 * Math.PI / 2.0));
            var0.pose().translate(0.0F, (float)(-var2));
            blitLayer(var0, var7, 0, var4);
            if (var5 >>> 24 != 0) {
               blitLayer(var0, var7, 1, var5);
            }

            if (var6 >>> 24 != 0) {
               blitLayer(var0, var7, 2, var6);
            }
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void blitLayer(GuiGraphicsExtractor var0, EmoteWheelWedge.WedgeTexture var1, int var2, int var3) {
      var0.blit(
         RenderPipelines.GUI_TEXTURED,
         tEXTURE_ID,
         minX,
         -117,
         0.0F,
         var1.layerHeight * var2,
         width,
         height,
         var1.textureWidth,
         var1.layerHeight,
         var1.textureWidth,
         var1.layerHeight * 3,
         AlphaScope.applyAlpha(var3)
      );
   }

   private static final class WedgeTexture extends ReloadableTexture {
      final int scale;
      final int textureWidth;
      final int layerHeight;

      WedgeTexture(int var1) {
         super(EmoteWheelWedge.tEXTURE_ID);
         this.scale = var1;
         this.textureWidth = EmoteWheelWedge.width * var1;
         this.layerHeight = EmoteWheelWedge.height * var1;
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.textureWidth, this.layerHeight * 3, false);

         try {
            for (int var3 = 0; var3 < this.layerHeight; var3++) {
               for (int var4 = 0; var4 < this.textureWidth; var4++) {
                  double var5 = EmoteWheelWedge.minX + (var4 + 0.5) / this.scale;
                  double var7 = -117.0 + (var3 + 0.5) / this.scale;
                  double var9 = EmoteWheelWedge.wedgeDistance(var5, var7);
                  double var11 = this.upload(var9);
                  double var13 = this.upload(var9) - this.upload(var9 - 1.1);
                  double var15 = Math.min(var11, this.upload(Math.hypot(var5, var7) - 112.5));
                  var2.setPixel(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.layerHeight, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
                  var2.setPixel(var4, var3 + this.layerHeight * 2, (int)Math.round(var15 * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var17) {
            var2.close();
            throw var17;
         }
      }

      private double upload(double var1) {
         return Math.clamp(var1 * this.scale + 0.5, 0.0, 1.0);
      }
   }
}
