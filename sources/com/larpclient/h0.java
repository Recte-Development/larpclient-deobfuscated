package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class h0 {
   public static final int O = 6;
   private final List<h0.n> o = new ArrayList<>();

   public void q(h0.n var1) {
      if (this.o.size() == 6) {
         this.o.removeFirst();
      }

      this.o.add(var1);
   }

   public void z(double var1) {
      this.o.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void Q() {
      this.o.clear();
   }

   public List<h0.n> d() {
      return Collections.unmodifiableList(this.o);
   }

   public static double d(h0.n var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double t(h0.n var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? yc.d.R.v(var3) : 0.0;
   }

   public static double T(h0.n var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = yc.H(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * yc.d.R.v(var5);
      } else {
         return 0.0;
      }
   }

   public enum j {
      w,
      L;
   }

   public record n(
      Vec3 source,
      double born,
      double radius,
      double duration,
      double waveWidth,
      double thickness,
      int from,
      int to,
      double opacity,
      double glow,
      List<jr.l> edges,
      h0.j style
   ) {
      public n(Vec3 var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<jr.l> var18) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, h0.j.w);
      }
   }
}
