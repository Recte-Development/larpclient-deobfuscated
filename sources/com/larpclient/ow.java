package com.larpclient;

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

public final class ow {
   private static boolean G;
   private static boolean f;
   private static boolean t;
   private static boolean Y;
   private static boolean N;
   private static boolean P;
   public static final int l = 2;
   private static final VertexFormat x = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat Z = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat O = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .build();
   private static boolean E;
   private static boolean q;
   private static boolean L;
   public static final float g = 64.0F;
   private static boolean B;
   private static boolean o;
   private static boolean y;
   private static boolean K;
   private static boolean j;

   private ow() {
   }

   public static boolean x(RenderPipeline var0) {
      return var0 == ow.w.y || var0 == ow.w.E || var0 == ow.w.X || var0 == ow.w.s || var0 == ow.w.i || var0 == ow.w.g;
   }

   public static RenderPipeline p(RenderPipeline var0) {
      if (x(var0) && var0 != ow.w.g) {
         B = (boolean)1;
         if (var0 == ow.w.i) {
            return K ? ow.w.T : null;
         } else if (!o) {
            return null;
         } else {
            return var0 == ow.w.y ? ow.w.f : (var0 == ow.w.E ? ow.w.Y : (var0 == ow.w.X ? ow.w.h : ow.w.d));
         }
      } else {
         return null;
      }
   }

   public static void k() {
      if (G && !f && !t) {
         try {
            e4.U(ow.w.y);
            e4.U(ow.w.E);
            e4.U(ow.w.X);
            e4.U(ow.w.s);
            f = (boolean)1;
            r4.V("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            t = (boolean)1;
            r4.r("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (Y && f && !N && !P) {
         try {
            e4.U(ow.w.i);
            N = (boolean)1;
            r4.V("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            P = (boolean)1;
            r4.r("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (E && f && !q && !L) {
         try {
            e4.U(ow.w.g);
            q = (boolean)1;
            r4.V("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            L = (boolean)1;
            r4.r("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (B && f && !o && !y) {
         try {
            e4.U(ow.w.f);
            e4.U(ow.w.Y);
            e4.U(ow.w.h);
            e4.U(ow.w.d);
            o = (boolean)1;
            r4.V("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            y = (boolean)1;
            r4.r("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (B && o && N && !K && !j) {
         try {
            e4.U(ow.w.T);
            K = (boolean)1;
         } catch (RuntimeException var1) {
            j = (boolean)1;
            r4.r("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void C() {
      L = (boolean)0;
      q = (boolean)0;
      P = (boolean)0;
      N = (boolean)0;
      t = (boolean)0;
      f = (boolean)0;
      j = (boolean)0;
      K = (boolean)0;
      y = (boolean)0;
      o = (boolean)0;
   }

   public static void P(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
            fr.k(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            G = (boolean)1;
            if (!f) {
               ll.E(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = r.D(var6);
               if (var8 >>> 24 != 0) {
                  var0.guiRenderState.addGuiElement(new ow.q(var0.pose(), var1, var2, var3, var4, p(var7, var3, var4), var8, var0.scissorStack.peek()));
               }
            }
         }
      }
   }

   private static float p(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void s(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            P(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
               fr.Y(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = p(var8, var3, var4);
               G = (boolean)1;
               if (!f) {
                  ll.E(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), oh.j(var6, var7, 0.5));
               } else {
                  var6 = r.D(var6);
                  var7 = r.D(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.guiRenderState.addGuiElement(new ow.q(var0.pose(), var1, var2, var3, var4, var8, var6, var7, var0.scissorStack.peek()));
                  }
               }
            }
         }
      }
   }

   public static void Z(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            s(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static void y(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         G = (boolean)1;
         if (!f) {
            ll.V(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = r.D(var6);
            var7 = r.D(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.guiRenderState.addGuiElement(new ow.q(var0.pose(), var1, var2, var3, var4, var8, var6, var7, true, var0.scissorStack.peek()));
            }
         }
      }
   }

   public static void A(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         G = (boolean)1;
         if (!f) {
            ll.V(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = r.D(var6);
            if (var6 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new ow.q(
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
                        ow.q.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void A(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         G = (boolean)1;
         if (f) {
            var7 = r.D(var7);
            var8 = r.D(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new ow.q(
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
                        ow.q.bounds(var0.pose(), var1, var2, var3, var4, var10, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void n(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         G = (boolean)1;
         Y = (boolean)1;
         if (N) {
            var9 = r.D(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new ow.q(
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
                        ow.q.bounds(var0.pose(), var1, var2, var3, var4, var11, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void j(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = p(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         G = (boolean)1;
         E = (boolean)1;
         if (q) {
            var9 = r.D(var9);
            if (var9 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new ow.k(
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
                        ow.q.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public record k(
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
      public RenderPipeline pipeline() {
         return ow.w.g;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = 1.0F;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.x - var2, this.y - var2, -var3, -var4);
         this.vertex(var1, this.x - var2, this.y + this.height + var2, -var3, var4);
         this.vertex(var1, this.x + this.width + var2, this.y + this.height + var2, var3, var4);
         this.vertex(var1, this.x + this.width + var2, this.y - var2, var3, -var4);
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

   public record q(
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
      public q(
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

      public q(
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

      public q(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, ScreenRectangle var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public q(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, ScreenRectangle var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public q(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, ScreenRectangle var10) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, var8, var9, false, var10, bounds(var1, var2, var3, var4, var5, var10));
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
         return this.contourLight() ? ow.w.i : (this.glow() ? ow.w.s : (this.ring ? ow.w.X : (this.rim ? ow.w.E : ow.w.y)));
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      public void buildVertices(VertexConsumer var1) {
         float var2 = this.padding;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.x - var2, this.y - var2, -var3, -var4, this.color);
         this.vertex(var1, this.x - var2, this.y + this.height + var2, -var3, var4, this.bottomColor);
         this.vertex(var1, this.x + this.width + var2, this.y + this.height + var2, var3, var4, this.bottomColor);
         this.vertex(var1, this.x + this.width + var2, this.y - var2, var3, -var4, this.color);
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

   private static final class w {
      static final RenderPipeline y = T(0, false);
      static final RenderPipeline E = T(1, false);
      static final RenderPipeline X = T(2, false);
      static final RenderPipeline s = T(-1, false);
      static final RenderPipeline i = N(false);
      static final RenderPipeline g = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_rounded_ripple"))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withVertexBinding(0, ow.O)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline f = T(0, true);
      static final RenderPipeline Y = T(1, true);
      static final RenderPipeline h = T(2, true);
      static final RenderPipeline d = T(-1, true);
      static final RenderPipeline T = N(true);

      private static RenderPipeline N(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var0 ? ow.Z : DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline T(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var1 ? ow.x : DefaultVertexFormat.POSITION_TEX_COLOR);
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
}
