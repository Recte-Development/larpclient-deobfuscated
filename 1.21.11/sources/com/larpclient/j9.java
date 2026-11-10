package com.larpclient;

import java.util.ArrayDeque;
import java.util.Iterator;

final class j9 {
   static final int a = 10;
   static final int G = 4;
   private final ArrayDeque<j9.c> R = new ArrayDeque<>();

   void q(long var1) {
      this.R.addLast(new j9.c(var1));
      if (this.R.size() > 10) {
         this.R.removeFirst();
      }
   }

   void c(long var1) {
      Iterator var3 = this.R.descendingIterator();

      while (var3.hasNext()) {
         j9.c var4 = (j9.c)var3.next();
         if (var4.a == var1 && !var4.R) {
            var4.R = (boolean)1;
            return;
         }
      }
   }

   int c() {
      int var1 = 0;

      for (j9.c var3 : this.R) {
         if (var3.R) {
            var1++;
         }
      }

      return var1;
   }

   boolean r() {
      return this.c() >= 4;
   }

   void h() {
      this.R.clear();
   }

   private static final class c {
      final long a;
      boolean R;

      c(long var1) {
         this.a = var1;
      }
   }
}
