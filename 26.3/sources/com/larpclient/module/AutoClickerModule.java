package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.nz;
import com.larpclient.sq;
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
   private static AutoClickerModule s;
   private final o i = this.addSetting(new o("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic"));
   private final m v = this.addSetting(new m("Left Click", "Repeat attacks", true));
   private final m a = this.addSetting(new m("Right Click", "Repeat item use and placement", false));
   private final b y = this.addSetting(new b("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final b q = this.addSetting(new b("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final m K = this.addSetting(new m("Randomize CPS", "Choose a rate within each button's range after a click", false));
   private final b e = this.addSetting(new b("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final b u = this.addSetting(new b("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final m t = this.addSetting(new m("Only Weapon", "Only click while holding a weapon", false));
   private final m f = this.addSetting(new m("Wait Cooldown", "Wait for a fully charged attack", false));
   private final m U = this.addSetting(new m("Only Entities", "Left clicks require an entity under your actual aim", false));
   private final m L = this.addSetting(new m("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final sq p = new sq();
   private final sq J = new sq();
   private boolean b;
   private boolean c;
   private String A = "Waiting for input";
   private long l;
   private int D;
   private int o;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      s = this;
      this.y.a(this.v::getValue);
      this.q.a(this.a::getValue);
      this.e.a(() -> this.v.getValue() && this.K.getValue());
      this.u.a(() -> this.a.getValue() && this.K.getValue());
      this.f.a(this.v::getValue);
      this.U.a(this.v::getValue);
      this.L.a(() -> this.v.getValue() && !this.U.getValue());

      for (r var2 : this.getSettings()) {
         var2.w(var1 -> this.p());
      }
   }

   public static AutoClickerModule l() {
      return s;
   }

   public static void r(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F);
   }

   public List<r<?>> a() {
      return List.of(this.i, this.v, this.a, this.t);
   }

   public List<r<?>> D() {
      return List.of(this.y, this.q, this.K, this.e, this.u, this.f);
   }

   public List<r<?>> g() {
      return List.of(this.U, this.L);
   }

   private boolean n() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && mc.gui.overlay() == null
         && (!this.t.getValue() || ReachModule.Y());
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.n() && this.v.getValue() && !mc.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON);
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.n() && this.a.getValue();
   }

   public boolean s() {
      return this.b;
   }

   private boolean r(boolean var1) {
      return this.i.w("Automatic") || (var1 ? mc.options.keyUse : mc.options.keyAttack).isDown();
   }

   private boolean o() {
      return nz.X("Aura") || ij.L() != ij.r.X;
   }

   private boolean V(boolean var1) {
      return (var1 ? this.U() : this.D())
         && this.r(var1)
         && _p.g()
         && !this.o()
         && !mc.player.isUsingItem()
         && !mc.player.isHandsBusy()
         && (var1 || !mc.options.keyUse.isDown());
   }

   public boolean H() {
      return !this.isEnabled() ? false : this.U() && this.i.w("Automatic") && !this.o() && _p.g() && mc.player.isUsingItem();
   }

   @Override
   public void onTick() {
      if (this.n() && _p.g() && !this.o()) {
         long var1 = System.nanoTime();
         this.A = mc.player.isUsingItem() ? "Using held item" : "Waiting for input";
         if (!this.V(false)) {
            this.p.p();
         }

         if (!this.V(true)) {
            this.J.p();
         }

         int var3 = this.V(true) && this.J.L(var1) ? 1 : 0;
         boolean var9 = false;
         int var8 = var3;
         int var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            boolean var11 = false;
            byte var10 = this.c;
            var10000 = Integer.signum(var10);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.W();
               return;
            }
         }

         byte var4 = 0;
         if (this.V(false) && this.p.L(var1)) {
            HitResult var5 = this.V();
            int var17 = var5 instanceof EntityHitResult;
            boolean var15 = false;
            byte var14 = (byte)var17;
            var17 = Integer.signum(var14);
            if (((var17 | -var17) >>> 31 ^ 1) == 0) {
               EntityHitResult var6 = (EntityHitResult)var5;
               if (this.f.getValue() && !_p.P()) {
                  this.A = "Waiting for attack cooldown";
               } else {
                  Entity var7 = var6.getEntity();
                  _p.U(
                     this,
                     var7,
                     mc.player.entityInteractionRange(),
                     this.f.getValue(),
                     () -> this.V(false) && this.V() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                     1,
                     () -> this.G(false),
                     var1x -> this.A = "Waiting: " + var1x
                  );
                  var4 = 1;
               }
            } else if (var5.getType() == Type.MISS && !this.U.getValue()) {
               _p.w(this, () -> this.w(() -> {
                  mc.startAttack();
                  this.G(false);
               }), () -> this.V(false) && !this.U.getValue() && this.V().getType() == Type.MISS && (!this.f.getValue() || _p.P()));
               var4 = 1;
            }
         }

         if (var3 != 0) {
            boolean var13 = false;
            byte var12 = var4;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.W();
            }
         }
      } else {
         this.p();
         this.A = "Paused: camera or input unavailable";
      }
   }

   private void W() {
      _p.w(this, () -> this.w(() -> {
         mc.startUseItem();
         this.G(true);
      }), () -> this.V(true));
   }

   public void J(Consumer<Boolean> var1, boolean var2) {
      if (!this.D()) {
         var1.accept(var2);
      } else {
         this.w(() -> {
            int var2x = this.V(false) && this.L.getValue() && !this.U.getValue() && mc.hitResult.getType() == Type.BLOCK ? 1 : 0;
            var1.accept(Boolean.valueOf((boolean)var2x));
            if (var2x != 0) {
               this.A = "Mining";
            }
         });
      }
   }

   private void w(Runnable var1) {
      HitResult var2 = mc.hitResult;
      boolean var3 = this.b;
      mc.hitResult = this.V();
      this.b = (boolean)1;

      try {
         var1.run();
      } finally {
         mc.hitResult = var2;
         this.b = var3;
      }
   }

   private HitResult V() {
      return mc.player.raycastHitResult(1.0F, mc.player);
   }

   private void G(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.q : this.y).getValue();
      double var6 = this.K.getValue() ? (var1 ? this.u : this.e).getValue() : var4;
      (var1 ? this.J : this.p).X(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.o++;
      } else {
         this.D++;
      }

      this.c = (boolean)(!var1 ? 1 : 0);
      this.A = var1 ? "Using" : "Clicking";
      if (var2 - this.l >= 1000000000L) {
         this.l = var2;
         _q.c("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.D, this.o, this.i.getValue());
      }
   }

   private void p() {
      this.p.p();
      this.J.p();
      _p.e(this);
   }

   @Override
   public void onEnable() {
      this.p();
      this.D = this.o = 0;
      this.l = 0L;
      this.c = (boolean)0;
      this.A = "Waiting for input";
      _q.c(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.i.getValue(),
         this.y.getValue(),
         this.q.getValue(),
         this.f.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.p();
      this.A = "Waiting for input";
      _q.c("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.A;
   }
}
