package com.larpclient.module.misc;

import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class AutoReconnectModule extends Module {
   private static AutoReconnectModule instance;
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Seconds to wait before reconnecting", 3.5, 0.0, 60.0, 0.1));
   private ServerAddress serverAddress;
   private ServerData serverData;
   private Screen lastScreen;
   private long disconnectedAt = -1L;
   private boolean reconnecting;

   public AutoReconnectModule() {
      super("Auto Reconnect", "Automatically reconnects when disconnected from a server.", Category.PLAYER, -1);
      instance = this;
   }

   public static void rememberServer(ServerAddress var0, ServerData var1) {
      AutoReconnectModule var2 = instance;
      if (var2 != null && var0 != null && var1 != null) {
         var2.serverAddress = var0;
         var2.serverData = var1;
      }
   }

   public static void onClientTick(Minecraft var0) {
      if (instance != null && instance.isEnabled()) {
         AutoReconnectModule var1 = instance;
         if (var1 != null) {
            var1.tickReconnect(var0);
         }
      }
   }

   private void tickReconnect(Minecraft var1) {
      if (!this.isOperational()) {
         this.lastScreen = null;
         this.resetTimer();
      } else {
         Screen var2 = var1.gui.screen();
         if (var2 != this.lastScreen) {
            this.lastScreen = var2;
            if (var2 instanceof DisconnectedScreen) {
               this.resetTimer();
            }
         }

         ServerData var3 = var1.getCurrentServer();
         if (var3 != null && var1.getConnection() != null) {
            rememberServer(ServerAddress.parseString(var3.ip), var3);
            this.resetTimer();
         }

         if (var2 instanceof DisconnectedScreen && this.serverAddress != null && this.serverData != null && !this.reconnecting) {
            long var4 = System.currentTimeMillis();
            if (this.disconnectedAt < 0L) {
               this.disconnectedAt = var4;
            }

            if (var4 - this.disconnectedAt >= (long)(this.delay.getValue() * 1000.0)) {
               this.reconnecting = (boolean)1;
               LarpLog.info("AutoReconnect", "Reconnecting to {}", this.serverData.ip);
               ConnectScreen.startConnecting(var2, var1, this.serverAddress, this.serverData, false, null);
            }
         }
      }
   }

   private void resetTimer() {
      this.disconnectedAt = -1L;
      this.reconnecting = (boolean)0;
   }
}
