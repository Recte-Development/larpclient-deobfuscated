package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class hd {
   private final ArrayDeque<hd.w> Q = new ArrayDeque<>();
   private final int Z;
   private boolean n;

   public hd(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.Z = var1;
   }

   public boolean i(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.n && this.Q.size() < this.Z) {
         this.Q.addLast(new hd.w(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int C() {
      return this.Q.size();
   }

   public boolean Z() {
      return this.n;
   }

   public void r() {
      this.Q.clear();
   }

   public void D(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.n) {
         this.n = (boolean)1;

         try {
            while (!this.Q.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.Q.peekFirst().deadline() >= 0L)) {
               this.Q.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.Q.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.Q.clear();
            throw var9;
         } finally {
            this.n = (boolean)0;
         }
      }
   }

   private record w(long deadline, Runnable action) {
   }
}
