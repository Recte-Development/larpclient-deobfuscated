package com.larpclient.module;

import com.larpclient.hu;
import com.larpclient.si;
import com.larpclient.ta;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule b;
   private final s H = this.addSetting(new s("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true));
   private final hu s = new hu(this);
   private String l = "Ready";
   private Player Z;
   private int e;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      b = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void Z(LocalPlayer var0, Entity var1) {
      if (b != null && b.isEnabled()) {
         ShieldBreakerModule var2 = b;
         if (var2 != null && var2.isRuntimeActive() && !ta.U() && var1 instanceof Player var3) {
            if (var0 == getMc().player && !var2.s.p() && si.k(var3, var0) && !si.x(var3)) {
               MaceModule var4 = MaceModule.W();
               if (var4 != null && var4.M(var0, var3)) {
                  var2.l = "Stun Slam by Auto Mace";
               } else {
                  int var5 = si.E(var0);
                  if (var5 < 0) {
                     var2.l = "No axe in hotbar";
                  } else if (!var2.s.O(var0, var5)) {
                     var2.l = "Hotbar busy";
                  } else {
                     si.V(var3);
                     var2.Z = var3;
                     var2.e = var3.tickCount;
                     var2.l = "Shield break hit";
                     com.larpclient.h.U(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.getId(),
                        var5 + 1,
                        var3.getTicksUsingItem(),
                        si.Y(var0)
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.l : "";
   }

   @Override
   public void onEnable() {
      this.l = "Ready";
      this.Z = null;
      com.larpclient.h.A("ShieldBreaker", "Enabled; swap back {}", this.H.getValue());
   }

   @Override
   public void onDisable() {
      if (this.s.p()) {
         this.s.V(getMc(), this.H.getValue());
      }

      this.Z = null;
   }

   @Override
   public void onTick() {
      this.f();
      int var10000 = this.s.p();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.s.J(mc, this.H.getValue());
         int var6 = this.s.p();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.l = "Ready";
         }
      }
   }

   private void f() {
      Player var1 = this.Z;
      if (var1 != null) {
         int var2 = var1.tickCount - this.e;
         if (var1.isRemoved() || var1.level() != mc.level) {
            this.Z = null;
         } else if (!var1.isUsingItem()) {
            this.Z = null;
            com.larpclient.h.U("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!si.x(var1)) {
            this.Z = null;
            this.l = "Retrying";
            com.larpclient.h.A("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
