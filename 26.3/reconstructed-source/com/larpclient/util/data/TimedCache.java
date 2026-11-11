package com.larpclient.util.data;

public final class TimedCache<T> {
   private Object key;
   private long timestamp;
   private int id;
   private T value;
   private boolean consumed;

   public void store(Object var1, long var2, int var4, T var5) {
      if (!this.matches(var1, var2, var4)) {
         this.key = var1;
         this.timestamp = var2;
         this.id = var4;
         this.value = (T)var5;
         this.consumed = (boolean)0;
      }
   }

   public boolean matches(Object var1, long var2, int var4) {
      return this.value != null && this.key == var1 && this.timestamp == var2 && this.id == var4;
   }

   public void invalidateUnlessMatching(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.matches(var1, var2, var4)) {
         this.clear();
      }
   }

   public T getUnconsumed() {
      return this.consumed ? null : this.value;
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public void markConsumed(T var1) {
      if (this.value != null && var1 == this.value) {
         this.consumed = (boolean)1;
      }
   }

   public void clear() {
      this.key = null;
      this.value = null;
      this.consumed = (boolean)0;
   }
}
