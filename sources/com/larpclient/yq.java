package com.larpclient;

import com.larpclient.module.ViewModelModule;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionHand;

public final class yq {
   private static final long i = 750000000L;
   private final BooleanSupplier s;
   private InteractionHand m;
   private InteractionHand I;
   private fg.v H;
   private double p;
   private double b;
   private double D;
   private double J;
   private double o;
   private boolean V;
   private long g;

   public yq() {
      this(() -> {
         mo var0 = LarpClient.y().v();
         return var0.a(var0.o());
      });
   }

   public yq(BooleanSupplier var1) {
      this.s = var1;
   }

   private static ViewModelModule l() {
      ViewModelModule var0 = ViewModelModule.p();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean Z() {
      return this.I != null;
   }

   public InteractionHand C() {
      return this.m;
   }

   private InteractionHand G(double var1, double var3) {
      if (this.m != null && z(this.m, var1, var3)) {
         return this.m;
      }

      for (InteractionHand var8 : InteractionHand.values()) {
         if (z(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean z(InteractionHand var0, double var1, double var3) {
      fg.v var5 = fg.T(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean f(double var1, double var3, int var5) {
      ViewModelModule var6 = l();
      if (this.Z()) {
         return true;
      } else {
         InteractionHand var7 = var6 == null ? null : this.G(var1, var3);
         if (var7 == null) {
            this.a();
            return false;
         } else if (var5 == 0) {
            this.H = fg.T(var7);
            this.m = this.I = var7;
            this.p = var1;
            this.b = var3;
            this.D = var6.Y(var7);
            this.J = var6.F(var7);
            r4.g("ViewModel", "Dragging {} in chat", H(var7));
            return true;
         } else if (var5 == 1) {
            this.m = var7;
            var6.O(var7);
            this.B();
            this.R();
            r4.g("ViewModel", "Reset {} size and position from chat", H(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean W(double var1, double var3, int var5) {
      if (this.Z() && var5 == 0) {
         ViewModelModule var6 = l();
         if (var6 == null) {
            this.r();
            return true;
         }

         if (this.H == null) {
            return true;
         }

         double var7 = (var1 - this.p) * this.H.unitsPerPixelX() * (this.H.left() ? -1 : 1);
         double var9 = -(var3 - this.b) * this.H.unitsPerPixelY();
         var6.g(this.I, this.D + var7, this.J + var9);
         this.B();
         return true;
      } else {
         return false;
      }
   }

   public boolean q(int var1) {
      if (this.Z() && var1 == 0) {
         this.r();
         return true;
      } else {
         return false;
      }
   }

   public boolean z(double var1, double var3, double var5) {
      ViewModelModule var7 = l();
      if (var7 != null && this.m != null && z(this.m, var1, var3)) {
         this.o += var5;
         int var8 = (int)this.o;
         if (var8 != 0) {
            this.o -= var8;
            var7.b(this.m, var8);
            this.B();
         }

         return true;
      } else {
         return false;
      }
   }

   public void a() {
      if (this.Z()) {
         this.r();
      }

      this.m = null;
      this.o = 0.0;
   }

   public void j(boolean var1) {
      if (!var1 && this.Z()) {
         this.r();
      }
   }

   public void Q() {
      this.a();
      this.R();
   }

   private void r() {
      this.I = null;
      this.H = null;
      this.R();
   }

   private void B() {
      this.V = (boolean)1;
      this.g = System.nanoTime();
   }

   private void R() {
      if (this.V) {
         if (this.s.getAsBoolean()) {
            this.V = (boolean)0;
            r4.g("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            r4.m("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void a(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = l();
      if (var5 == null) {
         if (this.m != null || this.Z()) {
            this.a();
         }
      } else {
         if (this.V && !this.Z() && System.nanoTime() - this.g > 750000000L) {
            this.R();
         }

         InteractionHand var6 = !var4 && !this.Z() ? this.G(var2, var3) : null;
         _5.e(this.m, var6);

         for (InteractionHand var10 : InteractionHand.values()) {
            fg.v var11 = fg.T(var10);
            if (var11 != null && (var10 == this.m || var10 == var6)) {
               String var12 = H(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.X(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               rd.v(var1, var12, Math.max(2, var11.x()), var13, 10.0, var10 == this.m ? -1 : -1325400065);
               if (var10 == this.m && !this.Z()) {
                  rd.v(
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

   private static String H(InteractionHand var0) {
      return var0 == InteractionHand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
