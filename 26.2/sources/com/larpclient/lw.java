package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class lw {
   public static final int z = 6;
   private final List<lw.q> d = new ArrayList<>();

   public void U(lw.q var1) {
      if (this.d.size() == 6) {
         this.d.removeFirst();
      }

      this.d.add(var1);
   }

   public void V(double var1) {
      this.d.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void I() {
      this.d.clear();
   }

   public List<lw.q> T() {
      return Collections.unmodifiableList(this.d);
   }

   public static double L(lw.q var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double l(lw.q var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? st.z.J.x(var3) : 0.0;
   }

   public static double i(lw.q var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = st.h(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * st.z.J.x(var5);
      } else {
         return 0.0;
      }
   }

   public record q(
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
      List<lp.b> edges,
      lw.u style
   ) {
      public q(Vec3 var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<lp.b> var18) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, lw.u.h);
      }
   }

   public enum u {
      h,
      b;
   }
}
