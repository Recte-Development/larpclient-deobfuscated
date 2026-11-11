package com.larpclient.gui.screen;

public final class SplashLayout {
   public static final String tITLE = "Larp Client";
   public static final int tITLE_TEXTURE_WIDTH = 512;
   public static final int tITLE_TEXTURE_HEIGHT = 96;
   public static final int mAX_TITLE_WIDTH = 256;
   public static final int tITLE_STRIP_WIDTH = ("Larp Client".length() + 1) * 96;
   public static final int aTLAS_WIDTH = tITLE_STRIP_WIDTH + 256;

   private SplashLayout() {
   }

   public static SplashLayout.Layout computeLayout(int var0, int var1) {
      int var2 = Math.max(12, Math.min(Math.min(104, var1 / 5), var0 / 3));
      int var3 = Math.max(1, Math.min(256, var0 - 32));
      int var4 = Math.max(1, var3 * 96 / 512);
      int var5 = Math.max(8, var2 / 6);
      int var6 = (var1 - var2 - var5 - var4) / 2;
      int var7 = Math.min(Math.min(var0, var1), var2 * 2);
      HomeLayout.Rect var8 = new HomeLayout.Rect((var0 - var2) / 2, var6, var2, var2);
      return new SplashLayout.Layout(
         var8,
         new HomeLayout.Rect((var0 - var3) / 2, var6 + var2 + var5, var3, var4),
         new HomeLayout.Rect((var0 - var7) / 2, var6 + (var2 - var7) / 2, var7, var7)
      );
   }

   public record Layout(HomeLayout.Rect logo, HomeLayout.Rect title, HomeLayout.Rect glow) {
   }
}
