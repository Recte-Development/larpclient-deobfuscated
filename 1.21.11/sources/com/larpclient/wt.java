package com.larpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_9779;

public class wt implements HudElement {
   private static final wt c = new wt();
   private final List<wt.z> Z = new ArrayList<>();
   private static final int d = 5;
   private static final int X = 7;
   private static final int w = 260;
   private static final int b = 10;
   public static final int a = 30;
   private static final int y = 10;
   private static final int R = 52;
   private static final int N = 14;
   private static final int D = 5;
   private static final int m = 16;
   private static final int A = 12;
   private static final int k = 7;
   private static final int T = 20;
   private static final int G = 30;
   private static final int e = 5;
   private static final double f = 12.0;
   private static final double M = q2.e(0.0, 30.0, 12.0);
   private static final int O = 330;
   private static final int t = 280;
   private static final int U = 240;
   private static final int W = 8;
   private static final int S = 14;
   private static final int v = -603321844;
   private static final int F = -1;
   private static final int I = -6908266;
   private static final int E = -5592406;
   private static final double C = 0.58;
   public static final String o = "Enabled";
   public static final String q = "Disabled";
   private static final long L = 2000L;

   public static wt T() {
      return c;
   }

   public void J() {
      HudElementRegistry.addLast(class_2960.method_60655("larp-client", "notificationmanager"), this);
   }

   public void k(String var1, String var2, wt.s var3, long var4) {
      PetsModule var6 = PetsModule.F();
      if (var6 == null || !var6.P(var1, var2, var3, var4)) {
         this.z(var1, var2, var3, var4);
      }
   }

   public void z(String var1, String var2, wt.s var3, long var4) {
      this.h(new wt.z(var1, var2, var3, Math.max(0L, var4)));
   }

   public void U(String var1, String var2, wt.s var3) {
      this.k(var1, var2, var3, 4000L);
   }

   public void m(String var1, boolean var2) {
      this.z(var2 ? "Enabled" : "Disabled", var1, wt.s.R, 2000L);
   }

   private void h(wt.z var1) {
      this.Z.add(var1);
      int var2 = 0;

      for (int var3 = this.Z.size() - 5; var2 < var3; var2++) {
         this.Z.get(var2).u();
      }

      while (this.Z.size() > 10) {
         this.Z.removeFirst();
      }
   }

   public void Q() {
      this.Z.clear();
   }

   private static String X() {
      return com.larpclient.module.lf.l() ? "Zoom" : "Kill Aura";
   }

   public void w(String var1) {
      this.Z.removeIf(var1x -> var1x.l.equals(var1));
      PetsModule var2 = PetsModule.F();
      if (var2 != null) {
         var2.Y(var1);
      }
   }

   public void render(class_332 var1, class_9779 var2) {
      class_310 var3 = class_310.method_1551();
      var1 = wi.p(var1, false, true);
      com.larpclient.k.U(var1);
      if (var3.field_1724 != null && !var3.field_1690.field_1842 && !tm.K(var3.field_1755) && !(var3.field_1755 instanceof tj var4 && !var4.N())) {
         wc.G(var1);
         int var17 = var3.method_22683().method_4486();
         int var18 = var3.method_22683().method_4502();
         k.l var6 = com.larpclient.k.m("notifications");
         var6.L = 260;
         var6.F = 178;
         var6.O(var17, var18);
         Iterator var7 = this.Z.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            wt.z var9 = (wt.z)var7.next();
            var9.x.G(1.0, 330, qb.y.y);
            if (var9.T() > var9.c) {
               var9.J.G(1.0, 280, qb.y.J);
            }

            if (var9.J.u() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.J.u(), 0.0, 1.0);
               if (!var6.q() && !(var10 <= 0.01)) {
                  int var12 = c(var9.l, var9.s);
                  int var13 = X(var6.l(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.k) {
                     var9.k = (boolean)1;
                     var9.C.w(var14 - 14);
                  }

                  int var15 = var6.M(var18) + (int)Math.round(var9.C.G(var14, 240, qb.y.z) - (1.0 - var9.x.u()) * 8.0);
                  V(var1, var6, var17, var18, var13, var15, var12, var9.l, var9.s, var9.o, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.q() && com.larpclient.k.k()) {
            int var19 = c("Enabled", X());
            V(var1, var6, var17, var18, X(var6.l(var17), var17, var19), var6.M(var18), var19, "Enabled", X(), wt.s.R, 1.0);
         }
      }
   }

   private static void V(class_332 var0, k.l var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, wt.s var9, double var10) {
      var1.e(var0, var1.l(var2), var1.M(var3));

      try {
         p(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.o(var0);
      }

      com.larpclient.k.d(
         var1,
         new cv.z(
            var1.l(var2) + (int)Math.floor((var4 - var1.l(var2)) * var1.o()),
            var1.M(var3) + (int)Math.floor((var5 - var1.M(var3)) * var1.o()),
            (int)Math.ceil(var6 * var1.o()),
            (int)Math.ceil(30.0 * var1.o())
         )
      );
   }

   public boolean h() {
      return this.Z.stream().anyMatch(var0 -> var0.T() < var0.c + 280L);
   }

   private static int X(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int Z(String var0, String var1) {
      if (var0.isBlank()) {
         return w5.A(var1, 12.0);
      }

      int var2 = w5.A(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + w5.A(var1, 12.0);
   }

   private static int c(String var0, String var1) {
      return Math.min(260, 52 + Z(var0, var1) + 14);
   }

   private static wt.a X(String var0) {
      return "Enabled".equals(var0) ? wt.a.q : ("Disabled".equals(var0) ? wt.a.V : wt.a.N);
   }

   private static String T(String var0, wt.s var1) {
      if (X(var0) != wt.a.N) {
         return "power";
      }

      return switch (var1) {
         case R, a -> "bell";
         case u -> "x";
         case C -> "badge-check";
      };
   }

   private static int o(String var0, wt.s var1) {
      wt.a var2 = X(var0);
      if (var2 != wt.a.N) {
         return var2 == wt.a.q ? ez.a : -6908266;
      }

      return switch (var1) {
         case R, C -> ez.a;
         case a -> -996986;
         case u -> -6908266;
      };
   }

   public static void f(class_332 var0, int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      int var4 = c("Enabled", X());
      p(var0, X(var1, var3.method_22683().method_4486(), var4), var2, var4, "Enabled", X(), wt.s.R, 1.0);
   }

   private static void p(class_332 var0, int var1, int var2, int var3, String var4, String var5, wt.s var6, double var7) {
      gu.N(var0, var1, var2, var3, 30.0F, 10.0F, ei.R(-603321844, var7));
      w5.C(var0, T(var4, var6), var1 + 12, var2 + 7, 16, ei.R(o(var4, var6), var7));
      w5.C(var0, "chevron-right", var1 + 30, var2 + 5, 20, ei.R(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         w5.y(var0, w5.O(var5, var9, 12.0), var1 + 52, var2 + M, 12.0, ei.R(-1, var7));
      } else {
         String var10 = w5.O(var4, var9, 12.0);
         w5.y(var0, var10, var1 + 52, var2 + M, 12.0, ei.R(-1, var7));
         if (!var5.isBlank()) {
            int var11 = w5.A(var10, 12.0) + 5;
            w5.y(var0, w5.O(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + M, 12.0, ei.R(ez.a, var7));
         }
      }
   }

   private enum a {
      N,
      q,
      V;
   }

   public enum s {
      R,
      a,
      u,
      C;
   }

   private static final class z {
      final String l;
      final String s;
      final wt.s o;
      final long c;
      long q = System.nanoTime();
      boolean k;
      final qb x = new qb(0.0);
      final qb J = new qb(0.0);
      final qb C = new qb(0.0);

      z(String var1, String var2, wt.s var3, long var4) {
         this.l = var1 == null ? "" : var1;
         this.s = var2 == null ? "" : var2;
         this.o = var3;
         this.c = var4;
      }

      long T() {
         return (System.nanoTime() - this.q) / 1000000L;
      }

      void u() {
         this.q = Math.min(this.q, System.nanoTime() - this.c * 1000000L);
      }
   }
}
