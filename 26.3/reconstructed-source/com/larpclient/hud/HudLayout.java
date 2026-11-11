package com.larpclient.hud;

import com.larpclient.LarpClient;
import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.combat.AnchorHunter;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.CasinoGameModule;
import com.larpclient.module.misc.FakeFPSModule;
import com.larpclient.module.misc.IslandLyricsModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleManager;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.StaffListModule;
import com.larpclient.module.misc.StreamProofState;
import com.larpclient.module.movement.AutoElytraPilot;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.module.render.TargetHUDModule;
import com.larpclient.module.world.AutoAreaMiner;
import com.larpclient.module.world.AutoBuilderModule;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.particle.SparkParticles;
import com.larpclient.social.cosmetic.CosmeticOverrides;
import com.larpclient.util.data.ModuleListCache;
import com.larpclient.util.data.SpeedHistory;
import com.larpclient.util.entity.EntityHealth;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class HudLayout {
   private static final Map<HudLayout.HudElement, List<HudLayout2.Rect>> elementBounds = new LinkedHashMap<>();
   private static Object lastGraphics;
   private static Object lastLevel;
   private static int guiWidth;
   private static int guiHeight;
   private static long layoutVersion;
   private static List<HudLayout.ElementBounds> lastLayout = List.of();
   private static long lastLayoutVersion;
   private static boolean wasChatOpen;
   public static final int hIT_PADDING = 8;
   public static final int aNIMATION_MS = 220;
   private static final Animation infoReveal = new Animation(0.0);
   private static long infoRevealVersion = Long.MIN_VALUE;
   private static HudLayout.HudElement selectedElement;
   private static boolean watermarkShown;
   private static final int tARGET_WIDTH = 224;
   private static final int tARGET_HEIGHT = 52;
   private static final int fACE_SIZE = 36;
   private static final int rOW_GAP = 12;
   private static final int eQUIPMENT_WIDTH = 79;
   private static final List<HudLayout.HudCard> cards = List.of(
      new HudLayout.HudCard(
         "miner", "Auto Area Miner", AutoAreaMiner::findDrillSlot, new ModulePanel.HudCard("Layer 2 of 3", "64%", "Mining toward 120 64 -45", "4m 12s", 0.64)
      ),
      new HudLayout.HudCard(
         "builder",
         "Auto Builder",
         AutoBuilderModule::getInstance,
         new ModulePanel.HudCard("house", "64%", "Walking to 120 64 -45", "412 placed, 230 missing", 0.64)
      ),
      new HudLayout.HudCard(
         "pilot",
         "Auto Elytra Pilot",
         AutoElytraPilot::getInstance,
         new ModulePanel.HudCard("12,340 blocks left, about 3m 10s", "64%", "Flying to 1000, -2500", "32 rockets, elytra 412/432", 0.64)
      ),
      new HudLayout.HudCard(
         "hunter", "Anchor Hunter", AnchorHunter::getInstance, new ModulePanel.HudCard("4 anchors, 24/h", "4 of 10", "Going to an anchor", "/rtp in 42s", 0.4)
      )
   );
   public static final List<HudLayout.HudElement> elements = appendCards(
      List.of(
         new HudLayout.HudElement("watermark", 170, 22, 0.015, 0.02),
         new HudLayout.HudElement("info", 178, 27, 0.015, 0.85),
         new HudLayout.HudElement("modules", 130, 100, 0.98, 0.06),
         new HudLayout.HudElement("target", 224, 52, 0.55, 0.62),
         new HudLayout.HudElement("effects", 150, 58, 0.015, 0.7),
         new HudLayout.HudElement("keybinds", 120, 58, 0.015, 0.35),
         new HudLayout.HudElement("armor", 84, 22, 0.5, 0.9),
         new HudLayout.HudElement("notifications", 260, 178, 0.98, 0.08),
         new HudLayout.HudElement("crosshair", 84, 84, 0.5, 0.5),
         new HudLayout.HudElement("speed", 172, 72, 0.015, 0.55),
         new HudLayout.HudElement("cooldowns", 168, 48, 0.78, 0.08),
         new HudLayout.HudElement("staffs", 168, 48, 0.35, 0.65),
         new HudLayout.HudElement("inventory", 207, 94, 0.04, 0.48),
         new HudLayout.HudElement("keystrokes", 81, 98, 0.985, 0.93),
         new HudLayout.HudElement("island", 300, 20, 0.5, 0.015),
         new HudLayout.HudElement("casino", 168, 140, 0.98, 0.5)
      )
   );
   private static final Animation targetHealthAnim = new Animation(1.0);
   private static final Animation targetVisibility = new Animation(0.0);
   private static final Animation targetDamageAnim = new Animation(1.0);
   private static double lastHealthFraction = 1.0;
   private static long damageHoldUntil;
   private static final Identifier heartContainerSprite = Identifier.withDefaultNamespace("hud/heart/container");
   private static final Identifier heartFullSprite = Identifier.withDefaultNamespace("hud/heart/full");
   private static LivingEntity targetEntity;
   private static final SparkParticles hitParticles = new SparkParticles();
   private static float lastTargetHealth;
   private static long lastTargetNanos;
   private static final UiState.HudStyle dEFAULT_DOCK_STATE = new UiState.HudStyle(1.0, false);
   private static final EquipmentSlot[] eQUIPMENT_SLOTS = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final Set<String> nonBasicIds = Set.of("target", "staffs", "casino", "miner", "builder", "pilot", "hunter");
   private static final List<HudLayout.HudElement> basicElements = elements.stream().filter(var0 -> !nonBasicIds.contains(var0.id)).toList();
   private static final SpeedHistory speedGraph = new SpeedHistory();
   private static final ModuleListCache moduleListLayout = new ModuleListCache();
   private static List<ItemStack> width;

   public static boolean isChatOpen() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null
         && var0.level != null
         && var0.isWindowActive()
         && var0.gui.screen() != null
         && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void reset() {
      lastLayout = List.of();
      elementBounds.clear();
      lastGraphics = null;
      lastLevel = null;
      HudPanels.reset();
      ModuleListLayout.reset();
   }

   public static void beginFrame(GuiGraphicsExtractor var0) {
      if (lastGraphics != var0) {
         elementBounds.clear();
         lastGraphics = var0;
         Minecraft var1 = Minecraft.getInstance();
         lastLevel = var1.level;
         guiWidth = var0.guiWidth();
         guiHeight = var0.guiHeight();
         layoutVersion = UiState.revision;
      }
   }

   public static void addBounds(HudLayout.HudElement var0, HudLayout2.Rect var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         elementBounds.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static String renderHud(String var0) {
      return "info".equals(var0) ? "watermark" : null;
   }

   public static boolean isAliasActive(HudLayout.HudElement var0) {
      String var1 = renderHud(var0.id);
      return var1 != null && var1.equals(var0.getDock());
   }

   public static void setSelected(HudLayout.HudElement var0) {
      selectedElement = var0;
   }

   public static HudLayout.HudElement getSelected() {
      return selectedElement;
   }

   public static boolean isAliasOverlapping(HudLayout.HudElement var0, int var1, int var2) {
      String var3 = renderHud(var0.id);
      if (var3 != null && watermarkShown) {
         HudLayout2.Rect var4 = getElement(var3).getBounds(var1, var2);
         HudLayout2.Rect var5 = var0.setPosition(var1, var2);
         return collectVisibleBounds(var4, var5.left() + var5.width() / 2.0, var5.top() + var5.height() / 2.0);
      } else {
         return false;
      }
   }

   public static boolean collectVisibleBounds(HudLayout2.Rect var0, double var1, double var3) {
      return var1 >= var0.left() - 8 && var1 <= var0.left() + var0.width() + 8 && var3 >= var0.top() - 8 && var3 <= var0.top() + var0.height() + 8;
   }

   private static boolean isShownInEditor(HudLayout.HudElement var0, boolean var1, boolean var2, HudLayout.HudElement var3) {
      boolean var4 = isEnabled(var0);
      return var1 ? HudLayout2.isAnyTrue(var4, var2, var3 == var0) : var4;
   }

   public static List<HudLayout.ElementBounds> collectVisibleBounds() {
      Minecraft var0 = Minecraft.getInstance();
      if (lastLevel == var0.level
         && layoutVersion == UiState.revision
         && var0.player != null
         && guiWidth == var0.getWindow().getGuiScaledWidth()
         && guiHeight == var0.getWindow().getGuiScaledHeight()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         ArrayList var1 = new ArrayList();
         elementBounds.forEach((var1x, var2) -> {
            if (isEnabled(var1x)) {
               for (HudLayout2.Rect var4 : var2) {
                  var1.add(new HudLayout.ElementBounds(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<HudLayout2.Rect> findElementBounds(List<HudLayout.ElementBounds> var0, HudLayout.HudElement var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(HudLayout.ElementBounds::bounds)
         .reduce(
            (var0x, var1x) -> new HudLayout2.Rect(
               Math.min(var0x.left(), var1x.left()),
               Math.min(var0x.top(), var1x.top()),
               Math.max(var0x.left() + var0x.width(), var1x.left() + var1x.width()) - Math.min(var0x.left(), var1x.left()),
               Math.max(var0x.top() + var0x.height(), var1x.top() + var1x.height()) - Math.min(var0x.top(), var1x.top())
            )
         );
   }

   public static List<Setting<?>> getSettings(HudLayout.HudElement var0) {
      if (var0.id.equals("crosshair")) {
         ModuleManager var5 = LarpClient.getInstance().getModuleManager();
         Module var7 = var5.getByName("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.getInstance();
      if (var0.id.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.getInstance();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.id.equals("target") && !var0.id.equals("staffs")) {
         return var1 == null ? List.of() : var1.getSpeedSettings(var0.id);
      }

      ModuleManager var2 = LarpClient.getInstance().getModuleManager();
      Module var3 = var0.id.equals("target") ? var2.getModule(TargetHUDModule.class) : var2.getModule(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.getSpeedSettings(var0.id));
      }

      return List.copyOf(var4);
   }

   public static boolean isChatOpen(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String getStatus(HudLayout.HudElement var0) {
      if (var0.isHidden()) {
         return "Hidden";
      }

      if (!isEnabled(var0)) {
         return "Disabled";
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.id) {
         case "notifications" -> NotificationManager.getInstance().isAnimating();
         case "target" -> VisualsRenderer.getRecentTarget() == null && (targetEntity == null || !(targetVisibility.getValue() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.player.getActiveEffects().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.getInstance().getModuleManager().getModules().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.getInstance();
            yield var5 != null
                  && Arrays.stream(eQUIPMENT_SLOTS)
                     .filter(var1x -> var5.armorHeldItems.getValue() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                     .anyMatch(var1x -> !var1.player.getItemBySlot(var1x).isEmpty())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> HudPanels2.shouldShow(var0.id, HudPanels2.buildPanel(var0.id, false));
         case "crosshair" -> var1.options.getCameraType().isFirstPerson();
         case "island" -> IslandLyricsModule.isHudVisible();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   private static HudLayout.HudCard renderElements(String var0) {
      return cards.stream().filter(var1 -> var1.id.equals(var0)).findFirst().orElseThrow();
   }

   private static List<HudLayout.HudElement> appendCards(List<HudLayout.HudElement> var0) {
      ArrayList var1 = new ArrayList(var0);

      for (HudLayout.HudCard var3 : cards) {
         var1.add(new HudLayout.HudElement(var3.id, 196, 80, 0.5, 0.1));
      }

      return List.copyOf(var1);
   }

   public static List<HudLayout.HudElement> addBounds() {
      return StreamProofState.isHidden() ? basicElements : elements;
   }

   public static HudLayout.HudElement getElement(String var0) {
      return elements.stream().filter(var1 -> var1.id.equals(var0)).findFirst().orElseThrow();
   }

   public static String getDisplayName(String var0) {
      return switch (var0) {
         case "watermark" -> "Watermark / server / FPS";
         case "speed" -> "Speed graph";
         case "info" -> "Coordinates";
         case "modules" -> "Active modules";
         case "target" -> "TargetHUD";
         case "effects" -> "Potions";
         case "keybinds" -> "Keybinds";
         case "armor" -> "Armor";
         case "notifications" -> "Notifications";
         case "crosshair" -> "Custom crosshair";
         case "cooldowns" -> "Cooldowns";
         case "staffs" -> "Staff List";
         case "inventory" -> "Inventory";
         case "keystrokes" -> "Keystrokes";
         case "miner" -> "Auto Area Miner";
         case "builder" -> "Auto Builder";
         case "island" -> "Island Lyrics";
         case "pilot" -> "Auto Elytra Pilot";
         case "hunter" -> "Anchor Hunter";
         case "casino" -> "Casino games";
         default -> var0;
      };
   }

   public static boolean isEnabled(HudLayout.HudElement var0) {
      if (var0.isHidden()) {
         return false;
      }

      HUDModule var1 = HUDModule.getInstance();

      return (boolean)(switch (var0.id) {
         case "crosshair" -> CustomCrosshairModule.isActive();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.getInstance().getModuleManager().getModule(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.isWatermarkVisible() ? 1 : 0;
         case "info" -> var1 != null && var1.getInstance() ? 1 : 0;
         case "modules" -> var1 != null && var1.isModuleListVisible() ? 1 : 0;
         case "armor" -> var1 != null && var1.isArmorVisible() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.isKeybindsVisible() ? 1 : 0;
         case "effects" -> var1 != null && var1.getSpeedSettings() ? 1 : 0;
         case "speed" -> var1 != null && var1.isSpeedGraphVisible() ? 1 : 0;
         case "miner", "builder", "pilot", "hunter" -> renderElements(var0.id).liveCard() != null ? 1 : 0;
         case "staffs" -> StaffListModule.parseStaffNames();
         case "island" -> IslandLyricsModule.isOperationalInstance();
         case "casino" -> CasinoGameModule.getActiveGame() != null ? 1 : 0;
         case "cooldowns", "inventory", "keystrokes" -> var1 != null && var1.getEffectSettings(var0.id) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void isChatOpen() {
      resetPlacements(var0 -> true);
   }

   public static void resetPlacements(Predicate<HudLayout.HudElement> var0) {
      for (HudLayout.HudElement var2 : elements) {
         if (var0.test(var2)) {
            UiState.hudPositions.remove(var2.id);
            UiState.HudStyle var3 = UiState.hudStyles.get(var2.id);
            if (var3 != null) {
               UiState.hudStyles.put(var2.id, new UiState.HudStyle(var2.getDock(), var3.hidden()));
            }
         }
      }
   }

   public static void setElementEnabled(HudLayout.HudElement var0, boolean var1) {
      applyElementEnabled(var0, var1, var0x -> LarpClient.getInstance().getModuleManager().getByName(var0x));
   }

   public static void applyElementEnabled(HudLayout.HudElement var0, boolean var1, Function<String, Module> var2) {
      var0.setHidden(!var1);
      if (var1 && !var0.id.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.id.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.id.equals("target") ? "TargetHUD" : (var0.id.equals("staffs") ? "Staff List" : (var0.id.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.id) {
               case "watermark" -> "Watermark";
               case "info" -> "Info Panel";
               case "modules" -> "Module List";
               case "armor" -> "Armor";
               case "keybinds" -> "Keybinds";
               case "effects" -> "Potions";
               case "speed" -> "Speed";
               case "cooldowns" -> "Cooldowns";
               case "inventory" -> "Inventory";
               case "keystrokes" -> "Keystrokes";
               default -> "";
            };

            for (Setting var9 : var3.getSettings()) {
               if (var9.getName().equals(var4) && var9 instanceof BooleanSetting var7) {
                  var7.setValue(true);
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void registerOverlay() {
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "hudoverlay"),
         (var0, var1) -> {
            Minecraft var2 = Minecraft.getInstance();
            if (var2.player != null
               && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && !LegacyClickGuiScreen.isClickGuiScreen(var2.gui.screen())
               && !(var2.gui.screen() instanceof HudEditorScreen)) {
               GuiGraphicsExtractor var3 = StreamProofManager.selectGraphics(var0, false, false);
               if (var3 != null) {
                  beginFrame(var3);
                  renderHud(var3, false);
               }
            }
         }
      );
      LarpLog.info("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", elements.size());
   }

   public static void tickSpeedGraph(Minecraft var0) {
      HUDModule var1 = HUDModule.getInstance();
      if (var0.level == null || var0.player == null || var1 == null || !var1.isOperational()) {
         speedGraph.reset();
      } else if (!var0.isPaused() && var0.level.tickRateManager().runsNormally()) {
         speedGraph.record_(var0.level, var0.player, var0.player.getX(), var0.player.getZ(), var0.level.tickRateManager().tickrate());
      } else {
         speedGraph.resetPosition();
      }
   }

   private static List<ItemStack> getBounds() {
      if (width == null) {
         width = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return width;
   }

   public static void renderHud(GuiGraphicsExtractor var0, boolean var1) {
      renderElements(var0, var1, null, false);
   }

   public static void setHidden(GuiGraphicsExtractor var0, HudLayout.HudElement var1, boolean var2) {
      renderElements(var0, true, var1, var2);
   }

   public static List<HudLayout.ElementBounds> registerOverlay() {
      return lastLayoutVersion == UiState.revision ? lastLayout : List.of();
   }

   public static void clearLayout() {
      lastLayout = List.of();
   }

   public static void setHidden(GuiGraphicsExtractor var0, HudLayout.HudElement var1, boolean var2, boolean var3) {
      if (var3) {
         beginFrame(var0);
         renderHud(var0, false);
         lastLayout = collectVisibleBounds();
         lastLayoutVersion = UiState.revision;
      } else {
         setHidden(var0, var1, var2);
      }
   }

   private static void renderElements(GuiGraphicsExtractor var0, boolean var1, HudLayout.HudElement var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            lastLayout = List.of();
         }

         long var6 = PerfProfiler.startTimer();
         HUDModule var8 = HUDModule.getInstance();
         int var9 = var4.getWindow().getGuiScaledWidth();
         int var10 = var4.getWindow().getGuiScaledHeight();
         int var11 = !var1 && isChatOpen() ? 1 : 0;
         if (!var1 && var11 != wasChatOpen) {
            wasChatOpen = (boolean)var11;
            LarpLog.debug("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = moduleListLayout.getEntries(
            ChooseASkyEffectAndAdjustItsAppearanceModule.mergeSkyModules(
               LarpClient.getInstance().getModuleManager().getVisibleModules(), LarpClient.getInstance().getConfigManager().getPreferences()
            ),
            var4.font,
            FontRenderer.getVersion(),
            PostProcessing.reloadCount,
            HudPanels::textWidth
         );
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(
               new ModuleListCache.ModuleEntry("Sprint", HudPanels.textWidth("Sprint")),
               new ModuleListCache.ModuleEntry("Fullbright", HudPanels.textWidth("Fullbright"))
            );
         }

         HudLayout.HudElement var14 = getElement("modules");
         double var15 = var14.scale();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> HudRenderer2.panelColor(var0x.width())).max().orElse(0));
         ModuleListLayout.Layout var20 = var8 != null && !var8.isCardModuleList()
            ? null
            : ModuleListLayout.layout(var12, var1 || var13 != 0, ModuleListLayout.maxRows(var10, var15));
         if (var20 != null) {
            var14.width = var20.width();
            var14.height = var20.height();
         } else {
            var14.width = var18 * var19 + (var18 - 1) * 8;
            var14.height = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.maxScale = Math.max(0.1, Math.min(4.0, Math.min((var9 - 16.0) / var14.width, (var10 - 16.0) / var14.height)));
         ArrayList var21 = new ArrayList();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var25 : eQUIPMENT_SLOTS) {
               if (var25 != EquipmentSlot.MAINHAND && var25 != EquipmentSlot.OFFHAND || var8.armorHeldItems.getValue()) {
                  ItemStack var26 = var4.player.getItemBySlot(var25);
                  if (!var26.isEmpty() || var8.armorEmptySlots.getValue()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.armorReverse.getValue()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(ItemStack::isEmpty)) {
            var21.clear();
            var21.addAll(getBounds());
            if (var8 != null && var8.armorReverse.getValue()) {
               Collections.reverse(var21);
            }
         }

         int var69 = var8 != null && var8.armorDurability.isMode("Remaining") ? 46 : 26;
         int var70 = var8 != null && !var8.armorDurability.isMode("Off") ? 37 : 27;
         int var71 = var8 == null ? 5 : var8.armorSpacing.getValue().intValue();
         int var72 = var8 != null && var8.armorLayout.isMode("Vertical") ? 1 : 0;
         if (var72 != 0 && var8 != null && !var8.armorDurability.isMode("Off")) {
            var69 += 38;
            var70 = 26;
         }

         int var73 = Math.max(1, var21.size());
         getElement("armor").width = 6 + (var72 != 0 ? var69 : var73 * (var69 + var71) - var71);
         getElement("armor").height = 6 + (var72 != 0 ? var73 * (var70 + var71) - var71 : var70);
         String var27 = FakeFPSModule.getDisplayedFps(var4.getFps()) + " fps";
         String var28 = var4.player == null ? "Player" : CosmeticOverrides.nameOrDefault(var4.player.getUUID(), var4.player.getName().getString());
         var28 = HudPanels.truncate(var28, 110.0, 11.0);
         String var29 = !var4.hasSingleplayerServer() && var4.getCurrentServer() != null ? var4.getCurrentServer().ip : "singleplayer";
         var29 = HudPanels.truncate(var29, 130.0, 11.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         String var31 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         getElement("info").width = HudRenderer2.animatedWidth(var31);
         HudLayout.HudElement var32 = getElement("watermark");
         HudLayout.HudElement var33 = getElement("info");
         boolean var34 = isShownInEditor(var32, var1, var3, var2);
         int var35 = !var34 || !isShownInEditor(var33, var1, var3, var2) || !isAliasActive(var33) && selectedElement != var33 ? 0 : 1;
         if (infoRevealVersion != UiState.revision) {
            infoRevealVersion = UiState.revision;
            infoReveal.snapTo(var35 != 0 ? 1.0 : 0.0);
         }

         double var36 = Math.clamp(infoReveal.animateTo(var35 != 0 ? 1.0 : 0.0, 220), 0.0, 1.0);
         watermarkShown = var34;
         int var38 = var8 != null && var8.watermarkLogoSide.isMode("Right") ? 1 : 0;
         HudLayout2.Rect var39 = null;
         var32.width = HudRenderer2.animatedWidth(HudRenderer2.measureHud(var28, var29, var27, var30), var31, var36);
         var32.height = HudRenderer2.headerHeight(var36);
         int var40 = var11 == 0 || VisualsRenderer.getRecentTarget() != null || targetEntity != null && !(targetVisibility.getValue() < 0.01) ? 0 : 1;
         HashMap var41 = new HashMap();

         for (String var43 : List.of("cooldowns", "staffs", "inventory")) {
            HudPanels2.Panel var44 = HudPanels2.buildPanel(var43, var1);
            var41.put(var43, var44);
            getElement(var43).width = var44.width();
            getElement(var43).height = var44.height();
         }

         if (var8 != null) {
            getElement("keystrokes").width = KeystrokesHud.getWidth(var8);
            getElement("keystrokes").height = KeystrokesHud.getHeight(var8);
         }

         HudPanels.Panel var76 = HudPanels.keybindsPanel(var1);
         HudPanels.Panel var77 = HudPanels.potionsPanel(var1);
         HudPanels.Panel var78 = HudPanels.blackjackPanel(var1);
         getElement("casino").width = var78.width();
         getElement("casino").height = var78.height();

         for (HudLayout.HudCard var46 : cards) {
            var46.updateCard(var1);
         }

         int var79 = var11 == 0 || var4.player != null && !var4.player.getActiveEffects().isEmpty() ? 0 : 1;
         if (var79 != 0) {
            var77 = HudPanels.potionsPanel(true);
         }

         getElement("keybinds").width = var76.width();
         getElement("keybinds").height = var76.height();
         getElement("effects").width = var77.width();
         getElement("effects").height = var77.height();

         for (HudLayout.HudElement var47 : addBounds()) {
            var47.fitTo(var9, var10);
            int var48 = var47.getX(var9);
            int var49 = var47.getY(var10);
            boolean var50 = isEnabled(var47);
            if ((var1 ? HudLayout2.isAnyTrue(var50, var3, var2 == var47) : var50)
               && (
                  var1
                     || (!var47.id.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty()))
                        && (!var47.id.equals("armor") || isChatOpen(var21))
               )
               && (var1 || var11 != 0 || !var41.containsKey(var47.id) || HudPanels2.shouldShow(var47.id, (HudPanels2.Panel)var41.get(var47.id)))
               && (var1 || var79 != 0 || !var47.id.equals("effects") || var4.player != null && !var4.player.getActiveEffects().isEmpty())) {
               int var51 = !var1 && (var11 == 0 || IslandLyricsModule.isHudVisible()) ? 0 : 1;
               if (var1 || !var47.id.equals("island") || var51 != 0 || IslandLyricsModule.isHudVisible()) {
                  int var52 = !var47.id.equals("notifications") && !var47.id.equals("crosshair") ? 0 : 1;
                  if (var52 == 0 || var1) {
                     if (var47 != var33 || var35 == 0) {
                        var47.getY(var0, var48, var49);

                        try {
                           switch (var47.id) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                                 HudPanels2.renderPanel(var0, var47.id, (HudPanels2.Panel)var41.get(var47.id), var48, var49, var1);
                                 break;
                              case "island":
                                 IslandLyricsModule var93 = IslandLyricsModule.getInstance();
                                 if (var93 == null) {
                                    continue;
                                 }

                                 var93.renderHud(var0, var48, var49, var47.width, (boolean)var51);
                                 break;
                              case "keystrokes":
                                 if (var8 == null) {
                                    continue;
                                 }

                                 if (var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen)) {
                                    registerOverlay(var0, var48, var49, var47.width, var47.height);
                                 }

                                 KeystrokesHud.render(var0, var8, var48, var49, var1);
                                 break;
                              case "notifications":
                                 NotificationManager.renderPreview(var0, var48, var49);
                                 break;
                              case "crosshair":
                                 CustomCrosshairModule.drawPreview(var0, var48 + var47.width / 2, var49 + var47.height / 2);
                                 break;
                              case "watermark":
                                 if (!var1 && !var8.isWatermarkVisible()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen)) {
                                    registerOverlay(var0, var48, var49, var47.width, var47.height);
                                 }

                                 HudRenderer2.drawHudWithInfo(var0, var48, var49, var47.width, var28, var29, var27, var30, var31, var36);
                                 if (var35 != 0) {
                                    HudLayout2.Rect var92 = HudRenderer2.drawHud(var48, var49, var47.width, (boolean)var38, var31);
                                    var39 = new HudLayout2.Rect(
                                       var48 + (int)Math.floor((var92.left() - var48) * var47.effectiveScale()),
                                       var49 + (int)Math.floor((var92.top() - var49) * var47.effectiveScale()),
                                       (int)Math.ceil(var92.width() * var47.effectiveScale()),
                                       (int)Math.ceil(var92.height() * var47.effectiveScale())
                                    );
                                 }
                                 break;
                              case "speed":
                                 HudRenderer2.drawSpeedGraph(var0, var48, var49, speedGraph, var1);
                                 break;
                              case "miner":
                              case "builder":
                              case "pilot":
                              case "hunter":
                                 HudLayout.HudCard var91 = renderElements(var47.id);
                                 if (var91.card == null) {
                                    continue;
                                 }

                                 if (var8 != null && var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen)) {
                                    registerOverlay(var0, var48, var49, var47.width, var47.height);
                                 }

                                 HudPanels.resetOnWorldChange(
                                    var0,
                                    var48,
                                    var49,
                                    var47.width,
                                    var91.title,
                                    "scan",
                                    var91.card.headline(),
                                    var91.card.value(),
                                    var91.card.action(),
                                    var91.card.time(),
                                    var91.progress,
                                    var91.card.alert()
                                 );
                                 break;
                              case "info":
                                 if (!var1 && !var8.getInstance()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen)) {
                                    registerOverlay(var0, var48, var49, var47.width, var47.height);
                                 }

                                 HudRenderer2.drawInfoBox(var0, var48, var49, var47.width, var31);
                                 break;
                              case "modules":
                                 if (!var1 && !var8.isModuleListVisible()) {
                                    continue;
                                 }

                                 int var90 = var8 != null && var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var90 != 0) {
                                       registerOverlay(var0, var48, var49, var47.width, var47.height);
                                    }

                                    ModuleListLayout.render(var0, var20, var48, var49);
                                    if (!var1) {
                                       addBounds(var47, var47.getBounds(var9, var10));
                                    }
                                 } else {
                                    for (int var95 = 0; var95 < var12.size(); var95++) {
                                       String var98 = ((ModuleListCache.ModuleEntry)var12.get(var95)).name();
                                       int var101 = var95 / var17;
                                       int var102 = var95 % var17;
                                       int var103 = var48 + var47.scaledWidth() / 2 > var9 / 2 ? 1 : 0;
                                       int var104 = var103 != 0
                                          ? var48 + var47.width - var101 * (var19 + 8)
                                          : var48 + var101 * (var19 + 8) + HudRenderer2.panelColor(((ModuleListCache.ModuleEntry)var12.get(var95)).width());
                                       int var62 = ((ModuleListCache.ModuleEntry)var12.get(var95)).width();
                                       int var105 = HudRenderer2.panelColor(var62);
                                       int var106 = var104 - var105;
                                       int var107 = var49 + var102 * 17;
                                       if (var90 != 0) {
                                          registerOverlay(var0, var106, var107, var105, 17);
                                       }

                                       HudRenderer2.drawTagPill(var0, var98, var62, var104, var107, (boolean)var103);
                                       if (!var1) {
                                          addBounds(
                                             var47,
                                             new HudLayout2.Rect(
                                                var48 + (int)Math.floor((var106 - var48) * var47.effectiveScale()),
                                                var49 + (int)Math.floor((var107 - var49) * var47.effectiveScale()),
                                                (int)Math.ceil(var105 * var47.effectiveScale()),
                                                (int)Math.ceil(17.0 * var47.effectiveScale())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    FontRenderer.drawDefaultText(var0, "No active modules", var48 + 4, var49 + 5, Theme.textSecondary);
                                 }
                                 break;
                              case "target":
                                 if (!var1 && var40 == 0) {
                                    setSelected(var0, var48, var49);
                                 } else {
                                    renderSampleTarget(var0, var48, var49);
                                 }
                                 break;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 HudPanels.Panel var89 = var47.id.equals("keybinds") ? var76 : (var47.id.equals("casino") ? var78 : var77);
                                 int var94 = var48 + var47.scaledWidth() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.isBlurEnabled() && !(var4.gui.screen() instanceof ClickGuiScreen)) {
                                    for (HudLayout2.Rect var99 : HudPanels.blackjackPanel(var89, var48, var49, (boolean)var94)) {
                                       registerOverlay(var0, var99.left(), var99.top(), var99.width(), var99.height());
                                    }
                                 }

                                 HudPanels.formatDuration(var0, var89, var48, var49, (boolean)var94);
                                 if (!var1) {
                                    for (HudLayout2.Rect var100 : HudPanels.blackjackPanel(var89, var48, var49, (boolean)var94)) {
                                       addBounds(
                                          var47,
                                          new HudLayout2.Rect(
                                             var48 + (int)Math.floor((var100.left() - var48) * var47.effectiveScale()),
                                             var49 + (int)Math.floor((var100.top() - var49) * var47.effectiveScale()),
                                             (int)Math.ceil(var100.width() * var47.effectiveScale()),
                                             (int)Math.ceil(var100.height() * var47.effectiveScale())
                                          )
                                       );
                                    }
                                 }
                                 break;
                              case "armor":
                                 if (!var1 && !var8.isArmorVisible()) {
                                    continue;
                                 }

                                 if (var8.armorBackground.getValue() && isChatOpen(var21)) {
                                    DrawUtil.fillRoundedRect(var0, var48, var49, var47.width, var47.height, 6.0F, HudRenderer2.panelColor());
                                    HudRenderer2.drawGlow(var0, var48, var49, var47.width, var47.height, 6.0F);
                                    HudRenderer2.drawShine(var0, var48, var49, var47.width, var47.height, 6);
                                 }

                                 for (int var55 = 0; var55 < var21.size(); var55++) {
                                    ItemStack var56 = (ItemStack)var21.get(var55);
                                    int var57 = var48 + 3 + (var72 != 0 ? 0 : var55 * (var69 + var71));
                                    int var58 = var49 + 3 + (var72 != 0 ? var55 * (var70 + var71) : 0);
                                    if (!var56.isEmpty()) {
                                       int var59 = var72 != 0 ? var57 : var57 + (var69 - 21) / 2;
                                       var0.pose().pushMatrix();
                                       var0.pose().translate(var59, var58).scale(1.3125F);
                                       var0.item(var56, 0, 0);
                                       var0.pose().popMatrix();
                                       if (var56.isDamageableItem()) {
                                          int var60 = Math.max(0, var56.getMaxDamage() - var56.getDamageValue());
                                          double var61 = (double)var60 / Math.max(1, var56.getMaxDamage());
                                          int var63 = ColorUtil.lerpColor(-43434, -8790368, var61);
                                          if (var8.armorBars.getValue()) {
                                             DrawUtil.fillRect(var0, var57 + 2, var58 + 23, var69 - 4, 2, 1879048192);
                                             DrawUtil.fillRect(var0, var57 + 2, var58 + 23, (int)Math.round((var69 - 4) * var61), 2, var63);
                                          }

                                          if (!var8.armorDurability.isMode("Off")) {
                                             String var64 = var8.armorDurability.isMode("Percent")
                                                ? (int)Math.ceil(var61 * 100.0) + "%"
                                                : Integer.toString(var60);
                                             int var65 = var72 != 0 ? var57 + 28 : var57 + (var69 - HudPanels.textWidthAtSize(var64, 10.4)) / 2;
                                             HudPanels.drawText(var0, var64, var65, var58 + (var72 != 0 ? 7 : 28), 10.4, var63);
                                          }
                                       }
                                    }
                                 }
                           }
                        } finally {
                           var47.popScale(var0);
                        }

                        if (!var1
                           && (
                              var47.id.equals("target")
                                 ? var40 != 0
                                 : !var47.id.equals("modules") && !var47.id.equals("keybinds") && !var47.id.equals("effects") && !var47.id.equals("casino")
                           )) {
                           addBounds(var47, var47.getBounds(var9, var10));
                        }

                        if (var1) {
                           var5.add(new HudLayout.ElementBounds(var47, var47.getBounds(var9, var10)));
                        }

                        if (var1 && !var50) {
                           HudLayout2.Rect var88 = var47.getBounds(var9, var10);
                           DrawUtil.fillRect(var0, var88.left(), var88.top(), var88.width(), var88.height(), 1882207290);
                        }
                     } else if (var39 != null) {
                        if (var1) {
                           var5.add(new HudLayout.ElementBounds(var47, var39));
                        } else {
                           addBounds(var47, var39);
                        }
                     }
                  }
               }
            }
         }

         if (var1) {
            lastLayout = List.copyOf(var5);
            lastLayoutVersion = UiState.revision;
         }

         if (!var1 && Boolean.getBoolean("larp.debug")) {
            int var81 = var10 - 15;

            for (Entry var84 : PerfProfiler.getCounters().entrySet()) {
               String var86 = (String)var84.getKey() + " " + var84.getValue();
               FontRenderer.drawDefaultText(var0, var86, var9 - FontRenderer.getDefaultWidth(var86) - 5, var81, Theme.textSecondary);
               var81 -= 11;
            }

            for (Entry var85 : PerfProfiler.getTimings().entrySet()) {
               String var87 = String.format(Locale.ROOT, "%s %.2f ms CPU", var85.getKey(), var85.getValue());
               FontRenderer.drawDefaultText(var0, var87, var9 - FontRenderer.getDefaultWidth(var87) - 5, var81, Theme.textSecondary);
               var81 -= 11;
            }
         }

         PerfProfiler.endTimer("HUD", var6);
      }
   }

   private static void registerOverlay(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.pose().transformPosition(var1, var2, new Vector2f());
      PostProcessing.addBlurRegion(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.pose().m00()), Math.round(var4 * var0.pose().m11()));
   }

   public static void addBounds() {
      targetEntity = null;
      targetHealthAnim.snapTo(1.0);
      targetDamageAnim.snapTo(1.0);
      lastHealthFraction = 1.0;
      damageHoldUntil = 0L;
      targetVisibility.snapTo(0.0);
      hitParticles.clear();
      lastTargetNanos = 0L;
      lastTargetHealth = 0.0F;
   }

   public static void renderSampleTarget(GuiGraphicsExtractor var0, int var1, int var2) {
      renderTarget(var0, var1, var2, Minecraft.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void renderTarget(
      GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3, String var4, float var5, float var6, float var7, double var8
   ) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      renderTargetCard(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void setSelected(GuiGraphicsExtractor var0, int var1, int var2) {
      long var3 = PerfProfiler.startTimer();

      try {
         Minecraft var5 = Minecraft.getInstance();
         if (targetEntity != null && targetEntity.level() != var5.level) {
            addBounds();
         }

         LivingEntity var6 = VisualsRenderer.getRecentTarget();
         if (var6 != null && var6 != targetEntity) {
            targetEntity = var6;
            LarpLog.debug(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getBbWidth(),
               var6.getBbHeight(),
               renderTarget(var6.getBbWidth(), var6.getBbHeight())
            );
            hitParticles.clear();
            lastTargetHealth = EntityHealth.getHealth(var6);
            lastTargetNanos = System.nanoTime();
            double var7 = EntityHealth.getHealthFraction(var6);
            targetHealthAnim.snapTo(var7);
            targetDamageAnim.snapTo(var7);
            lastHealthFraction = var7;
            damageHoldUntil = 0L;
         }

         long var22 = System.nanoTime();
         if (lastTargetNanos != 0L) {
            hitParticles.update((var22 - lastTargetNanos) / 1.0E9);
         }

         lastTargetNanos = var22;
         float var9 = targetEntity == null ? 0.0F : EntityHealth.getHealth(targetEntity);
         if (targetEntity != null) {
            hitParticles.burst(lastTargetHealth - var9, ThreadLocalRandom.current());
            lastTargetHealth = var9;
         }

         double var10 = targetVisibility.animateTo(var6 == null ? 0.0 : 1.0, 200);
         if (targetEntity == null || var10 < 0.01) {
            if (var6 == null) {
               targetEntity = null;
               hitParticles.clear();
            }

            return;
         }

         HudLayout.HudElement var12 = getElement("target");
         double var13 = 0.92 + 0.08 * var10;
         addBounds(var12, renderHud(var1, var2, var12.effectiveScale(), var13, new HudLayout2.Rect(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, targetEntity.getMaxHealth()), 0.0F, 1.0F);
         double var17 = targetHealthAnim.animateTo(var15, 220);
         renderTargetCard(
            var0,
            var1,
            var2,
            targetEntity,
            CosmeticOverrides.nameOrDefault(targetEntity.getUUID(), targetEntity.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, targetEntity.getMaxHealth()),
            Math.max(0.0F, targetEntity.getAbsorptionAmount()),
            var5.player == null ? 0.0 : var5.player.distanceTo(targetEntity),
            var10,
            var17,
            smoothDamage(var15, var17, var22),
            false
         );
      } finally {
         PerfProfiler.endTimer("TargetHUD", var3);
      }
   }

   private static double smoothDamage(double var0, double var2, long var4) {
      if (var0 < lastHealthFraction) {
         targetDamageAnim.snapTo(Math.max(targetDamageAnim.getValue(), var2));
         damageHoldUntil = var4 + 400000000L;
      }

      lastHealthFraction = var0;
      double var6 = var4 < damageHoldUntil ? targetDamageAnim.getValue() : targetDamageAnim.animateToWithEasing(var0, 450, Animation.Easing.EASE_OUT_QUAD);
      return Math.max(var6, var2);
   }

   public static HudLayout2.Rect renderHud(int var0, int var1, double var2, double var4, HudLayout2.Rect var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.left() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.top() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new HudLayout2.Rect(
         var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16
      );
   }

   public static float renderTarget(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void isChatOpen(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.createRenderState(var3, 1.0F);
      float var6 = renderTarget(var5.boundingBoxWidth, var5.boundingBoxHeight);
      if (var6 <= 0.0F) {
         LarpLog.errorOnce("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         HudPanels.drawIconOrFallback(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
      } else {
         var5.shadowPieces.clear();
         var5.outlineColor = 0;
         if (var5 instanceof LivingEntityRenderState var7) {
            var7.bodyRot = 180.0F;
            var7.yRot = 0.0F;
            var7.xRot = 0.0F;
         }

         ScreenRectangle var11 = new ScreenRectangle(var1, var2, 50, 50).transformAxisAligned(var0.pose());
         var0.enableScissor(var1, var2, var1 + 50, var2 + 50);

         try {
            var0.entity(
               var5,
               var6 * var0.pose().m00(),
               new Vector3f(0.0F, var5.boundingBoxHeight / 2.0F, 0.0F),
               new Quaternionf().rotateZ((float) Math.PI),
               new Quaternionf(),
               var11.left(),
               var11.top(),
               var11.right(),
               var11.bottom()
            );
         } finally {
            var0.disableScissor();
         }
      }
   }

   public static List<HudLayout.Row> isChatOpen(boolean var0, boolean var1) {
      if (var1) {
         return var0
            ? List.of(HudLayout.Row.HEALTH, HudLayout.Row.HEALTH_BAR, HudLayout.Row.NAME_AND_DISTANCE)
            : List.of(HudLayout.Row.HEALTH_BAR, HudLayout.Row.NAME_AND_DISTANCE, HudLayout.Row.HEALTH);
      } else {
         return var0
            ? List.of(HudLayout.Row.NAME_AND_DISTANCE, HudLayout.Row.HEALTH, HudLayout.Row.HEALTH_BAR)
            : List.of(HudLayout.Row.NAME_AND_DISTANCE, HudLayout.Row.HEALTH_BAR, HudLayout.Row.HEALTH);
      }
   }

   private static String renderTarget(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void renderTargetCard(
      GuiGraphicsExtractor var0,
      int var1,
      int var2,
      LivingEntity var3,
      String var4,
      float var5,
      float var6,
      float var7,
      double var8,
      double var10,
      double var12,
      double var14,
      boolean var16
   ) {
      Minecraft var17 = Minecraft.getInstance();
      TargetHUDModule var18 = LarpClient.getInstance().getModuleManager().getModule(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.pose().pushMatrix();
      var0.pose().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.getInstance();
         if (var23 != null && var23.isBlurEnabled() && !(var17.gui.screen() instanceof ClickGuiScreen)) {
            registerOverlay(var0, var1, var2, var19, var20);
         }

         DrawUtil.fillRoundedRect(var0, var1, var2, var19, var20, 10.0F, ColorUtil.multiplyAlpha(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !hitParticles.getParticles().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + var19, var2 + var20);

            try {
               for (SparkParticles.Spark var29 : hitParticles.getParticles()) {
                  double var30 = Math.clamp(var29.age / var29.lifetime, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.flipped ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.length * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.x);
                  int var36 = var27 + (int)Math.round(var29.y);
                  int var37 = var29.flipped ? Theme.accent : -419430401;
                  DrawUtil.fillCircle(var0, var35, var36, (int)Math.ceil(var34 * 1.125), ColorUtil.multiplyAlpha(var37, var32 * 0.22));
                  DrawUtil.fillCircle(var0, var35, var36, Math.max(1, var34 / 2), ColorUtil.multiplyAlpha(var37, var32));
               }
            } finally {
               var0.disableScissor();
            }
         }

         DrawUtil.fillRoundedRect(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, ColorUtil.multiplyAlpha(822083583, var10));
         if (var3 instanceof AbstractClientPlayer var55) {
            int var57 = Math.min(140, var55.hurtTime * 16);
            var0.pose().pushMatrix();
            float var59 = (float)Math.sin(var55.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.pose().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            PlayerFaceExtractor.extractRenderState(
               var0, var55.getSkin(), var24, var25, 36, ColorUtil.multiplyAlpha(-65536 | 255 - var57 << 8 | 255 - var57, var10)
            );
            var0.pose().popMatrix();
         } else if (var3 != null) {
            var0.pose().pushMatrix();
            var0.pose().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               isChatOpen(var0, var24, var25, var3);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            HudPanels.drawIconOrFallback(var0, "user", var24 + 7, var25 + 7, 22, ColorUtil.multiplyAlpha(-5592406, var10));
         }

         int var56 = var18 != null && !var18.equipmentPosition.isMode("Above") ? 0 : 1;
         int var58 = var18 != null && var18.healthBarPosition.isMode("Above") ? 1 : 0;
         int var60 = var18 != null && var18.handItemsSide.isMode("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = ColorUtil.multiplyAlpha(-1, var10);
         int var62 = ColorUtil.multiplyAlpha(-6645086, var10);
         double var63 = var25;

         for (HudLayout.Row var38 : isChatOpen((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case NAME_AND_DISTANCE:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - HudPanels.textWidthAtSize(var65, 10.0);
                  HudPanels.drawText(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  HudPanels.drawText(var0, HudPanels.truncate(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case HEALTH:
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, heartContainerSprite, var31, var39 + 2, 9, 9, var33);
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, heartFullSprite, var31, var39 + 2, 9, 9, var33);
                  String var40 = renderTarget(var5);
                  HudPanels.drawText(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     HudPanels.drawText(
                        var0,
                        "+" + renderTarget(var7),
                        var31 + 16 + HudPanels.textWidthAtSize(var40, 12.0),
                        var39 + 2.5,
                        10.0,
                        ColorUtil.multiplyAlpha(-14003, var10)
                     );
                  }

                  if (var3 instanceof AbstractClientPlayer) {
                     renderHud(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case HEALTH_BAR:
                  setHidden(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.HEIGHT + 1.5;
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   private static void setHidden(GuiGraphicsExtractor var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      DrawUtil.fillRoundedRect(var0, var1, var2, var3, 5.0F, 2.0F, ColorUtil.multiplyAlpha(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         DrawUtil.fillRoundedRect(var0, var1, var2, var15, 5.0F, 2.0F, ColorUtil.multiplyAlpha(-1862270977, var10));
      }

      if (var14 > 0) {
         RoundedShapes.fillRoundedGradientHorizontal(
            var0, var1, var2, var14, 5.0F, 2.0F, ColorUtil.multiplyAlpha(-4776932, var10), ColorUtil.multiplyAlpha(-42418, var10)
         );
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            DrawUtil.fillRoundedRect(var0, var1 + var14, var2, var16, 5.0F, 2.0F, ColorUtil.multiplyAlpha(-14003, var10));
         }
      }
   }

   private static void renderHud(GuiGraphicsExtractor var0, LivingEntity var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            ItemStack var13 = var1.getItemBySlot(eQUIPMENT_SLOTS[var12]);
            if (var13.isEmpty()) {
               DrawUtil.fillRoundedRect(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, ColorUtil.multiplyAlpha(486539263, var5));
            } else {
               if (var5 > 0.5) {
                  var0.pose().pushMatrix();
                  var0.pose().translate(var7, var3).scale(0.75F);

                  try {
                     var0.item(var13, 0, 0);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }

               if (var13.isDamageableItem() && var13.getDamageValue() > 0) {
                  double var14 = Math.clamp((double)(var13.getMaxDamage() - var13.getDamageValue()) / Math.max(1, var13.getMaxDamage()), 0.0, 1.0);
                  DrawUtil.fillRoundedRect(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, ColorUtil.multiplyAlpha(-1610612736, var5));
                  DrawUtil.fillRoundedRect(
                     var0,
                     var7 + 1,
                     var3 + 12,
                     (float)Math.max(1.0, 10.0 * var14),
                     1.5F,
                     0.75F,
                     ColorUtil.multiplyAlpha(ColorUtil.lerpColor(-43434, -10755203, var14), var5)
                  );
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   public record ElementBounds(HudLayout.HudElement element, HudLayout2.Rect bounds) {
   }

   private static final class HudCard {
      final String id;
      final String title;
      final Supplier<? extends ModulePanel> panelSupplier;
      final ModulePanel.HudCard previewCard;
      final Animation progressAnimation = new Animation(0.0);
      boolean hadLiveCard;
      ModulePanel.HudCard card;
      double progress;

      HudCard(String var1, String var2, Supplier<? extends ModulePanel> var3, ModulePanel.HudCard var4) {
         this.id = var1;
         this.title = var2;
         this.panelSupplier = var3;
         this.previewCard = var4;
      }

      ModulePanel.HudCard liveCard() {
         ModulePanel var1 = this.panelSupplier.get();
         return var1 == null ? null : var1.hudCard();
      }

      void updateCard(boolean var1) {
         ModulePanel.HudCard var2 = this.liveCard();
         this.card = var2 != null ? var2 : (var1 ? this.previewCard : null);
         this.progress = this.card == null ? 0.0 : this.card.progress();
         if (var2 != null && !var1) {
            if (!this.hadLiveCard) {
               this.progressAnimation.snapTo(Math.max(0.0, this.progress));
            }

            this.progress = this.progressAnimation.animateTo(Math.max(0.0, this.progress), 450);
         }

         if (!var1) {
            this.hadLiveCard = (boolean)(var2 != null ? 1 : 0);
         }

         if (this.card != null) {
            HudLayout.getElement(this.id).width = HudPanels.cardWidth(this.title, this.card.headline(), this.card.value(), this.card.action(), this.card.time());
         }
      }
   }

   public static final class HudElement {
      public final String id;
      public int width;
      public int height;
      private double maxScale = 4.0;
      private final UiState.HudPosition defaultPosition;

      HudElement(String var1, int var2, int var3, double var4, double var6) {
         this.id = var1;
         this.width = var2;
         this.height = var3;
         this.defaultPosition = new UiState.HudPosition(var4, var6);
      }

      public double getDock() {
         return switch (this.id) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "miner", "builder", "pilot", "hunter" -> 0.8;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double scale() {
         UiState.HudStyle var1 = UiState.hudStyles.get(this.id);
         return var1 == null ? this.getDock() : var1.scale();
      }

      public double effectiveScale() {
         return Math.min(this.maxScale, this.scale());
      }

      public void fitTo(int var1, int var2) {
         this.maxScale = Math.max(0.1, Math.min(4.0, Math.min((double)Math.max(1, var1) / this.width, (double)Math.max(1, var2) / this.height)));
      }

      public HudLayout2.Rect getBounds(int var1, int var2) {
         if (this.id.equals("crosshair")) {
            int var3 = CustomCrosshairModule.getCurrentExtent();
            return new HudLayout2.Rect(
               this.getX(var1) + (int)Math.round((this.width / 2.0 - var3) * this.effectiveScale()),
               this.getY(var2) + (int)Math.round((this.height / 2.0 - var3) * this.effectiveScale()),
               (int)Math.ceil(var3 * 2 * this.effectiveScale()),
               (int)Math.ceil(var3 * 2 * this.effectiveScale())
            );
         } else {
            return new HudLayout2.Rect(
               this.getX(var1),
               this.getY(var2),
               this.scaledWidth(),
               this.id.equals("notifications") ? (int)Math.round(30.0 * this.effectiveScale()) : this.scaledHeight()
            );
         }
      }

      public HudLayout2.Rect setPosition(int var1, int var2) {
         return new HudLayout2.Rect(this.getX(var1), this.getY(var2), this.scaledWidth(), this.scaledHeight());
      }

      public void resetPlacement() {
         UiState.hudPositions.remove(this.id);
         UiState.hudStyles.put(this.id, new UiState.HudStyle(this.getDock(), this.isHidden()));
      }

      public void clearOverrides() {
         UiState.hudPositions.remove(this.id);
         UiState.hudStyles.remove(this.id);
      }

      public void setScale() {
         UiState.hudPositions.put(this.id, new UiState.HudPosition(0.5, 0.5));
      }

      public boolean isHidden() {
         return UiState.hudStyles.getOrDefault(this.id, HudLayout.dEFAULT_DOCK_STATE).hidden();
      }

      public int scaledWidth() {
         return (int)Math.round(this.width * this.effectiveScale());
      }

      public int scaledHeight() {
         return (int)Math.round(this.height * this.effectiveScale());
      }

      public void setHidden(boolean var1) {
         UiState.hudStyles.put(this.id, new UiState.HudStyle(this.scale(), var1, this.getDock()));
      }

      public void setScale(double var1) {
         UiState.hudStyles.put(this.id, new UiState.HudStyle(var1, this.isHidden(), this.getDock()));
      }

      public String getDock() {
         UiState.HudStyle var1 = UiState.hudStyles.get(this.id);
         return var1 == null ? null : var1.dock();
      }

      public void popScale(String var1) {
         UiState.hudStyles.put(this.id, new UiState.HudStyle(this.scale(), this.isHidden(), var1));
      }

      public void getY(GuiGraphicsExtractor var1, int var2, int var3) {
         var1.pose().pushMatrix();
         var1.pose().translate(var2, var3).scale((float)this.effectiveScale()).translate(-var2, -var3);
      }

      public void popScale(GuiGraphicsExtractor var1) {
         var1.pose().popMatrix();
      }

      public int getX(int var1) {
         return this.id.equals("crosshair") && !UiState.hudPositions.containsKey(this.id)
            ? Math.max(0, var1 / 2 - this.scaledWidth() / 2)
            : (int)Math.round(UiState.hudPositions.getOrDefault(this.id, this.defaultPosition).xFraction() * Math.max(0, var1 - this.scaledWidth()));
      }

      public int getY(int var1) {
         return this.id.equals("crosshair") && !UiState.hudPositions.containsKey(this.id)
            ? Math.max(0, var1 / 2 - this.scaledHeight() / 2)
            : (int)Math.round(UiState.hudPositions.getOrDefault(this.id, this.defaultPosition).yFraction() * Math.max(0, var1 - this.scaledHeight()));
      }

      public void setPosition(int var1, int var2, int var3, int var4) {
         UiState.hudPositions
            .put(
               this.id, new UiState.HudPosition((double)var1 / Math.max(1, var3 - this.scaledWidth()), (double)var2 / Math.max(1, var4 - this.scaledHeight()))
            );
      }
   }

   public enum Row {
      NAME_AND_DISTANCE(14),
      HEALTH(14),
      HEALTH_BAR(5);

      public final int HEIGHT;

      Row(int var3) {
         this.HEIGHT = var3;
      }
   }
}
