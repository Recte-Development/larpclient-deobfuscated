package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1044;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class q2 {
   public static final class_2960 r = n("textures/gui/hud_sdf.png");
   public static final class_2960 v = n("textures/gui/hud_sdf.json");
   public static final float W = 0.72727275F;
   public static final float K = 0.729F;
   public static final float Z = 0.5625F;
   private static final int M = 8704;
   private static boolean l;
   private static boolean m;
   private static boolean S;
   private static q2.y J;
   private static final int j = 512;
   private static final float[] w = new float[0];
   private static final Float2ObjectOpenHashMap<HashMap<String, float[]>> P = new Float2ObjectOpenHashMap();
   private static int E;

   private q2() {
   }

   private static class_2960 n(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public static void d() {
      if (l && !m && !S) {
         try {
            f2.a(q2.x.J);
            if (class_310.method_1551().method_1478().method_14486(r).isEmpty()) {
               throw new IllegalStateException("Missing " + r);
            }

            try (InputStreamReader var0 = new InputStreamReader(class_310.method_1551().method_1478().open(v), StandardCharsets.UTF_8)) {
               J = q2.y.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            m = (boolean)1;
            uw.W();
            q8.G("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var5) {
            S = (boolean)1;
            J = null;
            q8.u("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var5);
         }
      }
   }

   public static void L() {
      boolean var0 = m;
      S = (boolean)0;
      m = (boolean)0;
      J = null;
      P.clear();
      E = 0;
      if (var0) {
         uw.W();
      }
   }

   private static q2.y a() {
      l = (boolean)1;
      return m && ib.o() == uw.r.V ? J : null;
   }

   public static double e(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.36277276F * var4;
   }

   public static double g(double var0, double var2, double var4) {
      return var0 + var2 / 2.0 - 0.44602275F * var4;
   }

   public static float U(String var0, double var1) {
      q2.y var3 = a();
      if (var3 == null) {
         return ib.x(var0, var1);
      }

      float var4 = 0.0F;
      int var5 = -1;
      int var6 = 0;

      while (var6 < var0.length()) {
         int var7 = var0.codePointAt(var6);
         q2.h var8 = var3.glyph(var7);
         if (var8 == null) {
            if (var5 < 0) {
               var5 = var6;
            }
         } else {
            if (var5 >= 0) {
               var4 += f(var0.substring(var5, var6)) * (float)(var1 / 11.0);
               var5 = -1;
            }

            var4 += var8.advance() * (float)var1;
         }

         var6 += Character.charCount(var7);
      }

      if (var5 >= 0) {
         var4 += f(var0.substring(var5)) * (float)(var1 / 11.0);
      }

      return var4;
   }

   private static float f(String var0) {
      return uw.z(var0, uw.r.V);
   }

   public static String K(String var0, double var1, double var3) {
      if (U(var0, var3) <= var1) {
         return var0;
      }

      int var5 = var0.length();

      while (var5 > 0 && U(var0.substring(0, var5) + "…", var3) > var1) {
         var5 = var0.offsetByCodePoints(var5, -1);
      }

      return var5 == 0 ? "" : var0.substring(0, var5) + "…";
   }

   public static void n(class_332 var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = fp.R(var8);
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         q2.y var9 = a();
         if (var9 == null) {
            r(var0, var1, var2, var4, var6, var8);
         } else {
            float[] var10 = p(var9, var1, (float)var6);
            if (var10 != null) {
               P(var0, var10, (float)var2, (float)var4, var8);
            } else {
               int var11 = var8;
               float[] var12 = new float[var1.length() * 16];
               int var13 = f(var9, var1, (float)var2, (float)var4, (float)var6, var12, (var6x, var7) -> {
                  r(var0, var6x, var7, var4, var6, var11);
                  return f(var6x) * (float)(var6 / 11.0);
               });
               K(var0, var12, var13, var8);
            }
         }
      }
   }

   static float[] p(q2.y var0, String var1, float var2) {
      HashMap var3 = (HashMap)P.get(var2);
      if (var3 == null) {
         P.put(var2, var3 = new HashMap());
      }

      float[] var4 = (float[])var3.get(var1);
      if (var4 == null) {
         if (E >= 512) {
            P.clear();
            E = 0;
            P.put(var2, var3 = new HashMap());
         }

         float[] var5 = new float[var1.length() * 16];
         boolean[] var6 = new boolean[1];
         int var7 = f(var0, var1, 0.0F, 0.0F, var2, var5, (var1x, var2x) -> {
            var6[0] = (boolean)1;
            return 0.0F;
         });
         var4 = var6[0] ? w : Arrays.copyOf(var5, var7 * 16);
         var3.put(var1, var4);
         E++;
      }

      return var4 == w ? null : var4;
   }

   public static int f(q2.y var0, String var1, float var2, float var3, float var4, float[] var5, q2.v var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.72727275F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         q2.h var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.d(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = f(var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13);
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.d(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void r(class_332 var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / ib.g());
      var0.method_51448().pushMatrix();
      var0.method_51448().translate((float)var2, (float)var4).scale(var9, var9);
      uw.D(var0, var1, 0, 0, var8, ib.o());
      var0.method_51448().popMatrix();
   }

   public static boolean w(class_332 var0, String var1, double var2, double var4, double var6, int var8) {
      q2.y var9 = a();
      q2.h var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      }

      var8 = fp.R(var8);
      if (var8 >>> 24 < 4) {
         return true;
      }

      float[] var11 = new float[16];
      K(var0, var11, t(var10, (float)var2, (float)var4, (float)var6, var11), var8);
      return true;
   }

   public static int t(q2.h var0, float var1, float var2, float var3, float[] var4) {
      return f(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int f(float[] var0, int var1, float var2, float var3, float var4, float var5, q2.h var6) {
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

   private static void K(class_332 var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         P(var0, Arrays.copyOf(var1, var2 * 16), 0.0F, 0.0F, var3);
      }
   }

   private static void P(class_332 var0, float[] var1, float var2, float var3, int var4) {
      if (var1.length != 0) {
         class_1044 var5 = class_310.method_1551().method_1531().method_4619(r);
         class_11231 var6 = class_11231.method_70900(var5.method_71659(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
         var0.field_59826.method_70919(new q2.e(var0.method_51448(), var1, var2, var3, var4, var6, var0.field_44659.method_70863()));
      }
   }

   public record e(
      Matrix3x2fc pose, float[] vertices, float offsetX, float offsetY, int color, class_11231 textureSetup, class_8030 scissorArea, class_8030 bounds
   ) implements class_11244 {
      public e(Matrix3x2fc var1, float[] var2, float var3, float var4, int var5, class_11231 var6, class_8030 var7) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, bounds(var1, var2, var3, var4, var7));
      }

      public RenderPipeline comp_4055() {
         return q2.x.J;
      }

      public void method_70917(class_4588 var1) {
         for (byte var2 = 0; var2 < this.vertices.length; var2 += 4) {
            float var3 = this.vertices[var2] + this.offsetX;
            float var4 = this.vertices[var2 + 1] + this.offsetY;
            var1.method_22912(
                  this.pose.m00() * var3 + this.pose.m10() * var4 + this.pose.m20(), this.pose.m01() * var3 + this.pose.m11() * var4 + this.pose.m21(), 0.0F
               )
               .method_22913(this.vertices[var2 + 2], this.vertices[var2 + 3])
               .method_39415(this.color);
         }
      }

      static class_8030 bounds(Matrix3x2fc var0, float[] var1, float var2, float var3, class_8030 var4) {
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
         class_8030 var11 = new class_8030(var12, var10, (int)Math.ceil(var7) - var12, (int)Math.ceil(var8) - var10).method_71523(var0);
         return var4 == null ? var11 : var4.method_49701(var11);
      }

      public class_11231 comp_4056() {
         return this.textureSetup;
      }

      public class_8030 comp_4069() {
         return this.scissorArea;
      }

      public class_8030 comp_4274() {
         return this.bounds;
      }
   }

   public record h(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      boolean visible() {
         return this.u1 > this.u0;
      }
   }

   public interface v {
      float d(String var1, float var2);
   }

   private static final class x {
      static final RenderPipeline J = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(q2.n("gui_sdf"))
         .withFragmentShader(q2.n("gui/sdf"))
         .build();
   }

   public record y(q2.h[] direct, Map<Integer, q2.h> extended, Map<String, q2.h> icons) {
      public q2.h glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static q2.y parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         q2.h[] var4 = new q2.h[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            q2.h var8 = glyph(var7.getAsJsonObject(), var2, var3);
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

         return new q2.y(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static q2.h glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new q2.h(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         JsonArray var4 = var0.getAsJsonArray("plane");
         JsonArray var5 = var0.getAsJsonArray("atlas");
         float var6 = var5.get(0).getAsFloat();
         float var7 = var5.get(1).getAsFloat();
         return new q2.h(
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
