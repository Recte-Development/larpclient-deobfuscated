package com.larpclient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents.AfterClientWorldChange;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.class_1923;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_6606;

public final class la {
   private static final fa t = new fa();
   private static class_638 b;
   private static boolean i;

   public static void p() {
      if (!i) {
         i = (boolean)1;
         ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((AfterClientWorldChange)(var0, var1) -> L(var1));
         ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var0, var1) -> {
            if (var0 == b) {
               t.r(var1.method_12004().method_8324());
            }
         });
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> {
            t.U();
            b = null;
         });
      }
   }

   public static fa L(class_638 var0) {
      if (var0 != b) {
         t.U();
         b = var0;
      }

      return t;
   }

   public static void m(class_638 var0, int var1, int var2, class_6606 var3) {
      class_310 var4 = class_310.method_1551();
      if (var0 != null && var4.field_1687 == var0 && var4.field_1724 != null && var4.method_18854()) {
         try {
            class_1923 var5 = var4.field_1724.method_31476();
            fa var6 = L(var0);
            var6.o(
               var2x -> {
                  class_1923 var4x = new class_1923(var2x);
                  return Math.abs((long)var4x.field_9181 - var5.field_9181) <= 12L
                     && Math.abs((long)var4x.field_9180 - var5.field_9180) <= 12L
                     && var0.method_2935().method_21730(var4x.field_9181, var4x.field_9180) != null;
               }
            );
            if (Math.abs((long)var1 - var5.field_9181) > 12L
               || Math.abs((long)var2 - var5.field_9180) > 12L
               || var0.method_2935().method_21730(var1, var2) == null) {
               return;
            }

            var6.p(var1, var2, (var0.method_31607() >> 4) - 1, var3.method_38608(), var3.method_38609(), var3.method_38610());
         } catch (RuntimeException var7) {
            q8.O("BaseFinding", "Received light capture failed; ordinary block scans remain available", var7);
         }
      }
   }

   private la() {
   }
}
