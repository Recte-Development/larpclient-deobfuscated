package com.larpclient.render.model;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class BananaModel2 {
   public static final String nAME = "Banana";
   private static final Identifier mESH_LOCATION = Identifier.fromNamespaceAndPath("larp-client", "models/player/banana/mesh.bin");
   private static final RenderType rENDER_TYPE = RenderType.create(
      "larp_banana_player",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "banana_player"))
               .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
               .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
               .withColorTargetState(ColorTargetState.DEFAULT)
               .withCull(true)
               .build()
         )
         .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/player/banana/albedo.png"))
         .createRenderSetup()
   );
   private static BananaModel.Mesh mesh;
   private static long loadedGeneration = -1L;

   public static boolean isReady() {
      return mesh != null && loadedGeneration == PostProcessing.reloadCount;
   }

   public static BananaModel.Mesh getMesh() {
      if (!isReady()) {
         throw new IllegalStateException("Banana asset is not prepared");
      } else {
         return mesh;
      }
   }

   public static void loadMesh() {
      if (!isReady()) {
         mesh = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(mESH_LOCATION).open()) {
            BananaModel.Mesh var1 = BananaModel.loadMesh(var0);
            ClientPipelines.validatePipeline(rENDER_TYPE.pipeline());
            mesh = var1;
            loadedGeneration = PostProcessing.reloadCount;
            LarpLog.debug(
               "Banana", "Mesh ready: {} triangles, {} vertices, {} bones, resource generation {}", mesh.triangles(), mesh.vertices(), 11, loadedGeneration
            );
         } catch (IOException var5) {
            throw new IllegalStateException("Banana asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void render(PoseStack var0, EffectBatch var1, Matrix4f var2, BananaModel var3) {
      if (isReady()) {
         Matrix4f var4 = new Matrix4f(var0.last().pose()).mul(var2);
         EffectsRenderer.drawRibbon(var1, rENDER_TYPE, var4, var3.getVertexData(), var3.getVertexColors());
         PerfProfiler.addCounter("Banana triangles", var3.getVertexColors().length / 3);
      }
   }

   private BananaModel2() {
   }
}
