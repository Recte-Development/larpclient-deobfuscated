package com.larpclient;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class me {
   private static sf.a t;
   private static boolean y;

   private me() {
   }

   public static sf.a v() {
      if (t == null && !y) {
         try (InputStream var0 = me.class.getResourceAsStream("/assets/larp-client/models/hats/dominus.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            t = A(var0.readAllBytes());
            h.A(
               "ChinaHat",
               "Dominus mesh loaded; {} triangles, {} vertices, {} blocks above the head",
               t.triangles(),
               t.vertices(),
               String.format(Locale.ROOT, "%.3f", t.rise())
            );
         } catch (IOException | RuntimeException var5) {
            y = (boolean)1;
            h.m("ChinaHat", "Dominus mesh unavailable; Dominus hidden until restart", var5);
         }

         return t;
      } else {
         return t;
      }
   }

   public static sf.a A(byte[] var0) {
      return sf.H(var0, false);
   }

   public static double E(double var0) {
      sf.a var2 = v();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   public static RenderType R() {
      if (me.z.D != wa.s) {
         ai.U(sf.f());
         me.z.D = wa.s;
      }

      return me.z.l;
   }

   static int K(int var0, int var1) {
      int var2 = -16777216;
      int var3 = var0 & 0xFF;

      for (byte var4 = 16; var4 >= 0; var4 -= 8) {
         var2 |= ((var1 >> var4 & 0xFF) * var3 + 127) / 255 << var4;
      }

      return var2;
   }

   public static void a(VertexConsumer var0, Matrix4f var1, sf.a var2, double var3, int var5) {
      float var6 = (float)var3;
      float[] var7 = var2.points();
      float[] var8 = var2.uvs();
      int[] var9 = var2.colors();

      for (int var13 : var2.indices()) {
         var0.addVertex(var1, var7[var13 * 3] * var6, -0.5F - var7[var13 * 3 + 1] * var6, var7[var13 * 3 + 2] * var6)
            .setUv(var8[var13 * 2], var8[var13 * 2 + 1])
            .setColor(K(var9[var13], var5));
      }

      zj.K("China Hat Dominus triangles", var2.triangles());
   }

   private static final class z {
      static final RenderType l = RenderType.create(
         "larp_china_hat_dominus",
         RenderSetup.builder(sf.f()).withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/hats/dominus.png")).createRenderSetup()
      );
      static long D = -1L;
   }
}
