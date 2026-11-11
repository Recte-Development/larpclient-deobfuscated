package com.larpclient.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.TextSetting;

public final class SettingCodec {
   public static Object parseValue(Setting<?> var0, JsonElement var1, boolean var2) {
      if (var1 != null && var1.isJsonPrimitive()) {
         JsonPrimitive var3 = var1.getAsJsonPrimitive();
         if (var0 instanceof BooleanSetting) {
            if (!var3.isBoolean()) {
               throw new IllegalArgumentException("Expected boolean");
            } else {
               return var3.getAsBoolean();
            }
         } else if (var0 instanceof TextSetting var8) {
            if (!var3.isString()) {
               throw new IllegalArgumentException("Expected text");
            } else {
               return var8.validate(var3.getAsString());
            }
         } else if (var0 instanceof ModeSetting var7) {
            if (var3.isString() && var7.acceptsValue(var3.getAsString())) {
               return var3.getAsString();
            } else {
               throw new IllegalArgumentException("Unsupported mode");
            }
         } else {
            if (var2 && !var3.isNumber()) {
               throw new IllegalArgumentException("Expected number");
            }

            double var4 = var3.getAsDouble();
            if (!Double.isFinite(var4)) {
               throw new IllegalArgumentException("Nonfinite setting");
            }

            if (var0 instanceof NumberSetting var9) {
               if (!var2 || !(var4 < var9.getMin()) && !(var4 > var9.getMax())) {
                  return var4;
               } else {
                  throw new IllegalArgumentException("Setting outside range");
               }
            } else if (!var2 || var4 == Math.rint(var4) && !(var4 < -2.1474836E9F) && !(var4 > 2.147483647E9)) {
               int var6 = var3.getAsInt();
               if (!(var0 instanceof KeybindSetting) || var6 >= -1 && var6 <= 348) {
                  return var6;
               } else {
                  throw new IllegalArgumentException("Invalid keybind");
               }
            } else {
               throw new IllegalArgumentException("Expected integer");
            }
         }
      } else {
         throw new IllegalArgumentException("Expected setting primitive");
      }
   }

   public static void applyValue(Setting var0, JsonElement var1) {
      var0.setValue(parseValue(var0, var1, false));
   }
}
