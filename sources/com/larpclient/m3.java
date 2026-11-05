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

public final class m3 {
   private static final m3.o Z = new m3.o("live");
   private static final m3.o E = new m3.o("preview");

   private m3() {
   }

   private static m3.d R(m3.o var0, x5.p var1) {
      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var0.k != var2) {
         var0.v = null;
         var0.X = null;
         var0.k = var2;
      }

      if (var1.equals(var0.X)) {
         return null;
      }

      if (var0.v == null || !var0.v.w.equals(var1)) {
         m3.d var3 = null;

         try {
            var3 = new m3.d(var0.q, var1);
            var2.registerAndLoad(var0.q, var3);
         } catch (RuntimeException var5) {
            if (var3 != null) {
               var3.close();
            }

            var0.X = var1;
            mt.B("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
            return null;
         }

         var0.v = var3;
         var0.X = null;
         mt.U("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
      }

      return var0.v;
   }

   public static void x(
      GuiGraphicsExtractor var0, int var1, int var2, String var3, int var4, int var5, dp var6, int var7, boolean var8, double var9, boolean var11
   ) {
      if (var7 >>> 24 != 0) {
         m3.o var12 = var11 ? E : Z;
         double var13 = Minecraft.getInstance().getWindow().getGuiScale();
         double var15 = x5.R(Math.hypot(var0.pose().m00(), var0.pose().m01()) * var13);
         x5.p var17 = new x5.p(var3, var4, var5, var8, var15, var6);
         m3.d var18 = R(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            c7.s(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            c7.s(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var1, var2);
               double var19 = var18.h % 2 == 0 ? 0.0 : 0.5;
               double var21 = var0.pose().m20() * var13;
               double var23 = var0.pose().m21() * var13;
               var0.pose()
                  .translateLocal((float)((Math.round(var21 - var19) + var19 - var21) / var13), (float)((Math.round(var23 - var19) + var19 - var23) / var13));
               if (var9 != 0.0) {
                  var0.pose().rotate((float)var9);
               }

               var0.pose().scale((float)(1.0 / var15));
               var0.pose().translate(-var18.h / 2.0F, -var18.P / 2.0F);
               j(var0, var12.q, var18, var7, var8);
            } finally {
               var0.pose().popMatrix();
            }
         }
      }
   }

   public static void t(GuiGraphicsExtractor var0, int var1, int var2, x1.b var3, int var4, int var5, double var6) {
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
               de.h(var5 | 16777215)
            );
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static int G(x1.b var0, int var1, boolean var2) {
      double var3 = 2.0 * var1 / Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot(var0.width(), var0.height()) * var3 / 2.0 : var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void j(GuiGraphicsExtractor var0, Identifier var1, m3.d var2, int var3, boolean var4) {
      if (var4) {
         var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, var2.P, var2.h, var2.P, var2.h, var2.P, var2.h, var2.P * 2, de.h(var3 & 0xFF000000));
      }

      var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, 0.0F, var2.h, var2.P, var2.h, var2.P, var2.h, var2.P * 2, de.h(var3));
   }

   private static final class d extends ReloadableTexture {
      final x5.p w;
      final int h;
      final int P;
      final x5.l s;

      d(Identifier var1, x5.p var2) {
         super(var1);
         this.w = var2;
         this.s = new x5(var2).b();
         this.h = this.s.width();
         this.P = this.s.height();
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.h, this.P * 2, false);

         try {
            for (int var3 = 0; var3 < this.P * 2; var3++) {
               for (int var4 = 0; var4 < this.h; var4++) {
                  var2.setPixel(var4, var3, this.s.pixels()[var3 * this.h + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   private static final class o {
      final Identifier q;
      m3.d v;
      TextureManager k;
      x5.p X;

      o(String var1) {
         this.q = Identifier.fromNamespaceAndPath("larp-client", "crosshair/" + var1);
      }
   }
}
