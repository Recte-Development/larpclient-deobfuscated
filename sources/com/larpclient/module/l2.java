package com.larpclient.module;

import com.larpclient.m9;
import com.larpclient.oh;
import com.larpclient.rv;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class l2 {
   private static final int A = 64;
   private static final float Q = 3.5F;
   private static final double x = 0.06;
   private static final double U = 0.07;
   private static Object K;
   private static long a = Long.MIN_VALUE;
   private static double h;
   private static double s = Double.NaN;

   public static void K() {
      LevelRenderEvents.BEFORE_GIZMOS.register(l2::m);
   }

   private static void m(LevelRenderContext var0) {
      MaceModule var1 = MaceModule.Q();
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null
         && var1.isRuntimeActive()
         && var2.player != null
         && var2.level != null
         && var2.gui.screen() == null
         && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         float var3 = var2.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         T(var1, var2, var3);
         V(var1, var2.player, var3);
      }
   }

   private static void T(MaceModule var0, Minecraft var1, float var2) {
      LocalPlayer var3 = var1.player;
      long var4 = var1.level.getGameTime();
      if (K != var1.level || a != var4) {
         K = var1.level;
         a = var4;
         h = var0.S(var3);
         s = h > 0.0 ? E(var1, var3, h) : Double.NaN;
      }

      g(var3, h, s, var2);
   }

   private static void g(LocalPlayer var0, double var1, double var3, float var5) {
      if (!(var1 <= 0.0) && !Double.isNaN(var3)) {
         Vec3 var6 = var0.getPosition(var5);
         double var7 = m9.g(var1, var6.y + var0.getEyeHeight(), var3);
         if (!(var7 <= 0.0)) {
            Vec3 var9 = new Vec3(var6.x, var3 + 0.06, var6.z);
            int var10 = oh.j(rv.e, -1, 0.3) | 0xFF000000;

            for (int var11 = 0; var11 < 64; var11++) {
               Gizmos.line(O(var9, var11, var7), O(var9, var11 + 1, var7), var10, 3.5F);
            }
         }
      }
   }

   private static double E(Minecraft var0, LocalPlayer var1, double var2) {
      Vec3 var4 = var1.position().add(0.0, 0.05, 0.0);
      Vec3 var5 = var1.getEyePosition().subtract(0.0, 1.8 + var2, 0.0);
      BlockHitResult var6 = var0.level.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.NONE, var1));
      return var6.getType() == Type.BLOCK ? var6.getLocation().y : Double.NaN;
   }

   private static void V(MaceModule var0, LocalPlayer var1, float var2) {
      LivingEntity var3 = var0.j();
      if (var3 != null) {
         E(var1, var3, var2);
      }
   }

   private static void E(LocalPlayer var0, LivingEntity var1, float var2) {
      AABB var3 = var1.getBoundingBox().move(var1.getPosition(var2).subtract(var1.position()));
      Vec3 var4 = var0.getEyePosition(var2);
      Vec3 var5 = var0.getViewVector(var2);
      Vec3 var6 = var4.add(var5.scale(var4.distanceTo(var3.getCenter()) + var3.getSize()));
      Optional var7 = var3.inflate(var1.getPickRadius()).clip(var4, var6);
      int var8 = (var7.isPresent() ? oh.j(rv.e, -8327246, 0.35) : oh.j(rv.e, -12672, 0.5)) | 0xFF000000;
      Gizmos.cuboid(var3, GizmoStyle.stroke(var8, 2.0F)).setAlwaysOnTop();
      Vec3 var9 = var7.orElseGet(() -> u(var4, var5, var3.getCenter()));
      Gizmos.cuboid(new AABB(var9, var9).inflate(0.07), GizmoStyle.fill(var8)).setAlwaysOnTop();
   }

   private static Vec3 u(Vec3 var0, Vec3 var1, Vec3 var2) {
      return var0.add(var1.scale(Math.max(0.0, var2.subtract(var0).dot(var1))));
   }

   private static Vec3 O(Vec3 var0, int var1, double var2) {
      double var4 = (Math.PI * 2) * var1 / 64.0;
      return var0.add(Math.cos(var4) * var2, 0.0, Math.sin(var4) * var2);
   }

   private l2() {
   }
}
