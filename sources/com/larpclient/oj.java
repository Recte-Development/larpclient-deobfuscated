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

public class oj implements HudElement {
   private static final oj o = new oj();
   private final List<oj.q> L = new ArrayList<>();
   private static final int M = 5;
   private static final int z = 7;
   private static final int a = 260;
   private static final int Z = 10;
   public static final int h = 30;
   private static final int G = 10;
   private static final int c = 52;
   private static final int N = 14;
   private static final int V = 5;
   private static final int C = 16;
   private static final int s = 12;
   private static final int A = 7;
   private static final int W = 20;
   private static final int p = 30;
   private static final int r = 5;
   private static final double d = 12.0;
   private static final double P = ew.N(0.0, 30.0, 12.0);
   private static final int g = 330;
   private static final int U = 280;
   private static final int t = 240;
   private static final int X = 8;
   private static final int Y = 14;
   private static final int f = -603321844;
   private static final int E = -1;
   private static final int b = -6908266;
   private static final int y = -5592406;
   private static final double l = 0.58;
   public static final String F = "Enabled";
   public static final String B = "Disabled";
   private static final long u = 2000L;

   public static oj K() {
      return o;
   }

   public void p() {
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "notificationmanager"), this);
   }

   public void g(String var1, String var2, oj.u var3, long var4) {
      PetsModule var6 = PetsModule.t();
      if (var6 == null || !var6.z(var1, var2, var3, var4)) {
         this.v(var1, var2, var3, var4);
      }
   }

   public void v(String var1, String var2, oj.u var3, long var4) {
      this.a(new oj.q(var1, var2, var3, Math.max(0L, var4)));
   }

   public void x(String var1, String var2, oj.u var3) {
      this.g(var1, var2, var3, 4000L);
   }

   public void r(String var1, boolean var2) {
      this.v(var2 ? "Enabled" : "Disabled", var1, oj.u.O, 2000L);
   }

   private void a(oj.q var1) {
      this.L.add(var1);
      int var2 = 0;

      for (int var3 = this.L.size() - 5; var2 < var3; var2++) {
         this.L.get(var2).I();
      }

      while (this.L.size() > 10) {
         this.L.removeFirst();
      }
   }

   public void B(String var1) {
      this.L.removeIf(var1x -> var1x.i.equals(var1));
      PetsModule var2 = PetsModule.t();
      if (var2 != null) {
         var2.F(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2) {
      Minecraft var3 = Minecraft.getInstance();
      var1 = zo.R(var1, false, true);
      mq.x(var1);
      if (var3.player != null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !h1.I(var3.gui.screen())
         && !(var3.gui.screen() instanceof hu var4 && !var4.Z())) {
         com.larpclient.h.F(var1);
         int var17 = var3.getWindow().getGuiScaledWidth();
         int var18 = var3.getWindow().getGuiScaledHeight();
         mq.s var6 = mq.B("notifications");
         var6.W = 260;
         var6.n = 178;
         var6.P(var17, var18);
         Iterator var7 = this.L.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            oj.q var9 = (oj.q)var7.next();
            var9.J.y(1.0, 330, ef.q.N);
            if (var9.M() > var9.Q) {
               var9.R.y(1.0, 280, ef.q.X);
            }

            if (var9.R.G() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.R.G(), 0.0, 1.0);
               if (!var6.N() && !(var10 <= 0.01)) {
                  int var12 = Z(var9.i, var9.E);
                  int var13 = E(var6.W(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.v) {
                     var9.v = (boolean)1;
                     var9.k.Q(var14 - 14);
                  }

                  int var15 = var6.i(var18) + (int)Math.round(var9.k.y(var14, 240, ef.q.w) - (1.0 - var9.J.G()) * 8.0);
                  P(var1, var6, var17, var18, var13, var15, var12, var9.i, var9.E, var9.U, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.N() && mq.L()) {
            int var19 = Z("Enabled", "Kill Aura");
            P(var1, var6, var17, var18, E(var6.W(var17), var17, var19), var6.i(var18), var19, "Enabled", "Kill Aura", oj.u.O, 1.0);
         }
      }
   }

   private static void P(
      GuiGraphicsExtractor var0, mq.s var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, oj.u var9, double var10
   ) {
      var1.v(var0, var1.W(var2), var1.i(var3));

      try {
         j(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.i(var0);
      }

      mq.H(
         var1,
         new ez.h(
            var1.W(var2) + (int)Math.floor((var4 - var1.W(var2)) * var1.b()),
            var1.i(var3) + (int)Math.floor((var5 - var1.i(var3)) * var1.b()),
            (int)Math.ceil(var6 * var1.b()),
            (int)Math.ceil(30.0 * var1.b())
         )
      );
   }

   public boolean F() {
      return this.L.stream().anyMatch(var0 -> var0.M() < var0.Q + 280L);
   }

   private static int E(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int U(String var0, String var1) {
      if (var0.isBlank()) {
         return we.V(var1, 12.0);
      }

      int var2 = we.V(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + we.V(var1, 12.0);
   }

   private static int Z(String var0, String var1) {
      return Math.min(260, 52 + U(var0, var1) + 14);
   }

   private static oj.f K(String var0) {
      return "Enabled".equals(var0) ? oj.f.V : ("Disabled".equals(var0) ? oj.f.p : oj.f.t);
   }

   private static String y(String var0, oj.u var1) {
      if (K(var0) != oj.f.t) {
         return "power";
      }

      return switch (var1) {
         case O, B -> "bell";
         case i -> "x";
         case j -> "badge-check";
      };
   }

   private static int j(String var0, oj.u var1) {
      oj.f var2 = K(var0);
      if (var2 != oj.f.t) {
         return var2 == oj.f.V ? com.larpclient.f.C : -6908266;
      }

      return switch (var1) {
         case O, j -> com.larpclient.f.C;
         case B -> -996986;
         case i -> -6908266;
      };
   }

   public static void N(GuiGraphicsExtractor var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = Z("Enabled", "Kill Aura");
      j(var0, E(var1, var3.getWindow().getGuiScaledWidth(), var4), var2, var4, "Enabled", "Kill Aura", oj.u.O, 1.0);
   }

   private static void j(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, oj.u var6, double var7) {
      c7.n(var0, var1, var2, var3, 30.0F, 10.0F, _3.U(-603321844, var7));
      we.C(var0, y(var4, var6), var1 + 12, var2 + 7, 16, _3.U(j(var4, var6), var7));
      we.C(var0, "chevron-right", var1 + 30, var2 + 5, 20, _3.U(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         we.F(var0, we.t(var5, var9, 12.0), var1 + 52, var2 + P, 12.0, _3.U(-1, var7));
      } else {
         String var10 = we.t(var4, var9, 12.0);
         we.F(var0, var10, var1 + 52, var2 + P, 12.0, _3.U(-1, var7));
         if (!var5.isBlank()) {
            int var11 = we.V(var10, 12.0) + 5;
            we.F(var0, we.t(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + P, 12.0, _3.U(com.larpclient.f.C, var7));
         }
      }
   }

   private enum f {
      t,
      V,
      p;
   }

   private static final class q {
      final String i;
      final String E;
      final oj.u U;
      final long Q;
      long Y = System.nanoTime();
      boolean v;
      final ef J = new ef(0.0);
      final ef R = new ef(0.0);
      final ef k = new ef(0.0);

      q(String var1, String var2, oj.u var3, long var4) {
         this.i = var1 == null ? "" : var1;
         this.E = var2 == null ? "" : var2;
         this.U = var3;
         this.Q = var4;
      }

      long M() {
         return (System.nanoTime() - this.Y) / 1000000L;
      }

      void I() {
         this.Y = Math.min(this.Y, System.nanoTime() - this.Q * 1000000L);
      }
   }

   public enum u {
      O,
      B,
      i,
      j;
   }
}
