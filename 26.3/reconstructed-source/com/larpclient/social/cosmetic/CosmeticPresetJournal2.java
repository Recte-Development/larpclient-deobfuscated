package com.larpclient.social.cosmetic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.config.ProfileValidator;
import com.larpclient.config.SettingCodec;
import com.larpclient.module.combat.TargetAuraModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.render.AmbientParticlesModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.PlayerTrailModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.module.render.WorldColorModule;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public final class CosmeticPresetJournal2 {
   private static final Gson gson = new Gson();
   private boolean active;
   private boolean applied;
   private String profile = "";
   private JsonObject previous;
   private int version = 3;

   public boolean isApplied() {
      return this.active && this.applied;
   }

   public void renameProfile(String var1, String var2) {
      if (this.active && this.profile.equals(var1)) {
         this.profile = ProfileValidator.validateName(var2);
      }
   }

   public JsonObject toJson() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.active);
      var1.addProperty("version", this.version);
      if (this.active && this.previous != null) {
         var1.addProperty("profile", this.profile);
         var1.add("previous", this.previous.deepCopy());
      }

      return var1;
   }

   public static CosmeticPresetJournal2 fromJson(JsonObject var0) {
      CosmeticPresetJournal2 var1 = new CosmeticPresetJournal2();

      try {
         if (!ProfileValidator.readBoolean(var0.get("active"))) {
            return var1;
         }

         String var2 = ProfileValidator.validateName(ProfileValidator.readString(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var0.has("version")) {
            ProfileValidator.checkNumber(var0.get("version"), 1.0, 3.0, true);
         }

         int var4 = var0.has("version") ? var0.get("version").getAsInt() : 1;
         if (var3 == null || var3.size() != (var4 == 1 ? 6 : 8)) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.version = var4;
         var1.active = (boolean)1;
         var1.profile = var2;
         var1.previous = var3.deepCopy();
      } catch (RuntimeException var5) {
         LarpLog.warn("Larp Preset", "Invalid local restore journal ignored; profile values retained: {}", var5.toString());
      }

      return var1;
   }

   private static <T extends Module> T findModule(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static CosmeticPresetJournal2.ModuleSnapshot presetEntry(List<Module> var0, Class<? extends Module> var1, Object... var2) {
      Module var3 = findModule(var0, var1);
      LinkedHashMap var4 = new LinkedHashMap();

      for (byte var5 = 0; var5 < var2.length; var5 += 2) {
         String var6 = (String)var2[var5];
         Setting var7 = var3.getSettings()
            .stream()
            .filter(var1x -> var1x.getName().equals(var6))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing preset field: " + var3.getName() + "/" + var6));
         Object var8 = SettingCodec.parseValue(var7, gson.toJsonTree(var2[var5 + 1]), true);
         var4.put(var7, var8);
      }

      return new CosmeticPresetJournal2.ModuleSnapshot(var3, var4);
   }

   private static List<CosmeticPresetJournal2.ModuleSnapshot> presetEntries(List<Module> var0) {
      return List.of(
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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
         presetEntry(
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

   private static JsonObject snapshot(List<CosmeticPresetJournal2.ModuleSnapshot> var0) {
      JsonObject var1 = new JsonObject();

      for (CosmeticPresetJournal2.ModuleSnapshot var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.getName(), gson.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<CosmeticPresetJournal2.ModuleSnapshot> parseSnapshot(List<CosmeticPresetJournal2.ModuleSnapshot> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (CosmeticPresetJournal2.ModuleSnapshot var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            ProfileValidator.readBoolean(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (Setting var9 : var4.values.keySet()) {
               var7.put(var9, SettingCodec.parseValue(var9, var6.get(var9.getName()), true));
            }

            var2.add(new CosmeticPresetJournal2.ModuleSnapshot(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void applyEntries(List<CosmeticPresetJournal2.ModuleSnapshot> var0, JsonObject var1) {
      for (CosmeticPresetJournal2.ModuleSnapshot var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((Setting<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !ProfileValidator.readBoolean(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled((boolean)var4);
         if (var3.module.isEnabled() != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   private static void saveAfterFailure(BooleanSupplier var0, RuntimeException var1) {
      try {
         if (!var0.getAsBoolean()) {
            LarpLog.warn("Larp Preset", "Recovery preference save failed; in-memory state retained for retry");
         }
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }
   }

   public boolean setEnabled(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.isApplied()) {
         return true;
      }

      try {
         List var5 = presetEntries(var2);
         if (var1) {
            this.version = 3;
            this.previous = snapshot(var5);
            this.profile = ProfileValidator.validateName(var3);
            this.active = (boolean)1;

            try {
               if (!var4.getAsBoolean()) {
                  this.active = (boolean)0;
                  this.previous = null;
                  this.profile = "";
                  return false;
               }
            } catch (RuntimeException var13) {
               this.active = (boolean)0;
               this.previous = null;
               this.profile = "";
               throw var13;
            }

            this.applied = (boolean)1;

            try {
               applyEntries(var5, null);
            } catch (RuntimeException var12) {
               try {
                  applyEntries(parseSnapshot(var5, this.previous), this.previous);
                  this.active = this.applied = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               saveAfterFailure(var4, var12);
               throw var12;
            }
         } else {
            List var6 = parseSnapshot(var5, this.previous);
            this.active = (boolean)0;

            try {
               if (!var4.getAsBoolean()) {
                  this.active = (boolean)1;
                  return false;
               }
            } catch (RuntimeException var14) {
               this.active = (boolean)1;
               throw var14;
            }

            try {
               applyEntries(var6, this.previous);
               this.applied = (boolean)0;
               this.previous = null;
               this.profile = "";
            } catch (RuntimeException var11) {
               this.active = (boolean)1;

               try {
                  applyEntries(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               saveAfterFailure(var4, var11);
               throw var11;
            }
         }

         LarpLog.info("Larp Preset", var1 ? "Enabled eight cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var15) {
         LarpLog.error("Larp Preset", "Could not change cosmetic preset; restore journal retained where needed", var15);
         return false;
      }
   }

   public boolean presetEntries(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.active && !this.applied) {
         try {
            if (!this.profile.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = presetEntries(var1);
            if (this.version < 3 && !this.upgradeLegacyJournal(var4, var3)) {
               return false;
            }

            List var5 = parseSnapshot(var4, this.previous);

            try {
               applyEntries(var4, null);
               this.applied = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  applyEntries(var5, this.previous);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.applied = (boolean)1;
                  LarpLog.error("Larp Preset", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            LarpLog.info("Larp Preset", "Resumed local cosmetic preset for {}", this.profile);
            return true;
         } catch (RuntimeException var10) {
            this.active = this.applied = (boolean)0;
            this.previous = null;
            this.profile = "";
            saveAfterFailure(var3, var10);
            LarpLog.error("Larp Preset", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   private boolean upgradeLegacyJournal(List<CosmeticPresetJournal2.ModuleSnapshot> var1, BooleanSupplier var2) {
      JsonObject var3 = snapshot(var1);
      Set var4 = Set.of("Sky Style", "Nebula Brightness", "Nebula Saturation", "Nebula Intensity");
      Set var5 = Set.of("Vertical Travel", "Sideways Travel", "Motion Period", "Maximum Particles");
      int var6 = 0;

      for (CosmeticPresetJournal2.ModuleSnapshot var8 : var1) {
         if (this.version != 1 || !(var8.module instanceof WorldColorModule) && !(var8.module instanceof AmbientParticlesModule)) {
            JsonObject var9 = this.previous.getAsJsonObject(var8.module.getName());
            ProfileValidator.readBoolean(var9.get("enabled"));
            JsonObject var10 = var9.getAsJsonObject("settings");
            Set var11 = this.version == 1 && var8.module instanceof CustomSkyModule
               ? var4
               : var8.values
                  .keySet()
                  .stream()
                  .map(Setting::getName)
                  .filter(var2x -> !(var8.module instanceof AmbientParticlesModule) || !var5.contains(var2x))
                  .collect(Collectors.toSet());
            if (!var10.keySet().equals(var11)) {
               throw new IllegalArgumentException("Invalid legacy restore fields");
            }

            JsonObject var12 = var3.getAsJsonObject(var8.module.getName());
            var12.add("enabled", var9.get("enabled").deepCopy());

            for (Setting var14 : var8.values.keySet()) {
               if (var10.has(var14.getName())) {
                  SettingCodec.parseValue(var14, var10.get(var14.getName()), true);
                  var12.getAsJsonObject("settings").add(var14.getName(), var10.get(var14.getName()).deepCopy());
               }
            }

            var6++;
         }
      }

      if (var6 != this.previous.size()) {
         throw new IllegalArgumentException("Invalid legacy restore modules");
      }

      JsonObject var16 = this.previous;
      int var17 = this.version;
      this.previous = var3;
      this.version = 3;

      try {
         if (var2.getAsBoolean()) {
            return true;
         }
      } catch (RuntimeException var15) {
         LarpLog.warn("Larp Preset", "Preset journal upgrade save failed; loaded profile retained: {}", var15.toString());
      }

      this.previous = var16;
      this.version = var17;
      return false;
   }

   public void restoreBeforeLoad(List<Module> var1, BooleanSupplier var2) {
      if (this.isApplied() && !this.setEnabled(false, var1, this.profile, var2)) {
         throw new IllegalStateException("Larp Preset restore/save failed; configuration load cancelled");
      }
   }

   public void overlayPrevious(Module var1, JsonObject var2) {
      if (this.isApplied() && this.previous != null && this.previous.has(var1.getName())) {
         JsonObject var3 = this.previous.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record ModuleSnapshot(Module module, Map<Setting<?>, Object> values) {
   }
}
