package com.larpclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.LarpClient;
import com.larpclient.gui.misc.UiState;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.setting.SettingShortcuts;
import com.larpclient.social.cosmetic.CosmeticPresetJournal;
import com.larpclient.social.cosmetic.CosmeticPresetJournal2;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SafetyLevel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

public final class ConfigManager {
   private static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path configDir;
   private final Path profilesDir;
   private final Path activeProfileFile;
   private final PreferencesWriter preferenceWriter = new PreferencesWriter(ConfigManager::deleteProfile);
   private String activeProfile = "Default";
   private String rejectedProfile;
   private ClientPreferences preferences = new ClientPreferences();
   private final Map<String, JsonObject> legacySettings = new HashMap<>();
   private JsonObject baseConfig = new JsonObject();
   private JsonObject lastSnapshot = new JsonObject();
   public static boolean toggle1;
   public static int setting1;
   public static int setting2;
   public static int setting3;
   public static boolean toggle2;
   public static boolean toggle3;
   public static int setting4;
   public static int setting5;
   public static int setting6;
   public static boolean toggle4;
   public static int setting7;
   public static boolean toggle5;
   public static int setting8;
   public static boolean toggle6;
   public static boolean toggle7;
   public static boolean toggle8;
   public static boolean toggle9;
   public static int setting9;
   public static boolean toggle10;
   public static int setting10;
   public static boolean toggle11;
   public static boolean toggle12;
   public static int setting11;
   public static int setting12;
   public static int setting13;
   public static int setting14;
   public static int setting15;
   public static boolean toggle13;
   public static boolean toggle14;
   public static int setting16;
   public static int setting17;
   public static int setting18;
   public static boolean toggle15;
   public static boolean toggle16;
   public static int setting19;
   public static int setting20;
   public static boolean toggle17;
   public static int setting21;
   public static int setting22;
   public static int setting23;
   public static boolean toggle18;
   public static boolean toggle19;
   public static int setting24;
   public static int setting25;
   public static boolean toggle20;
   public static int setting26;
   public static int setting27;
   public static int setting28;
   public static boolean toggle21;
   public static boolean toggle22;
   public static boolean toggle23;
   public static int setting29;
   public static int setting30;
   public static boolean toggle24;
   public static boolean toggle25;
   public static int setting31;
   public static boolean toggle26;
   public static boolean toggle27;
   public static boolean toggle28;

   public ConfigManager() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public ConfigManager(Path var1) {
      this.configDir = var1;
      this.profilesDir = var1.resolve("profiles");
      this.activeProfileFile = var1.resolve("active-profile.txt");
   }

   public ClientPreferences getPreferences() {
      return this.preferences;
   }

   public boolean setFeatureEnabled(boolean var1) {
      boolean var2 = this.preferences.discordPresence;
      if (var2 == var1) {
         return true;
      }

      this.preferences.discordPresence = var1;
      if (!this.savePreferences()) {
         this.preferences.discordPresence = var2;
         return false;
      }

      LarpClient var3 = LarpClient.getInstance();
      if (var3 != null) {
         var3.getModuleManager(var1);
      }

      return true;
   }

   public boolean loadProfile(boolean var1, List<Module> var2) {
      return var1
            && this.preferences.israelMode.isApplied()
            && !this.preferences.israelMode.setPresetEnabled(false, var2, this.activeProfile, this::savePreferences)
         ? false
         : this.preferences.larpPreset.setEnabled(var1, var2, this.activeProfile, this::savePreferences);
   }

   public boolean saveProfileAs(boolean var1, List<Module> var2) {
      return var1 && this.preferences.larpPreset.isApplied() && !this.preferences.larpPreset.setEnabled(false, var2, this.activeProfile, this::savePreferences)
         ? false
         : this.preferences.israelMode.setPresetEnabled(var1, var2, this.activeProfile, this::savePreferences);
   }

   public boolean saveRecoveryProfile(boolean var1) {
      if (this.preferences.softParticles == var1) {
         return true;
      } else {
         boolean var2 = this.preferences.softParticles;
         this.preferences.softParticles = var1;
         if (!this.savePreferences()) {
            this.preferences.softParticles = var2;
            return false;
         } else {
            SoftParticleRenderer.enabled = var1;
            return true;
         }
      }
   }

   public boolean setTheme(PostProcessing.Quality var1) {
      Objects.requireNonNull(var1);
      PostProcessing.Quality var2 = this.preferences.effectsQuality;
      if (var1 == var2) {
         return true;
      } else {
         this.preferences.effectsQuality = var1;
         if (!this.savePreferences()) {
            this.preferences.effectsQuality = var2;
            return false;
         } else {
            SafetyLevel.current = var1;
            return true;
         }
      }
   }

   public boolean setShortcut(Module var1, Setting<?> var2, int var3) {
      SettingShortcuts var4 = this.preferences.settingBinds.copy();
      this.preferences.settingBinds.setCode(var1, var2, var3);
      if (this.savePreferences()) {
         LarpLog.info("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.getName(), var3);
         return true;
      } else {
         this.preferences.settingBinds = var4;
         return false;
      }
   }

   public boolean savePreferences() {
      try {
         this.preferenceWriter.saveNow(this.configDir.resolve("menu.json"), gson.toJson(this.preferences.toJson()));
         return true;
      } catch (IOException var2) {
         LarpLog.error("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void queuePreferencesWrite() {
      this.preferenceWriter.scheduleSave(this.configDir.resolve("menu.json"), gson.toJson(this.preferences.toJson()));
   }

   public void saveActiveProfile() {
      if (this.rejectedProfile != null) {
         this.saveRecoveryProfile();
      } else {
         String var1;
         try {
            var1 = getPreferences(this.currentConfig());
         } catch (IllegalArgumentException var3) {
            LarpLog.error("Config", "Profile " + this.activeProfile + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.preferenceWriter.scheduleSave(this.profilePath(this.activeProfile), var1);
         this.preferenceWriter.scheduleSave(this.activeProfileFile, this.activeProfile);
         this.queuePreferencesWrite();
      }
   }

   public String getPersistenceStatus() {
      return this.preferenceWriter.getStatus();
   }

   public void close() {
      try {
         this.preferenceWriter.close();
      } catch (IOException var2) {
         LarpLog.error("Config", "Final preference flush failed", var2);
      }
   }

   private void loadPreferences() {
      Path var1 = this.configDir.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.preferences = ClientPreferences.fromJson(this.readJson(var1));
         } catch (Exception var3) {
            LarpLog.error("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void renameProfile(String var1, String var2) throws IOException {
      this.preferenceWriter.flush();
      Path var3 = this.profilePath(var1);
      Path var4 = this.profilePath(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.profilesDir) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.activeProfile.equals(var1)) {
               deleteProfile(this.activeProfileFile, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.activeProfile.equals(var1)) {
               deleteProfile(this.activeProfileFile, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.activeProfile.equals(var1)) {
            this.activeProfile = var2;
         }

         this.preferences.renameModule(var1, var2);
         this.savePreferences();
         LarpLog.info("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void deleteProfile(String var1) throws IOException {
      this.preferenceWriter.flush();
      if (this.activeProfile.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.profilePath(var1);
      if (!Files.isSymbolicLink(this.profilesDir) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.preferences.forgetModule(var1);
         this.savePreferences();
         LarpLog.info("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void saveProfileAs(String var1, boolean var2) throws IOException {
      this.getPersistenceStatus(var1, this.currentConfig(), var2, true);
   }

   public synchronized void copyProfile(String var1, String var2) throws IOException {
      this.getPersistenceStatus(var2, this.readProfile(var1), false, true);
   }

   public String getActiveProfile() {
      return this.activeProfile;
   }

   private Path profilePath(String var1) {
      ProfileValidator.validateName(var1);
      return this.profilesDir.resolve(var1 + ".json");
   }

   public List<String> listProfiles() {
      try {
         if (!Files.isDirectory(this.profilesDir)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.profilesDir)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     ProfileValidator.validateName(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         LarpLog.error("Config", "Cannot list profiles at " + this.profilesDir, var6);
         return List.of();
      }
   }

   public void hasProfile() {
      if (this.rejectedProfile != null) {
         this.saveRecoveryProfile();
      } else {
         this.saveProfile(this.activeProfile);
      }
   }

   public boolean saveProfile(String var1) {
      try {
         this.preferenceWriter.flush();
         Path var2 = this.profilePath(var1);
         deleteProfile(var2, getPreferences(this.currentConfig()));
         deleteProfile(this.activeProfileFile, var1);
         this.preferences.israelMode.renameProfile(this.activeProfile, var1);
         this.preferences.larpPreset.renameProfile(this.activeProfile, var1);
         this.activeProfile = var1;
         this.rejectedProfile = null;
         if (this.preferences.israelMode.isApplied() || this.preferences.larpPreset.isApplied()) {
            this.savePreferences();
         }

         LarpLog.info("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         LarpLog.error("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean loadProfile(String var1) {
      try {
         this.preferenceWriter.flush();
         Path var2 = this.profilePath(var1);
         if (!Files.exists(var2)) {
            LarpLog.warn("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.readProfile(var1);
         String var4 = this.activeProfile;
         deleteProfile(this.activeProfileFile, var1);

         try {
            this.applyConfig(var3);
         } catch (RuntimeException var6) {
            deleteProfile(this.activeProfileFile, var4);
            throw var6;
         }

         this.activeProfile = var1;
         this.rejectedProfile = null;
         this.preferences.clickGuiStyle(var1, System.currentTimeMillis());
         this.savePreferences();
         LarpLog.info("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         LarpLog.error("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void deleteProfileQuietly(String var1) {
      try {
         this.deleteProfile(var1);
      } catch (Exception var3) {
         LarpLog.error("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void load() {
      LarpLog.info("Config", "Configuration directory: {}", this.configDir);
      this.loadPreferences();

      try {
         Files.createDirectories(this.profilesDir);
         if (Files.exists(this.activeProfileFile)) {
            String var1 = Files.readString(this.activeProfileFile).trim();

            try {
               this.profilePath(var1);
               this.activeProfile = var1;
            } catch (IllegalArgumentException var3) {
               LarpLog.warn("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.profilePath(this.activeProfile);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.loadProfile(this.activeProfile)) {
               this.rejectedProfile = this.activeProfile;
               this.saveRecoveryProfile();
            }
         } else {
            Path var2 = this.configDir.resolve("modules.json");
            if (Files.exists(var2)) {
               this.applyConfig(this.readJson(var2));
               LarpLog.info("Config", "Migrated legacy {} to schema 2", var2);
               this.hasProfile();
            }
         }
      } catch (Exception var4) {
         this.rejectedProfile = this.activeProfile;
         LarpLog.error("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.saveRecoveryProfile();
      }

      SoftParticleRenderer.enabled = this.preferences.softParticles;
      SafetyLevel.current = this.preferences.effectsQuality;
      this.getPreferences(saveProfile());
   }

   private synchronized void saveRecoveryProfile() {
      String var1 = this.rejectedProfile;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.profilesDir)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.profilesDir);
            Path var2 = Files.createTempFile(this.profilesDir, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, getPreferences(this.currentConfig()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.profilePath(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               deleteProfile(this.activeProfileFile, var5);
               this.preferences.israelMode.renameProfile(this.activeProfile, var5);
               this.preferences.larpPreset.renameProfile(this.activeProfile, var5);
               this.activeProfile = var5;
               this.rejectedProfile = null;
               this.savePreferences();
               LarpLog.warn("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            LarpLog.error("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void getPreferences(List<Module> var1) {
      if (this.preferences.israelMode.toJson().get("active").getAsBoolean() && this.preferences.larpPreset.toJson().get("active").getAsBoolean()) {
         this.preferences.israelMode = new CosmeticPresetJournal();
         this.preferences.larpPreset = new CosmeticPresetJournal2();
         this.savePreferences();
         LarpLog.warn("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.preferences.israelMode.resume(var1, this.activeProfile, this::savePreferences);
      this.preferences.larpPreset.presetEntries(var1, this.activeProfile, this::savePreferences);
   }

   private JsonObject readJson(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         LarpLog.error("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject readProfile(String var1) throws IOException {
      this.preferenceWriter.flush();
      Path var2 = this.profilePath(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.profilesDir) && Files.size(var2) <= 131072L) {
         return this.readJson(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean hasProfile(String var1) {
      return Files.exists(this.profilePath(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void deleteProfile(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.getPersistenceStatus(var1, var2, var3, false);
   }

   private synchronized void getPersistenceStatus(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.preferenceWriter.flush();
      var2 = var4 ? ProfileValidator.sanitize(var2) : ProfileValidator.sanitizeStrict(var2);
      Path var5 = this.profilePath(var1);
      if (!Files.isSymbolicLink(this.profilesDir) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = getPreferences(var2);
         if (var3) {
            deleteProfile(var5, var6);
         } else {
            Files.createDirectories(this.profilesDir);
            Path var7 = Files.createTempFile(this.profilesDir, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         LarpLog.debug("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject currentConfig() {
      return this.readJson(saveProfile());
   }

   private static String getPreferences(JsonObject var0) {
      ProfileValidator.sanitize(var0);
      String var1 = gson.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject readJson(List<Module> var1) {
      return mergeConfig(this.baseConfig, this.readProfile(var1), this.lastSnapshot).getAsJsonObject();
   }

   private static JsonElement mergeConfig(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), mergeConfig(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject readProfile(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", UiState.save());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (Setting var9 : var5.getSettings()) {
            var7.add(var9.getName(), gson.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.preferences.israelMode.overlayPreviousSettings(var5, var6);
         this.preferences.larpPreset.overlayPrevious(var5, var6);
         if (this.legacySettings.containsKey(var5.getName())) {
            var6.add("legacySettings", this.legacySettings.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void applyConfig(JsonObject var1) {
      this.readProfile(var1, saveProfile());
   }

   private static List<Module> saveProfile() {
      LarpClient var0 = LarpClient.getInstance();
      return var0 == null ? List.of() : var0.getModuleManager().getModules();
   }

   void readProfile(JsonObject var1, List<Module> var2) {
      JsonObject var3 = ProfileValidator.sanitize(var1);
      ConfigMigrator2.logMigration(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.preferences.israelMode.restoreBeforeLoad(var2, this::savePreferences);
      this.preferences.larpPreset.restoreBeforeLoad(var2, this::savePreferences);
      if (var1.has("ui")) {
         UiState.load(var1.getAsJsonObject("ui"));
      } else {
         UiState.revision++;
      }

      if (!var1.has("schema")) {
         LarpLog.info("Config", "Reading legacy module configuration");
      }

      ConfigMigrator.migrate(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.legacySettings.clear();

      for (Entry var16 : var4.entrySet()) {
         String var17 = (String)var16.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var17)).findFirst().orElse(null);
         if (var9 == null) {
            LarpLog.info("Config", "Unknown module {} ignored; its values are kept for future saves", var17);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var16.getValue()).getAsJsonObject();
               ModuleConfigEntry var11 = ModuleConfigEntry.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.legacySettings.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               LarpLog.error("Config", "Invalid module entry " + var17, var12);
            }
         }
      }

      this.baseConfig = var1.deepCopy();
      this.lastSnapshot = this.readProfile(var2);
      LarpLog.info("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void deleteProfile(Path var0, String var1) throws IOException {
      Files.createDirectories(var0.getParent());
      Path var2 = Files.createTempFile(var0.getParent(), "larp-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
