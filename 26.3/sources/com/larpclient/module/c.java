package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class c {
   public float V;
   public float T;

   public c(float var1, float var2) {
      this.V = var1;
      this.T = var2;
   }

   public c n() {
      return new c(this.V, this.T);
   }

   public float z(c var1) {
      return Math.abs(Mth.wrapDegrees(this.V - var1.V)) + Math.abs(Mth.wrapDegrees(this.T - var1.T));
   }

   public Vec3 g() {
      return w.Y(this.V, this.T);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.V + ", pitch=" + this.T + ")";
   }
}
