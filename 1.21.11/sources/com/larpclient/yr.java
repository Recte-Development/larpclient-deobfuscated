package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import org.joml.Matrix4f;

public final class yr {
   public static final String v = "Banana";
   private static final class_2960 c = class_2960.method_60655("larp-client", "models/player/banana/mesh.bin");
   private static final class_1921 d = class_1921.method_75940(
      "larp_banana_player",
      class_12247.method_75927(
            RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
               .withLocation(class_2960.method_60655("larp-client", "banana_player"))
               .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
               .withVertexFormat(class_290.field_1575, class_5596.field_27379)
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(true)
               .withoutBlend()
               .withCull(true)
               .build()
         )
         .method_75934("Sampler0", class_2960.method_60655("larp-client", "models/player/banana/albedo.png"))
         .method_75938()
   );
   private static ef.b C;
   private static long w = -1L;

   public static boolean m() {
      return C != null && w == t_.g;
   }

   public static ef.b Y() {
      if (!m()) {
         throw new IllegalStateException("Banana asset is not prepared");
      } else {
         return C;
      }
   }

   public static void g() {
      if (!m()) {
         C = null;

         try (InputStream var0 = class_310.method_1551().method_1478().getResourceOrThrow(c).method_14482()) {
            ef.b var1 = ef.g(var0);
            f2.a(d.method_73243());
            C = var1;
            w = t_.g;
            q8.v("Banana", "Mesh ready: {} triangles, {} vertices, {} bones, resource generation {}", C.triangles(), C.vertices(), 11, w);
         } catch (IOException var5) {
            throw new IllegalStateException("Banana asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void X(class_4587 var0, cw var1, Matrix4f var2, ef var3) {
      if (m()) {
         Matrix4f var4 = new Matrix4f(var0.method_23760().method_23761()).mul(var2);
         f1.h(var1, d, var4, var3.y(), var3.a());
         c5.V("Banana triangles", var3.a().length / 3);
      }
   }

   private yr() {
   }
}
