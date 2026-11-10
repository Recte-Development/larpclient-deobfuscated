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

public final class db {
   private final BooleanSupplier V;
   private final Consumer<dk.q> D;
   private final Runnable f;
   private dk.q N;
   private dk.q w;
   private qy.a R;
   private qy.w y;
   private bh.u M;
   private bh.b c;
   private qy.i o;
   private int b = -1;
   private int n = -1;
   private long e = bh.k;
   private boolean x;
   private boolean K;
   private double i;
   private double C;
   private String B = "";
   private boolean E;
   private final hs T = new hs(() -> this.x = (boolean)1, this::p);
   private final qp U = new qp();
   private double H;
   private double G;
   private boolean j;
   private long I;
   private boolean d;
   private Predicate<dk.q> W = var0 -> false;
   private int k;
   private int Y;
   private int z = -1;

   public db() {
      this(
         () -> {
            aq var0 = LarpClient.N().h();
            return var0.Q(var0.S());
         },
         var0 -> {
            Minecraft var1 = Minecraft.getInstance();
            var1.gui.setScreen(f8.K(var1.gui.screen(), var0));
         },
         () -> Minecraft.getInstance()
            .gui
            .hud
            .getChat()
            .addClientSystemMessage(Component.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.E = (boolean)1;
      this.W = var0 -> LarpClient.N().h().k().o(var0.d);
   }

   public db(BooleanSupplier var1, Consumer<dk.q> var2, Runnable var3) {
      this.V = var1;
      this.D = var2;
      this.f = var3;
   }

   public void B(Predicate<dk.q> var1) {
      this.d = (boolean)1;
      this.W = Objects.requireNonNull(var1);
   }

   public void H(dk.q var1) {
      if (this.K()) {
         this.C();
      }

      this.N = var1;
      this.o = null;
   }

   public int C() {
      return this.b;
   }

   public int g() {
      return this.n;
   }

   public dk.q W() {
      return this.N;
   }

   public void q() {
      this.o = null;
      this.T.e();
   }

   public boolean K() {
      return this.w != null;
   }

   public boolean W(double var1, double var3, int var5, int var6, List<dk.i> var7) {
      if (!this.K() && (this.o == null || !this.o.contains(var1, var3, 0))) {
         if (this.E && this.T.h() && this.T.O().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.d || !var1x.element().X()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.E
               && dk.X()
                  .stream()
                  .filter(var1x -> !this.W.test(var1x) && !dk.R(var1x))
                  .anyMatch(var7x -> dk.O(var8, var7x).map(var6x -> qy.F(var6x, var5, var6).contains(var1, var3, 0)).orElse(false));
      } else {
         return true;
      }
   }

   public boolean i(double var1, double var3, int var5, int var6, int var7, List<dk.i> var8, boolean var9) {
      if (this.K()) {
         return true;
      }

      if (var9) {
         this.o = null;
         return false;
      }

      if (this.E && this.T.R(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.o != null && this.o.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.o.y()) / 20;
         this.o = null;
         switch (var15) {
            case 0:
               this.N.a(true);
               this.x = (boolean)1;
               this.p();
               this.N = null;
               break;
            case 1:
               this.p();
               this.D.accept(this.N);
               break;
            case 2:
               this.N.O();
               this.x = (boolean)1;
               this.p();
               break;
            case 3:
               this.p();
         }

         return true;
      } else {
         this.o = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.d || !var1x.element().X()).toList();
         Optional var11 = dk.O(var10, this.N);
         dk.q var12 = this.E && var5 == 0
            ? dk.X()
               .stream()
               .filter(var1x -> !this.W.test(var1x) && !dk.R(var1x))
               .filter(var7x -> dk.O(var10, var7x).map(var6x -> qy.F(var6x, var6, var7).contains(var1, var3, 0)).orElse(false))
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.N = var12;
            var11 = dk.O(var10, this.N);
         }

         if (var5 == 0
            && this.N != null
            && !this.W.test(this.N)
            && !dk.R(this.N)
            && var11.isPresent()
            && qy.F((qy.i)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.A(this.N);
            this.k = this.N.f(var6);
            this.Y = this.N.p(var7);
            qy.i var16 = (qy.i)var11.get();
            this.y = new qy.w(var16, var1, var3, this.N.F());
            this.i = (var16.x() - this.N.f(var6)) / this.N.F();
            this.C = (var16.y() - this.N.p(var7)) / this.N.F();
            return true;
         }

         dk.i var13 = qy.l(var10, var0 -> true, dk.i::bounds, var1, var3);
         if (var13 == null) {
            this.N = null;
            if (this.E && var5 == 1) {
               this.T.w((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.N = var13.element();
            _q.c("HUD editor", "Selected {} in {}", this.N.d, this.d ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.E) {
                  this.T.o(this.N, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.B.isEmpty() ? 60 : 80;
               this.o = new qy.i(qy.k((int)var1, 142, var6), qy.k((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
            } else {
               if (this.W.test(this.N)) {
                  return true;
               }

               this.A(this.N);
               if (dk.R(this.N)) {
                  this.N.o(var13.bounds().x(), var13.bounds().y(), var6, var7);
                  this.N.I((String)null);
                  dk.z(this.N);
                  _q.c("HUD editor", "Lifted {} out of {}", this.N.d, dk.M(this.N.d));
               }

               this.k = this.N.f(var6);
               this.Y = this.N.p(var7);
               this.R = new qy.a(var1 - this.N.f(var6), var3 - this.N.p(var7));
               this.U.O(this.N.f(var6), this.N.p(var7));
               this.H = var1;
               this.G = var3;
               this.j = (boolean)0;
               this.I = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void A(dk.q var1) {
      this.w = var1;
      this.z = -1;
      this.M = bh.Z.get(var1.d);
      this.c = bh.F.get(var1.d);
   }

   public boolean p(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.N(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean N(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.E && var5 == 0 && this.T.a(var1, var3, var6, var7)) {
         return true;
      }

      if (this.K() && var5 == 0) {
         if (this.y != null) {
            this.w.m(this.E ? qp.D(this.y, var1, var3) : this.y.scale(var1, var3));
            this.w.o((int)Math.round(this.y.bounds().x() - this.i * this.w.F()), (int)Math.round(this.y.bounds().y() - this.C * this.w.F()), var6, var7);
         } else {
            if (this.E) {
               this.H = var1;
               this.G = var3;
               this.j = var8;
               return true;
            }

            qy.o var10 = this.R.move(var1, var3, this.w.o(var6, var7), var6, var7, bh.z.snapping(), bh.z.grid(), var8, this.b, this.n);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.d && var9) {
               int var13 = var11 - this.k;
               int var14 = var12 - this.Y;
               if (this.z < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.z = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.z == 0) {
                  var12 = this.Y;
               } else if (this.z == 1) {
                  var11 = this.k;
               }
            } else {
               this.z = -1;
            }

            this.w.o(var11, var12, var6, var7);
            this.b = var9 && this.z == 1 ? -1 : var10.guideX();
            this.n = var9 && this.z == 0 ? -1 : var10.guideY();
            this.C(var6, var7, var8);
         }

         return true;
      } else {
         return false;
      }
   }

   private void C(int var1, int var2, boolean var3) {
      if (this.w != null && dk.M(this.w.d) != null) {
         int var4 = this.y == null && !var3 && dk.Z(this.w, var1, var2) ? 1 : 0;
         dk.z(var4 != 0 ? this.w : null);
         if (var4 != 0) {
            this.b = this.n = -1;
         }
      }
   }

   public boolean L(int var1) {
      if (this.E && var1 == 0 && this.T.p()) {
         return true;
      } else if (this.K() && var1 == 0) {
         this.C();
         return true;
      } else {
         return false;
      }
   }

   private void C() {
      if (this.w != null && dk.u() == this.w) {
         String var1 = dk.M(this.w.d);
         this.w.I(var1);
         _q.z("HUD editor", "Merged {} into {}", this.w.d, var1);
      }

      dk.z(null);
      if (this.w != null) {
         this.x = (boolean)(this.x | (Objects.equals(this.M, bh.Z.get(this.w.d)) && Objects.equals(this.c, bh.F.get(this.w.d)) ? 0 : 1));
         _q.c("HUD editor", "Released {} gesture for {}", this.y == null ? "move" : "scale", this.w.d);
      }

      this.w = null;
      this.R = null;
      this.y = null;
      this.M = null;
      this.c = null;
      this.b = this.n = -1;
      this.p();
   }

   private void p() {
      if (this.x) {
         if (this.V.getAsBoolean()) {
            this.x = (boolean)0;
            this.B = "";
            _q.c("HUD editor", "Committed {} edit to active profile", this.d ? "workspace" : "chat");
         } else {
            if (this.B.isEmpty()) {
               this.f.run();
            }

            this.B = "HUD save failed. Retry save in the menu.";
            _q.R("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void h(boolean var1, boolean var2) {
      if (this.e != bh.k) {
         this.T.l();
         this.w = null;
         this.R = null;
         this.y = null;
         this.x = (boolean)0;
         this.B = "";
         dk.z(null);
         this.b = this.n = -1;
         this.N = null;
         this.o = null;
         this.e = bh.k;
         _q.c("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.K = (boolean)0;
      } else {
         if (!this.K) {
            this.I();
         }

         this.K = (boolean)1;
      }
   }

   public void I() {
      this.T.e();
      this.C();
      this.N = null;
      this.o = null;
   }

   public boolean N(int var1) {
      return this.E && this.T.V(var1);
   }

   public boolean z(int var1) {
      return this.E && this.T.m(var1);
   }

   public boolean U(double var1, double var3, double var5, boolean var7) {
      return this.E && !var7 && this.T.c(var1, var3, var5);
   }

   static void W(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var1 / 2;
      int var9 = var2 / 2;
      int var10 = var4 * var5;

      for (int var11 = qy.M(var1, var4); var11 < var1; var11 += var4) {
         if (var11 != var8) {
            h5.N(var0, var11, 0, 1, var2, (var11 - var8) % var10 == 0 ? var7 : var6);
         }
      }

      for (int var12 = qy.M(var2, var4); var12 < var3; var12 += var4) {
         if (var12 != var9) {
            h5.N(var0, 0, var12, var1, 1, (var12 - var9) % var10 == 0 ? var7 : var6);
         }
      }
   }

   static void f(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = var1 / 2;
      int var6 = var2 / 2;
      int var7 = vv.N(com.larpclient.c.y, 0.9);
      int var8 = 1224736767;
      h5.N(var0, var5, 0, 1, var2, var3 == var5 ? var7 : var8);
      h5.N(var0, 0, var6, var1, 1, var4 == var6 ? var7 : var8);
      if (var3 >= 0 && var3 != var5) {
         h5.N(var0, Math.min(var1 - 1, var3), 0, 1, var2, var7);
      }

      if (var4 >= 0 && var4 != var6) {
         h5.N(var0, 0, Math.min(var2 - 1, var4), var1, 1, var7);
      }
   }

   public void D(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.E && this.w != null && this.R != null) {
         long var7 = System.nanoTime();
         qy.o var9 = this.R.move(this.H, this.G, this.w.o(var4, var5), var4, var5, bh.z.snapping(), bh.z.grid(), this.j, this.b, this.n);
         qy.o var10 = this.U.D(var9.x(), var9.y(), Minecraft.getInstance().hasShiftDown(), (var7 - this.I) / 1.0E9);
         this.I = var7;
         this.w.o(var10.x(), var10.y(), var4, var5);
         this.b = var9.guideX();
         this.n = var9.guideY();
         this.C(var4, var5, this.j);
      }

      List var12 = dk.q();
      Optional var8 = dk.O(var12, this.N);
      if (this.N != null && var8.isEmpty() && !this.K()) {
         this.N = null;
         this.o = null;
      }

      dk.i var13 = var6 ? null : qy.l(var12, var0 -> true, dk.i::bounds, var2, var3);
      if (var13 != null && var13.element() != this.N) {
         d(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         qy.i var14 = (qy.i)var8.get();
         d(var1, var14, vv.N(com.larpclient.c.y, 0.8));
         if (!this.W.test(this.N) && !dk.R(this.N)) {
            qy.i var11 = qy.F(var14, var4, var5);
            h5.q(var1, var11.x(), var11.y(), var11.width(), var11.height(), 2.0F, com.larpclient.c.y);
         }
      }

      if (this.K()) {
         if (this.E) {
            W(var1, var4, var5, var5 - 14, 20, 4, 268435455, 587202559);
         }

         f(var1, var4, var5, this.b, this.n);
      }

      if (this.E) {
         for (dk.q var17 : dk.X()) {
            if (!this.W.test(var17) && !dk.R(var17)) {
               dk.O(var12, var17).ifPresent(var5x -> {
                  qy.i var6x = qy.F(var5x, var4, var5);
                  h5.q(var1, var6x.x(), var6x.y(), var6x.width(), var6x.height(), 2.0F, vv.N(com.larpclient.c.y, var6x.contains(var2, var3, 0) ? 0.6 : 0.35));
                  h5.N(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                  h5.N(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
               });
            }
         }

         this.T.g(var1, var2, var3);
      }

      if (this.o != null) {
         h5.q(var1, this.o.x(), this.o.y(), this.o.width(), this.o.height(), 6.0F, -267645934);
         String[] var16 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var18 = 0; var18 < this.o.height() / 20; var18++) {
            if (!var6 && this.o.contains(var2, var3, 0) && (var3 - this.o.y()) / 20 == var18) {
               h5.q(var1, this.o.x() + 2, this.o.y() + var18 * 20, this.o.width() - 4, 20.0F, 4.0F, 905969663);
            }

            wz.D(
               var1,
               wz.M(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var18]),
               this.o.x() + 7,
               this.o.y() + var18 * 20 + 4,
               12,
               com.larpclient.c.y
            );
            i5.i(var1, var16[var18], this.o.x() + 25, n8.b(this.o.y() + var18 * 20, 20.0, 11.0), 11.0, -1);
         }
      }
   }

   private static void d(GuiGraphicsExtractor var0, qy.i var1, int var2) {
      h5.L(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
