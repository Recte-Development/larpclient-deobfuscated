package com.larpclient.module;

import com.larpclient.q8;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_419;
import net.minecraft.class_437;
import net.minecraft.class_639;
import net.minecraft.class_642;

public final class AutoReconnectModule extends Module {
   private static AutoReconnectModule u;
   private final lo O = this.addSetting(new lo("Delay", "Seconds to wait before reconnecting", 3.5, 0.0, 60.0, 0.1));
   private class_639 a;
   private class_642 x;
   private class_437 o;
   private long R = -1L;
   private boolean V;

   public AutoReconnectModule() {
      super("Auto Reconnect", "Automatically reconnects when disconnected from a server.", Category.PLAYER, -1);
      u = this;
   }

   public static void V(class_639 var0, class_642 var1) {
      AutoReconnectModule var2 = u;
      if (var2 != null && var0 != null && var1 != null) {
         var2.a = var0;
         var2.x = var1;
      }
   }

   public static void A(class_310 var0) {
      if (u != null && u.isEnabled()) {
         AutoReconnectModule var1 = u;
         if (var1 != null) {
            var1.O(var0);
         }
      }
   }

   private void O(class_310 var1) {
      if (!this.isOperational()) {
         this.o = null;
         this.V();
      } else {
         class_437 var2 = var1.field_1755;
         if (var2 != this.o) {
            this.o = var2;
            if (var2 instanceof class_419) {
               this.V();
            }
         }

         class_642 var3 = var1.method_1558();
         if (var3 != null && var1.method_1562() != null) {
            V(class_639.method_2950(var3.field_3761), var3);
            this.V();
         }

         if (var2 instanceof class_419 && this.a != null && this.x != null && !this.V) {
            long var4 = System.currentTimeMillis();
            if (this.R < 0L) {
               this.R = var4;
            }

            if (var4 - this.R >= (long)(this.O.getValue() * 1000.0)) {
               this.V = (boolean)1;
               q8.G("AutoReconnect", "Reconnecting to {}", this.x.field_3761);
               class_412.method_36877(var2, var1, this.a, this.x, false, null);
            }
         }
      }
   }

   private void V() {
      this.R = -1L;
      this.V = (boolean)0;
   }
}
