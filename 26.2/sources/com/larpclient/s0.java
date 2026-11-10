package com.larpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class s0 implements HudElement {
   private static final s0 c = new s0();
   private final List<s0.s> u = new ArrayList<>();
   private static final int A = 5;
   private static final int p = 7;
   private static final int t = 260;
   private static final int D = 10;
   public static final int I = 30;
   private static final int X = 10;
   private static final int B = 52;
   private static final int m = 14;
   private static final int k = 5;
   private static final int a = 16;
   private static final int S = 12;
   private static final int U = 7;
   private static final int O = 20;
   private static final int G = 30;
   private static final int d = 5;
   private static final double s = 12.0;
   private static final double r = c2.X(0.0, 30.0, 12.0);
   private static final int g = 330;
   private static final int y = 280;
   private static final int W = 240;
   private static final int R = 8;
   private static final int q = 14;
   private static final int x = -603321844;
   private static final int Y = -1;
   private static final int T = -6908266;
   private static final int l = -5592406;
   private static final double C = 0.58;
   public static final String h = "Enabled";
   public static final String e = "Disabled";
   private static final long j = 2000L;

   public static s0 s() {
      return c;
   }

   public void v() {
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "notificationmanager"), this);
   }

   public void k(String var1, String var2, s0.u var3, long var4) {
      PetsModule var6 = PetsModule.I();
      if (var6 == null || !var6.l(var1, var2, var3, var4)) {
         this.Z(var1, var2, var3, var4);
      }
   }

   public void Z(String var1, String var2, s0.u var3, long var4) {
      this.r(new s0.s(var1, var2, var3, Math.max(0L, var4)));
   }

   public void V(String var1, String var2, s0.u var3) {
      this.k(var1, var2, var3, 4000L);
   }

   public void x(String var1, boolean var2) {
      this.Z(var2 ? "Enabled" : "Disabled", var1, s0.u.B, 2000L);
   }

   private void r(s0.s var1) {
      this.u.add(var1);
      int var2 = 0;

      for (int var3 = this.u.size() - 5; var2 < var3; var2++) {
         this.u.get(var2).S();
      }

      while (this.u.size() > 10) {
         this.u.removeFirst();
      }
   }

   public void c() {
      this.u.clear();
   }

   private static String k() {
      return com.larpclient.module.c.p() ? "Zoom" : "Kill Aura";
   }

   public void G(String var1) {
      this.u.removeIf(var1x -> var1x.X.equals(var1));
      PetsModule var2 = PetsModule.I();
      if (var2 != null) {
         var2.A(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2) {
      Minecraft var3 = Minecraft.getInstance();
      var1 = p2.x(var1, false, true);
      m4.Z(var1);
      if (var3.player != null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !hi.X(var3.gui.screen())
         && !(var3.gui.screen() instanceof h7 var4 && !var4.G())) {
         kz.E(var1);
         int var17 = var3.getWindow().getGuiScaledWidth();
         int var18 = var3.getWindow().getGuiScaledHeight();
         m4.p var6 = m4.S("notifications");
         var6.d = 260;
         var6.T = 178;
         var6.b(var17, var18);
         Iterator var7 = this.u.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            s0.s var9 = (s0.s)var7.next();
            var9.D.y(1.0, 330, jp.m.A);
            if (var9.s() > var9.s) {
               var9.G.y(1.0, 280, jp.m.U);
            }

            if (var9.G.B() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.G.B(), 0.0, 1.0);
               if (!var6.k() && !(var10 <= 0.01)) {
                  int var12 = X(var9.X, var9.p);
                  int var13 = o(var6.T(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.F) {
                     var9.F = (boolean)1;
                     var9.t.F(var14 - 14);
                  }

                  int var15 = var6.P(var18) + (int)Math.round(var9.t.y(var14, 240, jp.m.X) - (1.0 - var9.D.B()) * 8.0);
                  N(var1, var6, var17, var18, var13, var15, var12, var9.X, var9.p, var9.i, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.k() && m4.t()) {
            int var19 = X("Enabled", k());
            N(var1, var6, var17, var18, o(var6.T(var17), var17, var19), var6.P(var18), var19, "Enabled", k(), s0.u.B, 1.0);
         }
      }
   }

   private static void N(
      GuiGraphicsExtractor var0, m4.p var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, s0.u var9, double var10
   ) {
      var1.N(var0, var1.T(var2), var1.P(var3));

      try {
         F(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.w(var0);
      }

      m4.k(
         var1,
         new hw.l(
            var1.T(var2) + (int)Math.floor((var4 - var1.T(var2)) * var1.s()),
            var1.P(var3) + (int)Math.floor((var5 - var1.P(var3)) * var1.s()),
            (int)Math.ceil(var6 * var1.s()),
            (int)Math.ceil(30.0 * var1.s())
         )
      );
   }

   public boolean o() {
      return this.u.stream().anyMatch(var0 -> var0.s() < var0.s + 280L);
   }

   private static int o(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int v(String var0, String var1) {
      if (var0.isBlank()) {
         return gw.m(var1, 12.0);
      }

      int var2 = gw.m(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + gw.m(var1, 12.0);
   }

   private static int X(String var0, String var1) {
      return Math.min(260, 52 + v(var0, var1) + 14);
   }

   private static s0.n a(String var0) {
      return "Enabled".equals(var0) ? s0.n.L : ("Disabled".equals(var0) ? s0.n.s : s0.n.u);
   }

   private static String q(String var0, s0.u var1) {
      if (a(var0) != s0.n.u) {
         return "power";
      }

      return switch (var1) {
         case B, p -> "bell";
         case M -> "x";
         case m -> "badge-check";
      };
   }

   private static int i(String var0, s0.u var1) {
      s0.n var2 = a(var0);
      if (var2 != s0.n.u) {
         return var2 == s0.n.L ? ho.J : -6908266;
      }

      return switch (var1) {
         case B, m -> ho.J;
         case p -> -996986;
         case M -> -6908266;
      };
   }

   public static void z(GuiGraphicsExtractor var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = X("Enabled", k());
      F(var0, o(var1, var3.getWindow().getGuiScaledWidth(), var4), var2, var4, "Enabled", k(), s0.u.B, 1.0);
   }

   private static void F(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, s0.u var6, double var7) {
      kv.f(var0, var1, var2, var3, 30.0F, 10.0F, uh.F(-603321844, var7));
      gw.i(var0, q(var4, var6), var1 + 12, var2 + 7, 16, uh.F(i(var4, var6), var7));
      gw.i(var0, "chevron-right", var1 + 30, var2 + 5, 20, uh.F(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         gw.S(var0, gw.J(var5, var9, 12.0), var1 + 52, var2 + r, 12.0, uh.F(-1, var7));
      } else {
         String var10 = gw.J(var4, var9, 12.0);
         gw.S(var0, var10, var1 + 52, var2 + r, 12.0, uh.F(-1, var7));
         if (!var5.isBlank()) {
            int var11 = gw.m(var10, 12.0) + 5;
            gw.S(var0, gw.J(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + r, 12.0, uh.F(ho.J, var7));
         }
      }
   }

   private enum n {
      u,
      L,
      s;
   }

   private static final class s {
      final String X;
      final String p;
      final s0.u i;
      final long s;
      long g = System.nanoTime();
      boolean F;
      final jp D = new jp(0.0);
      final jp G = new jp(0.0);
      final jp t = new jp(0.0);

      s(String var1, String var2, s0.u var3, long var4) {
         this.X = var1 == null ? "" : var1;
         this.p = var2 == null ? "" : var2;
         this.i = var3;
         this.s = var4;
      }

      long s() {
         return (System.nanoTime() - this.g) / 1000000L;
      }

      void S() {
         this.g = Math.min(this.g, System.nanoTime() - this.s * 1000000L);
      }
   }

   public enum u {
      B,
      p,
      M,
      m;
   }
}
