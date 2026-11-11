package com.larpclient.render.overlay;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class RoundedShapes {
   private static boolean shapesRequested;
   private static boolean shapesReady;
   private static boolean shapesFailed;
   private static boolean contourRequested;
   private static boolean contourReady;
   private static boolean contourFailed;
   public static final int rING_WIDTH = 2;
   private static final VertexFormat sIZED_SHAPE_FORMAT = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat sIZED_SWEEP_FORMAT = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat rIPPLE_FORMAT = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .build();
   private static boolean rippleRequested;
   private static boolean rippleReady;
   private static boolean rippleFailed;
   public static final float fIXED_POINT_SCALE = 64.0F;
   private static boolean worldShapesRequested;
   private static boolean worldShapesReady;
   private static boolean worldShapesFailed;
   private static boolean worldContourReady;
   private static boolean worldContourFailed;

   private RoundedShapes() {
   }

   public static boolean isRoundedPipeline(RenderPipeline var0) {
      return var0 == RoundedShapes.Pipelines.fILL
         || var0 == RoundedShapes.Pipelines.rIM
         || var0 == RoundedShapes.Pipelines.rING
         || var0 == RoundedShapes.Pipelines.gLOW
         || var0 == RoundedShapes.Pipelines.cONTOUR_LIGHT
         || var0 == RoundedShapes.Pipelines.rIPPLE;
   }

   public static RenderPipeline sizedPipelineFor(RenderPipeline var0) {
      if (isRoundedPipeline(var0) && var0 != RoundedShapes.Pipelines.rIPPLE) {
         worldShapesRequested = (boolean)1;
         if (var0 == RoundedShapes.Pipelines.cONTOUR_LIGHT) {
            return worldContourReady ? RoundedShapes.Pipelines.cONTOUR_LIGHT_SIZED : null;
         } else if (!worldShapesReady) {
            return null;
         } else {
            return var0 == RoundedShapes.Pipelines.fILL
               ? RoundedShapes.Pipelines.fILL_SIZED
               : (
                  var0 == RoundedShapes.Pipelines.rIM
                     ? RoundedShapes.Pipelines.rIM_SIZED
                     : (var0 == RoundedShapes.Pipelines.rING ? RoundedShapes.Pipelines.rING_SIZED : RoundedShapes.Pipelines.gLOW_SIZED)
               );
         }
      } else {
         return null;
      }
   }

   public static void initPipelines() {
      if (shapesRequested && !shapesReady && !shapesFailed) {
         try {
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.fILL);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.rIM);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.rING);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.gLOW);
            shapesReady = (boolean)1;
            LarpLog.info("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            shapesFailed = (boolean)1;
            LarpLog.error("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (contourRequested && shapesReady && !contourReady && !contourFailed) {
         try {
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.cONTOUR_LIGHT);
            contourReady = (boolean)1;
            LarpLog.info("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            contourFailed = (boolean)1;
            LarpLog.error("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (rippleRequested && shapesReady && !rippleReady && !rippleFailed) {
         try {
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.rIPPLE);
            rippleReady = (boolean)1;
            LarpLog.info("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            rippleFailed = (boolean)1;
            LarpLog.error("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (worldShapesRequested && shapesReady && !worldShapesReady && !worldShapesFailed) {
         try {
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.fILL_SIZED);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.rIM_SIZED);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.rING_SIZED);
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.gLOW_SIZED);
            worldShapesReady = (boolean)1;
            LarpLog.info("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            worldShapesFailed = (boolean)1;
            LarpLog.error("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (worldShapesRequested && worldShapesReady && contourReady && !worldContourReady && !worldContourFailed) {
         try {
            ClientPipelines.validatePipeline(RoundedShapes.Pipelines.cONTOUR_LIGHT_SIZED);
            worldContourReady = (boolean)1;
         } catch (RuntimeException var1) {
            worldContourFailed = (boolean)1;
            LarpLog.error("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void resetFailures() {
      rippleFailed = (boolean)0;
      rippleReady = (boolean)0;
      contourFailed = (boolean)0;
      contourReady = (boolean)0;
      shapesFailed = (boolean)0;
      shapesReady = (boolean)0;
      worldContourFailed = (boolean)0;
      worldContourReady = (boolean)0;
      worldShapesFailed = (boolean)0;
      worldShapesReady = (boolean)0;
   }

   public static void fillRounded(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
            DrawUtil.fillRect(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            shapesRequested = (boolean)1;
            if (!shapesReady) {
               GlassPainter.drawGlassFill(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = AlphaScope.applyAlpha(var6);
               if (var8 >>> 24 != 0) {
                  var0.guiRenderState
                     .addGuiElement(
                        new RoundedShapes.ShapeRenderState(var0.pose(), var1, var2, var3, var4, clampRadius(var7, var3, var4), var8, var0.scissorStack.peek())
                     );
               }
            }
         }
      }
   }

   private static float clampRadius(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void fillRoundedGradient(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            fillRounded(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
               DrawUtil.fillGradient(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = clampRadius(var8, var3, var4);
               shapesRequested = (boolean)1;
               if (!shapesReady) {
                  GlassPainter.drawGlassFill(
                     var0,
                     Math.round(var1),
                     Math.round(var2),
                     Math.round(var3),
                     Math.round(var4),
                     Math.max(1, Math.round(var8)),
                     ColorUtil.lerpColor(var6, var7, 0.5)
                  );
               } else {
                  var6 = AlphaScope.applyAlpha(var6);
                  var7 = AlphaScope.applyAlpha(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.guiRenderState
                        .addGuiElement(new RoundedShapes.ShapeRenderState(var0.pose(), var1, var2, var3, var4, var8, var6, var7, var0.scissorStack.peek()));
                  }
               }
            }
         }
      }
   }

   public static void fillRoundedGradientHorizontal(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            fillRoundedGradient(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static void drawRimGradient(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         shapesRequested = (boolean)1;
         if (!shapesReady) {
            GlassPainter.drawGlassRim(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = AlphaScope.applyAlpha(var6);
            var7 = AlphaScope.applyAlpha(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(new RoundedShapes.ShapeRenderState(var0.pose(), var1, var2, var3, var4, var8, var6, var7, true, var0.scissorStack.peek()));
            }
         }
      }
   }

   public static void drawRim(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         shapesRequested = (boolean)1;
         if (!shapesReady) {
            GlassPainter.drawGlassRim(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = AlphaScope.applyAlpha(var6);
            if (var6 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new RoundedShapes.ShapeRenderState(
                        new Matrix3x2f(var0.pose()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var7,
                        var6,
                        var6,
                        true,
                        true,
                        var0.scissorStack.peek(),
                        RoundedShapes.ShapeRenderState.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void drawGlow(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         shapesRequested = (boolean)1;
         if (shapesReady) {
            var7 = AlphaScope.applyAlpha(var7);
            var8 = AlphaScope.applyAlpha(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new RoundedShapes.ShapeRenderState(
                        new Matrix3x2f(var0.pose()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var9,
                        var7,
                        var8,
                        false,
                        false,
                        var10,
                        var0.scissorStack.peek(),
                        RoundedShapes.ShapeRenderState.bounds(var0.pose(), var1, var2, var3, var4, var10, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void drawContourLight(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         shapesRequested = (boolean)1;
         contourRequested = (boolean)1;
         if (contourReady) {
            var9 = AlphaScope.applyAlpha(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new RoundedShapes.ShapeRenderState(
                        new Matrix3x2f(var0.pose()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var10,
                        var9,
                        var9,
                        false,
                        false,
                        var11,
                        (float)(var7 - Math.floor(var7)),
                        var0.scissorStack.peek(),
                        RoundedShapes.ShapeRenderState.bounds(var0.pose(), var1, var2, var3, var4, var11, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void drawRipple(
      GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9
   ) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = clampRadius(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         shapesRequested = (boolean)1;
         rippleRequested = (boolean)1;
         if (rippleReady) {
            var9 = AlphaScope.applyAlpha(var9);
            if (var9 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new RoundedShapes.RippleRenderState(
                        new Matrix3x2f(var0.pose()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var10,
                        (var6 - var1 - var3 / 2.0F) / var10,
                        (var7 - var2 - var4 / 2.0F) / var10,
                        var8 / var10,
                        var9,
                        var0.scissorStack.peek(),
                        RoundedShapes.ShapeRenderState.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   private static final class Pipelines {
      static final RenderPipeline fILL = createShapePipeline(0, false);
      static final RenderPipeline rIM = createShapePipeline(1, false);
      static final RenderPipeline rING = createShapePipeline(2, false);
      static final RenderPipeline gLOW = createShapePipeline(-1, false);
      static final RenderPipeline cONTOUR_LIGHT = createSweepPipeline(false);
      static final RenderPipeline rIPPLE = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_rounded_ripple"))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withVertexBinding(0, RoundedShapes.rIPPLE_FORMAT)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline fILL_SIZED = createShapePipeline(0, true);
      static final RenderPipeline rIM_SIZED = createShapePipeline(1, true);
      static final RenderPipeline rING_SIZED = createShapePipeline(2, true);
      static final RenderPipeline gLOW_SIZED = createShapePipeline(-1, true);
      static final RenderPipeline cONTOUR_LIGHT_SIZED = createSweepPipeline(true);

      private static RenderPipeline createSweepPipeline(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var0 ? RoundedShapes.sIZED_SWEEP_FORMAT : DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline createShapePipeline(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var1 ? RoundedShapes.sIZED_SHAPE_FORMAT : DefaultVertexFormat.POSITION_TEX_COLOR);
         if (var0 < 0) {
            var3.withShaderDefine("LARP_UI_GLOW");
         }

         if (var0 > 0) {
            var3.withShaderDefine("LARP_UI_RIM");
         }

         if (var0 > 1) {
            var3.withShaderDefine("LARP_UI_RIM_WIDTH", var0);
         }

         if (var1) {
            var3.withShaderDefine("LARP_UI_SIZED");
         }

         return var3.build();
      }
   }

   public record RippleRenderState(
      Matrix3x2fc pose,
      float x,
      float y,
      float width,
      float height,
      float radius,
      float circleX,
      float circleY,
      float circle,
      int color,
      ScreenRectangle scissorArea,
      ScreenRectangle bounds
   ) implements GuiElementRenderState {
      private final float left;
      private final float top;

      public RippleRenderState(
         Matrix3x2fc pose,
         float x,
         float y,
         float width,
         float height,
         float radius,
         float circleX,
         float circleY,
         float circle,
         int color,
         ScreenRectangle scissorArea,
         ScreenRectangle bounds
      ) {
         this.pose = pose;
         this.left = x;
         this.top = y;
         this.width = width;
         this.height = height;
         this.radius = radius;
         this.circleX = circleX;
         this.circleY = circleY;
         this.circle = circle;
         this.color = color;
         this.scissorArea = scissorArea;
         this.bounds = bounds;
      }

      public RenderPipeline pipeline() {
         return RoundedShapes.Pipelines.rIPPLE;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = 1.0F;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.left - var2, this.top - var2, -var3, -var4);
         this.vertex(var1, this.left - var2, this.top + this.height + var2, -var3, var4);
         this.vertex(var1, this.left + this.width + var2, this.top + this.height + var2, var3, var4);
         this.vertex(var1, this.left + this.width + var2, this.top - var2, var3, -var4);
      }

      private void vertex(VertexConsumer var1, float var2, float var3, float var4, float var5) {
         var1.addVertex(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               1.0F / this.radius
            )
            .setUv(var4, var5)
            .setColor(this.color)
            .setUv1(fixed(this.circleX), fixed(this.circleY))
            .setUv2(fixed(this.circle), 0);
      }

      private static int fixed(float var0) {
         return Math.clamp(Math.round(var0 * 64.0F), -32768, 32767);
      }
   }

   public record ShapeRenderState(
      Matrix3x2fc pose,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int color,
      int bottomColor,
      boolean rim,
      boolean ring,
      float padding,
      float sweep,
      ScreenRectangle scissorArea,
      ScreenRectangle bounds
   ) implements GuiElementRenderState {
      private final float left;
      private final float top;

      public ShapeRenderState(
         Matrix3x2fc var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         int var7,
         int var8,
         boolean var9,
         boolean var10,
         float var11,
         ScreenRectangle var12,
         ScreenRectangle var13
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, -1.0F, var12, var13);
      }

      public ShapeRenderState(
         Matrix3x2fc var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         int var7,
         int var8,
         boolean var9,
         boolean var10,
         ScreenRectangle var11,
         ScreenRectangle var12
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 1.0F, var11, var12);
      }

      public ShapeRenderState(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, ScreenRectangle var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public ShapeRenderState(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, ScreenRectangle var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public ShapeRenderState(
         Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, ScreenRectangle var10
      ) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, var8, var9, false, var10, bounds(var1, var2, var3, var4, var5, var10));
      }

      public ShapeRenderState(
         Matrix3x2fc pose,
         float x,
         float y,
         float width,
         float height,
         float radius,
         int color,
         int bottomColor,
         boolean rim,
         boolean ring,
         float padding,
         float sweep,
         ScreenRectangle scissorArea,
         ScreenRectangle bounds
      ) {
         this.pose = pose;
         this.left = x;
         this.top = y;
         this.width = width;
         this.height = height;
         this.radius = radius;
         this.color = color;
         this.bottomColor = bottomColor;
         this.rim = rim;
         this.ring = ring;
         this.padding = padding;
         this.sweep = sweep;
         this.scissorArea = scissorArea;
         this.bounds = bounds;
      }

      static ScreenRectangle bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, ScreenRectangle var5) {
         return bounds(var0, var1, var2, var3, var4, 1.0F, var5);
      }

      static ScreenRectangle bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, float var5, ScreenRectangle var6) {
         int var7 = (int)Math.floor(var1 - var5);
         int var8 = (int)Math.floor(var2 - var5);
         int var9 = (int)Math.ceil(var1 + var3 + var5);
         int var10 = (int)Math.ceil(var2 + var4 + var5);
         ScreenRectangle var11 = new ScreenRectangle(var7, var8, var9 - var7, var10 - var8).transformMaxBounds(var0);
         return var6 == null ? var11 : var6.intersection(var11);
      }

      public boolean glow() {
         return this.padding > 1.0F;
      }

      public boolean contourLight() {
         return this.sweep >= 0.0F;
      }

      public RenderPipeline pipeline() {
         return this.contourLight()
            ? RoundedShapes.Pipelines.cONTOUR_LIGHT
            : (
               this.glow()
                  ? RoundedShapes.Pipelines.gLOW
                  : (this.ring ? RoundedShapes.Pipelines.rING : (this.rim ? RoundedShapes.Pipelines.rIM : RoundedShapes.Pipelines.fILL))
            );
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = this.padding;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.left - var2, this.top - var2, -var3, -var4, this.color);
         this.vertex(var1, this.left - var2, this.top + this.height + var2, -var3, var4, this.bottomColor);
         this.vertex(var1, this.left + this.width + var2, this.top + this.height + var2, var3, var4, this.bottomColor);
         this.vertex(var1, this.left + this.width + var2, this.top - var2, var3, -var4, this.color);
      }

      private void vertex(VertexConsumer var1, float var2, float var3, float var4, float var5, int var6) {
         VertexConsumer var7 = var1.addVertex(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               this.padding / this.radius
            )
            .setUv(var4, var5)
            .setColor(var6);
         if (this.contourLight()) {
            var7.setUv2(Math.round(this.sweep * 32768.0F) & 32767, this.glow() ? 1 : 0);
         }
      }
   }
}
