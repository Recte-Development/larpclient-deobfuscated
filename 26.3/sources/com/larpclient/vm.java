package com.larpclient;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public final class vm {
   private static final List<String> j = List.of("paper", "purpur", "folia", "pufferfish", "leaves", "gale", "leaf");

   public static boolean X(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.toLowerCase(Locale.ROOT);

      for (String var3 : j) {
         if (var1.contains(var3)) {
            return true;
         }
      }

      return false;
   }

   public static String M() {
      ClientPacketListener var0 = Minecraft.getInstance().getConnection();
      return var0 == null ? null : var0.serverBrand();
   }

   public static boolean G() {
      return X(M());
   }

   private vm() {
   }
}
