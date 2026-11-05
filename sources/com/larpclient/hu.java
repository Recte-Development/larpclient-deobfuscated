package com.larpclient;

import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dz;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public final class hu extends h8 {
   private static final List<String> v = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen J;
   private cr o;
   private cr.a i;
   private cr.y S = cr.y.U;
   private final List<hu.a> W = new ArrayList<>();
   private final List<hu.a> l = new ArrayList<>();
   private final List<b> m = new ArrayList<>();
   private final Set<b> Y = Collections.newSetFromMap(new IdentityHashMap<>());
   private final ef L = new ef(1.0);
   private final Map<df<?>, Object> O = new IdentityHashMap<>();
   private ur D;
   private hu.a H;
   private hu.a P;
   private hu.a z;
   private hu.a M;
   private com.larpclient.module.d9 A;
   private com.larpclient.module.d9 F;
   private com.larpclient.module.d9 p;
   private com.larpclient.module.d6 Q;
   private dh w;
   private dh e;
   private boolean x = (boolean)1;
   private boolean h;
   private boolean U;
   private boolean g;
   private boolean c = (boolean)1;
   private boolean K;
   private boolean C;
   private boolean d = (boolean)1;
   private double Z;
   private double R;
   private int u;
   private int j;
   private int n = -1;
   private int k = -1;
   private static boolean s;
   private String y = "";
   private Object a;

   public hu(Screen var1) {
      super("HUD Editor");
      this.J = var1;
   }

   public boolean Z() {
      return this.U;
   }

   private w9 J() {
      return LarpClient.F().f();
   }

   private boolean H(mq.s var1) {
      return this.J().t().c(var1.g);
   }

   protected void init() {
      if (this.o == null) {
         dz var1 = LarpClient.F().x();
         List var2 = Stream.of(var1.k("HUD"), var1.k("TargetHUD"), var1.k("Staff List"), var1.k("CustomCrosshair")).filter(Objects::nonNull).toList();
         this.o = new cr(() -> this.J().E(this.J().w()), this::H, var2);
         this.a = this.minecraft.level;
         this.K = s;
         mt.K("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.J().w());
      } else {
         this.o.b();
      }

      mq.v();
      this.A();
   }

   private hu.a I(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      hu.a var5 = new hu.a(var1, var2, var3, var4);
      this.G.add(var5);
      return var5;
   }

   private void v(df<?> var1, boolean var2) {
      un var3 = new un(var1, true);
      this.m.add(var3);
      this.G.add(var3);
      if (var2) {
         this.Y.add(var3);
      }
   }

   private void V(String var1, Runnable var2, boolean var3, boolean var4) {
      u7 var5 = new u7(var1, var2);
      var5.r = (boolean)1;
      var5.k = var4;
      var5.W = 28;
      this.m.add(var5);
      this.G.add(var5);
      if (var3) {
         this.Y.add(var5);
      }
   }

   private void r(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      dh var5 = new dh(var1, var2, var3);
      var5.h(var4);
      this.v(var5, false);
   }

   private void A() {
      this.z();
      this.W.clear();
      this.l.clear();
      this.m.clear();
      this.Y.clear();
      this.D = null;
      this.A = this.F = this.p = null;
      this.Q = null;
      this.w = this.e = null;
      this.W.add(this.I("Widgets", "panels-top-left", () -> this.a(cr.y.T), () -> this.S == cr.y.T));
      this.H = this.I("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.o();
         this.o.T();
      }, () -> false);
      this.H.Y = (boolean)1;
      this.W.add(this.H);
      this.P = this.I("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.o();
         this.o.z();
      }, () -> false);
      this.P.Y = this.P.a = (boolean)1;
      this.W.add(this.P);
      hu.a var1 = this.I("Editor options", "settings", () -> this.a(cr.y.r), () -> this.S == cr.y.r);
      var1.Y = (boolean)1;
      this.W.add(var1);
      this.z = this.I("Edit", "paintbrush", () -> {
         if (this.o.l() != null) {
            if (this.S != cr.y.c && this.S != cr.y.P) {
               this.d(this.o.l());
            } else {
               this.L(cr.y.U);
            }
         }
      }, () -> this.S == cr.y.c || this.S == cr.y.P);
      this.W.add(this.z);
      hu.a var2 = this.I("Done", "", this::onClose, () -> false);
      var2.n = (boolean)1;
      this.W.add(var2);
      this.M = this.I("Save failed. Retry", "rotate-ccw", () -> {
         this.o.m();
         this.h();
      }, () -> false);
      if (this.S != cr.y.U) {
         hu.a var3 = this.I("Close panel", "x", () -> this.L(cr.y.U), () -> false);
         var3.Y = (boolean)1;
         this.l.add(var3);
         if (this.S == cr.y.T) {
            this.D = new ur("Search widgets", var1x -> {
               this.y = var1x;
               this.u = 0;
               this.d();
            }, 80);
            this.D.d = this.y;
            this.G.add(this.D);

            for (mq.s var5 : mq.Y) {
               hu.l var6 = new hu.l(var5);
               this.m.add(var6);
               this.G.add(var6);
            }
         } else if (this.S == cr.y.r) {
            this.r("Snap to guides", "Align with screen edges and center", cx.l.snapping(), var1x -> this.o.e(var1x, cx.l.grid()));
            this.r("Show grid", "Show an eight-pixel placement grid", cx.l.grid(), var1x -> this.o.e(cx.l.snapping(), var1x));
            dh var7 = new dh("Preview hidden widgets", "Preview without enabling widgets", this.h);
            var7.H(() -> !this.U);
            var7.h(var1x -> this.h = var1x);
            un var9 = new un(var7);
            this.m.add(var9);
            this.G.add(var9);
            this.r("Use live content", "Show current gameplay content instead of samples", this.U, var1x -> {
               this.o.b();
               this.U = var1x;
               mq.v();
            });
            this.V("Reset layout", () -> {
               this.o();
               this.o.R();
            }, false, true);
         } else if (this.o.l() != null) {
            this.l.add(this.I("Appearance", "", () -> this.L(cr.y.c), () -> this.S == cr.y.c));
            this.l.add(this.I("Position", "", () -> this.L(cr.y.P), () -> this.S == cr.y.P));
            mq.s var8 = this.o.l();
            if (this.S == cr.y.c) {
               this.w = new dh("Show widget", "Keep this widget visible in gameplay", mq.M(var8));
               this.w.h(var2x -> {
                  if (!this.g) {
                     mq.m(var8, var2x);
                     this.o.y();
                  }
               });
               this.v(this.w, false);
               this.j(var8);

               for (df var11 : mq.E(var8)) {
                  this.v(var11, false);
               }
            } else {
               this.e = new dh("Lock position", "Prevent moving and resizing this widget", this.H(var8));
               this.e.h(var2x -> {
                  if (!this.g) {
                     this.i(var8, var2x);
                  }
               });
               this.v(this.e, false);
               this.A = new com.larpclient.module.d9("X", "Horizontal position in GUI pixels", var8.W(this.width), 0.0, this.width, 1.0);
               this.F = new com.larpclient.module.d9("Y", "Vertical position in GUI pixels", var8.i(this.height), 0.0, this.height, 1.0);
               this.A.h(var2x -> {
                  if (!this.g && !this.H(var8)) {
                     var8.s(var2x.intValue(), var8.i(this.height), this.width, this.height);
                  }
               });
               this.F.h(var2x -> {
                  if (!this.g && !this.H(var8)) {
                     var8.s(var8.W(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.v(this.A, true);
               this.v(this.F, true);
               this.j(var8);
               this.Q = new com.larpclient.module.d6("Align", "Align within the full game viewport", O(var8), v.toArray(String[]::new));
               this.Q.h(var2x -> {
                  if (!this.g && !this.H(var8) && !var2x.equals("Free")) {
                     int var3x = v.indexOf(var2x) - 1;
                     cx.O.put(var8.g, new cx.m(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.v(this.Q, true);
               this.V("Reset placement", () -> {
                  this.o();
                  if (!this.H(var8)) {
                     this.o.S(var8::f);
                  }
               }, true, false);
            }
         }
      }

      this.d();
   }

   private void j(mq.s var1) {
      this.p = new com.larpclient.module.d9("Size", "Uniform widget scale", var1.X(), 0.5, 2.5, 0.01);
      this.p.h(var2 -> {
         if (!this.g && !this.H(var1)) {
            var1.W(var2);
         }
      });
      this.v(this.p, true);
   }

   private void a(cr.y var1) {
      this.L(this.S == var1 ? cr.y.U : var1);
   }

   private void L(cr.y var1) {
      this.o();
      this.o.b();
      this.C = (boolean)0;
      this.x = (boolean)1;
      if (this.S == cr.y.U || var1 == cr.y.T || var1 == cr.y.r) {
         this.n = this.k = -1;
      }

      this.S = var1;
      this.u = 0;
      this.L.Q(this.d ? 0.0 : 1.0);
      this.L.m(1.0, 160);
      this.A();
   }

   private void d(mq.s var1) {
      this.o();
      this.o.M(var1);
      this.G(var1);
      ez.h var2 = mq.e(mq.K(), var1).orElse(var1.K(this.width, this.height));
      this.n = var2.x() + var2.width() + 14;
      this.k = var2.y();
      if (this.n + 260 > this.width - 12) {
         this.n = Math.max(12, var2.x() - 274);
      }

      this.S = cr.y.c;
      this.x = (boolean)1;
      this.u = 0;
      this.L.Q(this.d ? 0.0 : 1.0);
      this.L.m(1.0, 160);
      this.A();
   }

   private void W(mq.s var1) {
      this.o();
      this.o.M(var1);
      this.G(var1);
      this.S = cr.y.U;
      this.u = 0;
      this.A();
   }

   private void G(mq.s var1) {
      if (this.U && mq.e(mq.K(), var1).isEmpty()) {
         this.U = (boolean)0;
      }
   }

   private void i(mq.s var1, boolean var2) {
      this.o.b();
      this.J().t().f(var1.g, var2);
      this.h();
   }

   private void h() {
      this.K = s = (boolean)(!this.J().S() ? 1 : 0);
   }

   private boolean N(mq.s var1) {
      return this.y.isBlank() || (mq.w(var1.g) + " " + var1.g).toLowerCase(Locale.ROOT).contains(this.y.toLowerCase(Locale.ROOT));
   }

   private boolean c(b var1) {
      return !(var1 instanceof hu.l var2 && !this.N(var2.c));
   }

   private boolean l(b var1) {
      return !(var1 instanceof un var2 && !var2.x());
   }

   private void d() {
      this.d = this.J().t().V;
      this.j = this.m.stream().filter(this::c).filter(this::l).mapToInt(var0 -> var0.W + 4).sum();
      this.i = cr.a.calculate(this.width, this.height, this.S, this.j, this.n, this.k, this.x, this.o.Y() || this.K);

      for (int var1 = 0; var1 < this.W.size(); var1++) {
         hu.a var2 = this.W.get(var1);
         var2.D = (boolean)(this.i.dock() != null ? 1 : 0);
         if (var2.D) {
            G(var2, this.i.tools().get(var1));
         }
      }

      this.H.X = this.o.D();
      this.P.X = this.o.n();
      this.z.X = (boolean)(this.o.l() != null ? 1 : 0);
      this.M.D = (boolean)(this.i.retry() != null ? 1 : 0);
      if (this.M.D) {
         G(this.M, this.i.retry());
      }

      ez.h var9 = this.i.panel();
      ez.h var10 = this.i.body();

      for (hu.a var4 : this.l) {
         var4.D = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.l.isEmpty()) {
         this.l.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.l.getFirst().W = 26;
         if (this.l.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.l.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.l.get(var13).W = 26;
            }
         }
      }

      if (this.D != null) {
         this.D.D = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.D.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.D.W = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.u = Math.clamp(this.u, 0, Math.max(0, this.j - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.u;

      for (b var6 : this.m) {
         int var7 = this.c(var6) && this.l(var6) ? 1 : 0;
         var6.D = (boolean)(var10 != null && var7 != 0 && var14 + var6.W > var10.y() && var14 < var10.y() + var10.height() ? 1 : 0);
         var6.X = (boolean)(this.Y.contains(var6) && this.o.l() != null && this.H(this.o.l()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof un var8) {
               var8.N(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.W + 4;
         }
      }

      if (this.q != null && (!this.q.D || !this.q.X)) {
         this.q.blur();
         this.q = this.f = null;
         this.W();
      }
   }

   private static void G(b var0, ez.h var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.W = var1.height();
   }

   @Override
   protected boolean accepts(b var1, double var2, double var4) {
      return this.m.contains(var1)
         ? var1.contains(var2, var4) && this.i.controlContains(new ez.h(var1.q, var1.J, var1.A, var1.W), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(b var1) {
      if (this.m.contains(var1) && this.i.body() != null) {
         ez.h var2 = this.i.body();
         if (var1.J < var2.y()) {
            this.u = this.u - (var2.y() - var1.J);
         }

         if (var1.J + var1.W > var2.y() + var2.height()) {
            this.u = this.u + (var1.J + var1.W - var2.y() - var2.height());
         }

         this.d();
      }
   }

   private static String O(mq.s var0) {
      cx.m var1 = cx.O.get(var0.g);
      if (var1 == null) {
         return var0.g.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return var2 >= 0 && var2 <= 2 && var3 >= 0 && var3 <= 2 && Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? v.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void c() {
      mq.s var1 = this.o.l();
      if (var1 != null) {
         this.g = (boolean)1;

         try {
            if (this.A != null) {
               this.A.o((double)var1.W(this.width));
            }

            if (this.F != null) {
               this.F.o((double)var1.i(this.height));
            }

            if (this.p != null) {
               this.p.o(var1.X());
            }

            if (this.Q != null) {
               this.Q.setValue(O(var1));
            }

            if (this.w != null) {
               this.w.setValue(mq.M(var1));
            }

            if (this.e != null) {
               this.e.setValue(this.H(var1));
            }
         } finally {
            this.g = (boolean)0;
         }
      }
   }

   private void D() {
      this.o.M();
      if (this.O.isEmpty() && this.o.l() != null) {
         for (df var2 : mq.E(this.o.l())) {
            this.O.put(var2, var2.P());
         }
      }
   }

   private void W() {
      if (this.O.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().P()))) {
         this.o.y();
      }

      this.O.clear();
      this.o.o();
   }

   private void p() {
      this.q = this.f = null;
      this.G.clear();
      this.O.clear();
      mq.v();
      this.S = cr.y.U;
      this.C = (boolean)0;
      this.u = 0;
   }

   private void o() {
      if (this.o.k()) {
         this.p();
      } else {
         if (this.q != null) {
            this.q.blur();
         }

         this.q = this.f = null;
         this.W();
      }
   }

   private boolean t() {
      return this.q instanceof ur || this.q instanceof un var1 && var1.s();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         if (this.o.k()) {
            this.p();
            this.A();
         }

         if (this.o.J().s()) {
            return true;
         }

         this.d();

         for (b var4 : this.G) {
            if (var4 instanceof un var5 && var5.g()) {
               if (var5.e(var1.x(), var1.y())) {
                  this.D();
                  var5.C(var1.x(), var1.y(), mo.Z(var1));
                  this.f = var5;
                  return true;
               }

               var5.M();
            }
         }

         b var6 = this.G.stream().filter(var2x -> this.accepts(var2x, var1.x(), var1.y())).findFirst().orElse(null);
         if (var6 != this.q) {
            this.o();
         }

         if (var6 != null) {
            this.D();
            this.q = var6;
            var6.l = (boolean)1;
            var6.R = (boolean)0;
            if (var6.click(var1.x(), var1.y(), mo.Z(var1))) {
               if (this.G.contains(var6)) {
                  this.f = var6;
               } else {
                  this.q = this.f = null;
               }
            }

            if (!(this.f instanceof un) && !this.t()) {
               this.W();
            }

            return true;
         } else if (this.i.panelContains(var1.x(), var1.y())) {
            if (mo.Z(var1) == 0 && var1.y() < this.i.panel().y() + 39) {
               this.C = (boolean)1;
               this.Z = var1.x() - this.i.panel().x();
               this.R = var1.y() - this.i.panel().y();
            }

            return true;
         } else {
            if (this.i.dock() != null && this.i.dock().contains(var1.x(), var1.y(), 0)) {
               return true;
            }

            if (this.S != cr.y.U) {
               this.L(cr.y.U);
            }

            if (this.i.canvasContains(var1.x(), var1.y())) {
               mq.l var7 = ez.z(mq.K(), var0 -> true, mq.l::bounds, var1.x(), var1.y());
               if (mo.Z(var1) == 1) {
                  if (var7 == null) {
                     this.L(cr.y.T);
                  } else {
                     this.d(var7.element());
                  }
               } else if (mo.Z(var1) == 0) {
                  this.D();
                  this.o.J().n(var1.x(), var1.y(), 0, this.width, this.height, mq.K(), false);
               }
            }

            if (!this.o.J().s()) {
               this.W();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.o.k()) {
         this.p();
         this.A();
         return true;
      } else if (!this.minecraft.isWindowActive()) {
         this.C();
         return true;
      } else if (this.C && this.i.panel() != null) {
         this.n = Math.max(0, (int)Math.round(var1.x() - this.Z));
         this.k = Math.max(0, (int)Math.round(var1.y() - this.R));
         this.d();
         return true;
      } else {
         return this.o.J().s()
            ? this.o.J().X(var1.x(), var1.y(), mo.Z(var1), this.width, this.height, this.minecraft.hasAltDown(), this.minecraft.hasShiftDown())
            : super.mouseDragged(var1, var2, var4);
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.o.k()) {
         this.p();
         this.A();
         return true;
      }

      this.C = (boolean)0;
      this.o.J().a(mo.Z(var1));
      super.mouseReleased(var1);
      if (!this.t()) {
         this.W();
      }

      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.o.k()) {
         this.p();
         this.A();
         return true;
      }

      for (b var10 : this.G) {
         if (var10 instanceof un var11 && var11.e(var1, var3)) {
            var11.z(var7);
            return true;
         }
      }

      if (this.i.panelContains(var1, var3)) {
         this.o();
         this.u = this.u - (int)Math.round(var7 * 28.0);
         this.d();
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.o.k()) {
         this.p();
         this.A();
         return true;
      } else {
         return super.charTyped(var1);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.o.k()) {
         this.p();
         this.A();
      }

      int var2 = mo.O(var1);
      int var3 = (var1.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (var1.modifiers() & 1) != 0 ? 1 : 0;
      if (this.t()) {
         if (var2 == 258) {
            this.o();
            return super.keyPressed(var1);
         }

         this.D();
         boolean var9 = super.keyPressed(var1);
         if (!this.t()) {
            this.W();
         }

         return var9;
      } else if (var2 == 256) {
         for (b var11 : this.m) {
            if (var11 instanceof un var7 && var7.g()) {
               var7.M();
               return true;
            }
         }

         if (this.S != cr.y.U) {
            this.L(cr.y.U);
         } else {
            this.onClose();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.o();
            this.o.b();
            this.S = cr.y.U;
            this.C = (boolean)0;
            this.x = (boolean)(!this.x ? 1 : 0);
            this.A();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.a(cr.y.T);
               return true;
            }

            mq.s var5 = this.o.l();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.d(var5);
               return true;
            }

            if (this.q == null && var5 != null && !this.H(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.o.M(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            }

            if (this.q == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.o();
               this.o.S(() -> var5.r(true));
               return true;
            }

            this.D();
            boolean var6 = super.keyPressed(var1);
            if (!this.t()) {
               this.W();
            }

            return var6;
         } else {
            this.o();
            if (var2 != 89 && var4 == 0) {
               this.o.T();
            } else {
               this.o.z();
            }

            return true;
         }
      }
   }

   private void C() {
      this.C = (boolean)0;
      this.o();
      this.o.b();
   }

   public void tick() {
      if (this.o.k()) {
         this.p();
         this.A();
      }

      if (this.minecraft.level == this.a && this.minecraft.player != null) {
         boolean var1 = this.minecraft.isWindowActive();
         if (!var1 && this.c) {
            this.C();
         }

         this.c = var1;
      } else {
         this.onClose();
      }
   }

   public void resize(int var1, int var2) {
      if (this.o != null) {
         this.C();
      }

      super.resize(var1, var2);
   }

   public void onClose() {
      if (this.o != null) {
         this.o();
         this.o.G();
      }

      if (this.K) {
         this.h();
      }

      if (this.o != null && this.o.Y() || this.K) {
         oj.K().v("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", oj.u.B, 6000L);
      }

      this.minecraft.gui.setScreen(this.minecraft.level == this.a ? this.J : null);
   }

   public void removed() {
      if (this.o != null) {
         this.o();
         this.o.G();
      }

      mt.U("HUD editor", "Released canvas input");
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.o != null && this.minecraft.player != null && this.minecraft.level != null) {
         if (this.o.k()) {
            this.p();
            this.A();
         }

         this.d();
         if (cx.l.grid() && !this.U) {
            for (byte var5 = 0; var5 < this.width; var5 += 8) {
               c7.s(var1, var5, 0, 1, this.height, var5 % 40 == 0 ? 318767103 : 117440511);
            }

            for (byte var16 = 0; var16 < this.height; var16 += 8) {
               c7.s(var1, 0, var16, this.width, 1, var16 % 40 == 0 ? 318767103 : 117440511);
            }
         }

         mq.K(var1, this.o.l(), this.h, this.U);
         this.y(var1, var2, var3);
         this.c();
         xv var17 = this.J().t();

         try (f.s var6 = com.larpclient.f.F(var17.c, var17.T, var17.i, var17.O())) {
            var1.nextStratum();
            if (this.i.dock() != null) {
               G(var1, this.i.dock());

               for (hu.a var8 : this.W) {
                  var8.render(var1, var2, var3);
               }

               if (this.M.D) {
                  this.M.render(var1, var2, var3);
               }

               if (this.i.panel() == null && !this.o.J().s() && !this.M.D) {
                  String var18 = this.o.l() == null ? "HUD editor" : mq.w(this.o.l().g);
                  String var21 = this.o.l() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = k6.a(var18, k6.d.a);
                  int var10 = k6.a(var21, k6.d.K);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.i.dock().y() - 28;
                  pn.b(var1, var12, var13, var11, 21, 8, com.larpclient.f.h());
                  k6.T(var1, var18, var12 + 9, var13 + 6, com.larpclient.f.J, k6.d.a);
                  if (var9 + var10 + 28 <= var11) {
                     k6.T(var1, var21, var12 + var9 + 19, var13 + 7, com.larpclient.f.s, k6.d.K);
                  }
               }
            }

            this.v(var1, var2, var3);

            for (b var22 : this.m) {
               if (var22 instanceof un var24 && var24.g() && var22.D) {
                  var24.M(var1, var2, var3);
               }
            }

            if (this.G.stream().noneMatch(var0 -> var0 instanceof un var1x && var1x.g())) {
               for (hu.a var23 : this.W) {
                  if (var23.D && (var23.contains(var2, var3) || var23.l && var23.R)) {
                     var23.B(var1);
                  }
               }
            }
         }
      }
   }

   private static void G(GuiGraphicsExtractor var0, ez.h var1) {
      pn.i(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      pn.g(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, com.larpclient.f.h(), 0.75);
   }

   private void v(GuiGraphicsExtractor var1, int var2, int var3) {
      ez.h var4 = this.i.panel();
      ez.h var5 = this.i.body();
      if (var4 != null) {
         if (!this.d) {
            this.L.Q(1.0);
         }

         try (de var6 = de.l(this.L.G())) {
            G(var1, var4);
            String var7 = this.S == cr.y.T ? "Widgets" : (this.S == cr.y.r ? "Editor options" : mq.w(this.o.l().g));
            if (this.S == cr.y.c || this.S == cr.y.P) {
               tp.N(var1, tp.j(tp.M(this.o.l().g)), var4.x() + 13, var4.y() + 15, 13, com.larpclient.f.s);
            }

            int var8 = var4.x() + (this.S != cr.y.c && this.S != cr.y.P ? 14 : 34);
            k6.T(var1, k6.j(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, com.larpclient.f.J, k6.d.a);

            for (hu.a var10 : this.l) {
               var10.render(var1, var2, var3);
            }

            if (this.D != null) {
               this.D.render(var1, var2, var3);
            }

            var1.enableScissor(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (b var19 : this.m) {
                  if (var19.D) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.S == cr.y.T && this.m.stream().noneMatch(this::c)) {
                  k6.T(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, com.larpclient.f.s, k6.d.C);
               }
            } finally {
               var1.disableScissor();
            }

            pn.u(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.j, this.u);
         }
      }
   }

   private void y(GuiGraphicsExtractor var1, int var2, int var3) {
      List var4 = mq.K();
      mq.l var5 = this.i.canvasContains(var2, var3) ? ez.z(var4, var0 -> true, mq.l::bounds, var2, var3) : null;
      if (var5 != null && var5.element() != this.o.l()) {
         o(var1, var5.bounds(), -2137614688);
      }

      mq.s var6 = this.o.l();
      mq.e(var4, var6).ifPresent(var3x -> {
         o(var1, var3x, _3.A(com.larpclient.f.C, 0.85));
         if (!this.H(var6)) {
            ez.h var4x = ez.Z(var3x, this.width, this.height);
            pn.b(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            pn.b(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, com.larpclient.f.C);
         }

         if (this.o.J().s()) {
            String var8x = var6.W(this.width) + ", " + var6.i(this.height);
            int var5x = k6.a(var8x, k6.d.K) + 12;
            int var6x = ez.a(var3x.x(), var5x, this.width);
            int var7x = Math.max(2, var3x.y() - 20);
            pn.b(var1, var6x, var7x, var5x, 16, 5, -552003303);
            k6.T(var1, var8x, var6x + 6, var7x + 4, -1381654, k6.d.K);
         }
      });
      if (this.o.J().s()) {
         int var7 = this.o.J().T();
         int var8 = this.o.J().P();
         if (var7 >= 0) {
            c7.s(var1, Math.min(this.width - 1, var7), 0, 1, this.height, _3.A(com.larpclient.f.C, 0.45));
         }

         if (var8 >= 0) {
            c7.s(var1, 0, Math.min(this.height - 1, var8), this.width, 1, _3.A(com.larpclient.f.C, 0.45));
         }
      }
   }

   private static void o(GuiGraphicsExtractor var0, ez.h var1, int var2) {
      c7.t(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class a extends b {
      final String y;
      final String j;
      final Runnable G;
      final BooleanSupplier o;
      final ef K = new ef(0.0);
      boolean Y;
      boolean a;
      boolean n;

      a(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.y = var2;
         this.j = var3;
         this.G = var4;
         this.o = var5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.D) {
            boolean var4 = this.o.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.l || !this.R) ? 0 : 1;
            if (!hu.this.d) {
               this.K.Q(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.K.m(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.n ? com.larpclient.f.u : (this.X ? com.larpclient.f.J : com.larpclient.f.G);
            if (this.n || var4 || var6 > 0.001) {
               pn.b(
                  var1,
                  this.q,
                  this.J,
                  this.A,
                  this.W,
                  7,
                  this.n ? _3.D(com.larpclient.f.J, com.larpclient.f.s, var6 * 0.12) : _3.D(com.larpclient.f.u, com.larpclient.f.V, var4 ? 1.0 : var6)
               );
            }

            byte var9 = 13;
            int var10 = this.Y ? 0 : k6.P(this.y);
            int var11 = var10 + (this.j.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.q + Math.max(5, (this.A - var11) / 2);
            if (!this.j.isEmpty()) {
               var1.pose().pushMatrix();

               try {
                  if (this.a) {
                     var1.pose().translate(var12 * 2 + var9, 0.0F);
                     var1.pose().scale(-1.0F, 1.0F);
                  }

                  tp.N(var1, tp.j(this.j), this.Y ? this.q + (this.A - var9) / 2 : var12, this.J + (this.W - var9) / 2, var9, var8);
               } finally {
                  var1.pose().popMatrix();
               }
            }

            if (!this.Y) {
               k6.Z(var1, k6.j(this.y, this.A - (this.j.isEmpty() ? 12 : 27)), var12 + (this.j.isEmpty() ? 0 : var9 + 6), this.J + (this.W - 9) / 2, var8);
            }

            if (this.l && this.R) {
               pn.y(var1, this.q, this.J, this.A, this.W, 7);
            }
         }
      }

      void B(GuiGraphicsExtractor var1) {
         if (this.Y) {
            int var2 = k6.a(this.y, k6.d.K) + 16;
            int var3 = ez.a(this.q + (this.A - var2) / 2, var2, hu.this.width);
            int var4 = Math.max(0, this.J - 27);
            pn.b(var1, var3, var4, var2, 20, 6, com.larpclient.f.c);
            k6.T(var1, this.y, var3 + 8, var4 + 6, com.larpclient.f.J, k6.d.K);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            kb.d(kb.n.w);
            this.G.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.X && (var1 == 257 || var1 == 32)) {
            kb.d(kb.n.w);
            this.G.run();
            return true;
         } else {
            return false;
         }
      }
   }

   private final class l extends b {
      final mq.s c;
      private final ef S = new ef(0.0);
      private final ef V = new ef(0.0);

      l(mq.s var2) {
         this.c = var2;
         this.W = 36;
         this.V.Q(mq.M(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = mq.M(this.c);
         int var5 = hu.this.o.l() == this.c ? 1 : 0;
         if (!hu.this.d) {
            this.S.Q(this.contains(var2, var3) ? 1.0 : 0.0);
            this.V.Q(var4 ? 1.0 : 0.0);
         }

         double var6 = this.S.m(!this.contains(var2, var3) && !this.l ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            pn.b(var1, this.q, this.J, this.A, this.W, 7, _3.D(com.larpclient.f.u, com.larpclient.f.V, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         pn.b(var1, this.q + 4, this.J + 6, 24, 24, 6, com.larpclient.f.S);
         tp.N(var1, tp.j(tp.M(this.c.g)), this.q + 10, this.J + 12, 12, var4 ? com.larpclient.f.J : com.larpclient.f.s);
         k6.T(var1, k6.j(mq.w(this.c.g), this.A - 74), this.q + 36, this.J + 7, com.larpclient.f.J, k6.d.a);
         k6.T(var1, var4 ? "Visible" : "Hidden", this.q + 36, this.J + 21, com.larpclient.f.s, k6.d.K);
         pn.Y(var1, this.q + this.A - 30, this.J + 12, this.V.m(var4 ? 1.0 : 0.0, 180));
         if (this.l && this.R) {
            pn.y(var1, this.q, this.J, this.A, this.W, 7);
         }
      }

      private void T() {
         hu.this.o.S(() -> {
            mq.m(this.c, !mq.M(this.c));
            hu.this.o.y();
         });
         kb.d(mq.M(this.c) ? kb.n.l : kb.n.O);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            hu.this.d(this.c);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.q + this.A - 38) {
            this.T();
         } else {
            hu.this.W(this.c);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.T();
            return true;
         } else if (var1 == 257) {
            hu.this.W(this.c);
            return true;
         } else {
            return false;
         }
      }
   }
}
