package com.larpclient;

public final class qn {
   public static qn.o c(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? qn.o.i : qn.o.j;
      } else {
         return qn.o.M;
      }
   }

   private qn() {
   }

   public enum o {
      M,
      j,
      i;
   }
}
