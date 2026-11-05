package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class lx {
   public float z;
   public float I;

   public lx(float var1, float var2) {
      this.z = var1;
      this.I = var2;
   }

   public lx j() {
      return new lx(this.z, this.I);
   }

   public float t(lx var1) {
      return Math.abs(Mth.wrapDegrees(this.z - var1.z)) + Math.abs(Mth.wrapDegrees(this.I - var1.I));
   }

   public Vec3 t() {
      return le.W(this.z, this.I);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.z + ", pitch=" + this.I + ")";
   }
}
