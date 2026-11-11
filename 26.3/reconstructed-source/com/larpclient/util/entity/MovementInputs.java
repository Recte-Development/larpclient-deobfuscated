package com.larpclient.util.entity;

import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class MovementInputs {
   public static boolean canSprint(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static Input withoutForward(Input var0) {
      return new Input(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static Vec2 getMovementVector(Input var0) {
      return new Vec2((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0), (var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0)).normalized();
   }

   private MovementInputs() {
   }

   public static final class BurstCounter {
      private int remaining;
      private int lastKey = Integer.MIN_VALUE;
      private boolean active;

      public void arm(int var1) {
         if (this.remaining == 0 && !this.active) {
            this.remaining = Math.clamp(var1, 1, 3);
         }
      }

      public boolean update(int var1, boolean var2) {
         if (!var2) {
            this.reset();
            return false;
         }

         if (var1 != this.lastKey) {
            this.lastKey = var1;
            this.active = (boolean)(this.remaining > 0 ? 1 : 0);
            if (this.active) {
               this.remaining--;
            }
         }

         return this.active;
      }

      public boolean isActiveFor(int var1) {
         return this.active && this.lastKey == var1;
      }

      public boolean isPending() {
         return this.remaining > 0 || this.active;
      }

      public void reset() {
         this.remaining = 0;
         this.active = (boolean)0;
         this.lastKey = Integer.MIN_VALUE;
      }
   }

   public static final class PendingConfirm {
      private Object first;
      private Object second;
      private boolean armed;
      private boolean confirmed;

      public void arm(Object var1, Object var2, boolean var3) {
         this.first = var1;
         this.second = var2;
         this.armed = var3;
         this.confirmed = (boolean)0;
      }

      public void confirm(Object var1, Object var2) {
         if (this.first == var1 && this.second == var2) {
            this.confirmed = (boolean)1;
         }
      }

      public boolean consume(Object var1, Object var2) {
         int var3 = this.armed && this.confirmed && this.first == var1 && this.second == var2 ? 1 : 0;
         this.clear();
         return (boolean)var3;
      }

      public void clear() {
         this.first = this.second = null;
         this.armed = this.confirmed = (boolean)0;
      }
   }
}
