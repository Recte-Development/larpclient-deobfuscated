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

public final class f_ {
   private static final Gson w = new Gson();
   private boolean u;
   private boolean V;
   private String G = "";
   private JsonObject f;

   public boolean B() {
      return this.u && this.V;
   }

   public void e(String var1, String var2) {
      if (this.u && this.G.equals(var1)) {
         this.G = h.Y(var2);
      }
   }

   public JsonObject z() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.u);
      if (this.u && this.f != null) {
         var1.addProperty("profile", this.G);
         var1.add("previous", this.f.deepCopy());
      }

      return var1;
   }

   public static f_ f(JsonObject var0) {
      f_ var1 = new f_();

      try {
         if (!h.o(var0.get("active"))) {
            return var1;
         }

         String var2 = h.Y(h.f(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         if (var3.get("China Hat") instanceof JsonObject var4 && var4.get("settings") instanceof JsonObject var5) {
            ChinaHatModule.w(var5);
         }

         var1.u = (boolean)1;
         var1.G = var2;
         var1.f = var3.deepCopy();
      } catch (RuntimeException var7) {
         q8.L("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var7.toString());
      }

      return var1;
   }

   private static <T extends Module> T M(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<f_.k> u(List<Module> var0) {
      CustomSkyModule var1 = M(var0, CustomSkyModule.class);
      ChinaHatModule var2 = M(var0, ChinaHatModule.class);
      JumpFXModule var3 = M(var0, JumpFXModule.class);
      HitFXModule var4 = M(var0, HitFXModule.class);
      LinkedHashMap var5 = new LinkedHashMap();

      for (com.larpclient.module.lb var7 : var2.L()) {
         var5.put(var7, var7.R().equals("Kippah"));
      }

      var5.put(var2.B(), true);
      return List.of(
         new f_.k(var1, Map.of(var1.r(), "Israel Flag")),
         new f_.k(var2, var5),
         new f_.k(var3, Map.of(var3.K(), "Star of David")),
         new f_.k(var4, Map.of(var4.h(), "Cash", var4.H(), true))
      );
   }

   private static JsonObject C(List<f_.k> var0) {
      JsonObject var1 = new JsonObject();

      for (f_.k var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.R(), w.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<f_.k> p(List<f_.k> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (f_.k var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            h.o(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (com.larpclient.module.lb var9 : var4.values.keySet()) {
               var7.put(var9, uv.N(var9, var6.get(var9.R()), true));
            }

            var2.add(new f_.k(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void T(List<f_.k> var0, JsonObject var1) {
      for (f_.k var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((com.larpclient.module.lb<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !h.o(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean m(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.B()) {
         return true;
      }

      try {
         List var5 = u(var2);
         if (var1) {
            this.f = C(var5);
            this.G = h.Y(var3);
            this.u = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.u = (boolean)0;
               this.f = null;
               return false;
            }

            this.V = (boolean)1;

            try {
               T(var5, null);
            } catch (RuntimeException var12) {
               try {
                  T(p(var5, this.f), this.f);
                  this.u = this.V = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = p(var5, this.f);
            this.u = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.u = (boolean)1;
               return false;
            }

            try {
               T(var6, this.f);
               this.V = (boolean)0;
               this.f = null;
               this.G = "";
            } catch (RuntimeException var11) {
               this.u = (boolean)1;

               try {
                  T(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         q8.G("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         q8.u("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean N(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.u && !this.V) {
         try {
            if (!this.G.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = u(var1);
            List var5 = p(var4, this.f);

            try {
               T(var4, null);
               this.V = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  T(var5, this.f);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.V = (boolean)1;
                  q8.u("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            q8.G("Israel mode", "Resumed local cosmetic preset for {}", this.G);
            return true;
         } catch (RuntimeException var10) {
            this.u = this.V = (boolean)0;
            this.f = null;
            this.G = "";
            var3.getAsBoolean();
            q8.u("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void n(List<Module> var1, BooleanSupplier var2) {
      if (this.B() && !this.m(false, var1, this.G, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void C(Module var1, JsonObject var2) {
      if (this.B() && this.f != null && this.f.has(var1.getName())) {
         JsonObject var3 = this.f.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record k(Module module, Map<com.larpclient.module.lb<?>, Object> values) {
   }
}
