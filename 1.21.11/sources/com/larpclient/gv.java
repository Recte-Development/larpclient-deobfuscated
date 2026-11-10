package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_10017;
import net.minecraft.class_1058;
import net.minecraft.class_10799;
import net.minecraft.class_11659;
import net.minecraft.class_11785;
import net.minecraft.class_12075;
import net.minecraft.class_12247;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3879;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_777;
import net.minecraft.class_811;
import net.minecraft.class_9950;
import net.minecraft.class_11659.class_11660;
import net.minecraft.class_12247.class_12338;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;

public final class gv implements InvocationHandler {
   private static final Map<gv.c, class_1921> E = new LinkedHashMap<>();
   private static final Map<gv.z, RenderPipeline> j = new HashMap<>();
   private static final gv v = new gv();
   private final class_11659 y = (class_11659)Proxy.newProxyInstance(class_11659.class.getClassLoader(), new Class[]{class_11659.class}, this);
   private final class_4587 g = new class_4587();
   private final class_12075 S = new class_12075();
   private cw W;
   private gv.y I;
   private int t;
   private double f;
   private double n;
   private double X;
   private boolean x;
   private boolean J;
   private boolean T;
   private SelfVisualsModule a;
   private int Y = -1;
   private boolean b;
   private boolean c;
   private boolean Q;
   private boolean l;
   private class_4665 R;
   private boolean Z;
   private boolean i;
   private boolean s;
   private int z;
   private int H;
   private float A;
   private float r;
   private float M;
   private float u;
   private float k;
   private float N;
   private float q;
   private float e;
   private float m;
   private float C;
   private final Matrix4f V = new Matrix4f();
   private static final class_2960 P = class_2960.method_60655("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<gv.y> G = EnumSet.range(gv.y.p, gv.y.x);

   public static void S(int var0, int var1) {
      gv var2 = v;
      var2.z = var0;
      var2.H = var1;
      var2.A = var2.r = Float.POSITIVE_INFINITY;
      var2.M = var2.u = Float.NEGATIVE_INFINITY;
      var2.i = (boolean)(!sm.J(var2.V) ? 1 : 0);
      if (var2.i) {
         if (!var2.s) {
            var2.s = (boolean)1;
            q8.L("Render", "World projection not captured; model post passes cover the whole screen");
         }
      } else {
         var2.V.mul(RenderSystem.getModelViewMatrix());
         var2.Z = (boolean)1;
         var2.L();
      }
   }

   public static int[] E() {
      gv var0 = v;
      var0.Z = (boolean)0;
      if (var0.i) {
         return null;
      }

      if (var0.A > var0.M) {
         return new int[4];
      }

      int var1 = (int)Math.floor(var0.A);
      int var2 = (int)Math.floor(var0.r);
      return new int[]{var1, var2, (int)Math.ceil(var0.M) - var1, (int)Math.ceil(var0.u) - var2};
   }

   private void L() {
      this.k = this.N = this.q = Float.POSITIVE_INFINITY;
      this.e = this.m = this.C = Float.NEGATIVE_INFINITY;
   }

   private void y() {
      if (!(this.k > this.e) && !this.i) {
         if (this.k <= 0.0F && this.e >= 0.0F && this.N <= 0.0F && this.m >= 0.0F && this.q <= 0.0F && this.C >= 0.0F) {
            this.i = (boolean)1;
         } else {
            byte var1 = 0;
            eg.f var2 = eg.A(new class_238(this.k, this.N, this.q, this.e, this.m, this.C), class_243.field_1353, this.V, this.z, this.H, (boolean)var1);
            this.L();
            if (var2 != null) {
               this.A = Math.min(this.A, var2.left());
               this.M = Math.max(this.M, var2.right());
               this.r = Math.min(this.r, var2.top());
               this.u = Math.max(this.u, var2.bottom());
            }
         }
      }
   }

   public static class_10017 i(class_1297 var0, float var1) {
      return class_310.method_1551().method_1561().method_72977(var0, var1);
   }

   public static boolean a() {
      return v.W != null || v.b;
   }

   public static Matrix4f Q(class_10017 var0, class_4587 var1) {
      class_4665 var2 = i(var0, var1, false);
      if (var2 == null && !v.Q) {
         v.Q = (boolean)1;
         q8.L(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.method_23761());
   }

   public static class_4665 s(class_10017 var0, class_4587 var1) {
      class_4665 var2 = i(var0, var1, true);
      if (var2 == null && !v.l) {
         v.l = (boolean)1;
         q8.L(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static class_4665 i(class_10017 var0, class_4587 var1, boolean var2) {
      gv var3 = v;
      class_310 var4 = class_310.method_1551();
      class_243 var5 = var4.field_1773.method_19418().method_71156();
      var3.S.field_63078 = var5;
      var3.S.field_63081.set(var4.field_1773.method_19418().method_23767());
      var3.g.method_23760().method_66521(var1.method_23760());
      var3.b = (boolean)1;
      var3.c = var2;
      var3.R = null;

      try {
         var4.method_1561()
            .method_72976(
               var0, var3.S, var0.field_53325 - var5.field_1352, var0.field_53326 - var5.field_1351, var0.field_53327 - var5.field_1350, var3.g, var3.y
            );
         return var3.R;
      } finally {
         var3.b = (boolean)0;
         var3.R = null;

         while (!var3.g.method_67795()) {
            var3.g.method_22909();
         }
      }
   }

   public static void A() {
      v.Q = (boolean)0;
      v.l = (boolean)0;
      E.clear();
      j.clear();
   }

   public static void A(class_630 var0, class_4587 var1, cw var2, class_2960 var3, int var4, double var5, double var7, boolean var9) {
      if (v.Y != t_.g) {
         A();
         v.Y = t_.g;
      }

      gv.y var10 = var7 > 0.0 ? (var9 ? gv.y.n : gv.y.A) : gv.y.K;
      if (!var9 || !(var7 <= 0.0)) {
         gv.n var11 = new gv.n(var2.N(k(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null);
         var11.J = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.method_22698(var1, var11, 15728880, class_4608.field_21444);
      }
   }

   public static void x(class_10017 var0, class_4587 var1, cw var2, gv.y var3, int var4, double var5, boolean var7) {
      o(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void o(class_10017 var0, class_4587 var1, cw var2, gv.y var3, int var4, double var5, boolean var7, boolean var8) {
      v.D(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void D(class_10017 var1, class_4587 var2, cw var3, gv.y var4, int var5, double var6, boolean var8, boolean var9) {
      if (this.Y != t_.g) {
         A();
         this.Y = t_.g;
      }

      this.W = var3;
      this.I = var4;
      this.t = var5;
      this.f = var6;
      this.x = var8;
      this.T = var9;
      this.J = (boolean)(var4 == gv.y.s ? 1 : 0);
      this.a = var8 ? SelfVisualsModule.o() : null;
      class_310 var10 = class_310.method_1551();
      class_243 var11 = var10.field_1773.method_19418().method_71156();
      this.n = var1.field_53326 - var11.field_1351;
      this.X = var1.field_53325 - var11.field_1352;
      this.S.field_63078 = var11;
      this.S.field_63081.set(var10.field_1773.method_19418().method_23767());
      this.g.method_23760().method_66521(var2.method_23760());

      try {
         var10.method_1561()
            .method_72976(
               var1, this.S, var1.field_53325 - var11.field_1352, var1.field_53326 - var11.field_1351, var1.field_53327 - var11.field_1350, this.g, this.y
            );
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.field_58171, var16);
      } finally {
         while (!this.g.method_67795()) {
            this.g.method_22909();
         }

         this.W = null;
         this.a = null;
         if (this.Z) {
            this.y();
         }
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      }

      String var4 = M(var2);
      if (this.b && var2.getDeclaringClass() != Object.class && !var4.equals("submitModel") && !var4.equals("order")) {
         return null;
      }

      switch (var4) {
         case "order":
            return this.y;
         case "submitModel":
            class_3879 var18 = (class_3879)var3[0];
            Object var20 = var3[1];
            class_4587 var21 = (class_4587)var3[2];
            if (this.b) {
               if (this.R == null && var18 instanceof class_591 var23) {
                  var18.method_2819(var20);
                  var21.method_22903();

                  try {
                     (this.c ? var23.field_3391 : var23.field_3398).method_22703(var21);
                     this.R = var21.method_23760().method_56822();
                  } finally {
                     var21.method_22909();
                  }
               }

               return null;
            } else {
               class_1921 var22 = (class_1921)var3[3];
               boolean var24 = var18 instanceof class_591;
               if (this.x && var18 instanceof class_9950) {
                  if (b(this.I) || this.a == null || !this.a.W()) {
                     return null;
                  }
               } else if (this.x && !this.J && !var24 && (this.a == null || (b(this.I) ? !c1.d() : !this.a.I.getValue()))) {
                  return null;
               }

               class_4588 var25 = this.y(var22);
               if (var25 == null) {
                  return null;
               }

               if (var3[7] instanceof class_1058 var26) {
                  var25 = var26.method_24108(var25);
               }

               var18.method_2819(var20);
               var18.method_62100(var21, var25, (Integer)var3[4], (Integer)var3[5], (Integer)var3[6]);
               return null;
            }
         case "submitItem":
            if (!this.x || this.J || this.a != null && !this.a.d() && this.a.P.getValue()) {
               class_4587 var17 = (class_4587)var3[0];
               int[] var19 = (int[])var3[5];
               class_1921 var9 = (class_1921)var3[7];
               class_12338 var10 = (class_12338)var9.field_64013.field_63987.get("Sampler0");
               if (var10 == null) {
                  return null;
               }

               class_4588 var11 = this.I(var10.comp_5228());

               for (class_777 var13 : z(var3[6])) {
                  int var14 = var13.comp_3722();
                  O(var11, var17.method_23760(), var13, var14 >= 0 && var14 < var19.length ? var19[var14] : -1, (Integer)var3[2], (Integer)var3[3]);
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.x || this.J || this.a != null && !this.a.d() && this.a.P.getValue()) {
               class_1921 var7 = (class_1921)var3[1];
               class_4588 var8 = this.y(var7);
               if (var8 != null) {
                  ((class_11660)var3[2]).render(((class_4587)var3[0]).method_23760(), var8);
               }

               return null;
            }

            return null;
         case "toString":
            return "Larp visual model collector";
         case "hashCode":
            return System.identityHashCode(this);
         case "equals":
            return var1 == var3[0];
         default:
            return null;
      }
   }

   private static String M(Method var0) {
      if (var0.getDeclaringClass() == Object.class) {
         return var0.getName();
      } else {
         Class[] var1 = var0.getParameterTypes();
         if (var1.length == 1 && var1[0] == int.class && var0.getReturnType() == class_11785.class) {
            return "order";
         } else if (var1.length == 10 && var1[0] == class_3879.class) {
            return "submitModel";
         } else if (var1.length == 9 && var1[0] == class_4587.class && var1[1] == class_811.class) {
            return "submitItem";
         } else {
            return var1.length == 3 && var1[0] == class_4587.class && var1[1] == class_1921.class && var1[2] == class_11660.class ? "submitCustomGeometry" : "";
         }
      }
   }

   private static void O(class_4588 var0, class_4665 var1, class_777 var2, int var3, int var4, int var5) {
      var0.method_22919(
         var1, var2, (var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F, (var3 >>> 24 & 0xFF) / 255.0F, var4, var5
      );
   }

   public static List<class_777> z(Object var0) {
      return (List<class_777>)var0;
   }

   public static class_11659 m(class_11659 var0) {
      SelfVisualsModule var1 = SelfVisualsModule.o();
      if (var1 != null && var1.isOperational() && var1.s() && !var1.d() && var1.P.getValue()) {
         gv.y var2 = var1.A();
         return var2 == null ? var0 : (class_11659)Y(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object Y(class_11785 var0, SelfVisualsModule var1, gv.y var2, boolean var3) {
      Class<class_11659> var4 = var3 ? class_11659.class : class_11785.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         }

         String var6 = M(var4x);
         if (var6.equals("order")) {
            return Y(((class_11659)var0).method_73529((Integer)var5[0]), var1, var2, false);
         }

         if (!var6.equals("submitItem")) {
            try {
               return var4x.invoke(var0, var5);
            } catch (InvocationTargetException var22) {
               throw var22.getCause();
            }
         } else {
            class_4587 var7 = (class_4587)var5[0];
            int var8 = (Integer)var5[2];
            int var9 = (Integer)var5[3];
            int[] var10 = (int[])((int[])var5[5]).clone();
            List var11 = List.copyOf(z(var5[6]));
            LinkedHashMap var12 = new LinkedHashMap();

            for (class_777 var14 : var11) {
               class_12338 var15 = (class_12338)((class_1921)var5[7]).field_64013.field_63987.get("Sampler0");
               if (var15 == null) {
                  return var4x.invoke(var0, var5);
               }

               var12.computeIfAbsent(var15.comp_5228(), var0xx -> new ArrayList<>()).add(var14);
            }

            double var23 = var7.method_23760().method_23761().m30();
            double var24 = var7.method_23760().method_23761().m31();
            double var17 = var1.S.getValue();

            for (Entry var20 : var12.entrySet()) {
               List var21 = List.copyOf((Collection)var20.getValue());
               var0.method_73483(var7, k((class_2960)var20.getKey(), var2, true), (var12x, var13) -> {
                  gv.n var14x = new gv.n(var13, var2, ez.a, var17, var23, var24, var1);

                  for (class_777 var16 : var21) {
                     int var17x = var16.comp_3722();
                     O(var14x, var12x, var16, var17x >= 0 && var17x < var10.length ? var10[var17x] : -1, var8, var9);
                  }
               });
            }

            return null;
         }
      });
   }

   public static boolean l(class_630 var0, class_4587 var1, class_11659 var2, class_1921 var3, int var4, int var5, class_1058 var6) {
      SelfVisualsModule var7 = SelfVisualsModule.o();
      if (var7 != null && var7.isOperational() && var7.s() && var7.M.getValue() && (!var7.d() || c1.s())) {
         class_12338 var8 = (class_12338)var3.field_64013.field_63987.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         gv.y var9 = var7.A() == null ? gv.y.V : var7.A();
         double var10 = var1.method_23760().method_23761().m31();
         double var12 = var1.method_23760().method_23761().m30();
         var2.method_73483(var1, k(var8.comp_5228(), var9, true), (var10x, var11) -> {
            long var12x = c5.X();
            class_4587 var14 = new class_4587();
            var14.method_23760().method_66521(var10x);
            class_4588 var15 = new gv.n(var11, var9, ez.a, var7.S(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.method_24108(var15);
            }

            try {
               var0.method_22698(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               q8.O("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.d()) {
                  c1.f(var20);
               } else {
                  var7.disable();
               }
            } finally {
               c5.w("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static class_4588 f(cw var0, gv.y var1) {
      if (!e(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (v.Y != t_.g) {
         A();
         v.Y = t_.g;
      }

      return var0.N(k(P, var1, false));
   }

   public static void X(
      class_4588 var0,
      class_4665 var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      int var10,
      int var11,
      double var12,
      double var14,
      double var16,
      double var18
   ) {
      var0.method_56824(var1, var2, var3, var4);
      var0.method_39415(ei.R(var10, var12));
      var0.method_22913(var5, var6);
      var0.method_60796(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.method_22921(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.method_60831(var1, var7, var8, var9);
   }

   public static void F() {
      k(class_2960.method_60655("minecraft", "textures/entity/player/wide/steve.png"), gv.y.D, true);
   }

   public static boolean b(gv.y var0) {
      return var0 == gv.y.D || var0 == gv.y.C || var0 == gv.y.w;
   }

   public static boolean e(gv.y var0) {
      return G.contains(var0);
   }

   private class_4588 I(class_2960 var1) {
      return new gv.n(
         this.W.N(k(var1, this.I, this.I == gv.y.J || this.x && !this.J && this.I != gv.y.C && this.I != gv.y.w)),
         this.I,
         this.t,
         this.f,
         this.X,
         this.n,
         this.a
      );
   }

   static boolean L(gv.y var0, boolean var1, boolean var2) {
      return var1 && var2 && (var0 == gv.y.O || var0 == gv.y.I || var0 == gv.y.V || var0 == gv.y.K || var0 == gv.y.D || e(var0) && var0 != gv.y.x);
   }

   private class_4588 y(class_1921 var1) {
      if (var1.method_73243() == class_10799.field_56816) {
         return L(this.I, this.x, this.T) ? this.W.N(var1) : null;
      }

      class_12338 var2 = (class_12338)var1.field_64013.field_63987.get("Sampler0");
      return var2 == null ? null : this.I(var2.comp_5228());
   }

   private static class_1921 k(class_2960 var0, gv.y var1, boolean var2) {
      gv.c var3 = new gv.c(var0, var1, var2);
      class_1921 var4 = E.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (E.size() >= 256) {
         E.clear();
      }

      gv.z var5 = new gv.z(var1, var2);
      RenderPipeline var6 = j.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
            .withLocation(class_2960.method_60655("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(class_2960.method_60655("larp-client", "model/material"))
            .withVertexShader(class_2960.method_60655("larp-client", "model/material"))
            .withVertexFormat(class_290.field_1580, class_5596.field_27382)
            .withDepthTestFunction(var1 == gv.y.b ? DepthTestFunction.GREATER_DEPTH_TEST : DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withDepthWrite(var2)
            .withCull(var1 != gv.y.s && var1 != gv.y.J);
         if (var1 == gv.y.J) {
            var7.withoutBlend();
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (b(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == gv.y.C) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == gv.y.w) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == gv.y.C) {
            var7.withoutBlend();
         }

         if (var1 == gv.y.O) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == gv.y.s || var1 == gv.y.U || var1 == gv.y.G || var1 == gv.y.b) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == gv.y.I) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == gv.y.s) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (e(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == gv.y.G || var1 == gv.y.b) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == gv.y.A || var1 == gv.y.n) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == gv.y.n) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         f2.a(var6);
         j.put(var5, var6);
         q8.G("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      class_1921 var8 = class_1921.method_75940(
         "larp_model_" + var1, class_12247.method_75927(var6).method_75934("Sampler0", var0).method_75937().method_75938()
      );
      E.put(var3, var8);
      return var8;
   }

   private record c(class_2960 texture, gv.y material, boolean depthWrite) {
   }

   static final class n implements class_4588 {
      private final class_4588 M;
      private final gv.y R;
      private final int O;
      private final double T;
      private final double w;
      private final double y;
      private final boolean E;
      private final boolean Y;
      private final boolean z;
      private final boolean i;
      private final boolean V;
      private final int f;
      private final int L;
      private final int W;
      private final int Q;
      private final int a;
      private final double U;
      private final double N;
      private final double r;
      private final int v;
      private float m;
      private float b;
      private int J;
      private int K = -1;

      n(class_4588 var1, gv.y var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.M = var1;
         this.R = var2;
         this.O = var3;
         this.T = var4;
         this.w = var6;
         this.y = var8;
         this.i = (boolean)(gv.b(var2) && var10 != null ? 1 : 0);
         this.f = c1.a();
         this.L = c1.N();
         this.U = this.i ? c1.O(var10) : 0.0;
         this.V = (boolean)(gv.e(var2) && var10 != null ? 1 : 0);
         int var11 = this.V ? var10.J() : 0;
         this.W = var11 >> 8 & 65535;
         this.Q = this.V ? var11 & 0xFF | (int)Math.round(var10.A.getValue() * 255.0) << 8 : 0;
         this.a = this.V ? (int)Math.round(var10.w.getValue() * 256.0) : 0;
         this.E = (boolean)(var2 == gv.y.O && var10 != null ? 1 : 0);
         this.Y = (boolean)(this.E && var10.d.G("Horizontal") ? 1 : 0);
         this.N = this.E ? var10.G.getValue() : 0.0;
         this.r = this.V
            ? qo.x() * var10.T.getValue() % 1.0
            : (this.E && !var10.d.G("Static") ? qo.x() * var10.L.getValue() * (var10.d.G("Upward") ? -1 : 1) % 1.0 : 0.0);
         this.z = (boolean)(!this.E && !this.i && !this.V && var2 != gv.y.s && var2 != gv.y.U && var2 != gv.y.G && var2 != gv.y.J && var2 != gv.y.b ? 0 : 1);
         int var12 = this.E
            ? 0xFF000000 | (int)(var10.q.getValue() * 255.0) << 16 | (int)(var10.C.getValue() * 255.0) << 8
            : (this.i ? c1.L(var10, 1.0) : (this.V ? var10.N() : var3));
         this.v = ei.R(var12, var4);
      }

      public class_4588 method_22912(float var1, float var2, float var3) {
         this.m = var1;
         this.b = var2;
         gv var4 = gv.v;
         if (var4.Z) {
            var4.k = Math.min(var4.k, var1);
            var4.e = Math.max(var4.e, var1);
            var4.N = Math.min(var4.N, var2);
            var4.m = Math.max(var4.m, var2);
            var4.q = Math.min(var4.q, var3);
            var4.C = Math.max(var4.C, var3);
         }

         if (this.i) {
            this.K++;
         }

         this.M.method_22912(var1, var2, var3);
         return this;
      }

      public class_4588 method_1336(int var1, int var2, int var3, int var4) {
         return this.method_39415(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public class_4588 method_39415(int var1) {
         int var2 = this.J;
         if (this.i) {
            var2 = (int)(this.U * 4096.0);
         } else if (this.V) {
            var2 = (int)(this.r * 4096.0);
         } else if (this.E) {
            double var3 = this.Y ? this.m - this.w : this.b - this.y;
            double var5 = var3 * this.N + this.r;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.K & 3;
         this.M.method_22921(var2, this.i ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.V ? this.a : 0));
         this.M.method_39415(this.z ? this.v : ei.R(this.R == gv.y.K ? var1 : ei.W(var1, this.O, 0.85), this.T));
         return this;
      }

      public class_4588 method_22913(float var1, float var2) {
         this.M.method_22913(var1, var2);
         return this;
      }

      public class_4588 method_60796(int var1, int var2) {
         this.M.method_60796(this.i ? this.f : (this.V ? this.W : var1), this.i ? this.L : (this.V ? this.Q : var2));
         return this;
      }

      public class_4588 method_22921(int var1, int var2) {
         return this;
      }

      public class_4588 method_22914(float var1, float var2, float var3) {
         this.M.method_22914(var1, var2, var3);
         return this;
      }

      public class_4588 method_75298(float var1) {
         return this;
      }
   }

   public enum y {
      K,
      V,
      I,
      U,
      O,
      s,
      G,
      b,
      A,
      n,
      D,
      C,
      w,
      J,
      p,
      M,
      r,
      y,
      Z,
      X,
      x;
   }

   private record z(gv.y material, boolean depthWrite) {
   }
}
