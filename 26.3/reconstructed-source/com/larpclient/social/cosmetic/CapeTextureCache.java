package com.larpclient.social.cosmetic;

import com.larpclient.util.misc.LarpLog;
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

public final class CapeTextureCache {
   private static final long rETRY_DELAY_NANOS = 30000000000L;
   private static final Map<String, CompletableFuture<Texture>> textures = new HashMap<>();
   private static final Map<String, Long> failureTimes = new HashMap<>();
   private static SkinTextureDownloader downloader;

   public static CompletableFuture<Texture> loadCape(CapeCatalog.Cape var0) {
      CompletableFuture var1 = textures.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && canRetry(var0)) {
         Minecraft var2 = Minecraft.getInstance();
         if (downloader == null) {
            downloader = new SkinTextureDownloader(var2.getProxy(), var2.getTextureManager(), var2);
         }

         Identifier var3 = Identifier.fromNamespaceAndPath("larp-client", "capes/" + var0.hash());
         Path var4 = var2.gameDirectory.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         LarpLog.debug("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = downloader.downloadAndRegisterSkin(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               onLoadFailed(var0, var4, var3x);
            }
         }, var2);
         textures.put(var0.hash(), var5);
         failureTimes.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static Texture getTexture(CapeCatalog.Cape var0) {
      CompletableFuture var1 = loadCape(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (Texture)var1.join() : null;
   }

   private static boolean canRetry(CapeCatalog.Cape var0) {
      Long var1 = failureTimes.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void onLoadFailed(CapeCatalog.Cape var0, Path var1, Throwable var2) {
      failureTimes.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var2x) {
         }
      }, Util.nonCriticalIoPool().forName("larpCapeCache"));
      LarpLog.errorOnce("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private CapeTextureCache() {
   }
}
