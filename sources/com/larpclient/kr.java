package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class kr {
   private static final Gson Q = new Gson();
   private boolean B;
   private boolean e;
   private String K = "";
   private JsonObject t;

   public boolean X() {
      return this.B && this.e;
   }

   public void k(String var1, String var2) {
      if (this.B && this.K.equals(var1)) {
         this.K = p_.g(var2);
      }
   }

   public JsonObject D() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.B);
      if (this.B && this.t != null) {
         var1.addProperty("profile", this.K);
         var1.add("previous", this.t.deepCopy());
      }

      return var1;
   }

   public static kr x(JsonObject var0) {
      kr var1 = new kr();

      try {
         if (!p_.v(var0.get("active"))) {
            return var1;
         }

         String var2 = p_.g(p_.Q(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.B = (boolean)1;
         var1.K = var2;
         var1.t = var3.deepCopy();
      } catch (RuntimeException var4) {
         mt.T("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var4.toString());
      }

      return var1;
   }

   private static <T extends Module> T l(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<kr.f> X(List<Module> var0) {
      CustomSkyModule var1 = l(var0, CustomSkyModule.class);
      ChinaHatModule var2 = l(var0, ChinaHatModule.class);
      JumpFXModule var3 = l(var0, JumpFXModule.class);
      HitFXModule var4 = l(var0, HitFXModule.class);
      return List.of(
         new kr.f(var1, Map.of(var1.N(), "Israel Flag")),
         new kr.f(var2, Map.of(var2.t(), "Kippah", var2.R(), true)),
         new kr.f(var3, Map.of(var3.I(), "Star of David")),
         new kr.f(var4, Map.of(var4.G(), "Cash", var4.y(), true))
      );
   }

   private static JsonObject D(List<kr.f> var0) {
      JsonObject var1 = new JsonObject();

      for (kr.f var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.D(), Q.toJsonTree(var1x.P())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<kr.f> Z(List<kr.f> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (kr.f var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            p_.v(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (df var9 : var4.values.keySet()) {
               var7.put(var9, w0.C(var9, var6.get(var9.D()), true));
            }

            var2.add(new kr.f(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void E(List<kr.f> var0, JsonObject var1) {
      for (kr.f var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.P(), var1x)) {
               ((df<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !p_.v(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean g(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.X()) {
         return true;
      }

      try {
         List var5 = X(var2);
         if (var1) {
            this.t = D(var5);
            this.K = p_.g(var3);
            this.B = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.B = (boolean)0;
               this.t = null;
               return false;
            }

            this.e = (boolean)1;

            try {
               E(var5, null);
            } catch (RuntimeException var12) {
               try {
                  E(Z(var5, this.t), this.t);
                  this.B = this.e = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = Z(var5, this.t);
            this.B = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.B = (boolean)1;
               return false;
            }

            try {
               E(var6, this.t);
               this.e = (boolean)0;
               this.t = null;
               this.K = "";
            } catch (RuntimeException var11) {
               this.B = (boolean)1;

               try {
                  E(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         mt.K("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         mt.P("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean D(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.B && !this.e) {
         try {
            if (!this.K.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = X(var1);
            List var5 = Z(var4, this.t);

            try {
               E(var4, null);
               this.e = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  E(var5, this.t);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.e = (boolean)1;
                  mt.P("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            mt.K("Israel mode", "Resumed local cosmetic preset for {}", this.K);
            return true;
         } catch (RuntimeException var10) {
            this.B = this.e = (boolean)0;
            this.t = null;
            this.K = "";
            var3.getAsBoolean();
            mt.P("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void k(List<Module> var1, BooleanSupplier var2) {
      if (this.X() && !this.g(false, var1, this.K, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void u(Module var1, JsonObject var2) {
      if (this.X() && this.t != null && this.t.has(var1.getName())) {
         JsonObject var3 = this.t.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record f(Module module, Map<df<?>, Object> values) {
   }
}
