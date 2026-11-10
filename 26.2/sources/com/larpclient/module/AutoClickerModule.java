package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.c5;
import com.larpclient.p_;
import com.larpclient.t_;
import com.larpclient.ta;
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
   private static AutoClickerModule b;
   private final l H = this.addSetting(new l("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic"));
   private final s j = this.addSetting(new s("Left Click", "Repeat attacks", true));
   private final s z = this.addSetting(new s("Right Click", "Repeat item use and placement", false));
   private final e W = this.addSetting(new e("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final e L = this.addSetting(new e("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final s p = this.addSetting(new s("Randomize CPS", "Choose a rate within each button's range after a click", false));
   private final e V = this.addSetting(new e("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final e c = this.addSetting(new e("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final s B = this.addSetting(new s("Only Weapon", "Only click while holding a weapon", false));
   private final s m = this.addSetting(new s("Wait Cooldown", "Wait for a fully charged attack", false));
   private final s o = this.addSetting(new s("Only Entities", "Left clicks require an entity under your actual aim", false));
   private final s w = this.addSetting(new s("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final t_ S = new t_();
   private final t_ q = new t_();
   private boolean A;
   private boolean U;
   private String h = "Waiting for input";
   private long l;
   private int J;
   private int v;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      b = this;
      this.W.P(this.j::getValue);
      this.L.P(this.z::getValue);
      this.V.P(() -> this.j.getValue() && this.p.getValue());
      this.c.P(() -> this.z.getValue() && this.p.getValue());
      this.m.P(this.j::getValue);
      this.o.P(this.j::getValue);
      this.w.P(() -> this.j.getValue() && !this.o.getValue());

      for (q var2 : this.getSettings()) {
         var2.o(var1 -> this.q());
      }
   }

   public static AutoClickerModule N() {
      return b;
   }

   public static void t(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K);
   }

   public List<q<?>> m() {
      return List.of(this.H, this.j, this.z, this.B);
   }

   public List<q<?>> Q() {
      return List.of(this.W, this.L, this.p, this.V, this.c, this.m);
   }

   public List<q<?>> Z() {
      return List.of(this.o, this.w);
   }

   private boolean K() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && mc.gui.overlay() == null
         && (!this.B.getValue() || ReachModule.x());
   }

   public boolean H() {
      return !this.isEnabled() ? false : this.K() && this.j.getValue() && !mc.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON);
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.K() && this.z.getValue();
   }

   public boolean i() {
      return this.A;
   }

   private boolean d(boolean var1) {
      return this.H.O("Automatic") || (var1 ? mc.options.keyUse : mc.options.keyAttack).isDown();
   }

   private boolean J() {
      return c5.t("Aura") || p_.A() != p_.o.T;
   }

   private boolean H(boolean var1) {
      return (var1 ? this.h() : this.H())
         && this.d(var1)
         && ta.Y()
         && !this.J()
         && !mc.player.isUsingItem()
         && !mc.player.isHandsBusy()
         && (var1 || !mc.options.keyUse.isDown());
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.h() && this.H.O("Automatic") && !this.J() && ta.Y() && mc.player.isUsingItem();
   }

   @Override
   public void onTick() {
      if (this.K() && ta.Y() && !this.J()) {
         long var1 = System.nanoTime();
         this.h = mc.player.isUsingItem() ? "Using held item" : "Waiting for input";
         if (!this.H(false)) {
            this.S.i();
         }

         int var10000 = this.H(true);
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.q.i();
         }

         int var3 = this.H(true) && this.q.V(var1) ? 1 : 0;
         boolean var11 = false;
         int var10 = var3;
         var10000 = Integer.signum(var10);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            boolean var13 = false;
            byte var12 = this.U;
            var10000 = Integer.signum(var12);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.z();
               return;
            }
         }

         byte var4 = 0;
         if (this.H(false) && this.S.V(var1)) {
            HitResult var5 = this.N();
            if (var5 instanceof EntityHitResult var6) {
               if (this.m.getValue() && !ta.K()) {
                  this.h = "Waiting for attack cooldown";
               } else {
                  Entity var7 = var6.getEntity();
                  ta.M(
                     this,
                     var7,
                     mc.player.entityInteractionRange(),
                     this.m.getValue(),
                     () -> this.H(false) && this.N() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                     1,
                     () -> this.t(false),
                     var1x -> this.h = "Waiting: " + var1x
                  );
                  var4 = 1;
               }
            } else if (var5.getType() == Type.MISS && !this.o.getValue()) {
               ta.F(this, () -> this.f(() -> {
                  mc.startAttack();
                  this.t(false);
               }), () -> this.H(false) && !this.o.getValue() && this.N().getType() == Type.MISS && (!this.m.getValue() || ta.K()));
               var4 = 1;
            }
         }

         boolean var15 = false;
         int var14 = var3;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && var4 == 0) {
            this.z();
         }
      } else {
         this.q();
         this.h = "Paused: camera or input unavailable";
      }
   }

   private void z() {
      ta.F(this, () -> this.f(() -> {
         mc.startUseItem();
         this.t(true);
      }), () -> this.H(true));
   }

   public void M(Consumer<Boolean> var1, boolean var2) {
      if (!this.H()) {
         var1.accept(var2);
      } else {
         this.f(() -> {
            int var2x = this.H(false) && this.w.getValue() && !this.o.getValue() && mc.hitResult.getType() == Type.BLOCK ? 1 : 0;
            var1.accept(Boolean.valueOf((boolean)var2x));
            if (var2x != 0) {
               this.h = "Mining";
            }
         });
      }
   }

   private void f(Runnable var1) {
      HitResult var2 = mc.hitResult;
      boolean var3 = this.A;
      mc.hitResult = this.N();
      this.A = (boolean)1;

      try {
         var1.run();
      } finally {
         mc.hitResult = var2;
         this.A = var3;
      }
   }

   private HitResult N() {
      return mc.player.raycastHitResult(1.0F, mc.player);
   }

   private void t(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.L : this.W).getValue();
      double var6 = this.p.getValue() ? (var1 ? this.c : this.V).getValue() : var4;
      (var1 ? this.q : this.S).m(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.v++;
      } else {
         this.J++;
      }

      this.U = (boolean)(!var1 ? 1 : 0);
      this.h = var1 ? "Using" : "Clicking";
      if (var2 - this.l >= 1000000000L) {
         this.l = var2;
         com.larpclient.h.U("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.J, this.v, this.H.getValue());
      }
   }

   private void q() {
      this.S.i();
      this.q.i();
      ta.s(this);
   }

   @Override
   public void onEnable() {
      this.q();
      this.J = this.v = 0;
      this.l = 0L;
      this.U = (boolean)0;
      this.h = "Waiting for input";
      com.larpclient.h.U(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.H.getValue(),
         this.W.getValue(),
         this.L.getValue(),
         this.m.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.q();
      this.h = "Waiting for input";
      com.larpclient.h.U("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.h;
   }
}
