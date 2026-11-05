package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class mu {
   private static final xi m = new xi();
   private static final __.l[] O = new __.l[3];
   private static final Vec3[] u = new Vec3[3];
   private static Object l;
   private static Object P;
   private static Vec3 h;
   private static Vec3 q;
   private static double E;
   private static double Z;
   private static double K;
   private static double A;
   private static double V;
   private static double Q;
   private static int S;
   private static int n;
   private static boolean p;
   private static boolean F;

   public static void s() {
      m.i();
      P = null;
      l = null;
      q = null;
      h = null;
      p = (boolean)0;
   }

   public static void s(float var0) {
      Minecraft var1 = Minecraft.getInstance();
      SelfVisualsModule var2 = SelfVisualsModule.V();
      LocalPlayer var3 = var1.player;
      if (var2 != null && var2.M() && var2.w.S("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()) {
         Vec3 var4 = var3.position();
         int var5 = l == var3 && P == var1.level && (h == null || !(h.distanceToSqr(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            s();
         }

         l = var3;
         P = var1.level;
         h = var4;
         q = var3.getPosition(var0).distanceToSqr(var4) > 64.0 ? var4 : var3.getPosition(var0);
         Z = var3.getBbHeight();
         K = xi.f(var2.X.P(), Z);
         A = var2.L.P();
         V = var2.Z.P();
         F = var2.e.P();
         S = var2.T.P() ? f.C : var2.v.P();
         n = var2.T.P() ? f.O : var2.a.P();
         E = ty.K();
         double var6 = Math.max(var2.n.P(), var3.getBbWidth() * 0.71 + K);
         m.O(E, var2.h.P(), var6, A);
         Q = m.s(E);
         Vec3 var8 = var1.gameRenderer.mainCamera().position();

         for (int var9 = 0; var9 < 3; var9++) {
            __.l var10 = O[var9];
            var10.y();
            Vec3 var11 = u[var9] = xi.U(m.K(var9), q, Z, K);

            for (int var12 = 0; var12 <= m.V(); var12++) {
               Vec3 var13 = var12 == m.V() ? var11 : xi.U(m.f(var9, var12), q, Z, K);
               double var14 = var12 == m.V() ? 1.0 : Math.clamp(1.0 - (E - m.K(var12)) / A, 0.0, 1.0);
               var10.y(var13, K * 1.4 * var14 * var14, _3.U(_3.D(n, S, var14), var14 * var14 * V * Q));
            }

            var10.F();
            var10.a(var8);
         }

         p = (boolean)1;
      } else {
         s();
      }
   }

   public static boolean b() {
      return p && F;
   }

   public static void b(ki var0, boolean var1) {
      if (p && (!var1 || F)) {
         var0.blend(pf.x.E);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3 var3 = u[var2];
            var0.M(O[var2], true);
            var0.W(var3.x, var3.y, var3.z, K * 2.2, 0.0, _3.U(S, 0.35 * V * Q), cg.u.a, 0.0, 0.0, 0.0);
            var0.W(var3.x, var3.y, var3.z, K, 0.0, _3.U(S, 0.9 * Q), cg.u.n, 0.0, 0.0, 0.0);
            var0.W(var3.x, var3.y, var3.z, K * 0.55, 0.0, _3.U(_3.D(S, S & 0xFF000000 | 16777215, 0.8), Q), cg.u.a, 0.0, 0.0, 0.0);
         }

         var0.blend(pf.x.S);
      }
   }

   private mu() {
   }

   static {
      for (int var0 = 0; var0 < O.length; var0++) {
         O[var0] = new __.l(257);
      }
   }
}
