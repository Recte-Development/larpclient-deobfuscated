package com.larpclient;

import com.larpclient.module.HalloweenModule;
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

public final class dm {
   private static final Identifier C = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final Identifier f = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo-hd.png");
   private static final Identifier v = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo-halloween.png");
   private static final x9 l = new x9();
   private static int H = -1;
   private static boolean z;
   private static boolean V;
   private static boolean K;
   private static boolean I;
   private static final Set<Identifier> Z = new HashSet<>();
   private static Identifier b;

   private dm() {
   }

   public static Identifier Q() {
      if (H != jk.v) {
         H = jk.v;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();
         z = var0.getResource(C).isPresent();
         V = var0.getResource(f).isPresent();
         K = var0.getResource(v).isPresent();
         if (!z) {
            r4.m("Menu", "Logo texture missing; monochrome waves remain available, retry after resource reload");
         }

         if (!V) {
            r4.m("Menu", "High-resolution logo missing; using the 64px logo, retry after resource reload");
         }

         if (!K) {
            r4.m("Menu", "Halloween logo missing; using the regular logo, retry after resource reload");
         }

         if (V) {
            Q(f);
         }

         if (K) {
            Q(v);
         }

         b = null;
         I = (boolean)0;
      }

      Identifier var1 = HalloweenModule.M() && K ? v : (V ? f : C);
      if (!var1.equals(b)) {
         b = var1;
         r4.V("Menu", "Menu logo: {}; standard backdrop and navigation retained", var1);
      }

      return var1;
   }

   private static void Q(Identifier var0) {
      if (Z.add(var0)) {
         long var1 = System.nanoTime();

         try {
            Minecraft.getInstance().getTextureManager().registerAndLoad(var0, new ec(var0));
            r4.V("Menu", "Mipmapped {} in {} ms", var0, (System.nanoTime() - var1) / 1000000L);
         } catch (RuntimeException var4) {
            r4.m("Menu", "Mipmapped {} unavailable; Minecraft loads it with single-level sampling", var0, var4);
         }
      }
   }

   public static void r(GuiGraphicsExtractor var0, int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var3 = Minecraft.getInstance();
         fs var4 = LarpClient.y().v().E();
         int var5 = var4.o && var4.S != xb.s.C ? 1 : 0;
         Identifier var6 = Q();
         double var7 = var3.gui.screen() instanceof rf var9 ? var9.T().I() : 1.0;
         l.p(System.nanoTime(), (boolean)var5, (Double)var3.options.panoramaSpeed().get());
         var0.fill(0, 0, var1, var2, oh.j(-16777216, -16185079, var7));
         boolean var20 = MenuBackgroundModule.b(var0, var1, var2, var7);
         var0.nextStratum();
         float var21 = 1 / Math.max(1, var3.getWindow().getGuiScale());

         for (int var11 = 0; var11 < 2 && (!var20 || MenuBackgroundModule.e()); var11++) {
            int var12 = var11 == 1 ? 1 : 0;
            float var13 = (float)((1.0 - var7) * x9.K(var1, var2) * (var12 != 0 ? 1 : -1));
            Matrix3x2f var14 = new Matrix3x2f(var0.pose()).translate(var13, 0.0F);
            int var15 = (int)Math.ceil(x9.K(var1, var2) + var21);
            ScreenRectangle var16 = new ScreenRectangle(var12 != 0 ? var1 - var15 : 0, 0, var15, var2).transformMaxBounds(var14);
            if (var0.scissorStack.peek() != null) {
               var16 = var0.scissorStack.peek().intersection(var16);
            }

            var0.guiRenderState.addGuiElement(new dm.o(var14, var1, var2, (boolean)var12, l.i(), var21, var0.scissorStack.peek(), var16));
         }

         if ((!var6.equals(C) || z) && var4.J) {
            var0.nextStratum();

            for (int var22 = 0; var22 < 18; var22++) {
               x9.h var23 = l.Q(var22, var1, var2);
               if (!(var23.alpha() < 0.001)) {
                  int var24 = (int)Math.round(var23.size());
                  var0.pose().pushMatrix();

                  try {
                     double var25 = (1.0 - var7) * x9.K(var1, var2) * (var22 % 2 == 0 ? -1 : 1);
                     var0.pose().translate((float)(var23.x() + var25), (float)var23.y());
                     var0.pose().rotate((float)var23.rotation());
                     var0.blit(
                        RenderPipelines.GUI_TEXTURED, var6, -var24 / 2, -var24 / 2, 0.0F, 0.0F, var24, var24, 64, 64, 64, 64, oh.i(-1, var23.alpha() * var7)
                     );
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }
         }

         if (!I) {
            I = (boolean)1;
            r4.V("Menu", "Lit monochrome side waves ready; {} logo slots, shaded folds, Minecraft panorama speed controls motion", 18);
         }
      }
   }

   public static void X() {
      H = -1;
      b = null;
      I = (boolean)0;
   }

   record o(Matrix3x2fc pose, int width, int height, boolean right, double time, float feather, ScreenRectangle scissorArea, ScreenRectangle bounds)
      implements GuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = (float)x9.K(this.width, this.height);
         float var3 = this.right ? this.width : 0.0F;

         for (int var4 = 0; var4 < 6; var4++) {
            for (int var5 = 0; var5 < 96; var5++) {
               double var6 = var5 / 96.0;
               double var8 = (var5 + 1.0) / 96.0;
               float var10 = (float)(var6 * this.height);
               float var11 = (float)(var8 * this.height);
               float var12 = (float)(var2 * x9.x(var6, this.time, var4, this.right));
               float var13 = (float)(var2 * x9.x(var8, this.time, var4, this.right));
               if (this.right) {
                  var12 = this.width - var12;
                  var13 = this.width - var13;
               }

               int var14 = x9.j(var6, var4, this.right);
               int var15 = x9.j(var8, var4, this.right);

               for (int var16 = 0; var16 < 4; var16++) {
                  double var17 = x9.G(var16);
                  double var19 = x9.G(var16 + 1);
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
                     x9.J(var6, var4, this.right, var17),
                     x9.J(var8, var4, this.right, var17),
                     x9.J(var8, var4, this.right, var19),
                     x9.J(var6, var4, this.right, var19)
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
