package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

abstract class fp extends fs {
   protected f8 T;
   protected h_ G;
   protected final List<j0> V = new ArrayList<>();
   protected final List<s8> P = new ArrayList<>();
   protected int L;
   protected int E;
   protected int c;
   protected int p;
   protected int r;
   protected int k;
   protected boolean A;
   protected String j;
   protected String z = "";
   protected String v = "";
   private final List<s8> i = new ArrayList<>();
   private final List<s8> I = new ArrayList<>();
   private final s8 F = new s8("", this::dismissDialog);
   private List<String> Z = List.of();
   private List<String> n = List.of();
   private int y;
   private int K;
   private int N;
   private int o;
   private final fq U = new fq(1.0);
   private final fq C = new fq(0.0);
   private final fq s = new fq(0.0);
   private final fq d = new fq(0.0);
   private boolean R;
   private boolean X;
   private boolean b;
   private static final int B = 300;
   private static final int f = 14;
   private static final int M = 20;

   protected final void C() {
      this.U.p(0.0);
      this.U.n(1.0, 200);
   }

   protected double revealAmount() {
      return this.U.n(1.0, 200);
   }

   fp(String var1) {
      super(var1);
      this.j = var1;
      this.F.N = (boolean)1;
      this.F.x = "x";
      this.F.O = 18;
      this.F.R = (boolean)0;
   }

   final void u(f8 var1, h_ var2) {
      this.T = var1;
      this.G = var2;
      this.init(var1.width, var1.height);
      this.C();
   }

   void u(h_ var1) {
      if (this.G == null || this.G.width() != var1.width() || this.G.height() != var1.height()) {
         this.R = (boolean)0;
      }

      this.G = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   protected void D() {
      this.W();
      this.V.clear();
      this.P.clear();
      this.i.clear();
      this.I.clear();
      this.z = this.v = "";
      this.f(this.F);
   }

   protected void dismissDialog() {
   }

   protected s8 U(String var1, Runnable var2) {
      s8 var3 = new s8(var1, var2);
      var3.Z = (boolean)1;
      var3.O = 20;
      this.i.add(var3);
      this.f(var3);
      return var3;
   }

   protected s8 option(String var1, String var2, Runnable var3) {
      s8 var4 = this.t(var1, var3);
      var4.N = (boolean)1;
      var4.d = (boolean)0;
      var4.z = var2;
      var4.O = 30;
      this.I.add(var4);
      return var4;
   }

   protected void O() {
      this.f(new fp.b());
   }

   private boolean b(j0 var1) {
      return var1 == this.F || var1 instanceof s8 var2 && this.i.contains(var2);
   }

   protected void f(j0 var1) {
      this.V.add(var1);
      this.J.add(var1);
   }

   protected void text(String var1) {
      this.f(new fp.x(var1));
   }

   protected void n(Supplier<String> var1) {
      this.f(new fp.x(var1));
   }

   protected void z(Supplier<String> var1, IntSupplier var2) {
      this.f(new fp.x(var1, var2));
   }

   protected void i(String var1, Supplier<String> var2) {
      this.f(new fp.x(var1, var2));
   }

   protected s8 t(String var1, Runnable var2) {
      s8 var3 = new s8(var1, var2);
      var3.d = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.f = var1.startsWith("Delete");
      this.f(var3);
      return var3;
   }

   protected void f(String var1, boolean var2, Runnable var3) {
      s8 var4 = new s8(var1, var3);
      var4.N = (boolean)1;
      var4.g = (boolean)1;
      var4.a = var2;
      this.P.add(var4);
      this.J.add(var4);
   }

   protected int bodyTop() {
      return this.G.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.G.contentX();
      int var2 = Math.max(1, (this.G.right() - var1) / Math.max(1, this.P.size()));

      for (s8 var4 : this.P) {
         int var5 = Math.min(var2, Math.max(62, ic.H(var4.W) + 20));
         var4.bounds(var1, this.G.top() + 12, var5 - 4);
         var4.O = 24;
         var1 += var5;
      }

      this.F.R = this.A;
      if (this.A) {
         this.u();
      } else {
         this.E = this.G.contentX();
         this.p = this.G.right();
         this.c = this.bodyTop();
         this.r = this.G.bottom();
         this.T();
      }
   }

   private void u() {
      int var1 = this.G.bodyTop() + 8;
      int var2 = Math.max(1, this.G.bottom() - 8 - var1);
      int var3 = this.G.right() - this.G.contentX();
      this.N = Math.max(1, Math.min(300, var3 - 16));
      this.y = this.G.contentX() + (var3 - this.N) / 2;
      this.E = this.y + 14 - 8;
      this.p = this.y + this.N - 14 + 12;
      int var4 = Math.max(1, this.N - 28 - 22);
      this.Z = this.z.isEmpty() ? List.of() : L(this.z, var4, var0 -> ic.f(var0, ic.n.Z));
      this.n = this.v.isEmpty() ? List.of() : L(this.v, var4, var0 -> ic.f(var0, ic.n.I));
      int var5 = 14 + Math.max(14, this.Z.size() * (ic.n.Z.T() + 3) + (this.n.isEmpty() ? 0 : 1 + this.n.size() * (ic.n.I.T() + 3))) + 8;
      this.l();
      int var6 = Math.max(0, this.k - 4);
      int var7 = this.i.isEmpty() ? 10 : 46;
      this.o = Math.min(var2, var5 + var6 + var7);
      this.K = var1 + (var2 - this.o) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.c = this.K + var5;
      this.r = Math.max(this.c, this.K + this.o - var7);
      this.A(0);
      this.F.bounds(this.y + this.N - 14 - 18 + 4, this.K + 14 - 4, 18);
      int var8 = this.K + this.o - 14 - 20;
      List var9 = this.i.stream().map(var0 -> Math.max(64, ic.f(var0.W, ic.n.I) + 24)).toList();
      int var10 = Math.max(0, this.i.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.N - 28 - var10) / Math.max(1, var11));
      int var14 = this.y + this.N - 14;

      for (int var15 = this.i.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.i.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void T() {
      if (!this.layoutCustomContent()) {
         this.l();
         this.A((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void l() {
      this.k = 0;

      for (j0 var2 : this.V) {
         if (!this.b(var2)) {
            var2.R = this.componentShown(var2);
            if (!var2.R) {
               var2.blur();
            } else {
               var2.bounds(this.E + 8, 0, Math.max(1, this.p - this.E - 20));
               if (this.A && var2 instanceof s8 var3 && this.I.contains(var3)) {
                  var2.P -= 8;
                  var2.L += 16;
               }

               if (var2 instanceof fp.x var11) {
                  if (this.A) {
                     var11.P -= 4;
                  }

                  var11.C();
               }
            }
         }
      }

      int var9 = 0;

      while (var9 < this.V.size()) {
         j0 var10 = this.V.get(var9);
         if (!var10.R || this.b(var10)) {
            var9++;
         } else if (var10 instanceof su var12 && var12.x()) {
            ArrayList var13 = new ArrayList();

            while (var9 < this.V.size()) {
               if (!(this.V.get(var9) instanceof su var15) || !var15.x()) {
                  break;
               }

               if (var15.R) {
                  var13.add(var15);
               }

               var9++;
            }

            pf.t var16 = pf.h(Math.max(1, this.p - this.E - 20), var13.stream().map(su::j).toList());

            for (int var19 = 0; var19 < var13.size(); var19++) {
               pf.j var20 = var16.cells().get(var19);
               ((su)var13.get(var19)).bounds(this.E + 8 + var20.x(), this.k + var20.y(), var20.width());
            }

            this.k = this.k + var16.height() + 4;
         } else if (var10 instanceof s8 var4 && var4.l != null) {
            ArrayList var5 = new ArrayList();

            while (var9 < this.V.size()) {
               if (!(this.V.get(var9) instanceof s8 var6) || var6.l != var4.l) {
                  break;
               }

               if (var6.R) {
                  var5.add(var6);
               }

               var9++;
            }

            pf.t var14 = pf.q(Math.max(1, this.p - this.E - 20), var5.stream().map(var0 -> ic.f(var0.W, ic.n.C) + 18).toList());

            for (int var17 = 0; var17 < var5.size(); var17++) {
               pf.j var8 = var14.cells().get(var17);
               ((s8)var5.get(var17)).bounds(this.E + 8 + var8.x(), this.k + var8.y(), var8.width());
               ((s8)var5.get(var17)).O = 24;
            }

            this.k = this.k + var14.height() + 4;
         } else {
            var10.T = this.k;
            this.k = this.k + var10.O + 4;
            var9++;
         }
      }
   }

   private void A(int var1) {
      int var2 = this.k - (this.A ? 4 : 0) - (this.r - this.c);
      this.L = Math.clamp(this.L, 0, Math.max(0, var2));
      if (!this.R) {
         this.C.p(this.L);
         this.R = (boolean)1;
      }

      int var3 = (int)Math.round(this.C.n(this.L, 150));
      int var4 = this.c - var3 + var1;

      for (j0 var6 : this.V) {
         if (var6.R && !this.b(var6)) {
            var6.T += var4;
            if (var6 instanceof su var7) {
               var7.B(this.G.top() + 12, this.r);
            }
         }
      }
   }

   protected boolean componentShown(j0 var1) {
      return var1 instanceof su var3 ? var3.a() : !(var1 instanceof sj var2 && !var2.t());
   }

   protected boolean layoutCustomContent() {
      return false;
   }

   protected boolean contentShown() {
      return true;
   }

   protected boolean showTitle() {
      return true;
   }

   protected int titleX() {
      return this.G.contentX() + 8;
   }

   protected int titleY() {
      return this.G.bodyTop() + 8;
   }

   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.A != this.b) {
         this.C();
         this.b = this.A;
      }

      this.layout();

      for (s8 var6 : this.P) {
         if (var6.a) {
            if (!this.X) {
               this.s.p(var6.P);
               this.d.p(var6.L);
               this.X = (boolean)1;
            }

            int var7 = (int)Math.round(this.s.n(var6.P, 200));
            int var8 = (int)Math.round(this.d.n(var6.L, 200));
            int var9 = com.larpclient.c.J() ? com.larpclient.c.e(var7, com.larpclient.c.D) : vv.N(com.larpclient.c.H, 0.75);
            wc.b(var1, var7 + 7, var6.T + var6.O, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, vv.N(var9, 0.45));
            wc.b(var1, var7 + 7, var6.T + var6.O, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (s8 var24 : this.P) {
         var24.render(var1, this.A ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         ic.N(var1, ic.o(this.j, Math.max(1, this.p - this.titleX() - 32), ic.n.Z), this.titleX(), this.titleY(), com.larpclient.c.H, ic.n.Z);
      }

      this.renderExtra(var1, this.A ? -1 : var2, var3);
      if (this.A) {
         this.a(var1, var2, var3);
      }

      var1.enableScissor(this.E, this.c, this.p, this.r);

      try (oa var22 = oa.j(this.revealAmount())) {
         for (j0 var27 : this.V) {
            if (this.contentShown() && var27.R && !this.b(var27) && var27.T + var27.O > this.c && var27.T < this.r) {
               var27.render(var1, var3 >= this.c && var3 < this.r ? var2 : -1, var3);
            }
         }
      } finally {
         var1.disableScissor();
      }

      int var23 = this.r - this.c;
      if (this.contentShown()) {
         wc.V(var1, this.p - 3, this.c, Math.max(1, var23), this.k - (this.A ? 4 : 0), (int)Math.round(this.C.K()));
      }

      if (this.A) {
         try (oa var26 = oa.j(this.revealAmount())) {
            if (this.k - 4 > var23) {
               int var28 = vv.O(com.larpclient.c.Xn, 1.2);
               if (this.L > 0) {
                  wc.b(var1, this.y + 14, this.c - 1, this.N - 28, 1, 0, var28);
               }

               wc.b(var1, this.y + 14, this.r, this.N - 28, 1, 0, var28);
            }

            this.F.render(var1, var2, var3);

            for (s8 var30 : this.i) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.X(var1, var2, var3);
      }
   }

   private void a(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.revealAmount();
      wc.b(
         var1,
         this.G.contentX(),
         this.G.bodyTop(),
         this.G.right() - this.G.contentX(),
         this.G.bottom() - this.G.bodyTop(),
         com.larpclient.c.i,
         vv.O(-1728053248, var4)
      );
      w1.h(var1, this.y, this.K + 3, this.N, this.o, com.larpclient.c.i, 16.0F, vv.O(805306368, var4), vv.O(1342177280, var4));
      wc.g(var1, this.y, this.K, this.N, this.o, com.larpclient.c.i, vv.O(com.larpclient.c.r, var4), 0.0);

      try (oa var6 = oa.j(var4)) {
         int var7 = this.K + 14;

         for (String var9 : this.Z) {
            ic.N(var1, var9, this.y + 14, var7, com.larpclient.c.H, ic.n.Z);
            var7 += ic.n.Z.T() + 3;
         }

         var7++;

         for (String var14 : this.n) {
            ic.N(var1, var14, this.y + 14, var7, com.larpclient.c.W, ic.n.I);
            var7 += ic.n.I.T() + 3;
         }
      }
   }

   private boolean Q(double var1, double var3) {
      return this.A
         && !h5.H(var1, var3, this.y, this.K, this.N, this.o)
         && var1 >= this.G.contentX()
         && var1 < this.G.right()
         && var3 >= this.G.top()
         && var3 < this.G.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void X(GuiGraphicsExtractor var1, int var2, int var3) {
      su var4 = this.d();
      if (var4 != null) {
         var4.l(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private su d() {
      for (j0 var2 : this.V) {
         if (var2 instanceof su var3 && var3.n()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(j0 var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.A && !this.V.contains(var1)) {
         return false;
      } else {
         return this.b(var1) ? this.A : !this.V.contains(var1) || this.contentShown() && var4 >= this.c && var4 < this.r && var2 >= this.E && var2 < this.p;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.layout();
      su var3 = this.d();
      if (var3 == null) {
         if (j2.L(var1) == 0 && this.Q(var1.x(), var1.y()) && this.horizontalInput(var1.x()) && this.V.stream().noneMatch(var0 -> var0 instanceof sk)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.x()) && var3.W(var1.x(), var1.y())) {
            var3.b(var1.x(), var1.y(), j2.L(var1));
            this.g = var3;
         } else {
            var3.O();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      su var9 = this.d();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.W(var1, var3)) {
            var9.L(var7);
         }

         return true;
      } else if (this.horizontalInput(var1) && this.contentShown() && !(var1 < this.E) && !(var1 >= this.p) && !(var3 < this.c) && !(var3 >= this.r)) {
         this.L -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(j0 var1) {
      if (this.V.contains(var1)) {
         if (var1.T < this.c) {
            this.L = this.L - (this.c - var1.T);
         } else if (var1.T + var1.O > this.r) {
            this.L = this.L + (var1.T + var1.O - this.r);
         }

         this.C.p(this.L);
         this.layout();
      }
   }

   protected void i(j0 var1) {
      this.q = var1;
      var1.F = var1.X = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<j0> j() {
      return this.J.stream().filter(var1 -> var1.R && var1.V && (!this.V.contains(var1) || this.contentShown()) && (!this.A || this.V.contains(var1))).toList();
   }

   j0 t() {
      return this.q;
   }

   void y() {
      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.g = null;
   }

   void h(j0 var1) {
      this.i(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.W();
   }

   String d(int var1, int var2) {
      if (this.A) {
         return "";
      } else if (this.q instanceof su var3 && this.q.X) {
         return var3.p();
      } else if (this.horizontalInput(var1) && var2 >= this.c && var2 < this.r) {
         for (j0 var7 : this.V) {
            if (var7 instanceof su var5 && var5.contains(var1, var2)) {
               return var5.p();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean c() {
      return this.A || this.d() != null;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      su var2 = this.d();
      if (j2.P(var1) == 256 && var2 != null) {
         var2.O();
         return true;
      }

      if (j2.P(var1) == 258) {
         List var3 = this.j();
         int var4 = (var1.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.q == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.q);
         this.y();
         if (!var3.isEmpty()) {
            this.h((j0)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      this.T.onClose();
   }

   static List<String> L(String var0, int var1, ToIntFunction<String> var2) {
      ArrayList var3 = new ArrayList();

      for (String var7 : var0.split("\n", -1)) {
         String var8 = var7;

         while (!var8.isEmpty() && var2.applyAsInt(var8) > var1) {
            int var9 = var8.length();

            while (var9 > Character.charCount(var8.codePointAt(0)) && var2.applyAsInt(var8.substring(0, var9)) > var1) {
               var9 = var8.offsetByCodePoints(var9, -1);
            }

            int var10 = var8.lastIndexOf(32, var9 - 1);
            if (var10 > 0) {
               var9 = var10 + 1;
            }

            var3.add(var8.substring(0, var9));
            var8 = var8.substring(var9);
         }

         var3.add(var8);
      }

      return var3;
   }

   private static final class b extends j0 {
      b() {
         this.V = (boolean)0;
         this.O = 5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         wc.b(var1, this.P, this.T + 2, Math.max(1, this.L), 1, 0, vv.O(com.larpclient.c.Xn, 1.2));
      }
   }

   private static final class x extends j0 {
      private final Supplier<String> N;
      private List<String> t = List.of();
      private int I = -1;
      private String G;
      private final String y;
      private final IntSupplier Y;

      x(String var1) {
         this(() -> var1);
      }

      x(Supplier<String> var1) {
         this(var1, () -> com.larpclient.c.W);
      }

      x(Supplier<String> var1, IntSupplier var2) {
         this(null, var1, var2);
      }

      x(String var1, Supplier<String> var2) {
         this(var1, var2, () -> com.larpclient.c.W);
      }

      private x(String var1, Supplier<String> var2, IntSupplier var3) {
         this.y = var1;
         this.N = var2;
         this.Y = var3;
         this.V = (boolean)0;
      }

      void C() {
         if (this.y == null) {
            String var1 = this.N.get();
            if (this.I != this.L || !var1.equals(this.G)) {
               this.I = this.L;
               this.G = var1;
               this.t = fp.L(var1, this.L - 8, ic::H);
               this.O = Math.max(22, this.t.size() * 12 + 6);
            }
         }
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.y != null) {
            String var6 = ic.U(this.N.get(), Math.max(1, this.L * 2 / 3));
            int var5 = ic.H(var6);
            ic.N(var1, ic.U(this.y, Math.max(1, this.L - var5 - 20)), this.P + 4, this.T + 7, com.larpclient.c.H);
            ic.N(var1, var6, this.P + this.L - 4 - var5, this.T + 7, this.Y.getAsInt());
         } else {
            for (int var4 = 0; var4 < this.t.size(); var4++) {
               ic.N(var1, this.t.get(var4), this.P + 4, this.T + 4 + var4 * 12, this.Y.getAsInt());
            }
         }
      }
   }
}
