package com.larpclient;

public record br(int left, int top, int width, int height, int cardY, int cardWidth, int cardHeight, int statusY, int footerY) {
   public static br calculate(int var0, int var1) {
      int var2 = Math.min(560, var0 - 24);
      int var3 = Math.min(292, var1 - 24);
      int var4 = (var0 - var2) / 2;
      int var5 = (var1 - var3) / 2;
      int var6 = var5 + (var3 >= 220 ? 64 : 44);
      int var7 = var5 + var3 - 36;
      int var8 = var7 - 28;
      int var9 = (var2 - 48) / 3;
      return new br(var4, var5, var2, var3, var6, var9, var8 - 12 - var6, var8, var7);
   }

   public ue.c card(int var1) {
      return new ue.c(this.left + 16 + var1 * (this.cardWidth + 8), this.cardY, this.cardWidth, this.cardHeight);
   }

   public ue.c motion() {
      return new ue.c(this.left + 16, this.footerY, Math.min(112, this.width - 160), 24);
   }

   public ue.c back() {
      return new ue.c(this.left + this.width - 140, this.footerY, 58, 24);
   }

   public ue.c apply() {
      return new ue.c(this.left + this.width - 74, this.footerY, 58, 24);
   }
}
