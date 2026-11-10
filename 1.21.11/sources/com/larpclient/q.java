package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class q {
   private static final q T = new q();
   private final List<q.y> d = new ArrayList<>();
   private q.n M = q.n.q;
   private boolean X;
   private boolean I;
   private long O = Long.MIN_VALUE;
   private int[] f = in.O();
   private boolean j;
   private boolean B;
   private boolean R;

   private q() {
   }

   public static q U() {
      return T;
   }

   public void y(q.y var1) {
      if (!this.d.contains(var1)) {
         this.d.add(var1);
      }
   }

   public synchronized void a() {
      if (!this.I) {
         this.I = (boolean)1;
         in.n(this);
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> in.l(this));
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            if (var0.field_1687 != null) {
               yu.o().L(var0);
               il.D(var0);
               il.C(var0);
            }
         });
         q8.G("Rig", "Casino game support ready");
      }
   }

   public q.n D() {
      return this.X ? this.M : q.n.q;
   }

   public boolean Z() {
      return this.X && !hu.f().N();
   }

   public int[] n() {
      return (int[])this.f.clone();
   }

   public void X(int[] var1) {
      this.f = in.L(var1);
   }

   public void C(q.n var1) {
      if (var1 == null || var1 == q.n.q) {
         this.x();
      } else if (this.M != var1 || !this.X) {
         q.n var2 = this.D();
         this.M = var1;
         this.X = (boolean)1;
         this.q();
         q8.G("Rig", "Active game: {} (was {})", var1.N, var2.N);
         this.G();
      }
   }

   public void x() {
      boolean var1 = this.X;
      this.X = (boolean)0;
      this.q();
      this.r();
      if (var1) {
         q8.G("Rig", "Active game released: {}", this.M.N);
      }

      this.M = q.n.q;
   }

   public void e() {
      il.A();
      this.x();
      q7.u().clearQueue();
      qr.h().clearQueue();
      bk.W().U(false);
      q8.G("Rig", "All games stopped");
      this.G();
   }

   private void G() {
      for (q.y var2 : List.copyOf(this.d)) {
         var2.U(this.D());
      }
   }

   private void r() {
      this.B = (boolean)0;
      this.R = (boolean)0;
      il.A();
      q7.u().clearQueue();
      qr.h().clearQueue();
      qc.i().clearQueue();
      qq.f().clearQueue();
      ql.Y().clearQueue();
      yb.b().N = (boolean)0;
   }

   private void l(q.n var1) {
      ju.b().C(var1 == q.n.O);
      tw.s().u(var1 == q.n.x);
      f4.H.a(var1 == q.n.d);
      f4.Y.a(var1 == q.n.e);
      t3.c().C = (boolean)(var1 == q.n.C ? 1 : 0);
      ux.o().n = (boolean)(var1 == q.n.P ? 1 : 0);
      j0.g().W = (boolean)(var1 == q.n.W ? 1 : 0);
      hs.B().p = (boolean)(var1 == q.n.S ? 1 : 0);
      fd.h().v = (boolean)(var1 == q.n.g ? 1 : 0);
      v.D().V = (boolean)(var1 == q.n.j ? 1 : 0);
      s1.j().e = (boolean)(var1 == q.n.m ? 1 : 0);
      yu.o().C = (boolean)(var1 == q.n.X ? 1 : 0);
      yb var2 = yb.b();
      var2.j = (boolean)(var1 == q.n.y ? 1 : 0);
      if (!var2.j) {
         var2.N = (boolean)0;
      }
   }

   private void q() {
      this.l(this.X && !this.B ? this.M : q.n.q);
   }

   public static boolean K() {
      q var0 = T;
      return !var0.I ? false : q7.L().isSwapBusy() || ju.b().g() || tw.s().m() || bk.W().l();
   }

   public static void l() {
      q var0 = T;
      if (var0.I && K()) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
            var0.E(var1);
         }
      }
   }

   public boolean y() {
      return this.B;
   }

   public void E(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         long var2 = var1.field_1687.method_75260() * 31L + System.identityHashCode(var1.field_1687);
         if (var2 != this.O) {
            this.O = var2;
            this.j = (boolean)1;
            hu.f().u(var1);
            this.m();
            bk.W().I(var1);
            this.J(var1);
         }
      } else {
         this.Y();
      }
   }

   public void Y() {
      if (this.j) {
         this.j = (boolean)0;
         this.O = Long.MIN_VALUE;
         this.r();
         il.A();
         q7.u().clearQueue();
         t3.c().O((class_310)null);
         q8.G("Rig", "Game context lost; pending actions cleared");
      }
   }

   private void m() {
      boolean var1 = hu.f().N();
      if (var1 && this.X && !this.B) {
         this.B = (boolean)1;
         this.R = bk.W().U;
         bk.W().U(false);
         il.A();
         q7.u().clearQueue();
         qr.h().clearQueue();
         this.q();
         q8.G("Rig", "Looking at an unassigned dispenser; games paused");
      } else if (!var1 && this.B) {
         this.B = (boolean)0;
         this.q();
         if (this.R) {
            bk.W().U(true);
         }

         this.R = (boolean)0;
         q8.G("Rig", "Games resumed");
      }
   }

   private void J(class_310 var1) {
      f4.H.A(var1);
      f4.Y.A(var1);
      t3.c().O(var1);
      ux.o().h(var1);
      yb.b().e(var1);
      hu var2 = hu.f();
      int var3 = this.Z() && j0.g().W ? 1 : 0;
      int var4 = this.Z() && tw.s().j() ? 1 : 0;
      q7 var5 = q7.u();
      l4.K().P((boolean)var3);
      if (var3 != 0) {
         var5.tick(var1);
      } else if (var5.isSwapActive()) {
         var5.clearQueue();
         var5.tick(var1);
      } else {
         var5.clearQueue();
      }

      l4.K().Q(var1, var5, var3 != 0 || var2.E(hu.q.G) || var4 != 0 && var2.E(hu.q.k));
      hs.B().u(var1);
      fd.h().m(var1);
      ju.b().q(var1);
      tw.s().L(var1);
      v.D().A(var1);
      lk.k().Y(var1, qq.f(), v.D().V || var2.E(hu.q.q) || var2.E(hu.q.E));
      s1.j().F(var1);
      q_.j().s(var1, ql.Y(), s1.j().e || var2.E(hu.q.m));
   }

   public String p(q.n var1) {
      switch (var1) {
         case C:
            t3 var10 = t3.c();
            this.C(q.n.C);
            var10.M();
            break;
         case P:
            ux var9 = ux.o();
            if (!var9.n) {
               var9.z = ux.k.L;
               this.C(q.n.P);
            } else if (var9.z == ux.k.L) {
               var9.z = ux.k.l;
            } else if (var9.z == ux.k.l) {
               var9.z = ux.k.V;
            } else {
               var9.z = ux.k.L;
            }
            break;
         case O:
            ju var8 = ju.b();
            if (!var8.U()) {
               var8.k(1);
               this.C(q.n.O);
            } else if (var8.K() == 1) {
               var8.k(2);
            } else {
               this.x();
            }
            break;
         case W:
            this.C(q.n.W);
            l4.K().X();
            break;
         case x:
            this.C(q.n.x);
            break;
         case S:
            qc var7 = qc.i();
            if (!hs.B().p) {
               var7.p(0);
               this.C(q.n.S);
            } else {
               var7.m();
               var7.clearQueue();
            }
            break;
         case g:
            qr var6 = qr.h();
            if (fd.h().v && var6.M() != qr.z.u) {
               var6.k(var6.M() == qr.z.a ? "lower" : "higher");
            } else {
               var6.k("higher");
               this.C(q.n.g);
            }
            break;
         case j:
            this.C(q.n.j);
            lk var5 = lk.k();
            var5.U("player".equals(var5.m()) ? "banker" : "player", qq.f());
            break;
         case m:
            this.C(q.n.m);
            q_.j().B(ql.Y());
            break;
         case y:
            yb var4 = yb.b();
            this.C(q.n.y);
            if (var4.N) {
               var4.N = (boolean)0;
            } else {
               var4.Z();
            }
            break;
         case X:
            yu var3 = yu.o();
            if (var3.C && !"off".equals(var3.B())) {
               var3.q("win".equals(var3.B()) ? "lose" : "win");
            } else {
               var3.q("win");
               this.C(q.n.X);
            }
            break;
         case d:
         case e:
            int var2 = var1 == q.n.d ? 1 : 0;
            this.C((boolean)var2, (var2 != 0 ? f4.H : f4.Y).l());
      }

      return this.k(var1);
   }

   public void C(boolean var1, String var2) {
      f4 var3 = var1 ? f4.H : f4.Y;
      if ("off".equalsIgnoreCase(var2)) {
         this.x();
      } else {
         try {
            int var4 = Integer.parseInt(var2);
            if (var4 < 1 || var4 > var3.W()) {
               return;
            }

            this.C(var1 ? q.n.d : q.n.e);
            var3.x(var4);
            var3.a(true);
         } catch (NumberFormatException var5) {
         }
      }
   }

   public String k(q.n var1) {
      return switch (var1) {
         case C -> t3.c().B();
         case P -> ux.o().z.g;
         case O -> ju.b().K() == 1 ? "First" : "Second";
         case W -> l4.K().Z();
         case x -> (l4.K().F() ? "Auto: " : "") + l4.K().Z();
         case S -> qc.i().h();
         case g -> R(qr.h().p());
         case j -> R(lk.k().m());
         case m -> R(q_.j().v()) + " draws";
         case y -> yb.b().N ? "Armed" : "Normal";
         case X -> yu.o().A();
         case d -> f4.H.q(f4.H.e());
         case e -> f4.Y.f(f4.Y.e());
         default -> "";
      };
   }

   private static String R(String var0) {
      return var0 != null && !var0.isEmpty() ? Character.toUpperCase(var0.charAt(0)) + var0.substring(1) : "";
   }

   public boolean C() {
      return j0.g().W || hs.B().p || fd.h().v || v.D().V || s1.j().e;
   }

   public q7 R() {
      if (fd.h().v) {
         return qr.h();
      } else if (hs.B().p) {
         return qc.i();
      } else if (v.D().V) {
         return qq.f();
      } else {
         return s1.j().e ? ql.Y() : q7.u();
      }
   }

   public boolean B(int var1, int[] var2) {
      return v.D().V && var2 != null && var2.length != 0 ? lk.k().F(qq.f(), var2) : false;
   }

   public class_1799 g(class_1799 var1) {
      if (var1 != null && !var1.method_7960()) {
         var1 = f4.L().U(var1);
         class_1799 var2 = q7.L().getVisualOverride(var1);
         if (var2 != null) {
            var1 = var2;
         }

         var1 = ux.o().X(var1);
         return yb.b().U(var1);
      } else {
         return var1;
      }
   }

   public q.z T(q.n var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null) {
         class_1799 var3 = var2.field_1724.method_6047();
         if (var3 != null && !var3.method_7960()) {
            class_1792 var4 = var3.method_7909();
            switch (var1) {
               case C:
                  return new q.z(false, "Paper learns items from its assigned dispensers");
               case P:
                  ux var6 = ux.o();
                  switch (var6.z) {
                     case L:
                        var6.Q = var4;
                        break;
                     case l:
                        var6.D = var4;
                        break;
                     case V:
                        var6.S = var4;
                  }

                  this.y();
                  return new q.z(true, "45/45/10: " + var6.z.g + " item set to " + var3.method_7964().getString());
               case O:
               default:
                  return new q.z(false, "From Hand is unavailable for " + var1.N);
               case W:
               case x:
                  return new q.z(false, "Blackjack imports from its dispenser or hotbar maps");
               case S:
               case j:
                  return new q.z(false, var1.N + " imports from its dispenser maps");
               case g:
               case m:
                  return new q.z(false, var1.N + " imports from its assigned dispenser");
               case y:
                  yb var5 = yb.b();
                  if (var5.N) {
                     var5.P = var4;
                     this.y();
                     return new q.z(true, "Russian Roulette: trigger item set to " + var3.method_7964().getString());
                  }

                  var5.Y = var4;
                  this.y();
                  return new q.z(true, "Russian Roulette: normal item set to " + var3.method_7964().getString() + " (arm first to set the trigger item)");
               case X:
                  return new q.z(false, "Arrow Game uses detected item frames and a held map");
            }
         } else {
            return new q.z(false, "From Hand failed: main hand is empty");
         }
      } else {
         return new q.z(false, "From Hand failed: no player");
      }
   }

   public void y() {
      in.l(this);
   }

   public enum n {
      C("Paper"),
      P("45/45/10"),
      O("50/50"),
      W("Blackjack"),
      x("Blackjack Force Only"),
      S("Even or Odd"),
      g("Higher or Lower"),
      j("Baccarat"),
      m("Mexican Jack"),
      y("Russian Roulette"),
      X("Arrow Game"),
      d("Horse Racing"),
      e("Armor Game"),
      q("Off");

      public final String N;

      n(String var3) {
         this.N = var3;
      }
   }

   public interface y {
      void U(q.n var1);
   }

   public static final class z {
      public final boolean B;
      public final String f;

      public z(boolean var1, String var2) {
         this.B = var1;
         this.f = var2;
      }
   }
}
