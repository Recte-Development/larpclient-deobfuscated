package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class d5 {
   public float M;
   public float G;

   public d5(float var1, float var2) {
      this.M = var1;
      this.G = var2;
   }

   public d5 S() {
      return new d5(this.M, this.G);
   }

   public float b(d5 var1) {
      return Math.abs(Mth.wrapDegrees(this.M - var1.M)) + Math.abs(Mth.wrapDegrees(this.G - var1.G));
   }

   public Vec3 n() {
      return dj.v(this.M, this.G);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.M + ", pitch=" + this.G + ")";
   }
}
