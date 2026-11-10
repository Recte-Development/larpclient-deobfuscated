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
import com.larpclient.module.kj;
import com.larpclient.module.kv;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class fu extends fp {
   private Module u;
   private String a = "";
   private String D = "";
   private String k5 = "";
   private String H = "";
   private s8 l;
   private final Map<s8, kj.o> t = new IdentityHashMap<>();
   private final Map<s8, ac.f> ka = new IdentityHashMap<>();
   private ac.u x;
   private Object S;
   private ik h;
   private int Q;
   private int O;
   private final Set<String> w = new HashSet<>();
   private final Map<String, s8> m = new HashMap<>();
   private final Map<j0, BooleanSupplier> Y = new IdentityHashMap<>();
   private String e;
   private j1 W;

   fu() {
      super("Module");
   }

   void o(Module var1) {
      if (this.u instanceof HitSoundsModule var2) {
         var2.t();
      }

      this.u = var1;
      this.w.clear();
      this.L = 0;
      if (var1 instanceof FakePayModule var4) {
         this.D = var4.a();
      }

      this.k5 = "";
      this.H = "";
      this.build();
   }

   void M(h_ var1, int var2, int var3) {
      this.Q = var2;
      this.O = var3;
      this.u(var1);
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
      return var1 >= this.Q && var1 < this.O;
   }

   void z() {
      int var1 = this.L;
      this.build();
      this.layout();
      this.L = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.D();
      this.m.clear();
      this.Y.clear();
      this.t.clear();
      this.l = null;
      this.W = null;
      this.ka.clear();
      this.x = null;
      if (this.u != null) {
         Module var1 = this.u;
         this.e = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.l().getValue() : null;
         this.j = var1.getDisplayName();
         this.W = new j1("", var1);
         this.J.add(this.W);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof kv var12) {
            this.n(var12::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var13) {
            this.f(new fu.i(var13));
            s8 var3 = this.t("Clear drawing", var13::F);
            var3.D = (boolean)1;
            this.Y.put(var3, var13::P);
         }

         if (var1 instanceof AutoSellModule var14) {
            this.n(var14::m);
            this.t("Choose items to sell", () -> var14.b(this.T, false));
            this.t("Choose excluded items", () -> var14.b(this.T, true));
            this.t("Add held item to selection", () -> {
               try {
                  var14.z(false);
                  this.H = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.n(() -> this.H);
         }

         HashMap var15 = new HashMap();
         if (var1 instanceof ac var16) {
            ac.u var4 = var16.status();
            if (var4 != null) {
               for (ac.r var6 : var16.settingGroups()) {
                  var15.put(var6.title(), var6);
               }

               this.x = var4;
               this.z(() -> this.x.text(), () -> this.x.problem() ? -996986 : com.larpclient.c.H);
            } else {
               this.n(var16::summary);
               this.n(var16::detail);
               j0 var30 = var16.preview();
               if (var30 != null) {
                  this.f(var30);
               }

               String var34 = null;

               for (ac.b var8 : var16.commands()) {
                  if (!var8.section().equals(var34)) {
                     var34 = var8.section();
                     this.f(new sj(var34));
                  }

                  this.t(var8.label(), () -> this.H = var8.run().get());
               }

               this.n(() -> this.H);
            }
         }

         if (var1 instanceof WaypointsModule var17) {
            if (this.h == null || this.h.Z != var17) {
               this.h = new ik(var17, this);
            }

            this.h.Q();
         } else {
            this.h = null;
         }

         if (var1 instanceof OrderDropperModule var18) {
            this.n(var18::S);
            this.t("Use held item", () -> {
               try {
                  var18.V();
                  this.H = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.n(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.t("Select shulkers", () -> {
               try {
                  var18.r();
                  this.H = "";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.t("Clear selected shulkers", () -> {
               try {
                  var18.j();
                  this.H = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.t("Rescan selected shulkers", () -> {
               try {
                  var18.C();
                  this.H = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.t("Reset run progress", () -> {
               try {
                  var18.A();
                  this.H = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.H = var3x.getMessage();
               }
            });
            this.n(() -> this.H);
         }

         if (var1 instanceof kj var19) {
            this.f(new sj("Setup"));
            this.n(var19::setupSummary);
            this.n(var19::setupHint);

            for (kj.o var31 : var19.q()) {
               s8 var35 = this.t(var31.currentLabel(), var31.run());
               this.t.put(var35, var31);
            }
         }

         Iterator var20 = ag.G(var1).iterator();

         while (true) {
            ag.g var28;
            do {
               if (!var20.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.t("Open HUD editor", () -> this.T.j());
                  }

                  if (var1 instanceof SkinChangerModule var21) {
                     this.t("Apply skin", var21::l);
                     this.t("Refresh skins", () -> {
                        var21.h();
                        this.build();
                        this.layout();
                     });
                     this.t("Open skins folder", var21::Z);
                     this.n(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var22) {
                     var22.J();
                     this.t("Refresh images", () -> {
                        var22.x();
                        this.build();
                        this.layout();
                     });
                     this.t("Open backgrounds folder", var22::s);
                     this.n(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var22.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var23) {
                     this.t("Preview sound", var23::I);
                  }

                  if (var1 instanceof LARPModule var24) {
                     sk var29 = new sk("Minecraft username", var1x -> this.a = var1x, 16);
                     var29.j = this.a;
                     this.f(var29);
                     this.l = new s8("Start larping", () -> {
                        if (!this.a.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           be.k = "Enter a Minecraft username";
                        } else {
                           var24.setEnabled(true);
                           be.z(this.a.trim());
                        }
                     });
                     this.f(this.l);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.f(new sj("Keybind"));
                     com.larpclient.module.x var25 = new com.larpclient.module.x(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var25.w(var1::setKeyBind);
                     this.f(new su(var25));
                     if (var1.keybindMode() != null) {
                        this.f(new su(var1.keybindMode()));
                     }
                  }

                  s8 var26 = this.t("Reset module settings", () -> {
                     for (com.larpclient.module.r var3x : var1.getSettings()) {
                        var3x.u();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.A();
                     }

                     if (var1 instanceof FakePayModule var5) {
                        this.D = var5.a();
                     }

                     this.k5 = "";
                     this.build();
                  });
                  var26.D = (boolean)1;
                  return;
               }

               var28 = (ag.g)var20.next();
               if (!var28.collapsed()) {
                  break;
               }

               String var32 = (this.w.contains(var28.title()) ? "Hide " : "Show ") + var28.title().toLowerCase(Locale.ROOT);
               s8 var36 = this.t(var32, () -> {
                  boolean var3x = this.m.get(var28.title()).X;
                  if (var1 instanceof ac) {
                     this.S = null;
                  }

                  if (!this.w.remove(var28.title())) {
                     this.w.add(var28.title());
                  }

                  this.build();
                  this.layout();
                  if (var3x) {
                     this.h(this.m.get(var28.title()));
                  }
               });
               this.m.put(var28.title(), var36);
               if (!var15.isEmpty()) {
                  var36.D = (boolean)1;
               }

               this.Y.put(var36, () -> var28.settings().stream().anyMatch(com.larpclient.module.r::j));
            } while (!this.w.contains(var28.title()));

            ac.r var33 = (ac.r)var15.get(var28.title());
            ac.m var37 = var33 == null ? null : var33.setup();
            this.f(new sj(var28.title(), () -> var28.settings().stream().anyMatch(com.larpclient.module.r::j), var28.chips()));
            if (var37 != null) {
               this.n(var37.value());
               Supplier var38 = var37.value();
               this.Y.put(this.V.getLast(), () -> !((String)var38.get()).isBlank());
               if (!var37.settingsFirst()) {
                  this.Z(var37);
               }
            }

            for (com.larpclient.module.r var40 : var28.settings()) {
               if (var1 instanceof FakePayModule var9 && var40 == var9.O()) {
                  this.f(new sj("Fake balance"));
                  sk var10 = new sk("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.D = var1x;
                     this.k5 = "";
                  }, 40);
                  var10.j = this.D;
                  Runnable var11 = () -> {
                     String var2x = var9.t(this.D);
                     this.k5 = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var10.O(var11);
                  this.f(var10);
                  this.t("Submit", var11);
                  this.n(() -> var9.R() + (this.k5.isEmpty() ? "" : "\n" + this.k5));
               } else if (!(var40 instanceof com.larpclient.module.b var41 && var41.Y())) {
                  if (var28.chips()) {
                     this.f(su.t(var40));
                  } else {
                     this.f(new su(var40));
                  }

                  if (var1 instanceof HitFXModule var42 && var40 == var42.G()) {
                     this.n(var42::J);
                  }

                  if (var1 instanceof KillFXModule var43 && var40 == var43.h()) {
                     this.n(var43::u);
                  }
               }
            }

            if (var37 != null && var37.settingsFirst()) {
               this.Z(var37);
            }
         }
      }
   }

   @Override
   protected boolean componentShown(j0 var1) {
      BooleanSupplier var2 = this.Y.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   private void Z(ac.m var1) {
      for (List var3 : var1.rows()) {
         this.D(var3);
      }

      j0 var4 = var1.picture().get();
      if (var4 != null) {
         this.f(var4);
      }
   }

   private void D(List<ac.f> var1) {
      if (!var1.isEmpty()) {
         Object var2 = new Object();

         for (ac.f var4 : var1) {
            s8 var5 = new s8(var4.label(), () -> {
               this.S = var2;
               this.H = var4.run().get();
            });
            var5.Z = (boolean)1;
            var5.l = var2;
            var5.O = 24;
            var5.V = var4.available().getAsBoolean();
            this.ka.put(var5, var4);
            this.f(var5);
         }

         this.n(() -> this.S == var2 ? this.H : "");
         this.Y.put(this.V.getLast(), () -> this.S == var2 && !this.H.isBlank());
      }
   }

   private void I(ac var1) {
      var1.panelTick();
      ac.u var2 = var1.status();
      if (var2 != null) {
         this.x = var2;
      }

      this.ka.forEach((var0, var1x) -> var0.V = var1x.available().getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.E + 12;
   }

   @Override
   protected int titleY() {
      return this.G.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.G.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.W != null) {
         this.W.render(var1, var2, var3);
      }

      h5.T(var1, this.E + 12, this.c - 5, this.p - this.E - 24, false, com.larpclient.c.Xn);
   }

   @Override
   protected void layout() {
      if (this.G != null) {
         this.E = this.G.columns() ? this.G.inspectorX() : this.G.contentX();
         this.p = this.G.right();
         this.c = this.bodyTop();
         this.r = this.G.bottom();
         if (this.W != null) {
            this.W.bounds(this.E + 8, this.G.bodyTop() + 21, Math.max(1, this.p - this.E - 20));
            this.W.O = 16;
         }

         this.T();
      }
   }

   @Override
   void suspend() {
      if (this.u instanceof HitSoundsModule var1) {
         var1.t();
      }

      super.suspend();
   }

   @Override
   public void onClose() {
      if (this.u instanceof HitSoundsModule var1) {
         var1.t();
      }

      this.T.E();
   }

   public void tick() {
      if (this.u instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.e, var1.l().getValue())) {
         int var4 = this.q != null && this.q.X ? 1 : 0;
         this.z();
         if (var4 != 0) {
            this.V.stream().filter(var0 -> var0 instanceof su).findFirst().ifPresent(this::h);
         }
      }

      this.t.forEach((var0, var1x) -> var0.W = var1x.currentLabel());
      if (this.u instanceof ac var3) {
         this.I(var3);
      }

      if (this.h != null) {
         this.h.s();
      }

      if (this.l != null) {
         this.l.V = (boolean)(!be.T() ? 1 : 0);
         this.l.W = this.l.V ? "Start larping" : "Starting...";
      }
   }

   private static final class i extends j0 {
      private static final int e = 88;
      private static final int y = 6;
      private static final int S = 14;
      private static final int p = 15;
      private static final int k = -14999514;
      private static final int E = -14275533;
      private static final int A = -13880517;
      private static final int h = -12958899;
      private final CustomCrosshairModule N;
      private boolean a;
      private boolean t;
      private int i;
      private int H;

      i(CustomCrosshairModule var1) {
         this.N = var1;
         this.O = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.O = this.N.P() ? 12 + 15 * this.a() + 14 : 88;
         if (!this.N.P()) {
            this.release();
         }
      }

      private int R() {
         return Math.clamp(this.L / 3, 56, 96);
      }

      private int T() {
         return this.L - this.R() - 4;
      }

      private int a() {
         return Math.clamp((this.T() - 12) / 15, 3, 10);
      }

      private int w() {
         return this.P + (this.T() - 15 * this.a()) / 2;
      }

      private int S() {
         return this.T + 6;
      }

      private int X(double var1) {
         return (int)Math.floor((var1 - this.w()) / this.a());
      }

      private int U(double var1) {
         return (int)Math.floor((var1 - this.S()) / this.a());
      }

      private static boolean V(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (!this.N.P()) {
            int var18 = this.L / 2;
            this.F(var1, this.P, this.T, var18 - 4, this.O, true);
            this.F(var1, this.P + var18, this.T, var18 - 4, this.O, false);
         } else {
            int var4 = this.T();
            int var5 = this.a();
            int var6 = this.w();
            int var7 = this.S();
            int var8 = var5 >= 5 ? 1 : 0;
            wc.b(var1, this.P, this.T, var4, this.O, 5, -14999514);
            qj var9 = this.N.T();
            int var10 = this.N.e();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.k(var13, var12)) {
                     h5.N(var1, var14, var15, var5, var5, var10);
                  } else {
                     h5.N(
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

            int var19 = this.X(var2);
            int var20 = this.U(var3);
            if (var2 >= 0 && V(var19, var20)) {
               h5.L(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = ic.o("Left click draws, right click erases", Math.max(1, var4 - 12), ic.n.I);
            ic.N(var1, var21, this.P + 6, var7 + 15 * var5 + 4, -6643287, ic.n.I);
            int var22 = this.P + var4 + 4;
            int var16 = this.R() - 4;
            int var17 = (this.O - 4) / 2;
            this.F(var1, var22, this.T, var16, var17, true);
            this.F(var1, var22, this.T + var17 + 4, var16, this.O - var17 - 4, false);
         }
      }

      private void F(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.X());
         wc.b(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         ic.N(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.pose().scale((float)var9, (float)var9);
            CustomCrosshairModule.U(var1, 0, 0);
         } finally {
            var1.pose().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.N.P() && var5 <= 1) {
            int var6 = this.X(var1);
            int var7 = this.U(var3);
            if (!V(var6, var7)) {
               return false;
            }

            this.t = (boolean)(var5 == 0 && !this.N.T().k(var6, var7) ? 1 : 0);
            this.a = (boolean)1;
            this.i = var6;
            this.H = var7;
            this.N.R(var6, var7, this.t);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.a && this.N.P()) {
            int var5 = this.X(var1);
            int var6 = this.U(var3);
            int var7 = var5 - this.i;
            int var8 = var6 - this.H;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.i + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.H + (int)Math.round((double)var8 * var10 / var9);
               if (V(var11, var12)) {
                  this.N.R(var11, var12, this.t);
               }
            }

            this.i = var5;
            this.H = var6;
         }
      }

      @Override
      public void release() {
         if (this.a) {
            this.a = (boolean)0;
            _q.z("Crosshair", "Drawing edited: {} cells filled", this.N.T().n());
         }
      }
   }
}
