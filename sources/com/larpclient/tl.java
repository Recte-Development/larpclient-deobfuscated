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

public final class tl {
   private static boolean d;
   private static boolean Y;
   private static boolean R;
   private static boolean b;
   private static boolean r;
   private static boolean F;
   public static final int m = 2;
   private static final VertexFormat z = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat y = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .build();
   private static final VertexFormat H = VertexFormat.builder(0)
      .addAttribute("Position", GpuFormat.RGB32_FLOAT)
      .addAttribute("UV0", GpuFormat.RG32_FLOAT)
      .addAttribute("Color", GpuFormat.RGBA8_UNORM)
      .addAttribute("UV1", GpuFormat.RG16_SINT)
      .addAttribute("UV2", GpuFormat.RG16_SINT)
      .build();
   private static boolean p;
   private static boolean Q;
   private static boolean s;
   public static final float I = 64.0F;
   private static boolean j;
   private static boolean w;
   private static boolean V;
   private static boolean O;
   private static boolean G;

   private tl() {
   }

   public static boolean y(RenderPipeline var0) {
      return var0 == tl.c.e || var0 == tl.c.a || var0 == tl.c.k || var0 == tl.c.p || var0 == tl.c.y || var0 == tl.c.W;
   }

   public static RenderPipeline m(RenderPipeline var0) {
      if (y(var0) && var0 != tl.c.W) {
         j = (boolean)1;
         if (var0 == tl.c.y) {
            return O ? tl.c.K : null;
         } else if (!w) {
            return null;
         } else {
            return var0 == tl.c.e ? tl.c.g : (var0 == tl.c.a ? tl.c.Y : (var0 == tl.c.k ? tl.c.G : tl.c.c));
         }
      } else {
         return null;
      }
   }

   public static void m() {
      if (d && !Y && !R) {
         try {
            ea.z(tl.c.e);
            ea.z(tl.c.a);
            ea.z(tl.c.k);
            ea.z(tl.c.p);
            Y = (boolean)1;
            mt.K("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            R = (boolean)1;
            mt.P("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (b && Y && !r && !F) {
         try {
            ea.z(tl.c.y);
            r = (boolean)1;
            mt.K("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            F = (boolean)1;
            mt.P("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (p && Y && !Q && !s) {
         try {
            ea.z(tl.c.W);
            Q = (boolean)1;
            mt.K("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            s = (boolean)1;
            mt.P("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (j && Y && !w && !V) {
         try {
            ea.z(tl.c.g);
            ea.z(tl.c.Y);
            ea.z(tl.c.G);
            ea.z(tl.c.c);
            w = (boolean)1;
            mt.K("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            V = (boolean)1;
            mt.P("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (j && w && r && !O && !G) {
         try {
            ea.z(tl.c.K);
            O = (boolean)1;
         } catch (RuntimeException var1) {
            G = (boolean)1;
            mt.P("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void k() {
      s = (boolean)0;
      Q = (boolean)0;
      F = (boolean)0;
      r = (boolean)0;
      R = (boolean)0;
      Y = (boolean)0;
      G = (boolean)0;
      O = (boolean)0;
      V = (boolean)0;
      w = (boolean)0;
   }

   public static void g(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
            c7.s(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            d = (boolean)1;
            if (!Y) {
               pn.q(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = de.h(var6);
               if (var8 >>> 24 != 0) {
                  var0.guiRenderState.addGuiElement(new tl.u(var0.pose(), var1, var2, var3, var4, r(var7, var3, var4), var8, var0.scissorStack.peek()));
               }
            }
         }
      }
   }

   private static float r(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void G(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            g(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
               c7.l(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = r(var8, var3, var4);
               d = (boolean)1;
               if (!Y) {
                  pn.q(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), _3.D(var6, var7, 0.5));
               } else {
                  var6 = de.h(var6);
                  var7 = de.h(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.guiRenderState.addGuiElement(new tl.u(var0.pose(), var1, var2, var3, var4, var8, var6, var7, var0.scissorStack.peek()));
                  }
               }
            }
         }
      }
   }

   public static void A(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            G(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static void V(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         d = (boolean)1;
         if (!Y) {
            pn.E(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = de.h(var6);
            var7 = de.h(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.guiRenderState.addGuiElement(new tl.u(var0.pose(), var1, var2, var3, var4, var8, var6, var7, true, var0.scissorStack.peek()));
            }
         }
      }
   }

   public static void M(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         d = (boolean)1;
         if (!Y) {
            pn.E(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = de.h(var6);
            if (var6 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new tl.u(
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
                        tl.u.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void j(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         d = (boolean)1;
         if (Y) {
            var7 = de.h(var7);
            var8 = de.h(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new tl.u(
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
                        tl.u.bounds(var0.pose(), var1, var2, var3, var4, var10, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void Q(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         d = (boolean)1;
         b = (boolean)1;
         if (r) {
            var9 = de.h(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.guiRenderState
                  .addGuiElement(
                     new tl.u(
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
                        tl.u.bounds(var0.pose(), var1, var2, var3, var4, var11, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   public static void q(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = r(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         d = (boolean)1;
         p = (boolean)1;
         if (Q) {
            var9 = de.h(var9);
            if (var9 >>> 24 != 0) {
               var0.guiRenderState
                  .addGuiElement(
                     new tl.l(
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
                        tl.u.bounds(var0.pose(), var1, var2, var3, var4, var0.scissorStack.peek())
                     )
                  );
            }
         }
      }
   }

   private static final class c {
      static final RenderPipeline e = y(0, false);
      static final RenderPipeline a = y(1, false);
      static final RenderPipeline k = y(2, false);
      static final RenderPipeline p = y(-1, false);
      static final RenderPipeline y = I(false);
      static final RenderPipeline W = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_rounded_ripple"))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
         .withVertexBinding(0, tl.H)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline g = y(0, true);
      static final RenderPipeline Y = y(1, true);
      static final RenderPipeline G = y(2, true);
      static final RenderPipeline c = y(-1, true);
      static final RenderPipeline K = I(true);

      private static RenderPipeline I(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var0 ? tl.y : DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline y(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/rounded"))
            .withVertexBinding(0, var1 ? tl.z : DefaultVertexFormat.POSITION_TEX_COLOR);
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

   public record l(
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
         return tl.c.W;
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

   public record u(
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
      public u(
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

      public u(
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

      public u(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, ScreenRectangle var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public u(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, ScreenRectangle var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public u(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, ScreenRectangle var10) {
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
         return this.contourLight() ? tl.c.y : (this.glow() ? tl.c.p : (this.ring ? tl.c.k : (this.rim ? tl.c.a : tl.c.e)));
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
