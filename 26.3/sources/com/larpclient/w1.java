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

public final class w1 {
   private static boolean y;
   private static boolean g;
   private static boolean M;
   private static boolean D;
   private static boolean m;
   private static boolean u;
   public static final int Q = 2;
   private static final VertexFormat X = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat t = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat p = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .build();
   private static boolean O;
   private static boolean I;
   private static boolean o;
   public static final float K = 64.0F;
   private static boolean W;
   private static boolean G;
   private static boolean k;
   private static boolean T;
   private static boolean U;

   private w1() {
   }

   public static boolean B(RenderPipeline var0) {
      return var0 == w1.e.E || var0 == w1.e.D || var0 == w1.e.K || var0 == w1.e.h || var0 == w1.e.O || var0 == w1.e.k;
   }

   public static RenderPipeline q(RenderPipeline var0) {
      if (B(var0) && var0 != w1.e.k) {
         W = (boolean)1;
         if (var0 == w1.e.O) {
            return T ? w1.e.s : null;
         } else if (!G) {
            return null;
         } else {
            return var0 == w1.e.E ? w1.e.n : (var0 == w1.e.D ? w1.e.B : (var0 == w1.e.K ? w1.e.N : w1.e.X));
         }
      } else {
         return null;
      }
   }

   public static void U() {
      if (y && !g && !M) {
         try {
            se.j(w1.e.E);
            se.j(w1.e.D);
            se.j(w1.e.K);
            se.j(w1.e.h);
            g = (boolean)1;
            _q.z("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            M = (boolean)1;
            _q.o("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (D && g && !m && !u) {
         try {
            se.j(w1.e.O);
            m = (boolean)1;
            _q.z("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            u = (boolean)1;
            _q.o("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (O && g && !I && !o) {
         try {
            se.j(w1.e.k);
            I = (boolean)1;
            _q.z("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            o = (boolean)1;
            _q.o("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (W && g && !G && !k) {
         try {
            se.j(w1.e.n);
            se.j(w1.e.B);
            se.j(w1.e.N);
            se.j(w1.e.X);
            G = (boolean)1;
            _q.z("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            k = (boolean)1;
            _q.o("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (W && G && m && !T && !U) {
         try {
            se.j(w1.e.s);
            T = (boolean)1;
         } catch (RuntimeException var1) {
            U = (boolean)1;
            _q.o("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void o() {
      o = (boolean)0;
      I = (boolean)0;
      u = (boolean)0;
      m = (boolean)0;
      M = (boolean)0;
      g = (boolean)0;
      U = (boolean)0;
      T = (boolean)0;
      k = (boolean)0;
      G = (boolean)0;
   }

   public static void a(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
            h5.N(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            y = (boolean)1;
            if (!g) {
               wc.y(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = oa.e(var6);
               if (var8 >>> 24 != 0) {
                  var0.guiRenderState.addGuiElement(new w1.o(var0.pose(), var1, var2, var3, var4, V(var7, var3, var4), var8, var0.scissorStack.peek()));
               }
            }
         }
      }
   }

   private static float V(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void p(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            a(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
               h5.f(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = V(var8, var3, var4);
               y = (boolean)1;
               if (!g) {
                  wc.y(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), vv.Q(var6, var7, 0.5));
               } else {
                  var6 = oa.e(var6);
                  var7 = oa.e(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.guiRenderState.addGuiElement(new w1.o(var0.pose(), var1, var2, var3, var4, var8, var6, var7, var0.scissorStack.peek()));
                  }
               }
            }
         }
      }
   }

   public static void c(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            p(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static void T(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         y = (boolean)1;
         if (!g) {
            wc.a(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = oa.e(var6);
            var7 = oa.e(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.guiRenderState.addGuiElement(new w1.o(var0.pose(), var1, var2, var3, var4, var8, var6, var7, true, var0.scissorStack.peek()));
            }
         }
      }
   }

   public static void X(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         y = (boolean)1;
         if (!g) {
            wc.a(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = oa.e(var6);
            if (var6 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new w1.o(
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
                        w1.o.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void h(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         y = (boolean)1;
         if (g) {
            var7 = oa.e(var7);
            var8 = oa.e(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new w1.o(
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
                        w1.o.bounds(var0.pose(), var1, var2, var3, var4, var10, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void W(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         y = (boolean)1;
         D = (boolean)1;
         if (m) {
            var9 = oa.e(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new w1.o(
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
                        w1.o.bounds(var0.pose(), var1, var2, var3, var4, var11, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void A(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = V(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         y = (boolean)1;
         O = (boolean)1;
         if (I) {
            var9 = oa.e(var9);
            if (var9 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new w1.m(
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
                        w1.o.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   private static final class e {
      static final RenderPipeline E = M(0, false);
      static final RenderPipeline D = M(1, false);
      static final RenderPipeline K = M(2, false);
      static final RenderPipeline h = M(-1, false);
      static final RenderPipeline O = y(false);
      static final RenderPipeline k = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_rounded_ripple"))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withVertexBinding(0, w1.p)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline n = M(0, true);
      static final RenderPipeline B = M(1, true);
      static final RenderPipeline N = M(2, true);
      static final RenderPipeline X = M(-1, true);
      static final RenderPipeline s = y(true);

      private static RenderPipeline y(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var0 ? w1.t : DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline M(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var1 ? w1.X : DefaultVertexFormat.POSITION_TEX_COLOR);
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

   public record m(
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
         return w1.e.k;
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

   public record o(
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
      public o(
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

      public o(
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

      public o(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, ScreenRectangle var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public o(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, ScreenRectangle var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public o(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, ScreenRectangle var10) {
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
         return this.contourLight() ? w1.e.O : (this.glow() ? w1.e.h : (this.ring ? w1.e.K : (this.rim ? w1.e.D : w1.e.E)));
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
}
