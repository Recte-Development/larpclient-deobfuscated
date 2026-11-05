package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.AmbientParticlesModule;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.Module;
import com.larpclient.module.PlayerTrailModule;
import com.larpclient.module.SelfVisualsModule;
import com.larpclient.module.TargetAuraModule;
import com.larpclient.module.WorldColorModule;
import com.larpclient.module.df;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public final class _4 {
   private static final Gson B = new Gson();
   private boolean h;
   private boolean k;
   private String F = "";
   private JsonObject U;
   private int Y = 3;

   public boolean Y() {
      return this.h && this.k;
   }

   public void f(String var1, String var2) {
      if (this.h && this.F.equals(var1)) {
         this.F = p_.g(var2);
      }
   }

   public JsonObject F() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.h);
      var1.addProperty("version", this.Y);
      if (this.h && this.U != null) {
         var1.addProperty("profile", this.F);
         var1.add("previous", this.U.deepCopy());
      }

      return var1;
   }

   public static _4 M(JsonObject var0) {
      _4 var1 = new _4();

      try {
         if (!p_.v(var0.get("active"))) {
            return var1;
         }

         String var2 = p_.g(p_.Q(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var0.has("version")) {
            p_.w(var0.get("version"), 1.0, 3.0, true);
         }

         int var4 = var0.has("version") ? var0.get("version").getAsInt() : 1;
         if (var3 == null || var3.size() != (var4 == 1 ? 6 : 8)) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.Y = var4;
         var1.h = (boolean)1;
         var1.F = var2;
         var1.U = var3.deepCopy();
      } catch (RuntimeException var5) {
         mt.T("Larp Preset", "Invalid local restore journal ignored; profile values retained: {}", var5.toString());
      }

      return var1;
   }

   private static <T extends Module> T Y(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static _4.i L(List<Module> var0, Class<? extends Module> var1, Object... var2) {
      Module var3 = Y(var0, var1);
      LinkedHashMap var4 = new LinkedHashMap();

      for (byte var5 = 0; var5 < var2.length; var5 += 2) {
         String var6 = (String)var2[var5];
         df var7 = var3.getSettings()
            .stream()
            .filter(var1x -> var1x.D().equals(var6))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing preset field: " + var3.getName() + "/" + var6));
         Object var8 = w0.C(var7, B.toJsonTree(var2[var5 + 1]), true);
         var4.put(var7, var8);
      }

      return new _4.i(var3, var4);
   }

   private static List<_4.i> F(List<Module> var0) {
      return List.of(
         L(
            var0,
            CustomSkyModule.class,
            "Sky Style",
            "Nebula",
            "Nebula Brightness",
            0.75,
            "Nebula Saturation",
            0.85,
            "Nebula Intensity",
            0.65,
            "Nebula Motion",
            "Flow",
            "Nebula Animation Speed",
            1.0,
            "Nebula Flow Strength",
            0.35,
            "Nebula Scale",
            1.0,
            "Nebula Color",
            -1,
            "Nebula Rotation Speed",
            0.02,
            "Match Fog",
            true,
            "Clouds",
            false
         ),
         L(
            var0,
            SelfVisualsModule.class,
            "Style",
            "Outline",
            "Model Opacity",
            0.85,
            "Outline",
            true,
            "Outline Color",
            -988929,
            "Outline Thickness",
            1.5,
            "Outline Opacity",
            0.8,
            "Outline Bloom",
            true,
            "Model Bloom",
            false,
            "Third Person",
            true
         ),
         L(
            var0,
            PlayerTrailModule.class,
            "Style",
            "Glow",
            "Player",
            true,
            "Width",
            0.09,
            "Lifetime",
            0.8,
            "Bloom",
            true,
            "Smoothing",
            true,
            "Color",
            -2043905,
            "Gradient",
            -7909428,
            "Trail Particles",
            true
         ),
         L(
            var0,
            TargetAuraModule.class,
            "Style",
            "Halo",
            "Speed",
            0.45,
            "Width",
            0.035,
            "Particles Per Second",
            12.0,
            "Bloom",
            true,
            "Theme Colors",
            false,
            "Color",
            -1780481,
            "Gradient",
            -7054365
         ),
         L(
            var0,
            HitFXModule.class,
            "Preset",
            "Energy",
            "Particles",
            true,
            "Hit Rings",
            true,
            "Amount",
            14.0,
            "Size",
            0.07,
            "Lifetime",
            0.6,
            "Bloom",
            true,
            "Color",
            -2571009,
            "Gradient",
            -7977523,
            "Ring Size",
            0.7,
            "Ring Fade",
            0.55
         ),
         L(
            var0,
            JumpFXModule.class,
            "Mode",
            "Terrain",
            "Color Theme",
            "Custom",
            "Color",
            -3758859,
            "Maximum Radius",
            3.0,
            "Pulse Duration",
            0.9,
            "Opacity",
            0.7,
            "Glow Intensity",
            0.55
         ),
         L(
            var0,
            WorldColorModule.class,
            "Ambient Color",
            -5342721,
            "Ambient Strength",
            0.65,
            "Tint",
            -1517057,
            "Shadow Tint",
            -3363084,
            "Highlight Tint",
            -923393,
            "Saturation",
            0.85,
            "Contrast",
            1.0,
            "Exposure",
            0.05,
            "Gamma",
            1.0,
            "Vignette",
            0.08
         ),
         L(
            var0,
            AmbientParticlesModule.class,
            "Style",
            "Both",
            "Radius",
            12.0,
            "Height",
            10.0,
            "Density",
            160.0,
            "Lifetime",
            5.0,
            "Speed",
            1.5,
            "Size",
            0.05,
            "Comet Count",
            12.0,
            "Vertical Travel",
            3.0,
            "Sideways Travel",
            2.5,
            "Motion Period",
            5.0,
            "Maximum Particles",
            1536.0,
            "Trail Length",
            0.8,
            "Trail Width",
            0.025,
            "Color",
            -1517057,
            "Tail Color",
            -6794006,
            "Opacity",
            0.7,
            "Bloom",
            true
         )
      );
   }

   private static JsonObject A(List<_4.i> var0) {
      JsonObject var1 = new JsonObject();

      for (_4.i var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.D(), B.toJsonTree(var1x.P())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<_4.i> O(List<_4.i> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (_4.i var4 : var0) {
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

            var2.add(new _4.i(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void o(List<_4.i> var0, JsonObject var1) {
      for (_4.i var3 : var0) {
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

   private static void Q(BooleanSupplier var0, RuntimeException var1) {
      try {
         if (!var0.getAsBoolean()) {
            mt.T("Larp Preset", "Recovery preference save failed; in-memory state retained for retry");
         }
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }
   }

   public boolean o(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.Y()) {
         return true;
      }

      try {
         List var5 = F(var2);
         if (var1) {
            this.Y = 3;
            this.U = A(var5);
            this.F = p_.g(var3);
            this.h = (boolean)1;

            try {
               if (!var4.getAsBoolean()) {
                  this.h = (boolean)0;
                  this.U = null;
                  this.F = "";
                  return false;
               }
            } catch (RuntimeException var13) {
               this.h = (boolean)0;
               this.U = null;
               this.F = "";
               throw var13;
            }

            this.k = (boolean)1;

            try {
               o(var5, null);
            } catch (RuntimeException var12) {
               try {
                  o(O(var5, this.U), this.U);
                  this.h = this.k = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               Q(var4, var12);
               throw var12;
            }
         } else {
            List var6 = O(var5, this.U);
            this.h = (boolean)0;

            try {
               if (!var4.getAsBoolean()) {
                  this.h = (boolean)1;
                  return false;
               }
            } catch (RuntimeException var14) {
               this.h = (boolean)1;
               throw var14;
            }

            try {
               o(var6, this.U);
               this.k = (boolean)0;
               this.U = null;
               this.F = "";
            } catch (RuntimeException var11) {
               this.h = (boolean)1;

               try {
                  o(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               Q(var4, var11);
               throw var11;
            }
         }

         mt.K("Larp Preset", var1 ? "Enabled eight cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var15) {
         mt.P("Larp Preset", "Could not change cosmetic preset; restore journal retained where needed", var15);
         return false;
      }
   }

   public boolean Q(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.h && !this.k) {
         try {
            if (!this.F.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = F(var1);
            if (this.Y < 3 && !this.H(var4, var3)) {
               return false;
            }

            List var5 = O(var4, this.U);

            try {
               o(var4, null);
               this.k = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  o(var5, this.U);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.k = (boolean)1;
                  mt.P("Larp Preset", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            mt.K("Larp Preset", "Resumed local cosmetic preset for {}", this.F);
            return true;
         } catch (RuntimeException var10) {
            this.h = this.k = (boolean)0;
            this.U = null;
            this.F = "";
            Q(var3, var10);
            mt.P("Larp Preset", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   private boolean H(List<_4.i> var1, BooleanSupplier var2) {
      JsonObject var3 = A(var1);
      Set var4 = Set.of("Sky Style", "Nebula Brightness", "Nebula Saturation", "Nebula Intensity");
      Set var5 = Set.of("Vertical Travel", "Sideways Travel", "Motion Period", "Maximum Particles");
      int var6 = 0;

      for (_4.i var8 : var1) {
         if (this.Y != 1 || !(var8.module instanceof WorldColorModule) && !(var8.module instanceof AmbientParticlesModule)) {
            JsonObject var9 = this.U.getAsJsonObject(var8.module.getName());
            p_.v(var9.get("enabled"));
            JsonObject var10 = var9.getAsJsonObject("settings");
            Set var11 = this.Y == 1 && var8.module instanceof CustomSkyModule
               ? var4
               : var8.values
                  .keySet()
                  .stream()
                  .map(df::D)
                  .filter(var2x -> !(var8.module instanceof AmbientParticlesModule) || !var5.contains(var2x))
                  .collect(Collectors.toSet());
            if (!var10.keySet().equals(var11)) {
               throw new IllegalArgumentException("Invalid legacy restore fields");
            }

            JsonObject var12 = var3.getAsJsonObject(var8.module.getName());
            var12.add("enabled", var9.get("enabled").deepCopy());

            for (df var14 : var8.values.keySet()) {
               if (var10.has(var14.D())) {
                  w0.C(var14, var10.get(var14.D()), true);
                  var12.getAsJsonObject("settings").add(var14.D(), var10.get(var14.D()).deepCopy());
               }
            }

            var6++;
         }
      }

      if (var6 != this.U.size()) {
         throw new IllegalArgumentException("Invalid legacy restore modules");
      }

      JsonObject var16 = this.U;
      int var17 = this.Y;
      this.U = var3;
      this.Y = 3;

      try {
         if (var2.getAsBoolean()) {
            return true;
         }
      } catch (RuntimeException var15) {
         mt.T("Larp Preset", "Preset journal upgrade save failed; loaded profile retained: {}", var15.toString());
      }

      this.U = var16;
      this.Y = var17;
      return false;
   }

   public void F(List<Module> var1, BooleanSupplier var2) {
      if (this.Y() && !this.o(false, var1, this.F, var2)) {
         throw new IllegalStateException("Larp Preset restore/save failed; configuration load cancelled");
      }
   }

   public void L(Module var1, JsonObject var2) {
      if (this.Y() && this.U != null && this.U.has(var1.getName())) {
         JsonObject var3 = this.U.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record i(Module module, Map<df<?>, Object> values) {
   }
}
