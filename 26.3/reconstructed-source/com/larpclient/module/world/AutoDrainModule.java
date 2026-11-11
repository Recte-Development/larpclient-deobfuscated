package com.larpclient.module.world;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoDrainModule extends Module {
   public static final long ownWaterIgnoreMs = 120000L;
   public static final int maxRefills = 2;
   public static final long refillSkipMs = 60000L;
   private static final double aimYOffset = 0.4;
   private static final int maxTrackedEntries = 512;
   private static final Map<BlockPos, Long> ownWaterPlacedTimes = new HashMap<>();
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final BooleanSetting waterlogged = this.addSetting(
      new BooleanSetting("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false)
   );
   private final BooleanSetting ignoreOwnWater = this.addSetting(
      new BooleanSetting("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true)
   );
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private final Map<BlockPos, int[]> refillCounts = new HashMap<>();
   private final Map<BlockPos, Long> lastScoopTimes = new HashMap<>();
   private LocalPlayer player;
   private ClientLevel level;
   private BlockPos targetPos;
   private Vec3 aimPoint;
   private boolean running;
   private float savedYaw;
   private float savedPitch;
   private int cooldownTicks;
   private int scoopCount;
   private String status = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int compareCandidates(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean hasRoomForBucket(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean hasRefilledTooOften(int var0) {
      return var0 >= 2;
   }

   public static void recordWaterPlaced(Player var0, InteractionHand var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 == var2.player && var2.level != null && var0.getItemInHand(var1).is(Items.WATER_BUCKET)) {
         Vec3 var3 = var0.getEyePosition();
         Vec3 var4 = var3.add(var0.getLookAngle().scale(var0.blockInteractionRange()));
         BlockHitResult var5 = var2.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (ownWaterPlacedTimes.size() >= 512) {
               ownWaterPlacedTimes.clear();
            }

            ownWaterPlacedTimes.put(var5.getBlockPos().immutable(), var6);
            ownWaterPlacedTimes.put(var5.getBlockPos().relative(var5.getDirection()).immutable(), var6);
         }
      }
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
      LarpLog.info(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.range.getValue(),
         this.delay.getValue().intValue(),
         this.waterlogged.getValue(),
         this.ignoreOwnWater.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.running && this.player != null && this.player == getMc().player) {
         this.hotbarSwap.restore(minecraft, true);
         this.player.setYRot(this.savedYaw);
         this.player.setXRot(this.savedPitch);
      }

      this.reset();
      this.player = null;
      this.level = null;
   }

   private void reset() {
      InteractionArbiter.findAttackBlocker(this);
      this.running = (boolean)0;
      this.targetPos = null;
      this.aimPoint = null;
      this.cooldownTicks = 0;
      this.scoopCount = 0;
      this.refillCounts.clear();
      this.lastScoopTimes.clear();
   }

   private boolean isBlocked() {
      ViewController.Owner var1 = ViewController.update();
      return !InteractionArbiter.canInteract() || minecraft.gui.screen() != null || var1 != ViewController.Owner.NONE;
   }

   @Override
   public void onTick() {
      if (this.player != minecraft.player || this.level != minecraft.level) {
         this.hotbarSwap.restore(getMc(), false);
         this.reset();
         ownWaterPlacedTimes.clear();
         this.player = minecraft.player;
         this.level = minecraft.level;
      }

      if (this.player != null && this.level != null) {
         if (this.isBlocked()) {
            if (this.running) {
               this.status = "Paused";
            }
         } else {
            this.pruneCaches(System.currentTimeMillis());
            if (this.cooldownTicks > 0) {
               byte var7 = 1;
               int var6 = this.cooldownTicks;
               this.cooldownTicks = var6 - var7;
            } else {
               int var1 = NativePlacement.findItemSlot(Items.BUCKET, true);
               byte var9 = -1;
               int var8 = var1;
               int var10000 = Integer.compare(var8, var9);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.endRun("No empty buckets");
               } else {
                  byte var5 = -2;
                  int var4 = var1;
                  var10000 = Integer.compare(var4, var5);
                  ItemStack var2 = (var10000 | -var10000) >>> 31 == 0 ? this.player.getOffhandItem() : this.player.getInventory().getItem(var1);
                  var10000 = var2.getCount();
                  int var10001 = this.player.getInventory().getFreeSlot();
                  byte var11 = -1;
                  int var10 = var10001;
                  var10001 = Integer.compare(var10, var11);
                  if (!hasRoomForBucket(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.endRun("Inventory full");
                  } else if (!this.findTarget()) {
                     this.endRun(this.scoopCount > 0 ? "Drained " + this.scoopCount : "No water in reach");
                  } else {
                     if (!this.running) {
                        this.running = (boolean)1;
                        this.scoopCount = 0;
                        this.savedYaw = this.player.getYRot();
                        this.savedPitch = this.player.getXRot();
                        LarpLog.debug("AutoDrain", "Run started at {}", this.targetPos);
                     }

                     this.status = "Draining";
                     float[] var3 = RotationManager.calculateAngles(this.player.getEyePosition(), this.aimPoint);
                     RotationManager.requestForModule(this, RotationManager.Priority.DRAIN, var3[0], var3[1], 1.0);
                     InteractionArbiter.queueAction(this, this::scoop, this::canScoop);
                  }
               }
            }
         }
      }
   }

   private boolean findTarget() {
      double var1 = Math.min(this.range.getValue(), this.player.blockInteractionRange());
      Vec3 var3 = this.player.getEyePosition();
      int var4 = (int)Math.ceil(var1);
      BlockPos var5 = BlockPos.containing(var3);
      long var6 = System.currentTimeMillis();
      BlockPos var8 = null;
      Vec3 var9 = null;
      double var10 = 0.0;

      for (BlockPos var13 : BlockPos.betweenClosed(var5.offset(-var4, -var4, -var4), var5.offset(var4, var4, var4))) {
         if (this.isDrainable(var13, var6)) {
            Vec3 var14 = Vec3.atBottomCenterOf(var13).add(0.0, 0.4, 0.0);
            double var15 = var3.distanceTo(var14);
            if (!(var15 > var1)
               && (var8 == null || compareCandidates(var15, var13.getY(), var10, var8.getY()) < 0)
               && this.hasLineToWater(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.immutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.targetPos = var8;
      this.aimPoint = var9;
      return var8 != null;
   }

   private boolean isDrainable(BlockPos var1, long var2) {
      if (!this.level.getFluidState(var1).isSourceOfType(Fluids.WATER)) {
         return false;
      }

      BlockState var4 = this.level.getBlockState(var1);
      if (!(var4.getBlock() instanceof BucketPickup)) {
         return false;
      }

      if (!(var4.getBlock() instanceof LiquidBlock) && !this.waterlogged.getValue()) {
         return false;
      }

      if (this.player.getBoundingBox().intersects(new AABB(var1))) {
         return false;
      }

      if (this.ignoreOwnWater.getValue()) {
         Long var5 = ownWaterPlacedTimes.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.refillCounts.get(var1);
      return var6 == null || !hasRefilledTooOften(var6[0]);
   }

   private boolean hasLineToWater(BlockPos var1, Vec3 var2, Vec3 var3, double var4) {
      Vec3 var6 = var2.add(var3.normalize().scale(var4));
      BlockHitResult var7 = this.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.SOURCE_ONLY, this.player));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean canScoop() {
      if (this.running && this.targetPos != null && !this.player.isUsingItem() && this.canUseBucket()) {
         if (!this.level.getFluidState(this.targetPos).isSourceOfType(Fluids.WATER)) {
            return false;
         }

         double var1 = Math.min(this.range.getValue(), this.player.blockInteractionRange());
         return this.hasLineToWater(this.targetPos, this.player.getEyePosition(), this.player.getLookAngle(), var1);
      } else {
         return false;
      }
   }

   private boolean canUseBucket() {
      int var1 = NativePlacement.findItemSlot(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.player.getInventory().getSelectedSlot() || HotbarSwapSession.canSwap(this));
   }

   private InteractionHand getBucketHand() {
      int var1 = NativePlacement.findItemSlot(Items.BUCKET, true);
      if (var1 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.player.getInventory().getSelectedSlot() && !this.hotbarSwap.swapTo(this.player, var1) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private void scoop() {
      if (this.canScoop()) {
         InteractionHand var1 = this.getBucketHand();
         if (var1 != null) {
            BlockPos var2 = this.targetPos;
            boolean var3 = minecraft.gameMode.useItem(this.player, var1).consumesAction();
            this.cooldownTicks = this.delay.getValue().intValue();
            if (!var3) {
               this.status = "Scoop rejected";
               LarpLog.debug("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               SwingHelper.swingInteract(this.player, var1);
               ((MinecraftMixin_3)minecraft).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.refillCounts.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.lastScoopTimes.put(var2, var4);
               this.scoopCount++;
               if (hasRefilledTooOften(var6[0])) {
                  LarpLog.debug("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               LarpLog.debug("AutoDrain", "Scooped {} ({} this run)", var2, this.scoopCount);
            }
         }
      }
   }

   private void endRun(String var1) {
      this.status = var1;
      if (this.running) {
         int var2 = this.player.getInventory().getSelectedSlot();
         this.hotbarSwap.restore(minecraft, true);
         if (minecraft.gameMode != null && var2 != this.player.getInventory().getSelectedSlot()) {
            ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
         }

         RotationManager.requestForModule(this, RotationManager.Priority.DRAIN, this.savedYaw, this.savedPitch, 1.0);
         LarpLog.debug("AutoDrain", "Run ended after {} scoops: {}", this.scoopCount, var1);
         this.running = (boolean)0;
         this.targetPos = null;
         this.aimPoint = null;
      }
   }

   private void pruneCaches(long var1) {
      ownWaterPlacedTimes.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.lastScoopTimes.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.refillCounts.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.refillCounts.size() > 512) {
         this.refillCounts.clear();
         this.lastScoopTimes.clear();
      }
   }
}
