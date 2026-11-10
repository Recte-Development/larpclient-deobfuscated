package com.larpclient;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class z_ {
   public static final String V = "Banana";
   private static final Identifier q = Identifier.fromNamespaceAndPath("larp-client", "models/player/banana/mesh.bin");
   private static final RenderType N = RenderType.create(
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
   private static w5.o k;
   private static long Q = -1L;

   public static boolean O() {
      return k != null && Q == wa.s;
   }

   public static w5.o Z() {
      if (!O()) {
         throw new IllegalStateException("Banana asset is not prepared");
      } else {
         return k;
      }
   }

   public static void G() {
      if (!O()) {
         k = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(q).open()) {
            w5.o var1 = w5.y(var0);
            ai.U(N.pipeline());
            k = var1;
            Q = wa.s;
            h.U("Banana", "Mesh ready: {} triangles, {} vertices, {} bones, resource generation {}", k.triangles(), k.vertices(), 11, Q);
         } catch (IOException var5) {
            throw new IllegalStateException("Banana asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void w(PoseStack var0, hh var1, Matrix4f var2, w5 var3) {
      if (O()) {
         Matrix4f var4 = new Matrix4f(var0.last().pose()).mul(var2);
         kj.E(var1, N, var4, var3.K(), var3.m());
         zj.K("Banana triangles", var3.m().length / 3);
      }
   }

   private z_() {
   }
}
