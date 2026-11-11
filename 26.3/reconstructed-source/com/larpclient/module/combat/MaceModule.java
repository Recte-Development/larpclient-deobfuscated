package com.larpclient.module.combat;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.network.ServerBrandCheck;
import com.larpclient.util.entity.AimPoints;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.RotationController;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.math.MaceMath;
import com.larpclient.util.math.RotationLimiter;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class MaceModule extends Module {
   private static MaceModule instance;
   private final BooleanSetting autoSwap = this.addSetting(isSilentAimActive("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final ModeSetting pickMode = this.addSetting(
      findMaceSlot("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst")
   );
   private final BooleanSetting breachSwap = this.addSetting(
      isSilentAimActive(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final BooleanSetting swapBack = this.addSetting(
      isSilentAimActive("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true)
   );
   private final BooleanSetting smashAssist = this.addSetting(
      isSilentAimActive("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false)
   );
   private final ModeSetting smashTiming = this.addSetting(
      findMaceSlot("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final NumberSetting minFall = this.addSetting(new NumberSetting("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final BooleanSetting mobs = this.addSetting(isSilentAimActive("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final BooleanSetting silentAim = this.addSetting(
      isSilentAimActive("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false)
   );
   private final NumberSetting aimRange = this.addSetting(
      trySmash("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5)
   );
   private final NumberSetting turnSpeed = this.addSetting(
      trySmash("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0)
   );
   private final ModeSetting rotationMode = this.addSetting(
      findMaceSlot(
         "rotation",
         "Rotation",
         "Kill Aura rotation engine that turns the player toward the target; Legit moves most like a hand",
         "Sloth",
         "Sloth",
         "FunTime",
         "Polar",
         "Matrix",
         "Legit"
      )
   );
   private final BooleanSetting leadTurn = this.addSetting(
      isSilentAimActive("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final ModeSetting stunSlam = this.addSetting(
      findMaceSlot(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final ModeSetting rangeRing = this.addSetting(
      findMaceSlot(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final BooleanSetting targetMark = this.addSetting(
      isSilentAimActive(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final NumberSetting returnSpeed = this.addSetting(
      trySmash(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final RotationController viewReturnController = new RotationController();
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private final HumanizedRotation slothRotation = new HumanizedRotation();
   private final HumanizedRotation2 funTimeRotation = new HumanizedRotation2();
   private final SmoothRotation polarRotation = new SmoothRotation();
   private final HumanizedRotator matrixRotation = new HumanizedRotator();
   private final HumanizedRotation3 legitRotation = new HumanizedRotation3();
   private RotationProvider activeRotation;
   private boolean smashUsedThisFall;
   private boolean delayingSmash;
   private LocalPlayer delayPlayer;
   private Object delayLevel;
   private int delayTick = Integer.MIN_VALUE;
   private LivingEntity silentAimTarget;
   private String status = "Waiting";
   private LivingEntity smashTarget;
   private int smashTick;
   private int targetHurtTime;
   private int retryCount;
   private double smashFallSpeed;
   private boolean awaitingConfirmation;
   private LivingEntity stunSlamTarget;
   private int stunSlamTick = Integer.MIN_VALUE;
   private boolean stunSlamSameTick;
   private String lastHoldReason = "";
   private String lastSkippedSwap = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      instance = this;
      this.pickMode.visibleWhen(this.autoSwap::getValue);
      this.breachSwap.visibleWhen(this.autoSwap::getValue);
      this.swapBack.visibleWhen(() -> this.autoSwap.getValue() || this.isAssistEnabled());
      this.smashTiming.visibleWhen(this::isAssistEnabled);
      this.minFall.visibleWhen(this::isAssistEnabled);
      this.mobs.visibleWhen(this::isAssistEnabled);
      this.aimRange.visibleWhen(this.silentAim::getValue);
      this.leadTurn.visibleWhen(this.silentAim::getValue);
      this.turnSpeed.visibleWhen(this.silentAim::getValue);
      this.rotationMode.visibleWhen(this.silentAim::getValue);
      this.rangeRing.visibleWhen(this.silentAim::getValue);
      this.targetMark.visibleWhen(this.silentAim::getValue);
      this.returnSpeed.visibleWhen(this.silentAim::getValue);
      this.silentAim.onChange(var1 -> this.releaseView());
   }

   public static MaceModule getInstance() {
      return instance;
   }

   public RotationController getRotationController() {
      return this.viewReturnController;
   }

   public double shouldDelaySmash() {
      return this.returnSpeed.getValue();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   private boolean isAssistEnabled() {
      return this.smashAssist.getValue() || this.silentAim.getValue();
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public void onEnable() {
      this.resetState();
      LarpLog.info(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.autoSwap.getValue(),
         this.smashAssist.getValue(),
         this.silentAim.getValue(),
         this.smashTiming.getValue(),
         this.minFall.getValue(),
         this.stunSlam.getValue(),
         this.leadTurn.getValue(),
         this.rangeRing.getValue(),
         this.targetMark.getValue(),
         this.returnSpeed.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.resetState();
      this.releaseView();
      this.hotbarSwap.restore(getMc(), this.swapBack.getValue());
      LarpLog.debug("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void updateSmashConfirmation() {
      if (instance != null && instance.isEnabled()) {
         MaceModule var0 = instance;
         if (var0 != null && var0.isRuntimeActive()) {
            Minecraft var1 = getMc();
            var0.delayPlayer = null;
            var0.delayLevel = null;
            var0.delayTick = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.onGround()) {
               var0.getEnchantLevel();
            }

            if (var0.stunSlamTarget == null) {
               var0.hotbarSwap.tick(var1, var0.swapBack.getValue());
            }
         }
      }
   }

   private void getEnchantLevel() {
      if (this.awaitingConfirmation) {
         LarpLog.info("Mace", "Landed before the server confirmed the smash on {}", getEntityName(this.smashTarget));
      }

      this.smashUsedThisFall = this.awaitingConfirmation = (boolean)0;
      this.smashTarget = null;
      this.retryCount = 0;
      this.stunSlamTarget = null;
      this.lastHoldReason = "";
   }

   public void onPreAttack(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
            boolean var4 = MaceItem.canSmashAttack(var1);
            if (this.shouldStunSlam(var1, var2)) {
               int var7 = CombatUtil.findShieldBreakerSlot(var1);
               if (this.hotbarSwap.swapTo(var1, var7)) {
                  CombatUtil.markRecentTarget(var3);
                  this.stunSlamTarget = var3;
                  this.stunSlamTick = var1.tickCount;
                  this.stunSlamSameTick = this.stunSlam.isMode("Same Tick");
                  this.status = "Stun Slam";
                  LarpLog.info(
                     "Mace",
                     "Stun Slam on {}: axe slot {} for your hit, smash {}",
                     getEntityName(var3),
                     var7 + 1,
                     this.stunSlamSameTick ? "in the same tick" : "next tick"
                  );
               }
            } else if (this.autoSwap.getValue() && (!CombatUtil.isBlockingAttack(var3, var1) || CombatUtil.isRecentTarget(var3))) {
               if (var4 || this.breachSwap.getValue() && !this.formatNumber("Breach Swap")) {
                  int var5 = this.findMaceSlot(var1, var3, var4);
                  if (var5 >= 0
                     && MaceMath.hasEnoughDamage(
                        var4, expectedDamage(var1, var1.getInventory().getItem(var5), var3), expectedDamage(var1, var1.getMainHandItem(), var3)
                     )) {
                     int var6 = var1.getInventory().getSelectedSlot();
                     if (this.hotbarSwap.swapTo(var1, var5) && var6 != var5) {
                        LarpLog.debug("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.markSmashSent(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean formatNumber(String var1) {
      if (!ServerBrandCheck.isPaperServer()) {
         return false;
      }

      if (!var1.equals(this.lastSkippedSwap)) {
         LarpLog.info("Mace", "{} skipped: {} resets the charge on a swap", var1, ServerBrandCheck.getServerBrand());
      }

      this.lastSkippedSwap = var1;
      return true;
   }

   public boolean shouldStunSlam(LocalPlayer var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.stunSlam.isMode("Off")
         && var1 == getMc().player
         && var2 instanceof Player var3
         && !InteractionArbiter.isAttacking()
         && MaceItem.canSmashAttack(var1)) {
         MaceMath.SmashMode var4 = MaceMath.resolveSmashMode(
            this.stunSlam.getValue(),
            CombatUtil.isBlockingAttack(var3, var1),
            CombatUtil.isRecentTarget(var3),
            CombatUtil.findShieldBreakerSlot(var1) >= 0,
            this.findUsableMaceSlot(var1, var3) >= 0
         );
         return var4 == MaceMath.SmashMode.SAME_TICK || var4 == MaceMath.SmashMode.NEXT_TICK;
      } else {
         return false;
      }
   }

   public void isAssistEnabled(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.stunSlamTarget && this.stunSlamSameTick && this.stunSlamTick == var1.tickCount) {
            LivingEntity var3 = this.stunSlamTarget;
            this.stunSlamTarget = null;
            this.shouldStunSlam(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean shouldStunSlam(Minecraft var1, LocalPlayer var2, LivingEntity var3, String var4) {
      int var5 = this.findUsableMaceSlot(var2, var3);
      if (var5 >= 0 && this.hotbarSwap.swapTo(var2, var5)) {
         boolean var6 = InteractionArbiter.runAttackFollowup(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            SwingHelper.swingAttack(var2, InteractionHand.MAIN_HAND);
         });
         if (var6) {
            this.markSmashSent(var2, var3);
            LarpLog.info("Mace", "{}: fall {}, mace slot {}", var4, formatNumber(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         LarpLog.info("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int findUsableMaceSlot(LocalPlayer var1, LivingEntity var2) {
      if (var1.getMainHandItem().is(Items.MACE) && !var1.getMainHandItem().nextDamageWillBreak()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.autoSwap.getValue() ? this.findMaceSlot(var1, var2, true) : -1;
      }
   }

   private int findMaceSlot(LocalPlayer var1, LivingEntity var2, boolean var3) {
      MaceMath.Anonymous[] var4 = new MaceMath.Anonymous[Inventory.getSelectionSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getItem(var5);
         if (var6.is(Items.MACE) && !var6.nextDamageWillBreak()) {
            int var7 = getEnchantLevel(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new MaceMath.Anonymous(var5, true, false, var7, getEnchantLevel(var6, Enchantments.WIND_BURST), expectedDamage(var1, var6, var2));
            }
         }
      }

      return MaceMath.selectMaceSlot(var4, var3, this.pickMode.isMode("Wind Burst"));
   }

   private static float expectedDamage(LocalPlayer var0, ItemStack var1, LivingEntity var2) {
      return MaceMath.calculateDamage(
         CombatUtil.getAttackDamage(var0.getMainHandItem()),
         CombatUtil.getAttackStrength(var0),
         var0.fallDistance,
         var1.is(Items.MACE),
         MaceItem.canSmashAttack(var0),
         getEnchantLevel(var1, Enchantments.DENSITY),
         CombatUtil.isCriticalHit(var0, var2),
         getEnchantLevel(var1, Enchantments.SHARPNESS),
         var2.getArmorValue(),
         (float)var2.getAttributeValue(Attributes.ARMOR_TOUGHNESS),
         getEnchantLevel(var1, Enchantments.BREACH)
      );
   }

   private static int getEnchantLevel(ItemStack var0, ResourceKey<Enchantment> var1) {
      for (Entry var3 : var0.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(var1)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   @Override
   public void onTick() {
      boolean var1 = this.delayingSmash;
      this.delayingSmash = (boolean)0;
      Minecraft var2 = getMc();
      if (var2.player != null) {
         this.updateSmashConfirmation(var2.player);
      }

      boolean var10 = false;
      byte var9 = this.smashUsedThisFall;
      int var10001 = Integer.signum(var9);
      String var18;
      if (((var10001 | -var10001) >>> 31 ^ 1) == 0) {
         boolean var12 = false;
         byte var11 = this.awaitingConfirmation;
         var10001 = Integer.signum(var11);
         var18 = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "Waiting for the server" : "Smash used this fall";
      } else {
         var18 = "Waiting";
      }

      this.status = var18;
      if (this.stunSlamTarget != null && !this.stunSlamSameTick) {
         this.handleNextTickSmash(var2);
      } else {
         label120: {
            if (this.stunSlamSameTick && this.stunSlamTarget != null && var2.player != null) {
               int var16 = var2.player.tickCount;
               int var15 = this.stunSlamTick;
               var10001 = Integer.compare(var15, var16);
               if ((var10001 | -var10001) >>> 31 == 0) {
                  var20 = this.stunSlamTarget;
                  break label120;
               }
            }

            var20 = null;
         }

         this.stunSlamTarget = var20;
         if (!this.isAssistEnabled()) {
            this.releaseView();
         } else {
            LivingEntity var3 = this.silentAim.getValue() ? this.findSilentAimTarget(var2) : null;
            if (var3 == null) {
               this.releaseView();
            }

            if (CombatUtil.isActiveFor(var2, ViewController.Owner.MACE) && HotbarSwapSession.canSwap(this)) {
               LocalPlayer var4;
               EntityHitResult var5;
               LivingEntity var6;
               int var7;
               int var22;
               boolean var10000;
               boolean var10002;
               int var10003;
               boolean var10004;
               int var10005;
               boolean var10006;
               double var10007;
               double var10008;
               label96: {
                  var4 = var2.player;
                  var5 = CombatUtil.getCrosshairEntity(var2);
                  var6 = this.findCrosshairTarget(var4, var5, var3);
                  var7 = var6 == null && var3 == null ? 0 : 1;
                  var10000 = CombatUtil.hasTickedThisTick(var4);
                  var21 = CombatUtil.isAttackPressed(var2);
                  var10002 = CombatUtil.isUsePressed(var2);
                  var10003 = var3 == null && RotationManager.hasRequest() ? 1 : 0;
                  var10004 = CombatUtil.canAttack(var2, var4);
                  var10005 = var4.getDeltaMovement().y < 0.0 ? 1 : 0;
                  var10006 = MaceItem.canSmashAttack(var4);
                  var10007 = var4.fallDistance;
                  var10008 = this.minFall.getValue();
                  if (this.smashUsedThisFall) {
                     boolean var14 = false;
                     byte var13 = this.awaitingConfirmation;
                     var22 = Integer.signum(var13);
                     if ((var22 | -var22) >>> 31 == 0) {
                        var22 = 1;
                        break label96;
                     }
                  }

                  var22 = 0;
               }

               String var8 = MaceMath.blockReason(
                  var10000,
                  var21,
                  var10002,
                  (boolean)var10003,
                  var10004,
                  (boolean)var10005,
                  var10006,
                  var10007,
                  var10008,
                  (boolean)var22,
                  this.awaitingConfirmation,
                  var6 != null && this.findUsableMaceSlot(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.trySmash(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.resetState(var8, var4, var3);
               }

               if (var3 != null && (!this.smashUsedThisFall || this.awaitingConfirmation)) {
                  if (!CombatUtil.hasTickedThisTick(var4)) {
                     this.findSilentAimTarget(var2, var3);
                  }
               } else {
                  this.releaseView();
               }
            } else {
               this.releaseView();
            }
         }
      }
   }

   private LivingEntity findCrosshairTarget(LocalPlayer var1, EntityHitResult var2, LivingEntity var3) {
      if (!(var2 != null && var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.smashAssist.getValue() && var4 != var3) {
         return null;
      } else {
         return CombatUtil.isValidTarget(var1, var4) && (var4 instanceof Player || this.mobs.getValue()) ? var4 : null;
      }
   }

   private void resetState(String var1, LocalPlayer var2, LivingEntity var3) {
      if (!var1.equals(this.lastHoldReason)) {
         this.lastHoldReason = var1;
         LarpLog.info(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            formatNumber(var2.fallDistance),
            formatNumber(var2.getDeltaMovement().y),
            var3 == null
               ? ""
               : ", Silent Aim target "
                  + getEntityName(var3)
                  + " at "
                  + formatNumber(BoxRaycastUtil.distanceToBox(var2.getEyePosition(), var3.getBoundingBox()))
         );
      }
   }

   private void trySmash(Minecraft var1, LocalPlayer var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.lastHoldReason = "";
      MaceMath.SmashMode var7 = MaceMath.resolveSmashMode(
         this.stunSlam.getValue(),
         CombatUtil.isBlockingAttack(var4, var2),
         CombatUtil.isRecentTarget(var4),
         CombatUtil.findShieldBreakerSlot(var2) >= 0,
         this.findUsableMaceSlot(var2, var4) >= 0
      );
      if ((var7 != MaceMath.SmashMode.NEXT_TICK || !isNextStepInReach(var2, var4)) && var7 != MaceMath.SmashMode.NORMAL) {
         if (var7 == MaceMath.SmashMode.NONE && CombatUtil.isBlockingAttack(var4, var2) && !CombatUtil.isRecentTarget(var4)) {
            this.resetState("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.shouldDelaySmash(var2, var4)) {
            this.delayingSmash = (boolean)1;
            this.delayPlayer = var2;
            this.delayLevel = var1.level;
            this.delayTick = var2.tickCount;
            var2.setSprinting(false);
            CombatUtil.canAttack(var1, Math.max(CombatUtil.getAttackDelay(var1), 1));
            this.status = "Delaying smash";
            if (!var6) {
               LarpLog.debug("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != MaceMath.SmashMode.NONE) {
            this.isAssistEnabled(var1, var2, var4, var7);
         } else {
            int var8 = this.findUsableMaceSlot(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.hotbarSwap.swapTo(var2, var8)) {
               if (CombatUtil.isUsePressed(var1, var3)) {
                  this.markSmashSent(var2, var4);
                  CombatUtil.consumeClicks(var1);
                  LarpLog.info(
                     "Mace",
                     "Smash sent on {}: fall {}, speed {}, silent={}",
                     getEntityName(var4),
                     formatNumber(var2.fallDistance),
                     formatNumber(var2.getDeltaMovement().y),
                     var4 == var5
                  );
               } else {
                  LarpLog.info("Mace", "Smash attack refused by the client gate on {}", getEntityName(var4));
               }
            }
         }
      } else {
         this.isAssistEnabled(var1, var2, var4, var7);
      }
   }

   private void isAssistEnabled(Minecraft var1, LocalPlayer var2, LivingEntity var3, MaceMath.SmashMode var4) {
      int var5 = CombatUtil.findShieldBreakerSlot(var2);
      int var6 = this.findUsableMaceSlot(var2, var3);
      if (this.hotbarSwap.swapTo(var2, var5)) {
         int var7 = var4 == MaceMath.SmashMode.SAME_TICK ? 1 : 0;
         boolean var8 = InteractionArbiter.runAttackClaim(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            SwingHelper.swingAttack(var2, InteractionHand.MAIN_HAND);
            CombatUtil.markRecentTarget(var3);
            if (var7 && this.hotbarSwap.swapTo(var2, var6)) {
               var1.gameMode.attack(var2, var3);
               SwingHelper.swingAttack(var2, InteractionHand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.hotbarSwap.restore(var1, true);
            LarpLog.info("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            CombatUtil.consumeClicks(var1);
            if (var7 != 0) {
               this.markSmashSent(var2, var3);
               this.status = "Stun Slam";
               LarpLog.info(
                  "Mace",
                  "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}",
                  getEntityName(var3),
                  var5 + 1,
                  var6 + 1,
                  formatNumber(var2.fallDistance)
               );
            } else if (var4 == MaceMath.SmashMode.NEXT_TICK) {
               this.stunSlamTarget = var3;
               this.stunSlamTick = var2.tickCount;
               this.stunSlamSameTick = (boolean)0;
               this.status = "Stun Slam";
               LarpLog.info("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", getEntityName(var3), var5 + 1);
            } else {
               this.status = "Shield break hit";
               LarpLog.info("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", getEntityName(var3), var5 + 1);
            }
         }
      }
   }

   private void handleNextTickSmash(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      LivingEntity var3 = this.stunSlamTarget;
      if (var2 != null && var2.tickCount == this.stunSlamTick + 1) {
         this.stunSlamTarget = null;
         EntityHitResult var4 = CombatUtil.getCrosshairEntity(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.canSmashAttack(var2) && !var2.onGround()) {
            int var5 = this.findUsableMaceSlot(var2, var3);
            if (var5 >= 0 && this.hotbarSwap.swapTo(var2, var5)) {
               boolean var6 = InteractionArbiter.runAttackClaim(this, var3, () -> {
                  var1.gameMode.attack(var2, var3);
                  SwingHelper.swingAttack(var2, InteractionHand.MAIN_HAND);
               });
               if (var6) {
                  this.markSmashSent(var2, var3);
                  CombatUtil.consumeClicks(var1);
                  LarpLog.info("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", getEntityName(var3), formatNumber(var2.fallDistance));
               } else {
                  LarpLog.info("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", getEntityName(var3));
               }
            } else {
               this.hotbarSwap.restore(var1, this.swapBack.getValue());
            }
         } else {
            LarpLog.info(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               getEntityName(var3),
               var2.onGround() ? "landed" : (!MaceItem.canSmashAttack(var2) ? "fall ended" : "facing left the target")
            );
            this.hotbarSwap.restore(var1, this.swapBack.getValue());
         }
      } else {
         if (var2 == null || var2.tickCount > this.stunSlamTick + 1) {
            this.stunSlamTarget = null;
            this.hotbarSwap.restore(var1, this.swapBack.getValue());
         }
      }
   }

   private void markSmashSent(LocalPlayer var1, LivingEntity var2) {
      this.smashUsedThisFall = this.awaitingConfirmation = (boolean)1;
      this.smashTarget = var2;
      this.smashTick = var1.tickCount;
      this.targetHurtTime = var2.hurtTime;
      this.smashFallSpeed = var1.getDeltaMovement().y;
      this.status = "Waiting for the server";
   }

   private void updateSmashConfirmation(LocalPlayer var1) {
      if (this.awaitingConfirmation && this.smashTarget != null) {
         if (MaceMath.shouldAbortFall(this.smashFallSpeed, var1.getDeltaMovement().y, this.targetHurtTime, this.smashTarget.hurtTime)) {
            this.awaitingConfirmation = (boolean)0;
            LarpLog.info("Mace", "Smash confirmed on {} after {} ticks", getEntityName(this.smashTarget), var1.tickCount - this.smashTick);
         } else if (var1.tickCount - this.smashTick >= MaceMath.smashTicks(this.markSmashSent(var1))) {
            this.awaitingConfirmation = (boolean)0;
            if (this.retryCount < 1 && !var1.onGround() && MaceItem.canSmashAttack(var1)) {
               this.retryCount++;
               this.smashUsedThisFall = (boolean)0;
               LarpLog.info(
                  "Mace",
                  "No server confirmation for the smash on {} after {} ticks; retrying once this fall",
                  getEntityName(this.smashTarget),
                  var1.tickCount - this.smashTick
               );
            } else {
               LarpLog.info(
                  "Mace",
                  "No server confirmation for the smash on {}; not retrying ({})",
                  getEntityName(this.smashTarget),
                  this.retryCount >= 1 ? "already retried" : "fall ended"
               );
            }
         }
      }
   }

   private int markSmashSent(LocalPlayer var1) {
      ClientPacketListener var2 = getMc().getConnection();
      PlayerInfo var3 = var2 == null ? null : var2.getPlayerInfo(var1.getUUID());
      return var3 == null ? 0 : Math.round(var3.getLatency() / 50.0F);
   }

   private LivingEntity findSilentAimTarget(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      if (var2 != null
         && var1.level != null
         && (!this.smashUsedThisFall || this.awaitingConfirmation)
         && !var2.onGround()
         && !(var2.getDeltaMovement().y >= 0.0)
         && ViewController.canAcquire(ViewController.Owner.MACE)) {
         if (this.silentAimTarget != null && this.isValidSilentTarget(var2, this.silentAimTarget, this.markSmashSent(var2, this.silentAimTarget))) {
            return this.silentAimTarget;
         }

         Vec3 var3 = var2.getEyePosition();
         double var4 = 16.0;
         LivingEntity var6 = null;
         double var7 = Double.MAX_VALUE;

         for (Entity var10 : var1.level
            .getEntities(var2, var2.getBoundingBox().inflate(Math.max(this.aimRange.getValue(), var4)), var0 -> var0 instanceof LivingEntity)) {
            LivingEntity var11 = (LivingEntity)var10;
            double var12 = BoxRaycastUtil.distanceToBox(var3, var11.getBoundingBox());
            if (var12 < var7 && this.isValidSilentTarget(var2, var11, this.markSmashSent(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double markSmashSent(LocalPlayer var1, LivingEntity var2) {
      Rotation var3 = new Rotation(var1.getYRot(), var1.getXRot());
      int var4 = this.getRotationProvider().ticksToReach(var3, RotationMath.rotationTo(findUsableMaceSlot(var1, var2)));
      return MaceMath.extendedReach(this.aimRange.getValue(), this.leadTurn.getValue(), -var1.getDeltaMovement().y, var4, var1.entityInteractionRange());
   }

   public double getRangeRingRadius(LocalPlayer var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive()
         && this.silentAim.getValue()
         && var1 != null
         && !this.rangeRing.isMode("Off")
         && (!this.rangeRing.isMode("In Air") || !var1.onGround())) {
         Rotation var2 = new Rotation(var1.getYRot(), var1.getXRot());
         int var3 = this.getRotationProvider().ticksToReach(var2, new Rotation(var1.getYRot(), 90.0F));
         return MaceMath.extendedReach(this.aimRange.getValue(), this.leadTurn.getValue(), -var1.getDeltaMovement().y, var3, var1.entityInteractionRange());
      } else {
         return 0.0;
      }
   }

   public LivingEntity getTargetMarkEntity() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.targetMark.getValue() && this.isSilentAimActive() ? this.silentAimTarget : null;
      }
   }

   private boolean isValidSilentTarget(LocalPlayer var1, LivingEntity var2, double var3) {
      Vec3 var5 = var1.getEyePosition();
      return CombatUtil.isValidTarget(var1, var2)
         && (var2 instanceof Player || this.mobs.getValue())
         && BoxRaycastUtil.distanceToBox(var5, var2.getBoundingBox()) <= var3
         && this.getEnchantLevel(var1, var2)
         && CombatUtil.hasLineOfSight(var1, var5, findUsableMaceSlot(var1, var2));
   }

   private static Vec3 findUsableMaceSlot(LocalPlayer var0, LivingEntity var1) {
      AABB var2 = var1.getBoundingBox();
      Vec3 var3 = var0.getEyePosition();
      AABB var4 = var2.deflate(Math.min(0.1, var2.getXsize() * 0.25), Math.min(0.1, var2.getYsize() * 0.25), Math.min(0.1, var2.getZsize() * 0.25));
      Vec3 var5 = new Vec3(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3 var6 = new Vec3(var2.getCenter().x, var2.minY + var2.getYsize() * 0.6, var2.getCenter().z);
      return InteractionArbiter.findVisibleAimPoint(var1, var0.entityInteractionRange(), var6, AimPoints.toRelative(var2, var5)).orElse(var6);
   }

   private RotationProvider getRotationProvider() {
      return switch ((String)this.rotationMode.getValue()) {
         case "FunTime" -> this.funTimeRotation;
         case "Polar" -> this.polarRotation;
         case "Matrix" -> this.matrixRotation;
         case "Legit" -> this.legitRotation;
         default -> this.slothRotation;
      };
   }

   private void findSilentAimTarget(Minecraft var1, LivingEntity var2) {
      LocalPlayer var3 = var1.player;
      if (!InteractionArbiter.ownsInteraction(this)) {
         this.releaseView();
      } else {
         RotationProvider var4 = this.getRotationProvider();
         if (var4 != this.activeRotation) {
            if (this.activeRotation != null) {
               this.activeRotation.reset();
            }

            this.activeRotation = var4;
         }

         AABB var5 = var2.getBoundingBox();
         if (var2 != this.silentAimTarget) {
            this.activeRotation.targetChanged(var2);
            LarpLog.info(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               getEntityName(var2),
               formatNumber(BoxRaycastUtil.distanceToBox(var3.getEyePosition(), var5)),
               formatNumber(-var3.getDeltaMovement().y),
               this.rotationMode.getValue()
            );
         }

         this.silentAimTarget = var2;
         ViewController.acquire(ViewController.Owner.MACE);
         if (ViewController.update() != ViewController.Owner.MACE) {
            this.silentAimTarget = null;
         } else {
            Rotation var6 = new Rotation(var3.getYRot(), var3.getXRot());
            double var7 = var3.entityInteractionRange();
            int var9 = BoxRaycastUtil.distanceToBox(var3.getEyePosition(), var5) <= var7 ? 1 : 0;
            LocalPlayerMixin_4 var10 = (LocalPlayerMixin_4)var3;
            boolean var11 = com.larpclient.module.misc.CombatUtil.isLookingAtBox(
               var3, var5.inflate(var2.getPickRadius()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false
            );
            Rotation var12 = this.activeRotation.rotate(var6, RotationMath.rotationTo(findUsableMaceSlot(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = RotationLimiter.stepToward(var6.yaw, var6.pitch, var12.yaw, var12.pitch, this.turnSpeed.getValue());
            var12 = new Rotation(var13[0], var13[1]);
            var12 = this.activeRotation == this.legitRotation ? this.legitRotation.rotateFallback(var6, var12) : RotationMath.snapToMouseStep(var6, var12);
            RotationManager.requestForModule(this, RotationManager.Priority.MACE, var12.yaw, var12.pitch, 1.0);
            if (!RotationManager.isActive(RotationManager.Priority.MACE)) {
               LarpLog.debug("Mace", "Silent Aim yielded to a higher rotation owner");
               this.releaseView();
            } else {
               if (!this.delayingSmash) {
                  this.status = "Silent aim";
               }
            }
         }
      }
   }

   private void releaseView() {
      if (this.silentAimTarget != null && this.activeRotation != null) {
         this.activeRotation.reset();
      }

      this.silentAimTarget = null;
      ViewController.requestReturn(ViewController.Owner.MACE, this.viewReturnController, this.returnSpeed.getValue(), true);
   }

   public float adjustLookDelta(float var1, float var2) {
      if (this.isEnabled()) {
         return this.isSilentAimActive() ? AuraModule.getCombatSettings(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean isSilentAimActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.silentAim.getValue()
            && this.silentAimTarget != null
            && this.silentAimTarget.isAlive()
            && !this.silentAimTarget.isRemoved()
            && InteractionArbiter.getPriority(this);
   }

   public void markSmashUsed() {
      if (this.isEnabled()) {
         this.smashUsedThisFall = (boolean)1;
         this.status = "Smash used this fall";
      }
   }

   public boolean getEnchantLevel(LocalPlayer var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.getMainHandItem().is(Items.MACE);
      int var4 = !var3
            && this.autoSwap.getValue()
            && this.isRuntimeActive()
            && !this.smashUsedThisFall
            && MaceItem.canSmashAttack(var1)
            && var1.fallDistance >= this.minFall.getValue()
            && this.findMaceSlot(var1, var2, true) >= 0
         ? 1
         : 0;
      return MaceMath.canSmash(
         this.isRuntimeActive(),
         this.smashUsedThisFall,
         MaceItem.canSmashAttack(var1),
         var1.fallDistance,
         this.minFall.getValue(),
         var3,
         this.autoSwap.getValue(),
         (boolean)var4
      );
   }

   public boolean shouldDelaySmash(LocalPlayer var1, LivingEntity var2) {
      return this.isEnabled()
         ? MaceMath.shouldAttack(
            isNextStepInReach(var1, var2),
            this.smashTiming.isMode("Delayed"),
            CombatUtil.getAttackStrength(var1),
            var1.getAttackStrengthScale(1.5F),
            var2.hurtTime,
            var1.isSprinting()
         )
         : false;
   }

   private static boolean isNextStepInReach(LocalPlayer var0, LivingEntity var1) {
      Vec3 var2 = var0.getDeltaMovement();
      return MaceMath.isPathClear(
         var0.level().noCollision(var0, var0.getBoundingBox().move(var2)),
         var0.entityInteractionRange(),
         var0.getEyePosition(),
         var2,
         var1.getBoundingBox(),
         var1.position().subtract(var1.xo, var1.yo, var1.zo),
         var1.getPickRadius(),
         var0.getViewVector(1.0F),
         (var1x, var2x) -> CombatUtil.hasLineOfSight(var0, var1x, var2x)
      );
   }

   public static boolean isViewOwned() {
      if (instance != null && instance.isEnabled()) {
         MaceModule var0 = instance;
         if (var0 != null && var0.isRuntimeActive() && var0.isAssistEnabled()) {
            Minecraft var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return ViewController.update() == ViewController.Owner.MACE
                  ? true
                  : var1.player == var0.delayPlayer
                     && var1.level == var0.delayLevel
                     && var1.gui.screen() == null
                     && var1.player.tickCount >= var0.delayTick
                     && (long)var1.player.tickCount - var0.delayTick <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input lockSprintInput(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         MaceModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && ViewController.update() == ViewController.Owner.MACE) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               LarpLog.debug("Mace", "Silent Aim lock released sprint");
            }

            return CombatUtil.hasTickedThisTick(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void resetState() {
      this.smashUsedThisFall = this.awaitingConfirmation = (boolean)0;
      this.delayingSmash = (boolean)0;
      this.smashTarget = null;
      this.retryCount = 0;
      this.stunSlamTarget = null;
      this.lastHoldReason = "";
      this.delayPlayer = null;
      this.delayLevel = null;
      this.delayTick = Integer.MIN_VALUE;
      this.status = "Waiting";
   }

   private static String getEntityName(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String formatNumber(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static BooleanSetting isSilentAimActive(String var0, final String var1, String var2, boolean var3) {
      return new BooleanSetting(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static NumberSetting trySmash(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new NumberSetting(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static ModeSetting findMaceSlot(String var0, final String var1, String var2, String var3, String... var4) {
      return new ModeSetting(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}
