package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
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

public final class c2 {
   public static final Identifier B = q("textures/gui/hud_sdf.png");
   public static final Identifier Y = q("textures/gui/hud_sdf.json");
   public static final float d = 0.6363636F;
   public static final float D = 0.729F;
   public static final float g = 0.5625F;
   private static final int q = 8704;
   private static boolean m;
   private static boolean x;
   private static boolean Z;
   private static c2.y Q;
   private static final int o = 512;
   private static final float[] p = new float[0];
   private static final Float2ObjectOpenHashMap<HashMap<String, float[]>> t = new Float2ObjectOpenHashMap();
   private static int R;

   private c2() {
   }

   private static Identifier q(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static void Q() {
      if (m && !x && !Z) {
         try {
            ai.U(c2.q.X);
            if (Minecraft.getInstance().getResourceManager().getResource(B).isEmpty()) {
               throw new IllegalStateException("Missing " + B);
            }

            try (InputStreamReader var0 = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(Y), StandardCharsets.UTF_8)) {
               Q = c2.y.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            x = (boolean)1;
            bj.I();
            h.A("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var5) {
            Z = (boolean)1;
            Q = null;
            h.m("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var5);
         }
      }
   }

   public static void K() {
      boolean var0 = x;
      Z = (boolean)0;
      x = (boolean)0;
      Q = null;
      t.clear();
      R = 0;
      if (var0) {
         bj.I();
      }
   }

   private static c2.y N() {
      m = (boolean)1;
      return x ? Q : null;
   }

   public static double X(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.27186364F * var4;
   }

   public static double C(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.35511363F * var4;
   }

   public static float M(String var0, double var1) {
      c2.y var3 = N();
      if (var3 == null) {
         return S(var0) * (float)(var1 / 11.0);
      }

      float var4 = 0.0F;
      int var5 = -1;
      int var6 = 0;

      while (var6 < var0.length()) {
         int var7 = var0.codePointAt(var6);
         c2.r var8 = var3.glyph(var7);
         if (var8 == null) {
            if (var5 < 0) {
               var5 = var6;
            }
         } else {
            if (var5 >= 0) {
               var4 += S(var0.substring(var5, var6)) * (float)(var1 / 11.0);
               var5 = -1;
            }

            var4 += var8.advance() * (float)var1;
         }

         var6 += Character.charCount(var7);
      }

      if (var5 >= 0) {
         var4 += S(var0.substring(var5)) * (float)(var1 / 11.0);
      }

      return var4;
   }

   private static float S(String var0) {
      return bj.G(var0, bj.j.T);
   }

   public static String M(String var0, double var1, double var3) {
      if (M(var0, var3) <= var1) {
         return var0;
      }

      int var5 = var0.length();

      while (var5 > 0 && M(var0.substring(0, var5) + "…", var3) > var1) {
         var5 = var0.offsetByCodePoints(var5, -1);
      }

      return var5 == 0 ? "" : var0.substring(0, var5) + "…";
   }

   public static void V(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = ua.I(var8);
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         c2.y var9 = N();
         if (var9 == null) {
            d(var0, var1, var2, var4, var6, var8);
         } else {
            float[] var10 = R(var9, var1, (float)var6);
            if (var10 != null) {
               w(var0, var10, (float)var2, (float)var4, var8);
            } else {
               int var11 = var8;
               float[] var12 = new float[var1.length() * 16];
               int var13 = Z(var9, var1, (float)var2, (float)var4, (float)var6, var12, (var6x, var7) -> {
                  d(var0, var6x, var7, var4, var6, var11);
                  return S(var6x) * (float)(var6 / 11.0);
               });
               y(var0, var12, var13, var8);
            }
         }
      }
   }

   static float[] R(c2.y var0, String var1, float var2) {
      HashMap var3 = (HashMap)t.get(var2);
      if (var3 == null) {
         t.put(var2, var3 = new HashMap());
      }

      float[] var4 = (float[])var3.get(var1);
      if (var4 == null) {
         if (R >= 512) {
            t.clear();
            R = 0;
            t.put(var2, var3 = new HashMap());
         }

         float[] var5 = new float[var1.length() * 16];
         boolean[] var6 = new boolean[1];
         int var7 = Z(var0, var1, 0.0F, 0.0F, var2, var5, (var1x, var2x) -> {
            var6[0] = (boolean)1;
            return 0.0F;
         });
         var4 = var6[0] ? p : Arrays.copyOf(var5, var7 * 16);
         var3.put(var1, var4);
         R++;
      }

      return var4 == p ? null : var4;
   }

   public static int Z(c2.y var0, String var1, float var2, float var3, float var4, float[] var5, c2.n var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.6363636F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         c2.r var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.P(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = c(var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13);
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.P(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void d(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / 11.0);
      var0.pose().pushMatrix();
      var0.pose().translate((float)var2, (float)var4).scale(var9, var9);
      bj.z(var0, var1, 0, -1, var8, bj.j.T);
      var0.pose().popMatrix();
   }

   public static boolean p(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      c2.y var9 = N();
      c2.r var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      }

      var8 = ua.I(var8);
      if (var8 >>> 24 < 4) {
         return true;
      }

      float[] var11 = new float[16];
      y(var0, var11, h(var10, (float)var2, (float)var4, (float)var6, var11), var8);
      return true;
   }

   public static int h(c2.r var0, float var1, float var2, float var3, float[] var4) {
      return c(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int c(float[] var0, int var1, float var2, float var3, float var4, float var5, c2.r var6) {
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

   private static void y(GuiGraphicsExtractor var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         w(var0, Arrays.copyOf(var1, var2 * 16), 0.0F, 0.0F, var3);
      }
   }

   private static void w(GuiGraphicsExtractor var0, float[] var1, float var2, float var3, int var4) {
      if (var1.length != 0) {
         AbstractTexture var5 = Minecraft.getInstance().getTextureManager().getTexture(B);
         TextureSetup var6 = TextureSetup.singleTexture(var5.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var0.guiRenderState.addGuiElement(new c2.u(var0.pose(), var1, var2, var3, var4, var6, var0.scissorStack.peek()));
      }
   }

   public interface n {
      float P(String var1, float var2);
   }

   private static final class q {
      static final RenderPipeline X = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(c2.q("gui_sdf"))
         .withFragmentShader(c2.q("gui/sdf"))
         .build();
   }

   public record r(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      boolean visible() {
         return this.u1 > this.u0;
      }
   }

   public record u(
      Matrix3x2fc pose,
      float[] vertices,
      float offsetX,
      float offsetY,
      int color,
      TextureSetup textureSetup,
      ScreenRectangle scissorArea,
      ScreenRectangle bounds
   ) implements GuiElementRenderState {
      public u(Matrix3x2fc var1, float[] var2, float var3, float var4, int var5, TextureSetup var6, ScreenRectangle var7) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, bounds(var1, var2, var3, var4, var7));
      }

      public RenderPipeline pipeline() {
         return c2.q.X;
      }

      public void buildVertices(VertexConsumer var1) {
         for (byte var2 = 0; var2 < this.vertices.length; var2 += 4) {
            float var3 = this.vertices[var2] + this.offsetX;
            float var4 = this.vertices[var2 + 1] + this.offsetY;
            var1.addVertex(
                  this.pose.m00() * var3 + this.pose.m10() * var4 + this.pose.m20(), this.pose.m01() * var3 + this.pose.m11() * var4 + this.pose.m21(), 0.0F
               )
               .setUv(this.vertices[var2 + 2], this.vertices[var2 + 3])
               .setColor(this.color);
         }
      }

      static ScreenRectangle bounds(Matrix3x2fc var0, float[] var1, float var2, float var3, ScreenRectangle var4) {
         float var5 = Float.MAX_VALUE;
         float var6 = Float.MAX_VALUE;
         float var7 = -Float.MAX_VALUE;
         float var8 = -Float.MAX_VALUE;

         for (byte var9 = 0; var9 < var1.length; var9 += 4) {
            var5 = Math.min(var5, var1[var9] + var2);
            var7 = Math.max(var7, var1[var9] + var2);
            var6 = Math.min(var6, var1[var9 + 1] + var3);
            var8 = Math.max(var8, var1[var9 + 1] + var3);
         }

         int var12 = (int)Math.floor(var5);
         int var10 = (int)Math.floor(var6);
         ScreenRectangle var11 = new ScreenRectangle(var12, var10, (int)Math.ceil(var7) - var12, (int)Math.ceil(var8) - var10).transformMaxBounds(var0);
         return var4 == null ? var11 : var4.intersection(var11);
      }
   }

   public record y(c2.r[] direct, Map<Integer, c2.r> extended, Map<String, c2.r> icons) {
      public c2.r glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static c2.y parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         c2.r[] var4 = new c2.r[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            c2.r var8 = glyph(var7.getAsJsonObject(), var2, var3);
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

         return new c2.y(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static c2.r glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new c2.r(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         JsonArray var4 = var0.getAsJsonArray("plane");
         JsonArray var5 = var0.getAsJsonArray("atlas");
         float var6 = var5.get(0).getAsFloat();
         float var7 = var5.get(1).getAsFloat();
         return new c2.r(
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
}
