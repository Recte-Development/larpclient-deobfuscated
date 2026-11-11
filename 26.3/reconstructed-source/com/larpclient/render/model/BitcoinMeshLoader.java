package com.larpclient.render.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitcoinMeshLoader {
   public static final int mAX_TRIANGLES = 1024;

   public static BitcoinMeshLoader.Anonymous readMesh(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Bitcoin mesh missing");
      }

      DataInputStream var1 = new DataInputStream(var0);
      if (var1.readInt() != 1112818481) {
         throw new IOException("Invalid Bitcoin mesh header");
      }

      int var2 = var1.readInt();
      if (var2 >= 3 && var2 <= 3072 && var2 % 3 == 0) {
         float[] var3 = new float[var2 * 5];
         int[] var4 = new int[var2];

         for (int var5 = 0; var5 < var2; var5++) {
            for (int var6 = 0; var6 < 5; var6++) {
               float var7 = var1.readFloat();
               if (!Float.isFinite(var7) || (var6 < 3 ? Math.abs(var7) > 0.5F : var7 < 0.0F || var7 > 1.0F)) {
                  throw new IOException("Invalid Bitcoin " + (var6 < 3 ? "coordinate" : "texture coordinate"));
               }

               var3[var5 * 5 + var6] = var7;
            }

            int var8 = var1.readInt();
            if (var8 >>> 24 != 255) {
               throw new IOException("Bitcoin mesh must be opaque");
            }

            var4[var5] = var8;
         }

         if (var1.read() != -1) {
            throw new IOException("Unexpected Bitcoin mesh trailer");
         } else {
            return new BitcoinMeshLoader.Anonymous(var3, var4);
         }
      } else {
         throw new IOException("Invalid Bitcoin triangle budget");
      }
   }

   private BitcoinMeshLoader() {
   }

   public record Anonymous(float[] vertices, int[] colors) {
      public int triangles() {
         return this.colors.length / 3;
      }
   }
}
