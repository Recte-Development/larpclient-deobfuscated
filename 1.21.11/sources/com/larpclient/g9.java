package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_332;

public final class g9 {
   public static final int S = 21;
   public static final int C = 4;
   public static final int l = 13;
   public static final int U = 4;
   public static final int y = 10;
   public static final int b = 7;
   public static final double c = 34.0;
   public static final double T = 12.0;
   public static final long N = 90000000L;
   private static final Map<String, Float> q = new HashMap<>();
   private static final g9.r H = new g9.r();
   private static final g9.r g = new g9.r();
   private static Object M;
   private static long J;
   private static long r;
   private static long B;

   private g9() {
   }

   public static boolean I() {
      HUDModule var0 = HUDModule.y();
      return var0 != null && var0.u("keystrokes");
   }

   public static void Y(int var0) {
      if (I() && class_310.method_1551().field_1755 == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? H : g).v(System.nanoTime());
      }
   }

   public static void n() {
      if (I()) {
         long var0 = System.nanoTime();
         H.v(var0);
         r = var0;
      }
   }

   public static void B() {
      if (I()) {
         B = System.nanoTime();
      }
   }

   public static boolean D(long var0, long var2) {
      return var2 != 0L && var0 - var2 >= 0L && var0 - var2 < 90000000L;
   }

   public static int n(boolean var0) {
      return (var0 ? H : g).B(System.nanoTime());
   }

   public static int T(HUDModule var0) {
      return t(var0) * 2 + 63 + 8;
   }

   public static int I(HUDModule var0) {
      int var1 = t(var0) * 2 + 42 + 4;
      if (var0.v.getValue()) {
         var1 += 25;
      }

      if (var0.G.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int t(HUDModule var0) {
      return var0.m.getValue().intValue();
   }

   public static float x(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void N(class_332 var0, HUDModule var1, int var2, int var3, boolean var4) {
      class_310 var5 = class_310.method_1551();
      long var6 = System.nanoTime();
      double var8 = J == 0L ? 0.016666666666666666 : (var6 - J) / 1.0E9;
      J = var6;
      if (M != var5.field_1687) {
         q.clear();
         H.Q();
         g.Q();
         B = 0L;
         r = 0L;
         M = var5.field_1687;
      }

      int var10 = T(var1);
      int var11 = I(var1);
      gu.N(var0, var2, var3, var10, var11, 8.0F, ei.D(-16119284, var1.L.getValue() / 255.0));
      ur.D(var0, var2, var3, var10, var11, 8.0F);
      ur.x(var0, var2, var3, var10, var11, 8);
      class_315 var12 = var5.field_1690;
      boolean var13 = var1.s.getValue();
      int var14 = var2 + t(var1);
      int var15 = var3 + t(var1);
      byte var16 = 25;
      int var17 = A(var12.field_1894, var4) && !D(var6, B) ? 1 : 0;
      p(var0, var13 ? "↑" : "W", null, (boolean)var17, var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      p(var0, var13 ? "←" : "A", null, A(var12.field_1913, var4), var14, var15, 21, 21, var8);
      p(var0, var13 ? "↓" : "S", null, A(var12.field_1881, var4), var14 + var16, var15, 21, 21, var8);
      p(var0, var13 ? "→" : "D", null, A(var12.field_1849, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var18 = 71;
      if (var1.v.getValue()) {
         int var19 = (var18 - 4) / 2;
         boolean var20 = var1.n.getValue();
         int var21 = !A(var12.field_1886, var4) && (var4 || !D(var6, r)) ? 0 : 1;
         p(var0, "LMB", var20 ? n(true) + " CPS" : null, (boolean)var21, var14, var15, var19, 21, var8);
         p(var0, "RMB", var20 ? n(false) + " CPS" : null, A(var12.field_1904, var4), var14 + var18 - var19, var15, var19, 21, var8);
         var15 += var16;
      }

      if (var1.G.getValue()) {
         float var24 = H("Space", A(var12.field_1903, var4), var8);
         Z(var0, var14, var15, var18, 13, var24);
         int var25 = Math.round(var18 * 0.3F);
         gu.N(var0, var14 + (var18 - var25) / 2, var15 + 6 - 1, var25, 2.0F, 1.0F, ei.W(-7697772, -1, var24));
      }
   }

   private static boolean A(class_304 var0, boolean var1) {
      return !var1 && var0.method_1434();
   }

   private static float H(String var0, boolean var1, double var2) {
      float var4 = x(q.getOrDefault(var0, 0.0F), var1, var2);
      q.put(var0, var4);
      return var4;
   }

   private static void Z(class_332 var0, int var1, int var2, int var3, int var4, float var5) {
      gu.N(var0, var1, var2, var3, var4, 4.0F, ei.D(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         ws.K(var0, var1, var2, var3, var4, 4.0F, 6.0F * var5, ei.R(ez.a, 0.45 * var5), ei.R(ez.a, 0.45 * var5));
         gu.N(var0, var1, var2, var3, var4, 4.0F, ei.R(ez.a, 0.9 * var5));
      }
   }

   private static void p(class_332 var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = H(var1, var3, var8);
      Z(var0, var4, var5, var6, var7, var10);
      int var11 = ei.W(ez.PH, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         w5.y(var0, var1, var12 - w5.A(var1, 10.0) / 2.0, q2.e(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 7.5368185F;
         w5.y(var0, var1, var12 - w5.A(var1, 10.0) / 2.0, var14, 10.0, var11);
         w5.y(var0, var2, var12 - w5.A(var2, 7.0) / 2.0, var14 + 10.0, 7.0, ei.W(-7697772, -1, var10));
      }
   }

   public static final class r {
      public static final long C = 1000000000L;
      private final ArrayDeque<Long> p = new ArrayDeque<>();

      public void v(long var1) {
         this.p.addLast(var1);
         this.x(var1);
      }

      public int B(long var1) {
         this.x(var1);
         return this.p.size();
      }

      public void Q() {
         this.p.clear();
      }

      private void x(long var1) {
         while (!this.p.isEmpty() && var1 - this.p.peekFirst() > 1000000000L) {
            this.p.removeFirst();
         }
      }
   }
}
