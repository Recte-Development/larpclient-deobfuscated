package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.util.math.MaceMath;
import com.larpclient.util.render.ColorUtil;
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

public final class MaceRenderer {
   private static final int rING_SEGMENTS = 64;
   private static final float rING_LINE_WIDTH = 3.5F;
   private static final double rING_HEIGHT_OFFSET = 0.06;
   private static final double hIT_POINT_HALF_SIZE = 0.07;
   private static Object cachedLevel;
   private static long cachedGameTime = Long.MIN_VALUE;
   private static double fallHeight;
   private static double groundY = Double.NaN;

   public static void register() {
      LevelRenderEvents.BEFORE_GIZMOS.register(MaceRenderer::onRender);
   }

   private static void onRender(LevelRenderContext var0) {
      MaceModule var1 = MaceModule.getInstance();
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null
         && var1.isRuntimeActive()
         && var2.player != null
         && var2.level != null
         && var2.gui.screen() == null
         && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         float var3 = var2.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         drawLandingRing(var1, var2, var3);
         drawTargetHitbox(var1, var2.player, var3);
      }
   }

   private static void drawLandingRing(MaceModule var0, Minecraft var1, float var2) {
      LocalPlayer var3 = var1.player;
      long var4 = var1.level.getGameTime();
      if (cachedLevel != var1.level || cachedGameTime != var4) {
         cachedLevel = var1.level;
         cachedGameTime = var4;
         fallHeight = var0.getRangeRingRadius(var3);
         groundY = fallHeight > 0.0 ? findGroundY(var1, var3, fallHeight) : Double.NaN;
      }

      drawLandingRing(var3, fallHeight, groundY, var2);
   }

   private static void drawLandingRing(LocalPlayer var0, double var1, double var3, float var5) {
      if (!(var1 <= 0.0) && !Double.isNaN(var3)) {
         Vec3 var6 = var0.getPosition(var5);
         double var7 = MaceMath.horizontalReachAtHeight(var1, var6.y + var0.getEyeHeight(), var3);
         if (!(var7 <= 0.0)) {
            Vec3 var9 = new Vec3(var6.x, var3 + 0.06, var6.z);
            int var10 = ColorUtil.lerpColor(Theme.accent, -1, 0.3) | 0xFF000000;

            for (int var11 = 0; var11 < 64; var11++) {
               Gizmos.line(ringPoint(var9, var11, var7), ringPoint(var9, var11 + 1, var7), var10, 3.5F);
            }
         }
      }
   }

   private static double findGroundY(Minecraft var0, LocalPlayer var1, double var2) {
      Vec3 var4 = var1.position().add(0.0, 0.05, 0.0);
      Vec3 var5 = var1.getEyePosition().subtract(0.0, 1.8 + var2, 0.0);
      BlockHitResult var6 = var0.level.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.NONE, var1));
      return var6.getType() == Type.BLOCK ? var6.getLocation().y : Double.NaN;
   }

   private static void drawTargetHitbox(MaceModule var0, LocalPlayer var1, float var2) {
      LivingEntity var3 = var0.getTargetMarkEntity();
      if (var3 != null) {
         drawHitboxAndAimPoint(var1, var3, var2);
      }
   }

   private static void drawHitboxAndAimPoint(LocalPlayer var0, LivingEntity var1, float var2) {
      AABB var3 = var1.getBoundingBox().move(var1.getPosition(var2).subtract(var1.position()));
      Vec3 var4 = var0.getEyePosition(var2);
      Vec3 var5 = var0.getViewVector(var2);
      Vec3 var6 = var4.add(var5.scale(var4.distanceTo(var3.getCenter()) + var3.getSize()));
      Optional var7 = var3.inflate(var1.getPickRadius()).clip(var4, var6);
      int var8 = (var7.isPresent() ? ColorUtil.lerpColor(Theme.accent, -8327246, 0.35) : ColorUtil.lerpColor(Theme.accent, -12672, 0.5)) | 0xFF000000;
      Gizmos.cuboid(var3, GizmoStyle.stroke(var8, 2.0F)).setAlwaysOnTop();
      Vec3 var9 = var7.orElseGet(() -> closestPointOnRay(var4, var5, var3.getCenter()));
      Gizmos.cuboid(new AABB(var9, var9).inflate(0.07), GizmoStyle.fill(var8)).setAlwaysOnTop();
   }

   private static Vec3 closestPointOnRay(Vec3 var0, Vec3 var1, Vec3 var2) {
      return var0.add(var1.scale(Math.max(0.0, var2.subtract(var0).dot(var1))));
   }

   private static Vec3 ringPoint(Vec3 var0, int var1, double var2) {
      double var4 = (Math.PI * 2) * var1 / 64.0;
      return var0.add(Math.cos(var4) * var2, 0.0, Math.sin(var4) * var2);
   }

   private MaceRenderer() {
   }
}
