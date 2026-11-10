package com.larpclient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public final class n6 {
   private static final long Y = 30000000000L;
   private static final Map<String, CompletableFuture<Texture>> c = new HashMap<>();
   private static final Map<String, Long> b = new HashMap<>();
   private static SkinTextureDownloader p;

   public static CompletableFuture<Texture> l(i9.w var0) {
      CompletableFuture var1 = c.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && j(var0)) {
         Minecraft var2 = Minecraft.getInstance();
         if (p == null) {
            p = new SkinTextureDownloader(var2.getProxy(), var2.getTextureManager(), var2);
         }

         Identifier var3 = Identifier.fromNamespaceAndPath("larp-client", "capes/" + var0.hash());
         Path var4 = var2.gameDirectory.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         _q.c("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = p.downloadAndRegisterSkin(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               q(var0, var4, var3x);
            }
         }, var2);
         c.put(var0.hash(), var5);
         b.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static Texture P(i9.w var0) {
      CompletableFuture var1 = l(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (Texture)var1.join() : null;
   }

   private static boolean j(i9.w var0) {
      Long var1 = b.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void q(i9.w var0, Path var1, Throwable var2) {
      b.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var2x) {
         }
      }, Util.nonCriticalIoPool().forName("larpCapeCache"));
      _q.g("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private n6() {
   }
}
