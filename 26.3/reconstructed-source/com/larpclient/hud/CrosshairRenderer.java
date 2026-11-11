package com.larpclient.hud;

import com.larpclient.gui.misc.DrawingGrid;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.CrosshairImages;
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

public final class CrosshairRenderer {
   private static final CrosshairRenderer.MaskSlot liveMask = new CrosshairRenderer.MaskSlot("live");
   private static final CrosshairRenderer.MaskSlot previewMask = new CrosshairRenderer.MaskSlot("preview");

   private CrosshairRenderer() {
   }

   private static CrosshairRenderer.MaskTexture prepareMask(CrosshairRenderer.MaskSlot var0, CrosshairShape.CrosshairGeometry var1) {
      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var0.textureManager != var2) {
         var0.texture = null;
         var0.failedKey = null;
         var0.textureManager = var2;
      }

      if (var1.equals(var0.failedKey)) {
         return null;
      }

      if (var0.texture == null || !var0.texture.key.equals(var1)) {
         CrosshairRenderer.MaskTexture var3 = null;

         try {
            var3 = new CrosshairRenderer.MaskTexture(var0.textureId, var1);
            var2.registerAndLoad(var0.textureId, var3);
         } catch (RuntimeException var5) {
            if (var3 != null) {
               var3.close();
            }

            var0.failedKey = var1;
            LarpLog.errorOnce("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
            return null;
         }

         var0.texture = var3;
         var0.failedKey = null;
         LarpLog.debug("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
      }

      return var0.texture;
   }

   public static void drawCrosshair(
      GuiGraphicsExtractor var0, int var1, int var2, String var3, int var4, int var5, DrawingGrid var6, int var7, boolean var8, double var9, boolean var11
   ) {
      if (var7 >>> 24 != 0) {
         CrosshairRenderer.MaskSlot var12 = var11 ? previewMask : liveMask;
         double var13 = Minecraft.getInstance().getWindow().getGuiScale();
         double var15 = CrosshairShape.clampScale(Math.hypot(var0.pose().m00(), var0.pose().m01()) * var13);
         CrosshairShape.CrosshairGeometry var17 = new CrosshairShape.CrosshairGeometry(var3, var4, var5, var8, var15, var6);
         CrosshairRenderer.MaskTexture var18 = prepareMask(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            DrawUtil.fillRect(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            DrawUtil.fillRect(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var1, var2);
               double var19 = var18.width % 2 == 0 ? 0.0 : 0.5;
               double var21 = var0.pose().m20() * var13;
               double var23 = var0.pose().m21() * var13;
               var0.pose()
                  .translateLocal((float)((Math.round(var21 - var19) + var19 - var21) / var13), (float)((Math.round(var23 - var19) + var19 - var23) / var13));
               if (var9 != 0.0) {
                  var0.pose().rotate((float)var9);
               }

               var0.pose().scale((float)(1.0 / var15));
               var0.pose().translate(-var18.width / 2.0F, -var18.height / 2.0F);
               blitMask(var0, var12.textureId, var18, var7, var8);
            } finally {
               var0.pose().popMatrix();
            }
         }
      }
   }

   public static void drawImageCrosshair(GuiGraphicsExtractor var0, int var1, int var2, CrosshairImages.TextureHandle var3, int var4, int var5, double var6) {
      if (var5 >>> 24 != 0) {
         double var8 = 2.0 * var4 / Math.max(var3.width(), var3.height());
         float var10 = (float)(var3.width() * var8);
         float var11 = (float)(var3.height() * var8);
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2);
            if (var6 != 0.0) {
               var0.pose().rotate((float)var6);
            }

            var0.pose().scale(var10 / 64.0F, var11 / 64.0F);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               var3.textureId(),
               -32,
               -32,
               0.0F,
               0.0F,
               64,
               64,
               var3.width(),
               var3.height(),
               var3.width(),
               var3.height(),
               AlphaScope.applyAlpha(var5 | 16777215)
            );
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static int imageRadius(CrosshairImages.TextureHandle var0, int var1, boolean var2) {
      double var3 = 2.0 * var1 / Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot(var0.width(), var0.height()) * var3 / 2.0 : var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void blitMask(GuiGraphicsExtractor var0, Identifier var1, CrosshairRenderer.MaskTexture var2, int var3, boolean var4) {
      if (var4) {
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            var1,
            0,
            0,
            0.0F,
            var2.height,
            var2.width,
            var2.height,
            var2.width,
            var2.height,
            var2.width,
            var2.height * 2,
            AlphaScope.applyAlpha(var3 & 0xFF000000)
         );
      }

      var0.blit(
         RenderPipelines.GUI_TEXTURED,
         var1,
         0,
         0,
         0.0F,
         0.0F,
         var2.width,
         var2.height,
         var2.width,
         var2.height,
         var2.width,
         var2.height * 2,
         AlphaScope.applyAlpha(var3)
      );
   }

   private static final class MaskSlot {
      final Identifier textureId;
      CrosshairRenderer.MaskTexture texture;
      TextureManager textureManager;
      CrosshairShape.CrosshairGeometry failedKey;

      MaskSlot(String var1) {
         this.textureId = Identifier.fromNamespaceAndPath("larp-client", "crosshair/" + var1);
      }
   }

   private static final class MaskTexture extends ReloadableTexture {
      final CrosshairShape.CrosshairGeometry key;
      final int width;
      final int height;
      final CrosshairShape.RenderedCrosshair image;

      MaskTexture(Identifier var1, CrosshairShape.CrosshairGeometry var2) {
         super(var1);
         this.key = var2;
         this.image = new CrosshairShape(var2).render();
         this.width = this.image.width();
         this.height = this.image.height();
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.width, this.height * 2, false);

         try {
            for (int var3 = 0; var3 < this.height * 2; var3++) {
               for (int var4 = 0; var4 < this.width; var4++) {
                  var2.setPixel(var4, var3, this.image.pixels()[var3 * this.width + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }
}
