package com.larpclient.render.particle;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.entity.OrbitTrail;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class CometTrailRenderer {
   private static final OrbitTrail trailPath = new OrbitTrail();
   private static final CatmullRomSpline.Ribbon[] ribbons = new CatmullRomSpline.Ribbon[3];
   private static final Vec3[] headPositions = new Vec3[3];
   private static Object lastPlayer;
   private static Object lastLevel;
   private static Vec3 lastPlayerPos;
   private static Vec3 renderPos;
   private static double time;
   private static double playerHeight;
   private static double size;
   private static double trailLifetime;
   private static double trailAlpha;
   private static double fade;
   private static int headColor;
   private static int tailColor;
   private static boolean active;
   private static boolean glowEnabled;

   public static void reset() {
      trailPath.reset();
      lastLevel = null;
      lastPlayer = null;
      renderPos = null;
      lastPlayerPos = null;
      active = (boolean)0;
   }

   public static void update(float var0) {
      Minecraft var1 = Minecraft.getInstance();
      SelfVisualsModule var2 = SelfVisualsModule.getPrimaryColor();
      LocalPlayer var3 = var1.player;
      if (var2 != null && var2.isActiveForSelf() && var2.style.isMode("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()
         )
       {
         Vec3 var4 = var3.position();
         int var5 = lastPlayer == var3 && lastLevel == var1.level && (lastPlayerPos == null || !(lastPlayerPos.distanceToSqr(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            reset();
         }

         lastPlayer = var3;
         lastLevel = var1.level;
         lastPlayerPos = var4;
         renderPos = var3.getPosition(var0).distanceToSqr(var4) > 64.0 ? var4 : var3.getPosition(var0);
         playerHeight = var3.getBbHeight();
         size = OrbitTrail.getTrailTime(var2.cometSize.getValue(), playerHeight);
         trailLifetime = var2.trailLength.getValue();
         trailAlpha = var2.glowIntensity.getValue();
         glowEnabled = var2.cometBloom.getValue();
         headColor = var2.themeColors.getValue() ? Theme.accent : var2.cometColor.getValue();
         tailColor = var2.themeColors.getValue() ? Theme.accentEnd : var2.tailColor.getValue();
         time = TickTimer.getTime();
         double var6 = Math.max(var2.orbitRadius.getValue(), var3.getBbWidth() * 0.71 + size);
         trailPath.update(time, var2.orbitSpeed.getValue(), var6, trailLifetime);
         fade = trailPath.fadeIn(time);
         Vec3 var8 = var1.gameRenderer.mainCamera().position();

         for (int var9 = 0; var9 < 3; var9++) {
            CatmullRomSpline.Ribbon var10 = ribbons[var9];
            var10.clear();
            Vec3 var11 = headPositions[var9] = OrbitTrail.anchorOffset(trailPath.getHead(var9), renderPos, playerHeight, size);

            for (int var12 = 0; var12 <= trailPath.getSize(); var12++) {
               Vec3 var13 = var12 == trailPath.getSize() ? var11 : OrbitTrail.anchorOffset(trailPath.getTrailPoint(var9, var12), renderPos, playerHeight, size);
               double var14 = var12 == trailPath.getSize() ? 1.0 : Math.clamp(1.0 - (time - trailPath.getTrailTime(var12)) / trailLifetime, 0.0, 1.0);
               var10.addPoint(
                  var13,
                  size * 1.4 * var14 * var14,
                  ColorUtil.multiplyAlpha(ColorUtil.lerpColor(tailColor, headColor, var14), var14 * var14 * trailAlpha * fade)
               );
            }

            var10.fadeInTail();
            var10.computeOffsets(var8);
         }

         active = (boolean)1;
      } else {
         reset();
      }
   }

   public static boolean isGlowActive() {
      return active && glowEnabled;
   }

   public static void render(EffectsRenderer var0, boolean var1) {
      if (active && (!var1 || glowEnabled)) {
         var0.blend(HitEffectConfig.BlendMode.ADDITIVE);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3 var3 = headPositions[var2];
            var0.drawTrail(ribbons[var2], true);
            var0.drawSpriteWithType(
               var3.x, var3.y, var3.z, size * 2.2, 0.0, ColorUtil.multiplyAlpha(headColor, 0.35 * trailAlpha * fade), ParticleSystem.Style.GLOW, 0.0, 0.0, 0.0
            );
            var0.drawSpriteWithType(
               var3.x, var3.y, var3.z, size, 0.0, ColorUtil.multiplyAlpha(headColor, 0.9 * fade), ParticleSystem.Style.CIRCLE, 0.0, 0.0, 0.0
            );
            var0.drawSpriteWithType(
               var3.x,
               var3.y,
               var3.z,
               size * 0.55,
               0.0,
               ColorUtil.multiplyAlpha(ColorUtil.lerpColor(headColor, headColor & 0xFF000000 | 16777215, 0.8), fade),
               ParticleSystem.Style.GLOW,
               0.0,
               0.0,
               0.0
            );
         }

         var0.blend(HitEffectConfig.BlendMode.NORMAL);
      }
   }

   private CometTrailRenderer() {
   }

   static {
      for (int var0 = 0; var0 < ribbons.length; var0++) {
         ribbons[var0] = new CatmullRomSpline.Ribbon(257);
      }
   }
}
