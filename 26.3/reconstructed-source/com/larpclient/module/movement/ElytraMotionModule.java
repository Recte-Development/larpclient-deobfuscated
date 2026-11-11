package com.larpclient.module.movement;

import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class ElytraMotionModule extends Module {
   private static ElytraMotionModule instance;
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final BooleanSetting swapChestplate = this.addSetting(
      new BooleanSetting("Swap Chestplate", "Equip a chestplate while holding near the target", true)
   );
   private final BooleanSetting useFirework = this.addSetting(new BooleanSetting("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final BooleanSetting legit = this.addSetting(new BooleanSetting("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private LocalPlayer player;
   private LocalPlayer swapPlayer;
   private ClientLevel level;
   private ClientLevel swapLevel;
   private int swapSlot = -1;
   private ItemStack originalChestStack = ItemStack.EMPTY;
   private ItemStack swappedChestStack = ItemStack.EMPTY;
   private boolean holding;
   private boolean resumingFlight;
   private final RocketBooster rocketBooster = new RocketBooster(this);

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      instance = this;
      this.useFirework.visibleWhen(this.swapChestplate::getValue);
      this.legit.visibleWhen(() -> false);
   }

   public static ElytraMotionModule getInstance() {
      return instance;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.LATE;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   private boolean isNearTarget() {
      LivingEntity var1 = ElytraHelperModule.getAuraTarget();
      return var1 != null && minecraft.player.getEyePosition().distanceTo(var1.position()) < this.distance.getValue();
   }

   public boolean isHolding() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && !RocketBooster.isHotbarClaimedByOther(this)
            && this.isContextValid()
            && PlayerInputUtil.isInWorld()
            && !minecraft.player.isPassenger()
            && this.isNearTarget()
            && (this.swapSlot >= 0 && this.isSwapContextValid() || minecraft.player.isFallFlying());
   }

   @Override
   public void onEnable() {
      this.holding = (boolean)0;
      this.resumingFlight = (boolean)0;
      this.clearSwap();
      this.player = minecraft.player;
      this.level = minecraft.level;
   }

   private boolean isContextValid() {
      return this.isInGame() && minecraft.player == this.player && minecraft.level == this.level;
   }

   private boolean isSwapContextValid() {
      return this.isInGame() && minecraft.player == this.swapPlayer && minecraft.level == this.swapLevel;
   }

   @Override
   public void onDisable() {
      InteractionArbiter.findAttackBlocker(this);
      if (this.isSwapIntact() && this.canSwapEquipment()) {
         this.restoreElytra();
      } else if (this.swapSlot >= 0) {
         LarpLog.warn("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.holding = (boolean)0;
      this.resumingFlight = (boolean)0;
      this.clearSwap();
      this.player = null;
      this.level = null;
   }

   @Override
   public void onTick() {
      if (!this.isContextValid()) {
         InteractionArbiter.findAttackBlocker(this);
         this.holding = (boolean)0;
         this.resumingFlight = (boolean)0;
         this.clearSwap();
      } else if (PlayerInputUtil.isInWorld()) {
         if (this.swapSlot >= 0 && !this.isSwapIntact()) {
            this.clearSwap();
            this.holding = (boolean)0;
            LarpLog.debug("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.isHolding();
         if (var1) {
            minecraft.player.setDeltaMovement(Vec3.ZERO);
            boolean var5 = false;
            byte var4 = this.holding;
            int var10 = Integer.signum(var4);
            if ((var10 | -var10) >>> 31 == 0) {
               LarpLog.debug("ElytraMotion", "Holding near Aura target");
            }

            this.holding = (boolean)1;
            if (this.swapChestplate.getValue()) {
               boolean var7 = false;
               int var6 = this.swapSlot;
               if ((Integer.signum(var6) >>> 31 ^ 1) == 0 && minecraft.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && this.canQueueSwap()) {
                  InteractionArbiter.queueAction(
                     this,
                     this::equipChestplate,
                     () -> this.isHolding()
                        && this.swapChestplate.getValue()
                        && this.canQueueSwap()
                        && minecraft.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                  );
               }
            }
         } else {
            boolean var9 = false;
            byte var8 = this.holding;
            int var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.holding = (boolean)0;
               this.resumingFlight = (boolean)(this.swapSlot >= 0 ? 1 : 0);
               LarpLog.debug("ElytraMotion", "Target left hold range; restoring flight");
            }

            if (this.swapSlot >= 0 && this.canQueueSwap()) {
               InteractionArbiter.queueAction(this, this::restoreElytra, () -> !this.isHolding() && this.isSwapIntact() && this.canQueueSwap());
            } else if (this.resumingFlight) {
               boolean var3 = false;
               int var2 = this.swapSlot;
               if ((Integer.signum(var2) >>> 31 ^ 1) == 0) {
                  if (!this.equipChestplate()) {
                     this.resumingFlight = (boolean)0;
                     return;
                  }

                  InteractionArbiter.queueAction(this, () -> {
                     if (this.equipChestplate()) {
                        this.rocketBooster.startGlide();
                        if (this.useFirework.getValue()) {
                           this.rocketBooster.boost(4.0, 0, () -> {});
                        }

                        this.resumingFlight = (boolean)0;
                        LarpLog.debug("ElytraMotion", "Flight restored; firework={}", this.useFirework.getValue());
                     }
                  }, this::equipChestplate);
               }
            }
         }
      }
   }

   private boolean equipChestplate() {
      AirStuckModule var1 = AirStuckModule.getInstance();
      return this.isContextValid()
         && PlayerInputUtil.isInWorld()
         && InteractionArbiter.canInteract()
         && !minecraft.player.getAbilities().flying
         && !minecraft.player.onGround()
         && !minecraft.player.isInWater()
         && !minecraft.player.isInLava()
         && !minecraft.player.isPassenger()
         && !this.isNearTarget()
         && minecraft.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && (var1 == null || !var1.isHolding());
   }

   private boolean canQueueSwap() {
      return this.canSwapEquipment() && InteractionArbiter.canInteract();
   }

   private boolean canSwapEquipment() {
      return this.isContextValid()
         && minecraft.gameMode != null
         && minecraft.player.isAlive()
         && !minecraft.player.isUsingItem()
         && minecraft.player.containerMenu == minecraft.player.inventoryMenu
         && minecraft.player.inventoryMenu.getCarried().isEmpty();
   }

   private void equipChestplate() {
      int var1 = this.findChestplateSlot();
      if (var1 >= 0) {
         ItemStack var2 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST).copy();
         ItemStack var3 = minecraft.player.inventoryMenu.getSlot(var1).getItem().copy();
         swapWithSlot(var1);
         if (ItemStack.isSameItemSameComponents(minecraft.player.getItemBySlot(EquipmentSlot.CHEST), var3)
            && ItemStack.isSameItemSameComponents(minecraft.player.inventoryMenu.getSlot(var1).getItem(), var2)) {
            this.swapSlot = var1;
            this.originalChestStack = var2;
            this.swappedChestStack = var3;
            this.swapPlayer = minecraft.player;
            this.swapLevel = minecraft.level;
            LarpLog.debug("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            LarpLog.warn("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int findChestplateSlot() {
      Item[] var1 = new Item[]{
         Items.NETHERITE_CHESTPLATE,
         Items.DIAMOND_CHESTPLATE,
         Items.IRON_CHESTPLATE,
         Items.GOLDEN_CHESTPLATE,
         Items.CHAINMAIL_CHESTPLATE,
         Items.LEATHER_CHESTPLATE
      };

      for (int var2 = 9; var2 < 45; var2++) {
         for (Item var6 : var1) {
            if (minecraft.player.inventoryMenu.getSlot(var2).getItem().is(var6)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private boolean isSwapIntact() {
      return this.swapSlot >= 0
         && this.isSwapContextValid()
         && stacksMatchIgnoringDamage(minecraft.player.getItemBySlot(EquipmentSlot.CHEST), this.swappedChestStack)
         && stacksMatchIgnoringDamage(minecraft.player.inventoryMenu.getSlot(this.swapSlot).getItem(), this.originalChestStack);
   }

   private static boolean stacksMatchIgnoringDamage(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamageValue(var1.getDamageValue());
      return ItemStack.matches(var2, var1);
   }

   private void restoreElytra() {
      if (this.isSwapIntact() && this.canSwapEquipment()) {
         swapWithSlot(this.swapSlot);
         if (minecraft.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            LarpLog.debug("ElytraMotion", "Restored owned elytra");
            this.clearSwap();
         } else {
            LarpLog.warn("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   static void swapWithSlot(int var0) {
      Minecraft var1 = getMc();
      int var2 = var1.player.inventoryMenu.containerId;
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, 6, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
   }

   private void clearSwap() {
      this.swapSlot = -1;
      this.originalChestStack = ItemStack.EMPTY;
      this.swappedChestStack = ItemStack.EMPTY;
      this.swapPlayer = null;
      this.swapLevel = null;
   }

   @Override
   public String getStatus() {
      return this.holding ? "Holding" : (this.resumingFlight ? "Resume" : "");
   }
}
