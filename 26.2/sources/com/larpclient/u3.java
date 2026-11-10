package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class u3 {
   private static final Gson U = new Gson();
   private boolean f;
   private boolean g;
   private String x = "";
   private JsonObject n;

   public boolean X() {
      return this.f && this.g;
   }

   public void i(String var1, String var2) {
      if (this.f && this.x.equals(var1)) {
         this.x = km.r(var2);
      }
   }

   public JsonObject a() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.f);
      if (this.f && this.n != null) {
         var1.addProperty("profile", this.x);
         var1.add("previous", this.n.deepCopy());
      }

      return var1;
   }

   public static u3 v(JsonObject var0) {
      u3 var1 = new u3();

      try {
         if (!km.t(var0.get("active"))) {
            return var1;
         }

         String var2 = km.r(km.a(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         if (var3.get("China Hat") instanceof JsonObject var4 && var4.get("settings") instanceof JsonObject var5) {
            ChinaHatModule.e(var5);
         }

         var1.f = (boolean)1;
         var1.x = var2;
         var1.n = var3.deepCopy();
      } catch (RuntimeException var7) {
         h.g("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var7.toString());
      }

      return var1;
   }

   private static <T extends Module> T m(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<u3.q> N(List<Module> var0) {
      CustomSkyModule var1 = m(var0, CustomSkyModule.class);
      ChinaHatModule var2 = m(var0, ChinaHatModule.class);
      JumpFXModule var3 = m(var0, JumpFXModule.class);
      HitFXModule var4 = m(var0, HitFXModule.class);
      LinkedHashMap var5 = new LinkedHashMap();

      for (com.larpclient.module.q var7 : var2.B()) {
         var5.put(var7, var7.e().equals("Kippah"));
      }

      var5.put(var2.o(), true);
      return List.of(
         new u3.q(var1, Map.of(var1.d(), "Israel Flag")),
         new u3.q(var2, var5),
         new u3.q(var3, Map.of(var3.K(), "Star of David")),
         new u3.q(var4, Map.of(var4.Z(), "Cash", var4.W(), true))
      );
   }

   private static JsonObject n(List<u3.q> var0) {
      JsonObject var1 = new JsonObject();

      for (u3.q var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.e(), U.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<u3.q> u(List<u3.q> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (u3.q var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            km.t(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (com.larpclient.module.q var9 : var4.values.keySet()) {
               var7.put(var9, ht.A(var9, var6.get(var9.e()), true));
            }

            var2.add(new u3.q(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void X(List<u3.q> var0, JsonObject var1) {
      for (u3.q var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((com.larpclient.module.q<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !km.t(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean q(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.X()) {
         return true;
      }

      try {
         List var5 = N(var2);
         if (var1) {
            this.n = n(var5);
            this.x = km.r(var3);
            this.f = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.f = (boolean)0;
               this.n = null;
               return false;
            }

            this.g = (boolean)1;

            try {
               X(var5, null);
            } catch (RuntimeException var12) {
               try {
                  X(u(var5, this.n), this.n);
                  this.f = this.g = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = u(var5, this.n);
            this.f = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.f = (boolean)1;
               return false;
            }

            try {
               X(var6, this.n);
               this.g = (boolean)0;
               this.n = null;
               this.x = "";
            } catch (RuntimeException var11) {
               this.f = (boolean)1;

               try {
                  X(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         h.A("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         h.m("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean u(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.f && !this.g) {
         try {
            if (!this.x.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = N(var1);
            List var5 = u(var4, this.n);

            try {
               X(var4, null);
               this.g = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  X(var5, this.n);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.g = (boolean)1;
                  h.m("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            h.A("Israel mode", "Resumed local cosmetic preset for {}", this.x);
            return true;
         } catch (RuntimeException var10) {
            this.f = this.g = (boolean)0;
            this.n = null;
            this.x = "";
            var3.getAsBoolean();
            h.m("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void O(List<Module> var1, BooleanSupplier var2) {
      if (this.X() && !this.q(false, var1, this.x, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void n(Module var1, JsonObject var2) {
      if (this.X() && this.n != null && this.n.has(var1.getName())) {
         JsonObject var3 = this.n.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record q(Module module, Map<com.larpclient.module.q<?>, Object> values) {
   }
}
