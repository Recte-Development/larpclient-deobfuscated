package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_243;

public final class uq {
   public static final int h = 6;
   private final List<uq.f> K = new ArrayList<>();

   public void s(uq.f var1) {
      if (this.K.size() == 6) {
         this.K.removeFirst();
      }

      this.K.add(var1);
   }

   public void U(double var1) {
      this.K.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void B() {
      this.K.clear();
   }

   public List<uq.f> e() {
      return Collections.unmodifiableList(this.K);
   }

   public static double c(uq.f var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double b(uq.f var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? u9.u.b.E(var3) : 0.0;
   }

   public static double k(uq.f var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = u9.L(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * u9.u.b.E(var5);
      } else {
         return 0.0;
      }
   }

   public record f(
      class_243 source,
      double born,
      double radius,
      double duration,
      double waveWidth,
      double thickness,
      int from,
      int to,
      double opacity,
      double glow,
      List<e6.e> edges,
      uq.o style
   ) {
      public f(
         class_243 var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<e6.e> var18
      ) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, uq.o.R);
      }
   }

   public enum o {
      R,
      Q;
   }
}
