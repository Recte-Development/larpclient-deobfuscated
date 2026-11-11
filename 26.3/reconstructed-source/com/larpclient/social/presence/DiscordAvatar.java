package com.larpclient.social.presence;

import com.larpclient.network.ConnectionStatus;
import com.larpclient.network.HttpsFetcher;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.CrosshairImages;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public final class DiscordAvatar {
   private static final int downloadSize = 128;
   private static final int maxDownloadBytes = 262144;
   private static final int maxTextureSize = 256;
   private static final int maxSourceSide = 512;
   private static final long retryDelayNanos = TimeUnit.MINUTES.toNanos(1L);
   private static final AtomicInteger textureCounter = new AtomicInteger();
   private final Identifier textureId = Identifier.fromNamespaceAndPath("larp-client", "discord/avatar-" + textureCounter.getAndIncrement());
   private volatile String avatarKey = "";
   private volatile CrosshairImages.PixelImage picture;
   private volatile boolean downloading;
   private volatile long retryAtNanos;
   private DiscordAvatar.AvatarTexture texture;
   private TextureManager textureManager;
   private int textureSize;

   public boolean render(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      DiscordUser var5 = ConnectionStatus.current().account();
      if (var5 != null && var4 > 0) {
         String var6 = var5.discordId() + "/" + var5.avatar();
         if (!var6.equals(this.avatarKey)) {
            this.reset();
            this.avatarKey = var6;
         }

         CrosshairImages.PixelImage var7 = this.picture;
         if (var7 == null) {
            if (!this.downloading && System.nanoTime() >= this.retryAtNanos) {
               this.downloadAvatar(var5, var6);
            }

            return false;
         } else {
            int var8 = (int)Math.clamp(Math.ceil(Minecraft.getInstance().getWindow().getGuiScale()), 1.0, 6.0);
            int var9 = Math.min(256, var4 * var8);
            if (!this.uploadTexture(var7, var9)) {
               return false;
            }

            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               this.textureId,
               var2,
               var3,
               0.0F,
               0.0F,
               var4,
               var4,
               this.textureSize,
               this.textureSize,
               this.textureSize,
               this.textureSize,
               AlphaScope.applyAlpha(-1)
            );
            return true;
         }
      } else {
         this.reset();
         return false;
      }
   }

   public void reset() {
      if (this.texture != null || !this.avatarKey.isEmpty()) {
         if (this.texture != null) {
            Minecraft.getInstance().getTextureManager().release(this.textureId);
            this.texture = null;
         }

         this.avatarKey = "";
         this.picture = null;
         this.textureSize = 0;
         this.retryAtNanos = 0L;
      }
   }

   private boolean uploadTexture(CrosshairImages.PixelImage var1, int var2) {
      TextureManager var3 = Minecraft.getInstance().getTextureManager();
      if (var3 != this.textureManager) {
         this.texture = null;
         this.textureSize = 0;
         this.textureManager = var3;
      }

      if (this.texture != null && this.textureSize == var2) {
         return true;
      }

      DiscordAvatar.AvatarTexture var4 = new DiscordAvatar.AvatarTexture(this.textureId, var1, var2);

      try {
         var3.registerAndLoad(this.textureId, var4);
      } catch (RuntimeException var6) {
         var4.close();
         this.picture = null;
         this.texture = null;
         this.textureSize = 0;
         this.retryAtNanos = System.nanoTime() + retryDelayNanos;
         LarpLog.errorOnce("Discord", "Avatar upload failed; the account card keeps the game head", var6);
         return false;
      }

      this.texture = var4;
      this.textureSize = var2;
      LarpLog.debug("Discord", "Prepared the linked avatar at {} pixels", var2);
      return true;
   }

   private void downloadAvatar(DiscordUser var1, String var2) {
      this.downloading = (boolean)1;
      String var3 = var1.avatarUrl(128);
      Thread.ofPlatform().daemon().name("Larp-Discord-Avatar").start(() -> {
         try {
            byte[] var3x = HttpsFetcher.fetchBytes(var3, 262144, "image/png");
            validatePng(var3x);
            CrosshairImages.DecodedImage var4 = CrosshairImages.decodePng(new ByteArrayInputStream(var3x));
            if (var2.equals(this.avatarKey)) {
               this.picture = var4.picture();
            }
         } catch (Exception var8) {
            this.retryAtNanos = System.nanoTime() + retryDelayNanos;
            LarpLog.errorOnce("Discord", "Avatar download failed; the account card keeps the game head", var8);
         } finally {
            this.downloading = (boolean)0;
         }
      });
   }

   private static void validatePng(byte[] var0) throws IOException {
      if (var0.length < 24) {
         throw new IOException("Avatar response is not a PNG");
      }

      ByteBuffer var1 = ByteBuffer.wrap(var0);
      if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
         int var2 = var1.getInt();
         int var3 = var1.getInt();
         if (var2 < 1 || var3 < 1 || var2 > 512 || var3 > 512) {
            throw new IOException("Avatar is " + var2 + "x" + var3 + "; at most 512 per side");
         }
      } else {
         throw new IOException("Avatar response is not a PNG");
      }
   }

   private static int[] downsample(CrosshairImages.PixelImage var0, int var1) {
      int var2 = var0.width();
      int var3 = var0.height();
      int[] var4 = new int[var1 * var1];

      for (int var5 = 0; var5 < var1; var5++) {
         int var6 = var5 * var3 / var1;
         int var7 = Math.max(var6 + 1, (var5 + 1) * var3 / var1);

         for (int var8 = 0; var8 < var1; var8++) {
            int var9 = var8 * var2 / var1;
            int var10 = Math.max(var9 + 1, (var8 + 1) * var2 / var1);
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;
            long var17 = 0L;
            int var19 = 0;

            for (int var20 = var6; var20 < var7; var20++) {
               for (int var21 = var9; var21 < var10; var21++) {
                  int var22 = var0.pixels()[var20 * var2 + var21];
                  int var23 = var22 >>> 24;
                  var11 += var23;
                  var13 += (var22 >> 16 & 0xFF) * var23;
                  var15 += (var22 >> 8 & 0xFF) * var23;
                  var17 += (var22 & 0xFF) * var23;
                  var19++;
               }
            }

            var4[var5 * var1 + var8] = var11 == 0L
               ? 0
               : (int)(var11 / var19) << 24 | (int)(var13 / var11) << 16 | (int)(var15 / var11) << 8 | (int)(var17 / var11);
         }
      }

      return var4;
   }

   private static final class AvatarTexture extends ReloadableTexture {
      private final CrosshairImages.PixelImage picture;
      private final int size;

      AvatarTexture(Identifier var1, CrosshairImages.PixelImage var2, int var3) {
         super(var1);
         this.picture = var2;
         this.size = var3;
      }

      public TextureContents loadContents(ResourceManager var1) {
         int[] var2 = DiscordAvatar.downsample(this.picture, this.size);
         NativeImage var3 = new NativeImage(this.size, this.size, false);

         try {
            double var4 = this.size / 2.0;

            for (int var6 = 0; var6 < this.size; var6++) {
               for (int var7 = 0; var7 < this.size; var7++) {
                  double var8 = var4 - Math.hypot(var7 + 0.5 - var4, var6 + 0.5 - var4);
                  int var10 = var2[var6 * this.size + var7];
                  int var11 = (int)Math.round((var10 >>> 24) * Math.clamp(var8 + 0.5, 0.0, 1.0));
                  var3.setPixel(var7, var6, var11 << 24 | var10 & 16777215);
               }
            }

            return new TextureContents(var3, new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var12) {
            var3.close();
            throw var12;
         }
      }
   }
}
