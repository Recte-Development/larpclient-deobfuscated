package com.larpclient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public final class j7 {
   private final BooleanSupplier F;
   private final Consumer<o8.a> V;
   private final Runnable A;
   private o8.a x;
   private o8.a K;
   private _h.q T;
   private _h.z l;
   private fc.g e;
   private fc.n L;
   private _h.p z;
   private int H = -1;
   private int n = -1;
   private long O = fc.S;
   private boolean h;
   private boolean m;
   private double D;
   private double Q;
   private String B = "";
   private boolean v;
   private final lq E = new lq(() -> this.h = (boolean)1, this::S);
   private final lz M = new lz();
   private double P;
   private double u;
   private boolean U;
   private long I;
   private boolean g;
   private Predicate<o8.a> Y = var0 -> false;
   private int w;
   private int o;
   private int r = -1;

   public j7() {
      this(
         () -> {
            mo var0 = LarpClient.y().v();
            return var0.a(var0.o());
         },
         var0 -> {
            Minecraft var1 = Minecraft.getInstance();
            var1.gui.setScreen(p0.T(var1.gui.screen(), var0));
         },
         () -> Minecraft.getInstance()
            .gui
            .hud
            .getChat()
            .addClientSystemMessage(Component.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.v = (boolean)1;
      this.Y = var0 -> LarpClient.y().v().E().K(var0.M);
   }

   public j7(BooleanSupplier var1, Consumer<o8.a> var2, Runnable var3) {
      this.F = var1;
      this.V = var2;
      this.A = var3;
   }

   public void Y(Predicate<o8.a> var1) {
      this.g = (boolean)1;
      this.Y = Objects.requireNonNull(var1);
   }

   public void i(o8.a var1) {
      if (this.R()) {
         this.c();
      }

      this.x = var1;
      this.z = null;
   }

   public int h() {
      return this.H;
   }

   public int m() {
      return this.n;
   }

   public o8.a n() {
      return this.x;
   }

   public void O() {
      this.z = null;
      this.E.s();
   }

   public boolean R() {
      return this.K != null;
   }

   public boolean Q(double var1, double var3, int var5, int var6, List<o8.r> var7) {
      if (!this.R() && (this.z == null || !this.z.contains(var1, var3, 0))) {
         if (this.v && this.E.g() && this.E.Y().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.g || !var1x.element().z()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.v
               && o8.d
                  .stream()
                  .filter(var1x -> !this.Y.test(var1x))
                  .anyMatch(var7x -> o8.B(var8, var7x).map(var6x -> _h.G(var6x, var5, var6).contains(var1, var3, 0)).orElse(false));
      } else {
         return true;
      }
   }

   public boolean l(double var1, double var3, int var5, int var6, int var7, List<o8.r> var8, boolean var9) {
      if (this.R()) {
         return true;
      }

      if (var9) {
         this.z = null;
         return false;
      }

      if (this.v && this.E.C(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.z != null && this.z.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.z.y()) / 20;
         this.z = null;
         switch (var15) {
            case 0:
               this.x.W(true);
               this.h = (boolean)1;
               this.S();
               this.x = null;
               break;
            case 1:
               this.S();
               this.V.accept(this.x);
               break;
            case 2:
               this.x.j();
               this.h = (boolean)1;
               this.S();
               break;
            case 3:
               this.S();
         }

         return true;
      } else {
         this.z = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.g || !var1x.element().z()).toList();
         Optional var11 = o8.B(var10, this.x);
         o8.a var12 = this.v && var5 == 0
            ? o8.d
               .stream()
               .filter(var1x -> !this.Y.test(var1x))
               .filter(var7x -> o8.B(var10, var7x).map(var6x -> _h.G(var6x, var6, var7).contains(var1, var3, 0)).orElse(false))
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.x = var12;
            var11 = o8.B(var10, this.x);
         }

         if (var5 == 0 && this.x != null && !this.Y.test(this.x) && var11.isPresent() && _h.G((_h.p)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.V(this.x);
            this.w = this.x.b(var6);
            this.o = this.x.M(var7);
            _h.p var16 = (_h.p)var11.get();
            this.l = new _h.z(var16, var1, var3, this.x.p());
            this.D = (var16.x() - this.x.b(var6)) / this.x.p();
            this.Q = (var16.y() - this.x.M(var7)) / this.x.p();
            return true;
         }

         o8.r var13 = _h.N(var10, var0 -> true, o8.r::bounds, var1, var3);
         if (var13 == null) {
            this.x = null;
            if (this.v && var5 == 1) {
               this.E.p((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.x = var13.element();
            r4.g("HUD editor", "Selected {} in {}", this.x.M, this.g ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.v) {
                  this.E.X(this.x, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.B.isEmpty() ? 60 : 80;
               this.z = new _h.p(_h.n((int)var1, 142, var6), _h.n((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
            } else {
               if (this.Y.test(this.x)) {
                  return true;
               }

               this.V(this.x);
               this.w = this.x.b(var6);
               this.o = this.x.M(var7);
               this.T = new _h.q(var1 - this.x.b(var6), var3 - this.x.M(var7));
               this.M.n(this.x.b(var6), this.x.M(var7));
               this.P = var1;
               this.u = var3;
               this.U = (boolean)0;
               this.I = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void V(o8.a var1) {
      this.K = var1;
      this.r = -1;
      this.e = fc.m.get(var1.M);
      this.L = fc.Q.get(var1.M);
   }

   public boolean b(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.w(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean w(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.v && var5 == 0 && this.E.C(var1, var3, var6, var7)) {
         return true;
      }

      if (this.R() && var5 == 0) {
         if (this.l != null) {
            this.K.m(this.v ? lz.w(this.l, var1, var3) : this.l.scale(var1, var3));
            this.K.i((int)Math.round(this.l.bounds().x() - this.D * this.K.p()), (int)Math.round(this.l.bounds().y() - this.Q * this.K.p()), var6, var7);
         } else {
            if (this.v) {
               this.P = var1;
               this.u = var3;
               this.U = var8;
               return true;
            }

            _h.n var10 = this.T.move(var1, var3, this.K.O(var6, var7), var6, var7, fc.h.snapping(), fc.h.grid(), var8, this.H, this.n);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.g && var9) {
               int var13 = var11 - this.w;
               int var14 = var12 - this.o;
               if (this.r < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.r = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.r == 0) {
                  var12 = this.o;
               } else if (this.r == 1) {
                  var11 = this.w;
               }
            } else {
               this.r = -1;
            }

            this.K.i(var11, var12, var6, var7);
            this.H = var9 && this.r == 1 ? -1 : var10.guideX();
            this.n = var9 && this.r == 0 ? -1 : var10.guideY();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean R(int var1) {
      if (this.v && var1 == 0 && this.E.M()) {
         return true;
      } else if (this.R() && var1 == 0) {
         this.c();
         return true;
      } else {
         return false;
      }
   }

   private void c() {
      if (this.K != null) {
         this.h = (boolean)(this.h | (Objects.equals(this.e, fc.m.get(this.K.M)) && Objects.equals(this.L, fc.Q.get(this.K.M)) ? 0 : 1));
         r4.g("HUD editor", "Released {} gesture for {}", this.l == null ? "move" : "scale", this.K.M);
      }

      this.K = null;
      this.T = null;
      this.l = null;
      this.e = null;
      this.L = null;
      this.H = this.n = -1;
      this.S();
   }

   private void S() {
      if (this.h) {
         if (this.F.getAsBoolean()) {
            this.h = (boolean)0;
            this.B = "";
            r4.g("HUD editor", "Committed {} edit to active profile", this.g ? "workspace" : "chat");
         } else {
            if (this.B.isEmpty()) {
               this.A.run();
            }

            this.B = "HUD save failed. Retry save in the menu.";
            r4.m("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void I(boolean var1, boolean var2) {
      if (this.O != fc.S) {
         this.E.G();
         this.K = null;
         this.T = null;
         this.l = null;
         this.h = (boolean)0;
         this.B = "";
         this.H = this.n = -1;
         this.x = null;
         this.z = null;
         this.O = fc.S;
         r4.g("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.m = (boolean)0;
      } else {
         if (!this.m) {
            this.N();
         }

         this.m = (boolean)1;
      }
   }

   public void N() {
      this.E.s();
      this.c();
      this.x = null;
      this.z = null;
   }

   public boolean V(int var1) {
      return this.v && this.E.Z(var1);
   }

   public boolean K(int var1) {
      return this.v && this.E.q(var1);
   }

   public boolean T(double var1, double var3, double var5, boolean var7) {
      return this.v && !var7 && this.E.v(var1, var3, var5);
   }

   public void V(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.v && this.K != null && this.T != null) {
         long var7 = System.nanoTime();
         _h.n var9 = this.T.move(this.P, this.u, this.K.O(var4, var5), var4, var5, fc.h.snapping(), fc.h.grid(), this.U, this.H, this.n);
         _h.n var10 = this.M.X(var9.x(), var9.y(), Minecraft.getInstance().hasShiftDown(), (var7 - this.I) / 1.0E9);
         this.I = var7;
         this.K.i(var10.x(), var10.y(), var4, var5);
         this.H = var9.guideX();
         this.n = var9.guideY();
      }

      List var12 = o8.d();
      Optional var8 = o8.B(var12, this.x);
      if (this.x != null && var8.isEmpty() && !this.R()) {
         this.x = null;
         this.z = null;
      }

      o8.r var13 = var6 ? null : _h.N(var12, var0 -> true, o8.r::bounds, var2, var3);
      if (var13 != null && var13.element() != this.x) {
         y(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         _h.p var14 = (_h.p)var8.get();
         y(var1, var14, oh.i(rv.e, 0.8));
         if (!this.Y.test(this.x)) {
            _h.p var11 = _h.G(var14, var4, var5);
            fr.S(var1, var11.x(), var11.y(), var11.width(), var11.height(), 2.0F, rv.e);
         }
      }

      if (this.R()) {
         if (this.v) {
            for (byte var15 = 0; var15 < var4; var15 += 40) {
               fr.k(var1, var15, 0, 1, var5, var15 % 200 == 0 ? 721420287 : 385875967);
            }

            for (byte var16 = 0; var16 < var5 - 14; var16 += 40) {
               fr.k(var1, 0, var16, var4, 1, var16 % 200 == 0 ? 721420287 : 385875967);
            }

            Optional var17 = o8.B(var12, this.K);
            if (var17.isPresent()) {
               fr.k(var1, ((_h.p)var17.get()).x() + ((_h.p)var17.get()).width() / 2, 0, 1, var5, oh.i(rv.e, 0.6));
               fr.k(var1, 0, ((_h.p)var17.get()).y() + ((_h.p)var17.get()).height() / 2, var4, 1, oh.i(rv.e, 0.6));
            }
         }

         if (this.H >= 0) {
            fr.k(var1, Math.min(var4 - 1, this.H), 0, 1, var5, oh.i(rv.e, 0.3));
         }

         if (this.n >= 0) {
            fr.k(var1, 0, Math.min(var5 - 1, this.n), var4, 1, oh.i(rv.e, 0.3));
         }
      }

      if (this.v) {
         for (o8.a var20 : o8.d) {
            if (!this.Y.test(var20)) {
               o8.B(var12, var20).ifPresent(var5x -> {
                  _h.p var6x = _h.G(var5x, var4, var5);
                  fr.S(var1, var6x.x(), var6x.y(), var6x.width(), var6x.height(), 2.0F, oh.i(rv.e, var6x.contains(var2, var3, 0) ? 0.6 : 0.35));
                  fr.k(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                  fr.k(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
               });
            }
         }

         this.E.c(var1, var2, var3);
      }

      if (this.z != null) {
         fr.S(var1, this.z.x(), this.z.y(), this.z.width(), this.z.height(), 6.0F, -267645934);
         String[] var19 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var21 = 0; var21 < this.z.height() / 20; var21++) {
            if (!var6 && this.z.contains(var2, var3, 0) && (var3 - this.z.y()) / 20 == var21) {
               fr.S(var1, this.z.x() + 2, this.z.y() + var21 * 20, this.z.width() - 4, 20.0F, 4.0F, 905969663);
            }

            ou.u(var1, ou.e(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var21]), this.z.x() + 7, this.z.y() + var21 * 20 + 4, 12, rv.e);
            rd.v(var1, var19[var21], this.z.x() + 25, rz.W(this.z.y() + var21 * 20, 20.0, 11.0), 11.0, -1);
         }
      }
   }

   private static void y(GuiGraphicsExtractor var0, _h.p var1, int var2) {
      fr.K(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
