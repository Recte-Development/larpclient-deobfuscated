package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class l_ {
   private static final hr l = new hr();
   private static final u_.p[] B = new u_.p[3];
   private static final Vec3[] u = new Vec3[3];
   private static Object g;
   private static Object Q;
   private static Vec3 G;
   private static Vec3 V;
   private static double L;
   private static double K;
   private static double M;
   private static double h;
   private static double y;
   private static double T;
   private static int P;
   private static int J;
   private static boolean o;
   private static boolean I;

   public static void f() {
      l.J();
      Q = null;
      g = null;
      V = null;
      G = null;
      o = (boolean)0;
   }

   public static void D(float var0) {
      Minecraft var1 = Minecraft.getInstance();
      SelfVisualsModule var2 = SelfVisualsModule.k();
      LocalPlayer var3 = var1.player;
      if (var2 != null && var2.S() && var2.n.O("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()) {
         Vec3 var4 = var3.position();
         int var5 = g == var3 && Q == var1.level && (G == null || !(G.distanceToSqr(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            f();
         }

         g = var3;
         Q = var1.level;
         G = var4;
         V = var3.getPosition(var0).distanceToSqr(var4) > 64.0 ? var4 : var3.getPosition(var0);
         K = var3.getBbHeight();
         M = hr.a(var2.G.getValue(), K);
         h = var2.m.getValue();
         y = var2.l.getValue();
         I = var2.s.getValue();
         P = var2.N.getValue() ? ho.J : var2.g.getValue();
         J = var2.N.getValue() ? ho.ds : var2.k.getValue();
         L = cf.F();
         double var6 = Math.max(var2.S.getValue(), var3.getBbWidth() * 0.71 + M);
         l.B(L, var2.X.getValue(), var6, h);
         T = l.O(L);
         Vec3 var8 = var1.gameRenderer.mainCamera().position();

         for (int var9 = 0; var9 < 3; var9++) {
            u_.p var10 = B[var9];
            var10.q();
            Vec3 var11 = u[var9] = hr.C(l.M(var9), V, K, M);

            for (int var12 = 0; var12 <= l.D(); var12++) {
               Vec3 var13 = var12 == l.D() ? var11 : hr.C(l.U(var9, var12), V, K, M);
               double var14 = var12 == l.D() ? 1.0 : Math.clamp(1.0 - (L - l.o(var12)) / h, 0.0, 1.0);
               var10.n(var13, M * 1.4 * var14 * var14, uh.F(uh.q(J, P, var14), var14 * var14 * y * T));
            }

            var10.O();
            var10.x(var8);
         }

         o = (boolean)1;
      } else {
         f();
      }
   }

   public static boolean A() {
      return o && I;
   }

   public static void i(kj var0, boolean var1) {
      if (o && (!var1 || I)) {
         var0.blend(gl.c.E);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3 var3 = u[var2];
            var0.b(B[var2], true);
            var0.I(var3.x, var3.y, var3.z, M * 2.2, 0.0, uh.F(P, 0.35 * y * T), c3.a.Z, 0.0, 0.0, 0.0);
            var0.I(var3.x, var3.y, var3.z, M, 0.0, uh.F(P, 0.9 * T), c3.a.l, 0.0, 0.0, 0.0);
            var0.I(var3.x, var3.y, var3.z, M * 0.55, 0.0, uh.F(uh.q(P, P & 0xFF000000 | 16777215, 0.8), T), c3.a.Z, 0.0, 0.0, 0.0);
         }

         var0.blend(gl.c.K);
      }
   }

   private l_() {
   }

   static {
      for (int var0 = 0; var0 < B.length; var0++) {
         B[var0] = new u_.p(257);
      }
   }
}
