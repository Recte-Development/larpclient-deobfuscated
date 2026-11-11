package com.larpclient.render.entity;

import com.larpclient.gui.theme.Theme;
import com.larpclient.mixin.GameRendererMixin_3;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.entity.EntityHealth;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class EspRenderer {
   private final ModeSetting colorMode = new ModeSetting(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final ColorSetting customColor = new ColorSetting("Custom Color", "Color used by Custom mode", -4291585);
   private final NumberSetting lineWidth = new NumberSetting("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final NumberSetting fillOpacity = new NumberSetting("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final BooleanSetting throughWalls = new BooleanSetting(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public EspRenderer(Consumer<Setting<?>> var1) {
      this(var1, true);
   }

   public EspRenderer(Consumer<Setting<?>> var1, boolean var2) {
      this.customColor.visibleWhen(() -> this.colorMode.isMode("Custom"));

      for (Setting var4 : List.of(this.colorMode, this.customColor, this.lineWidth, this.throughWalls)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.fillOpacity);
      }
   }

   public void drawLine(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.colorMode.visibleWhen(var1);
      this.customColor.visibleWhen(() -> var1.getAsBoolean() && this.colorMode.isMode("Custom"));
      this.lineWidth.visibleWhen(var2);
      this.throughWalls.visibleWhen(var3);
      this.fillOpacity.visibleWhen(var4);
   }

   public List<Setting<?>> colorSettings() {
      return List.of(this.colorMode, this.customColor);
   }

   public List<Setting<?>> outlineSettings() {
      return List.of(this.lineWidth, this.throughWalls);
   }

   public List<Setting<?>> fillSettings() {
      return List.of(this.fillOpacity);
   }

   public boolean isThroughWalls() {
      return this.throughWalls.getValue();
   }

   public double getFillOpacity() {
      return this.fillOpacity.getValue();
   }

   public float drawLine() {
      return this.lineWidth.getValue().floatValue();
   }

   public int resolveColor(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.colorMode.getValue()) {
         case "Category" -> var1;
         case "Custom" -> this.customColor.getValue();
         case "Distance" -> Theme.scaleRounding(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> ColorUtil.lerpColor(-1017711, -7347266, var6);
         case "Rainbow" -> ColorUtil.hsbToRgb((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> Theme.scaleRounding(0.25);
      };
      return ColorUtil.multiplyAlpha(var9, distanceFade(var2, var4));
   }

   public static double distanceFade(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> nearbyEntities(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
      double var6 = var2 * var2;
      ArrayList var8 = new ArrayList();

      for (Entity var10 : var0) {
         if (var10 != var1 && var10.isAlive() && var1.distanceToSqr(var10) <= var6 && var5.test(var10)) {
            var8.add(var10);
         }
      }

      if (var8.size() > 1) {
         var8.sort(Comparator.comparingDouble(var1::distanceToSqr));
      }

      return var8.size() > var4 ? List.copyOf(var8.subList(0, Math.max(0, var4))) : var8;
   }

   public static Matrix4f buildWorldProjection(Matrix4f var0) {
      Minecraft var1 = Minecraft.getInstance();
      GameRenderState var2 = var1.gameRenderer.gameRenderState();
      CameraRenderState var3 = var2.levelRenderState.cameraRenderState;
      if (var3 != null && var3.projectionMatrix != null && var3.viewRotationMatrix != null) {
         var0.set(var3.projectionMatrix);

         try {
            GameRendererMixin_3 var4 = (GameRendererMixin_3)var1.gameRenderer;
            PoseStack var5 = new PoseStack();
            var4.larpBobHurt(var3, var5);
            if (var2.optionsRenderState.bobView) {
               var4.larpBobView(var3, var5);
            }

            var0.mul(var5.last().pose());
            LocalPlayer var6 = var1.player;
            if (var6 != null) {
               float var7 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(false);
               float var8 = var2.optionsRenderState.screenEffectScale;
               float var9 = Mth.lerp(var7, var6.oPortalEffectIntensity, var6.portalEffectIntensity);
               float var10 = var6.getEffectBlendFactor(MobEffects.NAUSEA, var7);
               float var11 = Math.max(var9, var10) * var8 * var8;
               if (var11 > 0.0F) {
                  float var12 = 5.0F / (var11 * var11 + 5.0F) - var11 * 0.04F;
                  var12 *= var12;
                  Vector3f var13 = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
                  float var14 = var6.getSpinningEffectAngle(var7) * (float) (Math.PI / 180.0);
                  var0.rotate(var14, var13).scale(1.0F / var12, 1.0F, 1.0F).rotate(-var14, var13);
               }
            }
         } catch (RuntimeException var15) {
            LarpLog.errorOnce("ESP", "Live world projection fell back to the unbobbed camera", var15);
            var0.set(var3.projectionMatrix);
         }

         return var0.mul(var3.viewRotationMatrix);
      } else {
         return var1.gameRenderer.mainCamera().getViewRotationProjectionMatrix(var0);
      }
   }

   public static Vec3 interpolatedPosition(Entity var0, float var1) {
      return new Vec3(Mth.lerp(var1, var0.xOld, var0.getX()), Mth.lerp(var1, var0.yOld, var0.getY()), Mth.lerp(var1, var0.zOld, var0.getZ()));
   }

   public static AABB interpolatedBox(Entity var0, float var1) {
      return var0.getBoundingBox().move(interpolatedPosition(var0, var1).subtract(var0.position())).inflate(0.025);
   }

   private void applyDepthMode(GizmoProperties var1) {
      if (this.throughWalls.getValue()) {
         var1.setAlwaysOnTop();
      }
   }

   public void drawLine(Vec3 var1, Vec3 var2, int var3) {
      this.applyDepthMode(Gizmos.line(var1, var2, var3, this.drawLine()));
   }

   public void drawBox(AABB var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getXsize() * 0.25;
            double var6 = var1.getYsize() * 0.18;
            double var8 = var1.getZsize() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3 var13 = new Vec3(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.drawLine(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.drawLine(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.drawLine(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? ColorUtil.multiplyAlpha(var3, this.fillOpacity.getValue()) : 0;
            this.applyDepthMode(Gizmos.cuboid(var1, GizmoStyle.strokeAndFill(var3, this.drawLine(), var14)));
         }
      }
   }

   public void drawHealthBar(AABB var1, double var2, int var4) {
      Vec3 var5 = new Vec3(var1.minX - 0.08, var1.minY, var1.minZ);
      this.applyDepthMode(
         Gizmos.line(var5, var5.add(0.0, var1.getYsize(), 0.0), ColorUtil.multiplyAlpha(Theme.controlBackground, (var4 >>> 24) / 255.0), this.drawLine() + 1.0F)
      );
      int var6 = ColorUtil.lerpColor(-1017711, -7347266, var2);
      this.drawLine(var5, var5.add(0.0, var1.getYsize() * Math.clamp(var2, 0.0, 1.0), 0.0), ColorUtil.withAlpha(var6, (var4 >>> 24) / 255.0));
   }

   public void drawLabel(Vec3 var1, String var2, int var3) {
      this.applyDepthMode(Gizmos.billboardText(var2, var1, Style.forColorAndCentered(var3).withScale(0.7F)));
   }

   public static double healthFraction(LivingEntity var0) {
      return EntityHealth.getHealthFraction(var0);
   }
}
