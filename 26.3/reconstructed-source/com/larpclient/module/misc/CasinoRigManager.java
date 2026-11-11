package com.larpclient.module.misc;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.core.ClientFlags;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.module.exploit.HotbarForceQueue;
import com.larpclient.module.exploit.HotbarSwapQueue2;
import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.util.data.BlackjackTracker;
import com.larpclient.util.data.CasinoGameStorage;
import com.larpclient.util.data.ForceQueue;
import com.larpclient.util.data.HigherLowerMapTracker;
import com.larpclient.util.data.OddEvenMapTracker;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.inventory.HotbarSwapQueue;
import com.larpclient.util.inventory.MapSwapInventory;
import com.larpclient.util.misc.BaccaratRig;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.DispenserRegistry;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public final class CasinoRigManager {
   private static final CasinoRigManager iNSTANCE = new CasinoRigManager();
   private final List<CasinoRigManager.GameListener> listeners = new ArrayList<>();
   private CasinoRigManager.Game activeGame = CasinoRigManager.Game.NONE;
   private boolean gameActive;
   private boolean initialized;
   private long lastTickKey = Long.MIN_VALUE;
   private int[] slotMapping = CasinoGameStorage.createEmptyLayout();
   private boolean contextActive;
   private boolean paused;
   private boolean resumeAutoplay;

   private CasinoRigManager() {
   }

   public static CasinoRigManager getInstance() {
      return iNSTANCE;
   }

   public void addListener(CasinoRigManager.GameListener var1) {
      if (!this.listeners.contains(var1)) {
         this.listeners.add(var1);
      }
   }

   public synchronized void getInstance() {
      if (!this.initialized) {
         this.initialized = (boolean)1;
         CasinoGameStorage.load(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> CasinoGameStorage.save(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.level != null) {
               ArrowGame.getInstance().tick(var0);
               MapSwapInventory.update(var0);
               MapSwapInventory.syncSelectedSlot(var0);
            }
         });
         LarpLog.info("Rig", "Casino game support ready");
      }
   }

   public CasinoRigManager.Game getActiveGame() {
      return this.gameActive ? this.activeGame : CasinoRigManager.Game.NONE;
   }

   public boolean isRigActive() {
      return this.gameActive && !DispenserRegistry.getInstance().isLookingAtUnassigned();
   }

   public int[] getSlotMapping() {
      return (int[])this.slotMapping.clone();
   }

   public void setSlotMapping(int[] var1) {
      this.slotMapping = CasinoGameStorage.sanitizeLayout(var1);
   }

   public void activateGame(CasinoRigManager.Game var1) {
      if (var1 == null || var1 == CasinoRigManager.Game.NONE) {
         this.releaseGame();
      } else if (this.activeGame != var1 || !this.gameActive) {
         CasinoRigManager.Game var2 = this.getActiveGame();
         this.activeGame = var1;
         this.gameActive = (boolean)1;
         this.updateGameFlags();
         LarpLog.info("Rig", "Active game: {} (was {})", var1.DISPLAYNAME, var2.DISPLAYNAME);
         this.notifyListeners();
      }
   }

   public void releaseGame() {
      boolean var1 = this.gameActive;
      this.gameActive = (boolean)0;
      this.updateGameFlags();
      this.resetState();
      if (var1) {
         LarpLog.info("Rig", "Active game released: {}", this.activeGame.DISPLAYNAME);
      }

      this.activeGame = CasinoRigManager.Game.NONE;
   }

   public void stopAll() {
      MapSwapInventory.reset();
      this.releaseGame();
      CardForce.getInstance().clearQueue();
      HigherLowerSwapper.getInstance().clearQueue();
      FishingRodSwapper.getInstance().setAutoSwap(false);
      LarpLog.info("Rig", "All games stopped");
      this.notifyListeners();
   }

   private void notifyListeners() {
      for (CasinoRigManager.GameListener var2 : List.copyOf(this.listeners)) {
         var2.onGameChanged(this.getActiveGame());
      }
   }

   private void resetState() {
      this.paused = (boolean)0;
      this.resumeAutoplay = (boolean)0;
      MapSwapInventory.reset();
      CardForce.getInstance().clearQueue();
      HigherLowerSwapper.getInstance().clearQueue();
      OddEvenCardForce.getInstance().clearQueue();
      HotbarForceQueue.getInstance().clearQueue();
      HotbarSwapQueue2.getInstance().clearQueue();
      DispenserRemap.getInstance().refill = (boolean)0;
   }

   private void applyGameFlags(CasinoRigManager.Game var1) {
      FiftyFiftyDispenser.getInstance().setEnabled(var1 == CasinoRigManager.Game.FIFTY_FIFTY);
      BlackjackRigger.getInstance().setEnabled(var1 == CasinoRigManager.Game.BLACKJACK_RIG);
      DispenserMinigameSpoofer.hORSE_RACING.setEnabled(var1 == CasinoRigManager.Game.HORSE_RACING);
      DispenserMinigameSpoofer.aRMOR_GAME.setEnabled(var1 == CasinoRigManager.Game.ARMOR_GAME);
      VisualWinnerConfig.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.VISUAL_WINNER ? 1 : 0);
      DispenserSpoofer.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.SPLIT_45_45_10 ? 1 : 0);
      ClientFlags.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.BLACKJACK_ASSIST ? 1 : 0);
      OddEvenMapTracker.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.ODD_EVEN ? 1 : 0);
      HigherLowerMapTracker.getInstance().paused = (boolean)(var1 == CasinoRigManager.Game.HIGHER_LOWER ? 1 : 0);
      HotbarSwapQueue.getInstance().queueEnabled = (boolean)(var1 == CasinoRigManager.Game.BACCARAT ? 1 : 0);
      ForceQueue.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.BLACKJACK_DRAWS ? 1 : 0);
      ArrowGame.getInstance().enabled = (boolean)(var1 == CasinoRigManager.Game.ARROW_GAME ? 1 : 0);
      DispenserRemap var2 = DispenserRemap.getInstance();
      var2.enabled = (boolean)(var1 == CasinoRigManager.Game.RUSSIAN_ROULETTE ? 1 : 0);
      if (!var2.enabled) {
         var2.refill = (boolean)0;
      }
   }

   private void updateGameFlags() {
      this.applyGameFlags(this.gameActive && !this.paused ? this.activeGame : CasinoRigManager.Game.NONE);
   }

   public static boolean isBusy() {
      CasinoRigManager var0 = iNSTANCE;
      return !var0.initialized
         ? false
         : CardForce.getActive().isSwapBusy()
            || FiftyFiftyDispenser.getInstance().isActive()
            || BlackjackRigger.getInstance().isTransferActive()
            || FishingRodSwapper.getInstance().isLocked();
   }

   public static void tickIfBusy() {
      CasinoRigManager var0 = iNSTANCE;
      if (var0.initialized && isBusy()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null && var1.level != null) {
            var0.tick(var1);
         }
      }
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         long var2 = var1.level.getGameTime() * 31L + System.identityHashCode(var1.level);
         if (var2 != this.lastTickKey) {
            this.lastTickKey = var2;
            this.contextActive = (boolean)1;
            DispenserRegistry.getInstance().tick(var1);
            this.updatePause();
            FishingRodSwapper.getInstance().tick(var1);
            this.tickGames(var1);
         }
      } else {
         this.clearContext();
      }
   }

   public void clearContext() {
      if (this.contextActive) {
         this.contextActive = (boolean)0;
         this.lastTickKey = Long.MIN_VALUE;
         this.resetState();
         MapSwapInventory.reset();
         CardForce.getInstance().clearQueue();
         VisualWinnerConfig.getInstance().tick(null);
         LarpLog.info("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void updatePause() {
      boolean var1 = DispenserRegistry.getInstance().isLookingAtUnassigned();
      if (var1 && this.gameActive && !this.paused) {
         this.paused = (boolean)1;
         this.resumeAutoplay = FishingRodSwapper.getInstance().autoSwapEnabled;
         FishingRodSwapper.getInstance().setAutoSwap(false);
         MapSwapInventory.reset();
         CardForce.getInstance().clearQueue();
         HigherLowerSwapper.getInstance().clearQueue();
         this.updateGameFlags();
         LarpLog.info("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.paused) {
         this.paused = (boolean)0;
         this.updateGameFlags();
         if (this.resumeAutoplay) {
            FishingRodSwapper.getInstance().setAutoSwap(true);
         }

         this.resumeAutoplay = (boolean)0;
         LarpLog.info("Rig", "Games resumed");
      }
   }

   private void tickGames(Minecraft var1) {
      DispenserMinigameSpoofer.hORSE_RACING.onDispenserOpened(var1);
      DispenserMinigameSpoofer.aRMOR_GAME.onDispenserOpened(var1);
      VisualWinnerConfig.getInstance().tick(var1);
      DispenserSpoofer.getInstance().updateActive(var1);
      DispenserRemap.getInstance().tick(var1);
      DispenserRegistry var2 = DispenserRegistry.getInstance();
      int var3 = this.isRigActive() && ClientFlags.getInstance().enabled ? 1 : 0;
      int var4 = this.isRigActive() && BlackjackRigger.getInstance().isEnabled() ? 1 : 0;
      CardForce var5 = CardForce.getInstance();
      BlackjackAssistant.getInstance().setAutoArm((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      BlackjackAssistant.getInstance()
         .tick(
            var1,
            var5,
            var3 != 0
               || var2.saveAssignments(DispenserRegistry.GameType.BLACKJACK)
               || var4 != 0 && var2.saveAssignments(DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY)
         );
      OddEvenMapTracker.getInstance().tick(var1);
      HigherLowerMapTracker.getInstance().tick(var1);
      FiftyFiftyDispenser.getInstance().tick(var1);
      BlackjackRigger.getInstance().tick(var1);
      HotbarSwapQueue.getInstance().tick(var1);
      BaccaratRig.getInstance()
         .tick(
            var1,
            HotbarForceQueue.getInstance(),
            HotbarSwapQueue.getInstance().queueEnabled
               || var2.saveAssignments(DispenserRegistry.GameType.BACCARAT_PLAYER)
               || var2.saveAssignments(DispenserRegistry.GameType.BACCARAT_BANKER)
         );
      ForceQueue.getInstance().tick(var1);
      BlackjackTracker.getInstance()
         .tick(var1, HotbarSwapQueue2.getInstance(), ForceQueue.getInstance().enabled || var2.saveAssignments(DispenserRegistry.GameType.MEXICAN_JACK));
   }

   public String cycleGame(CasinoRigManager.Game var1) {
      switch (var1) {
         case VISUAL_WINNER:
            VisualWinnerConfig var10 = VisualWinnerConfig.getInstance();
            this.activateGame(CasinoRigManager.Game.VISUAL_WINNER);
            var10.toggleTarget();
            break;
         case SPLIT_45_45_10:
            DispenserSpoofer var9 = DispenserSpoofer.getInstance();
            if (!var9.enabled) {
               var9.selectedSlotType = DispenserSpoofer.SlotType.FIRST;
               this.activateGame(CasinoRigManager.Game.SPLIT_45_45_10);
            } else if (var9.selectedSlotType == DispenserSpoofer.SlotType.FIRST) {
               var9.selectedSlotType = DispenserSpoofer.SlotType.SECOND;
            } else if (var9.selectedSlotType == DispenserSpoofer.SlotType.SECOND) {
               var9.selectedSlotType = DispenserSpoofer.SlotType.MIDDLE;
            } else {
               var9.selectedSlotType = DispenserSpoofer.SlotType.FIRST;
            }
            break;
         case FIFTY_FIFTY:
            FiftyFiftyDispenser var8 = FiftyFiftyDispenser.getInstance();
            if (!var8.isEnabled()) {
               var8.setChosenIndex(1);
               this.activateGame(CasinoRigManager.Game.FIFTY_FIFTY);
            } else if (var8.getChosenIndex() == 1) {
               var8.setChosenIndex(2);
            } else {
               this.releaseGame();
            }
            break;
         case BLACKJACK_ASSIST:
            this.activateGame(CasinoRigManager.Game.BLACKJACK_ASSIST);
            BlackjackAssistant.getInstance().isDealerTurn();
            break;
         case BLACKJACK_RIG:
            this.activateGame(CasinoRigManager.Game.BLACKJACK_RIG);
            break;
         case ODD_EVEN:
            OddEvenCardForce var7 = OddEvenCardForce.getInstance();
            if (!OddEvenMapTracker.getInstance().enabled) {
               var7.setParity(0);
               this.activateGame(CasinoRigManager.Game.ODD_EVEN);
            } else {
               var7.setParity();
               var7.clearQueue();
            }
            break;
         case HIGHER_LOWER:
            HigherLowerSwapper var6 = HigherLowerSwapper.getInstance();
            if (HigherLowerMapTracker.getInstance().paused && var6.getMode() != HigherLowerSwapper.Mode.OFF) {
               var6.setMode(var6.getMode() == HigherLowerSwapper.Mode.HIGHER ? "lower" : "higher");
            } else {
               var6.setMode("higher");
               this.activateGame(CasinoRigManager.Game.HIGHER_LOWER);
            }
            break;
         case BACCARAT:
            this.activateGame(CasinoRigManager.Game.BACCARAT);
            BaccaratRig var5 = BaccaratRig.getInstance();
            var5.setTarget("player".equals(var5.targetName()) ? "banker" : "player", HotbarForceQueue.getInstance());
            break;
         case BLACKJACK_DRAWS:
            this.activateGame(CasinoRigManager.Game.BLACKJACK_DRAWS);
            BlackjackTracker.getInstance().toggleTurn(HotbarSwapQueue2.getInstance());
            break;
         case RUSSIAN_ROULETTE:
            DispenserRemap var4 = DispenserRemap.getInstance();
            this.activateGame(CasinoRigManager.Game.RUSSIAN_ROULETTE);
            if (var4.refill) {
               var4.refill = (boolean)0;
            } else {
               var4.arm();
            }
            break;
         case ARROW_GAME:
            ArrowGame var3 = ArrowGame.getInstance();
            if (var3.enabled && !"off".equals(var3.getTargetName())) {
               var3.setTarget("win".equals(var3.getTargetName()) ? "lose" : "win");
            } else {
               var3.setTarget("win");
               this.activateGame(CasinoRigManager.Game.ARROW_GAME);
            }
            break;
         case HORSE_RACING:
         case ARMOR_GAME:
            int var2 = var1 == CasinoRigManager.Game.HORSE_RACING ? 1 : 0;
            this.resetState((boolean)var2, (var2 != 0 ? DispenserMinigameSpoofer.hORSE_RACING : DispenserMinigameSpoofer.aRMOR_GAME).getNextLabel());
      }

      return this.updateGameFlags(var1);
   }

   public void resetState(boolean var1, String var2) {
      DispenserMinigameSpoofer var3 = var1 ? DispenserMinigameSpoofer.hORSE_RACING : DispenserMinigameSpoofer.aRMOR_GAME;
      if ("off".equalsIgnoreCase(var2)) {
         this.releaseGame();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.slotCount()) {
               return;
            }

            this.activateGame(var1 ? CasinoRigManager.Game.HORSE_RACING : CasinoRigManager.Game.ARMOR_GAME);
            var3.setSelectedIndex(var4);
            var3.setEnabled(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String updateGameFlags(CasinoRigManager.Game var1) {
      return switch (var1) {
         case VISUAL_WINNER -> VisualWinnerConfig.getInstance().isLinked();
         case SPLIT_45_45_10 -> DispenserSpoofer.getInstance().selectedSlotType.LABEL;
         case FIFTY_FIFTY -> FiftyFiftyDispenser.getInstance().getChosenIndex() == 1 ? "First" : "Second";
         case BLACKJACK_ASSIST -> BlackjackAssistant.getInstance().pickHitCard();
         case BLACKJACK_RIG -> (BlackjackAssistant.getInstance().isAutoSide() ? "Auto: " : "") + BlackjackAssistant.getInstance().pickHitCard();
         case ODD_EVEN -> OddEvenCardForce.getInstance().parityLabel();
         case HIGHER_LOWER -> getActiveGame(HigherLowerSwapper.getInstance().getModeLabel());
         case BACCARAT -> getActiveGame(BaccaratRig.getInstance().targetName());
         case BLACKJACK_DRAWS -> getActiveGame(BlackjackTracker.getInstance().turnName()) + " draws";
         case RUSSIAN_ROULETTE -> DispenserRemap.getInstance().refill ? "Armed" : "Normal";
         case ARROW_GAME -> ArrowGame.getInstance().getTargetLabel();
         case HORSE_RACING -> DispenserMinigameSpoofer.hORSE_RACING.getSlotLabel(DispenserMinigameSpoofer.hORSE_RACING.getSelectedIndex());
         case ARMOR_GAME -> DispenserMinigameSpoofer.aRMOR_GAME.setEnabled(DispenserMinigameSpoofer.aRMOR_GAME.getSelectedIndex());
         default -> "";
      };
   }

   private static String getActiveGame(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean isCardGameActive() {
      return ClientFlags.getInstance().enabled
         || OddEvenMapTracker.getInstance().enabled
         || HigherLowerMapTracker.getInstance().paused
         || HotbarSwapQueue.getInstance().queueEnabled
         || ForceQueue.getInstance().enabled;
   }

   public CardForce getActiveController() {
      if (HigherLowerMapTracker.getInstance().paused) {
         return HigherLowerSwapper.getInstance();
      } else if (OddEvenMapTracker.getInstance().enabled) {
         return OddEvenCardForce.getInstance();
      } else if (HotbarSwapQueue.getInstance().queueEnabled) {
         return HotbarForceQueue.getInstance();
      } else {
         return ForceQueue.getInstance().enabled ? HotbarSwapQueue2.getInstance() : CardForce.getInstance();
      }
   }

   public boolean releaseGame(int var1, int[] var2) {
      return HotbarSwapQueue.getInstance().queueEnabled && var2 != null && var2.length != 0
         ? BaccaratRig.getInstance().startPreset(HotbarForceQueue.getInstance(), var2)
         : false;
   }

   public ItemStack applyVisualOverrides(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = DispenserMinigameSpoofer.getActive().spoofStack(var1);
         ItemStack var2 = CardForce.getActive().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = DispenserSpoofer.getInstance().spoofStack(var1);
         return DispenserRemap.getInstance().tick(var1);
      } else {
         return var1;
      }
   }

   public CasinoRigManager.FromHandResult setItemFromHand(CasinoRigManager.Game var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3 != null && !var3.isEmpty()) {
            Item var4 = var3.getItem();
            switch (var1) {
               case VISUAL_WINNER:
                  return new CasinoRigManager.FromHandResult(false, "Paper learns items from its assigned dispensers");
               case SPLIT_45_45_10:
                  DispenserSpoofer var6 = DispenserSpoofer.getInstance();
                  switch (var6.selectedSlotType) {
                     case FIRST:
                        var6.firstItem = var4;
                        break;
                     case SECOND:
                        var6.secondItem = var4;
                        break;
                     case MIDDLE:
                        var6.middleItem = var4;
                  }

                  this.saveConfig();
                  return new CasinoRigManager.FromHandResult(
                     true, "45/45/10: " + var6.selectedSlotType.LABEL + " item set to " + var3.getHoverName().getString()
                  );
               case FIFTY_FIFTY:
               default:
                  return new CasinoRigManager.FromHandResult(false, "From Hand is unavailable for " + var1.DISPLAYNAME);
               case BLACKJACK_ASSIST:
               case BLACKJACK_RIG:
                  return new CasinoRigManager.FromHandResult(false, "Blackjack imports from its dispenser or hotbar maps");
               case ODD_EVEN:
               case BACCARAT:
                  return new CasinoRigManager.FromHandResult(false, var1.DISPLAYNAME + " imports from its dispenser maps");
               case HIGHER_LOWER:
               case BLACKJACK_DRAWS:
                  return new CasinoRigManager.FromHandResult(false, var1.DISPLAYNAME + " imports from its assigned dispenser");
               case RUSSIAN_ROULETTE:
                  DispenserRemap var5 = DispenserRemap.getInstance();
                  if (var5.refill) {
                     var5.tntItem = var4;
                     this.saveConfig();
                     return new CasinoRigManager.FromHandResult(true, "Russian Roulette: trigger item set to " + var3.getHoverName().getString());
                  }

                  var5.paperItem = var4;
                  this.saveConfig();
                  return new CasinoRigManager.FromHandResult(
                     true, "Russian Roulette: normal item set to " + var3.getHoverName().getString() + " (arm first to set the trigger item)"
                  );
               case ARROW_GAME:
                  return new CasinoRigManager.FromHandResult(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new CasinoRigManager.FromHandResult(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new CasinoRigManager.FromHandResult(false, "From Hand failed: no player");
      }
   }

   public void saveConfig() {
      CasinoGameStorage.save(this);
   }

   public static final class FromHandResult {
      public final boolean success;
      public final String message;

      public FromHandResult(boolean var1, String var2) {
         this.success = var1;
         this.message = var2;
      }
   }

   public enum Game {
      VISUAL_WINNER("Paper"),
      SPLIT_45_45_10("45/45/10"),
      FIFTY_FIFTY("50/50"),
      BLACKJACK_ASSIST("Blackjack"),
      BLACKJACK_RIG("Blackjack Force Only"),
      ODD_EVEN("Even or Odd"),
      HIGHER_LOWER("Higher or Lower"),
      BACCARAT("Baccarat"),
      BLACKJACK_DRAWS("Mexican Jack"),
      RUSSIAN_ROULETTE("Russian Roulette"),
      ARROW_GAME("Arrow Game"),
      HORSE_RACING("Horse Racing"),
      ARMOR_GAME("Armor Game"),
      NONE("Off");

      public final String DISPLAYNAME;

      Game(String var3) {
         this.DISPLAYNAME = var3;
      }
   }

   public interface GameListener {
      void onGameChanged(CasinoRigManager.Game var1);
   }
}
