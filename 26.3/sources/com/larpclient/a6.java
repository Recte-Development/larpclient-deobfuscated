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
public final class a6 {
   private static final a6 Q = new a6();
   private final List<a6.k> s = new ArrayList<>();
   private a6.d z = a6.d.s;
   private boolean Z;
   private boolean w;
   private long F = Long.MIN_VALUE;
   private int[] D = wb.S();
   private boolean p;
   private boolean l;
   private boolean t;

   private a6() {
   }

   public static a6 F() {
      return Q;
   }

   public void r(a6.k var1) {
      if (!this.s.contains(var1)) {
         this.s.add(var1);
      }
   }

   public synchronized void F() {
      if (!this.w) {
         this.w = (boolean)1;
         wb.a(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> wb.e(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.level != null) {
               ig.r().E(var0);
               qm.p(var0);
               qm.z(var0);
            }
         });
         _q.z("Rig", "Casino game support ready");
      }
   }

   public a6.d E() {
      return this.Z ? this.z : a6.d.s;
   }

   public boolean j() {
      return this.Z && !hk.e().q();
   }

   public int[] c() {
      return (int[])this.D.clone();
   }

   public void t(int[] var1) {
      this.D = wb.N(var1);
   }

   public void S(a6.d var1) {
      if (var1 == null || var1 == a6.d.s) {
         this.V();
      } else if (this.z != var1 || !this.Z) {
         a6.d var2 = this.E();
         this.z = var1;
         this.Z = (boolean)1;
         this.N();
         _q.z("Rig", "Active game: {} (was {})", var1.A, var2.A);
         this.H();
      }
   }

   public void V() {
      boolean var1 = this.Z;
      this.Z = (boolean)0;
      this.N();
      this.R();
      if (var1) {
         _q.z("Rig", "Active game released: {}", this.z.A);
      }

      this.z = a6.d.s;
   }

   public void x() {
      qm.X();
      this.V();
      vy.w().clearQueue();
      vh.R().clearQueue();
      hh.R().U(false);
      _q.z("Rig", "All games stopped");
      this.H();
   }

   private void H() {
      for (a6.k var2 : List.copyOf(this.s)) {
         var2.M(this.E());
      }
   }

   private void R() {
      this.l = (boolean)0;
      this.t = (boolean)0;
      qm.X();
      vy.w().clearQueue();
      vh.R().clearQueue();
      vz.E().clearQueue();
      vf.x().clearQueue();
      v2.L().clearQueue();
      pp.A().g = (boolean)0;
   }

   private void l(a6.d var1) {
      wj.m().O(var1 == a6.d.K);
      q5.a().t(var1 == a6.d.a);
      sl.I.s(var1 == a6.d.H);
      sl.s.s(var1 == a6.d.F);
      qo.Z().u = (boolean)(var1 == a6.d.L ? 1 : 0);
      p2.p().U = (boolean)(var1 == a6.d.k ? 1 : 0);
      a.N().B = (boolean)(var1 == a6.d.d ? 1 : 0);
      ww.J().K = (boolean)(var1 == a6.d.g ? 1 : 0);
      com.larpclient.k.D().h = (boolean)(var1 == a6.d.Y ? 1 : 0);
      _u.b().Q = (boolean)(var1 == a6.d.B ? 1 : 0);
      ql.S().k = (boolean)(var1 == a6.d.j ? 1 : 0);
      ig.r().I = (boolean)(var1 == a6.d.N ? 1 : 0);
      pp var2 = pp.A();
      var2.z = (boolean)(var1 == a6.d.f ? 1 : 0);
      if (!var2.z) {
         var2.g = (boolean)0;
      }
   }

   private void N() {
      this.l(this.Z && !this.l ? this.z : a6.d.s);
   }

   public static boolean i() {
      a6 var0 = Q;
      return !var0.w ? false : vy.a().isSwapBusy() || wj.m().r() || q5.a().L() || hh.R().y();
   }

   public static void M() {
      a6 var0 = Q;
      if (var0.w && i()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null && var1.level != null) {
            var0.g(var1);
         }
      }
   }

   public boolean K() {
      return this.l;
   }

   public void g(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         long var2 = var1.level.getGameTime() * 31L + System.identityHashCode(var1.level);
         if (var2 != this.F) {
            this.F = var2;
            this.p = (boolean)1;
            hk.e().u(var1);
            this.f();
            hh.R().c(var1);
            this.D(var1);
         }
      } else {
         this.T();
      }
   }

   public void T() {
      if (this.p) {
         this.p = (boolean)0;
         this.F = Long.MIN_VALUE;
         this.R();
         qm.X();
         vy.w().clearQueue();
         qo.Z().o(null);
         _q.z("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void f() {
      boolean var1 = hk.e().q();
      if (var1 && this.Z && !this.l) {
         this.l = (boolean)1;
         this.t = hh.R().s;
         hh.R().U(false);
         qm.X();
         vy.w().clearQueue();
         vh.R().clearQueue();
         this.N();
         _q.z("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.l) {
         this.l = (boolean)0;
         this.N();
         if (this.t) {
            hh.R().U(true);
         }

         this.t = (boolean)0;
         _q.z("Rig", "Games resumed");
      }
   }

   private void D(Minecraft var1) {
      sl.I.k(var1);
      sl.s.k(var1);
      qo.Z().o(var1);
      p2.p().W(var1);
      pp.A().F(var1);
      hk var2 = hk.e();
      int var3 = this.j() && a.N().B ? 1 : 0;
      int var4 = this.j() && q5.a().N() ? 1 : 0;
      vy var5 = vy.w();
      io.z().h((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      io.z().O(var1, var5, var3 != 0 || var2.L(hk.k.x) || var4 != 0 && var2.L(hk.k.q));
      ww.J().A(var1);
      com.larpclient.k.D().S(var1);
      wj.m().Z(var1);
      q5.a().m(var1);
      _u.b().F(var1);
      h6.l().J(var1, vf.x(), _u.b().Q || var2.L(hk.k.A) || var2.L(hk.k.I));
      ql.S().P(var1);
      i6.k().F(var1, v2.L(), ql.S().k || var2.L(hk.k.Y));
   }

   public String O(a6.d var1) {
      switch (var1) {
         case L:
            qo var10 = qo.Z();
            this.S(a6.d.L);
            var10.B();
            break;
         case k:
            p2 var9 = p2.p();
            if (!var9.U) {
               var9.z = p2.v.g;
               this.S(a6.d.k);
            } else if (var9.z == p2.v.g) {
               var9.z = p2.v.F;
            } else if (var9.z == p2.v.F) {
               var9.z = p2.v.b;
            } else {
               var9.z = p2.v.g;
            }
            break;
         case K:
            wj var8 = wj.m();
            if (!var8.J()) {
               var8.P(1);
               this.S(a6.d.K);
            } else if (var8.w() == 1) {
               var8.P(2);
            } else {
               this.V();
            }
            break;
         case d:
            this.S(a6.d.d);
            io.z().e();
            break;
         case a:
            this.S(a6.d.a);
            break;
         case g:
            vz var7 = vz.E();
            if (!ww.J().K) {
               var7.d(0);
               this.S(a6.d.g);
            } else {
               var7.d();
               var7.clearQueue();
            }
            break;
         case Y:
            vh var6 = vh.R();
            if (com.larpclient.k.D().h && var6.w() != vh.i.a) {
               var6.x(var6.w() == vh.i.t ? "lower" : "higher");
            } else {
               var6.x("higher");
               this.S(a6.d.Y);
            }
            break;
         case B:
            this.S(a6.d.B);
            h6 var5 = h6.l();
            var5.Y("player".equals(var5.V()) ? "banker" : "player", vf.x());
            break;
         case j:
            this.S(a6.d.j);
            i6.k().g(v2.L());
            break;
         case f:
            pp var4 = pp.A();
            this.S(a6.d.f);
            if (var4.g) {
               var4.g = (boolean)0;
            } else {
               var4.z();
            }
            break;
         case N:
            ig var3 = ig.r();
            if (var3.I && !"off".equals(var3.Q())) {
               var3.d("win".equals(var3.Q()) ? "lose" : "win");
            } else {
               var3.d("win");
               this.S(a6.d.N);
            }
            break;
         case H:
         case F:
            int var2 = var1 == a6.d.H ? 1 : 0;
            this.R((boolean)var2, (var2 != 0 ? sl.I : sl.s).U());
      }

      return this.N(var1);
   }

   public void R(boolean var1, String var2) {
      sl var3 = var1 ? sl.I : sl.s;
      if ("off".equalsIgnoreCase(var2)) {
         this.V();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.w()) {
               return;
            }

            this.S(var1 ? a6.d.H : a6.d.F);
            var3.S(var4);
            var3.s(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String N(a6.d var1) {
      return switch (var1) {
         case L -> qo.Z().a();
         case k -> p2.p().z.z;
         case K -> wj.m().w() == 1 ? "First" : "Second";
         case d -> io.z().m();
         case a -> (io.z().U() ? "Auto: " : "") + io.z().m();
         case g -> vz.E().y();
         case Y -> E(vh.R().G());
         case B -> E(h6.l().V());
         case j -> E(i6.k().y()) + " draws";
         case f -> pp.A().g ? "Armed" : "Normal";
         case N -> ig.r().W();
         case H -> sl.I.z(sl.I.m());
         case F -> sl.s.s(sl.s.m());
         default -> "";
      };
   }

   private static String E(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean h() {
      return a.N().B || ww.J().K || com.larpclient.k.D().h || _u.b().Q || ql.S().k;
   }

   public vy P() {
      if (com.larpclient.k.D().h) {
         return vh.R();
      } else if (ww.J().K) {
         return vz.E();
      } else if (_u.b().Q) {
         return vf.x();
      } else {
         return ql.S().k ? v2.L() : vy.w();
      }
   }

   public boolean V(int var1, int[] var2) {
      return _u.b().Q && var2 != null && var2.length != 0 ? h6.l().t(vf.x(), var2) : false;
   }

   public ItemStack m(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = sl.X().V(var1);
         ItemStack var2 = vy.a().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = p2.p().j(var1);
         return pp.A().F(var1);
      } else {
         return var1;
      }
   }

   public a6.g b(a6.d var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3 != null && !var3.isEmpty()) {
            Item var4 = var3.getItem();
            switch (var1) {
               case L:
                  return new a6.g(false, "Paper learns items from its assigned dispensers");
               case k:
                  p2 var6 = p2.p();
                  switch (var6.z) {
                     case g:
                        var6.S = var4;
                        break;
                     case F:
                        var6.O = var4;
                        break;
                     case b:
                        var6.G = var4;
                  }

                  this.A();
                  return new a6.g(true, "45/45/10: " + var6.z.z + " item set to " + var3.getHoverName().getString());
               case K:
               default:
                  return new a6.g(false, "From Hand is unavailable for " + var1.A);
               case d:
               case a:
                  return new a6.g(false, "Blackjack imports from its dispenser or hotbar maps");
               case g:
               case B:
                  return new a6.g(false, var1.A + " imports from its dispenser maps");
               case Y:
               case j:
                  return new a6.g(false, var1.A + " imports from its assigned dispenser");
               case f:
                  pp var5 = pp.A();
                  if (var5.g) {
                     var5.E = var4;
                     this.A();
                     return new a6.g(true, "Russian Roulette: trigger item set to " + var3.getHoverName().getString());
                  }

                  var5.n = var4;
                  this.A();
                  return new a6.g(true, "Russian Roulette: normal item set to " + var3.getHoverName().getString() + " (arm first to set the trigger item)");
               case N:
                  return new a6.g(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new a6.g(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new a6.g(false, "From Hand failed: no player");
      }
   }

   public void A() {
      wb.e(this);
   }

   public enum d {
      L("Paper"),
      k("45/45/10"),
      K("50/50"),
      d("Blackjack"),
      a("Blackjack Force Only"),
      g("Even or Odd"),
      Y("Higher or Lower"),
      B("Baccarat"),
      j("Mexican Jack"),
      f("Russian Roulette"),
      N("Arrow Game"),
      H("Horse Racing"),
      F("Armor Game"),
      s("Off");

      public final String A;

      d(String var3) {
         this.A = var3;
      }
   }

   public static final class g {
      public final boolean D;
      public final String i;

      public g(boolean var1, String var2) {
         this.D = var1;
         this.i = var2;
      }
   }

   public interface k {
      void M(a6.d var1);
   }
}
