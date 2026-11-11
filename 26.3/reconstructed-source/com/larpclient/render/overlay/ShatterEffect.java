package com.larpclient.render.overlay;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.model.CrystalMesh;
import com.larpclient.render.particle.KillEffect;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.particle.YinYangRitualEffect;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.GroundProbeGrid;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ShatterEffect {
   public static final double tILT_DURATION = 0.16;
   public static final double sHATTER_START = 0.95;
   public static final double sHATTER_END = 1.32;
   public static final double sETTLE_TIME = 2.7;
   public static final double tOTAL_DURATION = 4.6;
   public static final int bODY_PIECES = 48;
   public static final int rING_PIECES = 14;
   public static final int dUST_PIECES = 40;
   public static final int cRACK_COUNT = 8;
   public static final int sPARK_COUNT = 40;
   public static final int tOTAL_PIECES = 102;
   static final double mAX_TILT = 0.12;
   static final double gRAVITY = 9.8;
   private static final double[][] pART_SIZES = new double[][]{
      {4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}
   };
   private static final int[] pART_WEIGHTS = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] cRACK_PARTS = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final KillEffect pose;
   private final int[] pieceParts = new int[102];
   private final double[] offsetX = new double[102];
   private final double[] offsetY = new double[102];
   private final double[] offsetZ = new double[102];
   private final double[] normalX = new double[102];
   private final double[] normalY = new double[102];
   private final double[] normalZ = new double[102];
   private final double[] pieceSize = new double[102];
   private final double[] appearTime = new double[102];
   private final double[] driftSpeed = new double[102];
   private final double[] launchSpeed = new double[102];
   private final double[] spinYaw = new double[102];
   private final double[] spinPitch = new double[102];
   private final double[] spinRoll = new double[102];
   private final double[] seedA = new double[102];
   private final double[] seedB = new double[102];
   private final Quaternionf rotation = new Quaternionf();
   private final Vector3f scratchVector = new Vector3f();

   ShatterEffect(KillEffect var1, GroundProbeGrid var2) {
      this.pose = var1;
      int var3 = 0;

      for (int var7 : pART_WEIGHTS) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.pieceSize[var17] = 0.09 + 0.11 * var1.randomFor(var17 + 11000);
         this.spinYaw[var17] = (var1.randomFor(var17 + 11200) - 0.5) * 9.0;
         this.spinPitch[var17] = (var1.randomFor(var17 + 11400) - 0.5) * 11.0;
         this.spinRoll[var17] = (var1.randomFor(var17 + 11600) - 0.5) * 7.0;
         this.driftSpeed[var17] = 1.2 + 1.4 * var1.randomFor(var17 + 11800);
         this.launchSpeed[var17] = 1.0 + 2.0 * var1.randomFor(var17 + 12000);
         this.seedA[var17] = var1.randomFor(var17 + 12200);
         this.seedB[var17] = var1.randomFor(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.randomFor(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.randomFor(var17 + 13600);
               this.pieceParts[var17] = -1;
               this.offsetX[var17] = Math.cos(var22) * var25;
               this.offsetZ[var17] = Math.sin(var22) * var25;
               this.normalX[var17] = Math.cos(var22) * 0.55;
               this.normalY[var17] = 1.0;
               this.normalZ[var17] = Math.sin(var22) * 0.55;
               this.pieceSize[var17] = 0.12 + 0.18 * var1.randomFor(var17 + 13800);
               this.driftSpeed[var17] = this.driftSpeed[var17] * 0.6;
               this.launchSpeed[var17] = this.launchSpeed[var17] * 0.55;
            } else {
               this.pieceParts[var17] = -2;
               double var20 = var1.randomFor(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.randomFor(var17 + 13600));
               this.offsetX[var17] = Math.cos(var20) * var24;
               this.offsetZ[var17] = Math.sin(var20) * var24;
               this.offsetY[var17] = 0.1 + 1.7 * var1.randomFor(var17 + 13800);
               this.pieceSize[var17] = 0.05 + 0.07 * var1.randomFor(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.randomFor(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= pART_WEIGHTS[var21]) {
               var18 -= pART_WEIGHTS[var21++];
            }

            this.pieceParts[var17] = var21;
            double[] var23 = pART_SIZES[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.slimArms ? 1.5 : var23[0];
            int var10 = (int)(var1.randomFor(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.randomFor(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.randomFor(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.offsetX[var17] = var8 + var15;
                  this.offsetY[var17] = var13 * var23[1];
                  this.offsetZ[var17] = var11 * var23[2];
                  this.normalX[var17] = 1.0;
                  break;
               case 1:
                  this.offsetX[var17] = -var8 - var15;
                  this.offsetY[var17] = var13 * var23[1];
                  this.offsetZ[var17] = var11 * var23[2];
                  this.normalX[var17] = -1.0;
                  break;
               case 2:
                  this.offsetZ[var17] = var23[2] + var15;
                  this.offsetY[var17] = var13 * var23[1];
                  this.offsetX[var17] = var11 * var8;
                  this.normalZ[var17] = 1.0;
                  break;
               case 3:
                  this.offsetZ[var17] = -var23[2] - var15;
                  this.offsetY[var17] = var13 * var23[1];
                  this.offsetX[var17] = var11 * var8;
                  this.normalZ[var17] = -1.0;
                  break;
               default:
                  this.offsetY[var17] = -var23[1] - var15;
                  this.offsetX[var17] = var11 * var8;
                  this.offsetZ[var17] = var13 * var23[2];
                  this.normalY[var17] = -1.0;
            }
         }

         this.appearTime[var17] = this.pieceParts[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.offsetX[var17], this.offsetZ[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.pieceHeight(var17) / 1.9, 0.0, 1.0) + 0.08 * this.seedA[var17];
      }

      var2.enqueue(var1.origin.x, var1.origin.z);
      var2.enqueueRadius(var1.origin.x, var1.origin.z, 4.0);
   }

   private double pieceHeight(int var1) {
      return this.pieceParts[var1] < 0 ? this.offsetY[var1] : (24.0 - KillEffect.pART_OFFSETS[this.pieceParts[var1]][1] - this.offsetY[var1]) / 16.0 * 0.9375;
   }

   private static double fallTime(double var0) {
      return 0.12 * KillEffect.ease(var0 / 0.16);
   }

   private static double partSway(int var0, double var1) {
      double var3 = KillEffect.ease(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double drawGlow(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * KillEffect.ease((var0 - 0.95) / 0.25) * (1.0 - KillEffect.ease((var0 - 1.32 + 0.07) / 0.07));
   }

   public static KillEffect.PartState partPose(KillEffect var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = fallTime(var2);
      double var6 = KillEffect.getTrail(var1, var0.slimArms) / 16.0 * 0.9375;
      double var8 = (24.0 - KillEffect.pART_OFFSETS[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.yaw);
      double var12 = Math.sin(var0.yaw);
      double var14 = var8 * Math.sin(var4);
      Vec3 var16 = var0.origin.add(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new KillEffect.PartState(var16, var4, drawGlow(var2), partSway(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double maxTilt() {
      return 0.12;
   }

   static double partSwayFor(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static Vec3 randomDirection(KillEffect var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.randomFor(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.randomFor(var1 + 14200);
      return new Vec3(Math.cos(var2) * var4, 0.5 + 0.8 * var0.randomFor(var1 + 14400), Math.sin(var2) * var4);
   }

   private Vec3 pieceAnchor(int var1, double var2, Vector3f var4) {
      if (this.pieceParts[var1] == -1) {
         var4.set(this.normalX[var1], this.normalY[var1], this.normalZ[var1]).normalize();
         return new Vec3(this.pose.origin.x + this.offsetX[var1], this.pose.getTrail().getCenterHeight() + 0.02, this.pose.origin.z + this.offsetZ[var1]);
      } else if (this.pieceParts[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.pose.origin.add(this.offsetX[var1], this.offsetY[var1], this.offsetZ[var1]);
      } else {
         KillEffect.PartState var5 = partPose(this.pose, this.pieceParts[var1], var2);
         this.rotation.identity().rotateY((float)(Math.PI - this.pose.yaw)).rotateXYZ((float)var5.rotX(), (float)var5.rotY(), (float)var5.rotZ());
         double var6 = 0.05859375;
         this.rotation
            .transform(this.scratchVector.set((float)(-this.offsetX[var1] * var6), (float)(-this.offsetY[var1] * var6), (float)(this.offsetZ[var1] * var6)));
         this.rotation.transform(var4.set((float)(-this.normalX[var1]), (float)(-this.normalY[var1]), (float)this.normalZ[var1]));
         return var5.position().add(this.scratchVector.x, this.scratchVector.y, this.scratchVector.z);
      }
   }

   private static double polarAngle(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double azimuthAngle(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public ShatterEffect.ShatterPiece pieceAt(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - KillEffect.ease((var2 - 3.45 - 0.45 * this.seedB[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.pieceParts[var1] == -2 ? 0.0 : KillEffect.ease((var2 - this.appearTime[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         Vec3 var9 = this.pieceAnchor(var1, var2, var4);
         double var60 = azimuthAngle(var4) + (this.seedA[var1] - 0.5) * 0.7;
         double var61 = polarAngle(var4) + (this.seedB[var1] - 0.5) * 0.6;
         double var62 = this.pieceSize[var1] * var59;
         return new ShatterEffect.ShatterPiece(
            var9.add(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.seedA[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         Vec3 var7 = this.pieceAnchor(var1, 1.32, var4);
         if (this.pieceParts[var1] != -2) {
            var7 = var7.add(var4.x * this.pieceSize[var1] * 0.35, var4.y * this.pieceSize[var1] * 0.35, var4.z * this.pieceSize[var1] * 0.35);
         }

         double var8 = azimuthAngle(var4) + (this.seedA[var1] - 0.5) * 0.7;
         double var10 = polarAngle(var4) + (this.seedB[var1] - 0.5) * 0.6;
         double var12 = (this.seedA[var1] - 0.5) * 0.4;
         double var14 = var7.x - this.pose.origin.x;
         double var16 = var7.z - this.pose.origin.z;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.seedA[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.seedB[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.driftSpeed[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.driftSpeed[var1] + var14 / var18 * var63;
         double var26 = this.launchSpeed[var1];
         double var28 = this.pieceParts[var1] == -2 ? this.pieceSize[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.pieceSize[var1];
         double var30 = Math.PI / 2;
         double var32 = CrystalMesh.minHeight(var28, var30, 0.0);
         double var34 = this.pose.getTrail().getCenterHeight();
         double var36 = fallTime(var7.y, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.pose.getTrail().getGroundHeight(var7.x + var22 * var36, var7.z + var24 * var36);
            var34 = Double.isNaN(var39) ? this.pose.getTrail().getCenterHeight() - 4.5 : var39;
            var36 = fallTime(var7.y, var26, var34 + var32);
         }

         double var64 = var2 - 1.32;
         double var40 = 0.28 * Math.abs(var26 - 9.8 * var36);
         double var42 = 2.0 * var40 / 9.8;
         double var45 = Math.min(var64, var36);
         double var47 = 0.0;
         Vec3 var44;
         if (var64 < var36) {
            var44 = var7.add(var22 * var64, var26 * var64 - 4.9 * var64 * var64, var24 * var64);
         } else if (var64 < var36 + var42) {
            double var49 = var64 - var36;
            var44 = new Vec3(
               var7.x + var22 * (var36 + 0.45 * var49), var34 + var32 + var40 * var49 - 4.9 * var49 * var49, var7.z + var24 * (var36 + 0.45 * var49)
            );
            var47 = KillEffect.ease(var49 / var42);
         } else {
            var44 = new Vec3(var7.x + var22 * (var36 + 0.45 * var42), var34 + var32, var7.z + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.spinYaw[var1] * var45;
         double var51 = var10 + this.spinPitch[var1] * var45;
         double var53 = var12 + this.spinRoll[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.add(0.0, (CrystalMesh.minHeight(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new ShatterEffect.ShatterPiece(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double fallTime(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void render(EffectsRenderer var0, KillEffect var1, double var2, int var4, int var5, double var6, boolean var8) {
      ShatterEffect var9 = var1.partOffset();
      if (var9 != null) {
         var9.renderEffects(var0, var2, var4, var5, var6, var8);
      }
   }

   private void renderEffects(EffectsRenderer var1, double var2, int var4, int var5, double var6, boolean var8) {
      Vec3 var9 = new Vec3(this.pose.origin.x, this.pose.getTrail().getCenterHeight(), this.pose.origin.z);
      int var10 = ColorUtil.lerpColor(var4, var5, 0.45);
      int var11 = ColorUtil.lerpColor(var5, -1, 0.5);
      double var12 = KillEffect.physicsStart(var2, 0.0, 0.35) * var6;
      drawGlow(var1, this.pose.origin.add(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, ColorUtil.multiplyAlpha(var11, var12 * 0.8));
      double var14 = KillEffect.ease((var2 - 0.05) / 0.7);
      double var16 = 1.0 - KillEffect.ease((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         YinYangRitualEffect.drawDisc(var1, var9.add(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, ColorUtil.multiplyAlpha(var4, var18 * 0.32));
         var1.drawRingFlat(var9.x, var9.y + 0.045, var9.z, 0.3 + 1.25 * var14, 0.12, ColorUtil.multiplyAlpha(var11, var18 * 0.6));
      }

      double var20 = KillEffect.ease((var2 - 0.95) / 0.29000000000000004);
      double var22 = KillEffect.physicsStart(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.drawCrack(var1, var24, Math.min(var2, 1.32), var20, ColorUtil.multiplyAlpha(var11, var22));
         }
      }

      double var41 = KillEffect.physicsStart(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      drawGlow(
         var1, this.pose.origin.add(0.0, 1.05, 0.0), 0.25 + 0.5 * KillEffect.ease((var2 - 1.32 + 0.35) / 0.35), ColorUtil.multiplyAlpha(var11, var41 * 0.7)
      );
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = KillEffect.physicsStart(var26, 0.0, 0.5) * var6;
         drawGlow(var1, this.pose.origin.add(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, ColorUtil.multiplyAlpha(var11, var28));
         if (var26 < 0.7) {
            var1.drawRingFlat(
               var9.x,
               var9.y + 0.06,
               var9.z,
               0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)),
               0.3,
               ColorUtil.multiplyAlpha(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            YinYangRitualEffect.drawCylinderWall(
               var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, ColorUtil.multiplyAlpha(var4, (1.0 - var26 / 1.3) * 0.35 * var6)
            );
         }

         double var30 = KillEffect.physicsStart(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.pose.randomFor(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.pose.randomFor(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.pose.randomFor(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.pose.randomFor(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.drawSprite(
               this.pose.origin.x + Math.cos(var33) * var35,
               this.pose.origin.y + Math.max(0.05, var37),
               this.pose.origin.z + Math.sin(var33) * var35,
               0.022 + 0.02 * this.pose.randomFor(var32 + 15800),
               var33 + var2,
               ColorUtil.multiplyAlpha(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? HitEffectConfig.BlendMode.ADDITIVE : HitEffectConfig.BlendMode.NORMAL);

      for (int var42 = 0; var42 < 102; var42++) {
         ShatterEffect.ShatterPiece var29 = this.pieceAt(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.drawCone(
               var29.position().x,
               var29.position().y,
               var29.position().z,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               ColorUtil.multiplyAlpha(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(HitEffectConfig.BlendMode.ADDITIVE);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            ShatterEffect.ShatterPiece var44 = this.pieceAt(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.drawSprite(var44.position().x, var44.position().y + 0.04, var44.position().z, 0.07, var2, ColorUtil.multiplyAlpha(-1, var46));
               }
            }
         }
      }
   }

   private void drawCrack(EffectsRenderer var1, int var2, double var3, double var5, int var7) {
      int var8 = cRACK_PARTS[var2][0];
      int var9 = cRACK_PARTS[var2][1];
      KillEffect.PartState var10 = partPose(this.pose, var8, var3);
      this.rotation.identity().rotateY((float)(Math.PI - this.pose.yaw)).rotateXYZ((float)var10.rotX(), (float)var10.rotY(), (float)var10.rotZ());
      double[] var11 = pART_SIZES[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.pose.randomFor(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.pose.randomFor(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      Vec3 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.pose.randomFor(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.pose.randomFor(var2 + 16600) < 0.5 ? 1 : -1);
         this.rotation
            .transform(
               this.scratchVector
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         Vec3 var29 = var10.position().add(this.scratchVector.x, this.scratchVector.y, this.scratchVector.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void drawGlow(EffectsRenderer var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.drawSpriteWithType(var1.x, var1.y, var1.z, var2, 0.0, var4, ParticleSystem.Style.GLOW, 0.0, 0.0, 0.0);
      }
   }

   public record ShatterPiece(Vec3 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
