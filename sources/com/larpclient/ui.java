package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ui {
   private static final ui l = new ui();
   public boolean O;

   private ui() {
   }

   public static ui C() {
      return l;
   }
}
