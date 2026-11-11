package com.larpclient.util.world;

import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.WorldAutomationModule;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockBreaker {
   private static WorldAutomationModule activeModule;
   private static BlockPos currentBlock;
   private static Object level;
   private static Object player;
   private static int originalSlot = -1;
   private static int toolSlot = -1;
   private static int lastDigTick;
   private static ItemStack toolStack = ItemStack.EMPTY;
   private static boolean releasing;
   private static BlockPos aimedBlock;
   private static BlockPos missedBlock;
   private static final ArrayDeque<BlockBreaker.TraceEntry> digTrace = new ArrayDeque<>();
   private static int traceDumpCount;
   private static final BlockPos rESET_POS = new BlockPos(-1, -1, -1);
   private static int stallTicks;
   private static int aimAttempt;
   private static int missTicks;
   static final int mIN_STALL_TICKS = 10;
   static final int mAX_STALL_TICKS = 200;
   static final double ePSILON = 0.03;
   static final double sAMPLE_OFFSET = 0.3;
   public static final int mAX_AIM_ATTEMPTS = 3;
   static final int aIM_CACHE_LIMIT = 64;
   private static final Map<BlockPos, Integer> aimIndexCache = new HashMap<>();

   private static void recordTrace(BlockPos var0, String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         if (digTrace.size() >= 80) {
            digTrace.removeFirst();
         }

         digTrace.addLast(new BlockBreaker.TraceEntry(var2.player.tickCount, var0, var1));
      }
   }

   private static String describeDigState() {
      Minecraft var0 = Minecraft.getInstance();
      MultiPlayerGameModeMixin_6 var1 = (MultiPlayerGameModeMixin_6)var0.gameMode;
      LocalPlayerMixin_4 var2 = (LocalPlayerMixin_4)var0.player;
      return String.format(
         Locale.ROOT,
         "%s %s p%.2f d%d slot%d/sent%d %s%s look %.1f/%.1f sent %.1f/%.1f",
         var1.larpIsDestroying() ? "digging" : "idle",
         var1.larpDestroyBlockPos() == null ? "-" : var1.larpDestroyBlockPos().toShortString(),
         var1.larpDestroyProgress(),
         var1.larpDestroyDelay(),
         var0.player.getInventory().getSelectedSlot(),
         var1.larpCarriedIndex(),
         var0.player.onGround() ? "ground" : "air",
         var0.player.isShiftKeyDown() ? " sneak" : "",
         var0.player.getYRot(),
         var0.player.getXRot(),
         var2.larpYRotLast(),
         var2.larpXRotLast()
      );
   }

   public static void traceVanillaAbort() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.gameMode != null && var0.player != null) {
         MultiPlayerGameModeMixin_6 var1 = (MultiPlayerGameModeMixin_6)var0.gameMode;
         if (var1.larpIsDestroying() && var1.larpDestroyBlockPos() != null) {
            recordTrace(var1.larpDestroyBlockPos(), "vanilla abort (attack released) " + describeDigState());
         }
      }
   }

   public static void dumpTrace(String var0, BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && traceDumpCount < 12) {
         traceDumpCount++;
         int var3 = var2.player.tickCount;
         StringBuilder var4 = new StringBuilder();

         for (BlockBreaker.TraceEntry var6 : digTrace) {
            if (var3 - var6.tick() <= 120) {
               var4.append(System.lineSeparator())
                  .append("  ")
                  .append(var6.tick() - var3)
                  .append(' ')
                  .append(var6.pos().equals(var1) ? "*" : " ")
                  .append(var6.pos().toShortString())
                  .append(' ')
                  .append(var6.text());
            }
         }

         LarpLog.info(var0, "Dig trace before {} came back ({} of 12):{}", var1.toShortString(), traceDumpCount, var4);
      }
   }

   public static boolean canBreak(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.level != null
         && var1.player != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)) {
         BlockState var2 = var1.level.getBlockState(var0);
         return !var2.isAir() && var2.getFluidState().isEmpty() && var2.getDestroySpeed(var1.level, var0) >= 0.0F;
      } else {
         return false;
      }
   }

   public static BlockHitResult findAimHit(BlockPos var0, double var1) {
      return canBreak(null, var0, var1);
   }

   public static BlockHitResult canBreak(WorldAutomationModule var0, BlockPos var1, double var2) {
      return findAimHitAttempt(var0, var1, var2, 0);
   }

   static BlockHitResult findAimHitAttempt(WorldAutomationModule var0, BlockPos var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (!InteractionArbiter.canModuleInteract(var0)) {
         return null;
      }

      if (var4 == 0 && var0 != null && var0.aimsAtOpenFaces(var1)) {
         return findFaceAimHitFor(var0, var1, var2);
      }

      if (var4 > 0) {
         ArrayList var15 = new ArrayList();

         for (Vec3 var8 : breakBlock(var1)) {
            BlockHitResult var17 = hasLineOfSight(var1, var8, var2);
            if (var17 != null
               && (var0 == null || var0.aimFaceAllowed(var1, var17.getDirection()))
               && var15.stream().noneMatch(var1x -> var1x.getLocation().distanceToSqr(var17.getLocation()) < 0.01)) {
               var15.add(var17);
            }
         }

         return var15.isEmpty() ? null : (BlockHitResult)var15.get(var4 % var15.size());
      } else {
         Vec3 var6 = var5.player.getEyePosition();
         double var7 = Math.min(var2, var5.player.blockInteractionRange());
         VoxelShape var9 = var5.level.getBlockState(var1).getShape(var5.level, var1);
         ArrayList var10 = new ArrayList();
         if (!var9.isEmpty()) {
            var10.add(var9.bounds().getCenter().add(var1.getX(), var1.getY(), var1.getZ()));
         }

         for (Direction var14 : Direction.values()) {
            var10.addAll(NativePlacement.facePoints(var1, var14));
         }

         for (Vec3 var19 : var10) {
            Vec3 var20 = var19.subtract(var6);
            if (!(var20.lengthSqr() > var7 * var7)) {
               BlockHitResult var21 = var5.level.clip(new ClipContext(var6, var6.add(var20.normalize().scale(var7)), Block.OUTLINE, Fluid.NONE, var5.player));
               if (var21.getType() == Type.BLOCK && var21.getBlockPos().equals(var1)) {
                  return var21;
               }
            }
         }

         return null;
      }
   }

   public static BlockHitResult findFaceAimHit(BlockPos var0, double var1) {
      return findFaceAimHitFor(null, var0, var1);
   }

   public static BlockHitResult findFaceAimHitFor(WorldAutomationModule var0, BlockPos var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.min(var2, var4.player.blockInteractionRange());
      if (var4.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) > var5 * var5) {
         return null;
      }

      List var7 = candidateFaceHits(var4.player.getEyePosition(), var1, var5);
      if (var0 != null) {
         EnumMap var8 = new EnumMap(Direction.class);
         var7.removeIf(var3 -> !var8.computeIfAbsent(var3.hit().getDirection(), var2xx -> var0.aimFaceAllowed(var1, var2xx)));
         if (var0.aimsAtFaceCentre(var1)) {
            for (BlockBreaker.FaceHit var10 : var7) {
               if (var10.index() % 9 == 0) {
                  return var10.hit();
               }
            }
         }
      }

      if (var7.isEmpty()) {
         return null;
      }

      Integer var18 = aimIndexCache.get(var1);
      if (var18 != null) {
         for (BlockBreaker.FaceHit var21 : var7) {
            if (var21.index() == var18) {
               return var21.hit();
            }
         }
      }

      Vec3 var20 = var4.player.getEyePosition();
      Vec3 var22 = var4.player.getViewVector(1.0F);
      BlockBreaker.FaceHit var11 = null;
      double var12 = -2.0;

      for (BlockBreaker.FaceHit var15 : var7) {
         double var16 = var15.hit().getLocation().subtract(var20).normalize().dot(var22);
         if (var16 > var12) {
            var12 = var16;
            var11 = var15;
         }
      }

      if (aimIndexCache.size() >= 64) {
         aimIndexCache.clear();
      }

      aimIndexCache.put(var1.immutable(), var11.index());
      return var11.hit();
   }

   public static boolean describeDigState(Vec3 var0, BlockPos var1, double var2) {
      return !candidateFaceHits(var0, var1, var2).isEmpty();
   }

   private static List<BlockBreaker.FaceHit> candidateFaceHits(Vec3 var0, BlockPos var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = Vec3.atCenterOf(var1);

      for (Direction var10 : Direction.values()) {
         Vec3 var11 = Vec3.atLowerCornerOf(var10.getUnitVec3i());
         Vec3 var12 = var6.add(var11.scale(0.499));
         if (!(var0.subtract(var12).dot(var11) <= 0.0)) {
            ArrayList var13 = new ArrayList(9);
            int var14 = 0;

            for (double var18 : new double[]{0.0, -0.3, 0.3}) {
               for (double var23 : new double[]{0.0, -0.3, 0.3}) {
                  int var25 = var10.ordinal() * 9 + var14++;

                  Vec3 var26 = switch (var10.getAxis()) {
                     case X -> var12.add(0.0, var18, var23);
                     case Y -> var12.add(var18, 0.0, var23);
                     case Z -> var12.add(var18, var23, 0.0);
                     default -> throw new MatchException(null, null);
                  };
                  Vec3 var27 = var26.subtract(var0);
                  if (!(var27.lengthSqr() > var2 * var2) && !(var27.lengthSqr() < 1.0E-8)) {
                     BlockHitResult var28 = var4.level
                        .clip(new ClipContext(var0, var0.add(var27.normalize().scale(var2)), Block.OUTLINE, Fluid.NONE, var4.player));
                     if (var28.getType() == Type.BLOCK && var28.getBlockPos().equals(var1) && var28.getDirection() == var10) {
                        var13.add(new BlockBreaker.FaceHit(var28, var25));
                     }
                  }
               }
            }

            if (var13.size() >= 3) {
               var5.addAll(var13);
            }
         }
      }

      return var5;
   }

   private static List<Vec3> breakBlock(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      VoxelShape var2 = var1.level.getBlockState(var0).getShape(var1.level, var0);
      ArrayList var3 = new ArrayList();
      if (!var2.isEmpty()) {
         var3.add(var2.bounds().getCenter().add(var0.getX(), var0.getY(), var0.getZ()));
      }

      for (Direction var7 : Direction.values()) {
         var3.addAll(NativePlacement.facePoints(var0, var7));
      }

      return var3;
   }

   private static BlockHitResult hasLineOfSight(BlockPos var0, Vec3 var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.player.getEyePosition();
      Vec3 var6 = var1.subtract(var5);
      double var7 = Math.min(var2, var4.player.blockInteractionRange());
      if (!(var6.lengthSqr() > var7 * var7) && !(var6.lengthSqr() < 1.0E-8)) {
         BlockHitResult var9 = var4.level.clip(new ClipContext(var5, var5.add(var6.normalize().scale(var7)), Block.OUTLINE, Fluid.NONE, var4.player));
         return var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0) ? var9 : null;
      } else {
         return null;
      }
   }

   public static boolean bestToolSlot(BlockPos var0, Direction var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      LocalPlayer var5 = var4.player;
      if (var5 != null && var4.level != null) {
         LocalPlayerMixin_4 var6 = (LocalPlayerMixin_4)var5;
         float var7 = var6.larpYRotLast();
         float var8 = var6.larpXRotLast();
         if (Float.isFinite(var7) && Float.isFinite(var8)) {
            Vec3 var9 = Vec3.directionFromRotation(var8, var7);
            double var10 = Math.min(var2, var5.blockInteractionRange());
            AABB var12 = new AABB(var0);
            Vec3 var13 = new Vec3(var6.larpXLast(), var6.larpYLast() + var5.getEyeHeight(), var6.larpZLast());
            if (boxReachedByRay(var12, var13, var9, var10) && boxReachedByRay(var12, var5.getEyePosition(), var9, var10)) {
               VoxelShape var14 = var4.level.getBlockState(var0).getShape(var4.level, var0);
               return var1 == null || hasLineOfSight(var14.isEmpty() ? var12 : var14.bounds().move(var0), var1, var13);
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean boxReachedByRay(AABB var0, Vec3 var1, Vec3 var2, double var3) {
      return var0.inflate(0.03).contains(var1) || var0.clip(var1, var1.add(var2.scale(var3))).isPresent();
   }

   public static boolean hasLineOfSight(AABB var0, Direction var1, Vec3 var2) {
      if (var0.inflate(0.03).contains(var2)) {
         return true;
      }

      Axis var3 = var1.getAxis();
      double var4 = var3.choose(var2.x, var2.y, var2.z);
      return var1.getAxisDirection() == AxisDirection.POSITIVE ? var4 + 0.03 >= var0.max(var3) : var4 - 0.03 <= var0.min(var3);
   }

   public static boolean hasLineOfSight(BlockPos var0, Vec3 var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.player.getEyePosition();
      Vec3 var6 = var1.subtract(var5);
      double var7 = Math.min(var2, var4.player.blockInteractionRange());
      if (!(var6.lengthSqr() > var7 * var7) && !(var6.lengthSqr() < 1.0E-8)) {
         BlockHitResult var9 = var4.level.clip(new ClipContext(var5, var5.add(var6.normalize().scale(var7)), Block.OUTLINE, Fluid.NONE, var4.player));
         return var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0);
      } else {
         return false;
      }
   }

   public static BlockHitResult raycastLook(BlockPos var0, double var1) {
      Minecraft var3 = Minecraft.getInstance();
      Vec3 var4 = var3.player.getEyePosition();
      BlockHitResult var5 = var3.level
         .clip(
            new ClipContext(
               var4, var4.add(var3.player.getLookAngle().scale(Math.min(var1, var3.player.blockInteractionRange()))), Block.OUTLINE, Fluid.NONE, var3.player
            )
         );
      return var5.getType() == Type.BLOCK && var5.getBlockPos().equals(var0) ? var5 : null;
   }

   static boolean breakBlockWithBestTool(WorldAutomationModule var0, BlockPos var1, double var2, boolean var4, float var5, boolean var6, BooleanSupplier var7) {
      return breakBlock(var0, var1, var2, var4, var5, var6 ? BlockBreaker::bestToolSlot : null, var7);
   }

   public static boolean breakBlock(
      WorldAutomationModule var0, BlockPos var1, double var2, boolean var4, float var5, ToIntFunction<BlockState> var6, BooleanSupplier var7
   ) {
      int var8 = var6 != null ? 1 : 0;
      if (var0.isReady() && canBreak(var1)) {
         Minecraft var9 = Minecraft.getInstance();
         BlockPos var10 = var1.immutable();
         ClientLevel var11 = var9.level;
         LocalPlayer var12 = var9.player;
         int var13 = var9.player.tickCount;
         BlockState var14 = var9.level.getBlockState(var10);
         BooleanSupplier var15 = () -> var0.isReady()
            && var7.getAsBoolean()
            && NativePlacement.isSameContext(var11, var9.level, var12, var9.player, var13, var9.player == null ? -10 : var9.player.tickCount)
            && canBreak(var10)
            && var9.level.getBlockState(var10).equals(var14)
            && var9.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var10)) <= var2 * var2;
         if (!var10.equals(aimedBlock)) {
            aimedBlock = var10;
            stallTicks = 0;
            aimAttempt = 0;
         } else if (raycastLook(var10, var2) != null && ++stallTicks > stallLimit(var14, var10)) {
            stallTicks = 0;
            aimAttempt++;
            LarpLog.debug(var0.getName(), "{} has not broken; aiming at another point of it ({})", var10.toShortString(), aimAttempt);
         }

         BlockHitResult var16 = var4 ? findAimHitAttempt(var0, var10, var2, aimAttempt) : null;
         if (var4 && var16 == null) {
            release(var0);
            return false;
         }

         if (var4 && InteractionArbiter.ownsInteraction(var0)) {
            var0.aimAt(var16.getLocation(), var5);
         }

         InteractionArbiter.queueAction(
            var0,
            () -> {
               if (var15.getAsBoolean()) {
                  BlockHitResult var10x = var4 ? raycastLook(var10, var2) : null;
                  if (!var4 || var10x != null) {
                     if (activeModule != null && activeModule != var0) {
                        release(activeModule);
                     }

                     if (var4 && !bestToolSlot(var10, var10x.getDirection(), var2)) {
                        recordTrace(var10, "hold: sent look misses " + describeDigState());
                        if (!var10.equals(missedBlock)) {
                           missedBlock = var10;
                           missTicks = 0;
                        }

                        if (++missTicks == 20) {
                           LarpLog.info(
                              var0.getName(), "Holding the break of {} for 20 ticks: the look the server has does not reach it", var10.toShortString()
                           );
                        }

                        if (activeModule == var0) {
                           lastDigTick = var9.player.tickCount;
                        }
                     } else {
                        missedBlock = null;
                        if (activeModule != null && var9.player.getInventory().getSelectedSlot() != toolSlot) {
                           release(var0);
                        } else {
                           if (activeModule == null) {
                              activeModule = var0;
                              level = var9.level;
                              player = var9.player;
                              originalSlot = var9.player.getInventory().getSelectedSlot();
                           }

                           if (!var10.equals(currentBlock)) {
                              if (currentBlock != null) {
                                 LarpLog.debug(var0.getName(), "Next block {}, {}, {}", var10.getX(), var10.getY(), var10.getZ());
                              }

                              currentBlock = var10;
                              int var11x = var8 ? var6.applyAsInt(var14) : -1;
                              toolSlot = var11x >= 0 && var11x < 9 ? var11x : var9.player.getInventory().getSelectedSlot();
                              toolStack = var9.player.getInventory().getItem(toolSlot);
                           }

                           if (var8) {
                              var9.player.getInventory().setSelectedSlot(toolSlot);
                           }

                           lastDigTick = var9.player.tickCount;
                           Direction var15x = var10x == null ? Direction.UP : var10x.getDirection();
                           MultiPlayerGameModeMixin_6 var12x = (MultiPlayerGameModeMixin_6)var9.gameMode;
                           if (!var12x.larpIsDestroying() && var10.equals(var12x.larpDestroyBlockPos()) && var12x.larpDestroyDelay() == 0) {
                              var12x.larpDestroyBlockPos(rESET_POS);
                           }

                           String var13x = describeDigState();
                           boolean var14x = var9.gameMode.continueDestroyBlock(var10, var15x);
                           if (var9.level.getBlockState(var10).isAir()) {
                              var0.digFinished(var10);
                           }

                           recordTrace(
                              var10,
                              "dig "
                                 + var15x.getSerializedName()
                                 + (var14x ? " swing" : "")
                                 + (var9.level.getBlockState(var10).isAir() ? " BROKEN" : "")
                                 + " | "
                                 + var13x
                                 + " -> "
                                 + describeDigState()
                           );
                           if (var14x) {
                              var9.level.addBreakingBlockEffects(var10, var15x, true);
                              SwingHelper.swingAttack(var9.player, InteractionHand.MAIN_HAND);
                           }

                           var0.setStatus("Mining " + var10.getX() + ", " + var10.getY() + ", " + var10.getZ());
                        }
                     }
                  }
               }
            },
            () -> var15.getAsBoolean() && (!var4 || raycastLook(var10, var2) != null)
         );
         return true;
      } else {
         release(var0);
         return false;
      }
   }

   private static int stallLimit(BlockState var0, BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      float var3 = var0.getDestroyProgress(var2.player, var2.level, var1);
      int var4 = var3 <= 0.0F ? 200 : (int)Math.ceil(1.0F / var3);
      return Math.clamp(2L * var4, 10, 200);
   }

   public static int bestToolSlot(BlockState var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = var1.getSelectedSlot();
      float var3 = var1.getItem(var2).getDestroySpeed(var0) + (var1.getItem(var2).isCorrectToolForDrops(var0) ? 1000 : 0);

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getItem(var4);
         float var6 = var5.getDestroySpeed(var0);
         if (var5.isCorrectToolForDrops(var0)) {
            var6 += 1000.0F;
         }

         if (var6 > var3) {
            var3 = var6;
            var2 = var4;
         }
      }

      return var2;
   }

   public static boolean isBreaking() {
      Minecraft var0 = Minecraft.getInstance();
      return !releasing
         && activeModule != null
         && activeModule.isReady()
         && var0.level == level
         && var0.player == player
         && var0.player.tickCount - lastDigTick <= 1
         && canBreak(currentBlock)
         && var0.player.getInventory().getSelectedSlot() == toolSlot;
   }

   public static void release(Module var0) {
      if (activeModule == var0) {
         Minecraft var1 = Minecraft.getInstance();

         try {
            releasing = (boolean)1;
            if (var1.level == level && var1.player == player && var1.gameMode != null) {
               if (!var1.options.keyAttack.isDown()) {
                  if (currentBlock != null) {
                     recordTrace(currentBlock, "release (abort) " + describeDigState());
                  }

                  var1.gameMode.stopDestroyBlock();
               }

               int var2 = var0 instanceof WorldAutomationModule var3 && var3.holdsToolBetweenBlocks() ? 1 : 0;
               if (var2 == 0
                  && !var1.options.keyAttack.isDown()
                  && toolSlot != originalSlot
                  && var1.player.getInventory().getSelectedSlot() == toolSlot
                  && var1.player.getInventory().getItem(toolSlot) == toolStack) {
                  var1.player.getInventory().setSelectedSlot(originalSlot);
               }
            }
         } finally {
            releasing = (boolean)0;
            activeModule = null;
            currentBlock = null;
            level = null;
            player = null;
            missedBlock = null;
            aimedBlock = null;
            missTicks = 0;
            aimAttempt = 0;
            stallTicks = 0;
            originalSlot = -1;
            toolSlot = -1;
            toolStack = ItemStack.EMPTY;
         }
      }
   }

   private BlockBreaker() {
   }

   private record FaceHit(BlockHitResult hit, int index) {
   }

   private record TraceEntry(int tick, BlockPos pos, String text) {
   }
}
