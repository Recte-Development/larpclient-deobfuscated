package com.larpclient;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.vertex.VertexFormat;
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
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class of {
   private static final Identifier g = h("performance/single_color");
   static final Identifier S = h("performance/sodium_single_color");
   private static final String V = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> E = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, of.x> X = new IdentityHashMap<>();
   private static final List<RenderPipeline> e = new ArrayList<>();
   private static GpuBuffer f;
   private static int c;
   private static int j = -1;
   private static boolean J;
   private static boolean n;
   private static boolean o;
   private static boolean C;
   private static boolean b;

   static void I() {
      if (!J) {
         J = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         o = (boolean)(!var0.isModLoaded("iris") && (!var0.isModLoaded("sodium") || w.t()) ? 0 : 1);
         n = (boolean)(var0.isModLoaded("sodium") && !o ? 1 : 0);
         if (o) {
            mt.K("SingleColor", "Iris or an unsupported Sodium is loaded; Single Color Blocks keeps native terrain");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0x -> V());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> p());
      }
   }

   public static boolean x() {
      return !o && !C;
   }

   static boolean A() {
      return b && !C && PerformanceModule.n();
   }

   private static void V() {
      if (j != m5.k) {
         j = m5.k;
         E.clear();
         X.clear();
         e.clear();
         if (C) {
            mt.K("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         C = (boolean)0;
      }

      if (!o && !C && PerformanceModule.n()) {
         String var0 = "uniform";

         try {
            if (f == null) {
               f = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               c = ~PerformanceModule.g();
               mt.K("SingleColor", "Single Color Blocks active on {} terrain", n ? "Sodium " + w.q() : "vanilla");
            }

            int var1 = PerformanceModule.g();
            if (var1 != c) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(f.slice(), var3);
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

               c = var1;
            }

            if (!n) {
               var0 = "vanilla-compile";
               l(RenderPipelines.SOLID_TERRAIN);
               l(RenderPipelines.CUTOUT_TERRAIN);
            } else {
               var0 = "sodium-compile";

               for (RenderPipeline var10 : e) {
                  of.x var4 = H(var10);
                  w.x(var4.pipeline(), var4.source());
                  X.put(var10, var4);
                  mt.K("SingleColor", "Sodium pass {} draws through {}", var10.getLocation(), var4.pipeline().getLocation());
               }

               e.clear();

               for (of.x var11 : X.values()) {
                  w.x(var11.pipeline(), var11.source());
               }
            }

            b = (boolean)1;
         } catch (RuntimeException var7) {
            M(var0, var7);
         }
      } else {
         if (f != null) {
            p();
         }
      }
   }

   static RenderPipeline y(RenderPipeline var0) {
      return !n && A() ? E.get(var0) : null;
   }

   static RenderPipeline N(RenderPipeline var0) {
      if (n && !C && !o && PerformanceModule.n() && !w.z(var0)) {
         of.x var1 = X.get(var0);
         if (var1 != null && b) {
            return var1.pipeline();
         }

         if (!e.contains(var0)) {
            e.add(var0);
         }

         return null;
      } else {
         return null;
      }
   }

   static boolean B(RenderPass var0, RenderPipeline var1) {
      if (f != null && z(var1)) {
         var0.setUniform("LarpSingleColor", f.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean z(RenderPipeline var0) {
      if (E.containsValue(var0)) {
         return true;
      }

      for (of.x var2 : X.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   private static RenderPipeline l(RenderPipeline var0) {
      return E.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(h("pipeline/performance/single_color/" + var0x.getLocation().getPath()))
               .withVertexShader(g)
               .withFragmentShader(g)
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));
            g(var0x, var1);
            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2x) -> var1.withShaderDefine(var1x, Float.parseFloat(var2x)));
            RenderPipeline var2 = var1.build();
            ea.z(var2);
            return var2;
         }
      );
   }

   public static String g(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return w.j(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static of.x H(RenderPipeline var0) {
      ShaderSource var1 = hj.k();
      String var2 = g(w.k(var1.getShader(hj.D(var0), ShaderType.VERTEX), hj.D(var0)), w.k(var1.getShader(S, ShaderType.VERTEX), S));
      String var3 = w.j(
         w.k(var1.getShader(hj.q(var0), ShaderType.FRAGMENT), hj.q(var0)),
         w.k(var1.getShader(S, ShaderType.FRAGMENT), S),
         ShaderType.FRAGMENT,
         "larp_single_block"
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "single_color/" + var0.getLocation().getPath()))
         .withVertexShader(S)
         .withFragmentShader(S)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));
      g(var0, var4);
      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var5 = hj.t((var3x, var4x) -> !var3x.equals(S) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new of.x(var4.build(), var5);
   }

   private static void g(RenderPipeline var0, Builder var1) {
      for (BindGroupLayout var3 : var0.getBindGroupLayouts()) {
         var1.withBindGroupLayout(var3);
      }

      var1.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpSingleColor", UniformType.UNIFORM_BUFFER).build());
      List var5 = hj.n(var0);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
      }

      List var7 = hj.P(var0);

      for (int var4 = 0; var4 < var7.size(); var4++) {
         var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
      }
   }

   private static void M(String var0, RuntimeException var1) {
      C = (boolean)1;
      b = (boolean)0;
      mt.P("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void p() {
      b = (boolean)0;
      if (f != null) {
         f.close();
         f = null;
         mt.K("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static Identifier h(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private of() {
   }

   private record x(RenderPipeline pipeline, ShaderSource source) {
   }
}
