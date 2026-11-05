package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class __ {
   private __.a T;
   private boolean Z;
   private boolean K;
   private Vec3 F;

   public void g() {
      this.T = null;
      this.Z = this.K = (boolean)0;
      this.F = null;
   }

   public __.z d(__.a var1) {
      __.z var2 = this.J(var1);
      if (var1.eligible && var1.grounded && this.Z) {
         this.F = var1.position;
      }

      return var2;
   }

   public __.z J(__.a var1) {
      if (this.T != null
         && this.T.world == var1.world
         && this.T.player == var1.player
         && var1.eligible
         && this.T.eligible
         && !(this.T.position.distanceToSqr(var1.position) > 16.0)) {
         __.z var2 = __.z.NONE;
         if (var1.grounded) {
            if (this.Z && this.K) {
               var2 = new __.z(__.l.s, var1.position);
            }

            this.Z = (boolean)1;
            this.K = (boolean)0;
            this.F = null;
         } else if (this.Z) {
            if (this.F != null && var1.position.y > this.F.y + 0.001) {
               var2 = new __.z(__.l.y, this.F);
            }

            this.K = (boolean)(this.K | (this.T.position.distanceToSqr(var1.position) > 1.0E-6 ? 1 : 0));
            this.F = null;
         }

         this.T = var1;
         return var2;
      } else {
         this.g();
         this.T = var1;
         return __.z.NONE;
      }
   }

   public static boolean p(String var0, __.l var1) {
      return var1 != __.l.t && (var0.equals("Both") || var0.equals(var1 == __.l.y ? "Jump" : "Land"));
   }

   public record a(Object world, Object player, Vec3 position, boolean grounded, boolean eligible) {
   }

   public enum l {
      t,
      y,
      s;
   }

   public record z(__.l event, Vec3 source) {
      public static final __.z NONE = new __.z(__.l.t, Vec3.ZERO);
   }
}
