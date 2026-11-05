package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._m;
import com.larpclient.m_;
import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.tq;
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
   private static AutoClickerModule y;
   private final d6 X = this.addSetting(new d6("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic"));
   private final dh n = this.addSetting(new dh("Left Click", "Repeat attacks", true));
   private final dh M = this.addSetting(new dh("Right Click", "Repeat item use and placement", false));
   private final d9 O = this.addSetting(new d9("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final d9 m = this.addSetting(new d9("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final dh Z = this.addSetting(new dh("Randomize CPS", "Choose a rate within each button's range after a click", false));
   private final d9 x = this.addSetting(new d9("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final d9 l = this.addSetting(new d9("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final dh U = this.addSetting(new dh("Only Weapon", "Only click while holding a weapon", false));
   private final dh c = this.addSetting(new dh("Wait Cooldown", "Wait for a fully charged attack", false));
   private final dh C = this.addSetting(new dh("Only Entities", "Left clicks require an entity under your actual aim", false));
   private final dh z = this.addSetting(new dh("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final m_ W = new m_();
   private final m_ N = new m_();
   private boolean K;
   private boolean Y;
   private String s = "Waiting for input";
   private long v;
   private int t;
   private int T;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      y = this;
      this.O.H(this.n::P);
      this.m.H(this.M::P);
      this.x.H(() -> this.n.P() && this.Z.P());
      this.l.H(() -> this.M.P() && this.Z.P());
      this.c.H(this.n::P);
      this.C.H(this.n::P);
      this.z.H(() -> this.n.P() && !this.C.P());

      for (df var2 : this.getSettings()) {
         var2.h(var1 -> this.m());
      }
   }

   public static AutoClickerModule E() {
      return y;
   }

   public static void I(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   public List<df<?>> Z() {
      return List.of(this.X, this.n, this.M, this.U);
   }

   public List<df<?>> w() {
      return List.of(this.O, this.m, this.Z, this.x, this.l, this.c);
   }

   public List<df<?>> l() {
      return List.of(this.C, this.z);
   }

   private boolean b() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && mc.gui.overlay() == null
         && (!this.U.P() || ReachModule.p());
   }

   public boolean z() {
      return !this.isEnabled() ? false : this.b() && this.n.P() && !mc.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON);
   }

   public boolean e() {
      return !this.isEnabled() ? false : this.b() && this.M.P();
   }

   public boolean x() {
      return this.K;
   }

   private boolean u(boolean var1) {
      return this.X.S("Automatic") || (var1 ? mc.options.keyUse : mc.options.keyAttack).isDown();
   }

   private boolean n() {
      return te.i("Aura") || tq.l() != tq.y.A;
   }

   private boolean x(boolean var1) {
      return (var1 ? this.e() : this.z())
         && this.u(var1)
         && _m.z()
         && !this.n()
         && !mc.player.isUsingItem()
         && !mc.player.isHandsBusy()
         && (var1 || !mc.options.keyUse.isDown());
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.e() && this.X.S("Automatic") && !this.n() && _m.z() && mc.player.isUsingItem();
   }

   @Override
   public void onTick() {
      if (this.b() && _m.z()) {
         int var10000 = this.n();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            long var1 = System.nanoTime();
            this.s = mc.player.isUsingItem() ? "Using held item" : "Waiting for input";
            if (!this.x(false)) {
               this.W.A();
            }

            if (!this.x(true)) {
               this.N.A();
            }

            int var3 = this.x(true) && this.N.z(var1) ? 1 : 0;
            if (var3 != 0) {
               boolean var11 = false;
               byte var10 = this.Y;
               var10000 = Integer.signum(var10);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.U();
                  return;
               }
            }

            byte var4 = 0;
            if (this.x(false) && this.W.z(var1)) {
               HitResult var5 = this.m();
               if (var5 instanceof EntityHitResult var6) {
                  int var18 = this.c.P();
                  boolean var15 = false;
                  byte var14 = (byte)var18;
                  var18 = Integer.signum(var14);
                  if (((var18 | -var18) >>> 31 ^ 1) == 0 && !_m.d()) {
                     this.s = "Waiting for attack cooldown";
                  } else {
                     Entity var7 = var6.getEntity();
                     _m.f(
                        this,
                        var7,
                        mc.player.entityInteractionRange(),
                        this.c.P(),
                        () -> this.x(false) && this.m() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                        1,
                        () -> this.H(false),
                        var1x -> this.s = "Waiting: " + var1x
                     );
                     var4 = 1;
                  }
               } else if (var5.getType() == Type.MISS && !this.C.P()) {
                  _m.F(this, () -> this.s(() -> {
                     mc.startAttack();
                     this.H(false);
                  }), () -> this.x(false) && !this.C.P() && this.m().getType() == Type.MISS && (!this.c.P() || _m.d()));
                  var4 = 1;
               }
            }

            boolean var13 = false;
            int var12 = var3;
            var10000 = Integer.signum(var12);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && var4 == 0) {
               this.U();
            }

            return;
         }
      }

      this.m();
      this.s = "Paused: camera or input unavailable";
   }

   private void U() {
      _m.F(this, () -> this.s(() -> {
         mc.startUseItem();
         this.H(true);
      }), () -> this.x(true));
   }

   public void Y(Consumer<Boolean> var1, boolean var2) {
      if (!this.z()) {
         var1.accept(var2);
      } else {
         this.s(() -> {
            int var2x = this.x(false) && this.z.P() && !this.C.P() && mc.hitResult.getType() == Type.BLOCK ? 1 : 0;
            var1.accept(Boolean.valueOf((boolean)var2x));
            if (var2x != 0) {
               this.s = "Mining";
            }
         });
      }
   }

   private void s(Runnable var1) {
      HitResult var2 = mc.hitResult;
      boolean var3 = this.K;
      mc.hitResult = this.m();
      this.K = (boolean)1;

      try {
         var1.run();
      } finally {
         mc.hitResult = var2;
         this.K = var3;
      }
   }

   private HitResult m() {
      return mc.player.raycastHitResult(1.0F, mc.player);
   }

   private void H(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.m : this.O).P();
      double var6 = this.Z.P() ? (var1 ? this.l : this.x).P() : var4;
      (var1 ? this.N : this.W).X(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.T++;
      } else {
         this.t++;
      }

      this.Y = (boolean)(!var1 ? 1 : 0);
      this.s = var1 ? "Using" : "Clicking";
      if (var2 - this.v >= 1000000000L) {
         this.v = var2;
         mt.U("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.t, this.T, this.X.P());
      }
   }

   private void m() {
      this.W.A();
      this.N.A();
      _m.w(this);
   }

   @Override
   public void onEnable() {
      this.m();
      this.t = this.T = 0;
      this.v = 0L;
      this.Y = (boolean)0;
      this.s = "Waiting for input";
      mt.U("AutoClicker", "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}", this.X.P(), this.O.P(), this.m.P(), this.c.P());
   }

   @Override
   public void onDisable() {
      this.m();
      this.s = "Waiting for input";
      mt.U("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.s;
   }
}
