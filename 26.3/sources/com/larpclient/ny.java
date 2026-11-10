package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.UUID;
import net.minecraft.resources.Identifier;

public final class ny {
   public static ny.p p;

   public static ny.p v(UUID var0, UUID var1) {
      return var1.equals(var0) ? p : null;
   }

   public static ny.p f(String var0, i8 var1, NativeImage var2, NativeImage var3) {
      return new ny.p(ao.P(var0 + "/skin", var2), ao.P(var0 + "/cape", var3), var1.slim(), var1.hideCape(), var1.name(), var1.cosmetics());
   }

   public static void P() {
      if (p != null) {
         p.close();
      }

      p = null;
   }

   private ny() {
   }

   public record p(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         ao.z(this.skin);
         ao.z(this.cape);
      }
   }
}
