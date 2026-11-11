package com.larpclient.social.pet;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.SahurMesh;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class SahurPet {
   public static final String dISPLAY_NAME = "Tung Tung Tung Sahur";
   private static final Identifier mESH_RESOURCE = Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderType petRenderType = createRenderType(false);
   private static final RenderType strikeRenderType = createRenderType(true);
   private static SahurMesh.Mesh mesh;
   private static long meshGeneration = -1L;
   private static long strikePipelineGeneration = -1L;

   private static RenderType createRenderType(boolean var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "sahur_strike" : "sahur_pet"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(true);
      if (var0) {
         var1.withShaderDefine("DITHER_FADE");
      }

      return RenderType.create(
         var0 ? "larp_sahur_strike" : "larp_sahur_pet",
         RenderSetup.builder(var1.build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/albedo.png"))
            .createRenderSetup()
      );
   }

   public static boolean isReady() {
      return mesh != null && meshGeneration == PostProcessing.reloadCount;
   }

   public static SahurMesh.Mesh getMesh() {
      if (!isReady()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return mesh;
      }
   }

   public static void prepareMesh() {
      if (mesh == null || meshGeneration != PostProcessing.reloadCount) {
         mesh = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(mESH_RESOURCE).open()) {
            SahurMesh.Mesh var1 = SahurMesh.read(var0);
            ClientPipelines.validatePipeline(petRenderType.pipeline());
            mesh = var1;
            meshGeneration = PostProcessing.reloadCount;
            LarpLog.debug("Sahur", "Static mesh ready: {} triangles, resource generation {}", mesh.triangles(), meshGeneration);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void prepareStrikePipeline() {
      prepareMesh();
      if (strikePipelineGeneration != PostProcessing.reloadCount) {
         ClientPipelines.validatePipeline(strikeRenderType.pipeline());
         strikePipelineGeneration = PostProcessing.reloadCount;
      }
   }

   public static void renderMesh(EffectsRenderer var0, Vec3 var1, Quaternionfc var2, float var3) {
      if (isReady()) {
         var0.drawTransformedVertices(petRenderType, var1, var2, var3, mesh.vertices(), mesh.colors());
         PerfProfiler.addCounter("Sahur triangles", mesh.triangles());
      }
   }

   public static void renderTransformed(PoseStack var0, EffectBatch var1, Matrix4f var2) {
      if (isReady()) {
         Matrix4f var3 = new Matrix4f(var0.last().pose()).mul(var2);
         EffectsRenderer.drawRibbon(var1, petRenderType, var3, mesh.vertices(), mesh.colors());
         PerfProfiler.addCounter("Sahur triangles", mesh.triangles());
      }
   }

   public static void renderCustomMesh(PoseStack var0, EffectBatch var1, Matrix4f var2, float[] var3, int[] var4) {
      if (isReady()) {
         Matrix4f var5 = new Matrix4f(var0.last().pose()).mul(var2);
         EffectsRenderer.drawRibbon(var1, petRenderType, var5, var3, var4);
         PerfProfiler.addCounter("Sahur triangles", var4.length / 3);
      }
   }

   public static void renderFadingMesh(PoseStack var0, EffectBatch var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (isReady() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.last().pose()).mul(var2);
         VertexConsumer var8 = var1.getConsumer(strikeRenderType);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.addVertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2])
               .setUv(var3[var10 + 3], var3[var10 + 4])
               .setColor(ColorUtil.multiplyAlpha(var4[var9], var5));
         }

         PerfProfiler.addCounter("Sahur triangles", var4.length / 3);
      }
   }

   private SahurPet() {
   }
}
