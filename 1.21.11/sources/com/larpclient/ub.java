package com.larpclient;

import com.google.gson.JsonObject;
import java.util.UUID;
import net.minecraft.class_1011;
import net.minecraft.class_2960;

public final class ub {
   public static ub.k p;

   public static ub.k o(UUID var0, UUID var1) {
      return var1.equals(var0) ? p : null;
   }

   public static ub.k x(String var0, uc var1, class_1011 var2, class_1011 var3) {
      return new ub.k(sp.r(var0 + "/skin", var2), sp.r(var0 + "/cape", var3), var1.slim(), var1.hideCape(), var1.name(), var1.cosmetics());
   }

   public static void l() {
      if (p != null) {
         p.close();
      }

      p = null;
   }

   private ub() {
   }

   public record k(class_2960 skin, class_2960 cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         sp.G(this.skin);
         sp.G(this.cape);
      }
   }
}
