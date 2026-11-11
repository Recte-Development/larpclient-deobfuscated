package com.larpclient.module.movement;

import com.larpclient.module.misc.Module;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class RocketBooster {
   public static final double bLOCK_CHECK_DISTANCE = 4.0;
   private final Module module;
   private KeyMapping jumpKey;
   private boolean jumpHeld;
   private boolean jumpOverridden;
   private int jumpOverrideTick = Integer.MIN_VALUE;
   private RocketBooster.BoostResult lastResult = RocketBooster.BoostResult.USED;
   private int boostCount;
   private boolean keepSlot;
   private static Module hotbarOwner;
   private static Module activeModule;

   public RocketBooster(Module var1) {
      this.module = var1;
   }

   public RocketBooster boost(boolean var1) {
      this.keepSlot = var1;
      return this;
   }

   public boolean selectRocketSlot(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return false;
      }

      RocketBooster.RocketSlot var3 = findRocket(var2, var1).rocket();
      if (var3 == null) {
         return false;
      }

      if (var3.hand() == InteractionHand.OFF_HAND || var3.slot() == var2.getInventory().getSelectedSlot()) {
         return true;
      }

      if (!HotbarSwapSession.canSwap(this.module)) {
         return false;
      }

      var2.getInventory().setSelectedSlot(var3.slot());
      return true;
   }

   public static void claimHotbar(Module var0) {
      hotbarOwner = var0;
   }

   public static void releaseHotbar(Module var0) {
      if (hotbarOwner == var0) {
         hotbarOwner = null;
      }
   }

   public static boolean isHotbarClaimedByOther(Module var0) {
      Module var1 = hotbarOwner;
      return var1 != null && var1 != var0 && var1.isRuntimeActive();
   }

   public static void setActiveModule(Module var0) {
      activeModule = var0;
   }

   public static void clearActiveModule(Module var0) {
      if (activeModule == var0) {
         activeModule = null;
      }
   }

   public static boolean findRocket() {
      Module var0 = activeModule;
      return var0 != null && var0.isRuntimeActive();
   }

   public static boolean isPlainRocket(ItemStack var0) {
      if (var0 != null && var0.is(Items.FIREWORK_ROCKET)) {
         Fireworks var1 = (Fireworks)var0.get(DataComponents.FIREWORKS);
         return var1 == null || var1.explosions().isEmpty();
      } else {
         return false;
      }
   }

   public static int flightDuration(ItemStack var0) {
      Fireworks var1 = var0 == null ? null : (Fireworks)var0.get(DataComponents.FIREWORKS);
      return var1 == null ? 0 : var1.flightDuration();
   }

   public static RocketBooster.RocketSearch pickRocket(ItemStack var0, List<ItemStack> var1, int var2, int var3) {
      int var4 = var0 != null && var0.is(Items.FIREWORK_ROCKET) && !isPlainRocket(var0) ? 1 : 0;
      if (isPlainRocket(var0)) {
         return new RocketBooster.RocketSearch(new RocketBooster.RocketSlot(InteractionHand.OFF_HAND, -1, flightDuration(var0)), false);
      }

      int var5 = -1;
      int var6 = Integer.MIN_VALUE;

      for (int var7 = 0; var7 < var1.size() && var7 < 9; var7++) {
         ItemStack var8 = (ItemStack)var1.get(var7);
         if (var8 != null && var8.is(Items.FIREWORK_ROCKET) && !isPlainRocket(var8)) {
            var4 = 1;
         }

         if (isPlainRocket(var8)) {
            int var9 = flightDuration(var8);
            int var10 = (var3 > 0 ? (var9 == var3 ? 1000 : -Math.abs(var9 - var3)) : var9) * 10 + (var7 == var2 ? 1 : 0);
            if (var10 > var6) {
               var6 = var10;
               var5 = var7;
            }
         }
      }

      return var5 < 0
         ? new RocketBooster.RocketSearch(null, (boolean)var4)
         : new RocketBooster.RocketSearch(new RocketBooster.RocketSlot(InteractionHand.MAIN_HAND, var5, flightDuration((ItemStack)var1.get(var5))), false);
   }

   public static RocketBooster.RocketSearch findRocket(LocalPlayer var0, int var1) {
      Inventory var2 = var0.getInventory();
      ArrayList var3 = new ArrayList(9);

      for (int var4 = 0; var4 < 9; var4++) {
         var3.add(var2.getItem(var4));
      }

      return pickRocket(var0.getOffhandItem(), var3, var2.getSelectedSlot(), var1);
   }

   public static int countRockets(LocalPlayer var0) {
      int var1 = isPlainRocket(var0.getOffhandItem()) ? var0.getOffhandItem().getCount() : 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (isPlainRocket(var3)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static boolean isBlockAhead(LocalPlayer var0, double var1) {
      if (var1 <= 0.0) {
         return false;
      }

      Vec3 var3 = var0.getEyePosition();
      Vec3 var4 = var3.add(var0.getLookAngle().scale(var1));
      BlockHitResult var5 = var0.level().clip(new ClipContext(var3, var4, Block.COLLIDER, Fluid.NONE, var0));
      return var5.getType() == Type.BLOCK;
   }

   public boolean hasRocket() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return var1 != null && findRocket(var1, 0).rocket() != null;
   }

   public void scheduleBoost(BooleanSupplier var1, double var2, int var4, Runnable var5) {
      InteractionArbiter.queueAction(this.module, () -> this.boost(var2, var4, var5), var1);
   }

   public RocketBooster.BoostResult boost(double var1, int var3, Runnable var4) {
      Minecraft var5 = Minecraft.getInstance();
      LocalPlayer var6 = var5.player;
      if (var6 != null && var5.gameMode != null && InteractionArbiter.canModuleInteract(this.module) && !var6.isUsingItem()) {
         RocketBooster.RocketSearch var7 = findRocket(var6, var3);
         RocketBooster.RocketSlot var8 = var7.rocket();
         if (var8 == null) {
            return this.selectRocketSlot(var7.starsOnly() ? RocketBooster.BoostResult.STARS_ONLY : RocketBooster.BoostResult.NO_ROCKET);
         }

         if (isBlockAhead(var6, var1)) {
            return this.selectRocketSlot(RocketBooster.BoostResult.BLOCK_AHEAD);
         }

         Inventory var9 = var6.getInventory();
         int var10 = var9.getSelectedSlot();
         int var11 = var8.hand() == InteractionHand.MAIN_HAND && var8.slot() != var10 ? 1 : 0;
         if (var11 != 0 && !HotbarSwapSession.canSwap(this.module)) {
            return this.selectRocketSlot(RocketBooster.BoostResult.HOTBAR_IN_USE);
         }

         if (var11 != 0) {
            var9.setSelectedSlot(var8.slot());
         }

         try {
            if (var5.gameMode.useItem(var6, var8.hand()).consumesAction()) {
               SwingHelper.swingInteract(var6, var8.hand());
               this.boostCount++;
               var4.run();
            }
         } finally {
            if (var11 != 0 && !this.keepSlot && var9.getSelectedSlot() == var8.slot()) {
               var9.setSelectedSlot(var10);
            }
         }

         if (this.lastResult != RocketBooster.BoostResult.USED) {
            LarpLog.debug("ElytraControl", "{}: rockets usable again", this.module.getName());
         }

         this.lastResult = RocketBooster.BoostResult.USED;
         return RocketBooster.BoostResult.USED;
      } else {
         return this.selectRocketSlot(RocketBooster.BoostResult.BUSY);
      }
   }

   private RocketBooster.BoostResult selectRocketSlot(RocketBooster.BoostResult var1) {
      if (var1 != this.lastResult && var1 != RocketBooster.BoostResult.BUSY) {
         LarpLog.debug("ElytraControl", "{}: no boost, {}", this.module.getName(), var1.REASON);
      }

      this.lastResult = var1;
      return var1;
   }

   public RocketBooster.BoostResult getLastResult() {
      return this.lastResult;
   }

   public int releaseHotbar() {
      return this.boostCount;
   }

   public void hasRocket() {
      this.setJump(true);
   }

   public void setJump() {
      this.setJump(false);
   }

   public boolean boost() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return this.jumpOverridden && this.jumpHeld && var1 != null && var1.tickCount == this.jumpOverrideTick;
   }

   public void releaseHotbar() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (this.jumpOverridden && (var1 == null || var1.tickCount != this.jumpOverrideTick)) {
         this.restoreJump();
      }
   }

   private void setJump(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.jumpKey = var2.options.keyJump;
      this.jumpKey.setDown(var1);
      this.jumpHeld = var1;
      this.jumpOverridden = (boolean)1;
      this.jumpOverrideTick = var2.player == null ? Integer.MIN_VALUE : var2.player.tickCount;
   }

   public void restoreJump() {
      if (this.jumpOverridden && this.jumpKey != null && this.jumpKey.isDown() == this.jumpHeld) {
         this.jumpKey.setDown(PlayerInputUtil.isKeyDown(this.jumpKey));
      }

      this.jumpOverridden = (boolean)0;
      this.jumpKey = null;
   }

   public boolean startGlide() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.tryToStartFallFlying()) {
         ServerboundPlayerCommandPacket var2 = new ServerboundPlayerCommandPacket(var1, Action.START_FALL_FLYING);
         if (!NoFallModule.holdGlideStart(var2)) {
            var1.connection.send(var2);
         }

         LarpLog.debug("ElytraControl", "{}: glide started directly", this.module.getName());
         return true;
      } else {
         return false;
      }
   }

   public void reset() {
      this.restoreJump();
      this.lastResult = RocketBooster.BoostResult.USED;
      this.boostCount = 0;
   }

   public static int clearActiveModule(double var0) {
      return (int)Math.ceil(Math.max(0.0, var0) / 50.0 - 1.0E-9);
   }

   public enum BoostResult {
      USED("used"),
      NO_ROCKET("no rocket in the offhand or hotbar"),
      STARS_ONLY("only rockets with firework stars, which hurt when boosting"),
      BLOCK_AHEAD("a block is right in front of the view"),
      HOTBAR_IN_USE("another module is using the hotbar"),
      BUSY("the hands or the world are busy");

      public final String REASON;

      BoostResult(String var3) {
         this.REASON = var3;
      }
   }

   public record RocketSearch(RocketBooster.RocketSlot rocket, boolean starsOnly) {
   }

   public record RocketSlot(InteractionHand hand, int slot, int duration) {
   }
}
