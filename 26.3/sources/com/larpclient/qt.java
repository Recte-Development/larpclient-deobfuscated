package com.larpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class qt {
   public static final int Q = 3000;

   public static qt.v s(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Sahur mesh missing");
      }

      DataInputStream var1 = new DataInputStream(var0);
      if (var1.readInt() != 1396787249) {
         throw new IOException("Invalid Sahur mesh header");
      }

      int var2 = var1.readInt();
      if (var2 >= 3 && var2 <= 9000 && var2 % 3 == 0) {
         float[] var3 = new float[var2 * 5];
         int[] var4 = new int[var2];
         float var5 = Float.POSITIVE_INFINITY;
         float var6 = Float.NEGATIVE_INFINITY;

         for (int var7 = 0; var7 < var2; var7++) {
            for (int var8 = 0; var8 < 5; var8++) {
               float var9 = var1.readFloat();
               if (!Float.isFinite(var9) || (var8 < 3 ? Math.abs(var9) > 0.5F : var9 < 0.0F || var9 > 1.0F)) {
                  throw new IOException("Invalid Sahur " + (var8 < 3 ? "coordinate" : "texture coordinate"));
               }

               var3[var7 * 5 + var8] = var9;
               if (var8 == 1) {
                  var5 = Math.min(var5, var9);
                  var6 = Math.max(var6, var9);
               }
            }

            int var10 = var1.readInt();
            if (var10 >>> 24 != 255) {
               throw new IOException("Sahur mesh must be opaque");
            }

            var4[var7] = var10;
         }

         if (var1.read() != -1) {
            throw new IOException("Unexpected Sahur mesh trailer");
         } else if (var6 - var5 < 0.001F) {
            throw new IOException("Invalid Sahur height");
         } else {
            return new qt.v(var3, var4, var5, var6 - var5);
         }
      } else {
         throw new IOException("Invalid Sahur triangle budget");
      }
   }

   private qt() {
   }

   public record v(float[] vertices, int[] colors, float minimumY, float height) {
      public int triangles() {
         return this.colors.length / 3;
      }
   }
}
