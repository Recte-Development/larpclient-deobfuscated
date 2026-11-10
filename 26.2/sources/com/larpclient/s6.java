package com.larpclient;

import java.util.ArrayDeque;
import java.util.Iterator;

final class s6 {
   static final int H = 10;
   static final int G = 4;
   private final ArrayDeque<s6.o> V = new ArrayDeque<>();

   void v(long var1) {
      this.V.addLast(new s6.o(var1));
      if (this.V.size() > 10) {
         this.V.removeFirst();
      }
   }

   void g(long var1) {
      Iterator var3 = this.V.descendingIterator();

      while (var3.hasNext()) {
         s6.o var4 = (s6.o)var3.next();
         if (var4.v == var1 && !var4.X) {
            var4.X = (boolean)1;
            return;
         }
      }
   }

   int t() {
      int var1 = 0;

      for (s6.o var3 : this.V) {
         if (var3.X) {
            var1++;
         }
      }

      return var1;
   }

   boolean K() {
      return this.t() >= 4;
   }

   void k() {
      this.V.clear();
   }

   private static final class o {
      final long v;
      boolean X;

      o(long var1) {
         this.v = var1;
      }
   }
}
