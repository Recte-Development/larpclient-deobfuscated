package com.larpclient.render.overlay;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.util.misc.LarpLog;
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

public final class SdfText {
   public static final Identifier aTLAS_TEXTURE = identifier("textures/gui/hud_sdf.png");
   public static final Identifier mETRICS_FILE = identifier("textures/gui/hud_sdf.json");
   public static final float bASELINE_RATIO = 0.6363636F;
   public static final float cAP_HEIGHT = 0.729F;
   public static final float x_HEIGHT = 0.5625F;
   private static final int dIRECT_GLYPH_LIMIT = 8704;
   private static boolean requested;
   private static boolean ready;
   private static boolean failed;
   private static SdfText.SdfFont font;
   private static final int cACHE_LIMIT = 512;
   private static final float[] eMPTY_VERTICES = new float[0];
   private static final Float2ObjectOpenHashMap<HashMap<String, float[]>> vertexCache = new Float2ObjectOpenHashMap();
   private static int cacheSize;

   private SdfText() {
   }

   private static Identifier identifier(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static void initialize() {
      if (requested && !ready && !failed) {
         try {
            ClientPipelines.validatePipeline(SdfText.SdfPipelines.sdfTextPipeline);
            if (Minecraft.getInstance().getResourceManager().getResource(aTLAS_TEXTURE).isEmpty()) {
               throw new IllegalStateException("Missing " + aTLAS_TEXTURE);
            }

            try (InputStreamReader var0 = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(mETRICS_FILE), StandardCharsets.UTF_8)) {
               font = SdfText.SdfFont.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            ready = (boolean)1;
            FontRenderer.invalidateCache();
            LarpLog.info("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var5) {
            failed = (boolean)1;
            font = null;
            LarpLog.error("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var5);
         }
      }
   }

   public static void reset() {
      boolean var0 = ready;
      failed = (boolean)0;
      ready = (boolean)0;
      font = null;
      vertexCache.clear();
      cacheSize = 0;
      if (var0) {
         FontRenderer.invalidateCache();
      }
   }

   private static SdfText.SdfFont getFont() {
      requested = (boolean)1;
      return ready ? font : null;
   }

   public static double centerBaselineY(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.27186364F * var4;
   }

   public static double drawIcon(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.35511363F * var4;
   }

   public static float measureWidth(String var0, double var1) {
      SdfText.SdfFont var3 = getFont();
      if (var3 == null) {
         return measureFallback(var0) * (float)(var1 / 11.0);
      }

      float var4 = 0.0F;
      int var5 = -1;
      int var6 = 0;

      while (var6 < var0.length()) {
         int var7 = var0.codePointAt(var6);
         SdfText.Glyph var8 = var3.glyph(var7);
         if (var8 == null) {
            if (var5 < 0) {
               var5 = var6;
            }
         } else {
            if (var5 >= 0) {
               var4 += measureFallback(var0.substring(var5, var6)) * (float)(var1 / 11.0);
               var5 = -1;
            }

            var4 += var8.advance() * (float)var1;
         }

         var6 += Character.charCount(var7);
      }

      if (var5 >= 0) {
         var4 += measureFallback(var0.substring(var5)) * (float)(var1 / 11.0);
      }

      return var4;
   }

   private static float measureFallback(String var0) {
      return FontRenderer.measureRawWidth(var0, FontRenderer.Face.MEDIUM);
   }

   public static String truncate(String var0, double var1, double var3) {
      if (measureWidth(var0, var3) <= var1) {
         return var0;
      }

      int var5 = var0.length();

      while (var5 > 0 && measureWidth(var0.substring(0, var5) + "…", var3) > var1) {
         var5 = var0.offsetByCodePoints(var5, -1);
      }

      return var5 == 0 ? "" : var0.substring(0, var5) + "…";
   }

   public static void drawText(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = AlphaScope.applyAlpha(var8);
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         SdfText.SdfFont var9 = getFont();
         if (var9 == null) {
            drawFallback(var0, var1, var2, var4, var6, var8);
         } else {
            float[] var10 = getCachedVertices(var9, var1, (float)var6);
            if (var10 != null) {
               submitQuads(var0, var10, (float)var2, (float)var4, var8);
            } else {
               int var11 = var8;
               float[] var12 = new float[var1.length() * 16];
               int var13 = buildVertices(var9, var1, (float)var2, (float)var4, (float)var6, var12, (var6x, var7) -> {
                  drawFallback(var0, var6x, var7, var4, var6, var11);
                  return measureFallback(var6x) * (float)(var6 / 11.0);
               });
               submitVertices(var0, var12, var13, var8);
            }
         }
      }
   }

   static float[] getCachedVertices(SdfText.SdfFont var0, String var1, float var2) {
      HashMap var3 = (HashMap)vertexCache.get(var2);
      if (var3 == null) {
         vertexCache.put(var2, var3 = new HashMap());
      }

      float[] var4 = (float[])var3.get(var1);
      if (var4 == null) {
         if (cacheSize >= 512) {
            vertexCache.clear();
            cacheSize = 0;
            vertexCache.put(var2, var3 = new HashMap());
         }

         float[] var5 = new float[var1.length() * 16];
         boolean[] var6 = new boolean[1];
         int var7 = buildVertices(var0, var1, 0.0F, 0.0F, var2, var5, (var1x, var2x) -> {
            var6[0] = (boolean)1;
            return 0.0F;
         });
         var4 = var6[0] ? eMPTY_VERTICES : Arrays.copyOf(var5, var7 * 16);
         var3.put(var1, var4);
         cacheSize++;
      }

      return var4 == eMPTY_VERTICES ? null : var4;
   }

   public static int buildVertices(SdfText.SdfFont var0, String var1, float var2, float var3, float var4, float[] var5, SdfText.FallbackDrawer var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.6363636F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         SdfText.Glyph var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.drawFallback(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = writeQuad(
                  var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13
               );
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.drawFallback(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void drawFallback(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / 11.0);
      var0.pose().pushMatrix();
      var0.pose().translate((float)var2, (float)var4).scale(var9, var9);
      FontRenderer.drawTextRaw(var0, var1, 0, -1, var8, FontRenderer.Face.MEDIUM);
      var0.pose().popMatrix();
   }

   public static boolean drawIcon(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      SdfText.SdfFont var9 = getFont();
      SdfText.Glyph var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      }

      var8 = AlphaScope.applyAlpha(var8);
      if (var8 >>> 24 < 4) {
         return true;
      }

      float[] var11 = new float[16];
      submitVertices(var0, var11, addIconQuad(var10, (float)var2, (float)var4, (float)var6, var11), var8);
      return true;
   }

   public static int addIconQuad(SdfText.Glyph var0, float var1, float var2, float var3, float[] var4) {
      return writeQuad(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int writeQuad(float[] var0, int var1, float var2, float var3, float var4, float var5, SdfText.Glyph var6) {
      int var7 = var1 * 16;
      var0[var7] = var2;
      var0[var7 + 1] = var3;
      var0[var7 + 2] = var6.minU();
      var0[var7 + 3] = var6.minV();
      var0[var7 + 4] = var2;
      var0[var7 + 5] = var5;
      var0[var7 + 6] = var6.minU();
      var0[var7 + 7] = var6.maxV();
      var0[var7 + 8] = var4;
      var0[var7 + 9] = var5;
      var0[var7 + 10] = var6.maxU();
      var0[var7 + 11] = var6.maxV();
      var0[var7 + 12] = var4;
      var0[var7 + 13] = var3;
      var0[var7 + 14] = var6.maxU();
      var0[var7 + 15] = var6.minV();
      return var1 + 1;
   }

   private static void submitVertices(GuiGraphicsExtractor var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         submitQuads(var0, Arrays.copyOf(var1, var2 * 16), 0.0F, 0.0F, var3);
      }
   }

   private static void submitQuads(GuiGraphicsExtractor var0, float[] var1, float var2, float var3, int var4) {
      if (var1.length != 0) {
         AbstractTexture var5 = Minecraft.getInstance().getTextureManager().getTexture(aTLAS_TEXTURE);
         TextureSetup var6 = TextureSetup.singleTexture(var5.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var0.guiRenderState.addGuiElement(new SdfText.SdfTextElement(var0.pose(), var1, var2, var3, var4, var6, var0.scissorStack.peek()));
      }
   }

   public interface FallbackDrawer {
      float drawFallback(String var1, float var2);
   }

   public record Glyph(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      private final float minU;
      private final float minV;
      private final float maxU;
      private final float maxV;

      public Glyph(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
         this.advance = advance;
         this.left = left;
         this.top = top;
         this.right = right;
         this.bottom = bottom;
         this.minU = u0;
         this.minV = v0;
         this.maxU = u1;
         this.maxV = v1;
      }

      boolean visible() {
         return this.maxU > this.minU;
      }
   }

   public record SdfFont(SdfText.Glyph[] direct, Map<Integer, SdfText.Glyph> extended, Map<String, SdfText.Glyph> icons) {
      public SdfText.Glyph glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static SdfText.SdfFont parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         SdfText.Glyph[] var4 = new SdfText.Glyph[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            SdfText.Glyph var8 = glyph(var7.getAsJsonObject(), var2, var3);
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

         return new SdfText.SdfFont(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static SdfText.Glyph glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new SdfText.Glyph(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         JsonArray var4 = var0.getAsJsonArray("plane");
         JsonArray var5 = var0.getAsJsonArray("atlas");
         float var6 = var5.get(0).getAsFloat();
         float var7 = var5.get(1).getAsFloat();
         return new SdfText.Glyph(
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

   private static final class SdfPipelines {
      static final RenderPipeline sdfTextPipeline = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(SdfText.identifier("gui_sdf"))
         .withFragmentShader(SdfText.identifier("gui/sdf"))
         .build();
   }

   public record SdfTextElement(
      Matrix3x2fc pose,
      float[] vertices,
      float offsetX,
      float offsetY,
      int color,
      TextureSetup textureSetup,
      ScreenRectangle scissorArea,
      ScreenRectangle bounds
   ) implements GuiElementRenderState {
      public SdfTextElement(Matrix3x2fc var1, float[] var2, float var3, float var4, int var5, TextureSetup var6, ScreenRectangle var7) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, bounds(var1, var2, var3, var4, var7));
      }

      public RenderPipeline pipeline() {
         return SdfText.SdfPipelines.sdfTextPipeline;
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
}
