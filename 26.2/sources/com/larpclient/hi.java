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

public final class hi extends h3 {
   static final int K = 8;
   static final int f = 6;
   static final int b = 20;
   static final int E = 14;
   static final int j = 36;
   static final int G = 46;
   static final int Y = 20;
   static final int A = 18;
   static final int i = 90;
   static final int S = 122;
   static final int J = 160;
   private static final int V = -401337324;
   private static final int r = -16119286;
   private final Screen U;
   private final List<hi.f> m = new ArrayList<>();
   private final jp R = new jp(0.0);
   private final Matrix3x2f s = new Matrix3x2f();
   private tq w;
   private j9 e;
   private hi.z q;
   private hi.z t;
   private hi.g X;
   private String z = "";
   private String L = "";
   private int u;
   private int d;
   private long N;
   private hi.f D;
   private int T;
   private int k;
   private boolean a;
   private boolean c;
   private boolean g;

   public static Screen w(Screen var0) {
      return V().G ? new hi(var0) : new hf(var0);
   }

   public static boolean X(Screen var0) {
      return var0 instanceof hf || var0 instanceof hi;
   }

   private static sv V() {
      return LarpClient.R().o().g().P;
   }

   public hi(Screen var1) {
      super("Larp client");
      this.U = var1;
   }

   static int s(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] W(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] L(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.w = this.N();
      this.s.set(this.w.transform());
      this.a = (boolean)0;
      if (!this.c) {
         this.c = (boolean)1;
         this.R.F(0.0);
         sr.v(sr.e.X);
      }

      this.R.k(1.0, this.r());
      if (this.m.isEmpty()) {
         this.c();
      }

      this.a();
      String var1 = this.e == null ? null : this.e.H;
      this.e = new j9("Search modules...", var1x -> {
         this.z = var1x;

         for (hi.f var3 : this.m) {
            var3.y();
            var3.X = 0;
         }
      }, 64);
      this.e.X = "search";
      this.e.H = var1 == null ? this.z : var1;
      this.q = new hi.z(() -> "Settings", this::p, true);
      this.t = new hi.z(() -> "Reset layout", this::n, true);
      this.X = new hi.g(this::y);
      this.x();
      h.U("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.m.size(), this.w.width(), this.w.height());
   }

   private tq N() {
      return tq.from(this.minecraft.getWindow(), LarpClient.R().o().g().q);
   }

   private int r() {
      return LarpClient.R().o().g().c;
   }

   private void c() {
      this.m.clear();

      for (pz var4 : pz.values()) {
         if (var4 != pz.s) {
            this.m.add(new hi.f(var4));
         }
      }

      this.m.add(new hi.f(pz.s));
   }

   private void a() {
      ArrayList var1 = new ArrayList<>(this.m);
      var1.sort(Comparator.comparingInt(var0 -> var0.S));
      int var2 = this.N().width();
      int var3 = s(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         hi.f var5 = (hi.f)var1.get(var4);
         sv.i var6 = V().s(var5.N.name());
         int[] var7 = var6 == null ? W(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.Y = var7[0];
         var5.R = var7[1];
         var5.W = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.y();
      }
   }

   private void n() {
      V().z();
      this.a();
      this.g = (boolean)1;
      this.t();
      sr.v(sr.e.q);
      h.A("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void y() {
      if (V().G) {
         V().G = (boolean)0;
         this.N = System.nanoTime();
         this.g = (boolean)1;
         this.t();
         sr.v(sr.e.v);
         h.A("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void p() {
      this.E();
      this.minecraft.gui.setScreen(hf.Z(this, "Interface"));
   }

   private void h(Module var1) {
      this.E();
      c7.c = pz.B(var1).name();
      c7.D = new c7.x("Modules", var1.getName(), "", 0, 0);
      this.minecraft.gui.setScreen(new hf(this));
   }

   private void x(hi.f var1) {
      try {
         V().p(var1.N.name(), new sv.i(var1.Y, var1.R, var1.W));
      } catch (IllegalArgumentException var3) {
         h.g("GUI", "Legacy panel {} place not saved: {}", var1.N.name(), var3.getMessage());
      }

      this.g = (boolean)1;
   }

   private void t() {
      if (this.g) {
         LarpClient.R().o().W();
         this.g = (boolean)0;
      }
   }

   public void onClose() {
      if (!this.a) {
         this.a = (boolean)1;
         this.E();
         sr.v(sr.e.s);
         this.minecraft.gui.setScreen(this.U);
      }
   }

   public void removed() {
      this.E();
      this.t();
      LarpClient.R().o().C();
      h.U("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.N != 0L && System.nanoTime() - this.N < 100000000L ? 1 : 0;
      if (!this.a && var1 == 0 && !V().G) {
         this.a = (boolean)1;
         h.A("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.minecraft.gui.setScreen(new hf(this.U));
      } else {
         for (hi.f var3 : this.m) {
            for (hi.v var5 : var3.j) {
               var5.M();
            }
         }
      }
   }

   private void E() {
      if (this.o != null) {
         this.o.blur();
      }

      this.o = this.F = null;
   }

   private void x() {
      tq var1 = this.N();
      if (!var1.equals(this.w) && this.D == null && this.F == null) {
         this.w = var1;
      }

      int var2 = s(this.w.width(), this.m.size());
      this.e.bounds(this.w.width() / 2 - 95, 8, 190);
      this.e.J = 20;
      this.q.bounds(this.e.S + this.e.A + 6, 8, 58);
      this.q.J = 20;
      this.t.bounds(this.q.S + this.q.A + 4, 8, 72);
      this.t.J = 20;
      this.L = bc.G(LarpClient.R().m());
      this.d = bj.x(this.L, bj.j.w) + bj.x(" = menu", bj.j.w) + 22;
      int var3 = this.X.u();
      this.u = (this.w.width() - this.d - 6 - var3) / 2;
      this.X.bounds(this.u + this.d + 6, this.n(), var3);
      this.X.J = 18;
      this.p.clear();
      this.p.add(this.e);
      this.p.add(this.q);
      this.p.add(this.t);
      this.p.add(this.X);

      for (hi.f var5 : this.m) {
         int[] var6 = L(var5.Y, var5.R, var5.c, this.w.width(), this.w.height());
         if (var5 != this.D) {
            var5.Y = var6[0];
            var5.R = var6[1];
         }

         var5.h(var2, this.w.height());
         if (var5.w()) {
            for (hi.v var8 : var5.j) {
               if (var8.R) {
                  for (jd var10 : var8.p) {
                     if (var10.i) {
                        this.p.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f F(double var1, double var3) {
      return new Matrix3x2f(this.s).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.level == null) {
         super.extractBackground(var1, var2, var3, var4);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = zj.m();
      this.x();
      hb var7 = LarpClient.R().o().g();
      double var8 = this.R.k(this.a ? 0.0 : 1.0, var7.c);
      this.s.set(this.w.transform()).mul(by.K(var7.E, var8, this.w.width() / 2.0, this.w.height() / 2.0));
      if (var7.E.equals("Slide")) {
         this.s.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.F(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.s);

      try (ua var11 = ua.K(var8)) {
         int var12 = V().Z;
         this.e.render(var1, var2, var3);
         this.q.render(var1, var2, var3);
         this.t.render(var1, var2, var3);
         hi.f var13 = this.x(var2, var3);
         this.X.render(var1, var13 == null ? var2 : -1, var3);

         for (hi.f var15 : this.m) {
            if (var15.w()) {
               var15.m(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.o instanceof ju var25 && var25.m()) {
            var25.J(var1, var2, var3);
         }

         String var26 = var13 != null ? var13.o(var2, var3) : (this.X.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.m(var1, var26, var12);
      } finally {
         var1.pose().popMatrix();
      }

      zj.l("Legacy ClickGUI", var5);
   }

   private int n() {
      return this.w.height() - 46 + 20;
   }

   private void m(GuiGraphicsExtractor var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = bj.T(var4, this.w.width() - 40);
      int var5 = bj.u(var4);
      int var6 = this.n();
      bj.E(var1, var4, (this.w.width() - var5) / 2, var6 - 13, ho.Z);
      bj.j var7 = bj.j.w;
      int var8 = this.u;
      int var9 = this.d;
      kv.f(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      kv.f(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      bj.J(var1, this.L, var8 + 11, var6 + 5, lv.readableAccent(var3, -16448248), var7);
      bj.J(var1, " = menu", var8 + 11 + bj.x(this.L, var7), var6 + 5, -1, var7);
   }

   private hi.f x(double var1, double var3) {
      for (int var5 = this.m.size() - 1; var5 >= 0; var5--) {
         if (this.m.get(var5).w() && this.m.get(var5).m(var1, var3)) {
            return this.m.get(var5);
         }
      }

      return null;
   }

   private void P(hi.f var1) {
      this.m.remove(var1);
      this.m.add(var1);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.a) {
         return true;
      }

      Vector2f var3 = this.F(var1.x(), var1.y());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = k2.w(var1);
      this.x();
      if (this.o != null && this.o.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.R().m() == bc.Z(var8)) {
         this.onClose();
         return true;
      }

      if (this.o instanceof ju var9 && var9.y(var4, var6)) {
         if (var9.X(var4, var6, var8)) {
            this.F = var9;
         }

         return true;
      } else {
         hi.f var12 = this.x(var4, var6);
         if (var12 != null) {
            this.P(var12);
            return var12.a(var4, var6, var8);
         }

         for (jd var11 : List.of(this.e, this.q, this.t, this.X)) {
            if (var11.contains(var4, var6)) {
               this.i(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.E();
         return false;
      }
   }

   private void i(jd var1) {
      if (this.o != null && this.o != var1) {
         this.o.blur();
      }

      this.o = var1;
      var1.T = (boolean)1;
      var1.W = (boolean)0;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.a) {
         return true;
      } else {
         Vector2f var6 = this.F(var1.x(), var1.y());
         if (this.D != null) {
            int[] var7 = L(Math.round(var6.x) - this.T, Math.round(var6.y) - this.k, this.D.c, this.w.width(), this.w.height());
            this.D.Y = var7[0];
            this.D.R = var7[1];
            return true;
         } else if (this.F != null) {
            this.F.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.D != null) {
         this.x(this.D);
         this.t();
         h.U("GUI", "Legacy panel {} moved to {}, {}", this.D.N.u, this.D.Y, this.D.R);
         this.D = null;
      }

      if (this.F != null) {
         this.F.release();
      }

      this.F = null;
      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.a) {
         return true;
      } else {
         Vector2f var9 = this.F(var1, var3);
         if (this.o instanceof ju var10 && var10.y(var9.x, var9.y)) {
            var10.N(var7);
            return true;
         } else {
            hi.f var12 = this.x(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.X = var12.X - (int)Math.round(var7 * 20.0);
            this.x();
            return true;
         }
      }
   }

   public boolean q() {
      return hf.Z(this.o);
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.a) {
         return true;
      } else {
         return hf.m(k2.a(var1), this.o, null, this::onClose) ? true : super.keyPressed(var1);
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.a) {
         return true;
      } else if (super.charTyped(var1)) {
         return true;
      } else if ((this.o == null || this.o == this.e) && Character.isLetterOrDigit(var1.codepoint())) {
         this.i(this.e);
         return this.e.character(var1.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(jd var1) {
      for (hi.f var3 : this.m) {
         for (hi.v var5 : var3.j) {
            if (var5.R && var5.p.contains(var1)) {
               if (var1.u < var3.e()) {
                  var3.X = var3.X - (var3.e() - var1.u);
               } else if (var1.u + var1.J > var3.A) {
                  var3.X = var3.X + (var1.u + var1.J - var3.A);
               }

               this.x();
               return;
            }
         }
      }
   }

   private final class f {
      final pz N;
      final Map<Module, hi.v> i = new IdentityHashMap<>();
      final List<hi.v> j = new ArrayList<>();
      final jp r = new jp(-1.0);
      final jp F = new jp(0.0);
      final jp e = new jp(0.0);
      final int S;
      int Y;
      int R;
      int c = 90;
      int X;
      int A;
      int O;
      boolean W = (boolean)1;

      f(pz var2) {
         this.N = var2;
         this.S = hi.this.m.size();
      }

      void y() {
         this.j.clear();
         List var1 = LarpClient.R().z().a();
         hb var2 = LarpClient.R().o().g();

         for (Module var4 : this.N.P(var1, "", var2)) {
            if (hi.this.z.isBlank() || sy.G(var4, hi.this.z) != Integer.MAX_VALUE) {
               this.j.add(this.i.computeIfAbsent(var4, var1x -> hi.this.new v(var1x)));
            }
         }
      }

      boolean w() {
         return !this.j.isEmpty();
      }

      int e() {
         return this.R + 20;
      }

      boolean m(double var1, double var3) {
         return kv.f(var1, var3, this.Y, this.R, this.c, 20 + (this.W ? this.A - this.e() : 0));
      }

      void h(int var1, int var2) {
         boolean var3 = this.j.stream().anyMatch(var0 -> var0.R);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.r.B() < 0.0) {
            this.r.F(var4);
         }

         this.c = (int)Math.round(this.r.k(var4, 200));
         this.O = 0;

         for (hi.v var6 : this.j) {
            for (jd var8 : var6.p) {
               var8.i = (boolean)(this.W && var6.R && var6.F(var8) ? 1 : 0);
            }

            this.O = this.O + 14 + (var6.R ? var6.h() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.e());
         this.A = this.e() + (this.W ? Math.min(this.O, var12) : 0);
         this.X = Math.clamp(this.X, 0, Math.max(0, this.O - (this.A - this.e())));
         int var13 = this.e() - this.X;

         for (hi.v var16 : this.j) {
            var16.Z = var13;
            var13 += 14;
            if (var16.R) {
               var13 += 2;

               for (jd var10 : var16.p) {
                  if (var10.i) {
                     var10.bounds(this.Y + 4, var13, this.c - 8);
                     if (var10 instanceof ju var11) {
                        var11.q(6, hi.this.w.height() - 40);
                     }

                     var13 += var10.J + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void m(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
         int var5 = 20 + this.A - this.e();
         kv.d(var1, this.Y - 1, this.R - 1, this.c + 2, var5 + 2, -16119286);
         kv.d(var1, this.Y, this.R, this.c, var5, -401337324);
         String var6 = bj.o(this.N.u, (int)((this.c - 10) / 1.2F), bj.j.T);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.Y + this.c / 2.0F, this.R + 5);
            var1.pose().scale(1.2F, 1.2F);
            bj.J(var1, var6, -bj.x(var6, bj.j.T) / 2, 0, -1, bj.j.T);
         } finally {
            var1.pose().popMatrix();
         }

         if (this.W) {
            int var7 = var3 >= this.e() && var3 < this.A ? 1 : 0;
            hi.v var8 = null;
            if (var7 != 0) {
               for (hi.v var10 : this.j) {
                  if (kv.f(var2, var3, this.Y, var10.Z, this.c, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.e() - this.X;
            if (var8 != null) {
               if (this.e.B() < 0.05) {
                  this.F.F(var8.Z - var29);
               }

               this.F.k(var8.Z - var29, 160);
            }

            double var30 = this.e.k(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.Y, this.e(), this.Y + this.c, this.A);

            try {
               hb var12 = LarpClient.R().o().g();

               for (hi.v var14 : this.j) {
                  int var15 = var14.Z + 14 + (var14.R ? var14.h() : 0);
                  if (var15 >= this.e() && var14.Z <= this.A && var14.y.isEnabled()) {
                     kv.d(var1, this.Y, var14.Z, this.c, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  kv.d(var1, this.Y, var29 + (int)Math.round(this.F.B()), this.c, 14, uh.F(419430399, var30));
               }

               for (hi.v var34 : this.j) {
                  int var36 = var34.Z + 14 + (var34.R ? var34.h() : 0);
                  if (var36 >= this.e() && var34.Z <= this.A) {
                     boolean var16 = var34.y.isEnabled();
                     int var17 = var16 ? lv.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = bj.T(var34.y.getDisplayName(), this.c - 14);
                     bj.E(var1, var18, this.Y + (this.c - bj.u(var18)) / 2, var34.Z + 3, var17);
                     if (var12.M(var34.y.getName()) && this.N != pz.s) {
                        kv.d(var1, this.Y + 3, var34.Z + 5, 3, 3, var16 ? var17 : uh.H(var4, 0.9));
                     }

                     if (var34.R) {
                        int var19 = var34.h();
                        kv.d(var1, this.Y, var34.Z + 14, this.c, var19, 1342177280);
                        kv.d(var1, this.Y, var34.Z + 14, 1, var19, var4 | 0xFF000000);

                        for (jd var21 : var34.p) {
                           if (var21.i) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var31 = this.A - this.e();
            if (this.O > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.O);
               int var35 = var31 - var33;
               int var37 = this.e() + (int)Math.round(var35 * ((double)this.X / (this.O - var31)));
               kv.d(var1, this.Y + this.c - 2, var37, 2, var33, uh.H(var4, 0.8));
            }
         }
      }

      String o(int var1, int var2) {
         if (var2 < this.e()) {
            return "Drag to move · Right click folds " + this.N.u;
         }

         for (hi.v var4 : this.j) {
            if (kv.f(var1, var2, this.Y, var4.Z, this.c, 14)) {
               String var8 = var4.y.getDescription();
               return var8.isBlank() ? var4.y.getDisplayName() : var8;
            }

            if (var4.R) {
               for (jd var7 : var4.p) {
                  if (var7.i && var7.contains(var1, var2)) {
                     return var7 instanceof ju var5 ? var5.G() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean a(double var1, double var3, int var5) {
         if (var3 < this.e()) {
            if (var5 == 0) {
               hi.this.E();
               hi.this.D = this;
               hi.this.T = (int)Math.round(var1) - this.Y;
               hi.this.k = (int)Math.round(var3) - this.R;
            } else if (var5 == 1) {
               this.W = (boolean)(!this.W ? 1 : 0);
               hi.this.x(this);
               hi.this.t();
               sr.v(this.W ? sr.e.S : sr.e.d);
            }

            return true;
         } else if (this.W && !(var3 >= this.A)) {
            for (hi.v var7 : this.j) {
               if (kv.f(var1, var3, this.Y, var7.Z, this.c, 14)) {
                  var7.w(var5);
                  return true;
               }

               if (var7.R) {
                  for (jd var9 : var7.p) {
                     if (var9.i && var9.contains(var1, var3)) {
                        hi.this.i(var9);
                        if (var9.click(var1, var3, var5)) {
                           hi.this.F = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            hi.this.E();
            return true;
         } else {
            return true;
         }
      }
   }

   private static final class g extends jd {
      private static final String k = "Legacy";
      private static final int K = 18;
      private static final int E = 10;
      private final Runnable r;
      private final jp f = new jp(1.0);

      g(Runnable var1) {
         this.r = var1;
         this.J = 18;
      }

      int u() {
         return 11 + bj.x("Legacy", bj.j.w) + 6 + 18 + 8;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.f.k(hi.V().G ? 1.0 : 0.0, 100);
         kv.f(var1, this.S - 1, this.u - 1, this.A + 2, this.J + 2, this.J / 2 + 1, var4 ? -12961216 : -14803422);
         kv.f(var1, this.S, this.u, this.A, this.J, this.J / 2, -268106488);
         if (this.T && this.W) {
            kv.I(var1, this.S, this.u, this.A, this.J, ho.u);
         }

         bj.J(var1, "Legacy", this.S + 11, this.u + 5, var4 ? -1 : ho.Z, bj.j.w);
         int var7 = this.S + this.A - 8 - 18;
         int var8 = this.u + (this.J - 10) / 2;
         int var9 = hi.V().Z | 0xFF000000;
         kv.f(var1, var7, var8, 18.0F, 10.0F, 5.0F, uh.q(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         kv.Q(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.r.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.r.run();
         return true;
      }
   }

   private static final class j extends jd {
      private final String Z;

      j(String var1) {
         this.Z = var1;
         this.J = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.i) {
            bj.k(var1, bj.i(this.Z.toUpperCase(Locale.ROOT), this.A - 4, bj.u.m), this.S + 2, this.u + 2, ho.j, bj.u.m);
         }
      }
   }

   private final class v {
      final Module y;
      final List<jd> p = new ArrayList<>();
      final Map<jd, BooleanSupplier> D = new IdentityHashMap<>();
      final Set<String> k = new HashSet<>();
      boolean R;
      int Z;
      String m;

      v(Module var2) {
         this.y = var2;
      }

      boolean F(jd var1) {
         BooleanSupplier var2 = this.D.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int h() {
         int var1 = 5;

         for (jd var3 : this.p) {
            if (var3.i) {
               var1 += var3.J + 2;
            }
         }

         return var1;
      }

      void w(int var1) {
         switch (var1) {
            case 0:
               this.y.toggle();
               sr.v(this.y.isEnabled() ? sr.e.J : sr.e.v);
               break;
            case 1:
               this.R = (boolean)(!this.R ? 1 : 0);
               if (this.R) {
                  this.o();
               } else if (this.p.contains(hi.this.o)) {
                  hi.this.E();
               }

               sr.v(this.R ? sr.e.S : sr.e.d);
               break;
            case 2:
               hb var2 = LarpClient.R().o().g();
               var2.e(this.y.getName(), !var2.M(this.y.getName()));
               LarpClient.R().o().W();

               for (hi.f var4 : hi.this.m) {
                  var4.y();
               }

               sr.v(sr.e.q);
         }
      }

      void M() {
         if (this.R && this.y instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.m, var1.a().getValue())) {
            this.o();
         }
      }

      void o() {
         if (this.p.contains(hi.this.o)) {
            hi.this.E();
         }

         this.p.clear();
         this.D.clear();
         this.m = this.y instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.a().getValue() : null;
         List var8 = pa.I(this.y);
         Iterator var9 = var8.iterator();

         while (true) {
            pa.o var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.y instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     com.larpclient.module.v var10 = new com.larpclient.module.v(
                        this.y instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.y.getKeyBind()
                     );
                     var10.o(this.y::setKeyBind);
                     this.v(new ju(var10), null);
                     if (this.y.keybindMode() != null) {
                        this.v(new ju(this.y.keybindMode()), null);
                     }
                  }

                  this.v(new hi.z(() -> "Reset settings", () -> {
                     for (com.larpclient.module.q var2 : this.y.getSettings()) {
                        var2.u();
                     }

                     if (this.y instanceof FakeSpawnerModule var3x) {
                        var3x.s();
                     }

                     this.o();
                     sr.v(sr.e.q);
                  }, false), null);
                  this.v(new hi.z(() -> "Open full editor", () -> hi.this.h(this.y), false), null);
                  return;
               }

               var3 = (pa.o)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(com.larpclient.module.q::W);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               hi.z var6 = new hi.z(() -> (this.k.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.k.remove(var3.title())) {
                     this.k.add(var3.title());
                  }

                  this.o();
               }, false);
               this.v(var6, var4);
            } while (!this.k.contains(var3.title()));

            if (var8.size() > 1) {
               this.v(new hi.j(var3.title()), var4);
            }

            for (com.larpclient.module.q var12 : var3.settings()) {
               if (!(var12 instanceof com.larpclient.module.e var7 && var7.N())) {
                  ju var13 = new ju(var12);
                  this.v(var13, var13::d);
               }
            }
         }
      }

      private void v(jd var1, BooleanSupplier var2) {
         this.p.add(var1);
         if (var2 != null) {
            this.D.put(var1, var2);
         }
      }
   }

   private static final class z extends jd {
      private final Supplier<String> t;
      private final Runnable X;
      private final boolean N;

      z(Supplier<String> var1, Runnable var2, boolean var3) {
         this.t = var1;
         this.X = var2;
         this.N = var3;
         this.J = 14;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.i) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.T && this.W ? 1 : 0;
            if (this.N) {
               kv.d(var1, this.S - 1, this.u - 1, this.A + 2, this.J + 2, -16119286);
               kv.d(var1, this.S, this.u, this.A, this.J, var4 ? -266330076 : -401337324);
            } else if (var4) {
               kv.d(var1, this.S, this.u, this.A, this.J, 419430399);
            }

            if (var5 != 0) {
               kv.I(var1, this.S, this.u, this.A, this.J, ho.u);
            }

            String var6 = bj.T(this.t.get(), this.A - 8);
            bj.E(var1, var6, this.S + (this.A - bj.u(var6)) / 2, this.u + (this.J - 8) / 2, !var4 && !this.N ? ho.Z : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            sr.v(sr.e.q);
            this.X.run();
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

         this.X.run();
         return true;
      }
   }
}
