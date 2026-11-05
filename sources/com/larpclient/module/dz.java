package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.CrystalChamsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient._m;
import com.larpclient.c3;
import com.larpclient.ka;
import com.larpclient.kf;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.om;
import com.larpclient.t2;
import com.larpclient.tq;
import com.larpclient.w7;
import com.larpclient.wq;
import com.larpclient.xg;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class dz {
   private final List<Module> d = new ArrayList<>();
   private static final Set<Class<? extends Module>> Q = Set.of(
      AutoTotemModule.class,
      HoverTotemModule.class,
      AutoSellModule.class,
      ElytraMotionModule.class,
      AutoArmorModule.class,
      ChestStealerModule.class,
      InvCleanerModule.class,
      OrderDropperModule.class
   );
   private final xg a = new xg();

   public void w() {
      this.y(true);
   }

   public void y(boolean var1) {
      this.h(new AuraModule());
      this.h(new AntiKnockbackModule());
      this.h(new BacktrackModule());
      this.h(new CriticalsModule());
      this.h(new AutoTotemModule());
      this.h(new HoverTotemModule());
      this.h(new MaceModule());
      this.h(new LungeSwapModule());
      this.h(new TotemHitModule());
      this.h(new ShieldBreakerModule());
      this.h(new RodSwapModule());
      this.h(new ReachModule());
      this.h(new TriggerBotModule());
      this.h(new AutoClickerModule());
      this.h(new AutoCrystalModule());
      this.h(new CrystalOptimizerModule());
      this.h(new AnchorTapModule());
      this.h(new AutoAnchorModule());
      this.h(new AutoCartModule());
      this.h(new AntiTrapModule());
      this.h(new w());
      this.h(new f());
      this.h(new IgnoreEntityModule());
      this.h(new AimAssistModule());
      this.h(new WTapModule());
      this.h(new FlyModule());
      this.h(new SpeedModule());
      this.h(new GrimSpeedModule());
      this.h(new TargetStrafeModule());
      this.h(new ScaffoldModule());
      this.h(new JesusModule());
      this.h(new NoFallModule());
      this.h(new NoSlowdownModule());
      this.h(new NoJumpDelayModule());
      this.h(new AutoSprintModule());
      this.h(new ElytraFlyModule());
      this.h(new EntitySpeedModule());
      this.h(new BoatFlyModule());
      this.h(new AirStuckModule());
      this.h(new ElytraMotionModule());
      this.h(new ElytraHelperModule());
      this.h(new AutoPearlCatchModule());
      this.h(new WindChargeHopModule());
      this.h(new AutoMLGModule());
      this.h(new FullbrightModule());
      this.h(new ZoomModule());
      this.h(new ESPModule());
      this.h(new XRayModule());
      this.h(new NoBlockRotationModule());
      this.h(new RealisticWaterModule());
      this.h(new WavingFoliageModule());
      this.h(new WetGroundModule());
      this.h(new SnowGroundModule());
      this.h(new TracersModule());
      this.h(new NametagsModule());
      this.h(new NoWeatherModule());
      this.h(new ChamsModule());
      this.h(new HandChamsModule());
      this.h(new CrystalChamsModule());
      this.h(new WalkStyleModule());
      this.h(new EmotesModule());
      this.h(new PetsModule());
      this.h(new CryptoPetsModule());
      this.h(new ItemESPModule());
      this.h(new TimeChangerModule());
      this.h(new AnimationsModule());
      this.h(new ViewModelModule());
      this.h(new RemovalsModule());
      this.h(new GUIAnimationModule());
      this.h(new Feature3DUIModule());
      this.h(new WorldMenuModule());
      this.h(new LineGlyphsModule());
      this.h(new PearlParticlesModule());
      this.h(new SkyShaderModule());
      this.h(new SwordModelsModule());
      this.h(new StorageESPModule());
      this.h(new PlayerTrailModule());
      this.h(new HitFXModule());
      this.h(new KillFXModule());
      this.h(new HalloweenModule());
      this.h(new ItemPhysicModule());
      this.h(new DamageMarkersModule());
      this.h(new WorldLyricsModule());
      this.h(new IslandLyricsModule());
      this.h(new EchoHandModule());
      this.h(new FlameHandsModule());
      this.h(new HitSoundsModule());
      this.h(new PlayerModelModule());
      this.h(new SkinChangerModule());
      this.h(new MenuBackgroundModule());
      this.h(new CapesModule());
      this.h(new JumpFXModule());
      this.h(new BlockOverlayModule());
      this.h(new TargetAuraModule());
      this.h(new SelfVisualsModule());
      this.h(new WorldColorModule());
      this.h(new MotionBlurModule());
      this.h(new AmbientParticlesModule());
      this.h(new AstralWeaponTrailsModule());
      this.h(new CelestialWeatherModule());
      this.h(new FogModule());
      this.h(new TargetHUDModule());
      this.h(new StaffListModule());
      this.h(new CustomCrosshairModule());
      this.h(new ClickPulseModule());
      this.h(new FreelookModule());
      this.h(new HUDModule());
      this.h(new CustomSkyModule());
      this.h(new StretchedResModule());
      this.h(new ChinaHatModule());
      this.h(new WingsModule());
      this.h(new HeadlessKorbloxModule());
      this.h(new LARPModule());
      this.h(new NameChangerModule());
      if (var1) {
         this.V(HUDModule.class).setEnabled(true);
      }

      this.h(new PerformanceModule());
      this.h(new FreecamModule());
      this.h(new SmoothCameraModule());
      this.h(new FakeLagModule());
      this.h(new AutoArmorModule());
      this.h(new AutoEatModule());
      this.h(new ChestStealerModule());
      this.h(new AutoRespawnModule());
      this.h(new AutoDisconnectModule());
      this.h(new InvCleanerModule());
      this.h(new MCFriendModule());
      this.h(new AutoGapModule());
      this.h(new NoBreakDelayModule());
      this.h(new FastUseModule());
      this.h(new AutoDrainModule());
      this.h(new StreamProofModule());
      this.h(new an());
      this.h(new a5());
      this.h(new a0());
      this.h(new AutoSellModule());
      this.h(new HomeMacroModule());
      this.h(new OrderDropperModule());
      this.h(new LightDebugModule());
      this.h(new MotionScannerModule());
      this.h(new HolesESPModule());
      this.h(new kf());
      this.h(new ka());
      this.h(new wq());
      this.h(new FakePayModule());
      this.h(new FakeSpawnerModule());
      this.h(new SpinBotModule());
      this.h(new m.p());
      this.h(new m.x());
      this.h(new m.b());
      this.h(new m.c());
      this.h(new m.h());
      this.h(new m.z());
      this.h(new m.e());
      this.h(new m.s());
      this.h(new m.q());
      this.h(new m.v());
      this.h(new m.u());
      this.h(new m.f());
      this.h(new m.k());
      this.h(new m.j());
      if (var1) {
         this.V(FakeSpawnerModule.class).u();
      }
   }

   private void h(Module var1) {
      if (om.d(var1) == om.F) {
         var1.offerHoldKeybind();
      }

      this.d.add(var1);
   }

   public void P() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.a.h(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
         for (Module var3 : this.d) {
            var3.suspendForDisconnect();
         }

         FakeLagModule.w();
         t2.G();
         tq.l();
         m9.S();
         _m.U();
         w7.M();
      }
   }

   public void L() {
      this.P();
      m9.S();
      _m.u();
      tq.U();

      for (Module var2 : this.d) {
         var2.activatePending();
      }

      c3.G();

      for (Module.f var4 : Module.f.values()) {
         for (Module var6 : this.d) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!Q.contains(var6.getClass()) || !c3.Q())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  mt.P("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.f.Z) {
            tq.o();
            m9.j();
         }
      }
   }

   public List<Module> C() {
      return this.d;
   }

   public List<Module> I(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.d) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> X() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.d) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module k(String var1) {
      for (Module var3 : this.d) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T V(Class<T> var1) {
      for (Module var3 : this.d) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
