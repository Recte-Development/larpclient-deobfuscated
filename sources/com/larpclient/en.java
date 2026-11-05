package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class en {
   public static final int W = 6;
   private final List<en.j> T = new ArrayList<>();

   public void G(en.j var1) {
      if (this.T.size() == 6) {
         this.T.removeFirst();
      }

      this.T.add(var1);
   }

   public void z(double var1) {
      this.T.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void F() {
      this.T.clear();
   }

   public List<en.j> J() {
      return Collections.unmodifiableList(this.T);
   }

   public static double Z(en.j var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double V(en.j var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? hp.y.H.Z(var3) : 0.0;
   }

   public static double M(en.j var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = hp.R(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * hp.y.H.Z(var5);
      } else {
         return 0.0;
      }
   }

   public record j(
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
      List<zg.r> edges,
      en.o style
   ) {
      public j(Vec3 var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<zg.r> var18) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, en.o.p);
      }
   }

   public enum o {
      p,
      E;
   }
}
