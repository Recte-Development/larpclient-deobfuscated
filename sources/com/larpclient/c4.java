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

public final class c4 {
   private final BooleanSupplier z;
   private final Consumer<mq.s> K;
   private final Runnable y;
   private mq.s W;
   private mq.s x;
   private ez.t l;
   private ez.i i;
   private cx.m v;
   private cx.z U;
   private ez.h u;
   private int R = -1;
   private int E = -1;
   private long D = cx.U;
   private boolean Y;
   private boolean g;
   private double r;
   private double m;
   private String f = "";
   private boolean k;
   private final z4 a = new z4(() -> this.Y = (boolean)1, this::f);
   private final pv N = new pv();
   private double A;
   private double j;
   private boolean H;
   private long I;
   private boolean q;
   private Predicate<mq.s> d = var0 -> false;
   private int c;
   private int b;
   private int G = -1;

   public c4() {
      this(
         () -> {
            w9 var0 = LarpClient.F().f();
            return var0.E(var0.w());
         },
         var0 -> {
            Minecraft var1 = Minecraft.getInstance();
            var1.gui.setScreen(hw.t(var1.gui.screen(), var0));
         },
         () -> Minecraft.getInstance()
            .gui
            .hud
            .getChat()
            .addClientSystemMessage(Component.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.k = (boolean)1;
      this.d = var0 -> LarpClient.F().f().t().c(var0.g);
   }

   public c4(BooleanSupplier var1, Consumer<mq.s> var2, Runnable var3) {
      this.z = var1;
      this.K = var2;
      this.y = var3;
   }

   public void A(Predicate<mq.s> var1) {
      this.q = (boolean)1;
      this.d = Objects.requireNonNull(var1);
   }

   public void Q(mq.s var1) {
      if (this.s()) {
         this.i();
      }

      this.W = var1;
      this.u = null;
   }

   public int T() {
      return this.R;
   }

   public int P() {
      return this.E;
   }

   public mq.s k() {
      return this.W;
   }

   public void C() {
      this.u = null;
      this.a.n();
   }

   public boolean s() {
      return this.x != null;
   }

   public boolean a(double var1, double var3, int var5, int var6, List<mq.l> var7) {
      if (!this.s() && (this.u == null || !this.u.contains(var1, var3, 0))) {
         if (this.k && this.a.l() && this.a.m().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.q || !var1x.element().N()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.k
               && mq.Y
                  .stream()
                  .filter(var1x -> !this.d.test(var1x))
                  .anyMatch(var7x -> mq.e(var8, var7x).map(var6x -> ez.Z(var6x, var5, var6).contains(var1, var3, 0)).orElse(false));
      } else {
         return true;
      }
   }

   public boolean n(double var1, double var3, int var5, int var6, int var7, List<mq.l> var8, boolean var9) {
      if (this.s()) {
         return true;
      }

      if (var9) {
         this.u = null;
         return false;
      }

      if (this.k && this.a.B(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.u != null && this.u.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.u.y()) / 20;
         this.u = null;
         switch (var15) {
            case 0:
               this.W.r(true);
               this.Y = (boolean)1;
               this.f();
               this.W = null;
               break;
            case 1:
               this.f();
               this.K.accept(this.W);
               break;
            case 2:
               this.W.f();
               this.Y = (boolean)1;
               this.f();
               break;
            case 3:
               this.f();
         }

         return true;
      } else {
         this.u = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.q || !var1x.element().N()).toList();
         Optional var11 = mq.e(var10, this.W);
         mq.s var12 = this.k && var5 == 0
            ? mq.Y
               .stream()
               .filter(var1x -> !this.d.test(var1x))
               .filter(var7x -> mq.e(var10, var7x).map(var6x -> ez.Z(var6x, var6, var7).contains(var1, var3, 0)).orElse(false))
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.W = var12;
            var11 = mq.e(var10, this.W);
         }

         if (var5 == 0 && this.W != null && !this.d.test(this.W) && var11.isPresent() && ez.Z((ez.h)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.p(this.W);
            this.c = this.W.W(var6);
            this.b = this.W.i(var7);
            ez.h var16 = (ez.h)var11.get();
            this.i = new ez.i(var16, var1, var3, this.W.b());
            this.r = (var16.x() - this.W.W(var6)) / this.W.b();
            this.m = (var16.y() - this.W.i(var7)) / this.W.b();
            return true;
         }

         mq.l var13 = ez.z(var10, var0 -> true, mq.l::bounds, var1, var3);
         if (var13 == null) {
            this.W = null;
            if (this.k && var5 == 1) {
               this.a.m((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.W = var13.element();
            mt.U("HUD editor", "Selected {} in {}", this.W.g, this.q ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.k) {
                  this.a.w(this.W, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.f.isEmpty() ? 60 : 80;
               this.u = new ez.h(ez.a((int)var1, 142, var6), ez.a((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
            } else {
               if (this.d.test(this.W)) {
                  return true;
               }

               this.p(this.W);
               this.c = this.W.W(var6);
               this.b = this.W.i(var7);
               this.l = new ez.t(var1 - this.W.W(var6), var3 - this.W.i(var7));
               this.N.R(this.W.W(var6), this.W.i(var7));
               this.A = var1;
               this.j = var3;
               this.H = (boolean)0;
               this.I = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void p(mq.s var1) {
      this.x = var1;
      this.G = -1;
      this.v = cx.O.get(var1.g);
      this.U = cx.D.get(var1.g);
   }

   public boolean Q(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.X(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean X(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.k && var5 == 0 && this.a.R(var1, var3, var6, var7)) {
         return true;
      }

      if (this.s() && var5 == 0) {
         if (this.i != null) {
            this.x.W(this.k ? pv.o(this.i, var1, var3) : this.i.scale(var1, var3));
            this.x.s((int)Math.round(this.i.bounds().x() - this.r * this.x.b()), (int)Math.round(this.i.bounds().y() - this.m * this.x.b()), var6, var7);
         } else {
            if (this.k) {
               this.A = var1;
               this.j = var3;
               this.H = var8;
               return true;
            }

            ez.s var10 = this.l.move(var1, var3, this.x.H(var6, var7), var6, var7, cx.l.snapping(), cx.l.grid(), var8, this.R, this.E);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.q && var9) {
               int var13 = var11 - this.c;
               int var14 = var12 - this.b;
               if (this.G < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.G = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.G == 0) {
                  var12 = this.b;
               } else if (this.G == 1) {
                  var11 = this.c;
               }
            } else {
               this.G = -1;
            }

            this.x.s(var11, var12, var6, var7);
            this.R = var9 && this.G == 1 ? -1 : var10.guideX();
            this.E = var9 && this.G == 0 ? -1 : var10.guideY();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean a(int var1) {
      if (this.k && var1 == 0 && this.a.p()) {
         return true;
      } else if (this.s() && var1 == 0) {
         this.i();
         return true;
      } else {
         return false;
      }
   }

   private void i() {
      if (this.x != null) {
         this.Y = (boolean)(this.Y | (Objects.equals(this.v, cx.O.get(this.x.g)) && Objects.equals(this.U, cx.D.get(this.x.g)) ? 0 : 1));
         mt.U("HUD editor", "Released {} gesture for {}", this.i == null ? "move" : "scale", this.x.g);
      }

      this.x = null;
      this.l = null;
      this.i = null;
      this.v = null;
      this.U = null;
      this.R = this.E = -1;
      this.f();
   }

   private void f() {
      if (this.Y) {
         if (this.z.getAsBoolean()) {
            this.Y = (boolean)0;
            this.f = "";
            mt.U("HUD editor", "Committed {} edit to active profile", this.q ? "workspace" : "chat");
         } else {
            if (this.f.isEmpty()) {
               this.y.run();
            }

            this.f = "HUD save failed. Retry save in the menu.";
            mt.T("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void h(boolean var1, boolean var2) {
      if (this.D != cx.U) {
         this.a.y();
         this.x = null;
         this.l = null;
         this.i = null;
         this.Y = (boolean)0;
         this.f = "";
         this.R = this.E = -1;
         this.W = null;
         this.u = null;
         this.D = cx.U;
         mt.U("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.g = (boolean)0;
      } else {
         if (!this.g) {
            this.y();
         }

         this.g = (boolean)1;
      }
   }

   public void y() {
      this.a.n();
      this.i();
      this.W = null;
      this.u = null;
   }

   public boolean h(int var1) {
      return this.k && this.a.w(var1);
   }

   public boolean W(int var1) {
      return this.k && this.a.z(var1);
   }

   public boolean H(double var1, double var3, double var5, boolean var7) {
      return this.k && !var7 && this.a.D(var1, var3, var5);
   }

   public void Z(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.k && this.x != null && this.l != null) {
         long var7 = System.nanoTime();
         ez.s var9 = this.l.move(this.A, this.j, this.x.H(var4, var5), var4, var5, cx.l.snapping(), cx.l.grid(), this.H, this.R, this.E);
         ez.s var10 = this.N.s(var9.x(), var9.y(), Minecraft.getInstance().hasShiftDown(), (var7 - this.I) / 1.0E9);
         this.I = var7;
         this.x.s(var10.x(), var10.y(), var4, var5);
         this.R = var9.guideX();
         this.E = var9.guideY();
      }

      List var12 = mq.q();
      Optional var8 = mq.e(var12, this.W);
      if (this.W != null && var8.isEmpty() && !this.s()) {
         this.W = null;
         this.u = null;
      }

      mq.l var13 = var6 ? null : ez.z(var12, var0 -> true, mq.l::bounds, var2, var3);
      if (var13 != null && var13.element() != this.W) {
         Q(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         ez.h var14 = (ez.h)var8.get();
         Q(var1, var14, _3.A(com.larpclient.f.C, 0.8));
         if (!this.d.test(this.W)) {
            ez.h var11 = ez.Z(var14, var4, var5);
            c7.n(var1, var11.x(), var11.y(), var11.width(), var11.height(), 2.0F, com.larpclient.f.C);
         }
      }

      if (this.s()) {
         if (this.k) {
            for (byte var15 = 0; var15 < var4; var15 += 40) {
               c7.s(var1, var15, 0, 1, var5, var15 % 200 == 0 ? 721420287 : 385875967);
            }

            for (byte var16 = 0; var16 < var5 - 14; var16 += 40) {
               c7.s(var1, 0, var16, var4, 1, var16 % 200 == 0 ? 721420287 : 385875967);
            }

            Optional var17 = mq.e(var12, this.x);
            if (var17.isPresent()) {
               c7.s(var1, ((ez.h)var17.get()).x() + ((ez.h)var17.get()).width() / 2, 0, 1, var5, _3.A(com.larpclient.f.C, 0.6));
               c7.s(var1, 0, ((ez.h)var17.get()).y() + ((ez.h)var17.get()).height() / 2, var4, 1, _3.A(com.larpclient.f.C, 0.6));
            }
         }

         if (this.R >= 0) {
            c7.s(var1, Math.min(var4 - 1, this.R), 0, 1, var5, _3.A(com.larpclient.f.C, 0.3));
         }

         if (this.E >= 0) {
            c7.s(var1, 0, Math.min(var5 - 1, this.E), var4, 1, _3.A(com.larpclient.f.C, 0.3));
         }
      }

      if (this.k) {
         for (mq.s var20 : mq.Y) {
            if (!this.d.test(var20)) {
               mq.e(var12, var20).ifPresent(var5x -> {
                  ez.h var6x = ez.Z(var5x, var4, var5);
                  c7.n(var1, var6x.x(), var6x.y(), var6x.width(), var6x.height(), 2.0F, _3.A(com.larpclient.f.C, var6x.contains(var2, var3, 0) ? 0.6 : 0.35));
                  c7.s(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                  c7.s(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
               });
            }
         }

         this.a.g(var1, var2, var3);
      }

      if (this.u != null) {
         c7.n(var1, this.u.x(), this.u.y(), this.u.width(), this.u.height(), 6.0F, -267645934);
         String[] var19 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var21 = 0; var21 < this.u.height() / 20; var21++) {
            if (!var6 && this.u.contains(var2, var3, 0) && (var3 - this.u.y()) / 20 == var21) {
               c7.n(var1, this.u.x() + 2, this.u.y() + var21 * 20, this.u.width() - 4, 20.0F, 4.0F, 905969663);
            }

            tp.N(
               var1,
               tp.j(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var21]),
               this.u.x() + 7,
               this.u.y() + var21 * 20 + 4,
               12,
               com.larpclient.f.C
            );
            we.F(var1, var19[var21], this.u.x() + 25, ew.N(this.u.y() + var21 * 20, 20.0, 11.0), 11.0, -1);
         }
      }
   }

   private static void Q(GuiGraphicsExtractor var0, ez.h var1, int var2) {
      c7.t(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
