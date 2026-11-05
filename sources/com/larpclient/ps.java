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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public final class ps extends pw {
   private static final List<String> w = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen e;
   private pm a;
   private pm.c M;
   private pm.d Z = pm.d.e;
   private final List<ps.f> O = new ArrayList<>();
   private final List<ps.f> g = new ArrayList<>();
   private final List<oq> p = new ArrayList<>();
   private final Set<oq> u = Collections.newSetFromMap(new IdentityHashMap<>());
   private final u7 F = new u7(1.0);
   private final Map<com.larpclient.module.l6<?>, Object> j = new IdentityHashMap<>();
   private p_ f;
   private ps.f c;
   private ps.f G;
   private ps.f N;
   private ps.f s;
   private com.larpclient.module.lc z;
   private com.larpclient.module.lc X;
   private com.larpclient.module.lc q;
   private com.larpclient.module.lp h;
   private com.larpclient.module.ln t;
   private com.larpclient.module.ln I;
   private boolean i = (boolean)1;
   private boolean B;
   private boolean R;
   private boolean d;
   private boolean Q = (boolean)1;
   private boolean v;
   private boolean o;
   private boolean n = (boolean)1;
   private double l;
   private double A;
   private int C;
   private int y;
   private int U = -1;
   private int E = -1;
   private static boolean m;
   private String J = "";
   private Object T;

   public ps(Screen var1) {
      super("HUD Editor");
      this.e = var1;
   }

   public boolean w() {
      return this.R;
   }

   private mo G() {
      return LarpClient.y().v();
   }

   private boolean W(o8.a var1) {
      return this.G().E().K(var1.M);
   }

   protected void init() {
      if (this.a == null) {
         com.larpclient.module.ly var1 = LarpClient.y().e();
         List var2 = Stream.of(var1.O("HUD"), var1.O("TargetHUD"), var1.O("Staff List"), var1.O("CustomCrosshair")).filter(Objects::nonNull).toList();
         this.a = new pm(() -> this.G().a(this.G().o()), this::W, var2);
         this.T = this.minecraft.level;
         this.v = m;
         r4.V("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.G().o());
      } else {
         this.a.g();
      }

      o8.d();
      this.w();
   }

   private ps.f i(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      ps.f var5 = new ps.f(var1, var2, var3, var4);
      this.k.add(var5);
      return var5;
   }

   private void s(com.larpclient.module.l6<?> var1, boolean var2) {
      p9 var3 = new p9(var1, true);
      this.p.add(var3);
      this.k.add(var3);
      if (var2) {
         this.u.add(var3);
      }
   }

   private void b(String var1, Runnable var2, boolean var3, boolean var4) {
      p7 var5 = new p7(var1, var2);
      var5.c = (boolean)1;
      var5.z = var4;
      var5.O = 28;
      this.p.add(var5);
      this.k.add(var5);
      if (var3) {
         this.u.add(var5);
      }
   }

   private void Z(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      com.larpclient.module.ln var5 = new com.larpclient.module.ln(var1, var2, var3);
      var5.W(var4);
      this.s(var5, false);
   }

   private void w() {
      this.u();
      this.O.clear();
      this.g.clear();
      this.p.clear();
      this.u.clear();
      this.f = null;
      this.z = this.X = this.q = null;
      this.h = null;
      this.t = this.I = null;
      this.O.add(this.i("Widgets", "panels-top-left", () -> this.k(pm.d.W), () -> this.Z == pm.d.W));
      this.c = this.i("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.X();
         this.a.n();
      }, () -> false);
      this.c.w = (boolean)1;
      this.O.add(this.c);
      this.G = this.i("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.X();
         this.a.L();
      }, () -> false);
      this.G.w = this.G.i = (boolean)1;
      this.O.add(this.G);
      ps.f var1 = this.i("Editor options", "settings", () -> this.k(pm.d.p), () -> this.Z == pm.d.p);
      var1.w = (boolean)1;
      this.O.add(var1);
      this.N = this.i("Edit", "paintbrush", () -> {
         if (this.a.k() != null) {
            if (this.Z != pm.d.f && this.Z != pm.d.b) {
               this.W(this.a.k());
            } else {
               this.s(pm.d.e);
            }
         }
      }, () -> this.Z == pm.d.f || this.Z == pm.d.b);
      this.O.add(this.N);
      ps.f var2 = this.i("Done", "", this::onClose, () -> false);
      var2.k = (boolean)1;
      this.O.add(var2);
      this.s = this.i("Save failed. Retry", "rotate-ccw", () -> {
         this.a.i();
         this.c();
      }, () -> false);
      if (this.Z != pm.d.e) {
         ps.f var3 = this.i("Close panel", "x", () -> this.s(pm.d.e), () -> false);
         var3.w = (boolean)1;
         this.g.add(var3);
         if (this.Z == pm.d.W) {
            this.f = new p_("Search widgets", var1x -> {
               this.J = var1x;
               this.C = 0;
               this.b();
            }, 80);
            this.f.V = this.J;
            this.k.add(this.f);

            for (o8.a var5 : o8.d) {
               ps.m var6 = new ps.m(var5);
               this.p.add(var6);
               this.k.add(var6);
            }
         } else if (this.Z == pm.d.p) {
            this.Z("Snap to guides", "Align with screen edges and center", fc.h.snapping(), var1x -> this.a.p(var1x, fc.h.grid()));
            this.Z("Show grid", "Show an eight-pixel placement grid", fc.h.grid(), var1x -> this.a.p(fc.h.snapping(), var1x));
            com.larpclient.module.ln var7 = new com.larpclient.module.ln("Preview hidden widgets", "Preview without enabling widgets", this.B);
            var7.x(() -> !this.R);
            var7.W(var1x -> this.B = var1x);
            p9 var9 = new p9(var7);
            this.p.add(var9);
            this.k.add(var9);
            this.Z("Use live content", "Show current gameplay content instead of samples", this.R, var1x -> {
               this.a.g();
               this.R = var1x;
               o8.d();
            });
            this.b("Reset layout", () -> {
               this.X();
               this.a.r();
            }, false, true);
         } else if (this.a.k() != null) {
            this.g.add(this.i("Appearance", "", () -> this.s(pm.d.f), () -> this.Z == pm.d.f));
            this.g.add(this.i("Position", "", () -> this.s(pm.d.b), () -> this.Z == pm.d.b));
            o8.a var8 = this.a.k();
            if (this.Z == pm.d.f) {
               this.t = new com.larpclient.module.ln("Show widget", "Keep this widget visible in gameplay", o8.r(var8));
               this.t.W(var2x -> {
                  if (!this.d) {
                     o8.a(var8, var2x);
                     this.a.i();
                  }
               });
               this.s(this.t, false);
               this.m(var8);

               for (com.larpclient.module.l6 var11 : o8.z(var8)) {
                  this.s(var11, false);
               }
            } else {
               this.I = new com.larpclient.module.ln("Lock position", "Prevent moving and resizing this widget", this.W(var8));
               this.I.W(var2x -> {
                  if (!this.d) {
                     this.W(var8, var2x);
                  }
               });
               this.s(this.I, false);
               this.z = new com.larpclient.module.lc("X", "Horizontal position in GUI pixels", var8.b(this.width), 0.0, this.width, 1.0);
               this.X = new com.larpclient.module.lc("Y", "Vertical position in GUI pixels", var8.M(this.height), 0.0, this.height, 1.0);
               this.z.W(var2x -> {
                  if (!this.d && !this.W(var8)) {
                     var8.i(var2x.intValue(), var8.M(this.height), this.width, this.height);
                  }
               });
               this.X.W(var2x -> {
                  if (!this.d && !this.W(var8)) {
                     var8.i(var8.b(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.s(this.z, true);
               this.s(this.X, true);
               this.m(var8);
               this.h = new com.larpclient.module.lp("Align", "Align within the full game viewport", S(var8), w.toArray(String[]::new));
               this.h.W(var2x -> {
                  if (!this.d && !this.W(var8) && !var2x.equals("Free")) {
                     int var3x = w.indexOf(var2x) - 1;
                     fc.m.put(var8.M, new fc.g(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.s(this.h, true);
               this.b("Reset placement", () -> {
                  this.X();
                  if (!this.W(var8)) {
                     this.a.u(var8::j);
                  }
               }, true, false);
            }
         }
      }

      this.b();
   }

   private void m(o8.a var1) {
      this.q = new com.larpclient.module.lc("Size", "Uniform widget scale", var1.H(), 0.5, 2.5, 0.01);
      this.q.W(var2 -> {
         if (!this.d && !this.W(var1)) {
            var1.m(var2);
         }
      });
      this.s(this.q, true);
   }

   private void k(pm.d var1) {
      this.s(this.Z == var1 ? pm.d.e : var1);
   }

   private void s(pm.d var1) {
      this.X();
      this.a.g();
      this.o = (boolean)0;
      this.i = (boolean)1;
      if (this.Z == pm.d.e || var1 == pm.d.W || var1 == pm.d.p) {
         this.U = this.E = -1;
      }

      this.Z = var1;
      this.C = 0;
      this.F.c(this.n ? 0.0 : 1.0);
      this.F.y(1.0, 160);
      this.w();
   }

   private void W(o8.a var1) {
      this.X();
      this.a.j(var1);
      this.g(var1);
      _h.p var2 = o8.B(o8.z(), var1).orElse(var1.v(this.width, this.height));
      this.U = var2.x() + var2.width() + 14;
      this.E = var2.y();
      if (this.U + 260 > this.width - 12) {
         this.U = Math.max(12, var2.x() - 274);
      }

      this.Z = pm.d.f;
      this.i = (boolean)1;
      this.C = 0;
      this.F.c(this.n ? 0.0 : 1.0);
      this.F.y(1.0, 160);
      this.w();
   }

   private void t(o8.a var1) {
      this.X();
      this.a.j(var1);
      this.g(var1);
      this.Z = pm.d.e;
      this.C = 0;
      this.w();
   }

   private void g(o8.a var1) {
      if (this.R && o8.B(o8.z(), var1).isEmpty()) {
         this.R = (boolean)0;
      }
   }

   private void W(o8.a var1, boolean var2) {
      this.a.g();
      this.G().E().R(var1.M, var2);
      this.c();
   }

   private void c() {
      this.v = m = (boolean)(!this.G().Y() ? 1 : 0);
   }

   private boolean h(o8.a var1) {
      return this.J.isBlank() || (o8.F(var1.M) + " " + var1.M).toLowerCase(Locale.ROOT).contains(this.J.toLowerCase(Locale.ROOT));
   }

   private boolean u(oq var1) {
      return !(var1 instanceof ps.m var2 && !this.h(var2.q));
   }

   private boolean k(oq var1) {
      return !(var1 instanceof p9 var2 && !var2.B());
   }

   private void b() {
      this.n = this.G().E().o;
      this.y = this.p.stream().filter(this::u).filter(this::k).mapToInt(var0 -> var0.O + 4).sum();
      this.M = pm.c.calculate(this.width, this.height, this.Z, this.y, this.U, this.E, this.i, this.a.d() || this.v);

      for (int var1 = 0; var1 < this.O.size(); var1++) {
         ps.f var2 = this.O.get(var1);
         var2.F = (boolean)(this.M.dock() != null ? 1 : 0);
         if (var2.F) {
            u(var2, this.M.tools().get(var1));
         }
      }

      this.c.x = this.a.I();
      this.G.x = this.a.v();
      this.N.x = (boolean)(this.a.k() != null ? 1 : 0);
      this.s.F = (boolean)(this.M.retry() != null ? 1 : 0);
      if (this.s.F) {
         u(this.s, this.M.retry());
      }

      _h.p var9 = this.M.panel();
      _h.p var10 = this.M.body();

      for (ps.f var4 : this.g) {
         var4.F = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.g.isEmpty()) {
         this.g.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.g.getFirst().O = 26;
         if (this.g.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.g.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.g.get(var13).O = 26;
            }
         }
      }

      if (this.f != null) {
         this.f.F = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.f.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.f.O = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.C = Math.clamp(this.C, 0, Math.max(0, this.y - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.C;

      for (oq var6 : this.p) {
         int var7 = this.u(var6) && this.k(var6) ? 1 : 0;
         var6.F = (boolean)(var10 != null && var7 != 0 && var14 + var6.O > var10.y() && var14 < var10.y() + var10.height() ? 1 : 0);
         var6.x = (boolean)(this.u.contains(var6) && this.a.k() != null && this.W(this.a.k()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof p9 var8) {
               var8.Y(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.O + 4;
         }
      }

      if (this.W != null && (!this.W.F || !this.W.x)) {
         this.W.blur();
         this.W = this.L = null;
         this.J();
      }
   }

   private static void u(oq var0, _h.p var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.O = var1.height();
   }

   @Override
   protected boolean accepts(oq var1, double var2, double var4) {
      return this.p.contains(var1)
         ? var1.contains(var2, var4) && this.M.controlContains(new _h.p(var1.B, var1.L, var1.v, var1.O), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(oq var1) {
      if (this.p.contains(var1) && this.M.body() != null) {
         _h.p var2 = this.M.body();
         if (var1.L < var2.y()) {
            this.C = this.C - (var2.y() - var1.L);
         }

         if (var1.L + var1.O > var2.y() + var2.height()) {
            this.C = this.C + (var1.L + var1.O - var2.y() - var2.height());
         }

         this.b();
      }
   }

   private static String S(o8.a var0) {
      fc.g var1 = fc.m.get(var0.M);
      if (var1 == null) {
         return var0.M.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return var2 >= 0 && var2 <= 2 && var3 >= 0 && var3 <= 2 && Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? w.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void l() {
      o8.a var1 = this.a.k();
      if (var1 != null) {
         this.d = (boolean)1;

         try {
            if (this.z != null) {
               this.z.C((double)var1.b(this.width));
            }

            if (this.X != null) {
               this.X.C((double)var1.M(this.height));
            }

            if (this.q != null) {
               this.q.C(var1.H());
            }

            if (this.h != null) {
               this.h.setValue(S(var1));
            }

            if (this.t != null) {
               this.t.setValue(o8.r(var1));
            }

            if (this.I != null) {
               this.I.setValue(this.W(var1));
            }
         } finally {
            this.d = (boolean)0;
         }
      }
   }

   private void j() {
      this.a.Q();
      if (this.j.isEmpty() && this.a.k() != null) {
         for (com.larpclient.module.l6 var2 : o8.z(this.a.k())) {
            this.j.put(var2, var2.getValue());
         }
      }
   }

   private void J() {
      if (this.j.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.a.i();
      }

      this.j.clear();
      this.a.Y();
   }

   private void W() {
      this.W = this.L = null;
      this.k.clear();
      this.j.clear();
      o8.d();
      this.Z = pm.d.e;
      this.o = (boolean)0;
      this.C = 0;
   }

   private void X() {
      if (this.a.g()) {
         this.W();
      } else {
         if (this.W != null) {
            this.W.blur();
         }

         this.W = this.L = null;
         this.J();
      }
   }

   private boolean S() {
      return this.W instanceof p_ || this.W instanceof p9 var1 && var1.C();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         if (this.a.g()) {
            this.W();
            this.w();
         }

         if (this.a.q().R()) {
            return true;
         }

         this.b();

         for (oq var4 : this.k) {
            if (var4 instanceof p9 var5 && var5.y()) {
               if (var5.R(var1.x(), var1.y())) {
                  this.j();
                  var5.N(var1.x(), var1.y(), r7.y(var1));
                  this.L = var5;
                  return true;
               }

               var5.n();
            }
         }

         oq var6 = this.k.stream().filter(var2x -> this.accepts(var2x, var1.x(), var1.y())).findFirst().orElse(null);
         if (var6 != this.W) {
            this.X();
         }

         if (var6 != null) {
            this.j();
            this.W = var6;
            var6.A = (boolean)1;
            var6.h = (boolean)0;
            if (var6.click(var1.x(), var1.y(), r7.y(var1))) {
               if (this.k.contains(var6)) {
                  this.L = var6;
               } else {
                  this.W = this.L = null;
               }
            }

            if (!(this.L instanceof p9) && !this.S()) {
               this.J();
            }

            return true;
         } else if (this.M.panelContains(var1.x(), var1.y())) {
            if (r7.y(var1) == 0 && var1.y() < this.M.panel().y() + 39) {
               this.o = (boolean)1;
               this.l = var1.x() - this.M.panel().x();
               this.A = var1.y() - this.M.panel().y();
            }

            return true;
         } else {
            if (this.M.dock() != null && this.M.dock().contains(var1.x(), var1.y(), 0)) {
               return true;
            }

            if (this.Z != pm.d.e) {
               this.s(pm.d.e);
            }

            if (this.M.canvasContains(var1.x(), var1.y())) {
               o8.r var7 = _h.N(o8.z(), var0 -> true, o8.r::bounds, var1.x(), var1.y());
               if (r7.y(var1) == 1) {
                  if (var7 == null) {
                     this.s(pm.d.W);
                  } else {
                     this.W(var7.element());
                  }
               } else if (r7.y(var1) == 0) {
                  this.j();
                  this.a.q().l(var1.x(), var1.y(), 0, this.width, this.height, o8.z(), false);
               }
            }

            if (!this.a.q().R()) {
               this.J();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.a.g()) {
         this.W();
         this.w();
         return true;
      } else if (!this.minecraft.isWindowActive()) {
         this.z();
         return true;
      } else if (this.o && this.M.panel() != null) {
         this.U = Math.max(0, (int)Math.round(var1.x() - this.l));
         this.E = Math.max(0, (int)Math.round(var1.y() - this.A));
         this.b();
         return true;
      } else {
         return this.a.q().R()
            ? this.a.q().w(var1.x(), var1.y(), r7.y(var1), this.width, this.height, this.minecraft.hasAltDown(), this.minecraft.hasShiftDown())
            : super.mouseDragged(var1, var2, var4);
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.a.g()) {
         this.W();
         this.w();
         return true;
      }

      this.o = (boolean)0;
      this.a.q().R(r7.y(var1));
      super.mouseReleased(var1);
      if (!this.S()) {
         this.J();
      }

      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.a.g()) {
         this.W();
         this.w();
         return true;
      }

      for (oq var10 : this.k) {
         if (var10 instanceof p9 var11 && var11.R(var1, var3)) {
            var11.J(var7);
            return true;
         }
      }

      if (this.M.panelContains(var1, var3)) {
         this.X();
         this.C = this.C - (int)Math.round(var7 * 28.0);
         this.b();
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.a.g()) {
         this.W();
         this.w();
         return true;
      } else {
         return super.charTyped(var1);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.a.g()) {
         this.W();
         this.w();
      }

      int var2 = r7.g(var1);
      int var3 = (var1.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (var1.modifiers() & 1) != 0 ? 1 : 0;
      if (this.S()) {
         if (var2 == 258) {
            this.X();
            return super.keyPressed(var1);
         }

         this.j();
         boolean var9 = super.keyPressed(var1);
         if (!this.S()) {
            this.J();
         }

         return var9;
      } else if (var2 == 256) {
         for (oq var11 : this.p) {
            if (var11 instanceof p9 var7 && var7.y()) {
               var7.n();
               return true;
            }
         }

         if (this.Z != pm.d.e) {
            this.s(pm.d.e);
         } else {
            this.onClose();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.X();
            this.a.g();
            this.Z = pm.d.e;
            this.o = (boolean)0;
            this.i = (boolean)(!this.i ? 1 : 0);
            this.w();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.k(pm.d.W);
               return true;
            }

            o8.a var5 = this.a.k();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.W(var5);
               return true;
            }

            if (this.W == null && var5 != null && !this.W(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.a.F(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            }

            if (this.W == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.X();
               this.a.u(() -> var5.W(true));
               return true;
            }

            this.j();
            boolean var6 = super.keyPressed(var1);
            if (!this.S()) {
               this.J();
            }

            return var6;
         } else {
            this.X();
            if (var2 != 89 && var4 == 0) {
               this.a.n();
            } else {
               this.a.L();
            }

            return true;
         }
      }
   }

   private void z() {
      this.o = (boolean)0;
      this.X();
      this.a.g();
   }

   public void tick() {
      if (this.a.g()) {
         this.W();
         this.w();
      }

      if (this.minecraft.level == this.T && this.minecraft.player != null) {
         boolean var1 = this.minecraft.isWindowActive();
         if (!var1 && this.Q) {
            this.z();
         }

         this.Q = var1;
      } else {
         this.onClose();
      }
   }

   public void resize(int var1, int var2) {
      if (this.a != null) {
         this.z();
      }

      super.resize(var1, var2);
   }

   public void onClose() {
      if (this.a != null) {
         this.X();
         this.a.p();
      }

      if (this.v) {
         this.c();
      }

      if (this.a != null && this.a.d() || this.v) {
         hg.B().J("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", hg.u.Z, 6000L);
      }

      this.minecraft.gui.setScreen(this.minecraft.level == this.T ? this.e : null);
   }

   public void removed() {
      if (this.a != null) {
         this.X();
         this.a.p();
      }

      r4.g("HUD editor", "Released canvas input");
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.a != null && this.minecraft.player != null && this.minecraft.level != null) {
         if (this.a.g()) {
            this.W();
            this.w();
         }

         this.b();
         if (fc.h.grid() && !this.R) {
            for (byte var5 = 0; var5 < this.width; var5 += 8) {
               fr.k(var1, var5, 0, 1, this.height, var5 % 40 == 0 ? 318767103 : 117440511);
            }

            for (byte var16 = 0; var16 < this.height; var16 += 8) {
               fr.k(var1, 0, var16, this.width, 1, var16 % 40 == 0 ? 318767103 : 117440511);
            }
         }

         o8.w(var1, this.a.k(), this.B, this.R);
         this.I(var1, var2, var3);
         this.l();
         fs var17 = this.G().E();

         try (rv.s var6 = rv.v(var17.Nw, var17.w, var17.b, var17.e())) {
            var1.nextStratum();
            if (this.M.dock() != null) {
               E(var1, this.M.dock());

               for (ps.f var8 : this.O) {
                  var8.render(var1, var2, var3);
               }

               if (this.s.F) {
                  this.s.render(var1, var2, var3);
               }

               if (this.M.panel() == null && !this.a.q().R() && !this.s.F) {
                  String var18 = this.a.k() == null ? "HUD editor" : o8.F(this.a.k().M);
                  String var21 = this.a.k() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = j_.o(var18, j_.m.d);
                  int var10 = j_.o(var21, j_.m.U);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.M.dock().y() - 28;
                  ll.N(var1, var12, var13, var11, 21, 8, rv.B());
                  j_.L(var1, var18, var12 + 9, var13 + 6, rv.P, j_.m.d);
                  if (var9 + var10 + 28 <= var11) {
                     j_.L(var1, var21, var12 + var9 + 19, var13 + 7, rv.G0, j_.m.U);
                  }
               }
            }

            this.L(var1, var2, var3);

            for (oq var22 : this.p) {
               if (var22 instanceof p9 var24 && var24.y() && var22.F) {
                  var24.x(var1, var2, var3);
               }
            }

            if (this.k.stream().noneMatch(var0 -> var0 instanceof p9 var1x && var1x.y())) {
               for (ps.f var23 : this.O) {
                  if (var23.F && (var23.contains(var2, var3) || var23.A && var23.h)) {
                     var23.G(var1);
                  }
               }
            }
         }
      }
   }

   private static void E(GuiGraphicsExtractor var0, _h.p var1) {
      ll.N(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      ll.Y(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, rv.B(), 0.75);
   }

   private void L(GuiGraphicsExtractor var1, int var2, int var3) {
      _h.p var4 = this.M.panel();
      _h.p var5 = this.M.body();
      if (var4 != null) {
         if (!this.n) {
            this.F.c(1.0);
         }

         try (r var6 = r.p(this.F.V())) {
            E(var1, var4);
            String var7 = this.Z == pm.d.W ? "Widgets" : (this.Z == pm.d.p ? "Editor options" : o8.F(this.a.k().M));
            if (this.Z == pm.d.f || this.Z == pm.d.b) {
               ou.u(var1, ou.e(ou.Y(this.a.k().M)), var4.x() + 13, var4.y() + 15, 13, rv.G0);
            }

            int var8 = var4.x() + (this.Z != pm.d.f && this.Z != pm.d.b ? 14 : 34);
            j_.L(var1, j_.S(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, rv.P, j_.m.d);

            for (ps.f var10 : this.g) {
               var10.render(var1, var2, var3);
            }

            if (this.f != null) {
               this.f.render(var1, var2, var3);
            }

            var1.enableScissor(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (oq var19 : this.p) {
                  if (var19.F) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.Z == pm.d.W && this.p.stream().noneMatch(this::u)) {
                  j_.L(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, rv.G0, j_.m.r);
               }
            } finally {
               var1.disableScissor();
            }

            ll.g(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.y, this.C);
         }
      }
   }

   private void I(GuiGraphicsExtractor var1, int var2, int var3) {
      List var4 = o8.z();
      o8.r var5 = this.M.canvasContains(var2, var3) ? _h.N(var4, var0 -> true, o8.r::bounds, var2, var3) : null;
      if (var5 != null && var5.element() != this.a.k()) {
         Z(var1, var5.bounds(), -2137614688);
      }

      o8.a var6 = this.a.k();
      o8.B(var4, var6).ifPresent(var3x -> {
         Z(var1, var3x, oh.i(rv.e, 0.85));
         if (!this.W(var6)) {
            _h.p var4x = _h.G(var3x, this.width, this.height);
            ll.N(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            ll.N(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, rv.e);
         }

         if (this.a.q().R()) {
            String var8x = var6.b(this.width) + ", " + var6.M(this.height);
            int var5x = j_.o(var8x, j_.m.U) + 12;
            int var6x = _h.n(var3x.x(), var5x, this.width);
            int var7x = Math.max(2, var3x.y() - 20);
            ll.N(var1, var6x, var7x, var5x, 16, 5, -552003303);
            j_.L(var1, var8x, var6x + 6, var7x + 4, -1381654, j_.m.U);
         }
      });
      if (this.a.q().R()) {
         int var7 = this.a.q().h();
         int var8 = this.a.q().m();
         if (var7 >= 0) {
            fr.k(var1, Math.min(this.width - 1, var7), 0, 1, this.height, oh.i(rv.e, 0.45));
         }

         if (var8 >= 0) {
            fr.k(var1, 0, Math.min(this.height - 1, var8), this.width, 1, oh.i(rv.e, 0.45));
         }
      }
   }

   private static void Z(GuiGraphicsExtractor var0, _h.p var1, int var2) {
      fr.K(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class f extends oq {
      final String V;
      final String S;
      final Runnable E;
      final BooleanSupplier Y;
      final u7 o = new u7(0.0);
      boolean w;
      boolean i;
      boolean k;

      f(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.V = var2;
         this.S = var3;
         this.E = var4;
         this.Y = var5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.F) {
            boolean var4 = this.Y.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.A || !this.h) ? 0 : 1;
            if (!ps.this.n) {
               this.o.c(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.o.y(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.k ? rv.w : (this.x ? rv.P : rv.r);
            if (this.k || var4 || var6 > 0.001) {
               ll.N(var1, this.B, this.L, this.v, this.O, 7, this.k ? oh.j(rv.P, rv.G0, var6 * 0.12) : oh.j(rv.w, rv.K, var4 ? 1.0 : var6));
            }

            byte var9 = 13;
            int var10 = this.w ? 0 : j_.a(this.V);
            int var11 = var10 + (this.S.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.B + Math.max(5, (this.v - var11) / 2);
            if (!this.S.isEmpty()) {
               var1.pose().pushMatrix();

               try {
                  if (this.i) {
                     var1.pose().translate(var12 * 2 + var9, 0.0F);
                     var1.pose().scale(-1.0F, 1.0F);
                  }

                  ou.u(var1, ou.e(this.S), this.w ? this.B + (this.v - var9) / 2 : var12, this.L + (this.O - var9) / 2, var9, var8);
               } finally {
                  var1.pose().popMatrix();
               }
            }

            if (!this.w) {
               j_.E(var1, j_.S(this.V, this.v - (this.S.isEmpty() ? 12 : 27)), var12 + (this.S.isEmpty() ? 0 : var9 + 6), this.L + (this.O - 9) / 2, var8);
            }

            if (this.A && this.h) {
               ll.w(var1, this.B, this.L, this.v, this.O, 7);
            }
         }
      }

      void G(GuiGraphicsExtractor var1) {
         if (this.w) {
            int var2 = j_.o(this.V, j_.m.U) + 16;
            int var3 = _h.n(this.B + (this.v - var2) / 2, var2, ps.this.width);
            int var4 = Math.max(0, this.L - 27);
            ll.N(var1, var3, var4, var2, 20, 6, rv.Y);
            j_.L(var1, this.V, var3 + 8, var4 + 6, rv.P, j_.m.U);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            hx.w(hx.t.X);
            this.E.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.x && (var1 == 257 || var1 == 32)) {
            hx.w(hx.t.X);
            this.E.run();
            return true;
         } else {
            return false;
         }
      }
   }

   private final class m extends oq {
      final o8.a q;
      private final u7 S = new u7(0.0);
      private final u7 g = new u7(0.0);

      m(o8.a var2) {
         this.q = var2;
         this.O = 36;
         this.g.c(o8.r(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = o8.r(this.q);
         int var5 = ps.this.a.k() == this.q ? 1 : 0;
         if (!ps.this.n) {
            this.S.c(this.contains(var2, var3) ? 1.0 : 0.0);
            this.g.c(var4 ? 1.0 : 0.0);
         }

         double var6 = this.S.y(!this.contains(var2, var3) && !this.A ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            ll.N(var1, this.B, this.L, this.v, this.O, 7, oh.j(rv.w, rv.K, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         ll.N(var1, this.B + 4, this.L + 6, 24, 24, 6, rv.z);
         ou.u(var1, ou.e(ou.Y(this.q.M)), this.B + 10, this.L + 12, 12, var4 ? rv.P : rv.G0);
         j_.L(var1, j_.S(o8.F(this.q.M), this.v - 74), this.B + 36, this.L + 7, rv.P, j_.m.d);
         j_.L(var1, var4 ? "Visible" : "Hidden", this.B + 36, this.L + 21, rv.G0, j_.m.U);
         ll.e(var1, this.B + this.v - 30, this.L + 12, this.g.y(var4 ? 1.0 : 0.0, 180));
         if (this.A && this.h) {
            ll.w(var1, this.B, this.L, this.v, this.O, 7);
         }
      }

      private void B() {
         ps.this.a.u(() -> {
            o8.a(this.q, !o8.r(this.q));
            ps.this.a.i();
         });
         hx.w(o8.r(this.q) ? hx.t.N : hx.t.F);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            ps.this.W(this.q);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.B + this.v - 38) {
            this.B();
         } else {
            ps.this.t(this.q);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.B();
            return true;
         } else if (var1 == 257) {
            ps.this.t(this.q);
            return true;
         } else {
            return false;
         }
      }
   }
}
