package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class ao {
   public static NativeImage C(String var0, boolean var1) throws IOException {
      if (var0.isEmpty()) {
         return null;
      }

      if (var0.length() > 175000) {
         throw new IOException("PNG exceeds 128 KiB");
      }

      byte[] var2 = Base64.getDecoder().decode(var0);
      x(var2, var1);
      return NativeImage.read(new ByteArrayInputStream(var2));
   }

   public static void x(byte[] var0, boolean var1) throws IOException {
      if (var0.length >= 33 && var0.length <= 131072) {
         ByteBuffer var2 = ByteBuffer.wrap(var0);
         if (var2.getLong() == -8552249625308161526L && var2.getInt() == 13 && var2.getInt() == 1229472850) {
            int var3 = var2.getInt();
            int var4 = var2.getInt();
            if (var1 ? var3 != 64 || var4 != 64 : var3 < 64 || var3 > 512 || var4 * 2 != var3 || (var3 & var3 - 1) != 0) {
               throw new IOException(var1 ? "Skin must be 64x64 PNG" : "Cape must be 64x32, 128x64, 256x128 or 512x256 PNG");
            }
         } else {
            throw new IOException("Expected PNG");
         }
      } else {
         throw new IOException("PNG must be at most 128 KiB");
      }
   }

   public static Identifier P(String var0, NativeImage var1) {
      if (var1 == null) {
         return null;
      }

      Identifier var2 = Identifier.fromNamespaceAndPath("larp-client", "appearance/" + var0);
      Minecraft.getInstance().getTextureManager().register(var2, new DynamicTexture(var2::toString, var1));
      return var2;
   }

   public static void z(Identifier var0) {
      if (var0 != null) {
         Minecraft.getInstance().getTextureManager().release(var0);
      }
   }
}
