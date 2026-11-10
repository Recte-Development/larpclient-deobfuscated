package com.larpclient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;

public final class cp {
   private final BooleanSupplier w;
   private final Consumer<k.l> b;
   private final Runnable p;
   private k.l a;
   private k.l O;
   private cv.f D;
   private cv.b V;
   private hz.v d;
   private hz.s l;
   private cv.z s;
   private int J = -1;
   private int U = -1;
   private long e = hz.o;
   private boolean v;
   private boolean Q;
   private double n;
   private double N;
   private String S = "";
   private boolean H;
   private final ua I = new ua(() -> this.v = (boolean)1, this::g);
   private final u1 i = new u1();
   private double L;
   private double t;
   private boolean j;
   private long c;
   private boolean W;
   private Predicate<k.l> g = var0 -> false;
   private int y;
   private int R;
   private int G = -1;

   public cp() {
      this(
         () -> {
            qk var0 = LarpClient.b().e();
            return var0.d(var0.d());
         },
         var0 -> {
            class_310 var1 = class_310.method_1551();
            var1.method_1507(tk.V(var1.field_1755, var0));
         },
         () -> class_310.method_1551()
            .field_1705
            .method_1743()
            .method_1812(class_2561.method_43470("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.H = (boolean)1;
      this.g = var0 -> LarpClient.b().e().F().j(var0.s);
   }

   public cp(BooleanSupplier var1, Consumer<k.l> var2, Runnable var3) {
      this.w = var1;
      this.b = var2;
      this.p = var3;
   }

   public void b(Predicate<k.l> var1) {
      this.W = (boolean)1;
      this.g = Objects.requireNonNull(var1);
   }

   public void H(k.l var1) {
      if (this.O()) {
         this.U();
      }

      this.a = var1;
      this.s = null;
   }

   public int Z() {
      return this.J;
   }

   public int O() {
      return this.U;
   }

   public k.l r() {
      return this.a;
   }

   public void o() {
      this.s = null;
      this.I.z();
   }

   public boolean O() {
      return this.O != null;
   }

   public boolean B(double var1, double var3, int var5, int var6, List<k.i> var7) {
      if (!this.O() && (this.s == null || !this.s.contains(var1, var3, 0))) {
         if (this.H && this.I.q() && this.I.j().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.W || !var1x.element().q()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.H
               && k.A()
                  .stream()
                  .filter(var1x -> !this.g.test(var1x) && !k.k(var1x))
                  .anyMatch(var7x -> k.o(var8, var7x).map(var6x -> cv.n(var6x, var5, var6).contains(var1, var3, 0)).orElse(false));
      } else {
         return true;
      }
   }

   public boolean F(double var1, double var3, int var5, int var6, int var7, List<k.i> var8, boolean var9) {
      if (this.O()) {
         return true;
      }

      if (var9) {
         this.s = null;
         return false;
      }

      if (this.H && this.I.K(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.s != null && this.s.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.s.y()) / 20;
         this.s = null;
         switch (var15) {
            case 0:
               this.a.a(true);
               this.v = (boolean)1;
               this.g();
               this.a = null;
               break;
            case 1:
               this.g();
               this.b.accept(this.a);
               break;
            case 2:
               this.a.x();
               this.v = (boolean)1;
               this.g();
               break;
            case 3:
               this.g();
         }

         return true;
      } else {
         this.s = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.W || !var1x.element().q()).toList();
         Optional var11 = k.o(var10, this.a);
         k.l var12 = this.H && var5 == 0
            ? k.A()
               .stream()
               .filter(var1x -> !this.g.test(var1x) && !k.k(var1x))
               .filter(var7x -> k.o(var10, var7x).map(var6x -> cv.n(var6x, var6, var7).contains(var1, var3, 0)).orElse(false))
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.a = var12;
            var11 = k.o(var10, this.a);
         }

         if (var5 == 0
            && this.a != null
            && !this.g.test(this.a)
            && !k.k(this.a)
            && var11.isPresent()
            && cv.n((cv.z)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.x(this.a);
            this.y = this.a.l(var6);
            this.R = this.a.M(var7);
            cv.z var16 = (cv.z)var11.get();
            this.V = new cv.b(var16, var1, var3, this.a.o());
            this.n = (var16.x() - this.a.l(var6)) / this.a.o();
            this.N = (var16.y() - this.a.M(var7)) / this.a.o();
            return true;
         }

         k.i var13 = cv.q(var10, var0 -> true, k.i::bounds, var1, var3);
         if (var13 == null) {
            this.a = null;
            if (this.H && var5 == 1) {
               this.I.v((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.a = var13.element();
            q8.v("HUD editor", "Selected {} in {}", this.a.s, this.W ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.H) {
                  this.I.R(this.a, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.S.isEmpty() ? 60 : 80;
               this.s = new cv.z(cv.o((int)var1, 142, var6), cv.o((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
            } else {
               if (this.g.test(this.a)) {
                  return true;
               }

               this.x(this.a);
               if (k.k(this.a)) {
                  this.a.e(var13.bounds().x(), var13.bounds().y(), var6, var7);
                  this.a.w(null);
                  k.J(this.a);
                  q8.v("HUD editor", "Lifted {} out of {}", this.a.s, k.W(this.a.s));
               }

               this.y = this.a.l(var6);
               this.R = this.a.M(var7);
               this.D = new cv.f(var1 - this.a.l(var6), var3 - this.a.M(var7));
               this.i.z(this.a.l(var6), this.a.M(var7));
               this.L = var1;
               this.t = var3;
               this.j = (boolean)0;
               this.c = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void x(k.l var1) {
      this.O = var1;
      this.G = -1;
      this.d = hz.J.get(var1.s);
      this.l = hz.S.get(var1.s);
   }

   public boolean f(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.c(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean c(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.H && var5 == 0 && this.I.q(var1, var3, var6, var7)) {
         return true;
      }

      if (this.O() && var5 == 0) {
         if (this.V != null) {
            this.O.V(this.H ? u1.l(this.V, var1, var3) : this.V.scale(var1, var3));
            this.O.e((int)Math.round(this.V.bounds().x() - this.n * this.O.o()), (int)Math.round(this.V.bounds().y() - this.N * this.O.o()), var6, var7);
         } else {
            if (this.H) {
               this.L = var1;
               this.t = var3;
               this.j = var8;
               return true;
            }

            cv.s var10 = this.D.move(var1, var3, this.O.I(var6, var7), var6, var7, hz.l.snapping(), hz.l.grid(), var8, this.J, this.U);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.W && var9) {
               int var13 = var11 - this.y;
               int var14 = var12 - this.R;
               if (this.G < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.G = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.G == 0) {
                  var12 = this.R;
               } else if (this.G == 1) {
                  var11 = this.y;
               }
            } else {
               this.G = -1;
            }

            this.O.e(var11, var12, var6, var7);
            this.J = var9 && this.G == 1 ? -1 : var10.guideX();
            this.U = var9 && this.G == 0 ? -1 : var10.guideY();
            this.i(var6, var7, var8);
         }

         return true;
      } else {
         return false;
      }
   }

   private void i(int var1, int var2, boolean var3) {
      if (this.O != null && k.W(this.O.s) != null) {
         int var4 = this.V == null && !var3 && k.J(this.O, var1, var2) ? 1 : 0;
         k.J(var4 != 0 ? this.O : null);
         if (var4 != 0) {
            this.J = this.U = -1;
         }
      }
   }

   public boolean u(int var1) {
      if (this.H && var1 == 0 && this.I.A()) {
         return true;
      } else if (this.O() && var1 == 0) {
         this.U();
         return true;
      } else {
         return false;
      }
   }

   private void U() {
      if (this.O != null && k.e() == this.O) {
         String var1 = k.W(this.O.s);
         this.O.w(var1);
         q8.G("HUD editor", "Merged {} into {}", this.O.s, var1);
      }

      k.J(null);
      if (this.O != null) {
         this.v = (boolean)(this.v | (Objects.equals(this.d, hz.J.get(this.O.s)) && Objects.equals(this.l, hz.S.get(this.O.s)) ? 0 : 1));
         q8.v("HUD editor", "Released {} gesture for {}", this.V == null ? "move" : "scale", this.O.s);
      }

      this.O = null;
      this.D = null;
      this.V = null;
      this.d = null;
      this.l = null;
      this.J = this.U = -1;
      this.g();
   }

   private void g() {
      if (this.v) {
         if (this.w.getAsBoolean()) {
            this.v = (boolean)0;
            this.S = "";
            q8.v("HUD editor", "Committed {} edit to active profile", this.W ? "workspace" : "chat");
         } else {
            if (this.S.isEmpty()) {
               this.p.run();
            }

            this.S = "HUD save failed. Retry save in the menu.";
            q8.L("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void P(boolean var1, boolean var2) {
      if (this.e != hz.o) {
         this.I.g();
         this.O = null;
         this.D = null;
         this.V = null;
         this.v = (boolean)0;
         this.S = "";
         k.J(null);
         this.J = this.U = -1;
         this.a = null;
         this.s = null;
         this.e = hz.o;
         q8.v("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.Q = (boolean)0;
      } else {
         if (!this.Q) {
            this.u();
         }

         this.Q = (boolean)1;
      }
   }

   public void u() {
      this.I.z();
      this.U();
      this.a = null;
      this.s = null;
   }

   public boolean D(int var1) {
      return this.H && this.I.Q(var1);
   }

   public boolean f(int var1) {
      return this.H && this.I.y(var1);
   }

   public boolean y(double var1, double var3, double var5, boolean var7) {
      return this.H && !var7 && this.I.A(var1, var3, var5);
   }

   static void W(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var1 / 2;
      int var9 = var2 / 2;
      int var10 = var4 * var5;

      for (int var11 = cv.E(var1, var4); var11 < var1; var11 += var4) {
         if (var11 != var8) {
            gu.Z(var0, var11, 0, 1, var2, (var11 - var8) % var10 == 0 ? var7 : var6);
         }
      }

      for (int var12 = cv.E(var2, var4); var12 < var3; var12 += var4) {
         if (var12 != var9) {
            gu.Z(var0, 0, var12, var1, 1, (var12 - var9) % var10 == 0 ? var7 : var6);
         }
      }
   }

   static void o(class_332 var0, int var1, int var2, int var3, int var4) {
      int var5 = var1 / 2;
      int var6 = var2 / 2;
      int var7 = ei.D(ez.a, 0.9);
      int var8 = 1224736767;
      gu.Z(var0, var5, 0, 1, var2, var3 == var5 ? var7 : var8);
      gu.Z(var0, 0, var6, var1, 1, var4 == var6 ? var7 : var8);
      if (var3 >= 0 && var3 != var5) {
         gu.Z(var0, Math.min(var1 - 1, var3), 0, 1, var2, var7);
      }

      if (var4 >= 0 && var4 != var6) {
         gu.Z(var0, 0, Math.min(var2 - 1, var4), var1, 1, var7);
      }
   }

   public void e(class_332 var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.H && this.O != null && this.D != null) {
         long var7 = System.nanoTime();
         cv.s var9 = this.D.move(this.L, this.t, this.O.I(var4, var5), var4, var5, hz.l.snapping(), hz.l.grid(), this.j, this.J, this.U);
         cv.s var10 = this.i.a(var9.x(), var9.y(), class_310.method_1551().method_74187(), (var7 - this.c) / 1.0E9);
         this.c = var7;
         this.O.e(var10.x(), var10.y(), var4, var5);
         this.J = var9.guideX();
         this.U = var9.guideY();
         this.i(var4, var5, this.j);
      }

      List var12 = k.y();
      Optional var8 = k.o(var12, this.a);
      if (this.a != null && var8.isEmpty() && !this.O()) {
         this.a = null;
         this.s = null;
      }

      k.i var13 = var6 ? null : cv.q(var12, var0 -> true, k.i::bounds, var2, var3);
      if (var13 != null && var13.element() != this.a) {
         b(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         cv.z var14 = (cv.z)var8.get();
         b(var1, var14, ei.D(ez.a, 0.8));
         if (!this.g.test(this.a) && !k.k(this.a)) {
            cv.z var11 = cv.n(var14, var4, var5);
            gu.N(var1, var11.x(), var11.y(), var11.width(), var11.height(), 2.0F, ez.a);
         }
      }

      if (this.O()) {
         if (this.H) {
            W(var1, var4, var5, var5 - 14, 20, 4, 268435455, 587202559);
         }

         o(var1, var4, var5, this.J, this.U);
      }

      if (this.H) {
         for (k.l var17 : k.A()) {
            if (!this.g.test(var17) && !k.k(var17)) {
               k.o(var12, var17).ifPresent(var5x -> {
                  cv.z var6x = cv.n(var5x, var4, var5);
                  gu.N(var1, var6x.x(), var6x.y(), var6x.width(), var6x.height(), 2.0F, ei.D(ez.a, var6x.contains(var2, var3, 0) ? 0.6 : 0.35));
                  gu.Z(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                  gu.Z(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
               });
            }
         }

         this.I.D(var1, var2, var3);
      }

      if (this.s != null) {
         gu.N(var1, this.s.x(), this.s.y(), this.s.width(), this.s.height(), 6.0F, -267645934);
         String[] var16 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var18 = 0; var18 < this.s.height() / 20; var18++) {
            if (!var6 && this.s.contains(var2, var3, 0) && (var3 - this.s.y()) / 20 == var18) {
               gu.N(var1, this.s.x() + 2, this.s.y() + var18 * 20, this.s.width() - 4, 20.0F, 4.0F, 905969663);
            }

            lx.N(var1, lx.n(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var18]), this.s.x() + 7, this.s.y() + var18 * 20 + 4, 12, ez.a);
            w5.y(var1, var16[var18], this.s.x() + 25, q2.e(this.s.y() + var18 * 20, 20.0, 11.0), 11.0, -1);
         }
      }
   }

   private static void b(class_332 var0, cv.z var1, int var2) {
      gu.a(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
