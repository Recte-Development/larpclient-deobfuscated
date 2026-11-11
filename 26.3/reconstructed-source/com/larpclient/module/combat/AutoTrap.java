package com.larpclient.module.combat;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.data.BlockActionTracker;
import com.larpclient.util.math.RotationSmoother;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Plane;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class AutoTrap extends NativePlacement.PlacementModule implements PlacementHighlight.PlacementHighlightHolder {
   private final PlacementHighlight placementHighlight = new PlacementHighlight(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static AutoTrap instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final ModeSetting blocks = this.addSetting(
      new ModeSetting("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final BooleanSetting onlyStanding = this.addBooleanSetting("Only Standing", "Pause self trapping while standing on the ground", true);
   private final ModeSetting trapMode = this.addSetting(
      new ModeSetting("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
         @Override
         public String getDisplayName() {
            return "Trap Pattern";
         }
      }
   );
   private final BooleanSetting trapPredict = this.addBooleanSetting("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final NumberSetting predictTicks = this.addNumberSetting("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final BooleanSetting predictSort = this.addBooleanSetting("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final NumberSetting placementPreview = this.addNumberSetting("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final NumberSetting turnSpeed = this.addNumberSetting("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final BlockActionTracker placementPlanner = new BlockActionTracker();
   private final RotationSmoother aimSmoother = new RotationSmoother();
   private Player target;
   private Object lastLevel;
   private Object lastPlayer;
   private BlockPos pendingPos;
   private int pendingTick;
   private Vec3 lastPosition;

   @Override
   public PlacementHighlight placementHighlight() {
      return this.placementHighlight;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.reset();
      this.placementHighlight.reset();
      LarpLog.info(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.mode.getValue(),
         this.placementPreview.getValue(),
         this.delayPerAction.getValue(),
         this.turnSpeed.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.reset();
      this.placementHighlight.clear();
   }

   private void reset() {
      ViewController.release(ViewController.Owner.AUTO_TRAP);
      this.placementPlanner.reset();
      this.aimSmoother.reset();
      this.target = null;
      this.pendingPos = null;
      this.lastLevel = this.lastPlayer = null;
      this.lastPosition = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (ViewController.canAcquire(ViewController.Owner.AUTO_TRAP)) {
            if (this.isBlatant()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.aimSmoother
                  .smooth(minecraft.player.tickCount, minecraft.player.getYRot(), minecraft.player.getXRot(), var1, var2, this.turnSpeed.getValue());
               RotationManager.request(RotationManager.Priority.DEFENSE, var3[0], var3[1], 1.0);
            }

            if (RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
               ViewController.acquire(ViewController.Owner.AUTO_TRAP);
            } else {
               ViewController.release(ViewController.Owner.AUTO_TRAP);
            }
         }
      }
   }

   public AutoTrap() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.mode.onChange(var1 -> {
         NativePlacement.cancel(this);
         this.reset();
         this.placementHighlight.clear();
      });
      this.rotationMode.visibleWhen(() -> !this.isBlatant());
      this.rotationMode.onChange(var1 -> {
         NativePlacement.cancel(this);
         this.reset();
      });
      this.delayPerAction.visibleWhen(() -> !this.isBlatant());
      this.placementPreview.visibleWhen(() -> !this.isBlatant());
      this.turnSpeed.visibleWhen(() -> !this.isBlatant() && this.rotationMode.isMode("Visible"));
      instance = this;
      this.predictTicks.visibleWhen(this.trapPredict::getValue);
      this.predictSort.visibleWhen(this.trapPredict::getValue);
      this.onlyStanding.visibleWhen(() -> false);
   }

   public boolean isBlatant() {
      return this.mode.isMode("Blatant");
   }

   private boolean getInstance() {
      return this.isBlatant() || this.rotationMode.isMode("Visible");
   }

   public boolean isAimActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.getInstance()
            && minecraft.player != null
            && this.lastLevel == minecraft.level
            && this.lastPlayer == minecraft.player
            && this.isValidTarget(this.target)
            && (this.placementPlanner.getPos() != null || this.pendingPos != null)
            && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem())
            && (!this.pauseMining.getValue() || !minecraft.options.keyAttack.isDown());
   }

   public float getAimYaw(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ViewController.update() == ViewController.Owner.AUTO_TRAP && RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
            minecraft.player.setYHeadRot(minecraft.player.getYRot());
            return minecraft.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean shouldPlace(int var1, boolean var2, boolean var3) {
      return !this.isEnabled() ? false : this.isBlatant() || var2 && this.placementPlanner.shouldFire(var1, this.placementPreview.getValue().intValue(), var3);
   }

   public List<Setting<?>> getPrimarySettings() {
      return List.of(this.mode, this.blocks, this.range, this.trapMode);
   }

   public List<Setting<?>> getTrapPositions() {
      return List.of(
         this.targetRange,
         this.rotationMode,
         this.autoSwitch,
         this.pauseUsingItem,
         this.pauseMining,
         this.delayPerAction,
         this.placementPreview,
         this.turnSpeed,
         this.trapPredict,
         this.predictTicks,
         this.predictSort,
         this.onlyStanding
      );
   }

   private boolean canPlaceAt(BlockPos var1, Item var2) {
      return NativePlacement.findPlacement(var1, var2, this.range.getValue(), this.getInstance()) != null;
   }

   public static AutoTrap getInstance() {
      return instance;
   }

   public Item getTrapItem() {
      return switch ((String)this.blocks.getValue()) {
         case "Crying Obsidian" -> Items.CRYING_OBSIDIAN;
         case "Ender Chest" -> Items.ENDER_CHEST;
         case "Stone" -> Items.STONE;
         case "Oak Planks" -> Items.OAK_PLANKS;
         case "Held Block" -> minecraft.player != null && minecraft.player.getMainHandItem().getItem() instanceof BlockItem
            ? minecraft.player.getMainHandItem().getItem()
            : Items.AIR;
         default -> Items.OBSIDIAN;
      };
   }

   public List<BlockPos> getTrapPositions(Player var1) {
      if (var1 == minecraft.player && this.onlyStanding.getValue() && var1.onGround()) {
         return List.of();
      }

      Vec3 var2 = predictPosition(var1, this.trapPredict.getValue() ? this.predictTicks.getValue() : 0.0);
      ArrayList var3 = new ArrayList<>(expandTrapPattern(expandTrapPattern(var1, var2), this.trapMode.getValue()));
      var3.sort(
         Comparator.comparingInt(Vec3i::getY)
            .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3.atCenterOf(var0).distanceToSqr(minecraft.player.getEyePosition())).reversed())
      );
      if (this.trapPredict.getValue() && this.predictSort.getValue()) {
         var3.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var2)));
      }

      return var3;
   }

   public static List<BlockPos> expandTrapPattern(List<BlockPos> var0, String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      if (!var1.equals("Head")) {
         for (BlockPos var4 : var0) {
            var2.add(var4.below());
         }
      }

      for (BlockPos var10 : var0) {
         for (Direction var6 : Plane.HORIZONTAL) {
            BlockPos var7 = var10.relative(var6);
            if (!var0.contains(var7)) {
               var2.add(var7);
               if (!var1.equals("Legs")) {
                  var2.add(var7.above());
               }
            }
         }
      }

      if (!var1.equals("Legs")) {
         for (BlockPos var11 : var0) {
            var2.add(var11.above(2));
         }
      }

      return List.copyOf(var2);
   }

   public static List<BlockPos> getTopSupportedPositions(List<BlockPos> var0) {
      if (var0.isEmpty()) {
         return List.of();
      }

      int var1 = var0.stream().mapToInt(Vec3i::getY).max().orElse(0);
      LinkedHashSet var2 = new LinkedHashSet();

      for (BlockPos var4 : var0) {
         if (var4.getY() == var1) {
            for (Direction var6 : Plane.HORIZONTAL) {
               BlockPos var7 = var4.relative(var6);
               if (!var0.contains(var7) && var0.contains(var7.below())) {
                  var2.add(var7);
               }
            }
         }
      }

      return List.copyOf(var2);
   }

   public static List<BlockPos> getRoofPositions(List<BlockPos> var0, Vec3 var1) {
      return getTopSupportedPositions(var0)
         .stream()
         .sorted(Comparator.<BlockPos>comparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var1)).reversed())
         .toList();
   }

   static Vec3 predictPosition(Player var0, double var1) {
      Vec3 var3 = var0.getDeltaMovement();
      if (var3.horizontalDistanceSqr() < 1.0E-8) {
         var3 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      }

      return var0.position().add(var3.scale(var1));
   }

   static List<BlockPos> expandTrapPattern(Player var0, Vec3 var1) {
      AABB var2 = var0.getBoundingBox().move(var1.subtract(var0.position()));
      int var3 = (int)Math.floor(var1.y + 0.01);
      ArrayList var4 = new ArrayList(4);

      for (int var5 = (int)Math.floor(var2.minX + 0.001); var5 <= (int)Math.floor(var2.maxX - 0.001); var5++) {
         for (int var6 = (int)Math.floor(var2.minZ + 0.001); var6 <= (int)Math.floor(var2.maxZ - 0.001); var6++) {
            var4.add(new BlockPos(var5, var3, var6));
         }
      }

      return var4;
   }

   private boolean needsRoof(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.trapMode.isMode("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && NativePlacement.isReplaceable(var1x));
   }

   private boolean isTrapPosition(Player var1, BlockPos var2) {
      List var3 = this.getTrapPositions(var1);
      return var3.contains(var2) || this.needsRoof(var3) && getTopSupportedPositions(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.placementHighlight.setTarget(null);
      NativePlacement.cancel(this);
      if (!this.canInteract()) {
         this.placementPlanner.reset();
         this.aimSmoother.reset();
      } else if (this.getInstance() && ViewController.update() != ViewController.Owner.NONE && ViewController.update() != ViewController.Owner.AUTO_TRAP) {
         this.placementPlanner.reset();
         this.aimSmoother.reset();
         this.setStatus("Waiting for camera control");
      } else {
         if (this.lastLevel != minecraft.level
            || this.lastPlayer != minecraft.player
            || this.lastPosition != null && this.lastPosition.distanceToSqr(minecraft.player.position()) > 64.0) {
            this.reset();
         }

         this.lastLevel = minecraft.level;
         this.lastPlayer = minecraft.player;
         this.lastPosition = minecraft.player.position();
         if (!this.isValidTarget(this.target)) {
            this.target = this.findTarget();
            this.placementPlanner.reset();
            this.aimSmoother.reset();
         }

         if (this.target == null) {
            this.setStatus("Waiting for a target");
         } else {
            Item var1 = this.getTrapItem();
            if (var1 != Items.AIR) {
               int var10000 = NativePlacement.findItemSlot(var1, this.autoSwitch.getValue());
               byte var12 = -1;
               int var11 = var10000;
               var10000 = Integer.compare(var11, var12);
               if ((var10000 | -var10000) >>> 31 != 0) {
                  if (this.pendingPos != null) {
                     if (!NativePlacement.isReplaceable(this.pendingPos)) {
                        this.pendingPos = null;
                     } else {
                        int var14 = this.pendingTick;
                        int var13 = minecraft.player.tickCount;
                        var10000 = var13 - var14;
                        byte var16 = 10;
                        int var15 = var10000;
                        if ((Integer.compare(var15, var16) >>> 31 ^ 1) == 0) {
                           this.placementHighlight.setTarget(this.pendingPos);
                           this.setStatus("Waiting for world update");
                           return;
                        }

                        this.pendingPos = null;
                        this.placementPlanner.reset();
                        this.setStatus("Block update missing; previewing a retry");
                     }
                  }

                  List var2 = this.getTrapPositions(this.target);
                  ArrayList var3 = new ArrayList(var2);
                  int var23 = this.needsRoof(var2);
                  boolean var10 = false;
                  byte var9 = (byte)var23;
                  var23 = Integer.signum(var9);
                  if (((var23 | -var23) >>> 31 ^ 1) == 0) {
                     var3.addAll(getRoofPositions(var2, minecraft.player.getEyePosition()));
                  }

                  BlockPos var4 = this.placementPlanner.getPos();
                  if (var4 == null || !var3.contains(var4) || !this.canPlaceAt(var4, var1)) {
                     var4 = null;

                     for (BlockPos var6 : var3) {
                        if (this.canPlaceAt(var6, var1)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (var4 != null) {
                     BlockPos var18 = var4;
                     Player var20 = this.target;
                     this.placementPlanner.track(var18, var20, var1, minecraft.player.tickCount);
                     this.placementHighlight.setTarget(var18);
                     BlockHitResult var7 = NativePlacement.findPlacement(var18, var1, this.range.getValue(), this.getInstance());
                     if (var7 == null) {
                        this.placementPlanner.reset();
                        return;
                     }

                     boolean var8 = NativePlacement.pruneInactive(
                        this,
                        var18,
                        var1,
                        this.range.getValue(),
                        this.getInstance(),
                        this.autoSwitch.getValue(),
                        () -> this.canInteract()
                           && this.target == var20
                           && this.isValidTarget(var20)
                           && this.getTrapItem() == var1
                           && this.isTrapPosition(var20, var18),
                        () -> {
                           float[] var2x = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var7.getLocation());
                           int var3x = Math.abs(Mth.wrapDegrees(var2x[0] - minecraft.player.getYRot())) < 2.0F
                                 && Math.abs(var2x[1] - minecraft.player.getXRot()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.getInstance() || ViewController.update() == ViewController.Owner.AUTO_TRAP)
                              && this.shouldPlace(minecraft.player.tickCount, this.isReady(), (boolean)var3x);
                        },
                        var2x -> {
                           this.retryLater(var2x);
                           if (var2x) {
                              this.pendingPos = var18;
                              this.pendingTick = minecraft.player.tickCount;
                           } else {
                              this.placementPlanner.reset();
                           }
                        }
                     );
                     if (var8) {
                        this.setStatus("Preparing trap placement");
                     }

                     return;
                  }

                  this.placementPlanner.reset();
                  this.aimSmoother.reset();
                  boolean var17 = var2.stream().noneMatch(NativePlacement::isReplaceable);
                  int var19 = !this.trapMode.isMode("Legs")
                        && var2.stream()
                           .filter(NativePlacement::isReplaceable)
                           .allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0))
                     ? 1
                     : 0;
                  this.setStatus(
                     var17
                        ? "Trap enclosure filled"
                        : (
                           var19 != 0
                              ? "Roof needs a higher view; jump or move to expose a supported face"
                              : "Move closer or around the target to reach the remaining trap faces"
                        )
                  );
                  return;
               }
            }

            this.placementPlanner.reset();
            this.aimSmoother.reset();
            this.setStatus("Trap blocks missing from hotbar");
         }
      }
   }
}
