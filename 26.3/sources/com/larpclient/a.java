package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class a {
   private static final a F = new a();
   public boolean B;

   private a() {
   }

   public static a N() {
      return F;
   }
}
