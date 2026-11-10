package com.larpclient;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class ai {
   public static final RenderPipeline N = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "world"))
      .build();
   public static final RenderPipeline j = I("horizontal", "blur_h", false);
   public static final RenderPipeline F = I("vertical", "blur_v", false);
   public static final RenderPipeline C = I("copy", "copy", false);
   public static final RenderPipeline g = I("composite", "bloom", true);

   private static RenderPipeline I(String var0, String var1, boolean var2) {
      Builder var3 = BindGroupLayout.builder().withSampler("Source");
      if (!var1.equals("copy")) {
         var3.withUniform("PostParameters", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/" + var1))
         .withBindGroupLayout(var3.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var2 ? new ColorTargetState(BlendFunction.ADDITIVE) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   public static void U(RenderPipeline var0) {
      if (!mx.p(var0, null)) {
         throw new IllegalStateException(
            "Pipeline " + var0.getLocation() + " failed; shader=" + mx.K(var0) + "; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages()
         );
      }
   }
}
