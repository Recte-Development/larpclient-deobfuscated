package com.larpclient;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public final class uo {
   private uo() {
   }

   public static String S(Packet<?> var0) {
      String var1 = switch (var0) {
         case ServerboundChatCommandPacket var4 -> var4.command();
         case ServerboundChatCommandSignedPacket var5 -> var5.command();
         case ServerboundChatPacket var6 -> var6.message().strip().startsWith("/") ? var6.message() : null;
         default -> null;
      };
      return x0.B(var1) ? var1 : null;
   }
}
