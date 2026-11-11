package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.data.PendingActionTracker;
import com.larpclient.util.entity.ExplosionDamage2;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.math.RotationLimiter;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoCrystalModule extends Module implements PlacementHighlight.PlacementHighlightHolder {
   private final PlacementHighlight placementHighlight;
   private LocalPlayer lastPlayer;
   private ClientLevel lastLevel;
   private final NumberSetting range = this.addNumberSetting("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final NumberSetting targetRange = this.addNumberSetting("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final NumberSetting breakDelay = this.addNumberSetting("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final NumberSetting placeDelay = this.addNumberSetting("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final NumberSetting minDamage = this.addNumberSetting(
      "Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5
   );
   private final NumberSetting facePlaceHealth = this.addNumberSetting(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final NumberSetting predictTicks = this.addNumberSetting(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final NumberSetting maxSelfDamage = this.addNumberSetting("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting antiSelf = this.addNumberSetting("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final BooleanSetting targetPlayers = this.addNumberSetting("Players", "Target other survival players", true);
   private final BooleanSetting targetMobs = this.addNumberSetting("Mobs", "Also target hostile mobs for combat or testing", false);
   private final BooleanSetting autoSwitch = this.addNumberSetting("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final BooleanSetting pauseUsingItem = this.addNumberSetting("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final BooleanSetting place = this.addNumberSetting("Place", "Place crystals on existing obsidian or bedrock", true);
   private final BooleanSetting placeObsidian = this.addNumberSetting(
      "Place Obsidian", "Place an obsidian base first when no existing base is good enough", true
   );
   private final ModeSetting obsidianMode = this.addSetting(
      new ModeSetting(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final NumberSetting obsidianGain = this.addNumberSetting(
      "Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5
   );
   private final NumberSetting obsidianDelay = this.addNumberSetting(
      "Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0
   );
   private final BooleanSetting breakCrystals = this.addNumberSetting("Break", "Attack crystals that can damage a target", true);
   private final ModeSetting rotationMode = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final NumberSetting turnSpeed = this.addNumberSetting("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final NumberSetting aimHold = this.addNumberSetting("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final NumberSetting placeRange = this.addNumberSetting("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final NumberSetting breakRange = this.addNumberSetting("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final NumberSetting scanInterval = this.addNumberSetting(
      "Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0
   );
   private final NumberSetting responseTimeout = this.addNumberSetting(
      "Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0
   );
   private final BooleanSetting pauseWeakness = this.addNumberSetting("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final BooleanSetting pauseOnKill = this.addNumberSetting(
      "Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false
   );
   private final NumberSetting killPause = this.addNumberSetting("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final BooleanSetting workWithCrystal = this.addNumberSetting("Work with Crystal", "Start while holding an end crystal", true);
   private final BooleanSetting workWithSword = this.addNumberSetting("Work with Sword", "Start while holding a sword", true);
   private final BooleanSetting workWithTotem = this.addNumberSetting("Work with Totem", "Start while holding a totem", true);
   private final BooleanSetting workWithFist = this.addNumberSetting("Work with Fist", "Start with an empty main hand", true);
   private final NumberSetting switchDelayMin = this.addDelaySetting("Switch Delay Min", 1.0);
   private final NumberSetting switchDelayMax = this.addDelaySetting("Switch Delay Max", 50.0);
   private final NumberSetting placeDelayMin = this.addDelaySetting("Place Delay Min", 50.0);
   private final NumberSetting placeDelayMax = this.addDelaySetting("Place Delay Max", 100.0);
   private final NumberSetting breakDelayMin = this.addDelaySetting("Break Delay Min", 50.0);
   private final NumberSetting breakDelayMax = this.addDelaySetting("Break Delay Max", 100.0);
   private final BooleanSetting swapBack = this.addNumberSetting("Swap Back", "Restore your starting slot after releasing use", true);
   private final NumberSetting swapBackDelayMin = this.addDelaySetting("Swap Back Delay Min", 50.0);
   private final NumberSetting swapBackDelayMax = this.addDelaySetting("Swap Back Delay Max", 100.0);
   private static final double[][] aimOffsets = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] faceDirections = new Direction[]{
      Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN
   };
   private static final int maxObsidianEvaluations = 24;
   private static final int maxObsidianCandidates = 96;
   public static final double facePlaceMinDamage = 1.5;
   private AutoCrystalModule.CrystalAction currentAction;
   private long obsidianCooldownUntil;
   private String obsidianFailureReason = "";
   private final Map<LivingEntity, Vec3> predictedOffsets = new IdentityHashMap<>();
   private BlockPos targetBase;
   private LivingEntity lastVictim;
   private Object rotationPlayer;
   private Object rotationLevel;
   private int rotationStartTick;
   private String lastStatusKey = "";
   private Object lastActionKey;
   private LivingEntity lastActionVictim;
   private long actionTick;
   private long killPauseUntil;
   private final RotationLimiter aimTracker = new RotationLimiter();
   private static AutoCrystalModule instance;
   private final PendingActionTracker pendingActions = new PendingActionTracker();
   private String status = "Waiting for a target";

   @Override
   public PlacementHighlight placementHighlight() {
      return this.placementHighlight;
   }

   public static AutoCrystalModule placeCrystal() {
      return instance;
   }

   public List<Setting<?>> addNumberSetting() {
      return List.of(
         this.place,
         this.breakCrystals,
         this.range,
         this.placeRange,
         this.breakRange,
         this.placeDelay,
         this.breakDelay,
         this.responseTimeout,
         this.scanInterval,
         this.autoSwitch
      );
   }

   public List<Setting<?>> getObsidianSettings() {
      return List.of(this.placeObsidian, this.obsidianMode, this.obsidianGain, this.obsidianDelay);
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.targetPlayers, this.targetMobs, this.targetRange, this.predictTicks);
   }

   public List<Setting<?>> updateAim() {
      return List.of(this.rotationMode, this.turnSpeed, this.aimHold);
   }

   public List<Setting<?>> scoreCrystal() {
      return List.of(
         this.minDamage, this.facePlaceHealth, this.antiSelf, this.maxSelfDamage, this.pauseUsingItem, this.pauseWeakness, this.pauseOnKill, this.killPause
      );
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      instance = this;
      this.turnSpeed.visibleWhen(() -> this.rotationMode.isMode("Visible"));
      this.placeRange.visibleWhen(this.place::getValue);
      this.placeDelay.visibleWhen(this.place::getValue);
      this.placeObsidian.visibleWhen(this.place::getValue);
      this.obsidianMode.visibleWhen(() -> this.place.getValue() && this.placeObsidian.getValue());
      this.obsidianGain.visibleWhen(() -> this.place.getValue() && this.placeObsidian.getValue() && this.obsidianMode.isMode("Best"));
      this.obsidianDelay.visibleWhen(() -> this.place.getValue() && this.placeObsidian.getValue());
      this.breakRange.visibleWhen(this.breakCrystals::getValue);
      this.breakDelay.visibleWhen(this.breakCrystals::getValue);
      this.maxSelfDamage.visibleWhen(this.antiSelf::getValue);
      this.killPause.visibleWhen(this.pauseOnKill::getValue);
      this.pauseOnKill.onChange(var1 -> {
         if (!var1) {
            this.killPauseUntil = 0L;
         }
      });
      this.rotationMode.onChange(var1 -> {
         this.cancelAction();
         this.releaseView();
      });

      for (Setting var2 : List.of(
         this.workWithCrystal,
         this.workWithSword,
         this.workWithTotem,
         this.workWithFist,
         this.switchDelayMin,
         this.switchDelayMax,
         this.placeDelayMin,
         this.placeDelayMax,
         this.breakDelayMin,
         this.breakDelayMax,
         this.swapBack,
         this.swapBackDelayMin,
         this.swapBackDelayMax
      )) {
         var2.visibleWhen(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.placementHighlight = new PlacementHighlight(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting addNumberSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   private NumberSetting addDelaySetting(String var1, double var2) {
      return this.addNumberSetting(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long onEntityEvent(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
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
      this.placementHighlight.reset();
      LarpLog.info(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.placeObsidian.getValue() ? this.obsidianMode.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.reset();
      LarpLog.info("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void reset() {
      this.pendingActions.prune();
      this.cancelAction();
      this.releaseView();
      this.placementHighlight.clear();
      this.lastPlayer = null;
      this.lastLevel = null;
      this.targetBase = null;
      this.lastVictim = null;
      this.killPauseUntil = 0L;
      this.obsidianCooldownUntil = 0L;
      this.predictedOffsets.clear();
      this.status = "Waiting for a target";
   }

   private boolean canRun() {
      return this.isRuntimeActive()
         && this.isInGame()
         && InteractionArbiter.canInteract()
         && minecraft.isWindowActive()
         && this.describeFailure()
         && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem())
         && (!this.pauseWeakness.getValue() || !this.cancelAction());
   }

   private boolean describeFailure() {
      ViewController.Owner var1 = ViewController.update();
      return this.rotationMode.isMode("Visible") ? ViewController.canAcquire(ViewController.Owner.AUTO_CRYSTAL) : var1 == ViewController.Owner.NONE;
   }

   private void releaseView() {
      this.rotationPlayer = this.rotationLevel = null;
      ViewController.release(ViewController.Owner.AUTO_CRYSTAL);
   }

   public boolean isRotating() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.rotationMode.isMode("Visible")
         && minecraft.player != null
         && this.rotationPlayer == minecraft.player
         && this.rotationLevel == minecraft.level
         && minecraft.player.tickCount >= this.rotationStartTick
         && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem())) {
         int var1 = minecraft.player.tickCount - this.rotationStartTick;
         return var1 <= 1 || (this.pendingActions.hasPendingBlock() || this.pendingActions.hasPendingEntities()) && var1 <= this.responseTimeout.getValue();
      } else {
         return false;
      }
   }

   public float isActionValid(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ViewController.update() == ViewController.Owner.AUTO_CRYSTAL) {
            minecraft.player.setYHeadRot(minecraft.player.getYRot());
            return minecraft.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean isTargetable(LivingEntity var1) {
      return var1.isAlive() && !var1.isRemoved() && this.isValidTarget(var1);
   }

   private boolean isValidTarget(LivingEntity var1) {
      if (var1 == minecraft.player
         || var1.isSpectator()
         || var1.level() != minecraft.level
         || minecraft.player.distanceTo(var1) > this.targetRange.getValue()
         || minecraft.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.targetPlayers.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
            return var3 == null || !var3.isFriend(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.targetMobs.getValue() && var1 instanceof Enemy;
      }
   }

   public static double minDamageThreshold(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean shouldPlaceObsidian(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
      if (Double.isNaN(var1)) {
         return true;
      }

      if (var3) {
         return false;
      }

      return (boolean)(switch (var0) {
         case "Lethal" -> var6;
         case "Damage" -> 0;
         default -> var6 == 0 && !(var4 >= var1 + var7) ? 0 : 1;
      });
   }

   private AutoCrystalModule.TargetScore isTargetable(Vec3 var1, List<LivingEntity> var2) {
      return this.scoreCrystal(var1, var2, null, null, false, false);
   }

   private static double onEntityEvent(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private double getMinDamage(LivingEntity var1) {
      return minDamageThreshold(onEntityEvent(var1), this.minDamage.getValue(), this.facePlaceHealth.getValue());
   }

   private Vec3 predictOffset(LivingEntity var1) {
      int var2 = this.predictTicks.getValue().intValue();
      return var2 <= 0 ? Vec3.ZERO : this.predictedOffsets.computeIfAbsent(var1, var1x -> {
         Vec3 var2x = new Vec3(var1x.getX() - var1x.xo, 0.0, var1x.getZ() - var1x.zo);
         if (!(var2x.lengthSqr() < 1.0E-6) && !(var2x.lengthSqr() > 1.0)) {
            Vec3 var3 = var2x.scale(var2);
            return minecraft.level.noCollision(var1x, var1x.getBoundingBox().move(var3)) ? var3 : Vec3.ZERO;
         } else {
            return Vec3.ZERO;
         }
      });
   }

   private AutoCrystalModule.TargetScore scoreCrystal(
      Vec3 var1, List<LivingEntity> var2, AutoCrystalModule.SearchStats var3, BlockPos var4, boolean var5, boolean var6
   ) {
      double var7 = ExplosionDamage2.damageToWithBlock(minecraft.player, var1, var4);
      if (this.antiSelf.getValue()
         && !ExplosionDamage2.isWithinRange(var7, this.maxSelfDamage.getValue(), minecraft.player.getHealth() + minecraft.player.getAbsorptionAmount())) {
         if (var3 != null) {
            var3.selfDamageRejected++;
            var3.lowestSelfDamage = Math.min(var3.lowestSelfDamage, var7);
         }

         return null;
      } else {
         AutoCrystalModule.TargetScore var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.isTargetable(var11)) {
               double var12 = ExplosionDamage2.damageToWithBlockAndOffset(var11, var1, var4, var5 ? this.predictOffset(var11) : Vec3.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.getMinDamage(var11))) {
                  if (!var6 || !(var12 < onEntityEvent(var11))) {
                     AutoCrystalModule.TargetScore var14 = new AutoCrystalModule.TargetScore(var11, var12, var7, var12 >= onEntityEvent(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null
                     && (Double.isNaN(var3.bestLethalHealth) || var12 - onEntityEvent(var11) > var3.bestLethalDamage - var3.bestLethalHealth)) {
                     var3.bestLethalDamage = var12;
                     var3.bestLethalHealth = onEntityEvent(var11);
                  }
               } else if (var3 != null) {
                  var3.bestTargetDamage = Math.max(var3.bestTargetDamage, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3> reset(EndCrystal var1, Vec3 var2) {
      return InteractionArbiter.findVisibleAimPoint(var1, this.getBreakRange(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static AABB crystalBox(BlockPos var0) {
      return EntityTypes.END_CRYSTAL.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(var0.above()));
   }

   public static List<Vec3> aimPoints(BlockPos var0, Vec3 var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3 var3 = Vec3.atCenterOf(var0);

      for (Direction var7 : faceDirections) {
         int var8 = var7.getStepX();
         int var9 = var7.getStepY();
         int var10 = var7.getStepZ();
         if (!((var1.x - var3.x - var8 * 0.5) * var8 + (var1.y - var3.y - var9 * 0.5) * var9 + (var1.z - var3.z - var10 * 0.5) * var10 <= 0.0)) {
            for (double[] var14 : aimOffsets) {
               double var15 = var14[0];
               double var17 = var14[1];

               var2.add(switch (var7.getAxis()) {
                  case Y -> new Vec3(var0.getX() + var15, var0.getY() + (var9 > 0 ? 1 : 0), var0.getZ() + var17);
                  case X -> new Vec3(var0.getX() + (var8 > 0 ? 1 : 0), var0.getY() + var15, var0.getZ() + var17);
                  case Z -> new Vec3(var0.getX() + var15, var0.getY() + var17, var0.getZ() + (var10 > 0 ? 1 : 0));
                  default -> throw new MatchException(null, null);
               });
            }
         }
      }

      return var2;
   }

   @Override
   public void onTick() {
      this.placementHighlight.setTarget(null);
      if (!this.isInGame()) {
         this.reset();
      } else {
         if (this.lastPlayer != minecraft.player || this.lastLevel != minecraft.level) {
            this.reset();
            this.lastPlayer = minecraft.player;
            this.lastLevel = minecraft.level;
         }

         this.runTick();
         int var10000 = this.status.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.status.substring(0, this.status.indexOf(58)) : this.status;
         int var7 = var1.equals(this.lastStatusKey);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.lastStatusKey = var1;
            LarpLog.debug("AutoCrystal", "Status: {}", this.status);
         }
      }
   }

   private void runTick() {
      this.predictedOffsets.clear();
      if (!this.canRun()) {
         this.cancelAction();
         this.status = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = minecraft.level.getGameTime();
         if (var1 < this.killPauseUntil) {
            this.cancelAction();
            this.status = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.killPauseUntil - var1) / 20.0);
         } else {
            this.pendingActions
               .prune(
                  var1,
                  var0 -> minecraft.level.getEntity(var0) != null,
                  var0 -> !minecraft.level
                     .getEntities((Entity)null, new AABB(var0.above()).expandTowards(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystal)
                     .isEmpty()
               );
            if (!this.pendingActions.hasPendingBlock() && !this.pendingActions.hasPendingEntities()) {
               this.targetBase = null;
            } else {
               this.placementHighlight.setTarget(this.targetBase);
               if (this.lastVictim != null && this.isTargetable(this.lastVictim)) {
                  VisualsRenderer.setLastTarget(this.lastVictim);
               }
            }

            if (this.currentAction != null && var1 - this.currentAction.created() < this.scanInterval.getValue() && this.isActionValid(this.currentAction)) {
               this.executeAction(this.currentAction, var1);
            } else {
               this.currentAction = null;
               ArrayList var3 = new ArrayList();

               for (Entity var5 : minecraft.level.entitiesForRendering()) {
                  if (var5 instanceof LivingEntity var6 && this.isTargetable(var6)) {
                     var3.add(var6);
                  }
               }

               var3.sort(Comparator.comparingDouble(minecraft.player::distanceToSqr));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.status = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.breakCrystals.getValue() && !this.cancelAction()) {
                     EndCrystal var14 = null;
                     AutoCrystalModule.TargetScore var16 = null;
                     Vec3 var18 = null;

                     for (Entity var8 : minecraft.level.entitiesForRendering()) {
                        if (var8 instanceof EndCrystal var9
                           && this.pendingActions.canTargetEntity(var1, var8.getId())
                           && !(InteractionArbiter.claimManualInteraction(var8) > this.getBreakRange())) {
                           AutoCrystalModule.TargetScore var10 = this.isTargetable(var8.position(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.reset(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (Vec3)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.currentAction = new AutoCrystalModule.CrystalAction(null, var14, var16.target(), var1, var18, null);
                        this.executeAction(this.currentAction, var1);
                        return;
                     }
                  }

                  if (this.pendingActions.hasPendingEntities()) {
                     this.status = "Waiting for crystal removal";
                  } else if (!this.place.getValue()) {
                     this.status = "No suitable crystal to break";
                  } else if (this.pendingActions.hasPendingBlock()) {
                     this.status = "Waiting for crystal spawn";
                  } else if (!this.pendingActions.canTargetBlock(var1)) {
                     this.status = "Placement cooldown";
                  } else if (this.findCrystalSlot() == -1) {
                     this.status = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystalModule.TargetScore var17 = null;
                     AutoCrystalModule.SearchStats var19 = new AutoCrystalModule.SearchStats();
                     int var20 = (int)Math.ceil(this.isTargetable());
                     BlockPos var21 = BlockPos.containing(minecraft.player.getEyePosition());

                     for (BlockPos var24 : BlockPos.betweenClosed(var21.offset(-var20, -var20, -var20), var21.offset(var20, var20, var20))) {
                        BlockPos var25 = var24.immutable();
                        BlockHitResult var12 = this.findPlacement(var25, null);
                        if (var12 != null) {
                           var19.basesChecked++;
                           AutoCrystalModule.TargetScore var13 = this.scoreCrystal(Vec3.atBottomCenterOf(var25.above()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.placeObsidian.getValue() && (var17 == null || !var17.lethal() && !this.obsidianMode.isMode("Damage"))) {
                        AutoCrystalModule.CrystalAction var23 = this.planObsidian(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.currentAction = var23;
                           this.executeAction(this.currentAction, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.status = this.obsidianFailureReason;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.status = this.describeFailure(var19);
                     } else {
                        this.currentAction = new AutoCrystalModule.CrystalAction(var15.getBlockPos(), null, var17.target(), var1, var15.getLocation(), null);
                        this.executeAction(this.currentAction, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.CrystalAction planObsidian(
      List<LivingEntity> var1, AutoCrystalModule.TargetScore var2, long var3, AutoCrystalModule.SearchStats var5
   ) {
      boolean var6 = this.obsidianMode.isMode("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.obsidianCooldownUntil) {
         this.obsidianFailureReason = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (NativePlacement.findItemSlot(Items.OBSIDIAN, this.autoSwitch.getValue()) == -1) {
         this.obsidianFailureReason = this.describeFailure(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      Vec3 var9 = minecraft.player.getEyePosition();
      double var10 = this.isTargetable();
      int var12 = (int)Math.ceil(var10);
      BlockPos var13 = BlockPos.containing(var9);
      ArrayList var14 = new ArrayList();

      for (BlockPos var16 : BlockPos.betweenClosed(var13.offset(-var12, -var12, -var12), var13.offset(var12, var12, var12))) {
         if (this.canPlaceObsidianAt(var16, var9, var10)) {
            Vec3 var17 = Vec3.atBottomCenterOf(var16.above());
            double var18 = Double.MAX_VALUE;

            for (LivingEntity var21 : var1) {
               double var22 = var21.position().add(this.predictOffset(var21)).distanceTo(var17);
               double var24 = var6 ? onEntityEvent(var21) : this.getMinDamage(var21);
               if (var2 != null && this.obsidianMode.isMode("Best")) {
                  var24 = Math.max(var24, Math.min(onEntityEvent(var21), var7 + this.obsidianGain.getValue()));
               }

               if (ExplosionDamage2.rawDamage(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.BaseCandidate(var16.immutable(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.BaseCandidate::distance));
      AutoCrystalModule.SearchStats var26 = new AutoCrystalModule.SearchStats();
      AutoCrystalModule.TargetScore var27 = null;
      BlockHitResult var28 = null;
      BlockPos var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.BaseCandidate var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         BlockPos var32 = var31.pos();
         if (this.isAreaClear(var32) && this.isRotating(var32, var9, var10)) {
            BlockHitResult var23 = NativePlacement.findPlacementWithSwitch(var32, Items.OBSIDIAN, var10, true, this.autoSwitch.getValue());
            if (var23 != null) {
               var26.basesChecked++;
               var19++;
               AutoCrystalModule.TargetScore var33 = this.scoreCrystal(Vec3.atBottomCenterOf(var32.above()), var1, var26, var32, true, var6);
               if (var33 != null
                  && shouldPlaceObsidian(this.obsidianMode.getValue(), var7, false, var33.damage(), var33.lethal(), this.obsidianGain.getValue())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.obsidianFailureReason = this.describeObsidianFailure(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.CrystalAction(var29, null, var27.target(), var3, var28.getLocation(), var28);
      }
   }

   private boolean canPlaceObsidianAt(BlockPos var1, Vec3 var2, double var3) {
      if (!(var2.distanceToSqr(Vec3.atCenterOf(var1)) > (var3 + 1.0) * (var3 + 1.0))
         && NativePlacement.isReplaceable(var1)
         && minecraft.level.isEmptyBlock(var1.above())) {
         if (this.breakCrystals.getValue() && BoxRaycastUtil.distanceToBox(var2, crystalBox(var1)) > this.getBreakRange()) {
            return false;
         }

         for (Direction var8 : Direction.values()) {
            if (NativePlacement.getFailureReason(var1.relative(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean isAreaClear(BlockPos var1) {
      return minecraft.level.getEntities((Entity)null, new AABB(var1).expandTowards(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean isRotating(BlockPos var1, Vec3 var2, double var3) {
      for (Vec3 var6 : aimPoints(var1, var2)) {
         if (!(var2.distanceToSqr(var6) > var3 * var3)
            && minecraft.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.NONE, minecraft.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult revalidateSupport(BlockHitResult var1, BlockPos var2, Vec3 var3) {
      BlockHitResult var4 = NativePlacement.raycastLook(var1.getBlockPos(), var3, this.isTargetable());
      if (var4 != null && var4.getDirection() == var1.getDirection() && NativePlacement.getFailureReason(var4.getBlockPos())) {
         NativePlacement.SimulatedPlacement var5 = NativePlacement.simulatePlacement(var4, Items.OBSIDIAN, this.autoSwitch.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String describeObsidianFailure(AutoCrystalModule.SearchStats var1, boolean var2) {
      if (var1.basesChecked == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.bestLethalHealth) && var1.selfDamageRejected < var1.basesChecked
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.bestLethalDamage, var1.bestLethalHealth)
            : this.describeFailure(var1);
      }
   }

   public void onEntityEvent(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (var1.getEventId() == 3 && this.pauseOnKill.getValue() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(minecraft.level) instanceof LivingEntity var2 && this.isValidTarget(var2)) {
               this.killPauseUntil = minecraft.level.getGameTime() + Math.round(this.killPause.getValue() * 20.0);
               this.pendingActions.prune();
               this.cancelAction();
               this.releaseView();
               this.targetBase = null;
               this.lastVictim = null;
               this.status = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.killPause.getValue());
               LarpLog.info(
                  "AutoCrystal",
                  "{} died nearby; placing and breaking paused for {} s to protect drops",
                  var2.getName().getString(),
                  this.killPause.getValue().intValue()
               );
            }
         }
      }
   }

   private String describeFailure(AutoCrystalModule.SearchStats var1) {
      if (var1.basesChecked == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.selfDamageRejected == var1.basesChecked
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.lowestSelfDamage, this.maxSelfDamage.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.bestTargetDamage, this.minDamage.getValue());
      }
   }

   private void cancelAction() {
      this.currentAction = null;
      this.lastActionKey = null;
      this.lastActionVictim = null;
      this.aimTracker.reset();
      InteractionArbiter.findAttackBlocker(this);
   }

   private double isTargetable() {
      return Math.min(this.range.getValue(), Math.min(this.placeRange.getValue(), minecraft.player.blockInteractionRange()));
   }

   private double getBreakRange() {
      return Math.min(this.range.getValue(), Math.min(this.breakRange.getValue(), minecraft.player.entityInteractionRange()));
   }

   private boolean cancelAction() {
      return minecraft.player.hasEffect(MobEffects.WEAKNESS) && minecraft.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean isActionValid(AutoCrystalModule.CrystalAction var1) {
      if (this.canRun() && this.isTargetable(var1.victim())) {
         long var2 = minecraft.level.getGameTime();
         if (var1.crystal() != null) {
            return this.breakCrystals.getValue()
               && !this.cancelAction()
               && this.pendingActions.canTargetEntity(var2, var1.crystal().getId())
               && this.reset(var1.crystal(), var1.aim()).isPresent()
               && this.isTargetable(var1.crystal().position(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3 var4 = minecraft.player.getEyePosition();
            return this.place.getValue()
               && this.placeObsidian.getValue()
               && this.pendingActions.canTargetBlock(var2)
               && var2 >= this.obsidianCooldownUntil
               && this.findCrystalSlot() != -1
               && NativePlacement.findItemSlot(Items.OBSIDIAN, this.autoSwitch.getValue()) != -1
               && this.canPlaceObsidianAt(var1.base(), var4, this.isTargetable())
               && this.isAreaClear(var1.base())
               && this.isRotating(var1.base(), var4, this.isTargetable())
               && this.revalidateSupport(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.scoreCrystal(
                     Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, var1.base(), true, this.obsidianMode.isMode("Lethal")
                  )
                  != null;
         } else {
            return this.place.getValue()
               && this.pendingActions.canTargetBlock(var2)
               && this.findCrystalSlot() != -1
               && this.findPlacement(var1.base(), var1.aim()) != null
               && this.scoreCrystal(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean isAimAligned(AutoCrystalModule.CrystalAction var1) {
      if (var1.support() != null) {
         return this.revalidateSupport(var1.support(), var1.base(), minecraft.player.getLookAngle()) != null;
      } else {
         return var1.crystal() != null
            ? InteractionArbiter.isLookingAt(var1.crystal(), this.getBreakRange())
            : InteractionArbiter.wasManualClaimed(var1.base(), this.isTargetable()) != null;
      }
   }

   public void updateAim() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.currentAction != null && this.actionTick == minecraft.level.getGameTime()) {
            if (InteractionArbiter.ownsInteraction(this) && this.isActionValid(this.currentAction)) {
               if (this.rotationMode.isMode("Visible")) {
                  if (!ViewController.canAcquire(ViewController.Owner.AUTO_CRYSTAL)) {
                     this.aimTracker.reset();
                     return;
                  }

                  ViewController.acquire(ViewController.Owner.AUTO_CRYSTAL);
                  this.rotationPlayer = minecraft.player;
                  this.rotationLevel = minecraft.level;
                  this.rotationStartTick = minecraft.player.tickCount;
                  float[] var1 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), this.currentAction.aim());
                  float[] var2 = RotationLimiter.stepToward(minecraft.player.getYRot(), minecraft.player.getXRot(), var1[0], var1[1], this.turnSpeed.getValue());
                  RotationManager.request(RotationManager.Priority.CRYSTAL, var2[0], var2[1], 1.0);
                  RotationManager.applyToPlayer();
               }

               this.aimTracker.recordTick(minecraft.player.tickCount, this.isAimAligned(this.currentAction));
               this.status = this.aimTracker.hasStreak(minecraft.player.tickCount, this.aimHold.getValue().intValue())
                  ? "Aim ready"
                  : (
                     !this.rotationMode.isMode("Visible")
                        ? "Waiting for your crosshair"
                        : (this.currentAction.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action")
                  );
            } else {
               this.aimTracker.reset();
            }
         }
      }
   }

   private boolean getBreakRange(AutoCrystalModule.CrystalAction var1) {
      return this.currentAction == var1
         && this.actionTick == minecraft.level.getGameTime()
         && this.isActionValid(var1)
         && this.aimTracker.hasStreak(minecraft.player.tickCount, this.aimHold.getValue().intValue())
         && this.isAimAligned(var1);
   }

   private void executeAction(AutoCrystalModule.CrystalAction var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().blockPosition().below();
      this.placementHighlight.setTarget(var4);
      VisualsRenderer.setLastTarget(var1.victim());
      this.lastVictim = var1.victim();
      this.actionTick = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.ObsidianPlacement(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.lastActionKey, var5) || this.lastActionVictim != var1.victim()) {
         this.aimTracker.reset();
      }

      this.lastActionKey = var5;
      this.lastActionVictim = var1.victim();
      if (var1.crystal() != null) {
         InteractionArbiter.queueAttackWithCallbacks(
            this,
            var1.crystal(),
            this.getBreakRange(),
            false,
            () -> this.getBreakRange(var1),
            1,
            () -> {
               this.pendingActions
                  .markEntityWithTtl(
                     minecraft.level.getGameTime(), var1.crystal().getId(), this.breakDelay.getValue().intValue(), this.responseTimeout.getValue().intValue()
                  );
               this.targetBase = var4;
               this.status = "Waiting for crystal removal";
               this.cancelAction();
            },
            var2x -> {
               if (this.getBreakRange(var1)) {
                  this.status = "Waiting: " + var2x;
               }
            }
         );
      } else if (var1.support() != null) {
         InteractionArbiter.queueAction(this, () -> this.placeObsidian(var1), () -> this.getBreakRange(var1));
      } else {
         InteractionArbiter.queueAction(this, () -> this.placeCrystal(var1.base()), () -> this.getBreakRange(var1));
      }
   }

   private void placeObsidian(AutoCrystalModule.CrystalAction var1) {
      BlockHitResult var2 = this.revalidateSupport(var1.support(), var1.base(), minecraft.player.getLookAngle());
      int var3 = NativePlacement.findItemSlot(Items.OBSIDIAN, this.autoSwitch.getValue());
      if (var2 != null && var3 != -1) {
         int var4 = minecraft.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               minecraft.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = minecraft.gameMode.useItemOn(minecraft.player, var6, var2);
            long var8 = minecraft.level.getGameTime();
            if (var7.consumesAction()) {
               SwingHelper.swingInteract(minecraft.player, var6);
               this.pendingActions.delayBlocks(var8, this.obsidianDelay.getValue().intValue());
               this.obsidianCooldownUntil = var8 + this.responseTimeout.getValue().intValue();
               this.status = "Placed obsidian; crystal next";
               LarpLog.debug(
                  "AutoCrystal",
                  "Obsidian base at {} for {} ({} mode)",
                  var1.base().toShortString(),
                  var1.victim().getName().getString(),
                  this.obsidianMode.getValue()
               );
            } else {
               this.pendingActions.delayBlocks(var8, this.placeDelay.getValue().intValue());
               this.status = "Obsidian placement rejected; retrying";
            }

            this.cancelAction();
         } finally {
            if (var5 != 0) {
               minecraft.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int findCrystalSlot() {
      if (minecraft.player.getOffhandItem().is(Items.END_CRYSTAL)) {
         return -2;
      }

      int var1 = minecraft.player.getInventory().getSelectedSlot();
      if (minecraft.player.getMainHandItem().is(Items.END_CRYSTAL)) {
         return var1;
      }

      if (this.autoSwitch.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (minecraft.player.getInventory().getItem(var2).is(Items.END_CRYSTAL)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private BlockHitResult findPlacement(BlockPos var1, Vec3 var2) {
      if (minecraft.level.hasChunkAt(var1) && minecraft.level.getWorldBorder().isWithinBounds(var1)) {
         BlockState var3 = minecraft.level.getBlockState(var1);
         if ((var3.is(Blocks.OBSIDIAN) || var3.is(Blocks.BEDROCK))
            && minecraft.level.isEmptyBlock(var1.above())
            && minecraft.level.getEntities(null, new AABB(var1.above()).expandTowards(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3 var4 = minecraft.player.getEyePosition();
            double var5 = this.isTargetable();
            if (var4.distanceToSqr(Vec3.atCenterOf(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.breakCrystals.getValue() && BoxRaycastUtil.distanceToBox(var4, crystalBox(var1)) > this.getBreakRange()) {
               return null;
            }

            if (var2 != null) {
               BlockHitResult var7 = InteractionArbiter.reset(var1, var5, var2.subtract(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (Vec3 var8 : aimPoints(var1, var4)) {
               BlockHitResult var9 = InteractionArbiter.reset(var1, var5, var8.subtract(var4));
               if (var9 != null) {
                  return var9;
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void placeCrystal(BlockPos var1) {
      BlockHitResult var2 = InteractionArbiter.wasManualClaimed(var1, this.isTargetable());
      int var3 = this.findCrystalSlot();
      if (var2 != null && var3 != -1 && this.findPlacement(var1, var2.getLocation()) != null) {
         int var4 = minecraft.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               minecraft.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = minecraft.gameMode.useItemOn(minecraft.player, var6, var2);
            this.pendingActions
               .markBlockWithTtl(
                  minecraft.level.getGameTime(), var1, this.placeDelay.getValue().intValue(), var7.consumesAction(), this.responseTimeout.getValue().intValue()
               );
            if (var7.consumesAction()) {
               SwingHelper.swingInteract(minecraft.player, var6);
               this.targetBase = var1;
               this.obsidianCooldownUntil = 0L;
               this.status = "Waiting for crystal spawn";
            } else {
               this.status = "Placement rejected; retrying";
            }

            this.cancelAction();
         } finally {
            if (var5 != 0) {
               minecraft.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private record BaseCandidate(BlockPos pos, double distance) {
   }

   private record CrystalAction(BlockPos base, EndCrystal crystal, LivingEntity victim, long created, Vec3 aim, BlockHitResult support) {
   }

   private record ObsidianPlacement(BlockPos pos) {
   }

   private static final class SearchStats {
      int basesChecked;
      int selfDamageRejected;
      double lowestSelfDamage = Double.MAX_VALUE;
      double bestTargetDamage;
      double bestLethalDamage;
      double bestLethalHealth = Double.NaN;
   }

   private record TargetScore(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }
}
