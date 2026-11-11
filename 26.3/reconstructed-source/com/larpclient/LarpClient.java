package com.larpclient;

import com.larpclient.account.MicrosoftAccounts;
import com.larpclient.config.ConfigManager;
import com.larpclient.config.DisplayAliases;
import com.larpclient.core.BuildInfo;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.misc.MenuTrace;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.KeystrokesHud;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.module.combat.AutoPearlCatchModule;
import com.larpclient.module.combat.LungeSwapModule;
import com.larpclient.module.combat.WTapModule;
import com.larpclient.module.misc.AutoReconnectModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleManager;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.StreamProofState;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.module.movement.WindChargeHopModule;
import com.larpclient.module.render.AuraRenderer;
import com.larpclient.module.render.ClickPulseModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.module.render.FreelookModule;
import com.larpclient.module.render.MaceRenderer;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.network.ServiceStatusClient;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.world.TerrainMaterials;
import com.larpclient.setting.SettingShortcuts;
import com.larpclient.social.presence.DiscordRichPresence;
import com.larpclient.update.UpdateRuntime;
import com.larpclient.util.data.ActivityTracker2;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.data.KeyPressTracker;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarpClient implements ClientModInitializer {
   public static final String mOD_ID = "larp-client";
   public static final String vERSION = BuildInfo.larpVersion;
   public static final Logger lOGGER = LoggerFactory.getLogger("larp-client");
   public static boolean reachedTitleScreen = (boolean)0;
   private static LarpClient instance;
   private ModuleManager moduleManager;
   private ConfigManager configManager;
   private DiscordRichPresence discordPresence;
   private HudLayout hudLayout;
   private KeyMapping openGuiKey;
   private KeyMapping openEditorKey;
   private final KeyPressTracker<Module> moduleKeyTracker = new KeyPressTracker<>(349);
   private final KeyPressTracker<SettingShortcuts.ShortcutEntry> settingShortcutTracker = new KeyPressTracker<>(349);
   private boolean appliedDefaults = (boolean)0;
   private boolean seenTitleScreen = (boolean)0;
   private volatile boolean stopping;
   public static final Category mainCategory = Category.register(Identifier.fromNamespaceAndPath("larp-client", "main"));
   public static final Category editingCategory = Category.register(Identifier.fromNamespaceAndPath("larp-client", "editing"));
   private int presenceFlags = -1;
   private ServerData lastServer;
   private String lastServerIp;
   private final StreamProofState.TripleTapDetector legitModeDetector = new StreamProofState.TripleTapDetector();

   public void onInitializeClient() {
      instance = this;
      lOGGER.info("LarpClient v{} by Tobias - loading...", vERSION);
      ActivityTracker2.init();
      this.moduleManager = new ModuleManager();
      this.moduleManager.registerDefaults();
      this.configManager = new ConfigManager();
      this.configManager.load();
      StreamProofState.setActive(this.configManager.getPreferences().legitMode);
      MicrosoftAccounts.INSTANCE.start(Minecraft.getInstance());
      UpdateRuntime.start();
      this.discordPresence = new DiscordRichPresence(this.configManager.getPreferences().discordPresence);
      this.hudLayout = new HudLayout();
      this.hudLayout.registerOverlay();
      NotificationManager.getInstance().register();
      AuraRenderer.registerRenderHooks();
      MaceRenderer.register();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.discordPresence.close();
         ActivityTracker2.shutdown();
         this.stopping = (boolean)1;
         this.resetInputState();
         this.configManager.close();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      VisualsRenderer.register();
      TerrainMaterials.initialize();
      this.openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_gui", Type.KEYBOARD, KeyCodes.toScancode(344), mainCategory));
      this.openEditorKey = KeyMappingHelper.registerKeyMapping(
         new KeyMapping("key.larp-client.open_editor", Type.KEYBOARD, KeyCodes.toScancode(-1), editingCategory)
      );
      LarpLog.info("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         ActivityTracker2.flush();
         if (!this.stopping) {
            var2.execute(() -> {
               if (!this.stopping && (var2.getConnection() == null || var2.getConnection() == var1)) {
                  this.resetInputState();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
      lOGGER.info("LarpClient v{} loaded! {} modules registered.", vERSION, this.moduleManager.getModules().size());
      LarpLog.info("Startup", "Press RIGHT SHIFT to open the GUI");
   }

   public void getModuleManager(boolean var1) {
      if (this.discordPresence != null) {
         this.discordPresence.setEnabled(var1);
      }
   }

   private void updateDiscordState(Minecraft var1) {
      if (this.discordPresence != null) {
         ServerData var2 = var1.getCurrentServer();
         int var3 = var1.level != null ? 1 : 0;
         boolean var4 = var1.hasSingleplayerServer();
         String var5 = var2 == null ? null : var2.ip;
         int var6 = (var3 != 0 ? 1 : 0) | (var4 ? 2 : 0);
         if (var6 != this.presenceFlags || var2 != this.lastServer || !Objects.equals(var5, this.lastServerIp)) {
            this.presenceFlags = var6;
            this.lastServer = var2;
            this.lastServerIp = var5;
            this.discordPresence
               .setState(DiscordRichPresence.resolveState((boolean)var3, var4, var2 != null && var2.isRealm(), var2 != null && var2.isLan(), var5));
         }
      }
   }

   private void resetInputState() {
      this.moduleKeyTracker.clearPending();
      this.settingShortcutTracker.clearPending();

      for (Module var2 : this.moduleManager.getModules()) {
         var2.suspendForDisconnect();
      }

      this.configManager.hasProfile();
   }

   public boolean handleLegitModeKey(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      Screen var3 = var2.gui.screen();
      int var4 = var2.gui.overlay() != null ? 1 : 0;
      if (!this.stopping && this.configManager != null && var4 == 0 && !WorldMenuModule.isOccluded() && StreamProofState.isExempt(var3)) {
         if (!this.legitModeDetector.registerKey(var1, this.configManager.getPreferences().legitModeKey, System.nanoTime() / 1000000L)) {
            return false;
         }

         this.setLegitMode(!StreamProofState.isHidden());

         while (var2.options.keyCommand.consumeClick()) {
         }

         return true;
      } else {
         this.legitModeDetector.reset();
         return false;
      }
   }

   public void setLegitMode(boolean var1) {
      if (var1 != StreamProofState.isHidden()) {
         this.moduleManager.setLegitMode(var1);
         this.configManager.getPreferences().legitMode = var1;
         this.configManager.queuePreferencesWrite();
         this.moduleKeyTracker.clearPending();
         this.settingShortcutTracker.clearPending();
         Minecraft var2 = Minecraft.getInstance();
         Screen var3 = var2.gui.screen();
         if (!(var3 instanceof ChatScreen) && !(var3 instanceof ClickGuiScreen) && !(var3 instanceof LegacyClickGuiScreen)) {
            WorldMenuModule.closeWithKey();
         } else {
            var2.gui.setScreen(null);
         }

         if (var1) {
            NotificationManager.getInstance().clear();
         } else {
            NotificationManager.getInstance().notifyToggle("Legit mode", false);
         }
      }
   }

   public void onMouseClick(int var1) {
      KeystrokesHud.recordClick(var1);
      CustomCrosshairModule.onClick(var1);
      ClickPulseModule.onClick(var1);
      int var2 = BindCodes.toMouseBind(var1);
      if (var2 != -1) {
         if (WorldMenuModule.getPanelDistance()) {
            this.moduleKeyTracker.markHandled(var2);
            this.settingShortcutTracker.markHandled(var2);
         } else {
            this.onMouseBind(var2);
         }
      }
   }

   public void onMouseBind(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!this.stopping
         && var2.player != null
         && var2.gui.screen() == null
         && var2.gui.overlay() == null
         && var2.isWindowActive()
         && !WorldMenuModule.isOccluded()) {
         this.moduleKeyTracker.markPending(var1);
         this.settingShortcutTracker.markPending(var1);
         LungeSwapModule.onKeyPress(var1);
         AutoPearlCatchModule.onCatchKey(var1);
         WindChargeHopModule.onKeyPress(var1);
         SwordModelsModule var3 = SwordModelsModule.getInstance();
         if (var3 != null) {
            var3.getModel(var1);
         }
      } else {
         this.moduleKeyTracker.markHandled(var1);
         this.settingShortcutTracker.markHandled(var1);
      }
   }

   private void onEndTick(Minecraft var1) {
      FreecamModule.onScroll();
      FreelookModule.checkHold();

      for (Module var3 : this.moduleManager.getModules()) {
         var3.validateHeldBind();
      }

      this.moduleManager.suspendOnStateChange();
      AutoReconnectModule.onClientTick(var1);
      AimAssistModule.updateState();
      WTapModule.validateContext();
      HudLayout.tickSpeedGraph(var1);
      ActivityTracker2.tick(var1);
      DisplayAliases.tick();
      this.updateDiscordState(var1);
      MenuTrace.traceMenu(var1);
      ServiceStatusClient.iNSTANCE.fetchStatus();
      if (var1.gui.screen() instanceof TitleScreen) {
         reachedTitleScreen = (boolean)1;
      }

      if (!this.seenTitleScreen && var1.gui.screen() instanceof TitleScreen) {
         this.seenTitleScreen = (boolean)1;
      }

      if (this.seenTitleScreen && var1.gui.screen() instanceof TitleScreen) {
      }

      this.moduleKeyTracker.reset();
      long var13 = var1.getWindow().handle();

      for (Module var5 : this.moduleManager.getVisibleModules()) {
         int var6 = var5.getKeyBind();
         if (BindCodes.isValidBind(var6)) {
            this.moduleKeyTracker.update(var5, var6, BindCodes.isPressed(var13, var6));
         }
      }

      List var14 = this.moduleKeyTracker.poll();
      this.settingShortcutTracker.reset();

      for (SettingShortcuts.ShortcutEntry var17 : this.configManager.getPreferences().settingBinds.getEntries(this.moduleManager.getVisibleModules())) {
         this.settingShortcutTracker.update(var17, var17.code(), BindCodes.isPressed(var13, var17.code()));
      }

      List var16 = this.settingShortcutTracker.poll();
      int var18 = var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() && !WorldMenuModule.isOccluded() ? 1 : 0;
      boolean var7 = this.openGuiKey.consumeClick();
      byte var8 = 0;

      while (this.openEditorKey.consumeClick()) {
         var8 = 1;
      }

      if (var1.player != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.level != null) {
            var1.gui.setScreen(new HudEditorScreen(null));
         } else {
            if (!this.appliedDefaults) {
               this.appliedDefaults = (boolean)1;
               var1.options.fovEffectScale().set(0.0);
               var1.options.bobView().set(false);
               LarpLog.info("Startup", "Applied Larp defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.gui.screen() instanceof ClickGuiScreen var9) {
                  var9.onClose();
               } else if (var1.gui.screen() instanceof LegacyClickGuiScreen var10) {
                  var10.onClose();
               } else if (!WorldMenuModule.closeWithKey()) {
                  var1.gui.setScreen(LegacyClickGuiScreen.create(null));
               }
            }

            if (var18 != 0 && !var7 && var1.gui.screen() == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.isHoldMode()) {
                     if (!var24.getGuidedSpeed()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.isHoldMode()) {
                     if (!var12.tryStartHold()) {
                        continue;
                     }
                  } else if (var21.holdsKeybind()) {
                     if (!var21.activateHeldBind()) {
                        continue;
                     }
                  } else {
                     var21.toggle();
                  }

                  NotificationManager.getInstance().notifyToggle(var21.getDisplayName(), var21.isEnabled());
               }

               for (SettingShortcuts.ShortcutEntry var22 : var16) {
                  applySettingShortcut(var22);
               }
            }
         }
      }
   }

   private static void applySettingShortcut(SettingShortcuts.ShortcutEntry var0) {
      Setting var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.isVisible()) {
         LarpLog.debug("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof BooleanSetting var3) {
               var3.toggle();
               NotificationManager.getInstance().notifyToggle(var2, var3.getValue());
            } else if (var1 instanceof ModeSetting var4) {
               var4.cycleNext();
               NotificationManager.getInstance().push(var4.getModeLabel(var4.getValue()), var2, NotificationManager.Type.INFO, 2000L);
            }

            LarpLog.debug("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            LarpLog.warn("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient getInstance() {
      return instance;
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public int getGuiKey() {
      if (this.openGuiKey == null) {
         return 344;
      }

      Key var1 = KeyMappingHelper.getBoundKeyOf(this.openGuiKey);

      return switch (var1.getType()) {
         case KEYBOARD -> KeyCodes.fromScancode(var1.getValue());
         case MOUSE -> BindCodes.toMouseBind(KeyCodes.fromMinecraftButton(var1.getValue()));
         default -> -1;
      };
   }

   public void setGuiKey(int var1) {
      if (this.openGuiKey != null && var1 >= -1 && var1 <= 348) {
         this.openGuiKey
            .setKey(
               BindCodes.isMouseBind(var1)
                  ? Type.MOUSE.getOrCreate(KeyCodes.toMinecraftButton(BindCodes.toMouseButton(var1)))
                  : Type.KEYBOARD.getOrCreate(KeyCodes.toScancode(var1))
            );
         this.openGuiKey.setDown(false);
         KeyMapping.resetMapping();

         while (this.openGuiKey.consumeClick()) {
         }

         Minecraft.getInstance().options.save();
         LarpLog.info("Input", "ClickUI shortcut changed to {}", this.openGuiKey.getTranslatedKeyMessage().getString());
      }
   }
}
