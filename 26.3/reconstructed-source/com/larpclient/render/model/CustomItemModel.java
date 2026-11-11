package com.larpclient.render.model;

public enum CustomItemModel {
   bAYONET("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   sTILETTO("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   bUTTERFLY("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   lUXURY_WATCH("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   lEDGER_NANO_S("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String displayName;
   public final String modelId;
   public final double scale;
   public final double length;
   public final double thickness;
   public final float offsetX;
   public final float offsetY;
   public final float offsetZ;

   CustomItemModel(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.displayName = var3;
      this.modelId = var4;
      this.scale = var5;
      this.length = var7;
      this.thickness = var9;
      this.offsetX = var11;
      this.offsetY = var12;
      this.offsetZ = var13;
   }

   public static CustomItemModel fromDisplayName(String var0) {
      for (CustomItemModel var4 : values()) {
         if (var4.displayName.equals(var0)) {
            return var4;
         }
      }

      return bAYONET;
   }

   public boolean isAccessory() {
      return this == lUXURY_WATCH || this == lEDGER_NANO_S;
   }

   public boolean hasSwingAnimation() {
      return this != lEDGER_NANO_S;
   }
}
