package com.larpclient;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class kx {
   private static final Identifier w = u("performance/single_color");
   static final Identifier I = u("performance/sodium_single_color");
   private static final String v = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> q = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, kx.w> p = new IdentityHashMap<>();
   private static final List<RenderPipeline> G = new ArrayList<>();
   private static GpuBuffer S;
   private static int t;
   private static int Q = -1;
   private static boolean L;
   private static boolean P;
   private static boolean R;
   private static boolean D;
   private static boolean d;

   static void I() {
      if (!L) {
         L = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         R = (boolean)(!var0.isModLoaded("iris") && (!var0.isModLoaded("sodium") || k_.r()) ? 0 : 1);
         P = (boolean)(var0.isModLoaded("sodium") && !R ? 1 : 0);
         if (R) {
            h.A("SingleColor", "Iris or an unsupported Sodium is loaded; Single Color Blocks keeps native terrain");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0x -> t());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> J());
      }
   }

   public static boolean Z() {
      return !R && !D;
   }

   static boolean t() {
      return d && !D && PerformanceModule.D();
   }

   private static void t() {
      if (Q != wa.s) {
         Q = wa.s;
         q.clear();
         p.clear();
         G.clear();
         if (D) {
            h.A("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         D = (boolean)0;
      }

      if (!R && !D && PerformanceModule.D()) {
         String var0 = "uniform";

         try {
            if (S == null) {
               S = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               t = ~PerformanceModule.Q();
               h.A("SingleColor", "Single Color Blocks active on {} terrain", P ? "Sodium " + k_.E() : "vanilla");
            }

            int var1 = PerformanceModule.Q();
            if (var1 != t) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(S.slice(), var3);
               } catch (Throwable var6) {
                  if (var2 != null) {
                     try {
                        var2.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (var2 != null) {
                  var2.close();
               }

               t = var1;
            }

            if (!P) {
               var0 = "vanilla-compile";
               V(RenderPipelines.SOLID_TERRAIN);
               V(RenderPipelines.CUTOUT_TERRAIN);
            } else {
               var0 = "sodium-compile";

               for (RenderPipeline var10 : G) {
                  kx.w var4 = q(var10);
                  k_.n(var4.pipeline(), var4.source());
                  p.put(var10, var4);
                  h.A("SingleColor", "Sodium pass {} draws through {}", var10.getLocation(), var4.pipeline().getLocation());
               }

               G.clear();

               for (kx.w var11 : p.values()) {
                  k_.n(var11.pipeline(), var11.source());
               }
            }

            d = (boolean)1;
         } catch (RuntimeException var7) {
            A(var0, var7);
         }
      } else {
         if (S != null) {
            J();
         }
      }
   }

   static RenderPipeline o(RenderPipeline var0) {
      return !P && t() ? q.get(var0) : null;
   }

   static RenderPipeline N(RenderPipeline var0) {
      if (P && !D && !R && PerformanceModule.D() && !k_.u(var0)) {
         kx.w var1 = p.get(var0);
         if (var1 != null && d) {
            return var1.pipeline();
         }

         if (!G.contains(var0)) {
            G.add(var0);
         }

         return null;
      } else {
         return null;
      }
   }

   static boolean A(RenderPass var0, RenderPipeline var1) {
      if (S != null && f(var1)) {
         var0.setUniform("LarpSingleColor", S.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean f(RenderPipeline var0) {
      if (q.containsValue(var0)) {
         return true;
      }

      for (kx.w var2 : p.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   private static RenderPipeline V(RenderPipeline var0) {
      return q.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(u("pipeline/performance/single_color/" + var0x.getLocation().getPath()))
               .withVertexShader(w)
               .withFragmentShader(w)
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));
            s(var0x, var1);
            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2x) -> var1.withShaderDefine(var1x, Float.parseFloat(var2x)));
            RenderPipeline var2 = var1.build();
            ai.U(var2);
            return var2;
         }
      );
   }

   public static String z(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return k_.L(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static kx.w q(RenderPipeline var0) {
      ShaderManager var1 = mx.Q();
      String var2 = z(k_.O(var1.getShader(mx.R(var0), ShaderType.VERTEX), mx.R(var0)), k_.O(var1.getShader(I, ShaderType.VERTEX), I));
      String var3 = k_.L(
         k_.O(var1.getShader(mx.K(var0), ShaderType.FRAGMENT), mx.K(var0)),
         k_.O(var1.getShader(I, ShaderType.FRAGMENT), I),
         ShaderType.FRAGMENT,
         "larp_single_block"
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "single_color/" + var0.getLocation().getPath()))
         .withVertexShader(I)
         .withFragmentShader(I)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));
      s(var0, var4);
      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var5 = mx.V((var3x, var4x) -> !var3x.equals(I) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new kx.w(var4.build(), var5);
   }

   private static void s(RenderPipeline var0, Builder var1) {
      for (BindGroupLayout var3 : var0.getBindGroupLayouts()) {
         var1.withBindGroupLayout(var3);
      }

      var1.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpSingleColor", UniformType.UNIFORM_BUFFER).build());
      List var5 = mx.G(var0);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
      }

      List var7 = mx.i(var0);

      for (int var4 = 0; var4 < var7.size(); var4++) {
         var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
      }
   }

   private static void A(String var0, RuntimeException var1) {
      D = (boolean)1;
      d = (boolean)0;
      h.m("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void J() {
      d = (boolean)0;
      if (S != null) {
         S.close();
         S = null;
         h.A("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static Identifier u(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private kx() {
   }

   private record w(RenderPipeline pipeline, ShaderSource source) {
   }
}
