package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.AutoClickerModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FakePayModule;
import com.larpclient.module.JesusModule;
import com.larpclient.module.Module;
import com.larpclient.module.PlayerModelModule;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public record _y(boolean enabled, int keybind, Map<com.larpclient.module.l6<?>, Object> settings, JsonObject archive) {
   public static _y decode(Module var0, JsonObject var1) {
      if (var0 instanceof AimAssistModule) {
         var1 = w.U(var1);
      }

      if (var0 instanceof PlayerModelModule) {
         var1 = w.r(var1);
      }

      if (var0 instanceof JesusModule) {
         var1 = w.g(var1);
      }

      w.a(var1);
      boolean var2 = var1.has("enabled") ? yj.V(var1.get("enabled")) : var0.isEnabled();
      int var3 = var0.getKeyBind();
      if (var1.has("keybind")) {
         JsonElement var4 = var1.get("keybind");
         if (!var4.isJsonPrimitive()) {
            throw new IllegalArgumentException("Invalid module keybind");
         }

         double var5 = var4.getAsDouble();
         if (!Double.isFinite(var5) || var5 != Math.rint(var5) || var5 < -1.0 || var5 > 348.0) {
            throw new IllegalArgumentException("Invalid module keybind");
         }

         var3 = (int)var5;
      }

      JsonObject var11 = var1.has("settings") ? var1.getAsJsonObject("settings").deepCopy() : new JsonObject();
      if (var0 instanceof AutoClickerModule) {
         AutoClickerModule.J(var11);
      }

      if (var0 instanceof FakePayModule) {
         FakePayModule.E(var11);
      }

      if (var0 instanceof CustomCrosshairModule) {
         CustomCrosshairModule.N(var11);
      }

      if (var0 instanceof AnimationsModule) {
         AnimationsModule.Q(var11);
      }

      if (var0 instanceof AutoSellModule) {
         ej.Y(var11);
      }

      LinkedHashMap var12 = new LinkedHashMap();

      for (Entry var7 : var11.entrySet()) {
         com.larpclient.module.l6 var8 = var0.getSettings().stream().filter(var1x -> var1x.k().equals(var7.getKey())).findFirst().orElse(null);
         if (var8 == null) {
            r4.m("Config", "Unknown setting {}.{} ignored", var0.getName(), var7.getKey());
         } else {
            try {
               var12.put(var8, eh.Z(var8, (JsonElement)var7.getValue(), true));
            } catch (RuntimeException var10) {
               r4.m("Config", "Invalid {}.{}; keeping prior/default value: {}", var0.getName(), var8.k(), var10.toString());
            }
         }
      }

      JsonObject var13 = var1.has("legacySettings") ? var1.getAsJsonObject("legacySettings").deepCopy() : null;
      return new _y(var2, var3, Collections.unmodifiableMap(var12), var13);
   }

   public void apply(Module var1) {
      boolean var2 = var1.isEnabled();
      int var3 = var1.getKeyBind();
      LinkedHashMap var4 = new LinkedHashMap();

      for (com.larpclient.module.l6 var6 : var1.getSettings()) {
         var4.put(var6, var6.getValue());
      }

      try {
         if (var2) {
            var1.disable();
         }

         var1.setKeyBind(this.keybind);
         this.settings.forEach((var0, var1x) -> var0.setValue(var1x));
         var1.setEnabled(this.enabled);
      } catch (RuntimeException var10) {
         RuntimeException var11 = var10;
         var1.setKeyBind(var3);

         for (Entry var7 : var4.entrySet()) {
            try {
               ((com.larpclient.module.l6)var7.getKey()).setValue(var7.getValue());
            } catch (RuntimeException var9) {
               var11.addSuppressed(var9);
            }
         }

         var1.setEnabled(var2);
         throw var11;
      }
   }
}
