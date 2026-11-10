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

public class ib implements HudElement {
   private static final ib P = new ib();
   private final List<ib.h> r = new ArrayList<>();
   private static final int o = 5;
   private static final int d = 7;
   private static final int B = 260;
   private static final int R = 10;
   public static final int h = 30;
   private static final int n = 10;
   private static final int x = 52;
   private static final int K = 14;
   private static final int S = 5;
   private static final int A = 16;
   private static final int E = 12;
   private static final int w = 7;
   private static final int l = 20;
   private static final int i = 30;
   private static final int L = 5;
   private static final double c = 12.0;
   private static final double t = n8.b(0.0, 30.0, 12.0);
   private static final int k = 330;
   private static final int b = 280;
   private static final int f = 240;
   private static final int g = 8;
   private static final int T = 14;
   private static final int e = -603321844;
   private static final int W = -1;
   private static final int j = -6908266;
   private static final int X = -5592406;
   private static final double U = 0.58;
   public static final String C = "Enabled";
   public static final String u = "Disabled";
   private static final long Q = 2000L;

   public static ib L() {
      return P;
   }

   public void w() {
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "notificationmanager"), this);
   }

   public void N(String var1, String var2, ib.v var3, long var4) {
      PetsModule var6 = PetsModule.X();
      if (var6 == null || !var6.O(var1, var2, var3, var4)) {
         this.l(var1, var2, var3, var4);
      }
   }

   public void l(String var1, String var2, ib.v var3, long var4) {
      this.Q(new ib.h(var1, var2, var3, Math.max(0L, var4)));
   }

   public void t(String var1, String var2, ib.v var3) {
      this.N(var1, var2, var3, 4000L);
   }

   public void z(String var1, boolean var2) {
      this.l(var2 ? "Enabled" : "Disabled", var1, ib.v.f, 2000L);
   }

   private void Q(ib.h var1) {
      this.r.add(var1);
      int var2 = 0;

      for (int var3 = this.r.size() - 5; var2 < var3; var2++) {
         this.r.get(var2).D();
      }

      while (this.r.size() > 10) {
         this.r.removeFirst();
      }
   }

   public void R() {
      this.r.clear();
   }

   private static String j() {
      return com.larpclient.module.fj.r() ? "Zoom" : "Kill Aura";
   }

   public void i(String var1) {
      this.r.removeIf(var1x -> var1x.C.equals(var1));
      PetsModule var2 = PetsModule.X();
      if (var2 != null) {
         var2.L(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2) {
      Minecraft var3 = Minecraft.getInstance();
      var1 = qc.h(var1, false, true);
      dk.n(var1);
      if (var3.player != null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !fj.X(var3.gui.screen())
         && !(var3.gui.screen() instanceof fv var4 && !var4.C())) {
         wp.j(var1);
         int var17 = var3.getWindow().getGuiScaledWidth();
         int var18 = var3.getWindow().getGuiScaledHeight();
         dk.q var6 = dk.T("notifications");
         var6.w = 260;
         var6.Z = 178;
         var6.s(var17, var18);
         Iterator var7 = this.r.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            ib.h var9 = (ib.h)var7.next();
            var9.n.r(1.0, 330, fq.y.j);
            if (var9.u() > var9.O) {
               var9.i.r(1.0, 280, fq.y.q);
            }

            if (var9.i.K() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.i.K(), 0.0, 1.0);
               if (!var6.X() && !(var10 <= 0.01)) {
                  int var12 = p(var9.C, var9.Q);
                  int var13 = H(var6.f(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.w) {
                     var9.w = (boolean)1;
                     var9.J.p(var14 - 14);
                  }

                  int var15 = var6.p(var18) + (int)Math.round(var9.J.r(var14, 240, fq.y.X) - (1.0 - var9.n.K()) * 8.0);
                  Y(var1, var6, var17, var18, var13, var15, var12, var9.C, var9.Q, var9.m, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.X() && dk.w()) {
            int var19 = p("Enabled", j());
            Y(var1, var6, var17, var18, H(var6.f(var17), var17, var19), var6.p(var18), var19, "Enabled", j(), ib.v.f, 1.0);
         }
      }
   }

   private static void Y(
      GuiGraphicsExtractor var0, dk.q var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, ib.v var9, double var10
   ) {
      var1.p(var0, var1.f(var2), var1.p(var3));

      try {
         G(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.I(var0);
      }

      dk.X(
         var1,
         new qy.i(
            var1.f(var2) + (int)Math.floor((var4 - var1.f(var2)) * var1.F()),
            var1.p(var3) + (int)Math.floor((var5 - var1.p(var3)) * var1.F()),
            (int)Math.ceil(var6 * var1.F()),
            (int)Math.ceil(30.0 * var1.F())
         )
      );
   }

   public boolean r() {
      return this.r.stream().anyMatch(var0 -> var0.u() < var0.O + 280L);
   }

   private static int H(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int P(String var0, String var1) {
      if (var0.isBlank()) {
         return i5.l(var1, 12.0);
      }

      int var2 = i5.l(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + i5.l(var1, 12.0);
   }

   private static int p(String var0, String var1) {
      return Math.min(260, 52 + P(var0, var1) + 14);
   }

   private static ib.w g(String var0) {
      return "Enabled".equals(var0) ? ib.w.M : ("Disabled".equals(var0) ? ib.w.F : ib.w.t);
   }

   private static String o(String var0, ib.v var1) {
      if (g(var0) != ib.w.t) {
         return "power";
      }

      return switch (var1) {
         case f, o -> "bell";
         case V -> "x";
         case x -> "badge-check";
      };
   }

   private static int F(String var0, ib.v var1) {
      ib.w var2 = g(var0);
      if (var2 != ib.w.t) {
         return var2 == ib.w.M ? com.larpclient.c.y : -6908266;
      }

      return switch (var1) {
         case f, x -> com.larpclient.c.y;
         case o -> -996986;
         case V -> -6908266;
      };
   }

   public static void c(GuiGraphicsExtractor var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = p("Enabled", j());
      G(var0, H(var1, var3.getWindow().getGuiScaledWidth(), var4), var2, var4, "Enabled", j(), ib.v.f, 1.0);
   }

   private static void G(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, ib.v var6, double var7) {
      h5.q(var0, var1, var2, var3, 30.0F, 10.0F, vv.O(-603321844, var7));
      i5.t(var0, o(var4, var6), var1 + 12, var2 + 7, 16, vv.O(F(var4, var6), var7));
      i5.t(var0, "chevron-right", var1 + 30, var2 + 5, 20, vv.O(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         i5.i(var0, i5.I(var5, var9, 12.0), var1 + 52, var2 + t, 12.0, vv.O(-1, var7));
      } else {
         String var10 = i5.I(var4, var9, 12.0);
         i5.i(var0, var10, var1 + 52, var2 + t, 12.0, vv.O(-1, var7));
         if (!var5.isBlank()) {
            int var11 = i5.l(var10, 12.0) + 5;
            i5.i(var0, i5.I(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + t, 12.0, vv.O(com.larpclient.c.y, var7));
         }
      }
   }

   private static final class h {
      final String C;
      final String Q;
      final ib.v m;
      final long O;
      long K = System.nanoTime();
      boolean w;
      final fq n = new fq(0.0);
      final fq i = new fq(0.0);
      final fq J = new fq(0.0);

      h(String var1, String var2, ib.v var3, long var4) {
         this.C = var1 == null ? "" : var1;
         this.Q = var2 == null ? "" : var2;
         this.m = var3;
         this.O = var4;
      }

      long u() {
         return (System.nanoTime() - this.K) / 1000000L;
      }

      void D() {
         this.K = Math.min(this.K, System.nanoTime() - this.O * 1000000L);
      }
   }

   public enum v {
      f,
      o,
      V,
      x;
   }

   private enum w {
      t,
      M,
      F;
   }
}
