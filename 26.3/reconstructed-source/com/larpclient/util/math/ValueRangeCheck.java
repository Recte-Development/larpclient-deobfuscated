package com.larpclient.util.math;

public final class ValueRangeCheck {
   public static ValueRangeCheck.Action classify(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? ValueRangeCheck.Action.ATTACK : ValueRangeCheck.Action.CLEAR_ALL;
      } else {
         return ValueRangeCheck.Action.CANCEL;
      }
   }

   private ValueRangeCheck() {
   }

   public enum Action {
      CANCEL,
      CLEAR_ALL,
      ATTACK;
   }
}
