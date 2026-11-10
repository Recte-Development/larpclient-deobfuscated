package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class vd {
   public static final int c = -12564250;
   static final int D = 16384;
   static final int d = 32;
   public boolean U;
   public int h = -12564250;
   private final Map<String, vd.g> W = new LinkedHashMap<>();

   public vd.g N(String var1) {
      return this.W.get(var1);
   }

   public void H(String var1, vd.g var2) {
      if (var1 != null && var1.matches("[A-Z][A-Z0-9_]{0,31}")) {
         if (Math.abs(var2.x()) > 16384 || Math.abs(var2.y()) > 16384) {
            throw new IllegalArgumentException("Panel position out of range");
         }

         if (this.W.size() < 32 || this.W.containsKey(var1)) {
            this.W.put(var1, var2);
         }
      } else {
         throw new IllegalArgumentException("Invalid panel identifier");
      }
   }

   public void l() {
      this.W.clear();
   }

   public int n() {
      return this.W.size();
   }

   public JsonObject M() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("enabled", this.U);
      var1.addProperty("color", this.h);
      JsonObject var2 = new JsonObject();
      this.W.forEach((var1x, var2x) -> {
         JsonObject var3 = new JsonObject();
         var3.addProperty("x", var2x.x());
         var3.addProperty("y", var2x.y());
         var3.addProperty("open", var2x.open());
         var2.add(var1x, var3);
      });
      var1.add("panels", var2);
      return var1;
   }

   public static vd z(JsonObject var0) {
      vd var1 = new vd();

      try {
         if (var0.has("enabled")) {
            var1.U = _4.Y(var0.get("enabled"));
         }
      } catch (RuntimeException var12) {
      }

      try {
         JsonElement var2 = var0.get("color");
         if (var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isNumber()) {
            long var3 = var2.getAsLong();
            if (var3 >= -2147483648L && var3 <= 4294967295L) {
               var1.h = (int)var3;
            }
         }
      } catch (RuntimeException var11) {
      }

      try {
         JsonObject var15 = var0.getAsJsonObject("panels");
         if (var15 != null && var15.size() <= 32) {
            for (Entry var4 : var15.entrySet()) {
               try {
                  JsonObject var5 = ((JsonElement)var4.getValue()).getAsJsonObject();
                  if (var5.get("x").getAsJsonPrimitive().isNumber() && var5.get("y").getAsJsonPrimitive().isNumber()) {
                     double var6 = var5.get("x").getAsDouble();
                     double var8 = var5.get("y").getAsDouble();
                     if (var6 == Math.rint(var6) && var8 == Math.rint(var8)) {
                        int var10 = var5.has("open") && !_4.Y(var5.get("open")) ? 0 : 1;
                        var1.H((String)var4.getKey(), new vd.g((int)var6, (int)var8, (boolean)var10));
                     }
                  }
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var14) {
      }

      return var1;
   }

   public record g(int x, int y, boolean open) {
   }
}
