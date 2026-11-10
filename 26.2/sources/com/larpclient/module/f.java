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
import com.larpclient.a0;
import com.larpclient.a3;
import com.larpclient.a4;
import com.larpclient.ac;
import com.larpclient.ae;
import com.larpclient.af;
import com.larpclient.ak;
import com.larpclient.c0;
import com.larpclient.g7;
import com.larpclient.j6;
import com.larpclient.p_;
import com.larpclient.pz;
import com.larpclient.sg;
import com.larpclient.si;
import com.larpclient.ta;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class f {
   private final List<Module> b = new ArrayList<>();
   private static final Set<Class<? extends Module>> L = Set.of(
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
   private final c0 j = new c0();
   private List<Module> r;

   public void Q() {
      this.e(true);
   }

   public void e(boolean var1) {
      this.V(new AuraModule());
      this.V(new AntiKnockbackModule());
      this.V(new BacktrackModule());
      this.V(new CriticalsModule());
      this.V(new AutoTotemModule());
      this.V(new HoverTotemModule());
      this.V(new MaceModule());
      this.V(new LungeSwapModule());
      this.V(new TotemHitModule());
      this.V(new ShieldBreakerModule());
      this.V(new RodSwapModule());
      this.V(new ReachModule());
      this.V(new HitBoxesModule());
      this.V(new TriggerBotModule());
      this.V(new AutoClickerModule());
      this.V(new AutoCrystalModule());
      this.V(new CrystalOptimizerModule());
      this.V(new AnchorTapModule());
      this.V(new AnchorMacroModule());
      this.V(new AutoAnchorModule());
      this.V(new AutoCartModule());
      this.V(new AntiTrapModule());
      this.V(new th());
      this.V(new tb());
      this.V(new IgnoreEntityModule());
      this.V(new AimAssistModule());
      this.V(new WTapModule());
      this.V(new FlyModule());
      this.V(new SpeedModule());
      this.V(new GrimSpeedModule());
      this.V(new TargetStrafeModule());
      this.V(new ScaffoldModule());
      this.V(new JesusModule());
      this.V(new NoFallModule());
      this.V(new NoSlowdownModule());
      this.V(new NoJumpDelayModule());
      this.V(new AutoSprintModule());
      this.V(new ElytraFlyModule());
      this.V(new EntitySpeedModule());
      this.V(new BoatFlyModule());
      this.V(new AirStuckModule());
      this.V(new ElytraMotionModule());
      this.V(new ElytraHelperModule());
      this.V(new AutoPearlCatchModule());
      this.V(new AutoElytraSwapModule());
      this.V(new WindChargeHopModule());
      this.V(new AutoMLGModule());
      this.V(new FullbrightModule());
      this.V(new ZoomModule());
      this.V(new ESPModule());
      this.V(new XRayModule());
      this.V(new NoBlockRotationModule());
      this.V(new RealisticWaterModule());
      this.V(new WavingFoliageModule());
      this.V(new WetGroundModule());
      this.V(new SnowGroundModule());
      this.V(new TracersModule());
      this.V(new NametagsModule());
      this.V(new NoWeatherModule());
      this.V(new ChamsModule());
      this.V(new HandChamsModule());
      this.V(new CrystalChamsModule());
      this.V(new WalkStyleModule());
      this.V(new EmotesModule());
      this.V(new PetsModule());
      this.V(new CryptoPetsModule());
      this.V(new ItemESPModule());
      this.V(new TimeChangerModule());
      this.V(new AnimationsModule());
      this.V(new ViewModelModule());
      this.V(new RemovalsModule());
      this.V(new GUIAnimationModule());
      this.V(new Feature3DUIModule());
      this.V(new WorldMenuModule());
      this.V(new LineGlyphsModule());
      this.V(new PearlParticlesModule());
      this.V(new SkyShaderModule());
      this.V(new SwordModelsModule());
      this.V(new StorageESPModule());
      this.V(new PlayerTrailModule());
      this.V(new HitFXModule());
      this.V(new KillFXModule());
      this.V(new ItemPhysicModule());
      this.V(new DamageMarkersModule());
      this.V(new WorldLyricsModule());
      this.V(new IslandLyricsModule());
      this.V(new EchoHandModule());
      this.V(new FlameHandsModule());
      this.V(new HitSoundsModule());
      this.V(new PlayerModelModule());
      this.V(new SkinChangerModule());
      this.V(new MenuBackgroundModule());
      this.V(new CapesModule());
      this.V(new JumpFXModule());
      this.V(new BlockOverlayModule());
      this.V(new TargetAuraModule());
      this.V(new SelfVisualsModule());
      this.V(new WorldColorModule());
      this.V(new MotionBlurModule());
      this.V(new AmbientParticlesModule());
      this.V(new AstralWeaponTrailsModule());
      this.V(new CelestialWeatherModule());
      this.V(new FogModule());
      this.V(new TargetHUDModule());
      this.V(new StaffListModule());
      this.V(new CustomCrosshairModule());
      this.V(new ClickPulseModule());
      this.V(new FreelookModule());
      this.V(new HUDModule());
      this.V(new CustomSkyModule());
      this.V(new StretchedResModule());
      this.V(new ChinaHatModule());
      this.V(new WingsModule());
      this.V(new HeadlessKorbloxModule());
      this.V(new HideArmourModule());
      this.V(new LARPModule());
      this.V(new NameChangerModule());
      if (var1) {
         this.p(HUDModule.class).setEnabled(true);
      }

      this.V(new PerformanceModule());
      this.V(new AutoToolModule());
      this.V(new FreecamModule());
      this.V(new SmoothCameraModule());
      this.V(new FakeLagModule());
      this.V(new AutoArmorModule());
      this.V(new AutoEatModule());
      this.V(new ChestStealerModule());
      this.V(new AutoRespawnModule());
      this.V(new AutoDisconnectModule());
      this.V(new InvCleanerModule());
      this.V(new MCFriendModule());
      this.V(new AutoGapModule());
      this.V(new NoBreakDelayModule());
      this.V(new FastUseModule());
      this.V(new AutoDrainModule());
      this.V(new StreamProofModule());
      this.V(new AutoGGModule());
      this.V(new WaypointsModule());
      this.V(new MaceExploitModule());
      this.V(new CustomFOVModule());
      this.V(new FakePlayerModule());
      this.V(new AutoReconnectModule());
      this.V(new FakeScoreboardModule());
      this.V(new FakeFPSModule());
      this.V(new CrystalTapModule());
      this.V(new ae());
      this.V(new ac());
      this.V(new a4());
      this.V(new ak());
      this.V(new pl());
      this.V(new ps());
      this.V(new pi());
      this.V(new AutoSellModule());
      this.V(new HomeMacroModule());
      this.V(new OrderDropperModule());
      this.V(new LightDebugModule());
      this.V(new MotionScannerModule());
      this.V(new HolesESPModule());
      this.V(new a0());
      this.V(new a3());
      this.V(new af());
      this.V(new FakePayModule());
      this.V(new FakeSpawnerModule());
      this.V(new SpinBotModule());
      this.V(new n.a());
      this.V(new n.c());
      this.V(new n.b());
      this.V(new n.s());
      this.V(new n.z());
      this.V(new n.h());
      this.V(new n.p());
      this.V(new n.m());
      this.V(new n.g());
      this.V(new n.u());
      this.V(new n.l());
      this.V(new n.o());
      this.V(new n.t());
      this.V(new n.k());
      if (var1) {
         this.p(FakeSpawnerModule.class).L();
      }
   }

   private void V(Module var1) {
      if (pz.B(var1) == pz.C) {
         var1.offerHoldKeybind();
      }

      this.b.add(var1);
   }

   public void u() {
      Minecraft var1 = Minecraft.getInstance();
      int var10000 = Integer.compare(31, 31);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         for (Module var3 : this.b) {
            var3.suspendForDisconnect();
         }

         g7.P();
         p_.j();
         j6.R();
         ta.B();
         si.H();
      }

      if (this.j.c(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
         for (Module var5 : this.b) {
            var5.suspendForDisconnect();
         }

         FakeLagModule.h();
         g7.P();
         p_.j();
         j6.R();
         ta.B();
         si.H();
      }
   }

   public void r() {
      this.u();
      j6.R();
      ta.U();
      p_.W();

      for (Module var2 : this.b) {
         var2.activatePending();
      }

      sg.L();

      for (Module.f var4 : Module.f.values()) {
         for (Module var6 : this.b) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4 && (!L.contains(var6.getClass()) || !sg.I())) {
               try {
                  var6.onTick();
               } catch (RuntimeException var8) {
                  com.larpclient.h.m("Module", "Tick failed; disabling " + var6.getName(), var8);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.f.u) {
            p_.R();
            j6.n();
         }
      }
   }

   public List<Module> h() {
      return this.b;
   }

   public List<Module> a() {
      if (c.p()) {
         if (this.r == null) {
            this.r = this.b.stream().filter(c::P).toList();
         }

         return this.r;
      } else {
         return this.b;
      }
   }

   public void y(boolean var1) {
      if (var1 != c.p()) {
         c.a(var1);
         if (!var1) {
            com.larpclient.h.A("Legit mode", "Off; enabled modules resume on the next tick");
         } else {
            ArrayList var2 = new ArrayList();

            for (Module var4 : this.b) {
               if (c.W(var4) && var4.isEnabled()) {
                  var4.suspendForDisconnect();
                  var2.add(var4.getName());
               }
            }

            g7.P();
            p_.j();
            j6.R();
            ta.B();
            si.H();
            com.larpclient.h.A("Legit mode", "On; suspended {} and kept their preferences", var2);
         }
      }
   }

   public List<Module> q(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.b) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> I() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.b) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module d(String var1) {
      for (Module var3 : this.b) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T p(Class<T> var1) {
      for (Module var3 : this.b) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
