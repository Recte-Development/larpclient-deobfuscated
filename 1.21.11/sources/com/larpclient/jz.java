package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class jz {
   private final ArrayDeque<jz.x> l = new ArrayDeque<>();
   private final int k;
   private boolean x;

   public jz(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.k = var1;
   }

   public boolean k(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.x && this.l.size() < this.k) {
         this.l.addLast(new jz.x(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int C() {
      return this.l.size();
   }

   public boolean u() {
      return this.x;
   }

   public void x() {
      this.l.clear();
   }

   public void M(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.x) {
         this.x = (boolean)1;

         try {
            while (!this.l.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.l.peekFirst().deadline() >= 0L)) {
               this.l.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.l.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.l.clear();
            throw var9;
         } finally {
            this.x = (boolean)0;
         }
      }
   }

   private record x(long deadline, Runnable action) {
   }
}
