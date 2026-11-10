package com.larpclient;

public record uz(int width, int height, int rowHeight, int menuWidth, int menuY, int logoSize, int wordmarkWidth) {
   public static final int WORDMARK_WIDTH = 57;
   public static final int PLAY_ROWS = 3;
   public static final int ACTIONS = 6;

   public static uz calculate(int var0, int var1) {
      return calculate(var0, var1, 57);
   }

   public static uz calculate(int var0, int var1, int var2) {
      int var3 = utilityHeight(var1);
      int var4 = var1 < 220 ? 16 : (var1 < 300 ? 22 : (var1 < 360 ? 30 : 38));
      int var5 = utilityHeight(var1);
      int var6 = gap(var1);
      int var7 = groupGap(var1);
      int var8 = var4 + brandGap(var1);
      int var9 = var8 + var3 * 3 + var6 * 2 + var7 + var5 * 2 + var6 + quitGap(var1) + quitHeight(var1);
      int var10 = Math.max(34, (var1 - var9) / 2 - 4);
      return new uz(var0, var1, var3, Math.min(208, var0 - 40), var10 + var8, var4, Math.max(1, var2));
   }

   private static int gap(int var0) {
      return var0 < 220 ? 2 : 4;
   }

   private static int groupGap(int var0) {
      return var0 < 220 ? 5 : (var0 < 300 ? 8 : 12);
   }

   private static int utilityHeight(int var0) {
      return var0 < 220 ? 18 : (var0 < 240 ? 20 : (var0 < 300 ? 22 : 28));
   }

   private static int quitGap(int var0) {
      return var0 < 220 ? 4 : (var0 < 300 ? 8 : 12);
   }

   private static int quitHeight(int var0) {
      return var0 < 220 ? 14 : 18;
   }

   private static int brandGap(int var0) {
      return var0 < 220 ? 8 : (var0 < 300 ? 12 : 24);
   }

   public float brandScale() {
      return this.height < 220 ? 1.0F : (this.height < 300 ? 1.25F : 1.75F);
   }

   public int lockupGap() {
      return Math.round(this.logoSize * 0.26F);
   }

   public p1.e navigation(int var1) {
      int var2 = (this.width - this.menuWidth) / 2;
      int var3 = this.menuY + this.rowHeight * 3 + gap(this.height) * 2 + groupGap(this.height);

      return switch (var1) {
         case 0, 1, 2 -> new p1.e(var2, this.menuY + var1 * (this.rowHeight + gap(this.height)), this.menuWidth, this.rowHeight);
         case 3, 4 -> new p1.e(var2, var3 + (var1 - 3) * (utilityHeight(this.height) + gap(this.height)), this.menuWidth, utilityHeight(this.height));
         case 5 -> new p1.e(this.width / 2 - 50, var3 + utilityHeight(this.height) * 2 + gap(this.height) + quitGap(this.height), 100, quitHeight(this.height));
         default -> throw new IllegalArgumentException("Unknown root menu action");
      };
   }

   public p1.e logo() {
      int var1 = (int)Math.ceil(this.wordmarkWidth * this.brandScale());
      return new p1.e(
         (this.width - this.logoSize - this.lockupGap() - var1) / 2, this.menuY - brandGap(this.height) - this.logoSize, this.logoSize, this.logoSize
      );
   }

   public p1.e title() {
      p1.e var1 = this.logo();
      int var2 = (int)Math.ceil(this.wordmarkWidth * this.brandScale());
      int var3 = (int)Math.ceil(9.0F * this.brandScale());
      return new p1.e(var1.x() + this.logoSize + this.lockupGap(), var1.y() + (this.logoSize - var3) / 2, var2, var3);
   }

   public p1.e account() {
      return new p1.e(12, 10, Math.min(140, (this.width - 32) / 2), 22);
   }
}
