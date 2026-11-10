package com.larpclient;

import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import net.minecraft.class_2558.class_10606;
import net.minecraft.class_2568.class_10613;

public record i2(String title, String detail, int x, int y, int z) {
   public static final int NO_Y = Integer.MIN_VALUE;
   private static final int DIM = 5592405;
   private static final int BRAND = 16733695;
   private static final int COORDINATES = 16777215;
   private static final int MUTED = 11184810;
   private static final String SEPARATOR = " · ";

   public static i2 chunk(String var0, String var1, int var2, int var3) {
      return new i2(var0, var1, var2 + 8, Integer.MIN_VALUE, var3 + 8);
   }

   public static i2 spawners(int var0, int var1, h5.n var2) {
      List var3 = var2.hits();
      if (var3.isEmpty()) {
         return new i2(spawnerTitle("Spawner", var2), "", var0 + 8, var2.sampleY(), var1 + 8);
      }

      h5.v var4 = (h5.v)var3.getFirst();
      return new i2(spawnerTitle(var4.name(), var2), "", var0 + var4.x(), var4.y(), var1 + var4.z());
   }

   private static String spawnerTitle(String var0, h5.n var1) {
      int var2 = var1.spawners();
      if (var2 <= 1) {
         return var0;
      }

      int var3 = !var1.hits().isEmpty() && var1.hits().size() != var2 ? 0 : 1;

      for (h5.v var5 : var1.hits()) {
         var3 &= var5.name().equals(var0);
      }

      return var3 != 0 ? var0 + " x" + var2 : var0 + " +" + (var2 - 1) + " more";
   }

   public static i2 base(int var0, int var1, z var2) {
      String var3 = var2.dedicated() > 0 ? var2.clue() : "Possible build";
      if (var2.bounds() == null) {
         return new i2(var3, var2.counts(), var0 + 8, var2.sampleY(), var1 + 8);
      }

      class_243 var4 = var2.bounds().method_1005();
      return new i2(var3, var2.counts(), var0 + (int)Math.floor(var4.field_1352), (int)Math.floor(var4.field_1351), var1 + (int)Math.floor(var4.field_1350));
   }

   public i2 zone(int var1, int var2, int var3) {
      return new i2(this.title, var3 + " chunks" + (this.detail.isEmpty() ? "" : " · " + this.detail), var1, Integer.MIN_VALUE, var2);
   }

   public String coordinates() {
      return this.y == Integer.MIN_VALUE ? this.x + ", " + this.z : this.x + ", " + this.y + ", " + this.z;
   }

   public String clipboard() {
      return this.coordinates().replace(",", "");
   }

   public String summary(double var1) {
      return this.title + " · " + this.coordinates() + " · " + Math.round(var1) + "m";
   }

   public class_5250 chatLine(String var1, int var2, int var3, int var4, double var5) {
      class_5250 var7 = class_2561.method_43470(var1 + "\nChunk " + var2 + ", " + var3 + "\nClick to copy coordinates");
      class_5250 var8 = class_2561.method_43473()
         .method_27694(var2x -> var2x.method_10958(new class_10606(this.clipboard())).method_10949(new class_10613(var7)));
      var8.method_10852(text("[", 5592405))
         .method_10852(class_2561.method_43470("Larp").method_27694(var0 -> var0.method_36139(16733695).method_10982(true)))
         .method_10852(text("] ", 5592405));
      var8.method_10852(text(this.title, var4 & 16777215)).method_10852(text(" · ", 5592405)).method_10852(text(this.coordinates(), 16777215));
      var8.method_10852(text(" · ", 5592405)).method_10852(text(Math.round(var5) + "m", 11184810));
      if (!this.detail.isEmpty()) {
         var8.method_10852(text(" · ", 5592405)).method_10852(text(this.detail, 11184810));
      }

      return var8;
   }

   private static class_5250 text(String var0, int var1) {
      return class_2561.method_43470(var0).method_27694(var1x -> var1x.method_36139(var1));
   }
}
