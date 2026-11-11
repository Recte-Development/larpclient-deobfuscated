package com.larpclient.gui.theme;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.gui.screen.LarpTitleScreen;
import com.larpclient.module.render.MenuBackgroundModule;
import com.larpclient.render.misc.MipmappedTexture;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.DisplayFormat;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
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

public final class MenuBackground {
   private static final Identifier logoTexture = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final Identifier logoHdTexture = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo-hd.png");
   private static final MenuBackgroundAnimator waves = new MenuBackgroundAnimator();
   private static int resourceGeneration = -1;
   private static boolean hasLogo;
   private static boolean hasHdLogo;
   private static boolean readyLogged;
   private static final Set<Identifier> mipmappedLogos = new HashSet<>();
   private static Identifier loggedLogo;

   private MenuBackground() {
   }

   public static Identifier resolveLogo() {
      if (resourceGeneration != PostProcessing.reloadCount) {
         resourceGeneration = PostProcessing.reloadCount;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();
         hasLogo = var0.getResource(logoTexture).isPresent();
         hasHdLogo = var0.getResource(logoHdTexture).isPresent();
         if (!hasLogo) {
            LarpLog.warn("Menu", "Logo texture missing; monochrome waves remain available, retry after resource reload");
         }

         if (!hasHdLogo) {
            LarpLog.warn("Menu", "High-resolution logo missing; using the 64px logo, retry after resource reload");
         }

         if (hasHdLogo) {
            registerMipmapped(logoHdTexture);
         }

         loggedLogo = null;
         readyLogged = (boolean)0;
      }

      Identifier var1 = hasHdLogo ? logoHdTexture : logoTexture;
      if (!var1.equals(loggedLogo)) {
         loggedLogo = var1;
         LarpLog.info("Menu", "Menu logo: {}; standard backdrop and navigation retained", var1);
      }

      return var1;
   }

   private static void registerMipmapped(Identifier var0) {
      if (mipmappedLogos.add(var0)) {
         long var1 = System.nanoTime();

         try {
            Minecraft.getInstance().getTextureManager().registerAndLoad(var0, new MipmappedTexture(var0));
            LarpLog.info("Menu", "Mipmapped {} in {} ms", var0, (System.nanoTime() - var1) / 1000000L);
         } catch (RuntimeException var4) {
            LarpLog.warn("Menu", "Mipmapped {} unavailable; Minecraft loads it with single-level sampling", var0, var4);
         }
      }
   }

   public static void render(GuiGraphicsExtractor var0, int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var3 = Minecraft.getInstance();
         ClientPreferences var4 = LarpClient.getInstance().getConfigManager().getPreferences();
         int var5 = var4.motion && var4.menuQuality != DisplayFormat.MenuQuality.OFF ? 1 : 0;
         Identifier var6 = resolveLogo();
         double var7 = var3.gui.screen() instanceof LarpTitleScreen var9 ? var9.tickIntro().beginUndock() : 1.0;
         waves.update(System.nanoTime(), (boolean)var5, (Double)var3.options.panoramaSpeed().get());
         var0.fill(0, 0, var1, var2, ColorUtil.lerpColor(-16777216, -16185079, var7));
         boolean var20 = MenuBackgroundModule.drawBackground(var0, var1, var2, var7);
         var0.nextStratum();
         float var21 = 1 / Math.max(1, var3.getWindow().getGuiScale());

         for (int var11 = 0; var11 < 2 && (!var20 || MenuBackgroundModule.wantsSideWaves()); var11++) {
            int var12 = var11 == 1 ? 1 : 0;
            float var13 = (float)((1.0 - var7) * MenuBackgroundAnimator.sideWidth(var1, var2) * (var12 != 0 ? 1 : -1));
            Matrix3x2f var14 = new Matrix3x2f(var0.pose()).translate(var13, 0.0F);
            int var15 = (int)Math.ceil(MenuBackgroundAnimator.sideWidth(var1, var2) + var21);
            ScreenRectangle var16 = new ScreenRectangle(var12 != 0 ? var1 - var15 : 0, 0, var15, var2).transformMaxBounds(var14);
            if (var0.scissorStack.peek() != null) {
               var16 = var0.scissorStack.peek().intersection(var16);
            }

            var0.guiRenderState
               .addGuiElement(
                  new MenuBackground.WaveSideRenderState(var14, var1, var2, (boolean)var12, waves.getPhase(), var21, var0.scissorStack.peek(), var16)
               );
         }

         if ((!var6.equals(logoTexture) || hasLogo) && var4.softParticles) {
            var0.nextStratum();

            for (int var22 = 0; var22 < 18; var22++) {
               MenuBackgroundAnimator.Particle var23 = waves.computeParticle(var22, var1, var2);
               if (!(var23.alpha() < 0.001)) {
                  int var24 = (int)Math.round(var23.size());
                  var0.pose().pushMatrix();

                  try {
                     double var25 = (1.0 - var7) * MenuBackgroundAnimator.sideWidth(var1, var2) * (var22 % 2 == 0 ? -1 : 1);
                     var0.pose().translate((float)(var23.posX() + var25), (float)var23.posY());
                     var0.pose().rotate((float)var23.rotation());
                     var0.blit(
                        RenderPipelines.GUI_TEXTURED,
                        var6,
                        -var24 / 2,
                        -var24 / 2,
                        0.0F,
                        0.0F,
                        var24,
                        var24,
                        64,
                        64,
                        64,
                        64,
                        ColorUtil.withAlpha(-1, var23.alpha() * var7)
                     );
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }
         }

         if (!readyLogged) {
            readyLogged = (boolean)1;
            LarpLog.info("Menu", "Lit monochrome side waves ready; {} logo slots, shaded folds, Minecraft panorama speed controls motion", 18);
         }
      }
   }

   public static void render() {
      resourceGeneration = -1;
      loggedLogo = null;
      readyLogged = (boolean)0;
   }

   record WaveSideRenderState(
      Matrix3x2fc pose, int width, int height, boolean right, double time, float feather, ScreenRectangle scissorArea, ScreenRectangle bounds
   ) implements GuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = (float)MenuBackgroundAnimator.sideWidth(this.width, this.height);
         float var3 = this.right ? this.width : 0.0F;

         for (int var4 = 0; var4 < 6; var4++) {
            for (int var5 = 0; var5 < 96; var5++) {
               double var6 = var5 / 96.0;
               double var8 = (var5 + 1.0) / 96.0;
               float var10 = (float)(var6 * this.height);
               float var11 = (float)(var8 * this.height);
               float var12 = (float)(var2 * MenuBackgroundAnimator.waveBrightness(var6, this.time, var4, this.right));
               float var13 = (float)(var2 * MenuBackgroundAnimator.waveBrightness(var8, this.time, var4, this.right));
               if (this.right) {
                  var12 = this.width - var12;
                  var13 = this.width - var13;
               }

               int var14 = MenuBackgroundAnimator.tileColor(var6, var4, this.right);
               int var15 = MenuBackgroundAnimator.tileColor(var8, var4, this.right);

               for (int var16 = 0; var16 < 4; var16++) {
                  double var17 = MenuBackgroundAnimator.getIntensityStop(var16);
                  double var19 = MenuBackgroundAnimator.getIntensityStop(var16 + 1);
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
                     MenuBackgroundAnimator.tileColorAt(var6, var4, this.right, var17),
                     MenuBackgroundAnimator.tileColorAt(var8, var4, this.right, var17),
                     MenuBackgroundAnimator.tileColorAt(var8, var4, this.right, var19),
                     MenuBackgroundAnimator.tileColorAt(var6, var4, this.right, var19)
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
