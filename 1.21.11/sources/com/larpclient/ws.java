package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class ws {
   private static boolean w;
   private static boolean h;
   private static boolean A;
   private static boolean k;
   private static boolean l;
   private static boolean n;
   public static final int e = 2;
   private static final VertexFormat U = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("UV0", VertexFormatElement.UV0)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV1", VertexFormatElement.UV1)
      .build();
   private static final VertexFormat t = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV0", VertexFormatElement.UV0)
      .add("UV2", VertexFormatElement.UV2)
      .add("UV1", VertexFormatElement.UV1)
      .build();
   private static final VertexFormat H = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("UV0", VertexFormatElement.UV0)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV1", VertexFormatElement.UV1)
      .add("UV2", VertexFormatElement.UV2)
      .build();
   private static boolean Z;
   private static boolean a;
   private static boolean D;
   public static final float p = 64.0F;
   private static boolean c;
   private static boolean V;
   private static boolean S;
   private static boolean Y;
   private static boolean L;

   private ws() {
   }

   public static boolean L(RenderPipeline var0) {
      return var0 == ws.t.C || var0 == ws.t.b || var0 == ws.t.M || var0 == ws.t.W || var0 == ws.t.l || var0 == ws.t.o;
   }

   public static RenderPipeline i(RenderPipeline var0) {
      if (L(var0) && var0 != ws.t.o) {
         c = (boolean)1;
         if (var0 == ws.t.l) {
            return Y ? ws.t.F : null;
         } else if (!V) {
            return null;
         } else {
            return var0 == ws.t.C ? ws.t.Y : (var0 == ws.t.b ? ws.t.c : (var0 == ws.t.M ? ws.t.y : ws.t.k));
         }
      } else {
         return null;
      }
   }

   public static void n() {
      if (w && !h && !A) {
         try {
            f2.a(ws.t.C);
            f2.a(ws.t.b);
            f2.a(ws.t.M);
            f2.a(ws.t.W);
            h = (boolean)1;
            q8.G("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            A = (boolean)1;
            q8.u("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (k && h && !l && !n) {
         try {
            f2.a(ws.t.l);
            l = (boolean)1;
            q8.G("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            n = (boolean)1;
            q8.u("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (Z && h && !a && !D) {
         try {
            f2.a(ws.t.o);
            a = (boolean)1;
            q8.G("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            D = (boolean)1;
            q8.u("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (c && h && !V && !S) {
         try {
            f2.a(ws.t.Y);
            f2.a(ws.t.c);
            f2.a(ws.t.y);
            f2.a(ws.t.k);
            V = (boolean)1;
            q8.G("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            S = (boolean)1;
            q8.u("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (c && V && l && !Y && !L) {
         try {
            f2.a(ws.t.F);
            Y = (boolean)1;
         } catch (RuntimeException var1) {
            L = (boolean)1;
            q8.u("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void h() {
      D = (boolean)0;
      a = (boolean)0;
      n = (boolean)0;
      l = (boolean)0;
      A = (boolean)0;
      h = (boolean)0;
      L = (boolean)0;
      Y = (boolean)0;
      S = (boolean)0;
      V = (boolean)0;
   }

   public static void r(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
            gu.Z(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            w = (boolean)1;
            if (!h) {
               com.larpclient.p.S(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = fp.R(var6);
               if (var8 >>> 24 != 0) {
                  var0.field_59826
                     .method_70919(new ws.l(var0.method_51448(), var1, var2, var3, var4, N(var7, var3, var4), var8, var0.field_44659.method_70863()));
               }
            }
         }
      }
   }

   private static float N(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void F(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            r(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (int)var1 && var2 == (int)var2 && var3 == (int)var3 && var4 == (int)var4) {
               gu.R(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = N(var8, var3, var4);
               w = (boolean)1;
               if (!h) {
                  com.larpclient.p.S(
                     var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), ei.W(var6, var7, 0.5)
                  );
               } else {
                  var6 = fp.R(var6);
                  var7 = fp.R(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.field_59826.method_70919(new ws.l(var0.method_51448(), var1, var2, var3, var4, var8, var6, var7, var0.field_44659.method_70863()));
                  }
               }
            }
         }
      }
   }

   public static void u(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            F(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static void A(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         w = (boolean)1;
         if (!h) {
            com.larpclient.p.t(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = fp.R(var6);
            var7 = fp.R(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.field_59826.method_70919(new ws.l(var0.method_51448(), var1, var2, var3, var4, var8, var6, var7, true, var0.field_44659.method_70863()));
            }
         }
      }
   }

   public static void X(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         w = (boolean)1;
         if (!h) {
            com.larpclient.p.t(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = fp.R(var6);
            if (var6 >>> 24 != 0) {
               var0.field_59826
                  .method_70919(
                     new ws.l(
                        new Matrix3x2f(var0.method_51448()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var7,
                        var6,
                        var6,
                        true,
                        true,
                        var0.field_44659.method_70863(),
                        ws.l.bounds(var0.method_51448(), var1, var2, var3, var4, var0.field_44659.method_70863())
                     )
                  );
            }
         }
      }
   }

   public static void K(class_332 var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         w = (boolean)1;
         if (h) {
            var7 = fp.R(var7);
            var8 = fp.R(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.field_59826
                  .method_70919(
                     new ws.l(
                        new Matrix3x2f(var0.method_51448()),
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
                        var0.field_44659.method_70863(),
                        ws.l.bounds(var0.method_51448(), var1, var2, var3, var4, var10, var0.field_44659.method_70863())
                     )
                  );
            }
         }
      }
   }

   public static void X(class_332 var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         w = (boolean)1;
         k = (boolean)1;
         if (l) {
            var9 = fp.R(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.field_59826
                  .method_70919(
                     new ws.l(
                        new Matrix3x2f(var0.method_51448()),
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
                        var0.field_44659.method_70863(),
                        ws.l.bounds(var0.method_51448(), var1, var2, var3, var4, var11, var0.field_44659.method_70863())
                     )
                  );
            }
         }
      }
   }

   public static void u(class_332 var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = N(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         w = (boolean)1;
         Z = (boolean)1;
         if (a) {
            var9 = fp.R(var9);
            if (var9 >>> 24 != 0) {
               var0.field_59826
                  .method_70919(
                     new ws.o(
                        new Matrix3x2f(var0.method_51448()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var10,
                        (var6 - var1 - var3 / 2.0F) / var10,
                        (var7 - var2 - var4 / 2.0F) / var10,
                        var8 / var10,
                        var9,
                        var0.field_44659.method_70863(),
                        ws.l.bounds(var0.method_51448(), var1, var2, var3, var4, var0.field_44659.method_70863())
                     )
                  );
            }
         }
      }
   }

   public record l(
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
      class_8030 scissorArea,
      class_8030 bounds
   ) implements class_11244 {
      public l(
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
         class_8030 var12,
         class_8030 var13
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, -1.0F, var12, var13);
      }

      public l(
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
         class_8030 var11,
         class_8030 var12
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 1.0F, var11, var12);
      }

      public l(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, class_8030 var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public l(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, class_8030 var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public l(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, class_8030 var10) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, var8, var9, false, var10, bounds(var1, var2, var3, var4, var5, var10));
      }

      static class_8030 bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, class_8030 var5) {
         return bounds(var0, var1, var2, var3, var4, 1.0F, var5);
      }

      static class_8030 bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, float var5, class_8030 var6) {
         int var7 = (int)Math.floor(var1 - var5);
         int var8 = (int)Math.floor(var2 - var5);
         int var9 = (int)Math.ceil(var1 + var3 + var5);
         int var10 = (int)Math.ceil(var2 + var4 + var5);
         class_8030 var11 = new class_8030(var7, var8, var9 - var7, var10 - var8).method_71523(var0);
         return var6 == null ? var11 : var6.method_49701(var11);
      }

      public boolean glow() {
         return this.padding > 1.0F;
      }

      public boolean contourLight() {
         return this.sweep >= 0.0F;
      }

      public RenderPipeline comp_4055() {
         return this.contourLight() ? ws.t.l : (this.glow() ? ws.t.W : (this.ring ? ws.t.M : (this.rim ? ws.t.b : ws.t.C)));
      }

      public class_11231 comp_4056() {
         return class_11231.method_70899();
      }

      public void method_70917(class_4588 var1) {
         float var2 = this.padding;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.x - var2, this.y - var2, -var3, -var4, this.color);
         this.vertex(var1, this.x - var2, this.y + this.height + var2, -var3, var4, this.bottomColor);
         this.vertex(var1, this.x + this.width + var2, this.y + this.height + var2, var3, var4, this.bottomColor);
         this.vertex(var1, this.x + this.width + var2, this.y - var2, var3, -var4, this.color);
      }

      private void vertex(class_4588 var1, float var2, float var3, float var4, float var5, int var6) {
         class_4588 var7 = var1.method_22912(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               this.padding / this.radius
            )
            .method_22913(var4, var5)
            .method_39415(var6);
         if (this.contourLight()) {
            var7.method_22921(Math.round(this.sweep * 32768.0F) & 32767, this.glow() ? 1 : 0);
         }
      }

      public class_8030 comp_4069() {
         return this.scissorArea;
      }

      public class_8030 comp_4274() {
         return this.bounds;
      }
   }

   public record o(
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
      class_8030 scissorArea,
      class_8030 bounds
   ) implements class_11244 {
      public RenderPipeline comp_4055() {
         return ws.t.o;
      }

      public class_11231 comp_4056() {
         return class_11231.method_70899();
      }

      public void method_70917(class_4588 var1) {
         float var2 = 1.0F;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(var1, this.x - var2, this.y - var2, -var3, -var4);
         this.vertex(var1, this.x - var2, this.y + this.height + var2, -var3, var4);
         this.vertex(var1, this.x + this.width + var2, this.y + this.height + var2, var3, var4);
         this.vertex(var1, this.x + this.width + var2, this.y - var2, var3, -var4);
      }

      private void vertex(class_4588 var1, float var2, float var3, float var4, float var5) {
         var1.method_22912(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               1.0F / this.radius
            )
            .method_22913(var4, var5)
            .method_39415(this.color)
            .method_60796(fixed(this.circleX), fixed(this.circleY))
            .method_22921(fixed(this.circle), 0);
      }

      private static int fixed(float var0) {
         return Math.clamp(Math.round(var0 * 64.0F), -32768, 32767);
      }

      public class_8030 comp_4069() {
         return this.scissorArea;
      }

      public class_8030 comp_4274() {
         return this.bounds;
      }
   }

   private static final class t {
      static final RenderPipeline C = d(0, false);
      static final RenderPipeline b = d(1, false);
      static final RenderPipeline M = d(2, false);
      static final RenderPipeline W = d(-1, false);
      static final RenderPipeline l = N(false);
      static final RenderPipeline o = RenderPipeline.builder(new Snippet[]{class_10799.field_56863})
         .withLocation(class_2960.method_60655("larp-client", "gui_rounded_ripple"))
         .withVertexShader(class_2960.method_60655("larp-client", "gui/rounded"))
         .withFragmentShader(class_2960.method_60655("larp-client", "gui/rounded"))
         .withVertexFormat(ws.H, class_5596.field_27382)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline Y = d(0, true);
      static final RenderPipeline c = d(1, true);
      static final RenderPipeline y = d(2, true);
      static final RenderPipeline k = d(-1, true);
      static final RenderPipeline F = N(true);

      private static RenderPipeline N(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{class_10799.field_56863})
            .withLocation(class_2960.method_60655("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(class_2960.method_60655("larp-client", "gui/rounded"))
            .withFragmentShader(class_2960.method_60655("larp-client", "gui/rounded"))
            .withVertexFormat(var0 ? ws.t : class_290.field_20888, class_5596.field_27382)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline d(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{class_10799.field_56863})
            .withLocation(class_2960.method_60655("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(class_2960.method_60655("larp-client", "gui/rounded"))
            .withFragmentShader(class_2960.method_60655("larp-client", "gui/rounded"))
            .withVertexFormat(var1 ? ws.U : class_290.field_1575, class_5596.field_27382);
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
