package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.ExplosionDamage2;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.PendingBlockTracker;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public final class CrystalTapModule extends Module {
   private static CrystalTapModule instance;
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Extra ticks to wait once the crystal appears; 0 hits it on the first tick it exists", 0.0, 0.0, 10.0, 1.0)
   );
   private final NumberSetting randomDelay = this.addSetting(
      new NumberSetting("Random Delay", "Adds up to this many more ticks at random to each hit", 0.0, 0.0, 5.0, 1.0)
   );
   private final NumberSetting hitWindow = this.addSetting(
      new NumberSetting("Hit Window", "Ticks your crosshair has to reach a new crystal before it is left alone", 8.0, 1.0, 40.0, 1.0)
   );
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Hit reach in blocks, capped by your real reach", 3.0, 1.0, 6.0, 0.1));
   private final BooleanSetting antiSuicide = this.addSetting(new BooleanSetting("Anti Suicide", "Leave a crystal alone when its blast would kill you", true));
   private final PendingBlockTracker pendingCrystals = new PendingBlockTracker();
   private LocalPlayer trackedPlayer;
   private ClientLevel trackedLevel;
   private BlockPos pendingBase;
   private boolean wasDestroying;
   private String status = "Place a crystal";
   private String lastLoggedStatus = "";

   public CrystalTapModule() {
      super("Crystal Tap", "Hit a crystal the moment the one you placed appears", Category.COMBAT, -1);
      instance = this;
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   public static void onUseBlock(LocalPlayer var0, InteractionHand var1, BlockHitResult var2) {
      if (instance != null && instance.isEnabled()) {
         CrystalTapModule var3 = instance;
         if (var3 != null) {
            var3.pendingBase = null;
            if (var3.isRuntimeActive()
               && var0 == minecraft.player
               && !InteractionArbiter.isModuleInteracting()
               && var0.getItemInHand(var1).is(Items.END_CRYSTAL)) {
               BlockState var4 = minecraft.level.getBlockState(var2.getBlockPos());
               if (var4.is(Blocks.OBSIDIAN) || var4.is(Blocks.BEDROCK)) {
                  var3.pendingBase = var2.getBlockPos().immutable();
               }
            }
         }
      }
   }

   public static void onUseResult(InteractionResult var0) {
      if (instance != null && instance.isEnabled()) {
         CrystalTapModule var1 = instance;
         if (var1 != null && var1.pendingBase != null) {
            BlockPos var2 = var1.pendingBase;
            var1.pendingBase = null;
            if (var1.isRuntimeActive() && var0.consumesAction()) {
               int var3 = ThreadLocalRandom.current().nextInt(var1.randomDelay.getValue().intValue() + 1);
               var1.pendingCrystals.track(var2, minecraft.player.tickCount, var1.delay.getValue().intValue() + var3);
               var1.setStatus("Waiting for the crystal");
            }
         }
      }
   }

   public static boolean onUseBlock() {
      if (instance != null && instance.isEnabled()) {
         CrystalTapModule var0 = instance;
         return var0 != null
            && var0.isRuntimeActive()
            && !var0.pendingCrystals.positions()
            && var0.trackedPlayer == minecraft.player
            && var0.trackedLevel == minecraft.level
            && var0.findDueTarget() != null;
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.reset();
      } else {
         if (this.trackedPlayer != minecraft.player || this.trackedLevel != minecraft.level) {
            this.reset();
            this.trackedPlayer = minecraft.player;
            this.trackedLevel = minecraft.level;
         }

         boolean var1 = minecraft.gameMode.isDestroying();
         int var10000 = this.pendingCrystals.positions();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         CrystalTapModule.CrystalTarget var2 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? null : this.findDueTarget();
         this.wasDestroying = var1;
         if (var2 == null) {
            int var10 = this.pendingCrystals.positions();
            boolean var6 = false;
            byte var5 = (byte)var10;
            var10 = Integer.signum(var5);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               this.setStatus("Place a crystal");
            }
         } else {
            double var3 = InteractionArbiter.clampRange(this.range.getValue());
            InteractionArbiter.queueAttackWithCallbacks(
               this,
               var2.crystal(),
               var3,
               false,
               () -> this.getBlockReason(var2.crystal()) == null,
               1,
               () -> this.onCrystalHit(var2),
               var1x -> this.setStatus("Waiting: " + var1x)
            );
         }
      }
   }

   private void onCrystalHit(CrystalTapModule.CrystalTarget var1) {
      long var2 = this.pendingCrystals.ageOf(var1.base(), minecraft.player.tickCount);
      this.pendingCrystals.remove(var1.base());
      this.setStatus("Hit");
      LarpLog.debug("CrystalTap", "Hit crystal {} on {}, {} ticks after placing it", var1.crystal().getId(), var1.base().toShortString(), var2);
   }

   private CrystalTapModule.CrystalTarget findDueTarget() {
      long var1 = minecraft.player.tickCount;
      int var3 = this.hitWindow.getValue().intValue();
      String var4 = null;

      for (BlockPos var6 : this.pendingCrystals.positions()) {
         EndCrystal var7 = this.findCrystalAbove(var6);
         if (this.pendingCrystals.poll(var6, var7 != null, var1, var3) == PendingBlockTracker.PollResult.DUE) {
            String var8 = this.getBlockReason(var7);
            if (var8 == null) {
               return new CrystalTapModule.CrystalTarget(var6, var7);
            }

            if (var4 == null) {
               var4 = var8;
            }
         }
      }

      if (var4 != null) {
         this.setStatus(var4);
      } else if (!this.pendingCrystals.positions()) {
         this.setStatus("Waiting for the crystal");
      }

      return null;
   }

   private EndCrystal findCrystalAbove(BlockPos var1) {
      BlockPos var2 = var1.above();
      List var3 = minecraft.level.getEntitiesOfClass(EndCrystal.class, new AABB(var2), var1x -> !var1x.isRemoved() && var1x.blockPosition().equals(var2));
      return var3.isEmpty() ? null : (EndCrystal)var3.getFirst();
   }

   private String getBlockReason(EndCrystal var1) {
      LocalPlayer var2 = minecraft.player;
      if (!CombatUtil.isActive(minecraft)) {
         return "Paused";
      } else if (!CombatUtil.canAttack(minecraft, var2) || this.wasDestroying) {
         return "Your hands are busy";
      } else if (CombatUtil.hasTickedThisTick(var2)) {
         return "You already acted this tick";
      } else {
         ItemStack var3 = var2.getMainHandItem();
         if (var3.has(DataComponents.PIERCING_WEAPON) || !var3.isItemEnabled(minecraft.level.enabledFeatures()) || var2.cannotAttackWithItem(var3, 0)) {
            return "This item cannot hit";
         } else if (var1.isInvulnerable()) {
            return "This crystal cannot be broken";
         } else if (!CrystalOptimizerModule.canDealDamage(var2)) {
            return "Your hit deals no damage";
         } else {
            double var4 = InteractionArbiter.clampRange(this.range.getValue());
            if (BoxRaycastUtil.raycastBox(var2.getEyePosition(), var2.getLookAngle(), var1.getBoundingBox(), var4).isEmpty()) {
               return "Aim at the crystal";
            } else {
               String var6 = InteractionArbiter.findAttackBlocker(var1, var4, var2.getLookAngle());
               if (var6 != null) {
                  return "Blocked by " + var6;
               } else {
                  return this.antiSuicide.getValue()
                        && !ExplosionDamage2.isWithinRange(
                           ExplosionDamage2.damageTo(var2, var1.position()), Double.MAX_VALUE, var2.getHealth() + var2.getAbsorptionAmount()
                        )
                     ? "This crystal would kill you"
                     : null;
               }
            }
         }
      }
   }

   private void setStatus(String var1) {
      this.status = var1;
      if (!var1.equals(this.lastLoggedStatus)) {
         this.lastLoggedStatus = var1;
         LarpLog.debug("CrystalTap", "Status: {}", var1);
      }
   }

   private void reset() {
      this.pendingCrystals.clear();
      InteractionArbiter.findAttackBlocker(this);
      this.pendingBase = null;
      this.trackedPlayer = null;
      this.trackedLevel = null;
      this.wasDestroying = (boolean)0;
      this.status = "Place a crystal";
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("CrystalTap", "Enabled; hits your own placed crystals with the native attack on your real look");
   }

   @Override
   public void onDisable() {
      this.reset();
      LarpLog.debug("CrystalTap", "Disabled; tracked placements cleared");
   }

   private record CrystalTarget(BlockPos base, EndCrystal crystal) {
   }
}
