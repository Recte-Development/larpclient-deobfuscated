package com.larpclient.update;

import com.larpclient.gui.screen.HomeLayout;
import com.larpclient.gui.screen.MainMenuLayout;

public final class UpdateLayout {
   private UpdateLayout() {
   }

   public static UpdateLayout.Notice notice(int var0, int var1) {
      MainMenuLayout var2 = MainMenuLayout.calculate(var0, var1);
      int var3 = var2.navigation(0).left() - 24;
      if (var3 >= 180 && var1 >= 300) {
         int var6 = Math.min(208, var3);
         int var7 = var1 - 84;
         return new UpdateLayout.Notice(
            new HomeLayout.Rect(12, var7, var6, 72),
            new HomeLayout.Rect(20, var7 + 44, var6 - 80, 22),
            new HomeLayout.Rect(12 + var6 - 64, var7 + 44, 56, 22),
            false
         );
      } else {
         int var4 = Math.max(1, Math.min(144, (var0 - 40) / 2));
         HomeLayout.Rect var5 = new HomeLayout.Rect(var0 - 12 - var4, 10, var4, 22);
         return new UpdateLayout.Notice(var5, var5, new HomeLayout.Rect(0, 0, 0, 0), true);
      }
   }

   public static UpdateLayout.Dialog dialog(int var0, int var1) {
      int var2 = Math.max(1, Math.min(440, var0 - 24));
      int var3 = Math.max(1, Math.min(358, var1 - 16));
      int var4 = (var0 - var2) / 2;
      int var5 = (var1 - var3) / 2;
      return new UpdateLayout.Dialog(var4, var5, var2, var3, var5 + 56, var5 + var3 - 92, var5 + var3 - 82);
   }

   public record Dialog(int x, int y, int width, int height, int textTop, int textBottom, int actionsY) {
      public int textHeight() {
         return Math.max(1, this.textBottom - this.textTop);
      }
   }

   public record Notice(HomeLayout.Rect surface, HomeLayout.Rect open, HomeLayout.Rect later, boolean compact) {
   }
}
