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

public final class h7 extends h3 {
   private static final List<String> t = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen w;
   private pc Y;
   private pc.q d;
   private pc.v k = pc.v.D;
   private final List<h7.h> m = new ArrayList<>();
   private final List<h7.h> W = new ArrayList<>();
   private final List<jd> J = new ArrayList<>();
   private final Set<jd> s = Collections.newSetFromMap(new IdentityHashMap<>());
   private final jp a = new jp(1.0);
   private final Map<com.larpclient.module.q<?>, Object> n = new IdentityHashMap<>();
   private j9 M;
   private h7.h E;
   private h7.h C;
   private h7.h Q;
   private h7.h e;
   private com.larpclient.module.e z;
   private com.larpclient.module.e H;
   private com.larpclient.module.e D;
   private com.larpclient.module.l r;
   private com.larpclient.module.s l;
   private com.larpclient.module.s N;
   private boolean B = (boolean)1;
   private boolean V;
   private boolean K;
   private boolean Z;
   private boolean f = (boolean)1;
   private boolean b;
   private boolean U;
   private boolean g = (boolean)1;
   private double G;
   private double I;
   private int i;
   private int P;
   private int j = -1;
   private int v = -1;
   private static boolean T;
   private String A = "";
   private Object c;

   public h7(Screen var1) {
      super("HUD Editor");
      this.w = var1;
   }

   public boolean G() {
      return this.K;
   }

   private gg i() {
      return LarpClient.R().o();
   }

   private boolean S(m4.p var1) {
      return this.i().g().t(var1.r);
   }

   protected void init() {
      if (this.Y == null) {
         com.larpclient.module.f var1 = LarpClient.R().z();
         List var2 = Stream.of(var1.d("HUD"), var1.d("TargetHUD"), var1.d("Staff List"), var1.d("CustomCrosshair")).filter(Objects::nonNull).toList();
         this.Y = new pc(() -> this.i().K(this.i().M()), this::S, var2);
         this.c = this.minecraft.level;
         this.b = T;
         com.larpclient.h.A("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.i().M());
      } else {
         this.Y.o();
      }

      m4.a();
      this.Y();
   }

   private h7.h O(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      h7.h var5 = new h7.h(var1, var2, var3, var4);
      this.p.add(var5);
      return var5;
   }

   private void L(com.larpclient.module.q<?> var1, boolean var2) {
      ju var3 = new ju(var1, true);
      this.J.add(var3);
      this.p.add(var3);
      if (var2) {
         this.s.add(var3);
      }
   }

   private void S(String var1, Runnable var2, boolean var3, boolean var4) {
      jx var5 = new jx(var1, var2);
      var5.g = (boolean)1;
      var5.a = var4;
      var5.J = 28;
      this.J.add(var5);
      this.p.add(var5);
      if (var3) {
         this.s.add(var5);
      }
   }

   private void j(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      com.larpclient.module.s var5 = new com.larpclient.module.s(var1, var2, var3);
      var5.o(var4);
      this.L(var5, false);
   }

   private void Y() {
      this.u();
      this.m.clear();
      this.W.clear();
      this.J.clear();
      this.s.clear();
      this.M = null;
      this.z = this.H = this.D = null;
      this.r = null;
      this.l = this.N = null;
      this.m.add(this.O("Widgets", "panels-top-left", () -> this.N(pc.v.d), () -> this.k == pc.v.d));
      this.E = this.O("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.R();
         this.Y.y();
      }, () -> false);
      this.E.B = (boolean)1;
      this.m.add(this.E);
      this.C = this.O("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.R();
         this.Y.a();
      }, () -> false);
      this.C.B = this.C.q = (boolean)1;
      this.m.add(this.C);
      h7.h var1 = this.O("Editor options", "settings", () -> this.N(pc.v.s), () -> this.k == pc.v.s);
      var1.B = (boolean)1;
      this.m.add(var1);
      this.Q = this.O("Edit", "paintbrush", () -> {
         if (this.Y.z() != null) {
            if (this.k != pc.v.o && this.k != pc.v.F) {
               this.C(this.Y.z());
            } else {
               this.p(pc.v.D);
            }
         }
      }, () -> this.k == pc.v.o || this.k == pc.v.F);
      this.m.add(this.Q);
      h7.h var2 = this.O("Done", "", this::onClose, () -> false);
      var2.H = (boolean)1;
      this.m.add(var2);
      this.e = this.O("Save failed. Retry", "rotate-ccw", () -> {
         this.Y.Z();
         this.D();
      }, () -> false);
      if (this.k != pc.v.D) {
         h7.h var3 = this.O("Close panel", "x", () -> this.p(pc.v.D), () -> false);
         var3.B = (boolean)1;
         this.W.add(var3);
         if (this.k == pc.v.d) {
            this.M = new j9("Search widgets", var1x -> {
               this.A = var1x;
               this.i = 0;
               this.G();
            }, 80);
            this.M.H = this.A;
            this.p.add(this.M);

            for (m4.p var5 : m4.P()) {
               h7.n var6 = new h7.n(var5);
               this.J.add(var6);
               this.p.add(var6);
            }
         } else if (this.k == pc.v.s) {
            this.j("Snap to guides", "Align with screen edges and center", c7.B.snapping(), var1x -> this.Y.X(var1x, c7.B.grid()));
            this.j("Show grid", "Show an eight-pixel placement grid", c7.B.grid(), var1x -> this.Y.X(c7.B.snapping(), var1x));
            com.larpclient.module.s var7 = new com.larpclient.module.s("Preview hidden widgets", "Preview without enabling widgets", this.V);
            var7.P(() -> !this.K);
            var7.o(var1x -> this.V = var1x);
            ju var9 = new ju(var7);
            this.J.add(var9);
            this.p.add(var9);
            this.j("Use live content", "Show current gameplay content instead of samples", this.K, var1x -> {
               this.Y.o();
               this.K = var1x;
               m4.a();
            });
            this.S("Reset layout", () -> {
               this.R();
               this.Y.k();
            }, false, true);
         } else if (this.Y.z() != null) {
            this.W.add(this.O("Appearance", "", () -> this.p(pc.v.o), () -> this.k == pc.v.o));
            this.W.add(this.O("Position", "", () -> this.p(pc.v.F), () -> this.k == pc.v.F));
            m4.p var8 = this.Y.z();
            if (this.k == pc.v.o) {
               this.l = new com.larpclient.module.s("Show widget", "Keep this widget visible in gameplay", m4.B(var8));
               this.l.o(var2x -> {
                  if (!this.Z) {
                     m4.Z(var8, var2x);
                     this.Y.H();
                  }
               });
               this.L(this.l, false);
               this.P(var8);

               for (com.larpclient.module.q var11 : m4.y(var8)) {
                  this.L(var11, false);
               }
            } else {
               this.N = new com.larpclient.module.s("Lock position", "Prevent moving and resizing this widget", this.S(var8));
               this.N.o(var2x -> {
                  if (!this.Z) {
                     this.t(var8, var2x);
                  }
               });
               this.L(this.N, false);
               this.z = new com.larpclient.module.e("X", "Horizontal position in GUI pixels", var8.T(this.width), 0.0, this.width, 1.0);
               this.H = new com.larpclient.module.e("Y", "Vertical position in GUI pixels", var8.P(this.height), 0.0, this.height, 1.0);
               this.z.o(var2x -> {
                  if (!this.Z && !this.S(var8)) {
                     var8.E(var2x.intValue(), var8.P(this.height), this.width, this.height);
                  }
               });
               this.H.o(var2x -> {
                  if (!this.Z && !this.S(var8)) {
                     var8.E(var8.T(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.L(this.z, true);
               this.L(this.H, true);
               this.P(var8);
               this.r = new com.larpclient.module.l("Align", "Align within the full game viewport", m(var8), t.toArray(String[]::new));
               this.r.o(var2x -> {
                  if (!this.Z && !this.S(var8) && !var2x.equals("Free")) {
                     int var3x = t.indexOf(var2x) - 1;
                     c7.v.put(var8.r, new c7.q(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.L(this.r, true);
               this.S("Reset placement", () -> {
                  this.R();
                  if (!this.S(var8)) {
                     this.Y.l(var8::g);
                  }
               }, true, false);
            }
         }
      }

      this.G();
   }

   private void P(m4.p var1) {
      this.D = new com.larpclient.module.e("Size", "Uniform widget scale", var1.k(), 0.2, 4.0, 0.01);
      this.D.o(var2 -> {
         if (!this.Z && !this.S(var1)) {
            var1.f(var2);
         }
      });
      this.L(this.D, true);
   }

   private void N(pc.v var1) {
      this.p(this.k == var1 ? pc.v.D : var1);
   }

   private void p(pc.v var1) {
      this.R();
      this.Y.o();
      this.U = (boolean)0;
      this.B = (boolean)1;
      if (this.k == pc.v.D || var1 == pc.v.d || var1 == pc.v.s) {
         this.j = this.v = -1;
      }

      this.k = var1;
      this.i = 0;
      this.a.F(this.g ? 0.0 : 1.0);
      this.a.k(1.0, 160);
      this.Y();
   }

   private void C(m4.p var1) {
      this.R();
      this.Y.x(var1);
      this.u(var1);
      hw.l var2 = m4.J(m4.J(), var1).orElse(var1.X(this.width, this.height));
      this.j = var2.x() + var2.width() + 14;
      this.v = var2.y();
      if (this.j + 260 > this.width - 12) {
         this.j = Math.max(12, var2.x() - 274);
      }

      this.k = pc.v.o;
      this.B = (boolean)1;
      this.i = 0;
      this.a.F(this.g ? 0.0 : 1.0);
      this.a.k(1.0, 160);
      this.Y();
   }

   private void p(m4.p var1) {
      this.R();
      this.Y.x(var1);
      this.u(var1);
      this.k = pc.v.D;
      this.i = 0;
      this.Y();
   }

   private void u(m4.p var1) {
      if (this.K && m4.J(m4.J(), var1).isEmpty()) {
         this.K = (boolean)0;
      }
   }

   private void t(m4.p var1, boolean var2) {
      this.Y.o();
      this.i().g().K(var1.r, var2);
      this.D();
   }

   private void D() {
      this.b = T = (boolean)(!this.i().r() ? 1 : 0);
   }

   private boolean c(m4.p var1) {
      return this.A.isBlank() || (m4.W(var1.r) + " " + var1.r).toLowerCase(Locale.ROOT).contains(this.A.toLowerCase(Locale.ROOT));
   }

   private boolean G(jd var1) {
      return !(var1 instanceof h7.n var2 && !this.c(var2.a));
   }

   private boolean u(jd var1) {
      return !(var1 instanceof ju var2 && !var2.d());
   }

   private void G() {
      this.g = this.i().g().L;
      this.P = this.J.stream().filter(this::G).filter(this::u).mapToInt(var0 -> var0.J + 4).sum();
      this.d = pc.q.calculate(this.width, this.height, this.k, this.P, this.j, this.v, this.B, this.Y.S() || this.b);

      for (int var1 = 0; var1 < this.m.size(); var1++) {
         h7.h var2 = this.m.get(var1);
         var2.i = (boolean)(this.d.dock() != null ? 1 : 0);
         if (var2.i) {
            G(var2, this.d.tools().get(var1));
         }
      }

      this.E.p = this.Y.q();
      this.C.p = this.Y.e();
      this.Q.p = (boolean)(this.Y.z() != null ? 1 : 0);
      this.e.i = (boolean)(this.d.retry() != null ? 1 : 0);
      if (this.e.i) {
         G(this.e, this.d.retry());
      }

      hw.l var9 = this.d.panel();
      hw.l var10 = this.d.body();

      for (h7.h var4 : this.W) {
         var4.i = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.W.isEmpty()) {
         this.W.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.W.getFirst().J = 26;
         if (this.W.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.W.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.W.get(var13).J = 26;
            }
         }
      }

      if (this.M != null) {
         this.M.i = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.M.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.M.J = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.i = Math.clamp(this.i, 0, Math.max(0, this.P - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.i;

      for (jd var6 : this.J) {
         int var7 = this.G(var6) && this.u(var6) ? 1 : 0;
         var6.i = (boolean)(var10 != null && var7 != 0 && var14 + var6.J > var10.y() && var14 < var10.y() + var10.height() ? 1 : 0);
         var6.p = (boolean)(this.s.contains(var6) && this.Y.z() != null && this.S(this.Y.z()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof ju var8) {
               var8.q(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.J + 4;
         }
      }

      if (this.o != null && (!this.o.i || !this.o.p)) {
         this.o.blur();
         this.o = this.F = null;
         this.V();
      }
   }

   private static void G(jd var0, hw.l var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.J = var1.height();
   }

   @Override
   protected boolean accepts(jd var1, double var2, double var4) {
      return this.J.contains(var1)
         ? var1.contains(var2, var4) && this.d.controlContains(new hw.l(var1.S, var1.u, var1.A, var1.J), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(jd var1) {
      if (this.J.contains(var1) && this.d.body() != null) {
         hw.l var2 = this.d.body();
         if (var1.u < var2.y()) {
            this.i = this.i - (var2.y() - var1.u);
         }

         if (var1.u + var1.J > var2.y() + var2.height()) {
            this.i = this.i + (var1.u + var1.J - var2.y() - var2.height());
         }

         this.G();
      }
   }

   private static String m(m4.p var0) {
      c7.q var1 = c7.v.get(var0.r);
      if (var1 == null) {
         return var0.r.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return var2 >= 0 && var2 <= 2 && var3 >= 0 && var3 <= 2 && Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? t.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void X() {
      m4.p var1 = this.Y.z();
      if (var1 != null) {
         this.Z = (boolean)1;

         try {
            if (this.z != null) {
               this.z.X((double)var1.T(this.width));
            }

            if (this.H != null) {
               this.H.X((double)var1.P(this.height));
            }

            if (this.D != null) {
               this.D.X(var1.k());
            }

            if (this.r != null) {
               this.r.setValue(m(var1));
            }

            if (this.l != null) {
               this.l.setValue(m4.B(var1));
            }

            if (this.N != null) {
               this.N.setValue(this.S(var1));
            }
         } finally {
            this.Z = (boolean)0;
         }
      }
   }

   private void Z() {
      this.Y.j();
      if (this.n.isEmpty() && this.Y.z() != null) {
         for (com.larpclient.module.q var2 : m4.y(this.Y.z())) {
            this.n.put(var2, var2.getValue());
         }
      }
   }

   private void V() {
      if (this.n.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.Y.H();
      }

      this.n.clear();
      this.Y.o();
   }

   private void I() {
      this.o = this.F = null;
      this.p.clear();
      this.n.clear();
      m4.a();
      this.k = pc.v.D;
      this.U = (boolean)0;
      this.i = 0;
   }

   private void R() {
      if (this.Y.E()) {
         this.I();
      } else {
         if (this.o != null) {
            this.o.blur();
         }

         this.o = this.F = null;
         this.V();
      }
   }

   private boolean A() {
      return this.o instanceof j9 || this.o instanceof ju var1 && var1.k();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         if (this.Y.E()) {
            this.I();
            this.Y();
         }

         if (this.Y.y().H()) {
            return true;
         }

         this.G();

         for (jd var4 : this.p) {
            if (var4 instanceof ju var5 && var5.m()) {
               if (var5.y(var1.x(), var1.y())) {
                  this.Z();
                  var5.X(var1.x(), var1.y(), k2.w(var1));
                  this.F = var5;
                  return true;
               }

               var5.d();
            }
         }

         jd var6 = this.p.stream().filter(var2x -> this.accepts(var2x, var1.x(), var1.y())).findFirst().orElse(null);
         if (var6 != this.o) {
            this.R();
         }

         if (var6 != null) {
            this.Z();
            this.o = var6;
            var6.T = (boolean)1;
            var6.W = (boolean)0;
            if (var6.click(var1.x(), var1.y(), k2.w(var1))) {
               if (this.p.contains(var6)) {
                  this.F = var6;
               } else {
                  this.o = this.F = null;
               }
            }

            if (!(this.F instanceof ju) && !this.A()) {
               this.V();
            }

            return true;
         } else if (this.d.panelContains(var1.x(), var1.y())) {
            if (k2.w(var1) == 0 && var1.y() < this.d.panel().y() + 39) {
               this.U = (boolean)1;
               this.G = var1.x() - this.d.panel().x();
               this.I = var1.y() - this.d.panel().y();
            }

            return true;
         } else {
            if (this.d.dock() != null && this.d.dock().contains(var1.x(), var1.y(), 0)) {
               return true;
            }

            if (this.k != pc.v.D) {
               this.p(pc.v.D);
            }

            if (this.d.canvasContains(var1.x(), var1.y())) {
               m4.k var7 = hw.u(m4.J(), var0 -> true, m4.k::bounds, var1.x(), var1.y());
               if (k2.w(var1) == 1) {
                  if (var7 == null) {
                     this.p(pc.v.d);
                  } else {
                     this.C(var7.element());
                  }
               } else if (k2.w(var1) == 0) {
                  this.Z();
                  this.Y.y().R(var1.x(), var1.y(), 0, this.width, this.height, m4.J(), false);
               }
            }

            if (!this.Y.y().H()) {
               this.V();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.Y.E()) {
         this.I();
         this.Y();
         return true;
      } else if (!this.minecraft.isWindowActive()) {
         this.s();
         return true;
      } else if (this.U && this.d.panel() != null) {
         this.j = Math.max(0, (int)Math.round(var1.x() - this.G));
         this.v = Math.max(0, (int)Math.round(var1.y() - this.I));
         this.G();
         return true;
      } else {
         return this.Y.y().H()
            ? this.Y.y().t(var1.x(), var1.y(), k2.w(var1), this.width, this.height, this.minecraft.hasAltDown(), this.minecraft.hasShiftDown())
            : super.mouseDragged(var1, var2, var4);
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.Y.E()) {
         this.I();
         this.Y();
         return true;
      }

      this.U = (boolean)0;
      this.Y.y().O(k2.w(var1));
      super.mouseReleased(var1);
      if (!this.A()) {
         this.V();
      }

      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.Y.E()) {
         this.I();
         this.Y();
         return true;
      }

      for (jd var10 : this.p) {
         if (var10 instanceof ju var11 && var11.y(var1, var3)) {
            var11.N(var7);
            return true;
         }
      }

      if (this.d.panelContains(var1, var3)) {
         this.R();
         this.i = this.i - (int)Math.round(var7 * 28.0);
         this.G();
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.Y.E()) {
         this.I();
         this.Y();
         return true;
      } else {
         return super.charTyped(var1);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.Y.E()) {
         this.I();
         this.Y();
      }

      int var2 = k2.a(var1);
      int var3 = (var1.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (var1.modifiers() & 1) != 0 ? 1 : 0;
      if (this.A()) {
         if (var2 == 258) {
            this.R();
            return super.keyPressed(var1);
         }

         this.Z();
         boolean var9 = super.keyPressed(var1);
         if (!this.A()) {
            this.V();
         }

         return var9;
      } else if (var2 == 256) {
         for (jd var11 : this.J) {
            if (var11 instanceof ju var7 && var7.m()) {
               var7.d();
               return true;
            }
         }

         if (this.k != pc.v.D) {
            this.p(pc.v.D);
         } else {
            this.onClose();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.R();
            this.Y.o();
            this.k = pc.v.D;
            this.U = (boolean)0;
            this.B = (boolean)(!this.B ? 1 : 0);
            this.Y();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.N(pc.v.d);
               return true;
            }

            m4.p var5 = this.Y.z();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.C(var5);
               return true;
            }

            if (this.o == null && var5 != null && !this.S(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.Y.R(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            }

            if (this.o == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.R();
               this.Y.l(() -> var5.z(true));
               return true;
            }

            this.Z();
            boolean var6 = super.keyPressed(var1);
            if (!this.A()) {
               this.V();
            }

            return var6;
         } else {
            this.R();
            if (var2 != 89 && var4 == 0) {
               this.Y.y();
            } else {
               this.Y.a();
            }

            return true;
         }
      }
   }

   private void s() {
      this.U = (boolean)0;
      this.R();
      this.Y.o();
   }

   public void tick() {
      if (this.Y.E()) {
         this.I();
         this.Y();
      }

      if (this.minecraft.level == this.c && this.minecraft.player != null) {
         boolean var1 = this.minecraft.isWindowActive();
         if (!var1 && this.f) {
            this.s();
         }

         this.f = var1;
      } else {
         this.onClose();
      }
   }

   public void resize(int var1, int var2) {
      if (this.Y != null) {
         this.s();
      }

      super.resize(var1, var2);
   }

   public void onClose() {
      if (this.Y != null) {
         this.R();
         this.Y.I();
      }

      if (this.b) {
         this.D();
      }

      if (this.Y != null && this.Y.S() || this.b) {
         s0.s().Z("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", s0.u.p, 6000L);
      }

      this.minecraft.gui.setScreen(this.minecraft.level == this.c ? this.w : null);
   }

   public void removed() {
      if (this.Y != null) {
         this.R();
         this.Y.I();
      }

      com.larpclient.h.U("HUD editor", "Released canvas input");
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.Y != null && this.minecraft.player != null && this.minecraft.level != null) {
         if (this.Y.E()) {
            this.I();
            this.Y();
         }

         this.G();
         if (c7.B.grid() && !this.K) {
            s4.P(var1, this.width, this.height, this.height, 8, 5, 117440511, 318767103);
         }

         m4.N(var1, this.Y.z(), this.V, this.K);
         this.o(var1, var2, var3);
         this.X();
         hb var5 = this.i().g();

         try (ho.y var6 = ho.V(var5.f, var5.r, var5.W, var5.b())) {
            var1.nextStratum();
            if (this.d.dock() != null) {
               K(var1, this.d.dock());

               for (h7.h var8 : this.m) {
                  var8.render(var1, var2, var3);
               }

               if (this.e.i) {
                  this.e.render(var1, var2, var3);
               }

               if (this.d.panel() == null && !this.Y.y().H() && !this.e.i) {
                  String var16 = this.Y.z() == null ? "HUD editor" : m4.W(this.Y.z().r);
                  String var19 = this.Y.z() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = bj.e(var16, bj.u.i);
                  int var10 = bj.e(var19, bj.u.M);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.d.dock().y() - 28;
                  wp.I(var1, var12, var13, var11, 21, 8, ho.c());
                  bj.k(var1, var16, var12 + 9, var13 + 6, ho.u, bj.u.i);
                  if (var9 + var10 + 28 <= var11) {
                     bj.k(var1, var19, var12 + var9 + 19, var13 + 7, ho.Z, bj.u.M);
                  }
               }
            }

            this.B(var1, var2, var3);

            for (jd var20 : this.J) {
               if (var20 instanceof ju var22 && var22.m() && var20.i) {
                  var22.J(var1, var2, var3);
               }
            }

            if (this.p.stream().noneMatch(var0 -> var0 instanceof ju var1x && var1x.m())) {
               for (h7.h var21 : this.m) {
                  if (var21.i && (var21.contains(var2, var3) || var21.T && var21.W)) {
                     var21.t(var1);
                  }
               }
            }
         }
      }
   }

   private static void K(GuiGraphicsExtractor var0, hw.l var1) {
      wp.T(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      wp.Z(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, ho.c(), 0.75);
   }

   private void B(GuiGraphicsExtractor var1, int var2, int var3) {
      hw.l var4 = this.d.panel();
      hw.l var5 = this.d.body();
      if (var4 != null) {
         if (!this.g) {
            this.a.F(1.0);
         }

         try (ua var6 = ua.K(this.a.B())) {
            K(var1, var4);
            String var7 = this.k == pc.v.d ? "Widgets" : (this.k == pc.v.s ? "Editor options" : m4.W(this.Y.z().r));
            if (this.k == pc.v.o || this.k == pc.v.F) {
               ur.O(var1, ur.V(ur.t(this.Y.z().r)), var4.x() + 13, var4.y() + 15, 13, ho.Z);
            }

            int var8 = var4.x() + (this.k != pc.v.o && this.k != pc.v.F ? 14 : 34);
            bj.k(var1, bj.T(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, ho.u, bj.u.i);

            for (h7.h var10 : this.W) {
               var10.render(var1, var2, var3);
            }

            if (this.M != null) {
               this.M.render(var1, var2, var3);
            }

            var1.enableScissor(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (jd var19 : this.J) {
                  if (var19.i) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.k == pc.v.d && this.J.stream().noneMatch(this::G)) {
                  bj.k(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, ho.Z, bj.u.X);
               }
            } finally {
               var1.disableScissor();
            }

            wp.p(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.P, this.i);
         }
      }
   }

   private void o(GuiGraphicsExtractor var1, int var2, int var3) {
      List var4 = m4.J();
      m4.k var5 = this.d.canvasContains(var2, var3) ? hw.u(var4, var0 -> true, m4.k::bounds, var2, var3) : null;
      if (var5 != null && var5.element() != this.Y.z()) {
         m(var1, var5.bounds(), -2137614688);
      }

      m4.p var6 = this.Y.z();
      m4.J(var4, var6).ifPresent(var3x -> {
         m(var1, var3x, uh.H(ho.J, 0.85));
         if (!this.S(var6) && !m4.J(var6)) {
            hw.l var4x = hw.E(var3x, this.width, this.height);
            wp.I(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            wp.I(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, ho.J);
         }

         if (this.Y.y().H()) {
            String var8 = var6.T(this.width) + ", " + var6.P(this.height);
            int var5x = bj.e(var8, bj.u.M) + 12;
            int var6x = hw.m(var3x.x(), var5x, this.width);
            int var7 = Math.max(2, var3x.y() - 20);
            wp.I(var1, var6x, var7, var5x, 16, 5, -552003303);
            bj.k(var1, var8, var6x + 6, var7 + 4, -1381654, bj.u.M);
         }
      });
      if (this.Y.y().H()) {
         s4.V(var1, this.width, this.height, this.Y.y().o(), this.Y.y().L());
      }
   }

   private static void m(GuiGraphicsExtractor var0, hw.l var1, int var2) {
      kv.I(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class h extends jd {
      final String V;
      final String E;
      final Runnable X;
      final BooleanSupplier R;
      final jp Y = new jp(0.0);
      boolean B;
      boolean q;
      boolean H;

      h(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.V = var2;
         this.E = var3;
         this.X = var4;
         this.R = var5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.i) {
            boolean var4 = this.R.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.T || !this.W) ? 0 : 1;
            if (!h7.this.g) {
               this.Y.F(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.Y.k(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.H ? ho.A : (this.p ? ho.u : ho.j);
            if (this.H || var4 || var6 > 0.001) {
               wp.I(var1, this.S, this.u, this.A, this.J, 7, this.H ? uh.q(ho.u, ho.Z, var6 * 0.12) : uh.q(ho.A, ho.o, var4 ? 1.0 : var6));
            }

            byte var9 = 13;
            int var10 = this.B ? 0 : bj.u(this.V);
            int var11 = var10 + (this.E.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.S + Math.max(5, (this.A - var11) / 2);
            if (!this.E.isEmpty()) {
               var1.pose().pushMatrix();

               try {
                  if (this.q) {
                     var1.pose().translate(var12 * 2 + var9, 0.0F);
                     var1.pose().scale(-1.0F, 1.0F);
                  }

                  ur.O(var1, ur.V(this.E), this.B ? this.S + (this.A - var9) / 2 : var12, this.u + (this.J - var9) / 2, var9, var8);
               } finally {
                  var1.pose().popMatrix();
               }
            }

            if (!this.B) {
               bj.E(var1, bj.T(this.V, this.A - (this.E.isEmpty() ? 12 : 27)), var12 + (this.E.isEmpty() ? 0 : var9 + 6), this.u + (this.J - 9) / 2, var8);
            }

            if (this.T && this.W) {
               wp.t(var1, this.S, this.u, this.A, this.J, 7);
            }
         }
      }

      void t(GuiGraphicsExtractor var1) {
         if (this.B) {
            int var2 = bj.e(this.V, bj.u.M) + 16;
            int var3 = hw.m(this.S + (this.A - var2) / 2, var2, h7.this.width);
            int var4 = Math.max(0, this.u - 27);
            wp.I(var1, var3, var4, var2, 20, 6, ho.dw);
            bj.k(var1, this.V, var3 + 8, var4 + 6, ho.u, bj.u.M);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            sr.v(sr.e.q);
            this.X.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.p && (var1 == 257 || var1 == 32)) {
            sr.v(sr.e.q);
            this.X.run();
            return true;
         } else {
            return false;
         }
      }
   }

   private final class n extends jd {
      final m4.p a;
      private final jp X = new jp(0.0);
      private final jp z = new jp(0.0);

      n(m4.p var2) {
         this.a = var2;
         this.J = 36;
         this.z.F(m4.B(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = m4.B(this.a);
         int var5 = h7.this.Y.z() == this.a ? 1 : 0;
         if (!h7.this.g) {
            this.X.F(this.contains(var2, var3) ? 1.0 : 0.0);
            this.z.F(var4 ? 1.0 : 0.0);
         }

         double var6 = this.X.k(!this.contains(var2, var3) && !this.T ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            wp.I(var1, this.S, this.u, this.A, this.J, 7, uh.q(ho.A, ho.o, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         wp.I(var1, this.S + 4, this.u + 6, 24, 24, 6, ho.B);
         ur.O(var1, ur.V(ur.t(this.a.r)), this.S + 10, this.u + 12, 12, var4 ? ho.u : ho.Z);
         bj.k(var1, bj.T(m4.W(this.a.r), this.A - 74), this.S + 36, this.u + 7, ho.u, bj.u.i);
         bj.k(var1, var4 ? "Visible" : "Hidden", this.S + 36, this.u + 21, ho.Z, bj.u.M);
         wp.E(var1, this.S + this.A - 30, this.u + 12, this.z.k(var4 ? 1.0 : 0.0, 180));
         if (this.T && this.W) {
            wp.t(var1, this.S, this.u, this.A, this.J, 7);
         }
      }

      private void v() {
         h7.this.Y.l(() -> {
            m4.Z(this.a, !m4.B(this.a));
            h7.this.Y.H();
         });
         sr.v(m4.B(this.a) ? sr.e.J : sr.e.v);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            h7.this.C(this.a);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.S + this.A - 38) {
            this.v();
         } else {
            h7.this.p(this.a);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.v();
            return true;
         } else if (var1 == 257) {
            h7.this.p(this.a);
            return true;
         } else {
            return false;
         }
      }
   }
}
