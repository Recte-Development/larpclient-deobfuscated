package com.larpclient.social.cosmetic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.config.ProfileValidator;
import com.larpclient.config.SettingCodec;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class CosmeticPresetJournal {
   private static final Gson gSON = new Gson();
   private boolean active;
   private boolean applied;
   private String profileName = "";
   private JsonObject previousSettings;

   public boolean isApplied() {
      return this.active && this.applied;
   }

   public void renameProfile(String var1, String var2) {
      if (this.active && this.profileName.equals(var1)) {
         this.profileName = ProfileValidator.validateName(var2);
      }
   }

   public JsonObject toJson() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.active);
      if (this.active && this.previousSettings != null) {
         var1.addProperty("profile", this.profileName);
         var1.add("previous", this.previousSettings.deepCopy());
      }

      return var1;
   }

   public static CosmeticPresetJournal fromJson(JsonObject var0) {
      CosmeticPresetJournal var1 = new CosmeticPresetJournal();

      try {
         if (!ProfileValidator.readBoolean(var0.get("active"))) {
            return var1;
         }

         String var2 = ProfileValidator.validateName(ProfileValidator.readString(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         if (var3.get("China Hat") instanceof JsonObject var4 && var4.get("settings") instanceof JsonObject var5) {
            ChinaHatModule.migrateLegacyMode(var5);
         }

         var1.active = (boolean)1;
         var1.profileName = var2;
         var1.previousSettings = var3.deepCopy();
      } catch (RuntimeException var7) {
         LarpLog.warn("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var7.toString());
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

   private static List<CosmeticPresetJournal.ModuleSettings> resume(List<Module> var0) {
      CustomSkyModule var1 = findModule(var0, CustomSkyModule.class);
      ChinaHatModule var2 = findModule(var0, ChinaHatModule.class);
      JumpFXModule var3 = findModule(var0, JumpFXModule.class);
      HitFXModule var4 = findModule(var0, HitFXModule.class);
      LinkedHashMap var5 = new LinkedHashMap();

      for (Setting var7 : var2.getHatToggles()) {
         var5.put(var7, var7.getName().equals("Kippah"));
      }

      var5.put(var2.getSelfSetting(), true);
      return List.of(
         new CosmeticPresetJournal.ModuleSettings(var1, Map.of(var1.getSkyStyleSetting(), "Israel Flag")),
         new CosmeticPresetJournal.ModuleSettings(var2, var5),
         new CosmeticPresetJournal.ModuleSettings(var3, Map.of(var3.getModeSetting(), "Star of David")),
         new CosmeticPresetJournal.ModuleSettings(var4, Map.of(var4.getPresetSetting(), "Cash", var4.describePreset(), true))
      );
   }

   private static JsonObject captureSettings(List<CosmeticPresetJournal.ModuleSettings> var0) {
      JsonObject var1 = new JsonObject();

      for (CosmeticPresetJournal.ModuleSettings var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.getName(), gSON.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<CosmeticPresetJournal.ModuleSettings> parseRestoreTargets(List<CosmeticPresetJournal.ModuleSettings> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (CosmeticPresetJournal.ModuleSettings var4 : var0) {
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

            var2.add(new CosmeticPresetJournal.ModuleSettings(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void fromJson(List<CosmeticPresetJournal.ModuleSettings> var0, JsonObject var1) {
      for (CosmeticPresetJournal.ModuleSettings var3 : var0) {
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

   public boolean setPresetEnabled(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.isApplied()) {
         return true;
      }

      try {
         List var5 = resume(var2);
         if (var1) {
            this.previousSettings = captureSettings(var5);
            this.profileName = ProfileValidator.validateName(var3);
            this.active = (boolean)1;
            if (!var4.getAsBoolean()) {
               this.active = (boolean)0;
               this.previousSettings = null;
               return false;
            }

            this.applied = (boolean)1;

            try {
               fromJson(var5, null);
            } catch (RuntimeException var12) {
               try {
                  fromJson(parseRestoreTargets(var5, this.previousSettings), this.previousSettings);
                  this.active = this.applied = (boolean)0;
               } catch (RuntimeException var10) {
                  var12.addSuppressed(var10);
               }

               var4.getAsBoolean();
               throw var12;
            }
         } else {
            List var6 = parseRestoreTargets(var5, this.previousSettings);
            this.active = (boolean)0;
            if (!var4.getAsBoolean()) {
               this.active = (boolean)1;
               return false;
            }

            try {
               fromJson(var6, this.previousSettings);
               this.applied = (boolean)0;
               this.previousSettings = null;
               this.profileName = "";
            } catch (RuntimeException var11) {
               this.active = (boolean)1;

               try {
                  fromJson(var5, null);
               } catch (RuntimeException var9) {
                  var11.addSuppressed(var9);
               }

               var4.getAsBoolean();
               throw var11;
            }
         }

         LarpLog.info("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
         return true;
      } catch (RuntimeException var13) {
         LarpLog.error("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var13);
         return false;
      }
   }

   public boolean resume(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.active && !this.applied) {
         try {
            if (!this.profileName.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            }

            List var4 = resume(var1);
            List var5 = parseRestoreTargets(var4, this.previousSettings);

            try {
               fromJson(var4, null);
               this.applied = (boolean)1;
            } catch (RuntimeException var9) {
               try {
                  fromJson(var5, this.previousSettings);
               } catch (RuntimeException var8) {
                  var9.addSuppressed(var8);
                  this.applied = (boolean)1;
                  LarpLog.error("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                  return false;
               }

               throw var9;
            }

            LarpLog.info("Israel mode", "Resumed local cosmetic preset for {}", this.profileName);
            return true;
         } catch (RuntimeException var10) {
            this.active = this.applied = (boolean)0;
            this.previousSettings = null;
            this.profileName = "";
            var3.getAsBoolean();
            LarpLog.error("Israel mode", "Could not resume preset; loaded profile retained", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public void restoreBeforeLoad(List<Module> var1, BooleanSupplier var2) {
      if (this.isApplied() && !this.setPresetEnabled(false, var1, this.profileName, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void overlayPreviousSettings(Module var1, JsonObject var2) {
      if (this.isApplied() && this.previousSettings != null && this.previousSettings.has(var1.getName())) {
         JsonObject var3 = this.previousSettings.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private record ModuleSettings(Module module, Map<Setting<?>, Object> values) {
   }
}
