package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class d6 {
   private static final aa u = new aa();
   private static final x.c[] z = new x.c[3];
   private static final Vec3[] p = new Vec3[3];
   private static Object r;
   private static Object W;
   private static Vec3 I;
   private static Vec3 F;
   private static double X;
   private static double e;
   private static double J;
   private static double G;
   private static double q;
   private static double c;
   private static int S;
   private static int s;
   private static boolean g;
   private static boolean l;

   public static void N() {
      u.C();
      W = null;
      r = null;
      F = null;
      I = null;
      g = (boolean)0;
   }

   public static void Z(float var0) {
      Minecraft var1 = Minecraft.getInstance();
      SelfVisualsModule var2 = SelfVisualsModule.v();
      LocalPlayer var3 = var1.player;
      if (var2 != null && var2.g() && var2.k.w("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()) {
         Vec3 var4 = var3.position();
         int var5 = r == var3 && W == var1.level && (I == null || !(I.distanceToSqr(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            N();
         }

         r = var3;
         W = var1.level;
         I = var4;
         F = var3.getPosition(var0).distanceToSqr(var4) > 64.0 ? var4 : var3.getPosition(var0);
         e = var3.getBbHeight();
         J = aa.x(var2.W.getValue(), e);
         G = var2.o.getValue();
         q = var2.q.getValue();
         l = var2.A.getValue();
         S = var2.C.getValue() ? com.larpclient.c.y : var2.y.getValue();
         s = var2.C.getValue() ? com.larpclient.c.t : var2.D.getValue();
         X = hz.Q();
         double var6 = Math.max(var2.b.getValue(), var3.getBbWidth() * 0.71 + J);
         u.M(X, var2.f.getValue(), var6, G);
         c = u.y(X);
         Vec3 var8 = var1.gameRenderer.mainCamera().position();

         for (int var9 = 0; var9 < 3; var9++) {
            x.c var10 = z[var9];
            var10.w();
            Vec3 var11 = p[var9] = aa.K(u.j(var9), F, e, J);

            for (int var12 = 0; var12 <= u.F(); var12++) {
               Vec3 var13 = var12 == u.F() ? var11 : aa.K(u.n(var9, var12), F, e, J);
               double var14 = var12 == u.F() ? 1.0 : Math.clamp(1.0 - (X - u.x(var12)) / G, 0.0, 1.0);
               var10.J(var13, J * 1.4 * var14 * var14, vv.O(vv.Q(s, S, var14), var14 * var14 * q * c));
            }

            var10.v();
            var10.T(var8);
         }

         g = (boolean)1;
      } else {
         N();
      }
   }

   public static boolean F() {
      return g && l;
   }

   public static void G(fz var0, boolean var1) {
      if (g && (!var1 || l)) {
         var0.blend(_0.v.c);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3 var3 = p[var2];
            var0.n(z[var2], true);
            var0.a(var3.x, var3.y, var3.z, J * 2.2, 0.0, vv.O(S, 0.35 * q * c), l.b.P, 0.0, 0.0, 0.0);
            var0.a(var3.x, var3.y, var3.z, J, 0.0, vv.O(S, 0.9 * c), l.b.y, 0.0, 0.0, 0.0);
            var0.a(var3.x, var3.y, var3.z, J * 0.55, 0.0, vv.O(vv.Q(S, S & 0xFF000000 | 16777215, 0.8), c), l.b.P, 0.0, 0.0, 0.0);
         }

         var0.blend(_0.v.z);
      }
   }

   private d6() {
   }

   static {
      for (int var0 = 0; var0 < z.length; var0++) {
         z[var0] = new x.c(257);
      }
   }
}
