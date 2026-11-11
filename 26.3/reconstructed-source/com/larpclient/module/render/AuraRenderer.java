package com.larpclient.module.render;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.render.model.ReachZoneMesh;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.render.ColorUtil;
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

public final class AuraRenderer {
   private static Object cachedLevel;
   private static int cachedTargetId;
   private static long cachedGameTime = Long.MIN_VALUE;
   private static AABB cachedTargetBox;
   private static double cachedReach;
   private static double cachedEyeHeight;
   private static boolean cachedFlag;
   private static ReachZoneMesh.Mesh hitboxShape;
   private static int currentColor;
   private static Vec3 targetCenter;

   public static void registerRenderHooks() {
      LevelRenderEvents.BEFORE_GIZMOS.register(AuraRenderer::renderRangeArc);
   }

   private static void renderRangeArc(LevelRenderContext var0) {
      AuraModule var1 = AuraModule.getInstance();
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null
         && var1.isOperational()
         && var2.player != null
         && var2.gui.screen() == null
         && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         float var3 = var2.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         if (var1.isFovConeVisible()) {
            Vec3 var4 = var2.player.getPosition(var3).add(0.0, 0.12, 0.0);
            double var5 = var2.player.getYRot() - var1.getInstance() / 2.0;
            int var7 = Math.max(8, (int)Math.ceil(var1.getInstance() / 6.0));

            for (int var8 = 0; var8 < var7; var8++) {
               drawLine(
                  var1,
                  hasOutline(var4, var5 + var1.getInstance() * var8 / var7, var1.getClampedReach()),
                  hasOutline(var4, var5 + var1.getInstance() * (var8 + 1) / var7, var1.getClampedReach()),
                  ColorUtil.lerpColor(Theme.accent, -1, 0.3),
                  var1.getIndicatorWidth()
               );
            }

            if (var1.getInstance() < 360.0) {
               drawLine(var1, var4, hasOutline(var4, var5, var1.getClampedReach()), ColorUtil.lerpColor(Theme.accent, -1, 0.3), var1.getIndicatorWidth());
               drawLine(
                  var1,
                  var4,
                  hasOutline(var4, var5 + var1.getInstance(), var1.getClampedReach()),
                  ColorUtil.lerpColor(Theme.accent, -1, 0.3),
                  var1.getIndicatorWidth()
               );
            }
         }
      }
   }

   public static void updateTargetOverlay() {
      AuraModule var0 = AuraModule.getInstance();
      Minecraft var1 = Minecraft.getInstance();
      if (var0 != null
         && var0.isHitZoneActive()
         && var1.level != null
         && var1.player != null
         && var1.gui.screen() == null
         && !var1.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         rebuildShape(var0, var1);
         String var2 = var0.isTargeting();
         int var3 = var2.equals("Valid attack ray")
            ? ColorUtil.lerpColor(Theme.accent, -8327246, 0.35)
            : (var2.equals("Outside reach") ? ColorUtil.lerpColor(Theme.accent, -1277295, 0.55) : ColorUtil.lerpColor(Theme.accent, -12672, 0.5));
         currentColor = currentColor == 0 ? var3 : ColorUtil.lerpColor(currentColor, var3, 1.0 - Math.exp(-TickTimer.getDeltaTime() * 9.0));
      } else {
         hitboxShape = null;
         cachedLevel = null;
         currentColor = 0;
      }
   }

   private static void rebuildShape(AuraModule var0, Minecraft var1) {
      LivingEntity var2 = var0.applyMoveFix();
      AABB var3 = var2.getBoundingBox();
      double var4 = var0.getClampedReach();
      double var6 = var1.player.getEyeHeight();
      long var8 = var1.level.getGameTime();
      if (hitboxShape == null
         || cachedLevel != var1.level
         || cachedTargetId != var2.getId()
         || cachedGameTime != var8
         || !var3.equals(cachedTargetBox)
         || cachedReach != var4
         || cachedEyeHeight != var6
         || cachedFlag != var0.getMoveKeys()) {
         HashMap var10 = new HashMap();
         double var11 = var2.getY();
         CollisionContext var13 = CollisionContext.of(var1.player);
         ReachZoneMesh.SupportSampler var14 = (var5, var7) -> {
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
            return ReachZoneMesh.findSupport(var13x, var5, var7, var11);
         };
         hitboxShape = ReachZoneMesh.build(var3, var4, var6, var14, var0.getMoveKeys());
         if (cachedLevel != var1.level || cachedTargetId != var2.getId()) {
            currentColor = 0;
         }

         cachedLevel = var1.level;
         cachedTargetId = var2.getId();
         cachedGameTime = var8;
         cachedTargetBox = var3;
         cachedReach = var4;
         cachedEyeHeight = var6;
         cachedFlag = var0.getMoveKeys();
      }

      targetCenter = var3.getCenter();
   }

   public static boolean hasOutline() {
      AuraModule var0 = AuraModule.getInstance();
      return hitboxShape != null && !hitboxShape.outline().isEmpty() && var0 != null && var0.isZoneBloom();
   }

   public static void registerRenderHooks(EffectsRenderer var0, boolean var1) {
      AuraModule var2 = AuraModule.getInstance();
      if (hitboxShape != null && var2 != null && (!var1 || var2.isZoneBloom())) {
         double var3 = var2.breakBlocksAlongSight() * (var1 ? 0.65 : 1.0);
         double var5 = var2.breakBlocksAlongSight() * 0.035;
         double var7 = var2.isZoneMotion() ? TickTimer.getTime() : 0.0;
         var0.blend(HitEffectConfig.BlendMode.ADDITIVE);

         for (ReachZoneMesh.Segment var10 : hitboxShape.outline()) {
            Vec3 var11 = var10.from();
            Vec3 var12 = var10.end();
            int var13 = outlineColor(var11, var7, var3, var2.isZoneMotion());
            int var14 = outlineColor(var12, var7, var3, var2.isZoneMotion());
            var0.drawGlowRibbon(var11.x, var11.y, var11.z, var12.x, var12.y, var12.z, var5, var5, var13, var14);
         }

         if (var2.isZoneMotion()) {
            for (int var42 = 0; var42 < 3; var42++) {
               double var44 = var7 * 0.55 + var42 * Math.PI * 2.0 / 3.0;
               double var47 = Math.cos(var44);
               double var48 = Math.sin(var44);
               double var16 = Double.POSITIVE_INFINITY;
               Vec3 var18 = null;

               for (ReachZoneMesh.Segment var20 : hitboxShape.outline()) {
                  Vec3 var21 = var20.from();
                  Vec3 var22 = var20.end();
                  double var23 = var21.x - targetCenter.x;
                  double var25 = var21.z - targetCenter.z;
                  double var27 = var22.x - var21.x;
                  double var29 = var22.z - var21.z;
                  double var31 = var27 * var48 - var29 * var47;
                  double var33 = Math.abs(var31) < 1.0E-8 ? 0.5 : Math.clamp((var25 * var47 - var23 * var48) / var31, 0.0, 1.0);
                  Vec3 var35 = var21.lerp(var22, var33);
                  double var36 = var35.x - targetCenter.x;
                  double var38 = var35.z - targetCenter.z;
                  if (!(var36 * var47 + var38 * var48 <= 0.0)) {
                     double var40 = Math.abs(var36 * var48 - var38 * var47);
                     if (var40 < var16) {
                        var16 = var40;
                        var18 = var35;
                     }
                  }
               }

               if (var18 != null && var16 < 0.08) {
                  int var49 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(currentColor, -1, 0.65), var3);
                  var0.drawSpriteWithType(
                     var18.x, var18.y + 0.018, var18.z, var5 * 1.6, var7 * 0.6, ColorUtil.multiplyAlpha(var49, 0.45), ParticleSystem.Style.GLOW, 0.0, 0.0, 0.0
                  );
                  var0.drawSprite(var18.x, var18.y + 0.018, var18.z, var5 * 0.6, var7 * 0.6, var49);
               }
            }
         }

         var0.blend(HitEffectConfig.BlendMode.NORMAL);
         if (!var1) {
            int var43 = ColorUtil.multiplyAlpha(currentColor, var3 * 0.065);

            for (ReachZoneMesh.Triangle var46 : hitboxShape.fill()) {
               addVertex(var0, var46.first(), var43);
               addVertex(var0, var46.second(), var43);
               addVertex(var0, var46.third(), var43);
               addVertex(var0, var46.third(), var43);
            }
         }
      }
   }

   private static void addVertex(EffectsRenderer var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int outlineColor(Vec3 var0, double var1, double var3, boolean var5) {
      double var6 = Math.atan2(var0.z - targetCenter.z, var0.x - targetCenter.x);
      double var8 = var5 ? Math.pow(0.5 + 0.5 * Math.cos(var6 * 3.0 - var1 * 1.65), 12.0) : 0.0;
      return ColorUtil.multiplyAlpha(ColorUtil.lerpColor(currentColor, -1, 0.15 + var8 * 0.55), var3 * (0.65 + var8 * 0.35));
   }

   private static void drawLine(AuraModule var0, Vec3 var1, Vec3 var2, int var3, float var4) {
      GizmoProperties var5 = Gizmos.line(var1, var2, var3 | 0xFF000000, var4);
      if (var0.getViewPitch()) {
         var5.setAlwaysOnTop();
      }
   }

   private static Vec3 hasOutline(Vec3 var0, double var1, double var3) {
      double var5 = Math.toRadians(var1);
      return var0.add(-Math.sin(var5) * var3, 0.0, Math.cos(var5) * var3);
   }

   private AuraRenderer() {
   }
}
