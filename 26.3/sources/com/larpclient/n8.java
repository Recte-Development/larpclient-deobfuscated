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

public final class n8 {
   public static final Identifier g = i("textures/gui/hud_sdf.png");
   public static final Identifier w = i("textures/gui/hud_sdf.json");
   public static final float U = 0.6363636F;
   public static final float G = 0.729F;
   public static final float x = 0.5625F;
   private static final int C = 8704;
   private static boolean R;
   private static boolean h;
   private static boolean Y;
   private static n8.e E;
   private static final int M = 512;
   private static final float[] l = new float[0];
   private static final Float2ObjectOpenHashMap<HashMap<String, float[]>> L = new Float2ObjectOpenHashMap();
   private static int i;

   private n8() {
   }

   private static Identifier i(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static void u() {
      if (R && !h && !Y) {
         try {
            se.j(n8.n.Z);
            if (Minecraft.getInstance().getResourceManager().getResource(g).isEmpty()) {
               throw new IllegalStateException("Missing " + g);
            }

            try (InputStreamReader var0 = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(w), StandardCharsets.UTF_8)) {
               E = n8.e.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            h = (boolean)1;
            ic.C();
            _q.z("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var5) {
            Y = (boolean)1;
            E = null;
            _q.o("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var5);
         }
      }
   }

   public static void l() {
      boolean var0 = h;
      Y = (boolean)0;
      h = (boolean)0;
      E = null;
      L.clear();
      i = 0;
      if (var0) {
         ic.C();
      }
   }

   private static n8.e U() {
      R = (boolean)1;
      return h ? E : null;
   }

   public static double b(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.27186364F * var4;
   }

   public static double y(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.35511363F * var4;
   }

   public static float S(String var0, double var1) {
      n8.e var3 = U();
      if (var3 == null) {
         return r(var0) * (float)(var1 / 11.0);
      }

      float var4 = 0.0F;
      int var5 = -1;
      int var6 = 0;

      while (var6 < var0.length()) {
         int var7 = var0.codePointAt(var6);
         n8.b var8 = var3.glyph(var7);
         if (var8 == null) {
            if (var5 < 0) {
               var5 = var6;
            }
         } else {
            if (var5 >= 0) {
               var4 += r(var0.substring(var5, var6)) * (float)(var1 / 11.0);
               var5 = -1;
            }

            var4 += var8.advance() * (float)var1;
         }

         var6 += Character.charCount(var7);
      }

      if (var5 >= 0) {
         var4 += r(var0.substring(var5)) * (float)(var1 / 11.0);
      }

      return var4;
   }

   private static float r(String var0) {
      return ic.z(var0, ic.b.R);
   }

   public static String I(String var0, double var1, double var3) {
      if (S(var0, var3) <= var1) {
         return var0;
      }

      int var5 = var0.length();

      while (var5 > 0 && S(var0.substring(0, var5) + "…", var3) > var1) {
         var5 = var0.offsetByCodePoints(var5, -1);
      }

      return var5 == 0 ? "" : var0.substring(0, var5) + "…";
   }

   public static void X(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = oa.e(var8);
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         n8.e var9 = U();
         if (var9 == null) {
            P(var0, var1, var2, var4, var6, var8);
         } else {
            float[] var10 = m(var9, var1, (float)var6);
            if (var10 != null) {
               W(var0, var10, (float)var2, (float)var4, var8);
            } else {
               int var11 = var8;
               float[] var12 = new float[var1.length() * 16];
               int var13 = Q(var9, var1, (float)var2, (float)var4, (float)var6, var12, (var6x, var7) -> {
                  P(var0, var6x, var7, var4, var6, var11);
                  return r(var6x) * (float)(var6 / 11.0);
               });
               K(var0, var12, var13, var8);
            }
         }
      }
   }

   static float[] m(n8.e var0, String var1, float var2) {
      HashMap var3 = (HashMap)L.get(var2);
      if (var3 == null) {
         L.put(var2, var3 = new HashMap());
      }

      float[] var4 = (float[])var3.get(var1);
      if (var4 == null) {
         if (i >= 512) {
            L.clear();
            i = 0;
            L.put(var2, var3 = new HashMap());
         }

         float[] var5 = new float[var1.length() * 16];
         boolean[] var6 = new boolean[1];
         int var7 = Q(var0, var1, 0.0F, 0.0F, var2, var5, (var1x, var2x) -> {
            var6[0] = (boolean)1;
            return 0.0F;
         });
         var4 = var6[0] ? l : Arrays.copyOf(var5, var7 * 16);
         var3.put(var1, var4);
         i++;
      }

      return var4 == l ? null : var4;
   }

   public static int Q(n8.e var0, String var1, float var2, float var3, float var4, float[] var5, n8.w var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.6363636F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         n8.b var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.z(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = V(var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13);
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.z(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void P(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / 11.0);
      var0.pose().pushMatrix();
      var0.pose().translate((float)var2, (float)var4).scale(var9, var9);
      ic.Y(var0, var1, 0, -1, var8, ic.b.R);
      var0.pose().popMatrix();
   }

   public static boolean y(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      n8.e var9 = U();
      n8.b var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      }

      var8 = oa.e(var8);
      if (var8 >>> 24 < 4) {
         return true;
      }

      float[] var11 = new float[16];
      K(var0, var11, o(var10, (float)var2, (float)var4, (float)var6, var11), var8);
      return true;
   }

   public static int o(n8.b var0, float var1, float var2, float var3, float[] var4) {
      return V(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int V(float[] var0, int var1, float var2, float var3, float var4, float var5, n8.b var6) {
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

   private static void K(GuiGraphicsExtractor var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         W(var0, Arrays.copyOf(var1, var2 * 16), 0.0F, 0.0F, var3);
      }
   }

   private static void W(GuiGraphicsExtractor var0, float[] var1, float var2, float var3, int var4) {
      if (var1.length != 0) {
         AbstractTexture var5 = Minecraft.getInstance().getTextureManager().getTexture(g);
         TextureSetup var6 = TextureSetup.singleTexture(var5.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var0.guiRenderState.addGuiElement(new n8.g(var0.pose(), var1, var2, var3, var4, var6, var0.scissorStack.peek()));
      }
   }

   public record b(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      boolean visible() {
         return this.u1 > this.u0;
      }
   }

   public record e(n8.b[] direct, Map<Integer, n8.b> extended, Map<String, n8.b> icons) {
      public n8.b glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static n8.e parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         n8.b[] var4 = new n8.b[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            n8.b var8 = glyph(var7.getAsJsonObject(), var2, var3);
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

         return new n8.e(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static n8.b glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new n8.b(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         JsonArray var4 = var0.getAsJsonArray("plane");
         JsonArray var5 = var0.getAsJsonArray("atlas");
         float var6 = var5.get(0).getAsFloat();
         float var7 = var5.get(1).getAsFloat();
         return new n8.b(
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

   public record g(
      Matrix3x2fc pose,
      float[] vertices,
      float offsetX,
      float offsetY,
      int color,
      TextureSetup textureSetup,
      ScreenRectangle scissorArea,
      ScreenRectangle bounds
   ) implements GuiElementRenderState {
      public g(Matrix3x2fc var1, float[] var2, float var3, float var4, int var5, TextureSetup var6, ScreenRectangle var7) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, bounds(var1, var2, var3, var4, var7));
      }

      public RenderPipeline pipeline() {
         return n8.n.Z;
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

   private static final class n {
      static final RenderPipeline Z = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(n8.i("gui_sdf"))
         .withFragmentShader(n8.i("gui/sdf"))
         .build();
   }

   public interface w {
      float z(String var1, float var2);
   }
}
