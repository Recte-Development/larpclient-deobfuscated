package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class jz {
   public static final int U = 512;
   public static final int G = 256;
   public static final double g = 2.5;
   private final HashMap<Integer, jz.a> u = new HashMap<>();
   private final HashMap<Integer, jz.n> H = new HashMap<>();
   private final ArrayList<jz.t> x = new ArrayList<>();
   private final Random Q;

   public jz(long var1) {
      this.Q = new Random(var1);
   }

   public void f() {
      this.u.clear();
      this.H.clear();
      this.x.clear();
   }

   public int T() {
      return this.x.size();
   }

   public int g() {
      return this.u.size();
   }

   public void N(int var1) {
      this.H.remove(var1);
   }

   public void I(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4)) {
         if (this.H.size() < 512 || this.H.containsKey(var1)) {
            this.H.put(var1, new jz.n(var2, var4 + 2.5));
            jz.a var6 = this.u.get(var1);
            if ((var6 == null || var6.identity != var2) && this.u.size() < 512) {
               this.u.put(var1, new jz.a(var2, var3, var4));
            }
         }
      }
   }

   public void D(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         jz.a var17 = this.u.get(var1);
         if (var17 != null || this.u.size() < 512) {
            this.u.put(var1, new jz.a(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               jz.n var19 = this.H.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.x.size() >= 256) {
                     this.x.removeFirst();
                  }

                  this.x
                     .add(
                        new jz.t(
                           var2,
                           M(var18),
                           var18,
                           var4 + this.Q.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.Q.nextDouble(-0.05, 0.12),
                           var8 + this.Q.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.H.remove(var1);
            }
         }
      }
   }

   public void Q(double var1, Predicate<Object> var3) {
      this.u.entrySet().removeIf(var3x -> var1 - var3x.getValue().seen > 2.5 || var3.test(var3x.getValue().identity));
      this.H.entrySet().removeIf(var3x -> var1 > var3x.getValue().until || var3.test(var3x.getValue().identity));
      this.x.removeIf(var3x -> var1 - var3x.born >= var3x.lifetime || var3.test(var3x.owner));
   }

   public List<jz.o> U(double var1) {
      ArrayList var3 = new ArrayList(this.x.size());

      for (jz.t var5 : this.x) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(new jz.o(var5.text, var5.damage, var5.x, var5.y + var6 * 0.55, var5.z, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6)));
         }
      }

      return List.copyOf(var3);
   }

   public static String M(float var0) {
      return BigDecimal.valueOf(var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int C(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   private record a(Object identity, float value, double seen) {
   }

   private record n(Object identity, double until) {
   }

   public record o(String text, float damage, double x, double y, double z, double opacity, double scale) {
   }

   private record t(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
   }
}
