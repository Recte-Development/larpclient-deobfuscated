package com.larpclient.module.combat;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class Rotation {
   public float yaw;
   public float pitch;

   public Rotation(float var1, float var2) {
      this.yaw = var1;
      this.pitch = var2;
   }

   public Rotation copy() {
      return new Rotation(this.yaw, this.pitch);
   }

   public float distanceTo(Rotation var1) {
      return Math.abs(Mth.wrapDegrees(this.yaw - var1.yaw)) + Math.abs(Mth.wrapDegrees(this.pitch - var1.pitch));
   }

   public Vec3 toDirection() {
      return RotationMath.lookVector(this.yaw, this.pitch);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
   }
}
