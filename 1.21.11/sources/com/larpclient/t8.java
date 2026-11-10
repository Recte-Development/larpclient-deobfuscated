package com.larpclient;

import com.larpclient.module.AutoSellModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_7923;

public final class t8 extends t1 {
   private final class_437 u;
   private final AutoSellModule w;
   private final boolean D;
   private final LinkedHashSet<String> Y;
   private final List<t8.w> e = new ArrayList<>();
   private final List<lc> l = new ArrayList<>();
   private List<t8.w> O = List.of();
   private lo f;
   private ls X;
   private ls r;
   private ls d;
   private ls B;
   private ls Z;
   private ls P;
   private ls I;
   private String p = "";
   private String F = "";
   private boolean g;
   private boolean c;
   private int U;
   private int E;
   private int M;
   private int C;
   private int N;
   private int a;
   private int j;
   private int H;
   private int x;
   private Set<String> m = Set.of();
   private Map<String, Integer> L = Map.of();
   private final qb A = new qb(0.0);

   public t8(class_437 var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.u = var1;
      this.w = var2;
      this.D = var3;
      this.Y = new LinkedHashSet<>(var2.L(var3));

      for (class_1792 var5 : class_7923.field_41178) {
         class_1799 var6 = new class_1799(var5);
         if (!var6.method_7960()) {
            String var7 = class_7923.field_41178.method_10221(var5).toString();
            String var8 = var6.method_7964().getString();
            this.e.add(new t8.w(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.Y) {
         if (this.e.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.e.add(new t8.w(var10, var10 + " (unavailable)", class_1799.field_8037, var10.replace('_', ' ')));
         }
      }

      this.e.sort(Comparator.comparing(t8.w::name, String.CASE_INSENSITIVE_ORDER).thenComparing(t8.w::id));
      this.G();
      this.A.a(1.0, 200);
      q8.v("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.Y.size());
   }

   private void G() {
      HashSet var1 = new HashSet();
      if (this.field_22787.field_1724 != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            class_1799 var3 = this.field_22787.field_1724.method_31548().method_5438(var2);
            if (!var3.method_7960()) {
               var1.add(class_7923.field_41178.method_10221(var3.method_7909()).toString());
            }
         }
      }

      this.m = Set.copyOf(var1);
   }

   protected void method_25426() {
      this.t();
      this.l.clear();
      this.M = Math.max(1, Math.min(700, this.field_22789 - 24));
      this.C = Math.max(1, Math.min(500, this.field_22790 - 20));
      this.U = (this.field_22789 - this.M) / 2;
      this.E = (this.field_22790 - this.C) / 2;
      this.j = this.M >= 440 ? 2 : 1;
      this.N = this.E + 111;
      this.a = this.E + this.C - 66;
      this.H = Math.max(1, (this.a - this.N) / 34);
      this.f = new lo("Search items or namespace:item...", var1x -> {
         this.p = var1x;
         this.x = 0;
         this.R();
      }, 96);
      this.f.h = this.p;
      this.f.bounds(this.U + 12, this.E + 40, this.M - 24);
      this.f.w = 24;
      this.K.add(this.f);
      this.X = this.o("In inventory", () -> {
         this.G();
         this.g = (boolean)(!this.g ? 1 : 0);
         this.x = 0;
         this.R();
      });
      this.r = this.o("Selected", () -> {
         this.c = (boolean)(!this.c ? 1 : 0);
         this.x = 0;
         this.R();
      });
      this.d = this.o("Add held", () -> {
         if (this.field_22787.field_1724 != null && !this.field_22787.field_1724.method_6047().method_7960()) {
            String var1x = class_7923.field_41178.method_10221(this.field_22787.field_1724.method_6047().method_7909()).toString();
            this.b(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.Y);
               var2x.add(var1x);
               this.V(var2x);
            });
         } else {
            this.F = "Hold an item first.";
         }
      });
      this.B = this.o("Select results", () -> this.b(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.Y);
         this.O.forEach(var1xx -> var1x.add(var1xx.id));
         this.V(var1x);
      }));
      this.Z = this.o("Clear", () -> this.b(this.Y::clear));
      this.P = this.o("Cancel", this::method_25419);
      this.I = this.o("Done", () -> {
         try {
            this.w.A(this.D, this.Y);
            this.w.T();
            q8.v("AutoSell", "Saved {} item selection; size={}", this.D ? "excluded" : "selected", this.Y.size());
            this.field_22787.method_1507(this.u);
         } catch (IllegalArgumentException var2x) {
            this.F = var2x.getMessage();
         }
      });
      this.I.v = (boolean)1;
      int var1 = (this.M - 32) / 3;
      this.X.bounds(this.U + 12, this.E + 73, var1);
      this.r.bounds(this.U + 16 + var1, this.E + 73, var1);
      this.d.bounds(this.U + 20 + var1 * 2, this.E + 73, var1);
      int var2 = this.E + this.C - 31;
      int var3 = (this.M - 36) / 4;
      this.B.bounds(this.U + 12, var2, var3);
      this.Z.bounds(this.U + 16 + var3, var2, var3);
      this.P.bounds(this.U + 20 + var3 * 2, var2, var3);
      this.I.bounds(this.U + 24 + var3 * 3, var2, var3);
      this.R();
      this.G = this.f;
      this.f.M = (boolean)1;
   }

   private ls o(String var1, Runnable var2) {
      ls var3 = new ls(var1, var2);
      this.K.add(var3);
      return var3;
   }

   private void V(Collection<String> var1) {
      List var2 = i8.O(var1);
      this.Y.clear();
      this.Y.addAll(var2);
   }

   private void b(Runnable var1) {
      try {
         var1.run();
         this.F = "";
         this.R();
      } catch (IllegalArgumentException var3) {
         this.F = var3.getMessage();
      }
   }

   private void R() {
      String[] var1 = this.p.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.O = this.e
         .stream()
         .filter(var1x -> !this.g || this.m.contains(var1x.id))
         .filter(var1x -> !this.c || this.Y.contains(var1x.id))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.Y.contains(var1x.id)))
         .toList();
      this.x = Math.clamp(this.x, 0, Math.max(0, (this.O.size() + this.j - 1) / this.j - this.H));
      this.X.o = this.g;
      this.r.o = this.c;
      this.r();
      this.h();
   }

   private void r() {
      i8.e var1 = this.w.j();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      i8.e var3 = new i8.e(
         var2 != 0 ? i8.u.getFirst() : var1.mode(),
         this.D ? var1.selected() : this.Y,
         this.D ? this.Y : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.field_22787.field_1724 != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            i8.w var6 = AutoSellModule.T(this.field_22787.field_1724.method_31548().method_5438(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.L = Map.copyOf(var4);
   }

   private void h() {
      if (this.G != null && this.l.contains(this.G)) {
         this.G.blur();
         this.G = null;
      }

      this.K.removeAll(this.l);
      this.l.clear();
      int var1 = (this.M - 28) / this.j;

      for (int var2 = this.x * this.j; var2 < Math.min(this.O.size(), (this.x + this.H) * this.j); var2++) {
         t8.s var3 = new t8.s(this.O.get(var2));
         int var4 = var2 - this.x * this.j;
         var3.bounds(this.U + 12 + var4 % this.j * var1, this.N + var4 / this.j * 34, var1 - 4);
         var3.w = 30;
         this.l.add(var3);
         this.K.add(var3);
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      double var5 = this.A.a(1.0, 200);
      gu.Z(var1, 0, 0, this.field_22789, this.field_22790, -1609560044);

      try (fp var7 = fp.O(var5)) {
         com.larpclient.p.W(var1, this.U, this.E, this.M, this.C, ez.V, ez.d, 0.6);
         uw.X(var1, this.D ? "Never sell" : "Choose sell items", this.U + 12, this.E + 12, ez.H, uw.c.v);
         String var8 = this.Y.size() + " / 32";
         uw.G(var1, var8, this.U + this.M - 12 - uw.r(var8), this.E + 16, ez.PH);

         for (lc var10 : this.K) {
            if (!this.l.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.method_44379(this.U + 8, this.N, this.U + this.M - 8, this.a);

         try {
            for (lc var20 : this.l) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.method_44380();
         }

         if (this.O.isEmpty()) {
            uw.G(var1, "No matching items", this.U + 16, this.N + 12, ez.PH);
         }

         int var19 = (this.O.size() + this.j - 1) / this.j * 34;
         com.larpclient.p.t(var1, this.U + this.M - 7, this.N, Math.max(1, this.a - this.N), var19, this.x * 34);
         String var21 = !this.F.isEmpty()
            ? this.F
            : this.O.size() + " results, " + this.L.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         uw.X(var1, uw.X(var21, this.M - 24, uw.c.M), this.U + 12, this.E + this.C - 50, this.F.isEmpty() ? ez.PH : -24915, uw.c.M);
      }
   }

   @Override
   protected boolean accepts(lc var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.l.contains(var1) || var4 >= this.N && var4 < this.a);
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      return super.method_25402(var1, var2);
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.U) && !(var1 >= this.U + this.M) && !(var3 < this.N) && !(var3 >= this.a)) {
         this.x = this.x - (int)Math.signum(var7) * 2;
         this.R();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      if (s2.i(var1) == 256) {
         this.method_25419();
         return true;
      } else if (this.G != this.f && (s2.i(var1) == 266 || s2.i(var1) == 267 || s2.i(var1) == 264 || s2.i(var1) == 265)) {
         this.x = this.x + (s2.i(var1) == 266 ? -this.H : (s2.i(var1) == 267 ? this.H : (s2.i(var1) == 264 ? 1 : -1)));
         this.R();
         return true;
      } else {
         return super.method_25404(var1);
      }
   }

   public void method_25419() {
      q8.v("AutoSell", "Item picker draft discarded");
      this.field_22787.method_1507(this.u);
   }

   private final class s extends lc {
      private final t8.w L;
      private final qb b = new qb(0.0);

      s(t8.w var2) {
         this.L = var2;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         boolean var4 = t8.this.Y.contains(this.L.id);
         double var5 = this.b.a(!this.contains(var2, var3) && !this.M ? 0.0 : 1.0, 160);
         com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, ez.m, ei.W(var4 ? ez.PC : ez.B, ez.K, var5 * 0.6));
         if (!this.L.icon.method_7960()) {
            var1.method_51427(this.L.icon, this.D + 7, this.g + 7);
         }

         uw.G(var1, uw.J(this.L.name, this.d - 54), this.D + 29, this.g + 5, ez.H);
         uw.X(var1, uw.X(this.L.id, this.d - 54, uw.c.M), this.D + 29, this.g + 18, ez.PH, uw.c.M);
         if (var4) {
            uw.G(var1, "+", this.D + this.d - 15, this.g + 10, ez.P(this.D));
         }

         if (this.M && this.q) {
            com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, ez.m);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.L.id
               + (t8.this.m.contains(this.L.id) ? " | " + t8.this.L.getOrDefault(this.L.id, 0) + " eligible inventory stacks" : "")
               + (t8.this.w.L(!t8.this.D).contains(this.L.id) ? (t8.this.D ? " | Also selected for selling; exclusion wins" : " | Excluded from selling") : "");
            var1.method_71276(class_2561.method_43470(var7), var2, var3);
         }
      }

      private void s() {
         boolean var1 = this.q;
         t8.this.b(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(t8.this.Y);
            if (!var1x.remove(this.L.id)) {
               var1x.add(this.L.id);
            }

            t8.this.V(var1x);
         });

         for (lc var3 : t8.this.l) {
            if (var3 instanceof t8.s var4 && var4.L.id.equals(this.L.id)) {
               t8.this.G = var4;
               var4.M = (boolean)1;
               var4.q = var1;
               break;
            }
         }

         c_.b(c_.d.q);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.s();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.M && (var1 == 257 || var1 == 32)) {
            this.s();
            return true;
         } else {
            return false;
         }
      }
   }

   private record w(String id, String name, class_1799 icon, String search) {
   }
}
