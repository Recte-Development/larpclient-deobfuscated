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

public final class s4 {
   private final BooleanSupplier T;
   private final Consumer<m4.p> m;
   private final Runnable O;
   private m4.p K;
   private m4.p c;
   private hw.e A;
   private hw.p k;
   private c7.q r;
   private c7.l W;
   private hw.l V;
   private int o = -1;
   private int H = -1;
   private long C = c7.w;
   private boolean a;
   private boolean Q;
   private double l;
   private double G;
   private String d = "";
   private boolean f;
   private final m2 y = new m2(() -> this.a = (boolean)1, this::O);
   private final ux Z = new ux();
   private double J;
   private double F;
   private boolean n;
   private long z;
   private boolean D;
   private Predicate<m4.p> N = var0 -> false;
   private int R;
   private int M;
   private int X = -1;

   public s4() {
      this(
         () -> {
            gg var0 = LarpClient.R().o();
            return var0.K(var0.M());
         },
         var0 -> {
            Minecraft var1 = Minecraft.getInstance();
            var1.gui.setScreen(hf.o(var1.gui.screen(), var0));
         },
         () -> Minecraft.getInstance()
            .gui
            .hud
            .getChat()
            .addClientSystemMessage(Component.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.f = (boolean)1;
      this.N = var0 -> LarpClient.R().o().g().t(var0.r);
   }

   public s4(BooleanSupplier var1, Consumer<m4.p> var2, Runnable var3) {
      this.T = var1;
      this.m = var2;
      this.O = var3;
   }

   public void A(Predicate<m4.p> var1) {
      this.D = (boolean)1;
      this.N = Objects.requireNonNull(var1);
   }

   public void H(m4.p var1) {
      if (this.H()) {
         this.l();
      }

      this.K = var1;
      this.V = null;
   }

   public int o() {
      return this.o;
   }

   public int L() {
      return this.H;
   }

   public m4.p R() {
      return this.K;
   }

   public void m() {
      this.V = null;
      this.y.H();
   }

   public boolean H() {
      return this.c != null;
   }

   public boolean b(double var1, double var3, int var5, int var6, List<m4.k> var7) {
      if (!this.H() && (this.V == null || !this.V.contains(var1, var3, 0))) {
         if (this.f && this.y.H() && this.y.A().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.D || !var1x.element().k()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.f
               && m4.P()
                  .stream()
                  .filter(var1x -> !this.N.test(var1x) && !m4.J(var1x))
                  .anyMatch(var7x -> m4.J(var8, var7x).map(var6x -> hw.E(var6x, var5, var6).contains(var1, var3, 0)).orElse(false));
      } else {
         return true;
      }
   }

   public boolean R(double var1, double var3, int var5, int var6, int var7, List<m4.k> var8, boolean var9) {
      if (this.H()) {
         return true;
      }

      if (var9) {
         this.V = null;
         return false;
      }

      if (this.f && this.y.E(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.V != null && this.V.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.V.y()) / 20;
         this.V = null;
         switch (var15) {
            case 0:
               this.K.z(true);
               this.a = (boolean)1;
               this.O();
               this.K = null;
               break;
            case 1:
               this.O();
               this.m.accept(this.K);
               break;
            case 2:
               this.K.g();
               this.a = (boolean)1;
               this.O();
               break;
            case 3:
               this.O();
         }

         return true;
      } else {
         this.V = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.D || !var1x.element().k()).toList();
         Optional var11 = m4.J(var10, this.K);
         m4.p var12 = this.f && var5 == 0
            ? m4.P()
               .stream()
               .filter(var1x -> !this.N.test(var1x) && !m4.J(var1x))
               .filter(var7x -> m4.J(var10, var7x).map(var6x -> hw.E(var6x, var6, var7).contains(var1, var3, 0)).orElse(false))
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.K = var12;
            var11 = m4.J(var10, this.K);
         }

         if (var5 == 0
            && this.K != null
            && !this.N.test(this.K)
            && !m4.J(this.K)
            && var11.isPresent()
            && hw.E((hw.l)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.C(this.K);
            this.R = this.K.T(var6);
            this.M = this.K.P(var7);
            hw.l var16 = (hw.l)var11.get();
            this.k = new hw.p(var16, var1, var3, this.K.s());
            this.l = (var16.x() - this.K.T(var6)) / this.K.s();
            this.G = (var16.y() - this.K.P(var7)) / this.K.s();
            return true;
         }

         m4.k var13 = hw.u(var10, var0 -> true, m4.k::bounds, var1, var3);
         if (var13 == null) {
            this.K = null;
            if (this.f && var5 == 1) {
               this.y.H((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.K = var13.element();
            h.U("HUD editor", "Selected {} in {}", this.K.r, this.D ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.f) {
                  this.y.z(this.K, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.d.isEmpty() ? 60 : 80;
               this.V = new hw.l(hw.m((int)var1, 142, var6), hw.m((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
            } else {
               if (this.N.test(this.K)) {
                  return true;
               }

               this.C(this.K);
               if (m4.J(this.K)) {
                  this.K.E(var13.bounds().x(), var13.bounds().y(), var6, var7);
                  this.K.t(null);
                  m4.A(this.K);
                  h.U("HUD editor", "Lifted {} out of {}", this.K.r, m4.J(this.K.r));
               }

               this.R = this.K.T(var6);
               this.M = this.K.P(var7);
               this.A = new hw.e(var1 - this.K.T(var6), var3 - this.K.P(var7));
               this.Z.o(this.K.T(var6), this.K.P(var7));
               this.J = var1;
               this.F = var3;
               this.n = (boolean)0;
               this.z = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void C(m4.p var1) {
      this.c = var1;
      this.X = -1;
      this.r = c7.v.get(var1.r);
      this.W = c7.y.get(var1.r);
   }

   public boolean J(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.t(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean t(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.f && var5 == 0 && this.y.R(var1, var3, var6, var7)) {
         return true;
      }

      if (this.H() && var5 == 0) {
         if (this.k != null) {
            this.c.f(this.f ? ux.S(this.k, var1, var3) : this.k.scale(var1, var3));
            this.c.E((int)Math.round(this.k.bounds().x() - this.l * this.c.s()), (int)Math.round(this.k.bounds().y() - this.G * this.c.s()), var6, var7);
         } else {
            if (this.f) {
               this.J = var1;
               this.F = var3;
               this.n = var8;
               return true;
            }

            hw.b var10 = this.A.move(var1, var3, this.c.S(var6, var7), var6, var7, c7.B.snapping(), c7.B.grid(), var8, this.o, this.H);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.D && var9) {
               int var13 = var11 - this.R;
               int var14 = var12 - this.M;
               if (this.X < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.X = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.X == 0) {
                  var12 = this.M;
               } else if (this.X == 1) {
                  var11 = this.R;
               }
            } else {
               this.X = -1;
            }

            this.c.E(var11, var12, var6, var7);
            this.o = var9 && this.X == 1 ? -1 : var10.guideX();
            this.H = var9 && this.X == 0 ? -1 : var10.guideY();
            this.k(var6, var7, var8);
         }

         return true;
      } else {
         return false;
      }
   }

   private void k(int var1, int var2, boolean var3) {
      if (this.c != null && m4.J(this.c.r) != null) {
         int var4 = this.k == null && !var3 && m4.m(this.c, var1, var2) ? 1 : 0;
         m4.A(var4 != 0 ? this.c : null);
         if (var4 != 0) {
            this.o = this.H = -1;
         }
      }
   }

   public boolean O(int var1) {
      if (this.f && var1 == 0 && this.y.a()) {
         return true;
      } else if (this.H() && var1 == 0) {
         this.l();
         return true;
      } else {
         return false;
      }
   }

   private void l() {
      if (this.c != null && m4.C() == this.c) {
         String var1 = m4.J(this.c.r);
         this.c.t(var1);
         h.A("HUD editor", "Merged {} into {}", this.c.r, var1);
      }

      m4.A(null);
      if (this.c != null) {
         this.a = (boolean)(this.a | (Objects.equals(this.r, c7.v.get(this.c.r)) && Objects.equals(this.W, c7.y.get(this.c.r)) ? 0 : 1));
         h.U("HUD editor", "Released {} gesture for {}", this.k == null ? "move" : "scale", this.c.r);
      }

      this.c = null;
      this.A = null;
      this.k = null;
      this.r = null;
      this.W = null;
      this.o = this.H = -1;
      this.O();
   }

   private void O() {
      if (this.a) {
         if (this.T.getAsBoolean()) {
            this.a = (boolean)0;
            this.d = "";
            h.U("HUD editor", "Committed {} edit to active profile", this.D ? "workspace" : "chat");
         } else {
            if (this.d.isEmpty()) {
               this.O.run();
            }

            this.d = "HUD save failed. Retry save in the menu.";
            h.g("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void q(boolean var1, boolean var2) {
      if (this.C != c7.w) {
         this.y.g();
         this.c = null;
         this.A = null;
         this.k = null;
         this.a = (boolean)0;
         this.d = "";
         m4.A(null);
         this.o = this.H = -1;
         this.K = null;
         this.V = null;
         this.C = c7.w;
         h.U("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.Q = (boolean)0;
      } else {
         if (!this.Q) {
            this.N();
         }

         this.Q = (boolean)1;
      }
   }

   public void N() {
      this.y.H();
      this.l();
      this.K = null;
      this.V = null;
   }

   public boolean T(int var1) {
      return this.f && this.y.U(var1);
   }

   public boolean b(int var1) {
      return this.f && this.y.h(var1);
   }

   public boolean C(double var1, double var3, double var5, boolean var7) {
      return this.f && !var7 && this.y.m(var1, var3, var5);
   }

   static void P(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var1 / 2;
      int var9 = var2 / 2;
      int var10 = var4 * var5;

      for (int var11 = hw.E(var1, var4); var11 < var1; var11 += var4) {
         if (var11 != var8) {
            kv.d(var0, var11, 0, 1, var2, (var11 - var8) % var10 == 0 ? var7 : var6);
         }
      }

      for (int var12 = hw.E(var2, var4); var12 < var3; var12 += var4) {
         if (var12 != var9) {
            kv.d(var0, 0, var12, var1, 1, (var12 - var9) % var10 == 0 ? var7 : var6);
         }
      }
   }

   static void V(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = var1 / 2;
      int var6 = var2 / 2;
      int var7 = uh.H(ho.J, 0.9);
      int var8 = 1224736767;
      kv.d(var0, var5, 0, 1, var2, var3 == var5 ? var7 : var8);
      kv.d(var0, 0, var6, var1, 1, var4 == var6 ? var7 : var8);
      if (var3 >= 0 && var3 != var5) {
         kv.d(var0, Math.min(var1 - 1, var3), 0, 1, var2, var7);
      }

      if (var4 >= 0 && var4 != var6) {
         kv.d(var0, 0, Math.min(var2 - 1, var4), var1, 1, var7);
      }
   }

   public void I(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.f && this.c != null && this.A != null) {
         long var7 = System.nanoTime();
         hw.b var9 = this.A.move(this.J, this.F, this.c.S(var4, var5), var4, var5, c7.B.snapping(), c7.B.grid(), this.n, this.o, this.H);
         hw.b var10 = this.Z.D(var9.x(), var9.y(), Minecraft.getInstance().hasShiftDown(), (var7 - this.z) / 1.0E9);
         this.z = var7;
         this.c.E(var10.x(), var10.y(), var4, var5);
         this.o = var9.guideX();
         this.H = var9.guideY();
         this.k(var4, var5, this.n);
      }

      List var12 = m4.I();
      Optional var8 = m4.J(var12, this.K);
      if (this.K != null && var8.isEmpty() && !this.H()) {
         this.K = null;
         this.V = null;
      }

      m4.k var13 = var6 ? null : hw.u(var12, var0 -> true, m4.k::bounds, var2, var3);
      if (var13 != null && var13.element() != this.K) {
         y(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         hw.l var14 = (hw.l)var8.get();
         y(var1, var14, uh.H(ho.J, 0.8));
         if (!this.N.test(this.K) && !m4.J(this.K)) {
            hw.l var11 = hw.E(var14, var4, var5);
            kv.f(var1, var11.x(), var11.y(), var11.width(), var11.height(), 2.0F, ho.J);
         }
      }

      if (this.H()) {
         if (this.f) {
            P(var1, var4, var5, var5 - 14, 20, 4, 268435455, 587202559);
         }

         V(var1, var4, var5, this.o, this.H);
      }

      if (this.f) {
         for (m4.p var17 : m4.P()) {
            if (!this.N.test(var17) && !m4.J(var17)) {
               m4.J(var12, var17).ifPresent(var5x -> {
                  hw.l var6x = hw.E(var5x, var4, var5);
                  kv.f(var1, var6x.x(), var6x.y(), var6x.width(), var6x.height(), 2.0F, uh.H(ho.J, var6x.contains(var2, var3, 0) ? 0.6 : 0.35));
                  kv.d(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                  kv.d(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
               });
            }
         }

         this.y.J(var1, var2, var3);
      }

      if (this.V != null) {
         kv.f(var1, this.V.x(), this.V.y(), this.V.width(), this.V.height(), 6.0F, -267645934);
         String[] var16 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var18 = 0; var18 < this.V.height() / 20; var18++) {
            if (!var6 && this.V.contains(var2, var3, 0) && (var3 - this.V.y()) / 20 == var18) {
               kv.f(var1, this.V.x() + 2, this.V.y() + var18 * 20, this.V.width() - 4, 20.0F, 4.0F, 905969663);
            }

            ur.O(var1, ur.V(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var18]), this.V.x() + 7, this.V.y() + var18 * 20 + 4, 12, ho.J);
            gw.S(var1, var16[var18], this.V.x() + 25, c2.X(this.V.y() + var18 * 20, 20.0, 11.0), 11.0, -1);
         }
      }
   }

   private static void y(GuiGraphicsExtractor var0, hw.l var1, int var2) {
      kv.I(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
