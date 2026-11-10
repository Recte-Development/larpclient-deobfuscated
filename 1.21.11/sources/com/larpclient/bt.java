package com.larpclient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_10538;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_12079.class_12081;

public final class bt {
   private static final long V = 30000000000L;
   private static final Map<String, CompletableFuture<class_12081>> W = new HashMap<>();
   private static final Map<String, Long> w = new HashMap<>();
   private static class_10538 Y;

   public static CompletableFuture<class_12081> H(l6.t var0) {
      CompletableFuture var1 = W.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && z(var0)) {
         class_310 var2 = class_310.method_1551();
         if (Y == null) {
            Y = new class_10538(var2.method_1487(), var2.method_1531(), var2);
         }

         class_2960 var3 = class_2960.method_60655("larp-client", "capes/" + var0.hash());
         Path var4 = var2.field_1697.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         q8.v("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = Y.method_65861(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               g(var0, var4, var3x);
            }
         }, var2);
         W.put(var0.hash(), var5);
         w.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static class_12081 e(l6.t var0) {
      CompletableFuture var1 = H(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (class_12081)var1.join() : null;
   }

   private static boolean z(l6.t var0) {
      Long var1 = w.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void g(l6.t var0, Path var1, Throwable var2) {
      w.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var2x) {
         }
      }, class_156.method_55473().method_64116("larpCapeCache"));
      q8.O("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private bt() {
   }
}
