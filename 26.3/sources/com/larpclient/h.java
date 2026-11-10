package com.larpclient;

import com.larpclient.module.MenuBackgroundModule;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class h {
   private static final Identifier B = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final Identifier c = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo-hd.png");
   private static final ji C = new ji();
   private static int D = -1;
   private static boolean g;
   private static boolean H;
   private static boolean j;
   private static final Set<Identifier> k = new HashSet<>();
   private static Identifier l;

   private h() {
   }

   public static Identifier b() {
      if (D != nl.q) {
         D = nl.q;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();
         g = var0.getResource(B).isPresent();
         H = var0.getResource(c).isPresent();
         if (!g) {
            _q.R("Menu", "Logo texture missing; monochrome waves remain available, retry after resource reload");
         }

         if (!H) {
            _q.R("Menu", "High-resolution logo missing; using the 64px logo, retry after resource reload");
         }

         if (H) {
            K(c);
         }

         l = null;
         j = (boolean)0;
      }

      Identifier var1 = H ? c : B;
      if (!var1.equals(l)) {
         l = var1;
         _q.z("Menu", "Menu logo: {}; standard backdrop and navigation retained", var1);
      }

      return var1;
   }

   private static void K(Identifier var0) {
      if (k.add(var0)) {
         long var1 = System.nanoTime();

         try {
            Minecraft.getInstance().getTextureManager().registerAndLoad(var0, new wk(var0));
            _q.z("Menu", "Mipmapped {} in {} ms", var0, (System.nanoTime() - var1) / 1000000L);
         } catch (RuntimeException var4) {
            _q.R("Menu", "Mipmapped {} unavailable; Minecraft loads it with single-level sampling", var0, var4);
         }
      }
   }

   public static void z(GuiGraphicsExtractor var0, int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var3 = Minecraft.getInstance();
         i3 var4 = LarpClient.N().h().k();
         int var5 = var4.w && var4.Jw != ng.c.T ? 1 : 0;
         Identifier var6 = b();
         double var7 = var3.gui.screen() instanceof de var9 ? var9.X().e() : 1.0;
         C.b(System.nanoTime(), (boolean)var5, (Double)var3.options.panoramaSpeed().get());
         var0.fill(0, 0, var1, var2, vv.Q(-16777216, -16185079, var7));
         boolean var20 = MenuBackgroundModule.E(var0, var1, var2, var7);
         var0.nextStratum();
         float var21 = 1 / Math.max(1, var3.getWindow().getGuiScale());

         for (int var11 = 0; var11 < 2 && (!var20 || MenuBackgroundModule.U()); var11++) {
            int var12 = var11 == 1 ? 1 : 0;
            float var13 = (float)((1.0 - var7) * ji.y(var1, var2) * (var12 != 0 ? 1 : -1));
            Matrix3x2f var14 = new Matrix3x2f(var0.pose()).translate(var13, 0.0F);
            int var15 = (int)Math.ceil(ji.y(var1, var2) + var21);
            ScreenRectangle var16 = new ScreenRectangle(var12 != 0 ? var1 - var15 : 0, 0, var15, var2).transformMaxBounds(var14);
            if (var0.scissorStack.peek() != null) {
               var16 = var0.scissorStack.peek().intersection(var16);
            }

            var0.guiRenderState.addGuiElement(new h.q(var14, var1, var2, (boolean)var12, C.G(), var21, var0.scissorStack.peek(), var16));
         }

         if ((!var6.equals(B) || g) && var4.t) {
            var0.nextStratum();

            for (int var22 = 0; var22 < 18; var22++) {
               ji.g var23 = C.A(var22, var1, var2);
               if (!(var23.alpha() < 0.001)) {
                  int var24 = (int)Math.round(var23.size());
                  var0.pose().pushMatrix();

                  try {
                     double var25 = (1.0 - var7) * ji.y(var1, var2) * (var22 % 2 == 0 ? -1 : 1);
                     var0.pose().translate((float)(var23.x() + var25), (float)var23.y());
                     var0.pose().rotate((float)var23.rotation());
                     var0.blit(
                        RenderPipelines.GUI_TEXTURED, var6, -var24 / 2, -var24 / 2, 0.0F, 0.0F, var24, var24, 64, 64, 64, 64, vv.N(-1, var23.alpha() * var7)
                     );
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }
         }

         if (!j) {
            j = (boolean)1;
            _q.z("Menu", "Lit monochrome side waves ready; {} logo slots, shaded folds, Minecraft panorama speed controls motion", 18);
         }
      }
   }

   public static void z() {
      D = -1;
      l = null;
      j = (boolean)0;
   }

   record q(Matrix3x2fc pose, int width, int height, boolean right, double time, float feather, ScreenRectangle scissorArea, ScreenRectangle bounds)
      implements GuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = (float)ji.y(this.width, this.height);
         float var3 = this.right ? this.width : 0.0F;

         for (int var4 = 0; var4 < 6; var4++) {
            for (int var5 = 0; var5 < 96; var5++) {
               double var6 = var5 / 96.0;
               double var8 = (var5 + 1.0) / 96.0;
               float var10 = (float)(var6 * this.height);
               float var11 = (float)(var8 * this.height);
               float var12 = (float)(var2 * ji.K(var6, this.time, var4, this.right));
               float var13 = (float)(var2 * ji.K(var8, this.time, var4, this.right));
               if (this.right) {
                  var12 = this.width - var12;
                  var13 = this.width - var13;
               }

               int var14 = ji.O(var6, var4, this.right);
               int var15 = ji.O(var8, var4, this.right);

               for (int var16 = 0; var16 < 4; var16++) {
                  double var17 = ji.u(var16);
                  double var19 = ji.u(var16 + 1);
                  this.quad(
                     var1,
                     (float)(var3 + (var12 - var3) * var17),
                     var10,
                     (float)(var3 + (var13 - var3) * var17),
                     var11,
                     (float)(var3 + (var13 - var3) * var19),
                     var11,
                     (float)(var3 + (var12 - var3) * var19),
                     var10,
                     ji.T(var6, var4, this.right, var17),
                     ji.T(var8, var4, this.right, var17),
                     ji.T(var8, var4, this.right, var19),
                     ji.T(var6, var4, this.right, var19)
                  );
               }

               float var21 = this.right ? -this.feather : this.feather;
               this.quad(var1, var12, var10, var13, var11, var13 + var21, var11, var12 + var21, var10, var14, var15, var15 & 16777215, var14 & 16777215);
            }
         }
      }

      private void quad(
         VertexConsumer var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         float var7,
         float var8,
         float var9,
         int var10,
         int var11,
         int var12,
         int var13
      ) {
         if (this.right) {
            var1.addVertexWith2DPose(this.pose, var8, var9).setColor(var13);
            var1.addVertexWith2DPose(this.pose, var6, var7).setColor(var12);
            var1.addVertexWith2DPose(this.pose, var4, var5).setColor(var11);
            var1.addVertexWith2DPose(this.pose, var2, var3).setColor(var10);
         } else {
            var1.addVertexWith2DPose(this.pose, var2, var3).setColor(var10);
            var1.addVertexWith2DPose(this.pose, var4, var5).setColor(var11);
            var1.addVertexWith2DPose(this.pose, var6, var7).setColor(var12);
            var1.addVertexWith2DPose(this.pose, var8, var9).setColor(var13);
         }
      }
   }
}
