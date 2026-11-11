package com.larpclient.render.particle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public final class ParticleStyles {
   public static final List<String> sTYLE_NAMES = List.of(
      "Ripple", "Shockwave", "Rune", "Bloom", "Corona", "Vortex", "Spectrum", "Cloud", "End Rod", "Crit", "Flame", "Soul Flame", "Happy Villager"
   );

   public static int customStyleIndex(String var0) {
      int var1 = sTYLE_NAMES.indexOf(var0);
      return var1 >= 0 && var1 < 7 ? var1 : -1;
   }

   public static boolean isVanillaStyle(String var0) {
      return sTYLE_NAMES.indexOf(var0) >= 7;
   }

   public static boolean isKnownStyle(String var0) {
      return sTYLE_NAMES.contains(var0);
   }

   public static ParticleOptions vanillaParticle(String var0) {
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

   public static List<Vec3> ringPoints(Vec3 var0, double var1, int var3) {
      int var4 = Math.clamp(var3, 4, 64);
      ArrayList var5 = new ArrayList(var4);

      for (int var6 = 0; var6 < var4; var6++) {
         double var7 = (Math.PI * 2) * var6 / var4;
         var5.add(var0.add(Math.cos(var7) * var1, 0.05, Math.sin(var7) * var1));
      }

      return List.copyOf(var5);
   }

   private ParticleStyles() {
   }

   public record StyleSpec(Vec3 source, double born, float seed) {
   }

   public static final class StyleState {
      private final ArrayList<ParticleStyles.StyleSpec> age = new ArrayList<>();

      public void tick(ParticleStyles.StyleSpec var1) {
         if (this.age.size() == 6) {
            this.age.removeFirst();
         }

         this.age.add(var1);
      }

      public void isExpired(double var1, double var3) {
         this.age.removeIf(var4 -> var1 - var4.born() >= var3);
      }

      public List<ParticleStyles.StyleSpec> spawn() {
         return Collections.unmodifiableList(this.age);
      }

      public void tick() {
         this.age.clear();
      }
   }
}
