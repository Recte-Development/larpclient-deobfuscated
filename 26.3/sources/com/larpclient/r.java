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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public final class r {
   private static final Gson n = new Gson();
   private boolean U;
   private boolean m;
   private String J = "";
   private JsonObject k;
   private int b = 3;

   public boolean w() {
      return this.U && this.m;
   }

   public void c(String var1, String var2) {
      if (this.U && this.J.equals(var1)) {
         this.J = _4.p(var2);
      }
   }

   public JsonObject h() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.U);
      var1.addProperty("version", this.b);
      if (this.U && this.k != null) {
         var1.addProperty("profile", this.J);
         var1.add("previous", this.k.deepCopy());
      }

      return var1;
   }

   public static r Q(JsonObject var0) {
      r var1 = new r();

      try {
         if (!_4.Y(var0.get("active"))) {
            return var1;
         }

         String var2 = _4.p(_4.m(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var0.has("version")) {
            _4.N(var0.get("version"), 1.0, 3.0, true);
         }

         int var4 = var0.has("version") ? var0.get("version").getAsInt() : 1;
         if (var3 == null || var3.size() != (var4 == 1 ? 6 : 8)) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.b = var4;
         var1.U = (boolean)1;
         var1.J = var2;
         var1.k = var3.deepCopy();
      } catch (RuntimeException var5) {
         _q.R("Larp Preset", "Invalid local restore journal ignored; profile values retained: {}", var5.toString());
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

   private static r.f P(List<Module> var0, Class<? extends Module> var1, Object... var2) {
      Module var3 = l(var0, var1);
      LinkedHashMap var4 = new LinkedHashMap();

      for (byte var5 = 0; var5 < var2.length; var5 += 2) {
         String var6 = (String)var2[var5];
         com.larpclient.module.r var7 = var3.getSettings()
            .stream()
            .filter(var1x -> var1x.X().equals(var6))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing preset field: " + var3.getName() + "/" + var6));
         Object var8 = iu.e(var7, n.toJsonTree(var2[var5 + 1]), true);
         var4.put(var7, var8);
      }

      return new r.f(var3, var4);
   }

   private static List<r.f> I(List<Module> var0) {
      return List.of(
         P(
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
         P(
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
         P(
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
         P(
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
         P(
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
         P(
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
         P(
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
         P(
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

   private static JsonObject m(List<r.f> var0) {
      JsonObject var1 = new JsonObject();

      for (r.f var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.X(), n.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<r.f> o(List<r.f> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (r.f var4 : var0) {
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

            var2.add(new r.f(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void z(List<r.f> var0, JsonObject var1) {
      for (r.f var3 : var0) {
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

   private static void E(BooleanSupplier var0, RuntimeException var1) {
      try {
         if (!var0.getAsBoolean()) {
            _q.R("Larp Preset", "Recovery preference save failed; in-memory state retained for retry");
         }
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }
   }

   public boolean t(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.w()) {
         return true;
      }

      try {
         List var5 = I(var2);
         if (var1) {
            this.b = 3;
            this.k = m(var5);
            this.J = _4.p(var3);
            this.U = (boolean)1;

            try {
               if (!var4.getAsBoolean()) {
                  this.U = (boolean)0;
                  this.k = null;
                  this.J = "";
                  return false;
               }
            } catch (RuntimeException var13) {
               this.U = (boolean)0;
               this.k = null;
               this.J = "";
               throw var13;
            }

            this.m = (boolean)1;

            try {
               z(var5, null);
            } catch (RuntimeException var12) {
               try {
                  z(o(var5, this.k), this.k);
                  this.U = this.m = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               E(var4, var12);
               throw var12;
            }
         } else {
            List var6 = o(var5, this.k);
            this.U = (boolean)0;

            try {
               if (!var4.getAsBoolean()) {
                  this.U = (boolean)1;
                  return false;
               }
            } catch (RuntimeException var14) {
               this.U = (boolean)1;
               throw var14;
            }

            try {
               z(var6, this.k);
               this.m = (boolean)0;
               this.k = null;
               this.J = "";
            } catch (RuntimeException var11) {
               this.U = (boolean)1;

               try {
                  z(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               E(var4, var11);
               throw var11;
            }
         }

         _q.z("Larp Preset", var1 ? "Enabled eight cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var15) {
         _q.o("Larp Preset", "Could not change cosmetic preset; restore journal retained where needed", var15);
         return false;
      }
   }

   public boolean I(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.U && !this.m) {
         try {
            if (!this.J.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = I(var1);
            if (this.b < 3 && !this.S(var4, var3)) {
               return false;
            }

            List var5 = o(var4, this.k);

            try {
               z(var4, null);
               this.m = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  z(var5, this.k);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.m = (boolean)1;
                  _q.o("Larp Preset", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            _q.z("Larp Preset", "Resumed local cosmetic preset for {}", this.J);
            return true;
         } catch (RuntimeException var10) {
            this.U = this.m = (boolean)0;
            this.k = null;
            this.J = "";
            E(var3, var10);
            _q.o("Larp Preset", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   private boolean S(List<r.f> var1, BooleanSupplier var2) {
      JsonObject var3 = m(var1);
      Set var4 = Set.of("Sky Style", "Nebula Brightness", "Nebula Saturation", "Nebula Intensity");
      Set var5 = Set.of("Vertical Travel", "Sideways Travel", "Motion Period", "Maximum Particles");
      int var6 = 0;

      for (r.f var8 : var1) {
         if (this.b != 1 || !(var8.module instanceof WorldColorModule) && !(var8.module instanceof AmbientParticlesModule)) {
            JsonObject var9 = this.k.getAsJsonObject(var8.module.getName());
            _4.Y(var9.get("enabled"));
            JsonObject var10 = var9.getAsJsonObject("settings");
            Set var11 = this.b == 1 && var8.module instanceof CustomSkyModule
               ? var4
               : var8.values
                  .keySet()
                  .stream()
                  .map(com.larpclient.module.r::X)
                  .filter(var2x -> !(var8.module instanceof AmbientParticlesModule) || !var5.contains(var2x))
                  .collect(Collectors.toSet());
            if (!var10.keySet().equals(var11)) {
               throw new IllegalArgumentException("Invalid legacy restore fields");
            }

            JsonObject var12 = var3.getAsJsonObject(var8.module.getName());
            var12.add("enabled", var9.get("enabled").deepCopy());

            for (com.larpclient.module.r var14 : var8.values.keySet()) {
               if (var10.has(var14.X())) {
                  iu.e(var14, var10.get(var14.X()), true);
                  var12.getAsJsonObject("settings").add(var14.X(), var10.get(var14.X()).deepCopy());
               }
            }

            var6++;
         }
      }

      if (var6 != this.k.size()) {
         throw new IllegalArgumentException("Invalid legacy restore modules");
      }

      JsonObject var16 = this.k;
      int var17 = this.b;
      this.k = var3;
      this.b = 3;

      try {
         if (var2.getAsBoolean()) {
            return true;
         }
      } catch (RuntimeException var15) {
         _q.R("Larp Preset", "Preset journal upgrade save failed; loaded profile retained: {}", var15.toString());
      }

      this.k = var16;
      this.b = var17;
      return false;
   }

   public void C(List<Module> var1, BooleanSupplier var2) {
      if (this.w() && !this.t(false, var1, this.J, var2)) {
         throw new IllegalStateException("Larp Preset restore/save failed; configuration load cancelled");
      }
   }

   public void V(Module var1, JsonObject var2) {
      if (this.w() && this.k != null && this.k.has(var1.getName())) {
         JsonObject var3 = this.k.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record f(Module module, Map<com.larpclient.module.r<?>, Object> values) {
   }
}
