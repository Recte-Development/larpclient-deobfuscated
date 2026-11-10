package com.larpclient.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class AutoReconnectModule extends Module {
   private static AutoReconnectModule v;
   private final e b = this.addSetting(new e("Delay", "Seconds to wait before reconnecting", 3.5, 0.0, 60.0, 0.1));
   private ServerAddress z;
   private ServerData V;
   private Screen w;
   private long p = -1L;
   private boolean D;

   public AutoReconnectModule() {
      super("Auto Reconnect", "Automatically reconnects when disconnected from a server.", Category.PLAYER, -1);
      v = this;
   }

   public static void B(ServerAddress var0, ServerData var1) {
      AutoReconnectModule var2 = v;
      if (var2 != null && var0 != null && var1 != null) {
         var2.z = var0;
         var2.V = var1;
      }
   }

   public static void S(Minecraft var0) {
      if (v != null && v.isEnabled()) {
         AutoReconnectModule var1 = v;
         if (var1 != null) {
            var1.y(var0);
         }
      }
   }

   private void y(Minecraft var1) {
      if (!this.isOperational()) {
         this.w = null;
         this.j();
      } else {
         Screen var2 = var1.gui.screen();
         if (var2 != this.w) {
            this.w = var2;
            if (var2 instanceof DisconnectedScreen) {
               this.j();
            }
         }

         ServerData var3 = var1.getCurrentServer();
         if (var3 != null && var1.getConnection() != null) {
            B(ServerAddress.parseString(var3.ip), var3);
            this.j();
         }

         if (var2 instanceof DisconnectedScreen && this.z != null && this.V != null && !this.D) {
            long var4 = System.currentTimeMillis();
            if (this.p < 0L) {
               this.p = var4;
            }

            if (var4 - this.p >= (long)(this.b.getValue() * 1000.0)) {
               this.D = (boolean)1;
               com.larpclient.h.A("AutoReconnect", "Reconnecting to {}", this.V.ip);
               ConnectScreen.startConnecting(var2, var1, this.z, this.V, false, null);
            }
         }
      }
   }

   private void j() {
      this.p = -1L;
      this.D = (boolean)0;
   }
}
