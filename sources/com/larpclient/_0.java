package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class _0 {
   private static final ew y = new ew();
   private static final h3.b[] U = new h3.b[3];
   private static final Vec3[] Q = new Vec3[3];
   private static Object L;
   private static Object G;
   private static Vec3 X;
   private static Vec3 A;
   private static double r;
   private static double s;
   private static double a;
   private static double t;
   private static double j;
   private static double F;
   private static int b;
   private static int C;
   private static boolean c;
   private static boolean m;

   public static void q() {
      y.H();
      G = null;
      L = null;
      A = null;
      X = null;
      c = (boolean)0;
   }

   public static void H(float var0) {
      Minecraft var1 = Minecraft.getInstance();
      SelfVisualsModule var2 = SelfVisualsModule.f();
      LocalPlayer var3 = var1.player;
      if (var2 != null && var2.b() && var2.o.i("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()) {
         Vec3 var4 = var3.position();
         int var5 = L == var3 && G == var1.level && (X == null || !(X.distanceToSqr(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            q();
         }

         L = var3;
         G = var1.level;
         X = var4;
         A = var3.getPosition(var0).distanceToSqr(var4) > 64.0 ? var4 : var3.getPosition(var0);
         s = var3.getBbHeight();
         a = ew.c(var2.y.getValue(), s);
         t = var2.W.getValue();
         j = var2.Q.getValue();
         m = var2.N.getValue();
         b = var2.D.getValue() ? rv.e : var2.K.getValue();
         C = var2.D.getValue() ? rv.GQ : var2.u.getValue();
         r = ea.y();
         double var6 = Math.max(var2.U.getValue(), var3.getBbWidth() * 0.71 + a);
         y.X(r, var2.i.getValue(), var6, t);
         F = y.b(r);
         Vec3 var8 = var1.gameRenderer.mainCamera().position();

         for (int var9 = 0; var9 < 3; var9++) {
            h3.b var10 = U[var9];
            var10.H();
            Vec3 var11 = Q[var9] = ew.k(y.r(var9), A, s, a);

            for (int var12 = 0; var12 <= y.Y(); var12++) {
               Vec3 var13 = var12 == y.Y() ? var11 : ew.k(y.u(var9, var12), A, s, a);
               double var14 = var12 == y.Y() ? 1.0 : Math.clamp(1.0 - (r - y.h(var12)) / t, 0.0, 1.0);
               var10.P(var13, a * 1.4 * var14 * var14, oh.S(oh.j(C, b, var14), var14 * var14 * j * F));
            }

            var10.W();
            var10.n(var8);
         }

         c = (boolean)1;
      } else {
         q();
      }
   }

   public static boolean L() {
      return c && m;
   }

   public static void I(r6 var0, boolean var1) {
      if (c && (!var1 || m)) {
         var0.blend(fk.h.u);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3 var3 = Q[var2];
            var0.y(U[var2], true);
            var0.j(var3.x, var3.y, var3.z, a * 2.2, 0.0, oh.S(b, 0.35 * j * F), f4.a.u, 0.0, 0.0, 0.0);
            var0.j(var3.x, var3.y, var3.z, a, 0.0, oh.S(b, 0.9 * F), f4.a.D, 0.0, 0.0, 0.0);
            var0.j(var3.x, var3.y, var3.z, a * 0.55, 0.0, oh.S(oh.j(b, b & 0xFF000000 | 16777215, 0.8), F), f4.a.u, 0.0, 0.0, 0.0);
         }

         var0.blend(fk.h.z);
      }
   }

   private _0() {
   }

   static {
      for (int var0 = 0; var0 < U.length; var0++) {
         U[var0] = new h3.b(257);
      }
   }
}
