package com.larpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import net.minecraft.class_310;

public final class sp {
   public static class_1011 h(String var0, boolean var1) throws IOException {
      if (var0.isEmpty()) {
         return null;
      }

      if (var0.length() > 175000) {
         throw new IOException("PNG exceeds 128 KiB");
      }

      byte[] var2 = Base64.getDecoder().decode(var0);
      I(var2, var1);
      return class_1011.method_4309(new ByteArrayInputStream(var2));
   }

   public static void I(byte[] var0, boolean var1) throws IOException {
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

   public static class_2960 r(String var0, class_1011 var1) {
      if (var1 == null) {
         return null;
      }

      class_2960 var2 = class_2960.method_60655("larp-client", "appearance/" + var0);
      class_310.method_1551().method_1531().method_4616(var2, new class_1043(var2::toString, var1));
      return var2;
   }

   public static void G(class_2960 var0) {
      if (var0 != null) {
         class_310.method_1551().method_1531().method_4615(var0);
      }
   }
}
