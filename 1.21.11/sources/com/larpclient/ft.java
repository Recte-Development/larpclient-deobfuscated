package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_243;

public final class ft {
   public static final List<String> v = List.of(
      "Ripple", "Shockwave", "Rune", "Bloom", "Corona", "Vortex", "Spectrum", "Cloud", "End Rod", "Crit", "Flame", "Soul Flame", "Happy Villager"
   );

   public static int U(String var0) {
      int var1 = v.indexOf(var0);
      return var1 >= 0 && var1 < 7 ? var1 : -1;
   }

   public static boolean b(String var0) {
      return v.indexOf(var0) >= 7;
   }

   public static boolean F(String var0) {
      return v.contains(var0);
   }

   public static class_2394 y(String var0) {
      return switch (var0) {
         case "Cloud" -> class_2398.field_11204;
         case "End Rod" -> class_2398.field_11207;
         case "Crit" -> class_2398.field_11205;
         case "Flame" -> class_2398.field_11240;
         case "Soul Flame" -> class_2398.field_22246;
         case "Happy Villager" -> class_2398.field_11211;
         default -> null;
      };
   }

   public static List<class_243> C(class_243 var0, double var1, int var3) {
      int var4 = Math.clamp(var3, 4, 64);
      ArrayList var5 = new ArrayList(var4);

      for (int var6 = 0; var6 < var4; var6++) {
         double var7 = (Math.PI * 2) * var6 / var4;
         var5.add(var0.method_1031(Math.cos(var7) * var1, 0.05, Math.sin(var7) * var1));
      }

      return List.copyOf(var5);
   }

   private ft() {
   }

   public static final class j {
      private final ArrayList<ft.m> n = new ArrayList<>();

      public void A(ft.m var1) {
         if (this.n.size() == 6) {
            this.n.removeFirst();
         }

         this.n.add(var1);
      }

      public void Y(double var1, double var3) {
         this.n.removeIf(var4 -> var1 - var4.born() >= var3);
      }

      public List<ft.m> D() {
         return Collections.unmodifiableList(this.n);
      }

      public void k() {
         this.n.clear();
      }
   }

   public record m(class_243 source, double born, float seed) {
   }
}
