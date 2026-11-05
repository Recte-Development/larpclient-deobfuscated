package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public final class l6 {
   private static final l6 n = new l6();
   private final List<l6.a> H = new ArrayList<>();
   private l6.e u = l6.e.K;
   private boolean t;
   private boolean y;
   private long Q = Long.MIN_VALUE;
   private int[] c = com.larpclient.a.y();
   private boolean r;
   private boolean Z;
   private boolean G;

   private l6() {
   }

   public static l6 E() {
      return n;
   }

   public void r(l6.a var1) {
      if (!this.H.contains(var1)) {
         this.H.add(var1);
      }
   }

   public synchronized void Y() {
      if (!this.y) {
         this.y = (boolean)1;
         com.larpclient.a.r(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> com.larpclient.a.p(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.level != null) {
               f2.f().n(var0);
               yg.F(var0);
               yg.T(var0);
            }
         });
         r4.V("Rig", "Casino game support ready");
      }
   }

   public l6.e X() {
      return this.t ? this.u : l6.e.K;
   }

   public boolean c() {
      return this.t && !f9.W().Y();
   }

   public int[] c() {
      return (int[])this.c.clone();
   }

   public void L(int[] var1) {
      this.c = com.larpclient.a.c(var1);
   }

   public void c(l6.e var1) {
      if (var1 == null || var1 == l6.e.K) {
         this.f();
      } else if (this.u != var1 || !this.t) {
         l6.e var2 = this.X();
         this.u = var1;
         this.t = (boolean)1;
         this.H();
         r4.V("Rig", "Active game: {} (was {})", var1.S, var2.S);
         this.Q();
      }
   }

   public void f() {
      boolean var1 = this.t;
      this.t = (boolean)0;
      this.H();
      this.j();
      if (var1) {
         r4.V("Rig", "Active game released: {}", this.u.S);
      }

      this.u = l6.e.K;
   }

   public void m() {
      yg.A();
      this.f();
      ug.w().clearQueue();
      um.T().clearQueue();
      eg.y().C(false);
      r4.V("Rig", "All games stopped");
      this.Q();
   }

   private void Q() {
      for (l6.a var2 : List.copyOf(this.H)) {
         var2.Y(this.X());
      }
   }

   private void j() {
      this.Z = (boolean)0;
      this.G = (boolean)0;
      yg.A();
      ug.w().clearQueue();
      um.T().clearQueue();
      ue.V().clearQueue();
      u3.H().clearQueue();
      uu.M().clearQueue();
      js.A().o = (boolean)0;
   }

   private void v(l6.e var1) {
      ro.H().X(var1 == l6.e.k);
      mm.W().G(var1 == l6.e.L);
      hk.H.U(var1 == l6.e.s);
      hk.T.U(var1 == l6.e.w);
      oi.A().D = (boolean)(var1 == l6.e.H ? 1 : 0);
      xa.r().m = (boolean)(var1 == l6.e.E ? 1 : 0);
      ui.C().O = (boolean)(var1 == l6.e.F ? 1 : 0);
      _6.G().n = (boolean)(var1 == l6.e.Q ? 1 : 0);
      ft.V().x = (boolean)(var1 == l6.e.y ? 1 : 0);
      hm.O().T = (boolean)(var1 == l6.e.h ? 1 : 0);
      l_.F().j = (boolean)(var1 == l6.e.W ? 1 : 0);
      f2.f().p = (boolean)(var1 == l6.e.e ? 1 : 0);
      js var2 = js.A();
      var2.P = (boolean)(var1 == l6.e.v ? 1 : 0);
      if (!var2.P) {
         var2.o = (boolean)0;
      }
   }

   private void H() {
      this.v(this.t && !this.Z ? this.u : l6.e.K);
   }

   public static boolean n() {
      l6 var0 = n;
      return !var0.y ? false : ug.x().isSwapBusy() || ro.H().I() || mm.W().x() || eg.y().k();
   }

   public static void G() {
      l6 var0 = n;
      if (var0.y && n()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null && var1.level != null) {
            var0.f(var1);
         }
      }
   }

   public boolean G() {
      return this.Z;
   }

   public void f(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         long var2 = var1.level.getGameTime() * 31L + System.identityHashCode(var1.level);
         if (var2 != this.Q) {
            this.Q = var2;
            this.r = (boolean)1;
            f9.W().T(var1);
            this.u();
            eg.y().h(var1);
            this.d(var1);
         }
      } else {
         this.t();
      }
   }

   public void t() {
      if (this.r) {
         this.r = (boolean)0;
         this.Q = Long.MIN_VALUE;
         this.j();
         yg.A();
         ug.w().clearQueue();
         oi.A().B(null);
         r4.V("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void u() {
      boolean var1 = f9.W().Y();
      if (var1 && this.t && !this.Z) {
         this.Z = (boolean)1;
         this.G = eg.y().n;
         eg.y().C(false);
         yg.A();
         ug.w().clearQueue();
         um.T().clearQueue();
         this.H();
         r4.V("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.Z) {
         this.Z = (boolean)0;
         this.H();
         if (this.G) {
            eg.y().C(true);
         }

         this.G = (boolean)0;
         r4.V("Rig", "Games resumed");
      }
   }

   private void d(Minecraft var1) {
      hk.H.l(var1);
      hk.T.l(var1);
      oi.A().B(var1);
      xa.r().Z(var1);
      js.A().J(var1);
      f9 var2 = f9.W();
      int var3 = this.c() && ui.C().O ? 1 : 0;
      int var4 = this.c() && mm.W().I() ? 1 : 0;
      ug var5 = ug.w();
      mz.F().L((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      mz.F().l(var1, var5, var3 != 0 || var2.f(f9.b.E) || var4 != 0 && var2.f(f9.b.P));
      _6.G().l(var1);
      ft.V().g(var1);
      ro.H().D(var1);
      mm.W().B(var1);
      hm.O().Y(var1);
      jf.u().E(var1, u3.H(), hm.O().T || var2.f(f9.b.L) || var2.f(f9.b.z));
      l_.F().p(var1);
      li.D().U(var1, uu.M(), l_.F().j || var2.f(f9.b.c));
   }

   public String d(l6.e var1) {
      switch (var1) {
         case H:
            oi var10 = oi.A();
            this.c(l6.e.H);
            var10.M();
            break;
         case E:
            xa var9 = xa.r();
            if (!var9.m) {
               var9.Y = xa.i.y;
               this.c(l6.e.E);
            } else if (var9.Y == xa.i.y) {
               var9.Y = xa.i.Y;
            } else if (var9.Y == xa.i.Y) {
               var9.Y = xa.i.J;
            } else {
               var9.Y = xa.i.y;
            }
            break;
         case k:
            ro var8 = ro.H();
            if (!var8.z()) {
               var8.L(1);
               this.c(l6.e.k);
            } else if (var8.W() == 1) {
               var8.L(2);
            } else {
               this.f();
            }
            break;
         case F:
            this.c(l6.e.F);
            mz.F().J();
            break;
         case L:
            this.c(l6.e.L);
            break;
         case Q:
            ue var7 = ue.V();
            if (!_6.G().n) {
               var7.X(0);
               this.c(l6.e.Q);
            } else {
               var7.G();
               var7.clearQueue();
            }
            break;
         case y:
            um var6 = um.T();
            if (ft.V().x && var6.V() != um.e.R) {
               var6.I(var6.V() == um.e.D ? "lower" : "higher");
            } else {
               var6.I("higher");
               this.c(l6.e.y);
            }
            break;
         case h:
            this.c(l6.e.h);
            jf var5 = jf.u();
            var5.Y("player".equals(var5.a()) ? "banker" : "player", u3.H());
            break;
         case W:
            this.c(l6.e.W);
            li.D().Q(uu.M());
            break;
         case v:
            js var4 = js.A();
            this.c(l6.e.v);
            if (var4.o) {
               var4.o = (boolean)0;
            } else {
               var4.C();
            }
            break;
         case e:
            f2 var3 = f2.f();
            if (var3.p && !"off".equals(var3.v())) {
               var3.X("win".equals(var3.v()) ? "lose" : "win");
            } else {
               var3.X("win");
               this.c(l6.e.e);
            }
            break;
         case s:
         case w:
            int var2 = var1 == l6.e.s ? 1 : 0;
            this.e((boolean)var2, (var2 != 0 ? hk.H : hk.T).T());
      }

      return this.z(var1);
   }

   public void e(boolean var1, String var2) {
      hk var3 = var1 ? hk.H : hk.T;
      if ("off".equalsIgnoreCase(var2)) {
         this.f();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.m()) {
               return;
            }

            this.c(var1 ? l6.e.s : l6.e.w);
            var3.X(var4);
            var3.U(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String z(l6.e var1) {
      return switch (var1) {
         case H -> oi.A().J();
         case E -> xa.r().Y.Q;
         case k -> ro.H().W() == 1 ? "First" : "Second";
         case F -> mz.F().e();
         case L -> (mz.F().n() ? "Auto: " : "") + mz.F().e();
         case Q -> ue.V().Q();
         case y -> n(um.T().J());
         case h -> n(jf.u().a());
         case W -> n(li.D().q()) + " draws";
         case v -> js.A().o ? "Armed" : "Normal";
         case e -> f2.f().w();
         case s -> hk.H.Z(hk.H.A());
         case w -> hk.T.X(hk.T.A());
         default -> "";
      };
   }

   private static String n(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean S() {
      return ui.C().O || _6.G().n || ft.V().x || hm.O().T || l_.F().j;
   }

   public ug b() {
      if (ft.V().x) {
         return um.T();
      } else if (_6.G().n) {
         return ue.V();
      } else if (hm.O().T) {
         return u3.H();
      } else {
         return l_.F().j ? uu.M() : ug.w();
      }
   }

   public boolean q(int var1, int[] var2) {
      return hm.O().T && var2 != null && var2.length != 0 ? jf.u().N(u3.H(), var2) : false;
   }

   public ItemStack I(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = hk.G().B(var1);
         ItemStack var2 = ug.x().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = xa.r().I(var1);
         return js.A().T(var1);
      } else {
         return var1;
      }
   }

   public l6.x u(l6.e var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3 != null && !var3.isEmpty()) {
            Item var4 = var3.getItem();
            switch (var1) {
               case H:
                  return new l6.x(false, "Paper learns items from its assigned dispensers");
               case E:
                  xa var6 = xa.r();
                  switch (var6.Y) {
                     case y:
                        var6.c = var4;
                        break;
                     case Y:
                        var6.N = var4;
                        break;
                     case J:
                        var6.b = var4;
                  }

                  this.h();
                  return new l6.x(true, "45/45/10: " + var6.Y.Q + " item set to " + var3.getHoverName().getString());
               case k:
               default:
                  return new l6.x(false, "From Hand is unavailable for " + var1.S);
               case F:
               case L:
                  return new l6.x(false, "Blackjack imports from its dispenser or hotbar maps");
               case Q:
               case h:
                  return new l6.x(false, var1.S + " imports from its dispenser maps");
               case y:
               case W:
                  return new l6.x(false, var1.S + " imports from its assigned dispenser");
               case v:
                  js var5 = js.A();
                  if (var5.o) {
                     var5.E = var4;
                     this.h();
                     return new l6.x(true, "Russian Roulette: trigger item set to " + var3.getHoverName().getString());
                  }

                  var5.Z = var4;
                  this.h();
                  return new l6.x(true, "Russian Roulette: normal item set to " + var3.getHoverName().getString() + " (arm first to set the trigger item)");
               case e:
                  return new l6.x(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new l6.x(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new l6.x(false, "From Hand failed: no player");
      }
   }

   public void h() {
      com.larpclient.a.p(this);
   }

   public interface a {
      void Y(l6.e var1);
   }

   public enum e {
      H("Paper"),
      E("45/45/10"),
      k("50/50"),
      F("Blackjack"),
      L("Blackjack Force Only"),
      Q("Even or Odd"),
      y("Higher or Lower"),
      h("Baccarat"),
      W("Mexican Jack"),
      v("Russian Roulette"),
      e("Arrow Game"),
      s("Horse Racing"),
      w("Armor Game"),
      K("Off");

      public final String S;

      e(String var3) {
         this.S = var3;
      }
   }

   public static final class x {
      public final boolean g;
      public final String S;

      public x(boolean var1, String var2) {
         this.g = var1;
         this.S = var2;
      }
   }
}
