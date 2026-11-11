package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AnchorMacroModule extends Module {
   private static AnchorMacroModule instance;
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Milliseconds to wait after a slot switch before using the anchor", 0.0, 0.0, 500.0, 5.0)
   );
   private final NumberSetting chargeDelay = this.addSetting(
      new NumberSetting("Charge Delay", "Minimum milliseconds between glowstone charges", 0.0, 0.0, 500.0, 5.0)
   );
   private final NumberSetting totemSlot = this.addSetting(
      new NumberSetting("Totem Slot", "Hotbar slot to move to once the anchor is charged", 1.0, 1.0, 9.0, 1.0)
   );
   private final BooleanSetting autoExplode = this.addSetting(new BooleanSetting("Auto Explode", "Detonate the charged anchor from the totem slot", false));
   private final BooleanSetting spam = this.addSetting(
      new BooleanSetting("Spam", "Keep working on anchors while Use stays held; off needs a new press for each one", true)
   );
   private boolean engaged;
   private boolean pendingAction;
   private int switchedSlot = -1;
   private long switchTimeMs;
   private long lastChargeTimeMs;
   private String status = "Hold Use on an anchor";

   public AnchorMacroModule() {
      super("Anchor Macro", "Hold Use on an anchor to charge it and move to your totem slot", Category.COMBAT, -1);
      instance = this;
   }

   public static boolean isActive() {
      if (instance != null && instance.isEnabled()) {
         AnchorMacroModule var0 = instance;
         return var0 != null && var0.isRuntimeActive() && !var0.isAnchorTapActive()
            ? var0.engaged || minecraft.options.keyUse.isDown() && var0.getTargetedAnchor() != null
            : false;
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onTick() {
      NativePlacement.cancel(this);
      if (InteractionArbiter.canInteract()) {
         int var10000 = minecraft.isWindowActive();
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 != 0) {
            int var14 = this.isAnchorTapActive();
            boolean var12 = false;
            byte var11 = (byte)var14;
            var14 = Integer.signum(var11);
            if (((var14 | -var14) >>> 31 ^ 1) == 0) {
               this.reset("AnchorTap handles anchors while it is enabled");
               return;
            }

            if (!minecraft.options.keyUse.isDown()) {
               this.reset("Hold Use on an anchor");
               return;
            }

            boolean var8 = false;
            byte var7 = this.engaged;
            var14 = Integer.signum(var7);
            if (((var14 | -var14) >>> 31 ^ 1) == 0) {
               boolean var10 = false;
               byte var9 = this.pendingAction;
               var14 = Integer.signum(var9);
               if ((var14 | -var14) >>> 31 == 0 && !this.spam.getValue()) {
                  return;
               }
            }

            BlockHitResult var1 = this.getTargetedAnchor();
            if (var1 == null) {
               return;
            }

            this.engaged = this.pendingAction = (boolean)1;
            BlockPos var2 = var1.getBlockPos().immutable();
            long var3 = System.currentTimeMillis();
            if (this.getCharge(var2) == 0) {
               this.chargeAnchor(var2, var3);
            } else {
               this.detonateAnchor(var2, var3);
            }

            return;
         }
      }

      this.reset("Anchor interaction suspended");
   }

   private void chargeAnchor(BlockPos var1, long var2) {
      if (var2 - this.lastChargeTimeMs < Math.round(this.chargeDelay.getValue())) {
         this.status = "Waiting for Charge Delay";
      } else {
         int var4 = this.findGlowstoneSlot();
         if (var4 == -1) {
            this.status = "No glowstone in hotbar";
         } else if (!this.selectSlot(var4, var2)) {
            this.status = "Waiting for Delay";
         } else {
            this.interactWithAnchor(var1, Items.GLOWSTONE, "Charging anchor", () -> this.getCharge(var1) == 0, var2x -> {
               this.lastChargeTimeMs = System.currentTimeMillis();
               LarpLog.info("AnchorMacro", "Charge at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         }
      }
   }

   private void detonateAnchor(BlockPos var1, long var2) {
      if (!this.selectSlot(this.totemSlot.getValue().intValue() - 1, var2)) {
         this.status = "Waiting for Delay";
      } else if (!this.autoExplode.getValue()) {
         this.pendingAction = (boolean)0;
         this.status = "Holding totem slot";
      } else {
         ItemStack var4 = minecraft.player.getMainHandItem();
         if (!var4.is(Items.GLOWSTONE) && !minecraft.player.getOffhandItem().is(Items.GLOWSTONE)) {
            this.interactWithAnchor(var1, var4.isEmpty() ? Items.AIR : var4.getItem(), "Detonating anchor", () -> this.getCharge(var1) > 0, var2x -> {
               if (var2x) {
                  this.pendingAction = (boolean)0;
               }

               LarpLog.info("AnchorMacro", "Detonation at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         } else {
            this.status = "Glowstone in hand blocks detonation";
         }
      }
   }

   private void interactWithAnchor(BlockPos var1, Item var2, String var3, BooleanSupplier var4, Consumer<Boolean> var5) {
      boolean var6 = NativePlacement.pause(this, var1, var2, minecraft.player.blockInteractionRange(), false, false, var4, var5);
      this.status = var6 ? var3 : "Aim at the anchor within reach";
   }

   private BlockHitResult getTargetedAnchor() {
      if (InteractionArbiter.canInteract()
         && minecraft.isWindowActive()
         && !minecraft.player.isUsingItem()
         && !minecraft.player.isSecondaryUseActive()
         && minecraft.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK) {
         BlockPos var3 = var1.getBlockPos();
         return minecraft.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR)
               && !Boolean.TRUE.equals(minecraft.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var3))
            ? var1
            : null;
      } else {
         return null;
      }
   }

   private int getCharge(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private int findGlowstoneSlot() {
      Inventory var1 = minecraft.player.getInventory();
      if (minecraft.player.getMainHandItem().is(Items.GLOWSTONE)) {
         return var1.getSelectedSlot();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.getItem(var2).is(Items.GLOWSTONE)) {
            return var2;
         }
      }

      return -1;
   }

   private boolean selectSlot(int var1, long var2) {
      Inventory var4 = minecraft.player.getInventory();
      long var5 = Math.round(this.delay.getValue());
      if (var4.getSelectedSlot() != var1) {
         var4.setSelectedSlot(var1);
         this.switchedSlot = var1;
         this.switchTimeMs = var2;
         return var5 == 0L;
      } else {
         return var1 != this.switchedSlot || var2 - this.switchTimeMs >= var5;
      }
   }

   private boolean isAnchorTapActive() {
      AnchorTapModule var1 = LarpClient.getInstance().getModuleManager().getModule(AnchorTapModule.class);
      return var1 != null && var1.isRuntimeActive();
   }

   private void reset(String var1) {
      this.engaged = this.pendingAction = (boolean)0;
      this.switchedSlot = -1;
      this.switchTimeMs = 0L;
      this.status = var1;
   }

   @Override
   public void onEnable() {
      this.lastChargeTimeMs = 0L;
      this.reset("Hold Use on an anchor");
      LarpLog.info(
         "AnchorMacro",
         "Enabled; totemSlot={}, autoExplode={}, spam={}, delay={}ms, chargeDelay={}ms",
         this.totemSlot.getValue().intValue(),
         this.autoExplode.getValue(),
         this.spam.getValue(),
         this.delay.getValue().intValue(),
         this.chargeDelay.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      NativePlacement.cancel(this);
      this.reset("Hold Use on an anchor");
      LarpLog.debug("AnchorMacro", "Anchor intents cleared");
   }
}
