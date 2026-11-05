package com.larpclient;

import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class jz {
   public static final String M = "Bitcoin";
   public static final double b = 0.6;
   public static final double Y = 1.6;
   public static final int x = -552166;
   private static final String d = "models/pets/";
   private static final RenderPipeline I = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(A("bitcoin_pet"))
      .withFragmentShader(A("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   public static final List<jz.d> u = List.of(
      x("Bitcoin", "bitcoin", 0.6, -552166),
      x("Solana", "solana", 0.6, -6732289),
      x("Ethereum", "ethereum", 0.6, -10322198),
      x("Litecoin", "litecoin", 0.6, -13345379),
      x("Monero", "monero", 0.6, -39424)
   );
   private static u1.x w;
   private static long A = -1L;
   private static long G = -1L;

   private static Identifier A(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static jz.d x(String var0, String var1, double var2, int var4) {
      return new jz.d(
         var0,
         var1,
         var2,
         var4,
         RenderType.create(
            "larp_" + var1 + "_pet", RenderSetup.builder(I).withTexture("Sampler0", A("models/pets/" + var1 + "/albedo.png")).createRenderSetup()
         ),
         var0 + " triangles"
      );
   }

   public static jz.d Y(String var0) {
      for (jz.d var2 : u) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean M() {
      return w != null && A == jk.v;
   }

   public static void o() {
      if (!M()) {
         w = null;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(A("models/pets/bitcoin/mesh.bin")).open()) {
            for (jz.d var3 : u) {
               var0.getResourceOrThrow(A("models/pets/" + var3.folder() + "/albedo.png"));
            }

            u1.x var7 = u1.Q(var1);
            e4.U(I);
            w = var7;
            A = jk.v;
            r4.g("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", u.size(), w.triangles(), A);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean u(String var0) {
      if (M()) {
         return true;
      }

      if (G == jk.v) {
         return false;
      }

      try {
         o();
         return true;
      } catch (RuntimeException var2) {
         G = jk.v;
         r4.r(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static u1.x b() {
      return M() ? w : null;
   }

   public static void t(r6 var0, jz.d var1, Matrix4f var2) {
      if (M()) {
         var0.g(var1.type(), var2, w.vertices(), w.colors());
         h_.A(var1.profile(), w.triangles());
      }
   }

   public static void i(r6 var0, jz.d var1, Vec3 var2, Quaternionfc var3, float var4) {
      if (M()) {
         var0.R(var1.type(), var2, var3, var4, w.vertices(), w.colors());
         h_.A(var1.profile(), w.triangles());
      }
   }

   private jz() {
   }

   public record d(String style, String folder, double scale, int spark, RenderType type, String profile) {
   }
}
