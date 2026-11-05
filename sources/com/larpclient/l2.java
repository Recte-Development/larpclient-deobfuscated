package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public final class l2 {
   public static final List<String> k = List.of(
      "Ripple", "Shockwave", "Rune", "Bloom", "Corona", "Vortex", "Spectrum", "Cloud", "End Rod", "Crit", "Flame", "Soul Flame", "Happy Villager"
   );

   public static int N(String var0) {
      int var1 = k.indexOf(var0);
      return var1 >= 0 && var1 < 7 ? var1 : -1;
   }

   public static boolean V(String var0) {
      return k.indexOf(var0) >= 7;
   }

   public static boolean n(String var0) {
      return k.contains(var0);
   }

   public static ParticleOptions i(String var0) {
      return switch (var0) {
         case "Cloud" -> ParticleTypes.CLOUD;
         case "End Rod" -> ParticleTypes.END_ROD;
         case "Crit" -> ParticleTypes.CRIT;
         case "Flame" -> ParticleTypes.FLAME;
         case "Soul Flame" -> ParticleTypes.SOUL_FIRE_FLAME;
         case "Happy Villager" -> ParticleTypes.HAPPY_VILLAGER;
         default -> null;
      };
   }

   public static List<Vec3> s(Vec3 var0, double var1, int var3) {
      int var4 = Math.clamp(var3, 4, 64);
      ArrayList var5 = new ArrayList(var4);

      for (int var6 = 0; var6 < var4; var6++) {
         double var7 = (Math.PI * 2) * var6 / var4;
         var5.add(var0.add(Math.cos(var7) * var1, 0.05, Math.sin(var7) * var1));
      }

      return List.copyOf(var5);
   }

   private l2() {
   }

   public record k(Vec3 source, double born, float seed) {
   }

   public static final class s {
      private final ArrayList<l2.k> q = new ArrayList<>();

      public void M(l2.k var1) {
         if (this.q.size() == 6) {
            this.q.removeFirst();
         }

         this.q.add(var1);
      }

      public void W(double var1, double var3) {
         this.q.removeIf(var4 -> var1 - var4.born() >= var3);
      }

      public List<l2.k> F() {
         return Collections.unmodifiableList(this.q);
      }

      public void O() {
         this.q.clear();
      }
   }
}
