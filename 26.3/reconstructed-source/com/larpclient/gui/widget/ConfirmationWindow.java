package com.larpclient.gui.widget;

import java.util.ArrayDeque;
import java.util.Iterator;

final class ConfirmationWindow {
   static final int mAX_ENTRIES = 10;
   static final int rEQUIRED_CONFIRMATIONS = 4;
   private final ArrayDeque<ConfirmationWindow.Entry> entries = new ArrayDeque<>();

   void record_(long var1) {
      this.entries.addLast(new ConfirmationWindow.Entry(var1));
      if (this.entries.size() > 10) {
         this.entries.removeFirst();
      }
   }

   void confirm(long var1) {
      Iterator var3 = this.entries.descendingIterator();

      while (var3.hasNext()) {
         ConfirmationWindow.Entry var4 = (ConfirmationWindow.Entry)var3.next();
         if (var4.id == var1 && !var4.confirmed) {
            var4.confirmed = (boolean)1;
            return;
         }
      }
   }

   int confirmedCount() {
      int var1 = 0;

      for (ConfirmationWindow.Entry var3 : this.entries) {
         if (var3.confirmed) {
            var1++;
         }
      }

      return var1;
   }

   boolean isSatisfied() {
      return this.confirmedCount() >= 4;
   }

   void clear() {
      this.entries.clear();
   }

   private static final class Entry {
      final long id;
      boolean confirmed;

      Entry(long var1) {
         this.id = var1;
      }
   }
}
