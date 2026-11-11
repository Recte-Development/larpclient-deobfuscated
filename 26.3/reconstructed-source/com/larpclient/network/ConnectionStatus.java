package com.larpclient.network;

import com.larpclient.social.presence.DiscordUser;

public record ConnectionStatus(ConnectionStatus.State state, DiscordUser account) {
   private static volatile ConnectionStatus current = new ConnectionStatus(ConnectionStatus.State.ACTIVITY_OFF, null);

   public static ConnectionStatus current() {
      return current;
   }

   static void publish(ConnectionStatus.State var0, DiscordUser var1) {
      current = new ConnectionStatus(var0, var0 == ConnectionStatus.State.ONLINE ? var1 : null);
   }

   public enum State {
      ACTIVITY_OFF("Discord activity off", -8354674),
      OFFLINE("Discord offline", -8354674),
      ONLINE("Online", -14441126);

      private final String LABEL;
      private final int COLOR;

      State(String var3, int var4) {
         this.LABEL = var3;
         this.COLOR = var4;
      }

      public String label() {
         return this.LABEL;
      }

      public int color() {
         return this.COLOR;
      }
   }
}
