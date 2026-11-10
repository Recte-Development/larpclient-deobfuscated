package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class p3 {
   private p3.u O;
   private boolean L;
   private boolean q;
   private Vec3 m;

   public void S() {
      this.O = null;
      this.L = this.q = (boolean)0;
      this.m = null;
   }

   public p3.b a(p3.u var1) {
      p3.b var2 = this.C(var1);
      if (var1.eligible && var1.grounded && this.L) {
         this.m = var1.position;
      }

      return var2;
   }

   public p3.b C(p3.u var1) {
      if (this.O != null
         && this.O.world == var1.world
         && this.O.player == var1.player
         && var1.eligible
         && this.O.eligible
         && !(this.O.position.distanceToSqr(var1.position) > 16.0)) {
         p3.b var2 = p3.b.NONE;
         if (var1.grounded) {
            if (this.L && this.q) {
               var2 = new p3.b(p3.k.Z, var1.position);
            }

            this.L = (boolean)1;
            this.q = (boolean)0;
            this.m = null;
         } else if (this.L) {
            if (this.m != null && var1.position.y > this.m.y + 0.001) {
               var2 = new p3.b(p3.k.G, this.m);
            }

            this.q = (boolean)(this.q | (this.O.position.distanceToSqr(var1.position) > 1.0E-6 ? 1 : 0));
            this.m = null;
         }

         this.O = var1;
         return var2;
      } else {
         this.S();
         this.O = var1;
         return p3.b.NONE;
      }
   }

   public static boolean b(String var0, p3.k var1) {
      return var1 != p3.k.D && (var0.equals("Both") || var0.equals(var1 == p3.k.G ? "Jump" : "Land"));
   }

   public record b(p3.k event, Vec3 source) {
      public static final p3.b NONE = new p3.b(p3.k.D, Vec3.ZERO);
   }

   public enum k {
      D,
      G,
      Z;
   }

   public record u(Object world, Object player, Vec3 position, boolean grounded, boolean eligible) {
   }
}
