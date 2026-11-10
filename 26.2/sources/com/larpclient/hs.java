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
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class hs extends h6 {
   private Module n;
   private String Kv = "";
   private String c = "";
   private String A = "";
   private String B = "";
   private jx K;
   private final Map<jx, com.larpclient.module.to.b> X = new IdentityHashMap<>();
   private final Map<jx, s1.c> v = new IdentityHashMap<>();
   private s1.o Q;
   private Object T;
   private zg Z;
   private int e;
   private int D;
   private final Set<String> t = new HashSet<>();
   private final Map<String, jx> i = new HashMap<>();
   private final Map<jd, BooleanSupplier> Kf = new IdentityHashMap<>();
   private String f;
   private jg P;

   hs() {
      super("Module");
   }

   void v(Module var1) {
      if (this.n instanceof HitSoundsModule var2) {
         var2.z();
      }

      this.n = var1;
      this.t.clear();
      this.Y = 0;
      if (var1 instanceof FakePayModule var4) {
         this.c = var4.I();
      }

      this.A = "";
      this.B = "";
      this.build();
   }

   void O(uc var1, int var2, int var3) {
      this.e = var2;
      this.D = var3;
      this.x(var1);
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
      return var1 >= this.e && var1 < this.D;
   }

   void j() {
      int var1 = this.Y;
      this.build();
      this.layout();
      this.Y = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.v();
      this.i.clear();
      this.Kf.clear();
      this.X.clear();
      this.K = null;
      this.P = null;
      this.v.clear();
      this.Q = null;
      if (this.n != null) {
         Module var1 = this.n;
         this.f = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.a().getValue() : null;
         this.m = var1.getDisplayName();
         this.P = new jg("", var1);
         this.p.add(this.P);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof com.larpclient.module.tb var12) {
            this.Y(var12::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var13) {
            this.h(new hs.r(var13));
            jx var3 = this.a("Clear drawing", var13::W);
            var3.y = (boolean)1;
            this.Kf.put(var3, var13::I);
         }

         if (var1 instanceof AutoSellModule var14) {
            this.Y(var14::I);
            this.a("Choose items to sell", () -> var14.K(this.O, false));
            this.a("Choose excluded items", () -> var14.K(this.O, true));
            this.a("Add held item to selection", () -> {
               try {
                  var14.O(false);
                  this.B = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.Y(() -> this.B);
         }

         HashMap var15 = new HashMap();
         if (var1 instanceof s1 var16) {
            s1.o var4 = var16.status();
            if (var4 != null) {
               for (s1.w var6 : var16.settingGroups()) {
                  var15.put(var6.title(), var6);
               }

               this.Q = var4;
               this.F(() -> this.Q.text(), () -> this.Q.problem() ? -996986 : ho.u);
            } else {
               this.Y(var16::summary);
               this.Y(var16::detail);
               jd var30 = var16.preview();
               if (var30 != null) {
                  this.h(var30);
               }

               String var34 = null;

               for (s1.a var8 : var16.commands()) {
                  if (!var8.section().equals(var34)) {
                     var34 = var8.section();
                     this.h(new j1(var34));
                  }

                  this.a(var8.label(), () -> this.B = var8.run().get());
               }

               this.Y(() -> this.B);
            }
         }

         if (var1 instanceof WaypointsModule var17) {
            if (this.Z == null || this.Z.R != var17) {
               this.Z = new zg(var17, this);
            }

            this.Z.O();
         } else {
            this.Z = null;
         }

         if (var1 instanceof OrderDropperModule var18) {
            this.Y(var18::c);
            this.a("Use held item", () -> {
               try {
                  var18.Z();
                  this.B = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.Y(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.a("Select shulkers", () -> {
               try {
                  var18.w();
                  this.B = "";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.a("Clear selected shulkers", () -> {
               try {
                  var18.h();
                  this.B = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.a("Rescan selected shulkers", () -> {
               try {
                  var18.m();
                  this.B = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.a("Reset run progress", () -> {
               try {
                  var18.t();
                  this.B = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.B = var3x.getMessage();
               }
            });
            this.Y(() -> this.B);
         }

         if (var1 instanceof com.larpclient.module.to var19) {
            this.h(new j1("Setup"));
            this.Y(var19::setupSummary);
            this.Y(var19::setupHint);

            for (com.larpclient.module.to.b var31 : var19.i()) {
               jx var35 = this.a(var31.currentLabel(), var31.run());
               this.X.put(var35, var31);
            }
         }

         Iterator var20 = pa.I(var1).iterator();

         while (true) {
            pa.o var28;
            do {
               if (!var20.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.a("Open HUD editor", () -> this.O.T());
                  }

                  if (var1 instanceof SkinChangerModule var21) {
                     this.a("Apply skin", var21::F);
                     this.a("Refresh skins", () -> {
                        var21.r();
                        this.build();
                        this.layout();
                     });
                     this.a("Open skins folder", var21::J);
                     this.Y(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var22) {
                     var22.y();
                     this.a("Refresh images", () -> {
                        var22.Z();
                        this.build();
                        this.layout();
                     });
                     this.a("Open backgrounds folder", var22::A);
                     this.Y(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var22.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var23) {
                     this.a("Preview sound", var23::x);
                  }

                  if (var1 instanceof LARPModule var24) {
                     j9 var29 = new j9("Minecraft username", var1x -> this.Kv = var1x, 16);
                     var29.H = this.Kv;
                     this.h(var29);
                     this.K = new jx("Start larping", () -> {
                        if (!this.Kv.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           k9.R = "Enter a Minecraft username";
                        } else {
                           var24.setEnabled(true);
                           k9.t(this.Kv.trim());
                        }
                     });
                     this.h(this.K);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.h(new j1("Keybind"));
                     com.larpclient.module.v var25 = new com.larpclient.module.v(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var25.o(var1::setKeyBind);
                     this.h(new ju(var25));
                     if (var1.keybindMode() != null) {
                        this.h(new ju(var1.keybindMode()));
                     }
                  }

                  jx var26 = this.a("Reset module settings", () -> {
                     for (com.larpclient.module.q var3x : var1.getSettings()) {
                        var3x.u();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.s();
                     }

                     if (var1 instanceof FakePayModule var5) {
                        this.c = var5.I();
                     }

                     this.A = "";
                     this.build();
                  });
                  var26.y = (boolean)1;
                  return;
               }

               var28 = (pa.o)var20.next();
               if (!var28.collapsed()) {
                  break;
               }

               String var32 = (this.t.contains(var28.title()) ? "Hide " : "Show ") + var28.title().toLowerCase(Locale.ROOT);
               jx var36 = this.a(var32, () -> {
                  boolean var3x = this.i.get(var28.title()).W;
                  if (var1 instanceof s1) {
                     this.T = null;
                  }

                  if (!this.t.remove(var28.title())) {
                     this.t.add(var28.title());
                  }

                  this.build();
                  this.layout();
                  if (var3x) {
                     this.q(this.i.get(var28.title()));
                  }
               });
               this.i.put(var28.title(), var36);
               if (!var15.isEmpty()) {
                  var36.y = (boolean)1;
               }

               this.Kf.put(var36, () -> var28.settings().stream().anyMatch(com.larpclient.module.q::W));
            } while (!this.t.contains(var28.title()));

            s1.w var33 = (s1.w)var15.get(var28.title());
            s1.r var37 = var33 == null ? null : var33.setup();
            this.h(new j1(var28.title(), () -> var28.settings().stream().anyMatch(com.larpclient.module.q::W), var28.chips()));
            if (var37 != null) {
               this.Y(var37.value());
               Supplier var38 = var37.value();
               this.Kf.put(this.x.getLast(), () -> !((String)var38.get()).isBlank());
               if (!var37.settingsFirst()) {
                  this.w(var37);
               }
            }

            for (com.larpclient.module.q var40 : var28.settings()) {
               if (var1 instanceof FakePayModule var9 && var40 == var9.h()) {
                  this.h(new j1("Fake balance"));
                  j9 var10 = new j9("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.c = var1x;
                     this.A = "";
                  }, 40);
                  var10.H = this.c;
                  Runnable var11 = () -> {
                     String var2x = var9.y(this.c);
                     this.A = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var10.B(var11);
                  this.h(var10);
                  this.a("Submit", var11);
                  this.Y(() -> var9.w() + (this.A.isEmpty() ? "" : "\n" + this.A));
               } else if (!(var40 instanceof com.larpclient.module.e var41 && var41.N())) {
                  if (var28.chips()) {
                     this.h(ju.v(var40));
                  } else {
                     this.h(new ju(var40));
                  }

                  if (var1 instanceof HitFXModule var42 && var40 == var42.Z()) {
                     this.Y(var42::z);
                  }

                  if (var1 instanceof KillFXModule var43 && var40 == var43.j()) {
                     this.Y(var43::Z);
                  }
               }
            }

            if (var37 != null && var37.settingsFirst()) {
               this.w(var37);
            }
         }
      }
   }

   @Override
   protected boolean componentShown(jd var1) {
      BooleanSupplier var2 = this.Kf.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   private void w(s1.r var1) {
      for (List var3 : var1.rows()) {
         this.b(var3);
      }

      jd var4 = var1.picture().get();
      if (var4 != null) {
         this.h(var4);
      }
   }

   private void b(List<s1.c> var1) {
      if (!var1.isEmpty()) {
         Object var2 = new Object();

         for (s1.c var4 : var1) {
            jx var5 = new jx(var4.label(), () -> {
               this.T = var2;
               this.B = var4.run().get();
            });
            var5.G = (boolean)1;
            var5.r = var2;
            var5.J = 24;
            var5.p = var4.available().getAsBoolean();
            this.v.put(var5, var4);
            this.h(var5);
         }

         this.Y(() -> this.T == var2 ? this.B : "");
         this.Kf.put(this.x.getLast(), () -> this.T == var2 && !this.B.isBlank());
      }
   }

   private void s(s1 var1) {
      var1.panelTick();
      s1.o var2 = var1.status();
      if (var2 != null) {
         this.Q = var2;
      }

      this.v.forEach((var0, var1x) -> var0.p = var1x.available().getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.L + 12;
   }

   @Override
   protected int titleY() {
      return this.y.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.y.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.P != null) {
         this.P.render(var1, var2, var3);
      }

      kv.Y(var1, this.L + 12, this.R - 5, this.l - this.L - 24, false, ho.Y);
   }

   @Override
   protected void layout() {
      if (this.y != null) {
         this.L = this.y.columns() ? this.y.inspectorX() : this.y.contentX();
         this.l = this.y.right();
         this.R = this.bodyTop();
         this.h = this.y.bottom();
         if (this.P != null) {
            this.P.bounds(this.L + 8, this.y.bodyTop() + 21, Math.max(1, this.l - this.L - 20));
            this.P.J = 16;
         }

         this.N();
      }
   }

   @Override
   void suspend() {
      if (this.n instanceof HitSoundsModule var1) {
         var1.z();
      }

      super.suspend();
   }

   @Override
   public void onClose() {
      if (this.n instanceof HitSoundsModule var1) {
         var1.z();
      }

      this.O.d();
   }

   public void tick() {
      if (this.n instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.f, var1.a().getValue())) {
         int var4 = this.o != null && this.o.W ? 1 : 0;
         this.j();
         if (var4 != 0) {
            this.x.stream().filter(var0 -> var0 instanceof ju).findFirst().ifPresent(this::q);
         }
      }

      this.X.forEach((var0, var1x) -> var0.K = var1x.currentLabel());
      if (this.n instanceof s1 var3) {
         this.s(var3);
      }

      if (this.Z != null) {
         this.Z.D();
      }

      if (this.K != null) {
         this.K.p = (boolean)(!k9.b() ? 1 : 0);
         this.K.K = this.K.p ? "Start larping" : "Starting...";
      }
   }

   private static final class r extends jd {
      private static final int w = 88;
      private static final int x = 6;
      private static final int j = 14;
      private static final int U = 15;
      private static final int D = -14999514;
      private static final int R = -14275533;
      private static final int N = -13880517;
      private static final int g = -12958899;
      private final CustomCrosshairModule c;
      private boolean m;
      private boolean z;
      private int H;
      private int r;

      r(CustomCrosshairModule var1) {
         this.c = var1;
         this.J = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.J = this.c.I() ? 12 + 15 * this.D() + 14 : 88;
         if (!this.c.I()) {
            this.release();
         }
      }

      private int K() {
         return Math.clamp(this.A / 3, 56, 96);
      }

      private int T() {
         return this.A - this.K() - 4;
      }

      private int D() {
         return Math.clamp((this.T() - 12) / 15, 3, 10);
      }

      private int n() {
         return this.S + (this.T() - 15 * this.D()) / 2;
      }

      private int k() {
         return this.u + 6;
      }

      private int a(double var1) {
         return (int)Math.floor((var1 - this.n()) / this.D());
      }

      private int S(double var1) {
         return (int)Math.floor((var1 - this.k()) / this.D());
      }

      private static boolean x(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (!this.c.I()) {
            int var18 = this.A / 2;
            this.y(var1, this.S, this.u, var18 - 4, this.J, true);
            this.y(var1, this.S + var18, this.u, var18 - 4, this.J, false);
         } else {
            int var4 = this.T();
            int var5 = this.D();
            int var6 = this.n();
            int var7 = this.k();
            int var8 = var5 >= 5 ? 1 : 0;
            wp.I(var1, this.S, this.u, var4, this.J, 5, -14999514);
            kc var9 = this.c.l();
            int var10 = this.c.m();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.K(var13, var12)) {
                     kv.d(var1, var14, var15, var5, var5, var10);
                  } else {
                     kv.d(
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

            int var19 = this.a(var2);
            int var20 = this.S(var3);
            if (var2 >= 0 && x(var19, var20)) {
               kv.I(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = bj.i("Left click draws, right click erases", Math.max(1, var4 - 12), bj.u.M);
            bj.k(var1, var21, this.S + 6, var7 + 15 * var5 + 4, -6643287, bj.u.M);
            int var22 = this.S + var4 + 4;
            int var16 = this.K() - 4;
            int var17 = (this.J - 4) / 2;
            this.y(var1, var22, this.u, var16, var17, true);
            this.y(var1, var22, this.u + var17 + 4, var16, this.J - var17 - 4, false);
         }
      }

      private void y(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.q());
         wp.I(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         bj.E(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.pose().scale((float)var9, (float)var9);
            CustomCrosshairModule.b(var1, 0, 0);
         } finally {
            var1.pose().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.c.I() && var5 <= 1) {
            int var6 = this.a(var1);
            int var7 = this.S(var3);
            if (!x(var6, var7)) {
               return false;
            }

            this.z = (boolean)(var5 == 0 && !this.c.l().K(var6, var7) ? 1 : 0);
            this.m = (boolean)1;
            this.H = var6;
            this.r = var7;
            this.c.H(var6, var7, this.z);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.m && this.c.I()) {
            int var5 = this.a(var1);
            int var6 = this.S(var3);
            int var7 = var5 - this.H;
            int var8 = var6 - this.r;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.H + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.r + (int)Math.round((double)var8 * var10 / var9);
               if (x(var11, var12)) {
                  this.c.H(var11, var12, this.z);
               }
            }

            this.H = var5;
            this.r = var6;
         }
      }

      @Override
      public void release() {
         if (this.m) {
            this.m = (boolean)0;
            com.larpclient.h.A("Crosshair", "Drawing edited: {} cells filled", this.c.l().i());
         }
      }
   }
}
