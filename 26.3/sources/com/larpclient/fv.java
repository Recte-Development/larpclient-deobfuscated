package com.larpclient;

import com.larpclient.module.kl;
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

public final class fv extends fs {
   private static final List<String> X = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen U;
   private p0 h;
   private p0.l a;
   private p0.d B = p0.d.b;
   private final List<fv.c> p = new ArrayList<>();
   private final List<fv.c> r = new ArrayList<>();
   private final List<j0> A = new ArrayList<>();
   private final Set<j0> n = Collections.newSetFromMap(new IdentityHashMap<>());
   private final fq s = new fq(1.0);
   private final Map<com.larpclient.module.r<?>, Object> w = new IdentityHashMap<>();
   private sk v;
   private fv.c x;
   private fv.c L;
   private fv.c D;
   private fv.c j;
   private com.larpclient.module.b u;
   private com.larpclient.module.b k;
   private com.larpclient.module.b C;
   private com.larpclient.module.o W;
   private com.larpclient.module.m T;
   private com.larpclient.module.m t;
   private boolean i = (boolean)1;
   private boolean V;
   private boolean l;
   private boolean Z;
   private boolean b = (boolean)1;
   private boolean P;
   private boolean O;
   private boolean S = (boolean)1;
   private double y;
   private double G;
   private int e;
   private int Q;
   private int I = -1;
   private int Y = -1;
   private static boolean R;
   private String M = "";
   private Object c;

   public fv(Screen var1) {
      super("HUD Editor");
      this.U = var1;
   }

   public boolean C() {
      return this.l;
   }

   private aq f() {
      return LarpClient.N().h();
   }

   private boolean N(dk.q var1) {
      return this.f().k().o(var1.d);
   }

   protected void init() {
      if (this.h == null) {
         kl var1 = LarpClient.N().j();
         List var2 = Stream.of(var1.U("HUD"), var1.U("TargetHUD"), var1.U("Staff List"), var1.U("CustomCrosshair")).filter(Objects::nonNull).toList();
         this.h = new p0(() -> this.f().Q(this.f().S()), this::N, var2);
         this.c = this.minecraft.level;
         this.P = R;
         _q.z("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.f().S());
      } else {
         this.h.d();
      }

      dk.l();
      this.A();
   }

   private fv.c e(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      fv.c var5 = new fv.c(var1, var2, var3, var4);
      this.J.add(var5);
      return var5;
   }

   private void Q(com.larpclient.module.r<?> var1, boolean var2) {
      su var3 = new su(var1, true);
      this.A.add(var3);
      this.J.add(var3);
      if (var2) {
         this.n.add(var3);
      }
   }

   private void r(String var1, Runnable var2, boolean var3, boolean var4) {
      s8 var5 = new s8(var1, var2);
      var5.N = (boolean)1;
      var5.f = var4;
      var5.O = 28;
      this.A.add(var5);
      this.J.add(var5);
      if (var3) {
         this.n.add(var5);
      }
   }

   private void g(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      com.larpclient.module.m var5 = new com.larpclient.module.m(var1, var2, var3);
      var5.w(var4);
      this.Q(var5, false);
   }

   private void A() {
      this.W();
      this.p.clear();
      this.r.clear();
      this.A.clear();
      this.n.clear();
      this.v = null;
      this.u = this.k = this.C = null;
      this.W = null;
      this.T = this.t = null;
      this.p.add(this.e("Widgets", "panels-top-left", () -> this.w(p0.d.t), () -> this.B == p0.d.t));
      this.x = this.e("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.k();
         this.h.e();
      }, () -> false);
      this.x.y = (boolean)1;
      this.p.add(this.x);
      this.L = this.e("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.k();
         this.h.u();
      }, () -> false);
      this.L.y = this.L.s = (boolean)1;
      this.p.add(this.L);
      fv.c var1 = this.e("Editor options", "settings", () -> this.w(p0.d.V), () -> this.B == p0.d.V);
      var1.y = (boolean)1;
      this.p.add(var1);
      this.D = this.e("Edit", "paintbrush", () -> {
         if (this.h.b() != null) {
            if (this.B != p0.d.Y && this.B != p0.d.y) {
               this.d(this.h.b());
            } else {
               this.m(p0.d.b);
            }
         }
      }, () -> this.B == p0.d.Y || this.B == p0.d.y);
      this.p.add(this.D);
      fv.c var2 = this.e("Done", "", this::onClose, () -> false);
      var2.o = (boolean)1;
      this.p.add(var2);
      this.j = this.e("Save failed. Retry", "rotate-ccw", () -> {
         this.h.L();
         this.s();
      }, () -> false);
      if (this.B != p0.d.b) {
         fv.c var3 = this.e("Close panel", "x", () -> this.m(p0.d.b), () -> false);
         var3.y = (boolean)1;
         this.r.add(var3);
         if (this.B == p0.d.t) {
            this.v = new sk("Search widgets", var1x -> {
               this.M = var1x;
               this.e = 0;
               this.q();
            }, 80);
            this.v.j = this.M;
            this.J.add(this.v);

            for (dk.q var5 : dk.X()) {
               fv.f var6 = new fv.f(var5);
               this.A.add(var6);
               this.J.add(var6);
            }
         } else if (this.B == p0.d.V) {
            this.g("Snap to guides", "Align with screen edges and center", bh.z.snapping(), var1x -> this.h.w(var1x, bh.z.grid()));
            this.g("Show grid", "Show an eight-pixel placement grid", bh.z.grid(), var1x -> this.h.w(bh.z.snapping(), var1x));
            com.larpclient.module.m var7 = new com.larpclient.module.m("Preview hidden widgets", "Preview without enabling widgets", this.V);
            var7.a(() -> !this.l);
            var7.w(var1x -> this.V = var1x);
            su var9 = new su(var7);
            this.A.add(var9);
            this.J.add(var9);
            this.g("Use live content", "Show current gameplay content instead of samples", this.l, var1x -> {
               this.h.d();
               this.l = var1x;
               dk.l();
            });
            this.r("Reset layout", () -> {
               this.k();
               this.h.T();
            }, false, true);
         } else if (this.h.b() != null) {
            this.r.add(this.e("Appearance", "", () -> this.m(p0.d.Y), () -> this.B == p0.d.Y));
            this.r.add(this.e("Position", "", () -> this.m(p0.d.y), () -> this.B == p0.d.y));
            dk.q var8 = this.h.b();
            if (this.B == p0.d.Y) {
               this.T = new com.larpclient.module.m("Show widget", "Keep this widget visible in gameplay", dk.k(var8));
               this.T.w(var2x -> {
                  if (!this.Z) {
                     dk.I(var8, var2x);
                     this.h.p();
                  }
               });
               this.Q(this.T, false);
               this.K(var8);

               for (com.larpclient.module.r var11 : dk.K(var8)) {
                  this.Q(var11, false);
               }
            } else {
               this.t = new com.larpclient.module.m("Lock position", "Prevent moving and resizing this widget", this.N(var8));
               this.t.w(var2x -> {
                  if (!this.Z) {
                     this.V(var8, var2x);
                  }
               });
               this.Q(this.t, false);
               this.u = new com.larpclient.module.b("X", "Horizontal position in GUI pixels", var8.f(this.width), 0.0, this.width, 1.0);
               this.k = new com.larpclient.module.b("Y", "Vertical position in GUI pixels", var8.p(this.height), 0.0, this.height, 1.0);
               this.u.w(var2x -> {
                  if (!this.Z && !this.N(var8)) {
                     var8.o(var2x.intValue(), var8.p(this.height), this.width, this.height);
                  }
               });
               this.k.w(var2x -> {
                  if (!this.Z && !this.N(var8)) {
                     var8.o(var8.f(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.Q(this.u, true);
               this.Q(this.k, true);
               this.K(var8);
               this.W = new com.larpclient.module.o("Align", "Align within the full game viewport", g(var8), X.toArray(String[]::new));
               this.W.w(var2x -> {
                  if (!this.Z && !this.N(var8) && !var2x.equals("Free")) {
                     int var3x = X.indexOf(var2x) - 1;
                     bh.Z.put(var8.d, new bh.u(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.Q(this.W, true);
               this.r("Reset placement", () -> {
                  this.k();
                  if (!this.N(var8)) {
                     this.h.T(var8::O);
                  }
               }, true, false);
            }
         }
      }

      this.q();
   }

   private void K(dk.q var1) {
      this.C = new com.larpclient.module.b("Size", "Uniform widget scale", var1.Z(), 0.2, 4.0, 0.01);
      this.C.w(var2 -> {
         if (!this.Z && !this.N(var1)) {
            var1.m(var2);
         }
      });
      this.Q(this.C, true);
   }

   private void w(p0.d var1) {
      this.m(this.B == var1 ? p0.d.b : var1);
   }

   private void m(p0.d var1) {
      this.k();
      this.h.d();
      this.O = (boolean)0;
      this.i = (boolean)1;
      if (this.B == p0.d.b || var1 == p0.d.t || var1 == p0.d.V) {
         this.I = this.Y = -1;
      }

      this.B = var1;
      this.e = 0;
      this.s.p(this.S ? 0.0 : 1.0);
      this.s.n(1.0, 160);
      this.A();
   }

   private void d(dk.q var1) {
      this.k();
      this.h.O(var1);
      this.f(var1);
      qy.i var2 = dk.O(dk.G(), var1).orElse(var1.W(this.width, this.height));
      this.I = var2.x() + var2.width() + 14;
      this.Y = var2.y();
      if (this.I + 260 > this.width - 12) {
         this.I = Math.max(12, var2.x() - 274);
      }

      this.B = p0.d.Y;
      this.i = (boolean)1;
      this.e = 0;
      this.s.p(this.S ? 0.0 : 1.0);
      this.s.n(1.0, 160);
      this.A();
   }

   private void u(dk.q var1) {
      this.k();
      this.h.O(var1);
      this.f(var1);
      this.B = p0.d.b;
      this.e = 0;
      this.A();
   }

   private void f(dk.q var1) {
      if (this.l && dk.O(dk.G(), var1).isEmpty()) {
         this.l = (boolean)0;
      }
   }

   private void V(dk.q var1, boolean var2) {
      this.h.d();
      this.f().k().p(var1.d, var2);
      this.s();
   }

   private void s() {
      this.P = R = (boolean)(!this.f().P() ? 1 : 0);
   }

   private boolean F(dk.q var1) {
      return this.M.isBlank() || (dk.m(var1.d) + " " + var1.d).toLowerCase(Locale.ROOT).contains(this.M.toLowerCase(Locale.ROOT));
   }

   private boolean l(j0 var1) {
      return !(var1 instanceof fv.f var2 && !this.F(var2.C));
   }

   private boolean Q(j0 var1) {
      return !(var1 instanceof su var2 && !var2.a());
   }

   private void q() {
      this.S = this.f().k().w;
      this.Q = this.A.stream().filter(this::l).filter(this::Q).mapToInt(var0 -> var0.O + 4).sum();
      this.a = p0.l.calculate(this.width, this.height, this.B, this.Q, this.I, this.Y, this.i, this.h.H() || this.P);

      for (int var1 = 0; var1 < this.p.size(); var1++) {
         fv.c var2 = this.p.get(var1);
         var2.R = (boolean)(this.a.dock() != null ? 1 : 0);
         if (var2.R) {
            G(var2, this.a.tools().get(var1));
         }
      }

      this.x.V = this.h.U();
      this.L.V = this.h.V();
      this.D.V = (boolean)(this.h.b() != null ? 1 : 0);
      this.j.R = (boolean)(this.a.retry() != null ? 1 : 0);
      if (this.j.R) {
         G(this.j, this.a.retry());
      }

      qy.i var9 = this.a.panel();
      qy.i var10 = this.a.body();

      for (fv.c var4 : this.r) {
         var4.R = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.r.isEmpty()) {
         this.r.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.r.getFirst().O = 26;
         if (this.r.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.r.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.r.get(var13).O = 26;
            }
         }
      }

      if (this.v != null) {
         this.v.R = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.v.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.v.O = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.e = Math.clamp(this.e, 0, Math.max(0, this.Q - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.e;

      for (j0 var6 : this.A) {
         int var7 = this.l(var6) && this.Q(var6) ? 1 : 0;
         var6.R = (boolean)(var10 != null && var7 != 0 && var14 + var6.O > var10.y() && var14 < var10.y() + var10.height() ? 1 : 0);
         var6.V = (boolean)(this.n.contains(var6) && this.h.b() != null && this.N(this.h.b()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof su var8) {
               var8.B(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.O + 4;
         }
      }

      if (this.q != null && (!this.q.R || !this.q.V)) {
         this.q.blur();
         this.q = this.g = null;
         this.S();
      }
   }

   private static void G(j0 var0, qy.i var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.O = var1.height();
   }

   @Override
   protected boolean accepts(j0 var1, double var2, double var4) {
      return this.A.contains(var1)
         ? var1.contains(var2, var4) && this.a.controlContains(new qy.i(var1.P, var1.T, var1.L, var1.O), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(j0 var1) {
      if (this.A.contains(var1) && this.a.body() != null) {
         qy.i var2 = this.a.body();
         if (var1.T < var2.y()) {
            this.e = this.e - (var2.y() - var1.T);
         }

         if (var1.T + var1.O > var2.y() + var2.height()) {
            this.e = this.e + (var1.T + var1.O - var2.y() - var2.height());
         }

         this.q();
      }
   }

   private static String g(dk.q var0) {
      bh.u var1 = bh.Z.get(var0.d);
      if (var1 == null) {
         return var0.d.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return var2 >= 0 && var2 <= 2 && var3 >= 0 && var3 <= 2 && Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? X.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void L() {
      dk.q var1 = this.h.b();
      if (var1 != null) {
         this.Z = (boolean)1;

         try {
            if (this.u != null) {
               this.u.O((double)var1.f(this.width));
            }

            if (this.k != null) {
               this.k.O((double)var1.p(this.height));
            }

            if (this.C != null) {
               this.C.O(var1.Z());
            }

            if (this.W != null) {
               this.W.setValue(g(var1));
            }

            if (this.T != null) {
               this.T.setValue(dk.k(var1));
            }

            if (this.t != null) {
               this.t.setValue(this.N(var1));
            }
         } finally {
            this.Z = (boolean)0;
         }
      }
   }

   private void J() {
      this.h.z();
      if (this.w.isEmpty() && this.h.b() != null) {
         for (com.larpclient.module.r var2 : dk.K(this.h.b())) {
            this.w.put(var2, var2.getValue());
         }
      }
   }

   private void S() {
      if (this.w.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.h.p();
      }

      this.w.clear();
      this.h.a();
   }

   private void x() {
      this.q = this.g = null;
      this.J.clear();
      this.w.clear();
      dk.l();
      this.B = p0.d.b;
      this.O = (boolean)0;
      this.e = 0;
   }

   private void k() {
      if (this.h.j()) {
         this.x();
      } else {
         if (this.q != null) {
            this.q.blur();
         }

         this.q = this.g = null;
         this.S();
      }
   }

   private boolean K() {
      return this.q instanceof sk || this.q instanceof su var1 && var1.T();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         if (this.h.j()) {
            this.x();
            this.A();
         }

         if (this.h.a().K()) {
            return true;
         }

         this.q();

         for (j0 var4 : this.J) {
            if (var4 instanceof su var5 && var5.n()) {
               if (var5.W(var1.x(), var1.y())) {
                  this.J();
                  var5.b(var1.x(), var1.y(), j2.L(var1));
                  this.g = var5;
                  return true;
               }

               var5.O();
            }
         }

         j0 var6 = this.J.stream().filter(var2x -> this.accepts(var2x, var1.x(), var1.y())).findFirst().orElse(null);
         if (var6 != this.q) {
            this.k();
         }

         if (var6 != null) {
            this.J();
            this.q = var6;
            var6.F = (boolean)1;
            var6.X = (boolean)0;
            if (var6.click(var1.x(), var1.y(), j2.L(var1))) {
               if (this.J.contains(var6)) {
                  this.g = var6;
               } else {
                  this.q = this.g = null;
               }
            }

            if (!(this.g instanceof su) && !this.K()) {
               this.S();
            }

            return true;
         } else if (this.a.panelContains(var1.x(), var1.y())) {
            if (j2.L(var1) == 0 && var1.y() < this.a.panel().y() + 39) {
               this.O = (boolean)1;
               this.y = var1.x() - this.a.panel().x();
               this.G = var1.y() - this.a.panel().y();
            }

            return true;
         } else {
            if (this.a.dock() != null && this.a.dock().contains(var1.x(), var1.y(), 0)) {
               return true;
            }

            if (this.B != p0.d.b) {
               this.m(p0.d.b);
            }

            if (this.a.canvasContains(var1.x(), var1.y())) {
               dk.i var7 = qy.l(dk.G(), var0 -> true, dk.i::bounds, var1.x(), var1.y());
               if (j2.L(var1) == 1) {
                  if (var7 == null) {
                     this.m(p0.d.t);
                  } else {
                     this.d(var7.element());
                  }
               } else if (j2.L(var1) == 0) {
                  this.J();
                  this.h.a().i(var1.x(), var1.y(), 0, this.width, this.height, dk.G(), false);
               }
            }

            if (!this.h.a().K()) {
               this.S();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.h.j()) {
         this.x();
         this.A();
         return true;
      } else if (!this.minecraft.isWindowActive()) {
         this.g();
         return true;
      } else if (this.O && this.a.panel() != null) {
         this.I = Math.max(0, (int)Math.round(var1.x() - this.y));
         this.Y = Math.max(0, (int)Math.round(var1.y() - this.G));
         this.q();
         return true;
      } else {
         return this.h.a().K()
            ? this.h.a().N(var1.x(), var1.y(), j2.L(var1), this.width, this.height, this.minecraft.hasAltDown(), this.minecraft.hasShiftDown())
            : super.mouseDragged(var1, var2, var4);
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.h.j()) {
         this.x();
         this.A();
         return true;
      }

      this.O = (boolean)0;
      this.h.a().L(j2.L(var1));
      super.mouseReleased(var1);
      if (!this.K()) {
         this.S();
      }

      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.h.j()) {
         this.x();
         this.A();
         return true;
      }

      for (j0 var10 : this.J) {
         if (var10 instanceof su var11 && var11.W(var1, var3)) {
            var11.L(var7);
            return true;
         }
      }

      if (this.a.panelContains(var1, var3)) {
         this.k();
         this.e = this.e - (int)Math.round(var7 * 28.0);
         this.q();
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.h.j()) {
         this.x();
         this.A();
         return true;
      } else {
         return super.charTyped(var1);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.h.j()) {
         this.x();
         this.A();
      }

      int var2 = j2.P(var1);
      int var3 = (var1.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (var1.modifiers() & 1) != 0 ? 1 : 0;
      if (this.K()) {
         if (var2 == 258) {
            this.k();
            return super.keyPressed(var1);
         }

         this.J();
         boolean var9 = super.keyPressed(var1);
         if (!this.K()) {
            this.S();
         }

         return var9;
      } else if (var2 == 256) {
         for (j0 var11 : this.A) {
            if (var11 instanceof su var7 && var7.n()) {
               var7.O();
               return true;
            }
         }

         if (this.B != p0.d.b) {
            this.m(p0.d.b);
         } else {
            this.onClose();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.k();
            this.h.d();
            this.B = p0.d.b;
            this.O = (boolean)0;
            this.i = (boolean)(!this.i ? 1 : 0);
            this.A();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.w(p0.d.t);
               return true;
            }

            dk.q var5 = this.h.b();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.d(var5);
               return true;
            }

            if (this.q == null && var5 != null && !this.N(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.h.v(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            }

            if (this.q == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.k();
               this.h.T(() -> var5.a(true));
               return true;
            }

            this.J();
            boolean var6 = super.keyPressed(var1);
            if (!this.K()) {
               this.S();
            }

            return var6;
         } else {
            this.k();
            if (var2 != 89 && var4 == 0) {
               this.h.e();
            } else {
               this.h.u();
            }

            return true;
         }
      }
   }

   private void g() {
      this.O = (boolean)0;
      this.k();
      this.h.d();
   }

   public void tick() {
      if (this.h.j()) {
         this.x();
         this.A();
      }

      if (this.minecraft.level == this.c && this.minecraft.player != null) {
         boolean var1 = this.minecraft.isWindowActive();
         if (!var1 && this.b) {
            this.g();
         }

         this.b = var1;
      } else {
         this.onClose();
      }
   }

   public void resize(int var1, int var2) {
      if (this.h != null) {
         this.g();
      }

      super.resize(var1, var2);
   }

   public void onClose() {
      if (this.h != null) {
         this.k();
         this.h.M();
      }

      if (this.P) {
         this.s();
      }

      if (this.h != null && this.h.H() || this.P) {
         ib.L().l("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", ib.v.o, 6000L);
      }

      this.minecraft.gui.setScreen(this.minecraft.level == this.c ? this.U : null);
   }

   public void removed() {
      if (this.h != null) {
         this.k();
         this.h.M();
      }

      _q.c("HUD editor", "Released canvas input");
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.h != null && this.minecraft.player != null && this.minecraft.level != null) {
         if (this.h.j()) {
            this.x();
            this.A();
         }

         this.q();
         if (bh.z.grid() && !this.l) {
            db.W(var1, this.width, this.height, this.height, 8, 5, 117440511, 318767103);
         }

         dk.a(var1, this.h.b(), this.V, this.l);
         this.P(var1, var2, var3);
         this.L();
         i3 var5 = this.f().k();

         try (com.larpclient.c.z var6 = com.larpclient.c.N(var5.z, var5.j, var5.b, var5.m())) {
            var1.nextStratum();
            if (this.a.dock() != null) {
               k(var1, this.a.dock());

               for (fv.c var8 : this.p) {
                  var8.render(var1, var2, var3);
               }

               if (this.j.R) {
                  this.j.render(var1, var2, var3);
               }

               if (this.a.panel() == null && !this.h.a().K() && !this.j.R) {
                  String var16 = this.h.b() == null ? "HUD editor" : dk.m(this.h.b().d);
                  String var19 = this.h.b() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = ic.f(var16, ic.n.Q);
                  int var10 = ic.f(var19, ic.n.I);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.a.dock().y() - 28;
                  wc.b(var1, var12, var13, var11, 21, 8, com.larpclient.c.k());
                  ic.N(var1, var16, var12 + 9, var13 + 6, com.larpclient.c.H, ic.n.Q);
                  if (var9 + var10 + 28 <= var11) {
                     ic.N(var1, var19, var12 + var9 + 19, var13 + 7, com.larpclient.c.W, ic.n.I);
                  }
               }
            }

            this.K(var1, var2, var3);

            for (j0 var20 : this.A) {
               if (var20 instanceof su var22 && var22.n() && var20.R) {
                  var22.l(var1, var2, var3);
               }
            }

            if (this.J.stream().noneMatch(var0 -> var0 instanceof su var1x && var1x.n())) {
               for (fv.c var21 : this.p) {
                  if (var21.R && (var21.contains(var2, var3) || var21.F && var21.X)) {
                     var21.U(var1);
                  }
               }
            }
         }
      }
   }

   private static void k(GuiGraphicsExtractor var0, qy.i var1) {
      wc.T(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      wc.t(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, com.larpclient.c.k(), 0.75);
   }

   private void K(GuiGraphicsExtractor var1, int var2, int var3) {
      qy.i var4 = this.a.panel();
      qy.i var5 = this.a.body();
      if (var4 != null) {
         if (!this.S) {
            this.s.p(1.0);
         }

         try (oa var6 = oa.j(this.s.K())) {
            k(var1, var4);
            String var7 = this.B == p0.d.t ? "Widgets" : (this.B == p0.d.V ? "Editor options" : dk.m(this.h.b().d));
            if (this.B == p0.d.Y || this.B == p0.d.y) {
               wz.D(var1, wz.M(wz.I(this.h.b().d)), var4.x() + 13, var4.y() + 15, 13, com.larpclient.c.W);
            }

            int var8 = var4.x() + (this.B != p0.d.Y && this.B != p0.d.y ? 14 : 34);
            ic.N(var1, ic.U(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, com.larpclient.c.H, ic.n.Q);

            for (fv.c var10 : this.r) {
               var10.render(var1, var2, var3);
            }

            if (this.v != null) {
               this.v.render(var1, var2, var3);
            }

            var1.enableScissor(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (j0 var19 : this.A) {
                  if (var19.R) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.B == p0.d.t && this.A.stream().noneMatch(this::l)) {
                  ic.N(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, com.larpclient.c.W, ic.n.C);
               }
            } finally {
               var1.disableScissor();
            }

            wc.V(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.Q, this.e);
         }
      }
   }

   private void P(GuiGraphicsExtractor var1, int var2, int var3) {
      List var4 = dk.G();
      dk.i var5 = this.a.canvasContains(var2, var3) ? qy.l(var4, var0 -> true, dk.i::bounds, var2, var3) : null;
      if (var5 != null && var5.element() != this.h.b()) {
         t(var1, var5.bounds(), -2137614688);
      }

      dk.q var6 = this.h.b();
      dk.O(var4, var6).ifPresent(var3x -> {
         t(var1, var3x, vv.N(com.larpclient.c.y, 0.85));
         if (!this.N(var6) && !dk.R(var6)) {
            qy.i var4x = qy.F(var3x, this.width, this.height);
            wc.b(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            wc.b(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, com.larpclient.c.y);
         }

         if (this.h.a().K()) {
            String var8 = var6.f(this.width) + ", " + var6.p(this.height);
            int var5x = ic.f(var8, ic.n.I) + 12;
            int var6x = qy.k(var3x.x(), var5x, this.width);
            int var7 = Math.max(2, var3x.y() - 20);
            wc.b(var1, var6x, var7, var5x, 16, 5, -552003303);
            ic.N(var1, var8, var6x + 6, var7 + 4, -1381654, ic.n.I);
         }
      });
      if (this.h.a().K()) {
         db.f(var1, this.width, this.height, this.h.a().C(), this.h.a().g());
      }
   }

   private static void t(GuiGraphicsExtractor var0, qy.i var1, int var2) {
      h5.L(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class c extends j0 {
      final String N;
      final String i;
      final Runnable t;
      final BooleanSupplier a;
      final fq B = new fq(0.0);
      boolean y;
      boolean s;
      boolean o;

      c(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.N = var2;
         this.i = var3;
         this.t = var4;
         this.a = var5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.R) {
            boolean var4 = this.a.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.F || !this.X) ? 0 : 1;
            if (!fv.this.S) {
               this.B.p(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.B.n(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.o ? com.larpclient.c.D : (this.V ? com.larpclient.c.H : com.larpclient.c.f);
            if (this.o || var4 || var6 > 0.001) {
               wc.b(
                  var1,
                  this.P,
                  this.T,
                  this.L,
                  this.O,
                  7,
                  this.o ? vv.Q(com.larpclient.c.H, com.larpclient.c.W, var6 * 0.12) : vv.Q(com.larpclient.c.D, com.larpclient.c.J, var4 ? 1.0 : var6)
               );
            }

            byte var9 = 13;
            int var10 = this.y ? 0 : ic.H(this.N);
            int var11 = var10 + (this.i.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.P + Math.max(5, (this.L - var11) / 2);
            if (!this.i.isEmpty()) {
               var1.pose().pushMatrix();

               try {
                  if (this.s) {
                     var1.pose().translate(var12 * 2 + var9, 0.0F);
                     var1.pose().scale(-1.0F, 1.0F);
                  }

                  wz.D(var1, wz.M(this.i), this.y ? this.P + (this.L - var9) / 2 : var12, this.T + (this.O - var9) / 2, var9, var8);
               } finally {
                  var1.pose().popMatrix();
               }
            }

            if (!this.y) {
               ic.N(var1, ic.U(this.N, this.L - (this.i.isEmpty() ? 12 : 27)), var12 + (this.i.isEmpty() ? 0 : var9 + 6), this.T + (this.O - 9) / 2, var8);
            }

            if (this.F && this.X) {
               wc.U(var1, this.P, this.T, this.L, this.O, 7);
            }
         }
      }

      void U(GuiGraphicsExtractor var1) {
         if (this.y) {
            int var2 = ic.f(this.N, ic.n.I) + 16;
            int var3 = qy.k(this.P + (this.L - var2) / 2, var2, fv.this.width);
            int var4 = Math.max(0, this.T - 27);
            wc.b(var1, var3, var4, var2, 20, 6, com.larpclient.c.r);
            ic.N(var1, this.N, var3 + 8, var4 + 6, com.larpclient.c.H, ic.n.I);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            ft.h(ft.e.K);
            this.t.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.V && (var1 == 257 || var1 == 32)) {
            ft.h(ft.e.K);
            this.t.run();
            return true;
         } else {
            return false;
         }
      }
   }

   private final class f extends j0 {
      final dk.q C;
      private final fq c = new fq(0.0);
      private final fq E = new fq(0.0);

      f(dk.q var2) {
         this.C = var2;
         this.O = 36;
         this.E.p(dk.k(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = dk.k(this.C);
         int var5 = fv.this.h.b() == this.C ? 1 : 0;
         if (!fv.this.S) {
            this.c.p(this.contains(var2, var3) ? 1.0 : 0.0);
            this.E.p(var4 ? 1.0 : 0.0);
         }

         double var6 = this.c.n(!this.contains(var2, var3) && !this.F ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            wc.b(var1, this.P, this.T, this.L, this.O, 7, vv.Q(com.larpclient.c.D, com.larpclient.c.J, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         wc.b(var1, this.P + 4, this.T + 6, 24, 24, 6, com.larpclient.c.R);
         wz.D(var1, wz.M(wz.I(this.C.d)), this.P + 10, this.T + 12, 12, var4 ? com.larpclient.c.H : com.larpclient.c.W);
         ic.N(var1, ic.U(dk.m(this.C.d), this.L - 74), this.P + 36, this.T + 7, com.larpclient.c.H, ic.n.Q);
         ic.N(var1, var4 ? "Visible" : "Hidden", this.P + 36, this.T + 21, com.larpclient.c.W, ic.n.I);
         wc.q(var1, this.P + this.L - 30, this.T + 12, this.E.n(var4 ? 1.0 : 0.0, 180));
         if (this.F && this.X) {
            wc.U(var1, this.P, this.T, this.L, this.O, 7);
         }
      }

      private void Z() {
         fv.this.h.T(() -> {
            dk.I(this.C, !dk.k(this.C));
            fv.this.h.p();
         });
         ft.h(dk.k(this.C) ? ft.e.y : ft.e.J);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            fv.this.d(this.C);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.P + this.L - 38) {
            this.Z();
         } else {
            fv.this.u(this.C);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.Z();
            return true;
         } else if (var1 == 257) {
            fv.this.u(this.C);
            return true;
         } else {
            return false;
         }
      }
   }
}
