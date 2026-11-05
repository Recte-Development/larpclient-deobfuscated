package com.larpclient;

import com.larpclient.module.ViewModelModule;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionHand;

public final class uw {
   private static final long O = 750000000L;
   private final BooleanSupplier t;
   private InteractionHand H;
   private InteractionHand M;
   private x3.n r;
   private double Z;
   private double x;
   private double C;
   private double B;
   private double V;
   private boolean f;
   private long l;

   public uw() {
      this(() -> {
         w9 var0 = LarpClient.F().f();
         return var0.E(var0.w());
      });
   }

   public uw(BooleanSupplier var1) {
      this.t = var1;
   }

   private static ViewModelModule k() {
      ViewModelModule var0 = ViewModelModule.Z();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean y() {
      return this.M != null;
   }

   public InteractionHand R() {
      return this.H;
   }

   private InteractionHand N(double var1, double var3) {
      if (this.H != null && s(this.H, var1, var3)) {
         return this.H;
      }

      for (InteractionHand var8 : InteractionHand.values()) {
         if (s(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean s(InteractionHand var0, double var1, double var3) {
      x3.n var5 = x3.i(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean A(double var1, double var3, int var5) {
      ViewModelModule var6 = k();
      if (this.y()) {
         return true;
      } else {
         InteractionHand var7 = var6 == null ? null : this.N(var1, var3);
         if (var7 == null) {
            this.O();
            return false;
         } else if (var5 == 0) {
            this.r = x3.i(var7);
            this.H = this.M = var7;
            this.Z = var1;
            this.x = var3;
            this.C = var6.u(var7);
            this.B = var6.w(var7);
            mt.U("ViewModel", "Dragging {} in chat", b(var7));
            return true;
         } else if (var5 == 1) {
            this.H = var7;
            var6.t(var7);
            this.S();
            this.L();
            mt.U("ViewModel", "Reset {} size and position from chat", b(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean a(double var1, double var3, int var5) {
      if (this.y() && var5 == 0) {
         ViewModelModule var6 = k();
         if (var6 == null) {
            this.p();
            return true;
         }

         if (this.r == null) {
            return true;
         }

         double var7 = (var1 - this.Z) * this.r.unitsPerPixelX() * (this.r.left() ? -1 : 1);
         double var9 = -(var3 - this.x) * this.r.unitsPerPixelY();
         var6.l(this.M, this.C + var7, this.B + var9);
         this.S();
         return true;
      } else {
         return false;
      }
   }

   public boolean p(int var1) {
      if (this.y() && var1 == 0) {
         this.p();
         return true;
      } else {
         return false;
      }
   }

   public boolean L(double var1, double var3, double var5) {
      ViewModelModule var7 = k();
      if (var7 != null && this.H != null && s(this.H, var1, var3)) {
         this.V += var5;
         int var8 = (int)this.V;
         if (var8 != 0) {
            this.V -= var8;
            var7.I(this.H, var8);
            this.S();
         }

         return true;
      } else {
         return false;
      }
   }

   public void O() {
      if (this.y()) {
         this.p();
      }

      this.H = null;
      this.V = 0.0;
   }

   public void b(boolean var1) {
      if (!var1 && this.y()) {
         this.p();
      }
   }

   public void g() {
      this.O();
      this.L();
   }

   private void p() {
      this.M = null;
      this.r = null;
      this.L();
   }

   private void S() {
      this.f = (boolean)1;
      this.l = System.nanoTime();
   }

   private void L() {
      if (this.f) {
         if (this.t.getAsBoolean()) {
            this.f = (boolean)0;
            mt.U("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            mt.T("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void A(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = k();
      if (var5 == null) {
         if (this.H != null || this.y()) {
            this.O();
         }
      } else {
         if (this.f && !this.y() && System.nanoTime() - this.l > 750000000L) {
            this.L();
         }

         InteractionHand var6 = !var4 && !this.y() ? this.N(var2, var3) : null;
         ek.z(this.H, var6);

         for (InteractionHand var10 : InteractionHand.values()) {
            x3.n var11 = x3.i(var10);
            if (var11 != null && (var10 == this.H || var10 == var6)) {
               String var12 = b(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.h(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               we.F(var1, var12, Math.max(2, var11.x()), var13, 10.0, var10 == this.H ? -1 : -1325400065);
               if (var10 == this.H && !this.y()) {
                  we.F(
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

   private static String b(InteractionHand var0) {
      return var0 == InteractionHand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
