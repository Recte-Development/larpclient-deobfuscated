package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class ra {
   public static final int y = 512;
   public static final int w = 256;
   public static final double l = 2.5;
   private final HashMap<Integer, ra.y> X = new HashMap<>();
   private final HashMap<Integer, ra.m> p = new HashMap<>();
   private final ArrayList<ra.s> U = new ArrayList<>();
   private final Random B;

   public ra(long var1) {
      this.B = new Random(var1);
   }

   public void B() {
      this.X.clear();
      this.p.clear();
      this.U.clear();
   }

   public int l() {
      return this.U.size();
   }

   public int D() {
      return this.X.size();
   }

   public void H(int var1) {
      this.p.remove(var1);
   }

   public void H(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4)) {
         if (this.p.size() < 512 || this.p.containsKey(var1)) {
            this.p.put(var1, new ra.m(var2, var4 + 2.5));
            ra.y var6 = this.X.get(var1);
            if ((var6 == null || var6.identity != var2) && this.X.size() < 512) {
               this.X.put(var1, new ra.y(var2, var3, var4));
            }
         }
      }
   }

   public void x(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         ra.y var17 = this.X.get(var1);
         if (var17 != null || this.X.size() < 512) {
            this.X.put(var1, new ra.y(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               ra.m var19 = this.p.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.U.size() >= 256) {
                     this.U.removeFirst();
                  }

                  this.U
                     .add(
                        new ra.s(
                           var2,
                           G(var18),
                           var18,
                           var4 + this.B.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.B.nextDouble(-0.05, 0.12),
                           var8 + this.B.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.p.remove(var1);
            }
         }
      }
   }

   public void U(double var1, Predicate<Object> var3) {
      this.X.entrySet().removeIf(var3x -> var1 - var3x.getValue().seen > 2.5 || var3.test(var3x.getValue().identity));
      this.p.entrySet().removeIf(var3x -> var1 > var3x.getValue().until || var3.test(var3x.getValue().identity));
      this.U.removeIf(var3x -> var1 - var3x.born >= var3x.lifetime || var3.test(var3x.owner));
   }

   public List<ra.j> H(double var1) {
      ArrayList var3 = new ArrayList(this.U.size());

      for (ra.s var5 : this.U) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(new ra.j(var5.text, var5.damage, var5.x, var5.y + var6 * 0.55, var5.z, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6)));
         }
      }

      return List.copyOf(var3);
   }

   public static String G(float var0) {
      return BigDecimal.valueOf(var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int w(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   public record j(String text, float damage, double x, double y, double z, double opacity, double scale) {
   }

   private record m(Object identity, double until) {
   }

   private record s(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
   }

   private record y(Object identity, float value, double seen) {
   }
}
