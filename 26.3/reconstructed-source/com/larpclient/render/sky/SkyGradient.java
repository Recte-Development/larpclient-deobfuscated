package com.larpclient.render.sky;

import com.larpclient.gui.theme.Theme;
import com.larpclient.util.render.ColorUtil;

public record SkyGradient(int zenith, int horizon) {
   public static SkyGradient forPreset(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new SkyGradient(ColorUtil.lerpColor(-16315882, Theme.accent, 0.16), ColorUtil.lerpColor(-15263703, Theme.accentEnd, 0.48));
         case "Sunset" -> new SkyGradient(-14609097, -3245717);
         case "Ocean" -> new SkyGradient(-16378327, -12349780);
         case "Purple" -> new SkyGradient(-15725786, -8955221);
         case "Red" -> new SkyGradient(-14678256, -5486497);
         case "Black" -> new SkyGradient(-16645368, -15460316);
         case "Rainbow" -> new SkyGradient(ColorUtil.hsbToRgb(var2, 0.65F, 0.13F), ColorUtil.hsbToRgb(var2, 0.42F, 0.58F));
         case "Custom" -> new SkyGradient(ColorUtil.lerpColor(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new SkyGradient(-16446431, -12758401);
      };
   }
}
