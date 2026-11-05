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
public final class c3 {
   private static final c3 V = new c3();
   private final List<c3.d> v = new ArrayList<>();
   private c3.a A = c3.a.f;
   private boolean L;
   private boolean r;
   private long w = Long.MIN_VALUE;
   private int[] j = me.u();
   private boolean p;
   private boolean Y;
   private boolean a;

   private c3() {
   }

   public static c3 z() {
      return V;
   }

   public void k(c3.d var1) {
      if (!this.v.contains(var1)) {
         this.v.add(var1);
      }
   }

   public synchronized void j() {
      if (!this.r) {
         this.r = (boolean)1;
         me.b(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> me.j(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.level != null) {
               z.v().I(var0);
               w5.A(var0);
               w5.T(var0);
            }
         });
         mt.K("Rig", "Casino game support ready");
      }
   }

   public c3.a h() {
      return this.L ? this.A : c3.a.f;
   }

   public boolean W() {
      return this.L && !ep.H().O();
   }

   public int[] U() {
      return (int[])this.j.clone();
   }

   public void V(int[] var1) {
      this.j = me.Z(var1);
   }

   public void S(c3.a var1) {
      if (var1 == null || var1 == c3.a.f) {
         this.D();
      } else if (this.A != var1 || !this.L) {
         c3.a var2 = this.h();
         this.A = var1;
         this.L = (boolean)1;
         this.h();
         mt.K("Rig", "Active game: {} (was {})", var1.c, var2.c);
         this.A();
      }
   }

   public void D() {
      boolean var1 = this.L;
      this.L = (boolean)0;
      this.h();
      this.v();
      if (var1) {
         mt.K("Rig", "Active game released: {}", this.A.c);
      }

      this.A = c3.a.f;
   }

   public void c() {
      w5.d();
      this.D();
      ze.V().clearQueue();
      z8.l().clearQueue();
      pj.U().K(false);
      mt.K("Rig", "All games stopped");
      this.A();
   }

   private void A() {
      for (c3.d var2 : List.copyOf(this.v)) {
         var2.T(this.h());
      }
   }

   private void v() {
      this.Y = (boolean)0;
      this.a = (boolean)0;
      w5.d();
      ze.V().clearQueue();
      z8.l().clearQueue();
      zu.v().clearQueue();
      z6.u().clearQueue();
      zw.K().clearQueue();
      _u.O().J = (boolean)0;
   }

   private void h(c3.a var1) {
      c6.K().H(var1 == c3.a.R);
      kq.I().s(var1 == c3.a.r);
      _g.G.m(var1 == c3.a.l);
      _g.Y.m(var1 == c3.a.Z);
      mi.L().p = (boolean)(var1 == c3.a.e ? 1 : 0);
      zv.x().Q = (boolean)(var1 == c3.a.I ? 1 : 0);
      t4.N().s = (boolean)(var1 == c3.a.E ? 1 : 0);
      pg.G().G = (boolean)(var1 == c3.a.z ? 1 : 0);
      _z.e().p = (boolean)(var1 == c3.a.s ? 1 : 0);
      oz.R().r = (boolean)(var1 == c3.a.i ? 1 : 0);
      ku.y().f = (boolean)(var1 == c3.a.M ? 1 : 0);
      z.v().d = (boolean)(var1 == c3.a.K ? 1 : 0);
      _u var2 = _u.O();
      var2.K = (boolean)(var1 == c3.a.w ? 1 : 0);
      if (!var2.K) {
         var2.J = (boolean)0;
      }
   }

   private void h() {
      this.h(this.L && !this.Y ? this.A : c3.a.f);
   }

   public static boolean Q() {
      c3 var0 = V;
      return !var0.r ? false : ze.c().isSwapBusy() || c6.K().w() || kq.I().k() || pj.U().V();
   }

   public static void G() {
      c3 var0 = V;
      if (var0.r && Q()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null && var1.level != null) {
            var0.P(var1);
         }
      }
   }

   public boolean c() {
      return this.Y;
   }

   public void P(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         long var2 = var1.level.getGameTime() * 31L + System.identityHashCode(var1.level);
         if (var2 != this.w) {
            this.w = var2;
            this.p = (boolean)1;
            ep.H().u(var1);
            this.K();
            pj.U().k(var1);
            this.g(var1);
         }
      } else {
         this.i();
      }
   }

   public void i() {
      if (this.p) {
         this.p = (boolean)0;
         this.w = Long.MIN_VALUE;
         this.v();
         w5.d();
         ze.V().clearQueue();
         mi.L().s(null);
         mt.K("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void K() {
      boolean var1 = ep.H().O();
      if (var1 && this.L && !this.Y) {
         this.Y = (boolean)1;
         this.a = pj.U().F;
         pj.U().K(false);
         w5.d();
         ze.V().clearQueue();
         z8.l().clearQueue();
         this.h();
         mt.K("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.Y) {
         this.Y = (boolean)0;
         this.h();
         if (this.a) {
            pj.U().K(true);
         }

         this.a = (boolean)0;
         mt.K("Rig", "Games resumed");
      }
   }

   private void g(Minecraft var1) {
      _g.G.D(var1);
      _g.Y.D(var1);
      mi.L().s(var1);
      zv.x().m(var1);
      _u.O().U(var1);
      ep var2 = ep.H();
      int var3 = this.W() && t4.N().s ? 1 : 0;
      int var4 = this.W() && kq.I().Z() ? 1 : 0;
      ze var5 = ze.V();
      mj.D().x((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      mj.D().C(var1, var5, var3 != 0 || var2.q(ep.o.e) || var4 != 0 && var2.q(ep.o.v));
      pg.G().m(var1);
      _z.e().I(var1);
      c6.K().k(var1);
      kq.I().Z(var1);
      oz.R().k(var1);
      tb.q().o(var1, z6.u(), oz.R().r || var2.q(ep.o.m) || var2.q(ep.o.p));
      ku.y().d(var1);
      g.s().M(var1, zw.K(), ku.y().f || var2.q(ep.o.q));
   }

   public String Q(c3.a var1) {
      switch (var1) {
         case e:
            mi var10 = mi.L();
            this.S(c3.a.e);
            var10.h();
            break;
         case I:
            zv var9 = zv.x();
            if (!var9.Q) {
               var9.V = zv.u.D;
               this.S(c3.a.I);
            } else if (var9.V == zv.u.D) {
               var9.V = zv.u.U;
            } else if (var9.V == zv.u.U) {
               var9.V = zv.u.J;
            } else {
               var9.V = zv.u.D;
            }
            break;
         case R:
            c6 var8 = c6.K();
            if (!var8.F()) {
               var8.u(1);
               this.S(c3.a.R);
            } else if (var8.T() == 1) {
               var8.u(2);
            } else {
               this.D();
            }
            break;
         case E:
            this.S(c3.a.E);
            mj.D().k();
            break;
         case r:
            this.S(c3.a.r);
            break;
         case z:
            zu var7 = zu.v();
            if (!pg.G().G) {
               var7.F(0);
               this.S(c3.a.z);
            } else {
               var7.A();
               var7.clearQueue();
            }
            break;
         case s:
            z8 var6 = z8.l();
            if (_z.e().p && var6.I() != z8.c.w) {
               var6.o(var6.I() == z8.c.Q ? "lower" : "higher");
            } else {
               var6.o("higher");
               this.S(c3.a.s);
            }
            break;
         case i:
            this.S(c3.a.i);
            tb var5 = tb.q();
            var5.l("player".equals(var5.I()) ? "banker" : "player", z6.u());
            break;
         case M:
            this.S(c3.a.M);
            g.s().G(zw.K());
            break;
         case w:
            _u var4 = _u.O();
            this.S(c3.a.w);
            if (var4.J) {
               var4.J = (boolean)0;
            } else {
               var4.e();
            }
            break;
         case K:
            z var3 = z.v();
            if (var3.d && !"off".equals(var3.B())) {
               var3.A("win".equals(var3.B()) ? "lose" : "win");
            } else {
               var3.A("win");
               this.S(c3.a.K);
            }
            break;
         case l:
         case Z:
            int var2 = var1 == c3.a.l ? 1 : 0;
            this.h((boolean)var2, (var2 != 0 ? _g.G : _g.Y).R());
      }

      return this.w(var1);
   }

   public void h(boolean var1, String var2) {
      _g var3 = var1 ? _g.G : _g.Y;
      if ("off".equalsIgnoreCase(var2)) {
         this.D();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.r()) {
               return;
            }

            this.S(var1 ? c3.a.l : c3.a.Z);
            var3.o(var4);
            var3.m(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String w(c3.a var1) {
      return switch (var1) {
         case e -> mi.L().Q();
         case I -> zv.x().V.t;
         case R -> c6.K().T() == 1 ? "First" : "Second";
         case E -> mj.D().O();
         case r -> (mj.D().B() ? "Auto: " : "") + mj.D().O();
         case z -> zu.v().k();
         case s -> H(z8.l().M());
         case i -> H(tb.q().I());
         case M -> H(g.s().Q()) + " draws";
         case w -> _u.O().J ? "Armed" : "Normal";
         case K -> z.v().E();
         case l -> _g.G.D(_g.G.i());
         case Z -> _g.Y.X(_g.Y.i());
         default -> "";
      };
   }

   private static String H(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean O() {
      return t4.N().s || pg.G().G || _z.e().p || oz.R().r || ku.y().f;
   }

   public ze r() {
      if (_z.e().p) {
         return z8.l();
      } else if (pg.G().G) {
         return zu.v();
      } else if (oz.R().r) {
         return z6.u();
      } else {
         return ku.y().f ? zw.K() : ze.V();
      }
   }

   public boolean r(int var1, int[] var2) {
      return oz.R().r && var2 != null && var2.length != 0 ? tb.q().I(z6.u(), var2) : false;
   }

   public ItemStack r(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = _g.E().i(var1);
         ItemStack var2 = ze.c().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = zv.x().g(var1);
         return _u.O().M(var1);
      } else {
         return var1;
      }
   }

   public c3.q q(c3.a var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3 != null && !var3.isEmpty()) {
            Item var4 = var3.getItem();
            switch (var1) {
               case e:
                  return new c3.q(false, "Paper learns items from its assigned dispensers");
               case I:
                  zv var6 = zv.x();
                  switch (var6.V) {
                     case D:
                        var6.e = var4;
                        break;
                     case U:
                        var6.k = var4;
                        break;
                     case J:
                        var6.n = var4;
                  }

                  this.w();
                  return new c3.q(true, "45/45/10: " + var6.V.t + " item set to " + var3.getHoverName().getString());
               case R:
               default:
                  return new c3.q(false, "From Hand is unavailable for " + var1.c);
               case E:
               case r:
                  return new c3.q(false, "Blackjack imports from its dispenser or hotbar maps");
               case z:
               case i:
                  return new c3.q(false, var1.c + " imports from its dispenser maps");
               case s:
               case M:
                  return new c3.q(false, var1.c + " imports from its assigned dispenser");
               case w:
                  _u var5 = _u.O();
                  if (var5.J) {
                     var5.z = var4;
                     this.w();
                     return new c3.q(true, "Russian Roulette: trigger item set to " + var3.getHoverName().getString());
                  }

                  var5.l = var4;
                  this.w();
                  return new c3.q(true, "Russian Roulette: normal item set to " + var3.getHoverName().getString() + " (arm first to set the trigger item)");
               case K:
                  return new c3.q(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new c3.q(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new c3.q(false, "From Hand failed: no player");
      }
   }

   public void w() {
      me.j(this);
   }

   public enum a {
      e("Paper"),
      I("45/45/10"),
      R("50/50"),
      E("Blackjack"),
      r("Blackjack Force Only"),
      z("Even or Odd"),
      s("Higher or Lower"),
      i("Baccarat"),
      M("Mexican Jack"),
      w("Russian Roulette"),
      K("Arrow Game"),
      l("Horse Racing"),
      Z("Armor Game"),
      f("Off");

      public final String c;

      a(String var3) {
         this.c = var3;
      }
   }

   public interface d {
      void T(c3.a var1);
   }

   public static final class q {
      public final boolean p;
      public final String e;

      public q(boolean var1, String var2) {
         this.p = var1;
         this.e = var2;
      }
   }
}
