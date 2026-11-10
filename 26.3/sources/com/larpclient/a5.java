package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class a5 {
   public static final int j = 6;
   private final List<a5.h> t = new ArrayList<>();

   public void X(a5.h var1) {
      if (this.t.size() == 6) {
         this.t.removeFirst();
      }

      this.t.add(var1);
   }

   public void P(double var1) {
      this.t.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void L() {
      this.t.clear();
   }

   public List<a5.h> A() {
      return Collections.unmodifiableList(this.t);
   }

   public static double U(a5.h var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double i(a5.h var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? pr.b.Y.p(var3) : 0.0;
   }

   public static double z(a5.h var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = pr.k(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * pr.b.Y.p(var5);
      } else {
         return 0.0;
      }
   }

   public record h(
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
      List<n.p> edges,
      a5.p style
   ) {
      public h(Vec3 var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<n.p> var18) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, a5.p.l);
      }
   }

   public enum p {
      l,
      J;
   }
}
