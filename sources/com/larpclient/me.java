package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class me {
   private final ArrayDeque<me.d> g = new ArrayDeque<>();
   private final int L;
   private boolean R;

   public me(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.L = var1;
   }

   public boolean m(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.R && this.g.size() < this.L) {
         this.g.addLast(new me.d(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int p() {
      return this.g.size();
   }

   public boolean D() {
      return this.R;
   }

   public void h() {
      this.g.clear();
   }

   public void z(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.R) {
         this.R = (boolean)1;

         try {
            while (!this.g.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.g.peekFirst().deadline() >= 0L)) {
               this.g.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.g.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.g.clear();
            throw var9;
         } finally {
            this.R = (boolean)0;
         }
      }
   }

   private record d(long deadline, Runnable action) {
   }
}
