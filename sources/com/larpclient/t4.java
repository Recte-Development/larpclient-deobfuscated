package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class t4 {
   private static final t4 l = new t4();
   public boolean s;

   private t4() {
   }

   public static t4 N() {
      return l;
   }
}
