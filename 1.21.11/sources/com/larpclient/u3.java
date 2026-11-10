package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_2960;

public final class u3 {
   private static int b = -1;
   private static final class_1921 B = class_1921.method_75940(
      "larp_snowflakes",
      class_12247.method_75927(
            RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
               .withLocation(class_2960.method_60655("larp-client", "snowflakes"))
               .withFragmentShader(class_2960.method_60655("larp-client", "particle/snow"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.TRANSLUCENT)
               .withCull(false)
               .build()
         )
         .method_75934("Sampler0", class_2960.method_60655("larp-client", "textures/particles/effects.png"))
         .method_75937()
         .method_75938()
   );

   public static class_1921 B() {
      if (b != t_.g) {
         f2.a(B.method_73243());
         b = t_.g;
      }

      return B;
   }

   private u3() {
   }
}
