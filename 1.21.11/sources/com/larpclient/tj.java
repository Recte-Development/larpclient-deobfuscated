package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;
import net.minecraft.class_437;

public final class tj extends t1 {
   private static final List<String> B = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final class_437 O;
   private j4 x;
   private j4.w E;
   private j4.k u = j4.k.G;
   private final List<tj.f> n = new ArrayList<>();
   private final List<tj.f> w = new ArrayList<>();
   private final List<lc> v = new ArrayList<>();
   private final Set<lc> d = Collections.newSetFromMap(new IdentityHashMap<>());
   private final qb t = new qb(1.0);
   private final Map<com.larpclient.module.lb<?>, Object> i = new IdentityHashMap<>();
   private lo J;
   private tj.f a;
   private tj.f o;
   private tj.f M;
   private tj.f p;
   private com.larpclient.module.lo R;
   private com.larpclient.module.lo e;
   private com.larpclient.module.lo j;
   private com.larpclient.module.li W;
   private com.larpclient.module.l3 X;
   private com.larpclient.module.l3 f;
   private boolean L = (boolean)1;
   private boolean H;
   private boolean A;
   private boolean I;
   private boolean D = (boolean)1;
   private boolean Z;
   private boolean h;
   private boolean m = (boolean)1;
   private double P;
   private double k;
   private int r;
   private int z;
   private int V = -1;
   private int N = -1;
   private static boolean s;
   private String Q = "";
   private Object g;

   public tj(class_437 var1) {
      super("HUD Editor");
      this.O = var1;
   }

   public boolean N() {
      return this.A;
   }

   private qk R() {
      return LarpClient.b().e();
   }

   private boolean W(k.l var1) {
      return this.R().F().j(var1.s);
   }

   protected void method_25426() {
      if (this.x == null) {
         com.larpclient.module.lr var1 = LarpClient.b().G();
         List var2 = Stream.of(var1.j("HUD"), var1.j("TargetHUD"), var1.j("Staff List"), var1.j("CustomCrosshair")).filter(Objects::nonNull).toList();
         this.x = new j4(() -> this.R().d(this.R().d()), this::W, var2);
         this.g = this.field_22787.field_1687;
         this.Z = s;
         q8.G("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.field_22789, this.field_22790, this.R().d());
      } else {
         this.x.v();
      }

      com.larpclient.k.O();
      this.i();
   }

   private tj.f R(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      tj.f var5 = new tj.f(var1, var2, var3, var4);
      this.K.add(var5);
      return var5;
   }

   private void w(com.larpclient.module.lb<?> var1, boolean var2) {
      l9 var3 = new l9(var1, true);
      this.v.add(var3);
      this.K.add(var3);
      if (var2) {
         this.d.add(var3);
      }
   }

   private void K(String var1, Runnable var2, boolean var3, boolean var4) {
      ls var5 = new ls(var1, var2);
      var5.S = (boolean)1;
      var5.L = var4;
      var5.w = 28;
      this.v.add(var5);
      this.K.add(var5);
      if (var3) {
         this.d.add(var5);
      }
   }

   private void w(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      com.larpclient.module.l3 var5 = new com.larpclient.module.l3(var1, var2, var3);
      var5.a(var4);
      this.w(var5, false);
   }

   private void i() {
      this.t();
      this.n.clear();
      this.w.clear();
      this.v.clear();
      this.d.clear();
      this.J = null;
      this.R = this.e = this.j = null;
      this.W = null;
      this.X = this.f = null;
      this.n.add(this.R("Widgets", "panels-top-left", () -> this.S(j4.k.d), () -> this.u == j4.k.d));
      this.a = this.R("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.O();
         this.x.g();
      }, () -> false);
      this.a.S = (boolean)1;
      this.n.add(this.a);
      this.o = this.R("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.O();
         this.x.n();
      }, () -> false);
      this.o.S = this.o.Q = (boolean)1;
      this.n.add(this.o);
      tj.f var1 = this.R("Editor options", "settings", () -> this.S(j4.k.X), () -> this.u == j4.k.X);
      var1.S = (boolean)1;
      this.n.add(var1);
      this.M = this.R("Edit", "paintbrush", () -> {
         if (this.x.t() != null) {
            if (this.u != j4.k.Q && this.u != j4.k.A) {
               this.j(this.x.t());
            } else {
               this.c(j4.k.G);
            }
         }
      }, () -> this.u == j4.k.Q || this.u == j4.k.A);
      this.n.add(this.M);
      tj.f var2 = this.R("Done", "", this::method_25419, () -> false);
      var2.F = (boolean)1;
      this.n.add(var2);
      this.p = this.R("Save failed. Retry", "rotate-ccw", () -> {
         this.x.f();
         this.s();
      }, () -> false);
      if (this.u != j4.k.G) {
         tj.f var3 = this.R("Close panel", "x", () -> this.c(j4.k.G), () -> false);
         var3.S = (boolean)1;
         this.w.add(var3);
         if (this.u == j4.k.d) {
            this.J = new lo("Search widgets", var1x -> {
               this.Q = var1x;
               this.r = 0;
               this.I();
            }, 80);
            this.J.h = this.Q;
            this.K.add(this.J);

            for (k.l var5 : com.larpclient.k.A()) {
               tj.z var6 = new tj.z(var5);
               this.v.add(var6);
               this.K.add(var6);
            }
         } else if (this.u == j4.k.X) {
            this.w("Snap to guides", "Align with screen edges and center", hz.l.snapping(), var1x -> this.x.M(var1x, hz.l.grid()));
            this.w("Show grid", "Show an eight-pixel placement grid", hz.l.grid(), var1x -> this.x.M(hz.l.snapping(), var1x));
            com.larpclient.module.l3 var7 = new com.larpclient.module.l3("Preview hidden widgets", "Preview without enabling widgets", this.H);
            var7.O(() -> !this.A);
            var7.a(var1x -> this.H = var1x);
            l9 var9 = new l9(var7);
            this.v.add(var9);
            this.K.add(var9);
            this.w("Use live content", "Show current gameplay content instead of samples", this.A, var1x -> {
               this.x.v();
               this.A = var1x;
               com.larpclient.k.O();
            });
            this.K("Reset layout", () -> {
               this.O();
               this.x.H();
            }, false, true);
         } else if (this.x.t() != null) {
            this.w.add(this.R("Appearance", "", () -> this.c(j4.k.Q), () -> this.u == j4.k.Q));
            this.w.add(this.R("Position", "", () -> this.c(j4.k.A), () -> this.u == j4.k.A));
            k.l var8 = this.x.t();
            if (this.u == j4.k.Q) {
               this.X = new com.larpclient.module.l3("Show widget", "Keep this widget visible in gameplay", com.larpclient.k.B(var8));
               this.X.a(var2x -> {
                  if (!this.I) {
                     com.larpclient.k.F(var8, var2x);
                     this.x.s();
                  }
               });
               this.w(this.X, false);
               this.H(var8);

               for (com.larpclient.module.lb var11 : com.larpclient.k.J(var8)) {
                  this.w(var11, false);
               }
            } else {
               this.f = new com.larpclient.module.l3("Lock position", "Prevent moving and resizing this widget", this.W(var8));
               this.f.a(var2x -> {
                  if (!this.I) {
                     this.Y(var8, var2x.booleanValue());
                  }
               });
               this.w(this.f, false);
               this.R = new com.larpclient.module.lo("X", "Horizontal position in GUI pixels", var8.l(this.field_22789), 0.0, this.field_22789, 1.0);
               this.e = new com.larpclient.module.lo("Y", "Vertical position in GUI pixels", var8.M(this.field_22790), 0.0, this.field_22790, 1.0);
               this.R.a(var2x -> {
                  if (!this.I && !this.W(var8)) {
                     var8.e(var2x.intValue(), var8.M(this.field_22790), this.field_22789, this.field_22790);
                  }
               });
               this.e.a(var2x -> {
                  if (!this.I && !this.W(var8)) {
                     var8.e(var8.l(this.field_22789), var2x.intValue(), this.field_22789, this.field_22790);
                  }
               });
               this.w(this.R, true);
               this.w(this.e, true);
               this.H(var8);
               this.W = new com.larpclient.module.li("Align", "Align within the full game viewport", Q(var8), B.toArray(String[]::new));
               this.W.a(var2x -> {
                  if (!this.I && !this.W(var8) && !var2x.equals("Free")) {
                     int var3x = B.indexOf(var2x) - 1;
                     hz.J.put(var8.s, new hz.v(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.w(this.W, true);
               this.K("Reset placement", () -> {
                  this.O();
                  if (!this.W(var8)) {
                     this.x.b(var8::x);
                  }
               }, true, false);
            }
         }
      }

      this.I();
   }

   private void H(k.l var1) {
      this.j = new com.larpclient.module.lo("Size", "Uniform widget scale", var1.S(), 0.2, 4.0, 0.01);
      this.j.a(var2 -> {
         if (!this.I && !this.W(var1)) {
            var1.V(var2);
         }
      });
      this.w(this.j, true);
   }

   private void S(j4.k var1) {
      this.c(this.u == var1 ? j4.k.G : var1);
   }

   private void c(j4.k var1) {
      this.O();
      this.x.v();
      this.h = (boolean)0;
      this.L = (boolean)1;
      if (this.u == j4.k.G || var1 == j4.k.d || var1 == j4.k.X) {
         this.V = this.N = -1;
      }

      this.u = var1;
      this.r = 0;
      this.t.w(this.m ? 0.0 : 1.0);
      this.t.a(1.0, 160);
      this.i();
   }

   private void j(k.l var1) {
      this.O();
      this.x.V(var1);
      this.o(var1);
      cv.z var2 = com.larpclient.k.o(com.larpclient.k.D(), var1).orElse(var1.k(this.field_22789, this.field_22790));
      this.V = var2.x() + var2.width() + 14;
      this.N = var2.y();
      if (this.V + 260 > this.field_22789 - 12) {
         this.V = Math.max(12, var2.x() - 274);
      }

      this.u = j4.k.Q;
      this.L = (boolean)1;
      this.r = 0;
      this.t.w(this.m ? 0.0 : 1.0);
      this.t.a(1.0, 160);
      this.i();
   }

   private void c(k.l var1) {
      this.O();
      this.x.V(var1);
      this.o(var1);
      this.u = j4.k.G;
      this.r = 0;
      this.i();
   }

   private void o(k.l var1) {
      if (this.A && com.larpclient.k.o(com.larpclient.k.D(), var1).isEmpty()) {
         this.A = (boolean)0;
      }
   }

   private void Y(k.l var1, boolean var2) {
      this.x.v();
      this.R().F().J(var1.s, var2);
      this.s();
   }

   private void s() {
      this.Z = s = (boolean)(!this.R().N() ? 1 : 0);
   }

   private boolean T(k.l var1) {
      return this.Q.isBlank() || (com.larpclient.k.L(var1.s) + " " + var1.s).toLowerCase(Locale.ROOT).contains(this.Q.toLowerCase(Locale.ROOT));
   }

   private boolean b(lc var1) {
      return !(var1 instanceof tj.z var2 && !this.T(var2.k));
   }

   private boolean t(lc var1) {
      return !(var1 instanceof l9 var2 && !var2.L());
   }

   private void I() {
      this.m = this.R().F().k;
      this.z = this.v.stream().filter(this::b).filter(this::t).mapToInt(var0 -> var0.w + 4).sum();
      this.E = j4.w.calculate(this.field_22789, this.field_22790, this.u, this.z, this.V, this.N, this.L, this.x.k() || this.Z);

      for (int var1 = 0; var1 < this.n.size(); var1++) {
         tj.f var2 = this.n.get(var1);
         var2.I = (boolean)(this.E.dock() != null ? 1 : 0);
         if (var2.I) {
            d(var2, this.E.tools().get(var1));
         }
      }

      this.a.N = this.x.l();
      this.o.N = this.x.y();
      this.M.N = (boolean)(this.x.t() != null ? 1 : 0);
      this.p.I = (boolean)(this.E.retry() != null ? 1 : 0);
      if (this.p.I) {
         d(this.p, this.E.retry());
      }

      cv.z var9 = this.E.panel();
      cv.z var10 = this.E.body();

      for (tj.f var4 : this.w) {
         var4.I = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.w.isEmpty()) {
         this.w.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.w.getFirst().w = 26;
         if (this.w.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.w.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.w.get(var13).w = 26;
            }
         }
      }

      if (this.J != null) {
         this.J.I = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.J.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.J.w = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.r = Math.clamp(this.r, 0, Math.max(0, this.z - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.r;

      for (lc var6 : this.v) {
         int var7 = this.b(var6) && this.t(var6) ? 1 : 0;
         var6.I = (boolean)(var10 != null && var7 != 0 && var14 + var6.w > var10.y() && var14 < var10.y() + var10.height() ? 1 : 0);
         var6.N = (boolean)(this.d.contains(var6) && this.x.t() != null && this.W(this.x.t()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof l9 var8) {
               var8.Z(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.w + 4;
         }
      }

      if (this.G != null && (!this.G.I || !this.G.N)) {
         this.G.blur();
         this.G = this.y = null;
         this.y();
      }
   }

   private static void d(lc var0, cv.z var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.w = var1.height();
   }

   @Override
   protected boolean accepts(lc var1, double var2, double var4) {
      return this.v.contains(var1)
         ? var1.contains(var2, var4) && this.E.controlContains(new cv.z(var1.D, var1.g, var1.d, var1.w), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(lc var1) {
      if (this.v.contains(var1) && this.E.body() != null) {
         cv.z var2 = this.E.body();
         if (var1.g < var2.y()) {
            this.r = this.r - (var2.y() - var1.g);
         }

         if (var1.g + var1.w > var2.y() + var2.height()) {
            this.r = this.r + (var1.g + var1.w - var2.y() - var2.height());
         }

         this.I();
      }
   }

   private static String Q(k.l var0) {
      hz.v var1 = hz.J.get(var0.s);
      if (var1 == null) {
         return var0.s.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return var2 >= 0 && var2 <= 2 && var3 >= 0 && var3 <= 2 && Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? B.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void h() {
      k.l var1 = this.x.t();
      if (var1 != null) {
         this.I = (boolean)1;

         try {
            if (this.R != null) {
               this.R.q((double)var1.l(this.field_22789));
            }

            if (this.e != null) {
               this.e.q((double)var1.M(this.field_22790));
            }

            if (this.j != null) {
               this.j.q(var1.S());
            }

            if (this.W != null) {
               this.W.setValue(Q(var1));
            }

            if (this.X != null) {
               this.X.setValue(com.larpclient.k.B(var1));
            }

            if (this.f != null) {
               this.f.setValue(this.W(var1));
            }
         } finally {
            this.I = (boolean)0;
         }
      }
   }

   private void M() {
      this.x.r();
      if (this.i.isEmpty() && this.x.t() != null) {
         for (com.larpclient.module.lb var2 : com.larpclient.k.J(this.x.t())) {
            this.i.put(var2, var2.getValue());
         }
      }
   }

   private void y() {
      if (this.i.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.x.s();
      }

      this.i.clear();
      this.x.i();
   }

   private void S() {
      this.G = this.y = null;
      this.K.clear();
      this.i.clear();
      com.larpclient.k.O();
      this.u = j4.k.G;
      this.h = (boolean)0;
      this.r = 0;
   }

   private void O() {
      if (this.x.Z()) {
         this.S();
      } else {
         if (this.G != null) {
            this.G.blur();
         }

         this.G = this.y = null;
         this.y();
      }
   }

   private boolean l() {
      return this.G instanceof lo || this.G instanceof l9 var1 && var1.P();
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.field_22787.field_1724 != null && this.field_22787.field_1687 != null) {
         if (this.x.Z()) {
            this.S();
            this.i();
         }

         if (this.x.j().O()) {
            return true;
         }

         this.I();

         for (lc var4 : this.K) {
            if (var4 instanceof l9 var5 && var5.j()) {
               if (var5.p(var1.comp_4798(), var1.comp_4799())) {
                  this.M();
                  var5.b(var1.comp_4798(), var1.comp_4799(), s2.m(var1));
                  this.y = var5;
                  return true;
               }

               var5.f();
            }
         }

         lc var6 = this.K.stream().filter(var2x -> this.accepts(var2x, var1.comp_4798(), var1.comp_4799())).findFirst().orElse(null);
         if (var6 != this.G) {
            this.O();
         }

         if (var6 != null) {
            this.M();
            this.G = var6;
            var6.M = (boolean)1;
            var6.q = (boolean)0;
            if (var6.click(var1.comp_4798(), var1.comp_4799(), s2.m(var1))) {
               if (this.K.contains(var6)) {
                  this.y = var6;
               } else {
                  this.G = this.y = null;
               }
            }

            if (!(this.y instanceof l9) && !this.l()) {
               this.y();
            }

            return true;
         } else if (this.E.panelContains(var1.comp_4798(), var1.comp_4799())) {
            if (s2.m(var1) == 0 && var1.comp_4799() < this.E.panel().y() + 39) {
               this.h = (boolean)1;
               this.P = var1.comp_4798() - this.E.panel().x();
               this.k = var1.comp_4799() - this.E.panel().y();
            }

            return true;
         } else {
            if (this.E.dock() != null && this.E.dock().contains(var1.comp_4798(), var1.comp_4799(), 0)) {
               return true;
            }

            if (this.u != j4.k.G) {
               this.c(j4.k.G);
            }

            if (this.E.canvasContains(var1.comp_4798(), var1.comp_4799())) {
               k.i var7 = cv.q(com.larpclient.k.D(), var0 -> true, k.i::bounds, var1.comp_4798(), var1.comp_4799());
               if (s2.m(var1) == 1) {
                  if (var7 == null) {
                     this.c(j4.k.d);
                  } else {
                     this.j(var7.element());
                  }
               } else if (s2.m(var1) == 0) {
                  this.M();
                  this.x.j().F(var1.comp_4798(), var1.comp_4799(), 0, this.field_22789, this.field_22790, com.larpclient.k.D(), false);
               }
            }

            if (!this.x.j().O()) {
               this.y();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean method_25403(class_11909 var1, double var2, double var4) {
      if (this.x.Z()) {
         this.S();
         this.i();
         return true;
      } else if (!this.field_22787.method_1569()) {
         this.e();
         return true;
      } else if (this.h && this.E.panel() != null) {
         this.V = Math.max(0, (int)Math.round(var1.comp_4798() - this.P));
         this.N = Math.max(0, (int)Math.round(var1.comp_4799() - this.k));
         this.I();
         return true;
      } else {
         return this.x.j().O()
            ? this.x
               .j()
               .c(
                  var1.comp_4798(),
                  var1.comp_4799(),
                  s2.m(var1),
                  this.field_22789,
                  this.field_22790,
                  this.field_22787.method_74189(),
                  this.field_22787.method_74187()
               )
            : super.method_25403(var1, var2, var4);
      }
   }

   @Override
   public boolean method_25406(class_11909 var1) {
      if (this.x.Z()) {
         this.S();
         this.i();
         return true;
      }

      this.h = (boolean)0;
      this.x.j().u(s2.m(var1));
      super.method_25406(var1);
      if (!this.l()) {
         this.y();
      }

      return true;
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (this.x.Z()) {
         this.S();
         this.i();
         return true;
      }

      for (lc var10 : this.K) {
         if (var10 instanceof l9 var11 && var11.p(var1, var3)) {
            var11.V(var7);
            return true;
         }
      }

      if (this.E.panelContains(var1, var3)) {
         this.O();
         this.r = this.r - (int)Math.round(var7 * 28.0);
         this.I();
      }

      return true;
   }

   @Override
   public boolean method_25400(class_11905 var1) {
      if (this.x.Z()) {
         this.S();
         this.i();
         return true;
      } else {
         return super.method_25400(var1);
      }
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      if (this.x.Z()) {
         this.S();
         this.i();
      }

      int var2 = s2.i(var1);
      int var3 = (var1.comp_4797() & 2) != 0 ? 1 : 0;
      int var4 = (var1.comp_4797() & 1) != 0 ? 1 : 0;
      if (this.l()) {
         if (var2 == 258) {
            this.O();
            return super.method_25404(var1);
         }

         this.M();
         boolean var9 = super.method_25404(var1);
         if (!this.l()) {
            this.y();
         }

         return var9;
      } else if (var2 == 256) {
         for (lc var11 : this.v) {
            if (var11 instanceof l9 var7 && var7.j()) {
               var7.f();
               return true;
            }
         }

         if (this.u != j4.k.G) {
            this.c(j4.k.G);
         } else {
            this.method_25419();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.O();
            this.x.v();
            this.u = j4.k.G;
            this.h = (boolean)0;
            this.L = (boolean)(!this.L ? 1 : 0);
            this.i();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.S(j4.k.d);
               return true;
            }

            k.l var5 = this.x.t();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.j(var5);
               return true;
            }

            if (this.G == null && var5 != null && !this.W(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.x.A(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.field_22789, this.field_22790);
               return true;
            }

            if (this.G == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.O();
               this.x.b(() -> var5.a(true));
               return true;
            }

            this.M();
            boolean var6 = super.method_25404(var1);
            if (!this.l()) {
               this.y();
            }

            return var6;
         } else {
            this.O();
            if (var2 != 89 && var4 == 0) {
               this.x.g();
            } else {
               this.x.n();
            }

            return true;
         }
      }
   }

   private void e() {
      this.h = (boolean)0;
      this.O();
      this.x.v();
   }

   public void method_25393() {
      if (this.x.Z()) {
         this.S();
         this.i();
      }

      if (this.field_22787.field_1687 == this.g && this.field_22787.field_1724 != null) {
         boolean var1 = this.field_22787.method_1569();
         if (!var1 && this.D) {
            this.e();
         }

         this.D = var1;
      } else {
         this.method_25419();
      }
   }

   public void method_25410(int var1, int var2) {
      if (this.x != null) {
         this.e();
      }

      super.method_25410(var1, var2);
   }

   public void method_25419() {
      if (this.x != null) {
         this.O();
         this.x.C();
      }

      if (this.Z) {
         this.s();
      }

      if (this.x != null && this.x.k() || this.Z) {
         wt.T().z("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", wt.s.a, 6000L);
      }

      this.field_22787.method_1507(this.field_22787.field_1687 == this.g ? this.O : null);
   }

   public void method_25432() {
      if (this.x != null) {
         this.O();
         this.x.C();
      }

      q8.v("HUD editor", "Released canvas input");
   }

   public void method_25420(class_332 var1, int var2, int var3, float var4) {
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      if (this.x != null && this.field_22787.field_1724 != null && this.field_22787.field_1687 != null) {
         if (this.x.Z()) {
            this.S();
            this.i();
         }

         this.I();
         if (hz.l.grid() && !this.A) {
            cp.W(var1, this.field_22789, this.field_22790, this.field_22790, 8, 5, 117440511, 318767103);
         }

         com.larpclient.k.C(var1, this.x.t(), this.H, this.A);
         this.C(var1, var2, var3);
         this.h();
         y0 var5 = this.R().F();

         try (ez.m var6 = ez.G(var5.Gr, var5.G7, var5.O, var5.S())) {
            var1.method_71048();
            if (this.E.dock() != null) {
               o(var1, this.E.dock());

               for (tj.f var8 : this.n) {
                  var8.render(var1, var2, var3);
               }

               if (this.p.I) {
                  this.p.render(var1, var2, var3);
               }

               if (this.E.panel() == null && !this.x.j().O() && !this.p.I) {
                  String var16 = this.x.t() == null ? "HUD editor" : com.larpclient.k.L(this.x.t().s);
                  String var19 = this.x.t() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = uw.C(var16, uw.c.e);
                  int var10 = uw.C(var19, uw.c.M);
                  int var11 = Math.min(this.field_22789 - 24, var9 + var10 + 28);
                  int var12 = (this.field_22789 - var11) / 2;
                  int var13 = this.E.dock().y() - 28;
                  com.larpclient.p.B(var1, var12, var13, var11, 21, 8, ez.X());
                  uw.X(var1, var16, var12 + 9, var13 + 6, ez.H, uw.c.e);
                  if (var9 + var10 + 28 <= var11) {
                     uw.X(var1, var19, var12 + var9 + 19, var13 + 7, ez.PH, uw.c.M);
                  }
               }
            }

            this.L(var1, var2, var3);

            for (lc var20 : this.v) {
               if (var20 instanceof l9 var22 && var22.j() && var20.I) {
                  var22.S(var1, var2, var3);
               }
            }

            if (this.K.stream().noneMatch(var0 -> var0 instanceof l9 var1x && var1x.j())) {
               for (tj.f var21 : this.n) {
                  if (var21.I && (var21.contains(var2, var3) || var21.M && var21.q)) {
                     var21.Y(var1);
                  }
               }
            }
         }
      }
   }

   private static void o(class_332 var0, cv.z var1) {
      com.larpclient.p.f(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      com.larpclient.p.W(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, ez.X(), 0.75);
   }

   private void L(class_332 var1, int var2, int var3) {
      cv.z var4 = this.E.panel();
      cv.z var5 = this.E.body();
      if (var4 != null) {
         if (!this.m) {
            this.t.w(1.0);
         }

         try (fp var6 = fp.O(this.t.u())) {
            o(var1, var4);
            String var7 = this.u == j4.k.d ? "Widgets" : (this.u == j4.k.X ? "Editor options" : com.larpclient.k.L(this.x.t().s));
            if (this.u == j4.k.Q || this.u == j4.k.A) {
               lx.N(var1, lx.n(lx.j(this.x.t().s)), var4.x() + 13, var4.y() + 15, 13, ez.PH);
            }

            int var8 = var4.x() + (this.u != j4.k.Q && this.u != j4.k.A ? 14 : 34);
            uw.X(var1, uw.J(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, ez.H, uw.c.e);

            for (tj.f var10 : this.w) {
               var10.render(var1, var2, var3);
            }

            if (this.J != null) {
               this.J.render(var1, var2, var3);
            }

            var1.method_44379(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (lc var19 : this.v) {
                  if (var19.I) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.u == j4.k.d && this.v.stream().noneMatch(this::b)) {
                  uw.X(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, ez.PH, uw.c.o);
               }
            } finally {
               var1.method_44380();
            }

            com.larpclient.p.t(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.z, this.r);
         }
      }
   }

   private void C(class_332 var1, int var2, int var3) {
      List var4 = com.larpclient.k.D();
      k.i var5 = this.E.canvasContains(var2, var3) ? cv.q(var4, var0 -> true, k.i::bounds, var2, var3) : null;
      if (var5 != null && var5.element() != this.x.t()) {
         F(var1, var5.bounds(), -2137614688);
      }

      k.l var6 = this.x.t();
      com.larpclient.k.o(var4, var6).ifPresent(var3x -> {
         F(var1, var3x, ei.D(ez.a, 0.85));
         if (!this.W(var6) && !com.larpclient.k.k(var6)) {
            cv.z var4x = cv.n(var3x, this.field_22789, this.field_22790);
            com.larpclient.p.B(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            com.larpclient.p.B(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, ez.a);
         }

         if (this.x.j().O()) {
            String var8 = var6.l(this.field_22789) + ", " + var6.M(this.field_22790);
            int var5x = uw.C(var8, uw.c.M) + 12;
            int var6x = cv.o(var3x.x(), var5x, this.field_22789);
            int var7 = Math.max(2, var3x.y() - 20);
            com.larpclient.p.B(var1, var6x, var7, var5x, 16, 5, -552003303);
            uw.X(var1, var8, var6x + 6, var7 + 4, -1381654, uw.c.M);
         }
      });
      if (this.x.j().O()) {
         cp.o(var1, this.field_22789, this.field_22790, this.x.j().Z(), this.x.j().O());
      }
   }

   private static void F(class_332 var0, cv.z var1, int var2) {
      gu.a(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class f extends lc {
      final String Y;
      final String k;
      final Runnable K;
      final BooleanSupplier m;
      final qb x = new qb(0.0);
      boolean S;
      boolean Q;
      boolean F;

      f(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.Y = var2;
         this.k = var3;
         this.K = var4;
         this.m = var5;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         if (this.I) {
            boolean var4 = this.m.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.M || !this.q) ? 0 : 1;
            if (!tj.this.m) {
               this.x.w(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.x.a(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.F ? ez.d : (this.N ? ez.H : ez.t);
            if (this.F || var4 || var6 > 0.001) {
               com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, 7, this.F ? ei.W(ez.H, ez.PH, var6 * 0.12) : ei.W(ez.d, ez.PC, var4 ? 1.0 : var6));
            }

            byte var9 = 13;
            int var10 = this.S ? 0 : uw.r(this.Y);
            int var11 = var10 + (this.k.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.D + Math.max(5, (this.d - var11) / 2);
            if (!this.k.isEmpty()) {
               var1.method_51448().pushMatrix();

               try {
                  if (this.Q) {
                     var1.method_51448().translate(var12 * 2 + var9, 0.0F);
                     var1.method_51448().scale(-1.0F, 1.0F);
                  }

                  lx.N(var1, lx.n(this.k), this.S ? this.D + (this.d - var9) / 2 : var12, this.g + (this.w - var9) / 2, var9, var8);
               } finally {
                  var1.method_51448().popMatrix();
               }
            }

            if (!this.S) {
               uw.G(var1, uw.J(this.Y, this.d - (this.k.isEmpty() ? 12 : 27)), var12 + (this.k.isEmpty() ? 0 : var9 + 6), this.g + (this.w - 9) / 2, var8);
            }

            if (this.M && this.q) {
               com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, 7);
            }
         }
      }

      void Y(class_332 var1) {
         if (this.S) {
            int var2 = uw.C(this.Y, uw.c.M) + 16;
            int var3 = cv.o(this.D + (this.d - var2) / 2, var2, tj.this.field_22789);
            int var4 = Math.max(0, this.g - 27);
            com.larpclient.p.B(var1, var3, var4, var2, 20, 6, ez.p);
            uw.X(var1, this.Y, var3 + 8, var4 + 6, ez.H, uw.c.M);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            c_.b(c_.d.q);
            this.K.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.N && (var1 == 257 || var1 == 32)) {
            c_.b(c_.d.q);
            this.K.run();
            return true;
         } else {
            return false;
         }
      }
   }

   private final class z extends lc {
      final k.l k;
      private final qb f = new qb(0.0);
      private final qb A = new qb(0.0);

      z(k.l var2) {
         this.k = var2;
         this.w = 36;
         this.A.w(com.larpclient.k.B(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         boolean var4 = com.larpclient.k.B(this.k);
         int var5 = tj.this.x.t() == this.k ? 1 : 0;
         if (!tj.this.m) {
            this.f.w(this.contains(var2, var3) ? 1.0 : 0.0);
            this.A.w(var4 ? 1.0 : 0.0);
         }

         double var6 = this.f.a(!this.contains(var2, var3) && !this.M ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, 7, ei.W(ez.d, ez.PC, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         com.larpclient.p.B(var1, this.D + 4, this.g + 6, 24, 24, 6, ez.B);
         lx.N(var1, lx.n(lx.j(this.k.s)), this.D + 10, this.g + 12, 12, var4 ? ez.H : ez.PH);
         uw.X(var1, uw.J(com.larpclient.k.L(this.k.s), this.d - 74), this.D + 36, this.g + 7, ez.H, uw.c.e);
         uw.X(var1, var4 ? "Visible" : "Hidden", this.D + 36, this.g + 21, ez.PH, uw.c.M);
         com.larpclient.p.s(var1, this.D + this.d - 30, this.g + 12, this.A.a(var4 ? 1.0 : 0.0, 180));
         if (this.M && this.q) {
            com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, 7);
         }
      }

      private void f() {
         tj.this.x.b(() -> {
            com.larpclient.k.F(this.k, !com.larpclient.k.B(this.k));
            tj.this.x.s();
         });
         c_.b(com.larpclient.k.B(this.k) ? c_.d.d : c_.d.n);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            tj.this.j(this.k);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.D + this.d - 38) {
            this.f();
         } else {
            tj.this.c(this.k);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.f();
            return true;
         } else if (var1 == 257) {
            tj.this.c(this.k);
            return true;
         } else {
            return false;
         }
      }
   }
}
