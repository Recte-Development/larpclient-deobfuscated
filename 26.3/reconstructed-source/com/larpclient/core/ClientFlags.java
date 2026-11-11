package com.larpclient.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ClientFlags {
   private static final ClientFlags instance = new ClientFlags();
   public boolean enabled;

   private ClientFlags() {
   }

   public static ClientFlags getInstance() {
      return instance;
   }
}
