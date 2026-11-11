package com.larpclient.core;

import com.larpclient.module.misc.Module;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class RotationManager {
   private static RotationManager.Priority activePriority;
   private static Module owner;
   private static float targetYaw;
   private static float targetPitch;
   private static int heldCount;
   private static long lastNoteTime;
   private static Object lastPlayer;
   private static int lastTick = Integer.MIN_VALUE;
   private static float previousYaw;
   private static float previousPitch;
   private static float appliedYaw;
   private static float appliedPitch;

   public static void clear() {
      activePriority = null;
      owner = null;
   }

   public static boolean isActive(RotationManager.Priority var0) {
      return activePriority == var0;
   }

   public static boolean hasRequest() {
      return activePriority != null;
   }

   public static boolean outranks(RotationManager.Priority var0, RotationManager.Priority var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void request(RotationManager.Priority var0, float var1, float var2, double var3) {
      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 != null) {
         requestRotation(var0, var1, var2, var3, var5.getYRot(), var5.getXRot());
      }
   }

   public static void requestRotation(RotationManager.Priority var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == activePriority || outranks(var0, activePriority)) {
         activePriority = var0;
         owner = null;
         float[] var7 = blendAngles(var5, var6, var1, var2, var3);
         targetYaw = var7[0];
         targetPitch = var7[1];
      }
   }

   public static void requestForModule(Module var0, RotationManager.Priority var1, float var2, float var3, double var4) {
      request(var1, var2, var3, var4);
      if (activePriority == var1) {
         owner = var0;
      }
   }

   public static float[] blendAngles(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + Mth.wrapDegrees(var2 - var0) * (float)var6, Mth.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void applyToPlayer() {
      Minecraft var0 = Minecraft.getInstance();
      if (activePriority != null && InteractionArbiter.canModuleInteract(owner) && (owner == null || owner.isRuntimeActive())) {
         if (InteractionArbiter.wasManualClaimed()) {
            requestRotation();
         } else {
            float var1 = var0.player.getYRot();
            float var2 = var0.player.getXRot();
            var0.player.setYRot(targetYaw);
            var0.player.setXRot(targetPitch);
            if (activePriority == RotationManager.Priority.BOT) {
               lastPlayer = var0.player;
               lastTick = var0.player.tickCount;
               previousYaw = var1;
               previousPitch = var2;
               appliedYaw = targetYaw;
               appliedPitch = targetPitch;
            }
         }
      }
   }

   public static float getInterpolatedYaw(LocalPlayer var0, float var1) {
      return !isApplied(var0) ? Float.NaN : previousYaw + Mth.wrapDegrees(appliedYaw - previousYaw) * Mth.clamp(var1, 0.0F, 1.0F);
   }

   public static float getInterpolatedPitch(LocalPlayer var0, float var1) {
      return !isApplied(var0) ? Float.NaN : Mth.lerp(Mth.clamp(var1, 0.0F, 1.0F), previousPitch, appliedPitch);
   }

   private static boolean isApplied(LocalPlayer var0) {
      return var0 == lastPlayer
         && var0.tickCount == lastTick
         && !var0.isPassenger()
         && var0.getYRot() == appliedYaw
         && var0.getXRot() == appliedPitch
         && !ViewController.isFreecam();
   }

   private static void requestRotation() {
      heldCount++;
      long var0 = System.nanoTime();
      if (lastNoteTime == 0L || var0 - lastNoteTime >= 5000000000L) {
         LarpLog.info(
            "Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", activePriority, heldCount
         );
         lastNoteTime = var0;
         heldCount = 0;
      }
   }

   public static float[] calculateAngles(Vec3 var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private RotationManager() {
   }

   public enum Priority {
      VIEW,
      DRAIN,
      ORDER_DROP,
      BOT,
      TUNNEL,
      DEFENSE,
      AURA,
      MACE,
      CART,
      CRYSTAL,
      MLG,
      WIND_CHARGE,
      PEARL;
   }
}
