package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.pipeline.RenderPipeline.UniformDescription;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_290;
import net.minecraft.class_2960;

public final class f2 {
   public static final RenderPipeline L = RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
      .withLocation(class_2960.method_60655("larp-client", "world"))
      .build();
   public static final RenderPipeline y = J("horizontal", "blur_h", false);
   public static final RenderPipeline D = J("vertical", "blur_v", false);
   public static final RenderPipeline f = J("copy", "copy", false);
   public static final RenderPipeline X = J("composite", "bloom", true);

   private static RenderPipeline J(String var0, String var1, boolean var2) {
      Builder var3 = RenderPipeline.builder(new Snippet[0]).withSampler("Source");
      if (!var1.equals("copy")) {
         var3.withUniform("PostParameters", class_10789.field_60031);
      }

      if (var2) {
         var3.withBlend(BlendFunction.ADDITIVE);
      }

      return var3.withLocation(class_2960.method_60655("larp-client", var0))
         .withVertexShader(class_2960.method_60655("larp-client", "post/fullscreen"))
         .withFragmentShader(class_2960.method_60655("larp-client", "post/" + var1))
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   public static Builder i(RenderPipeline var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[0])
         .withVertexShader(yf.E(var0))
         .withFragmentShader(yf.z(var0))
         .withVertexFormat(var0.getVertexFormat(), var0.getVertexFormatMode())
         .withDepthTestFunction(var0.getDepthTestFunction())
         .withDepthWrite(var0.isWriteDepth())
         .withColorWrite(var0.isWriteColor(), var0.isWriteAlpha())
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withColorLogic(var0.getColorLogic())
         .withDepthBias(var0.getDepthBiasScaleFactor(), var0.getDepthBiasConstant());
      var0.getBlendFunction().ifPresentOrElse(var1::withBlend, var1::withoutBlend);
      var0.getSamplers().forEach(var1::withSampler);

      for (UniformDescription var3 : var0.getUniforms()) {
         if (var3.textureFormat() == null) {
            var1.withUniform(var3.name(), var3.type());
         } else {
            var1.withUniform(var3.name(), var3.type(), var3.textureFormat());
         }
      }

      var0.getShaderDefines().comp_3104().forEach(var1::withShaderDefine);
      var0.getShaderDefines().comp_3103().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
      return var1;
   }

   public static void a(RenderPipeline var0) {
      if (!yf.V(var0, null)) {
         throw new IllegalStateException(
            "Pipeline " + var0.getLocation() + " failed; shader=" + yf.z(var0) + "; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages()
         );
      }
   }
}
