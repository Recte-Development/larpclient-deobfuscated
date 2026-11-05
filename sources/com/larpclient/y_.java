package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.UUID;
import net.minecraft.resources.Identifier;

public final class y_ {
   public static y_.w X;

   public static y_.w T(UUID var0, UUID var1) {
      return var1.equals(var0) ? X : null;
   }

   public static y_.w i(String var0, _9 var1, NativeImage var2, NativeImage var3) {
      return new y_.w(om.W(var0 + "/skin", var2), om.W(var0 + "/cape", var3), var1.slim(), var1.hideCape(), var1.name(), var1.cosmetics());
   }

   public static void p() {
      if (X != null) {
         X.close();
      }

      X = null;
   }

   private y_() {
   }

   public record w(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         om.B(this.skin);
         om.B(this.cape);
      }
   }
}
