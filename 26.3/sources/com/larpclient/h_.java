package com.larpclient;

public record h_(int left, int top, int width, int height, int contentX, int right, int bodyTop, int bottom, int inspectorX, boolean columns, int inspectorWidth) {
   public h_(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 0);
   }

   public boolean detachedInspector() {
      return this.inspectorWidth > 0;
   }

   public int navigationRight() {
      return this.contentX - 14;
   }

   public boolean containsNavigation(double var1, double var3) {
      return var1 >= this.left && var1 < this.contentX - 8 && var3 >= this.top + 12 && var3 < this.bottom;
   }

   public int moduleCardRight(double var1) {
      return this.moduleListRight(var1) - 10;
   }

   public int moduleScrollbarX(double var1) {
      return this.moduleListRight(var1) - 3;
   }

   public int presetLeft() {
      return this.left + 8;
   }

   public int presetWidth() {
      return Math.min(108, (this.right - this.presetLeft() - 8) / 2);
   }

   public int larpPresetLeft() {
      return this.presetLeft() + this.presetWidth() + 8;
   }

   public int navigationTop() {
      return this.top + 56;
   }

   public int navigationGap() {
      return 22;
   }

   public int accountY() {
      return this.bottom - 36;
   }

   public int navigationBottom() {
      return this.accountY() - 8;
   }

   public int navigationPitch(int var1) {
      return 20;
   }

   public int navigationScrollLimit(int var1) {
      return Math.max(0, this.navigationPitch(var1) * var1 + this.navigationGap() - (this.navigationBottom() - this.navigationTop()));
   }

   public int themeSwitchY() {
      return this.bottom + 6;
   }

   public int themeSwitchHeight() {
      return 20;
   }

   public int profileLeft() {
      return this.larpPresetLeft() + this.presetWidth() + 12;
   }

   public int profileWidth() {
      return Math.max(1, this.right - this.profileLeft() - 4);
   }

   public int profileY() {
      return this.bottom + 12;
   }

   public int helpLeft() {
      return this.left + 12;
   }

   public int helpWidth() {
      return Math.max(1, this.width - 24);
   }

   public int helpY() {
      return this.bottom + 31;
   }

   public int moduleListLeft(double var1) {
      return this.contentX - (int)Math.round(12.0 * (1.0 - Math.clamp(var1, 0.0, 1.0)));
   }

   public int moduleClipLeft() {
      return this.contentX - 6;
   }

   public int moduleListRight(double var1) {
      if (this.columns && !this.detachedInspector()) {
         int var3 = Math.min(72, (this.right - this.contentX) / 8);
         return this.inspectorX - 10 + (int)Math.round(var3 * (1.0 - Math.clamp(var1, 0.0, 1.0)));
      } else {
         return this.right;
      }
   }

   public h_ moduleInspector(double var1) {
      if (this.detachedInspector()) {
         return new h_(
            this.inspectorX,
            this.top,
            this.inspectorWidth,
            this.height,
            this.inspectorX,
            this.inspectorX + this.inspectorWidth,
            this.top + 12,
            this.top + this.height - 10,
            this.inspectorX,
            true
         );
      }

      if (!this.columns) {
         return new h_(this.left, this.top, this.width, this.height, this.contentX, this.right, this.bodyTop, this.bottom, this.contentX, false);
      }

      int var3 = (int)Math.round(12.0 * (1.0 - Math.clamp(var1, 0.0, 1.0)));
      return new h_(
         this.left,
         this.top,
         this.width,
         this.height,
         this.columns ? this.contentX : this.contentX + var3,
         this.right + var3,
         this.bodyTop,
         this.bottom,
         this.inspectorX + var3,
         this.columns
      );
   }

   public int moduleInspectorClipLeft(double var1) {
      if (this.detachedInspector()) {
         return this.inspectorX;
      } else {
         return this.columns ? Math.max(this.moduleListRight(var1) + 10, this.moduleInspector(var1).inspectorX()) : this.contentX;
      }
   }

   public int moduleGridColumns() {
      return this.moduleCardRight(0.0) - this.contentX >= 340 ? 2 : 1;
   }

   public int moduleCardWidth() {
      return Math.max(1, (this.moduleCardRight(0.0) - this.contentX - (this.moduleGridColumns() - 1) * 6) / this.moduleGridColumns());
   }

   public int moduleCardX(int var1, double var2) {
      return this.moduleListLeft(var2) + var1 % this.moduleGridColumns() * (this.moduleCardWidth() + 6);
   }

   public int moduleContentHeight(int var1) {
      return (var1 + this.moduleGridColumns() - 1) / this.moduleGridColumns() * 38;
   }

   public boolean containsWorkspace(double var1, double var3) {
      return var1 >= this.contentX && var1 < this.right && var3 >= this.top + 12 && var3 < this.bottom;
   }

   public h_ offset(int var1, int var2) {
      return new h_(
         this.left + var1,
         this.top + var2,
         this.width,
         this.height,
         this.contentX + var1,
         this.right + var1,
         this.bodyTop + var2,
         this.bottom + var2,
         this.inspectorX + var1,
         this.columns,
         this.inspectorWidth
      );
   }

   public static h_ calculateModules(int var0, int var1, double var2) {
      int var4 = Math.max(1, Math.max(Math.min(530, var0 - 16), Math.min(620, var0 - 268)));
      int var5 = Math.max(1, Math.min(408, var1 - 16));
      int var6 = var0 >= 766 ? Math.min(224, var0 - var4 - 28) : 0;
      int var7 = (var0 - var4 - var6 - 12) / 2;
      int var8 = var6 == 0 ? (var0 - var4) / 2 : var7 + (int)Math.round(8.0 * (1.0 - Math.clamp(var2 * 2.0, 0.0, 1.0)));
      int var9 = (var1 - var5) / 2;
      int var10 = Math.min(118, Math.max(106, var4 / 5));
      int var11 = var8 + var10 + 10;
      int var12 = var8 + var4 - 10;
      int var13 = var6 == 0 ? var11 : var7 + var4 + 12;
      return new h_(var8, var9, var4, var5, var11, var12, var9 + 50, var9 + var5 - 44, var13, var6 > 0, var6);
   }

   public static h_ calculate(int var0, int var1) {
      h_ var2 = calculateModules(var0, var1, 0.0);
      return new h_(var2.left(), var2.top(), var2.width(), var2.height(), var2.contentX(), var2.right(), var2.bodyTop(), var2.bottom(), var2.contentX(), false);
   }
}
