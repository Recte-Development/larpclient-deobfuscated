package com.larpclient.module;

import com.larpclient.jz;
import com.larpclient.q8;
import io.netty.channel.ChannelFutureListener;
import java.util.Set;
import net.minecraft.class_243;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2827;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_2851;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_6374;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_8590;
import net.minecraft.class_9836;

public final class FakeLagModule extends Module {
   private static FakeLagModule P;
   public static final int g = 256;
   private static final int j = 10;
   private static final double T = 1.0E-6;
   private final lo c = this.addSetting(new lo("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0));
   private final l3 R = this.addSetting(new l3("Grim Compatibility", "Hold ping and chunk replies with movement. Off flags on Grim", false));
   private final jz E = new jz(256);
   private class_634 u;
   private class_638 n;
   private class_746 w;
   private int G;
   private boolean i;
   private long k;
   private static class_746 a;
   private static class_638 U;
   private static class_634 I;
   private boolean Y;
   private int o;
   private boolean v;
   private boolean C;
   private int r;
   private int d;
   private int L;
   private boolean H;

   public FakeLagModule() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      P = this;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (
            this.c.getValue() == 0.0
               ? "No delay"
               : (this.C ? "Recovery: stand still" : (this.o < 2 ? "Settling" : (this.Y ? "Resyncing" : this.E.C() + " held")))
         );
   }

   public static boolean X() {
      return P != null && P.isEnabled() ? P != null && P.isRuntimeActive() : false;
   }

   public static boolean j(class_2596<?> var0) {
      return var0 instanceof class_2828 || var0 instanceof class_2851 || var0 instanceof class_2848;
   }

   private static boolean O(class_2596<?> var0) {
      return var0 instanceof class_6374 || var0 instanceof class_8590;
   }

   public static boolean M(class_2596<?> var0) {
      return !j(var0) && !(var0 instanceof class_9836) && !(var0 instanceof class_2827) && !O(var0);
   }

   @Override
   public void onEnable() {
      this.E.x();
      this.d = 0;
      this.v = (boolean)0;
      this.H = (boolean)0;
      this.L = 0;
      this.C = (boolean)0;
      this.r = 0;
      this.u = mc.method_1562();
      this.n = mc.field_1687;
      this.w = mc.field_1724;
      this.G = this.c.getValue().intValue();
      this.i = this.R.getValue();
      this.o = 2;
      this.Y = (boolean)(a == this.w && U == this.n && I == this.u ? 1 : 0);
      q8.G("FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.G, this.i, 256);
   }

   private boolean g() {
      return this.u != null && mc != null && mc.method_1562() == this.u && this.u.method_48296().method_10758();
   }

   private boolean P() {
      return this.g() && mc.field_1687 == this.n && mc.field_1724 == this.w && this.w != null && this.w.method_5805();
   }

   @Override
   public void onDisable() {
      try {
         if (this.P()) {
            int var1 = this.E.C();
            int var2 = this.d;
            this.H("Stopped; preserve live trajectory");
            if (var1 > 0) {
               q8.G("FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2);
            }

            if (this.L > 0) {
               q8.G("FakeLag", "Activation summary: {} early in-order releases at interactions, setting changes or capacity; no movement dropped", this.L);
            }
         } else {
            this.N("Stopped after session change");
         }
      } finally {
         if (!this.P() && a == this.w) {
            O();
         }

         this.E.x();
         this.d = 0;
         this.u = null;
         this.n = null;
         this.w = null;
         this.Y = (boolean)0;
         this.o = 0;
         this.C = (boolean)0;
         this.r = 0;
      }
   }

   @Override
   public void onTick() {
      if (!this.P()) {
         this.H("Player or world changed; retain live pong replies");
      } else {
         this.T();
         boolean var9 = false;
         byte var8 = this.C;
         int var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            class_243 var1 = this.w.method_18798();
            if (!this.w.method_24828() || var1.method_37268() > 1.0E-6) {
               this.r = 0;
               return;
            }

            byte var3 = 1;
            int var2 = this.r;
            var10000 = this.r = var2 + var3;
            byte var5 = 10;
            int var4 = var10000;
            if ((Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
               return;
            }

            this.C = (boolean)0;
            this.o = 0;
            q8.G("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.G);
         }

         this.o = Math.min(2, this.o + 1);
         this.E.M(System.nanoTime(), false, this::g);
         var10000 = this.E.C();
         boolean var7 = false;
         int var6 = var10000;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.d = 0;
         }
      }
   }

   private void T() {
      int var1 = this.c.getValue().intValue();
      boolean var2 = this.R.getValue();
      if (var1 != this.G || var2 != this.i) {
         this.c("Settings changed");
         this.G = var1;
         this.i = var2;
      }
   }

   private void H(String var1) {
      int var2 = this.E.C();
      this.E.M(System.nanoTime(), true, this::g);
      this.d = 0;
      if (var2 > 0) {
         q8.v("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void c(String var1) {
      if (this.E.C() != 0) {
         this.L++;
         if (!this.H) {
            this.H = (boolean)1;
            q8.G("FakeLag", "Released {} held packets in order before continuing ({}); movement is never dropped at these boundaries", this.E.C(), var1);
         }

         this.H(var1);
      }
   }

   private void N(String var1) {
      this.k++;
      if (this.d > 0 && this.P()) {
         a = this.w;
         U = this.n;
         I = this.u;
         this.Y = (boolean)1;
      }

      this.H(var1);
   }

   private static void O() {
      a = null;
      U = null;
      I = null;
   }

   public static void F() {
      O();
   }

   public static boolean h(class_746 var0) {
      if (a == null) {
         return false;
      } else if (a == var0 && getMc() != null && getMc().field_1724 == var0 && getMc().field_1687 == U && getMc().method_1562() == I) {
         O();
         return true;
      } else {
         O();
         return false;
      }
   }

   public static boolean P(class_2535 var0, class_2596<?> var1, ChannelFutureListener var2, boolean var3) {
      if (P != null && P.isEnabled()) {
         FakeLagModule var4 = P;
         class_310 var5 = getMc();
         if (var4 == null || var5 == null || !var5.method_18854() || !var4.isRuntimeActive() || var4.E.u()) {
            return false;
         }

         if (!var4.P()) {
            var4.H("Player or world changed; retain live pong replies");
            return false;
         }

         if (var0 != var4.u.method_48296()) {
            return false;
         }

         var4.T();
         long var6 = System.nanoTime();
         if (var4.C && var1 instanceof class_2828 var8 && !var8.method_12273()) {
            var4.r = 0;
         }

         if (!var4.C && var4.o >= 2 && !var4.Y) {
            var4.E.M(var6, false, var4::g);
            if (var4.E.C() == 0) {
               var4.d = 0;
            }

            int var11 = !(var1 instanceof class_9836) && (!var4.i || !O(var1)) ? 0 : 1;
            if (!j(var1) && var11 == 0) {
               if (M(var1)) {
                  var4.c(var1.getClass().getSimpleName());
               }

               return false;
            } else {
               if (var4.G == 0) {
                  return false;
               }

               long var9 = var4.k;
               if (!var4.E.k(var6, var4.G * 1000000L, () -> {
                  if (var1 instanceof class_2828) {
                     var4.d = Math.max(0, var4.d - 1);
                  }

                  if (O(var1) || var4.P() && (!(var1 instanceof class_2828) || var9 == var4.k)) {
                     var0.method_52906(var1, var2, var3);
                  }
               })) {
                  var4.c("Capacity reached");
                  return false;
               }

               if (var1 instanceof class_2828) {
                  var4.d++;
                  if (!var4.v) {
                     var4.v = (boolean)1;
                     q8.G("FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.G);
                  }
               }

               return true;
            }
         } else {
            if (var1 instanceof class_9836 && a == null) {
               var4.Y = (boolean)0;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static void e(class_746 var0) {
      FakeLagModule var1 = P;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.w) {
         int var2 = !var1.C ? 1 : 0;
         var1.C = (boolean)1;
         var1.r = 0;
         var1.o = 0;
         var1.N("Server position correction; discard rejected movement");
         if (var2 != 0) {
            q8.G(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.G,
               10
            );
         }
      }
   }
}
