package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.ew;
import com.larpclient.fs;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.ug;
import com.larpclient.w6;
import com.larpclient.wp;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1536;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class RodSwapModule extends Module {
   private static RodSwapModule p;
   private final li E = this.addSetting(
      new li("Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel")
   );
   private final lo H = this.addSetting(
      new lo("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final lo C = this.addSetting(new lo("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0));
   private final lo o = this.addSetting(new lo("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0));
   private static final int u = 20;
   private static final long X = 250000000L;
   private static final int P = 20;
   private static final double t = 0.5;
   private final fs i = new fs(this);
   private final ew h = new ew();
   private final ew.e N = new ew.e();
   private String s;
   private long Z;
   private class_746 j;
   private class_638 A;
   private class_1657 k;
   private class_1657 L;
   private class_1536 c;
   private class_1799 F = class_1799.field_8037;
   private int n = -1;
   private int D = -1;
   private int w;
   private String U = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      p = this;
      this.E.a(var1 -> this.G("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<lb<?>> f() {
      return List.of(this.E);
   }

   public List<lb<?>> y() {
      return List.of(this.H, this.C, this.o);
   }

   public static void O() {
      if (p != null && p.isRuntimeActive()) {
         p.G("Manual slot selection", false);
         p.h.g(System.nanoTime());
      }
   }

   public static boolean S() {
      return p != null && p.isEnabled() ? p != null && p.isRuntimeActive() && p.h.C() && p.i.N() : false;
   }

   public static void H() {
      if (S()) {
         p.G("Your attack", true);
      }
   }

   public static double r() {
      RodSwapModule var0 = p;
      return var0 != null && var0.isRuntimeActive() && var0.j != null && var0.x() >= 0 ? var0.H.getValue() : 0.0;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.U : "";
   }

   @Override
   public void onEnable() {
      this.j = getMc().field_1724;
      this.A = mc.field_1687;
      this.h.l();
      this.N.n();
      this.W();
      this.U = "Ready";
      this.s = null;
      q8.G(
         "RodSwap",
         "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms",
         this.E.getValue(),
         this.H.getValue(),
         this.C.getValue().longValue(),
         this.o.getValue().longValue()
      );
   }

   @Override
   public void onDisable() {
      this.G("Disabled or disconnected", true);
      this.W();
      this.j = null;
      this.A = null;
   }

   private boolean d() {
      class_310 var1 = getMc();
      return this.j != null
         && this.j == var1.field_1724
         && this.A != null
         && this.A == var1.field_1687
         && (bn.q(var1) || bn.x(var1, wp.z.L))
         && !this.j.method_7325()
         && this.j.method_5805();
   }

   private boolean w() {
      for (class_304 var4 : mc.field_1690.field_1852) {
         if (bn.M(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean b() {
      if (this.j != null && this.n >= 0 && this.n < 9) {
         class_1799 var1 = this.j.method_31548().method_5438(this.n);
         return !var1.method_31574(class_1802.field_8378) && class_1799.method_31577(this.F, var1);
      } else {
         return false;
      }
   }

   private boolean d(int var1) {
      if (this.j != null && this.A != null && var1 >= 0 && var1 < 9) {
         class_1799 var2 = this.j.method_31548().method_5438(var1);
         return var2.method_31574(class_1802.field_8378)
            && !var2.method_63692()
            && var2.method_45435(this.A.method_45162())
            && !this.j.method_7357().method_7904(var2);
      } else {
         return false;
      }
   }

   private int x() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.d(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean L(class_1297 var1) {
      return var1 instanceof class_1657 var2 && bn.K(this.j, var2) && !this.j.method_5722(var2) && var2.method_5863();
   }

   private static class_243 s(class_1297 var0) {
      return var0.method_73189().method_1023(var0.field_6014, var0.field_6036, var0.field_5969);
   }

   private double v(class_1657 var1) {
      return ug.L(this.j.method_33571(), var1.method_5829());
   }

   private class_1297 V() {
      com.larpclient.b.k var1 = com.larpclient.b.K(this.j.method_33571(), this.j.method_36454(), this.j.method_36455());
      List var2 = this.A
         .method_8333(
            this.j,
            this.j.method_5829().method_1014(this.H.getValue() + 4.0),
            var1x -> var1x.method_5805() && !var1x.method_7325() && var1x.method_49108() && var1x != this.j.method_5854()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         class_243 var4 = var1.position();
         class_243 var5 = var1.collisionEnd();
         class_3965 var6 = this.A.method_17742(new class_3959(var4, var5, class_3960.field_17558, class_242.field_1347, this.j));
         double var7 = var6.method_17783() == class_240.field_1333 ? Double.POSITIVE_INFINITY : var4.method_1025(var6.method_17784());
         class_1297 var9 = null;

         for (class_1297 var11 : var2) {
            class_238 var12 = var11.method_5829().method_997(com.larpclient.b.K(s(var11), var3)).method_1014(com.larpclient.b.z(var3));
            double var13 = com.larpclient.b.g(var4, var5, var12);
            if (var13 < var7) {
               var7 = var13;
               var9 = var11;
            }
         }

         if (var9 != null || var6.method_17783() != class_240.field_1333) {
            return var9;
         }

         var1 = var1.next();
      }

      return null;
   }

   private String J(class_1657 var1) {
      if (!var1.method_7325() && !var1.method_68878()) {
         if (this.j.field_3944.method_2871(var1.method_5667()) == null) {
            return "no player-list entry";
         } else if (this.j.method_5722(var1) || !this.j.method_7256(var1)) {
            return "teammate";
         } else {
            return var1.method_5863() && var1.method_5732() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void Q(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.s) && (this.s == null || var2 - this.Z >= 1000000000L)) {
         this.s = var1;
         this.Z = var2;
         q8.G("RodSwap", "Waiting: {}", var1);
      }
   }

   private String E(long var1) {
      if (this.j.method_6047().method_31574(class_1802.field_8378)) {
         return "Hold your weapon, not the rod";
      } else if (this.j.method_6079().method_31574(class_1802.field_8378)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.j.field_7513 != null) {
         return "Existing bobber";
      } else if (this.x() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.h.S(var1) || this.w()) {
         return "Manual slot selection";
      } else if (this.j.method_5799()) {
         return "In water";
      } else if (!bn.R(mc, this.j) || bn.x(mc) || bn.l(mc)) {
         return "Your hands are busy";
      } else if (!fs.o(this)) {
         return "Another module owns the hotbar";
      } else {
         return cr.V() ? "Critical hit pending" : null;
      }
   }

   private boolean x(long var1) {
      return this.isRuntimeActive()
         && this.d()
         && !this.h.C()
         && this.E(var1) == null
         && t9.k(t9.v.m)
         && !bn.w(this.j)
         && ((qf65e76ba6afee893)mc).larpUseDelay() <= 0;
   }

   public static class_243 b(class_1309 var0) {
      if (p != null && p.isEnabled()) {
         RodSwapModule var1 = p;
         return var1 == null ? null : var1.j(var0);
      } else {
         return null;
      }
   }

   private class_243 j(class_1309 var1) {
      if (this.isRuntimeActive() && !this.h.C() && var1 instanceof class_1657 var2 && this.d()) {
         long var3 = System.nanoTime();
         String var5 = this.W(var2, var3);
         if (var5 != null) {
            this.W();
            if (!var5.isEmpty()) {
               this.Q(var5);
            }

            return null;
         } else {
            com.larpclient.b.m var6 = this.T(var2);
            if (var6 == null) {
               this.W();
               this.Q("No rod arc reaches " + var2.method_5477().getString());
               return null;
            }

            if (this.L != var2) {
               this.L = var2;
               this.w = 0;
            }

            if (++this.w > 20) {
               q8.G("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.method_5477().getString(), 20);
               this.N.n(var3, 250000000L);
               this.W();
               return null;
            } else {
               this.U = "Aiming";
               class_243 var7 = this.j.method_33571();
               double var8 = Math.max(1.0, var7.method_1022(var2.method_5829().method_1005()));
               return var7.method_1019(class_243.method_1030(var6.pitch(), var6.yaw()).method_1021(var8));
            }
         }
      } else {
         this.W();
         return null;
      }
   }

   private String W(class_1657 var1, long var2) {
      if (!this.N.E(var2)) {
         this.U = "Delay";
         return "";
      }

      if (!this.L(var1)) {
         return "Kill Aura target " + var1.method_5477().getString() + " ignored: " + this.J(var1);
      }

      AuraModule var4 = AuraModule.R();
      if (var4 == null) {
         return "";
      }

      double var5 = this.v(var1);
      if (var5 <= var4.D() + 0.5) {
         this.U = "Inside sword reach";
         return "";
      }

      if (var5 > this.H.getValue()) {
         this.U = "Beyond Range";
         return "";
      }

      String var7 = this.E(var2);
      if (var7 != null) {
         this.U = var7;
      }

      return var7;
   }

   private com.larpclient.b.m T(class_1657 var1) {
      class_243 var2 = this.j.method_33571();
      class_243 var3 = var1.method_5829().method_1005();
      class_243 var4 = s(var1);
      com.larpclient.b.m var5 = com.larpclient.b.d(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = com.larpclient.b.d(var2, var3.method_1019(com.larpclient.b.K(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void W() {
      this.L = null;
      this.w = 0;
   }

   public void L() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.j != getMc().field_1724 || this.A != mc.field_1687) {
               this.G("World changed", false);
               this.N.n();
               this.W();
               this.j = mc.field_1724;
               this.A = mc.field_1687;
            }

            if (!this.d()) {
               this.G("Paused", true);
            } else if (this.w()) {
               this.G("Manual slot selection", false);
               this.h.g(System.nanoTime());
            } else if (this.h.C()) {
               if (!this.i.N()) {
                  this.G("Slot ownership changed", false);
                  this.h.g(System.nanoTime());
               } else if (this.b()
                  && this.d(this.D)
                  && fs.o(this)
                  && bn.R(mc, this.j)
                  && !bn.l(mc)
                  && !bn.x(mc)
                  && (!mc.field_1690.field_1886.method_1434() || mc.field_1765 == null || mc.field_1765.method_17783() != class_240.field_1332)) {
                  if (!this.L(this.k)) {
                     this.G("Target unavailable", true);
                  } else {
                     class_1536 var1 = this.j.field_7513;
                     if (var1 == null || var1.method_6947() == this.j && var1.method_73183() == this.A && !var1.method_31481()) {
                        if (this.c != null && var1 != null && var1 != this.c) {
                           this.G("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.c == null) {
                              this.c = var1;
                              q8.v("RodSwap", "Owned bobber observed; entity={}", this.c.method_5628());
                           }

                           class_1297 var2 = var1 == null ? null : var1.method_26957();
                           int var3 = var1 != null
                                 && var1.field_6012 > 2
                                 && var1.method_73189().method_1020(this.k.method_5829().method_1005()).method_1026(var1.method_18798()) > 0.0
                                 && var1.method_5858(this.k) > 2.25
                              ? 1
                              : 0;
                           ew.j var4 = this.h
                              .H(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.k,
                                 var2 != null && var2 != this.k,
                                 var1 != null && var1.method_24828(),
                                 (boolean)var3
                              );
                           if (var4 == ew.j.r) {
                              this.G(var2 == this.k ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == ew.j.L) {
                              this.U = "Player hooked";
                           }
                        }
                     } else {
                        this.G("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.G("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.d();
      boolean var7 = false;
      byte var6 = (byte)var10000;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.G("Paused", true);
      } else {
         int var17 = this.h.C();
         boolean var9 = false;
         byte var8 = (byte)var17;
         var17 = Integer.signum(var8);
         if (((var17 | -var17) >>> 31 ^ 1) == 0) {
            if (this.h.E() == ew.h.E) {
               cr.y(this, this::m, this::Q);
            }
         } else {
            AuraModule var1 = AuraModule.R();
            if (var1 != null && var1.isRuntimeActive()) {
               class_1309 var3 = var1.B();
               int var19 = var3 instanceof class_1657;
               boolean var11 = false;
               byte var10 = (byte)var19;
               var19 = Integer.signum(var10);
               if (((var19 | -var19) >>> 31 ^ 1) == 0) {
                  class_1657 var2 = (class_1657)var3;
                  if (this.L == var2) {
                     long var14 = System.nanoTime();
                     if (!this.x(var14)) {
                        String var15 = this.E(var14);
                        this.U = var15 == null ? "Aiming" : var15;
                     } else if (this.V() != var2) {
                        this.U = "Aiming";
                     } else {
                        int var5 = this.x();
                        cr.y(
                           this,
                           () -> this.c(var2, var5),
                           () -> this.x(System.nanoTime()) && this.d(var5) && this.L(var2) && this.L == var2 && var1.B() == var2 && this.V() == var2
                        );
                     }
                  }
               } else {
                  this.W();
                  int var21 = this.U.equals("Delay");
                  boolean var13 = false;
                  byte var12 = (byte)var21;
                  var21 = Integer.signum(var12);
                  if ((var21 | -var21) >>> 31 == 0) {
                     this.U = "No player target";
                  }
               }
            } else {
               this.W();
               this.U = "Enable Kill Aura";
            }
         }
      }
   }

   private void c(class_1657 var1, int var2) {
      this.n = this.j.method_31548().method_67532();
      this.F = this.j.method_6047().method_7972();
      this.D = var2;
      this.W();
      if (!this.i.m(this.j, var2)) {
         this.N.n(System.nanoTime(), 250000000L);
         this.G("Hotbar busy", false);
      } else {
         this.k = var1;
         this.c = null;
         this.h.B(System.nanoTime(), this.o.getValue().longValue() * 1000000L, this.E.G("Hook and Reel"));
         boolean var3 = mc.field_1761.method_2919(this.j, class_1268.field_5808).method_23665();
         if (!var3) {
            this.G("Throw rejected locally", true);
         } else {
            w6.H(this.j, class_1268.field_5808);
            ((qf65e76ba6afee893)mc).larpUseDelay(4);
            this.U = "Thrown";
            this.s = null;
            q8.G(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.E.getValue(),
               var2 + 1,
               this.n + 1,
               this.k.method_5477().getString(),
               String.format("%.2f", this.v(this.k)),
               String.format("%.1f", this.j.method_36454()),
               String.format("%.1f", this.j.method_36455())
            );
         }
      }
   }

   private boolean Q() {
      return this.isRuntimeActive()
         && this.d()
         && this.h.E() == ew.h.E
         && this.i.N()
         && this.b()
         && this.d(this.D)
         && fs.o(this)
         && bn.R(mc, this.j)
         && !bn.l(mc)
         && !bn.x(mc)
         && !bn.w(this.j)
         && ((qf65e76ba6afee893)mc).larpUseDelay() <= 0
         && this.c != null
         && this.j.field_7513 == this.c
         && this.c.method_26957() == this.k
         && this.L(this.k);
   }

   private void m() {
      if (this.Q()) {
         this.h.N();
         if (!mc.field_1761.method_2919(this.j, class_1268.field_5808).method_23665()) {
            this.G("Reel rejected locally", true);
         } else {
            w6.H(this.j, class_1268.field_5808);
            ((qf65e76ba6afee893)mc).larpUseDelay(4);
            this.U = "Reeled";
            q8.v("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.k.method_5628());
         }
      }
   }

   private void G(String var1, boolean var2) {
      cr.p(this);
      int var3 = !this.h.C() && !this.i.I() ? 0 : 1;
      ew.h var4 = this.h.E();
      int var5 = this.j != null && this.j == getMc().field_1724 && this.A == mc.field_1687 && this.j.method_5805() ? 1 : 0;
      int var6 = var5 != 0 ? this.j.method_31548().method_67532() : -1;
      this.i.o(getMc(), var2 && var5 != 0 && this.b());
      if (var5 != 0 && mc.field_1761 != null && var6 != this.j.method_31548().method_67532()) {
         ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.N.S(System.nanoTime(), this.C.getValue().longValue() * 1000000L);
      }

      this.h.l();
      this.k = null;
      this.c = null;
      this.F = class_1799.field_8037;
      this.n = -1;
      this.D = -1;
      this.U = var1;
      if (var3 != 0) {
         q8.G("RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.j.method_31548().method_67532());
      }
   }
}
