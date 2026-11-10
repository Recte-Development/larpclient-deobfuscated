package com.larpclient;

import net.minecraft.class_243;

public final class fc {
   private fc.n t;
   private boolean N;
   private boolean e;
   private class_243 h;

   public void g() {
      this.t = null;
      this.N = this.e = (boolean)0;
      this.h = null;
   }

   public fc.y M(fc.n var1) {
      fc.y var2 = this.r(var1);
      if (var1.eligible && var1.grounded && this.N) {
         this.h = var1.position;
      }

      return var2;
   }

   public fc.y r(fc.n var1) {
      if (this.t != null
         && this.t.world == var1.world
         && this.t.player == var1.player
         && var1.eligible
         && this.t.eligible
         && !(this.t.position.method_1025(var1.position) > 16.0)) {
         fc.y var2 = fc.y.NONE;
         if (var1.grounded) {
            if (this.N && this.e) {
               var2 = new fc.y(fc.z.W, var1.position);
            }

            this.N = (boolean)1;
            this.e = (boolean)0;
            this.h = null;
         } else if (this.N) {
            if (this.h != null && var1.position.field_1351 > this.h.field_1351 + 0.001) {
               var2 = new fc.y(fc.z.Z, this.h);
            }

            this.e = (boolean)(this.e | (this.t.position.method_1025(var1.position) > 1.0E-6 ? 1 : 0));
            this.h = null;
         }

         this.t = var1;
         return var2;
      } else {
         this.g();
         this.t = var1;
         return fc.y.NONE;
      }
   }

   public static boolean T(String var0, fc.z var1) {
      return var1 != fc.z.Q && (var0.equals("Both") || var0.equals(var1 == fc.z.Z ? "Jump" : "Land"));
   }

   public record n(Object world, Object player, class_243 position, boolean grounded, boolean eligible) {
   }

   public record y(fc.z event, class_243 source) {
      public static final fc.y NONE = new fc.y(fc.z.Q, class_243.field_1353);
   }

   public enum z {
      Q,
      Z,
      W;
   }
}
