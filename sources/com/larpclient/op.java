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

public final class op {
   private static final long A = 30000000000L;
   private static final Map<String, CompletableFuture<Texture>> n = new HashMap<>();
   private static final Map<String, Long> w = new HashMap<>();
   private static SkinTextureDownloader m;

   public static CompletableFuture<Texture> y(xm.p var0) {
      CompletableFuture var1 = n.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && C(var0)) {
         Minecraft var2 = Minecraft.getInstance();
         if (m == null) {
            m = new SkinTextureDownloader(var2.getProxy(), var2.getTextureManager(), var2);
         }

         Identifier var3 = Identifier.fromNamespaceAndPath("larp-client", "capes/" + var0.hash());
         Path var4 = var2.gameDirectory.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         r4.g("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = m.downloadAndRegisterSkin(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               r(var0, var4, var3x);
            }
         }, var2);
         n.put(var0.hash(), var5);
         w.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static Texture w(xm.p var0) {
      CompletableFuture var1 = y(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (Texture)var1.join() : null;
   }

   private static boolean C(xm.p var0) {
      Long var1 = w.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void r(xm.p var0, Path var1, Throwable var2) {
      w.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var2x) {
         }
      }, Util.nonCriticalIoPool().forName("larpCapeCache"));
      r4.T("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private op() {
   }
}
