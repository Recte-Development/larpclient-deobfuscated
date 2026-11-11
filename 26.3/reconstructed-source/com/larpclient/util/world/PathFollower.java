package com.larpclient.util.world;

import com.larpclient.module.world.WorldAutomationModule;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.InteractionArbiter;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.function.ToIntFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class PathFollower {
   static final int mAX_STUCK_TICKS = 40;
   static final int mAX_BREAK_TICKS = 400;
   static final int mAX_REACH_FAILURES = 20;
   private final WorldAutomationModule input;
   private final RotationSmoother2 speedController;
   private final PathFollower.LookTarget lookTarget;
   private List<AStarPathfinder.Step> path = List.of();
   private int nodeIndex;
   private int stuckTicks;
   private int breakTicks;
   private int reachFailures;
   private int blocksBroken;
   private BlockPos breakingPos;
   private Vec3 lastPosition;
   private Vec3 moveDirection = Vec3.ZERO;
   private boolean jumping;
   private boolean braking;
   private boolean sprinting;
   private String failureReason = "";
   private boolean exactEnd = (boolean)1;
   private double wobbleAmplitude;
   private double wobblePhaseX;
   private double wobblePhaseZ;
   private double wobbleFreqX;
   private double wobbleFreqZ;
   private double endOffsetX;
   private double endOffsetZ;
   private boolean sprintJump;
   private final SplittableRandom random = new SplittableRandom();
   static final double wOBBLE_AMPLITUDE = 0.16;
   static final double eND_OFFSET_RANGE = 0.18;
   static final int mAX_STEPS_PER_TICK = 4;
   static final double sPRINT_JUMP_MIN_DISTANCE = 7.0;
   private int moveTicks;
   private int sprintTicks;
   static final double lOOKAHEAD_DISTANCE = 3.0;
   static final int mAX_SHORTCUT_NODES = 8;

   public PathFollower(WorldAutomationModule var1, RotationSmoother2 var2, PathFollower.LookTarget var3) {
      this.input = var1;
      this.speedController = var2;
      this.lookTarget = var3;
   }

   public void setPath(List<AStarPathfinder.Step> var1) {
      this.setPathWithEnd(var1, true);
   }

   public void setPathWithEnd(List<AStarPathfinder.Step> var1, boolean var2) {
      this.reset();
      this.path = var1 == null ? List.of() : List.copyOf(var1);
      this.exactEnd = var2;
      this.wobbleAmplitude = 0.0;
   }

   public void randomizeWobble() {
      if (!this.path.isEmpty()) {
         this.wobbleAmplitude = 0.16;
         this.wobblePhaseX = this.random.nextDouble() * Math.PI * 2.0;
         this.wobblePhaseZ = this.random.nextDouble() * Math.PI * 2.0;
         this.wobbleFreqX = 0.35 + this.random.nextDouble() * 0.4;
         this.wobbleFreqZ = 0.35 + this.random.nextDouble() * 0.4;
         AStarPathfinder.Step var1 = this.path.getLast();
         boolean var2 = hasEscapeRoute(var1.blockX(), var1.blockY(), var1.blockZ());
         this.endOffsetX = var2 ? 0.0 : (this.random.nextDouble() * 2.0 - 1.0) * 0.18;
         this.endOffsetZ = var2 ? 0.0 : (this.random.nextDouble() * 2.0 - 1.0) * 0.18;
      }
   }

   public boolean isSprintJump() {
      return this.sprintJump;
   }

   private Vec3 waypointAt(int var1, double var2) {
      AStarPathfinder.Step var4 = this.path.get(var1);
      if (this.wobbleAmplitude == 0.0) {
         return new Vec3(var4.blockX() + 0.5, var2, var4.blockZ() + 0.5);
      }

      int var5 = var1 == this.path.size() - 1 && this.exactEnd ? 1 : 0;
      return new Vec3(
         var4.blockX() + 0.5 + (var5 != 0 ? this.endOffsetX : Math.sin(var1 * this.wobbleFreqX + this.wobblePhaseX) * this.wobbleAmplitude),
         var2,
         var4.blockZ() + 0.5 + (var5 != 0 ? this.endOffsetZ : Math.sin(var1 * this.wobbleFreqZ + this.wobblePhaseZ) * this.wobbleAmplitude)
      );
   }

   public boolean randomizeWobble() {
      return this.hasRemainingNodes() && !this.exactEnd;
   }

   public void reset() {
      if (this.breakingPos != null) {
         BlockBreaker.release(this.input);
      }

      this.path = List.of();
      this.nodeIndex = this.stuckTicks = this.breakTicks = this.reachFailures = 0;
      this.breakingPos = null;
      this.lastPosition = null;
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.braking = this.sprinting = (boolean)0;
      this.failureReason = "";
   }

   public boolean hasRemainingNodes() {
      return this.nodeIndex < this.path.size();
   }

   public List<AStarPathfinder.Step> getRemainingPath() {
      return this.nodeIndex < this.path.size() ? this.path.subList(this.nodeIndex, this.path.size()) : List.of();
   }

   public BlockPos getRemainingPath() {
      return this.breakingPos;
   }

   public static boolean hasEscapeRoute(int var0, int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      if (var3 == null) {
         return true;
      }

      for (Direction var5 : Plane.HORIZONTAL) {
         BlockPos var6 = new BlockPos(var0 + var5.getStepX(), var1, var2 + var5.getStepZ());
         if (isPassable(var3, var6) && isPassable(var3, var6.above()) && isPassable(var3, var6.below()) && isPassable(var3, var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private static boolean isPassable(Level var0, BlockPos var1) {
      return var0.hasChunkAt(var1) && var0.getBlockState(var1).getCollisionShape(var0, var1).isEmpty();
   }

   public int getBlocksBroken() {
      return this.blocksBroken;
   }

   public String getRemainingPath() {
      return this.failureReason;
   }

   public Vec3 getMoveDirection() {
      return this.moveDirection;
   }

   public boolean shouldJump() {
      return this.jumping;
   }

   public boolean isBraking() {
      return this.braking;
   }

   public boolean shouldSprint() {
      return this.sprinting;
   }

   public PathFollower.Result tick(double var1, ToIntFunction<BlockState> var3, boolean var4) {
      return this.step(var1, var3, var4, 0);
   }

   private PathFollower.Result step(double var1, ToIntFunction<BlockState> var3, boolean var4, int var5) {
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.braking = this.sprinting = this.sprintJump = (boolean)0;
      Minecraft var6 = Minecraft.getInstance();
      LocalPlayer var7 = var6.player;
      if (this.nodeIndex >= this.path.size()) {
         this.isWalkClear();
         return PathFollower.Result.ARRIVED;
      }

      AStarPathfinder.Step var8 = this.path.get(this.nodeIndex);

      for (AStarPathfinder.DigTarget var10 : var8.digs()) {
         BlockPos var11 = new BlockPos(var10.blockX(), var10.blockY(), var10.blockZ());
         if (BlockBreaker.canBreak(var11)) {
            if (!var11.equals(this.breakingPos)) {
               if (this.breakingPos != null) {
                  this.blocksBroken++;
               }

               this.breakingPos = var11;
               this.breakTicks = this.reachFailures = 0;
            }

            if (++this.breakTicks > 400) {
               return this.step("could not break " + var11.toShortString());
            }

            boolean var12 = BlockBreaker.breakBlock(this.input, var11, var1, true, 12.0F, var3, () -> true);
            if (!var12 && ++this.reachFailures > 20) {
               return this.step("no face of " + var11.toShortString() + " in reach");
            }

            return PathFollower.Result.BREAKING;
         }
      }

      if (this.breakingPos != null) {
         this.blocksBroken++;
         this.isWalkClear();
      }

      Vec3 var35 = var7.position();
      if (!var7.onGround() && !var7.isInWater()) {
         Vec3 var37 = new Vec3(var8.blockX() + 0.5, var35.y, var8.blockZ() + 0.5);
         if (this.wobbleAmplitude > 0.0) {
            while (
               this.nodeIndex + 1 < this.path.size()
                  && this.path.get(this.nodeIndex + 1).blockY() == this.path.get(this.nodeIndex).blockY()
                  && !this.path.get(this.nodeIndex + 1).digging()
                  && this.waypointAt(this.nodeIndex + 1, var35.y).distanceToSqr(var35) <= this.waypointAt(this.nodeIndex, var35.y).distanceToSqr(var35)
            ) {
               this.nodeIndex++;
            }

            var37 = this.waypointAt(Math.min(this.nodeIndex + 2, this.path.size() - 1), var35.y);
         }

         if (var37.subtract(var35).horizontalDistanceSqr() > 0.04) {
            this.moveDirection = var37.subtract(var35).multiply(1.0, 0.0, 1.0);
         }

         this.lastPosition = var35;
         return PathFollower.Result.AIRBORNE;
      } else {
         int var36;
         for (var36 = this.findShortcutIndex(var35); this.nodeIndex < var36 && this.hasRemainingNodes(var35, this.nodeIndex, var36); this.stuckTicks = 0) {
            this.nodeIndex++;
         }

         var8 = this.path.get(this.nodeIndex);
         int var38 = this.nodeIndex == this.path.size() - 1 ? 1 : 0;
         int var39 = var38 != 0 && this.exactEnd ? 1 : 0;
         Vec3 var13 = this.waypointAt(this.nodeIndex, var35.y);
         double var14 = Math.sqrt(var13.subtract(var35).horizontalDistanceSqr());
         int var16 = Math.abs(var35.y - var8.blockY()) < 0.6 ? 1 : 0;
         int var17 = var36 > this.nodeIndex ? 1 : 0;
         int var18 = var7.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? 1 : 0;
         if (var16 == 0 || !(var14 < (var39 != 0 ? 0.22 : (var17 != 0 ? 0.7 : 0.38))) && (var39 == 0 || var18 == 0 || !(var14 < 0.45))) {
            if (var35.y < var8.blockY() - 0.4 && var8.move() != AStarPathfinder.MoveType.ASCEND && var14 < 0.5) {
               return this.step("fell below the route");
            }

            double var19 = var14;

            for (int var21 = this.nodeIndex + 1; var21 < this.path.size(); var21++) {
               AStarPathfinder.Step var22 = this.path.get(var21 - 1);
               AStarPathfinder.Step var23 = this.path.get(var21);
               if (var23.digging()) {
                  break;
               }

               var19 += Math.hypot(var23.blockX() - var22.blockX(), var23.blockZ() - var22.blockZ());
            }

            Vec3 var40 = var17 != 0 ? this.waypointAt(var36, var35.y) : var13;
            Vec3 var41 = var40.subtract(var35).multiply(1.0, 0.0, 1.0);
            double var42 = var41.lengthSqr() < 1.0E-6 ? 0.0 : var7.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var41.normalize());
            int var25 = var8.move() == AStarPathfinder.MoveType.ASCEND && var35.y < var8.blockY() - 0.4 ? 1 : 0;
            this.braking = (boolean)(var39 != 0
                  && var19 < 0.9
                  && var8.move() != AStarPathfinder.MoveType.DESCEND
                  && var25 == 0
                  && hasEscapeRoute(var8.blockX(), var8.blockY(), var8.blockZ())
               ? 1
               : 0);
            double var26 = !this.exactEnd ? var19 + 3.0 : (this.braking ? var19 : var19 - 0.1);
            byte var28 = this.speedController.updateLatch(var26, var42, InteractionArbiter.ownsInteraction(this.input));
            if (var25 != 0) {
               var28 = 1;
               this.jumping = (boolean)(var14 < 1.4 ? 1 : 0);
            }

            if (var7.isInWater() && var8.blockY() >= Math.floor(var35.y)) {
               this.jumping = (boolean)1;
               if (var8.move() == AStarPathfinder.MoveType.SWIM_UP || var14 > 0.2) {
                  var28 = (byte)(var28 == 0 && !(var14 > 0.2) ? 0 : 1);
               }
            }

            this.moveDirection = var28 != 0 ? var41 : Vec3.ZERO;
            byte var29 = 0;

            for (int var30 = this.nodeIndex; var30 < Math.min(this.path.size(), this.nodeIndex + 3); var30++) {
               if (this.path.get(var30).digging()) {
                  var29 = 1;
               }
            }

            this.sprinting = (boolean)(var4
                  && var28 != 0
                  && !this.braking
                  && var29 == 0
                  && (var19 > 2.5 || !this.exactEnd)
                  && !var7.isInWater()
                  && var7.getFoodData().getFoodLevel() > 6
               ? 1
               : 0);
            this.sprintJump = (boolean)(var28 != 0 && var25 == 0 && var16 != 0 && var36 - this.nodeIndex >= 4 && var19 > 7.0 && var29 == 0 ? 1 : 0);
            if (var28 != 0 && var5 == 0) {
               this.moveTicks++;
               if (var7.isSprinting()) {
                  this.sprintTicks++;
               }
            }

            double var43 = var7.getEyeHeight() - 0.6;
            ArrayList var32 = new ArrayList(this.path.size() - this.nodeIndex);

            for (int var33 = this.nodeIndex; var33 < this.path.size(); var33++) {
               var32.add(new Vec3(this.path.get(var33).blockX() + 0.5, this.path.get(var33).blockY() + var43, this.path.get(var33).blockZ() + 0.5));
            }

            this.lookTarget.setTarget(lookAheadPoint(new Vec3(var35.x, var8.blockY() + var43, var35.z), var32, 3.0));
            if (var28 != 0 && this.lastPosition != null && var35.distanceToSqr(this.lastPosition) < 1.0E-4) {
               this.stuckTicks++;
            } else if (var28 != 0) {
               this.stuckTicks = 0;
            }

            this.lastPosition = var35;
            return this.stuckTicks > 40 ? this.step("movement blocked near " + BlockPos.containing(var35).toShortString()) : PathFollower.Result.MOVING;
         } else {
            this.nodeIndex++;
            this.stuckTicks = 0;
            if (this.nodeIndex >= this.path.size()) {
               return PathFollower.Result.ARRIVED;
            } else {
               return var5 < 4 ? this.step(var1, var3, var4, var5 + 1) : PathFollower.Result.MOVING;
            }
         }
      }
   }

   static Vec3 lookAheadPoint(Vec3 var0, List<Vec3> var1, double var2) {
      int var4 = 0;

      while (var4 + 1 < var1.size() && ((Vec3)var1.get(var4)).subtract(var0).dot(((Vec3)var1.get(var4 + 1)).subtract((Vec3)var1.get(var4))) < 0.0) {
         var4++;
      }

      Vec3 var5 = var0;
      Vec3 var6 = null;
      double var7 = var2;

      for (Vec3 var10 : var1.subList(var4, var1.size())) {
         Vec3 var11 = var10.subtract(var5);
         double var12 = var11.length();
         if (!(var12 < 1.0E-6)) {
            if (var12 >= var7) {
               return var5.add(var11.scale(var7 / var12));
            }

            var7 -= var12;
            var6 = var11.scale(1.0 / var12);
            var5 = var10;
         }
      }

      return var6 == null ? var5 : var5.add(var6.scale(var7));
   }

   private int findShortcutIndex(Vec3 var1) {
      ClientLevel var2 = Minecraft.getInstance().level;
      AStarPathfinder.Step var3 = this.path.get(this.nodeIndex);
      if (!var3.digging()
         && (var3.move() == AStarPathfinder.MoveType.WALK || var3.move() == AStarPathfinder.MoveType.DIAGONAL)
         && !(Math.abs(var1.y - var3.blockY()) > 0.6)) {
         int var4 = this.nodeIndex;

         for (int var5 = this.nodeIndex + 1; var5 < Math.min(this.path.size(), this.nodeIndex + 1 + 8); var4 = var5++) {
            AStarPathfinder.Step var6 = this.path.get(var5);
            if (var6.digging()
               || var6.blockY() != var3.blockY()
               || var6.move() != AStarPathfinder.MoveType.WALK && var6.move() != AStarPathfinder.MoveType.DIAGONAL) {
               break;
            }

            Vec3 var7 = this.waypointAt(var5, 0.0);
            if (!isWalkClear(var2, var1, var7.x, var7.z, var3.blockY())) {
               break;
            }
         }

         return var4;
      } else {
         return this.nodeIndex;
      }
   }

   private boolean hasRemainingNodes(Vec3 var1, int var2, int var3) {
      AStarPathfinder.Step var4 = this.path.get(var2);
      AStarPathfinder.Step var5 = this.path.get(var3);
      double var6 = Math.hypot(var5.blockX() - var4.blockX(), var5.blockZ() - var4.blockZ());
      double var8 = Math.hypot(var5.blockX() + 0.5 - var1.x, var5.blockZ() + 0.5 - var1.z);
      return var8 <= var6 + 0.05;
   }

   static boolean isWalkClear(Level var0, Vec3 var1, double var2, double var4, int var6) {
      double var7 = var2 - var1.x;
      double var9 = var4 - var1.z;
      double var11 = Math.hypot(var7, var9);
      if (var11 < 1.0E-6) {
         return true;
      }

      double var13 = -var9 / var11 * 0.31;
      double var15 = var7 / var11 * 0.31;
      MutableBlockPos var17 = new MutableBlockPos();

      for (double var18 = 0.0; var18 <= var11; var18 += 0.25) {
         double var20 = var1.x + var7 * var18 / var11;
         double var22 = var1.z + var9 * var18 / var11;

         for (int var24 = -1; var24 <= 1; var24++) {
            int var25 = (int)Math.floor(var20 + var13 * var24);
            int var26 = (int)Math.floor(var22 + var15 * var24);

            for (int var27 = 0; var27 <= 1; var27++) {
               BlockState var28 = var0.getBlockState(var17.set(var25, var6 + var27, var26));
               if (!var28.getCollisionShape(var0, var17).isEmpty() || !var28.getFluidState().isEmpty()) {
                  return false;
               }
            }

            BlockState var29 = var0.getBlockState(var17.set(var25, var6 - 1, var26));
            if (var29.getCollisionShape(var0, var17).isEmpty() || var29.is(Blocks.MAGMA_BLOCK)) {
               return false;
            }
         }
      }

      return true;
   }

   public double reset() {
      return this.moveTicks == 0 ? 0.0 : (double)this.sprintTicks / this.moveTicks;
   }

   private PathFollower.Result step(String var1) {
      this.isWalkClear();
      this.failureReason = var1;
      this.moveDirection = Vec3.ZERO;
      return PathFollower.Result.FAILED;
   }

   private void isWalkClear() {
      if (this.breakingPos != null) {
         BlockBreaker.release(this.input);
      }

      this.breakingPos = null;
      this.breakTicks = this.reachFailures = 0;
   }

   public interface LookTarget {
      void setTarget(Vec3 var1);
   }

   public enum Result {
      MOVING,
      BREAKING,
      AIRBORNE,
      ARRIVED,
      FAILED;
   }
}
