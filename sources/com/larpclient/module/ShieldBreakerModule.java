package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.mt;
import com.larpclient.ti;
import com.larpclient.w7;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule Q;
   private final dh N = this.addSetting(new dh("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true));
   private final ti D = new ti(this);
   private String I = "Ready";
   private Player S;
   private int a;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      Q = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void X(LocalPlayer var0, Entity var1) {
      if (Q != null && Q.isEnabled()) {
         ShieldBreakerModule var2 = Q;
         if (var2 != null && var2.isRuntimeActive() && !_m.w() && var1 instanceof Player var3) {
            if (var0 == getMc().player && !var2.D.k() && w7.o(var3, var0) && !w7.p(var3)) {
               MaceModule var4 = MaceModule.c();
               if (var4 != null && var4.D(var0, var3)) {
                  var2.I = "Stun Slam by Auto Mace";
               } else {
                  int var5 = w7.W(var0);
                  if (var5 < 0) {
                     var2.I = "No axe in hotbar";
                  } else if (!var2.D.e(var0, var5)) {
                     var2.I = "Hotbar busy";
                  } else {
                     w7.l(var3);
                     var2.S = var3;
                     var2.a = var3.tickCount;
                     var2.I = "Shield break hit";
                     mt.U(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.getId(),
                        var5 + 1,
                        var3.getTicksUsingItem(),
                        w7.G(var0)
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.I : "";
   }

   @Override
   public void onEnable() {
      this.I = "Ready";
      this.S = null;
      mt.K("ShieldBreaker", "Enabled; swap back {}", this.N.P());
   }

   @Override
   public void onDisable() {
      if (this.D.k()) {
         this.D.W(getMc(), this.N.P());
      }

      this.S = null;
   }

   @Override
   public void onTick() {
      this.R();
      int var10000 = this.D.k();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.D.r(mc, this.N.P());
         int var6 = this.D.k();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.I = "Ready";
         }
      }
   }

   private void R() {
      Player var1 = this.S;
      if (var1 != null) {
         int var2 = var1.tickCount - this.a;
         if (var1.isRemoved() || var1.level() != mc.level) {
            this.S = null;
         } else if (!var1.isUsingItem()) {
            this.S = null;
            mt.U("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!w7.p(var1)) {
            this.S = null;
            this.I = "Retrying";
            mt.K("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
