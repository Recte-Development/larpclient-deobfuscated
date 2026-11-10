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

public final class ho {
   public static final String Y = "Bitcoin";
   public static final double e = 0.6;
   public static final double U = 1.6;
   public static final int F = -552166;
   private static final String v = "models/pets/";
   private static final RenderPipeline x = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(l("bitcoin_pet"))
      .withFragmentShader(l("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   public static final List<ho.z> X = List.of(
      d("Bitcoin", "bitcoin", 0.6, -552166),
      d("Solana", "solana", 0.6, -6732289),
      d("Ethereum", "ethereum", 0.6, -10322198),
      d("Litecoin", "litecoin", 0.6, -13345379),
      d("Monero", "monero", 0.6, -39424)
   );
   private static ir.j R;
   private static long D = -1L;
   private static long f = -1L;

   private static Identifier l(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static ho.z d(String var0, String var1, double var2, int var4) {
      return new ho.z(
         var0,
         var1,
         var2,
         var4,
         RenderType.create(
            "larp_" + var1 + "_pet", RenderSetup.builder(x).withTexture("Sampler0", l("models/pets/" + var1 + "/albedo.png")).createRenderSetup()
         ),
         var0 + " triangles"
      );
   }

   public static ho.z V(String var0) {
      for (ho.z var2 : X) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean T() {
      return R != null && D == nl.q;
   }

   public static void l() {
      if (!T()) {
         R = null;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(l("models/pets/bitcoin/mesh.bin")).open()) {
            for (ho.z var3 : X) {
               var0.getResourceOrThrow(l("models/pets/" + var3.folder() + "/albedo.png"));
            }

            ir.j var7 = ir.Y(var1);
            se.j(x);
            R = var7;
            D = nl.q;
            _q.c("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", X.size(), R.triangles(), D);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean F(String var0) {
      if (T()) {
         return true;
      }

      if (f == nl.q) {
         return false;
      }

      try {
         l();
         return true;
      } catch (RuntimeException var2) {
         f = nl.q;
         _q.o(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static ir.j P() {
      return T() ? R : null;
   }

   public static void R(fz var0, ho.z var1, Matrix4f var2) {
      if (T()) {
         var0.w(var1.type(), var2, R.vertices(), R.colors());
         he.Z(var1.profile(), R.triangles());
      }
   }

   public static void w(fz var0, ho.z var1, Vec3 var2, Quaternionfc var3, float var4) {
      if (T()) {
         var0.L(var1.type(), var2, var3, var4, R.vertices(), R.colors());
         he.Z(var1.profile(), R.triangles());
      }
   }

   private ho() {
   }

   public record z(String style, String folder, double scale, int spark, RenderType type, String profile) {
   }
}
