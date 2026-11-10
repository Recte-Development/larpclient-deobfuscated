package com.larpclient;

public record e0(int columns, int cardWidth, int height) {
   public static final int CARD_HEIGHT = 48;
   public static final int GAP = 8;

   public static e0 calculate(int var0, int var1) {
      int var2 = Math.clamp((var0 + 8) / 200, 1, 3);
      int var3 = Math.max(1, (var0 - 8 * (var2 - 1)) / var2);
      int var4 = (var1 + var2 - 1) / var2;
      return new e0(var2, var3, Math.max(0, var4 * 56 - 8));
   }

   public int x(int var1) {
      return var1 % this.columns * (this.cardWidth + 8);
   }

   public int y(int var1) {
      return var1 / this.columns * 56;
   }
}
