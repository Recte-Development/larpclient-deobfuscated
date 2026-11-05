package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.CrystalChamsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.ex;
import com.larpclient.j2;
import com.larpclient.j5;
import com.larpclient.lw;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.z;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class ly {
   private final List<Module> W = new ArrayList<>();
   private static final Set<Class<? extends Module>> c = Set.of(
      AutoTotemModule.class,
      HoverTotemModule.class,
      AutoSellModule.class,
      ElytraMotionModule.class,
      AutoArmorModule.class,
      ChestStealerModule.class,
      InvCleanerModule.class,
      OrderDropperModule.class
   );
   private final j2 B = new j2();

   public void P() {
      this.B(true);
   }

   public void B(boolean var1) {
      this.Y(new AuraModule());
      this.Y(new AntiKnockbackModule());
      this.Y(new BacktrackModule());
      this.Y(new CriticalsModule());
      this.Y(new AutoTotemModule());
      this.Y(new HoverTotemModule());
      this.Y(new MaceModule());
      this.Y(new LungeSwapModule());
      this.Y(new TotemHitModule());
      this.Y(new ShieldBreakerModule());
      this.Y(new RodSwapModule());
      this.Y(new ReachModule());
      this.Y(new TriggerBotModule());
      this.Y(new AutoClickerModule());
      this.Y(new AutoCrystalModule());
      this.Y(new CrystalOptimizerModule());
      this.Y(new AnchorTapModule());
      this.Y(new AutoAnchorModule());
      this.Y(new AutoCartModule());
      this.Y(new AntiTrapModule());
      this.Y(new k());
      this.Y(new t());
      this.Y(new IgnoreEntityModule());
      this.Y(new AimAssistModule());
      this.Y(new WTapModule());
      this.Y(new FlyModule());
      this.Y(new SpeedModule());
      this.Y(new GrimSpeedModule());
      this.Y(new TargetStrafeModule());
      this.Y(new ScaffoldModule());
      this.Y(new JesusModule());
      this.Y(new NoFallModule());
      this.Y(new NoSlowdownModule());
      this.Y(new NoJumpDelayModule());
      this.Y(new AutoSprintModule());
      this.Y(new ElytraFlyModule());
      this.Y(new EntitySpeedModule());
      this.Y(new BoatFlyModule());
      this.Y(new AirStuckModule());
      this.Y(new ElytraMotionModule());
      this.Y(new ElytraHelperModule());
      this.Y(new AutoPearlCatchModule());
      this.Y(new WindChargeHopModule());
      this.Y(new AutoMLGModule());
      this.Y(new FullbrightModule());
      this.Y(new ZoomModule());
      this.Y(new ESPModule());
      this.Y(new XRayModule());
      this.Y(new NoBlockRotationModule());
      this.Y(new RealisticWaterModule());
      this.Y(new WavingFoliageModule());
      this.Y(new WetGroundModule());
      this.Y(new SnowGroundModule());
      this.Y(new TracersModule());
      this.Y(new NametagsModule());
      this.Y(new NoWeatherModule());
      this.Y(new ChamsModule());
      this.Y(new HandChamsModule());
      this.Y(new CrystalChamsModule());
      this.Y(new WalkStyleModule());
      this.Y(new EmotesModule());
      this.Y(new PetsModule());
      this.Y(new CryptoPetsModule());
      this.Y(new ItemESPModule());
      this.Y(new TimeChangerModule());
      this.Y(new AnimationsModule());
      this.Y(new ViewModelModule());
      this.Y(new RemovalsModule());
      this.Y(new GUIAnimationModule());
      this.Y(new Feature3DUIModule());
      this.Y(new WorldMenuModule());
      this.Y(new LineGlyphsModule());
      this.Y(new PearlParticlesModule());
      this.Y(new SkyShaderModule());
      this.Y(new SwordModelsModule());
      this.Y(new StorageESPModule());
      this.Y(new PlayerTrailModule());
      this.Y(new HitFXModule());
      this.Y(new KillFXModule());
      this.Y(new HalloweenModule());
      this.Y(new ItemPhysicModule());
      this.Y(new DamageMarkersModule());
      this.Y(new WorldLyricsModule());
      this.Y(new IslandLyricsModule());
      this.Y(new EchoHandModule());
      this.Y(new FlameHandsModule());
      this.Y(new HitSoundsModule());
      this.Y(new PlayerModelModule());
      this.Y(new SkinChangerModule());
      this.Y(new MenuBackgroundModule());
      this.Y(new CapesModule());
      this.Y(new JumpFXModule());
      this.Y(new BlockOverlayModule());
      this.Y(new TargetAuraModule());
      this.Y(new SelfVisualsModule());
      this.Y(new WorldColorModule());
      this.Y(new MotionBlurModule());
      this.Y(new AmbientParticlesModule());
      this.Y(new AstralWeaponTrailsModule());
      this.Y(new CelestialWeatherModule());
      this.Y(new FogModule());
      this.Y(new TargetHUDModule());
      this.Y(new StaffListModule());
      this.Y(new CustomCrosshairModule());
      this.Y(new ClickPulseModule());
      this.Y(new FreelookModule());
      this.Y(new HUDModule());
      this.Y(new CustomSkyModule());
      this.Y(new StretchedResModule());
      this.Y(new ChinaHatModule());
      this.Y(new WingsModule());
      this.Y(new HeadlessKorbloxModule());
      this.Y(new LARPModule());
      this.Y(new NameChangerModule());
      if (var1) {
         this.y(HUDModule.class).setEnabled(true);
      }

      this.Y(new PerformanceModule());
      this.Y(new FreecamModule());
      this.Y(new SmoothCameraModule());
      this.Y(new FakeLagModule());
      this.Y(new AutoArmorModule());
      this.Y(new AutoEatModule());
      this.Y(new ChestStealerModule());
      this.Y(new AutoRespawnModule());
      this.Y(new AutoDisconnectModule());
      this.Y(new InvCleanerModule());
      this.Y(new MCFriendModule());
      this.Y(new AutoGapModule());
      this.Y(new NoBreakDelayModule());
      this.Y(new FastUseModule());
      this.Y(new AutoDrainModule());
      this.Y(new StreamProofModule());
      this.Y(new tg());
      this.Y(new td());
      this.Y(new to());
      this.Y(new AutoSellModule());
      this.Y(new HomeMacroModule());
      this.Y(new OrderDropperModule());
      this.Y(new LightDebugModule());
      this.Y(new MotionScannerModule());
      this.Y(new HolesESPModule());
      this.Y(new z());
      this.Y(new com.larpclient.t());
      this.Y(new com.larpclient.k());
      this.Y(new FakePayModule());
      this.Y(new FakeSpawnerModule());
      this.Y(new SpinBotModule());
      this.Y(new lo.q());
      this.Y(new lo.m());
      this.Y(new lo.p());
      this.Y(new lo.h());
      this.Y(new lo.t());
      this.Y(new lo.f());
      this.Y(new lo.g());
      this.Y(new lo.n());
      this.Y(new lo.v());
      this.Y(new lo.x());
      this.Y(new lo.l());
      this.Y(new lo.s());
      this.Y(new lo.u());
      this.Y(new lo.w());
      if (var1) {
         this.y(FakeSpawnerModule.class).A();
      }
   }

   private void Y(Module var1) {
      if (lw.k(var1) == lw.i) {
         var1.offerHoldKeybind();
      }

      this.W.add(var1);
   }

   public void Z() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.B.O(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
         for (Module var3 : this.W) {
            var3.suspendForDisconnect();
         }

         FakeLagModule.b();
         ex.F();
         rr.g();
         ep.U();
         d8.h();
         j5.P();
      }
   }

   public void I() {
      this.Z();
      ep.U();
      d8.U();
      rr.X();

      for (Module var2 : this.W) {
         var2.activatePending();
      }

      com.larpclient.l6.G();

      for (Module.b var4 : Module.b.values()) {
         for (Module var6 : this.W) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!c.contains(var6.getClass()) || !com.larpclient.l6.n())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  r4.r("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.b.w) {
            rr.t();
            ep.u();
         }
      }
   }

   public List<Module> X() {
      return this.W;
   }

   public List<Module> c(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.W) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> y() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.W) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module O(String var1) {
      for (Module var3 : this.W) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T y(Class<T> var1) {
      for (Module var3 : this.W) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
