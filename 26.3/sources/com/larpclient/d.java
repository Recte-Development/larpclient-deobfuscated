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

public final class d {
   private static final d.p m = new d.p("live");
   private static final d.p r = new d.p("preview");

   private d() {
   }

   private static d.b v(d.p var0, z.q var1) {
      TextureManager var2 = Minecraft.getInstance().getTextureManager();
      if (var0.Z != var2) {
         var0.v = null;
         var0.S = null;
         var0.Z = var2;
      }

      if (var1.equals(var0.S)) {
         return null;
      }

      if (var0.v == null || !var0.v.t.equals(var1)) {
         d.b var3 = null;

         try {
            var3 = new d.b(var0.W, var1);
            var2.registerAndLoad(var0.W, var3);
         } catch (RuntimeException var5) {
            if (var3 != null) {
               var3.close();
            }

            var0.S = var1;
            _q.g("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
            return null;
         }

         var0.v = var3;
         var0.S = null;
         _q.c("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
      }

      return var0.v;
   }

   public static void C(
      GuiGraphicsExtractor var0, int var1, int var2, String var3, int var4, int var5, qj var6, int var7, boolean var8, double var9, boolean var11
   ) {
      if (var7 >>> 24 != 0) {
         d.p var12 = var11 ? r : m;
         double var13 = Minecraft.getInstance().getWindow().getGuiScale();
         double var15 = z.X(Math.hypot(var0.pose().m00(), var0.pose().m01()) * var13);
         z.q var17 = new z.q(var3, var4, var5, var8, var15, var6);
         d.b var18 = v(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            h5.N(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            h5.N(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var1, var2);
               double var19 = var18.Q % 2 == 0 ? 0.0 : 0.5;
               double var21 = var0.pose().m20() * var13;
               double var23 = var0.pose().m21() * var13;
               var0.pose()
                  .translateLocal((float)((Math.round(var21 - var19) + var19 - var21) / var13), (float)((Math.round(var23 - var19) + var19 - var23) / var13));
               if (var9 != 0.0) {
                  var0.pose().rotate((float)var9);
               }

               var0.pose().scale((float)(1.0 / var15));
               var0.pose().translate(-var18.Q / 2.0F, -var18.h / 2.0F);
               B(var0, var12.W, var18, var7, var8);
            } finally {
               var0.pose().popMatrix();
            }
         }
      }
   }

   public static void s(GuiGraphicsExtractor var0, int var1, int var2, oc.j var3, int var4, int var5, double var6) {
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
               oa.e(var5 | 16777215)
            );
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static int D(oc.j var0, int var1, boolean var2) {
      double var3 = 2.0 * var1 / Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot(var0.width(), var0.height()) * var3 / 2.0 : var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void B(GuiGraphicsExtractor var0, Identifier var1, d.b var2, int var3, boolean var4) {
      if (var4) {
         var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, var2.h, var2.Q, var2.h, var2.Q, var2.h, var2.Q, var2.h * 2, oa.e(var3 & 0xFF000000));
      }

      var0.blit(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, 0.0F, var2.Q, var2.h, var2.Q, var2.h, var2.Q, var2.h * 2, oa.e(var3));
   }

   private static final class b extends ReloadableTexture {
      final z.q t;
      final int Q;
      final int h;
      final z.l Y;

      b(Identifier var1, z.q var2) {
         super(var1);
         this.t = var2;
         this.Y = new z(var2).I();
         this.Q = this.Y.width();
         this.h = this.Y.height();
      }

      public TextureContents loadContents(ResourceManager var1) {
         NativeImage var2 = new NativeImage(this.Q, this.h * 2, false);

         try {
            for (int var3 = 0; var3 < this.h * 2; var3++) {
               for (int var4 = 0; var4 < this.Q; var4++) {
                  var2.setPixel(var4, var3, this.Y.pixels()[var3 * this.Q + var4]);
               }
            }

            return new TextureContents(var2, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }
      }
   }

   private static final class p {
      final Identifier W;
      d.b v;
      TextureManager Z;
      z.q S;

      p(String var1) {
         this.W = Identifier.fromNamespaceAndPath("larp-client", "crosshair/" + var1);
      }
   }
}
