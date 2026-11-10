package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.CrystalChamsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WaypointsModule;
import com.larpclient.WetGroundModule;
import com.larpclient._3;
import com.larpclient._6;
import com.larpclient._7;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient._n;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient._z;
import com.larpclient.a6;
import com.larpclient.b6;
import com.larpclient.f7;
import com.larpclient.i_;
import com.larpclient.ij;
import com.larpclient.q_;
import com.larpclient.qg;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class kl {
   private final List<Module> A = new ArrayList<>();
   private static final Set<Class<? extends Module>> q = Set.of(
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
   private final i_ Z = new i_();
   private List<Module> x;

   public void g() {
      this.G(true);
   }

   public void G(boolean var1) {
      this.K(new AuraModule());
      this.K(new AntiKnockbackModule());
      this.K(new BacktrackModule());
      this.K(new CriticalsModule());
      this.K(new AutoTotemModule());
      this.K(new HoverTotemModule());
      this.K(new MaceModule());
      this.K(new LungeSwapModule());
      this.K(new TotemHitModule());
      this.K(new ShieldBreakerModule());
      this.K(new RodSwapModule());
      this.K(new ReachModule());
      this.K(new HitBoxesModule());
      this.K(new TriggerBotModule());
      this.K(new AutoClickerModule());
      this.K(new AutoCrystalModule());
      this.K(new CrystalOptimizerModule());
      this.K(new AnchorTapModule());
      this.K(new AnchorMacroModule());
      this.K(new AutoAnchorModule());
      this.K(new AutoCartModule());
      this.K(new AntiTrapModule());
      this.K(new kb());
      this.K(new kv());
      this.K(new IgnoreEntityModule());
      this.K(new AimAssistModule());
      this.K(new WTapModule());
      this.K(new FlyModule());
      this.K(new SpeedModule());
      this.K(new GrimSpeedModule());
      this.K(new TargetStrafeModule());
      this.K(new ScaffoldModule());
      this.K(new JesusModule());
      this.K(new NoFallModule());
      this.K(new NoSlowdownModule());
      this.K(new NoJumpDelayModule());
      this.K(new AutoSprintModule());
      this.K(new ElytraFlyModule());
      this.K(new EntitySpeedModule());
      this.K(new BoatFlyModule());
      this.K(new AirStuckModule());
      this.K(new ElytraMotionModule());
      this.K(new ElytraHelperModule());
      this.K(new AutoPearlCatchModule());
      this.K(new AutoElytraSwapModule());
      this.K(new WindChargeHopModule());
      this.K(new AutoMLGModule());
      this.K(new FullbrightModule());
      this.K(new ZoomModule());
      this.K(new ESPModule());
      this.K(new XRayModule());
      this.K(new NoBlockRotationModule());
      this.K(new RealisticWaterModule());
      this.K(new WavingFoliageModule());
      this.K(new WetGroundModule());
      this.K(new SnowGroundModule());
      this.K(new TracersModule());
      this.K(new NametagsModule());
      this.K(new NoWeatherModule());
      this.K(new ChamsModule());
      this.K(new HandChamsModule());
      this.K(new CrystalChamsModule());
      this.K(new WalkStyleModule());
      this.K(new EmotesModule());
      this.K(new PetsModule());
      this.K(new CryptoPetsModule());
      this.K(new ItemESPModule());
      this.K(new TimeChangerModule());
      this.K(new AnimationsModule());
      this.K(new ViewModelModule());
      this.K(new RemovalsModule());
      this.K(new GUIAnimationModule());
      this.K(new Feature3DUIModule());
      this.K(new WorldMenuModule());
      this.K(new LineGlyphsModule());
      this.K(new PearlParticlesModule());
      this.K(new SkyShaderModule());
      this.K(new SwordModelsModule());
      this.K(new StorageESPModule());
      this.K(new PlayerTrailModule());
      this.K(new HitFXModule());
      this.K(new KillFXModule());
      this.K(new ItemPhysicModule());
      this.K(new DamageMarkersModule());
      this.K(new WorldLyricsModule());
      this.K(new IslandLyricsModule());
      this.K(new EchoHandModule());
      this.K(new FlameHandsModule());
      this.K(new HitSoundsModule());
      this.K(new PlayerModelModule());
      this.K(new SkinChangerModule());
      this.K(new MenuBackgroundModule());
      this.K(new CapesModule());
      this.K(new JumpFXModule());
      this.K(new BlockOverlayModule());
      this.K(new TargetAuraModule());
      this.K(new SelfVisualsModule());
      this.K(new WorldColorModule());
      this.K(new MotionBlurModule());
      this.K(new AmbientParticlesModule());
      this.K(new AstralWeaponTrailsModule());
      this.K(new CelestialWeatherModule());
      this.K(new FogModule());
      this.K(new TargetHUDModule());
      this.K(new StaffListModule());
      this.K(new CustomCrosshairModule());
      this.K(new ClickPulseModule());
      this.K(new FreelookModule());
      this.K(new HUDModule());
      this.K(new CustomSkyModule());
      this.K(new StretchedResModule());
      this.K(new ChinaHatModule());
      this.K(new WingsModule());
      this.K(new HeadlessKorbloxModule());
      this.K(new HideArmourModule());
      this.K(new LARPModule());
      this.K(new NameChangerModule());
      if (var1) {
         this.y(HUDModule.class).setEnabled(true);
      }

      this.K(new PerformanceModule());
      this.K(new AutoToolModule());
      this.K(new FreecamModule());
      this.K(new SmoothCameraModule());
      this.K(new FakeLagModule());
      this.K(new AutoArmorModule());
      this.K(new AutoEatModule());
      this.K(new ChestStealerModule());
      this.K(new AutoRespawnModule());
      this.K(new AutoDisconnectModule());
      this.K(new InvCleanerModule());
      this.K(new MCFriendModule());
      this.K(new AutoGapModule());
      this.K(new NoBreakDelayModule());
      this.K(new FastUseModule());
      this.K(new AutoDrainModule());
      this.K(new StreamProofModule());
      this.K(new AutoGGModule());
      this.K(new WaypointsModule());
      this.K(new MaceExploitModule());
      this.K(new CustomFOVModule());
      this.K(new FakePlayerModule());
      this.K(new AutoReconnectModule());
      this.K(new FakeScoreboardModule());
      this.K(new FakeFPSModule());
      this.K(new CrystalTapModule());
      this.K(new _6());
      this.K(new _7());
      this.K(new _z());
      this.K(new _d());
      this.K(new wk());
      this.K(new wx());
      this.K(new kq());
      this.K(new AutoSellModule());
      this.K(new HomeMacroModule());
      this.K(new OrderDropperModule());
      this.K(new LightDebugModule());
      this.K(new MotionScannerModule());
      this.K(new HolesESPModule());
      this.K(new _m());
      this.K(new _3());
      this.K(new _n());
      this.K(new FakePayModule());
      this.K(new FakeSpawnerModule());
      this.K(new SpinBotModule());
      this.K(new t.x());
      this.K(new t.n());
      this.K(new t.k());
      this.K(new t.a());
      this.K(new t.m());
      this.K(new t.v());
      this.K(new t.s());
      this.K(new t.b());
      this.K(new t.p());
      this.K(new t.h());
      this.K(new t.u());
      this.K(new t.i());
      this.K(new t.d());
      this.K(new t.g());
      if (var1) {
         this.y(FakeSpawnerModule.class).R();
      }
   }

   private void K(Module var1) {
      if (q_.U(var1) == q_.B) {
         var1.offerHoldKeybind();
      }

      this.A.add(var1);
   }

   public void E() {
      Minecraft var1 = Minecraft.getInstance();
      int var10000 = Integer.compare(31, 31);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         for (Module var3 : this.A) {
            var3.suspendForDisconnect();
         }

         f7.j();
         ij.F();
         b6.b();
         _p.X();
         qg.g();
      }

      if (this.Z.d(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
         for (Module var5 : this.A) {
            var5.suspendForDisconnect();
         }

         FakeLagModule.f();
         f7.j();
         ij.F();
         b6.b();
         _p.X();
         qg.g();
      }
   }

   public void H() {
      this.E();
      b6.b();
      _p.j();
      ij.i();

      for (Module var2 : this.A) {
         var2.activatePending();
      }

      a6.M();

      for (Module.a var4 : Module.a.values()) {
         for (Module var6 : this.A) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!q.contains(var6.getClass()) || !a6.i())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  _q.o("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.a.T) {
            ij.z();
            b6.e();
         }
      }
   }

   public List<Module> d() {
      return this.A;
   }

   public List<Module> a() {
      if (fj.r()) {
         if (this.x == null) {
            this.x = this.A.stream().filter(fj::G).toList();
         }

         return this.x;
      } else {
         return this.A;
      }
   }

   public void i(boolean var1) {
      if (var1 != fj.r()) {
         fj.l(var1);
         if (!var1) {
            _q.z("Legit mode", "Off; enabled modules resume on the next tick");
         } else {
            ArrayList var2 = new ArrayList();

            for (Module var4 : this.A) {
               if (fj.r(var4) && var4.isEnabled()) {
                  var4.suspendForDisconnect();
                  var2.add(var4.getName());
               }
            }

            f7.j();
            ij.F();
            b6.b();
            _p.X();
            qg.g();
            _q.z("Legit mode", "On; suspended {} and kept their preferences", var2);
         }
      }
   }

   public List<Module> r(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.A) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> u() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.A) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module U(String var1) {
      for (Module var3 : this.A) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T y(Class<T> var1) {
      for (Module var3 : this.A) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
