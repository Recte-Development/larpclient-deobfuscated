package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class j2 {
   private static final j2 x = new j2();
   public boolean h;

   private j2() {
   }

   public static j2 y() {
      return x;
   }
}
