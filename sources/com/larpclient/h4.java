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
import com.larpclient.module.df;
import com.larpclient.module.dv;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class h4 extends hb {
   private Module k;
   private String S = "";
   private String J = "";
   private String H = "";
   private String l = "";
   private u7 o;
   private final Map<u7, com.larpclient.module.v.n> U = new IdentityHashMap<>();
   private int B;
   private int A;
   private final Set<String> P = new HashSet<>();
   private final Map<String, u7> h = new HashMap<>();
   private final Map<com.larpclient.b, BooleanSupplier> D = new IdentityHashMap<>();
   private String x;
   private l O;

   h4() {
      super("Module");
   }

   void l(Module var1) {
      if (this.k instanceof HitSoundsModule var2) {
         var2.d();
      }

      this.k = var1;
      this.P.clear();
      this.Z = 0;
      if (var1 instanceof FakePayModule var4) {
         this.J = var4.C();
      }

      this.H = "";
      this.l = "";
      this.build();
   }

   void J(mp var1, int var2, int var3) {
      this.B = var2;
      this.A = var3;
      this.w(var1);
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
      return var1 >= this.B && var1 < this.A;
   }

   void G() {
      int var1 = this.Z;
      this.build();
      this.layout();
      this.Z = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.d();
      this.h.clear();
      this.D.clear();
      this.U.clear();
      this.o = null;
      this.O = null;
      if (this.k != null) {
         Module var1 = this.k;
         this.x = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.q().P() : null;
         this.F = var1.getDisplayName();
         this.O = new l("", var1);
         this.G.add(this.O);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof com.larpclient.module.f var9) {
            this.Y(var9::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var10) {
            this.w(new h4.b(var10));
            u7 var3 = this.U("Clear drawing", var10::r);
            var3.g = (boolean)1;
            this.D.put(var3, var10::n);
         }

         if (var1 instanceof AutoSellModule var11) {
            this.Y(var11::h);
            this.U("Choose items to sell", () -> var11.N(this.M, false));
            this.U("Choose excluded items", () -> var11.N(this.M, true));
            this.U("Add held item to selection", () -> {
               try {
                  var11.j(false);
                  this.l = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.Y(() -> this.l);
         }

         if (var1 instanceof OrderDropperModule var12) {
            this.Y(var12::u);
            this.U("Use held item", () -> {
               try {
                  var12.f();
                  this.l = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.Y(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.U("Select shulkers", () -> {
               try {
                  var12.C();
                  this.l = "";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.U("Clear selected shulkers", () -> {
               try {
                  var12.E();
                  this.l = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.U("Rescan selected shulkers", () -> {
               try {
                  var12.i();
                  this.l = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.U("Reset run progress", () -> {
               try {
                  var12.x();
                  this.l = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.l = var3x.getMessage();
               }
            });
            this.Y(() -> this.l);
         }

         if (var1 instanceof com.larpclient.module.v var13) {
            this.w(new us("Setup"));
            this.Y(var13::setupSummary);
            this.Y(var13::setupHint);

            for (com.larpclient.module.v.n var4 : var13.H()) {
               u7 var5 = this.U(var4.currentLabel(), var4.run());
               this.U.put(var5, var4);
            }
         }

         Iterator var14 = ok.t(var1).iterator();

         while (true) {
            ok.m var22;
            do {
               if (!var14.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.U("Open HUD editor", () -> this.M.Z());
                  }

                  if (var1 instanceof SkinChangerModule var15) {
                     this.U("Apply skin", var15::r);
                     this.U("Refresh skins", () -> {
                        var15.T();
                        this.build();
                        this.layout();
                     });
                     this.U("Open skins folder", var15::A);
                     this.Y(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var16) {
                     var16.f();
                     this.U("Refresh images", () -> {
                        var16.N();
                        this.build();
                        this.layout();
                     });
                     this.U("Open backgrounds folder", var16::A);
                     this.Y(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var16.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var17) {
                     this.U("Preview sound", var17::h);
                  }

                  if (var1 instanceof LARPModule var18) {
                     ur var23 = new ur("Minecraft username", var1x -> this.S = var1x, 16);
                     var23.d = this.S;
                     this.w(var23);
                     this.o = new u7("Start larping", () -> {
                        if (!this.S.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           du.F = "Enter a Minecraft username";
                        } else {
                           var18.setEnabled(true);
                           du.t(this.S.trim());
                        }
                     });
                     this.w(this.o);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.w(new us("Keybind"));
                     dv var19 = new dv(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var19.h(var1::setKeyBind);
                     this.w(new un(var19));
                     if (var1.keybindMode() != null) {
                        this.w(new un(var1.keybindMode()));
                     }
                  }

                  u7 var20 = this.U("Reset module settings", () -> {
                     for (df var3x : var1.getSettings()) {
                        var3x.B();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.V();
                     }

                     if (var1 instanceof FakePayModule var5x) {
                        this.J = var5x.C();
                     }

                     this.H = "";
                     this.build();
                  });
                  var20.g = (boolean)1;
                  return;
               }

               var22 = (ok.m)var14.next();
               if (!var22.collapsed()) {
                  break;
               }

               String var24 = (this.P.contains(var22.title()) ? "Hide " : "Show ") + var22.title().toLowerCase(Locale.ROOT);
               u7 var26 = this.U(var24, () -> {
                  boolean var2x = this.h.get(var22.title()).R;
                  if (!this.P.remove(var22.title())) {
                     this.P.add(var22.title());
                  }

                  this.build();
                  this.layout();
                  if (var2x) {
                     this.M(this.h.get(var22.title()));
                  }
               });
               this.h.put(var22.title(), var26);
               this.D.put(var26, () -> var22.settings().stream().anyMatch(df::B));
            } while (!this.P.contains(var22.title()));

            this.w(new us(var22.title(), () -> var22.settings().stream().anyMatch(df::B), var22.chips()));

            for (df var27 : var22.settings()) {
               if (var1 instanceof FakePayModule var6 && var27 == var6.p()) {
                  this.w(new us("Fake balance"));
                  ur var7 = new ur("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.J = var1x;
                     this.H = "";
                  }, 40);
                  var7.d = this.J;
                  Runnable var8 = () -> {
                     String var2x = var6.j(this.J);
                     this.H = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var7.R(var8);
                  this.w(var7);
                  this.U("Submit", var8);
                  this.Y(() -> var6.L() + (this.H.isEmpty() ? "" : "\n" + this.H));
               } else if (!(var27 instanceof com.larpclient.module.d9 var28 && var28.q())) {
                  if (var22.chips()) {
                     this.w(un.o(var27));
                  } else {
                     this.w(new un(var27));
                  }

                  if (var1 instanceof HitFXModule var29 && var27 == var29.G()) {
                     this.Y(var29::C);
                  }

                  if (var1 instanceof KillFXModule var30 && var27 == var30.d()) {
                     this.Y(var30::i);
                  }
               }
            }
         }
      }
   }

   @Override
   protected boolean componentShown(com.larpclient.b var1) {
      BooleanSupplier var2 = this.D.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.V + 12;
   }

   @Override
   protected int titleY() {
      return this.j.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.j.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.O != null) {
         this.O.render(var1, var2, var3);
      }

      c7.Q(var1, this.V + 12, this.y - 5, this.X - this.V - 24, false, com.larpclient.f.H);
   }

   @Override
   protected void layout() {
      if (this.j != null) {
         this.V = this.j.columns() ? this.j.inspectorX() : this.j.contentX();
         this.X = this.j.right();
         this.y = this.bodyTop();
         this.L = this.j.bottom();
         if (this.O != null) {
            this.O.bounds(this.V + 8, this.j.bodyTop() + 21, Math.max(1, this.X - this.V - 20));
            this.O.W = 16;
         }

         this.a();
      }
   }

   @Override
   void suspend() {
      if (this.k instanceof HitSoundsModule var1) {
         var1.d();
      }

      super.suspend();
   }

   @Override
   public void onClose() {
      if (this.k instanceof HitSoundsModule var1) {
         var1.d();
      }

      this.M.Q();
   }

   public void tick() {
      if (this.k instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.x, var1.q().P())) {
         int var3 = this.q != null && this.q.R ? 1 : 0;
         this.G();
         if (var3 != 0) {
            this.K.stream().filter(var0 -> var0 instanceof un).findFirst().ifPresent(this::M);
         }
      }

      this.U.forEach((var0, var1x) -> var0.S = var1x.currentLabel());
      if (this.o != null) {
         this.o.X = (boolean)(!du.Q() ? 1 : 0);
         this.o.S = this.o.X ? "Start larping" : "Starting...";
      }
   }

   private static final class b extends com.larpclient.b {
      private static final int H = 88;
      private static final int S = 6;
      private static final int N = 14;
      private static final int n = 15;
      private static final int Q = -14999514;
      private static final int P = -14275533;
      private static final int K = -13880517;
      private static final int f = -12958899;
      private final CustomCrosshairModule E;
      private boolean M;
      private boolean j;
      private int k;
      private int y;

      b(CustomCrosshairModule var1) {
         this.E = var1;
         this.W = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.W = this.E.n() ? 12 + 15 * this.Z() + 14 : 88;
         if (!this.E.n()) {
            this.release();
         }
      }

      private int F() {
         return Math.clamp(this.A / 3, 56, 96);
      }

      private int T() {
         return this.A - this.F() - 4;
      }

      private int Z() {
         return Math.clamp((this.T() - 12) / 15, 3, 10);
      }

      private int P() {
         return this.q + (this.T() - 15 * this.Z()) / 2;
      }

      private int J() {
         return this.J + 6;
      }

      private int D(double var1) {
         return (int)Math.floor((var1 - this.P()) / this.Z());
      }

      private int e(double var1) {
         return (int)Math.floor((var1 - this.J()) / this.Z());
      }

      private static boolean S(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (!this.E.n()) {
            int var18 = this.A / 2;
            this.k(var1, this.q, this.J, var18 - 4, this.W, true);
            this.k(var1, this.q + var18, this.J, var18 - 4, this.W, false);
         } else {
            int var4 = this.T();
            int var5 = this.Z();
            int var6 = this.P();
            int var7 = this.J();
            int var8 = var5 >= 5 ? 1 : 0;
            pn.b(var1, this.q, this.J, var4, this.W, 5, -14999514);
            dp var9 = this.E.p();
            int var10 = this.E.b();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.x(var13, var12)) {
                     c7.s(var1, var14, var15, var5, var5, var10);
                  } else {
                     c7.s(
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

            int var19 = this.D(var2);
            int var20 = this.e(var3);
            if (var2 >= 0 && S(var19, var20)) {
               c7.t(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = k6.o("Left click draws, right click erases", Math.max(1, var4 - 12), k6.d.K);
            k6.T(var1, var21, this.q + 6, var7 + 15 * var5 + 4, -6643287, k6.d.K);
            int var22 = this.q + var4 + 4;
            int var16 = this.F() - 4;
            int var17 = (this.W - 4) / 2;
            this.k(var1, var22, this.J, var16, var17, true);
            this.k(var1, var22, this.J + var17 + 4, var16, this.W - var17 - 4, false);
         }
      }

      private void k(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.C());
         pn.b(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         k6.Z(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.pose().scale((float)var9, (float)var9);
            CustomCrosshairModule.l(var1, 0, 0);
         } finally {
            var1.pose().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.E.n() && var5 <= 1) {
            int var6 = this.D(var1);
            int var7 = this.e(var3);
            if (!S(var6, var7)) {
               return false;
            }

            this.j = (boolean)(var5 == 0 && !this.E.p().x(var6, var7) ? 1 : 0);
            this.M = (boolean)1;
            this.k = var6;
            this.y = var7;
            this.E.E(var6, var7, this.j);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.M && this.E.n()) {
            int var5 = this.D(var1);
            int var6 = this.e(var3);
            int var7 = var5 - this.k;
            int var8 = var6 - this.y;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.k + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.y + (int)Math.round((double)var8 * var10 / var9);
               if (S(var11, var12)) {
                  this.E.E(var11, var12, this.j);
               }
            }

            this.k = var5;
            this.y = var6;
         }
      }

      @Override
      public void release() {
         if (this.M) {
            this.M = (boolean)0;
            mt.K("Crosshair", "Drawing edited: {} cells filled", this.E.p().j());
         }
      }
   }
}
