package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public final class wx {
   private static int d = -1;
   private static final RenderType E = RenderType.create(
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

   public static RenderType N() {
      if (d != wa.s) {
         ai.U(E.pipeline());
         d = wa.s;
      }

      return E;
   }

   private wx() {
   }
}
