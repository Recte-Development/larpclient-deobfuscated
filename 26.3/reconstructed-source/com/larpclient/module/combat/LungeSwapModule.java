package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.LivingEntityMixin;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.network.ServerBrandCheck;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.inventory.SwapLogic;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public final class LungeSwapModule extends Module {
   private static LungeSwapModule instance;
   public static final int cHARGE_GRACE_TICKS = 10;
   public static final int jAB_RESULT_TICKS = 12;
   public static final double lUNGE_SPEED_THRESHOLD = 0.4;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final KeybindSetting activationKey = this.addSetting(
      new KeybindSetting("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86)
   );
   private final BooleanSetting swapBack = this.addSetting(new BooleanSetting("Swap Back", "Return after the jab; Blatant always returns", true));
   private final BooleanSetting swapBackToSlot = this.addSetting(
      new BooleanSetting("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false)
   );
   private final NumberSetting swapBackSlot = this.addSetting(new NumberSetting("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final BooleanSetting holdToLunge = this.addSetting(new BooleanSetting("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final BooleanSetting chargeAfterSwap = this.addSetting(
      new BooleanSetting(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final ModeSetting swapRule = this.addSetting(
      new ModeSetting(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final NumberSetting jabDelay = this.addSetting(
      new NumberSetting(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final NumberSetting jabDelayMax = this.addSetting(
      new NumberSetting("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0)
   );
   private final NumberSetting swapBackDelay = this.addSetting(
      new NumberSetting("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final NumberSetting swapBackDelayMax = this.addSetting(
      new NumberSetting("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0)
   );
   private final NumberSetting holdInterval = this.addSetting(
      new NumberSetting("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0)
   );
   private final NumberSetting holdIntervalMax = this.addSetting(
      new NumberSetting("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0)
   );
   private static final Map<String, Boolean> serverResetsCharge = new HashMap<>();
   private LungeSwapModule.State state = LungeSwapModule.State.IDLE;
   private boolean pressQueued;
   private int jabDelayTicks;
   private int swapBackTicks;
   private int holdCooldown;
   private int previousSlot = -1;
   private int spearSlot = -1;
   private int chargeWaitTicks;
   private int blockedTicks;
   private int requiredTicks;
   private int chargingTicks;
   private int chargeTarget;
   private LungeScheduler.CooldownMode cooldownMode = LungeScheduler.CooldownMode.KEPT;
   private String previousItemName = "";
   private String heldReason = "";
   private float previousCharge;
   private LocalPlayer player;
   private ClientLevel level;
   private String status = "Ready";
   private String conflictingBind = "";
   private int conflictCheckTimer;
   private int resultTicks;
   private double peakSpeed;
   private boolean swappedInJabTick;
   private boolean swappedItem;
   private Vec3 jabStartPos = Vec3.ZERO;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      instance = this;
      this.swapBack.visibleWhen(this::isLegit);
      this.swapBackToSlot.visibleWhen(this::shouldSwapBack);
      this.swapBackSlot.visibleWhen(() -> this.shouldSwapBack() && this.swapBackToSlot.getValue());
      this.swapRule.visibleWhen(this.chargeAfterSwap::getValue);
      this.jabDelay.linkUpperBound(this.jabDelayMax).setDisplayName("Jab Delay");
      this.swapBackDelay.linkUpperBound(this.swapBackDelayMax).setDisplayName("Swap Back Delay");
      this.holdInterval.linkUpperBound(this.holdIntervalMax).setDisplayName("Hold Interval");
      this.swapBackDelay.visibleWhen(this::shouldSwapBack);
      this.swapBackDelayMax.visibleWhen(this::shouldSwapBack);
      this.holdInterval.visibleWhen(this.holdToLunge::getValue);
      this.holdIntervalMax.visibleWhen(this.holdToLunge::getValue);
      this.mode.onChange(var1 -> this.cancel(true));
      this.activationKey.onChange(var1 -> {
         this.cancel(true);
         this.conflictCheckTimer = 0;
      });
      this.swapRule.onChange(var1 -> this.cancel(true));
   }

   private boolean isLegit() {
      return this.mode.isMode("Legit");
   }

   private boolean isBlatant() {
      return this.mode.isMode("Blatant");
   }

   private boolean shouldSwapBack() {
      return this.isBlatant() || this.swapBack.getValue();
   }

   public static int randomInRange(boolean var0) {
      return LungeScheduler.swapPenalty(var0);
   }

   public static int randomInRange(double var0, double var2, double var4) {
      return SwapLogic.randomInRange((int)var0, (int)var2, var4);
   }

   private int randomDelay(NumberSetting var1, NumberSetting var2) {
      return randomInRange(var1.getValue(), var2.getValue(), ThreadLocalRandom.current().nextDouble());
   }

   public static int resolveSwapBackSlot(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static LungeScheduler.CooldownMode resolveCooldownMode(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return LungeScheduler.CooldownMode.KEPT;
      } else if ("Reset".equals(var1)) {
         return LungeScheduler.CooldownMode.RESET;
      } else if (var2 != null) {
         return var2 ? LungeScheduler.CooldownMode.RESET : LungeScheduler.CooldownMode.KEPT;
      } else {
         return var3 ? LungeScheduler.CooldownMode.RESET : LungeScheduler.CooldownMode.KEPT;
      }
   }

   public static void onKeyPress(int var0) {
      if (instance != null && instance.isEnabled()) {
         LungeSwapModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && BindCodes.isValidBind(var0) && var0 == var1.activationKey.getValue()) {
            var1.pressQueued = (boolean)1;
         }
      }
   }

   public boolean resolveSwapBackSlot() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.state != LungeSwapModule.State.IDLE && this.player != null && this.player == getMc().player;
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
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.state == LungeSwapModule.State.IDLE && !this.conflictingBind.isEmpty()
            ? BindCodes.displayName(this.activationKey.getValue()) + " also triggers " + this.conflictingBind
            : this.status;
      }
   }

   @Override
   public void onEnable() {
      this.reset();
      this.player = minecraft.player;
      this.level = minecraft.level;
      this.status = "Ready";
      this.conflictingBind = "";
      this.conflictCheckTimer = 0;
      LarpLog.info(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.mode.getValue(),
         this.activationKey.getValue(),
         this.holdToLunge.getValue(),
         this.chargeAfterSwap.getValue(),
         this.swapRule.getValue(),
         this.jabDelay.getValue().intValue(),
         this.jabDelayMax.getValue().intValue(),
         this.swapBackDelay.getValue().intValue(),
         this.swapBackDelayMax.getValue().intValue(),
         this.holdInterval.getValue().intValue(),
         this.holdIntervalMax.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.cancel(true);
      this.player = null;
      this.level = null;
   }

   private boolean canRun() {
      return this.isInGame()
         && this.player == minecraft.player
         && this.level == minecraft.level
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && minecraft.gui.screen() == null
         && (CombatUtil.isActive(minecraft) || ViewController.isReturning() && CombatUtil.isActiveFor(minecraft, ViewController.Owner.RETURN));
   }

   @Override
   public void onTick() {
      boolean var1 = this.pressQueued;
      this.pressQueued = (boolean)0;
      int var10000 = --this.conflictCheckTimer;
      boolean var4 = false;
      int var3 = var10000;
      if (-Integer.signum(var3) >>> 31 == 0) {
         this.conflictCheckTimer = 20;
         this.checkBindConflict();
      }

      if (this.player != minecraft.player || this.level != minecraft.level) {
         this.cancel(false);
         this.player = minecraft.player;
         this.level = minecraft.level;
         this.resultTicks = 0;
      }

      if (this.resultTicks > 0) {
         this.trackJabResult();
      }

      if (!this.canRun()) {
         this.cancel(true);
         this.status = "Paused";
      } else {
         boolean var2 = BindCodes.isPressed(minecraft.getWindow().handle(), this.activationKey.getValue());
         boolean var8 = false;
         int var7 = this.holdCooldown;
         if ((-Integer.signum(var7) >>> 31 ^ 1) == 0 && (!var2 || var1)) {
            this.holdCooldown = 0;
         }

         label60:
         if (this.state == LungeSwapModule.State.IDLE) {
            if (!var1) {
               if (!this.holdToLunge.getValue() || !var2) {
                  break label60;
               }

               var10000 = this.holdCooldown;
               byte var10 = 1;
               int var9 = this.holdCooldown;
               this.holdCooldown = var9 - var10;
               if (var10000 > 0) {
                  break label60;
               }
            }

            this.checkBindConflict(var1);
         }

         if (this.state == LungeSwapModule.State.SWAPPING) {
            this.tickSwapping();
         } else if (this.state == LungeSwapModule.State.CHARGING) {
            this.tickCharging();
         } else {
            if (this.state == LungeSwapModule.State.LUNGED) {
               var10000 = --this.swapBackTicks;
               boolean var6 = false;
               int var5 = var10000;
               if (-Integer.signum(var5) >>> 31 == 0) {
                  this.swapBackToSlotNow();
                  if (this.holdToLunge.getValue() && var2) {
                     this.holdCooldown = this.randomDelay(this.holdInterval, this.holdIntervalMax);
                  }

                  this.status = "Ready";
                  return;
               }
            }

            if (this.state == LungeSwapModule.State.IDLE && !BindCodes.isValidBind(this.activationKey.getValue())) {
               this.status = "Set an Activation Key";
            }
         }
      }
   }

   public static String findKeyConflict(Module var0, int var1, Iterable<Module> var2) {
      if (!BindCodes.isValidBind(var1)) {
         return "";
      }

      if (var0.getKeyBind() == var1) {
         return "the Lunge Swap toggle";
      }

      for (Module var4 : var2) {
         if (var4 != var0) {
            if (var4.getKeyBind() == var1) {
               return var4.getName();
            }

            for (Setting var6 : var4.getSettings()) {
               if (var6 instanceof KeybindSetting var7 && var7.getValue() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void checkBindConflict() {
      LarpClient var1 = LarpClient.getInstance();
      int var2 = this.activationKey.getValue();
      String var3 = var1 != null && var1.getModuleManager() != null ? findKeyConflict(this, var2, var1.getModuleManager().getModules()) : "";
      if (!var3.isEmpty() && !var3.equals(this.conflictingBind)) {
         LarpLog.warn(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            BindCodes.displayName(var2),
            var3
         );
      }

      this.conflictingBind = var3;
   }

   private void trackJabResult() {
      LocalPlayer var1 = minecraft.player;
      this.peakSpeed = Math.max(this.peakSpeed, var1.getDeltaMovement().horizontalDistance());
      if (--this.resultTicks <= 0) {
         int var2 = this.peakSpeed >= 0.4 ? 1 : 0;
         LarpLog.info(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.peakSpeed),
            String.format(Locale.ROOT, "%.2f", var1.position().subtract(this.jabStartPos).horizontalDistance()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.swappedInJabTick && this.chargeAfterSwap.getValue() && this.swapRule.isMode("Detect")) {
            String var3 = this.pickSpear();
            Boolean var4 = serverResetsCharge.get(var3);
            serverResetsCharge.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               LarpLog.info("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               LarpLog.info("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String pickSpear() {
      ServerData var1 = minecraft.getCurrentServer();
      return var1 != null ? var1.ip : (minecraft.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void checkBindConflict(boolean var1) {
      String var2 = this.performJab();
      if (var2 != null) {
         this.status = var2;
         if (var1) {
            LarpLog.info("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.pickSpear() == null) {
         this.status = "No Lunge spear in hotbar";
         if (var1) {
            LarpLog.info("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.previousSlot = minecraft.player.getInventory().getSelectedSlot();
         this.previousItemName = minecraft.player.getMainHandItem().getHoverName().getString();
         this.previousCharge = minecraft.player.getAttackStrengthScale(0.0F);
         this.cooldownMode = resolveCooldownMode(
            this.chargeAfterSwap.getValue(), this.swapRule.getValue(), serverResetsCharge.get(this.pickSpear()), ServerBrandCheck.isPaperServer()
         );
         this.chargeWaitTicks = this.blockedTicks = 0;
         this.requiredTicks = -1;
         this.heldReason = "";
         this.jabDelayTicks = this.randomDelay(this.jabDelay, this.jabDelayMax);
         this.state = LungeSwapModule.State.SWAPPING;
      }
   }

   private String performJab() {
      LocalPlayer var1 = minecraft.player;
      return LungeScheduler.unavailableReason(
         var1.getFoodData().getFoodLevel(), var1.getAbilities().instabuild, var1.isInWater(), var1.isPassenger(), var1.isFallFlying()
      );
   }

   private LungeScheduler.Pick pickSpear() {
      LocalPlayer var1 = minecraft.player;
      ItemStack var2 = var1.getMainHandItem();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.getLungeLevel(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getItem(var4);
            if (var6.isItemEnabled(this.level.enabledFeatures())) {
               var3.add(
                  new LungeScheduler.Candidate(
                     var4,
                     isLegit(var1, var6),
                     (Float)var6.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F),
                     var5,
                     var6.isDamageableItem() ? var6.getMaxDamage() - var6.getDamageValue() : Integer.MAX_VALUE,
                     var6.nextDamageWillBreak(),
                     ItemStack.isSameItem(var6, var2)
                  )
               );
            }
         }
      }

      return LungeScheduler.pickBestSlot(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.cooldownMode,
         ((LivingEntityMixin)var1).larpAttackStrengthTicker(),
         var1.getCurrentItemAttackStrengthDelay(),
         this.isBlatant()
      );
   }

   private static float isLegit(LocalPlayer var0, ItemStack var1) {
      AttributeInstance var2 = var0.getAttribute(Attributes.ATTACK_SPEED);
      if (var2 == null) {
         return var0.getCurrentItemAttackStrengthDelay();
      }

      ArrayList var3 = new ArrayList();

      for (AttributeModifier var5 : var2.getModifiers()) {
         if (!var5.id().equals(Item.BASE_ATTACK_SPEED_ID)) {
            var3.add(var5);
         }
      }

      var1.forEachModifier(EquipmentSlot.MAINHAND, (var1x, var2x) -> {
         if (var1x.equals(Attributes.ATTACK_SPEED)) {
            var3.add(var2x);
         }
      });
      double var10 = var2.getBaseValue();

      for (AttributeModifier var9 : var3) {
         if (var9.operation() == Operation.ADD_VALUE) {
            var10 += var9.amount();
         }
      }

      double var6 = var10;

      for (AttributeModifier var13 : var3) {
         if (var13.operation() == Operation.ADD_MULTIPLIED_BASE) {
            var6 += var10 * var13.amount();
         }
      }

      for (AttributeModifier var14 : var3) {
         if (var14.operation() == Operation.ADD_MULTIPLIED_TOTAL) {
            var6 *= 1.0 + var14.amount();
         }
      }

      return (float)(1.0 / ((Attribute)var2.getAttribute().value()).sanitizeValue(var6) * 20.0);
   }

   private void tickSwapping() {
      LungeScheduler.Pick var1 = this.pickSpear();
      if (var1 == null) {
         this.reset();
         this.status = "No Lunge spear in hotbar";
      } else {
         if (this.requiredTicks < 0) {
            this.requiredTicks = var1.ticks();
            LarpLog.info(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.cooldownMode,
               var1.slot() + 1,
               var1.lunge(),
               this.previousSlot + 1,
               this.previousItemName,
               formatCharge(this.previousCharge),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.spearSlot = var1.slot();
         if (this.jabDelayTicks > 0) {
            this.jabDelayTicks--;
            this.status = "Lunge in " + LungeScheduler.formatTicks(Math.max(this.jabDelayTicks + 1, var1.ticks()));
            this.noteHeldBy("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.chargeWaitTicks++;
            this.status = "Lunge in " + LungeScheduler.formatTicks(var1.ticks());
            this.noteHeldBy("charge");
            if (this.chargeWaitTicks > this.requiredTicks + 10 + 2) {
               this.getLungeLevel("the charge never passed");
            }
         } else if (!this.finishViewReturn()) {
            String var2 = this.findBlocker();
            if (var2 != null) {
               this.noteHeldBy(var2);
               this.status = "Waiting for " + var2;
               if (++this.blockedTicks > 10) {
                  this.getLungeLevel(var2);
               }
            } else {
               this.status = "Lunging";
               InteractionArbiter.queueAction(this, this::onKeyPress, () -> this.findBlocker() == null && this.state == LungeSwapModule.State.SWAPPING);
            }
         }
      }
   }

   private boolean finishViewReturn() {
      if (!ViewController.isReturning()) {
         return false;
      }

      ViewController.finishReturn("a Lunge Swap jab");
      this.noteHeldBy("your look");
      this.status = "Lunging";
      return true;
   }

   private String findBlocker() {
      if (!this.isRuntimeActive() || !this.canRun()) {
         return "the game";
      } else if (!HotbarSwapSession.canSwap(this)) {
         return "the hotbar";
      } else if (!CombatUtil.canAttack(minecraft, minecraft.player)) {
         return "free hands";
      } else if (this.isLegit() && CombatUtil.getAttackDelay(minecraft) > 0) {
         return "the miss delay";
      } else if (this.performJab() != null) {
         return "the Lunge conditions";
      } else {
         return this.spearSlot >= 0 && this.hasLunge(this.spearSlot) ? null : "the spear";
      }
   }

   private void noteHeldBy(String var1) {
      if (!var1.equals(this.heldReason)) {
         this.heldReason = var1;
         LarpLog.info("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void getLungeLevel(String var1) {
      LarpLog.info("LungeSwap", "Press lapsed after {} ticks, held by {}", this.chargeWaitTicks + this.blockedTicks, var1);
      this.cancel(true);
      this.status = "Lunge blocked by " + var1;
   }

   private void onKeyPress() {
      LungeScheduler.Pick var1 = this.pickSpear();
      if (this.state == LungeSwapModule.State.SWAPPING && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.findBlocker() == null) {
         LocalPlayer var2 = minecraft.player;
         this.spearSlot = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.chargeTarget = LungeScheduler.ticksFor(this.resolveSwapBackSlot(this.spearSlot), false, this.cooldownMode, 0, 0.0F, this.isBlatant());
            var2.getInventory().setSelectedSlot(this.spearSlot);
            this.state = LungeSwapModule.State.CHARGING;
            this.chargingTicks = 0;
            this.status = "Charging spear " + LungeScheduler.formatTicks(this.chargeTarget);
            LarpLog.info(
               "LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.spearSlot + 1, this.chargeTarget
            );
         } else {
            this.swappedItem = (boolean)(var3 && !ItemStack.isSameItem(var2.getMainHandItem(), var2.getInventory().getItem(this.spearSlot)) ? 1 : 0);
            var2.getInventory().setSelectedSlot(this.spearSlot);
            this.performJab(var3);
         }
      }
   }

   private LungeScheduler.Candidate resolveSwapBackSlot(int var1) {
      LocalPlayer var2 = minecraft.player;
      ItemStack var3 = var2.getInventory().getItem(var1);
      return new LungeScheduler.Candidate(
         var1, isLegit(var2, var3), (Float)var3.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F), this.getLungeLevel(var1), 0, false, false
      );
   }

   private void tickCharging() {
      LocalPlayer var1 = minecraft.player;
      this.chargingTicks++;
      if (var1.getInventory().getSelectedSlot() == this.spearSlot && this.hasLunge(this.spearSlot)) {
         int var2 = this.chargeTarget - this.chargingTicks;
         if (var2 > 0) {
            this.status = "Charging spear " + LungeScheduler.formatTicks(var2);
         } else if (!this.finishViewReturn()) {
            String var3 = this.findBlocker();
            if (var3 != null) {
               this.noteHeldBy(var3);
               this.status = "Waiting for " + var3;
               if (++this.blockedTicks > 10) {
                  LarpLog.info("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  InteractionArbiter.findAttackBlocker(this);
                  this.swapBackToSlotNow();
                  this.status = "Lunge blocked by " + var3;
               }
            } else {
               InteractionArbiter.queueAction(this, () -> {
                  if (this.state == LungeSwapModule.State.CHARGING && this.findBlocker() == null) {
                     this.performJab(true);
                  }
               }, () -> this.state == LungeSwapModule.State.CHARGING && this.findBlocker() == null);
            }
         }
      } else {
         LarpLog.info("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.chargingTicks, var1.getInventory().getSelectedSlot() + 1);
         InteractionArbiter.findAttackBlocker(this);
         this.reset();
         this.status = "Swap interrupted";
      }
   }

   private void performJab(boolean var1) {
      LocalPlayer var2 = minecraft.player;
      ItemStack var3 = var2.getInventory().getItem(this.spearSlot);
      int var4 = var1 && this.state == LungeSwapModule.State.SWAPPING ? 1 : 0;
      if (var3.isItemEnabled(this.level.enabledFeatures())) {
         minecraft.gameMode.piercingAttack(var3.getAttackAnimation(), (PiercingWeapon)var3.get(DataComponents.PIERCING_WEAPON));
         this.status = "Lunged";
         this.resultTicks = 12;
         this.peakSpeed = 0.0;
         this.jabStartPos = var2.position();
         this.swappedInJabTick = (boolean)(var4 != 0 && this.swappedItem ? 1 : 0);
      } else {
         this.status = "Spear not enabled here";
      }

      int var5 = this.chargeWaitTicks + this.blockedTicks + this.chargingTicks;
      this.state = LungeSwapModule.State.LUNGED;
      this.swapBackTicks = this.randomDelay(this.swapBackDelay, this.swapBackDelayMax);
      LarpLog.info(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.status,
         this.mode.getValue(),
         this.cooldownMode,
         this.previousSlot + 1,
         this.previousItemName,
         formatCharge(this.previousCharge),
         this.spearSlot + 1,
         this.getLungeLevel(this.spearSlot),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getFoodData().getFoodLevel(),
         var2.onGround(),
         var2.isSprinting()
      );
      if (this.swapBackTicks > 1) {
         LarpLog.info("LungeSwap", "Swap back in {} ticks", this.swapBackTicks);
      }
   }

   private void swapBackToSlotNow() {
      LocalPlayer var1 = getMc() == null ? null : minecraft.player;
      if (var1 != null && var1 == this.player && var1.level() == this.level) {
         int var2 = resolveSwapBackSlot(
            this.shouldSwapBack(),
            this.swapBackToSlot.getValue(),
            this.swapBackSlot.getValue().intValue() - 1,
            this.previousSlot,
            var1.getInventory().getSelectedSlot(),
            this.spearSlot
         );
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.reset();
   }

   private void cancel(boolean var1) {
      InteractionArbiter.findAttackBlocker(this);
      if (this.state != LungeSwapModule.State.IDLE) {
         LarpLog.info("LungeSwap", "Cancelled at {}; restore={}, status {}", this.state, var1, this.status);
      }

      if (!var1 || this.state != LungeSwapModule.State.LUNGED && this.state != LungeSwapModule.State.CHARGING) {
         this.reset();
      } else {
         this.swapBackToSlotNow();
      }
   }

   private void reset() {
      this.state = LungeSwapModule.State.IDLE;
      this.swappedItem = (boolean)0;
      this.previousSlot = this.spearSlot = -1;
      this.jabDelayTicks = this.swapBackTicks = 0;
      this.chargeWaitTicks = this.blockedTicks = this.chargingTicks = this.chargeTarget = 0;
      this.requiredTicks = -1;
      this.pressQueued = (boolean)0;
      this.heldReason = "";
   }

   private boolean hasLunge(int var1) {
      return this.getLungeLevel(var1) > 0;
   }

   private int getLungeLevel(int var1) {
      if (var1 >= 0 && var1 < 9 && minecraft.player != null && minecraft.level != null) {
         ItemStack var2 = minecraft.player.getInventory().getItem(var1);
         if (!var2.has(DataComponents.PIERCING_WEAPON)) {
            return 0;
         }

         Optional var3 = minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.LUNGE);
         return var3.<Integer>map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var2)).orElse(0);
      } else {
         return 0;
      }
   }

   private static String formatCharge(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum State {
      IDLE,
      SWAPPING,
      CHARGING,
      LUNGED;
   }
}
