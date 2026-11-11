package com.larpclient.module.movement;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoMLGModule extends Module {
   public static final float lOOK_PITCH = 89.5F;
   public static final double mIN_FALL_SPEED = -0.6;
   public static final int mIN_PICKUP_TICKS = 3;
   public static final int pICKUP_TIMEOUT_TICKS = 40;
   private final NumberSetting fallDistance = this.addSetting(
      new NumberSetting("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0)
   );
   private final BooleanSetting pickUp = this.addSetting(new BooleanSetting("Pick Up", "Pick the water back up after landing", true));
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private AutoMLGModule.Phase phase = AutoMLGModule.Phase.IDLE;
   private LocalPlayer player;
   private ClientLevel level;
   private float originalPitch;
   private int pickUpTicks;
   private String status = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean shouldArm(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean canPickUp(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public void onEnable() {
      this.reset();
      this.player = minecraft.player;
      this.level = minecraft.level;
      this.status = "Ready";
      LarpLog.info("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.fallDistance.getValue().intValue(), this.pickUp.getValue());
   }

   @Override
   public void onDisable() {
      InteractionArbiter.findAttackBlocker(this);
      if (this.phase != AutoMLGModule.Phase.IDLE && this.player != null && this.player == getMc().player) {
         this.hotbarSwap.restore(minecraft, true);
         this.player.setXRot(this.originalPitch);
      }

      this.reset();
      this.player = null;
      this.level = null;
   }

   private void reset() {
      InteractionArbiter.findAttackBlocker(this);
      this.phase = AutoMLGModule.Phase.IDLE;
      this.pickUpTicks = 0;
   }

   private boolean shouldArm() {
      ViewController.Owner var1 = ViewController.update();
      return !InteractionArbiter.canInteract() || minecraft.gui.screen() != null || var1 != ViewController.Owner.NONE;
   }

   @Override
   public void onTick() {
      if (this.player != minecraft.player || this.level != minecraft.level) {
         this.hotbarSwap.restore(getMc(), false);
         this.reset();
         this.player = minecraft.player;
         this.level = minecraft.level;
      }

      int var10000 = this.shouldArm();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.phase != AutoMLGModule.Phase.IDLE) {
            this.status = "Paused";
         }
      } else {
         switch (this.phase) {
            case IDLE:
               this.tickIdle();
               break;
            case FALLING:
               this.tickFalling();
               break;
            case PICKING_UP:
               this.tickPickingUp();
               break;
            case DONE:
               this.finish();
         }
      }
   }

   private void tickIdle() {
      Abilities var1 = this.player.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.player.isFallFlying()
         && !this.player.isPassenger()
         && shouldArm(this.player.onGround(), this.player.isInWater(), this.player.fallDistance, this.player.getDeltaMovement().y, this.fallDistance.getValue())
         )
       {
         if (NativePlacement.findItemSlot(Items.WATER_BUCKET, true) == -1) {
            this.status = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.player.blockPosition())) {
            this.status = "Water evaporates here";
         } else {
            this.originalPitch = this.player.getXRot();
            this.pickUpTicks = 0;
            this.phase = AutoMLGModule.Phase.FALLING;
            this.status = "Falling";
            this.shouldArm(Items.WATER_BUCKET);
            LarpLog.debug(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.player.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.player.getDeltaMovement().y)
            );
            this.tickFalling();
         }
      }
   }

   private void tickFalling() {
      if (!this.player.onGround() && !this.player.isInWater() && !(this.player.fallDistance <= 0.0)) {
         this.requestDownwardLook();
         this.status = "Falling";
         if (this.finish(this.player.getYRot(), 89.5F) != null) {
            InteractionArbiter.queueAction(this, this::placeWater, this::pickUpWater);
         }
      } else {
         this.phase = AutoMLGModule.Phase.DONE;
         this.finish();
      }
   }

   private void tickPickingUp() {
      this.requestDownwardLook();
      this.pickUpTicks++;
      if (this.pickUpTicks > 40) {
         this.status = "Pick up timed out";
         this.phase = AutoMLGModule.Phase.DONE;
      } else if (canPickUp(this.player.onGround(), this.player.isInWater(), this.pickUpTicks)) {
         InteractionArbiter.queueAction(this, this::pickUpWater, () -> this.phase == AutoMLGModule.Phase.PICKING_UP && this.canUse(Items.BUCKET));
      }
   }

   private void finish() {
      int var1 = this.player.getInventory().getSelectedSlot();
      this.hotbarSwap.restore(minecraft, true);
      if (minecraft.gameMode != null && var1 != this.player.getInventory().getSelectedSlot()) {
         ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
      }

      RotationManager.requestForModule(this, RotationManager.Priority.MLG, this.player.getYRot(), this.originalPitch, 1.0);
      this.reset();
      this.status = "Ready";
   }

   private void requestDownwardLook() {
      RotationManager.requestForModule(this, RotationManager.Priority.MLG, this.player.getYRot(), 89.5F, 1.0);
   }

   private Vec3 finish(float var1, float var2) {
      Vec3 var3 = this.player.getEyePosition();
      Vec3 var4 = var3.add(Vec3.directionFromRotation(var2, var1).scale(this.player.blockInteractionRange()));
      BlockHitResult var5 = this.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, this.player));
      return var5.getType() == Type.BLOCK ? var5.getLocation() : null;
   }

   private boolean pickUpWater() {
      return this.phase == AutoMLGModule.Phase.FALLING
         && !this.player.onGround()
         && !this.player.isInWater()
         && Math.abs(this.player.getXRot() - 89.5F) < 0.5F
         && !this.player.isUsingItem()
         && this.canUse(Items.WATER_BUCKET)
         && this.finish(this.player.getYRot(), this.player.getXRot()) != null;
   }

   private boolean canUse(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.player.getInventory().getSelectedSlot() || HotbarSwapSession.canSwap(this));
   }

   private InteractionHand shouldArm(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      if (var2 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.player.getInventory().getSelectedSlot() && !this.hotbarSwap.swapTo(this.player, var2) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private boolean useItem(Item var1) {
      InteractionHand var2 = this.shouldArm(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = minecraft.gameMode.useItem(this.player, var2).consumesAction();
      if (var3) {
         SwingHelper.swingInteract(this.player, var2);
         ((MinecraftMixin_3)minecraft).larpUseDelay(4);
      }

      return var3;
   }

   private void placeWater() {
      if (this.pickUpWater()) {
         if (!this.useItem(Items.WATER_BUCKET)) {
            this.status = "Water placement rejected";
         } else {
            this.phase = this.pickUp.getValue() ? AutoMLGModule.Phase.PICKING_UP : AutoMLGModule.Phase.DONE;
            this.pickUpTicks = 0;
            this.status = "Water placed";
            LarpLog.debug("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.player.fallDistance));
         }
      }
   }

   private void pickUpWater() {
      boolean var1 = this.useItem(Items.BUCKET);
      this.status = var1 ? "Water picked up" : "Pick up rejected";
      this.phase = AutoMLGModule.Phase.DONE;
      LarpLog.debug("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum Phase {
      IDLE,
      FALLING,
      PICKING_UP,
      DONE;
   }
}
