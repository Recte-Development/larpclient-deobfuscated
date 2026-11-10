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
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.cu;
import com.larpclient.q;
import com.larpclient.q8;
import com.larpclient.s0;
import com.larpclient.s7;
import com.larpclient.sb;
import com.larpclient.sh;
import com.larpclient.sq;
import com.larpclient.sw;
import com.larpclient.sz;
import com.larpclient.t9;
import com.larpclient.wp;
import com.larpclient.wv;
import com.larpclient.yc;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_310;

public class lr {
   private final List<Module> z = new ArrayList<>();
   private static final Set<Class<? extends Module>> Y = Set.of(
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
   private final cu b = new cu();
   private List<Module> G;

   public void H() {
      this.b(true);
   }

   public void b(boolean var1) {
      this.R(new AuraModule());
      this.R(new AntiKnockbackModule());
      this.R(new BacktrackModule());
      this.R(new CriticalsModule());
      this.R(new AutoTotemModule());
      this.R(new HoverTotemModule());
      this.R(new MaceModule());
      this.R(new LungeSwapModule());
      this.R(new TotemHitModule());
      this.R(new ShieldBreakerModule());
      this.R(new RodSwapModule());
      this.R(new ReachModule());
      this.R(new HitBoxesModule());
      this.R(new TriggerBotModule());
      this.R(new AutoClickerModule());
      this.R(new AutoCrystalModule());
      this.R(new CrystalOptimizerModule());
      this.R(new AnchorTapModule());
      this.R(new AnchorMacroModule());
      this.R(new AutoAnchorModule());
      this.R(new AutoCartModule());
      this.R(new AntiTrapModule());
      this.R(new b());
      this.R(new g());
      this.R(new IgnoreEntityModule());
      this.R(new AimAssistModule());
      this.R(new WTapModule());
      this.R(new FlyModule());
      this.R(new SpeedModule());
      this.R(new GrimSpeedModule());
      this.R(new TargetStrafeModule());
      this.R(new ScaffoldModule());
      this.R(new JesusModule());
      this.R(new NoFallModule());
      this.R(new NoSlowdownModule());
      this.R(new NoJumpDelayModule());
      this.R(new AutoSprintModule());
      this.R(new ElytraFlyModule());
      this.R(new EntitySpeedModule());
      this.R(new BoatFlyModule());
      this.R(new AirStuckModule());
      this.R(new ElytraMotionModule());
      this.R(new ElytraHelperModule());
      this.R(new AutoPearlCatchModule());
      this.R(new AutoElytraSwapModule());
      this.R(new WindChargeHopModule());
      this.R(new AutoMLGModule());
      this.R(new FullbrightModule());
      this.R(new ZoomModule());
      this.R(new ESPModule());
      this.R(new XRayModule());
      this.R(new NoBlockRotationModule());
      this.R(new RealisticWaterModule());
      this.R(new WavingFoliageModule());
      this.R(new WetGroundModule());
      this.R(new SnowGroundModule());
      this.R(new TracersModule());
      this.R(new NametagsModule());
      this.R(new NoWeatherModule());
      this.R(new ChamsModule());
      this.R(new HandChamsModule());
      this.R(new CrystalChamsModule());
      this.R(new WalkStyleModule());
      this.R(new EmotesModule());
      this.R(new PetsModule());
      this.R(new CryptoPetsModule());
      this.R(new ItemESPModule());
      this.R(new TimeChangerModule());
      this.R(new AnimationsModule());
      this.R(new ViewModelModule());
      this.R(new RemovalsModule());
      this.R(new GUIAnimationModule());
      this.R(new Feature3DUIModule());
      this.R(new WorldMenuModule());
      this.R(new LineGlyphsModule());
      this.R(new PearlParticlesModule());
      this.R(new SkyShaderModule());
      this.R(new SwordModelsModule());
      this.R(new StorageESPModule());
      this.R(new PlayerTrailModule());
      this.R(new HitFXModule());
      this.R(new KillFXModule());
      this.R(new ItemPhysicModule());
      this.R(new DamageMarkersModule());
      this.R(new WorldLyricsModule());
      this.R(new IslandLyricsModule());
      this.R(new EchoHandModule());
      this.R(new FlameHandsModule());
      this.R(new HitSoundsModule());
      this.R(new PlayerModelModule());
      this.R(new SkinChangerModule());
      this.R(new MenuBackgroundModule());
      this.R(new CapesModule());
      this.R(new JumpFXModule());
      this.R(new BlockOverlayModule());
      this.R(new TargetAuraModule());
      this.R(new SelfVisualsModule());
      this.R(new WorldColorModule());
      this.R(new MotionBlurModule());
      this.R(new AmbientParticlesModule());
      this.R(new AstralWeaponTrailsModule());
      this.R(new CelestialWeatherModule());
      this.R(new FogModule());
      this.R(new TargetHUDModule());
      this.R(new StaffListModule());
      this.R(new CustomCrosshairModule());
      this.R(new ClickPulseModule());
      this.R(new FreelookModule());
      this.R(new HUDModule());
      this.R(new CustomSkyModule());
      this.R(new StretchedResModule());
      this.R(new ChinaHatModule());
      this.R(new WingsModule());
      this.R(new HeadlessKorbloxModule());
      this.R(new HideArmourModule());
      this.R(new LARPModule());
      this.R(new NameChangerModule());
      if (var1) {
         this.b(HUDModule.class).setEnabled(true);
      }

      this.R(new PerformanceModule());
      this.R(new AutoToolModule());
      this.R(new PrimeChunkFinderModule());
      this.R(new FreecamModule());
      this.R(new SmoothCameraModule());
      this.R(new FakeLagModule());
      this.R(new AutoArmorModule());
      this.R(new AutoEatModule());
      this.R(new ChestStealerModule());
      this.R(new AutoRespawnModule());
      this.R(new AutoDisconnectModule());
      this.R(new InvCleanerModule());
      this.R(new MCFriendModule());
      this.R(new AutoGapModule());
      this.R(new NoBreakDelayModule());
      this.R(new FastUseModule());
      this.R(new AutoDrainModule());
      this.R(new StreamProofModule());
      this.R(new AutoGGModule());
      this.R(new WaypointsModule());
      this.R(new MaceExploitModule());
      this.R(new CustomFOVModule());
      this.R(new FakePlayerModule());
      this.R(new AutoReconnectModule());
      this.R(new FakeScoreboardModule());
      this.R(new FakeFPSModule());
      this.R(new CrystalTapModule());
      this.R(new sh());
      this.R(new sb());
      this.R(new sz());
      this.R(new s0());
      this.R(new zk());
      this.R(new ze());
      this.R(new zu());
      this.R(new AutoSellModule());
      this.R(new HomeMacroModule());
      this.R(new OrderDropperModule());
      this.R(new LightDebugModule());
      this.R(new MotionScannerModule());
      this.R(new HolesESPModule());
      this.R(new sw());
      this.R(new s7());
      this.R(new sq());
      this.R(new FakePayModule());
      this.R(new FakeSpawnerModule());
      this.R(new SpinBotModule());
      this.R(new a2.s());
      this.R(new a2.n());
      this.R(new a2.b());
      this.R(new a2.t());
      this.R(new a2.l());
      this.R(new a2.z());
      this.R(new a2.f());
      this.R(new a2.v());
      this.R(new a2.k());
      this.R(new a2.i());
      this.R(new a2.g());
      this.R(new a2.w());
      this.R(new a2.x());
      this.R(new a2.o());
      if (var1) {
         this.b(FakeSpawnerModule.class).O();
      }
   }

   private void R(Module var1) {
      if (yc.w(var1) == yc.m) {
         var1.offerHoldKeybind();
      }

      this.z.add(var1);
   }

   public void Z() {
      class_310 var1 = class_310.method_1551();
      int var10000 = Integer.compare(31, 31);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         for (Module var3 : this.z) {
            var3.suspendForDisconnect();
         }

         wv.K();
         wp.B();
         t9.N();
         cr.H();
         bn.L();
      }

      if (this.b.u(var1.field_1724, var1.field_1687, var1.field_1724 != null && var1.field_1724.method_5805())) {
         for (Module var5 : this.z) {
            var5.suspendForDisconnect();
         }

         FakeLagModule.F();
         wv.K();
         wp.B();
         t9.N();
         cr.H();
         bn.L();
      }
   }

   public void j() {
      this.Z();
      t9.N();
      cr.m();
      wp.s();

      for (Module var2 : this.z) {
         var2.activatePending();
      }

      q.l();

      for (Module.p var4 : Module.p.values()) {
         for (Module var6 : this.z) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!Y.contains(var6.getClass()) || !q.K())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  q8.u("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.p.P) {
            wp.W();
            t9.d();
         }
      }
   }

   public List<Module> M() {
      return this.z;
   }

   public List<Module> Y() {
      if (lf.l()) {
         if (this.G == null) {
            this.G = this.z.stream().filter(lf::D).toList();
         }

         return this.G;
      } else {
         return this.z;
      }
   }

   public void x(boolean var1) {
      if (var1 != lf.l()) {
         lf.M(var1);
         if (!var1) {
            q8.G("Legit mode", "Off; enabled modules resume on the next tick");
         } else {
            ArrayList var2 = new ArrayList();

            for (Module var4 : this.z) {
               if (lf.Y(var4) && var4.isEnabled()) {
                  var4.suspendForDisconnect();
                  var2.add(var4.getName());
               }
            }

            wv.K();
            wp.B();
            t9.N();
            cr.H();
            bn.L();
            q8.G("Legit mode", "On; suspended {} and kept their preferences", var2);
         }
      }
   }

   public List<Module> Z(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.z) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> p() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.z) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module j(String var1) {
      for (Module var3 : this.z) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T b(Class<T> var1) {
      for (Module var3 : this.z) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
