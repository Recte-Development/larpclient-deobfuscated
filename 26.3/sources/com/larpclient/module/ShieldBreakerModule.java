package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.qg;
import com.larpclient.s6;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule S;
   private final m i = this.addSetting(new m("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true));
   private final s6 e = new s6(this);
   private String U = "Ready";
   private Player O;
   private int p;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      S = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void Y(LocalPlayer var0, Entity var1) {
      if (S != null && S.isEnabled()) {
         ShieldBreakerModule var2 = S;
         if (var2 != null && var2.isRuntimeActive() && !_p.v() && var1 instanceof Player var3) {
            if (var0 == getMc().player && !var2.e.j() && qg.S(var3, var0) && !qg.y(var3)) {
               MaceModule var4 = MaceModule.U();
               if (var4 != null && var4.R(var0, var3)) {
                  var2.U = "Stun Slam by Auto Mace";
               } else {
                  int var5 = qg.K(var0);
                  if (var5 < 0) {
                     var2.U = "No axe in hotbar";
                  } else if (!var2.e.h(var0, var5)) {
                     var2.U = "Hotbar busy";
                  } else {
                     qg.R(var3);
                     var2.O = var3;
                     var2.p = var3.tickCount;
                     var2.U = "Shield break hit";
                     _q.c(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.getId(),
                        var5 + 1,
                        var3.getTicksUsingItem(),
                        qg.C(var0)
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.U : "";
   }

   @Override
   public void onEnable() {
      this.U = "Ready";
      this.O = null;
      _q.z("ShieldBreaker", "Enabled; swap back {}", this.i.getValue());
   }

   @Override
   public void onDisable() {
      if (this.e.j()) {
         this.e.X(getMc(), this.i.getValue());
      }

      this.O = null;
   }

   @Override
   public void onTick() {
      this.d();
      int var10000 = this.e.j();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.e.G(mc, this.i.getValue());
         int var6 = this.e.j();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.U = "Ready";
         }
      }
   }

   private void d() {
      Player var1 = this.O;
      if (var1 != null) {
         int var2 = var1.tickCount - this.p;
         if (var1.isRemoved() || var1.level() != mc.level) {
            this.O = null;
         } else if (!var1.isUsingItem()) {
            this.O = null;
            _q.c("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!qg.y(var1)) {
            this.O = null;
            this.U = "Retrying";
            _q.z("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
