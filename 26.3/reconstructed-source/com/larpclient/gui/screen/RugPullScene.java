package com.larpclient.gui.screen;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.model.ModelAssemblyAnimation;
import com.larpclient.render.particle.KillEffect;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.TeleportEffectRenderer;
import com.larpclient.render.world.WorldTextRenderer;
import com.larpclient.util.render.ColorUtil;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class RugPullScene {
   public static final double fADE_IN_TIME = 0.4;
   public static final double cHART_START_TIME = 0.35;
   public static final double cHART_PEAK_TIME = 2.3;
   public static final double cRASH_TIME = 2.6;
   public static final double cOLLAPSE_TIME = 2.82;
   public static final double sHAKE_FADE_START = 3.25;
   public static final double tEXT_START_TIME = 2.7;
   public static final double tEXT_FADE_START = 5.3;
   public static final double eFFECT_END_TIME = 5.6;
   public static final double eND_TIME = 6.4;
   public static final int sTART_BALANCE = 1000;
   public static final int eND_BALANCE = -67000;
   public static final double bOARD_LEFT = -2.7;
   public static final double bOARD_RIGHT = 1.45;
   public static final double bOARD_MARGIN = 0.1;
   public static final double bOARD_TOP = 3.95;
   public static final double cHART_LEFT = -2.35;
   public static final double mARKER_X = -0.32;
   public static final double cHART_BASE_Y = 0.6;
   public static final double mARKER_Y = 3.3;
   public static final double cHART_RIGHT = 1.12;
   public static final double bOARD_BACK_OFFSET = 0.45;
   public static final double dROP_DISTANCE = 1.9;
   public static final double mIN_MARKER_HEIGHT = 0.18;
   public static final int cHART_SEGMENTS = 48;
   public static final int cANDLE_COUNT = 12;
   public static final int cOIN_COUNT = 16;
   public static final int sPARK_COUNT = 7;
   public static final int dEBRIS_COUNT = 18;
   public static final double bOARD_TILT = 1.35;
   public static final double mAX_SINK = 2.8;
   public static final double rUG_HALF_WIDTH = 1.05;
   public static final double rUG_LENGTH = 1.55;
   public static final String tITLE_TEXT = "YOU JUST GOT RUG PULLED";
   public static final String sUBTITLE_TEXT = "LOSER.";
   public static final double tITLE_HEIGHT = 2.75;
   public static final double sUBTITLE_HEIGHT = 2.24;
   public static final double tITLE_SCALE = 0.42;
   public static final double sUBTITLE_SCALE = 0.32;
   public static final double tEXT_FORWARD_OFFSET = 0.25;
   public static final double tITLE_DURATION = 0.93;
   public static final double sUBTITLE_DELAY = 0.45;
   public static final double sUBTITLE_DURATION = 0.7;
   public static final double tEXT_FADE_DURATION = 0.7;
   private static final int gRID_COLOR = -3549466;
   private static final int gREEN_COLOR = -10656141;
   private static final int rUG_SHADOW_COLOR = -6217428;
   private static final int rUG_COLOR = -1920436;
   private static final int bALANCE_COLOR = -13242230;
   private static final int lOSS_COLOR = -50360;
   private final KillEffect scene;
   private final double[] chartX = new double[49];
   private final double[] chartY = new double[49];
   private final double[] wickUp = new double[12];
   private final double[] wickDown = new double[12];
   private final double[] coinSpawnTimes = new double[16];
   private final double[] coinDrift = new double[16];
   private final double[] coinSpin = new double[16];
   private final double[] sparkAngles = new double[7];
   private final double[] sparkLengths = new double[7];
   private final double[] debrisAngles = new double[18];
   private final double[] debrisSpeeds = new double[18];
   private final double[] debrisLift = new double[18];
   private double smoothedYaw = Double.NaN;
   private double lastUpdateTime = Double.NaN;

   RugPullScene(KillEffect var1) {
      this.scene = var1;
      double var2 = Math.exp(2.4) - 1.0;
      double[] var4 = new double[]{var1.randomFor(15000) * Math.PI * 2.0, var1.randomFor(15001) * Math.PI * 2.0, var1.randomFor(15002) * Math.PI * 2.0};

      for (int var5 = 0; var5 <= 48; var5++) {
         double var6 = var5 / 48.0;
         double var8 = (Math.exp(2.4 * var6) - 1.0) / var2;
         double var10 = Math.sin(var6 * Math.PI * 5.0 + var4[0]) * 0.5
            + Math.sin(var6 * Math.PI * 9.0 + var4[1]) * 0.25
            + Math.sin(var6 * Math.PI * 15.0 + var4[2]) * 0.17;
         this.chartX[var5] = -2.35 + 2.0300000000000002 * var6;
         this.chartY[var5] = 0.6 + 2.6999999999999997 * var8 + var10 * 0.16 * Math.sin(Math.PI * var6);
      }

      for (int var12 = 0; var12 < 12; var12++) {
         this.wickUp[var12] = 0.03 + var1.randomFor(15100 + var12) * 0.07;
         this.wickDown[var12] = 0.03 + var1.randomFor(15200 + var12) * 0.07;
      }

      for (int var13 = 0; var13 < 16; var13++) {
         this.coinSpawnTimes[var13] = 0.7 + 1.5499999999999998 * var13 / 15.0 + var1.randomFor(15300 + var13) * 0.08;
         this.coinDrift[var13] = var1.randomFor(15400 + var13) * 2.0 - 1.0;
         this.coinSpin[var13] = var1.randomFor(15500 + var13) * Math.PI * 2.0;
      }

      for (int var14 = 0; var14 < 7; var14++) {
         this.sparkAngles[var14] = (var14 + var1.randomFor(15600 + var14) * 0.6) * Math.PI * 2.0 / 7.0;
         this.sparkLengths[var14] = 0.9 + var1.randomFor(15700 + var14) * 0.8;
      }

      for (int var15 = 0; var15 < 18; var15++) {
         this.debrisAngles[var15] = var1.randomFor(15800 + var15) * Math.PI * 2.0;
         this.debrisSpeeds[var15] = 0.9 + var1.randomFor(15900 + var15) * 1.4;
         this.debrisLift[var15] = 2.6 + var1.randomFor(16000 + var15) * 2.4;
      }
   }

   public static double chartProgress(double var0) {
      return KillEffect.ease((var0 - 0.35) / 1.9499999999999997);
   }

   public static double fadeIn(double var0) {
      return KillEffect.ease(var0 / 0.4);
   }

   public static double fadeOut(double var0) {
      return KillEffect.ease((6.4 - var0) / 0.8000000000000007);
   }

   public static double crashPhase(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      } else if (var0 < 2.82) {
         double var2 = (var0 - 2.6) / 0.21999999999999975;
         return var2 * var2;
      } else {
         return 1.0 + Math.min(1.0, (var0 - 2.82) / 0.18);
      }
   }

   public int pointCount() {
      return 49;
   }

   public double chartX(int var1) {
      return this.chartX[var1];
   }

   public double chartY(int var1) {
      return this.chartY[var1];
   }

   public double[] chartPointAt(double var1) {
      double var3 = chartProgress(var1) * 48.0;
      int var5 = Math.min(47, (int)Math.floor(var3));
      double var6 = var3 - var5;
      return new double[]{
         this.chartX[var5] + (this.chartX[var5 + 1] - this.chartX[var5]) * var6, this.chartY[var5] + (this.chartY[var5 + 1] - this.chartY[var5]) * var6
      };
   }

   public double balanceAt(double var1) {
      if (var1 < 2.6) {
         double var7 = this.chartPointAt(var1)[1];
         return Math.round(1000.0 * Math.clamp((var7 - 0.6) / 2.6999999999999997, 0.0, 1.0));
      } else {
         double var3 = Math.clamp((var1 - 2.6) / 0.6499999999999999, 0.0, 1.0);
         double var5 = 1.0 - Math.pow(1.0 - var3, 3.0);
         return Math.round(1000.0 + -68000.0 * var5);
      }
   }

   public static String formatMoney(double var0) {
      long var2 = Math.round(var0);
      return (var2 < 0L ? "-$" : "$") + String.format(Locale.ROOT, "%,d", Math.abs(var2));
   }

   public ModelAssemblyAnimation.Basis updateFacing(Vec3 var1, double var2) {
      double var4 = var1.x - this.scene.origin.x;
      double var6 = var1.z - this.scene.origin.z;
      double var8 = var4 * var4 + var6 * var6 < 1.0E-6 ? (Double.isNaN(this.smoothedYaw) ? this.scene.yaw : this.smoothedYaw) : Math.atan2(var4, var6);
      if (!Double.isNaN(this.smoothedYaw) && !Double.isNaN(this.lastUpdateTime) && !(var2 < this.lastUpdateTime)) {
         double var10 = Math.IEEEremainder(var8 - this.smoothedYaw, Math.PI * 2);
         this.smoothedYaw = this.smoothedYaw + var10 * (1.0 - Math.exp(-Math.min(0.25, var2 - this.lastUpdateTime) * 9.0));
      } else {
         this.smoothedYaw = var8;
      }

      this.lastUpdateTime = var2;
      return frameForYaw(this.smoothedYaw);
   }

   public static ModelAssemblyAnimation.Basis frameForYaw(double var0) {
      double var2 = Math.sin(var0);
      double var4 = Math.cos(var0);
      return new ModelAssemblyAnimation.Basis(new Vec3(var4, 0.0, -var2), new Vec3(0.0, 1.0, 0.0), new Vec3(var2, 0.0, var4));
   }

   public Vec3 boardPosition(ModelAssemblyAnimation.Basis var1, double var2, double var4) {
      Vec3 var6 = this.scene.origin.subtract(var1.normal().scale(0.45));
      return var6.add(var1.right().scale(var2)).add(0.0, var4, 0.0);
   }

   public Vec3 markerPosition(ModelAssemblyAnimation.Basis var1, double var2) {
      double var4 = crashPhase(var2);
      Vec3 var6 = this.boardPosition(var1, -0.32, 3.3);
      Vec3 var7 = this.scene.origin;
      Vec3 var8 = this.scene.origin.add(0.0, -1.9, 0.0);
      return var4 <= 1.0 ? var6.lerp(var7, var4) : var7.lerp(var8, var4 - 1.0);
   }

   private Vec3 forwardAxis() {
      return new Vec3(-Math.sin(this.scene.yaw), 0.0, Math.cos(this.scene.yaw));
   }

   private Vec3 chartPointAt() {
      return new Vec3(Math.cos(this.scene.yaw), 0.0, Math.sin(this.scene.yaw));
   }

   public static double rugOffset(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      }

      double var2 = var0 - 2.6;
      return 11.0 * var2 * var2 + 0.4 * var2;
   }

   public static KillEffect.PartState rugOffset(KillEffect var0, int var1, double var2) {
      double var4 = KillEffect.getTrail(var1, var0.slimArms) / 16.0 * 0.9375;
      double var6 = (24.0 - KillEffect.pART_OFFSETS[var1][1]) / 16.0 * 0.9375;
      double var8 = KillEffect.ease((var2 - 0.35) / 0.5) * KillEffect.ease((2.6 - var2) / 0.15);
      double var10 = 0.035 * Math.abs(Math.sin((var2 - 0.35) * Math.PI * 2.6)) * var8;
      double var12 = var2 < 2.6 ? 0.0 : Math.pow(Math.min(1.0, (var2 - 2.6) / 0.32), 2.0);
      double var14 = 1.35 * var12;
      double var16 = 0.26 * KillEffect.ease((var2 - 0.35) / 0.5) + 0.7 * KillEffect.ease((var2 - 2.6) / 0.25);
      double var18 = var1 == 2 ? -var16 : (var1 == 3 ? var16 : 0.0);
      double var20 = 0.35 * KillEffect.ease((var2 - 2.6) / 0.3);
      double var22 = var2 < 2.82 ? 0.0 : Math.min(2.8, 6.5 * (var2 - 2.82) * (var2 - 2.82));
      double var24 = Math.cos(var0.yaw);
      double var26 = Math.sin(var0.yaw);
      double var28 = var6 * Math.sin(var14);
      Vec3 var30 = var0.origin
         .add(var4 * var24 + var28 * var26 - var20 * var26, var6 * Math.cos(var14) + var10 - var22, var4 * var26 - var28 * var24 + var20 * var24);
      double var31 = 1.0 - KillEffect.ease((var2 - 2.82 - 0.3) / 0.45);
      return new KillEffect.PartState(var30, var14, 0.0, var18, 1.0, var31, 0.0, false);
   }

   public void frameForYaw(ModelAssemblyAnimation.Basis var1, double var2, double var4, ToIntFunction<String> var6, List<WorldTextRenderer.WorldLabel> var7) {
      double var8 = fadeIn(var2) * fadeOut(var2) * var4;
      if (!(var8 < 0.01)) {
         Quaternionf var10 = new Quaternionf().rotateY((float)Math.atan2(var1.normal().x, var1.normal().z));
         double var11 = KillEffect.ease((var2 - 2.6) / 0.12);

         for (int var13 = 0; var13 <= 2; var13++) {
            double var14 = 0.6 + 2.6999999999999997 * var13 / 2.0;
            String var16 = var13 == 0 ? "$0" : (var13 == 1 ? "$500" : "$1K");
            addLabel(
               var7, var6, var16, this.boardPosition(var1, 1.2400000000000002, var14), var1, 0.26, ColorUtil.multiplyAlpha(-6641994, var8 * 0.75), var10, true
            );
         }

         double[] var29 = this.chartPointAt(Math.min(var2, 2.6));
         double var30 = var2 < 2.6 ? 0.0 : 0.045 * (1.0 - KillEffect.ease((var2 - 3.25) / 0.6));
         double var31 = var30 * Math.sin(var2 * 71.0);
         double var18 = var30 * Math.sin(var2 * 53.0 + 1.3);
         double var20 = 1.0 + 0.35 * KillEffect.physicsStart(var2, 2.6, 3.1500000000000004) + 0.06 * var11;
         double var22 = KillEffect.ease((var2 - 0.35) / 0.2);
         Vec3 var24 = this.boardPosition(var1, var29[0] + var31, var29[1] + 0.48 + var18);
         int var25 = ColorUtil.lerpColor(-13242230, -50360, var11);
         addLabel(var7, var6, formatMoney(this.balanceAt(var2)), var24, var1, 0.62 * var20, ColorUtil.multiplyAlpha(var25, var8 * var22), var10, false);
         if (var2 >= 2.6) {
            String var26 = "-" + String.format(Locale.ROOT, "%,d", Math.round((1000.0 - this.balanceAt(var2)) * 100.0 / 1000.0)) + "%";
            addLabel(
               var7,
               var6,
               var26,
               this.boardPosition(var1, var29[0] + var31, var29[1] + 0.02 + var18),
               var1,
               0.3,
               ColorUtil.multiplyAlpha(-50360, var8 * var11 * 0.9),
               var10,
               false
            );
         }

         if (var2 >= 2.7) {
            Vec3 var32 = this.scene.origin.add(var1.normal().scale(0.25));
            double var27 = Math.clamp((var2 - 5.3) / 0.7, 0.0, 1.0);
            render(var7, "YOU JUST GOT RUG PULLED", var32.add(0.0, 2.75, 0.0), 0.42, -50360, (var2 - 2.7) / 0.93, var27, var2, var4, var10);
            render(var7, "LOSER.", var32.add(0.0, 2.24, 0.0), 0.32, -854792, (var2 - 2.7 - 0.45) / 0.7, var27, var2, var4, var10);
         }
      }
   }

   private static void render(
      List<WorldTextRenderer.WorldLabel> var0,
      String var1,
      Vec3 var2,
      double var3,
      int var5,
      double var6,
      double var8,
      double var10,
      double var12,
      Quaternionf var14
   ) {
      if (!(var6 <= 0.0) && !(var8 >= 1.0) && !(var12 <= 0.004)) {
         WorldTextRenderer.LabelMotion var15 = new WorldTextRenderer.LabelMotion(3, Math.min(1.0, var6), var8, var10, 0);
         var0.add(
            new WorldTextRenderer.WorldLabel(
               var1, var2.x, var2.y, var2.z, var3 / 0.605, ColorUtil.multiplyAlpha(var5, var12), var14.x, var14.y, var14.z, var14.w, true, var15
            )
         );
      }
   }

   private static void addLabel(
      List<WorldTextRenderer.WorldLabel> var0,
      ToIntFunction<String> var1,
      String var2,
      Vec3 var3,
      ModelAssemblyAnimation.Basis var4,
      double var5,
      int var7,
      Quaternionf var8,
      boolean var9
   ) {
      if (var7 >>> 24 != 0) {
         double var10 = var5 / 0.605;
         Vec3 var12 = var3.add(var4.normal().scale(0.01));
         if (var9) {
            var12 = var12.add(var4.right().scale(var1.applyAsInt(var2) * var5 / 32.0 * 0.5));
         }

         var0.add(new WorldTextRenderer.WorldLabel(var2, var12.x, var12.y, var12.z, var10, var7, var8.x, var8.y, var8.z, var8.w, true));
      }
   }

   public static void render(
      EffectsRenderer var0, KillEffect var1, double var2, int var4, int var5, double var6, boolean var8, ModelAssemblyAnimation.Basis var9
   ) {
      RugPullScene var10 = var1.isFinished();
      if (var10 != null && var9 != null) {
         double var11 = fadeIn(var2);
         double var13 = fadeOut(var2);
         double var15 = var6 * var11 * var13;
         if (!(var15 <= 0.001)) {
            double var17 = KillEffect.ease((var2 - 2.6) / 0.6);
            int var19 = ColorUtil.lerpColor(var4, -10656141, var17 * 0.7);
            if (!var8) {
               var0.blend(HitEffectConfig.BlendMode.NORMAL);
               var10.fadeOut(var0, var9, var15);
               var10.drawChartFill(var0, var9, var2, var19, var15 * (1.0 - var17 * 0.6));
               var10.fadeIn(var0, var9, var2, var4, var5, var17, var15);
               var10.drawChartLine(var0, var2, var6 * var13);
               var10.drawDebris(var0, var2, var5, var6 * var13);
               var10.drawCoins(var0, var9, var2, var6 * var13);
            }

            var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
            var10.drawChartLine(var0, var9, var2, var19, var15 * (var8 ? 0.8 : 1.0));
            var10.drawChartFill(var0, var9, var2, var19, var5, var15);
            var10.balanceAt(var0, var9, var2, var5, var15);
            var10.pointCount(var0, var2, var5, var6 * var13);
         }
      }
   }

   private void addQuad(EffectsRenderer var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6, int var7, int var8, int var9) {
      var1.vertex(var2.x, var2.y, var2.z, var6);
      var1.vertex(var3.x, var3.y, var3.z, var7);
      var1.vertex(var4.x, var4.y, var4.z, var8);
      var1.vertex(var5.x, var5.y, var5.z, var9);
   }

   private void forwardAxis(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      this.addQuad(
         var1,
         this.boardPosition(var2, var3, var5),
         this.boardPosition(var2, var7, var5),
         this.boardPosition(var2, var7, var9),
         this.boardPosition(var2, var3, var9),
         var11,
         var11,
         var12,
         var12
      );
   }

   private void addRugQuad(EffectsRenderer var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6) {
      Vec3 var7 = var3.subtract(var2).cross(var4.subtract(var2));
      if (var7.y >= 0.0) {
         this.addQuad(var1, var2, var3, var4, var5, var6, var6, var6, var6);
      } else {
         this.addQuad(var1, var5, var4, var3, var2, var6, var6, var6, var6);
      }
   }

   private void fadeOut(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3) {
      double var5 = KillEffect.ease((var3 - 0.3) / 0.7);
      int var7 = ColorUtil.multiplyAlpha(-3549466, var3 * 0.075 * var5);

      for (int var8 = 0; var8 <= 4; var8++) {
         double var9 = 0.6 + 2.6999999999999997 * var8 / 4.0;
         var1.ribbon(this.boardPosition(var2, -2.58, var9), this.boardPosition(var2, 1.08, var9), 0.012, var7, var7);
      }

      for (int var11 = 0; var11 <= 5; var11++) {
         double var13 = -2.35 + 3.47 * var11 / 5.0;
         var1.ribbon(this.boardPosition(var2, var13, 0.2), this.boardPosition(var2, var13, 3.5), 0.012, var7, var7);
      }

      int var12 = ColorUtil.multiplyAlpha(-3549466, var3 * 0.22 * var5);
      var1.ribbon(this.boardPosition(var2, 1.12, 0.2), this.boardPosition(var2, 1.12, 3.5), 0.016, var12, var12);
   }

   private void drawChartFill(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = chartProgress(var3) * 48.0;
         int var10 = ColorUtil.multiplyAlpha(var5, var6 * 0.3);
         int var11 = ColorUtil.multiplyAlpha(var5, 0.0);
         double var12 = 0.16;

         for (int var14 = 0; var14 < 48 && var14 < var8; var14++) {
            double var15 = Math.min(1.0, var8 - var14);
            double var17 = this.chartX[var14] + (this.chartX[var14 + 1] - this.chartX[var14]) * var15;
            double var19 = this.chartY[var14] + (this.chartY[var14 + 1] - this.chartY[var14]) * var15;
            this.addQuad(
               var1,
               this.boardPosition(var2, this.chartX[var14], var12),
               this.boardPosition(var2, var17, var12),
               this.boardPosition(var2, var17, var19),
               this.boardPosition(var2, this.chartX[var14], this.chartY[var14]),
               var11,
               var11,
               var10,
               var10
            );
         }
      }
   }

   private void fadeIn(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, int var5, int var6, double var7, double var9) {
      if (!(var3 < 0.35)) {
         double var11 = chartProgress(var3) * 48.0;
         byte var13 = 4;
         double var14 = 0.1691666666666667;
         double var16 = var14 * 0.62;

         for (int var18 = 0; var18 < 12; var18++) {
            int var19 = var18 * var13;
            if (var11 <= var19) {
               break;
            }

            int var20 = Math.min((var18 + 1) * var13, 48);
            double var21 = Math.min(var11, var20);
            double var23 = this.chartY[var19];
            double var25 = this.interpolateChartY(var21);
            double var27 = Math.max(var23, var25);
            double var29 = Math.min(var23, var25);

            for (int var31 = var19 + 1; var31 < var21; var31++) {
               var27 = Math.max(var27, this.chartY[var31]);
               var29 = Math.min(var29, this.chartY[var31]);
            }

            int var48 = var11 < var20 ? 1 : 0;
            double var32 = var48 != 0 ? 1.0 : KillEffect.ease((var11 - var20) / 2.0 + 0.5);
            var27 += this.wickUp[var18] * var32;
            var29 -= this.wickDown[var18] * var32;
            double var34 = -2.35 + var14 * (var18 + 0.5);
            int var36 = var25 >= var23 ? 1 : 0;
            int var37 = ColorUtil.lerpColor(var36 != 0 ? var5 : var6, -10656141, var7 * 0.65);
            double var38 = var9 * (0.55 + 0.2 * (1.0 - var7));
            int var40 = ColorUtil.multiplyAlpha(var37, var38 * 0.8);
            var1.ribbon(this.boardPosition(var2, var34, var29), this.boardPosition(var2, var34, var27), 0.018, var40, var40);
            double var41 = Math.min(var23, var25);
            double var43 = Math.max(Math.max(var23, var25), var41 + 0.02);
            int var45 = ColorUtil.multiplyAlpha(var37, var38);
            this.forwardAxis(var1, var2, var34 - var16 * 0.5, var41, var34 + var16 * 0.5, var43, var45, var45);
         }
      }
   }

   private double interpolateChartY(double var1) {
      int var3 = Math.min(47, (int)Math.floor(var1));
      return this.chartY[var3] + (this.chartY[var3 + 1] - this.chartY[var3]) * (var1 - var3);
   }

   private void drawChartLine(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = chartProgress(var3) * 48.0;
         Vec3 var10 = this.boardPosition(var2, this.chartX[0], this.chartY[0]);

         for (int var11 = 0; var11 < 48 && var11 < var8; var11++) {
            double var12 = Math.min(1.0, var8 - var11);
            Vec3 var14 = this.boardPosition(
               var2,
               this.chartX[var11] + (this.chartX[var11 + 1] - this.chartX[var11]) * var12,
               this.chartY[var11] + (this.chartY[var11 + 1] - this.chartY[var11]) * var12
            );
            TeleportEffectRenderer.drawRibbon(var1, var10, var14, 0.055, var5, var5, var6);
            var10 = var14;
         }

         double[] var17 = this.chartPointAt(var3);
         Vec3 var18 = this.boardPosition(var2, var17[0], var17[1]);
         double var13 = 0.5 + 0.5 * Math.sin(var3 * 9.0);
         double var15 = var3 < 2.6 ? 1.0 : 1.0 - KillEffect.ease((var3 - 2.6) / 0.2);
         var1.drawSprite(var18.x, var18.y, var18.z, 0.07 + var13 * 0.03, var3, ColorUtil.multiplyAlpha(-1, var6 * var15));
         var1.drawSprite(var18.x, var18.y, var18.z, 0.16 + var13 * 0.05, -var3, ColorUtil.multiplyAlpha(var5, var6 * 0.55 * var15));
      }
   }

   private void drawChartFill(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, int var5, int var6, double var7) {
      if (!(var3 < 0.35)) {
         double var9;
         double var11;
         if (var3 < 2.6) {
            double[] var13 = this.chartPointAt(var3);
            var9 = var13[1];
            var11 = var13[0];
         } else {
            var9 = Math.max(0.18, 3.3 * (1.0 - Math.min(1.0, crashPhase(var3))));
            var11 = -0.22;
         }

         int var20 = ColorUtil.multiplyAlpha(var3 < 2.6 ? var5 : var6, var7 * 0.45 * KillEffect.ease((var3 - 0.35) / 0.3));
         double var14 = 0.11;
         double var16 = 0.07;

         for (double var18 = var11 + 0.1; var18 < 1.1; var18 += var14 + var16) {
            var1.ribbon(this.boardPosition(var2, var18, var9), this.boardPosition(var2, Math.min(1.1, var18 + var14), var9), 0.014, var20, var20);
         }
      }
   }

   private void balanceAt(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, int var5, double var6) {
      if (!(var3 < 2.6)) {
         double var8 = crashPhase(var3);
         Vec3 var10 = this.boardPosition(var2, -0.32, 3.3);
         Vec3 var11 = this.markerPosition(var2, var3);
         if (var8 <= 1.0) {
            TeleportEffectRenderer.drawRibbon(var1, var10, var11, 0.085, var5, var5, var6);
         } else {
            TeleportEffectRenderer.drawRibbon(var1, var10, this.scene.origin, 0.085, var5, var5, var6);
            TeleportEffectRenderer.drawRibbon(var1, this.scene.origin, var11, 0.085, var5, var5, var6);
         }

         double var12 = -0.15083333333333332;
         double var14 = 0.0609;
         double var16 = Math.max(0.12000000000000001, 3.3 * (1.0 - Math.min(1.0, var8)));
         this.forwardAxis(
            var1, var2, var12 - var14, var16, var12 + var14, 3.3, ColorUtil.multiplyAlpha(var5, var6 * 0.55), ColorUtil.multiplyAlpha(var5, var6 * 0.8)
         );
         if (var8 < 1.0) {
            var1.drawSprite(var11.x, var11.y, var11.z, 0.14, var3 * 3.0, ColorUtil.multiplyAlpha(-1, var6));
         }
      }
   }

   private void pointCount(EffectsRenderer var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0)) {
         Vec3 var9 = this.scene.origin.add(0.0, 0.03, 0.0);
         double var10 = KillEffect.physicsStart(var7, 0.0, 0.8) * var5;
         TeleportEffectRenderer.drawRing(var1, var9, 0.15 + var7 * 3.4, 0.07, var4, var10);
         TeleportEffectRenderer.drawRing(var1, var9.add(0.0, 0.005, 0.0), 0.1 + var7 * 2.2, 0.02, -1, var10 * 0.6);
         double var12 = KillEffect.ease(var7 / 0.12) * (1.0 - KillEffect.ease((var2 - 5.6 + 0.4) / 0.8)) * var5;
         TeleportEffectRenderer.drawRing(var1, var9, 0.42 + 0.03 * Math.sin(var2 * 6.0), 0.05, var4, var12 * 0.5);
         double var14 = KillEffect.ease(var7 / 0.35);

         for (int var16 = 0; var16 < 7; var16++) {
            double var17 = this.sparkAngles[var16];
            double var19 = this.sparkLengths[var16] * var14;
            Vec3 var21 = var9;

            for (int var22 = 1; var22 <= 4; var22++) {
               double var23 = var22 / 4.0;
               double var25 = (this.scene.randomFor(16200 + var16 * 8 + var22) - 0.5) * 0.5 * var23;
               Vec3 var27 = var9.add(Math.cos(var17 + var25) * var19 * var23, 0.0, Math.sin(var17 + var25) * var19 * var23);
               TeleportEffectRenderer.drawRibbon(var1, var21, var27, 0.05 * (1.2 - var23), var4, var4, var12 * 0.85);
               var21 = var27;
            }
         }

         TeleportEffectRenderer.emitBurstSparks(var1, this.scene, var7, var9.add(0.0, 0.1, 0.0), var4, -11566, 34, 2.1, var5);
      }
   }

   private void drawDebris(EffectsRenderer var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0) && !(var7 > 1.6)) {
         for (int var9 = 0; var9 < 18; var9++) {
            double var10 = this.debrisSpeeds[var9];
            double var12 = this.debrisAngles[var9];
            double var14 = this.debrisLift[var9] * var7 - 4.9 * var7 * var7;
            double var16 = var14 < 0.0 ? 0.0 : 1.0;
            double var18 = var10 * var7;
            double var20 = var5 * KillEffect.ease((1.6 - var7) / 0.4) * var16;
            if (!(var20 <= 0.002)) {
               Vec3 var22 = this.scene.origin.add(Math.cos(var12) * var18, Math.max(0.0, var14) + 0.04, Math.sin(var12) * var18);
               int var23 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(-14016476, var4, this.scene.randomFor(16300 + var9) * 0.35), var20);
               var1.drawCone(var22.x, var22.y, var22.z, 0.06 + this.scene.randomFor(16400 + var9) * 0.05, var12 + var7 * 7.0, var7 * 9.0, var7 * 5.0, var23);
            }
         }
      }
   }

   private void drawCoins(EffectsRenderer var1, ModelAssemblyAnimation.Basis var2, double var3, double var5) {
      Vec3 var7 = this.scene.origin.add(0.0, -0.4, 0.0);

      for (int var8 = 0; var8 < 16; var8++) {
         double var9 = var3 - this.coinSpawnTimes[var8];
         if (!(var9 < 0.0)) {
            double[] var11 = this.chartPointAt(this.coinSpawnTimes[var8]);
            Vec3 var12 = this.boardPosition(var2, var11[0], var11[1]).add(var2.normal().scale(0.2));
            double var13 = Math.min(var9, Math.max(0.0, 2.6 - this.coinSpawnTimes[var8]));
            Vec3 var15 = var12.add(var2.right().scale(this.coinDrift[var8] * 0.35 * var13)).add(0.0, 0.45 * var13, 0.0).add(var2.normal().scale(0.25 * var13));
            double var16 = var3 < 2.6 ? 0.0 : KillEffect.ease((var3 - 2.6 - var8 * 0.012) / 0.5);
            Vec3 var18 = var15.lerp(var7, var16 * var16);
            double var19 = var5 * KillEffect.ease(var9 / 0.2) * (1.0 - KillEffect.ease((var16 - 0.75) / 0.25));
            if (!(var19 <= 0.002)) {
               double var21 = this.coinSpin[var8] + var9 * 2.4 + var16 * 8.0;
               var1.setSoftParticles(
                  var18.x,
                  var18.y,
                  var18.z,
                  0.17 * (1.0 - var16 * 0.5),
                  var21,
                  0.4 * Math.sin(var9 * 3.0 + var8),
                  0.3 * Math.cos(var9 * 2.3 + var8),
                  Math.sin(var9 * 6.0 + var8) * 0.6,
                  ColorUtil.multiplyAlpha(-1, var19)
               );
            }
         }
      }
   }

   private void drawChartLine(EffectsRenderer var1, double var2, double var4) {
      double var6 = KillEffect.ease(var2 / 0.5);
      double var8 = 1.0 - KillEffect.ease((var2 - 2.6 - 0.25) / 0.3);
      double var10 = var4 * var6 * var8;
      if (!(var10 <= 0.002)) {
         Vec3 var12 = this.forwardAxis();
         Vec3 var13 = this.chartPointAt();
         double var14 = rugOffset(var2);
         double var16 = var2 < 2.6 ? 0.0 : 0.5 * Math.sin(Math.min(1.0, (var2 - 2.6) / 0.45) * Math.PI);
         double var18 = -0.775 + var14;
         double var20 = var18 + 1.55 * var6;
         Vec3 var22 = this.scene.origin.add(0.0, 0.012, 0.0);
         double[] var23 = new double[]{var18, var20};
         double[] var24 = new double[]{var16, var16 * 0.35};
         int var25 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(-6217428, -16777216, 0.2), var10);
         this.addRugStrip(var1, var22, var12, var13, var23, var24, 0.525, ColorUtil.multiplyAlpha(-1920436, var10));
         this.addRugStrip(var1, var22.add(0.0, 0.002, 0.0), var12, var13, new double[]{var23[0] + 0.09, var23[1] - 0.09}, var24, 0.43500000000000005, var25);
         this.addRugStrip(
            var1,
            var22.add(0.0, 0.004, 0.0),
            var12,
            var13,
            new double[]{var23[0] + 0.15, var23[1] - 0.15},
            var24,
            0.375,
            ColorUtil.multiplyAlpha(-6217428, var10)
         );
         double var26 = (var23[0] + var23[1]) * 0.5;
         double var28 = 0.22 * var6;
         double var30 = (var24[0] + var24[1]) * 0.5;
         Vec3 var32 = var22.add(0.0, 0.006 + var30, 0.0).add(var12.scale(var26));
         this.addRugQuad(
            var1,
            var32.add(var12.scale(var28)),
            var32.add(var13.scale(var28 * 0.7)),
            var32.add(var12.scale(-var28)),
            var32.add(var13.scale(-var28 * 0.7)),
            ColorUtil.multiplyAlpha(-1920436, var10)
         );
         int var33 = ColorUtil.multiplyAlpha(-792892, var10 * 0.9);

         for (int var34 = 0; var34 < 2; var34++) {
            double var35 = var23[var34];
            double var37 = var34 == 0 ? -1.0 : 1.0;

            for (int var39 = 0; var39 < 9; var39++) {
               double var40 = (var39 / 8.0 - 0.5) * 0.93;
               Vec3 var42 = var22.add(0.0, var24[var34] + 0.008, 0.0).add(var12.scale(var35)).add(var13.scale(var40));
               var1.ribbon(var42, var42.add(var12.scale(var37 * 0.1)), 0.018, var33, ColorUtil.multiplyAlpha(var33, 0.0));
            }
         }
      }
   }

   private void addRugStrip(EffectsRenderer var1, Vec3 var2, Vec3 var3, Vec3 var4, double[] var5, double[] var6, double var7, int var9) {
      if (!(var5[1] - var5[0] <= 0.001)) {
         Vec3 var10 = var2.add(var3.scale(var5[0])).add(0.0, var6[0], 0.0);
         Vec3 var11 = var2.add(var3.scale(var5[1])).add(0.0, var6[1], 0.0);
         this.addRugQuad(var1, var10.add(var4.scale(-var7)), var10.add(var4.scale(var7)), var11.add(var4.scale(var7)), var11.add(var4.scale(-var7)), var9);
      }
   }
}
