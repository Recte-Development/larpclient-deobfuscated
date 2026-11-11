package com.larpclient.render.misc;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.textures.AddressMode;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.IOException;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public class MipmappedTexture extends ReloadableTexture {
   private final boolean mipmapped;

   public MipmappedTexture(Identifier var1) {
      this(var1, true);
   }

   protected MipmappedTexture(Identifier var1, boolean var2) {
      super(var1);
      this.mipmapped = var2;
   }

   public TextureContents loadContents(ResourceManager var1) throws IOException {
      return TextureContents.load(var1, this.resourceId());
   }

   public void apply(TextureContents var1) {
      super.apply(var1);
      if (this.mipmapped) {
         AddressMode var2 = var1.clamp() ? AddressMode.CLAMP_TO_EDGE : AddressMode.REPEAT;
         FilterMode var3 = var1.blur() ? FilterMode.LINEAR : FilterMode.NEAREST;
         this.sampler = RenderSystem.getSamplerCache().getSampler(var2, var2, var3, var3, true);
      }
   }

   protected void doLoad(NativeImage var1) {
      if (!this.mipmapped) {
         super.doLoad(var1);
      } else {
         GpuDevice var2 = RenderSystem.getDevice();
         this.close();
         int var3 = mipLevelCount(var1.getWidth(), var1.getHeight());
         Identifier var4 = this.resourceId();
         this.texture = var2.createTexture(() -> var4 + " (mipmapped)", 5, GpuFormat.RGBA8_UNORM, var1.getWidth(), var1.getHeight(), 1, var3);
         this.textureView = var2.createTextureView(this.texture);
         CommandEncoder var5 = var2.createCommandEncoder();
         var5.writeToTexture(this.texture, var1, 0, 0, 0, 0);
         NativeImage var6 = var1;

         try {
            for (int var7 = 1; var7 < var3; var7++) {
               NativeImage var8 = downsample(var6);
               if (var6 != var1) {
                  var6.close();
               }

               var6 = var8;
               var5.writeToTexture(this.texture, var6, var7, 0, 0, 0);
            }
         } finally {
            if (var6 != var1) {
               var6.close();
            }
         }
      }
   }

   public static int mipLevelCount(int var0, int var1) {
      return 32 - Integer.numberOfLeadingZeros(Math.max(1, Math.max(var0, var1)));
   }

   static NativeImage downsample(NativeImage var0) {
      int var1 = Math.max(1, var0.getWidth() / 2);
      int var2 = Math.max(1, var0.getHeight() / 2);
      NativeImage var3 = new NativeImage(var1, var2, false);
      int var4 = var0.getWidth() - 1;
      int var5 = var0.getHeight() - 1;

      for (int var6 = 0; var6 < var2; var6++) {
         for (int var7 = 0; var7 < var1; var7++) {
            long var8 = 0L;
            long[] var10 = new long[3];
            long[] var11 = new long[3];

            for (int var12 = 0; var12 < 2; var12++) {
               for (int var13 = 0; var13 < 2; var13++) {
                  int var14 = var0.getPixel(Math.min(var4, var7 * 2 + var13), Math.min(var5, var6 * 2 + var12));
                  int var15 = var14 >>> 24;
                  var8 += var15;

                  for (int var16 = 0; var16 < 3; var16++) {
                     int var17 = var14 >> 16 - 8 * var16 & 0xFF;
                     var10[var16] += (long)var17 * var15;
                     var11[var16] += var17;
                  }
               }
            }

            int var18 = (int)((var8 + 2L) / 4L) << 24;

            for (int var19 = 0; var19 < 3; var19++) {
               var18 |= (int)(var8 == 0L ? (var11[var19] + 2L) / 4L : (var10[var19] + var8 / 2L) / var8) << 16 - 8 * var19;
            }

            var3.setPixel(var7, var6, var18);
         }
      }

      return var3;
   }
}
