package com.larpclient.module;

import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_746;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule q;
   private final l3 i = this.addSetting(new l3("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true));
   private final fs J = new fs(this);
   private String t = "Ready";
   private class_1657 a;
   private int p;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      q = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void x(class_746 var0, class_1297 var1) {
      if (q != null && q.isEnabled()) {
         ShieldBreakerModule var2 = q;
         if (var2 != null && var2.isRuntimeActive() && !cr.C() && var1 instanceof class_1657 var3) {
            if (var0 == getMc().field_1724 && !var2.J.I() && bn.B(var3, var0) && !bn.G(var3)) {
               MaceModule var4 = MaceModule.c();
               if (var4 != null && var4.Z(var0, var3)) {
                  var2.t = "Stun Slam by Auto Mace";
               } else {
                  int var5 = bn.d(var0);
                  if (var5 < 0) {
                     var2.t = "No axe in hotbar";
                  } else if (!var2.J.m(var0, var5)) {
                     var2.t = "Hotbar busy";
                  } else {
                     bn.f(var3);
                     var2.a = var3;
                     var2.p = var3.field_6012;
                     var2.t = "Shield break hit";
                     q8.v(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.method_5628(),
                        var5 + 1,
                        var3.method_6048(),
                        bn.Z(var0)
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.t : "";
   }

   @Override
   public void onEnable() {
      this.t = "Ready";
      this.a = null;
      q8.G("ShieldBreaker", "Enabled; swap back {}", this.i.getValue());
   }

   @Override
   public void onDisable() {
      if (this.J.I()) {
         this.J.o(getMc(), this.i.getValue());
      }

      this.a = null;
   }

   @Override
   public void onTick() {
      this.m();
      int var10000 = this.J.I();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.J.u(mc, this.i.getValue());
         int var6 = this.J.I();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.t = "Ready";
         }
      }
   }

   private void m() {
      class_1657 var1 = this.a;
      if (var1 != null) {
         int var2 = var1.field_6012 - this.p;
         if (var1.method_31481() || var1.method_73183() != mc.field_1687) {
            this.a = null;
         } else if (!var1.method_6115()) {
            this.a = null;
            q8.v("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.method_5628(), var2);
         } else if (!bn.G(var1)) {
            this.a = null;
            this.t = "Retrying";
            q8.G("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.method_5628(), var2);
         }
      }
   }
}
