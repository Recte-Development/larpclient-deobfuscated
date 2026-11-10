package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class w1 {
   private final ArrayDeque<w1.h> O = new ArrayDeque<>();
   private final int r;
   private boolean m;

   public w1(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.r = var1;
   }

   public boolean A(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.m && this.O.size() < this.r) {
         this.O.addLast(new w1.h(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int r() {
      return this.O.size();
   }

   public boolean c() {
      return this.m;
   }

   public void v() {
      this.O.clear();
   }

   public void o(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.m) {
         this.m = (boolean)1;

         try {
            while (!this.O.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.O.peekFirst().deadline() >= 0L)) {
               this.O.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.O.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.O.clear();
            throw var9;
         } finally {
            this.m = (boolean)0;
         }
      }
   }

   private record h(long deadline, Runnable action) {
   }
}
