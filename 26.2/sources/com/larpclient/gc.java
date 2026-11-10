package com.larpclient;

import com.larpclient.module.ViewModelModule;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionHand;

public final class gc {
   private static final long x = 750000000L;
   private final BooleanSupplier N;
   private InteractionHand s;
   private InteractionHand E;
   private s8.v j;
   private double k;
   private double P;
   private double t;
   private double r;
   private double S;
   private boolean B;
   private long p;

   public gc() {
      this(() -> {
         gg var0 = LarpClient.R().o();
         return var0.K(var0.M());
      });
   }

   public gc(BooleanSupplier var1) {
      this.N = var1;
   }

   private static ViewModelModule Y() {
      ViewModelModule var0 = ViewModelModule.b();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean i() {
      return this.E != null;
   }

   public InteractionHand X() {
      return this.s;
   }

   private InteractionHand B(double var1, double var3) {
      if (this.s != null && T(this.s, var1, var3)) {
         return this.s;
      }

      for (InteractionHand var8 : InteractionHand.values()) {
         if (T(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean T(InteractionHand var0, double var1, double var3) {
      s8.v var5 = s8.W(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean s(double var1, double var3, int var5) {
      ViewModelModule var6 = Y();
      if (this.i()) {
         return true;
      } else {
         InteractionHand var7 = var6 == null ? null : this.B(var1, var3);
         if (var7 == null) {
            this.w();
            return false;
         } else if (var5 == 0) {
            this.j = s8.W(var7);
            this.s = this.E = var7;
            this.k = var1;
            this.P = var3;
            this.t = var6.Q(var7);
            this.r = var6.h(var7);
            h.U("ViewModel", "Dragging {} in chat", u(var7));
            return true;
         } else if (var5 == 1) {
            this.s = var7;
            var6.n(var7);
            this.U();
            this.W();
            h.U("ViewModel", "Reset {} size and position from chat", u(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean B(double var1, double var3, int var5) {
      if (this.i() && var5 == 0) {
         ViewModelModule var6 = Y();
         if (var6 == null) {
            this.K();
            return true;
         }

         if (this.j == null) {
            return true;
         }

         double var7 = (var1 - this.k) * this.j.unitsPerPixelX() * (this.j.left() ? -1 : 1);
         double var9 = -(var3 - this.P) * this.j.unitsPerPixelY();
         var6.c(this.E, this.t + var7, this.r + var9);
         this.U();
         return true;
      } else {
         return false;
      }
   }

   public boolean I(int var1) {
      if (this.i() && var1 == 0) {
         this.K();
         return true;
      } else {
         return false;
      }
   }

   public boolean d(double var1, double var3, double var5) {
      ViewModelModule var7 = Y();
      if (var7 != null && this.s != null && T(this.s, var1, var3)) {
         this.S += var5;
         int var8 = (int)this.S;
         if (var8 != 0) {
            this.S -= var8;
            var7.H(this.s, var8);
            this.U();
         }

         return true;
      } else {
         return false;
      }
   }

   public void w() {
      if (this.i()) {
         this.K();
      }

      this.s = null;
      this.S = 0.0;
   }

   public void Q(boolean var1) {
      if (!var1 && this.i()) {
         this.K();
      }
   }

   public void D() {
      this.w();
      this.W();
   }

   private void K() {
      this.E = null;
      this.j = null;
      this.W();
   }

   private void U() {
      this.B = (boolean)1;
      this.p = System.nanoTime();
   }

   private void W() {
      if (this.B) {
         if (this.N.getAsBoolean()) {
            this.B = (boolean)0;
            h.U("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            h.g("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void p(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = Y();
      if (var5 == null) {
         if (this.s != null || this.i()) {
            this.w();
         }
      } else {
         if (this.B && !this.i() && System.nanoTime() - this.p > 750000000L) {
            this.W();
         }

         InteractionHand var6 = !var4 && !this.i() ? this.B(var2, var3) : null;
         u8.w(this.s, var6);

         for (InteractionHand var10 : InteractionHand.values()) {
            s8.v var11 = s8.W(var10);
            if (var11 != null && (var10 == this.s || var10 == var6)) {
               String var12 = u(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.F(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               gw.S(var1, var12, Math.max(2, var11.x()), var13, 10.0, var10 == this.s ? -1 : -1325400065);
               if (var10 == this.s && !this.i()) {
                  gw.S(
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

   private static String u(InteractionHand var0) {
      return var0 == InteractionHand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
