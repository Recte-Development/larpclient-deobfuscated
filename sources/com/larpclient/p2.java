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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class p2 extends pb {
   private Module f;
   private String i = "";
   private String a = "";
   private String I = "";
   private String w = "";
   private p7 X;
   private final Map<p7, com.larpclient.module.a.s> R = new IdentityHashMap<>();
   private int G;
   private int r;
   private final Set<String> s = new HashSet<>();
   private final Map<String, p7> V = new HashMap<>();
   private final Map<oq, BooleanSupplier> q = new IdentityHashMap<>();
   private String y;
   private ov S;

   p2() {
      super("Module");
   }

   void K(Module var1) {
      if (this.f instanceof HitSoundsModule var2) {
         var2.T();
      }

      this.f = var1;
      this.s.clear();
      this.e = 0;
      if (var1 instanceof FakePayModule var4) {
         this.a = var4.u();
      }

      this.I = "";
      this.w = "";
      this.build();
   }

   void i(mj var1, int var2, int var3) {
      this.G = var2;
      this.r = var3;
      this.i(var1);
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
      return var1 >= this.G && var1 < this.r;
   }

   void P() {
      int var1 = this.e;
      this.build();
      this.layout();
      this.e = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.f();
      this.V.clear();
      this.q.clear();
      this.R.clear();
      this.X = null;
      this.S = null;
      if (this.f != null) {
         Module var1 = this.f;
         this.y = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.w().getValue() : null;
         this.Y = var1.getDisplayName();
         this.S = new ov("", var1);
         this.k.add(this.S);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof com.larpclient.module.t var9) {
            this.C(var9::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var10) {
            this.e(new p2.s(var10));
            p7 var3 = this.L("Clear drawing", var10::D);
            var3.Q = (boolean)1;
            this.q.put(var3, var10::y);
         }

         if (var1 instanceof AutoSellModule var11) {
            this.C(var11::T);
            this.L("Choose items to sell", () -> var11.w(this.x, false));
            this.L("Choose excluded items", () -> var11.w(this.x, true));
            this.L("Add held item to selection", () -> {
               try {
                  var11.O(false);
                  this.w = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.C(() -> this.w);
         }

         if (var1 instanceof OrderDropperModule var12) {
            this.C(var12::J);
            this.L("Use held item", () -> {
               try {
                  var12.p();
                  this.w = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.C(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.L("Select shulkers", () -> {
               try {
                  var12.A();
                  this.w = "";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.L("Clear selected shulkers", () -> {
               try {
                  var12.b();
                  this.w = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.L("Rescan selected shulkers", () -> {
               try {
                  var12.U();
                  this.w = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.L("Reset run progress", () -> {
               try {
                  var12.m();
                  this.w = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.w = var3x.getMessage();
               }
            });
            this.C(() -> this.w);
         }

         if (var1 instanceof com.larpclient.module.a var13) {
            this.e(new p8("Setup"));
            this.C(var13::setupSummary);
            this.C(var13::setupHint);

            for (com.larpclient.module.a.s var4 : var13.Y()) {
               p7 var5 = this.L(var4.currentLabel(), var4.run());
               this.R.put(var5, var4);
            }
         }

         Iterator var14 = dp.s(var1).iterator();

         while (true) {
            dp.x var22;
            do {
               if (!var14.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.L("Open HUD editor", () -> this.x.N());
                  }

                  if (var1 instanceof SkinChangerModule var15) {
                     this.L("Apply skin", var15::g);
                     this.L("Refresh skins", () -> {
                        var15.h();
                        this.build();
                        this.layout();
                     });
                     this.L("Open skins folder", var15::s);
                     this.C(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var16) {
                     var16.I();
                     this.L("Refresh images", () -> {
                        var16.Z();
                        this.build();
                        this.layout();
                     });
                     this.L("Open backgrounds folder", var16::e);
                     this.C(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var16.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var17) {
                     this.L("Preview sound", var17::z);
                  }

                  if (var1 instanceof LARPModule var18) {
                     p_ var23 = new p_("Minecraft username", var1x -> this.i = var1x, 16);
                     var23.V = this.i;
                     this.e(var23);
                     this.X = new p7("Start larping", () -> {
                        if (!this.i.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           em.R = "Enter a Minecraft username";
                        } else {
                           var18.setEnabled(true);
                           em.A(this.i.trim());
                        }
                     });
                     this.e(this.X);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.e(new p8("Keybind"));
                     com.larpclient.module.la var19 = new com.larpclient.module.la(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var19.W(var1::setKeyBind);
                     this.e(new p9(var19));
                     if (var1.keybindMode() != null) {
                        this.e(new p9(var1.keybindMode()));
                     }
                  }

                  p7 var20 = this.L("Reset module settings", () -> {
                     for (com.larpclient.module.l6 var3x : var1.getSettings()) {
                        var3x.x();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.g();
                     }

                     if (var1 instanceof FakePayModule var5x) {
                        this.a = var5x.u();
                     }

                     this.I = "";
                     this.build();
                  });
                  var20.Q = (boolean)1;
                  return;
               }

               var22 = (dp.x)var14.next();
               if (!var22.collapsed()) {
                  break;
               }

               String var24 = (this.s.contains(var22.title()) ? "Hide " : "Show ") + var22.title().toLowerCase(Locale.ROOT);
               p7 var26 = this.L(var24, () -> {
                  boolean var2x = this.V.get(var22.title()).h;
                  if (!this.s.remove(var22.title())) {
                     this.s.add(var22.title());
                  }

                  this.build();
                  this.layout();
                  if (var2x) {
                     this.x(this.V.get(var22.title()));
                  }
               });
               this.V.put(var22.title(), var26);
               this.q.put(var26, () -> var22.settings().stream().anyMatch(com.larpclient.module.l6::R));
            } while (!this.s.contains(var22.title()));

            this.e(new p8(var22.title(), () -> var22.settings().stream().anyMatch(com.larpclient.module.l6::R), var22.chips()));

            for (com.larpclient.module.l6 var27 : var22.settings()) {
               if (var1 instanceof FakePayModule var6 && var27 == var6.P()) {
                  this.e(new p8("Fake balance"));
                  p_ var7 = new p_("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.a = var1x;
                     this.I = "";
                  }, 40);
                  var7.V = this.a;
                  Runnable var8 = () -> {
                     String var2x = var6.G(this.a);
                     this.I = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var7.T(var8);
                  this.e(var7);
                  this.L("Submit", var8);
                  this.C(() -> var6.I() + (this.I.isEmpty() ? "" : "\n" + this.I));
               } else if (!(var27 instanceof com.larpclient.module.lc var28 && var28.b())) {
                  if (var22.chips()) {
                     this.e(p9.O(var27));
                  } else {
                     this.e(new p9(var27));
                  }

                  if (var1 instanceof HitFXModule var29 && var27 == var29.G()) {
                     this.C(var29::H);
                  }

                  if (var1 instanceof KillFXModule var30 && var27 == var30.m()) {
                     this.C(var30::V);
                  }
               }
            }
         }
      }
   }

   @Override
   protected boolean componentShown(oq var1) {
      BooleanSupplier var2 = this.q.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.O + 12;
   }

   @Override
   protected int titleY() {
      return this.M.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.M.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.S != null) {
         this.S.render(var1, var2, var3);
      }

      fr.W(var1, this.O + 12, this.t - 5, this.A - this.O - 24, false, rv.h);
   }

   @Override
   protected void layout() {
      if (this.M != null) {
         this.O = this.M.columns() ? this.M.inspectorX() : this.M.contentX();
         this.A = this.M.right();
         this.t = this.bodyTop();
         this.n = this.M.bottom();
         if (this.S != null) {
            this.S.bounds(this.O + 8, this.M.bodyTop() + 21, Math.max(1, this.A - this.O - 20));
            this.S.O = 16;
         }

         this.d();
      }
   }

   @Override
   void suspend() {
      if (this.f instanceof HitSoundsModule var1) {
         var1.T();
      }

      super.suspend();
   }

   @Override
   public void onClose() {
      if (this.f instanceof HitSoundsModule var1) {
         var1.T();
      }

      this.x.b();
   }

   public void tick() {
      if (this.f instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.y, var1.w().getValue())) {
         int var3 = this.W != null && this.W.h ? 1 : 0;
         this.P();
         if (var3 != 0) {
            this.p.stream().filter(var0 -> var0 instanceof p9).findFirst().ifPresent(this::x);
         }
      }

      this.R.forEach((var0, var1x) -> var0.J = var1x.currentLabel());
      if (this.X != null) {
         this.X.x = (boolean)(!em.C() ? 1 : 0);
         this.X.J = this.X.x ? "Start larping" : "Starting...";
      }
   }

   private static final class s extends oq {
      private static final int r = 88;
      private static final int M = 6;
      private static final int a = 14;
      private static final int g = 15;
      private static final int V = -14999514;
      private static final int H = -14275533;
      private static final int o = -13880517;
      private static final int E = -12958899;
      private final CustomCrosshairModule j;
      private boolean b;
      private boolean Z;
      private int Q;
      private int W;

      s(CustomCrosshairModule var1) {
         this.j = var1;
         this.O = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.O = this.j.y() ? 12 + 15 * this.i() + 14 : 88;
         if (!this.j.y()) {
            this.release();
         }
      }

      private int o() {
         return Math.clamp(this.v / 3, 56, 96);
      }

      private int B() {
         return this.v - this.o() - 4;
      }

      private int i() {
         return Math.clamp((this.B() - 12) / 15, 3, 10);
      }

      private int E() {
         return this.B + (this.B() - 15 * this.i()) / 2;
      }

      private int A() {
         return this.L + 6;
      }

      private int r(double var1) {
         return (int)Math.floor((var1 - this.E()) / this.i());
      }

      private int J(double var1) {
         return (int)Math.floor((var1 - this.A()) / this.i());
      }

      private static boolean e(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (!this.j.y()) {
            int var18 = this.v / 2;
            this.H(var1, this.B, this.L, var18 - 4, this.O, true);
            this.H(var1, this.B + var18, this.L, var18 - 4, this.O, false);
         } else {
            int var4 = this.B();
            int var5 = this.i();
            int var6 = this.E();
            int var7 = this.A();
            int var8 = var5 >= 5 ? 1 : 0;
            ll.N(var1, this.B, this.L, var4, this.O, 5, -14999514);
            _n var9 = this.j.e();
            int var10 = this.j.Q();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.F(var13, var12)) {
                     fr.k(var1, var14, var15, var5, var5, var10);
                  } else {
                     fr.k(
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

            int var19 = this.r(var2);
            int var20 = this.J(var3);
            if (var2 >= 0 && e(var19, var20)) {
               fr.K(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = j_.d("Left click draws, right click erases", Math.max(1, var4 - 12), j_.m.U);
            j_.L(var1, var21, this.B + 6, var7 + 15 * var5 + 4, -6643287, j_.m.U);
            int var22 = this.B + var4 + 4;
            int var16 = this.o() - 4;
            int var17 = (this.O - 4) / 2;
            this.H(var1, var22, this.L, var16, var17, true);
            this.H(var1, var22, this.L + var17 + 4, var16, this.O - var17 - 4, false);
         }
      }

      private void H(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.d());
         ll.N(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         j_.E(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.pose().scale((float)var9, (float)var9);
            CustomCrosshairModule.N(var1, 0, 0);
         } finally {
            var1.pose().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.j.y() && var5 <= 1) {
            int var6 = this.r(var1);
            int var7 = this.J(var3);
            if (!e(var6, var7)) {
               return false;
            }

            this.Z = (boolean)(var5 == 0 && !this.j.e().F(var6, var7) ? 1 : 0);
            this.b = (boolean)1;
            this.Q = var6;
            this.W = var7;
            this.j.Z(var6, var7, this.Z);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.b && this.j.y()) {
            int var5 = this.r(var1);
            int var6 = this.J(var3);
            int var7 = var5 - this.Q;
            int var8 = var6 - this.W;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.Q + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.W + (int)Math.round((double)var8 * var10 / var9);
               if (e(var11, var12)) {
                  this.j.Z(var11, var12, this.Z);
               }
            }

            this.Q = var5;
            this.W = var6;
         }
      }

      @Override
      public void release() {
         if (this.b) {
            this.b = (boolean)0;
            r4.V("Crosshair", "Drawing edited: {} cells filled", this.j.e().f());
         }
      }
   }
}
