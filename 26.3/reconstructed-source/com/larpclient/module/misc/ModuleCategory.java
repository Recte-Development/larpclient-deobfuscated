package com.larpclient.module.misc;

import com.larpclient.config.ClientPreferences;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.clickgui.ModuleSearch;
import com.larpclient.module.exploit.FakePayModule;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.module.render.StorageESPModule;
import com.larpclient.module.world.ChunkFindingModule;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum ModuleCategory {
   fAVORITES("Favorites", "star"),
   cOMBAT("Combat", "swords"),
   mOVEMENT("Movement", "footprints"),
   pLAYER("Player", "user"),
   vISUALS("Visuals", "eye"),
   cOSMETICS("Cosmetics", "paintbrush"),
   bASE_FINDING("Base Finding", "search"),
   mISC("Misc", "list"),
   dONUT_SMP("DonutSmp", "store");

   public final String label;
   public final String icon;
   private static final Set<String> cOMBAT_MODULES = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> pLAYER_MODULES = Set.of("IgnoreEntity");
   private static final Set<String> vISUAL_MODULES = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> bASE_FINDING_MODULES = Set.of("Light Debug", "Motion Scanner", "Holes ESP");
   private static final Set<String> cOSMETIC_MODULES = Set.of(
      "Animations",
      "ViewModel",
      "Sword Models",
      "Player Trail",
      "HitFX",
      "Hit Sounds",
      "Click Pulse",
      "JumpFX",
      "TargetAura",
      "SelfVisuals",
      "CustomSky",
      "Sky Shader",
      "China Hat",
      "Wings",
      "Headless Korblox",
      "Hide Armour",
      "Ambient Particles",
      "Item Physic",
      "EchoHand",
      "Hand Chams",
      "Flame Hands",
      "Player Model",
      "Skin Changer",
      "Capes",
      "Crystal Chams",
      "Walk Style",
      "Emotes",
      "Pets",
      "Crypto Pets",
      "Astral Weapon Trails",
      "Celestial Weather",
      "Fog",
      "WorldLyrics",
      "KillFX",
      "GUI Animation",
      "3D UI",
      "World Menu",
      "Line Glyphs",
      "Pearl Particles",
      "No Block Rotation",
      "TimeChanger",
      "WorldColor",
      "Motion Blur",
      "Realistic Water",
      "Waving Foliage",
      "Wet Ground",
      "Snow Ground"
   );

   ModuleCategory(String var3, String var4) {
      this.label = var3;
      this.icon = var4;
   }

   public static List<ModuleCategory> visibleCategories() {
      return StreamProofState.isHidden() ? List.of(fAVORITES, vISUALS, cOSMETICS, mISC) : List.of(values());
   }

   public boolean switchTo(ModuleCategory var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static ModuleCategory categorize(Module var0) {
      if (var0.getName().equals("LARP")
         || var0.getName().equals("Name Changer")
         || var0.getName().equals("Fake FPS")
         || var0 instanceof PerformanceModule
         || var0 instanceof StreamProofModule) {
         return mISC;
      }

      if (cOMBAT_MODULES.contains(var0.getName())) {
         return cOMBAT;
      }

      if (pLAYER_MODULES.contains(var0.getName())) {
         return pLAYER;
      }

      if (vISUAL_MODULES.contains(var0.getName())) {
         return vISUALS;
      }

      if (cOSMETIC_MODULES.contains(var0.getName())) {
         return cOSMETICS;
      }

      if (bASE_FINDING_MODULES.contains(var0.getName()) || var0 instanceof ChunkFindingModule || var0 instanceof StorageESPModule) {
         return bASE_FINDING;
      }

      if (var0 instanceof CasinoGameModule || var0 instanceof FakeSpawnerModule || var0 instanceof FakePayModule || var0 instanceof OrderDropperModule) {
         return dONUT_SMP;
      }

      if (var0 instanceof ModulePanel || var0 instanceof FakeScoreboardModule) {
         return dONUT_SMP;
      }

      if (var0 instanceof FakePlayerModule) {
         return mISC;
      }

      return switch (var0.getCategory()) {
         case COMBAT -> cOMBAT;
         case MOVEMENT -> mOVEMENT;
         case PLAYER -> pLAYER;
         case RENDER, VISUALS -> vISUALS;
      };
   }

   public static String groupName(Module var0) {
      if (var0 instanceof ModulePanel) {
         return "DonutSmp Bots";
      } else {
         return var0 instanceof CasinoGameModule ? "DonutSmp Gambling Rigs" : "";
      }
   }

   public boolean showsGroupHeaders(String var1) {
      return var1.isBlank() && this != fAVORITES;
   }

   public boolean categorize(Module var1, String var2, ClientPreferences var3) {
      if (!var2.isBlank()) {
         return ModuleSearch.rankModule(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == fAVORITES ? var3.clickGuiStyle(var1.getName()) : categorize(var1) == this;
      }
   }

   public List<Module> filterModules(List<Module> var1, String var2, ClientPreferences var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.mergeSkyModules(var1, var3)
         .stream()
         .filter(var3x -> this.categorize(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.showsGroupHeaders(var2) ? groupName(var2x) : "")
               .thenComparing(var1x -> !var3.clickGuiStyle(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static ModuleCategory fromName(String var0) {
      if (var0 == null) {
         return vISUALS;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return vISUALS;
         }
      } else {
         return vISUALS;
      }
   }
}
