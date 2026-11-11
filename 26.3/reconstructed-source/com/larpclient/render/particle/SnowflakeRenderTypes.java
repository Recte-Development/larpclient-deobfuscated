package com.larpclient.render.particle;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.overlay.PostProcessing;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public final class SnowflakeRenderTypes {
   private static int lastReloadGeneration = -1;
   private static final RenderType snowflakeRenderType = RenderType.create(
      "larp_snowflakes",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "snowflakes"))
               .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/snow"))
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
               .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
               .withCull(false)
               .build()
         )
         .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/effects.png"))
         .sortOnUpload()
         .createRenderSetup()
   );

   public static RenderType getRenderType() {
      if (lastReloadGeneration != PostProcessing.reloadCount) {
         ClientPipelines.validatePipeline(snowflakeRenderType.pipeline());
         lastReloadGeneration = PostProcessing.reloadCount;
      }

      return snowflakeRenderType;
   }

   private SnowflakeRenderTypes() {
   }
}
