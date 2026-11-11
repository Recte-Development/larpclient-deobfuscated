package com.larpclient.render.overlay;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage.Format;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix3x2fc;
import org.lwjgl.CLongBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FT_Bitmap;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FT_GlyphSlot;
import org.lwjgl.util.freetype.FT_Vector;
import org.lwjgl.util.freetype.FreeType;

public final class ExactTextRenderer {
   private static final int lOAD_FLAGS = 65544;
   private static final int aTLAS_SIZE = 512;
   private static final int mAX_PAGES = 6;
   private static final int sUBPIXEL_STEPS = 4;
   private static final int mAX_OUTLINE = 63;
   private static long freeTypeLibrary;
   private static final List<ByteBuffer> fontBuffers = new ArrayList<>();
   private static final Map<FontRenderer.Face, ExactTextRenderer.FontFace> fonts = new EnumMap<>(FontRenderer.Face.class);
   private static final Map<Long, ExactTextRenderer.Glyph> glyphCache = new HashMap<>();
   private static final Map<Long, Float> advanceCache = new HashMap<>();
   private static final Map<Long, Boolean> glyphPresenceCache = new HashMap<>();
   private static final List<GpuTexture> pageTextures = new ArrayList<>();
   private static final List<GpuTextureView> pageViews = new ArrayList<>();
   private static int cursorY;
   private static int rowHeight;
   private static int cursorX;
   private static final Map<Long, Integer> faceSizes = new HashMap<>();
   private static int loadedGeneration = -1;
   private static boolean disabled;
   private static boolean atlasFull;

   private ExactTextRenderer() {
   }

   static boolean canRender(FontRenderer.Face var0, String var1) {
      if (var0.name().startsWith("CLICK_") && Minecraft.getInstance() != null) {
         if (ensureLoaded() && !var1.isEmpty() && var1.indexOf(167) < 0) {
            ExactTextRenderer.FontFace var2 = fonts.get(var0);
            if (var2 == null) {
               return false;
            }

            int var3 = 0;

            while (var3 < var1.length()) {
               int var4 = var1.codePointAt(var3);
               var3 += Character.charCount(var4);
               if (hasGlyph(var2, var4) == 0) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   static int getWidthCeil(String var0, FontRenderer.Face var1) {
      return (int)Math.ceil(getWidth(var0, var1));
   }

   static float getWidth(String var0, FontRenderer.Face var1) {
      ExactTextRenderer.FontFace var2 = fonts.get(var1);
      float var3 = 0.0F;
      int var4 = 0;

      while (var4 < var0.length()) {
         int var5 = var0.codePointAt(var4);
         var4 += Character.charCount(var5);
         var3 += canRender(var2, var5);
      }

      return var3;
   }

   static boolean drawText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.Face var5) {
      return drawText(var0, var1, var2, var3, var4, var5, 0, 0.0F);
   }

   static boolean drawText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.Face var5, int var6, float var7) {
      if (!canRender(var5, var1)) {
         return false;
      }

      try {
         ExactTextRenderer.FontFace var8 = fonts.get(var5);
         if (var6 >>> 24 != 0 && !(var7 <= 0.0F)) {
            ExactTextRenderer.TextQuads var12 = buildQuads(var0, var1, var2, var3, var8, var7, false);
            ExactTextRenderer.TextQuads var10 = var12 == null ? null : buildQuads(var0, var1, var2, var3, var8, 0.0F, false);
            if (var12 != null && var10 != null) {
               var12.emit(var0, var6);
               var10.emit(var0, var4);
               return true;
            } else {
               return false;
            }
         } else {
            ExactTextRenderer.TextQuads var9 = buildQuads(var0, var1, var2, var3, var8, 0.0F, false);
            if (var9 == null) {
               return false;
            }

            var9.emit(var0, var4);
            return true;
         }
      } catch (RuntimeException var11) {
         disable("Exact-size ClickGUI text failed; native text until resource reload", var11);
         return false;
      }
   }

   static boolean drawGlow(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.Face var5, float var6) {
      if (var4 >>> 24 != 0 && !(var6 <= 0.0F) && canRender(var5, var1)) {
         try {
            ExactTextRenderer.TextQuads var7 = buildQuads(var0, var1, var2, var3, fonts.get(var5), var6, true);
            if (var7 == null) {
               return false;
            }

            var7.emit(var0, var4);
            return true;
         } catch (RuntimeException var8) {
            disable("Exact-size ClickGUI text failed; native text until resource reload", var8);
            return false;
         }
      } else {
         return false;
      }
   }

   private static ExactTextRenderer.TextQuads buildQuads(
      GuiGraphicsExtractor var0, String var1, int var2, int var3, ExactTextRenderer.FontFace var4, float var5, boolean var6
   ) {
      if (atlasFull) {
         resetAtlas();
      }

      Matrix3x2fStack var7 = var0.pose();
      float var8 = Minecraft.getInstance().getWindow().getGuiScale();
      int var9 = var7.m01() == 0.0F && var7.m10() == 0.0F && var7.m00() > 0.0F && Math.abs(var7.m00() - var7.m11()) < 1.0E-4F && !ScreenWarp.warpGraphics()
         ? 1
         : 0;
      float var10 = var9 != 0 ? var7.m00() : (float)Math.sqrt(Math.abs(var7.m00() * var7.m11() - var7.m01() * var7.m10()));
      float var11 = var10 * var8;
      int var12 = Math.max(64, var9 != 0 ? Math.round(var4.size * var11 * 4.0F) * 16 : Math.round(var4.size * var11) * 64);
      float var13 = var12 / 64.0F / var4.size;
      int var14 = Math.clamp(Math.round(var5 * var13 * 4.0F), 0, 63);
      if (var5 > 0.0F) {
         var14 = Math.max(1, var14);
      }

      HashMap var15 = new HashMap();
      if (var9 != 0) {
         float var16 = (var7.m00() * (var2 + var4.shiftX) + var7.m20()) * var8;
         float var17 = Math.round((var7.m11() * (var3 + 7 + var4.shiftY) + var7.m21()) * var8);
         int var18 = 0;

         while (var18 < var1.length()) {
            int var19 = var1.codePointAt(var18);
            var18 += Character.charCount(var19);
            float var20 = (float)Math.floor(var16);
            int var21 = Math.min(3, (int)((var16 - var20) * 4.0F));
            ExactTextRenderer.Glyph var22 = getWidthCeil(var4, var12, var19, var21, var14, var6);
            if (var22 == null) {
               return null;
            }

            if (!var22.empty()) {
               float var23 = (var20 + var22.left) / var8;
               float var24 = (var17 - var22.top) / var8;
               float var25 = var23 + var22.width / var8;
               float var26 = var24 + var22.height / var8;
               var15.computeIfAbsent(var22.page, var0x -> new ArrayList<>()).add(buildQuad(var22, var23, var24, var23, var26, var25, var26, var25, var24));
            }

            var16 += canRender(var4, var19) * var13;
         }
      } else {
         float var27 = var2 + var4.shiftX;
         float var28 = var3 + 7 + var4.shiftY;
         int var29 = 0;

         while (var29 < var1.length()) {
            int var30 = var1.codePointAt(var29);
            var29 += Character.charCount(var30);
            ExactTextRenderer.Glyph var31 = getWidthCeil(var4, var12, var30, 0, var14, var6);
            if (var31 == null) {
               return null;
            }

            if (!var31.empty()) {
               float var32 = var27 + var31.left / var13;
               float var33 = var28 - var31.top / var13;
               float var34 = var32 + var31.width / var13;
               float var35 = var33 + var31.height / var13;
               var15.computeIfAbsent(var31.page, var0x -> new ArrayList<>())
                  .add(
                     buildQuad(
                        var31,
                        transformX(var7, var32, var33),
                        transformY(var7, var32, var33),
                        transformX(var7, var32, var35),
                        transformY(var7, var32, var35),
                        transformX(var7, var34, var35),
                        transformY(var7, var34, var35),
                        transformX(var7, var34, var33),
                        transformY(var7, var34, var33)
                     )
                  );
            }

            var27 += canRender(var4, var30);
         }
      }

      return new ExactTextRenderer.TextQuads(var15, (boolean)var9);
   }

   private static float transformX(Matrix3x2fc var0, float var1, float var2) {
      return var0.m00() * var1 + var0.m10() * var2 + var0.m20();
   }

   private static float transformY(Matrix3x2fc var0, float var1, float var2) {
      return var0.m01() * var1 + var0.m11() * var2 + var0.m21();
   }

   private static float[] buildQuad(
      ExactTextRenderer.Glyph var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8
   ) {
      return new float[]{
         var1,
         var2,
         var3,
         var4,
         var5,
         var6,
         var7,
         var8,
         var0.atlasU / 512.0F,
         var0.atlasV / 512.0F,
         (var0.atlasU + var0.width) / 512.0F,
         (var0.atlasV + var0.height) / 512.0F
      };
   }

   private static int hasGlyph(ExactTextRenderer.FontFace var0, int var1) {
      long var2 = (long)var0.index << 32 | var1;
      Boolean var4 = glyphPresenceCache.get(var2);
      if (var4 != null) {
         return var4 ? 1 : 0;
      }

      int var5 = FreeType.FT_Get_Char_Index(var0.face, var1) != 0 ? 1 : 0;
      glyphPresenceCache.put(var2, Boolean.valueOf((boolean)var5));
      return var5 != 0 ? 1 : 0;
   }

   private static float canRender(ExactTextRenderer.FontFace var0, int var1) {
      long var2 = (long)var0.index << 32 | var1;
      Float var4 = advanceCache.get(var2);
      if (var4 != null) {
         return var4;
      }

      MemoryStack var6 = MemoryStack.stackPush();

      float var5;
      try {
         CLongBuffer var7 = var6.mallocCLong(1);
         int var8 = FreeType.FT_Get_Char_Index(var0.face, var1);
         checkResult(FreeType.FT_Get_Advance(var0.face, var8, 1, var7), "advance");
         var5 = (float)var7.get(0) * var0.size / var0.unitsPerEm;
      } catch (Throwable var10) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (var6 != null) {
         var6.close();
      }

      advanceCache.put(var2, var5);
      return var5;
   }

   private static ExactTextRenderer.Glyph getWidthCeil(ExactTextRenderer.FontFace var0, int var1, int var2, int var3, int var4, boolean var5) {
      long var6 = (long)var0.index << 60 | (var5 ? 4503599627370496L : 0L) | (long)(var1 & 1048575) << 32 | (long)var4 << 26 | (long)var3 << 24 | var2;
      ExactTextRenderer.Glyph var8 = glyphCache.get(var6);
      if (var8 != null) {
         return var8;
      }

      Integer var9 = faceSizes.get(var0.face.address());
      if (var9 == null || var9 != var1) {
         checkResult(FreeType.FT_Set_Char_Size(var0.face, 0L, var1, 72, 72), "size");
         faceSizes.put(var0.face.address(), var1);
      }

      MemoryStack var11 = MemoryStack.stackPush();

      Object var34;
      label123: {
         ExactTextRenderer.Glyph var10;
         try {
            FT_Vector var12 = FT_Vector.malloc(var11).x(var3 * 64 / 4).y(0L);
            FreeType.FT_Set_Transform(var0.face, null, var12);
            checkResult(FreeType.FT_Load_Glyph(var0.face, FreeType.FT_Get_Char_Index(var0.face, var2), 65544), "load");
            FT_GlyphSlot var13 = var0.face.glyph();
            checkResult(FreeType.FT_Render_Glyph(var13, 1), "render");
            FT_Bitmap var14 = var13.bitmap();
            int var15 = var14.width();
            int var16 = var14.rows();
            if (var15 != 0 && var16 != 0) {
               int var17 = var14.pitch();
               ByteBuffer var18 = var14.buffer(Math.abs(var17) * var16);
               int[] var19 = new int[var15 * var16];

               for (int var20 = 0; var20 < var16; var20++) {
                  for (int var21 = 0; var21 < var15; var21++) {
                     var19[var20 * var15 + var21] = var18.get(var20 * var17 + var21) & 255;
                  }
               }

               float var32 = var4 / 4.0F;
               int var33 = var4 == 0 ? 0 : (int)Math.ceil(var32) + 1;
               int var22 = var15 + var33 * 2;
               int var23 = var16 + var33 * 2;
               if (var33 > 0) {
                  var19 = dilateGlyph(var19, var15, var16, var33, var32, var5);
               }

               int[] var24 = allocateAtlasRegion(var22 + 2, var23 + 2);
               if (var24 == null) {
                  atlasFull = (boolean)1;
                  var34 = null;
                  break label123;
               }

               NativeImage var25 = new NativeImage(Format.RGBA, var22 + 2, var23 + 2, true);

               try {
                  for (int var26 = 0; var26 < var23; var26++) {
                     for (int var27 = 0; var27 < var22; var27++) {
                        var25.setPixel(var27 + 1, var26 + 1, var19[var26 * var22 + var27] << 24 | 16777215);
                     }
                  }

                  RenderSystem.getDevice().createCommandEncoder().writeToTexture(pageTextures.get(var24[0]), var25, 0, 0, var24[1], var24[2]);
               } catch (Throwable var30) {
                  try {
                     var25.close();
                  } catch (Throwable var29) {
                     var30.addSuppressed(var29);
                  }

                  throw var30;
               }

               var25.close();
               var10 = new ExactTextRenderer.Glyph(var24[0], var24[1] + 1, var24[2] + 1, var22, var23, var13.bitmap_left() - var33, var13.bitmap_top() + var33);
            } else {
               var10 = new ExactTextRenderer.Glyph(0, 0, 0, 0, 0, 0, 0);
            }
         } catch (Throwable var31) {
            if (var11 != null) {
               try {
                  var11.close();
               } catch (Throwable var28) {
                  var31.addSuppressed(var28);
               }
            }

            throw var31;
         }

         if (var11 != null) {
            var11.close();
         }

         glyphCache.put(var6, var10);
         return var10;
      }

      if (var11 != null) {
         var11.close();
      }

      return (ExactTextRenderer.Glyph)var34;
   }

   public static int[] dilateGlyph(int[] var0, int var1, int var2, int var3, float var4) {
      return dilateGlyph(var0, var1, var2, var3, var4, false);
   }

   public static int[] dilateGlyph(int[] var0, int var1, int var2, int var3, float var4, boolean var5) {
      int var6 = var1 + var3 * 2;
      int var7 = var2 + var3 * 2;
      int[] var8 = new int[var6 * var7];
      int var9 = (int)Math.ceil(var4 + 0.5F);
      float[] var10 = new float[(var9 * 2 + 1) * (var9 * 2 + 1)];

      for (int var11 = -var9; var11 <= var9; var11++) {
         for (int var12 = -var9; var12 <= var9; var12++) {
            var10[(var11 + var9) * (var9 * 2 + 1) + var12 + var9] = var5
               ? (float)Math.pow(Math.clamp(1.0 - Math.sqrt(var12 * var12 + var11 * var11) / (var4 + 0.5), 0.0, 1.0), 1.1)
               : Math.clamp(var4 + 0.5F - (float)Math.sqrt(var12 * var12 + var11 * var11), 0.0F, 1.0F);
         }
      }

      for (int var21 = 0; var21 < var7; var21++) {
         for (int var22 = 0; var22 < var6; var22++) {
            int var13 = var22 - var3;
            int var14 = var21 - var3;
            float var15 = 0.0F;

            for (int var16 = -var9; var16 <= var9 && var15 < 255.0F; var16++) {
               int var17 = var14 + var16;
               if (var17 >= 0 && var17 < var2) {
                  for (int var18 = -var9; var18 <= var9; var18++) {
                     int var19 = var13 + var18;
                     if (var19 >= 0 && var19 < var1) {
                        float var20 = var0[var17 * var1 + var19] * var10[(var16 + var9) * (var9 * 2 + 1) + var18 + var9];
                        if (var20 > var15) {
                           var15 = var20;
                        }
                     }
                  }
               }
            }

            var8[var21 * var6 + var22] = Math.round(var15);
         }
      }

      return var8;
   }

   private static int[] allocateAtlasRegion(int var0, int var1) {
      if (pageTextures.isEmpty()) {
         ensureLoaded();
      }

      if (cursorX + var0 > 512) {
         cursorY = cursorY + rowHeight;
         cursorX = 0;
         rowHeight = 0;
      }

      if (cursorY + var1 > 512) {
         if (pageTextures.size() >= 6) {
            return null;
         }

         ensureLoaded();
      }

      int[] var2 = new int[]{pageTextures.size() - 1, cursorX, cursorY};
      cursorX += var0;
      rowHeight = Math.max(rowHeight, var1);
      return var2;
   }

   private static void ensureLoaded() {
      GpuDevice var0 = RenderSystem.getDevice();
      GpuTexture var1 = var0.createTexture(() -> "Larp ClickGUI text " + pageTextures.size(), 5, GpuFormat.RGBA8_UNORM, 512, 512, 1, 1);
      pageTextures.add(var1);
      pageViews.add(var0.createTextureView(var1));
      rowHeight = 0;
      cursorY = 0;
      cursorX = 0;
      LarpLog.debug("Font", "Exact-size ClickGUI text page {} opened", pageTextures.size());
   }

   private static void resetAtlas() {
      atlasFull = (boolean)0;
      glyphCache.clear();

      for (GpuTextureView var1 : pageViews) {
         var1.close();
      }

      for (GpuTexture var3 : pageTextures) {
         var3.close();
      }

      pageViews.clear();
      pageTextures.clear();
      rowHeight = 0;
      cursorY = 0;
      cursorX = 0;
      LarpLog.debug("Font", "Exact-size ClickGUI text pages full; glyphs will be rasterized again");
   }

   private static boolean ensureLoaded() {
      if (loadedGeneration != PostProcessing.reloadCount) {
         release();
         loadedGeneration = PostProcessing.reloadCount;
         disabled = (boolean)0;

         try {
            loadFonts();
         } catch (RuntimeException | IOException var1) {
            disable("Exact-size ClickGUI text unavailable; native text until resource reload", var1);
         }
      }

      return !disabled && !fonts.isEmpty();
   }

   private static void loadFonts() throws IOException {
      ResourceManager var0 = Minecraft.getInstance().getResourceManager();
      MemoryStack var1 = MemoryStack.stackPush();

      try {
         PointerBuffer var2 = var1.mallocPointer(1);
         checkResult(FreeType.FT_Init_FreeType(var2), "init");
         freeTypeLibrary = var2.get(0);
      } catch (Throwable var23) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var19) {
               var23.addSuppressed(var19);
            }
         }

         throw var23;
      }

      if (var1 != null) {
         var1.close();
      }

      HashMap var24 = new HashMap();

      for (FontRenderer.Face var5 : FontRenderer.Face.values()) {
         if (var5.name().startsWith("CLICK_")) {
            String var6 = var5.name().toLowerCase(Locale.ROOT);
            Resource var7 = var0.getResourceOrThrow(Identifier.fromNamespaceAndPath("larp-client", "font/" + var6 + ".json"));
            InputStreamReader var9 = new InputStreamReader(var7.open(), StandardCharsets.UTF_8);

            JsonObject var8;
            try {
               var8 = JsonParser.parseReader(var9).getAsJsonObject().getAsJsonArray("providers").get(0).getAsJsonObject();
            } catch (Throwable var20) {
               try {
                  var9.close();
               } catch (Throwable var18) {
                  var20.addSuppressed(var18);
               }

               throw var20;
            }

            var9.close();
            Identifier var26 = Identifier.parse(var8.get("file").getAsString());
            FT_Face var10 = (FT_Face)var24.get(var26.toString());
            if (var10 == null) {
               byte[] var11;
               try (InputStream var12 = var0.getResourceOrThrow(var26.withPrefix("font/")).open()) {
                  var11 = var12.readAllBytes();
               }

               ByteBuffer var28 = MemoryUtil.memAlloc(var11.length).put(var11).flip();
               fontBuffers.add(var28);
               MemoryStack var13 = MemoryStack.stackPush();

               try {
                  PointerBuffer var14 = var13.mallocPointer(1);
                  checkResult(FreeType.FT_New_Memory_Face(freeTypeLibrary, var28, 0L, var14), "face " + var26);
                  var10 = FT_Face.create(var14.get(0));
               } catch (Throwable var21) {
                  if (var13 != null) {
                     try {
                        var13.close();
                     } catch (Throwable var16) {
                        var21.addSuppressed(var16);
                     }
                  }

                  throw var21;
               }

               if (var13 != null) {
                  var13.close();
               }

               var24.put(var26.toString(), var10);
            }

            JsonArray var27 = var8.getAsJsonArray("shift");
            int var29 = fonts.size();
            fonts.put(
               var5,
               new ExactTextRenderer.FontFace(
                  var29,
                  var10,
                  var8.get("size").getAsFloat(),
                  var27 == null ? 0.0F : var27.get(0).getAsFloat(),
                  var27 == null ? 0.0F : var27.get(1).getAsFloat(),
                  var10.units_per_EM()
               )
            );
         }
      }

      LarpLog.info(
         "Font", "Exact-size ClickGUI text ready: {} faces from {} files, rasterized by FreeType at the on-screen pixel size", fonts.size(), var24.size()
      );
   }

   private static void checkResult(int var0, String var1) {
      if (var0 != 0) {
         throw new IllegalStateException("FreeType " + var1 + " failed with error " + var0);
      }
   }

   private static void disable(String var0, Exception var1) {
      disabled = (boolean)1;
      LarpLog.error("Font", var0, var1);
      release();
   }

   public static void release() {
      for (GpuTextureView var1 : pageViews) {
         var1.close();
      }

      for (GpuTexture var5 : pageTextures) {
         var5.close();
      }

      pageViews.clear();
      pageTextures.clear();
      glyphCache.clear();
      advanceCache.clear();
      glyphPresenceCache.clear();
      faceSizes.clear();
      rowHeight = 0;
      cursorY = 0;
      cursorX = 0;
      HashSet var4 = new HashSet();

      for (ExactTextRenderer.FontFace var2 : fonts.values()) {
         if (var4.add(var2.face.address())) {
            FreeType.FT_Done_Face(var2.face);
         }
      }

      fonts.clear();
      if (freeTypeLibrary != 0L) {
         FreeType.FT_Done_FreeType(freeTypeLibrary);
         freeTypeLibrary = 0L;
      }

      for (ByteBuffer var8 : fontBuffers) {
         MemoryUtil.memFree(var8);
      }

      fontBuffers.clear();
   }

   private record FontFace(int index, FT_Face face, float size, float shiftX, float shiftY, int unitsPerEm) {
   }

   private record Glyph(int page, int u, int v, int width, int height, int left, int top) {
      private final int atlasU;
      private final int atlasV;

      private Glyph(int page, int u, int v, int width, int height, int left, int top) {
         this.page = page;
         this.atlasU = u;
         this.atlasV = v;
         this.width = width;
         this.height = height;
         this.left = left;
         this.top = top;
      }

      boolean empty() {
         return this.width == 0 || this.height == 0;
      }
   }

   private record TextQuadRenderState(TextureSetup textureSetup, List<float[]> quads, int color, ScreenRectangle scissorArea, ScreenRectangle bounds)
      implements GuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI_TEXTURED;
      }

      public void buildVertices(VertexConsumer var1) {
         for (float[] var3 : this.quads) {
            var1.addVertex(var3[0], var3[1], 0.0F).setUv(var3[8], var3[9]).setColor(this.color);
            var1.addVertex(var3[2], var3[3], 0.0F).setUv(var3[8], var3[11]).setColor(this.color);
            var1.addVertex(var3[4], var3[5], 0.0F).setUv(var3[10], var3[11]).setColor(this.color);
            var1.addVertex(var3[6], var3[7], 0.0F).setUv(var3[10], var3[9]).setColor(this.color);
         }
      }
   }

   private record TextQuads(Map<Integer, List<float[]>> quads, boolean upright) {
      void emit(GuiGraphicsExtractor var1, int var2) {
         GpuSampler var3 = RenderSystem.getSamplerCache().getClampToEdge(this.upright ? FilterMode.NEAREST : FilterMode.LINEAR);

         for (Entry var5 : this.quads.entrySet()) {
            float var6 = Float.MAX_VALUE;
            float var7 = Float.MAX_VALUE;
            float var8 = -Float.MAX_VALUE;
            float var9 = -Float.MAX_VALUE;

            for (float[] var11 : (List)var5.getValue()) {
               for (int var12 = 0; var12 < 4; var12++) {
                  var6 = Math.min(var6, var11[var12 * 2]);
                  var8 = Math.max(var8, var11[var12 * 2]);
                  var7 = Math.min(var7, var11[var12 * 2 + 1]);
                  var9 = Math.max(var9, var11[var12 * 2 + 1]);
               }
            }

            int var14 = (int)Math.floor(var6);
            int var15 = (int)Math.floor(var7);
            ScreenRectangle var16 = new ScreenRectangle(var14, var15, (int)Math.ceil(var8) - var14 + 1, (int)Math.ceil(var9) - var15 + 1);
            ScreenRectangle var13 = var1.scissorStack.peek();
            var1.guiRenderState
               .addGuiElement(
                  new ExactTextRenderer.TextQuadRenderState(
                     TextureSetup.singleTexture(ExactTextRenderer.pageViews.get((Integer)var5.getKey()), var3),
                     List.copyOf((Collection<? extends float[]>)var5.getValue()),
                     var2,
                     var13,
                     var13 == null ? var16 : var13.intersection(var16)
                  )
               );
         }
      }
   }
}
