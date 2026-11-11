package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.world.WorldShapeRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule instance;
   private final NumberSetting count = this.addSetting(new NumberSetting("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final BooleanSetting slowSpeed = this.addSetting(new BooleanSetting("Slow Speed", "Slow the segment growth", false));
   private final ModeSetting color = this.addSetting(new ModeSetting("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.GlyphLine> glyphs = new ArrayList<>();
   private final Random random = new Random(40506L);
   private Object trackedLevel;
   private Object trackedPlayer;
   private Vec3 lastPlayerPos;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      instance = this;
   }

   public static LineGlyphsModule getInstance() {
      return instance;
   }

   public void reset() {
      this.glyphs.clear();
      this.trackedLevel = this.trackedPlayer = null;
      this.lastPlayerPos = null;
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.count.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label50: {
            var1 = this.getPlayer().position();
            if (this.trackedLevel == getMc().level && this.trackedPlayer == this.getPlayer()) {
               if (this.lastPlayerPos == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.distanceToSqr(this.lastPlayerPos) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.reset();
         }

         this.trackedLevel = getMc().level;
         this.trackedPlayer = this.getPlayer();
         this.lastPlayerPos = var1;
         double var2 = TickTimer.getTickTime();
         this.glyphs.removeIf(var2x -> var2x.isExpired(var2));
         int var4 = this.count.getValue().intValue();

         while (this.glyphs.size() > var4) {
            this.glyphs.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.glyphs.size() < var4; var5++) {
            double var6 = ((Integer)getMc().options.fov().get()).intValue();
            double var8 = Math.toRadians(this.getPlayer().getYRot() + (this.random.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.random.nextInt(6, 24);
            Vec3 var11 = new Vec3(
               (int)var1.x - (int)(Math.sin(var8) * var10), (int)var1.y + this.random.nextInt(12), (int)var1.z + (int)(Math.cos(var8) * var10)
            );
            this.glyphs.add(new LineGlyphsModule.GlyphLine(var11, this.random.nextInt(7, 13), var2, this.random));
         }

         for (LineGlyphsModule.GlyphLine var15 : this.glyphs) {
            var15.advance(var2, this.slowSpeed.getValue(), this.random);
         }
      } else {
         this.reset();
      }
   }

   public boolean hasVisibleGlyphs(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.trackedLevel == getMc().level) {
         int var3 = 0;

         for (LineGlyphsModule.GlyphLine var5 : this.glyphs) {
            double var6 = var5.alpha(var1);

            for (int var8 = 1; var8 < var5.points.size() && var6 > 0.0; var8++) {
               int var9 = this.render(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.points.size() / 1.75));
               int var10 = this.render(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.points.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.points.get(var8 - 1).distanceToSqr(this.getEndPoint(var5, var8)) >= 1.0E-10) {
                  return true;
               }
            }

            var3++;
         }

         return false;
      } else {
         return false;
      }
   }

   private Vec3 getEndPoint(LineGlyphsModule.GlyphLine var1, int var2) {
      Vec3 var3 = var1.points.get(var2);
      return var2 == var1.points.size() - 1
         ? var1.points
            .get(var2 - 1)
            .lerp(
               var3,
               Math.clamp(
                  1.0 - (var1.stepCooldown - TickTimer.getPartialTick() * (this.slowSpeed.getValue() ? 1 : 2)) / Math.max(1.0, var1.stepLength), 0.0, 1.0
               )
            )
         : var3;
   }

   public void render(PoseStack var1, EffectBatch var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.trackedLevel == getMc().level) {
            WorldShapeRenderer var7 = new WorldShapeRenderer(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.GlyphLine var10 : this.glyphs) {
               double var11 = var10.alpha(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.origin.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.points.size(); var15++) {
                  Vec3 var16 = var10.points.get(var15 - 1);
                  Vec3 var17 = this.getEndPoint(var10, var15);
                  int var18 = this.render(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.points.size() / 1.75));
                  int var19 = this.render(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.points.size() / 1.75));
                  if (!var6) {
                     var7.drawLine(var16, var17, var13 * 3.0, ColorUtil.multiplyAlpha(var18, 0.35), ColorUtil.multiplyAlpha(var19, 0.35));
                  }

                  var7.drawLine(var16, var17, var13, var18, var19);
                  int var20 = ColorUtil.lerpColor(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = ColorUtil.lerpColor(var19, var19 & 0xFF000000 | 16777215, var15 == var10.points.size() - 1 ? 0.6 : 0.35);
                  var7.drawLine(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int render(int var1, double var2, double var4) {
      int var6 = this.color.isMode("Rainbow")
         ? ColorUtil.hsbToRgb((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.color.isMode("Single") ? Theme.accent : ColorUtil.lerpColor(Theme.accent, Theme.accentEnd, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return ColorUtil.withAlpha(var6, var4);
   }

   private static final class GlyphLine {
      final Vec3 origin;
      final ArrayList<Vec3> points = new ArrayList<>();
      final double spawnTime;
      int segmentsLeft;
      int stepCooldown;
      int stepLength;
      int yaw;
      int pitch;
      double fadeStart = Double.NaN;

      GlyphLine(Vec3 var1, int var2, double var3, Random var5) {
         this.origin = var1;
         this.segmentsLeft = var2;
         this.spawnTime = var3;
         this.points.add(var1);
         this.yaw = var5.nextInt(4) * 90;
         this.pitch = var5.nextInt(-1, 2) * 90;
      }

      void advance(double var1, boolean var3, Random var4) {
         if (this.segmentsLeft == 0) {
            if (Double.isNaN(this.fadeStart)) {
               this.fadeStart = var1;
            }
         } else if (this.stepCooldown > 0) {
            this.stepCooldown = Math.max(0, this.stepCooldown - (var3 ? 1 : 2));
         } else {
            int var5 = this.yaw;
            int var6 = this.pitch;

            for (int var7 = 0; var7 < 150 && Math.abs(this.pitch - var6) != 90; var7++) {
               this.pitch = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.yaw - var5) != 90; var9++) {
               this.yaw = var4.nextInt(4) * 90;
            }

            this.stepCooldown = this.stepLength = var4.nextInt(3);
            int var10 = Math.max(1, this.stepCooldown);
            Vec3 var8 = this.points.getLast();
            this.points
               .add(
                  var8.add(
                     this.pitch == 0 ? Math.round(-Math.sin(Math.toRadians(this.yaw)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.pitch)) * var10),
                     this.pitch == 0 ? Math.round(Math.cos(Math.toRadians(this.yaw)) * var10) : 0.0
                  )
               );
            this.segmentsLeft--;
         }
      }

      double alpha(double var1) {
         return Double.isNaN(this.fadeStart) ? Math.clamp((var1 - this.spawnTime) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.fadeStart) / 0.4, 0.0, 1.0);
      }

      boolean isExpired(double var1) {
         return !Double.isNaN(this.fadeStart) && var1 - this.fadeStart >= 0.4 || var1 - this.spawnTime > 5.0;
      }
   }
}
