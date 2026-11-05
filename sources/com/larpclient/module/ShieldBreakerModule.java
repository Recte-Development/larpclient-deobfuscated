package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.j5;
import com.larpclient.o_;
import com.larpclient.r4;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule X;
   private final ln Y = this.addSetting(new ln("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true));
   private final o_ W = new o_(this);
   private String y = "Ready";
   private Player I;
   private int S;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      X = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void H(LocalPlayer var0, Entity var1) {
      if (X != null && X.isEnabled()) {
         ShieldBreakerModule var2 = X;
         if (var2 != null && var2.isRuntimeActive() && !d8.E() && var1 instanceof Player var3) {
            if (var0 == getMc().player && !var2.W.K() && j5.X(var3, var0) && !j5.F(var3)) {
               MaceModule var4 = MaceModule.Q();
               if (var4 != null && var4.Y(var0, var3)) {
                  var2.y = "Stun Slam by Auto Mace";
               } else {
                  int var5 = j5.X(var0);
                  if (var5 < 0) {
                     var2.y = "No axe in hotbar";
                  } else if (!var2.W.s(var0, var5)) {
                     var2.y = "Hotbar busy";
                  } else {
                     j5.h(var3);
                     var2.I = var3;
                     var2.S = var3.tickCount;
                     var2.y = "Shield break hit";
                     r4.g(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.getId(),
                        var5 + 1,
                        var3.getTicksUsingItem(),
                        j5.H(var0)
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.y : "";
   }

   @Override
   public void onEnable() {
      this.y = "Ready";
      this.I = null;
      r4.V("ShieldBreaker", "Enabled; swap back {}", this.Y.getValue());
   }

   @Override
   public void onDisable() {
      if (this.W.K()) {
         this.W.m(getMc(), this.Y.getValue());
      }

      this.I = null;
   }

   @Override
   public void onTick() {
      this.q();
      int var10000 = this.W.K();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.W.C(mc, this.Y.getValue());
         int var6 = this.W.K();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.y = "Ready";
         }
      }
   }

   private void q() {
      Player var1 = this.I;
      if (var1 != null) {
         int var2 = var1.tickCount - this.S;
         if (var1.isRemoved() || var1.level() != mc.level) {
            this.I = null;
         } else if (!var1.isUsingItem()) {
            this.I = null;
            r4.g("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!j5.F(var1)) {
            this.I = null;
            this.y = "Retrying";
            r4.V("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
