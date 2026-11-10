package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.UUID;
import net.minecraft.resources.Identifier;

public final class bi {
   public static bi.q O;

   public static bi.q C(UUID var0, UUID var1) {
      return var1.equals(var0) ? O : null;
   }

   public static bi.q x(String var0, j8 var1, NativeImage var2, NativeImage var3) {
      return new bi.q(j3.g(var0 + "/skin", var2), j3.g(var0 + "/cape", var3), var1.slim(), var1.hideCape(), var1.name(), var1.cosmetics());
   }

   public static void U() {
      if (O != null) {
         O.close();
      }

      O = null;
   }

   private bi() {
   }

   public record q(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         j3.p(this.skin);
         j3.p(this.cape);
      }
   }
}
