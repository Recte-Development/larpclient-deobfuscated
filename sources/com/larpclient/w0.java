package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dv;

public final class w0 {
   public static Object C(df<?> var0, JsonElement var1, boolean var2) {
      if (var1 != null && var1.isJsonPrimitive()) {
         JsonPrimitive var3 = var1.getAsJsonPrimitive();
         if (var0 instanceof dh) {
            if (!var3.isBoolean()) {
               throw new IllegalArgumentException("Expected boolean");
            } else {
               return var3.getAsBoolean();
            }
         } else if (var0 instanceof com.larpclient.module.ds) {
            if (!var3.isString()) {
               throw new IllegalArgumentException("Expected text");
            } else {
               return com.larpclient.module.ds.S(var3.getAsString());
            }
         } else if (var0 instanceof com.larpclient.module.d6 var7) {
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

            if (var0 instanceof com.larpclient.module.d9 var8) {
               if (!var2 || !(var4 < var8.v()) && !(var4 > var8.J())) {
                  return var4;
               } else {
                  throw new IllegalArgumentException("Setting outside range");
               }
            } else if (!var2 || var4 == Math.rint(var4) && !(var4 < -2.1474836E9F) && !(var4 > 2.147483647E9)) {
               int var6 = var3.getAsInt();
               if (!(var0 instanceof dv) || var6 >= -1 && var6 <= 348) {
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

   public static void L(df var0, JsonElement var1) {
      var0.setValue(C(var0, var1, false));
   }
}
