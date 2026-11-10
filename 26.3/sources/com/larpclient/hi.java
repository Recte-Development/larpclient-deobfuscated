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

public final class hi {
   private static final Gson j = new Gson();
   private boolean h;
   private boolean G;
   private String q = "";
   private JsonObject Z;

   public boolean w() {
      return this.h && this.G;
   }

   public void h(String var1, String var2) {
      if (this.h && this.q.equals(var1)) {
         this.q = _4.p(var2);
      }
   }

   public JsonObject t() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.h);
      if (this.h && this.Z != null) {
         var1.addProperty("profile", this.q);
         var1.add("previous", this.Z.deepCopy());
      }

      return var1;
   }

   public static hi b(JsonObject var0) {
      hi var1 = new hi();

      try {
         if (!_4.Y(var0.get("active"))) {
            return var1;
         }

         String var2 = _4.p(_4.m(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         if (var3.get("China Hat") instanceof JsonObject var4 && var4.get("settings") instanceof JsonObject var5) {
            ChinaHatModule.Q(var5);
         }

         var1.h = (boolean)1;
         var1.q = var2;
         var1.Z = var3.deepCopy();
      } catch (RuntimeException var7) {
         _q.R("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var7.toString());
      }

      return var1;
   }

   private static <T extends Module> T X(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<hi.s> Z(List<Module> var0) {
      CustomSkyModule var1 = X(var0, CustomSkyModule.class);
      ChinaHatModule var2 = X(var0, ChinaHatModule.class);
      JumpFXModule var3 = X(var0, JumpFXModule.class);
      HitFXModule var4 = X(var0, HitFXModule.class);
      LinkedHashMap var5 = new LinkedHashMap();

      for (com.larpclient.module.r var7 : var2.u()) {
         var5.put(var7, var7.X().equals("Kippah"));
      }

      var5.put(var2.S(), true);
      return List.of(
         new hi.s(var1, Map.of(var1.Z(), "Israel Flag")),
         new hi.s(var2, var5),
         new hi.s(var3, Map.of(var3.W(), "Star of David")),
         new hi.s(var4, Map.of(var4.G(), "Cash", var4.J(), true))
      );
   }

   private static JsonObject F(List<hi.s> var0) {
      JsonObject var1 = new JsonObject();

      for (hi.s var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.X(), j.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<hi.s> U(List<hi.s> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (hi.s var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            _4.Y(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (com.larpclient.module.r var9 : var4.values.keySet()) {
               var7.put(var9, iu.e(var9, var6.get(var9.X()), true));
            }

            var2.add(new hi.s(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void b(List<hi.s> var0, JsonObject var1) {
      for (hi.s var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((com.larpclient.module.r<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !_4.Y(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean o(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.w()) {
         return true;
      }

      try {
         List var5 = Z(var2);
         if (var1) {
            this.Z = F(var5);
            this.q = _4.p(var3);
            this.h = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.h = (boolean)0;
               this.Z = null;
               return false;
            }

            this.G = (boolean)1;

            try {
               b(var5, null);
            } catch (RuntimeException var12) {
               try {
                  b(U(var5, this.Z), this.Z);
                  this.h = this.G = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = U(var5, this.Z);
            this.h = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.h = (boolean)1;
               return false;
            }

            try {
               b(var6, this.Z);
               this.G = (boolean)0;
               this.Z = null;
               this.q = "";
            } catch (RuntimeException var11) {
               this.h = (boolean)1;

               try {
                  b(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         _q.z("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         _q.o("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean Z(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.h && !this.G) {
         try {
            if (!this.q.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = Z(var1);
            List var5 = U(var4, this.Z);

            try {
               b(var4, null);
               this.G = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  b(var5, this.Z);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.G = (boolean)1;
                  _q.o("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            _q.z("Israel mode", "Resumed local cosmetic preset for {}", this.q);
            return true;
         } catch (RuntimeException var10) {
            this.h = this.G = (boolean)0;
            this.Z = null;
            this.q = "";
            var3.getAsBoolean();
            _q.o("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void S(List<Module> var1, BooleanSupplier var2) {
      if (this.w() && !this.o(false, var1, this.q, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void D(Module var1, JsonObject var2) {
      if (this.w() && this.Z != null && this.Z.has(var1.getName())) {
         JsonObject var3 = this.Z.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record s(Module module, Map<com.larpclient.module.r<?>, Object> values) {
   }
}
