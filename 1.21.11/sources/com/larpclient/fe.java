package com.larpclient;

import java.util.ArrayList;

public record fe(int light, int kelp, int bamboo, int berries, int vines, int dripstone) {
   public static final fe EMPTY = new fe(0, 0, 0, 0, 0, 0);

   public int score() {
      return 6 * Math.min(this.light, 16)
         + 2 * (Math.min(this.kelp, 6) + Math.min(this.bamboo, 6) + Math.min(this.berries, 5) + Math.min(this.vines, 6) + Math.min(this.dripstone, 5));
   }

   public fe plus(fe var1) {
      return new fe(
         Math.min(16, this.light + var1.light),
         Math.min(6, this.kelp + var1.kelp),
         Math.min(6, this.bamboo + var1.bamboo),
         Math.min(5, this.berries + var1.berries),
         Math.min(6, this.vines + var1.vines),
         Math.min(5, this.dripstone + var1.dripstone)
      );
   }

   public String describe() {
      ArrayList var1 = new ArrayList();
      if (this.light > 0) {
         var1.add(this.light + " low-light clues");
      }

      if (this.kelp > 0) {
         var1.add(this.kelp + " kelp");
      }

      if (this.bamboo > 0) {
         var1.add(this.bamboo + " bamboo");
      }

      if (this.berries > 0) {
         var1.add(this.berries + " mature berries");
      }

      if (this.vines > 0) {
         var1.add(this.vines + " vines");
      }

      if (this.dripstone > 0) {
         var1.add(this.dripstone + " dripstone");
      }

      return "Score " + this.score() + " · " + String.join(", ", var1);
   }
}
