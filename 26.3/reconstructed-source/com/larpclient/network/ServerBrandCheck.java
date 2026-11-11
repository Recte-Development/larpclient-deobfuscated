package com.larpclient.network;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public final class ServerBrandCheck {
   private static final List<String> pAPER_BRANDS = List.of("paper", "purpur", "folia", "pufferfish", "leaves", "gale", "leaf");

   public static boolean isPaperBrand(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.toLowerCase(Locale.ROOT);

      for (String var3 : pAPER_BRANDS) {
         if (var1.contains(var3)) {
            return true;
         }
      }

      return false;
   }

   public static String getServerBrand() {
      ClientPacketListener var0 = Minecraft.getInstance().getConnection();
      return var0 == null ? null : var0.serverBrand();
   }

   public static boolean isPaperServer() {
      return isPaperBrand(getServerBrand());
   }

   private ServerBrandCheck() {
   }
}
