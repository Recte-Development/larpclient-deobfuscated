package com.larpclient.social.pet;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.model.BitcoinMeshLoader;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class CryptoCoinPets {
   public static final String dEFAULT_STYLE = "Bitcoin";
   public static final double dEFAULT_SCALE = 0.6;
   public static final double hOVER_HEIGHT = 1.6;
   public static final int dEFAULT_SPARK_COLOR = -552166;
   private static final String mODEL_DIR = "models/pets/";
   private static final RenderPipeline pIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(loadAssets("bitcoin_pet"))
      .withFragmentShader(loadAssets("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   public static final List<CryptoCoinPets.Coin> cOINS = List.of(
      createCoin("Bitcoin", "bitcoin", 0.6, -552166),
      createCoin("Solana", "solana", 0.6, -6732289),
      createCoin("Ethereum", "ethereum", 0.6, -10322198),
      createCoin("Litecoin", "litecoin", 0.6, -13345379),
      createCoin("Monero", "monero", 0.6, -39424)
   );
   private static BitcoinMeshLoader.Anonymous mesh;
   private static long loadedGeneration = -1L;
   private static long failedGeneration = -1L;

   private static Identifier loadAssets(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static CryptoCoinPets.Coin createCoin(String var0, String var1, double var2, int var4) {
      return new CryptoCoinPets.Coin(
         var0,
         var1,
         var2,
         var4,
         RenderType.create(
            "larp_" + var1 + "_pet",
            RenderSetup.builder(pIPELINE).withTexture("Sampler0", loadAssets("models/pets/" + var1 + "/albedo.png")).createRenderSetup()
         ),
         var0 + " triangles"
      );
   }

   public static CryptoCoinPets.Coin findCoin(String var0) {
      for (CryptoCoinPets.Coin var2 : cOINS) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean isLoaded() {
      return mesh != null && loadedGeneration == PostProcessing.reloadCount;
   }

   public static void loadAssets() {
      if (!isLoaded()) {
         mesh = null;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(loadAssets("models/pets/bitcoin/mesh.bin")).open()) {
            for (CryptoCoinPets.Coin var3 : cOINS) {
               var0.getResourceOrThrow(loadAssets("models/pets/" + var3.folder() + "/albedo.png"));
            }

            BitcoinMeshLoader.Anonymous var7 = BitcoinMeshLoader.readMesh(var1);
            ClientPipelines.validatePipeline(pIPELINE);
            mesh = var7;
            loadedGeneration = PostProcessing.reloadCount;
            LarpLog.debug("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", cOINS.size(), mesh.triangles(), loadedGeneration);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean tryLoad(String var0) {
      if (isLoaded()) {
         return true;
      }

      if (failedGeneration == PostProcessing.reloadCount) {
         return false;
      }

      try {
         loadAssets();
         return true;
      } catch (RuntimeException var2) {
         failedGeneration = PostProcessing.reloadCount;
         LarpLog.error(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static BitcoinMeshLoader.Anonymous getMesh() {
      return isLoaded() ? mesh : null;
   }

   public static void renderWithMatrix(EffectsRenderer var0, CryptoCoinPets.Coin var1, Matrix4f var2) {
      if (isLoaded()) {
         var0.writeVertices(var1.type(), var2, mesh.vertices(), mesh.colors());
         PerfProfiler.addCounter(var1.profile(), mesh.triangles());
      }
   }

   public static void renderInWorld(EffectsRenderer var0, CryptoCoinPets.Coin var1, Vec3 var2, Quaternionfc var3, float var4) {
      if (isLoaded()) {
         var0.drawTransformedVertices(var1.type(), var2, var3, var4, mesh.vertices(), mesh.colors());
         PerfProfiler.addCounter(var1.profile(), mesh.triangles());
      }
   }

   private CryptoCoinPets() {
   }

   public record Coin(String style, String folder, double scale, int spark, RenderType type, String profile) {
   }
}
