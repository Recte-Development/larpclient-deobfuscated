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

public class hg implements HudElement {
   private static final hg Z = new hg();
   private final List<hg.s> x = new ArrayList<>();
   private static final int G = 5;
   private static final int z = 7;
   private static final int k = 260;
   private static final int b = 10;
   public static final int X = 30;
   private static final int v = 10;
   private static final int y = 52;
   private static final int l = 14;
   private static final int Y = 5;
   private static final int K = 16;
   private static final int m = 12;
   private static final int U = 7;
   private static final int j = 20;
   private static final int S = 30;
   private static final int f = 5;
   private static final double p = 12.0;
   private static final double h = rz.W(0.0, 30.0, 12.0);
   private static final int s = 330;
   private static final int e = 280;
   private static final int o = 240;
   private static final int P = 8;
   private static final int g = 14;
   private static final int t = -603321844;
   private static final int H = -1;
   private static final int w = -6908266;
   private static final int O = -5592406;
   private static final double L = 0.58;
   public static final String R = "Enabled";
   public static final String n = "Disabled";
   private static final long N = 2000L;

   public static hg B() {
      return Z;
   }

   public void K() {
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "notificationmanager"), this);
   }

   public void Y(String var1, String var2, hg.u var3, long var4) {
      PetsModule var6 = PetsModule.s();
      if (var6 == null || !var6.e(var1, var2, var3, var4)) {
         this.J(var1, var2, var3, var4);
      }
   }

   public void J(String var1, String var2, hg.u var3, long var4) {
      this.J(new hg.s(var1, var2, var3, Math.max(0L, var4)));
   }

   public void o(String var1, String var2, hg.u var3) {
      this.Y(var1, var2, var3, 4000L);
   }

   public void G(String var1, boolean var2) {
      this.J(var2 ? "Enabled" : "Disabled", var1, hg.u.U, 2000L);
   }

   private void J(hg.s var1) {
      this.x.add(var1);
      int var2 = 0;

      for (int var3 = this.x.size() - 5; var2 < var3; var2++) {
         this.x.get(var2).o();
      }

      while (this.x.size() > 10) {
         this.x.removeFirst();
      }
   }

   public void M(String var1) {
      this.x.removeIf(var1x -> var1x.J.equals(var1));
      PetsModule var2 = PetsModule.s();
      if (var2 != null) {
         var2.T(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2) {
      Minecraft var3 = Minecraft.getInstance();
      var1 = y8.U(var1, false, true);
      o8.e(var1);
      if (var3.player != null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !pc.s(var3.gui.screen())
         && !(var3.gui.screen() instanceof ps var4 && !var4.w())) {
         f8.w(var1);
         int var17 = var3.getWindow().getGuiScaledWidth();
         int var18 = var3.getWindow().getGuiScaledHeight();
         o8.a var6 = o8.q("notifications");
         var6.N = 260;
         var6.F = 178;
         var6.q(var17, var18);
         Iterator var7 = this.x.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            hg.s var9 = (hg.s)var7.next();
            var9.q.c(1.0, 330, u7.p.Y);
            if (var9.t() > var9.L) {
               var9.K.c(1.0, 280, u7.p.D);
            }

            if (var9.K.V() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.K.V(), 0.0, 1.0);
               if (!var6.z() && !(var10 <= 0.01)) {
                  int var12 = w(var9.J, var9.X);
                  int var13 = K(var6.b(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.r) {
                     var9.r = (boolean)1;
                     var9.M.c(var14 - 14);
                  }

                  int var15 = var6.M(var18) + (int)Math.round(var9.M.c(var14, 240, u7.p.Z) - (1.0 - var9.q.V()) * 8.0);
                  T(var1, var6, var17, var18, var13, var15, var12, var9.J, var9.X, var9.V, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.z() && o8.u()) {
            int var19 = w("Enabled", "Kill Aura");
            T(var1, var6, var17, var18, K(var6.b(var17), var17, var19), var6.M(var18), var19, "Enabled", "Kill Aura", hg.u.U, 1.0);
         }
      }
   }

   private static void T(
      GuiGraphicsExtractor var0, o8.a var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, hg.u var9, double var10
   ) {
      var1.J(var0, var1.b(var2), var1.M(var3));

      try {
         c(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.z(var0);
      }

      o8.D(
         var1,
         new _h.p(
            var1.b(var2) + (int)Math.floor((var4 - var1.b(var2)) * var1.p()),
            var1.M(var3) + (int)Math.floor((var5 - var1.M(var3)) * var1.p()),
            (int)Math.ceil(var6 * var1.p()),
            (int)Math.ceil(30.0 * var1.p())
         )
      );
   }

   public boolean K() {
      return this.x.stream().anyMatch(var0 -> var0.t() < var0.L + 280L);
   }

   private static int K(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int W(String var0, String var1) {
      if (var0.isBlank()) {
         return rd.H(var1, 12.0);
      }

      int var2 = rd.H(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + rd.H(var1, 12.0);
   }

   private static int w(String var0, String var1) {
      return Math.min(260, 52 + W(var0, var1) + 14);
   }

   private static hg.q x(String var0) {
      return "Enabled".equals(var0) ? hg.q.N : ("Disabled".equals(var0) ? hg.q.F : hg.q.y);
   }

   private static String z(String var0, hg.u var1) {
      if (x(var0) != hg.q.y) {
         return "power";
      }

      return switch (var1) {
         case U, Z -> "bell";
         case y -> "x";
         case K -> "badge-check";
      };
   }

   private static int U(String var0, hg.u var1) {
      hg.q var2 = x(var0);
      if (var2 != hg.q.y) {
         return var2 == hg.q.N ? rv.e : -6908266;
      }

      return switch (var1) {
         case U, K -> rv.e;
         case Z -> -996986;
         case y -> -6908266;
      };
   }

   public static void x(GuiGraphicsExtractor var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = w("Enabled", "Kill Aura");
      c(var0, K(var1, var3.getWindow().getGuiScaledWidth(), var4), var2, var4, "Enabled", "Kill Aura", hg.u.U, 1.0);
   }

   private static void c(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, hg.u var6, double var7) {
      fr.S(var0, var1, var2, var3, 30.0F, 10.0F, oh.S(-603321844, var7));
      rd.Z(var0, z(var4, var6), var1 + 12, var2 + 7, 16, oh.S(U(var4, var6), var7));
      rd.Z(var0, "chevron-right", var1 + 30, var2 + 5, 20, oh.S(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         rd.v(var0, rd.R(var5, var9, 12.0), var1 + 52, var2 + h, 12.0, oh.S(-1, var7));
      } else {
         String var10 = rd.R(var4, var9, 12.0);
         rd.v(var0, var10, var1 + 52, var2 + h, 12.0, oh.S(-1, var7));
         if (!var5.isBlank()) {
            int var11 = rd.H(var10, 12.0) + 5;
            rd.v(var0, rd.R(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + h, 12.0, oh.S(rv.e, var7));
         }
      }
   }

   private enum q {
      y,
      N,
      F;
   }

   private static final class s {
      final String J;
      final String X;
      final hg.u V;
      final long L;
      long H = System.nanoTime();
      boolean r;
      final u7 q = new u7(0.0);
      final u7 K = new u7(0.0);
      final u7 M = new u7(0.0);

      s(String var1, String var2, hg.u var3, long var4) {
         this.J = var1 == null ? "" : var1;
         this.X = var2 == null ? "" : var2;
         this.V = var3;
         this.L = var4;
      }

      long t() {
         return (System.nanoTime() - this.H) / 1000000L;
      }

      void o() {
         this.H = Math.min(this.H, System.nanoTime() - this.L * 1000000L);
      }
   }

   public enum u {
      U,
      Z,
      y,
      K;
   }
}
