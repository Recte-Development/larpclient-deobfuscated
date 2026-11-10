package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
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

public final class fj extends fs {
   static final int j = 8;
   static final int w = 6;
   static final int V = 20;
   static final int T = 14;
   static final int C = 36;
   static final int s = 46;
   static final int K = 20;
   static final int U = 18;
   static final int o = 90;
   static final int t = 122;
   static final int B = 160;
   private static final int p = -401337324;
   private static final int l = -16119286;
   private final Screen f;
   private final List<fj.f> Z = new ArrayList<>();
   private final fq L = new fq(0.0);
   private final Matrix3x2f d = new Matrix3x2f();
   private az N;
   private sk D;
   private fj.e Q;
   private fj.e P;
   private fj.o v;
   private String I = "";
   private String G = "";
   private int W;
   private int S;
   private long z;
   private fj.f r;
   private int M;
   private int h;
   private boolean m;
   private boolean u;
   private boolean c;

   public static Screen I(Screen var0) {
      return l().U ? new fj(var0) : new f8(var0);
   }

   public static boolean X(Screen var0) {
      return var0 instanceof f8 || var0 instanceof fj;
   }

   private static vd l() {
      return LarpClient.N().h().k().u;
   }

   public fj(Screen var1) {
      super("Larp client");
      this.f = var1;
   }

   static int A(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] x(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] e(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.N = this.h();
      this.d.set(this.N.transform());
      this.m = (boolean)0;
      if (!this.u) {
         this.u = (boolean)1;
         this.L.p(0.0);
         ft.h(ft.e.D);
      }

      this.L.n(1.0, this.k());
      if (this.Z.isEmpty()) {
         this.R();
      }

      this.x();
      String var1 = this.D == null ? null : this.D.j;
      this.D = new sk("Search modules...", var1x -> {
         this.I = var1x;

         for (fj.f var3 : this.Z) {
            var3.H();
            var3.T = 0;
         }
      }, 64);
      this.D.o = "search";
      this.D.j = var1 == null ? this.I : var1;
      this.Q = new fj.e(() -> "Settings", this::E, true);
      this.P = new fj.e(() -> "Reset layout", this::t, true);
      this.v = new fj.o(this::q);
      this.a();
      _q.c("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.Z.size(), this.N.width(), this.N.height());
   }

   private az h() {
      return az.from(this.minecraft.getWindow(), LarpClient.N().h().k().U);
   }

   private int k() {
      return LarpClient.N().h().k().n;
   }

   private void R() {
      this.Z.clear();

      for (q_ var4 : q_.values()) {
         if (var4 != q_.N) {
            this.Z.add(new fj.f(var4));
         }
      }

      this.Z.add(new fj.f(q_.N));
   }

   private void x() {
      ArrayList var1 = new ArrayList<>(this.Z);
      var1.sort(Comparator.comparingInt(var0 -> var0.z));
      int var2 = this.h().width();
      int var3 = A(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         fj.f var5 = (fj.f)var1.get(var4);
         vd.g var6 = l().N(var5.V.name());
         int[] var7 = var6 == null ? x(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.D = var7[0];
         var5.c = var7[1];
         var5.t = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.H();
      }
   }

   private void t() {
      l().l();
      this.x();
      this.c = (boolean)1;
      this.f();
      ft.h(ft.e.K);
      _q.z("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void q() {
      if (l().U) {
         l().U = (boolean)0;
         this.z = System.nanoTime();
         this.c = (boolean)1;
         this.f();
         ft.h(ft.e.J);
         _q.z("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void E() {
      this.h();
      this.minecraft.gui.setScreen(f8.i(this, "Interface"));
   }

   private void x(Module var1) {
      this.h();
      bh.C = q_.U(var1).name();
      bh.c = new bh.v("Modules", var1.getName(), "", 0, 0);
      this.minecraft.gui.setScreen(new f8(this));
   }

   private void s(fj.f var1) {
      try {
         l().H(var1.V.name(), new vd.g(var1.D, var1.c, var1.t));
      } catch (IllegalArgumentException var3) {
         _q.R("GUI", "Legacy panel {} place not saved: {}", var1.V.name(), var3.getMessage());
      }

      this.c = (boolean)1;
   }

   private void f() {
      if (this.c) {
         LarpClient.N().h().c();
         this.c = (boolean)0;
      }
   }

   public void onClose() {
      if (!this.m) {
         this.m = (boolean)1;
         this.h();
         ft.h(ft.e.n);
         this.minecraft.gui.setScreen(this.f);
      }
   }

   public void removed() {
      this.h();
      this.f();
      LarpClient.N().h().z();
      _q.c("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.z != 0L && System.nanoTime() - this.z < 100000000L ? 1 : 0;
      if (!this.m && var1 == 0 && !l().U) {
         this.m = (boolean)1;
         _q.z("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.minecraft.gui.setScreen(new f8(this.f));
      } else {
         for (fj.f var3 : this.Z) {
            for (fj.u var5 : var3.s) {
               var5.r();
            }
         }
      }
   }

   private void h() {
      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.g = null;
   }

   private void a() {
      az var1 = this.h();
      if (!var1.equals(this.N) && this.r == null && this.g == null) {
         this.N = var1;
      }

      int var2 = A(this.N.width(), this.Z.size());
      this.D.bounds(this.N.width() / 2 - 95, 8, 190);
      this.D.O = 20;
      this.Q.bounds(this.D.P + this.D.L + 6, 8, 58);
      this.Q.O = 20;
      this.P.bounds(this.Q.P + this.Q.L + 4, 8, 72);
      this.P.O = 20;
      this.G = p4.n(LarpClient.N().t());
      this.S = ic.U(this.G, ic.b.I) + ic.U(" = menu", ic.b.I) + 22;
      int var3 = this.v.I();
      this.W = (this.N.width() - this.S - 6 - var3) / 2;
      this.v.bounds(this.W + this.S + 6, this.V(), var3);
      this.v.O = 18;
      this.J.clear();
      this.J.add(this.D);
      this.J.add(this.Q);
      this.J.add(this.P);
      this.J.add(this.v);

      for (fj.f var5 : this.Z) {
         int[] var6 = e(var5.D, var5.c, var5.h, this.N.width(), this.N.height());
         if (var5 != this.r) {
            var5.D = var6[0];
            var5.c = var6[1];
         }

         var5.E(var2, this.N.height());
         if (var5.s()) {
            for (fj.u var8 : var5.s) {
               if (var8.Y) {
                  for (j0 var10 : var8.V) {
                     if (var10.R) {
                        this.J.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f w(double var1, double var3) {
      return new Matrix3x2f(this.d).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.level == null) {
         super.extractBackground(var1, var2, var3, var4);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = he.s();
      this.a();
      i3 var7 = LarpClient.N().h().k();
      double var8 = this.L.n(this.m ? 0.0 : 1.0, var7.n);
      this.d.set(this.N.transform()).mul(wv.d(var7.p, var8, this.N.width() / 2.0, this.N.height() / 2.0));
      if (var7.p.equals("Slide")) {
         this.d.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.w(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.d);

      try (oa var11 = oa.j(var8)) {
         int var12 = l().h;
         this.D.render(var1, var2, var3);
         this.Q.render(var1, var2, var3);
         this.P.render(var1, var2, var3);
         fj.f var13 = this.m(var2, var3);
         this.v.render(var1, var13 == null ? var2 : -1, var3);

         for (fj.f var15 : this.Z) {
            if (var15.s()) {
               var15.O(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.q instanceof su var25 && var25.n()) {
            var25.l(var1, var2, var3);
         }

         String var26 = var13 != null ? var13.R(var2, var3) : (this.v.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.s(var1, var26, var12);
      } finally {
         var1.pose().popMatrix();
      }

      he.T("Legacy ClickGUI", var5);
   }

   private int V() {
      return this.N.height() - 46 + 20;
   }

   private void s(GuiGraphicsExtractor var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = ic.U(var4, this.N.width() - 40);
      int var5 = ic.H(var4);
      int var6 = this.V();
      ic.N(var1, var4, (this.N.width() - var5) / 2, var6 - 13, com.larpclient.c.W);
      ic.b var7 = ic.b.I;
      int var8 = this.W;
      int var9 = this.S;
      h5.q(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      h5.q(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      ic.W(var1, this.G, var8 + 11, var6 + 5, w4.readableAccent(var3, -16448248), var7);
      ic.W(var1, " = menu", var8 + 11 + ic.U(this.G, var7), var6 + 5, -1, var7);
   }

   private fj.f m(double var1, double var3) {
      for (int var5 = this.Z.size() - 1; var5 >= 0; var5--) {
         if (this.Z.get(var5).s() && this.Z.get(var5).d(var1, var3)) {
            return this.Z.get(var5);
         }
      }

      return null;
   }

   private void R(fj.f var1) {
      this.Z.remove(var1);
      this.Z.add(var1);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.m) {
         return true;
      }

      Vector2f var3 = this.w(var1.x(), var1.y());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = j2.L(var1);
      this.a();
      if (this.q != null && this.q.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.N().t() == p4.f(var8)) {
         this.onClose();
         return true;
      }

      if (this.q instanceof su var9 && var9.W(var4, var6)) {
         if (var9.b(var4, var6, var8)) {
            this.g = var9;
         }

         return true;
      } else {
         fj.f var12 = this.m(var4, var6);
         if (var12 != null) {
            this.R(var12);
            return var12.P(var4, var6, var8);
         }

         for (j0 var11 : List.of(this.D, this.Q, this.P, this.v)) {
            if (var11.contains(var4, var6)) {
               this.Z(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.h();
         return false;
      }
   }

   private void Z(j0 var1) {
      if (this.q != null && this.q != var1) {
         this.q.blur();
      }

      this.q = var1;
      var1.F = (boolean)1;
      var1.X = (boolean)0;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.m) {
         return true;
      } else {
         Vector2f var6 = this.w(var1.x(), var1.y());
         if (this.r != null) {
            int[] var7 = e(Math.round(var6.x) - this.M, Math.round(var6.y) - this.h, this.r.h, this.N.width(), this.N.height());
            this.r.D = var7[0];
            this.r.c = var7[1];
            return true;
         } else if (this.g != null) {
            this.g.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.r != null) {
         this.s(this.r);
         this.f();
         _q.c("GUI", "Legacy panel {} moved to {}, {}", this.r.V.f, this.r.D, this.r.c);
         this.r = null;
      }

      if (this.g != null) {
         this.g.release();
      }

      this.g = null;
      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.m) {
         return true;
      } else {
         Vector2f var9 = this.w(var1, var3);
         if (this.q instanceof su var10 && var10.W(var9.x, var9.y)) {
            var10.L(var7);
            return true;
         } else {
            fj.f var12 = this.m(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.T = var12.T - (int)Math.round(var7 * 20.0);
            this.a();
            return true;
         }
      }
   }

   public boolean d() {
      return f8.q(this.q);
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.m) {
         return true;
      } else {
         return f8.z(j2.P(var1), this.q, null, this::onClose) ? true : super.keyPressed(var1);
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.m) {
         return true;
      } else if (super.charTyped(var1)) {
         return true;
      } else if ((this.q == null || this.q == this.D) && Character.isLetterOrDigit(var1.codepoint())) {
         this.Z(this.D);
         return this.D.character(var1.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(j0 var1) {
      for (fj.f var3 : this.Z) {
         for (fj.u var5 : var3.s) {
            if (var5.Y && var5.V.contains(var1)) {
               if (var1.T < var3.h()) {
                  var3.T = var3.T - (var3.h() - var1.T);
               } else if (var1.T + var1.O > var3.Y) {
                  var3.T = var3.T + (var1.T + var1.O - var3.Y);
               }

               this.a();
               return;
            }
         }
      }
   }

   private static final class e extends j0 {
      private final Supplier<String> C;
      private final Runnable u;
      private final boolean Q;

      e(Supplier<String> var1, Runnable var2, boolean var3) {
         this.C = var1;
         this.u = var2;
         this.Q = var3;
         this.O = 14;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.R) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.F && this.X ? 1 : 0;
            if (this.Q) {
               h5.N(var1, this.P - 1, this.T - 1, this.L + 2, this.O + 2, -16119286);
               h5.N(var1, this.P, this.T, this.L, this.O, var4 ? -266330076 : -401337324);
            } else if (var4) {
               h5.N(var1, this.P, this.T, this.L, this.O, 419430399);
            }

            if (var5 != 0) {
               h5.L(var1, this.P, this.T, this.L, this.O, com.larpclient.c.H);
            }

            String var6 = ic.U(this.C.get(), this.L - 8);
            ic.N(var1, var6, this.P + (this.L - ic.H(var6)) / 2, this.T + (this.O - 8) / 2, !var4 && !this.Q ? com.larpclient.c.W : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            ft.h(ft.e.K);
            this.u.run();
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

         this.u.run();
         return true;
      }
   }

   private final class f {
      final q_ V;
      final Map<Module, fj.u> L = new IdentityHashMap<>();
      final List<fj.u> s = new ArrayList<>();
      final fq i = new fq(-1.0);
      final fq U = new fq(0.0);
      final fq u = new fq(0.0);
      final int z;
      int D;
      int c;
      int h = 90;
      int T;
      int Y;
      int M;
      boolean t = (boolean)1;

      f(q_ var2) {
         this.V = var2;
         this.z = fj.this.Z.size();
      }

      void H() {
         this.s.clear();
         List var1 = LarpClient.N().j().a();
         i3 var2 = LarpClient.N().h().k();

         for (Module var4 : this.V.l(var1, "", var2)) {
            if (fj.this.I.isBlank() || com.larpclient.q.E(var4, fj.this.I) != Integer.MAX_VALUE) {
               this.s.add(this.L.computeIfAbsent(var4, var1x -> fj.this.new u(var1x)));
            }
         }
      }

      boolean s() {
         return !this.s.isEmpty();
      }

      int h() {
         return this.c + 20;
      }

      boolean d(double var1, double var3) {
         return h5.H(var1, var3, this.D, this.c, this.h, 20 + (this.t ? this.Y - this.h() : 0));
      }

      void E(int var1, int var2) {
         boolean var3 = this.s.stream().anyMatch(var0 -> var0.Y);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.i.K() < 0.0) {
            this.i.p(var4);
         }

         this.h = (int)Math.round(this.i.n(var4, 200));
         this.M = 0;

         for (fj.u var6 : this.s) {
            for (j0 var8 : var6.V) {
               var8.R = (boolean)(this.t && var6.Y && var6.L(var8) ? 1 : 0);
            }

            this.M = this.M + 14 + (var6.Y ? var6.p() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.h());
         this.Y = this.h() + (this.t ? Math.min(this.M, var12) : 0);
         this.T = Math.clamp(this.T, 0, Math.max(0, this.M - (this.Y - this.h())));
         int var13 = this.h() - this.T;

         for (fj.u var16 : this.s) {
            var16.z = var13;
            var13 += 14;
            if (var16.Y) {
               var13 += 2;

               for (j0 var10 : var16.V) {
                  if (var10.R) {
                     var10.bounds(this.D + 4, var13, this.h - 8);
                     if (var10 instanceof su var11) {
                        var11.B(6, fj.this.N.height() - 40);
                     }

                     var13 += var10.O + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void O(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
         int var5 = 20 + this.Y - this.h();
         h5.N(var1, this.D - 1, this.c - 1, this.h + 2, var5 + 2, -16119286);
         h5.N(var1, this.D, this.c, this.h, var5, -401337324);
         String var6 = ic.N(this.V.f, (int)((this.h - 10) / 1.2F), ic.b.R);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.D + this.h / 2.0F, this.c + 5);
            var1.pose().scale(1.2F, 1.2F);
            ic.W(var1, var6, -ic.U(var6, ic.b.R) / 2, 0, -1, ic.b.R);
         } finally {
            var1.pose().popMatrix();
         }

         if (this.t) {
            int var7 = var3 >= this.h() && var3 < this.Y ? 1 : 0;
            fj.u var8 = null;
            if (var7 != 0) {
               for (fj.u var10 : this.s) {
                  if (h5.H(var2, var3, this.D, var10.z, this.h, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.h() - this.T;
            if (var8 != null) {
               if (this.u.K() < 0.05) {
                  this.U.p(var8.z - var29);
               }

               this.U.n(var8.z - var29, 160);
            }

            double var30 = this.u.n(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.D, this.h(), this.D + this.h, this.Y);

            try {
               i3 var12 = LarpClient.N().h().k();

               for (fj.u var14 : this.s) {
                  int var15 = var14.z + 14 + (var14.Y ? var14.p() : 0);
                  if (var15 >= this.h() && var14.z <= this.Y && var14.S.isEnabled()) {
                     h5.N(var1, this.D, var14.z, this.h, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  h5.N(var1, this.D, var29 + (int)Math.round(this.U.K()), this.h, 14, vv.O(419430399, var30));
               }

               for (fj.u var34 : this.s) {
                  int var36 = var34.z + 14 + (var34.Y ? var34.p() : 0);
                  if (var36 >= this.h() && var34.z <= this.Y) {
                     boolean var16 = var34.S.isEnabled();
                     int var17 = var16 ? w4.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = ic.U(var34.S.getDisplayName(), this.h - 14);
                     ic.N(var1, var18, this.D + (this.h - ic.H(var18)) / 2, var34.z + 3, var17);
                     if (var12.m(var34.S.getName()) && this.V != q_.N) {
                        h5.N(var1, this.D + 3, var34.z + 5, 3, 3, var16 ? var17 : vv.N(var4, 0.9));
                     }

                     if (var34.Y) {
                        int var19 = var34.p();
                        h5.N(var1, this.D, var34.z + 14, this.h, var19, 1342177280);
                        h5.N(var1, this.D, var34.z + 14, 1, var19, var4 | 0xFF000000);

                        for (j0 var21 : var34.V) {
                           if (var21.R) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var31 = this.Y - this.h();
            if (this.M > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.M);
               int var35 = var31 - var33;
               int var37 = this.h() + (int)Math.round(var35 * ((double)this.T / (this.M - var31)));
               h5.N(var1, this.D + this.h - 2, var37, 2, var33, vv.N(var4, 0.8));
            }
         }
      }

      String R(int var1, int var2) {
         if (var2 < this.h()) {
            return "Drag to move · Right click folds " + this.V.f;
         }

         for (fj.u var4 : this.s) {
            if (h5.H(var1, var2, this.D, var4.z, this.h, 14)) {
               String var8 = var4.S.getDescription();
               return var8.isBlank() ? var4.S.getDisplayName() : var8;
            }

            if (var4.Y) {
               for (j0 var7 : var4.V) {
                  if (var7.R && var7.contains(var1, var2)) {
                     return var7 instanceof su var5 ? var5.p() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean P(double var1, double var3, int var5) {
         if (var3 < this.h()) {
            if (var5 == 0) {
               fj.this.h();
               fj.this.r = this;
               fj.this.M = (int)Math.round(var1) - this.D;
               fj.this.h = (int)Math.round(var3) - this.c;
            } else if (var5 == 1) {
               this.t = (boolean)(!this.t ? 1 : 0);
               fj.this.s(this);
               fj.this.f();
               ft.h(this.t ? ft.e.Q : ft.e.X);
            }

            return true;
         } else if (this.t && !(var3 >= this.Y)) {
            for (fj.u var7 : this.s) {
               if (h5.H(var1, var3, this.D, var7.z, this.h, 14)) {
                  var7.G(var5);
                  return true;
               }

               if (var7.Y) {
                  for (j0 var9 : var7.V) {
                     if (var9.R && var9.contains(var1, var3)) {
                        fj.this.Z(var9);
                        if (var9.click(var1, var3, var5)) {
                           fj.this.g = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            fj.this.h();
            return true;
         } else {
            return true;
         }
      }
   }

   private static final class o extends j0 {
      private static final String A = "Legacy";
      private static final int q = 18;
      private static final int J = 10;
      private final Runnable Q;
      private final fq e = new fq(1.0);

      o(Runnable var1) {
         this.Q = var1;
         this.O = 18;
      }

      int I() {
         return 11 + ic.U("Legacy", ic.b.I) + 6 + 18 + 8;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.e.n(fj.l().U ? 1.0 : 0.0, 100);
         h5.q(var1, this.P - 1, this.T - 1, this.L + 2, this.O + 2, this.O / 2 + 1, var4 ? -12961216 : -14803422);
         h5.q(var1, this.P, this.T, this.L, this.O, this.O / 2, -268106488);
         if (this.F && this.X) {
            h5.L(var1, this.P, this.T, this.L, this.O, com.larpclient.c.H);
         }

         ic.W(var1, "Legacy", this.P + 11, this.T + 5, var4 ? -1 : com.larpclient.c.W, ic.b.I);
         int var7 = this.P + this.L - 8 - 18;
         int var8 = this.T + (this.O - 10) / 2;
         int var9 = fj.l().h | 0xFF000000;
         h5.q(var1, var7, var8, 18.0F, 10.0F, 5.0F, vv.Q(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         h5.M(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.Q.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.Q.run();
         return true;
      }
   }

   private static final class t extends j0 {
      private final String A;

      t(String var1) {
         this.A = var1;
         this.O = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.R) {
            ic.N(var1, ic.o(this.A.toUpperCase(Locale.ROOT), this.L - 4, ic.n.L), this.P + 2, this.T + 2, com.larpclient.c.f, ic.n.L);
         }
      }
   }

   private final class u {
      final Module S;
      final List<j0> V = new ArrayList<>();
      final Map<j0, BooleanSupplier> B = new IdentityHashMap<>();
      final Set<String> D = new HashSet<>();
      boolean Y;
      int z;
      String m;

      u(Module var2) {
         this.S = var2;
      }

      boolean L(j0 var1) {
         BooleanSupplier var2 = this.B.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int p() {
         int var1 = 5;

         for (j0 var3 : this.V) {
            if (var3.R) {
               var1 += var3.O + 2;
            }
         }

         return var1;
      }

      void G(int var1) {
         switch (var1) {
            case 0:
               this.S.toggle();
               ft.h(this.S.isEnabled() ? ft.e.y : ft.e.J);
               break;
            case 1:
               this.Y = (boolean)(!this.Y ? 1 : 0);
               if (this.Y) {
                  this.s();
               } else if (this.V.contains(fj.this.q)) {
                  fj.this.h();
               }

               ft.h(this.Y ? ft.e.Q : ft.e.X);
               break;
            case 2:
               i3 var2 = LarpClient.N().h().k();
               var2.h(this.S.getName(), !var2.m(this.S.getName()));
               LarpClient.N().h().c();

               for (fj.f var4 : fj.this.Z) {
                  var4.H();
               }

               ft.h(ft.e.K);
         }
      }

      void r() {
         if (this.Y && this.S instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.m, var1.l().getValue())) {
            this.s();
         }
      }

      void s() {
         if (this.V.contains(fj.this.q)) {
            fj.this.h();
         }

         this.V.clear();
         this.B.clear();
         this.m = this.S instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.l().getValue() : null;
         List var8 = ag.G(this.S);
         Iterator var9 = var8.iterator();

         while (true) {
            ag.g var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.S instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     com.larpclient.module.x var10 = new com.larpclient.module.x(
                        this.S instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.S.getKeyBind()
                     );
                     var10.w(this.S::setKeyBind);
                     this.A(new su(var10), null);
                     if (this.S.keybindMode() != null) {
                        this.A(new su(this.S.keybindMode()), null);
                     }
                  }

                  this.A(new fj.e(() -> "Reset settings", () -> {
                     for (com.larpclient.module.r var2 : this.S.getSettings()) {
                        var2.u();
                     }

                     if (this.S instanceof FakeSpawnerModule var3x) {
                        var3x.A();
                     }

                     this.s();
                     ft.h(ft.e.K);
                  }, false), null);
                  this.A(new fj.e(() -> "Open full editor", () -> fj.this.x(this.S), false), null);
                  return;
               }

               var3 = (ag.g)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(com.larpclient.module.r::j);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               fj.e var6 = new fj.e(() -> (this.D.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.D.remove(var3.title())) {
                     this.D.add(var3.title());
                  }

                  this.s();
               }, false);
               this.A(var6, var4);
            } while (!this.D.contains(var3.title()));

            if (var8.size() > 1) {
               this.A(new fj.t(var3.title()), var4);
            }

            for (com.larpclient.module.r var12 : var3.settings()) {
               if (!(var12 instanceof com.larpclient.module.b var7 && var7.Y())) {
                  su var13 = new su(var12);
                  this.A(var13, var13::a);
               }
            }
         }
      }

      private void A(j0 var1, BooleanSupplier var2) {
         this.V.add(var1);
         if (var2 != null) {
            this.B.put(var1, var2);
         }
      }
   }
}
