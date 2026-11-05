package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.cg;
import com.larpclient.ki;
import com.larpclient.pf;
import com.larpclient.tw;
import com.larpclient.ty;
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

public final class d4 {
   private static Object p;
   private static int y;
   private static long U = Long.MIN_VALUE;
   private static AABB V;
   private static double k;
   private static double T;
   private static boolean h;
   private static tw.s G;
   private static int M;
   private static Vec3 m;

   public static void F() {
      LevelRenderEvents.BEFORE_GIZMOS.register(d4::J);
   }

   private static void J(LevelRenderContext var0) {
      AuraModule var1 = AuraModule.S();
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null
         && var1.isOperational()
         && var2.player != null
         && var2.gui.screen() == null
         && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         float var3 = var2.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         if (var1.G()) {
            Vec3 var4 = var2.player.getPosition(var3).add(0.0, 0.12, 0.0);
            double var5 = var2.player.getYRot() - var1.K() / 2.0;
            int var7 = Math.max(8, (int)Math.ceil(var1.K() / 6.0));

            for (int var8 = 0; var8 < var7; var8++) {
               I(
                  var1,
                  o(var4, var5 + var1.K() * var8 / var7, var1.l()),
                  o(var4, var5 + var1.K() * (var8 + 1) / var7, var1.l()),
                  _3.D(com.larpclient.f.C, -1, 0.3),
                  var1.q()
               );
            }

            if (var1.K() < 360.0) {
               I(var1, var4, o(var4, var5, var1.l()), _3.D(com.larpclient.f.C, -1, 0.3), var1.q());
               I(var1, var4, o(var4, var5 + var1.K(), var1.l()), _3.D(com.larpclient.f.C, -1, 0.3), var1.q());
            }
         }
      }
   }

   public static void O() {
      AuraModule var0 = AuraModule.S();
      Minecraft var1 = Minecraft.getInstance();
      if (var0 != null
         && var0.e()
         && var1.level != null
         && var1.player != null
         && var1.gui.screen() == null
         && !var1.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         G(var0, var1);
         String var2 = var0.B();
         int var3 = var2.equals("Valid attack ray")
            ? _3.D(com.larpclient.f.C, -8327246, 0.35)
            : (var2.equals("Outside reach") ? _3.D(com.larpclient.f.C, -1277295, 0.55) : _3.D(com.larpclient.f.C, -12672, 0.5));
         M = M == 0 ? var3 : _3.D(M, var3, 1.0 - Math.exp(-ty.N() * 9.0));
      } else {
         G = null;
         p = null;
         M = 0;
      }
   }

   private static void G(AuraModule var0, Minecraft var1) {
      LivingEntity var2 = var0.l();
      AABB var3 = var2.getBoundingBox();
      double var4 = var0.l();
      double var6 = var1.player.getEyeHeight();
      long var8 = var1.level.getGameTime();
      if (G == null || p != var1.level || y != var2.getId() || U != var8 || !var3.equals(V) || k != var4 || T != var6 || h != var0.g()) {
         HashMap var10 = new HashMap();
         double var11 = var2.getY();
         CollisionContext var13 = CollisionContext.of(var1.player);
         tw.t var14 = (var5, var7) -> {
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
            return tw.c(var13x, var5, var7, var11);
         };
         G = tw.f(var3, var4, var6, var14, var0.g());
         if (p != var1.level || y != var2.getId()) {
            M = 0;
         }

         p = var1.level;
         y = var2.getId();
         U = var8;
         V = var3;
         k = var4;
         T = var6;
         h = var0.g();
      }

      m = var3.getCenter();
   }

   public static boolean Y() {
      AuraModule var0 = AuraModule.S();
      return G != null && !G.outline().isEmpty() && var0 != null && var0.A();
   }

   public static void T(ki var0, boolean var1) {
      AuraModule var2 = AuraModule.S();
      if (G != null && var2 != null && (!var1 || var2.A())) {
         double var3 = var2.i() * (var1 ? 0.65 : 1.0);
         double var5 = var2.S() * 0.035;
         double var7 = var2.t() ? ty.K() : 0.0;
         var0.blend(pf.x.E);

         for (tw.o var10 : G.outline()) {
            Vec3 var11 = var10.from();
            Vec3 var12 = var10.to();
            int var13 = E(var11, var7, var3, var2.t());
            int var14 = E(var12, var7, var3, var2.t());
            var0.R(var11.x, var11.y, var11.z, var12.x, var12.y, var12.z, var5, var5, var13, var14);
         }

         if (var2.t()) {
            for (int var42 = 0; var42 < 3; var42++) {
               double var44 = var7 * 0.55 + var42 * Math.PI * 2.0 / 3.0;
               double var47 = Math.cos(var44);
               double var48 = Math.sin(var44);
               double var16 = Double.POSITIVE_INFINITY;
               Vec3 var18 = null;

               for (tw.o var20 : G.outline()) {
                  Vec3 var21 = var20.from();
                  Vec3 var22 = var20.to();
                  double var23 = var21.x - m.x;
                  double var25 = var21.z - m.z;
                  double var27 = var22.x - var21.x;
                  double var29 = var22.z - var21.z;
                  double var31 = var27 * var48 - var29 * var47;
                  double var33 = Math.abs(var31) < 1.0E-8 ? 0.5 : Math.clamp((var25 * var47 - var23 * var48) / var31, 0.0, 1.0);
                  Vec3 var35 = var21.lerp(var22, var33);
                  double var36 = var35.x - m.x;
                  double var38 = var35.z - m.z;
                  if (!(var36 * var47 + var38 * var48 <= 0.0)) {
                     double var40 = Math.abs(var36 * var48 - var38 * var47);
                     if (var40 < var16) {
                        var16 = var40;
                        var18 = var35;
                     }
                  }
               }

               if (var18 != null && var16 < 0.08) {
                  int var49 = _3.U(_3.D(M, -1, 0.65), var3);
                  var0.W(var18.x, var18.y + 0.018, var18.z, var5 * 1.6, var7 * 0.6, _3.U(var49, 0.45), cg.u.a, 0.0, 0.0, 0.0);
                  var0.t(var18.x, var18.y + 0.018, var18.z, var5 * 0.6, var7 * 0.6, var49);
               }
            }
         }

         var0.blend(pf.x.S);
         if (!var1) {
            int var43 = _3.U(M, var3 * 0.065);

            for (tw.x var46 : G.fill()) {
               p(var0, var46.a(), var43);
               p(var0, var46.b(), var43);
               p(var0, var46.c(), var43);
               p(var0, var46.c(), var43);
            }
         }
      }
   }

   private static void p(ki var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int E(Vec3 var0, double var1, double var3, boolean var5) {
      double var6 = Math.atan2(var0.z - m.z, var0.x - m.x);
      double var8 = var5 ? Math.pow(0.5 + 0.5 * Math.cos(var6 * 3.0 - var1 * 1.65), 12.0) : 0.0;
      return _3.U(_3.D(M, -1, 0.15 + var8 * 0.55), var3 * (0.65 + var8 * 0.35));
   }

   private static void I(AuraModule var0, Vec3 var1, Vec3 var2, int var3, float var4) {
      GizmoProperties var5 = Gizmos.line(var1, var2, var3 | 0xFF000000, var4);
      if (var0.F()) {
         var5.setAlwaysOnTop();
      }
   }

   private static Vec3 o(Vec3 var0, double var1, double var3) {
      double var5 = Math.toRadians(var1);
      return var0.add(-Math.sin(var5) * var3, 0.0, Math.cos(var5) * var3);
   }

   private d4() {
   }
}
