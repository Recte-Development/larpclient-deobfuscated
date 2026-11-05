package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dv;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class h1 extends h8 {
   static final int Q = 8;
   static final int Z = 6;
   static final int w = 20;
   static final int y = 14;
   static final int U = 36;
   static final int V = 46;
   static final int C = 20;
   static final int A = 18;
   static final int o = 90;
   static final int m = 122;
   static final int N = 160;
   private static final int W = -401337324;
   private static final int i = -16119286;
   private final Screen n;
   private final List<h1.t> v = new ArrayList<>();
   private final ef T = new ef(0.0);
   private final Matrix3x2f z = new Matrix3x2f();
   private m4 a;
   private ur L;
   private h1.y D;
   private h1.y I;
   private h1.n E;
   private String s = "";
   private String h = "";
   private int J;
   private int k;
   private long j;
   private h1.t u;
   private int x;
   private int Y;
   private boolean P;
   private boolean R;
   private boolean H;

   public static Screen G(Screen var0) {
      return R().e ? new h1(var0) : new hw(var0);
   }

   public static boolean I(Screen var0) {
      return var0 instanceof hw || var0 instanceof h1;
   }

   private static e8 R() {
      return LarpClient.F().f().t().O;
   }

   public h1(Screen var1) {
      super("Larp client");
      this.n = var1;
   }

   static int F(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] q(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] Z(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.a = this.B();
      this.z.set(this.a.transform());
      this.P = (boolean)0;
      if (!this.R) {
         this.R = (boolean)1;
         this.T.Q(0.0);
         kb.d(kb.n.K);
      }

      this.T.m(1.0, this.D());
      if (this.v.isEmpty()) {
         this.J();
      }

      this.r();
      String var1 = this.L == null ? null : this.L.d;
      this.L = new ur("Search modules...", var1x -> {
         this.s = var1x;

         for (h1.t var3 : this.v) {
            var3.K();
            var3.l = 0;
         }
      }, 64);
      this.L.u = "search";
      this.L.d = var1 == null ? this.s : var1;
      this.D = new h1.y(() -> "Settings", this::P, true);
      this.I = new h1.y(() -> "Reset layout", this::X, true);
      this.E = new h1.n(this::c);
      this.g();
      mt.U("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.v.size(), this.a.width(), this.a.height());
   }

   private m4 B() {
      return m4.from(this.minecraft.getWindow(), LarpClient.F().f().t().s8);
   }

   private int D() {
      return LarpClient.F().f().t().S;
   }

   private void J() {
      this.v.clear();

      for (om var4 : om.values()) {
         if (var4 != om.L) {
            this.v.add(new h1.t(var4));
         }
      }

      this.v.add(new h1.t(om.L));
   }

   private void r() {
      ArrayList var1 = new ArrayList<>(this.v);
      var1.sort(Comparator.comparingInt(var0 -> var0.A));
      int var2 = this.B().width();
      int var3 = F(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         h1.t var5 = (h1.t)var1.get(var4);
         e8.g var6 = R().W(var5.r.name());
         int[] var7 = var6 == null ? q(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.Z = var7[0];
         var5.W = var7[1];
         var5.L = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.K();
      }
   }

   private void X() {
      R().R();
      this.r();
      this.H = (boolean)1;
      this.f();
      kb.d(kb.n.w);
      mt.K("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void c() {
      if (R().e) {
         R().e = (boolean)0;
         this.j = System.nanoTime();
         this.H = (boolean)1;
         this.f();
         kb.d(kb.n.O);
         mt.K("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void P() {
      this.u();
      this.minecraft.gui.setScreen(hw.P(this, "Interface"));
   }

   private void S(Module var1) {
      this.u();
      cx.K = om.d(var1).name();
      cx.P = new cx.p("Modules", var1.getName(), "", 0, 0);
      this.minecraft.gui.setScreen(new hw(this));
   }

   private void b(h1.t var1) {
      try {
         R().j(var1.r.name(), new e8.g(var1.Z, var1.W, var1.L));
      } catch (IllegalArgumentException var3) {
         mt.T("GUI", "Legacy panel {} place not saved: {}", var1.r.name(), var3.getMessage());
      }

      this.H = (boolean)1;
   }

   private void f() {
      if (this.H) {
         LarpClient.F().f().P();
         this.H = (boolean)0;
      }
   }

   public void onClose() {
      if (!this.P) {
         this.P = (boolean)1;
         this.u();
         kb.d(kb.n.G);
         this.minecraft.gui.setScreen(this.n);
      }
   }

   public void removed() {
      this.u();
      this.f();
      LarpClient.F().f().f();
      mt.U("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.j != 0L && System.nanoTime() - this.j < 100000000L ? 1 : 0;
      if (!this.P && var1 == 0 && !R().e) {
         this.P = (boolean)1;
         mt.K("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.minecraft.gui.setScreen(new hw(this.n));
      } else {
         for (h1.t var3 : this.v) {
            for (h1.j var5 : var3.h) {
               var5.p();
            }
         }
      }
   }

   private void u() {
      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.f = null;
   }

   private void g() {
      m4 var1 = this.B();
      if (!var1.equals(this.a) && this.u == null && this.f == null) {
         this.a = var1;
      }

      int var2 = F(this.a.width(), this.v.size());
      this.L.bounds(this.a.width() / 2 - 95, 8, 190);
      this.L.W = 20;
      this.D.bounds(this.L.q + this.L.A + 6, 8, 58);
      this.D.W = 20;
      this.I.bounds(this.D.q + this.D.A + 4, 8, 72);
      this.I.W = 20;
      this.h = cc.u(LarpClient.F().R());
      this.k = k6.z(this.h, k6.v.L) + k6.z(" = menu", k6.v.L) + 22;
      int var3 = this.E.h();
      this.J = (this.a.width() - this.k - 6 - var3) / 2;
      this.E.bounds(this.J + this.k + 6, this.j(), var3);
      this.E.W = 18;
      this.G.clear();
      this.G.add(this.L);
      this.G.add(this.D);
      this.G.add(this.I);
      this.G.add(this.E);

      for (h1.t var5 : this.v) {
         int[] var6 = Z(var5.Z, var5.W, var5.K, this.a.width(), this.a.height());
         if (var5 != this.u) {
            var5.Z = var6[0];
            var5.W = var6[1];
         }

         var5.F(var2, this.a.height());
         if (var5.G()) {
            for (h1.j var8 : var5.h) {
               if (var8.B) {
                  for (b var10 : var8.b) {
                     if (var10.D) {
                        this.G.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f R(double var1, double var3) {
      return new Matrix3x2f(this.z).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.level == null) {
         super.extractBackground(var1, var2, var3, var4);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = kx.G();
      this.g();
      xv var7 = LarpClient.F().f().t();
      double var8 = this.T.m(this.P ? 0.0 : 1.0, var7.S);
      this.z.set(this.a.transform()).mul(p2.q(var7.sb, var8, this.a.width() / 2.0, this.a.height() / 2.0));
      if (var7.sb.equals("Slide")) {
         this.z.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.R(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.z);

      try (de var11 = de.l(var8)) {
         int var12 = R().A;
         this.L.render(var1, var2, var3);
         this.D.render(var1, var2, var3);
         this.I.render(var1, var2, var3);
         h1.t var13 = this.m(var2, var3);
         this.E.render(var1, var13 == null ? var2 : -1, var3);

         for (h1.t var15 : this.v) {
            if (var15.G()) {
               var15.r(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.q instanceof un var25 && var25.g()) {
            var25.M(var1, var2, var3);
         }

         String var26 = var13 != null ? var13.G(var2, var3) : (this.E.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.R(var1, var26, var12);
      } finally {
         var1.pose().popMatrix();
      }

      kx.u("Legacy ClickGUI", var5);
   }

   private int j() {
      return this.a.height() - 46 + 20;
   }

   private void R(GuiGraphicsExtractor var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = k6.j(var4, this.a.width() - 40);
      int var5 = k6.P(var4);
      int var6 = this.j();
      k6.Z(var1, var4, (this.a.width() - var5) / 2, var6 - 13, com.larpclient.f.s);
      k6.v var7 = k6.v.L;
      int var8 = this.J;
      int var9 = this.k;
      c7.n(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      c7.n(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      k6.M(var1, this.h, var8 + 11, var6 + 5, com.larpclient.n.readableAccent(var3, -16448248), var7);
      k6.M(var1, " = menu", var8 + 11 + k6.z(this.h, var7), var6 + 5, -1, var7);
   }

   private h1.t m(double var1, double var3) {
      for (int var5 = this.v.size() - 1; var5 >= 0; var5--) {
         if (this.v.get(var5).G() && this.v.get(var5).S(var1, var3)) {
            return this.v.get(var5);
         }
      }

      return null;
   }

   private void I(h1.t var1) {
      this.v.remove(var1);
      this.v.add(var1);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.P) {
         return true;
      }

      Vector2f var3 = this.R(var1.x(), var1.y());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = mo.Z(var1);
      this.g();
      if (this.q != null && this.q.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.F().R() == cc.M(var8)) {
         this.onClose();
         return true;
      }

      if (this.q instanceof un var9 && var9.e(var4, var6)) {
         if (var9.C(var4, var6, var8)) {
            this.f = var9;
         }

         return true;
      } else {
         h1.t var12 = this.m(var4, var6);
         if (var12 != null) {
            this.I(var12);
            return var12.K(var4, var6, var8);
         }

         for (b var11 : List.of(this.L, this.D, this.I, this.E)) {
            if (var11.contains(var4, var6)) {
               this.V(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.u();
         return false;
      }
   }

   private void V(b var1) {
      if (this.q != null && this.q != var1) {
         this.q.blur();
      }

      this.q = var1;
      var1.l = (boolean)1;
      var1.R = (boolean)0;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.P) {
         return true;
      } else {
         Vector2f var6 = this.R(var1.x(), var1.y());
         if (this.u != null) {
            int[] var7 = Z(Math.round(var6.x) - this.x, Math.round(var6.y) - this.Y, this.u.K, this.a.width(), this.a.height());
            this.u.Z = var7[0];
            this.u.W = var7[1];
            return true;
         } else if (this.f != null) {
            this.f.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.u != null) {
         this.b(this.u);
         this.f();
         mt.U("GUI", "Legacy panel {} moved to {}, {}", this.u.r.D, this.u.Z, this.u.W);
         this.u = null;
      }

      if (this.f != null) {
         this.f.release();
      }

      this.f = null;
      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.P) {
         return true;
      } else {
         Vector2f var9 = this.R(var1, var3);
         if (this.q instanceof un var10 && var10.e(var9.x, var9.y)) {
            var10.z(var7);
            return true;
         } else {
            h1.t var12 = this.m(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.l = var12.l - (int)Math.round(var7 * 20.0);
            this.g();
            return true;
         }
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.P) {
         return true;
      } else {
         return hw.q(mo.O(var1), this.q, null, this::onClose) ? true : super.keyPressed(var1);
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.P) {
         return true;
      } else if (super.charTyped(var1)) {
         return true;
      } else if ((this.q == null || this.q == this.L) && Character.isLetterOrDigit(var1.codepoint())) {
         this.V(this.L);
         return this.L.character(var1.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(b var1) {
      for (h1.t var3 : this.v) {
         for (h1.j var5 : var3.h) {
            if (var5.B && var5.b.contains(var1)) {
               if (var1.J < var3.q()) {
                  var3.l = var3.l - (var3.q() - var1.J);
               } else if (var1.J + var1.W > var3.J) {
                  var3.l = var3.l + (var1.J + var1.W - var3.J);
               }

               this.g();
               return;
            }
         }
      }
   }

   private static final class i extends b {
      private final String w;

      i(String var1) {
         this.w = var1;
         this.W = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.D) {
            k6.T(var1, k6.o(this.w.toUpperCase(Locale.ROOT), this.A - 4, k6.d.q), this.q + 2, this.J + 2, com.larpclient.f.G, k6.d.q);
         }
      }
   }

   private final class j {
      final Module o;
      final List<b> b = new ArrayList<>();
      final Map<b, BooleanSupplier> P = new IdentityHashMap<>();
      final Set<String> U = new HashSet<>();
      boolean B;
      int h;
      String J;

      j(Module var2) {
         this.o = var2;
      }

      boolean O(b var1) {
         BooleanSupplier var2 = this.P.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int B() {
         int var1 = 5;

         for (b var3 : this.b) {
            if (var3.D) {
               var1 += var3.W + 2;
            }
         }

         return var1;
      }

      void B(int var1) {
         switch (var1) {
            case 0:
               this.o.toggle();
               kb.d(this.o.isEnabled() ? kb.n.l : kb.n.O);
               break;
            case 1:
               this.B = (boolean)(!this.B ? 1 : 0);
               if (this.B) {
                  this.V();
               } else if (this.b.contains(h1.this.q)) {
                  h1.this.u();
               }

               kb.d(this.B ? kb.n.T : kb.n.z);
               break;
            case 2:
               xv var2 = LarpClient.F().f().t();
               var2.W(this.o.getName(), !var2.J(this.o.getName()));
               LarpClient.F().f().P();

               for (h1.t var4 : h1.this.v) {
                  var4.K();
               }

               kb.d(kb.n.w);
         }
      }

      void p() {
         if (this.B && this.o instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.J, var1.q().P())) {
            this.V();
         }
      }

      void V() {
         if (this.b.contains(h1.this.q)) {
            h1.this.u();
         }

         this.b.clear();
         this.P.clear();
         this.J = this.o instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.q().P() : null;
         List var8 = ok.t(this.o);
         Iterator var9 = var8.iterator();

         while (true) {
            ok.m var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.o instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     dv var10 = new dv(
                        this.o instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.o.getKeyBind()
                     );
                     var10.h(this.o::setKeyBind);
                     this.C(new un(var10), null);
                     if (this.o.keybindMode() != null) {
                        this.C(new un(this.o.keybindMode()), null);
                     }
                  }

                  this.C(new h1.y(() -> "Reset settings", () -> {
                     for (df var2 : this.o.getSettings()) {
                        var2.B();
                     }

                     if (this.o instanceof FakeSpawnerModule var3x) {
                        var3x.V();
                     }

                     this.V();
                     kb.d(kb.n.w);
                  }, false), null);
                  this.C(new h1.y(() -> "Open full editor", () -> h1.this.S(this.o), false), null);
                  return;
               }

               var3 = (ok.m)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(df::B);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               h1.y var6 = new h1.y(() -> (this.U.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.U.remove(var3.title())) {
                     this.U.add(var3.title());
                  }

                  this.V();
               }, false);
               this.C(var6, var4);
            } while (!this.U.contains(var3.title()));

            if (var8.size() > 1) {
               this.C(new h1.i(var3.title()), var4);
            }

            for (df var12 : var3.settings()) {
               if (!(var12 instanceof com.larpclient.module.d9 var7 && var7.q())) {
                  un var13 = new un(var12);
                  this.C(var13, var13::x);
               }
            }
         }
      }

      private void C(b var1, BooleanSupplier var2) {
         this.b.add(var1);
         if (var2 != null) {
            this.P.put(var1, var2);
         }
      }
   }

   private static final class n extends b {
      private static final String c = "Legacy";
      private static final int v = 18;
      private static final int F = 10;
      private final Runnable m;
      private final ef Q = new ef(1.0);

      n(Runnable var1) {
         this.m = var1;
         this.W = 18;
      }

      int h() {
         return 11 + k6.z("Legacy", k6.v.L) + 6 + 18 + 8;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.Q.m(h1.R().e ? 1.0 : 0.0, 100);
         c7.n(var1, this.q - 1, this.J - 1, this.A + 2, this.W + 2, this.W / 2 + 1, var4 ? -12961216 : -14803422);
         c7.n(var1, this.q, this.J, this.A, this.W, this.W / 2, -268106488);
         if (this.l && this.R) {
            c7.t(var1, this.q, this.J, this.A, this.W, com.larpclient.f.J);
         }

         k6.M(var1, "Legacy", this.q + 11, this.J + 5, var4 ? -1 : com.larpclient.f.s, k6.v.L);
         int var7 = this.q + this.A - 8 - 18;
         int var8 = this.J + (this.W - 10) / 2;
         int var9 = h1.R().A | 0xFF000000;
         c7.n(var1, var7, var8, 18.0F, 10.0F, 5.0F, _3.D(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         c7.K(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.m.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.m.run();
         return true;
      }
   }

   private final class t {
      final om r;
      final Map<Module, h1.j> y = new IdentityHashMap<>();
      final List<h1.j> h = new ArrayList<>();
      final ef F = new ef(-1.0);
      final ef U = new ef(0.0);
      final ef X = new ef(0.0);
      final int A;
      int Z;
      int W;
      int K = 90;
      int l;
      int J;
      int p;
      boolean L = (boolean)1;

      t(om var2) {
         this.r = var2;
         this.A = h1.this.v.size();
      }

      void K() {
         this.h.clear();
         List var1 = LarpClient.F().x().C();
         xv var2 = LarpClient.F().f().t();

         for (Module var4 : this.r.k(var1, "", var2)) {
            if (h1.this.s.isBlank() || py.N(var4, h1.this.s) != Integer.MAX_VALUE) {
               this.h.add(this.y.computeIfAbsent(var4, var1x -> h1.this.new j(var1x)));
            }
         }
      }

      boolean G() {
         return !this.h.isEmpty();
      }

      int q() {
         return this.W + 20;
      }

      boolean S(double var1, double var3) {
         return c7.T(var1, var3, this.Z, this.W, this.K, 20 + (this.L ? this.J - this.q() : 0));
      }

      void F(int var1, int var2) {
         boolean var3 = this.h.stream().anyMatch(var0 -> var0.B);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.F.G() < 0.0) {
            this.F.Q(var4);
         }

         this.K = (int)Math.round(this.F.m(var4, 200));
         this.p = 0;

         for (h1.j var6 : this.h) {
            for (b var8 : var6.b) {
               var8.D = (boolean)(this.L && var6.B && var6.O(var8) ? 1 : 0);
            }

            this.p = this.p + 14 + (var6.B ? var6.B() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.q());
         this.J = this.q() + (this.L ? Math.min(this.p, var12) : 0);
         this.l = Math.clamp(this.l, 0, Math.max(0, this.p - (this.J - this.q())));
         int var13 = this.q() - this.l;

         for (h1.j var16 : this.h) {
            var16.h = var13;
            var13 += 14;
            if (var16.B) {
               var13 += 2;

               for (b var10 : var16.b) {
                  if (var10.D) {
                     var10.bounds(this.Z + 4, var13, this.K - 8);
                     if (var10 instanceof un var11) {
                        var11.N(6, h1.this.a.height() - 40);
                     }

                     var13 += var10.W + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void r(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
         int var5 = 20 + this.J - this.q();
         c7.s(var1, this.Z - 1, this.W - 1, this.K + 2, var5 + 2, -16119286);
         c7.s(var1, this.Z, this.W, this.K, var5, -401337324);
         String var6 = k6.q(this.r.D, (int)((this.K - 10) / 1.2F), k6.v.B);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.Z + this.K / 2.0F, this.W + 5);
            var1.pose().scale(1.2F, 1.2F);
            k6.M(var1, var6, -k6.z(var6, k6.v.B) / 2, 0, -1, k6.v.B);
         } finally {
            var1.pose().popMatrix();
         }

         if (this.L) {
            int var7 = var3 >= this.q() && var3 < this.J ? 1 : 0;
            h1.j var8 = null;
            if (var7 != 0) {
               for (h1.j var10 : this.h) {
                  if (c7.T(var2, var3, this.Z, var10.h, this.K, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.q() - this.l;
            if (var8 != null) {
               if (this.X.G() < 0.05) {
                  this.U.Q(var8.h - var29);
               }

               this.U.m(var8.h - var29, 160);
            }

            double var30 = this.X.m(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.Z, this.q(), this.Z + this.K, this.J);

            try {
               xv var12 = LarpClient.F().f().t();

               for (h1.j var14 : this.h) {
                  int var15 = var14.h + 14 + (var14.B ? var14.B() : 0);
                  if (var15 >= this.q() && var14.h <= this.J && var14.o.isEnabled()) {
                     c7.s(var1, this.Z, var14.h, this.K, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  c7.s(var1, this.Z, var29 + (int)Math.round(this.U.G()), this.K, 14, _3.U(419430399, var30));
               }

               for (h1.j var34 : this.h) {
                  int var36 = var34.h + 14 + (var34.B ? var34.B() : 0);
                  if (var36 >= this.q() && var34.h <= this.J) {
                     boolean var16 = var34.o.isEnabled();
                     int var17 = var16 ? com.larpclient.n.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = k6.j(var34.o.getDisplayName(), this.K - 14);
                     k6.Z(var1, var18, this.Z + (this.K - k6.P(var18)) / 2, var34.h + 3, var17);
                     if (var12.J(var34.o.getName()) && this.r != om.L) {
                        c7.s(var1, this.Z + 3, var34.h + 5, 3, 3, var16 ? var17 : _3.A(var4, 0.9));
                     }

                     if (var34.B) {
                        int var19 = var34.B();
                        c7.s(var1, this.Z, var34.h + 14, this.K, var19, 1342177280);
                        c7.s(var1, this.Z, var34.h + 14, 1, var19, var4 | 0xFF000000);

                        for (b var21 : var34.b) {
                           if (var21.D) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var31 = this.J - this.q();
            if (this.p > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.p);
               int var35 = var31 - var33;
               int var37 = this.q() + (int)Math.round(var35 * ((double)this.l / (this.p - var31)));
               c7.s(var1, this.Z + this.K - 2, var37, 2, var33, _3.A(var4, 0.8));
            }
         }
      }

      String G(int var1, int var2) {
         if (var2 < this.q()) {
            return "Drag to move · Right click folds " + this.r.D;
         }

         for (h1.j var4 : this.h) {
            if (c7.T(var1, var2, this.Z, var4.h, this.K, 14)) {
               String var8 = var4.o.getDescription();
               return var8.isBlank() ? var4.o.getDisplayName() : var8;
            }

            if (var4.B) {
               for (b var7 : var4.b) {
                  if (var7.D && var7.contains(var1, var2)) {
                     return var7 instanceof un var5 ? var5.P() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean K(double var1, double var3, int var5) {
         if (var3 < this.q()) {
            if (var5 == 0) {
               h1.this.u();
               h1.this.u = this;
               h1.this.x = (int)Math.round(var1) - this.Z;
               h1.this.Y = (int)Math.round(var3) - this.W;
            } else if (var5 == 1) {
               this.L = (boolean)(!this.L ? 1 : 0);
               h1.this.b(this);
               h1.this.f();
               kb.d(this.L ? kb.n.T : kb.n.z);
            }

            return true;
         } else if (this.L && !(var3 >= this.J)) {
            for (h1.j var7 : this.h) {
               if (c7.T(var1, var3, this.Z, var7.h, this.K, 14)) {
                  var7.B(var5);
                  return true;
               }

               if (var7.B) {
                  for (b var9 : var7.b) {
                     if (var9.D && var9.contains(var1, var3)) {
                        h1.this.V(var9);
                        if (var9.click(var1, var3, var5)) {
                           h1.this.f = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            h1.this.u();
            return true;
         } else {
            return true;
         }
      }
   }

   private static final class y extends b {
      private final Supplier<String> t;
      private final Runnable L;
      private final boolean K;

      y(Supplier<String> var1, Runnable var2, boolean var3) {
         this.t = var1;
         this.L = var2;
         this.K = var3;
         this.W = 14;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.D) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.l && this.R ? 1 : 0;
            if (this.K) {
               c7.s(var1, this.q - 1, this.J - 1, this.A + 2, this.W + 2, -16119286);
               c7.s(var1, this.q, this.J, this.A, this.W, var4 ? -266330076 : -401337324);
            } else if (var4) {
               c7.s(var1, this.q, this.J, this.A, this.W, 419430399);
            }

            if (var5 != 0) {
               c7.t(var1, this.q, this.J, this.A, this.W, com.larpclient.f.J);
            }

            String var6 = k6.j(this.t.get(), this.A - 8);
            k6.Z(var1, var6, this.q + (this.A - k6.P(var6)) / 2, this.J + (this.W - 8) / 2, !var4 && !this.K ? com.larpclient.f.s : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            kb.d(kb.n.w);
            this.L.run();
            return false;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.L.run();
         return true;
      }
   }
}
