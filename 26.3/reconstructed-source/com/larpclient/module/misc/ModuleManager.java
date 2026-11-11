package com.larpclient.module.misc;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.module.combat.AnchorHunter;
import com.larpclient.module.combat.AnchorMacroModule;
import com.larpclient.module.combat.AnchorTapModule;
import com.larpclient.module.combat.AntiKnockbackModule;
import com.larpclient.module.combat.AntiTrapModule;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.AutoAnchorModule;
import com.larpclient.module.combat.AutoCartModule;
import com.larpclient.module.combat.AutoClickerModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.AutoPearlCatchModule;
import com.larpclient.module.combat.AutoTotemModule;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.combat.AutoWebModule;
import com.larpclient.module.combat.BacktrackModule;
import com.larpclient.module.combat.CriticalsModule;
import com.larpclient.module.combat.CrystalOptimizerModule;
import com.larpclient.module.combat.CrystalTapModule;
import com.larpclient.module.combat.HitBoxesModule;
import com.larpclient.module.combat.HoverTotemModule;
import com.larpclient.module.combat.LungeSwapModule;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.module.combat.ReachModule;
import com.larpclient.module.combat.ShieldBreakerModule;
import com.larpclient.module.combat.SpinBotModule;
import com.larpclient.module.combat.TargetAuraModule;
import com.larpclient.module.combat.TotemHitModule;
import com.larpclient.module.combat.TriggerBotModule;
import com.larpclient.module.combat.WTapModule;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.exploit.FakeLagModule;
import com.larpclient.module.exploit.FakePayModule;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.exploit.MaceExploitModule;
import com.larpclient.module.exploit.NoBlockRotationModule;
import com.larpclient.module.movement.AutoElytraPilot;
import com.larpclient.module.movement.AutoElytraSwapModule;
import com.larpclient.module.movement.AutoMLGModule;
import com.larpclient.module.movement.AutoSprintModule;
import com.larpclient.module.movement.BoatFlyModule;
import com.larpclient.module.movement.ElytraFlyModule;
import com.larpclient.module.movement.ElytraHelperModule;
import com.larpclient.module.movement.ElytraMotionModule;
import com.larpclient.module.movement.EntitySpeedModule;
import com.larpclient.module.movement.FlyModule;
import com.larpclient.module.movement.GrimSpeedModule;
import com.larpclient.module.movement.JesusModule;
import com.larpclient.module.movement.NoFallModule;
import com.larpclient.module.movement.NoJumpDelayModule;
import com.larpclient.module.movement.NoSlowdownModule;
import com.larpclient.module.movement.SpeedModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.movement.WalkStyleModule;
import com.larpclient.module.movement.WindChargeHopModule;
import com.larpclient.module.player.AutoArmorModule;
import com.larpclient.module.player.AutoEatModule;
import com.larpclient.module.player.AutoGapModule;
import com.larpclient.module.player.AutoRespawnModule;
import com.larpclient.module.player.AutoToolModule;
import com.larpclient.module.player.ChestStealerModule;
import com.larpclient.module.player.FastUseModule;
import com.larpclient.module.player.InvCleanerModule;
import com.larpclient.module.player.NoBreakDelayModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.player.RodSwapModule;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.render.AmbientParticlesModule;
import com.larpclient.module.render.AnimationsModule;
import com.larpclient.module.render.AstralWeaponTrailsModule;
import com.larpclient.module.render.BlockOverlayModule;
import com.larpclient.module.render.CapesModule;
import com.larpclient.module.render.CelestialWeatherModule;
import com.larpclient.module.render.ChamsModule;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.ClickPulseModule;
import com.larpclient.module.render.CrystalChamsModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.CustomFOVModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.DamageMarkersModule;
import com.larpclient.module.render.ESPModule;
import com.larpclient.module.render.Feature3DUIModule;
import com.larpclient.module.render.FlameHandsModule;
import com.larpclient.module.render.FogModule;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.module.render.FreelookModule;
import com.larpclient.module.render.FullbrightModule;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.module.render.HandChamsModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.module.render.HideArmourModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.HolesESPModule;
import com.larpclient.module.render.ItemESPModule;
import com.larpclient.module.render.ItemPhysicModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.render.LightDebugModule;
import com.larpclient.module.render.LineGlyphsModule;
import com.larpclient.module.render.MenuBackgroundModule;
import com.larpclient.module.render.MotionBlurModule;
import com.larpclient.module.render.NametagsModule;
import com.larpclient.module.render.NoWeatherModule;
import com.larpclient.module.render.PearlParticlesModule;
import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.module.render.PlayerTrailModule;
import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.module.render.RemovalsModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.module.render.SmoothCameraModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.StorageESPModule;
import com.larpclient.module.render.StretchedResModule;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.TargetHUDModule;
import com.larpclient.module.render.TimeChangerModule;
import com.larpclient.module.render.TracersModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.module.render.WavingFoliageModule;
import com.larpclient.module.render.WaypointsModule;
import com.larpclient.module.render.WetGroundModule;
import com.larpclient.module.render.WingsModule;
import com.larpclient.module.render.WorldColorModule;
import com.larpclient.module.render.XRayModule;
import com.larpclient.module.render.ZoomModule;
import com.larpclient.module.world.AirPlaceModule;
import com.larpclient.module.world.AutoAreaMiner;
import com.larpclient.module.world.AutoBuilderModule;
import com.larpclient.module.world.AutoDrainModule;
import com.larpclient.module.world.AutoFarm;
import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.module.world.BaseFinder;
import com.larpclient.module.world.MotionScannerModule;
import com.larpclient.module.world.SpawnerFinder;
import com.larpclient.module.world.SusChunkFinder;
import com.larpclient.util.data.ChangeTracker;
import com.larpclient.util.inventory.AutoEatHelper;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class ModuleManager {
   private final List<Module> modules = new ArrayList<>();
   private static final Set<Class<? extends Module>> packetTickExempt = Set.of(
      AutoTotemModule.class,
      HoverTotemModule.class,
      AutoSellModule.class,
      ElytraMotionModule.class,
      AutoArmorModule.class,
      ChestStealerModule.class,
      InvCleanerModule.class,
      AutoElytraSwapModule.class,
      OrderDropperModule.class
   );
   private final ChangeTracker stateWatcher = new ChangeTracker();
   private List<Module> legitModules;

   public void registerDefaults() {
      this.registerModules(true);
   }

   public void registerModules(boolean var1) {
      this.register(new AuraModule());
      this.register(new AntiKnockbackModule());
      this.register(new BacktrackModule());
      this.register(new CriticalsModule());
      this.register(new AutoTotemModule());
      this.register(new HoverTotemModule());
      this.register(new MaceModule());
      this.register(new LungeSwapModule());
      this.register(new TotemHitModule());
      this.register(new ShieldBreakerModule());
      this.register(new RodSwapModule());
      this.register(new ReachModule());
      this.register(new HitBoxesModule());
      this.register(new TriggerBotModule());
      this.register(new AutoClickerModule());
      this.register(new AutoCrystalModule());
      this.register(new CrystalOptimizerModule());
      this.register(new AnchorTapModule());
      this.register(new AnchorMacroModule());
      this.register(new AutoAnchorModule());
      this.register(new AutoCartModule());
      this.register(new AntiTrapModule());
      this.register(new AutoWebModule());
      this.register(new AutoTrap());
      this.register(new IgnoreEntityModule());
      this.register(new AimAssistModule());
      this.register(new WTapModule());
      this.register(new FlyModule());
      this.register(new SpeedModule());
      this.register(new GrimSpeedModule());
      this.register(new TargetStrafeModule());
      this.register(new ScaffoldModule());
      this.register(new JesusModule());
      this.register(new NoFallModule());
      this.register(new NoSlowdownModule());
      this.register(new NoJumpDelayModule());
      this.register(new AutoSprintModule());
      this.register(new ElytraFlyModule());
      this.register(new EntitySpeedModule());
      this.register(new BoatFlyModule());
      this.register(new AirStuckModule());
      this.register(new ElytraMotionModule());
      this.register(new ElytraHelperModule());
      this.register(new AutoPearlCatchModule());
      this.register(new AutoElytraSwapModule());
      this.register(new WindChargeHopModule());
      this.register(new AutoMLGModule());
      this.register(new FullbrightModule());
      this.register(new ZoomModule());
      this.register(new ESPModule());
      this.register(new XRayModule());
      this.register(new NoBlockRotationModule());
      this.register(new RealisticWaterModule());
      this.register(new WavingFoliageModule());
      this.register(new WetGroundModule());
      this.register(new SnowGroundModule());
      this.register(new TracersModule());
      this.register(new NametagsModule());
      this.register(new NoWeatherModule());
      this.register(new ChamsModule());
      this.register(new HandChamsModule());
      this.register(new CrystalChamsModule());
      this.register(new WalkStyleModule());
      this.register(new EmotesModule());
      this.register(new PetsModule());
      this.register(new CryptoPetsModule());
      this.register(new ItemESPModule());
      this.register(new TimeChangerModule());
      this.register(new AnimationsModule());
      this.register(new ViewModelModule());
      this.register(new RemovalsModule());
      this.register(new GUIAnimationModule());
      this.register(new Feature3DUIModule());
      this.register(new WorldMenuModule());
      this.register(new LineGlyphsModule());
      this.register(new PearlParticlesModule());
      this.register(new SkyShaderModule());
      this.register(new SwordModelsModule());
      this.register(new StorageESPModule());
      this.register(new PlayerTrailModule());
      this.register(new HitFXModule());
      this.register(new KillFXModule());
      this.register(new ItemPhysicModule());
      this.register(new DamageMarkersModule());
      this.register(new WorldLyricsModule());
      this.register(new IslandLyricsModule());
      this.register(new EchoHandModule());
      this.register(new FlameHandsModule());
      this.register(new HitSoundsModule());
      this.register(new PlayerModelModule());
      this.register(new SkinChangerModule());
      this.register(new MenuBackgroundModule());
      this.register(new CapesModule());
      this.register(new JumpFXModule());
      this.register(new BlockOverlayModule());
      this.register(new TargetAuraModule());
      this.register(new SelfVisualsModule());
      this.register(new WorldColorModule());
      this.register(new MotionBlurModule());
      this.register(new AmbientParticlesModule());
      this.register(new AstralWeaponTrailsModule());
      this.register(new CelestialWeatherModule());
      this.register(new FogModule());
      this.register(new TargetHUDModule());
      this.register(new StaffListModule());
      this.register(new CustomCrosshairModule());
      this.register(new ClickPulseModule());
      this.register(new FreelookModule());
      this.register(new HUDModule());
      this.register(new CustomSkyModule());
      this.register(new StretchedResModule());
      this.register(new ChinaHatModule());
      this.register(new WingsModule());
      this.register(new HeadlessKorbloxModule());
      this.register(new HideArmourModule());
      this.register(new LARPModule());
      this.register(new NameChangerModule());
      if (var1) {
         this.getModule(HUDModule.class).setEnabled(true);
      }

      this.register(new PerformanceModule());
      this.register(new AutoToolModule());
      this.register(new FreecamModule());
      this.register(new SmoothCameraModule());
      this.register(new FakeLagModule());
      this.register(new AutoArmorModule());
      this.register(new AutoEatModule());
      this.register(new ChestStealerModule());
      this.register(new AutoRespawnModule());
      this.register(new AutoDisconnectModule());
      this.register(new InvCleanerModule());
      this.register(new MCFriendModule());
      this.register(new AutoGapModule());
      this.register(new NoBreakDelayModule());
      this.register(new FastUseModule());
      this.register(new AutoDrainModule());
      this.register(new StreamProofModule());
      this.register(new AutoGGModule());
      this.register(new WaypointsModule());
      this.register(new MaceExploitModule());
      this.register(new CustomFOVModule());
      this.register(new FakePlayerModule());
      this.register(new AutoReconnectModule());
      this.register(new FakeScoreboardModule());
      this.register(new FakeFPSModule());
      this.register(new CrystalTapModule());
      this.register(new AutoAreaMiner());
      this.register(new AutoElytraPilot());
      this.register(new AnchorHunter());
      this.register(new AutoBuilderModule());
      this.register(new SusChunkFinder());
      this.register(new SpawnerFinder());
      this.register(new BaseFinder());
      this.register(new AutoSellModule());
      this.register(new HomeMacroModule());
      this.register(new OrderDropperModule());
      this.register(new LightDebugModule());
      this.register(new MotionScannerModule());
      this.register(new HolesESPModule());
      this.register(new AirPlaceModule());
      this.register(new AutoFarm());
      this.register(new AutoTunnelModule());
      this.register(new FakePayModule());
      this.register(new FakeSpawnerModule());
      this.register(new SpinBotModule());
      this.register(new CasinoModules.PaperModule());
      this.register(new CasinoModules.FortyFiveModule());
      this.register(new CasinoModules.FiftyFiftyModule());
      this.register(new CasinoModules.BlackjackModule());
      this.register(new CasinoModules.BlackjackForceOnlyModule());
      this.register(new CasinoModules.EvenOrOddModule());
      this.register(new CasinoModules.HigherOrLowerModule());
      this.register(new CasinoModules.BaccaratModule());
      this.register(new CasinoModules.MexicanJackModule());
      this.register(new CasinoModules.RussianRouletteModule());
      this.register(new CasinoModules.ArrowGameModule());
      this.register(new CasinoModules.HorseRacingModule());
      this.register(new CasinoModules.ArmorGameModule());
      this.register(new CasinoModules.FishingGameModule());
      if (var1) {
         this.getModule(FakeSpawnerModule.class).loadPreferences();
      }
   }

   private void register(Module var1) {
      if (ModuleCategory.categorize(var1) == ModuleCategory.cOMBAT) {
         var1.offerHoldKeybind();
      }

      this.modules.add(var1);
   }

   public void suspendOnStateChange() {
      Minecraft var1 = Minecraft.getInstance();
      int var10000 = Integer.compare(31, 31);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         for (Module var3 : this.modules) {
            var3.suspendForDisconnect();
         }

         AutoEatHelper.tick();
         ViewController.reset();
         RotationManager.clear();
         InteractionArbiter.reset();
         com.larpclient.util.entity.CombatUtil.canAttack();
      }

      if (this.stateWatcher.hasChanged(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
         for (Module var5 : this.modules) {
            var5.suspendForDisconnect();
         }

         FakeLagModule.discardHeld();
         AutoEatHelper.tick();
         ViewController.reset();
         RotationManager.clear();
         InteractionArbiter.reset();
         com.larpclient.util.entity.CombatUtil.canAttack();
      }
   }

   public void tick() {
      this.suspendOnStateChange();
      RotationManager.clear();
      InteractionArbiter.clearQueues();
      ViewController.tickReturn();

      for (Module var2 : this.modules) {
         var2.activatePending();
      }

      CasinoRigManager.tickIfBusy();

      for (Module.TickPhase var4 : Module.TickPhase.values()) {
         for (Module var6 : this.modules) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!packetTickExempt.contains(var6.getClass()) || !CasinoRigManager.isBusy())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  LarpLog.error("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.TickPhase.MAIN) {
            ViewController.checkReturnInterrupted();
            RotationManager.applyToPlayer();
         }
      }
   }

   public List<Module> getModules() {
      return this.modules;
   }

   public List<Module> getVisibleModules() {
      if (StreamProofState.isHidden()) {
         if (this.legitModules == null) {
            this.legitModules = this.modules.stream().filter(StreamProofState::isExempt).toList();
         }

         return this.legitModules;
      } else {
         return this.modules;
      }
   }

   public void setLegitMode(boolean var1) {
      if (var1 != StreamProofState.isHidden()) {
         StreamProofState.setActive(var1);
         if (!var1) {
            LarpLog.info("Legit mode", "Off; enabled modules resume on the next tick");
         } else {
            ArrayList var2 = new ArrayList();

            for (Module var4 : this.modules) {
               if (StreamProofState.isHidden(var4) && var4.isEnabled()) {
                  var4.suspendForDisconnect();
                  var2.add(var4.getName());
               }
            }

            AutoEatHelper.tick();
            ViewController.reset();
            RotationManager.clear();
            InteractionArbiter.reset();
            com.larpclient.util.entity.CombatUtil.canAttack();
            LarpLog.info("Legit mode", "On; suspended {} and kept their preferences", var2);
         }
      }
   }

   public List<Module> getByCategory(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.modules) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> getEnabledModules() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.modules) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module getByName(String var1) {
      for (Module var3 : this.modules) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T getModule(Class<T> var1) {
      for (Module var3 : this.modules) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
