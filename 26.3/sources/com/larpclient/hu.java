package com.larpclient;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class hu {
   private static np.u m;
   private static boolean u;

   private hu() {
   }

   public static np.u j() {
      if (m == null && !u) {
         try (InputStream var0 = hu.class.getResourceAsStream("/assets/larp-client/models/hats/dominus.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            m = T(var0.readAllBytes());
            _q.z(
               "ChinaHat",
               "Dominus mesh loaded; {} triangles, {} vertices, {} blocks above the head",
               m.triangles(),
               m.vertices(),
               String.format(Locale.ROOT, "%.3f", m.rise())
            );
         } catch (IOException | RuntimeException var5) {
            u = (boolean)1;
            _q.o("ChinaHat", "Dominus mesh unavailable; Dominus hidden until restart", var5);
         }

         return m;
      } else {
         return m;
      }
   }

   public static np.u T(byte[] var0) {
      return np.c(var0, false);
   }

   public static double j(double var0) {
      np.u var2 = j();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   public static RenderType n() {
      if (hu.j.B != nl.q) {
         se.j(np.F());
         hu.j.B = nl.q;
      }

      return hu.j.v;
   }

   static int G(int var0, int var1) {
      int var2 = -16777216;
      int var3 = var0 & 0xFF;

      for (byte var4 = 16; var4 >= 0; var4 -= 8) {
         var2 |= ((var1 >> var4 & 0xFF) * var3 + 127) / 255 << var4;
      }

      return var2;
   }

   public static void E(VertexConsumer var0, Matrix4f var1, np.u var2, double var3, int var5) {
      float var6 = (float)var3;
      float[] var7 = var2.points();
      float[] var8 = var2.uvs();
      int[] var9 = var2.colors();

      for (int var13 : var2.indices()) {
         var0.addVertex(var1, var7[var13 * 3] * var6, -0.5F - var7[var13 * 3 + 1] * var6, var7[var13 * 3 + 2] * var6)
            .setUv(var8[var13 * 2], var8[var13 * 2 + 1])
            .setColor(G(var9[var13], var5));
      }

      he.Z("China Hat Dominus triangles", var2.triangles());
   }

   private static final class j {
      static final RenderType v = RenderType.create(
         "larp_china_hat_dominus",
         RenderSetup.builder(np.F()).withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/hats/dominus.png")).createRenderSetup()
      );
      static long B = -1L;
   }
}
