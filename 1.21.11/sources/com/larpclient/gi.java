package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494.q313984e4ec8a45e1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_11417;
import net.minecraft.class_11418;
import net.minecraft.class_11419;
import net.minecraft.class_11426;
import net.minecraft.class_11432;
import net.minecraft.class_11434;
import net.minecraft.class_11435;
import net.minecraft.class_11473;
import net.minecraft.class_11519;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_7923;
import net.minecraft.class_9288;
import net.minecraft.class_9290;
import net.minecraft.class_9334;

public final class gi {
   static final int b = 100;
   static final int A = 3;
   static final int N = 60;
   static final int Z = 100;
   static final int a = 12;
   static final int u = 5;
   static final int q = 192;
   static final int V = 16;
   static final int D = 6;
   static final int M = 3;
   private static final List<String> f = new ArrayList<>();
   private final String K;
   private gi.a H = gi.a.J;
   private gi.r R = gi.r.N;
   private wh.z h;
   private int j;
   private int s;
   private int z;
   private int I;
   private int U;
   private int E;
   private int C;
   private int w = -1000;
   private int J;
   private int g;
   private long T;
   private long n;
   private long m;
   private int d = -1;
   private int L = -1;
   private int G;
   private gi.v y;
   private final Set<String> S = new HashSet<>();
   private String X = "";
   private boolean i;
   private boolean P;
   private class_1792 c;
   private String e = "";
   private long Y = -1L;
   private boolean O;
   private boolean k;
   private final List<gi.v> B = new ArrayList<>();
   private final List<gi.v> F = new ArrayList<>();
   private int r;
   private int v;
   private boolean x;
   private long o = -1L;

   gi(String var1) {
      this.K = var1;
   }

   public static void g(class_2561 var0) {
      if (var0 != null) {
         synchronized (f) {
            if (f.size() < 64) {
               f.add(var0.getString());
            }
         }
      }
   }

   public void u(wh.z var1, int var2, long var3, long var5) {
      this.c = null;
      this.e = "";
      this.Y = -1L;
      this.O = (boolean)0;
      this.t(var1, var2, var3, var5);
   }

   public void d(class_1792 var1, int var2, long var3, long var5, long var7) {
      this.c = var1;
      this.e = new class_1799(var1).method_7964().getString();
      this.Y = var5;
      this.O = (boolean)1;
      this.t(null, var2, var3 * 64L, var7);
   }

   private void t(wh.z var1, int var2, long var3, long var5) {
      this.h = var1;
      this.j = var2;
      this.T = var3;
      this.n = var5;
      this.s = this.z = this.I = this.g = 0;
      this.m = 0L;
      this.S.clear();
      this.y = null;
      this.X = "";
      this.d = this.L = -1;
      this.P = (boolean)0;
      this.i = (boolean)0;
      this.k = (boolean)(this.c != null ? 1 : 0);
      this.x = (boolean)0;
      this.B.clear();
      this.F.clear();
      this.r = this.v = 0;
      this.o = -1L;
      synchronized (f) {
         f.clear();
      }

      this.H = gi.a.D;
      this.m(gi.r.N);
      if (this.c != null) {
         q8.G(
            this.K,
            "Buying {} {} from /ah: at most {} a block, {} a listing, budget {}",
            var2,
            this.c(),
            wh.y(var3 / 64L),
            this.Y < 0L ? "no cap" : wh.y(this.Y),
            wh.y(var5)
         );
      } else {
         q8.G(this.K, "Buying {} {} from /ah: at most {} per 64, budget {}", var2, this.c(), wh.y(var3), wh.y(var5));
      }
   }

   public gi.a b() {
      return this.H;
   }

   public int U() {
      return this.s;
   }

   public long G() {
      return this.m;
   }

   public String n() {
      return this.X;
   }

   public boolean L() {
      return this.i;
   }

   public boolean x() {
      return this.H == gi.a.D;
   }

   public long u() {
      return this.o;
   }

   public void O(String var1) {
      if (this.H == gi.a.D) {
         this.c(gi.a.a, var1);
      }
   }

   private void m(gi.r var1) {
      this.R = var1;
      this.U = 0;
      this.E = 0;
      this.C = 0;
   }

   private String c() {
      return this.c != null ? this.e : (this.h == wh.z.Z ? "Flight Duration 3 rockets" : "XP bottles");
   }

   private String b() {
      return this.c != null ? wh.W(this.e) : wh.O(this.h);
   }

   public gi.a Z() {
      if (this.H != gi.a.D) {
         return this.H;
      }

      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1761 != null && var1.method_1562() != null) {
         this.J++;
         this.U++;
         if (!var1.field_1724.field_7512.method_34255().method_7960() && this.R != gi.r.I && this.R != gi.r.j && this.J - this.w > 40) {
            return this.c(gi.a.a, "the cursor holds an item");
         }

         class_437 var2 = cm.j();
         String var3 = var2 == null ? "" : var2.method_25440().getString();
         class_1703 var4 = var1.field_1724.field_7512;
         int var5 = var4 instanceof class_1707 var6 && var6.method_17388() == 6 && wh.b(var3) > 0 ? 1 : 0;
         switch (this.R) {
            case N:
               this.o(var1, var2 != null, (boolean)var5);
               break;
            case n:
            case i:
               if (var5 == 0) {
                  if (var2 != null && !this.P) {
                     return this.c(gi.a.a, "another menu opened: " + var3);
                  }

                  if (var2 == null && this.U > 50) {
                     this.m(gi.r.N);
                     return this.H;
                  }

                  if (var2 != null) {
                     return this.c(gi.a.a, "an unexpected menu opened: " + var3);
                  }

                  return this.H;
               }

               if (!this.Y(var4)) {
                  return this.H;
               }

               int var8 = wh.b(var3);
               if (this.R == gi.r.i && var8 <= this.L) {
                  if (this.U > 100) {
                     return this.c(gi.a.a, "the next page did not open");
                  }

                  return this.H;
               }

               this.d = var4.field_7763;
               this.L = var8;
               if (this.R == gi.r.i) {
                  this.m(gi.r.n);
               }

               this.W(var1, (class_1707)var4, var8);
               break;
            case I:
               if (var2 instanceof class_11473 var7) {
                  this.L(var1, var7, var3);
               } else {
                  this.S(var1, var2 != null, (boolean)var5, var3, var4);
               }
               break;
            case j:
               this.k(var1, (boolean)var5, var2 != null, var3);
         }

         return this.H;
      } else {
         return this.c(gi.a.a, "the world went away");
      }
   }

   private void o(class_310 var1, boolean var2, boolean var3) {
      if (var3 && this.P && !this.x) {
         this.m(gi.r.n);
      } else if (this.U == 1) {
         this.x = (boolean)0;
         if (var2) {
            var1.field_1724.method_7346();
         }

         if (++this.z > 12 + this.g) {
            this.c(gi.a.a, "searched /ah " + this.z + " times without finishing");
         } else {
            this.I = 0;
            var1.field_1724.field_3944.method_45730(this.b());
            this.P = (boolean)1;
            q8.v(this.K, "Sent /{}", this.b());
         }
      } else if (var3) {
         this.m(gi.r.n);
      } else {
         if (this.U > 100) {
            this.c(gi.a.a, "/ah did not open");
         }
      }
   }

   private boolean Y(class_1703 var1) {
      int var2 = var1.field_7763 * 31 + var1.method_37421();

      for (int var3 = 0; var3 < 54 && var3 < var1.field_7761.size(); var3++) {
         class_1799 var4 = var1.method_7611(var3).method_7677();
         var2 = 31 * var2 + class_1799.method_57355(var4) * 7 + var4.method_7947();
      }

      if (var2 != this.C) {
         this.C = var2;
         this.E = 0;
         return false;
      } else {
         return ++this.E >= 3;
      }
   }

   private void W(class_310 var1, class_1707 var2, int var3) {
      if (this.s >= this.j) {
         this.c(gi.a.X, "");
      } else if (this.g >= 192) {
         this.c(gi.a.X, "at the purchase limit for one stop");
      } else if (this.J - this.w >= 6) {
         if (this.c != null) {
            this.c(var1, var2, var3);
         } else if (h(var1) == 0) {
            this.c(this.s > 0 ? gi.a.X : gi.a.a, "no free hotbar slot for more " + this.c());
         } else {
            gi.v var4 = null;
            long var5 = Long.MAX_VALUE;

            for (int var7 = 0; var7 < 45; var7++) {
               gi.v var8 = this.q(var2, var7, var3);
               if (var8 != null
                  && !this.S.contains(G(var8))
                  && wh.x(var8.price(), var8.count(), this.T)
                  && (this.n < 0L || this.m + var8.price() <= this.n)
                  && var8.count() <= this.j - this.s + 63) {
                  long var9 = var8.price() * 64L / var8.count();
                  if (var9 < var5) {
                     var5 = var9;
                     var4 = var8;
                  }
               }
            }

            if (var4 != null) {
               this.R(var1, var2, var4, var3);
            } else {
               int var11 = this.I(var2, wh.d.H);
               if (var11 >= 0 && ++this.I <= 5) {
                  this.T(var1, var2, var11);
                  this.m(gi.r.i);
               } else {
                  this.c(
                     this.s > 0 ? gi.a.X : gi.a.a,
                     this.n >= 0L && this.m > 0L
                        ? "no listing fits the price limit and the budget left"
                        : "no listing under the price limit (" + wh.y(this.T) + " per 64)"
                  );
               }
            }
         }
      }
   }

   private void c(class_310 var1, class_1707 var2, int var3) {
      int var4 = this.L(var1);
      int var5 = Math.min(this.j - this.s, var4);
      if (var5 <= 0) {
         this.c(this.s > 0 ? gi.a.X : gi.a.a, "no free inventory room for more " + this.c());
      } else {
         int var6 = this.I(var2, wh.d.H);
         if (this.k) {
            long var17 = 0L;

            for (int var18 = 0; var18 < 45; var18++) {
               class_1799 var10 = var2.method_7611(var18).method_7677();
               if (!var10.method_7960()) {
                  var17 = Math.max(var17, wh.t(H(var10)));
               }

               gi.v var11 = this.q(var2, var18, var3);
               if (var11 != null) {
                  long var12 = (var11.price() + var11.count() - 1L) / var11.count();
                  if (this.o < 0L || var12 < this.o) {
                     this.o = var12;
                  }

                  if (wh.x(var11.price(), var11.count(), this.T) && (this.Y < 0L || var11.price() <= this.Y) && !this.B.contains(var11)) {
                     this.B.add(var11);
                  }
               }
            }

            int var19 = new class_1799(this.c).method_7914();
            long var20 = 0L;

            for (gi.v var13 : this.B) {
               if (var17 > 0L && var13.price() * var19 <= var17 * var13.count()) {
                  var20 += var13.count();
               }
            }

            if (var20 < var5 && var6 >= 0 && this.I < 16) {
               this.I++;
               this.T(var1, var2, var6);
               this.m(gi.r.i);
            } else {
               this.k = (boolean)0;
               this.v++;
               this.F.clear();
               this.F.addAll(g(this.B, var5, var4, this.n < 0L ? -1L : this.n - this.m));
               if (this.F.isEmpty()) {
                  this.c(this.s > 0 ? gi.a.X : gi.a.a, this.X());
               } else {
                  this.r = 0;
                  long var22 = 0L;
                  int var14 = 0;

                  for (gi.v var16 : this.F) {
                     this.r = Math.max(this.r, var16.page());
                     var22 += var16.price();
                     var14 += var16.count();
                  }

                  q8.G(
                     this.K,
                     "Read {} pages of /ah for {}: {} listings under the caps, cheapest {} each; plan {} for {} in {} listings ({} each){}",
                     var3,
                     this.c(),
                     this.B.size(),
                     this.o < 0L ? "none" : wh.y(this.o),
                     var14,
                     wh.y(var22),
                     this.F.size(),
                     wh.y((var22 + var14 - 1L) / var14),
                     var14 < var5 ? "; " + var5 + " were needed, the budget covers no more" : ""
                  );
                  this.x = (boolean)1;
                  this.m(gi.r.N);
               }
            }
         } else {
            for (int var7 = 0; var7 < 45; var7++) {
               gi.v var8 = this.q(var2, var7, var3);
               if (var8 != null && !this.S.contains(G(var8))) {
                  int var9 = this.j(var8);
                  if (var9 >= 0 && (this.n < 0L || this.m + var8.price() <= this.n)) {
                     this.F.remove(var9);
                     this.R(var1, var2, var8, var3);
                     return;
                  }
               }
            }

            if (!this.F.isEmpty() && var6 >= 0 && var3 < this.r + 1 && this.I < 16) {
               this.I++;
               this.T(var1, var2, var6);
               this.m(gi.r.i);
            } else if (this.v < 3) {
               q8.G(this.K, "{} planned listings of {} were gone; reading /ah again for the {} still needed", this.F.size(), this.c(), var5);
               this.k = (boolean)1;
               this.B.clear();
               this.F.clear();
               this.x = (boolean)1;
               this.m(gi.r.N);
            } else {
               this.c(this.s > 0 ? gi.a.X : gi.a.a, "the listings kept changing; bought " + this.s + " of " + this.j);
            }
         }
      }
   }

   private String X() {
      return this.n >= 0L && this.m > 0L
         ? "no listing fits the price limit and the budget left"
         : "no listing under "
            + wh.y(this.T / 64L)
            + " per block"
            + (this.Y >= 0L ? " and " + wh.y(this.Y) + " per listing" : "")
            + (this.o >= 0L ? " (the cheapest was " + wh.y(this.o) + " each)" : "");
   }

   private int j(gi.v var1) {
      for (int var2 = 0; var2 < this.F.size(); var2++) {
         if (this.F.get(var2).count() == var1.count() && this.F.get(var2).price() == var1.price()) {
            return var2;
         }
      }

      return -1;
   }

   private void R(class_310 var1, class_1707 var2, gi.v var3, int var4) {
      this.y = var3;
      this.G = this.b(var1);
      this.T(var1, var2, var3.slot());
      q8.G(this.K, "Buying {} {} for {} (page {}, slot {})", var3.count(), this.c(), wh.y(var3.price()), var4, var3.slot());
      this.m(gi.r.I);
   }

   static List<gi.v> g(List<gi.v> var0, int var1, int var2, long var3) {
      var1 = Math.min(var1, var2);
      if (var1 > 0 && !var0.isEmpty()) {
         long var5 = 2305843009213693951L;
         int var7 = var0.size();
         long[] var8 = new long[var2 + 1];
         Arrays.fill(var8, 2305843009213693951L);
         var8[0] = 0L;
         boolean[][] var9 = new boolean[var7][var2 + 1];

         for (int var10 = 0; var10 < var7; var10++) {
            int var11 = ((gi.v)var0.get(var10)).count();
            long var12 = ((gi.v)var0.get(var10)).price();
            if (var11 > 0 && var11 <= var2 && var12 > 0L) {
               for (int var14 = var2; var14 >= var11; var14--) {
                  if (var8[var14 - var11] != 2305843009213693951L) {
                     long var15 = var8[var14 - var11] + var12;
                     if (var15 < var8[var14]) {
                        var8[var14] = var15;
                        var9[var10][var14] = (boolean)1;
                     }
                  }
               }
            }
         }

         int var18 = -1;

         for (int var19 = var1; var19 <= var2; var19++) {
            if (var8[var19] < 2305843009213693951L && (var3 < 0L || var8[var19] <= var3) && (var18 < 0 || var8[var19] < var8[var18])) {
               var18 = var19;
            }
         }

         if (var18 < 0) {
            for (int var20 = var1 - 1; var20 > 0; var20--) {
               if (var8[var20] < 2305843009213693951L && (var3 < 0L || var8[var20] <= var3)) {
                  var18 = var20;
                  break;
               }
            }
         }

         if (var18 < 0) {
            return List.of();
         }

         ArrayList var21 = new ArrayList();
         int var22 = var7 - 1;

         for (int var13 = var18; var22 >= 0 && var13 > 0; var22--) {
            if (var9[var22][var13]) {
               var21.add((gi.v)var0.get(var22));
               var13 -= ((gi.v)var0.get(var22)).count();
            }
         }

         var21.sort(Comparator.comparingInt(gi.v::page).thenComparingInt(gi.v::slot));
         return var21;
      } else {
         return List.of();
      }
   }

   private int L(class_310 var1) {
      int var2 = new class_1799(this.c).method_7914();
      int var3 = 0;
      class_1661 var4 = var1.field_1724.method_31548();

      for (int var5 = 0; var5 < 36; var5++) {
         class_1799 var6 = var4.method_5438(var5);
         if (var6.method_7960()) {
            var3 += var2;
         } else if (this.H(var6) && var6.method_57380().method_57848()) {
            var3 += Math.max(0, var2 - var6.method_7947());
         }
      }

      return var3;
   }

   gi.v q(class_1707 var1, int var2, int var3) {
      class_1799 var4 = var1.method_7611(var2).method_7677();
      if (!this.H(var4)) {
         return null;
      }

      long var5 = wh.t(H(var4));
      return var5 <= 0L ? null : new gi.v(var2, var4.method_7947(), var5, var3);
   }

   private boolean H(class_1799 var1) {
      if (var1.method_7960()) {
         return false;
      } else if (this.c != null) {
         class_9288 var2 = (class_9288)var1.method_58694(class_9334.field_49622);
         return var1.method_31574(this.c) && (var2 == null || var2.method_59712().findAny().isEmpty());
      } else {
         return this.h != wh.z.Z
            ? var1.method_31574(class_1802.field_8287)
            : var1.method_31574(class_1802.field_8639) && com.larpclient.w.P(var1) && com.larpclient.w.E(var1) == 3;
      }
   }

   private static List<String> H(class_1799 var0) {
      class_9290 var1 = (class_9290)var0.method_58694(class_9334.field_49632);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (class_2561 var4 : var1.comp_2400()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String G(gi.v var0) {
      return var0.page() + ":" + var0.slot() + ":" + var0.count() + ":" + var0.price();
   }

   private int I(class_1707 var1, wh.d var2) {
      int var3 = -1;

      for (int var4 = 45; var4 < 54; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         if (!var5.method_7960() && wh.X(class_7923.field_41178.method_10221(var5.method_7909()).toString(), var5.method_7964().getString()) == var2) {
            if (var3 >= 0) {
               return -1;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void S(class_310 var1, boolean var2, boolean var3, String var4, class_1703 var5) {
      if (!this.b()) {
         if (this.b(var1) > this.G) {
            this.m(gi.r.j);
         } else if (var3) {
            if (this.U > 60) {
               this.S.add(G(this.y));
               q8.G(this.K, "No confirm screen for that listing; trying another");
               this.m(gi.r.n);
            }
         } else if (!var2) {
            if (this.U > 60) {
               this.c(gi.a.a, "the confirm screen never opened");
            }
         } else if (!this.Y(var5)) {
            if (this.U > 60) {
               this.c(gi.a.a, "the confirm screen kept changing");
            }
         } else if (this.J - this.w >= 6) {
            int var6 = -1;
            int var7 = 0;
            byte var8 = 0;
            String var9 = null;

            for (int var10 = 0; var10 < var5.field_7761.size(); var10++) {
               class_1735 var11 = var5.method_7611(var10);
               if (var11.field_7871 != var1.field_1724.method_31548()) {
                  class_1799 var12 = var11.method_7677();
                  if (!var12.method_7960()) {
                     String var13 = var12.method_7964().getString();
                     String var14 = class_7923.field_41178.method_10221(var12.method_7909()).toString();
                     if (wh.H(var13).contains("sell")) {
                        this.c(gi.a.a, "the confirm screen has a selling button: " + var4);
                        return;
                     }

                     if (this.H(var12)) {
                        long var15 = wh.t(H(var12));
                        if (var12.method_7947() != this.y.count() || var15 >= 0L && var15 != this.y.price()) {
                           this.c(gi.a.a, "the confirm screen shows " + var12.method_7947() + " for " + wh.y(var15) + ", not the chosen listing");
                           return;
                        }

                        var8 = 1;
                     } else {
                        wh.c var17 = wh.r(var14, var13);
                        if (var17 == wh.c.U) {
                           var7++;
                        }

                        if (var17 == wh.c.R) {
                           String var16 = var14 + "|" + wh.H(var13);
                           if (var9 != null && !var9.equals(var16)) {
                              this.c(gi.a.a, "the confirm screen has two different confirm buttons: " + var4);
                              return;
                           }

                           var9 = var16;
                           if (var6 < 0) {
                              var6 = var10;
                           }
                        }
                     }
                  }
               }
            }

            if (var8 != 0 && var6 >= 0 && var7 != 0) {
               this.T(var1, var5, var6);
               q8.v(this.K, "Confirmed on '{}' slot {}", var4, var6);
               this.m(gi.r.j);
            } else {
               q8.G(
                  this.K,
                  "Unrecognized confirm screen '{}': listing shown {}, confirm slot {}, cancel buttons {}",
                  var4,
                  Boolean.valueOf((boolean)var8),
                  var6,
                  var7
               );
               this.c(gi.a.a, "the confirm screen was not recognized (" + var4 + ")");
            }
         }
      }
   }

   private void L(class_310 var1, class_11473<?> var2, String var3) {
      if (!this.b()) {
         if (this.J - this.w >= 6) {
            class_11419 var4 = ((q313984e4ec8a45e1)var2).larp$dialog();
            class_11519 var5 = null;
            if (var4 instanceof class_11418 var6) {
               if (wh.O(var6.comp_4306().comp_4390().comp_4298().getString())) {
                  var5 = var6.comp_4306();
               }
            } else if (var4 instanceof class_11426 var7 && var7.comp_4317().size() == 2) {
               String var8 = ((class_11519)var7.comp_4317().get(0)).comp_4390().comp_4298().getString();
               String var9 = ((class_11519)var7.comp_4317().get(1)).comp_4390().comp_4298().getString();
               if (wh.O(var8) && wh.k(var9)) {
                  var5 = (class_11519)var7.comp_4317().get(0);
               } else if (wh.k(var8) && wh.O(var9)) {
                  var5 = (class_11519)var7.comp_4317().get(1);
               }
            }

            if (var4 != null && var5 != null) {
               class_11417 var17 = var4.comp_4305();
               String var18 = wh.H(var17.comp_4301().getString());
               if (!wh.y(var18)) {
                  this.c(gi.a.a, "an unexpected dialog opened: " + var17.comp_4301().getString());
               } else if (!var17.comp_4394().isEmpty()) {
                  this.c(gi.a.a, "the purchase dialog asks for input");
               } else {
                  int var19 = 0;
                  long var20 = -1L;

                  for (class_11432 var12 : var17.comp_4304()) {
                     if (var12 instanceof class_11434 var13) {
                        class_1799 var21 = var13.comp_4324();
                        if (!this.H(var21) || var21.method_7947() != this.y.count()) {
                           this.c(
                              gi.a.a, "the purchase dialog shows " + var21.method_7947() + " " + var21.method_7964().getString() + ", not the chosen listing"
                           );
                           return;
                        }

                        var19++;
                        if (var13.comp_4325().isPresent()) {
                           var20 = Math.max(var20, wh.t(List.of(((class_11435)var13.comp_4325().get()).comp_4330().getString())));
                        }
                     } else if (var12 instanceof class_11435 var14) {
                        long var15 = wh.t(List.of(var14.comp_4330().getString()));
                        if (var15 >= 0L) {
                           var20 = var15;
                        }
                     }
                  }

                  if (var19 == 1 && var20 == this.y.price()) {
                     var2.method_72141(var5.comp_4391().flatMap(var0 -> var0.method_72068(Map.of())));
                     this.w = this.J;
                     q8.G(this.K, "Answered Yes to '{}' for {} {} at {}", var17.comp_4301().getString(), this.y.count(), this.c(), wh.y(this.y.price()));
                     this.m(gi.r.j);
                  } else {
                     q8.G(this.K, "Purchase dialog '{}' did not match: items {}, price {} for listing {}", var17.comp_4301().getString(), var19, var20, this.y);
                     this.c(gi.a.a, "the purchase dialog did not show the chosen listing and its price");
                  }
               }
            } else {
               q8.G(this.K, "Unrecognized dialog '{}': {}", var3, var4 == null ? "none" : var4.getClass().getSimpleName() + " " + var4);
               this.c(gi.a.a, "an unexpected dialog opened: " + var3);
            }
         }
      }
   }

   private void k(class_310 var1, boolean var2, boolean var3, String var4) {
      if (!this.b()) {
         int var5 = this.b(var1);
         if (var5 >= this.G + this.y.count() || var5 > this.G && this.U > 20) {
            int var6 = var5 - this.G;
            this.s += var6;
            this.g++;
            this.m = this.m + this.y.price();
            this.S.add(G(this.y));
            q8.G(this.K, "Bought {} {} for {}; {} of {} wanted, {} spent", var6, this.c(), wh.y(this.y.price()), this.s, this.j, wh.y(this.m));
            this.y = null;
            if (this.s >= this.j) {
               this.c(gi.a.X, "");
            } else {
               this.m(var2 ? gi.r.n : gi.r.N);
            }
         } else if (var3 && !var2 && this.U > 10 && !wh.H(var4).isEmpty()) {
            if (this.U > 100) {
               this.c(gi.a.a, "the purchase was not confirmed (still on " + var4 + ")");
            }
         } else {
            if (this.U > 100) {
               this.c(gi.a.a, "the items never arrived after confirming");
            }
         }
      }
   }

   private boolean b() {
      List var1;
      synchronized (f) {
         var1 = List.copyOf(f);
         f.clear();
      }

      for (String var3 : var1) {
         wh.m var4 = wh.f(var3);
         if (var4 != wh.m.k) {
            q8.G(this.K, "Auction reply ({}): {}", var4, var3);
            switch (var4) {
               case E:
                  this.S.add(G(this.y));
                  this.y = null;
                  this.m(gi.r.N);
                  return true;
               case D:
                  this.i = (boolean)1;
                  this.c(this.s > 0 ? gi.a.X : gi.a.a, "not enough money");
                  return true;
               case j:
                  this.c(this.s > 0 ? gi.a.X : gi.a.a, "the inventory is full");
                  return true;
               case q:
                  long var5 = wh.z(var3);
                  if (this.y != null && var5 > this.y.price()) {
                     this.c(gi.a.a, "the receipt says " + wh.y(var5) + ", more than the listing's " + wh.y(this.y.price()));
                     return true;
                  }
            }
         }
      }

      return false;
   }

   private void T(class_310 var1, class_1703 var2, int var3) {
      class_1799 var4 = var2.method_7611(var3).method_7677();
      if (wh.H(var4.method_7964().getString()).contains("sell")) {
         this.c(gi.a.a, "refused to click a selling button");
      } else {
         var1.field_1761.method_2906(var2.field_7763, var3, 0, class_1713.field_7790, var1.field_1724);
         this.w = this.J;
      }
   }

   private int b(class_310 var1) {
      int var2 = 0;
      class_1661 var3 = var1.field_1724.method_31548();

      for (int var4 = 0; var4 < 36; var4++) {
         if (this.H(var3.method_5438(var4))) {
            var2 += var3.method_5438(var4).method_7947();
         }
      }

      if (this.H(var1.field_1724.method_6079())) {
         var2 += var1.field_1724.method_6079().method_7947();
      }

      return var2;
   }

   private static int v(class_310 var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (var0.field_1724.method_31548().method_5438(var2).method_7960()) {
            var1++;
         }
      }

      return var1;
   }

   private static int h(class_310 var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.field_1724.method_31548().method_5438(var2).method_7960()) {
            var1++;
         }
      }

      return var1;
   }

   private gi.a c(gi.a var1, String var2) {
      this.H = var1;
      this.X = var2 == null ? "" : var2;
      class_310 var3 = class_310.method_1551();
      if (this.P
         && var3.field_1724 != null
         && var3.field_1724.field_7512 != var3.field_1724.field_7498
         && var3.field_1724.field_7512.method_34255().method_7960()) {
         var3.field_1724.method_7346();
      }

      this.P = (boolean)0;
      q8.G(this.K, "Buying {} ended: {}{}; bought {} for {}", this.c(), var1, this.X.isEmpty() ? "" : " (" + this.X + ")", this.s, wh.y(this.m));
      return this.H;
   }

   public enum a {
      J,
      D,
      X,
      a;
   }

   private enum r {
      N,
      n,
      i,
      I,
      j;
   }

   record v(int slot, int count, long price, int page) {
   }
}
