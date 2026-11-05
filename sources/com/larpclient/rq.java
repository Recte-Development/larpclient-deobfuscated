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

public final class rq {
   private static final Gson n = new Gson();
   private boolean m;
   private boolean i;
   private String f = "";
   private JsonObject x;

   public boolean S() {
      return this.m && this.i;
   }

   public void d(String var1, String var2) {
      if (this.m && this.f.equals(var1)) {
         this.f = yj.S(var2);
      }
   }

   public JsonObject z() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.m);
      if (this.m && this.x != null) {
         var1.addProperty("profile", this.f);
         var1.add("previous", this.x.deepCopy());
      }

      return var1;
   }

   public static rq Q(JsonObject var0) {
      rq var1 = new rq();

      try {
         if (!yj.V(var0.get("active"))) {
            return var1;
         }

         String var2 = yj.S(yj.y(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.m = (boolean)1;
         var1.f = var2;
         var1.x = var3.deepCopy();
      } catch (RuntimeException var4) {
         r4.m("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var4.toString());
      }

      return var1;
   }

   private static <T extends Module> T c(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<rq.k> O(List<Module> var0) {
      CustomSkyModule var1 = c(var0, CustomSkyModule.class);
      ChinaHatModule var2 = c(var0, ChinaHatModule.class);
      JumpFXModule var3 = c(var0, JumpFXModule.class);
      HitFXModule var4 = c(var0, HitFXModule.class);
      return List.of(
         new rq.k(var1, Map.of(var1.G(), "Israel Flag")),
         new rq.k(var2, Map.of(var2.a(), "Kippah", var2.i(), true)),
         new rq.k(var3, Map.of(var3.l(), "Star of David")),
         new rq.k(var4, Map.of(var4.G(), "Cash", var4.y(), true))
      );
   }

   private static JsonObject P(List<rq.k> var0) {
      JsonObject var1 = new JsonObject();

      for (rq.k var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.k(), n.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<rq.k> W(List<rq.k> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (rq.k var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            yj.V(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (com.larpclient.module.l6 var9 : var4.values.keySet()) {
               var7.put(var9, eh.Z(var9, var6.get(var9.k()), true));
            }

            var2.add(new rq.k(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void L(List<rq.k> var0, JsonObject var1) {
      for (rq.k var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((com.larpclient.module.l6<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !yj.V(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean B(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.S()) {
         return true;
      }

      try {
         List var5 = O(var2);
         if (var1) {
            this.x = P(var5);
            this.f = yj.S(var3);
            this.m = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.m = (boolean)0;
               this.x = null;
               return false;
            }

            this.i = (boolean)1;

            try {
               L(var5, null);
            } catch (RuntimeException var12) {
               try {
                  L(W(var5, this.x), this.x);
                  this.m = this.i = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = W(var5, this.x);
            this.m = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.m = (boolean)1;
               return false;
            }

            try {
               L(var6, this.x);
               this.i = (boolean)0;
               this.x = null;
               this.f = "";
            } catch (RuntimeException var11) {
               this.m = (boolean)1;

               try {
                  L(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         r4.V("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         r4.r("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean L(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.m && !this.i) {
         try {
            if (!this.f.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = O(var1);
            List var5 = W(var4, this.x);

            try {
               L(var4, null);
               this.i = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  L(var5, this.x);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.i = (boolean)1;
                  r4.r("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            r4.V("Israel mode", "Resumed local cosmetic preset for {}", this.f);
            return true;
         } catch (RuntimeException var10) {
            this.m = this.i = (boolean)0;
            this.x = null;
            this.f = "";
            var3.getAsBoolean();
            r4.r("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void i(List<Module> var1, BooleanSupplier var2) {
      if (this.S() && !this.B(false, var1, this.f, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void E(Module var1, JsonObject var2) {
      if (this.S() && this.x != null && this.x.has(var1.getName())) {
         JsonObject var3 = this.x.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record k(Module module, Map<com.larpclient.module.l6<?>, Object> values) {
   }
}
