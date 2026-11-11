package com.larpclient.social.cosmetic;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.model.ChinaHatHorns;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class DominusHatMesh {
   private static ChinaHatHorns.HornMesh mesh;
   private static boolean loadFailed;

   private DominusHatMesh() {
   }

   public static ChinaHatHorns.HornMesh loadMesh() {
      if (mesh == null && !loadFailed) {
         try (InputStream var0 = DominusHatMesh.class.getResourceAsStream("/assets/larp-client/models/hats/dominus.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            mesh = parseMesh(var0.readAllBytes());
            LarpLog.info(
               "ChinaHat",
               "Dominus mesh loaded; {} triangles, {} vertices, {} blocks above the head",
               mesh.triangles(),
               mesh.vertices(),
               String.format(Locale.ROOT, "%.3f", mesh.rise())
            );
         } catch (IOException | RuntimeException var5) {
            loadFailed = (boolean)1;
            LarpLog.error("ChinaHat", "Dominus mesh unavailable; Dominus hidden until restart", var5);
         }

         return mesh;
      } else {
         return mesh;
      }
   }

   public static ChinaHatHorns.HornMesh parseMesh(byte[] var0) {
      return ChinaHatHorns.parseMeshChecked(var0, false);
   }

   public static double loadMesh(double var0) {
      ChinaHatHorns.HornMesh var2 = loadMesh();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   public static RenderType getRenderType() {
      if (DominusHatMesh.DominusRenderTypes.lastReloadCount != PostProcessing.reloadCount) {
         ClientPipelines.validatePipeline(ChinaHatHorns.getRenderType());
         DominusHatMesh.DominusRenderTypes.lastReloadCount = PostProcessing.reloadCount;
      }

      return DominusHatMesh.DominusRenderTypes.renderType;
   }

   static int tintColor(int var0, int var1) {
      int var2 = -16777216;
      int var3 = var0 & 0xFF;

      for (byte var4 = 16; var4 >= 0; var4 -= 8) {
         var2 |= ((var1 >> var4 & 0xFF) * var3 + 127) / 255 << var4;
      }

      return var2;
   }

   public static void renderMesh(VertexConsumer var0, Matrix4f var1, ChinaHatHorns.HornMesh var2, double var3, int var5) {
      float var6 = (float)var3;
      float[] var7 = var2.points();
      float[] var8 = var2.uvs();
      int[] var9 = var2.colors();

      for (int var13 : var2.indices()) {
         var0.addVertex(var1, var7[var13 * 3] * var6, -0.5F - var7[var13 * 3 + 1] * var6, var7[var13 * 3 + 2] * var6)
            .setUv(var8[var13 * 2], var8[var13 * 2 + 1])
            .setColor(tintColor(var9[var13], var5));
      }

      PerfProfiler.addCounter("China Hat Dominus triangles", var2.triangles());
   }

   private static final class DominusRenderTypes {
      static final RenderType renderType = RenderType.create(
         "larp_china_hat_dominus",
         RenderSetup.builder(ChinaHatHorns.getRenderType())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/hats/dominus.png"))
            .createRenderSetup()
      );
      static long lastReloadCount = -1L;
   }
}
