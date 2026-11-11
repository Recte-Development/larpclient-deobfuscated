package com.larpclient.util.entity;

import net.minecraft.world.phys.Vec3;

public final class JumpLandTracker {
   private JumpLandTracker.Sample previous;
   private boolean wasGrounded;
   private boolean movedInAir;
   private Vec3 lastGroundPosition;

   public void reset() {
      this.previous = null;
      this.wasGrounded = this.movedInAir = (boolean)0;
      this.lastGroundPosition = null;
   }

   public JumpLandTracker.Result updateAndRecordGround(JumpLandTracker.Sample var1) {
      JumpLandTracker.Result var2 = this.update(var1);
      if (var1.eligible && var1.grounded && this.wasGrounded) {
         this.lastGroundPosition = var1.position;
      }

      return var2;
   }

   public JumpLandTracker.Result update(JumpLandTracker.Sample var1) {
      if (this.previous != null
         && this.previous.world == var1.world
         && this.previous.player == var1.player
         && var1.eligible
         && this.previous.eligible
         && !(this.previous.position.distanceToSqr(var1.position) > 16.0)) {
         JumpLandTracker.Result var2 = JumpLandTracker.Result.NONE;
         if (var1.grounded) {
            if (this.wasGrounded && this.movedInAir) {
               var2 = new JumpLandTracker.Result(JumpLandTracker.Event.LAND, var1.position);
            }

            this.wasGrounded = (boolean)1;
            this.movedInAir = (boolean)0;
            this.lastGroundPosition = null;
         } else if (this.wasGrounded) {
            if (this.lastGroundPosition != null && var1.position.y > this.lastGroundPosition.y + 0.001) {
               var2 = new JumpLandTracker.Result(JumpLandTracker.Event.JUMP, this.lastGroundPosition);
            }

            this.movedInAir = (boolean)(this.movedInAir | (this.previous.position.distanceToSqr(var1.position) > 1.0E-6 ? 1 : 0));
            this.lastGroundPosition = null;
         }

         this.previous = var1;
         return var2;
      } else {
         this.reset();
         this.previous = var1;
         return JumpLandTracker.Result.NONE;
      }
   }

   public static boolean matchesMode(String var0, JumpLandTracker.Event var1) {
      return var1 != JumpLandTracker.Event.NONE && (var0.equals("Both") || var0.equals(var1 == JumpLandTracker.Event.JUMP ? "Jump" : "Land"));
   }

   public enum Event {
      NONE,
      JUMP,
      LAND;
   }

   public record Result(JumpLandTracker.Event event, Vec3 source) {
      public static final JumpLandTracker.Result NONE = new JumpLandTracker.Result(JumpLandTracker.Event.NONE, Vec3.ZERO);
   }

   public record Sample(Object world, Object player, Vec3 position, boolean grounded, boolean eligible) {
   }
}
