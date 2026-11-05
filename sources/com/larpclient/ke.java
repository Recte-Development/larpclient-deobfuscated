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

public final class ke {
   private static final long A = 30000000000L;
   private static final Map<String, CompletableFuture<Texture>> l = new HashMap<>();
   private static final Map<String, Long> f = new HashMap<>();
   private static SkinTextureDownloader m;

   public static CompletableFuture<Texture> l(ei.b var0) {
      CompletableFuture var1 = l.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && U(var0)) {
         Minecraft var2 = Minecraft.getInstance();
         if (m == null) {
            m = new SkinTextureDownloader(var2.getProxy(), var2.getTextureManager(), var2);
         }

         Identifier var3 = Identifier.fromNamespaceAndPath("larp-client", "capes/" + var0.hash());
         Path var4 = var2.gameDirectory.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         mt.U("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = m.downloadAndRegisterSkin(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               x(var0, var4, var3x);
            }
         }, var2);
         l.put(var0.hash(), var5);
         f.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static Texture I(ei.b var0) {
      CompletableFuture var1 = l(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (Texture)var1.join() : null;
   }

   private static boolean U(ei.b var0) {
      Long var1 = f.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void x(ei.b var0, Path var1, Throwable var2) {
      f.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var2x) {
         }
      }, Util.nonCriticalIoPool().forName("larpCapeCache"));
      mt.B("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private ke() {
   }
}
