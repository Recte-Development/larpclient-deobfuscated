package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.UUID;
import net.minecraft.resources.Identifier;

public final class z1 {
   public static z1.l z;

   public static z1.l f(UUID var0, UUID var1) {
      return var1.equals(var0) ? z : null;
   }

   public static z1.l R(String var0, o var1, NativeImage var2, NativeImage var3) {
      return new z1.l(el.r(var0 + "/skin", var2), el.r(var0 + "/cape", var3), var1.slim(), var1.hideCape(), var1.name(), var1.cosmetics());
   }

   public static void n() {
      if (z != null) {
         z.close();
      }

      z = null;
   }

   private z1() {
   }

   public record l(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         el.b(this.skin);
         el.b(this.cape);
      }
   }
}
