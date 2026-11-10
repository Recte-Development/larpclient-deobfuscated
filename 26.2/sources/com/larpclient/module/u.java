package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class u {
   public float R;
   public float x;

   public u(float var1, float var2) {
      this.R = var1;
      this.x = var2;
   }

   public u H() {
      return new u(this.R, this.x);
   }

   public float c(u var1) {
      return Math.abs(Mth.wrapDegrees(this.R - var1.R)) + Math.abs(Mth.wrapDegrees(this.x - var1.x));
   }

   public Vec3 s() {
      return tv.Z(this.R, this.x);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.R + ", pitch=" + this.x + ")";
   }
}
