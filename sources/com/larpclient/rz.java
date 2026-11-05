package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class rz {
   public static final Identifier l = j("textures/gui/hud_sdf.png");
   public static final Identifier h = j("textures/gui/hud_sdf.json");
   public static final float j = 0.6363636F;
   public static final float o = 0.729F;
   public static final float N = 0.5625F;
   private static final int z = 8704;
   private static boolean r;
   private static boolean U;
   private static boolean X;
   private static rz.b g;

   private rz() {
   }

   private static Identifier j(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static void U() {
      if (r && !U && !X) {
         try {
            e4.U(rz.t.E);
            if (Minecraft.getInstance().getResourceManager().getResource(l).isEmpty()) {
               throw new IllegalStateException("Missing " + l);
            }

            try (InputStreamReader var0 = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(h), StandardCharsets.UTF_8)) {
               g = rz.b.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            U = (boolean)1;
            j_.H();
            r4.V("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var5) {
            X = (boolean)1;
            g = null;
            r4.r("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var5);
         }
      }
   }

   public static void C() {
      boolean var0 = U;
      X = (boolean)0;
      U = (boolean)0;
      g = null;
      if (var0) {
         j_.H();
      }
   }

   private static rz.b a() {
      r = (boolean)1;
      return U ? g : null;
   }

   public static double W(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.27186364F * var4;
   }

   public static double t(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.35511363F * var4;
   }

   public static float V(String var0, double var1) {
      rz.b var3 = a();
      if (var3 == null) {
         return P(var0) * (float)(var1 / 11.0);
      }

      float var4 = 0.0F;
      int var5 = -1;
      int var6 = 0;

      while (var6 < var0.length()) {
         int var7 = var0.codePointAt(var6);
         rz.q var8 = var3.glyph(var7);
         if (var8 == null) {
            if (var5 < 0) {
               var5 = var6;
            }
         } else {
            if (var5 >= 0) {
               var4 += P(var0.substring(var5, var6)) * (float)(var1 / 11.0);
               var5 = -1;
            }

            var4 += var8.advance() * (float)var1;
         }

         var6 += Character.charCount(var7);
      }

      if (var5 >= 0) {
         var4 += P(var0.substring(var5)) * (float)(var1 / 11.0);
      }

      return var4;
   }

   private static float P(String var0) {
      return j_.E(var0, j_.w.Q);
   }

   public static String H(String var0, double var1, double var3) {
      if (V(var0, var3) <= var1) {
         return var0;
      }

      int var5 = var0.length();

      while (var5 > 0 && V(var0.substring(0, var5) + "…", var3) > var1) {
         var5 = var0.offsetByCodePoints(var5, -1);
      }

      return var5 == 0 ? "" : var0.substring(0, var5) + "…";
   }

   public static void j(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = com.larpclient.r.D(var8);
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         rz.b var9 = a();
         if (var9 == null) {
            h(var0, var1, var2, var4, var6, var8);
         } else {
            int var10 = var8;
            float[] var11 = new float[var1.length() * 16];
            int var12 = T(var9, var1, (float)var2, (float)var4, (float)var6, var11, (var6x, var7) -> {
               h(var0, var6x, var7, var4, var6, var10);
               return P(var6x) * (float)(var6 / 11.0);
            });
            z(var0, var11, var12, var8);
         }
      }
   }

   public static int T(rz.b var0, String var1, float var2, float var3, float var4, float[] var5, rz.u var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.6363636F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         rz.q var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.m(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = r(var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13);
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.m(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void h(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / 11.0);
      var0.pose().pushMatrix();
      var0.pose().translate((float)var2, (float)var4).scale(var9, var9);
      j_.y(var0, var1, 0, -1, var8, j_.w.Q);
      var0.pose().popMatrix();
   }

   public static boolean n(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      rz.b var9 = a();
      rz.q var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      }

      var8 = com.larpclient.r.D(var8);
      if (var8 >>> 24 < 4) {
         return true;
      }

      float[] var11 = new float[16];
      z(var0, var11, S(var10, (float)var2, (float)var4, (float)var6, var11), var8);
      return true;
   }

   public static int S(rz.q var0, float var1, float var2, float var3, float[] var4) {
      return r(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int r(float[] var0, int var1, float var2, float var3, float var4, float var5, rz.q var6) {
      int var7 = var1 * 16;
      var0[var7] = var2;
      var0[var7 + 1] = var3;
      var0[var7 + 2] = var6.u0();
      var0[var7 + 3] = var6.v0();
      var0[var7 + 4] = var2;
      var0[var7 + 5] = var5;
      var0[var7 + 6] = var6.u0();
      var0[var7 + 7] = var6.v1();
      var0[var7 + 8] = var4;
      var0[var7 + 9] = var5;
      var0[var7 + 10] = var6.u1();
      var0[var7 + 11] = var6.v1();
      var0[var7 + 12] = var4;
      var0[var7 + 13] = var3;
      var0[var7 + 14] = var6.u1();
      var0[var7 + 15] = var6.v0();
      return var1 + 1;
   }

   private static void z(GuiGraphicsExtractor var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         AbstractTexture var4 = Minecraft.getInstance().getTextureManager().getTexture(l);
         TextureSetup var5 = TextureSetup.singleTexture(var4.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var0.guiRenderState.addGuiElement(new rz.o(var0.pose(), Arrays.copyOf(var1, var2 * 16), var3, var5, var0.scissorStack.peek()));
      }
   }

   public record b(rz.q[] direct, Map<Integer, rz.q> extended, Map<String, rz.q> icons) {
      public rz.q glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static rz.b parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         rz.q[] var4 = new rz.q[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            rz.q var8 = glyph(var7.getAsJsonObject(), var2, var3);
            int var9 = var7.getAsJsonObject().get("unicode").getAsInt();
            if (var9 < 8704) {
               var4[var9] = var8;
            } else {
               var5.put(var9, var8);
            }
         }

         HashMap var10 = new HashMap();

         for (JsonElement var12 : var0.getAsJsonArray("icons")) {
            var10.put(var12.getAsJsonObject().get("name").getAsString(), glyph(var12.getAsJsonObject(), var2, var3));
         }

         return new rz.b(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static rz.q glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new rz.q(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         JsonArray var4 = var0.getAsJsonArray("plane");
         JsonArray var5 = var0.getAsJsonArray("atlas");
         float var6 = var5.get(0).getAsFloat();
         float var7 = var5.get(1).getAsFloat();
         return new rz.q(
            var3,
            var4.get(0).getAsFloat(),
            var4.get(1).getAsFloat(),
            var4.get(2).getAsFloat(),
            var4.get(3).getAsFloat(),
            var6 / var1,
            var7 / var2,
            (var6 + var5.get(2).getAsFloat()) / var1,
            (var7 + var5.get(3).getAsFloat()) / var2
         );
      }
   }

   public record o(Matrix3x2fc pose, float[] vertices, int color, TextureSetup textureSetup, ScreenRectangle scissorArea, ScreenRectangle bounds)
      implements GuiElementRenderState {
      public o(Matrix3x2fc var1, float[] var2, int var3, TextureSetup var4, ScreenRectangle var5) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, bounds(var1, var2, var5));
      }

      public RenderPipeline pipeline() {
         return rz.t.E;
      }

      public void buildVertices(VertexConsumer var1) {
         for (byte var2 = 0; var2 < this.vertices.length; var2 += 4) {
            float var3 = this.vertices[var2];
            float var4 = this.vertices[var2 + 1];
            var1.addVertex(
                  this.pose.m00() * var3 + this.pose.m10() * var4 + this.pose.m20(), this.pose.m01() * var3 + this.pose.m11() * var4 + this.pose.m21(), 0.0F
               )
               .setUv(this.vertices[var2 + 2], this.vertices[var2 + 3])
               .setColor(this.color);
         }
      }

      static ScreenRectangle bounds(Matrix3x2fc var0, float[] var1, ScreenRectangle var2) {
         float var3 = Float.MAX_VALUE;
         float var4 = Float.MAX_VALUE;
         float var5 = -Float.MAX_VALUE;
         float var6 = -Float.MAX_VALUE;

         for (byte var7 = 0; var7 < var1.length; var7 += 4) {
            var3 = Math.min(var3, var1[var7]);
            var5 = Math.max(var5, var1[var7]);
            var4 = Math.min(var4, var1[var7 + 1]);
            var6 = Math.max(var6, var1[var7 + 1]);
         }

         int var10 = (int)Math.floor(var3);
         int var8 = (int)Math.floor(var4);
         ScreenRectangle var9 = new ScreenRectangle(var10, var8, (int)Math.ceil(var5) - var10, (int)Math.ceil(var6) - var8).transformMaxBounds(var0);
         return var2 == null ? var9 : var2.intersection(var9);
      }
   }

   public record q(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      boolean visible() {
         return this.u1 > this.u0;
      }
   }

   private static final class t {
      static final RenderPipeline E = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(rz.j("gui_sdf"))
         .withFragmentShader(rz.j("gui/sdf"))
         .build();
   }

   public interface u {
      float m(String var1, float var2);
   }
}
