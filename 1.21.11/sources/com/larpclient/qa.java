package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_7923;
import net.minecraft.class_9209;
import net.minecraft.class_9331;
import net.minecraft.class_9334;

final class qa {
   final class_1703 j;
   final List<class_1799> Y = new ArrayList<>();
   private final List<class_1799> C = new ArrayList<>();
   private final Set<Integer> F = new HashSet<>();
   private final ee O = new ee();
   private class_1799 Q = class_1799.field_8037;
   private class_1799 Z = class_1799.field_8037;
   private t.p H;
   private BiConsumer<Integer, Integer> X;
   private boolean d;
   private long v;
   private int a;
   private int u;
   boolean R;
   boolean l;
   boolean i;
   boolean V;
   boolean y;
   String w;
   private final long f = System.nanoTime();
   private final ArrayDeque<String> K = new ArrayDeque<>();

   void k(String var1) {
      if (this.K.size() == 48) {
         this.K.removeFirst();
      }

      this.K.addLast((System.nanoTime() - this.f) / 1000000L + "ms " + var1);
   }

   String r() {
      return "sync="
         + this.j.field_7763
         + " revision="
         + this.j.method_37421()
         + " pending="
         + this.R
         + " ack["
         + this.O.U()
         + "] source{"
         + this.w(this.a)
         + "} destination{"
         + this.w(this.u)
         + "} "
         + this.w(-1)
         + " events="
         + this.K;
   }

   qa(class_1703 var1) {
      this.j = var1;
   }

   qa Z() {
      qa var1 = new qa(this.j);
      if (this.V) {
         var1.b(this.Y, this.Q);
      }

      return var1;
   }

   void b(List<class_1799> var1, class_1799 var2) {
      if (var1.size() == this.j.field_7761.size()) {
         this.Y.clear();

         for (class_1799 var4 : var1) {
            this.Y.add(var4.method_7972());
         }

         this.Q = var2.method_7972();
         this.V = (boolean)1;
         this.O.O(this.j, ++this.v, true, -1, true);
      }
   }

   void n(int var1, class_1799 var2) {
      if (this.V) {
         if (var1 == -1) {
            this.Q = var2.method_7972();
         } else {
            if (var1 < 0 || var1 >= this.Y.size()) {
               return;
            }

            this.Y.set(var1, var2.method_7972());
         }

         this.O.O(this.j, ++this.v, false, var1, var1 == -1);
      }
   }

   boolean d() {
      if (this.V && ss.h(this.Q, this.Z) && ss.h(this.j.method_34255(), this.Z)) {
         for (int var1 = 0; var1 < this.Y.size(); var1++) {
            if (!ss.h(this.Y.get(var1), this.j.method_7611(var1).method_7677()) || this.F.contains(var1) && !ss.h(this.Y.get(var1), this.C.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   class_1799 j(int var1) {
      return this.C.get(var1);
   }

   boolean m(int var1) {
      return this.F.contains(var1);
   }

   boolean K() {
      return this.V && !this.R && this.w == null && this.Q.method_7960() && this.j.method_34255().method_7960();
   }

   boolean E() {
      return this.I(true);
   }

   boolean I(boolean var1) {
      if (this.l) {
         return false;
      }

      if (this.w != null) {
         return false;
      }

      if (!this.R) {
         return true;
      }

      if (this.O.Z(ss.h(this.Y.get(this.a), this.C.get(this.a)), ss.h(this.Y.get(this.u), this.C.get(this.u)), ss.h(this.Q, this.Z))
         && (!this.d || this.O.K())
         && this.d()) {
         if (this.H == null) {
            this.R = (boolean)0;
            return true;
         }

         class_1799 var2 = this.Y.get(this.a);
         if (class_1799.method_7973(var2, this.j.method_7611(this.a).method_7677())
            && class_1799.method_7973(this.Q, this.j.method_34255())
            && (class_1799.method_31577(var2, this.Q) || yk.M(var2, this.Q))
            && this.j.method_7611(this.a).method_7680(this.Q)
            && var2.method_7947() + this.Q.method_7947() <= Math.min(this.j.method_7611(this.a).method_7676(this.Q), this.Q.method_7914())) {
            this.C.set(this.a, var2.method_46651(this.H.returned().slotCount()));
            this.Z = class_1799.field_8037;
            this.H = null;
            this.O.v(this.j, this.a, this.u, this.v);
            this.l = this.i = (boolean)1;

            try {
               this.X.accept(this.a, 0);
            } catch (RuntimeException var7) {
               this.w = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.l = this.i = (boolean)0;
               this.X = null;
            }

            return false;
         } else {
            this.w = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.O.m()) {
            this.w = "batch acknowledgement timeout; no replay " + this.O.U();
         }

         return false;
      }
   }

   t.p k(class_310 var1, int var2, int var3, boolean var4) {
      return this.q(var1, var2, var3, var4).batch();
   }

   t.p h(class_310 var1, int var2, int var3) {
      return this.u(var1, var2, var3, false, true).batch();
   }

   qa.i q(class_310 var1, int var2, int var3, boolean var4) {
      return this.u(var1, var2, var3, var4, false);
   }

   qa.i u(class_310 var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.V) {
         return new qa.i(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.Y.size() && var3 < this.Y.size()) {
         class_1735 var6 = this.j.method_7611(var2);
         class_1735 var7 = this.j.method_7611(var3);
         class_1799 var8 = this.Y.get(var2);
         class_1799 var9 = var4 ? class_1799.field_8037 : this.Y.get(var3);
         class_1799 var10 = var6.method_7677();
         class_1799 var11 = var4 ? class_1799.field_8037 : var7.method_7677();
         int var12 = !var9.method_7960() && !class_1799.method_31577(var8, var9) && (!var5 || !yk.M(var8, var9)) ? 0 : 1;
         int var13 = !var11.method_7960() && !class_1799.method_31577(var10, var11) && (!var5 || !yk.M(var10, var11)) ? 0 : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         t.p var15 = t.batch(
            var2,
            var3,
            var8.method_7947(),
            var9.method_7947(),
            (boolean)var14,
            Math.min(var6.method_7676(var8), var8.method_7914()),
            Math.min(var7.method_7676(var8), var8.method_7914())
         );
         t.p var16 = t.batch(
            var2,
            var3,
            var10.method_7947(),
            var11.method_7947(),
            (boolean)var14,
            Math.min(var6.method_7676(var10), var10.method_7914()),
            Math.min(var7.method_7676(var10), var10.method_7914())
         );
         String var17 = null;
         if (var2 == var3) {
            var17 = "source equals destination";
         } else if (var14 == 0) {
            var17 = (var12 == 0 ? "server merge mismatch " + X(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + X(var10, var11) : "");
         } else if (var15 == null) {
            var17 = "server count/capacity rejected";
         } else if (var16 == null) {
            var17 = "local count/capacity rejected";
         } else if (!var15.equals(var16)) {
            var17 = "local/server plan divergence server=" + var15 + " local=" + var16;
         } else if (!var6.method_7674(var1.field_1724)) {
            var17 = "source take permission denied";
         } else if (!var7.method_7680(var8)) {
            var17 = "destination insert permission denied (server stack)";
         } else if (!var7.method_7680(var10)) {
            var17 = "destination insert permission denied (local stack)";
         } else if (var15.clicks() == 3 && !var6.method_7680(var8)) {
            var17 = "source remainder insert permission denied (server stack)";
         } else if (var15.clicks() == 3 && !var6.method_7680(var10)) {
            var17 = "source remainder insert permission denied (local stack)";
         }

         return new qa.i(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new qa.i(null, "source/destination slot unavailable");
      }
   }

   private static String X(class_1799 var0, class_1799 var1) {
      HashSet var2 = new HashSet(var0.method_57353().method_57831());
      var2.addAll(var1.method_57353().method_57831());
      TreeSet var3 = new TreeSet();

      for (class_9331 var5 : var2) {
         if (!Objects.equals(var0.method_57353().method_58694(var5), var1.method_57353().method_58694(var5))) {
            var3.add(String.valueOf(class_7923.field_49658.method_10221(var5)));
         }
      }

      return "itemEqual="
         + var0.method_31574(var1.method_7909())
         + " componentKeys="
         + var3
         + (var3.contains("minecraft:lore") ? " loreA=" + yk.o(var0) + " loreB=" + yk.o(var1) : "");
   }

   String w(int var1) {
      if (var1 == -1) {
         return "cursor server{" + n(this.Q, null) + "} local{" + n(this.j.method_34255(), null) + "} server/local " + X(this.Q, this.j.method_34255());
      } else if (this.V && var1 >= 0 && var1 < this.Y.size()) {
         class_1735 var2 = this.j.method_7611(var1);
         class_1799 var3 = this.Y.get(var1);
         class_1799 var4 = var2.method_7677();
         return "slot=" + var1 + " server{" + n(var3, var2) + "} local{" + n(var4, var2) + "} server/local " + X(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String n(class_1799 var0, class_1735 var1) {
      class_9209 var2 = (class_9209)var0.method_58694(class_9334.field_49646);
      return "map="
         + (var2 == null ? -1 : var2.comp_2315())
         + " count="
         + var0.method_7947()
         + (var1 == null ? "" : " limit=" + Math.min(var1.method_7676(var0), var0.method_7914()));
   }

   String Z(int var1, int var2) {
      class_1735 var3 = this.j.method_7611(var1);
      class_1735 var4 = this.j.method_7611(var2);
      class_1799 var5 = this.Y.get(var1);
      class_1799 var6 = var3.method_7677();
      return "effectiveLimits(server/local) source="
         + Math.min(var3.method_7676(var5), var5.method_7914())
         + "/"
         + Math.min(var3.method_7676(var6), var6.method_7914())
         + " destination="
         + Math.min(var4.method_7676(var5), var5.method_7914())
         + "/"
         + Math.min(var4.method_7676(var6), var6.method_7914());
   }

   boolean u(class_310 var1, int var2, int var3, String var4) {
      return this.T(var1, var2, var3, var4, false);
   }

   boolean T(class_310 var1, int var2, int var3, String var4, boolean var5) {
      if (this.R || this.w != null) {
         return false;
      } else if (var1.field_1761 == null) {
         this.w = "interaction manager unavailable; no replay";
         return false;
      } else {
         class_634 var6 = var1.method_1562();
         class_640 var7 = var6 == null ? null : var6.method_2871(var1.field_1724.method_5667());
         this.O.T(var7 == null ? 0 : var7.method_2959());
         t.p var8 = this.u(var1, var2, var3, false, var5).batch();
         if (this.d() && var8 != null && var1.field_1724.field_7512 == this.j) {
            yq.R("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.P(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.field_1724.field_7512 != this.j) {
                  throw new IllegalStateException("handler lost");
               }

               var1.field_1761.method_2906(this.j.field_7763, var2x, var3x, class_1713.field_7790, var1.field_1724);
            }, var5);
         } else {
            this.w = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean S(int var1, int var2, t.p var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.P(var1, var2, var3, var4, var5, false);
   }

   boolean P(int var1, int var2, t.p var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.R && this.w == null && var3 != null && this.d()) {
         class_1799 var7 = this.Y.get(var1);
         class_1799 var8 = this.Y.get(var2);
         class_1799 var9 = this.j.method_7611(var1).method_7677().method_7972();
         class_1799 var10 = this.j.method_7611(var2).method_7677().method_7972();
         this.d = (boolean)(!var6 || var8.method_7960() || class_1799.method_31577(var7, var8) && class_1799.method_31577(var9, var10) ? 0 : 1);
         if (!this.d || (class_1799.method_31577(var7, var8) || yk.M(var7, var8)) && (class_1799.method_31577(var9, var10) || yk.M(var9, var10))) {
            if (this.C.isEmpty()) {
               for (class_1799 var12 : this.Y) {
                  this.C.add(var12.method_7972());
               }
            }

            this.F.add(var1);
            this.F.add(var2);
            this.H = this.d && var3.clicks() == 3 ? var3 : null;
            this.X = this.H == null ? null : var5;
            this.Z = this.H == null ? class_1799.field_8037 : var7.method_46651(var3.placement().cursorCount());
            this.C.set(var1, var7.method_46651(this.H == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.C.set(var2, (var8.method_7960() ? var7 : var8).method_46651(var3.placement().slotCount()));
            this.a = var1;
            this.u = var2;
            this.k("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.R = this.y = (boolean)1;
            this.O.v(this.j, var1, var2, this.v);
            return var3.execute(
                  var7x -> {
                     this.l = (boolean)1;
                     this.i = (boolean)(var7x == (this.d ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.k("click call index=" + var7x + " probe=" + this.i + " revision=" + this.j.method_37421());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.w = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.l = this.i = (boolean)0;
                     }

                     if (this.d && var7x == 1) {
                        return false;
                     }

                     this.k("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.method_7947() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (class_1799.method_7973(this.j.method_7611(var1).method_7677(), var9.method_46651(var8x))
                        && class_1799.method_7973(this.j.method_7611(var2).method_7677(), (var10.method_7960() ? var9 : var10).method_46651(var9x))
                        && class_1799.method_7973(this.j.method_34255(), var9.method_46651(var10x))) {
                        return true;
                     }

                     this.w = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.d && this.w == null;
         } else {
            this.w = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record i(t.p batch, String reason) {
   }
}
