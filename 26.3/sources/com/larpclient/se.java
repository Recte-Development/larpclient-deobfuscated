package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class se {
   public static final RenderPipeline Q = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "world"))
      .build();
   public static final RenderPipeline i = D("horizontal", "blur_h", false);
   public static final RenderPipeline p = D("vertical", "blur_v", false);
   public static final RenderPipeline j = D("copy", "copy", false);
   public static final RenderPipeline g = D("composite", "bloom", true);

   private static RenderPipeline D(String var0, String var1, boolean var2) {
      Builder var3 = BindGroupLayout.builder().withUniform("Source", p7.X);
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

   public static void j(RenderPipeline var0) {
      if (!p7.L(var0, null)) {
         throw new IllegalStateException(
            "Pipeline " + var0.getLocation() + " failed; shader=" + p7.a(var0) + "; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages()
         );
      }
   }
}
