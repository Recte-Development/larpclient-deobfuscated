package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;

public final class pl extends AbstractTexture {
   public static final int V = 4096;
   private final int p;
   private final int D;

   public pl(String var1, List<NativeImage> var2) {
      if (var2.isEmpty()) {
         throw new IllegalArgumentException("No wallpaper levels");
      }

      NativeImage var3 = (NativeImage)var2.getFirst();
      this.p = var3.getWidth();
      this.D = var3.getHeight();
      GpuDevice var4 = RenderSystem.getDevice();
      this.texture = var4.createTexture(() -> var1, 5, GpuFormat.RGBA8_UNORM, this.p, this.D, 1, var2.size());
      this.textureView = var4.createTextureView(this.texture);
      CommandEncoder var5 = var4.createCommandEncoder();

      for (int var6 = 0; var6 < var2.size(); var6++) {
         var5.writeToTexture(this.texture, (NativeImage)var2.get(var6), var6, 0, 0, 0);
      }

      this.sampler = RenderSystem.getSamplerCache()
         .getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, true);
   }

   public int y() {
      return this.p;
   }

   public int u() {
      return this.D;
   }

   public static List<NativeImage> v(NativeImage var0) {
      ArrayList var1 = new ArrayList();
      NativeImage var2 = var0;

      try {
         int var3 = Math.max(var0.getWidth(), var0.getHeight());
         if (var3 > 4096) {
            double var4 = 4096.0 / var3;
            int var6 = Math.max(1, (int)Math.round(var0.getWidth() * var4));
            int var7 = Math.max(1, (int)Math.round(var0.getHeight() * var4));
            NativeImage var8 = new NativeImage(var6, var7, false);
            var1.add(var8);
            var0.resizeSubRectTo(0, 0, var0.getWidth(), var0.getHeight(), var8);
            var0.close();
         } else {
            var1.add(var0);
         }

         var2 = null;
         NativeImage var10 = (NativeImage)var1.getFirst();
         int var5 = r(var10.getWidth(), var10.getHeight());

         for (int var11 = 1; var11 < var5; var11++) {
            var1.add(mc.t((NativeImage)var1.getLast()));
         }

         return var1;
      } catch (RuntimeException var9) {
         if (var2 != null) {
            var2.close();
         }

         S(var1);
         throw var9;
      }
   }

   public static int r(int var0, int var1) {
      return 32 - Integer.numberOfLeadingZeros(Math.max(1, Math.min(var0, var1)));
   }

   public static void S(List<NativeImage> var0) {
      for (NativeImage var2 : var0) {
         var2.close();
      }

      var0.clear();
   }
}
