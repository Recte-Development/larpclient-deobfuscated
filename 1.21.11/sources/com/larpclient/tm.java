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
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class tm extends t1 {
   static final int w = 8;
   static final int N = 6;
   static final int X = 20;
   static final int S = 14;
   static final int m = 36;
   static final int z = 46;
   static final int v = 20;
   static final int W = 18;
   static final int s = 90;
   static final int F = 122;
   static final int d = 160;
   private static final int h = -401337324;
   private static final int R = -16119286;
   private final class_437 D;
   private final List<tm.z> Q = new ArrayList<>();
   private final qb L = new qb(0.0);
   private final Matrix3x2f t = new Matrix3x2f();
   private eh P;
   private lo Y;
   private tm.k g;
   private tm.k l;
   private tm.t c;
   private String M = "";
   private String i = "";
   private int k;
   private int r;
   private long C;
   private tm.z b;
   private int a;
   private int u;
   private boolean B;
   private boolean n;
   private boolean o;

   public static class_437 y(class_437 var0) {
      return U().Z ? new tm(var0) : new tk(var0);
   }

   public static boolean K(class_437 var0) {
      return var0 instanceof tk || var0 instanceof tm;
   }

   private static ec U() {
      return LarpClient.b().e().F().M;
   }

   public tm(class_437 var1) {
      super("Larp client");
      this.D = var1;
   }

   static int a(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] E(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] p(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void method_25426() {
      this.P = this.K();
      this.t.set(this.P.transform());
      this.B = (boolean)0;
      if (!this.n) {
         this.n = (boolean)1;
         this.L.w(0.0);
         c_.b(c_.d.R);
      }

      this.L.a(1.0, this.d());
      if (this.Q.isEmpty()) {
         this.I();
      }

      this.y();
      String var1 = this.Y == null ? null : this.Y.h;
      this.Y = new lo("Search modules...", var1x -> {
         this.M = var1x;

         for (tm.z var3 : this.Q) {
            var3.X();
            var3.E = 0;
         }
      }, 64);
      this.Y.R = "search";
      this.Y.h = var1 == null ? this.M : var1;
      this.g = new tm.k(() -> "Settings", this::N, true);
      this.l = new tm.k(() -> "Reset layout", this::i, true);
      this.c = new tm.t(this::s);
      this.h();
      q8.v("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.Q.size(), this.P.width(), this.P.height());
   }

   private eh K() {
      return eh.from(this.field_22787.method_22683(), LarpClient.b().e().F().f);
   }

   private int d() {
      return LarpClient.b().e().F().G;
   }

   private void I() {
      this.Q.clear();

      for (yc var4 : yc.values()) {
         if (var4 != yc.e) {
            this.Q.add(new tm.z(var4));
         }
      }

      this.Q.add(new tm.z(yc.e));
   }

   private void y() {
      ArrayList var1 = new ArrayList<>(this.Q);
      var1.sort(Comparator.comparingInt(var0 -> var0.N));
      int var2 = this.K().width();
      int var3 = a(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         tm.z var5 = (tm.z)var1.get(var4);
         ec.y var6 = U().F(var5.d.name());
         int[] var7 = var6 == null ? E(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.l = var7[0];
         var5.F = var7[1];
         var5.G = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.X();
      }
   }

   private void i() {
      U().k();
      this.y();
      this.o = (boolean)1;
      this.L();
      c_.b(c_.d.q);
      q8.G("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void s() {
      if (U().Z) {
         U().Z = (boolean)0;
         this.C = System.nanoTime();
         this.o = (boolean)1;
         this.L();
         c_.b(c_.d.n);
         q8.G("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void N() {
      this.W();
      this.field_22787.method_1507(tk.Q(this, "Interface"));
   }

   private void L(Module var1) {
      this.W();
      hz.k = yc.w(var1).name();
      hz.Z = new hz.y("Modules", var1.getName(), "", 0, 0);
      this.field_22787.method_1507(new tk(this));
   }

   private void i(tm.z var1) {
      try {
         U().A(var1.d.name(), new ec.y(var1.l, var1.F, var1.G));
      } catch (IllegalArgumentException var3) {
         q8.L("GUI", "Legacy panel {} place not saved: {}", var1.d.name(), var3.getMessage());
      }

      this.o = (boolean)1;
   }

   private void L() {
      if (this.o) {
         LarpClient.b().e().g();
         this.o = (boolean)0;
      }
   }

   public void method_25419() {
      if (!this.B) {
         this.B = (boolean)1;
         this.W();
         c_.b(c_.d.Y);
         this.field_22787.method_1507(this.D);
      }
   }

   public void method_25432() {
      this.W();
      this.L();
      LarpClient.b().e().U();
      q8.v("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void method_25393() {
      int var1 = this.C != 0L && System.nanoTime() - this.C < 100000000L ? 1 : 0;
      if (!this.B && var1 == 0 && !U().Z) {
         this.B = (boolean)1;
         q8.G("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.field_22787.method_1507(new tk(this.D));
      } else {
         for (tm.z var3 : this.Q) {
            for (tm.r var5 : var3.h) {
               var5.p();
            }
         }
      }
   }

   private void W() {
      if (this.G != null) {
         this.G.blur();
      }

      this.G = this.y = null;
   }

   private void h() {
      eh var1 = this.K();
      if (!var1.equals(this.P) && this.b == null && this.y == null) {
         this.P = var1;
      }

      int var2 = a(this.P.width(), this.Q.size());
      this.Y.bounds(this.P.width() / 2 - 95, 8, 190);
      this.Y.w = 20;
      this.g.bounds(this.Y.D + this.Y.d + 6, 8, 58);
      this.g.w = 20;
      this.l.bounds(this.g.D + this.g.d + 4, 8, 72);
      this.l.w = 20;
      this.i = s5.S(LarpClient.b().V());
      this.r = uw.O(this.i, uw.r.M) + uw.O(" = menu", uw.r.M) + 22;
      int var3 = this.c.k();
      this.k = (this.P.width() - this.r - 6 - var3) / 2;
      this.c.bounds(this.k + this.r + 6, this.u(), var3);
      this.c.w = 18;
      this.K.clear();
      this.K.add(this.Y);
      this.K.add(this.g);
      this.K.add(this.l);
      this.K.add(this.c);

      for (tm.z var5 : this.Q) {
         int[] var6 = p(var5.l, var5.F, var5.u, this.P.width(), this.P.height());
         if (var5 != this.b) {
            var5.l = var6[0];
            var5.F = var6[1];
         }

         var5.S(var2, this.P.height());
         if (var5.Y()) {
            for (tm.r var8 : var5.h) {
               if (var8.Q) {
                  for (lc var10 : var8.N) {
                     if (var10.I) {
                        this.K.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f p(double var1, double var3) {
      return new Matrix3x2f(this.t).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void method_25420(class_332 var1, int var2, int var3, float var4) {
      if (this.field_22787.field_1687 == null) {
         super.method_25420(var1, var2, var3, var4);
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      long var5 = c5.X();
      this.h();
      y0 var7 = LarpClient.b().e().F();
      double var8 = this.L.a(this.B ? 0.0 : 1.0, var7.G);
      this.t.set(this.P.transform()).mul(sd.X(var7.K, var8, this.P.width() / 2.0, this.P.height() / 2.0));
      if (var7.K.equals("Slide")) {
         this.t.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.p(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.method_51448().pushMatrix();
      var1.method_51448().mul(this.t);

      try (fp var11 = fp.O(var8)) {
         int var12 = U().b;
         this.Y.render(var1, var2, var3);
         this.g.render(var1, var2, var3);
         this.l.render(var1, var2, var3);
         tm.z var13 = this.O(var2, var3);
         this.c.render(var1, var13 == null ? var2 : -1, var3);

         for (tm.z var15 : this.Q) {
            if (var15.Y()) {
               var15.M(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.G instanceof l9 var25 && var25.j()) {
            var25.S(var1, var2, var3);
         }

         String var26 = var13 != null ? var13.v(var2, var3) : (this.c.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.U(var1, var26, var12);
      } finally {
         var1.method_51448().popMatrix();
      }

      c5.w("Legacy ClickGUI", var5);
   }

   private int u() {
      return this.P.height() - 46 + 20;
   }

   private void U(class_332 var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = uw.J(var4, this.P.width() - 40);
      int var5 = uw.r(var4);
      int var6 = this.u();
      uw.G(var1, var4, (this.P.width() - var5) / 2, var6 - 13, ez.PH);
      uw.r var7 = uw.r.M;
      int var8 = this.k;
      int var9 = this.r;
      gu.N(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      gu.N(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      uw.N(var1, this.i, var8 + 11, var6 + 5, jx.readableAccent(var3, -16448248), var7);
      uw.N(var1, " = menu", var8 + 11 + uw.O(this.i, var7), var6 + 5, -1, var7);
   }

   private tm.z O(double var1, double var3) {
      for (int var5 = this.Q.size() - 1; var5 >= 0; var5--) {
         if (this.Q.get(var5).Y() && this.Q.get(var5).S(var1, var3)) {
            return this.Q.get(var5);
         }
      }

      return null;
   }

   private void I(tm.z var1) {
      this.Q.remove(var1);
      this.Q.add(var1);
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.B) {
         return true;
      }

      Vector2f var3 = this.p(var1.comp_4798(), var1.comp_4799());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = s2.m(var1);
      this.h();
      if (this.G != null && this.G.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.b().V() == s5.K(var8)) {
         this.method_25419();
         return true;
      }

      if (this.G instanceof l9 var9 && var9.p(var4, var6)) {
         if (var9.b(var4, var6, var8)) {
            this.y = var9;
         }

         return true;
      } else {
         tm.z var12 = this.O(var4, var6);
         if (var12 != null) {
            this.I(var12);
            return var12.N(var4, var6, var8);
         }

         for (lc var11 : List.of(this.Y, this.g, this.l, this.c)) {
            if (var11.contains(var4, var6)) {
               this.C(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.W();
         return false;
      }
   }

   private void C(lc var1) {
      if (this.G != null && this.G != var1) {
         this.G.blur();
      }

      this.G = var1;
      var1.M = (boolean)1;
      var1.q = (boolean)0;
   }

   @Override
   public boolean method_25403(class_11909 var1, double var2, double var4) {
      if (this.B) {
         return true;
      } else {
         Vector2f var6 = this.p(var1.comp_4798(), var1.comp_4799());
         if (this.b != null) {
            int[] var7 = p(Math.round(var6.x) - this.a, Math.round(var6.y) - this.u, this.b.u, this.P.width(), this.P.height());
            this.b.l = var7[0];
            this.b.F = var7[1];
            return true;
         } else if (this.y != null) {
            this.y.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean method_25406(class_11909 var1) {
      if (this.b != null) {
         this.i(this.b);
         this.L();
         q8.v("GUI", "Legacy panel {} moved to {}, {}", this.b.d.t, this.b.l, this.b.F);
         this.b = null;
      }

      if (this.y != null) {
         this.y.release();
      }

      this.y = null;
      return true;
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (this.B) {
         return true;
      } else {
         Vector2f var9 = this.p(var1, var3);
         if (this.G instanceof l9 var10 && var10.p(var9.x, var9.y)) {
            var10.V(var7);
            return true;
         } else {
            tm.z var12 = this.O(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.E = var12.E - (int)Math.round(var7 * 20.0);
            this.h();
            return true;
         }
      }
   }

   public boolean s() {
      return tk.Z(this.G);
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      if (this.B) {
         return true;
      } else {
         return tk.A(s2.i(var1), this.G, null, this::method_25419) ? true : super.method_25404(var1);
      }
   }

   @Override
   public boolean method_25400(class_11905 var1) {
      if (this.B) {
         return true;
      } else if (super.method_25400(var1)) {
         return true;
      } else if ((this.G == null || this.G == this.Y) && Character.isLetterOrDigit(var1.comp_4793())) {
         this.C(this.Y);
         return this.Y.character(var1.comp_4793());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(lc var1) {
      for (tm.z var3 : this.Q) {
         for (tm.r var5 : var3.h) {
            if (var5.Q && var5.N.contains(var1)) {
               if (var1.g < var3.Z()) {
                  var3.E = var3.E - (var3.Z() - var1.g);
               } else if (var1.g + var1.w > var3.q) {
                  var3.E = var3.E + (var1.g + var1.w - var3.q);
               }

               this.h();
               return;
            }
         }
      }
   }

   private static final class f extends lc {
      private final String S;

      f(String var1) {
         this.S = var1;
         this.w = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         if (this.I) {
            uw.X(var1, uw.X(this.S.toUpperCase(Locale.ROOT), this.d - 4, uw.c.H), this.D + 2, this.g + 2, ez.t, uw.c.H);
         }
      }
   }

   private static final class k extends lc {
      private final Supplier<String> B;
      private final Runnable k;
      private final boolean J;

      k(Supplier<String> var1, Runnable var2, boolean var3) {
         this.B = var1;
         this.k = var2;
         this.J = var3;
         this.w = 14;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         if (this.I) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.M && this.q ? 1 : 0;
            if (this.J) {
               gu.Z(var1, this.D - 1, this.g - 1, this.d + 2, this.w + 2, -16119286);
               gu.Z(var1, this.D, this.g, this.d, this.w, var4 ? -266330076 : -401337324);
            } else if (var4) {
               gu.Z(var1, this.D, this.g, this.d, this.w, 419430399);
            }

            if (var5 != 0) {
               gu.a(var1, this.D, this.g, this.d, this.w, ez.H);
            }

            String var6 = uw.J(this.B.get(), this.d - 8);
            uw.G(var1, var6, this.D + (this.d - uw.r(var6)) / 2, this.g + (this.w - 8) / 2, !var4 && !this.J ? ez.PH : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            c_.b(c_.d.q);
            this.k.run();
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

         this.k.run();
         return true;
      }
   }

   private final class r {
      final Module X;
      final List<lc> N = new ArrayList<>();
      final Map<lc, BooleanSupplier> c = new IdentityHashMap<>();
      final Set<String> r = new HashSet<>();
      boolean Q;
      int Y;
      String x;

      r(Module var2) {
         this.X = var2;
      }

      boolean f(lc var1) {
         BooleanSupplier var2 = this.c.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int z() {
         int var1 = 5;

         for (lc var3 : this.N) {
            if (var3.I) {
               var1 += var3.w + 2;
            }
         }

         return var1;
      }

      void A(int var1) {
         switch (var1) {
            case 0:
               this.X.toggle();
               c_.b(this.X.isEnabled() ? c_.d.d : c_.d.n);
               break;
            case 1:
               this.Q = (boolean)(!this.Q ? 1 : 0);
               if (this.Q) {
                  this.e();
               } else if (this.N.contains(tm.this.G)) {
                  tm.this.W();
               }

               c_.b(this.Q ? c_.d.D : c_.d.g);
               break;
            case 2:
               y0 var2 = LarpClient.b().e().F();
               var2.p(this.X.getName(), !var2.H(this.X.getName()));
               LarpClient.b().e().g();

               for (tm.z var4 : tm.this.Q) {
                  var4.X();
               }

               c_.b(c_.d.q);
         }
      }

      void p() {
         if (this.Q && this.X instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.x, var1.s().getValue())) {
            this.e();
         }
      }

      void e() {
         if (this.N.contains(tm.this.G)) {
            tm.this.W();
         }

         this.N.clear();
         this.c.clear();
         this.x = this.X instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.s().getValue() : null;
         List var8 = fl.T(this.X);
         Iterator var9 = var8.iterator();

         while (true) {
            fl.u var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.X instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     com.larpclient.module.lw var10 = new com.larpclient.module.lw(
                        this.X instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.X.getKeyBind()
                     );
                     var10.a(this.X::setKeyBind);
                     this.G(new l9(var10), null);
                     if (this.X.keybindMode() != null) {
                        this.G(new l9(this.X.keybindMode()), null);
                     }
                  }

                  this.G(new tm.k(() -> "Reset settings", () -> {
                     for (com.larpclient.module.lb var2 : this.X.getSettings()) {
                        var2.T();
                     }

                     if (this.X instanceof FakeSpawnerModule var3x) {
                        var3x.P();
                     }

                     this.e();
                     c_.b(c_.d.q);
                  }, false), null);
                  this.G(new tm.k(() -> "Open full editor", () -> tm.this.L(this.X), false), null);
                  return;
               }

               var3 = (fl.u)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(com.larpclient.module.lb::B);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               tm.k var6 = new tm.k(() -> (this.r.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.r.remove(var3.title())) {
                     this.r.add(var3.title());
                  }

                  this.e();
               }, false);
               this.G(var6, var4);
            } while (!this.r.contains(var3.title()));

            if (var8.size() > 1) {
               this.G(new tm.f(var3.title()), var4);
            }

            for (com.larpclient.module.lb var12 : var3.settings()) {
               if (!(var12 instanceof com.larpclient.module.lo var7 && var7.m())) {
                  l9 var13 = new l9(var12);
                  this.G(var13, var13::L);
               }
            }
         }
      }

      private void G(lc var1, BooleanSupplier var2) {
         this.N.add(var1);
         if (var2 != null) {
            this.c.put(var1, var2);
         }
      }
   }

   private static final class t extends lc {
      private static final String e = "Legacy";
      private static final int J = 18;
      private static final int y = 10;
      private final Runnable Y;
      private final qb H = new qb(1.0);

      t(Runnable var1) {
         this.Y = var1;
         this.w = 18;
      }

      int k() {
         return 11 + uw.O("Legacy", uw.r.M) + 6 + 18 + 8;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.H.a(tm.U().Z ? 1.0 : 0.0, 100);
         gu.N(var1, this.D - 1, this.g - 1, this.d + 2, this.w + 2, this.w / 2 + 1, var4 ? -12961216 : -14803422);
         gu.N(var1, this.D, this.g, this.d, this.w, this.w / 2, -268106488);
         if (this.M && this.q) {
            gu.a(var1, this.D, this.g, this.d, this.w, ez.H);
         }

         uw.N(var1, "Legacy", this.D + 11, this.g + 5, var4 ? -1 : ez.PH, uw.r.M);
         int var7 = this.D + this.d - 8 - 18;
         int var8 = this.g + (this.w - 10) / 2;
         int var9 = tm.U().b | 0xFF000000;
         gu.N(var1, var7, var8, 18.0F, 10.0F, 5.0F, ei.W(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         gu.Q(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.Y.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.Y.run();
         return true;
      }
   }

   private final class z {
      final yc d;
      final Map<Module, tm.r> W = new IdentityHashMap<>();
      final List<tm.r> h = new ArrayList<>();
      final qb w = new qb(-1.0);
      final qb j = new qb(0.0);
      final qb V = new qb(0.0);
      final int N;
      int l;
      int F;
      int u = 90;
      int E;
      int q;
      int I;
      boolean G = (boolean)1;

      z(yc var2) {
         this.d = var2;
         this.N = tm.this.Q.size();
      }

      void X() {
         this.h.clear();
         List var1 = LarpClient.b().G().Y();
         y0 var2 = LarpClient.b().e().F();

         for (Module var4 : this.d.T(var1, "", var2)) {
            if (tm.this.M.isBlank() || qi.H(var4, tm.this.M) != Integer.MAX_VALUE) {
               this.h.add(this.W.computeIfAbsent(var4, var1x -> tm.this.new r(var1x)));
            }
         }
      }

      boolean Y() {
         return !this.h.isEmpty();
      }

      int Z() {
         return this.F + 20;
      }

      boolean S(double var1, double var3) {
         return gu.X(var1, var3, this.l, this.F, this.u, 20 + (this.G ? this.q - this.Z() : 0));
      }

      void S(int var1, int var2) {
         boolean var3 = this.h.stream().anyMatch(var0 -> var0.Q);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.w.u() < 0.0) {
            this.w.w(var4);
         }

         this.u = (int)Math.round(this.w.a(var4, 200));
         this.I = 0;

         for (tm.r var6 : this.h) {
            for (lc var8 : var6.N) {
               var8.I = (boolean)(this.G && var6.Q && var6.f(var8) ? 1 : 0);
            }

            this.I = this.I + 14 + (var6.Q ? var6.z() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.Z());
         this.q = this.Z() + (this.G ? Math.min(this.I, var12) : 0);
         this.E = Math.clamp(this.E, 0, Math.max(0, this.I - (this.q - this.Z())));
         int var13 = this.Z() - this.E;

         for (tm.r var16 : this.h) {
            var16.Y = var13;
            var13 += 14;
            if (var16.Q) {
               var13 += 2;

               for (lc var10 : var16.N) {
                  if (var10.I) {
                     var10.bounds(this.l + 4, var13, this.u - 8);
                     if (var10 instanceof l9 var11) {
                        var11.Z(6, tm.this.P.height() - 40);
                     }

                     var13 += var10.w + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void M(class_332 var1, int var2, int var3, int var4) {
         int var5 = 20 + this.q - this.Z();
         gu.Z(var1, this.l - 1, this.F - 1, this.u + 2, var5 + 2, -16119286);
         gu.Z(var1, this.l, this.F, this.u, var5, -401337324);
         String var6 = uw.H(this.d.t, (int)((this.u - 10) / 1.2F), uw.r.V);
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(this.l + this.u / 2.0F, this.F + 5);
            var1.method_51448().scale(1.2F, 1.2F);
            uw.N(var1, var6, -uw.O(var6, uw.r.V) / 2, 0, -1, uw.r.V);
         } finally {
            var1.method_51448().popMatrix();
         }

         if (this.G) {
            int var7 = var3 >= this.Z() && var3 < this.q ? 1 : 0;
            tm.r var8 = null;
            if (var7 != 0) {
               for (tm.r var10 : this.h) {
                  if (gu.X(var2, var3, this.l, var10.Y, this.u, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.Z() - this.E;
            if (var8 != null) {
               if (this.V.u() < 0.05) {
                  this.j.w(var8.Y - var29);
               }

               this.j.a(var8.Y - var29, 160);
            }

            double var30 = this.V.a(var8 == null ? 0.0 : 1.0, 160);
            var1.method_44379(this.l, this.Z(), this.l + this.u, this.q);

            try {
               y0 var12 = LarpClient.b().e().F();

               for (tm.r var14 : this.h) {
                  int var15 = var14.Y + 14 + (var14.Q ? var14.z() : 0);
                  if (var15 >= this.Z() && var14.Y <= this.q && var14.X.isEnabled()) {
                     gu.Z(var1, this.l, var14.Y, this.u, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  gu.Z(var1, this.l, var29 + (int)Math.round(this.j.u()), this.u, 14, ei.R(419430399, var30));
               }

               for (tm.r var34 : this.h) {
                  int var36 = var34.Y + 14 + (var34.Q ? var34.z() : 0);
                  if (var36 >= this.Z() && var34.Y <= this.q) {
                     boolean var16 = var34.X.isEnabled();
                     int var17 = var16 ? jx.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = uw.J(var34.X.getDisplayName(), this.u - 14);
                     uw.G(var1, var18, this.l + (this.u - uw.r(var18)) / 2, var34.Y + 3, var17);
                     if (var12.H(var34.X.getName()) && this.d != yc.e) {
                        gu.Z(var1, this.l + 3, var34.Y + 5, 3, 3, var16 ? var17 : ei.D(var4, 0.9));
                     }

                     if (var34.Q) {
                        int var19 = var34.z();
                        gu.Z(var1, this.l, var34.Y + 14, this.u, var19, 1342177280);
                        gu.Z(var1, this.l, var34.Y + 14, 1, var19, var4 | 0xFF000000);

                        for (lc var21 : var34.N) {
                           if (var21.I) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.method_44380();
            }

            int var31 = this.q - this.Z();
            if (this.I > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.I);
               int var35 = var31 - var33;
               int var37 = this.Z() + (int)Math.round(var35 * ((double)this.E / (this.I - var31)));
               gu.Z(var1, this.l + this.u - 2, var37, 2, var33, ei.D(var4, 0.8));
            }
         }
      }

      String v(int var1, int var2) {
         if (var2 < this.Z()) {
            return "Drag to move · Right click folds " + this.d.t;
         }

         for (tm.r var4 : this.h) {
            if (gu.X(var1, var2, this.l, var4.Y, this.u, 14)) {
               String var8 = var4.X.getDescription();
               return var8.isBlank() ? var4.X.getDisplayName() : var8;
            }

            if (var4.Q) {
               for (lc var7 : var4.N) {
                  if (var7.I && var7.contains(var1, var2)) {
                     return var7 instanceof l9 var5 ? var5.J() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean N(double var1, double var3, int var5) {
         if (var3 < this.Z()) {
            if (var5 == 0) {
               tm.this.W();
               tm.this.b = this;
               tm.this.a = (int)Math.round(var1) - this.l;
               tm.this.u = (int)Math.round(var3) - this.F;
            } else if (var5 == 1) {
               this.G = (boolean)(!this.G ? 1 : 0);
               tm.this.i(this);
               tm.this.L();
               c_.b(this.G ? c_.d.D : c_.d.g);
            }

            return true;
         } else if (this.G && !(var3 >= this.q)) {
            for (tm.r var7 : this.h) {
               if (gu.X(var1, var3, this.l, var7.Y, this.u, 14)) {
                  var7.A(var5);
                  return true;
               }

               if (var7.Q) {
                  for (lc var9 : var7.N) {
                     if (var9.I && var9.contains(var1, var3)) {
                        tm.this.C(var9);
                        if (var9.click(var1, var3, var5)) {
                           tm.this.y = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            tm.this.W();
            return true;
         } else {
            return true;
         }
      }
   }
}
