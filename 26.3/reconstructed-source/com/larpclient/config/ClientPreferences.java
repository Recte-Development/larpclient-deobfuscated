package com.larpclient.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.ToastAnimation;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.sky.SkyboxTheme;
import com.larpclient.setting.SettingShortcuts;
import com.larpclient.social.cosmetic.CosmeticPresetJournal;
import com.larpclient.social.cosmetic.CosmeticPresetJournal2;
import com.larpclient.util.misc.DisplayFormat;
import com.larpclient.util.misc.LarpLog;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

public final class ClientPreferences {
   public SkyboxTheme background = SkyboxTheme.nEBULA;
   public boolean motion = (boolean)1;
   public boolean updateAutoCheck = (boolean)1;
   public String updateSkippedRelease = "";
   public String updateSeenReceipt = "";
   public boolean discordPresence = (boolean)1;
   public boolean legitMode;
   public int legitModeKey = 47;
   public DisplayFormat.Season menuSeason = DisplayFormat.Season.SUMMER;
   public DisplayFormat.MenuQuality menuQuality = DisplayFormat.MenuQuality.HIGH;
   public double menuTime = 0.33;
   public boolean cinematicMenu = (boolean)1;
   public boolean customMenu = (boolean)1;
   public boolean clickGuiLight;
   public boolean clickGuiGradient;
   public String clickGuiAnimation = "Fade";
   public static final List<String> cLOSE_ANIMATIONS;
   public String clickGuiCloseAnimation = "Reverse";
   public int clickGuiDuration = 280;
   public double clickGuiOpacity = 0.67;
   public static final double mIN_SCALE = 0.5;
   public static final double mAX_SCALE = 2.0;
   public double clickGuiScale = 1.0;
   public static final List<String> bACKDROPS;
   public String clickGuiBackdrop = "None";
   public static final double mAX_DIM = 0.9;
   public double clickGuiDim = 0.42;
   public static final int mAX_BLUR = 4;
   public int clickGuiBlur = 2;
   public static final List<String> bLUR_AREAS;
   public String clickGuiBlurArea = "Window";
   public static final List<String> cARD_STYLES;
   public static final List<String> eNABLED_STYLES;
   public static final List<String> sWITCH_STYLES;
   public static final List<String> sLIDER_STYLES;
   public static final double mAX_ROUNDING = 2.0;
   public String clickGuiCardStyle = "Filled";
   public String clickGuiEnabledStyle = "Glow";
   public String clickGuiSwitchStyle = "Pill";
   public String clickGuiSliderStyle = "Pill";
   public double clickGuiRounding = 1.0;
   public boolean clickGuiAccentGlow = (boolean)1;
   public boolean clickGuiRipple = (boolean)1;
   public boolean clickGuiHoverLift = (boolean)1;
   private static final int bACKDROP_REVISION = 2;
   public boolean softParticles = (boolean)1;
   public PostProcessing.Quality effectsQuality = PostProcessing.Quality.BALANCED;
   public String skySource = "World";
   public CosmeticPresetJournal israelMode = new CosmeticPresetJournal();
   public CosmeticPresetJournal2 larpPreset = new CosmeticPresetJournal2();
   public SettingShortcuts settingBinds = new SettingShortcuts();
   public PanelLayoutConfig legacyClickGui = new PanelLayoutConfig();
   private final Set<String> favorites = new LinkedHashSet<>();
   private final Set<String> moduleFavorites = new LinkedHashSet<>();
   private final Set<String> hudLocks = new LinkedHashSet<>();
   public boolean moduleFavoritesOpen;
   private final Map<String, Long> recent = new LinkedHashMap<>();
   public static boolean toggleOnSave;
   private static final String[] kEYS;

   public Theme.Look clickGuiStyle() {
      return new Theme.Look(
         this.clickGuiCardStyle,
         this.clickGuiEnabledStyle,
         this.clickGuiSwitchStyle,
         this.clickGuiSliderStyle,
         this.clickGuiRounding,
         this.clickGuiAccentGlow,
         this.clickGuiRipple,
         this.clickGuiHoverLift
      );
   }

   public boolean isHudLocked(String var1) {
      return this.hudLocks.contains(var1);
   }

   public void setHudLocked(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.hudLocks.size() < 128) {
            this.hudLocks.add(var1);
         } else if (!var2) {
            this.hudLocks.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean clickGuiStyle(String var1) {
      return this.moduleFavorites.contains(var1) || "CustomSky".equals(var1) && this.moduleFavorites.contains("Sky Shader");
   }

   public void setModuleFavorite(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.moduleFavorites.size() >= 1000 && !this.moduleFavorites.contains(var1)) {
               return;
            }

            this.moduleFavorites.add(var1);
         } else {
            this.moduleFavorites.remove(var1);
            if (var1.equals("CustomSky")) {
               this.moduleFavorites.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean isFavorite(String var1) {
      return this.favorites.contains(var1);
   }

   public void setFavorite(String var1, boolean var2) {
      ProfileValidator.validateName(var1);
      if (var2) {
         this.favorites.add(var1);
      } else {
         this.favorites.remove(var1);
      }
   }

   public long lastUsed(String var1) {
      return this.recent.getOrDefault(var1, 0L);
   }

   public void clickGuiStyle(String var1, long var2) {
      ProfileValidator.validateName(var1);
      if (var2 > 0L) {
         this.recent.put(var1, var2);
      }
   }

   public void forgetModule(String var1) {
      this.favorites.remove(var1);
      this.recent.remove(var1);
   }

   public void renameModule(String var1, String var2) {
      ProfileValidator.validateName(var2);
      this.israelMode.renameProfile(var1, var2);
      this.larpPreset.renameProfile(var1, var2);
      if (this.favorites.remove(var1)) {
         this.favorites.add(var2);
      }

      Long var3 = this.recent.remove(var1);
      if (var3 != null) {
         this.recent.put(var2, var3);
      }
   }

   public List<String> filterModules(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::isFavorite);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.lastUsed(var1x) > 0L)
            .sorted(Comparator.comparingLong(this::lastUsed).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject toJson() {
      JsonObject var2 = new JsonObject();
      String[] var7 = kEYS;
      var2.addProperty(var7[35], this.background.id);
      var2.addProperty(var7[11], this.motion);
      var2.addProperty(var7[12], this.updateAutoCheck);
      var2.addProperty(var7[28], this.updateSkippedRelease);
      var2.addProperty(var7[0], this.updateSeenReceipt);
      var2.addProperty(var7[6], this.menuSeason.name());
      var2.addProperty(var7[33], this.menuQuality.name());
      var2.addProperty(var7[37], this.menuTime);
      var2.addProperty(var7[9], this.cinematicMenu);
      var2.addProperty(var7[31], this.customMenu);
      var2.addProperty(var7[24], this.discordPresence);
      boolean var10000 = toggleOnSave;
      var2.addProperty(var7[40], this.legitMode);
      boolean var1 = var10000;
      var2.addProperty(var7[16], this.legitModeKey);
      var2.addProperty(var7[14], this.softParticles);
      var2.addProperty(var7[7], this.effectsQuality.name());
      var2.addProperty(var7[29], this.skySource);
      var2.addProperty(var7[38], this.clickGuiLight);
      var2.addProperty(var7[43], this.clickGuiGradient);
      var2.addProperty(var7[17], this.clickGuiAnimation);
      var2.addProperty(var7[10], this.clickGuiCloseAnimation);
      var2.addProperty(var7[21], this.clickGuiDuration);
      var2.addProperty(var7[2], this.clickGuiOpacity);
      var2.addProperty(var7[44], this.clickGuiScale);
      var2.addProperty(var7[32], this.clickGuiBackdrop);
      var2.addProperty(var7[20], 2);
      var2.addProperty(var7[13], this.clickGuiDim);
      var2.addProperty(var7[23], this.clickGuiBlur);
      var2.addProperty(var7[18], this.clickGuiBlurArea);
      var2.addProperty(var7[19], this.clickGuiCardStyle);
      var2.addProperty(var7[42], this.clickGuiEnabledStyle);
      var2.addProperty(var7[36], this.clickGuiSwitchStyle);
      var2.addProperty(var7[1], this.clickGuiSliderStyle);
      var2.addProperty(var7[3], this.clickGuiRounding);
      var2.addProperty(var7[34], this.clickGuiAccentGlow);
      var2.addProperty(var7[26], this.clickGuiRipple);
      var2.addProperty(var7[25], this.clickGuiHoverLift);
      JsonArray var3 = new JsonArray();
      this.favorites.forEach(var3::add);
      var2.add(var7[30], var3);
      JsonObject var4 = new JsonObject();
      this.recent.forEach(var4::addProperty);
      var2.add(var7[15], var4);
      JsonArray var5 = new JsonArray();
      this.moduleFavorites.forEach(var5::add);
      var2.add(var7[27], var5);
      var2.addProperty(var7[4], this.moduleFavoritesOpen);
      JsonArray var6 = new JsonArray();
      this.hudLocks.forEach(var6::add);
      var2.add(var7[8], var6);
      var2.add(var7[5], this.israelMode.toJson());
      var2.add(var7[22], this.larpPreset.toJson());
      var2.add(var7[41], this.settingBinds.toJson());
      var2.add(var7[39], this.legacyClickGui.toJson());
      if (var1) {
         ConfigManager.toggle1 = (boolean)(ConfigManager.toggle1 ? 0 : 1);
      }

      return var2;
   }

   public static ClientPreferences fromJson(JsonObject var0) {
      ClientPreferences var1 = new ClientPreferences();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.updateAutoCheck = ProfileValidator.readBoolean(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var47) {
      }

      try {
         String var2 = ProfileValidator.readString(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.updateSkippedRelease = var2;
         }
      } catch (RuntimeException var54) {
      }

      try {
         String var55 = ProfileValidator.readString(var0, "updateSeenReceipt", 257);
         if (var55.isEmpty() || var55.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.updateSeenReceipt = var55;
         }
      } catch (RuntimeException var53) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.discordPresence = ProfileValidator.readBoolean(var0.get("discordPresence"));
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("legitMode")) {
            var1.legitMode = ProfileValidator.readBoolean(var0.get("legitMode"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         if (var0.has("legitModeKey")) {
            ProfileValidator.checkNumber(var0.get("legitModeKey"), 32.0, 348.0, true);
            var1.legitModeKey = var0.get("legitModeKey").getAsInt();
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var56 = ProfileValidator.readString(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var56)) {
            var1.skySource = var56;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.menuSeason = DisplayFormat.Season.valueOf(ProfileValidator.readString(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.menuQuality = DisplayFormat.MenuQuality.valueOf(ProfileValidator.readString(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         ProfileValidator.checkNumber(var0.get("menuTime"), 0.0, 1.0, false);
         var1.menuTime = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.cinematicMenu = ProfileValidator.readBoolean(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.customMenu = ProfileValidator.readBoolean(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.clickGuiGradient = ProfileValidator.readBoolean(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.clickGuiLight = ProfileValidator.readBoolean(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var57 = ProfileValidator.readString(var0, "clickGuiAnimation", 16);
         if (ToastAnimation.sTYLES.contains(var57)) {
            var1.clickGuiAnimation = var57;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var58 = ProfileValidator.readString(var0, "clickGuiCloseAnimation", 16);
         if (cLOSE_ANIMATIONS.contains(var58)) {
            var1.clickGuiCloseAnimation = var58;
         }
      } catch (RuntimeException var34) {
      }

      try {
         ProfileValidator.checkNumber(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.clickGuiDuration = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         ProfileValidator.checkNumber(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.clickGuiOpacity = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            ProfileValidator.checkNumber(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.clickGuiScale = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var59 = ProfileValidator.readString(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var59.equals("Aurora") && var3 == 0) {
            LarpLog.debug("GUI", "Moved the former default Aurora backdrop to None");
         } else if (bACKDROPS.contains(var59)) {
            var1.clickGuiBackdrop = var59;
         }
      } catch (RuntimeException var52) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            ProfileValidator.checkNumber(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.clickGuiDim = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            ProfileValidator.checkNumber(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.clickGuiBlur = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var60 = ProfileValidator.readString(var0, "clickGuiBlurArea", 16);
         if (bLUR_AREAS.contains(var60)) {
            var1.clickGuiBlurArea = var60;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var61 = ProfileValidator.readString(var0, "clickGuiCardStyle", 16);
         if (cARD_STYLES.contains(var61)) {
            var1.clickGuiCardStyle = var61;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var62 = ProfileValidator.readString(var0, "clickGuiEnabledStyle", 16);
         if (eNABLED_STYLES.contains(var62)) {
            var1.clickGuiEnabledStyle = var62;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var63 = ProfileValidator.readString(var0, "clickGuiSwitchStyle", 16);
         if (sWITCH_STYLES.contains(var63)) {
            var1.clickGuiSwitchStyle = var63;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var64 = ProfileValidator.readString(var0, "clickGuiSliderStyle", 16);
         if (sLIDER_STYLES.contains(var64)) {
            var1.clickGuiSliderStyle = var64;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            ProfileValidator.checkNumber(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.clickGuiRounding = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.clickGuiAccentGlow = ProfileValidator.readBoolean(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.clickGuiRipple = ProfileValidator.readBoolean(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.clickGuiHoverLift = ProfileValidator.readBoolean(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.background = SkyboxTheme.fromId(ProfileValidator.readString(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.softParticles = ProfileValidator.readBoolean(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.effectsQuality = PostProcessing.Quality.valueOf(ProfileValidator.readString(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.motion = ProfileValidator.readBoolean(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("favorites");
         if (var65.size() <= 1000) {
            for (JsonElement var4 : var65) {
               try {
                  var1.setFavorite(ProfileValidator.validateName(var4.getAsString()), true);
               } catch (RuntimeException var14) {
               }
            }
         }
      } catch (RuntimeException var51) {
      }

      try {
         JsonObject var66 = var0.getAsJsonObject("recent");
         if (var66.size() <= 1000) {
            for (Entry var74 : var66.entrySet()) {
               try {
                  ProfileValidator.checkNumber((JsonElement)var74.getValue(), 1.0, 9.223372E18F, true);
                  var1.clickGuiStyle((String)var74.getKey(), ((JsonElement)var74.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var50) {
      }

      try {
         var1.moduleFavoritesOpen = ProfileValidator.readBoolean(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var67 = var0.getAsJsonArray("hudLocks");
         if (var67.size() <= 128) {
            for (JsonElement var75 : var67) {
               try {
                  if (var75.isJsonPrimitive() && var75.getAsJsonPrimitive().isString()) {
                     var1.setHudLocked(var75.getAsString(), true);
                  }
               } catch (RuntimeException var11) {
               }
            }
         }
      } catch (RuntimeException var49) {
      }

      try {
         JsonArray var68 = var0.getAsJsonArray("moduleFavorites");
         if (var68.size() <= 1000) {
            for (JsonElement var76 : var68) {
               try {
                  if (var76.isJsonPrimitive() && var76.getAsJsonPrimitive().isString()) {
                     var1.setModuleFavorite(var76.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.settingBinds = SettingShortcuts.fromJson(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         LarpLog.warn("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.legacyClickGui = PanelLayoutConfig.fromJson(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         LarpLog.warn("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.israelMode = CosmeticPresetJournal.fromJson(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         LarpLog.warn("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var69 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var69)) {
            var1.larpPreset = CosmeticPresetJournal2.fromJson(var0.getAsJsonObject(var69));
            if (!var0.has("larpPreset")) {
               LarpLog.debug("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         LarpLog.warn("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[45];
      int var3 = 0;
      String var2 = "GVY\u001d>\u00061WCS./\u0000\u0007[VI\u0013QJT\u001f!$\u0017[uQ\u0015.\u0006\u0010aRD\u0010/\u000fQJT\u001f!$\u0017[iM\u001d)\n\u0016K\u0010QJT\u001f!$\u0017[tR\t$\u0007\u000b\\A\u0013_IY\t&\u0006$SPR\u000e#\u0017\u0007AiM\u0019$\n[UO\u001d/\u000f/]BX\n_CS\t\u0019\u0006\u0003AIS\u000eW@[\u0019)\u0017\u0011cS\\\u0010#\u0017\u001b\bZSY0%\u0000\tA\rQOS\u0019'\u0002\u0016[Ep\u0019$\u0016\u0016QJT\u001f!$\u0017[eQ\u00139\u0006#\\OP\u001d>\n\r\\\u0006_II\u0015%\r\u000fGVY\u001d>\u0006#GRR?\"\u0006\u0001Y\u000bQJT\u001f!$\u0017[bT\u0011\rAI[\b\u001a\u0002\u0010FO^\u0010/\u0010\u0006@C^\u0019$\u0017\f^CZ\u0015>.\rVCv\u00193\u0011QJT\u001f!$\u0017[gS\u0015'\u0002\u0016[IS\u0010QJT\u001f!$\u0017[dQ\t8\"\u0010WG\u0011QJT\u001f!$\u0017[e\\\u000e.0\u0016KJX\u0018QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]Vo\u0019<\n\u0011[IS\u0010QJT\u001f!$\u0017[bH\u000e+\u0017\u000b]H\n^GO\f\u001a\u0011\u0007ACI\fQJT\u001f!$\u0017[dQ\t8\u000fVON\u001f%\u0011\u0006bTX\u000f/\r\u0001W\u0011QJT\u001f!$\u0017[nR\n/\u0011.[@I\u000eQJT\u001f!$\u0017[tT\f:\u000f\u0007\u000f_IY\t&\u0006$SPR\u000e#\u0017\u0007A\u0014GVY\u001d>\u00061YOM\f/\u00070WJX\u001d9\u0006\tAMD/%\u0016\u0010QC\tTGK\u00138\n\u0016WU\nQSN\b%\u000e/WHH\u0010QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]V\u000b_CS\t\u001b\u0016\u0003^OI\u0005\u0012QJT\u001f!$\u0017[g^\u001f/\r\u0016uJR\u000b\nPG^\u0017-\u0011\rGHY\u0013QJT\u001f!$\u0017[uJ\u0015>\u0000\naRD\u0010/\b_CS\t\u001e\n\u000fW\rQJT\u001f!$\u0017[jT\u001b\"\u0017\u000e^CZ\u001d)\u001a!^O^\u0017\r\u0016\u000b\t^CZ\u0015>.\rVC\fACI\b#\r\u0005pOS\u00189\u0014QJT\u001f!$\u0017[cS\u001d(\u000f\u0007VuI\u0005&\u0006";
      int var4 = "GVY\u001d>\u00061WCS./\u0000\u0007[VI\u0013QJT\u001f!$\u0017[uQ\u0015.\u0006\u0010aRD\u0010/\u000fQJT\u001f!$\u0017[iM\u001d)\n\u0016K\u0010QJT\u001f!$\u0017[tR\t$\u0007\u000b\\A\u0013_IY\t&\u0006$SPR\u000e#\u0017\u0007AiM\u0019$\n[UO\u001d/\u000f/]BX\n_CS\t\u0019\u0006\u0003AIS\u000eW@[\u0019)\u0017\u0011cS\\\u0010#\u0017\u001b\bZSY0%\u0000\tA\rQOS\u0019'\u0002\u0016[Ep\u0019$\u0016\u0016QJT\u001f!$\u0017[eQ\u00139\u0006#\\OP\u001d>\n\r\\\u0006_II\u0015%\r\u000fGVY\u001d>\u0006#GRR?\"\u0006\u0001Y\u000bQJT\u001f!$\u0017[bT\u0011\rAI[\b\u001a\u0002\u0010FO^\u0010/\u0010\u0006@C^\u0019$\u0017\f^CZ\u0015>.\rVCv\u00193\u0011QJT\u001f!$\u0017[gS\u0015'\u0002\u0016[IS\u0010QJT\u001f!$\u0017[dQ\t8\"\u0010WG\u0011QJT\u001f!$\u0017[e\\\u000e.0\u0016KJX\u0018QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]Vo\u0019<\n\u0011[IS\u0010QJT\u001f!$\u0017[bH\u000e+\u0017\u000b]H\n^GO\f\u001a\u0011\u0007ACI\fQJT\u001f!$\u0017[dQ\t8\u000fVON\u001f%\u0011\u0006bTX\u000f/\r\u0001W\u0011QJT\u001f!$\u0017[nR\n/\u0011.[@I\u000eQJT\u001f!$\u0017[tT\f:\u000f\u0007\u000f_IY\t&\u0006$SPR\u000e#\u0017\u0007A\u0014GVY\u001d>\u00061YOM\f/\u00070WJX\u001d9\u0006\tAMD/%\u0016\u0010QC\tTGK\u00138\n\u0016WU\nQSN\b%\u000e/WHH\u0010QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]V\u000b_CS\t\u001b\u0016\u0003^OI\u0005\u0012QJT\u001f!$\u0017[g^\u001f/\r\u0016uJR\u000b\nPG^\u0017-\u0011\rGHY\u0013QJT\u001f!$\u0017[uJ\u0015>\u0000\naRD\u0010/\b_CS\t\u001e\n\u000fW\rQJT\u001f!$\u0017[jT\u001b\"\u0017\u000e^CZ\u001d)\u001a!^O^\u0017\r\u0016\u000b\t^CZ\u0015>.\rVC\fACI\b#\r\u0005pOS\u00189\u0014QJT\u001f!$\u0017[cS\u001d(\u000f\u0007VuI\u0005&\u0006"
         .length();
      char var1 = 17;
      int var7 = -1;

      label24:
      while (true) {
         String var10000 = var2.substring(++var7, var7 + var1);
         byte var10001 = -1;

         while (true) {
            String var12 = decrypt(decrypt(var10000));
            switch (var10001) {
               case 0:
                  var5[var3++] = var12;
                  if ((var7 += var1) >= var4) {
                     kEYS = var5;
                     cLOSE_ANIMATIONS = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     bACKDROPS = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     bLUR_AREAS = List.of("Window", "Everywhere");
                     cARD_STYLES = List.of("Filled", "Outline", "Flat");
                     eNABLED_STYLES = List.of("Glow", "Tint", "Text", "None");
                     sWITCH_STYLES = List.of("Pill", "Square", "Minimal");
                     sLIDER_STYLES = List.of("Pill", "Minimal", "Classic");
                     return;
                  }

                  var1 = var2.charAt(var7);
                  break;
               default:
                  var5[var3++] = var12;
                  if ((var7 += var1) < var4) {
                     var1 = var2.charAt(var7);
                     continue label24;
                  }

                  var2 = "QJT\u001f!$\u0017[aO\u001d.\n\u0007\\R\rQJT\u001f!$\u0017[u^\u001d&\u0006";
                  var4 = "QJT\u001f!$\u0017[aO\u001d.\n\u0007\\R\rQJT\u001f!$\u0017[u^\u001d&\u0006".length();
                  var1 = 16;
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
