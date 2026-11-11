package com.larpclient.module.combat;

import com.larpclient.qx$j$Phase;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.KeyMappingMixin;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.Marker;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.inventory.SwapLogic;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AutoTotemModule extends Module {
   private static AutoTotemModule instance;
   private Object lastSwapPlayer;
   private Object lastSwapLevel;
   private long lastSwapTime = Long.MIN_VALUE;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final NumberSetting healthThreshold = this.addSetting(
      new NumberSetting("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5)
   );
   private final BooleanSetting includeAbsorption = this.addSetting(
      new BooleanSetting("Include Absorption", "Count absorption hearts when comparing health", false)
   );
   private final ModeSetting swapMode = this.addSetting(new ModeSetting("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final ModeSetting swapStyle = this.addSetting(
      new ModeSetting(
         "Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant"
      )
   );
   private final NumberSetting clickDelayMin = this.addSetting(
      new NumberSetting("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0)
   );
   private final NumberSetting clickDelayMax = this.addSetting(
      new NumberSetting("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0)
   );
   private final BooleanSetting offhandKey = this.addSetting(
      new BooleanSetting("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true)
   );
   private final BooleanSetting openInventory = this.addSetting(
      new BooleanSetting("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false)
   );
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final NumberSetting randomDelay = this.addSetting(
      new NumberSetting("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0)
   );
   private final BooleanSetting hotbarRefill = this.addSetting(
      new BooleanSetting("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false)
   );
   private final NumberSetting hotbarReserves = this.addSetting(
      new NumberSetting("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0)
   );
   private final BooleanSetting inInventory = this.addSetting(
      new BooleanSetting("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true)
   );
   private final SwapLogic.ActionCooldown cooldown = new SwapLogic.ActionCooldown();
   private final SwapLogic.SwapSequence inventoryVisit = new SwapLogic.SwapSequence();
   private final SwapLogic.SlotSwitchTracker swapHandsRoute = new SwapLogic.SlotSwitchTracker();
   private LocalPlayer sessionPlayer;
   private Object sessionLevel;
   private Screen openedScreen;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      instance = this;
      this.healthThreshold.visibleWhen(() -> this.mode.isMode("Health"));
      this.includeAbsorption.visibleWhen(() -> this.mode.isMode("Health"));
      this.hotbarReserves.visibleWhen(this.hotbarRefill::getValue);
      this.clickDelayMin.linkUpperBound(this.clickDelayMax).setDisplayName("Click Delay");
      this.clickDelayMin.visibleWhen(() -> this.swapStyle.isMode("Legit"));
      this.clickDelayMax.visibleWhen(() -> this.swapStyle.isMode("Legit"));
      this.offhandKey.visibleWhen(() -> this.swapStyle.isMode("Legit"));
      this.openInventory.visibleWhen(() -> this.swapStyle.isMode("Legit"));
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      this.cooldown.reset();
      this.lastSwapPlayer = this.lastSwapLevel = null;
      this.reset();
   }

   @Override
   public void onDisable() {
      this.abort("module disabled", true);
      this.cooldown.reset();
      this.lastSwapPlayer = this.lastSwapLevel = null;
   }

   public static boolean isSwapping(LocalPlayer var0) {
      if (instance != null && instance.isEnabled()) {
         AutoTotemModule var1 = instance;
         return var1 != null && var0 != null && var1.inventoryVisit.isActive() && var1.sessionPlayer == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean hasPendingSwap() {
      return hasPendingSwapFor(false);
   }

   public static boolean needsHotbarSwap() {
      return hasPendingSwapFor(true);
   }

   private static boolean hasPendingSwapFor(boolean var0) {
      AutoTotemModule var1 = instance;
      if (var1 != null && var1.isRuntimeActive()) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var1.sessionPlayer != var3 || !var1.swapHandsRoute.isActive() && (!var1.inventoryVisit.isActive() || var0)) {
            if (var3 != null
               && var2.level != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !ViewController.isFreecam()
               && var3.containerMenu == var3.inventoryMenu
               && var3.inventoryMenu.getCarried().isEmpty()
               && var1.isMoving(var2.gui.screen())) {
               if (var0 && !var1.swapMode.isMode("Hotbar")) {
                  return false;
               }

               if (var1.lastSwapPlayer == var3 && var1.lastSwapLevel == var2.level && Util.getMillis() - var1.lastSwapTime < 100L) {
                  return true;
               }

               boolean var4 = SwapLogic.canAct(
                  var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                  var3.getOffhandItem().isEmpty(),
                  var1.mode.isMode("Always"),
                  var3.getHealth(),
                  var3.getAbsorptionAmount(),
                  var1.includeAbsorption.getValue(),
                  var1.healthThreshold.getValue()
               );
               long var5 = 0L;
               int var7 = 0;

               for (int var8 = 0; var8 < 36; var8++) {
                  ItemStack var9 = var3.getInventory().getItem(var8);
                  if (var9.is(Items.TOTEM_OF_UNDYING)) {
                     var5 |= 1L << var8;
                  } else if (var8 < 9 && var9.isEmpty()) {
                     var7 |= 1 << var8;
                  }
               }

               return SwapLogic.selectSwap(
                     var5, var7, var4, var1.swapMode.isMode("Hotbar"), var1.hotbarRefill.getValue(), var1.hotbarReserves.getValue().intValue()
                  )
                  != null;
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (this.sessionPlayer != null && (this.sessionPlayer != minecraft.player || this.sessionLevel != minecraft.level)) {
         LarpLog.debug("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.reset();
      }

      if (this.isInGame() && minecraft.gameMode != null) {
         int var10000 = minecraft.player.isAlive();
         boolean var7 = false;
         byte var6 = (byte)var10000;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0
            && !minecraft.player.isSpectator()
            && !ViewController.isFreecam()
            && minecraft.player.containerMenu == minecraft.player.inventoryMenu
            && minecraft.player.inventoryMenu.getCarried().isEmpty()) {
            int var16 = this.isMoving(minecraft.gui.screen());
            boolean var9 = false;
            byte var8 = (byte)var16;
            var16 = Integer.signum(var8);
            if ((var16 | -var16) >>> 31 != 0) {
               long var1 = Util.getMillis();
               int var18 = this.swapHandsRoute.isActive();
               boolean var11 = false;
               byte var10 = (byte)var18;
               var18 = Integer.signum(var10);
               if (((var18 | -var18) >>> 31 ^ 1) == 0) {
                  this.tickSwapHandsRoute(var1);
                  return;
               }

               if (this.inventoryVisit.isActive()) {
                  this.isMoving(var1);
                  return;
               }

               if (this.cooldown.clearReadyTime()) {
                  if (minecraft.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                     this.cooldown.clearHold();
                     LarpLog.debug("AutoTotem", "Server confirmed the swap hands press");
                  } else {
                     if (this.cooldown.isHolding(var1)) {
                        return;
                     }

                     this.cooldown.clearHold();
                     LarpLog.warn("AutoTotem", "No totem {} ms after swap hands; the server dropped it or is lagging, retrying", 750L);
                  }
               }

               byte var3 = this.needsTotem();
               if (var3 == 0) {
                  this.cooldown.clearReadyTime();
               }

               SwapLogic.SwapAction var4 = this.selectSwapAction((boolean)var3);
               if (var4 == null) {
                  this.cooldown.clearReadyTime();
                  return;
               }

               boolean var13 = false;
               byte var12 = var3;
               var18 = Integer.signum(var12);
               if (((var18 | -var18) >>> 31 ^ 1) == 0 && this.swapStyle.isMode("Legit") && this.offhandKey.getValue() && this.isNoScreenOpen()) {
                  var4 = SwapLogic.overrideWithHotbar(var4, this.hotbarTotemMask(), !this.isMoving(), minecraft.player.isUsingItem());
               }

               if (var3 != 0) {
                  int var5 = !this.cooldown.hasReadyTime() && this.randomDelay.getValue() > 0.0
                     ? ThreadLocalRandom.current().nextInt(this.randomDelay.getValue().intValue() + 1)
                     : 0;
                  if (!this.cooldown.isHolding(var1, this.delay.getValue().intValue() + var5)) {
                     return;
                  }
               } else if (!this.cooldown.isOffCooldown(var1)) {
                  return;
               }

               if (!this.canPerformSwap(var4)) {
                  this.cooldown.clearReadyTime();
                  return;
               }

               if (this.swapStyle.isMode("Legit")) {
                  this.startLegitSwap(var1, var4, (boolean)var3);
                  return;
               }

               InventoryMenu var14 = minecraft.player.inventoryMenu;
               minecraft.gameMode.handleContainerInput(var14.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, minecraft.player);
               this.lastSwapPlayer = minecraft.player;
               this.lastSwapLevel = minecraft.level;
               this.lastSwapTime = var1;
               this.cooldown.startCooldown(var1, var4.equips());
               return;
            }
         }
      }

      this.abort("context changed", true);
      this.cooldown.clearReadyTime();
   }

   private void startLegitSwap(long var1, SwapLogic.SwapAction var3, boolean var4) {
      Screen var5 = minecraft.gui.screen();
      if (var3.equips() && var3.source() < 9 && this.offhandKey.getValue() && var5 == null) {
         this.sessionPlayer = minecraft.player;
         this.sessionLevel = minecraft.level;
         int var8 = minecraft.player.getInventory().getSelectedSlot();
         this.swapHandsRoute.begin(var1, var8, var3.source(), this.randomClickDelay());
         LarpLog.debug("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var8 + 1);
         this.tickSwapHandsRoute(var1);
      } else {
         boolean var6 = var5 instanceof InventoryScreen;
         if (var4 || var6 || this.isMoving()) {
            this.sessionPlayer = minecraft.player;
            this.sessionLevel = minecraft.level;
            this.inventoryVisit.begin(var1, !var6);
            int var7 = var5 == null && this.openInventory.getValue() ? 1 : 0;
            if (var7 != 0) {
               this.openedScreen = new InventoryScreen(minecraft.player);
               minecraft.gui.setScreen(this.openedScreen);
            }

            LarpLog.debug(
               "AutoTotem",
               "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
               var4 ? "equip" : "refill",
               var3.source(),
               var7 != 0 ? "opened the inventory" : (var6 ? "player inventory already open" : "silent visit")
            );
            this.isMoving(var1);
         }
      }
   }

   private void isMoving(long var1) {
      if (this.openedScreen != null && minecraft.gui.screen() != this.openedScreen) {
         LarpLog.debug("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.inventoryVisit.getRepeatCount());
         this.openedScreen = null;
         this.inventoryVisit.reset();
         this.isMoving();
      } else if (this.inventoryVisit.isTimedOut(var1)) {
         LarpLog.warn("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.inventoryVisit.getPhase());
         this.abort("timed out", true);
      } else {
         boolean var3 = this.isMoving();
         switch (this.inventoryVisit.getPhase()) {
            case STARTED:
               if (this.inventoryVisit.arm(var1, var3, this.randomClickDelay())) {
                  LarpLog.debug("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case REPEATING:
               if (!this.inventoryVisit.canRepeat(var1, var3)) {
                  return;
               }

               SwapLogic.SwapAction var4 = this.selectSwapAction(this.needsTotem());
               if (var4 != null && this.canPerformSwap(var4)) {
                  InventoryMenu var5 = minecraft.player.inventoryMenu;
                  minecraft.gameMode.handleContainerInput(var5.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, minecraft.player);
                  this.lastSwapPlayer = minecraft.player;
                  this.lastSwapLevel = minecraft.level;
                  this.lastSwapTime = var1;
                  this.cooldown.startCooldown(var1, var4.equips());
                  this.inventoryVisit.recordRepeat(var1, this.randomClickDelay());
                  LarpLog.debug(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getStateId()
                  );
                  break;
               }

               if (this.inventoryVisit.startFinish(var1, this.randomClickDelay())) {
                  LarpLog.debug("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.inventoryVisit.getRepeatCount());
               } else {
                  LarpLog.debug("AutoTotem", "Visit ended after {} clicks", this.inventoryVisit.getRepeatCount());
                  this.isSwapping();
               }

               return;
            case FINISHING:
               if (!this.inventoryVisit.canFinish(var1, var3)) {
                  return;
               }

               this.closeInventory();
               LarpLog.debug("AutoTotem", "Closed the inventory while still; input released");
               this.isSwapping();
               break;
            default:
               this.isSwapping();
         }
      }
   }

   private void tickSwapHandsRoute(long var1) {
      LocalPlayer var3 = minecraft.player;
      Inventory var4 = var3.getInventory();
      if (this.swapHandsRoute.isTimedOut(var1)) {
         LarpLog.warn("AutoTotem", "Swap hands route timed out in stage {}", this.swapHandsRoute.getPhase());
         this.abort("timed out", true);
      } else if (this.swapHandsRoute.isDue(var1)) {
         int var5 = this.swapHandsRoute.getToSlot();
         switch (this.swapHandsRoute.getPhase()) {
            case SWITCHING:
               if (!var4.getItem(var5).is(Items.TOTEM_OF_UNDYING) || var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                  LarpLog.debug("AutoTotem", "Hotbar totem moved before selection; route cancelled");
                  this.swapHandsRoute.reset();
                  this.isMoving();
                  return;
               }

               var4.setSelectedSlot(var5);
               this.swapHandsRoute.advance(var1, this.randomClickDelay());
               LarpLog.debug("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var5 + 1);
               break;
            case HOLDING:
               if (var4.getSelectedSlot() != var5) {
                  LarpLog.debug("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.swapHandsRoute.reset();
                  this.isMoving();
                  return;
               }

               if (var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || !var4.getItem(var5).is(Items.TOTEM_OF_UNDYING)) {
                  LarpLog.debug("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
                  this.swapHandsRoute.advance(var1, this.randomClickDelay());
                  return;
               }

               if (this.isNoScreenOpen() && !CombatUtil.markPlayerTicked(minecraft.options.keySwapOffhand)) {
                  ((KeyMappingMixin)minecraft.options.keySwapOffhand).larpSetClickCount(1);
                  this.lastSwapPlayer = var3;
                  this.lastSwapLevel = minecraft.level;
                  this.lastSwapTime = var1;
                  this.cooldown.startHold(var1);
                  this.swapHandsRoute.advance(var1, this.randomClickDelay());
                  LarpLog.debug("AutoTotem", "Pressed swap hands for hotbar {}", var5 + 1);
                  break;
               }

               return;
            case RETURNING:
               if (this.swapHandsRoute.isTargetSlot(var4.getSelectedSlot())) {
                  var4.setSelectedSlot(this.swapHandsRoute.getFromSlot());
                  LarpLog.debug("AutoTotem", "Restored hotbar {}", this.swapHandsRoute.getFromSlot() + 1);
               } else {
                  LarpLog.debug("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.swapHandsRoute.reset();
               this.isMoving();
               break;
            default:
               this.swapHandsRoute.reset();
               this.isMoving();
         }
      }
   }

   private void abort(String var1, boolean var2) {
      int var3 = this.sessionPlayer != null && this.sessionPlayer == minecraft.player && this.sessionLevel == minecraft.level ? 1 : 0;
      if (this.inventoryVisit.isActive()) {
         if (var2 && var3 != 0 && this.inventoryVisit.isFinishAfterRepeats() && (this.inventoryVisit.getRepeatCount() > 0 || this.openedScreen != null)) {
            this.closeInventory();
         }

         LarpLog.debug("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.inventoryVisit.getRepeatCount());
      }

      if (this.swapHandsRoute.isActive()
         && var3 != 0
         && this.swapHandsRoute.getPhase() != qx$j$Phase.SWITCHING
         && this.swapHandsRoute.isTargetSlot(this.sessionPlayer.getInventory().getSelectedSlot())) {
         this.sessionPlayer.getInventory().setSelectedSlot(this.swapHandsRoute.getFromSlot());
         LarpLog.debug("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.swapHandsRoute.getFromSlot() + 1);
      }

      this.reset();
   }

   private void closeInventory() {
      LocalPlayer var1 = minecraft.player;
      if (var1 != null && var1 == this.sessionPlayer && var1.containerMenu == var1.inventoryMenu) {
         if (this.openedScreen != null && minecraft.gui.screen() == this.openedScreen) {
            var1.closeContainer();
         } else if (!(minecraft.gui.screen() instanceof InventoryScreen)) {
            var1.connection.send(new ServerboundContainerClosePacket(var1.inventoryMenu.containerId));
         }

         this.openedScreen = null;
      }
   }

   private void isSwapping() {
      this.inventoryVisit.reset();
      this.isMoving();
   }

   private void isMoving() {
      if (!this.inventoryVisit.isActive() && !this.swapHandsRoute.isActive()) {
         this.reset();
      }
   }

   private void reset() {
      this.inventoryVisit.reset();
      this.swapHandsRoute.reset();
      this.sessionPlayer = null;
      this.sessionLevel = null;
      this.openedScreen = null;
   }

   private boolean isMoving(Screen var1) {
      return var1 == null
         || this.openedScreen != null && var1 == this.openedScreen
         || this.inInventory.getValue() && var1 instanceof InventoryScreen
         || var1 instanceof ChatScreen
         || var1 instanceof PauseScreen
         || var1 instanceof Marker;
   }

   private boolean isNoScreenOpen() {
      return minecraft.gui.screen() == null;
   }

   private int hotbarTotemMask() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (minecraft.player.getInventory().getItem(var2).is(Items.TOTEM_OF_UNDYING)) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   private boolean isMoving() {
      LocalPlayerMixin_4 var1 = (LocalPlayerMixin_4)minecraft.player;
      Input var2 = var1.larpLastSentInput();
      return var2 != null
         && SwapLogic.shouldRun(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.shift());
   }

   private int randomClickDelay() {
      return SwapLogic.randomInRange(
         this.clickDelayMin.getValue().intValue(), this.clickDelayMax.getValue().intValue(), ThreadLocalRandom.current().nextDouble()
      );
   }

   private boolean needsTotem() {
      ItemStack var1 = minecraft.player.getOffhandItem();
      return SwapLogic.canAct(
         var1.is(Items.TOTEM_OF_UNDYING),
         var1.isEmpty(),
         this.mode.isMode("Always"),
         minecraft.player.getHealth(),
         minecraft.player.getAbsorptionAmount(),
         this.includeAbsorption.getValue(),
         this.healthThreshold.getValue()
      );
   }

   private SwapLogic.SwapAction selectSwapAction(boolean var1) {
      long var2 = 0L;
      int var4 = 0;

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = minecraft.player.getInventory().getItem(var5);
         if (var6.is(Items.TOTEM_OF_UNDYING)) {
            var2 |= 1L << var5;
         } else if (var5 < 9 && var6.isEmpty()) {
            var4 |= 1 << var5;
         }
      }

      return SwapLogic.selectSwap(var2, var4, var1, this.swapMode.isMode("Hotbar"), this.hotbarRefill.getValue(), this.hotbarReserves.getValue().intValue());
   }

   private boolean canPerformSwap(SwapLogic.SwapAction var1) {
      Slot var2 = minecraft.player.inventoryMenu.getSlot(var1.menuSlot());
      return var2.getItem().is(Items.TOTEM_OF_UNDYING)
         && var2.mayPickup(minecraft.player)
         && var2.mayPlace(var1.equips() ? minecraft.player.getOffhandItem() : ItemStack.EMPTY)
         && (var1.equips() || minecraft.player.getInventory().getItem(var1.button()).isEmpty());
   }
}
