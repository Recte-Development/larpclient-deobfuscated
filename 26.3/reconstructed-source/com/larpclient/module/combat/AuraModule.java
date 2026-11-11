package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.CombatUtil;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.TpsTracker;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.player.RodSwapModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.CombatState;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SdlWindowShim;
import com.larpclient.util.world.NativePlacement;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AuraModule extends Module {
   private static AuraModule instance;
   public static volatile boolean sprintResetActive;
   private final ModeSetting activation = (ModeSetting)this.addSetting(
         new ModeSetting("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .setDisplayName("Keybind Mode");
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.getTargetSettings(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.getTargetSettings(var1));
         }
      }
   );
   private final ModeSetting auraMode = this.addSetting(
      new ModeSetting("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.normalizeAuraMode(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.normalizeAuraMode(var1));
         }
      }
   );
   private final ModeSetting combatVersion = this.addSetting(
      new ModeSetting("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
         @Override
         public void setValue(String var1) {
            super.setValue("1.9+".equals(var1) ? "1.9" : var1);
         }

         @Override
         public boolean acceptsValue(String var1) {
            return "1.9+".equals(var1) || super.acceptsValue(var1);
         }

         @Override
         public String getModeLabel(String var1) {
            return "1.9".equals(var1) ? "1.9+" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final NumberSetting attackRange = this.addSetting(
      new NumberSetting("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1)
   );
   private final NumberSetting aimRange = this.addSetting(
      new NumberSetting("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1)
   );
   private final NumberSetting fov = this.addSetting(new NumberSetting("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Target players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Target hostile and neutral mobs", true));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Target animals", false));
   private final BooleanSetting invisibles = this.addSetting(new BooleanSetting("Invisibles", "Target invisible players", true));
   private final BooleanSetting nakedPlayers = this.addSetting(new BooleanSetting("Naked Players", "Target players without armor", true));
   private final ModeSetting criticals = this.addSetting(
      new ModeSetting(
         "Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None"
      )
   );
   private final ModeSetting aimPoint = this.addSetting(
      new ModeSetting("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body")
   );
   private final BooleanSetting predict = this.addSetting(new BooleanSetting("Predict", "Lead moving targets", false));
   private final NumberSetting predictValue = this.addSetting(new NumberSetting("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final NumberSetting reachBuffer = this.addSetting(
      new NumberSetting("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05)
   );
   private final BooleanSetting rotationEta = this.addSetting(
      new BooleanSetting("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true)
   );
   private final BooleanSetting entityRaycast = this.addSetting(
      new BooleanSetting("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true)
   );
   private final BooleanSetting dynamicSafeReach = this.addSetting(
      new BooleanSetting("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true)
   );
   private final BooleanSetting predictCriticals = this.addSetting(
      new BooleanSetting("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true)
   );
   private final BooleanSetting randomizeTiming = this.addSetting(
      new BooleanSetting("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true)
   );
   private final ModeSetting sprintMode = this.addSetting(
      new ModeSetting(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Select and attack targets behind blocks", false));
   private final BooleanSetting wallBypass = this.addSetting(
      new BooleanSetting("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final BooleanSetting shieldBreaker = this.addSetting(
      new BooleanSetting("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true)
   );
   private final BooleanSetting shieldUnpress = this.addSetting(new BooleanSetting("Shield Unpress", "Lower your own shield right before attacking", false));
   private final BooleanSetting raycast = this.addSetting(new BooleanSetting("Raycast", "Require a block-free line to the target for each attack", true));
   private final BooleanSetting tpsSync = this.addSetting(new BooleanSetting("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final BooleanSetting eating = this.addSetting(new BooleanSetting("Eating", "Do not attack while eating or drinking", true));
   private final BooleanSetting breaking = this.addSetting(new BooleanSetting("Breaking", "Do not attack while mining", false));
   private final BooleanSetting placing = this.addSetting(new BooleanSetting("Placing", "Do not attack while placing blocks", false));
   private final BooleanSetting inInventory = this.addSetting(new BooleanSetting("In Inventory", "Do not attack with a screen open", false));
   private final BooleanSetting inLava = this.addSetting(new BooleanSetting("In Lava", "Do not attack while in lava", false));
   private final BooleanSetting inWeb = this.addSetting(new BooleanSetting("In Web", "Do not attack while in cobweb", false));
   private final BooleanSetting inWater = this.addSetting(new BooleanSetting("In Water", "Do not attack while in water", false));
   private final ModeSetting movementReference = this.addSetting(
      new ModeSetting("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final ModeSetting moveFix = this.addSetting(
      new ModeSetting(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final NumberSetting minCps = this.addSetting(new NumberSetting("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final NumberSetting maxCps = this.addSetting(new NumberSetting("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump to set up criticals", false));
   private final BooleanSetting smartAimVisibility = this.addSetting(
      new BooleanSetting("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
         @Override
         public String getDisplayName() {
            return "Smart Aim (Visibility)";
         }
      }
   );
   private final BooleanSetting focusTarget = this.addSetting(new BooleanSetting("Focus Target", "Keep the current target while it stays valid", true));
   private final ModeSetting sortMode = this.addSetting(
      new ModeSetting("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor")
   );
   private final BooleanSetting deliberateMiss = this.addSetting(new BooleanSetting("Deliberate Miss", "Occasionally swing just past the target", false));
   private final NumberSetting missThreshold = this.addSetting(
      new NumberSetting("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0)
   );
   private final BooleanSetting fovCone = this.addSetting(new BooleanSetting("FOV Cone", "Show the attack range arc on the ground", true));
   private final NumberSetting indicatorWidth = this.addSetting(new NumberSetting("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final BooleanSetting visibleLines = this.addSetting(new BooleanSetting("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final BooleanSetting hitZone = this.addSetting(new BooleanSetting("Hit Zone", "Terrain positioning guide around the current target", true));
   private final NumberSetting zoneWidth = this.addSetting(new NumberSetting("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final NumberSetting zoneOpacity = this.addSetting(new NumberSetting("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final BooleanSetting zoneFill = this.addSetting(new BooleanSetting("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final BooleanSetting zoneBloom = this.addSetting(new BooleanSetting("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final BooleanSetting zoneMotion = this.addSetting(new BooleanSetting("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final TargetSelector targetSelector = new TargetSelector();
   private final AuraAttackController attackController = new AuraAttackController(this);
   private final AttackWobble attackWobble = new AttackWobble();
   private final PerlinNoise noise = new PerlinNoise();
   private final HumanizedRotation slothRotation = new HumanizedRotation();
   private final HumanizedRotation2 funTimeRotation = new HumanizedRotation2();
   private final SmoothRotation polarRotation = new SmoothRotation();
   private final HumanizedRotator matrixRotation = new HumanizedRotator();
   private final HumanizedRotation3 legitRotation = new HumanizedRotation3();
   private RotationProvider rotationProvider;
   private LivingEntity target;
   private LivingEntity lastTarget;
   private float aimNoiseTime;
   private float pointNoiseTime;
   private long targetAcquiredTime;
   private int lastLatencyTick = Integer.MIN_VALUE;
   private double latencyFactor = 1.0;
   private boolean shieldReleased;
   private int shieldCooldownTicks;
   private boolean useKeyOverridden;
   private String status = "Idle";
   private int jumpLogBudget;
   private int jumpLogUntilTick = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      instance = this;
      this.predictValue.visibleWhen(this.predict::getValue);
      this.moveFix.visibleWhen(() -> this.movementReference.isMode("Camera"));
      this.predictCriticals.visibleWhen(() -> !this.criticals.isMode("None"));
      this.randomizeTiming.visibleWhen(() -> !this.isLegacyCombat());
      this.minCps.visibleWhen(this::isLegacyCombat);
      this.maxCps.visibleWhen(this::isLegacyCombat);
      this.missThreshold.visibleWhen(this.deliberateMiss::getValue);
      this.indicatorWidth.visibleWhen(this.fovCone::getValue);
      this.visibleLines.visibleWhen(this.fovCone::getValue);
      this.zoneWidth.visibleWhen(this.hitZone::getValue);
      this.zoneOpacity.visibleWhen(this.hitZone::getValue);
      this.zoneFill.visibleWhen(this.hitZone::getValue);
      this.zoneBloom.visibleWhen(this.hitZone::getValue);
      this.zoneMotion.visibleWhen(this.hitZone::getValue);
      this.rotation.onChange(var1 -> this.getAimSettings("Rotation engine: " + var1));
   }

   public static AuraModule getInstance() {
      return instance;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean isHoldMode() {
      return this.activation.isMode("Hold");
   }

   @Override
   public ModeSetting keybindMode() {
      return this.activation;
   }

   @Override
   public void toggle() {
      if (this.isHoldMode() && minecraft != null && minecraft.gui != null && minecraft.gui.screen() == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean isActivationHeld() {
      if (!this.isHoldMode()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!BindCodes.isValidBind(var1) || minecraft.getWindow() == null) {
            return false;
         } else {
            return minecraft.gui.screen() == null && minecraft.isWindowActive() ? BindCodes.isPressed(minecraft.getWindow().handle(), var1) : false;
         }
      }
   }

   private static String normalizeAuraMode(String var0) {
      if ("Balanced".equalsIgnoreCase(var0) || "Balant".equalsIgnoreCase(var0)) {
         return "Blatant";
      } else {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      }
   }

   private static String getTargetSettings(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.inInventory.getValue();
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   public List<Setting<?>> getAimSettings() {
      return List.of(
         this.rotation,
         this.auraMode,
         this.movementReference,
         this.moveFix,
         this.aimPoint,
         this.predict,
         this.predictValue,
         this.smartAimVisibility,
         this.rotationEta
      );
   }

   public List<Setting<?>> getCombatSettings() {
      return List.of(
         this.combatVersion,
         this.attackRange,
         this.aimRange,
         this.reachBuffer,
         this.dynamicSafeReach,
         this.criticals,
         this.predictCriticals,
         this.sprintMode,
         this.autoJump,
         this.randomizeTiming,
         this.minCps,
         this.maxCps,
         this.entityRaycast,
         this.deliberateMiss,
         this.missThreshold
      );
   }

   public List<Setting<?>> getMoveYaw() {
      return List.of(this.players, this.mobs, this.animals, this.invisibles, this.nakedPlayers);
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.fov, this.sortMode, this.focusTarget);
   }

   public List<Setting<?>> getMiscSettings() {
      return List.of(this.throughWalls, this.wallBypass, this.shieldBreaker, this.shieldUnpress, this.raycast, this.tpsSync);
   }

   public List<Setting<?>> getBaseReach() {
      return List.of(this.eating, this.breaking, this.placing, this.inInventory, this.inLava, this.inWeb, this.inWater);
   }

   public List<Setting<?>> getVisualSettings() {
      return List.of(
         this.fovCone, this.indicatorWidth, this.visibleLines, this.hitZone, this.zoneWidth, this.zoneOpacity, this.zoneFill, this.zoneBloom, this.zoneMotion
      );
   }

   @Override
   public void onEnable() {
      this.jumpLogBudget = 3;
      this.jumpLogUntilTick = Integer.MIN_VALUE;
      this.attackController.reset();
      this.targetSelector.clear();
      this.attackWobble.reset();
      AimPointPicker.reset();
      this.target = this.lastTarget = null;
      sprintResetActive = (boolean)0;
      this.aimNoiseTime = this.pointNoiseTime = 0.0F;
      this.targetAcquiredTime = 0L;
      this.lastLatencyTick = Integer.MIN_VALUE;
      this.latencyFactor = 1.0;
      this.getMiscSettings();
      this.status = "Idle";
      LarpLog.info(
         "Aura",
         "Movement: Reference="
            + this.movementReference.getValue()
            + "; Move Fix="
            + this.moveFix.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.target = this.lastTarget = null;
      sprintResetActive = (boolean)0;
      this.aimNoiseTime = this.pointNoiseTime = 0.0F;
      this.targetAcquiredTime = 0L;
      this.lastLatencyTick = Integer.MIN_VALUE;
      this.latencyFactor = 1.0;
      this.getMiscSettings();
      this.isHoldMode();
      if (this.rotationProvider != null) {
         this.rotationProvider.reset();
      }

      this.rotationProvider = null;
      this.attackController.clearSprintReset(this.getPlayer());
      this.attackController.restoreSlot(minecraft);
      this.attackController.reset();
      this.targetSelector.clear();
      this.attackWobble.reset();
      AimPointPicker.reset();
      this.status = "Idle";
   }

   @Override
   public void onTick() {
      this.attackController.scheduleNextAttack(minecraft);
      if (this.isInGame()) {
         LocalPlayer var1 = minecraft.player;
         if (!this.isActivationHeld()) {
            this.pause("Hold released");
         } else {
            if (InteractionArbiter.canModuleInteract(this)) {
               int var10000 = var1.isSpectator();
               boolean var43 = false;
               byte var42 = (byte)var10000;
               var10000 = Integer.signum(var42);
               if ((var10000 | -var10000) >>> 31 == 0 && var1.isAlive()) {
                  boolean var45 = false;
                  int var44 = this.shieldCooldownTicks;
                  if ((-Integer.signum(var44) >>> 31 ^ 1) == 0) {
                     byte var47 = 1;
                     int var46 = this.shieldCooldownTicks;
                     this.shieldCooldownTicks = var46 - var47;
                  }

                  boolean var2 = this.auraMode.isMode("Blatant");
                  boolean var3 = this.getCritMode();
                  int var4 = !var3
                        && this.attackController.isCooldownReady(var1, this.isLegacyCombat(), this.tpsSync.getValue(), this.randomizeTiming.getValue(), var2)
                     ? 1
                     : 0;
                  float var5 = this.attackController
                     .ticksUntilReady(var1, this.isLegacyCombat(), this.tpsSync.getValue(), this.randomizeTiming.getValue(), var2);
                  int var6 = !this.throughWalls.getValue() && !this.wallBypass.getValue() ? 0 : 1;
                  double var7 = Math.max(this.attackRange.getValue() + this.aimRange.getValue(), RodSwapModule.getAimRange());
                  TargetSelector.TargetFilter var9 = new TargetSelector.TargetFilter(
                     var7,
                     this.players.getValue(),
                     this.mobs.getValue(),
                     this.animals.getValue(),
                     this.invisibles.getValue(),
                     this.nakedPlayers.getValue(),
                     this.focusTarget.getValue(),
                     (boolean)var6,
                     this.sortMode.getValue(),
                     this.fov.getValue()
                  );
                  this.target = this.targetSelector.findTarget(var9);
                  Rotation var10 = new Rotation(var1.getYRot(), var1.getXRot());
                  if (this.target != this.lastTarget) {
                     if (this.rotationProvider != null) {
                        this.rotationProvider.targetChanged(this.target);
                     }

                     AimPointPicker.reset();
                     if (this.target != null) {
                        this.targetAcquiredTime = System.currentTimeMillis();
                        this.attackController.markTargetChanged();
                        this.getAimSettings("Target acquired: entity=" + this.target.getId());
                     } else {
                        this.isHoldMode();
                        this.getAimSettings("Target lost");
                     }

                     this.lastTarget = this.target;
                  }

                  if (this.target == null) {
                     this.getMiscSettings();
                     sprintResetActive = (boolean)0;
                     this.attackController.clearSprintReset(var1);
                     this.targetAcquiredTime = 0L;
                     this.isHoldMode();
                     this.status = "No target";
                     return;
                  }

                  RotationProvider var11 = this.getRotationProvider();
                  if (var11 != this.rotationProvider) {
                     if (this.rotationProvider != null) {
                        this.rotationProvider.reset();
                     }

                     this.rotationProvider = var11;
                  }

                  AABB var12 = this.target.getBoundingBox();
                  double var13 = this.getEffectiveReach(this.target);
                  int var15 = CombatUtil.distanceToBox(var1, var12) <= var13 ? 1 : 0;
                  float var16 = 1.25F;
                  if (var15 != 0) {
                     Rotation var17 = RotationMath.rotationTo(AimPointPicker.closestPoint(var12));
                     var16 = Math.max(var16, this.rotationProvider.ticksToReach(var10, var17) + 0.35F);
                     var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
                  }

                  int var51 = var3 || var15 == 0 || var4 == 0 && !(var5 <= var16) ? 0 : 1;
                  int var18 = var15 != 0 && this.targetAcquiredTime > 0L && System.currentTimeMillis() - this.targetAcquiredTime < (var2 ? 150L : 500L) ? 1 : 0;
                  int var19 = var15 == 0 || !var2 && var51 == 0 && var18 == 0 ? 0 : 1;
                  Vec3 var20 = this.isZoneBloom(this.target, (boolean)var19);
                  Vec3 var21 = RodSwapModule.getAimPoint(this.target);
                  if (var21 != null) {
                     var20 = var21;
                  }

                  Rotation var22 = RotationMath.rotationTo(var20);
                  var19 = !(CombatUtil.distanceToBox(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
                  int var23 = var19 == 0 || var4 == 0 && !(var5 <= (var2 ? 0.5F : 1.15F)) ? 0 : 1;
                  boolean var24 = this.getMiscSettings((boolean)var23);
                  int var25 = this.rotationProvider == this.legitRotation ? 1 : 0;
                  if (this.targetAcquiredTime > 0L && var19 == 0 && var25 == 0) {
                     long var26 = System.currentTimeMillis() - this.targetAcquiredTime;
                     long var28 = var2 ? 0L : 120L;
                     float var30 = Mth.clamp((float)var26 / (float)var28, 0.0F, 1.0F);
                     if (var30 < 1.0F) {
                        var22 = new Rotation(
                           var10.yaw + RotationMath.angleDelta(var10.yaw, var22.yaw) * var30, var10.pitch + (var22.pitch - var10.pitch) * var30
                        );
                     }
                  }

                  Rotation var53 = getLastRotation(var1);
                  boolean var27 = CombatUtil.isLookingAtBox(
                     var1, var12.inflate(this.target.getPickRadius()), var53.yaw, var53.pitch, this.attackRange.getValue() + 1.0, (boolean)var6
                  );
                  int var54 = var19 == 0 && var21 == null ? 0 : 1;
                  Rotation var29 = this.rotationProvider.rotate(var10, var22, this.target, (boolean)var54, var27);
                  if (var2 && var25 == 0) {
                     this.aimNoiseTime += 0.015F;
                     double var56 = this.noise.noise(this.aimNoiseTime, 0.0, 0.0);
                     double var32 = this.noise.noise(0.0, this.aimNoiseTime, 0.0);
                     float var34 = var54 != 0 ? 0.15F : 1.0F;
                     var29.yaw += (float)(var56 * 0.12F * var34);
                     var29.pitch = Mth.clamp(var29.pitch + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
                  }

                  if (this.deliberateMiss.getValue()) {
                     this.attackWobble.setInterval(this.missThreshold.getValue().intValue());
                     this.attackWobble.update();
                     if (this.attackWobble.isActive()) {
                        var29 = this.attackWobble.applyOffset(var29);
                     }
                  } else {
                     this.attackWobble.reset();
                  }

                  if (this.rotationProvider == this.legitRotation) {
                     var29 = this.legitRotation.rotateFallback(var10, var29);
                  } else {
                     var29 = RotationMath.snapToMouseStep(var10, var29);
                  }

                  this.isZoneBloom(var29);
                  if (!var24 && !var3 && (!this.deliberateMiss.getValue() || !this.attackWobble.isActive())) {
                     boolean var57 = CombatUtil.isLookingAtBox(var1, var12.inflate(this.target.getPickRadius()), var29.yaw, var29.pitch, var13, (boolean)var6);
                     Rotation var31 = !var2 && !var57 ? var53 : var29;
                     LivingEntity var58 = this.target;
                     AABB var33 = var12;
                     double var59 = var13;
                     byte var36 = 1;
                     if (this.entityRaycast.getValue()) {
                        EntityHitResult var37 = CombatUtil.raycastEntity(var1, var31.yaw, var31.pitch, var13, (boolean)var6);
                        var36 = 0;
                        if (var37 != null && var37.getEntity() instanceof LivingEntity var38 && this.targetSelector.isValidTarget(var38, var9)) {
                           double var62 = this.getEffectiveReach(var38);
                           AABB var41 = var38.getBoundingBox();
                           if (CombatUtil.distanceToBox(var1, var41) <= var62) {
                              var58 = var38;
                              var33 = var41;
                              var59 = var62;
                              var36 = 1;
                           }
                        }
                     }

                     LivingEntity var60 = var58;
                     Runnable var61 = this.wallBypass.getValue() ? () -> breakBlocksAlongSight(var60) : null;
                     boolean var63 = this.attackController
                        .tryAttack(
                           var1,
                           new AuraAttackController.AttackRequest(
                              var58,
                              var33,
                              var31.yaw,
                              var31.pitch,
                              var59,
                              (boolean)var6,
                              this.getCritMode(),
                              this.getAimSettings(),
                              this.shieldBreaker.getValue(),
                              this.eating.getValue(),
                              this.raycast.getValue() && var6 == 0,
                              (boolean)var36,
                              this.autoJump.getValue(),
                              this.predictCriticals.getValue(),
                              var61,
                              this.isLegacyCombat(),
                              this.tpsSync.getValue(),
                              this.randomizeTiming.getValue(),
                              var2,
                              this.minCps.getValue().floatValue(),
                              this.maxCps.getValue().floatValue()
                           )
                        );
                     this.isLegacyCombat();
                     if (var63) {
                        this.getAimSettings("Attack executed: entity=" + var58.getId());
                        int var65 = this.deliberateMiss.getValue();
                        boolean var49 = false;
                        byte var48 = (byte)var65;
                        var65 = Integer.signum(var48);
                        if (((var65 | -var65) >>> 31 ^ 1) == 0) {
                           this.attackWobble.incrementTicks();
                        }
                     }
                  } else if (var3) {
                     this.attackController.clearSprintReset(var1);
                  }

                  this.status = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
                  return;
               }
            }

            this.pause("Interaction unavailable");
         }
      }
   }

   private void pause(String var1) {
      if (this.target != null || this.lastTarget != null) {
         this.getAimSettings("Paused: " + var1);
      }

      this.target = this.lastTarget = null;
      sprintResetActive = (boolean)0;
      this.attackController.clearSprintReset(this.getPlayer());
      this.getMiscSettings();
      this.isHoldMode();
      this.status = var1;
   }

   private double getEffectiveReach(LivingEntity var1) {
      double var2 = Math.max(0.0, this.attackRange.getValue() - this.reachBuffer.getValue());
      if (this.dynamicSafeReach.getValue() && minecraft.player != null && var1 != null) {
         AABB var4 = var1.getBoundingBox();
         double var5 = (var4.minX + var4.maxX) * 0.5 - minecraft.player.getX();
         double var7 = (var4.minZ + var4.maxZ) * 0.5 - minecraft.player.getZ();
         double var9 = var5 * var5 + var7 * var7;
         if (var9 <= 1.0E-6) {
            return var2;
         }

         double var11 = 1.0 / Math.sqrt(var9);
         var5 *= var11;
         var7 *= var11;
         Vec3 var13 = minecraft.player.getDeltaMovement();
         Vec3 var14 = var1.getDeltaMovement();
         double var15 = Math.abs((var14.x - var13.x) * var5 + (var14.z - var13.z) * var7);
         boolean var17 = this.auraMode.isMode("Blatant");
         double var18 = var15 * this.getLatencyFactor() * (var17 ? 0.15 : 0.4);
         double var20 = Mth.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double getLatencyFactor() {
      int var1 = minecraft.player.tickCount;
      if (this.lastLatencyTick == Integer.MIN_VALUE || var1 < this.lastLatencyTick || var1 - this.lastLatencyTick >= 20) {
         double var2 = 1.0;
         if (minecraft.getConnection() != null) {
            PlayerInfo var4 = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID());
            if (var4 != null) {
               var2 = var4.getLatency() / 50.0F;
            }
         }

         this.latencyFactor = Mth.clamp(var2, 0.5, 3.0);
         this.lastLatencyTick = var1;
      }

      return this.latencyFactor;
   }

   private Vec3 isZoneBloom(LivingEntity var1, boolean var2) {
      double var3 = this.attackRange.getValue() + this.aimRange.getValue();
      int var5 = this.rotationProvider == this.legitRotation ? 1 : 0;
      int var6 = var5;
      int var7 = this.rotationProvider == this.legitRotation && this.predict.getValue() && !var2 ? 1 : 0;
      Vec3 var8 = var7 != 0
         ? MotionPredictor.predictPosition(var1, this.isHoldMode())
         : (this.predict.getValue() && !var2 && var6 == 0 ? MotionPredictor.predictPosition(var1, this.getPredictTicks()) : var1.position());
      AABB var9 = var1.getBoundingBox().move(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3 var10;
      if (var6 == 0
         && !this.aimPoint.isMode("Center")
         && (!this.auraMode.isMode("Normal") || this.aimPoint.isMode("Head") || this.aimPoint.isMode("Body") || this.aimPoint.isMode("Multipoint"))) {
         if (this.aimPoint.isMode("Multipoint")) {
            var10 = AimPointPicker.reset(var1, var9, var3);
         } else if (this.aimPoint.isMode("Head")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.78F, var9.getCenter().z);
         } else if (this.aimPoint.isMode("Body")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.55F, var9.getCenter().z);
         } else {
            var10 = AimPointPicker.closestPoint(var9);
         }
      } else {
         var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = getAimSettings(var1, var9, var10);
      }

      int var11 = !var2 && (this.auraMode.isMode("Normal") || var5 != 0) && (var6 == 0 || var5 != 0) && (var5 == 0 || !isTargetMoving(var1)) ? 1 : 0;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.getBbWidth() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.getBbHeight() * (var5 != 0 ? 0.16F : 0.12F);
         this.pointNoiseTime += var12;
         double var17 = this.noise.noise(this.pointNoiseTime, 120.0, 0.0) * var13;
         double var19 = this.noise.noise(0.0, this.pointNoiseTime, 240.0) * var15;
         double var21 = this.noise.noise(0.0, 120.0, this.pointNoiseTime) * var13;
         var10 = applyMoveFix(var10.add(var17, var19, var21), var9);
      }

      if (this.smartAimVisibility.getValue() && !this.throughWalls.getValue() && !this.wallBypass.getValue()) {
         var10 = AimPointPicker.adjustToVisiblePoint(var9, var10, var3);
      }

      return var10;
   }

   private double getPredictTicks() {
      double var1 = 1.0;
      if (this.tpsSync.getValue()) {
         float var3 = TpsTracker.averageTps();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / Mth.clamp(var3, 10.0F, 20.0F);
         }
      }

      return Mth.clamp(this.predictValue.getValue() * var1, 0.0, 4.0);
   }

   private double isHoldMode() {
      return Mth.clamp(Math.max(1.0, this.getPredictTicks()) + 1.0, 1.0, 3.0);
   }

   private static Vec3 getAimSettings(LivingEntity var0, AABB var1, Vec3 var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 != null && var0 != null && var1 != null) {
         Vec3 var4 = var0.getDeltaMovement();
         Vec3 var5 = var3.getDeltaMovement();
         double var6 = (var0.getX() - var0.xo) * 0.7F + var4.x * 0.3F - var5.x;
         double var8 = (var0.getY() - var0.yo) * 0.7F + var4.y * 0.3F - var5.y;
         double var10 = (var0.getZ() - var0.zo) * 0.7F + var4.z * 0.3F - var5.z;
         double var12 = Math.min(0.08F, var1.getXsize() * 0.25);
         double var14 = Math.min(0.08F, var1.getZsize() * 0.25);
         double var16 = Math.min(0.12F, var1.getYsize() * 0.12F);
         double var18 = var1.getXsize() * 0.22F;
         double var20 = var1.getZsize() * 0.22F;
         double var22 = var1.getYsize() * 0.1F;
         Vec3 var24 = var1.getCenter();
         return new Vec3(
            Mth.clamp(var24.x + Mth.clamp(var6 * 0.65F, -var18, var18), var1.minX + var12, var1.maxX - var12),
            Mth.clamp(var24.y + Mth.clamp(var8 * 0.45F, -var22, var22), var1.minY + var16, var1.maxY - var16),
            Mth.clamp(var24.z + Mth.clamp(var10 * 0.65F, -var20, var20), var1.minZ + var14, var1.maxZ - var14)
         );
      } else {
         return var2;
      }
   }

   private static boolean isTargetMoving(LivingEntity var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var0 != null && var1 != null) {
         Vec3 var2 = var0.getDeltaMovement();
         Vec3 var3 = var1.getDeltaMovement();
         double var4 = (var0.getX() - var0.xo) * 0.7F + var2.x * 0.3F - var3.x;
         double var6 = (var0.getZ() - var0.zo) * 0.7F + var2.z * 0.3F - var3.z;
         return var4 * var4 + var6 * var6 >= 0.0025F;
      } else {
         return false;
      }
   }

   private static Vec3 applyMoveFix(Vec3 var0, AABB var1) {
      double var2 = 0.001F;
      return new Vec3(
         Mth.clamp(var0.x, var1.minX + var2, var1.maxX - var2),
         Mth.clamp(var0.y, var1.minY + var2, var1.maxY - var2),
         Mth.clamp(var0.z, var1.minZ + var2, var1.maxZ - var2)
      );
   }

   private RotationProvider getRotationProvider() {
      return switch ((String)this.rotation.getValue()) {
         case "FunTime" -> this.funTimeRotation;
         case "Polar" -> this.polarRotation;
         case "Matrix" -> this.matrixRotation;
         case "Legit" -> this.legitRotation;
         default -> this.slothRotation;
      };
   }

   private AuraAttackController.CritMode getCritMode() {
      return switch ((String)this.criticals.getValue()) {
         case "Always" -> AuraAttackController.CritMode.ALWAYS;
         case "None" -> AuraAttackController.CritMode.NONE;
         default -> AuraAttackController.CritMode.SMART;
      };
   }

   private AuraAttackController.SprintMode getAimSettings() {
      return switch ((String)this.sprintMode.getValue()) {
         case "Keep Sprint" -> AuraAttackController.SprintMode.KEEP_SPRINT;
         case "None" -> AuraAttackController.SprintMode.NONE;
         default -> AuraAttackController.SprintMode.SPRINT_RESET;
      };
   }

   private boolean isLegacyCombat() {
      return this.combatVersion.isMode("1.8");
   }

   private boolean getCritMode() {
      if (minecraft.player != null && minecraft.level != null) {
         AutoTrap var1 = AutoTrap.getInstance();
         AutoWebModule var2 = LarpClient.getInstance().getModuleManager().getModule(AutoWebModule.class);
         if (var1 != null && var1.isRuntimeActive() && NativePlacement.setFailure(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && NativePlacement.setFailure(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.getInstance();
            if (var3 != null && var3.isRuntimeActive() && NativePlacement.setFailure(var3)) {
               return true;
            } else if (this.eating.getValue() && CombatUtil.isConsuming()) {
               return true;
            } else if (this.breaking.getValue() && this.isBreaking()) {
               return true;
            } else if (this.placing.getValue() && this.getClampedReach()) {
               return true;
            } else if (this.inInventory.getValue() && minecraft.gui.screen() != null) {
               return true;
            } else if (this.inLava.getValue() && minecraft.player.isInLava()) {
               return true;
            } else {
               return this.inWeb.getValue() && CombatUtil.isInCobweb()
                  ? true
                  : this.inWater.getValue() && (minecraft.player.isInWater() || minecraft.player.isUnderWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean isBreaking() {
      return minecraft.gameMode != null && minecraft.gameMode.isDestroying()
         ? true
         : minecraft.options != null && minecraft.options.keyAttack.isDown() && minecraft.hitResult != null && minecraft.hitResult.getType() == Type.BLOCK;
   }

   private boolean getClampedReach() {
      return minecraft.options != null && minecraft.options.keyUse.isDown()
         ? minecraft.player.getMainHandItem().getItem() instanceof BlockItem || minecraft.player.getOffhandItem().getItem() instanceof BlockItem
         : false;
   }

   private boolean getMiscSettings(boolean var1) {
      if (!this.shieldUnpress.getValue() || !var1) {
         this.getMiscSettings();
         return false;
      }

      if (this.shieldReleased) {
         return false;
      }

      if (!minecraft.player.isUsingItem() || !(minecraft.player.getUseItem().getItem() instanceof ShieldItem)) {
         return false;
      }

      if (minecraft.gameMode == null) {
         return false;
      }

      if (minecraft.options != null) {
         minecraft.options.keyUse.setDown(false);
         this.useKeyOverridden = (boolean)1;
      }

      minecraft.gameMode.releaseUsingItem(minecraft.player);
      this.shieldReleased = (boolean)1;
      return true;
   }

   private void isLegacyCombat() {
      if (this.shieldReleased) {
         this.shieldCooldownTicks = 1;
      }

      this.shieldReleased = (boolean)0;
      this.getRotationProvider();
   }

   private void getMiscSettings() {
      this.shieldReleased = (boolean)0;
      this.shieldCooldownTicks = 0;
      this.getRotationProvider();
   }

   private void getRotationProvider() {
      if (this.useKeyOverridden && minecraft.options != null) {
         int var1 = this.getMoveKeys();
         int var2;
         if (var1 >= 0 && minecraft.getWindow() != null) {
            var2 = SdlWindowShim.getMouseButtonState(minecraft.getWindow().handle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = minecraft.options.keyUse.isDown();
         }

         minecraft.options.keyUse.setDown((boolean)var2);
         this.useKeyOverridden = (boolean)0;
      } else {
         this.useKeyOverridden = (boolean)0;
      }
   }

   private int getMoveKeys() {
      if (minecraft.options == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (minecraft.options.keyUse.matches(com.mojang.blaze3d.platform.InputConstants.Type.MOUSE.getOrCreate(KeyCodes.toMinecraftButton(var1)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void breakBlocksAlongSight(LivingEntity var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.level != null && var0 != null) {
         Vec3 var2 = var1.player.getEyePosition();
         Vec3 var3 = var0.getEyePosition().subtract(var2);
         double var4 = var3.length();
         if (!(var4 < 0.01)) {
            Vec3 var6 = var3.normalize();

            for (double var7 = 0.5; var7 < var4; var7 += 0.5) {
               Vec3 var9 = var2.add(var6.scale(var7));
               BlockPos var10 = new BlockPos((int)Math.floor(var9.x), (int)Math.floor(var9.y), (int)Math.floor(var9.z));
               if (!var1.level.isEmptyBlock(var10)) {
                  var1.player.connection.send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var10, Direction.UP));
                  var1.player.connection.send(new ServerboundPlayerActionPacket(Action.ABORT_DESTROY_BLOCK, var10, Direction.UP));
               }
            }
         }
      }
   }

   private void isZoneBloom(Rotation var1) {
      if (minecraft.gui.screen() == null && !PlayerInputUtil.isModuleActive("Freelook") && !PlayerInputUtil.isModuleActive("Freecam")) {
         ViewController.acquire(ViewController.Owner.AURA);
      }

      RotationManager.requestForModule(this, RotationManager.Priority.AURA, var1.yaw, var1.pitch, 1.0);
   }

   private void isHoldMode() {
      ViewController.release(ViewController.Owner.AURA);
   }

   private static Rotation getLastRotation(LocalPlayer var0) {
      LocalPlayerMixin_4 var1 = (LocalPlayerMixin_4)var0;
      return new Rotation(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean isTargeting() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.target != null && InteractionArbiter.getPriority(this);
   }

   public boolean getRotationProvider() {
      return !this.isEnabled()
         ? false
         : this.isTargeting() && this.movementReference.isMode("Camera") && this.moveFix.isMode("None") && ViewController.update() == ViewController.Owner.AURA;
   }

   public static Input getAimSettings(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         AuraModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var2.target != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = var1.sprint();
            if (var2.attackController.isSprintResetActive(var0)) {
               var4 = 0;
            } else if (var2.sprintMode.isMode("Keep Sprint")
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision
               && !var0.isInWater()
               && !var0.isUnderWater()) {
               var4 = 1;
            } else if (var2.sprintMode.isMode("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.keySprint.isDown())
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            Input var5 = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.shift(), (boolean)var4);
            if (ViewController.update() == ViewController.Owner.AURA) {
               double var6 = var2.target.getX() - var0.getX();
               double var8 = var2.target.getZ() - var0.getZ();
               double var10 = Mth.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.applyMoveFix(var0.input, var5, var0.getYRot(), var2.movementReference.isMode("Camera") ? getInstance() : var0.getYRot(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Input applyMoveFix(ClientInput var1, Input var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.movementReference.isMode("Target") && !this.moveFix.isMode("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.moveFix.isMode("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : Mth.wrapDegrees(Math.toDegrees(getMoveYaw(var4, var7, var8)));
         float[] var11 = getMoveKeys(var9, var3);
         var1.moveVector = new Vec2(var11[1], var11[0]).normalized();
         return new Input(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.shift(), var2.sprint());
      } else {
         return var2;
      }
   }

   public static double getMoveYaw(float var0, float var1, float var2) {
      if (var1 < 0.0F) {
         var0 += 180.0F;
      }

      float var3 = 1.0F;
      if (var1 < 0.0F) {
         var3 = -0.5F;
      }

      if (var1 > 0.0F) {
         var3 = 0.5F;
      }

      if (var2 > 0.0F) {
         var0 -= 90.0F * var3;
      }

      if (var2 < 0.0F) {
         var0 += 90.0F * var3;
      }

      return Math.toRadians(var0);
   }

   public static float[] getMoveKeys(double var0, float var2) {
      double var3 = Mth.wrapDegrees(var0 - var2);
      if (var3 >= -22.5 && var3 <= 22.5) {
         return new float[]{1.0F, 0.0F};
      } else if (var3 > 22.5 && var3 <= 67.5) {
         return new float[]{1.0F, -1.0F};
      } else if (var3 > 67.5 && var3 <= 112.5) {
         return new float[]{0.0F, -1.0F};
      } else if (var3 > 112.5 && var3 <= 157.5) {
         return new float[]{-1.0F, -1.0F};
      } else if (var3 > 157.5 || var3 <= -157.5) {
         return new float[]{-1.0F, 0.0F};
      } else if (var3 < -22.5 && var3 >= -67.5) {
         return new float[]{1.0F, 1.0F};
      } else {
         return var3 < -67.5 && var3 >= -112.5 ? new float[]{0.0F, 1.0F} : new float[]{-1.0F, 1.0F};
      }
   }

   public static boolean isShieldUnpressActive() {
      if (instance != null && instance.isEnabled()) {
         AuraModule var0 = instance;
         return var0 != null
            && var0.isRuntimeActive()
            && var0.shieldUnpress.getValue()
            && isLegacyCombat() != null
            && (var0.shieldReleased || var0.shieldCooldownTicks > 0);
      } else {
         return false;
      }
   }

   public float smoothYaw(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.isTargeting()) {
            return var1;
         } else if (this.getInstance()) {
            float var4 = Mth.wrapDegrees(getInstance() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : getInstance();
         } else {
            boolean var3 = this.auraMode.isMode("Normal");
            return getCombatSettings(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float getCombatSettings(float var0, float var1, float var2, float var3) {
      float var4 = Mth.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + Mth.wrapDegrees(var5 - var0) * var3;
   }

   private boolean getInstance() {
      LocalPlayer var1 = isLegacyCombat();
      if (this.movementReference.isMode("Camera") && this.moveFix.isMode("Targeted") && var1 != null && var1.input != null && this.target != null) {
         Vec2 var2 = var1.input.getMoveVector();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(this.target.getZ() - var1.getZ(), this.target.getX() - var1.getX())) - 90.0F);
            return Math.abs(Mth.wrapDegrees(var3 - getInstance())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static LocalPlayer isLegacyCombat() {
      return getMc().player;
   }

   public static boolean isSprintResetActive() {
      if (instance != null && instance.isEnabled()) {
         AuraModule var0 = instance;
         return var0 != null && var0.isRuntimeActive() && sprintResetActive;
      } else {
         return false;
      }
   }

   public static float getInstance() {
      Minecraft var0 = Minecraft.getInstance();
      return isTargetMoving()
         ? ViewController.toPlayerRotation(ViewController.getRawYaw(), ViewController.getPosition(), var0.options.getCameraType().isMirrored())[0]
         : var0.player.getYRot();
   }

   private static boolean isTargetMoving() {
      ViewController.Owner var0 = ViewController.update();
      return var0 == ViewController.Owner.AURA || var0 == ViewController.Owner.MACE || var0 == ViewController.Owner.RETURN;
   }

   public static float getViewPitch() {
      Minecraft var0 = Minecraft.getInstance();
      return isTargetMoving()
         ? ViewController.toPlayerRotation(ViewController.getRawYaw(), ViewController.getPosition(), var0.options.getCameraType().isMirrored())[1]
         : var0.player.getXRot();
   }

   public static Vec3 normalizeAuraMode() {
      return RotationMath.lookVector(getInstance(), getViewPitch());
   }

   public double getAttackRange() {
      return this.attackRange.getValue();
   }

   public LivingEntity applyMoveFix() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.target : null;
      } else {
         return null;
      }
   }

   public double getInstance() {
      return this.fov.getValue();
   }

   public double getClampedReach() {
      return InteractionArbiter.clampRange(this.getBaseReach());
   }

   private double getBaseReach() {
      return Math.max(0.0, this.attackRange.getValue() - this.reachBuffer.getValue());
   }

   public boolean isFovConeVisible() {
      return !this.isEnabled()
         ? false
         : this.isOperational() && this.isInGame() && minecraft.gui.screen() == null && this.fovCone.getValue() && this.isActivationHeld();
   }

   public float getIndicatorWidth() {
      return this.indicatorWidth.getValue().floatValue();
   }

   public boolean getViewPitch() {
      return this.visibleLines.getValue();
   }

   public boolean isHitZoneActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.target != null && this.hitZone.getValue() && InteractionArbiter.canInteract() && this.isActivationHeld();
   }

   public float breakBlocksAlongSight() {
      return this.zoneWidth.getValue().floatValue();
   }

   public double breakBlocksAlongSight() {
      return this.zoneOpacity.getValue();
   }

   public boolean getMoveKeys() {
      return this.zoneFill.getValue();
   }

   public boolean isZoneBloom() {
      return this.zoneBloom.getValue();
   }

   public boolean isZoneMotion() {
      return this.zoneMotion.getValue();
   }

   public String isTargeting() {
      if (this.target != null && minecraft.player != null) {
         String var1 = InteractionArbiter.findAttackBlocker(this.target, this.getBaseReach(), minecraft.player.getLookAngle());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   private void getAimSettings(String var1) {
      LarpLog.debug("Aura", var1);
   }

   public static void getCombatSettings(LocalPlayer var0, String var1) {
      AuraModule var2 = instance;
      if (var2 != null && var2.isRuntimeActive() && var2.target != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.jumpLogBudget <= 0) {
               return;
            }

            var2.jumpLogBudget--;
            var2.jumpLogUntilTick = var0.tickCount + 16;
         }

         if (var0.tickCount <= var2.jumpLogUntilTick) {
            Input var3 = var0.input.keyPresses;
            Vec2 var4 = var0.input.getMoveVector();
            Vec3 var5 = var0.getDeltaMovement();
            Rotation var6 = getLastRotation(var0);
            Options var7 = getMc().options;
            Input var8 = new Input(
               var7.keyUp.isDown(),
               var7.keyDown.isDown(),
               var7.keyLeft.isDown(),
               var7.keyRight.isDown(),
               var7.keyJump.isDown(),
               var7.keyShift.isDown(),
               var7.keySprint.isDown()
            );
            LarpLog.info(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.tickCount,
                  var2.rotation.getValue(),
                  var2.moveFix.getValue(),
                  var2.sprintMode.getValue(),
                  var2.movementReference.getValue(),
                  var2.target.getId(),
                  var0.onGround(),
                  var0.isSprinting(),
                  CombatState.isSprinting(var0.isSprinting()),
                  sprintResetActive,
                  var0.getYRot(),
                  var6.yaw,
                  var3,
                  var8,
                  var4.x,
                  var4.y,
                  var0.getX(),
                  var0.getY(),
                  var0.getZ(),
                  var5.x,
                  var5.y,
                  var5.z,
                  var0.fallDistance,
                  TargetStrafeModule.restoreInput()
               )
            );
         }
      }
   }

   public static Input getMiscSettings(Input var0) {
      return com.larpclient.util.entity.CombatUtil.hasTickedThisTick(var0);
   }
}
