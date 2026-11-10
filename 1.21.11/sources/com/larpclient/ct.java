package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class ct {
   public static final int A = 512;
   public static final int c = 256;
   public static final double C = 2.5;
   private final HashMap<Integer, ct.x> G = new HashMap<>();
   private final HashMap<Integer, ct.m> m = new HashMap<>();
   private final ArrayList<ct.a> w = new ArrayList<>();
   private final Random r;

   public ct(long var1) {
      this.r = new Random(var1);
   }

   public void m() {
      this.G.clear();
      this.m.clear();
      this.w.clear();
   }

   public int b() {
      return this.w.size();
   }

   public int R() {
      return this.G.size();
   }

   public void X(int var1) {
      this.m.remove(var1);
   }

   public void m(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4)) {
         if (this.m.size() < 512 || this.m.containsKey(var1)) {
            this.m.put(var1, new ct.m(var2, var4 + 2.5));
            ct.x var6 = this.G.get(var1);
            if ((var6 == null || var6.identity != var2) && this.G.size() < 512) {
               this.G.put(var1, new ct.x(var2, var3, var4));
            }
         }
      }
   }

   public void U(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         ct.x var17 = this.G.get(var1);
         if (var17 != null || this.G.size() < 512) {
            this.G.put(var1, new ct.x(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               ct.m var19 = this.m.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.w.size() >= 256) {
                     this.w.removeFirst();
                  }

                  this.w
                     .add(
                        new ct.a(
                           var2,
                           m(var18),
                           var18,
                           var4 + this.r.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.r.nextDouble(-0.05, 0.12),
                           var8 + this.r.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.m.remove(var1);
            }
         }
      }
   }

   public void E(double var1, Predicate<Object> var3) {
      this.G.entrySet().removeIf(var3x -> var1 - var3x.getValue().seen > 2.5 || var3.test(var3x.getValue().identity));
      this.m.entrySet().removeIf(var3x -> var1 > var3x.getValue().until || var3.test(var3x.getValue().identity));
      this.w.removeIf(var3x -> var1 - var3x.born >= var3x.lifetime || var3.test(var3x.owner));
   }

   public List<ct.t> Y(double var1) {
      ArrayList var3 = new ArrayList(this.w.size());

      for (ct.a var5 : this.w) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(new ct.t(var5.text, var5.damage, var5.x, var5.y + var6 * 0.55, var5.z, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6)));
         }
      }

      return List.copyOf(var3);
   }

   public static String m(float var0) {
      return BigDecimal.valueOf(var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int s(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   private record a(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
   }

   private record m(Object identity, double until) {
   }

   public record t(String text, float damage, double x, double y, double z, double opacity, double scale) {
   }

   private record x(Object identity, float value, double seen) {
   }
}
