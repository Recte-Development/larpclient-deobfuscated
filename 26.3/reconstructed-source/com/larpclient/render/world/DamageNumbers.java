package com.larpclient.render.world;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class DamageNumbers {
   public static final int mAX_TRACKED = 512;
   public static final int mAX_NUMBERS = 256;
   public static final double lIFETIME = 2.5;
   private final HashMap<Integer, DamageNumbers.HealthSample> lastHealth = new HashMap<>();
   private final HashMap<Integer, DamageNumbers.ExpectedDamage> expectedDamage = new HashMap<>();
   private final ArrayList<DamageNumbers.FloatingNumber> numbers = new ArrayList<>();
   private final Random random;

   public DamageNumbers(long var1) {
      this.random = new Random(var1);
   }

   public void clear() {
      this.lastHealth.clear();
      this.expectedDamage.clear();
      this.numbers.clear();
   }

   public int numberCount() {
      return this.numbers.size();
   }

   public int trackedCount() {
      return this.lastHealth.size();
   }

   public void forget(int var1) {
      this.expectedDamage.remove(var1);
   }

   public void expectDamage(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4)) {
         if (this.expectedDamage.size() < 512 || this.expectedDamage.containsKey(var1)) {
            this.expectedDamage.put(var1, new DamageNumbers.ExpectedDamage(var2, var4 + 2.5));
            DamageNumbers.HealthSample var6 = this.lastHealth.get(var1);
            if ((var6 == null || var6.identity != var2) && this.lastHealth.size() < 512) {
               this.lastHealth.put(var1, new DamageNumbers.HealthSample(var2, var3, var4));
            }
         }
      }
   }

   public void trackHealth(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         DamageNumbers.HealthSample var17 = this.lastHealth.get(var1);
         if (var17 != null || this.lastHealth.size() < 512) {
            this.lastHealth.put(var1, new DamageNumbers.HealthSample(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               DamageNumbers.ExpectedDamage var19 = this.expectedDamage.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.numbers.size() >= 256) {
                     this.numbers.removeFirst();
                  }

                  this.numbers
                     .add(
                        new DamageNumbers.FloatingNumber(
                           var2,
                           formatDamage(var18),
                           var18,
                           var4 + this.random.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.random.nextDouble(-0.05, 0.12),
                           var8 + this.random.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.expectedDamage.remove(var1);
            }
         }
      }
   }

   public void prune(double var1, Predicate<Object> var3) {
      this.lastHealth.entrySet().removeIf(var3x -> var1 - var3x.getValue().seen > 2.5 || var3.test(var3x.getValue().identity));
      this.expectedDamage.entrySet().removeIf(var3x -> var1 > var3x.getValue().until || var3.test(var3x.getValue().identity));
      this.numbers.removeIf(var3x -> var1 - var3x.born >= var3x.lifetime || var3.test(var3x.owner));
   }

   public List<DamageNumbers.NumberSnapshot> snapshot(double var1) {
      ArrayList var3 = new ArrayList(this.numbers.size());

      for (DamageNumbers.FloatingNumber var5 : this.numbers) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(
               new DamageNumbers.NumberSnapshot(var5.text, var5.damage, var5.posX, var5.posY + var6 * 0.55, var5.posZ, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6))
            );
         }
      }

      return List.copyOf(var3);
   }

   public static String formatDamage(float var0) {
      return BigDecimal.valueOf(var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int damageColor(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   private record ExpectedDamage(Object identity, double until) {
   }

   private record FloatingNumber(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
      private final double posX;
      private final double posY;
      private final double posZ;

      private FloatingNumber(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
         this.owner = owner;
         this.text = text;
         this.damage = damage;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.born = born;
         this.lifetime = lifetime;
      }

      public double getX() {
         return this.posX;
      }

      public double getY() {
         return this.posY;
      }

      public double getZ() {
         return this.posZ;
      }
   }

   private record HealthSample(Object identity, float value, double seen) {
   }

   public record NumberSnapshot(String text, float damage, double x, double y, double z, double opacity, double scale) {
      private final double posX;
      private final double posY;
      private final double posZ;

      public NumberSnapshot(String text, float damage, double x, double y, double z, double opacity, double scale) {
         this.text = text;
         this.damage = damage;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.opacity = opacity;
         this.scale = scale;
      }

      public double getX() {
         return this.posX;
      }

      public double getY() {
         return this.posY;
      }

      public double getZ() {
         return this.posZ;
      }
   }
}
