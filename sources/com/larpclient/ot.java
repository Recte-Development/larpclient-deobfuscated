package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class ot {
   private ot.v X;
   private boolean I;
   private boolean H;
   private Vec3 p;

   public void M() {
      this.X = null;
      this.I = this.H = (boolean)0;
      this.p = null;
   }

   public ot.q C(ot.v var1) {
      ot.q var2 = this.Q(var1);
      if (var1.eligible && var1.grounded && this.I) {
         this.p = var1.position;
      }

      return var2;
   }

   public ot.q Q(ot.v var1) {
      if (this.X != null
         && this.X.world == var1.world
         && this.X.player == var1.player
         && var1.eligible
         && this.X.eligible
         && !(this.X.position.distanceToSqr(var1.position) > 16.0)) {
         ot.q var2 = ot.q.NONE;
         if (var1.grounded) {
            if (this.I && this.H) {
               var2 = new ot.q(ot.c.A, var1.position);
            }

            this.I = (boolean)1;
            this.H = (boolean)0;
            this.p = null;
         } else if (this.I) {
            if (this.p != null && var1.position.y > this.p.y + 0.001) {
               var2 = new ot.q(ot.c.R, this.p);
            }

            this.H = (boolean)(this.H | (this.X.position.distanceToSqr(var1.position) > 1.0E-6 ? 1 : 0));
            this.p = null;
         }

         this.X = var1;
         return var2;
      } else {
         this.M();
         this.X = var1;
         return ot.q.NONE;
      }
   }

   public static boolean P(String var0, ot.c var1) {
      return var1 != ot.c.i && (var0.equals("Both") || var0.equals(var1 == ot.c.R ? "Jump" : "Land"));
   }

   public enum c {
      i,
      R,
      A;
   }

   public record q(ot.c event, Vec3 source) {
      public static final ot.q NONE = new ot.q(ot.c.i, Vec3.ZERO);
   }

   public record v(Object world, Object player, Vec3 position, boolean grounded, boolean eligible) {
   }
}
