package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import java.util.ArrayList;
import java.util.List;

public class HUDModule extends Module {
   private final BooleanSetting watermark = this.addSetting(new BooleanSetting("Watermark", "Show LARP watermark", true));
   private final BooleanSetting moduleList = this.addSetting(new BooleanSetting("Module List", "Show active modules", true));
   private final BooleanSetting infoPanel = this.addSetting(new BooleanSetting("Info Panel", "Show coords/fps/ping etc", true));
   private final BooleanSetting keybinds = this.addSetting(new BooleanSetting("Keybinds", "Show active module keybinds", true));
   private final BooleanSetting potions = this.addSetting(new BooleanSetting("Potions", "Show active potion effects", false));
   private final BooleanSetting armor = this.addSetting(new BooleanSetting("Armor", "Show armor display", true));
   public final BooleanSetting speedGraph = this.addSetting(new BooleanSetting("Speed", "Show the speed graph", false));
   public final BooleanSetting inventory = this.addSetting(new BooleanSetting("Inventory", "Show the Larp inventory widget", false));
   public final BooleanSetting cooldowns = this.addSetting(new BooleanSetting("Cooldowns", "Show item cooldowns", false));
   public final BooleanSetting radar = this.addSetting(new BooleanSetting("Radar", "Retired radar-widget preference", false));
   public final BooleanSetting keystrokes = this.addSetting(new BooleanSetting("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final BooleanSetting musicBar = this.addSetting(new BooleanSetting("Music Bar", "Retired music-widget preference", false));
   public final BooleanSetting watermarkUser = this.addSetting(new BooleanSetting("Watermark User", "Show player name", true));
   public final BooleanSetting watermarkIp = this.addSetting(new BooleanSetting("Watermark IP", "Show server address", true));
   public final BooleanSetting watermarkFps = this.addSetting(new BooleanSetting("Watermark FPS", "Show frame rate", true));
   public final BooleanSetting watermarkTime = this.addSetting(new BooleanSetting("Watermark Time", "Show local time", true));
   public final ModeSetting watermarkLogoSide = this.addSetting(new ModeSetting("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final ModeSetting keybindDisplay = this.addSetting(
      new ModeSetting("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only")
   );
   public final ModeSetting cooldownItems = this.addSetting(new ModeSetting("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final BooleanSetting inventoryHeader = this.addSetting(new BooleanSetting("Inventory Header", "Show inventory title", true));
   public final NumberSetting radarRange = this.addSetting(new NumberSetting("Radar Range", "Retired radar-widget preference", 47.0, 20.0, 80.0, 5.0));
   public final BooleanSetting radarMobs = this.addSetting(new BooleanSetting("Radar Mobs", "Retired radar-widget preference", false));
   public final BooleanSetting keystrokesMouse = this.addSetting(new BooleanSetting("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final BooleanSetting keystrokesSpace = this.addSetting(new BooleanSetting("Keystrokes Space", "Show the jump key", true));
   public final BooleanSetting keystrokesCps = this.addSetting(new BooleanSetting("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final BooleanSetting keystrokesArrows = this.addSetting(
      new BooleanSetting("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false)
   );
   public final NumberSetting keystrokesPadding = this.addSetting(
      new NumberSetting("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0)
   );
   public final NumberSetting keystrokesBgAlpha = this.addSetting(
      new NumberSetting("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0)
   );
   public final BooleanSetting keystrokesShadow = this.addSetting(new BooleanSetting("Keystrokes Shadow", "Soft shadow under the card", true));
   public final ModeSetting musicLayout = this.addSetting(new ModeSetting("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final BooleanSetting musicProgress = this.addSetting(new BooleanSetting("Music Progress", "Show track progress", true));
   public final BooleanSetting musicTime = this.addSetting(new BooleanSetting("Music Time", "Show track position and duration", true));
   public final ModeSetting armorLayout = this.addSetting(new ModeSetting("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final NumberSetting armorSpacing = this.addSetting(new NumberSetting("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final ModeSetting armorDurability = this.addSetting(
      new ModeSetting("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining")
   );
   public final BooleanSetting armorBars = this.addSetting(new BooleanSetting("Armor Bars", "Color-coded durability bars", true));
   public final BooleanSetting armorBackground = this.addSetting(new BooleanSetting("Armor Background", "Draw equipment panel", false));
   public final BooleanSetting armorEmptySlots = this.addSetting(
      new BooleanSetting("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true)
   );
   public final BooleanSetting armorReverse = this.addSetting(new BooleanSetting("Armor Reverse", "Reverse equipment order", false));
   public final BooleanSetting armorHeldItems = this.addSetting(new BooleanSetting("Armor Held Items", "Include main hand and offhand", false));
   public final ColorSetting speedHeader = this.addSetting(new ColorSetting("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final ColorSetting speedBackground = this.addSetting(new ColorSetting("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final ColorSetting speedLine = this.addSetting(new ColorSetting("Speed Line", "Speed graph curve color", -1841171));
   public final BooleanSetting speedCardBackground = this.addSetting(
      new BooleanSetting("Speed Card Background", "Use the same dark background as the other HUD cards", true)
   );
   private final BooleanSetting blurPanels = this.addSetting(new BooleanSetting("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final BooleanSetting lightSweep = this.addSetting(new BooleanSetting("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final BooleanSetting panelGlow = this.addSetting(new BooleanSetting("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final ModeSetting moduleListStyle = this.addSetting(
      new ModeSetting("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule instance;

   public List<Setting<?>> getSpeedSettings() {
      return List.of(this.speedCardBackground, this.speedHeader, this.speedBackground, this.speedLine);
   }

   public List<Setting<?>> getKeystrokesSettings() {
      return List.of(this.keystrokesMouse, this.keystrokesSpace, this.keystrokesCps, this.keystrokesArrows, this.keystrokesPadding, this.keystrokesBgAlpha);
   }

   public List<Setting<?>> getWidgetToggles() {
      return List.of(
         this.watermark,
         this.moduleList,
         this.infoPanel,
         this.keybinds,
         this.potions,
         this.armor,
         this.speedGraph,
         this.inventory,
         this.cooldowns,
         this.keystrokes
      );
   }

   public List<Setting<?>> getMusicSettings() {
      ArrayList var1 = new ArrayList<>(List.of(this.musicBar, this.musicLayout, this.musicProgress, this.musicTime));
      var1.addAll(List.of(this.radar, this.radarRange, this.radarMobs));
      var1.add(this.keystrokesShadow);
      return List.copyOf(var1);
   }

   public List<Setting<?>> getEffectSettings() {
      return List.of(this.blurPanels, this.lightSweep, this.panelGlow);
   }

   public List<Setting<?>> getSpeedSettings(String var1) {
      return switch (var1) {
         case "speed" -> this.getSpeedSettings();
         case "keybinds" -> List.of(this.keybindDisplay, this.blurPanels);
         case "inventory" -> List.of(this.inventoryHeader, this.blurPanels);
         case "cooldowns" -> List.of(this.cooldownItems, this.blurPanels);
         case "staffs" -> List.of(this.blurPanels);
         case "keystrokes" -> {
            ArrayList var4 = new ArrayList<>(this.getKeystrokesSettings());
            var4.add(this.blurPanels);
            yield List.copyOf(var4);
         }
         case "armor" -> List.of(
            this.armorLayout,
            this.armorSpacing,
            this.armorDurability,
            this.armorBars,
            this.armorBackground,
            this.armorEmptySlots,
            this.armorReverse,
            this.armorHeldItems,
            this.blurPanels
         );
         case "watermark" -> List.of(this.watermarkUser, this.watermarkIp, this.watermarkFps, this.watermarkTime, this.watermarkLogoSide, this.blurPanels);
         case "modules" -> List.of(this.moduleListStyle, this.blurPanels);
         case "info", "effects", "target", "miner", "builder", "pilot", "hunter" -> List.of(this.blurPanels);
         default -> List.of();
      };
   }

   public boolean isSpeedGraphVisible() {
      return !this.isEnabled() ? false : this.isOperational() && this.speedGraph.getValue();
   }

   public boolean getEffectSettings(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (this.inventory.getValue()) {
                  return true;
               }
               break;
            case "cooldowns":
               if (this.cooldowns.getValue()) {
                  return true;
               }
               break;
            case "keystrokes":
               if (this.keystrokes.getValue()) {
                  return true;
               }
         }
      }

      return false;
   }

   public boolean isBlurEnabled() {
      return this.isOperational() && this.blurPanels.getValue();
   }

   public boolean getWidgetToggles() {
      return this.lightSweep.getValue();
   }

   public boolean getMusicSettings() {
      return this.panelGlow.getValue();
   }

   public boolean isCardModuleList() {
      return this.moduleListStyle.isMode("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      instance = this;

      for (Setting var2 : List.of(this.watermarkUser, this.watermarkIp, this.watermarkFps, this.watermarkTime, this.watermarkLogoSide)) {
         var2.visibleWhen(this.watermark::getValue);
      }

      this.keybindDisplay.visibleWhen(this.keybinds::getValue);
      this.moduleListStyle.visibleWhen(this.moduleList::getValue);
      this.cooldownItems.visibleWhen(this.cooldowns::getValue);
      this.inventoryHeader.visibleWhen(this.inventory::getValue);

      for (Setting var9 : this.getKeystrokesSettings()) {
         var9.visibleWhen(this.keystrokes::getValue);
      }

      this.keystrokesCps.visibleWhen(() -> this.keystrokes.getValue() && this.keystrokesMouse.getValue());

      for (Setting var10 : this.getMusicSettings()) {
         var10.visibleWhen(() -> false);
      }

      this.speedCardBackground.visibleWhen(this.speedGraph::getValue);
      this.speedLine.visibleWhen(this.speedGraph::getValue);

      for (ColorSetting var11 : List.of(this.speedHeader, this.speedBackground)) {
         var11.visibleWhen(() -> this.speedGraph.getValue() && !this.speedCardBackground.getValue());
      }

      for (Setting var4 : new Setting[]{
         this.armorLayout,
         this.armorSpacing,
         this.armorDurability,
         this.armorBars,
         this.armorBackground,
         this.armorEmptySlots,
         this.armorReverse,
         this.armorHeldItems
      }) {
         var4.visibleWhen(this.armor::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule getInstance() {
      return instance;
   }

   public boolean isWatermarkVisible() {
      return !this.isEnabled() ? false : this.isOperational() && this.watermark.getValue();
   }

   public boolean isModuleListVisible() {
      return !this.isEnabled() ? false : this.isOperational() && this.moduleList.getValue();
   }

   public boolean getInstance() {
      return !this.isEnabled() ? false : this.isOperational() && this.infoPanel.getValue();
   }

   public boolean isKeybindsVisible() {
      return !this.isEnabled() ? false : this.isOperational() && this.keybinds.getValue();
   }

   public boolean getSpeedSettings() {
      return !this.isEnabled() ? false : this.isOperational() && this.potions.getValue();
   }

   public boolean isArmorVisible() {
      return !this.isEnabled() ? false : this.isOperational() && this.armor.getValue();
   }
}
