package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.d8;
import com.larpclient.fj;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
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
   private static AutoClickerModule M;
   private final lp X = this.addSetting(new lp("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic"));
   private final ln J = this.addSetting(new ln("Left Click", "Repeat attacks", true));
   private final ln x = this.addSetting(new ln("Right Click", "Repeat item use and placement", false));
   private final lc m = this.addSetting(new lc("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final lc v = this.addSetting(new lc("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final ln c = this.addSetting(new ln("Randomize CPS", "Choose a rate within each button's range after a click", false));
   private final lc F = this.addSetting(new lc("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final lc r = this.addSetting(new lc("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final ln u = this.addSetting(new ln("Only Weapon", "Only click while holding a weapon", false));
   private final ln A = this.addSetting(new ln("Wait Cooldown", "Wait for a fully charged attack", false));
   private final ln k = this.addSetting(new ln("Only Entities", "Left clicks require an entity under your actual aim", false));
   private final ln b = this.addSetting(new ln("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final fj w = new fj();
   private final fj i = new fj();
   private boolean H;
   private boolean h;
   private String P = "Waiting for input";
   private long Z;
   private int C;
   private int K;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      M = this;
      this.m.x(this.J::getValue);
      this.v.x(this.x::getValue);
      this.F.x(() -> this.J.getValue() && this.c.getValue());
      this.r.x(() -> this.x.getValue() && this.c.getValue());
      this.A.x(this.J::getValue);
      this.k.x(this.J::getValue);
      this.b.x(() -> this.J.getValue() && !this.k.getValue());

      for (l6 var2 : this.getSettings()) {
         var2.W(var1 -> this.d());
      }
   }

   public static AutoClickerModule p() {
      return M;
   }

   public static void J(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   public List<l6<?>> t() {
      return List.of(this.X, this.J, this.x, this.u);
   }

   public List<l6<?>> m() {
      return List.of(this.m, this.v, this.c, this.F, this.r, this.A);
   }

   public List<l6<?>> E() {
      return List.of(this.k, this.b);
   }

   private boolean P() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && mc.gui.overlay() == null
         && (!this.u.getValue() || ReachModule.s());
   }

   public boolean x() {
      return !this.isEnabled() ? false : this.P() && this.J.getValue() && !mc.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON);
   }

   public boolean N() {
      return !this.isEnabled() ? false : this.P() && this.x.getValue();
   }

   public boolean l() {
      return this.H;
   }

   private boolean J(boolean var1) {
      return this.X.i("Automatic") || (var1 ? mc.options.keyUse : mc.options.keyAttack).isDown();
   }

   private boolean c() {
      return yu.G("Aura") || rr.u() != rr.f.i;
   }

   private boolean b(boolean var1) {
      return (var1 ? this.N() : this.x())
         && this.J(var1)
         && d8.a()
         && !this.c()
         && !mc.player.isUsingItem()
         && !mc.player.isHandsBusy()
         && (var1 || !mc.options.keyUse.isDown());
   }

   public boolean i() {
      return !this.isEnabled() ? false : this.N() && this.X.i("Automatic") && !this.c() && d8.a() && mc.player.isUsingItem();
   }

   @Override
   public void onTick() {
      int var10000 = this.P();
      boolean var9 = false;
      byte var8 = (byte)var10000;
      var10000 = Integer.signum(var8);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && d8.a() && !this.c()) {
         long var1 = System.nanoTime();
         this.P = mc.player.isUsingItem() ? "Using held item" : "Waiting for input";
         if (!this.b(false)) {
            this.w.K();
         }

         if (!this.b(true)) {
            this.i.K();
         }

         int var3 = this.b(true) && this.i.j(var1) ? 1 : 0;
         boolean var13 = false;
         int var12 = var3;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            boolean var15 = false;
            byte var14 = this.h;
            var10000 = Integer.signum(var14);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.t();
               return;
            }
         }

         byte var4 = 0;
         if (this.b(false) && this.w.j(var1)) {
            HitResult var5 = this.N();
            if (var5 instanceof EntityHitResult var6) {
               if (this.A.getValue() && !d8.R()) {
                  this.P = "Waiting for attack cooldown";
               } else {
                  Entity var7 = var6.getEntity();
                  d8.n(
                     this,
                     var7,
                     mc.player.entityInteractionRange(),
                     this.A.getValue(),
                     () -> this.b(false) && this.N() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                     1,
                     () -> this.L(false),
                     var1x -> this.P = "Waiting: " + var1x
                  );
                  var4 = 1;
               }
            } else if (var5.getType() == Type.MISS && !this.k.getValue()) {
               d8.A(this, () -> this.M(() -> {
                  mc.startAttack();
                  this.L(false);
               }), () -> this.b(false) && !this.k.getValue() && this.N().getType() == Type.MISS && (!this.A.getValue() || d8.R()));
               var4 = 1;
            }
         }

         if (var3 != 0) {
            boolean var11 = false;
            byte var10 = var4;
            var10000 = Integer.signum(var10);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.t();
            }
         }
      } else {
         this.d();
         this.P = "Paused: camera or input unavailable";
      }
   }

   private void t() {
      d8.A(this, () -> this.M(() -> {
         mc.startUseItem();
         this.L(true);
      }), () -> this.b(true));
   }

   public void P(Consumer<Boolean> var1, boolean var2) {
      if (!this.x()) {
         var1.accept(var2);
      } else {
         this.M(() -> {
            int var2x = this.b(false) && this.b.getValue() && !this.k.getValue() && mc.hitResult.getType() == Type.BLOCK ? 1 : 0;
            var1.accept(Boolean.valueOf((boolean)var2x));
            if (var2x != 0) {
               this.P = "Mining";
            }
         });
      }
   }

   private void M(Runnable var1) {
      HitResult var2 = mc.hitResult;
      boolean var3 = this.H;
      mc.hitResult = this.N();
      this.H = (boolean)1;

      try {
         var1.run();
      } finally {
         mc.hitResult = var2;
         this.H = var3;
      }
   }

   private HitResult N() {
      return mc.player.raycastHitResult(1.0F, mc.player);
   }

   private void L(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.v : this.m).getValue();
      double var6 = this.c.getValue() ? (var1 ? this.r : this.F).getValue() : var4;
      (var1 ? this.i : this.w).M(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.K++;
      } else {
         this.C++;
      }

      this.h = (boolean)(!var1 ? 1 : 0);
      this.P = var1 ? "Using" : "Clicking";
      if (var2 - this.Z >= 1000000000L) {
         this.Z = var2;
         r4.g("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.C, this.K, this.X.getValue());
      }
   }

   private void d() {
      this.w.K();
      this.i.K();
      d8.K(this);
   }

   @Override
   public void onEnable() {
      this.d();
      this.C = this.K = 0;
      this.Z = 0L;
      this.h = (boolean)0;
      this.P = "Waiting for input";
      r4.g(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.X.getValue(),
         this.m.getValue(),
         this.v.getValue(),
         this.A.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.d();
      this.P = "Waiting for input";
      r4.g("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.P;
   }
}
