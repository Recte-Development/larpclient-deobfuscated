package com.larpclient.module;

import com.larpclient._q;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class AutoReconnectModule extends Module {
   private static AutoReconnectModule Z;
   private final b I = this.addSetting(new b("Delay", "Seconds to wait before reconnecting", 3.5, 0.0, 60.0, 0.1));
   private ServerAddress M;
   private ServerData s;
   private Screen f;
   private long d = -1L;
   private boolean X;

   public AutoReconnectModule() {
      super("Auto Reconnect", "Automatically reconnects when disconnected from a server.", Category.PLAYER, -1);
      Z = this;
   }

   public static void q(ServerAddress var0, ServerData var1) {
      AutoReconnectModule var2 = Z;
      if (var2 != null && var0 != null && var1 != null) {
         var2.M = var0;
         var2.s = var1;
      }
   }

   public static void h(Minecraft var0) {
      if (Z != null && Z.isEnabled()) {
         AutoReconnectModule var1 = Z;
         if (var1 != null) {
            var1.f(var0);
         }
      }
   }

   private void f(Minecraft var1) {
      if (!this.isOperational()) {
         this.f = null;
         this.O();
      } else {
         Screen var2 = var1.gui.screen();
         if (var2 != this.f) {
            this.f = var2;
            if (var2 instanceof DisconnectedScreen) {
               this.O();
            }
         }

         ServerData var3 = var1.getCurrentServer();
         if (var3 != null && var1.getConnection() != null) {
            q(ServerAddress.parseString(var3.ip), var3);
            this.O();
         }

         if (var2 instanceof DisconnectedScreen && this.M != null && this.s != null && !this.X) {
            long var4 = System.currentTimeMillis();
            if (this.d < 0L) {
               this.d = var4;
            }

            if (var4 - this.d >= (long)(this.I.getValue() * 1000.0)) {
               this.X = (boolean)1;
               _q.z("AutoReconnect", "Reconnecting to {}", this.s.ip);
               ConnectScreen.startConnecting(var2, var1, this.M, this.s, false, null);
            }
         }
      }
   }

   private void O() {
      this.d = -1L;
      this.X = (boolean)0;
   }
}
