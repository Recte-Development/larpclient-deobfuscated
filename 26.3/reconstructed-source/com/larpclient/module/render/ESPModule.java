package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.util.entity.EntityHealth;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.NotificationSound;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.ScreenProjection;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ESPModule extends Module {
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box"
      )
   );
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Show players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Show hostile mobs", true));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Show animals", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final EspRenderer espRenderer = new EspRenderer(var1 -> this.addSetting(var1));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final BooleanSetting healthBars = this.addSetting(new BooleanSetting("Health Bars", "High-contrast screen-space health bar", true));
   private final NumberSetting healthWidth = this.addSetting(new NumberSetting("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final BooleanSetting healthText = this.addSetting(new BooleanSetting("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESPModule instance;
   private final Set<Integer> outlinedEntityIds = new HashSet<>();
   private final Map<Integer, Boolean> lineOfSightCache = new HashMap<>();
   private static final int sHADOW_COLOR = -670364656;
   private final BooleanSetting notifications = this.addSetting(new BooleanSetting("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> notifiedPlayerIds = new HashSet<>();
   private final Set<Integer> previousPlayerIds = new HashSet<>();
   private boolean firstScan = (boolean)1;

   public ESPModule() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      instance = this;
      this.healthWidth.visibleWhen(this.healthBars::getValue);
      this.healthText.visibleWhen(this.healthBars::getValue);
      this.espRenderer
         .drawLine(
            () -> true, () -> !this.mode.isMode("Outline"), () -> !this.mode.isMode("Outline") || this.healthBars.getValue(), () -> this.mode.isMode("Filled")
         );
      LevelRenderEvents.BEFORE_GIZMOS.register(this::renderBoxes);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "entity_esp"), this::renderHud);
   }

   @Override
   public void onEnable() {
      this.outlinedEntityIds.clear();
      this.lineOfSightCache.clear();
      this.notifiedPlayerIds.clear();
      this.previousPlayerIds.clear();
      this.firstScan = (boolean)1;
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.players, this.mobs, this.animals);
   }

   public List<Setting<?>> getModeSettings() {
      return List.of(this.mode, this.range);
   }

   public List<Setting<?>> getColorSettings() {
      return this.espRenderer.colorSettings();
   }

   public List<Setting<?>> getOutlineFillSettings() {
      ArrayList var1 = new ArrayList<>(this.espRenderer.outlineSettings());
      var1.addAll(this.espRenderer.fillSettings());
      return List.copyOf(var1);
   }

   public List<Setting<?>> getHealthBarSettings() {
      return List.of(this.healthBars, this.healthWidth, this.healthText);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxEntities, this.notifications);
   }

   @Override
   public void onDisable() {
      this.outlinedEntityIds.clear();
      this.lineOfSightCache.clear();
      this.notifiedPlayerIds.clear();
      this.previousPlayerIds.clear();
   }

   @Override
   public void onTick() {
      this.lineOfSightCache.clear();
      if (this.isInGame()) {
         this.outlinedEntityIds.clear();
         int var10000 = this.mode.isMode("Outline");
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            for (Entity var2 : EspRenderer.nearbyEntities(
               minecraft.level.entitiesForRendering(), minecraft.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::isValidTarget
            )) {
               this.outlinedEntityIds.add(var2.getId());
            }
         }

         int var19 = this.notifications.getValue();
         boolean var13 = false;
         byte var12 = (byte)var19;
         var19 = Integer.signum(var12);
         if ((var19 | -var19) >>> 31 == 0) {
            this.notifiedPlayerIds.clear();
            this.previousPlayerIds.clear();
            this.firstScan = (boolean)1;
         } else {
            HashSet var16 = new HashSet();

            for (Entity var3 : minecraft.level.entitiesForRendering()) {
               if (var3 != minecraft.player && var3 instanceof Player var4 && var4.isAlive()) {
                  double var5 = minecraft.player.distanceTo(var3);
                  if (!(var5 > this.range.getValue())) {
                     var16.add(var3.getId());
                     int var7 = !this.notifiedPlayerIds.contains(var3.getId()) ? 1 : 0;
                     int var8 = !this.previousPlayerIds.contains(var3.getId()) ? 1 : 0;
                     if ((var7 != 0 || var8 != 0) && !this.firstScan) {
                        this.notifiedPlayerIds.add(var3.getId());
                        String var9 = var4.getName().getString();
                        int var10 = (int)EntityHealth.getHealth(var4);
                        String var11 = String.format("%.0fm", var5);
                        NotificationManager.getInstance()
                           .notify("Player Detected", var9 + " - " + var10 + "HP - " + var11, NotificationManager.Type.WARNING, 10000L);
                        NotificationSound.play();
                     } else {
                        this.notifiedPlayerIds.add(var3.getId());
                     }
                  }
               }
            }

            this.notifiedPlayerIds.retainAll(var16);
            this.previousPlayerIds.clear();
            this.previousPlayerIds.addAll(var16);
            this.firstScan = (boolean)0;
         }
      }
   }

   private boolean isValidTarget(Entity var1) {
      return var1 instanceof LivingEntity
         && (
            var1 instanceof Player
               ? this.players.getValue()
               : (var1 instanceof Enemy ? this.mobs.getValue() : var1 instanceof Animal && this.animals.getValue())
         );
   }

   public static boolean getOutlineColor() {
      return instance != null && instance.isEnabled() && !instance.outlinedEntityIds.isEmpty();
   }

   public static int getOutlineColor(LivingEntity var0) {
      if (instance == null || !instance.isEnabled()) {
         return 0;
      } else if (instance != null
         && instance.isOperational()
         && instance.mode.isMode("Outline")
         && instance.isInGame()
         && var0 != minecraft.player
         && var0.isAlive()
         && instance.isValidTarget(var0)
         && instance.outlinedEntityIds.contains(var0.getId())) {
         double var1 = minecraft.player.distanceTo(var0);
         return var1 >= instance.range.getValue() ? 0 : instance.resolveColor(var0, var1);
      } else {
         return 0;
      }
   }

   private int resolveColor(LivingEntity var1, double var2) {
      int var4 = var1 instanceof Player ? -4221441 : (var1 instanceof Enemy ? -943189 : -7677757);
      return this.espRenderer.resolveColor(var4, var2, this.range.getValue(), EspRenderer.healthFraction(var1), var1.getId());
   }

   private void renderBoxes(LevelRenderContext var1) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && this.mode.isMode("3D Box")) {
            float var2 = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            long var3 = PerfProfiler.startTimer();

            try {
               for (Entity var6 : EspRenderer.nearbyEntities(
                  minecraft.level.entitiesForRendering(), minecraft.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::isValidTarget
               )) {
                  int var7 = this.resolveColor((LivingEntity)var6, minecraft.player.distanceTo(var6));
                  AABB var8 = EspRenderer.interpolatedBox(var6, var2);
                  this.espRenderer.drawBox(var8, "Box", var7);
               }
            } catch (RuntimeException var12) {
               LarpLog.errorOnce("ESP", "Entity overlay failed", var12);
            } finally {
               PerfProfiler.endTimer("ESP", var3);
            }
         }
      }
   }

   private void renderHud(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isOperational() && this.isInGame() && !minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = StreamProofManager.selectGraphics(var1, true, false);
         if (var1 != null) {
            Camera var3 = minecraft.gameRenderer.mainCamera();
            Matrix4f var4 = EspRenderer.buildWorldProjection(new Matrix4f());
            int var5 = minecraft.getWindow().getGuiScaledWidth();
            int var6 = minecraft.getWindow().getGuiScaledHeight();
            float var7 = 1.0F / Math.max(1, minecraft.getWindow().getGuiScale());
            float var8 = var2.getGameTimeDeltaPartialTick(false);
            boolean var9 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            float var10 = snapThickness(this.espRenderer.drawLine(), var7);
            float var11 = snapThickness(0.5F, var7);
            List var12 = EspRenderer.nearbyEntities(
               minecraft.level.entitiesForRendering(), minecraft.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::isValidTarget
            );

            for (int var13 = var12.size() - 1; var13 >= 0; var13--) {
               Entity var14 = (Entity)var12.get(var13);
               if (this.espRenderer.isThroughWalls() || this.lineOfSightCache.computeIfAbsent(var14.getId(), var1x -> minecraft.player.hasLineOfSight(var14))) {
                  ScreenProjection.FloatBounds var15 = ScreenProjection.projectBox(
                     EspRenderer.interpolatedBox(var14, var8), var3.position(), var4, var5, var6, var9
                  );
                  if (var15 != null) {
                     float var16 = snapToPixel(var15.left(), var7);
                     float var17 = snapToPixel(var15.top(), var7);
                     float var18 = snapToPixel(var15.right(), var7);
                     float var19 = snapToPixel(var15.bottom(), var7);
                     if (!(var18 - var16 < 2.0F) && !(var19 - var17 < 3.0F)) {
                        LivingEntity var20 = (LivingEntity)var14;
                        int var21 = this.resolveColor(var20, minecraft.player.distanceTo(var14));
                        if (var21 >>> 24 != 0) {
                           double var22 = (var21 >>> 24) / 255.0;
                           int var24 = ColorUtil.multiplyAlpha(-670364656, var22);
                           if (!this.mode.isMode("Box") && !this.mode.isMode("Filled")) {
                              if (this.mode.isMode("Corners")) {
                                 float var39 = var18 - var16;
                                 float var26 = var19 - var17;
                                 float var27 = Math.min(Math.max(snapToPixel(var39 * 0.28F, var7), 2.0F * var10), var39 / 2.0F - var11);
                                 float var28 = Math.min(Math.max(snapToPixel(var26 * 0.2F, var7), 2.0F * var10), var26 / 2.0F - var11);
                                 var27 = Math.max(var27, var10);
                                 var28 = Math.max(var28, var10);

                                 for (int var29 = 0; var29 < 2; var29++) {
                                    float var30 = var29 == 0 ? var11 : 0.0F;
                                    int var31 = var29 == 0 ? var24 : var21;

                                    for (int var32 = 0; var32 < 4; var32++) {
                                       int var33 = (var32 & 1) == 0 ? 1 : -1;
                                       int var34 = var32 < 2 ? 1 : -1;
                                       float var35 = (var33 > 0 ? var16 : var18) - var33 * var30;
                                       float var36 = (var34 > 0 ? var17 : var19) - var34 * var30;
                                       drawCorner(var1, var35, var36, var33, var34, var27 + 2.0F * var30, var28 + 2.0F * var30, var10 + 2.0F * var30, var31);
                                    }
                                 }
                              }
                           } else {
                              if (this.mode.isMode("Filled")) {
                                 int var25 = ColorUtil.multiplyAlpha(var21, this.espRenderer.getFillOpacity());
                                 DrawUtil.fillGradientF(
                                    var1,
                                    var16 + var10 + var11,
                                    var17 + var10 + var11,
                                    var18 - var10 - var11,
                                    var19 - var10 - var11,
                                    var25,
                                    ColorUtil.multiplyAlpha(var25, 0.45)
                                 );
                              }

                              drawBoxOutline(var1, var16 - var11, var17 - var11, var18 + var11, var19 + var11, var10 + 2.0F * var11, var24);
                              drawBoxOutline(var1, var16, var17, var18, var19, var10, var21);
                           }

                           if (this.mode.isMode("Skeleton")) {
                              try {
                                 this.getOutlineColor(var1, var20, var8, var3, var4, var5, var6, var9, var21, var10, var11);
                              } catch (RuntimeException var37) {
                                 LarpLog.errorOnce("ESP", "Skeleton overlay failed", var37);
                              }
                           }

                           if (this.healthBars.getValue()) {
                              this.getModeSettings(var1, var20, var16, var17, var18, var19, var5, var6, var7, var11, var22, var24);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void getModeSettings(
      GuiGraphicsExtractor var1,
      LivingEntity var2,
      float var3,
      float var4,
      float var5,
      float var6,
      int var7,
      int var8,
      float var9,
      float var10,
      double var11,
      int var13
   ) {
      float var14 = snapThickness(this.healthWidth.getValue().floatValue(), var9);
      float var15 = snapToPixel(2.0F, var9);
      int var16 = var3 - var10 - var15 - var14 - var10 >= 1.0F ? 1 : 0;
      float var17 = var16 != 0 ? var3 - var10 - var15 - var14 : Math.min(var7 - var14 - var10, var5 + var10 + var15);
      double var18 = Math.clamp(EspRenderer.healthFraction(var2), 0.0, 1.0);
      float var20 = snapToPixel(var6 - (var6 - var4) * (float)var18, var9);
      int var21 = var18 < 0.5 ? ColorUtil.lerpColor(-44442, -10660, var18 * 2.0) : ColorUtil.lerpColor(-10660, -10951269, (var18 - 0.5) * 2.0);
      DrawUtil.fillRectF(var1, var17 - var10, var4 - var10, var17 + var14 + var10, var6 + var10, var13);
      DrawUtil.fillRectF(var1, var17, var4, var17 + var14, var6, ColorUtil.multiplyAlpha(-1070977739, var11));
      DrawUtil.fillGradientF(
         var1,
         var17,
         var20,
         var17 + var14,
         var6,
         ColorUtil.multiplyAlpha(var21, var11),
         ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var21, -16777216, 0.22), var11)
      );
      if (var2.getAbsorptionAmount() > 0.0F) {
         double var22 = Math.min(1.0F, var2.getAbsorptionAmount() / Math.max(1.0F, var2.getMaxHealth()));
         float var24 = Math.max(var9, snapToPixel(var14 / 3.0F, var9));
         float var25 = snapToPixel(var6 - (var6 - var4) * (float)var22, var9);
         float var26 = var16 != 0 ? var17 : var17 + var14 - var24;
         DrawUtil.fillRectF(var1, var26, var25, var26 + var24, var6, ColorUtil.multiplyAlpha(-10660, var11));
      }

      if (this.healthText.getValue()) {
         String var30 = (int)Math.ceil(EntityHealth.getHealth(var2)) + " HP";
         int var23 = FontRenderer.getDefaultWidth(var30);
         float var31 = snapToPixel(Math.clamp((var3 + var5 - var23) / 2.0F, 3.0F, Math.max(3, var7 - var23 - 3)), var9);
         float var32 = snapToPixel(Math.min(var8 - 11, var6 + var10 + 3.0F), var9);
         DrawUtil.fillRectF(var1, var31 - 3.0F, var32 - 1.5F, var31 + var23 + 3.0F, var32 + 9.5F, var13);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var31, var32);
            FontRenderer.drawDefaultText(var1, var30, 0, 0, ColorUtil.multiplyAlpha(-723464, var11));
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   private static float snapToPixel(float var0, float var1) {
      return Math.round(var0 / var1) * var1;
   }

   private static float snapThickness(float var0, float var1) {
      return Math.max(var1, snapToPixel(var0, var1));
   }

   private static void drawBoxOutline(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      DrawUtil.fillRectF(var0, var1, var2, var3, var2 + var5, var6);
      DrawUtil.fillRectF(var0, var1, var4 - var5, var3, var4, var6);
      DrawUtil.fillRectF(var0, var1, var2 + var5, var1 + var5, var4 - var5, var6);
      DrawUtil.fillRectF(var0, var3 - var5, var2 + var5, var3, var4 - var5, var6);
   }

   private static void drawCorner(GuiGraphicsExtractor var0, float var1, float var2, int var3, int var4, float var5, float var6, float var7, int var8) {
      DrawUtil.fillRectF(
         var0,
         Math.min(var1, var1 + var3 * var5),
         Math.min(var2, var2 + var4 * var7),
         Math.max(var1, var1 + var3 * var5),
         Math.max(var2, var2 + var4 * var7),
         var8
      );
      DrawUtil.fillRectF(
         var0,
         Math.min(var1, var1 + var3 * var7),
         Math.min(var2 + var4 * var7, var2 + var4 * var6),
         Math.max(var1, var1 + var3 * var7),
         Math.max(var2 + var4 * var7, var2 + var4 * var6),
         var8
      );
   }

   private void getOutlineColor(
      GuiGraphicsExtractor var1,
      LivingEntity var2,
      float var3,
      Camera var4,
      Matrix4f var5,
      int var6,
      int var7,
      boolean var8,
      int var9,
      float var10,
      float var11
   ) {
      Vec3 var12 = EspRenderer.interpolatedPosition(var2, var3);
      double var13 = var2.getBbHeight();
      double var15 = var2.getBbWidth();
      double var17 = Math.toRadians(Mth.rotLerp(var3, var2.yBodyRotO, var2.yBodyRot));
      Vec3 var19 = new Vec3(-Math.sin(var17), 0.0, Math.cos(var17));
      Vec3 var20 = new Vec3(Math.cos(var17), 0.0, Math.sin(var17));
      double var21 = var2.walkAnimation.position(var3) * 0.6662;
      double var23 = Math.min(1.0F, var2.walkAnimation.speed(var3));
      double var25 = Math.cos(var21) * 1.4 * var23;
      double var27 = Math.cos(var21) * var23;
      Vec3 var29 = var12.add(0.0, var13 * 0.375, 0.0);
      Vec3 var30 = var12.add(0.0, var13 * 0.75, 0.0);
      Vec3 var31 = var12.add(0.0, var13 * 0.6875, 0.0);
      Vec3 var32 = var31.add(var20.scale(var15 * 0.55));
      Vec3 var33 = var31.add(var20.scale(-var15 * 0.55));
      Vec3 var34 = var29.add(var20.scale(var15 * 0.18));
      Vec3 var35 = var29.add(var20.scale(-var15 * 0.18));
      Vec3[][] var36 = new Vec3[][]{
         {var29, var30},
         {var32, var33},
         {var34, var35},
         {var32, drawCorner(var32, var19, var13 * 0.3125, var27)},
         {var33, drawCorner(var33, var19, var13 * 0.3125, -var27)},
         {var34, drawCorner(var34, var19, var13 * 0.375, -var25)},
         {var35, drawCorner(var35, var19, var13 * 0.375, var25)}
      };
      Vec3 var37 = var4.position();
      ArrayList var38 = new ArrayList(var36.length);

      for (Vec3[] var42 : var36) {
         ScreenProjection.ProjectedLine var43 = ScreenProjection.projectLine(var42[0], var42[1], var37, var5, var6, var7, var8);
         if (var43 != null) {
            var38.add(var43);
         }
      }

      ScreenProjection.ProjectedPoint var47 = ScreenProjection.projectPoint(
         var12.add(0.0, var13 * 0.875, 0.0), var37, var5, new Vector3f(var4.upVector()), var6, var7, var8
      );
      int var48 = var47 == null ? 0 : Math.max(1, Math.round(var47.scale() * (float)var13 * 0.125F));
      int var49 = ColorUtil.multiplyAlpha(-670364656, (var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var51 = var50 == 0 ? var49 : var9;
         float var44 = var50 == 0 ? var10 + 2.0F * var11 : var10;

         for (ScreenProjection.ProjectedLine var46 : var38) {
            DrawUtil.drawLine(var1, var46.getStartX(), var46.getStartY(), var46.getEndX(), var46.getEndY(), var44, var51);
         }

         if (var47 != null) {
            DrawUtil.fillCircle(var1, Math.round(var47.getX()), Math.round(var47.getY()), var48 + (var50 == 0 ? 1 : 0), var51);
         }
      }
   }

   private static Vec3 drawCorner(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(var1.scale(-Math.sin(var4) * var2)).add(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
