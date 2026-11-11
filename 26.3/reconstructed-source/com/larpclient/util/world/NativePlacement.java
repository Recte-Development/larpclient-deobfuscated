package com.larpclient.util.world;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.combat.AntiTrapModule;
import com.larpclient.module.combat.AutoAnchorModule;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.combat.AutoWebModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.IgnoreEntityModule;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class NativePlacement {
   private static final Map<Module, NativePlacement.PlacementIntent> intents = new LinkedHashMap<>();
   private static final Map<Module, String> failureReasons = new HashMap<>();
   private static final Map<Module, Long> lastLogNanos = new HashMap<>();
   private static final Direction[] sUPPORT_FACE_ORDER = new Direction[]{
      Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN
   };

   public static boolean isSameContext(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String getFailureReason(Module var0) {
      return failureReasons.getOrDefault(var0, "No supported face within native reach");
   }

   private static void setFailure(Module var0, String var1) {
      failureReasons.put(var0, var1);
   }

   private static void pause(Module var0, String var1) {
      setFailure(var0, var1);
      long var2 = System.nanoTime();
      long var4 = lastLogNanos.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         lastLogNanos.put(var0, var2);
         LarpLog.info(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void cancel(Module var0) {
      intents.remove(var0);
      InteractionArbiter.findAttackBlocker(var0);
   }

   public static void clear() {
      intents.clear();
      failureReasons.clear();
      lastLogNanos.clear();
   }

   public static void pruneInactive() {
      intents.values().removeIf(var0 -> !var0.source.isRuntimeActive() || !var0.source.allowsScreenInteractions());
   }

   public static void pruneUnscheduled() {
      intents.values().removeIf(var0 -> !InteractionArbiter.allowsDetachedCamera(var0.source));
   }

   private static boolean isAllowed(Module var0) {
      return var0 == null ? InteractionArbiter.canInteract() : InteractionArbiter.canModuleInteract(var0);
   }

   public static boolean setFailure(Module var0) {
      NativePlacement.PlacementIntent var1 = intents.get(var0);
      Minecraft var2 = Minecraft.getInstance();
      return var1 != null && var2.player != null && isSameContext(var1.world, var2.level, var1.player, var2.player, var1.tick, var2.player.tickCount);
   }

   public static int findItemSlot(Item var0, boolean var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return -1;
      }

      int var3 = var2.getInventory().getSelectedSlot();
      if (matchesItem(var2.getMainHandItem(), var0)) {
         return var3;
      }

      if (matchesItem(var2.getOffhandItem(), var0)) {
         return -2;
      }

      if (var1) {
         for (int var4 = 0; var4 < 9; var4++) {
            if (matchesItem(var2.getInventory().getItem(var4), var0)) {
               return var4;
            }
         }
      }

      return -1;
   }

   private static boolean matchesItem(ItemStack var0, Item var1) {
      return var1 == Items.AIR ? var0.isEmpty() : var0.is(var1);
   }

   public static boolean isReplaceable(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.level != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)
         && var1.level.getBlockState(var0).canBeReplaced();
   }

   public static boolean facePoints(BlockPos var0, Item var1) {
      if (isReplaceable(var0)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var1 instanceof BlockItem var3
         && var2.level != null
         && var2.level.hasChunkAt(var0)
         && !var2.level.isOutsideBuildHeight(var0)
         && var2.level.getWorldBorder().isWithinBounds(var0)
         && var2.level.getBlockState(var0).is(var3.getBlock());
   }

   private static BlockHitResult raycastBlock(Module var0, BlockPos var1, Vec3 var2, double var3, boolean var5) {
      return !isAllowed(var0) ? null : setFailure(var0, Minecraft.getInstance().player.getEyePosition(), var1, var2, var3, var5);
   }

   private static BlockHitResult setFailure(Module var0, Vec3 var1, BlockPos var2, Vec3 var3, double var4, boolean var6) {
      if (!isAllowed(var0)) {
         return null;
      }

      Minecraft var7 = Minecraft.getInstance();
      double var8 = Math.min(var4, var7.player.blockInteractionRange());
      Vec3 var10 = var1.add(var3.normalize().scale(var8));
      BlockHitResult var11 = var7.level.clip(new ClipContext(var1, var10, Block.OUTLINE, Fluid.NONE, var7.player));
      if (var11.getType() == Type.BLOCK && var11.getBlockPos().equals(var2) && !AntiTrapModule.shouldIgnoreBlock(var7.level.getBlockState(var2))) {
         if (var6) {
            return var11;
         }

         for (Entity var13 : var7.level.getEntities(var7.player, new AABB(var1, var11.getLocation()).inflate(1.0))) {
            if (var13.isPickable()
               && !var13.isSpectator()
               && !AntiTrapModule.shouldIgnoreEntity(var13)
               && !IgnoreEntityModule.shouldIgnoreEntity(var13)
               && BoxRaycastUtil.isBoxBlocking(var1, var11.getLocation(), var13.getBoundingBox())) {
               return null;
            }
         }

         return var11;
      } else {
         return null;
      }
   }

   public static BlockHitResult findSupportFace(BlockPos var0, Vec3 var1, Vec3 var2, double var3, boolean var5, NativePlacement.SupportWorld var6) {
      if (!var6.replaceable(var0)) {
         return null;
      }

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var0.relative(var10);
         if (var6.usableSupport(var11)) {
            Direction var12 = var10.getOpposite();

            for (Vec3 var14 : facePoints(var11, var12)) {
               BlockHitResult var15 = var6.ray(var11, var5 ? var14.subtract(var1) : var2, var3);
               if (var15 != null && var15.getDirection() == var12 && var6.contextMatches(var15, var0)) {
                  return var15;
               }

               if (!var5) {
                  break;
               }
            }
         }
      }

      return null;
   }

   public static List<Vec3> facePoints(BlockPos var0, Direction var1) {
      Vec3 var2 = Vec3.atCenterOf(var0).add(new Vec3(var1.getStepX(), var1.getStepY(), var1.getStepZ()).scale(0.499));
      ArrayList var3 = new ArrayList(9);

      for (double var7 : new double[]{0.0, -0.42, 0.42}) {
         for (double var12 : new double[]{0.0, -0.42, 0.42}) {
            var3.add(switch (var1.getAxis()) {
               case X -> var2.add(0.0, var7, var12);
               case Y -> var2.add(var7, 0.0, var12);
               case Z -> var2.add(var7, var12, 0.0);
               default -> throw new MatchException(null, null);
            });
         }
      }

      return var3;
   }

   private static boolean pruneInactive(Module var0, BlockPos var1) {
      return !(var0 instanceof NativePlacement.PlacementAimer var2 && !var2.trustsSupport(var1));
   }

   public static boolean getFailureReason(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.level.hasChunkAt(var0)) {
         return false;
      }

      BlockState var2 = var1.level.getBlockState(var0);
      return !var2.canBeReplaced()
         && (
            var1.player.isShiftKeyDown()
               || !var2.hasBlockEntity() && !(var2.getBlock() instanceof RespawnAnchorBlock) && !(var2.getBlock() instanceof CraftingTableBlock)
         );
   }

   public static BlockHitResult findPlacement(BlockPos var0, Item var1, double var2, boolean var4) {
      return findPlacementWithSwitch(var0, var1, var2, var4, true);
   }

   public static BlockHitResult findPlacementWithSwitch(BlockPos var0, Item var1, double var2, boolean var4, boolean var5) {
      return findPlacementFor(null, var0, var1, var2, var4, var5);
   }

   public static BlockHitResult findPlacementFor(final Module var0, BlockPos var1, final Item var2, double var3, boolean var5, final boolean var6) {
      if (var2 instanceof BlockItem && isAllowed(var0)) {
         Minecraft var7 = Minecraft.getInstance();
         return findSupportFace(
            var1,
            var7.player.getEyePosition(),
            var7.player.getLookAngle(),
            Math.min(var3, var7.player.blockInteractionRange()),
            var5,
            new NativePlacement.SupportWorld() {
               @Override
               public boolean replaceable(BlockPos var1) {
                  return NativePlacement.isReplaceable(var1);
               }

               @Override
               public boolean usableSupport(BlockPos var1) {
                  return NativePlacement.getFailureReason(var1) && NativePlacement.pruneInactive(var0, var1);
               }

               @Override
               public BlockHitResult ray(BlockPos var1, Vec3 var2x, double var3x) {
                  return NativePlacement.raycastBlock(var0, var1, var2x, var3x, true);
               }

               @Override
               public boolean contextMatches(BlockHitResult var1, BlockPos var2x) {
                  return NativePlacement.pruneInactive(var1, var2x, var2, var6);
               }
            }
         );
      } else {
         return null;
      }
   }

   private static boolean pruneInactive(BlockHitResult var0, BlockPos var1, Item var2, boolean var3) {
      return simulate(var0, var2, var3, var1) != null;
   }

   public static NativePlacement.SimulatedPlacement simulatePlacement(BlockHitResult var0, Item var1, boolean var2) {
      return simulate(var0, var1, var2, null);
   }

   private static NativePlacement.SimulatedPlacement simulate(BlockHitResult var0, Item var1, boolean var2, BlockPos var3) {
      Minecraft var4 = Minecraft.getInstance();
      int var5 = findItemSlot(var1, var2);
      if (var5 != -1 && var1 instanceof BlockItem var6) {
         ItemStack var7 = var5 == -2 ? var4.player.getOffhandItem() : var4.player.getInventory().getItem(var5);
         InteractionHand var8 = var5 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
         BlockPlaceContext var9 = new BlockPlaceContext(var4.player, var8, var7, var0);
         if (!var9.canPlace()) {
            return null;
         }

         BlockPos var10 = var9.getClickedPos();
         if (var3 != null && !var10.equals(var3)) {
            return null;
         }

         BlockState var11 = var6.getBlock().getStateForPlacement(var9);
         return var11 != null && var11.canSurvive(var4.level, var10) && var4.level.isUnobstructed(var11, var10, CollisionContext.placementContext(var4.player))
            ? new NativePlacement.SimulatedPlacement(var10, var11)
            : null;
      } else {
         return null;
      }
   }

   public static BlockHitResult raycastLook(BlockPos var0, Vec3 var1, double var2) {
      return isReplaceable(null, var0, var1, var2);
   }

   public static BlockHitResult isReplaceable(Module var0, BlockPos var1, Vec3 var2, double var3) {
      return raycastBlock(var0, var1, var2, var3, true);
   }

   public static BlockHitResult raycastFrom(Vec3 var0, BlockPos var1, Vec3 var2, double var3) {
      return raycastFromOrigin(null, var0, var1, var2, var3);
   }

   public static BlockHitResult raycastFromOrigin(Module var0, Vec3 var1, BlockPos var2, Vec3 var3, double var4) {
      return setFailure(var0, var1, var2, var3, var4, true);
   }

   public static boolean canReach(BlockPos var0, double var1) {
      return matchesItem(null, var0, var1);
   }

   public static boolean matchesItem(Module var0, BlockPos var1, double var2) {
      LocalPlayer var4 = Minecraft.getInstance().player;
      if (var4 == null) {
         return false;
      }

      LocalPlayerMixin_4 var5 = (LocalPlayerMixin_4)var4;
      float var6 = var5.larpYRotLast();
      float var7 = var5.larpXRotLast();
      return Float.isFinite(var6) && Float.isFinite(var7) && raycastBlock(var0, var1, Vec3.directionFromRotation(var7, var6), var2, true) != null;
   }

   public static boolean requestPlacement(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8
   ) {
      return pruneInactive(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean pruneInactive(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (findItemSlot(var2, var6) == -1) {
         setFailure(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         BlockHitResult var10 = findPlacementFor(var0, var1, var2, var3, var5, var6);
         if (var10 == null) {
            setFailure(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return queueIntent(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean matchesItem(
      Module var0, BlockPos var1, BlockHitResult var2, Item var3, double var4, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (findItemSlot(var3, var6) == -1) {
         setFailure(var0, "Required item missing from held hands or hotbar");
         return false;
      }

      if (var2 != null && var3 instanceof BlockItem && isAllowed(var0) && facePoints(var1, var3) && pruneInactive(var2, var1, var3, var6)) {
         return queueIntent(var0, var1, var2, var3, var4, true, true, var6, var7, var8, var9);
      }

      setFailure(var0, "The chosen support face no longer accepts this block");
      return false;
   }

   public static boolean isReplaceable(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (isAllowed(var0) && findItemSlot(var2, var6) != -1) {
         Minecraft var10 = Minecraft.getInstance();
         Vec3 var11 = var5 ? Vec3.atCenterOf(var1).subtract(var10.player.getEyePosition()) : var10.player.getLookAngle();
         BlockHitResult var12 = raycastBlock(var0, var1, var11, var3, false);
         if (var12 == null && var5) {
            var12 = findVisibleFace(var0, var1, var10.player.getEyePosition(), var3);
         }

         return var12 == null ? false : queueIntent(var0, var1, var12, var2, var3, false, var5, var6, var7, var8, var9);
      } else {
         return false;
      }
   }

   public static boolean pause(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return isReplaceable(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   private static BlockHitResult findVisibleFace(Module var0, BlockPos var1, Vec3 var2, double var3) {
      Vec3 var5 = Vec3.atCenterOf(var1);

      for (Direction var9 : sUPPORT_FACE_ORDER) {
         Vec3 var10 = new Vec3(var9.getStepX(), var9.getStepY(), var9.getStepZ());
         if (!(var2.subtract(var5.add(var10.scale(0.5))).dot(var10) <= 0.0)) {
            for (Vec3 var12 : facePoints(var1, var9)) {
               BlockHitResult var13 = raycastBlock(var0, var1, var12.subtract(var2), var3, false);
               if (var13 != null && var13.getDirection() == var9) {
                  return var13;
               }
            }
         }
      }

      return null;
   }

   private static boolean queueIntent(
      Module var0,
      BlockPos var1,
      BlockHitResult var2,
      Item var3,
      double var4,
      boolean var6,
      boolean var7,
      boolean var8,
      BooleanSupplier var9,
      BooleanSupplier var10,
      Consumer<Boolean> var11
   ) {
      Minecraft var12 = Minecraft.getInstance();
      NativePlacement.PlacementIntent var13 = new NativePlacement.PlacementIntent(
         var0, var1.immutable(), var2, var3, var4, var6, var7, var9, var10, var11, var12.level, var12.player, var12.player.tickCount
      );
      intents.put(var0, var13);
      InteractionArbiter.queueAction(
         var0,
         () -> findItemSlot(var13, var8),
         () -> raycastBlock(var13) && var13.actionReady.getAsBoolean() && findItemSlot(var3, var8) != -1 && pruneInactive(var13, var8) != null
      );
      return true;
   }

   private static boolean raycastBlock(NativePlacement.PlacementIntent var0) {
      Minecraft var1 = Minecraft.getInstance();
      return intents.get(var0.source) == var0
         && isSameContext(var0.world, var1.level, var0.player, var1.player, var0.tick, var1.player == null ? 0 : var1.player.tickCount)
         && var0.source.isRuntimeActive()
         && InteractionArbiter.canModuleInteract(var0.source)
         && (var1.isWindowActive() || var0.source.allowsScreenInteractions())
         && var0.valid.getAsBoolean();
   }

   private static BlockHitResult pruneInactive(NativePlacement.PlacementIntent var0, boolean var1) {
      if (!raycastBlock(var0)) {
         return null;
      }

      Minecraft var2 = Minecraft.getInstance();
      BlockHitResult var3 = raycastBlock(var0.source, var0.hit.getBlockPos(), var2.player.getLookAngle(), var0.range, var0.place);
      if (var3 != null && var3.getDirection() == var0.hit.getDirection()) {
         if (!var0.place || getFailureReason(var3.getBlockPos()) && pruneInactive(var0.source, var3.getBlockPos())) {
            if (var0.place && var0.source instanceof NativePlacement.PlacementAimer var4 && var4.waitsForSentLook()) {
               LocalPlayerMixin_4 var8 = (LocalPlayerMixin_4)var2.player;
               BlockHitResult var6 = raycastBlock(
                  var0.source, var0.hit.getBlockPos(), Vec3.directionFromRotation(var8.larpXRotLast(), var8.larpYRotLast()), var0.range, true
               );
               if (var6 == null || var6.getDirection() != var0.hit.getDirection()) {
                  pause(
                     var0.source,
                     "The look the server has does not reach the support face "
                        + var0.hit.getDirection().getSerializedName()
                        + " of "
                        + var0.hit.getBlockPos().toShortString()
                        + " yet; waiting a tick"
                  );
                  return null;
               }
            }

            if (var0.place && !var2.player.isShiftKeyDown()) {
               BlockState var7 = var2.level.getBlockState(var3.getBlockPos());
               if (var7.hasBlockEntity() || var7.getBlock() instanceof RespawnAnchorBlock || var7.getBlock() instanceof CraftingTableBlock) {
                  return null;
               }
            }

            if (!var0.place || facePoints(var0.target, var0.item) && pruneInactive(var3, var0.target, var0.item, var1)) {
               return var3;
            }

            pause(var0.source, "Native placement context rejects the current block position, support or entity collision");
            return null;
         } else {
            pause(var0.source, "The support " + var3.getBlockPos().toShortString() + " is gone or not trusted yet; waiting instead of an air place");
            return null;
         }
      } else {
         pause(
            var0.source,
            "Current look does not reach the planned support face "
               + var0.hit.getDirection().getSerializedName()
               + " of "
               + var0.hit.getBlockPos().toShortString()
               + (var3 == null ? " (looking at no block of it)" : " (looking at its " + var3.getDirection().getSerializedName() + " face)")
               + "; check competing rotations or Crosshair aim"
         );
         return null;
      }
   }

   public static void tick() {
      intents.values().removeIf(var0x -> {
         if (raycastBlock(var0x)) {
            return false;
         }

         pause(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
         return true;
      });
      Optional var0 = intents.values()
         .stream()
         .filter(var0x -> var0x.visible && raycastBlock(var0x) && InteractionArbiter.ownsInteraction(var0x.source))
         .max(Comparator.comparingInt(var0x -> InteractionArbiter.getPriority(var0x.source.getName())));
      if (var0.isEmpty() || !(((NativePlacement.PlacementIntent)var0.get()).source instanceof AutoTrap)) {
         ViewController.release(ViewController.Owner.AUTO_TRAP);
      }

      if (var0.isEmpty() || !(((NativePlacement.PlacementIntent)var0.get()).source instanceof AutoWebModule)) {
         ViewController.release(ViewController.Owner.AUTO_WEB);
      }

      if (var0.isEmpty() || !(((NativePlacement.PlacementIntent)var0.get()).source instanceof AutoAnchorModule)) {
         ViewController.release(ViewController.Owner.AUTO_ANCHOR);
      }

      if (var0.isPresent() && !(((NativePlacement.PlacementIntent)var0.get()).source instanceof ScaffoldModule)) {
         ViewController.release(ViewController.Owner.SCAFFOLD);
      }

      if (!var0.isEmpty()) {
         NativePlacement.PlacementIntent var1 = (NativePlacement.PlacementIntent)var0.get();
         Minecraft var2 = Minecraft.getInstance();
         float[] var3 = RotationManager.calculateAngles(var2.player.getEyePosition(), var1.hit.getLocation());
         if (var1.source instanceof NativePlacement.PlacementAimer var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            RotationManager.request(RotationManager.Priority.DEFENSE, var3[0], var3[1], 1.0);
         }

         RotationManager.applyToPlayer();
      }
   }

   private static void findItemSlot(NativePlacement.PlacementIntent var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         BlockHitResult var2 = pruneInactive(var0, var1);
         int var3 = findItemSlot(var0.item, var1);
         if (var2 != null && var3 != -1) {
            Minecraft var4 = Minecraft.getInstance();
            int var5 = var4.player.getInventory().getSelectedSlot();

            try {
               if (var3 >= 0) {
                  var4.player.getInventory().setSelectedSlot(var3);
               }

               InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
               boolean var7 = var4.gameMode.useItemOn(var4.player, var6, var2).consumesAction();
               if (var7) {
                  SwingHelper.swingInteract(var4.player, var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.player.getInventory().setSelectedSlot(var5);
               intents.remove(var0.source, var0);
            }
         } else {
            pause(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private NativePlacement() {
   }

   public interface PlacementAimer {
      void aimPlacement(float var1, float var2);

      default boolean trustsSupport(BlockPos var1) {
         return true;
      }

      default boolean waitsForSentLook() {
         return false;
      }
   }

   private record PlacementIntent(
      Module source,
      BlockPos target,
      BlockHitResult hit,
      Item item,
      double range,
      boolean place,
      boolean visible,
      BooleanSupplier valid,
      BooleanSupplier actionReady,
      Consumer<Boolean> completed,
      Object world,
      Object player,
      int tick
   ) {
   }

   public abstract static class PlacementModule extends Module implements NativePlacement.PlacementAimer {
      protected final NumberSetting range = this.addNumberSetting("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final NumberSetting targetRange = this.addNumberSetting("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final NumberSetting delayPerAction;
      protected final ModeSetting rotationMode;
      protected final BooleanSetting autoSwitch;
      protected final BooleanSetting pauseUsingItem;
      protected final BooleanSetting pauseMining;
      private long nextActionTick;
      private String status = "Waiting for a target";

      protected PlacementModule(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected PlacementModule(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.delayPerAction = this.addNumberSetting("Delay Per Action", "Ticks between native placement attempts", var3, 0.0, 20.0, 1.0);
         this.rotationMode = this.addSetting(
            new ModeSetting("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.autoSwitch = this.addBooleanSetting("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.pauseUsingItem = this.addBooleanSetting("Pause Using Item", "Pause while eating or using an item", true);
         this.pauseMining = this.addBooleanSetting("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            RotationManager.request(RotationManager.Priority.DEFENSE, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void retryLater(boolean var1) {
         this.nextActionTick = minecraft.level.getGameTime() + Math.max(1, this.delayPerAction.getValue().intValue());
         this.setStatus(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
      }

      protected final BooleanSetting addBooleanSetting(String var1, String var2, boolean var3) {
         return this.addSetting(new BooleanSetting(var1, var2, var3));
      }

      @Override
      public Module.TickPhase getTickPhase() {
         return Module.TickPhase.MAIN;
      }

      @Override
      public Set<ScanPhase> getNetworkImpacts() {
         return Set.of(ScanPhase.dONE);
      }

      protected final void setStatus(String var1) {
         if (!var1.equals(this.status) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            NativePlacement.pause(this, var1);
         }

         this.status = var1;
      }

      @Override
      public String getStatus() {
         return this.status;
      }

      protected final boolean isReady() {
         return !this.isEnabled() ? false : this.canInteract() && minecraft.level.getGameTime() >= this.nextActionTick;
      }

      protected final boolean canInteract() {
         return !this.isEnabled()
            ? false
            : InteractionArbiter.canInteract()
               && minecraft.isWindowActive()
               && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem())
               && (!this.pauseMining.getValue() || !minecraft.options.keyAttack.isDown());
      }

      protected final boolean canPlace(BlockPos var1, Item var2) {
         return !this.isEnabled()
            ? false
            : NativePlacement.findItemSlot(var2, this.autoSwitch.getValue()) != -1
               && NativePlacement.findPlacementWithSwitch(var1, var2, this.range.getValue(), this.rotationMode.isMode("Visible"), this.autoSwitch.getValue())
                  != null;
      }

      protected final boolean isValidTarget(Player var1) {
         if (var1 != null
            && var1 != minecraft.player
            && var1.level() == minecraft.level
            && var1.isAlive()
            && !var1.isSpectator()
            && !var1.isCreative()
            && !var1.isRemoved()
            && !(minecraft.player.distanceToSqr(var1) > this.targetRange.getValue() * this.targetRange.getValue())) {
            MCFriendModule var2 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
            return var2 == null || !var2.isFriend(var1.getName().getString());
         } else {
            return false;
         }
      }

      protected final Player findTarget() {
         return minecraft.level.players().stream().filter(this::isValidTarget).min(Comparator.comparingDouble(minecraft.player::distanceToSqr)).orElse(null);
      }

      protected final boolean requestPlacement(BlockPos var1, Item var2, Player var3) {
         return this.isEnabled()
            ? NativePlacement.requestPlacement(
               this,
               var1,
               var2,
               this.range.getValue(),
               this.rotationMode.isMode("Visible"),
               this.autoSwitch.getValue(),
               () -> this.isReady() && this.isValidTarget(var3),
               this::retryLater
            )
            : false;
      }

      @Override
      public void onEnable() {
         this.nextActionTick = 0L;
         NativePlacement.lastLogNanos.remove(this);
         NativePlacement.failureReasons.remove(this);
         LarpLog.info(
            this.getName(),
            "Native placement enabled; rotation={}, range={}, autoSwitch={}",
            this.rotationMode.getValue(),
            this.range.getValue(),
            this.autoSwitch.getValue()
         );
      }

      @Override
      public void onDisable() {
         NativePlacement.cancel(this);
         this.nextActionTick = 0L;
         this.status = "Waiting for a target";
         LarpLog.debug(this.getName(), "Placement intents cleared");
      }
   }

   public record SimulatedPlacement(BlockPos target, BlockState state) {
   }

   public interface SupportWorld {
      boolean replaceable(BlockPos var1);

      boolean usableSupport(BlockPos var1);

      BlockHitResult ray(BlockPos var1, Vec3 var2, double var3);

      boolean contextMatches(BlockHitResult var1, BlockPos var2);
   }
}
