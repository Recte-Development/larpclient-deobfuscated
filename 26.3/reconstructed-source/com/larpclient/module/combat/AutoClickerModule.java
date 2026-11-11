package com.larpclient.module.combat;

import com.google.gson.JsonObject;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.ClickScheduler;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoClickerModule extends Module {
   private static AutoClickerModule instance;
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic")
   );
   private final BooleanSetting leftClick = this.addSetting(new BooleanSetting("Left Click", "Repeat attacks", true));
   private final BooleanSetting rightClick = this.addSetting(new BooleanSetting("Right Click", "Repeat item use and placement", false));
   private final NumberSetting leftCps = this.addSetting(new NumberSetting("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final NumberSetting rightCps = this.addSetting(new NumberSetting("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final BooleanSetting randomizeCps = this.addSetting(
      new BooleanSetting("Randomize CPS", "Choose a rate within each button's range after a click", false)
   );
   private final NumberSetting minLeftCps = this.addSetting(new NumberSetting("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final NumberSetting minRightCps = this.addSetting(new NumberSetting("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final BooleanSetting onlyWeapon = this.addSetting(new BooleanSetting("Only Weapon", "Only click while holding a weapon", false));
   private final BooleanSetting waitCooldown = this.addSetting(new BooleanSetting("Wait Cooldown", "Wait for a fully charged attack", false));
   private final BooleanSetting onlyEntities = this.addSetting(
      new BooleanSetting("Only Entities", "Left clicks require an entity under your actual aim", false)
   );
   private final BooleanSetting breakBlocks = this.addSetting(new BooleanSetting("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final ClickScheduler leftScheduler = new ClickScheduler();
   private final ClickScheduler rightScheduler = new ClickScheduler();
   private boolean overridingHit;
   private boolean lastClickWasLeft;
   private String status = "Waiting for input";
   private long lastLogTime;
   private int leftClicks;
   private int rightClicks;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      instance = this;
      this.leftCps.visibleWhen(this.leftClick::getValue);
      this.rightCps.visibleWhen(this.rightClick::getValue);
      this.minLeftCps.visibleWhen(() -> this.leftClick.getValue() && this.randomizeCps.getValue());
      this.minRightCps.visibleWhen(() -> this.rightClick.getValue() && this.randomizeCps.getValue());
      this.waitCooldown.visibleWhen(this.leftClick::getValue);
      this.onlyEntities.visibleWhen(this.leftClick::getValue);
      this.breakBlocks.visibleWhen(() -> this.leftClick.getValue() && !this.onlyEntities.getValue());

      for (Setting var2 : this.getSettings()) {
         var2.onChange(var1 -> this.resetTimers());
      }
   }

   public static AutoClickerModule getInstance() {
      return instance;
   }

   public static void isInputHeld(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   public List<Setting<?>> getActivationSettings() {
      return List.of(this.activation, this.leftClick, this.rightClick, this.onlyWeapon);
   }

   public List<Setting<?>> isLeftClickActive() {
      return List.of(this.leftCps, this.rightCps, this.randomizeCps, this.minLeftCps, this.minRightCps, this.waitCooldown);
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.onlyEntities, this.breakBlocks);
   }

   private boolean canRun() {
      return this.isRuntimeActive()
         && this.isInGame()
         && minecraft.gameMode != null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && minecraft.isWindowActive()
         && minecraft.gui.screen() == null
         && minecraft.gui.overlay() == null
         && (!this.onlyWeapon.getValue() || ReachModule.isHoldingWeapon());
   }

   public boolean isLeftClickActive() {
      return !this.isEnabled() ? false : this.canRun() && this.leftClick.getValue() && !minecraft.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON);
   }

   public boolean isRightClickActive() {
      return !this.isEnabled() ? false : this.canRun() && this.rightClick.getValue();
   }

   public boolean isOverridingHit() {
      return this.overridingHit;
   }

   private boolean isInputHeld(boolean var1) {
      return this.activation.isMode("Automatic") || (var1 ? minecraft.options.keyUse : minecraft.options.keyAttack).isDown();
   }

   private boolean isOtherControllerActive() {
      return PlayerInputUtil.isModuleActive("Aura") || ViewController.update() != ViewController.Owner.NONE;
   }

   private boolean shouldClick(boolean var1) {
      return (var1 ? this.isRightClickActive() : this.isLeftClickActive())
         && this.isInputHeld(var1)
         && InteractionArbiter.canInteract()
         && !this.isOtherControllerActive()
         && !minecraft.player.isUsingItem()
         && !minecraft.player.isHandsBusy()
         && (var1 || !minecraft.options.keyUse.isDown());
   }

   public boolean isAutoUsingItem() {
      return !this.isEnabled()
         ? false
         : this.isRightClickActive()
            && this.activation.isMode("Automatic")
            && !this.isOtherControllerActive()
            && InteractionArbiter.canInteract()
            && minecraft.player.isUsingItem();
   }

   @Override
   public void onTick() {
      if (this.canRun() && InteractionArbiter.canInteract() && !this.isOtherControllerActive()) {
         long var1 = System.nanoTime();
         this.status = minecraft.player.isUsingItem() ? "Using held item" : "Waiting for input";
         if (!this.shouldClick(false)) {
            this.leftScheduler.reset();
         }

         if (!this.shouldClick(true)) {
            this.rightScheduler.reset();
         }

         int var3 = this.shouldClick(true) && this.rightScheduler.isReady(var1) ? 1 : 0;
         boolean var9 = false;
         int var8 = var3;
         int var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            boolean var11 = false;
            byte var10 = this.lastClickWasLeft;
            var10000 = Integer.signum(var10);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.queueRightClick();
               return;
            }
         }

         byte var4 = 0;
         if (this.shouldClick(false) && this.leftScheduler.isReady(var1)) {
            HitResult var5 = this.shouldClick();
            int var17 = var5 instanceof EntityHitResult;
            boolean var15 = false;
            byte var14 = (byte)var17;
            var17 = Integer.signum(var14);
            if (((var17 | -var17) >>> 31 ^ 1) == 0) {
               EntityHitResult var6 = (EntityHitResult)var5;
               if (this.waitCooldown.getValue() && !InteractionArbiter.processQueue()) {
                  this.status = "Waiting for attack cooldown";
               } else {
                  Entity var7 = var6.getEntity();
                  InteractionArbiter.queueAttackWithCallbacks(
                     this,
                     var7,
                     minecraft.player.entityInteractionRange(),
                     this.waitCooldown.getValue(),
                     () -> this.shouldClick(false) && this.shouldClick() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                     1,
                     () -> this.recordClick(false),
                     var1x -> this.status = "Waiting: " + var1x
                  );
                  var4 = 1;
               }
            } else if (var5.getType() == Type.MISS && !this.onlyEntities.getValue()) {
               InteractionArbiter.queueAction(
                  this,
                  () -> this.runWithAimedHit(() -> {
                     minecraft.startAttack();
                     this.recordClick(false);
                  }),
                  () -> this.shouldClick(false)
                     && !this.onlyEntities.getValue()
                     && this.shouldClick().getType() == Type.MISS
                     && (!this.waitCooldown.getValue() || InteractionArbiter.processQueue())
               );
               var4 = 1;
            }
         }

         if (var3 != 0) {
            boolean var13 = false;
            byte var12 = var4;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.queueRightClick();
            }
         }
      } else {
         this.resetTimers();
         this.status = "Paused: camera or input unavailable";
      }
   }

   private void queueRightClick() {
      InteractionArbiter.queueAction(this, () -> this.runWithAimedHit(() -> {
         minecraft.startUseItem();
         this.recordClick(true);
      }), () -> this.shouldClick(true));
   }

   public void mineWithAimedHit(Consumer<Boolean> var1, boolean var2) {
      if (!this.isLeftClickActive()) {
         var1.accept(var2);
      } else {
         this.runWithAimedHit(
            () -> {
               int var2x = this.shouldClick(false)
                     && this.breakBlocks.getValue()
                     && !this.onlyEntities.getValue()
                     && minecraft.hitResult.getType() == Type.BLOCK
                  ? 1
                  : 0;
               var1.accept(Boolean.valueOf((boolean)var2x));
               if (var2x != 0) {
                  this.status = "Mining";
               }
            }
         );
      }
   }

   private void runWithAimedHit(Runnable var1) {
      HitResult var2 = minecraft.hitResult;
      boolean var3 = this.overridingHit;
      minecraft.hitResult = this.shouldClick();
      this.overridingHit = (boolean)1;

      try {
         var1.run();
      } finally {
         minecraft.hitResult = var2;
         this.overridingHit = var3;
      }
   }

   private HitResult shouldClick() {
      return minecraft.player.raycastHitResult(1.0F, minecraft.player);
   }

   private void recordClick(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.rightCps : this.leftCps).getValue();
      double var6 = this.randomizeCps.getValue() ? (var1 ? this.minRightCps : this.minLeftCps).getValue() : var4;
      (var1 ? this.rightScheduler : this.leftScheduler).schedule(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.rightClicks++;
      } else {
         this.leftClicks++;
      }

      this.lastClickWasLeft = (boolean)(!var1 ? 1 : 0);
      this.status = var1 ? "Using" : "Clicking";
      if (var2 - this.lastLogTime >= 1000000000L) {
         this.lastLogTime = var2;
         LarpLog.debug("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.leftClicks, this.rightClicks, this.activation.getValue());
      }
   }

   private void resetTimers() {
      this.leftScheduler.reset();
      this.rightScheduler.reset();
      InteractionArbiter.findAttackBlocker(this);
   }

   @Override
   public void onEnable() {
      this.resetTimers();
      this.leftClicks = this.rightClicks = 0;
      this.lastLogTime = 0L;
      this.lastClickWasLeft = (boolean)0;
      this.status = "Waiting for input";
      LarpLog.debug(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.activation.getValue(),
         this.leftCps.getValue(),
         this.rightCps.getValue(),
         this.waitCooldown.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.resetTimers();
      this.status = "Waiting for input";
      LarpLog.debug("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.status;
   }
}
