package com.larpclient;

import com.larpclient.module.ViewModelModule;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionHand;

public final class jo {
   private static final long v = 750000000L;
   private final BooleanSupplier O;
   private InteractionHand A;
   private InteractionHand u;
   private jp.c b;
   private double r;
   private double U;
   private double T;
   private double z;
   private double W;
   private boolean P;
   private long Y;

   public jo() {
      this(() -> {
         aq var0 = LarpClient.N().h();
         return var0.Q(var0.S());
      });
   }

   public jo(BooleanSupplier var1) {
      this.O = var1;
   }

   private static ViewModelModule C() {
      ViewModelModule var0 = ViewModelModule.t();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean V() {
      return this.u != null;
   }

   public InteractionHand V() {
      return this.A;
   }

   private InteractionHand j(double var1, double var3) {
      if (this.A != null && f(this.A, var1, var3)) {
         return this.A;
      }

      for (InteractionHand var8 : InteractionHand.values()) {
         if (f(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean f(InteractionHand var0, double var1, double var3) {
      jp.c var5 = jp.t(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean Z(double var1, double var3, int var5) {
      ViewModelModule var6 = C();
      if (this.V()) {
         return true;
      } else {
         InteractionHand var7 = var6 == null ? null : this.j(var1, var3);
         if (var7 == null) {
            this.m();
            return false;
         } else if (var5 == 0) {
            this.b = jp.t(var7);
            this.A = this.u = var7;
            this.r = var1;
            this.U = var3;
            this.T = var6.b(var7);
            this.z = var6.F(var7);
            _q.c("ViewModel", "Dragging {} in chat", N(var7));
            return true;
         } else if (var5 == 1) {
            this.A = var7;
            var6.R(var7);
            this.Z();
            this.W();
            _q.c("ViewModel", "Reset {} size and position from chat", N(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean E(double var1, double var3, int var5) {
      if (this.V() && var5 == 0) {
         ViewModelModule var6 = C();
         if (var6 == null) {
            this.p();
            return true;
         }

         if (this.b == null) {
            return true;
         }

         double var7 = (var1 - this.r) * this.b.unitsPerPixelX() * (this.b.left() ? -1 : 1);
         double var9 = -(var3 - this.U) * this.b.unitsPerPixelY();
         var6.C(this.u, this.T + var7, this.z + var9);
         this.Z();
         return true;
      } else {
         return false;
      }
   }

   public boolean q(int var1) {
      if (this.V() && var1 == 0) {
         this.p();
         return true;
      } else {
         return false;
      }
   }

   public boolean t(double var1, double var3, double var5) {
      ViewModelModule var7 = C();
      if (var7 != null && this.A != null && f(this.A, var1, var3)) {
         this.W += var5;
         int var8 = (int)this.W;
         if (var8 != 0) {
            this.W -= var8;
            var7.i(this.A, var8);
            this.Z();
         }

         return true;
      } else {
         return false;
      }
   }

   public void m() {
      if (this.V()) {
         this.p();
      }

      this.A = null;
      this.W = 0.0;
   }

   public void u(boolean var1) {
      if (!var1 && this.V()) {
         this.p();
      }
   }

   public void Q() {
      this.m();
      this.W();
   }

   private void p() {
      this.u = null;
      this.b = null;
      this.W();
   }

   private void Z() {
      this.P = (boolean)1;
      this.Y = System.nanoTime();
   }

   private void W() {
      if (this.P) {
         if (this.O.getAsBoolean()) {
            this.P = (boolean)0;
            _q.c("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            _q.R("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void I(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = C();
      if (var5 == null) {
         if (this.A != null || this.V()) {
            this.m();
         }
      } else {
         if (this.P && !this.V() && System.nanoTime() - this.Y > 750000000L) {
            this.W();
         }

         InteractionHand var6 = !var4 && !this.V() ? this.j(var2, var3) : null;
         bp.C(this.A, var6);

         for (InteractionHand var10 : InteractionHand.values()) {
            jp.c var11 = jp.t(var10);
            if (var11 != null && (var10 == this.A || var10 == var6)) {
               String var12 = N(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.q(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               i5.i(var1, var12, Math.max(2, var11.x()), var13, 10.0, var10 == this.A ? -1 : -1325400065);
               if (var10 == this.A && !this.V()) {
                  i5.i(
                     var1,
                     "Drag to move, scroll to resize, right-click to reset",
                     Math.max(2, var11.x()),
                     var11.y() > 16 ? var13 - 11 : var13 + 11,
                     9.0,
                     -1694498817
                  );
               }
            }
         }
      }
   }

   private static String N(InteractionHand var0) {
      return var0 == InteractionHand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
