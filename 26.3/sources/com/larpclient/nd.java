package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.AutoClickerModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FakePayModule;
import com.larpclient.module.JesusModule;
import com.larpclient.module.Module;
import com.larpclient.module.PlayerModelModule;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public record nd(boolean enabled, int keybind, Map<com.larpclient.module.r<?>, Object> settings, JsonObject archive) {
   public static nd decode(Module var0, JsonObject var1) {
      if (var0 instanceof AimAssistModule) {
         var1 = pi.u(var1);
      }

      if (var0 instanceof PlayerModelModule) {
         var1 = pi.M(var1);
      }

      if (var0 instanceof JesusModule) {
         var1 = pi.q(var1);
      }

      pi.q(var1);
      boolean var2 = var1.has("enabled") ? _4.Y(var1.get("enabled")) : var0.isEnabled();
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
         AutoClickerModule.r(var11);
      }

      if (var0 instanceof FakePayModule) {
         FakePayModule.T(var11);
      }

      if (var0 instanceof CustomCrosshairModule) {
         CustomCrosshairModule.y(var11);
      }

      if (var0 instanceof AnimationsModule) {
         AnimationsModule.K(var11);
      }

      if (var0 instanceof ChinaHatModule) {
         ChinaHatModule.Q(var11);
      }

      if (var0 instanceof AutoSellModule) {
         as.X(var11);
      }

      LinkedHashMap var12 = new LinkedHashMap();

      for (Entry var7 : var11.entrySet()) {
         com.larpclient.module.r var8 = var0.getSettings().stream().filter(var1x -> var1x.X().equals(var7.getKey())).findFirst().orElse(null);
         if (var8 == null) {
            _q.z("Config", "Unknown setting {}.{} ignored; its value is kept for future saves", var0.getName(), var7.getKey());
         } else {
            try {
               var12.put(var8, iu.e(var8, (JsonElement)var7.getValue(), true));
            } catch (RuntimeException var10) {
               _q.R("Config", "Invalid {}.{}; keeping prior/default value: {}", var0.getName(), var8.X(), var10.toString());
            }
         }
      }

      JsonObject var13 = var1.has("legacySettings") ? var1.getAsJsonObject("legacySettings").deepCopy() : null;
      return new nd(var2, var3, Collections.unmodifiableMap(var12), var13);
   }

   public void apply(Module var1) {
      boolean var2 = var1.isEnabled();
      int var3 = var1.getKeyBind();
      LinkedHashMap var4 = new LinkedHashMap();

      for (com.larpclient.module.r var6 : var1.getSettings()) {
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
               ((com.larpclient.module.r)var7.getKey()).setValue(var7.getValue());
            } catch (RuntimeException var9) {
               var11.addSuppressed(var9);
            }
         }

         var1.setEnabled(var2);
         throw var11;
      }
   }
}
