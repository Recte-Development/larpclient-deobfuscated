package com.larpclient.util.data;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class KeyPressTracker<T> {
   private final boolean[] previousDown;
   private final boolean[] currentDown;
   private final boolean[] pendingPress;
   private final LinkedHashSet<T> queued = new LinkedHashSet<>();

   public KeyPressTracker(int var1) {
      this.previousDown = new boolean[var1];
      this.currentDown = new boolean[var1];
      this.pendingPress = new boolean[var1];
   }

   public void markPending(int var1) {
      if (var1 > 0 && var1 < this.pendingPress.length) {
         this.pendingPress[var1] = (boolean)1;
      }
   }

   public void markHandled(int var1) {
      if (var1 > 0 && var1 < this.pendingPress.length) {
         this.previousDown[var1] = (boolean)1;
         this.pendingPress[var1] = (boolean)0;
      }
   }

   public void clearPending() {
      Arrays.fill(this.pendingPress, false);
   }

   public void reset() {
      this.queued.clear();
      Arrays.fill(this.currentDown, false);
   }

   public void enqueue(T var1) {
      if (var1 != null) {
         this.queued.add((T)var1);
      }
   }

   public void update(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.currentDown.length) {
         this.currentDown[var2] = var3;
         if (this.pendingPress[var2] || var3 && !this.previousDown[var2]) {
            this.enqueue((T)var1);
         }
      }
   }

   public List<T> poll() {
      System.arraycopy(this.currentDown, 0, this.previousDown, 0, this.currentDown.length);
      this.clearPending();
      return List.copyOf(this.queued);
   }
}
