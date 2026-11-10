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
public final class sg {
   private static final sg u = new sg();
   private final List<sg.k> a = new ArrayList<>();
   private sg.i r = sg.i.H;
   private boolean t;
   private boolean O;
   private long F = Long.MIN_VALUE;
   private int[] f = c1.T();
   private boolean X;
   private boolean w;
   private boolean x;

   private sg() {
   }

   public static sg G() {
      return u;
   }

   public void s(sg.k var1) {
      if (!this.a.contains(var1)) {
         this.a.add(var1);
      }
   }

   public synchronized void v() {
      if (!this.O) {
         this.O = (boolean)1;
         c1.D(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> c1.n(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.level != null) {
               bs.R().v(var0);
               mg.N(var0);
               mg.T(var0);
            }
         });
         h.A("Rig", "Casino game support ready");
      }
   }

   public sg.i o() {
      return this.t ? this.r : sg.i.H;
   }

   public boolean J() {
      return this.t && !gj.c().F();
   }

   public int[] H() {
      return (int[])this.f.clone();
   }

   public void N(int[] var1) {
      this.f = c1.N(var1);
   }

   public void Q(sg.i var1) {
      if (var1 == null || var1 == sg.i.H) {
         this.K();
      } else if (this.r != var1 || !this.t) {
         sg.i var2 = this.o();
         this.r = var1;
         this.t = (boolean)1;
         this.p();
         h.A("Rig", "Active game: {} (was {})", var1.X, var2.X);
         this.O();
      }
   }

   public void K() {
      boolean var1 = this.t;
      this.t = (boolean)0;
      this.p();
      this.F();
      if (var1) {
         h.A("Rig", "Active game released: {}", this.r.X);
      }

      this.r = sg.i.H;
   }

   public void P() {
      mg.J();
      this.K();
      we.u().clearQueue();
      wc.s().clearQueue();
      cc.z().F(false);
      h.A("Rig", "All games stopped");
      this.O();
   }

   private void O() {
      for (sg.k var2 : List.copyOf(this.a)) {
         var2.l(this.o());
      }
   }

   private void F() {
      this.w = (boolean)0;
      this.x = (boolean)0;
      mg.J();
      we.u().clearQueue();
      wc.s().clearQueue();
      w3.M().clearQueue();
      wk.m().clearQueue();
      w0.l().clearQueue();
      pq.Q().a = (boolean)0;
   }

   private void o(sg.i var1) {
      pw.S().Y(var1 == sg.i.a);
      lh.I().f(var1 == sg.i.g);
      cy.Z.G(var1 == sg.i.I);
      cy.d.G(var1 == sg.i.Q);
      g5.a().Z = (boolean)(var1 == sg.i.V ? 1 : 0);
      t5.O().g = (boolean)(var1 == sg.i.D ? 1 : 0);
      j2.y().h = (boolean)(var1 == sg.i.c ? 1 : 0);
      lj.M().j = (boolean)(var1 == sg.i.n ? 1 : 0);
      k6.d().Q = (boolean)(var1 == sg.i.h ? 1 : 0);
      m6.W().K = (boolean)(var1 == sg.i.O ? 1 : 0);
      c9.G().j = (boolean)(var1 == sg.i.A ? 1 : 0);
      bs.R().l = (boolean)(var1 == sg.i.L ? 1 : 0);
      pq var2 = pq.Q();
      var2.s = (boolean)(var1 == sg.i.J ? 1 : 0);
      if (!var2.s) {
         var2.a = (boolean)0;
      }
   }

   private void p() {
      this.o(this.t && !this.w ? this.r : sg.i.H);
   }

   public static boolean I() {
      sg var0 = u;
      return !var0.O ? false : we.Y().isSwapBusy() || pw.S().P() || lh.I().k() || cc.z().L();
   }

   public static void L() {
      sg var0 = u;
      if (var0.O && I()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null && var1.level != null) {
            var0.g(var1);
         }
      }
   }

   public boolean p() {
      return this.w;
   }

   public void g(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         long var2 = var1.level.getGameTime() * 31L + System.identityHashCode(var1.level);
         if (var2 != this.F) {
            this.F = var2;
            this.X = (boolean)1;
            gj.c().L(var1);
            this.d();
            cc.z().J(var1);
            this.v(var1);
         }
      } else {
         this.U();
      }
   }

   public void U() {
      if (this.X) {
         this.X = (boolean)0;
         this.F = Long.MIN_VALUE;
         this.F();
         mg.J();
         we.u().clearQueue();
         g5.a().M(null);
         h.A("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void d() {
      boolean var1 = gj.c().F();
      if (var1 && this.t && !this.w) {
         this.w = (boolean)1;
         this.x = cc.z().y;
         cc.z().F(false);
         mg.J();
         we.u().clearQueue();
         wc.s().clearQueue();
         this.p();
         h.A("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.w) {
         this.w = (boolean)0;
         this.p();
         if (this.x) {
            cc.z().F(true);
         }

         this.x = (boolean)0;
         h.A("Rig", "Games resumed");
      }
   }

   private void v(Minecraft var1) {
      cy.Z.f(var1);
      cy.d.f(var1);
      g5.a().M(var1);
      t5.O().O(var1);
      pq.Q().Q(var1);
      gj var2 = gj.c();
      int var3 = this.J() && j2.y().h ? 1 : 0;
      int var4 = this.J() && lh.I().S() ? 1 : 0;
      we var5 = we.u();
      s3.S().s((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      s3.S().P(var1, var5, var3 != 0 || var2.H(gj.d.m) || var4 != 0 && var2.H(gj.d.a));
      lj.M().Q(var1);
      k6.d().k(var1);
      pw.S().k(var1);
      lh.I().t(var1);
      m6.W().h(var1);
      m9.O().X(var1, wk.m(), m6.W().K || var2.H(gj.d.x) || var2.H(gj.d.c));
      c9.G().G(var1);
      kr.N().R(var1, w0.l(), c9.G().j || var2.H(gj.d.q));
   }

   public String A(sg.i var1) {
      switch (var1) {
         case V:
            g5 var10 = g5.a();
            this.Q(sg.i.V);
            var10.A();
            break;
         case D:
            t5 var9 = t5.O();
            if (!var9.g) {
               var9.b = t5.o.i;
               this.Q(sg.i.D);
            } else if (var9.b == t5.o.i) {
               var9.b = t5.o.E;
            } else if (var9.b == t5.o.E) {
               var9.b = t5.o.B;
            } else {
               var9.b = t5.o.i;
            }
            break;
         case a:
            pw var8 = pw.S();
            if (!var8.K()) {
               var8.m(1);
               this.Q(sg.i.a);
            } else if (var8.Z() == 1) {
               var8.m(2);
            } else {
               this.K();
            }
            break;
         case c:
            this.Q(sg.i.c);
            s3.S().S();
            break;
         case g:
            this.Q(sg.i.g);
            break;
         case n:
            w3 var7 = w3.M();
            if (!lj.M().j) {
               var7.s(0);
               this.Q(sg.i.n);
            } else {
               var7.Y();
               var7.clearQueue();
            }
            break;
         case h:
            wc var6 = wc.s();
            if (k6.d().Q && var6.O() != wc.n.H) {
               var6.d(var6.O() == wc.n.s ? "lower" : "higher");
            } else {
               var6.d("higher");
               this.Q(sg.i.h);
            }
            break;
         case O:
            this.Q(sg.i.O);
            m9 var5 = m9.O();
            var5.q("player".equals(var5.T()) ? "banker" : "player", wk.m());
            break;
         case A:
            this.Q(sg.i.A);
            kr.N().Q(w0.l());
            break;
         case J:
            pq var4 = pq.Q();
            this.Q(sg.i.J);
            if (var4.a) {
               var4.a = (boolean)0;
            } else {
               var4.g();
            }
            break;
         case L:
            bs var3 = bs.R();
            if (var3.l && !"off".equals(var3.V())) {
               var3.H("win".equals(var3.V()) ? "lose" : "win");
            } else {
               var3.H("win");
               this.Q(sg.i.L);
            }
            break;
         case I:
         case Q:
            int var2 = var1 == sg.i.I ? 1 : 0;
            this.I((boolean)var2, (var2 != 0 ? cy.Z : cy.d).U());
      }

      return this.d(var1);
   }

   public void I(boolean var1, String var2) {
      cy var3 = var1 ? cy.Z : cy.d;
      if ("off".equalsIgnoreCase(var2)) {
         this.K();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.w()) {
               return;
            }

            this.Q(var1 ? sg.i.I : sg.i.Q);
            var3.f(var4);
            var3.G(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String d(sg.i var1) {
      return switch (var1) {
         case V -> g5.a().X();
         case D -> t5.O().b.G;
         case a -> pw.S().Z() == 1 ? "First" : "Second";
         case c -> s3.S().y();
         case g -> (s3.S().F() ? "Auto: " : "") + s3.S().y();
         case n -> w3.M().c();
         case h -> T(wc.s().J());
         case O -> T(m9.O().T());
         case A -> T(kr.N().L()) + " draws";
         case J -> pq.Q().a ? "Armed" : "Normal";
         case L -> bs.R().J();
         case I -> cy.Z.i(cy.Z.g());
         case Q -> cy.d.h(cy.d.g());
         default -> "";
      };
   }

   private static String T(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean U() {
      return j2.y().h || lj.M().j || k6.d().Q || m6.W().K || c9.G().j;
   }

   public we P() {
      if (k6.d().Q) {
         return wc.s();
      } else if (lj.M().j) {
         return w3.M();
      } else if (m6.W().K) {
         return wk.m();
      } else {
         return c9.G().j ? w0.l() : we.u();
      }
   }

   public boolean M(int var1, int[] var2) {
      return m6.W().K && var2 != null && var2.length != 0 ? m9.O().T(wk.m(), var2) : false;
   }

   public ItemStack b(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1 = cy.Q().D(var1);
         ItemStack var2 = we.Y().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = t5.O().U(var1);
         return pq.Q().P(var1);
      } else {
         return var1;
      }
   }

   public sg.f J(sg.i var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3 != null && !var3.isEmpty()) {
            Item var4 = var3.getItem();
            switch (var1) {
               case V:
                  return new sg.f(false, "Paper learns items from its assigned dispensers");
               case D:
                  t5 var6 = t5.O();
                  switch (var6.b) {
                     case i:
                        var6.F = var4;
                        break;
                     case E:
                        var6.m = var4;
                        break;
                     case B:
                        var6.X = var4;
                  }

                  this.Q();
                  return new sg.f(true, "45/45/10: " + var6.b.G + " item set to " + var3.getHoverName().getString());
               case a:
               default:
                  return new sg.f(false, "From Hand is unavailable for " + var1.X);
               case c:
               case g:
                  return new sg.f(false, "Blackjack imports from its dispenser or hotbar maps");
               case n:
               case O:
                  return new sg.f(false, var1.X + " imports from its dispenser maps");
               case h:
               case A:
                  return new sg.f(false, var1.X + " imports from its assigned dispenser");
               case J:
                  pq var5 = pq.Q();
                  if (var5.a) {
                     var5.Y = var4;
                     this.Q();
                     return new sg.f(true, "Russian Roulette: trigger item set to " + var3.getHoverName().getString());
                  }

                  var5.c = var4;
                  this.Q();
                  return new sg.f(true, "Russian Roulette: normal item set to " + var3.getHoverName().getString() + " (arm first to set the trigger item)");
               case L:
                  return new sg.f(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new sg.f(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new sg.f(false, "From Hand failed: no player");
      }
   }

   public void Q() {
      c1.n(this);
   }

   public static final class f {
      public final boolean o;
      public final String f;

      public f(boolean var1, String var2) {
         this.o = var1;
         this.f = var2;
      }
   }

   public enum i {
      V("Paper"),
      D("45/45/10"),
      a("50/50"),
      c("Blackjack"),
      g("Blackjack Force Only"),
      n("Even or Odd"),
      h("Higher or Lower"),
      O("Baccarat"),
      A("Mexican Jack"),
      J("Russian Roulette"),
      L("Arrow Game"),
      I("Horse Racing"),
      Q("Armor Game"),
      H("Off");

      public final String X;

      i(String var3) {
         this.X = var3;
      }
   }

   public interface k {
      void l(sg.i var1);
   }
}
