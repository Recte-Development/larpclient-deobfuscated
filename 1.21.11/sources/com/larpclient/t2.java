package com.larpclient;

import com.larpclient.module.AutoSellModule;
import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FakePayModule;
import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.HitSoundsModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.LARPModule;
import com.larpclient.module.MenuBackgroundModule;
import com.larpclient.module.Module;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.SkinChangerModule;
import java.util.HashMap;
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
import net.minecraft.class_332;

final class t2 extends t6 {
   private Module L;
   private String E = "";
   private String Eb = "";
   private String t = "";
   private String k = "";
   private ls V;
   private final Map<ls, com.larpclient.module.r.d> Q = new IdentityHashMap<>();
   private final Map<ls, tc.q> Y = new IdentityHashMap<>();
   private tc.b d;
   private Object u;
   private bs q;
   private int M;
   private int o;
   private final Set<String> g = new HashSet<>();
   private final Map<String, ls> J = new HashMap<>();
   private final Map<lc, BooleanSupplier> c = new IdentityHashMap<>();
   private String U;
   private le EQ;

   t2() {
      super("Module");
   }

   void T(Module var1) {
      if (this.L instanceof HitSoundsModule var2) {
         var2.a();
      }

      this.L = var1;
      this.g.clear();
      this.f = 0;
      if (var1 instanceof FakePayModule var4) {
         this.Eb = var4.r();
      }

      this.t = "";
      this.k = "";
      this.build();
   }

   void Q(bu var1, int var2, int var3) {
      this.M = var2;
      this.o = var3;
      this.L(var1);
   }

   @Override
   protected double revealAmount() {
      return 1.0;
   }

   @Override
   protected boolean renderPopupInline() {
      return false;
   }

   @Override
   protected boolean horizontalInput(double var1) {
      return var1 >= this.M && var1 < this.o;
   }

   void O() {
      int var1 = this.f;
      this.build();
      this.layout();
      this.f = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.o();
      this.J.clear();
      this.c.clear();
      this.Q.clear();
      this.V = null;
      this.EQ = null;
      this.Y.clear();
      this.d = null;
      if (this.L != null) {
         Module var1 = this.L;
         this.U = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.s().getValue() : null;
         this.v = var1.getDisplayName();
         this.EQ = new le("", var1);
         this.K.add(this.EQ);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof com.larpclient.module.g var12) {
            this.o(var12::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var13) {
            this.u(new t2.l(var13));
            ls var3 = this.s("Clear drawing", var13::M);
            var3.r = (boolean)1;
            this.c.put(var3, var13::s);
         }

         if (var1 instanceof AutoSellModule var14) {
            this.o(var14::a);
            this.s("Choose items to sell", () -> var14.a(this.X, false));
            this.s("Choose excluded items", () -> var14.a(this.X, true));
            this.s("Add held item to selection", () -> {
               try {
                  var14.B(false);
                  this.k = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.o(() -> this.k);
         }

         HashMap var15 = new HashMap();
         if (var1 instanceof tc var16) {
            tc.b var4 = var16.status();
            if (var4 != null) {
               for (tc.v var6 : var16.settingGroups()) {
                  var15.put(var6.title(), var6);
               }

               this.d = var4;
               this.x(() -> this.d.text(), () -> this.d.problem() ? -996986 : ez.H);
            } else {
               this.o(var16::summary);
               this.o(var16::detail);
               lc var30 = var16.preview();
               if (var30 != null) {
                  this.u(var30);
               }

               String var34 = null;

               for (tc.k var8 : var16.commands()) {
                  if (!var8.section().equals(var34)) {
                     var34 = var8.section();
                     this.u(new l5(var34));
                  }

                  this.s(var8.label(), () -> this.k = var8.run().get());
               }

               this.o(() -> this.k);
            }
         }

         if (var1 instanceof WaypointsModule var17) {
            if (this.q == null || this.q.S != var17) {
               this.q = new bs(var17, this);
            }

            this.q.O();
         } else {
            this.q = null;
         }

         if (var1 instanceof OrderDropperModule var18) {
            this.o(var18::T);
            this.s("Use held item", () -> {
               try {
                  var18.A();
                  this.k = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.o(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.s("Select shulkers", () -> {
               try {
                  var18.p();
                  this.k = "";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.s("Clear selected shulkers", () -> {
               try {
                  var18.J();
                  this.k = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.s("Rescan selected shulkers", () -> {
               try {
                  var18.r();
                  this.k = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.s("Reset run progress", () -> {
               try {
                  var18.Z();
                  this.k = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.k = var3x.getMessage();
               }
            });
            this.o(() -> this.k);
         }

         if (var1 instanceof com.larpclient.module.r var19) {
            this.u(new l5("Setup"));
            this.o(var19::setupSummary);
            this.o(var19::setupHint);

            for (com.larpclient.module.r.d var31 : var19.z()) {
               ls var35 = this.s(var31.currentLabel(), var31.run());
               this.Q.put(var35, var31);
            }
         }

         Iterator var20 = fl.T(var1).iterator();

         while (true) {
            fl.u var28;
            do {
               if (!var20.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.s("Open HUD editor", () -> this.X.R());
                  }

                  if (var1 instanceof SkinChangerModule var21) {
                     this.s("Apply skin", var21::G);
                     this.s("Refresh skins", () -> {
                        var21.v();
                        this.build();
                        this.layout();
                     });
                     this.s("Open skins folder", var21::M);
                     this.o(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var22) {
                     var22.X();
                     this.s("Refresh images", () -> {
                        var22.A();
                        this.build();
                        this.layout();
                     });
                     this.s("Open backgrounds folder", var22::u);
                     this.o(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var22.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var23) {
                     this.s("Preview sound", var23::b);
                  }

                  if (var1 instanceof LARPModule var24) {
                     lo var29 = new lo("Minecraft username", var1x -> this.E = var1x, 16);
                     var29.h = this.E;
                     this.u(var29);
                     this.V = new ls("Start larping", () -> {
                        if (!this.E.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           wk.C = "Enter a Minecraft username";
                        } else {
                           var24.setEnabled(true);
                           wk.d(this.E.trim());
                        }
                     });
                     this.u(this.V);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.u(new l5("Keybind"));
                     com.larpclient.module.lw var25 = new com.larpclient.module.lw(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var25.a(var1::setKeyBind);
                     this.u(new l9(var25));
                     if (var1.keybindMode() != null) {
                        this.u(new l9(var1.keybindMode()));
                     }
                  }

                  ls var26 = this.s("Reset module settings", () -> {
                     for (com.larpclient.module.lb var3x : var1.getSettings()) {
                        var3x.T();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.P();
                     }

                     if (var1 instanceof FakePayModule var5) {
                        this.Eb = var5.r();
                     }

                     this.t = "";
                     this.build();
                  });
                  var26.r = (boolean)1;
                  return;
               }

               var28 = (fl.u)var20.next();
               if (!var28.collapsed()) {
                  break;
               }

               String var32 = (this.g.contains(var28.title()) ? "Hide " : "Show ") + var28.title().toLowerCase(Locale.ROOT);
               ls var36 = this.s(var32, () -> {
                  boolean var3x = this.J.get(var28.title()).q;
                  if (var1 instanceof tc) {
                     this.u = null;
                  }

                  if (!this.g.remove(var28.title())) {
                     this.g.add(var28.title());
                  }

                  this.build();
                  this.layout();
                  if (var3x) {
                     this.H(this.J.get(var28.title()));
                  }
               });
               this.J.put(var28.title(), var36);
               if (!var15.isEmpty()) {
                  var36.r = (boolean)1;
               }

               this.c.put(var36, () -> var28.settings().stream().anyMatch(com.larpclient.module.lb::B));
            } while (!this.g.contains(var28.title()));

            tc.v var33 = (tc.v)var15.get(var28.title());
            tc.l var37 = var33 == null ? null : var33.setup();
            this.u(new l5(var28.title(), () -> var28.settings().stream().anyMatch(com.larpclient.module.lb::B), var28.chips()));
            if (var37 != null) {
               this.o(var37.value());
               Supplier var38 = var37.value();
               this.c.put(this.F.getLast(), () -> !((String)var38.get()).isBlank());
               if (!var37.settingsFirst()) {
                  this.v(var37);
               }
            }

            for (com.larpclient.module.lb var40 : var28.settings()) {
               if (var1 instanceof FakePayModule var9 && var40 == var9.Q()) {
                  this.u(new l5("Fake balance"));
                  lo var10 = new lo("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.Eb = var1x;
                     this.t = "";
                  }, 40);
                  var10.h = this.Eb;
                  Runnable var11 = () -> {
                     String var2x = var9.s(this.Eb);
                     this.t = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var10.T(var11);
                  this.u(var10);
                  this.s("Submit", var11);
                  this.o(() -> var9.j() + (this.t.isEmpty() ? "" : "\n" + this.t));
               } else if (!(var40 instanceof com.larpclient.module.lo var41 && var41.m())) {
                  if (var28.chips()) {
                     this.u(l9.g(var40));
                  } else {
                     this.u(new l9(var40));
                  }

                  if (var1 instanceof HitFXModule var42 && var40 == var42.h()) {
                     this.o(var42::K);
                  }

                  if (var1 instanceof KillFXModule var43 && var40 == var43.b()) {
                     this.o(var43::j);
                  }
               }
            }

            if (var37 != null && var37.settingsFirst()) {
               this.v(var37);
            }
         }
      }
   }

   @Override
   protected boolean componentShown(lc var1) {
      BooleanSupplier var2 = this.c.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   private void v(tc.l var1) {
      for (List var3 : var1.rows()) {
         this.q(var3);
      }

      lc var4 = var1.picture().get();
      if (var4 != null) {
         this.u(var4);
      }
   }

   private void q(List<tc.q> var1) {
      if (!var1.isEmpty()) {
         Object var2 = new Object();

         for (tc.q var4 : var1) {
            ls var5 = new ls(var4.label(), () -> {
               this.u = var2;
               this.k = var4.run().get();
            });
            var5.x = (boolean)1;
            var5.U = var2;
            var5.w = 24;
            var5.N = var4.available().getAsBoolean();
            this.Y.put(var5, var4);
            this.u(var5);
         }

         this.o(() -> this.u == var2 ? this.k : "");
         this.c.put(this.F.getLast(), () -> this.u == var2 && !this.k.isBlank());
      }
   }

   private void a(tc var1) {
      var1.panelTick();
      tc.b var2 = var1.status();
      if (var2 != null) {
         this.d = var2;
      }

      this.Y.forEach((var0, var1x) -> var0.N = var1x.available().getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.C + 12;
   }

   @Override
   protected int titleY() {
      return this.r.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.r.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(class_332 var1, int var2, int var3) {
      if (this.EQ != null) {
         this.EQ.render(var1, var2, var3);
      }

      gu.q(var1, this.C + 12, this.i - 5, this.A - this.C - 24, false, ez.o);
   }

   @Override
   protected void layout() {
      if (this.r != null) {
         this.C = this.r.columns() ? this.r.inspectorX() : this.r.contentX();
         this.A = this.r.right();
         this.i = this.bodyTop();
         this.l = this.r.bottom();
         if (this.EQ != null) {
            this.EQ.bounds(this.C + 8, this.r.bodyTop() + 21, Math.max(1, this.A - this.C - 20));
            this.EQ.w = 16;
         }

         this.q();
      }
   }

   @Override
   void suspend() {
      if (this.L instanceof HitSoundsModule var1) {
         var1.a();
      }

      super.suspend();
   }

   @Override
   public void method_25419() {
      if (this.L instanceof HitSoundsModule var1) {
         var1.a();
      }

      this.X.H();
   }

   public void method_25393() {
      if (this.L instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.U, var1.s().getValue())) {
         int var4 = this.G != null && this.G.q ? 1 : 0;
         this.O();
         if (var4 != 0) {
            this.F.stream().filter(var0 -> var0 instanceof l9).findFirst().ifPresent(this::H);
         }
      }

      this.Q.forEach((var0, var1x) -> var0.O = var1x.currentLabel());
      if (this.L instanceof tc var3) {
         this.a(var3);
      }

      if (this.q != null) {
         this.q.S();
      }

      if (this.V != null) {
         this.V.N = (boolean)(!wk.E() ? 1 : 0);
         this.V.O = this.V.N ? "Start larping" : "Starting...";
      }
   }

   private static final class l extends lc {
      private static final int z = 88;
      private static final int U = 6;
      private static final int O = 14;
      private static final int W = 15;
      private static final int Q = -14999514;
      private static final int E = -14275533;
      private static final int c = -13880517;
      private static final int J = -12958899;
      private final CustomCrosshairModule V;
      private boolean m;
      private boolean Y;
      private int j;
      private int e;

      l(CustomCrosshairModule var1) {
         this.V = var1;
         this.w = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.w = this.V.s() ? 12 + 15 * this.r() + 14 : 88;
         if (!this.V.s()) {
            this.release();
         }
      }

      private int B() {
         return Math.clamp(this.d / 3, 56, 96);
      }

      private int w() {
         return this.d - this.B() - 4;
      }

      private int r() {
         return Math.clamp((this.w() - 12) / 15, 3, 10);
      }

      private int S() {
         return this.D + (this.w() - 15 * this.r()) / 2;
      }

      private int u() {
         return this.g + 6;
      }

      private int Q(double var1) {
         return (int)Math.floor((var1 - this.S()) / this.r());
      }

      private int e(double var1) {
         return (int)Math.floor((var1 - this.u()) / this.r());
      }

      private static boolean Z(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         if (!this.V.s()) {
            int var18 = this.d / 2;
            this.e(var1, this.D, this.g, var18 - 4, this.w, true);
            this.e(var1, this.D + var18, this.g, var18 - 4, this.w, false);
         } else {
            int var4 = this.w();
            int var5 = this.r();
            int var6 = this.S();
            int var7 = this.u();
            int var8 = var5 >= 5 ? 1 : 0;
            com.larpclient.p.B(var1, this.D, this.g, var4, this.w, 5, -14999514);
            i_ var9 = this.V.o();
            int var10 = this.V.A();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.J(var13, var12)) {
                     gu.Z(var1, var14, var15, var5, var5, var10);
                  } else {
                     gu.Z(
                        var1,
                        var14,
                        var15,
                        var5 - var8,
                        var5 - var8,
                        var13 == var11 && var12 == var11 ? -12958899 : (var13 != var11 && var12 != var11 ? -14275533 : -13880517)
                     );
                  }
               }
            }

            int var19 = this.Q(var2);
            int var20 = this.e(var3);
            if (var2 >= 0 && Z(var19, var20)) {
               gu.a(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = uw.X("Left click draws, right click erases", Math.max(1, var4 - 12), uw.c.M);
            uw.X(var1, var21, this.D + 6, var7 + 15 * var5 + 4, -6643287, uw.c.M);
            int var22 = this.D + var4 + 4;
            int var16 = this.B() - 4;
            int var17 = (this.w - 4) / 2;
            this.e(var1, var22, this.g, var16, var17, true);
            this.e(var1, var22, this.g + var17 + 4, var16, this.w - var17 - 4, false);
         }
      }

      private void e(class_332 var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.X());
         com.larpclient.p.B(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         uw.G(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.method_51448().scale((float)var9, (float)var9);
            CustomCrosshairModule.J(var1, 0, 0);
         } finally {
            var1.method_51448().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.V.s() && var5 <= 1) {
            int var6 = this.Q(var1);
            int var7 = this.e(var3);
            if (!Z(var6, var7)) {
               return false;
            }

            this.Y = (boolean)(var5 == 0 && !this.V.o().J(var6, var7) ? 1 : 0);
            this.m = (boolean)1;
            this.j = var6;
            this.e = var7;
            this.V.k(var6, var7, this.Y);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.m && this.V.s()) {
            int var5 = this.Q(var1);
            int var6 = this.e(var3);
            int var7 = var5 - this.j;
            int var8 = var6 - this.e;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.j + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.e + (int)Math.round((double)var8 * var10 / var9);
               if (Z(var11, var12)) {
                  this.V.k(var11, var12, this.Y);
               }
            }

            this.j = var5;
            this.e = var6;
         }
      }

      @Override
      public void release() {
         if (this.m) {
            this.m = (boolean)0;
            q8.G("Crosshair", "Drawing edited: {} cells filled", this.V.o().n());
         }
      }
   }
}
