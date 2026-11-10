package com.larpclient;

import com.larpclient.module.ViewModelModule;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.class_1268;
import net.minecraft.class_332;

public final class tr {
   private static final long p = 750000000L;
   private final BooleanSupplier d;
   private class_1268 l;
   private class_1268 m;
   private bw.s e;
   private double n;
   private double K;
   private double k;
   private double i;
   private double v;
   private boolean t;
   private long L;

   public tr() {
      this(() -> {
         qk var0 = LarpClient.b().e();
         return var0.d(var0.d());
      });
   }

   public tr(BooleanSupplier var1) {
      this.d = var1;
   }

   private static ViewModelModule V() {
      ViewModelModule var0 = ViewModelModule.B();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean y() {
      return this.m != null;
   }

   public class_1268 r() {
      return this.l;
   }

   private class_1268 y(double var1, double var3) {
      if (this.l != null && i(this.l, var1, var3)) {
         return this.l;
      }

      for (class_1268 var8 : class_1268.values()) {
         if (i(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean i(class_1268 var0, double var1, double var3) {
      bw.s var5 = bw.n(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean l(double var1, double var3, int var5) {
      ViewModelModule var6 = V();
      if (this.y()) {
         return true;
      } else {
         class_1268 var7 = var6 == null ? null : this.y(var1, var3);
         if (var7 == null) {
            this.K();
            return false;
         } else if (var5 == 0) {
            this.e = bw.n(var7);
            this.l = this.m = var7;
            this.n = var1;
            this.K = var3;
            this.k = var6.n(var7);
            this.i = var6.w(var7);
            q8.v("ViewModel", "Dragging {} in chat", z(var7));
            return true;
         } else if (var5 == 1) {
            this.l = var7;
            var6.x(var7);
            this.o();
            this.I();
            q8.v("ViewModel", "Reset {} size and position from chat", z(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean d(double var1, double var3, int var5) {
      if (this.y() && var5 == 0) {
         ViewModelModule var6 = V();
         if (var6 == null) {
            this.p();
            return true;
         }

         if (this.e == null) {
            return true;
         }

         double var7 = (var1 - this.n) * this.e.unitsPerPixelX() * (this.e.left() ? -1 : 1);
         double var9 = -(var3 - this.K) * this.e.unitsPerPixelY();
         var6.G(this.m, this.k + var7, this.i + var9);
         this.o();
         return true;
      } else {
         return false;
      }
   }

   public boolean j(int var1) {
      if (this.y() && var1 == 0) {
         this.p();
         return true;
      } else {
         return false;
      }
   }

   public boolean k(double var1, double var3, double var5) {
      ViewModelModule var7 = V();
      if (var7 != null && this.l != null && i(this.l, var1, var3)) {
         this.v += var5;
         int var8 = (int)this.v;
         if (var8 != 0) {
            this.v -= var8;
            var7.U(this.l, var8);
            this.o();
         }

         return true;
      } else {
         return false;
      }
   }

   public void K() {
      if (this.y()) {
         this.p();
      }

      this.l = null;
      this.v = 0.0;
   }

   public void x(boolean var1) {
      if (!var1 && this.y()) {
         this.p();
      }
   }

   public void q() {
      this.K();
      this.I();
   }

   private void p() {
      this.m = null;
      this.e = null;
      this.I();
   }

   private void o() {
      this.t = (boolean)1;
      this.L = System.nanoTime();
   }

   private void I() {
      if (this.t) {
         if (this.d.getAsBoolean()) {
            this.t = (boolean)0;
            q8.v("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            q8.L("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void k(class_332 var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = V();
      if (var5 == null) {
         if (this.l != null || this.y()) {
            this.K();
         }
      } else {
         if (this.t && !this.y() && System.nanoTime() - this.L > 750000000L) {
            this.I();
         }

         class_1268 var6 = !var4 && !this.y() ? this.y(var2, var3) : null;
         ci.k(this.l, var6);

         for (class_1268 var10 : class_1268.values()) {
            bw.s var11 = bw.n(var10);
            if (var11 != null && (var10 == this.l || var10 == var6)) {
               String var12 = z(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.l(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               w5.y(var1, var12, Math.max(2, var11.x()), var13, 10.0, var10 == this.l ? -1 : -1325400065);
               if (var10 == this.l && !this.y()) {
                  w5.y(
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

   private static String z(class_1268 var0) {
      return var0 == class_1268.field_5810 ? "Offhand" : "Main hand";
   }
}
