package com.larpclient.util.data;

public final class ChangeTracker {
   private Object lastFirst;
   private Object lastSecond;
   private boolean lastFlag;

   public boolean hasChanged(Object var1, Object var2, boolean var3) {
      int var4 = this.lastFirst == var1 && this.lastSecond == var2 && this.lastFlag == var3 ? 0 : 1;
      this.lastFirst = var1;
      this.lastSecond = var2;
      this.lastFlag = var3;
      return (boolean)var4;
   }
}
