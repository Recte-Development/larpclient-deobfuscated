package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class f6 {
   private f6.i d;
   private boolean z;
   private boolean A;
   private Vec3 T;

   public void r() {
      this.d = null;
      this.z = this.A = (boolean)0;
      this.T = null;
   }

   public f6.t O(f6.i var1) {
      f6.t var2 = this.i(var1);
      if (var1.eligible && var1.grounded && this.z) {
         this.T = var1.position;
      }

      return var2;
   }

   public f6.t i(f6.i var1) {
      if (this.d != null
         && this.d.world == var1.world
         && this.d.player == var1.player
         && var1.eligible
         && this.d.eligible
         && !(this.d.position.distanceToSqr(var1.position) > 16.0)) {
         f6.t var2 = f6.t.NONE;
         if (var1.grounded) {
            if (this.z && this.A) {
               var2 = new f6.t(f6.o.G, var1.position);
            }

            this.z = (boolean)1;
            this.A = (boolean)0;
            this.T = null;
         } else if (this.z) {
            if (this.T != null && var1.position.y > this.T.y + 0.001) {
               var2 = new f6.t(f6.o.l, this.T);
            }

            this.A = (boolean)(this.A | (this.d.position.distanceToSqr(var1.position) > 1.0E-6 ? 1 : 0));
            this.T = null;
         }

         this.d = var1;
         return var2;
      } else {
         this.r();
         this.d = var1;
         return f6.t.NONE;
      }
   }

   public static boolean M(String var0, f6.o var1) {
      return var1 != f6.o.w && (var0.equals("Both") || var0.equals(var1 == f6.o.l ? "Jump" : "Land"));
   }

   public record i(Object world, Object player, Vec3 position, boolean grounded, boolean eligible) {
   }

   public enum o {
      w,
      l,
      G;
   }

   public record t(f6.o event, Vec3 source) {
      public static final f6.t NONE = new f6.t(f6.o.w, Vec3.ZERO);
   }
}
