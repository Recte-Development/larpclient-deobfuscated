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

public final class pm {
   private static final pm.s I = new pm.s("live");
   private static final pm.s K = new pm.s("preview");

   private pm() {
   }

   private static pm.q m(pm.s var0, sb.k var1) {
      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var0.J != var2) {
         var0.b = null;
         var0.c = null;
         var0.J = var2;
      }

      if (var1.equals(var0.c)) {
         return null;
      }

      if (var0.b == null || !var0.b.z.equals(var1)) {
         pm.q var3 = null;

         try {
            var3 = new pm.q(var0.X, var1);
            var2.registerAndLoad(var0.X, var3);
         } catch (RuntimeException var5) {
            if (var3 != null) {
               var3.close();
            }

            var0.c = var1;
            h.w("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
            return null;
         }

         var0.b = var3;
         var0.c = null;
         h.U("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
      }

      return var0.b;
   }

   public static void m(
      GuiGraphicsExtractor var0, int var1, int var2, String var3, int var4, int var5, kc var6, int var7, boolean var8, double var9, boolean var11
   ) {
      if (var7 >>> 24 != 0) {
         pm.s var12 = var11 ? K : I;
         double var13 = Minecraft.getInstance().getWindow().getGuiScale();
         double var15 = sb.z(Math.hypot(var0.pose().m00(), var0.pose().m01()) * var13);
         sb.k var17 = new sb.k(var3, var4, var5, var8, var15, var6);
         pm.q var18 = m(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            kv.d(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            kv.d(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var1, var2);
               double var19 = var18.I % 2 == 0 ? 0.0 : 0.5;
               double var21 = var0.pose().m20() * var13;
               double var23 = var0.pose().m21() * var13;
               var0.pose()
                  .translateLocal((float)((Math.round(var21 - var19) + var19 - var21) / var13), (float)((Math.round(var23 - var19) + var19 - var23) / var13));
               if (var9 != 0.0) {
                  var0.pose().rotate((float)var9);
               }

               var0.pose().scale((float)(1.0 / var15));
               var0.pose().translate(-var18.I / 2.0F, -var18.h / 2.0F);
               L(var0, var12.X, var18, var7, var8);
            } finally {
               var0.pose().popMatrix();
            }
         }
      }
   }

   public static void U(GuiGraphicsExtractor var0, int var1, int var2, zd.e var3, int var4, int var5, double var6) {
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
               var3.id(),
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
               ua.I(var5 | 16777215)
            );
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static int t(zd.e var0, int var1, boolean var2) {
      double var3 = 2.0 * var1 / Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot(var0.width(), var0.height()) * var3 / 2.0 : var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void L(GuiGraphicsExtractor var0, Identifier var1, pm.q var2, int var3, boolean var4) {
      if (var4) {
         var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, var2.h, var2.I, var2.h, var2.I, var2.h, var2.I, var2.h * 2, ua.I(var3 & 0xFF000000));
      }

      var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, 0.0F, var2.I, var2.h, var2.I, var2.h, var2.I, var2.h * 2, ua.I(var3));
   }

   private static final class q extends ReloadableTexture {
      final sb.k z;
      final int I;
      final int h;
      final sb.t J;

      q(Identifier var1, sb.k var2) {
         super(var1);
         this.z = var2;
         this.J = new sb(var2).y();
         this.I = this.J.width();
         this.h = this.J.height();
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.I, this.h * 2, false);

         try {
            for (int var3 = 0; var3 < this.h * 2; var3++) {
               for (int var4 = 0; var4 < this.I; var4++) {
                  var2.setPixel(var4, var3, this.J.pixels()[var3 * this.I + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   private static final class s {
      final Identifier X;
      pm.q b;
      TextureManager J;
      sb.k c;

      s(String var1) {
         this.X = Identifier.fromNamespaceAndPath("larp-client", "crosshair/" + var1);
      }
   }
}
