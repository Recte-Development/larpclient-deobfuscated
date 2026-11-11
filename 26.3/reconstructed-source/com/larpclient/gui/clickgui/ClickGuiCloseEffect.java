package com.larpclient.gui.clickgui;

import com.larpclient.core.StreamProofManager;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;

public final class ClickGuiCloseEffect {
   private static final VertexFormat vertexFormat = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .build();
   private static final RenderPipeline pipeline = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_exit_snapshot"))
      .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/snapshot"))
      .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/snapshot"))
      .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
      .withVertexBinding(0, vertexFormat)
      .build();
   static final double durationScale = 2.0;
   static final double crtDurationScale = 1.6;
   private static TextureTarget snapshotTarget;
   private static boolean unavailable;
   private static int resourceGeneration = -1;
   private final String effectName;
   private final long startNanos;
   private final double durationNanos;
   private final List<ClickGuiCloseEffect.WindowBounds> windows;
   private final List<ClickGuiCloseEffect.Piece> pieces = new ArrayList<>();
   private boolean logged;

   private ClickGuiCloseEffect(String var1, List<ClickGuiCloseEffect.WindowBounds> var2, int var3) {
      this.effectName = var1;
      this.windows = List.copyOf(var2);
      this.startNanos = System.nanoTime();
      this.durationNanos = var3 * (var1.equals("CRT") ? 1.6 : 2.0) * 1000000.0;
      Random var4 = new Random(this.startNanos);

      for (int var5 = 0; var5 < var2.size(); var5++) {
         this.buildPieces(var5, (ClickGuiCloseEffect.WindowBounds)var2.get(var5), var4);
      }
   }

   static ClickGuiCloseEffect create(String var0, List<ClickGuiCloseEffect.WindowBounds> var1, int var2) {
      if (!var1.isEmpty() && ensureSnapshotTarget()) {
         LarpLog.info("GUI", "{} close effect over {} window(s), {} ms", var0, var1.size(), Math.round(var2 * (var0.equals("CRT") ? 1.6 : 2.0)));
         return new ClickGuiCloseEffect(var0, var1, var2);
      } else {
         return null;
      }
   }

   private static boolean ensureSnapshotTarget() {
      if (resourceGeneration != PostProcessing.reloadCount) {
         releaseSnapshot();
         resourceGeneration = PostProcessing.reloadCount;
         unavailable = (boolean)0;
      }

      if (unavailable) {
         return false;
      }

      try {
         RenderTarget var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (snapshotTarget == null || snapshotTarget.width != var0.width || snapshotTarget.height != var0.height) {
            releaseSnapshot();
            ClientPipelines.validatePipeline(pipeline);
            snapshotTarget = RenderPipelineCompat.createTextureTarget("Larp ClickGUI exit", var0.width, var0.height, false, GpuFormat.RGBA8_UNORM);
         }

         if (!StreamProofManager.blitOverlayTarget(snapshotTarget)) {
            PostProcessing.runPostPass(snapshotTarget);
         }

         return true;
      } catch (RuntimeException var1) {
         unavailable = (boolean)1;
         releaseSnapshot();
         LarpLog.error("GUI", "Close effect unavailable; ClickGUI plays its reverse motion until resource reload", var1);
         return false;
      }
   }

   double progress() {
      return Math.clamp((System.nanoTime() - this.startNanos) / this.durationNanos, 0.0, 1.0);
   }

   boolean isFinished() {
      return this.progress() >= 1.0;
   }

   public static void releaseSnapshot() {
      if (snapshotTarget != null) {
         snapshotTarget.destroyBuffers();
         snapshotTarget = null;
      }
   }

   void render(GuiGraphicsExtractor var1) {
      if (snapshotTarget != null) {
         double var2 = this.progress();
         Window var4 = Minecraft.getInstance().getWindow();
         float var5 = var4.getGuiScale();
         float var6 = snapshotTarget.width;
         float var7 = snapshotTarget.height;
         TextureSetup var8 = TextureSetup.singleTexture(snapshotTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         ArrayList var9 = new ArrayList();
         float var10 = Float.MAX_VALUE;
         float var11 = Float.MAX_VALUE;
         float var12 = -Float.MAX_VALUE;
         float var13 = -Float.MAX_VALUE;

         for (ClickGuiCloseEffect.Piece var15 : this.pieces) {
            float[] var16 = var15.transformAt(this, var2);
            if (var16 != null) {
               ClickGuiCloseEffect.WindowBounds var17 = this.windows.get(var15.window);
               float var18 = var17.left + var17.width / 2.0F;
               float var19 = var17.top + var17.height / 2.0F;
               ClickGuiCloseEffect.PieceQuad var20 = new ClickGuiCloseEffect.PieceQuad(new float[16], var16[8], var17);

               for (int var21 = 0; var21 < 4; var21++) {
                  var20.data[var21 * 4] = var16[var21 * 2];
                  var20.data[var21 * 4 + 1] = var16[var21 * 2 + 1];
                  var20.data[var21 * 4 + 2] = var15.xCoords[var21] - var18;
                  var20.data[var21 * 4 + 3] = var15.yCoords[var21] - var19;
                  var10 = Math.min(var10, var16[var21 * 2]);
                  var11 = Math.min(var11, var16[var21 * 2 + 1]);
                  var12 = Math.max(var12, var16[var21 * 2]);
                  var13 = Math.max(var13, var16[var21 * 2 + 1]);
               }

               var9.add(var20);
            }
         }

         if (!this.logged) {
            this.logged = (boolean)1;
            LarpLog.debug(
               "GUI",
               "{} close effect drawing {} of {} pieces at {}",
               this.effectName,
               var9.size(),
               this.pieces.size(),
               String.format(Locale.ROOT, "%.2f", var2)
            );
         }

         if (!var9.isEmpty()) {
            int var22 = (int)Math.floor(var10);
            int var23 = (int)Math.floor(var11);
            ScreenRectangle var24 = new ScreenRectangle(var22, var23, (int)Math.ceil(var12) - var22 + 1, (int)Math.ceil(var13) - var23 + 1);
            var1.guiRenderState.addGuiElement(new ClickGuiCloseEffect.CloseEffectElement(var8, List.copyOf(var9), var5, var6, var7, var24));
         }

         if (this.effectName.equals("CRT")) {
            this.drawCrtLine(var1, var2);
         }
      }
   }

   private void drawCrtLine(GuiGraphicsExtractor var1, double var2) {
      double var4 = Math.min(1.0, var2 / 0.55);
      double var6 = 0.9 * var4 * (1.0 - smoothstep(0.85, 1.0, var2));
      if (!(var6 < 0.01)) {
         for (ClickGuiCloseEffect.WindowBounds var9 : this.windows) {
            float[] var10 = crtScale(var2);
            float var11 = var9.left + var9.width / 2.0F;
            float var12 = var9.top + var9.height / 2.0F;
            float var13 = var9.width * var10[0];
            float var14 = Math.max(1.0F, var9.height * var10[1]);
            RoundedShapes.fillRounded(
               var1,
               var11 - var13 / 2.0F,
               var12 - var14 / 2.0F,
               var13,
               var14,
               Math.min(var9.radius, Math.min(var13, var14) / 2.0F),
               ColorUtil.withAlpha(-1, var6)
            );
         }
      }
   }

   private static float[] crtScale(double var0) {
      double var2 = Math.min(1.0, var0 / 0.55);
      float var4 = (float)(1.0 - Animation.Easing.EASE_IN_CUBIC.apply(var2) * 0.99);
      float var5 = var0 < 0.55 ? 1.0F : (float)Math.max(0.003, 1.0 - Animation.Easing.EASE_IN_CUBIC.apply((var0 - 0.55) / 0.45));
      return new float[]{var5, var4};
   }

   private static double smoothstep(double var0, double var2, double var4) {
      double var6 = Math.clamp((var4 - var0) / (var2 - var0), 0.0, 1.0);
      return var6 * var6 * (3.0 - 2.0 * var6);
   }

   private void buildPieces(int var1, ClickGuiCloseEffect.WindowBounds var2, Random var3) {
      switch (this.effectName) {
         case "Shatter":
            this.buildShatterPieces(var1, var2, var3);
            break;
         case "Slice":
            byte var16 = 7;

            for (int var17 = 0; var17 < var16; var17++) {
               float var18 = var2.top + var2.height * var17 / var16;
               float var19 = var2.top + var2.height * (var17 + 1) / var16;
               this.pieces
                  .add(
                     new ClickGuiCloseEffect.Piece(
                        var1,
                        new float[]{var2.left, var2.left, var2.left + var2.width, var2.left + var2.width},
                        new float[]{var18, var19, var19, var18},
                        var17 % 2 == 0 ? -1.0F : 1.0F,
                        var17 - (var16 - 1) / 2.0F,
                        0.0F,
                        0.06F * var17,
                        var3.nextFloat()
                     )
                  );
            }
            break;
         case "Crumble":
         case "Implode":
            float var6 = Math.max(18.0F, Math.min(var2.width, var2.height) / (this.effectName.equals("Crumble") ? 9 : 6));
            int var7 = Math.max(2, Math.round(var2.width / var6));
            int var8 = Math.max(2, Math.round(var2.height / var6));

            for (int var9 = 0; var9 < var8; var9++) {
               for (int var10 = 0; var10 < var7; var10++) {
                  float var11 = var2.left + var2.width * var10 / var7;
                  float var12 = var2.left + var2.width * (var10 + 1) / var7;
                  float var13 = var2.top + var2.height * var9 / var8;
                  float var14 = var2.top + var2.height * (var9 + 1) / var8;
                  float var15 = this.effectName.equals("Crumble") ? 0.42F * (1.0F - (var9 + 0.5F) / var8) + 0.12F * var3.nextFloat() : 0.22F * var3.nextFloat();
                  this.pieces
                     .add(
                        new ClickGuiCloseEffect.Piece(
                           var1,
                           new float[]{var11, var11, var12, var12},
                           new float[]{var13, var14, var14, var13},
                           var3.nextFloat() - 0.5F,
                           var3.nextFloat() - 0.5F,
                           var3.nextFloat() * 2.0F - 1.0F,
                           var15,
                           var3.nextFloat()
                        )
                     );
               }
            }
            break;
         default:
            this.pieces
               .add(
                  new ClickGuiCloseEffect.Piece(
                     var1,
                     new float[]{var2.left, var2.left, var2.left + var2.width, var2.left + var2.width},
                     new float[]{var2.top, var2.top + var2.height, var2.top + var2.height, var2.top},
                     0.0F,
                     0.0F,
                     0.0F,
                     0.0F,
                     0.0F
                  )
               );
      }
   }

   private void buildShatterPieces(int var1, ClickGuiCloseEffect.WindowBounds var2, Random var3) {
      int var4 = Math.clamp(Math.round(5.0F * var2.width / Math.max(1.0F, var2.height)), 3, 9);
      byte var5 = 5;
      float[][] var6 = new float[var5 + 1][var4 + 1];
      float[][] var7 = new float[var5 + 1][var4 + 1];
      float var8 = var2.width / var4;
      float var9 = var2.height / var5;

      for (int var10 = 0; var10 <= var5; var10++) {
         for (int var11 = 0; var11 <= var4; var11++) {
            int var12 = var10 > 0 && var10 < var5 && var11 > 0 && var11 < var4 ? 1 : 0;
            var6[var10][var11] = var2.left + var11 * var8 + (var12 != 0 ? (var3.nextFloat() - 0.5F) * 0.64F * var8 : 0.0F);
            var7[var10][var11] = var2.top + var10 * var9 + (var12 != 0 ? (var3.nextFloat() - 0.5F) * 0.64F * var9 : 0.0F);
         }
      }

      float var28 = var2.left + var2.width * (0.4F + 0.2F * var3.nextFloat());
      float var29 = var2.top + var2.height * (0.4F + 0.2F * var3.nextFloat());
      float var30 = (float)Math.hypot(var2.width, var2.height) / 2.0F;

      for (int var13 = 0; var13 < var5; var13++) {
         for (int var14 = 0; var14 < var4; var14++) {
            float[][] var15 = new float[][]{
               {var6[var13][var14], var7[var13][var14]},
               {var6[var13 + 1][var14], var7[var13 + 1][var14]},
               {var6[var13 + 1][var14 + 1], var7[var13 + 1][var14 + 1]},
               {var6[var13][var14 + 1], var7[var13][var14 + 1]}
            };
            int[][] var16 = var3.nextBoolean() ? new int[][]{{0, 1, 2}, {0, 2, 3}} : new int[][]{{0, 1, 3}, {1, 2, 3}};

            for (int[] var20 : var16) {
               float[] var21 = new float[]{var15[var20[0]][0], var15[var20[1]][0], var15[var20[2]][0], var15[var20[2]][0]};
               float[] var22 = new float[]{var15[var20[0]][1], var15[var20[1]][1], var15[var20[2]][1], var15[var20[2]][1]};
               float var23 = (var21[0] + var21[1] + var21[2]) / 3.0F;
               float var24 = (var22[0] + var22[1] + var22[2]) / 3.0F;
               float var25 = var23 - var28;
               float var26 = var24 - var29;
               float var27 = (float)Math.hypot(var25, var26);
               if (var27 < 0.001F) {
                  var25 = var3.nextFloat() - 0.5F;
                  var26 = -0.5F;
                  var27 = (float)Math.hypot(var25, var26);
               }

               this.pieces
                  .add(
                     new ClickGuiCloseEffect.Piece(
                        var1,
                        var21,
                        var22,
                        var25 / var27,
                        var26 / var27,
                        var3.nextFloat() * 2.0F - 1.0F,
                        0.18F * Math.min(1.0F, var27 / var30),
                        var3.nextFloat()
                     )
                  );
            }
         }
      }
   }

   private record CloseEffectElement(
      TextureSetup textureSetup, List<ClickGuiCloseEffect.PieceQuad> quads, float guiScale, float textureWidth, float textureHeight, ScreenRectangle bounds
   ) implements GuiElementRenderState {
      public RenderPipeline pipeline() {
         return ClickGuiCloseEffect.pipeline;
      }

      public ScreenRectangle scissorArea() {
         return null;
      }

      public void buildVertices(VertexConsumer var1) {
         for (ClickGuiCloseEffect.PieceQuad var3 : this.quads) {
            ClickGuiCloseEffect.WindowBounds var4 = var3.window;
            float var5 = var4.left() + var4.width() / 2.0F;
            float var6 = var4.top() + var4.height() / 2.0F;
            int var7 = Math.clamp(Math.round(var3.alpha * 255.0F), 0, 255) << 24 | 16777215;

            for (int var8 = 0; var8 < 4; var8++) {
               float var9 = var3.data[var8 * 4 + 2];
               float var10 = var3.data[var8 * 4 + 3];
               float var11 = (var5 + var9) * this.guiScale / this.textureWidth;
               float var12 = 1.0F - (var6 + var10) * this.guiScale / this.textureHeight;
               var1.addVertex(var3.data[var8 * 4], var3.data[var8 * 4 + 1], var4.radius())
                  .setUv(var11, var12)
                  .setColor(var7)
                  .setUv1(fixed(var9), fixed(var10))
                  .setUv2(fixed(var4.width() / 2.0F), fixed(var4.height() / 2.0F));
            }
         }
      }

      private static int fixed(float var0) {
         return Math.clamp(Math.round(var0 * 16.0F), -32768, 32767);
      }
   }

   private record Piece(int window, float[] xs, float[] ys, float dirX, float dirY, float spin, float delay, float speed) {
      private final float[] xCoords;
      private final float[] yCoords;

      private Piece(int window, float[] xs, float[] ys, float dirX, float dirY, float spin, float delay, float speed) {
         this.window = window;
         this.xCoords = xs;
         this.yCoords = ys;
         this.dirX = dirX;
         this.dirY = dirY;
         this.spin = spin;
         this.delay = delay;
         this.speed = speed;
      }

      float[] transformAt(ClickGuiCloseEffect var1, double var2) {
         double var4 = Math.clamp((var2 - this.delay) / Math.max(0.001, 1.0F - this.delay), 0.0, 1.0);
         ClickGuiCloseEffect.WindowBounds var6 = var1.windows.get(this.window);
         float var7 = 0.0F;
         float var8 = 0.0F;
         int var9 = this.xCoords[2] == this.xCoords[3] && this.yCoords[2] == this.yCoords[3] ? 3 : 4;

         for (int var10 = 0; var10 < var9; var10++) {
            var7 += this.xCoords[var10] / var9;
            var8 += this.yCoords[var10] / var9;
         }

         float var29 = 0.0F;
         float var11 = 0.0F;
         float var12 = 0.0F;
         float var13 = 1.0F;
         double var14 = 1.0;
         float var16 = Math.max(var6.width, var6.height);
         switch (var1.effectName) {
            case "Shatter":
               float var37 = (float)(Animation.Easing.EASE_OUT_CUBIC.apply(var4) * (0.35 + 0.45 * this.speed) * var16 * 0.9);
               var29 = this.dirX * var37;
               var11 = this.dirY * var37 + (float)(var4 * var4 * var6.height * 0.9);
               var12 = (float)(this.spin * 2.4 * var4);
               var13 = (float)(1.0 - 0.25 * var4);
               var14 = 1.0 - ClickGuiCloseEffect.smoothstep(0.55, 1.0, var4);
               break;
            case "Crumble":
               double var36 = var4 * var4;
               var29 = (float)(this.dirX * 1.2 * (this.xCoords[3] - this.xCoords[0]) * var4);
               var11 = (float)(var36 * (var6.height * 0.9 + 60.0));
               var12 = (float)(this.spin * 1.4 * var4);
               var14 = 1.0 - ClickGuiCloseEffect.smoothstep(0.6, 1.0, var4);
               break;
            case "Slice":
               var29 = (float)(this.dirX * Animation.Easing.EASE_IN_CUBIC.apply(var4) * var6.width * 0.75);
               var11 = (float)(this.dirY * 4.0F * var4);
               var14 = 1.0 - ClickGuiCloseEffect.smoothstep(0.35, 1.0, var4);
               break;
            case "Implode":
               double var35 = Animation.Easing.EASE_IN_CUBIC.apply(var4);
               float var40 = var6.left + var6.width / 2.0F;
               float var42 = var6.top + var6.height / 2.0F;
               double var44 = var35 * 1.6;
               float var25 = (float)((var7 - var40) * (1.0 - var35));
               float var26 = (float)((var8 - var42) * (1.0 - var35));
               float var27 = (float)(var25 * Math.cos(var44) - var26 * Math.sin(var44));
               float var28 = (float)(var25 * Math.sin(var44) + var26 * Math.cos(var44));
               var29 = var40 + var27 - var7;
               var11 = var42 + var28 - var8;
               var12 = (float)(var44 + this.spin * 0.6 * var35);
               var13 = (float)(1.0 - var35);
               var14 = 1.0 - ClickGuiCloseEffect.smoothstep(0.5, 1.0, var4);
               break;
            default:
               float[] var19 = ClickGuiCloseEffect.crtScale(var2);
               float var20 = var6.left + var6.width / 2.0F;
               float var21 = var6.top + var6.height / 2.0F;
               float[] var22 = new float[9];

               for (int var23 = 0; var23 < 4; var23++) {
                  var22[var23 * 2] = var20 + (this.xCoords[var23] - var20) * var19[0];
                  var22[var23 * 2 + 1] = var21 + (this.yCoords[var23] - var21) * var19[1];
               }

               var22[8] = (float)(1.0 - ClickGuiCloseEffect.smoothstep(0.85, 1.0, var2));
               return var22[8] <= 0.002F ? null : var22;
         }

         if (!(var14 <= 0.002) && !(var13 <= 0.002F)) {
            float var33 = (float)Math.cos(var12);
            float var34 = (float)Math.sin(var12);
            float[] var38 = new float[9];

            for (int var39 = 0; var39 < 4; var39++) {
               float var41 = (this.xCoords[var39] - var7) * var13;
               float var43 = (this.yCoords[var39] - var8) * var13;
               var38[var39 * 2] = var7 + var29 + var41 * var33 - var43 * var34;
               var38[var39 * 2 + 1] = var8 + var11 + var41 * var34 + var43 * var33;
            }

            var38[8] = (float)var14;
            return var38;
         } else {
            return null;
         }
      }
   }

   private record PieceQuad(float[] data, float alpha, ClickGuiCloseEffect.WindowBounds window) {
   }

   record WindowBounds(float x, float y, float width, float height, float radius) {
      private final float left;
      private final float top;

      WindowBounds(float x, float y, float width, float height, float radius) {
         this.left = x;
         this.top = y;
         this.width = width;
         this.height = height;
         this.radius = radius;
      }
   }
}
