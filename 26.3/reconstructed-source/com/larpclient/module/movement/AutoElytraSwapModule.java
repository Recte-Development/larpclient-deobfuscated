package com.larpclient.module.movement;

import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class AutoElytraSwapModule extends Module {
   static final double teleportMinDistance = 1.2;
   static final double groundCheckDepth = 1.5;
   private static final int pearlCooldownGraceTicks = 40;
   private static final int pearlThrownGraceTicks = 5;
   private static final int equipTimeoutTicks = 10;
   private static final int glideDelayTicks = 5;
   private static final Item[] chestplates = new Item[]{
      Items.NETHERITE_CHESTPLATE,
      Items.DIAMOND_CHESTPLATE,
      Items.IRON_CHESTPLATE,
      Items.COPPER_CHESTPLATE,
      Items.GOLDEN_CHESTPLATE,
      Items.CHAINMAIL_CHESTPLATE,
      Items.LEATHER_CHESTPLATE
   };
   private final NumberSetting swapRange = this.addSetting(
      new NumberSetting("Swap Range", "How close an opponent must be, in clear view, to swap back to your chestplate", 3.0, 1.0, 6.0, 0.1)
   );
   private final BooleanSetting startGliding = this.addSetting(new BooleanSetting("Start Gliding", "Open the elytra right after equipping it", true));
   private final BooleanSetting swapOnLanding = this.addSetting(
      new BooleanSetting("Swap On Landing", "Put your chestplate back when you land without reaching anyone", true)
   );
   private final BooleanSetting hotbarOnly = this.addSetting(
      new BooleanSetting("Hotbar Only", "Only swap pieces in your hotbar and never click inventory slots, which anticheats can flag while you move", false)
   );
   private final RocketBooster rocketBooster = new RocketBooster(this);
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private LocalPlayer player;
   private ClientLevel level;
   private ItemStack pearlStack;
   private Vec3 lastPosition;
   private Vec3 lastVelocity = Vec3.ZERO;
   private AutoElytraSwapModule.State state = AutoElytraSwapModule.State.IDLE;
   private boolean pearlOnCooldown;
   private long tick;
   private long expectedCatchTick = -1L;
   private long equipDeadline;
   private long elytraOnTick;
   private long glideDeadline = -1L;
   private int chestplateSlot = -1;
   private String status = "";

   public AutoElytraSwapModule() {
      super("Auto Elytra Swap", "Wear the elytra after a pearl catch and swap back to your chestplate when you can hit someone", Category.MOVEMENT, -1);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return !this.isEnabled()
         ? ""
         : (this.state == AutoElytraSwapModule.State.ELYTRA_ON ? "Elytra on" : (this.state == AutoElytraSwapModule.State.EQUIPPING ? "Equipping" : this.status));
   }

   @Override
   public void onEnable() {
      this.findSlot();
      this.player = null;
      this.level = null;
      this.status = "";
      LarpLog.info(
         "ElytraSwap",
         "Enabled; swap range={}, start gliding={}, swap on landing={}, hotbar only={}",
         this.swapRange.getValue(),
         this.startGliding.getValue(),
         this.swapOnLanding.getValue(),
         this.hotbarOnly.getValue()
      );
   }

   @Override
   public void onDisable() {
      InteractionArbiter.findAttackBlocker(this);
      if (this.hotbarSwap.isSwapped()) {
         this.hotbarSwap.restore(getMc(), true);
      }

      if (this.state != AutoElytraSwapModule.State.IDLE) {
         LarpLog.info("ElytraSwap", "Disabled during a swap; equipment left as worn");
      }

      this.findSlot();
      this.player = null;
      this.level = null;
   }

   private void findSlot() {
      this.state = AutoElytraSwapModule.State.IDLE;
      this.chestplateSlot = -1;
      this.glideDeadline = this.expectedCatchTick = -1L;
      this.lastPosition = null;
      this.lastVelocity = Vec3.ZERO;
      this.pearlOnCooldown = (boolean)0;
      RocketBooster.clearActiveModule(this);
      this.rocketBooster.reset();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         LocalPlayer var1 = minecraft.player;
         if (var1 != this.player || minecraft.level != this.level) {
            if (this.state != AutoElytraSwapModule.State.IDLE) {
               LarpLog.info("ElytraSwap", "World changed; swap state released");
            }

            this.findSlot();
            this.player = var1;
            this.level = minecraft.level;
         }

         this.tick++;
         int var10000 = RocketBooster.isHotbarClaimedByOther(this);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            if (this.state != AutoElytraSwapModule.State.IDLE) {
               LarpLog.info("ElytraSwap", "Elytra Pilot took the flight; swap state released");
               this.findSlot();
            }
         } else {
            byte var2 = this.detectTeleport(var1);
            this.trackPearl(var1);
            boolean var4 = false;
            byte var3 = var2;
            var10000 = Integer.signum(var3);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               long var12;
               var10000 = (var12 = this.tick - this.expectedCatchTick) == 0L ? 0 : (var12 < 0L ? -1 : 1);
               boolean var6 = false;
               int var5 = var10000;
               if (-Integer.signum(var5) >>> 31 == 0) {
                  this.expectedCatchTick = -1L;
                  this.equipElytra(var1);
               }
            }

            if (this.state == AutoElytraSwapModule.State.EQUIPPING) {
               this.tickEquipping();
            } else if (this.state == AutoElytraSwapModule.State.ELYTRA_ON) {
               this.tickElytraOn(var1);
            }
         }
      }
   }

   private boolean detectTeleport(LocalPlayer var1) {
      Vec3 var2 = var1.position();
      int var3 = this.lastPosition != null && isTeleport(var2.subtract(this.lastPosition), this.lastVelocity) ? 1 : 0;
      this.lastPosition = var2;
      this.lastVelocity = var1.getDeltaMovement();
      return (boolean)var3;
   }

   private void trackPearl(LocalPlayer var1) {
      if (this.pearlStack == null) {
         this.pearlStack = new ItemStack(Items.ENDER_PEARL);
      }

      boolean var2 = var1.getCooldowns().isOnCooldown(this.pearlStack);
      if (var2 && !this.pearlOnCooldown) {
         this.expectedCatchTick = Math.max(this.expectedCatchTick, this.tick + 40L);
      }

      this.pearlOnCooldown = var2;

      for (Entity var4 : minecraft.level.entitiesForRendering()) {
         if (var4 instanceof ThrownEnderpearl var5 && !var5.isRemoved() && var5.getOwner() == var1) {
            this.expectedCatchTick = Math.max(this.expectedCatchTick, this.tick + 5L);
            return;
         }
      }
   }

   private void equipElytra(LocalPlayer var1) {
      if (this.state == AutoElytraSwapModule.State.IDLE) {
         if (var1.onGround()
            || var1.isInWater()
            || var1.isInLava()
            || var1.isPassenger()
            || !minecraft.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, -1.5, 0.0))) {
            LarpLog.debug("ElytraSwap", "Pearl teleport ended near the ground or in water; no swap");
         } else if (this.isOpponentNearby(var1)) {
            LarpLog.info("ElytraSwap", "Caught next to an opponent; keeping the chestplate on");
         } else {
            this.status = "";
            if (isElytraEquipped(var1)) {
               this.chestplateSlot = -1;
               this.isOpponentNearby();
               LarpLog.info("ElytraSwap", "Caught with the elytra already on");
            } else {
               this.state = AutoElytraSwapModule.State.EQUIPPING;
               this.equipDeadline = this.tick + 10L;
               LarpLog.info("ElytraSwap", "Pearl catch seen at y {}; equipping the elytra", Math.round(var1.getY()));
            }
         }
      }
   }

   private void tickEquipping() {
      if (this.tick > this.equipDeadline) {
         this.failEquip("Could not equip the elytra in time");
      } else {
         InteractionArbiter.queueAction(this, this::equipElytra, () -> this.state == AutoElytraSwapModule.State.EQUIPPING && this.canAct());
      }
   }

   private void tickElytraOn(LocalPlayer var1) {
      if (!isElytraEquipped(var1)) {
         LarpLog.info("ElytraSwap", "Elytra was taken off by something else; swap state released");
         this.markIdle();
      } else {
         boolean var2 = this.isOpponentNearby(var1);
         int var3 = this.swapOnLanding.getValue() && var1.onGround() ? 1 : 0;
         if (!var2 && var3 == 0) {
            if (this.startGliding.getValue()
               && this.tick > this.elytraOnTick
               && this.tick <= this.glideDeadline
               && !var1.isFallFlying()
               && !var1.onGround()
               && InteractionArbiter.canInteract()
               && this.rocketBooster.startGlide()) {
               this.glideDeadline = -1L;
            }
         } else if (this.findChestplateSlot() < 0) {
            this.status = "No chestplate";
         } else {
            InteractionArbiter.queueAction(
               this,
               () -> this.detectTeleport(var2 ? "opponent in range" : "landed"),
               () -> this.state == AutoElytraSwapModule.State.ELYTRA_ON && this.canAct()
            );
         }
      }
   }

   private void isOpponentNearby() {
      this.state = AutoElytraSwapModule.State.ELYTRA_ON;
      this.elytraOnTick = this.tick;
      this.glideDeadline = this.tick + 5L;
      RocketBooster.setActiveModule(this);
   }

   private void markIdle() {
      this.state = AutoElytraSwapModule.State.IDLE;
      this.chestplateSlot = -1;
      this.glideDeadline = -1L;
      RocketBooster.clearActiveModule(this);
   }

   private void failEquip(String var1) {
      this.markIdle();
      this.status = var1;
      LarpLog.info("ElytraSwap", "Elytra not equipped: {}", var1);
   }

   private void equipElytra() {
      int var1 = this.findSlot(AutoElytraSwapModule::elytraDurability);
      if (var1 < 0) {
         this.failEquip(this.hotbarOnly.getValue() ? "No elytra in hotbar" : "No elytra");
      } else {
         LocalPlayer var2 = minecraft.player;
         int var3 = chestplateRank(var2.getItemBySlot(EquipmentSlot.CHEST)) > 0 ? 1 : 0;
         if (this.swapFromSlot(var1) && isElytraEquipped(var2)) {
            this.chestplateSlot = var3 != 0 ? var1 : -1;
            this.isOpponentNearby();
            LarpLog.info("ElytraSwap", "Elytra equipped from inventory slot {}; chestplate kept there: {}", var1, Boolean.valueOf((boolean)var3));
         }
      }
   }

   private void detectTeleport(String var1) {
      int var2 = this.findChestplateSlot();
      if (var2 >= 0) {
         LocalPlayer var3 = minecraft.player;
         if (this.swapFromSlot(var2) && !isElytraEquipped(var3)) {
            this.markIdle();
            LarpLog.info("ElytraSwap", "Chestplate back on from inventory slot {}: {}", var2, var1);
         }
      }
   }

   private int findChestplateSlot() {
      return this.chestplateSlot >= 0 && chestplateRank(minecraft.player.getInventory().getItem(this.chestplateSlot)) > 0
         ? this.chestplateSlot
         : this.findSlot(AutoElytraSwapModule::chestplateRank);
   }

   private int findSlot(ToIntFunction<ItemStack> var1) {
      int var2 = this.findBestSlotInRange(var1, 0, 9);
      return var2 < 0 && !this.hotbarOnly.getValue() ? this.findBestSlotInRange(var1, 9, 36) : var2;
   }

   private int findBestSlotInRange(ToIntFunction<ItemStack> var1, int var2, int var3) {
      Inventory var4 = minecraft.player.getInventory();
      int var5 = -1;
      int var6 = 0;

      for (int var7 = var2; var7 < var3; var7++) {
         int var8 = var1.applyAsInt(var4.getItem(var7));
         if (var8 > var6) {
            var6 = var8;
            var5 = var7;
         }
      }

      return var5;
   }

   private boolean swapFromSlot(int var1) {
      if (var1 < 9) {
         return this.useItemInSlot(var1);
      }

      if (!this.isInventoryClear()) {
         return false;
      }

      ElytraMotionModule.swapWithSlot(var1);
      return true;
   }

   private boolean useItemInSlot(int var1) {
      LocalPlayer var2 = minecraft.player;
      if (var1 != var2.getInventory().getSelectedSlot() && !this.hotbarSwap.swapTo(var2, var1)) {
         return false;
      }

      try {
         boolean var3 = minecraft.gameMode.useItem(var2, InteractionHand.MAIN_HAND).consumesAction();
         if (var3) {
            SwingHelper.swingInteract(var2, InteractionHand.MAIN_HAND);
         }

         return var3;
      } finally {
         if (this.hotbarSwap.isSwapped()) {
            int var7 = var2.getInventory().getSelectedSlot();
            this.hotbarSwap.restore(getMc(), true);
            if (var7 != var2.getInventory().getSelectedSlot()) {
               ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
            }
         }
      }
   }

   private boolean canAct() {
      return this.isRuntimeActive()
         && this.isInGame()
         && minecraft.player == this.player
         && InteractionArbiter.canInteract()
         && !minecraft.player.isUsingItem()
         && !AuraModule.isShieldUnpressActive();
   }

   private boolean isInventoryClear() {
      return minecraft.player.containerMenu == minecraft.player.inventoryMenu && minecraft.player.inventoryMenu.getCarried().isEmpty();
   }

   private static boolean isElytraEquipped(LocalPlayer var0) {
      return var0.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
   }

   private boolean isOpponentNearby(LocalPlayer var1) {
      LivingEntity var2 = ElytraHelperModule.getAuraTarget();
      if (var2 != null && this.trackPearl(var1, var2)) {
         return true;
      }

      for (AbstractClientPlayer var4 : minecraft.level.players()) {
         if (var4 != var1 && CombatUtil.isValidTarget(var1, var4) && this.trackPearl(var1, var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean trackPearl(LocalPlayer var1, LivingEntity var2) {
      return isWithinRange(var1.getEyePosition(), var1.getDeltaMovement(), var2.getBoundingBox(), this.swapRange.getValue()) && var1.hasLineOfSight(var2);
   }

   static boolean isTeleport(Vec3 var0, Vec3 var1) {
      return var0.length() > 1.2 && var0.subtract(var1).length() > 1.2;
   }

   static boolean isWithinRange(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      return Math.min(BoxRaycastUtil.distanceToBox(var0, var2), BoxRaycastUtil.distanceToBox(var0.add(var1), var2)) <= var3;
   }

   static int chestplateRank(ItemStack var0) {
      for (int var1 = 0; var1 < chestplates.length; var1++) {
         if (var0.is(chestplates[var1])) {
            return chestplates.length - var1;
         }
      }

      return 0;
   }

   static int elytraDurability(ItemStack var0) {
      return var0.is(Items.ELYTRA) && !var0.nextDamageWillBreak() ? var0.getMaxDamage() - var0.getDamageValue() : 0;
   }

   private enum State {
      IDLE,
      EQUIPPING,
      ELYTRA_ON;
   }
}
