package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class es {
   public static final int D = 512;
   public static final int y = 256;
   public static final double i = 2.5;
   private final HashMap<Integer, es.r> r = new HashMap<>();
   private final HashMap<Integer, es.t> L = new HashMap<>();
   private final ArrayList<es.u> B = new ArrayList<>();
   private final Random v;

   public es(long var1) {
      this.v = new Random(var1);
   }

   public void o() {
      this.r.clear();
      this.L.clear();
      this.B.clear();
   }

   public int p() {
      return this.B.size();
   }

   public int q() {
      return this.r.size();
   }

   public void j(int var1) {
      this.L.remove(var1);
   }

   public void V(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4)) {
         if (this.L.size() < 512 || this.L.containsKey(var1)) {
            this.L.put(var1, new es.t(var2, var4 + 2.5));
            es.r var6 = this.r.get(var1);
            if ((var6 == null || var6.identity != var2) && this.r.size() < 512) {
               this.r.put(var1, new es.r(var2, var3, var4));
            }
         }
      }
   }

   public void L(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         es.r var17 = this.r.get(var1);
         if (var17 != null || this.r.size() < 512) {
            this.r.put(var1, new es.r(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               es.t var19 = this.L.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.B.size() >= 256) {
                     this.B.removeFirst();
                  }

                  this.B
                     .add(
                        new es.u(
                           var2,
                           v(var18),
                           var18,
                           var4 + this.v.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.v.nextDouble(-0.05, 0.12),
                           var8 + this.v.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.L.remove(var1);
            }
         }
      }
   }

   public void G(double var1, Predicate<Object> var3) {
      this.r.entrySet().removeIf(var3x -> var1 - var3x.getValue().seen > 2.5 || var3.test(var3x.getValue().identity));
      this.L.entrySet().removeIf(var3x -> var1 > var3x.getValue().until || var3.test(var3x.getValue().identity));
      this.B.removeIf(var3x -> var1 - var3x.born >= var3x.lifetime || var3.test(var3x.owner));
   }

   public List<es.i> N(double var1) {
      ArrayList var3 = new ArrayList(this.B.size());

      for (es.u var5 : this.B) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(new es.i(var5.text, var5.damage, var5.x, var5.y + var6 * 0.55, var5.z, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6)));
         }
      }

      return List.copyOf(var3);
   }

   public static String v(float var0) {
      return BigDecimal.valueOf(var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int H(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   public record i(String text, float damage, double x, double y, double z, double opacity, double scale) {
   }

   private record r(Object identity, float value, double seen) {
   }

   private record t(Object identity, double until) {
   }

   private record u(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
   }
}
