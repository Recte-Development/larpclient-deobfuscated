package com.larpclient.module;

import com.larpclient.c3;
import com.larpclient.cf;
import com.larpclient.gl;
import com.larpclient.ho;
import com.larpclient.kj;
import com.larpclient.ml;
import com.larpclient.uh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class k {
   private static Object i;
   private static int U;
   private static long t = Long.MIN_VALUE;
   private static AABB L;
   private static double a;
   private static double M;
   private static boolean Y;
   private static ml.z s;
   private static int z;
   private static Vec3 u;

   public static void D() {
      LevelRenderEvents.BEFORE_GIZMOS.register(k::j);
   }

   private static void j(LevelRenderContext var0) {
      AuraModule var1 = AuraModule.V();
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null
         && var1.isOperational()
         && var2.player != null
         && var2.gui.screen() == null
         && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         float var3 = var2.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         if (var1.x()) {
            Vec3 var4 = var2.player.getPosition(var3).add(0.0, 0.12, 0.0);
            double var5 = var2.player.getYRot() - var1.b() / 2.0;
            int var7 = Math.max(8, (int)Math.ceil(var1.b() / 6.0));

            for (int var8 = 0; var8 < var7; var8++) {
               Z(var1, Z(var4, var5 + var1.b() * var8 / var7, var1.d()), Z(var4, var5 + var1.b() * (var8 + 1) / var7, var1.d()), uh.q(ho.J, -1, 0.3), var1.o());
            }

            if (var1.b() < 360.0) {
               Z(var1, var4, Z(var4, var5, var1.d()), uh.q(ho.J, -1, 0.3), var1.o());
               Z(var1, var4, Z(var4, var5 + var1.b(), var1.d()), uh.q(ho.J, -1, 0.3), var1.o());
            }
         }
      }
   }

   public static void N() {
      AuraModule var0 = AuraModule.V();
      Minecraft var1 = Minecraft.getInstance();
      if (var0 != null
         && var0.k()
         && var1.level != null
         && var1.player != null
         && var1.gui.screen() == null
         && !var1.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         l(var0, var1);
         String var2 = var0.G();
         int var3 = var2.equals("Valid attack ray")
            ? uh.q(ho.J, -8327246, 0.35)
            : (var2.equals("Outside reach") ? uh.q(ho.J, -1277295, 0.55) : uh.q(ho.J, -12672, 0.5));
         z = z == 0 ? var3 : uh.q(z, var3, 1.0 - Math.exp(-cf.v() * 9.0));
      } else {
         s = null;
         i = null;
         z = 0;
      }
   }

   private static void l(AuraModule var0, Minecraft var1) {
      LivingEntity var2 = var0.u();
      AABB var3 = var2.getBoundingBox();
      double var4 = var0.d();
      double var6 = var1.player.getEyeHeight();
      long var8 = var1.level.getGameTime();
      if (s == null || i != var1.level || U != var2.getId() || t != var8 || !var3.equals(L) || a != var4 || M != var6 || Y != var0.F()) {
         HashMap var10 = new HashMap();
         double var11 = var2.getY();
         CollisionContext var13 = CollisionContext.of(var1.player);
         ml.r var14 = (var5, var7) -> {
            int var9 = (int)Math.floor(var5);
            int var10x = (int)Math.floor(var7);
            long var11x = BlockPos.asLong(var9, 0, var10x);
            List var13x = var10.computeIfAbsent(var11x, var6x -> {
               ArrayList var7x = new ArrayList();

               for (int var8x = (int)Math.floor(var11 - 2.5); var8x <= Math.ceil(var11 + 3.1); var8x++) {
                  BlockPos var9x = new BlockPos(var9, var8x, var10x);
                  if (var1.level.hasChunkAt(var9x)) {
                     for (AABB var11xx : var1.level.getBlockState(var9x).getCollisionShape(var1.level, var9x, var13).toAabbs()) {
                        var7x.add(var11xx.move(var9, var8x, var10x));
                     }
                  }
               }

               return var7x;
            });
            return ml.P(var13x, var5, var7, var11);
         };
         s = ml.u(var3, var4, var6, var14, var0.F());
         if (i != var1.level || U != var2.getId()) {
            z = 0;
         }

         i = var1.level;
         U = var2.getId();
         t = var8;
         L = var3;
         a = var4;
         M = var6;
         Y = var0.F();
      }

      u = var3.getCenter();
   }

   public static boolean E() {
      AuraModule var0 = AuraModule.V();
      return s != null && !s.outline().isEmpty() && var0 != null && var0.z();
   }

   public static void h(kj var0, boolean var1) {
      AuraModule var2 = AuraModule.V();
      if (s != null && var2 != null && (!var1 || var2.z())) {
         double var3 = var2.i() * (var1 ? 0.65 : 1.0);
         double var5 = var2.J() * 0.035;
         double var7 = var2.u() ? cf.F() : 0.0;
         var0.blend(gl.c.E);

         for (ml.u var10 : s.outline()) {
            Vec3 var11 = var10.from();
            Vec3 var12 = var10.to();
            int var13 = k(var11, var7, var3, var2.u());
            int var14 = k(var12, var7, var3, var2.u());
            var0.W(var11.x, var11.y, var11.z, var12.x, var12.y, var12.z, var5, var5, var13, var14);
         }

         if (var2.u()) {
            for (int var42 = 0; var42 < 3; var42++) {
               double var44 = var7 * 0.55 + var42 * Math.PI * 2.0 / 3.0;
               double var47 = Math.cos(var44);
               double var48 = Math.sin(var44);
               double var16 = Double.POSITIVE_INFINITY;
               Vec3 var18 = null;

               for (ml.u var20 : s.outline()) {
                  Vec3 var21 = var20.from();
                  Vec3 var22 = var20.to();
                  double var23 = var21.x - u.x;
                  double var25 = var21.z - u.z;
                  double var27 = var22.x - var21.x;
                  double var29 = var22.z - var21.z;
                  double var31 = var27 * var48 - var29 * var47;
                  double var33 = Math.abs(var31) < 1.0E-8 ? 0.5 : Math.clamp((var25 * var47 - var23 * var48) / var31, 0.0, 1.0);
                  Vec3 var35 = var21.lerp(var22, var33);
                  double var36 = var35.x - u.x;
                  double var38 = var35.z - u.z;
                  if (!(var36 * var47 + var38 * var48 <= 0.0)) {
                     double var40 = Math.abs(var36 * var48 - var38 * var47);
                     if (var40 < var16) {
                        var16 = var40;
                        var18 = var35;
                     }
                  }
               }

               if (var18 != null && var16 < 0.08) {
                  int var49 = uh.F(uh.q(z, -1, 0.65), var3);
                  var0.I(var18.x, var18.y + 0.018, var18.z, var5 * 1.6, var7 * 0.6, uh.F(var49, 0.45), c3.a.Z, 0.0, 0.0, 0.0);
                  var0.P(var18.x, var18.y + 0.018, var18.z, var5 * 0.6, var7 * 0.6, var49);
               }
            }
         }

         var0.blend(gl.c.K);
         if (!var1) {
            int var43 = uh.F(z, var3 * 0.065);

            for (ml.e var46 : s.fill()) {
               y(var0, var46.a(), var43);
               y(var0, var46.b(), var43);
               y(var0, var46.c(), var43);
               y(var0, var46.c(), var43);
            }
         }
      }
   }

   private static void y(kj var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int k(Vec3 var0, double var1, double var3, boolean var5) {
      double var6 = Math.atan2(var0.z - u.z, var0.x - u.x);
      double var8 = var5 ? Math.pow(0.5 + 0.5 * Math.cos(var6 * 3.0 - var1 * 1.65), 12.0) : 0.0;
      return uh.F(uh.q(z, -1, 0.15 + var8 * 0.55), var3 * (0.65 + var8 * 0.35));
   }

   private static void Z(AuraModule var0, Vec3 var1, Vec3 var2, int var3, float var4) {
      GizmoProperties var5 = Gizmos.line(var1, var2, var3 | 0xFF000000, var4);
      if (var0.A()) {
         var5.setAlwaysOnTop();
      }
   }

   private static Vec3 Z(Vec3 var0, double var1, double var3) {
      double var5 = Math.toRadians(var1);
      return var0.add(-Math.sin(var5) * var3, 0.0, Math.cos(var5) * var3);
   }

   private k() {
   }
}
