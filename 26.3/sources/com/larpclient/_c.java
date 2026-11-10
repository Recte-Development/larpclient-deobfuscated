package com.larpclient;

public record _c(int width, int height, int dockX, int dockY, int cell, int columns, int cardY, int cardWidth, int clockY, float clockScale) {
   public static _c calculate(int var0, int var1) {
      int var2 = var0 < 280 ? 26 : 30;
      int var3 = Math.min(7, Math.max(1, (var0 - 24) / var2));
      int var4 = (7 + var3 - 1) / var3;
      int var5 = var1 - 22 - var4 * var2;
      int var6 = Math.min(170, Math.max(76, (var0 - 32) / 2));
      int var7 = var0 >= var3 * var2 + 2 * var6 + 48 ? 1 : 0;
      int var8 = var7 != 0 ? var5 : var5 - 42;
      int var9 = Math.max(24, Math.min((int)(var1 * 0.35), var8 - 80));
      float var10 = (float)Math.clamp(Math.min(var0 / 110.0, var1 / 68.0), 1.7, 4.0);
      return new _c(var0, var1, (var0 - var3 * var2) / 2, var5, var2, var3, var8, var6, var9, var10);
   }

   public _c.f dock(int var1) {
      return new _c.f(this.dockX + var1 % this.columns * this.cell, this.dockY + var1 / this.columns * this.cell, this.cell, this.cell);
   }

   public _c.f backgroundCard() {
      return new _c.f(12, this.cardY, this.cardWidth, 34);
   }

   public _c.f identityCard() {
      return new _c.f(this.width - 12 - this.cardWidth, this.cardY, this.cardWidth, 34);
   }

   public record f(int x, int y, int width, int height) {
      public boolean intersects(_c.f var1) {
         return this.x < var1.x + var1.width && this.x + this.width > var1.x && this.y < var1.y + var1.height && this.y + this.height > var1.y;
      }
   }
}
