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

public final class in {
   private static final Identifier C = r("performance/single_color");
   static final Identifier K = r("performance/sodium_single_color");
   private static final String k = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> a = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, in.v> X = new IdentityHashMap<>();
   private static final List<RenderPipeline> T = new ArrayList<>();
   private static GpuBuffer Q;
   private static int H;
   private static int J = -1;
   private static boolean Y;
   private static boolean U;
   private static boolean F;
   private static boolean c;
   private static boolean q;

   static void w() {
      if (!Y) {
         Y = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         F = (boolean)(!var0.isModLoaded("iris") && (!var0.isModLoaded("sodium") || p.T()) ? 0 : 1);
         U = (boolean)(var0.isModLoaded("sodium") && !F ? 1 : 0);
         if (F) {
            _q.z("SingleColor", "Iris or an unsupported Sodium is loaded; Single Color Blocks keeps native terrain");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0x -> q());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> a());
      }
   }

   public static boolean I() {
      return !F && !c;
   }

   static boolean y() {
      return q && !c && PerformanceModule.h();
   }

   private static void q() {
      if (J != nl.q) {
         J = nl.q;
         a.clear();
         X.clear();
         T.clear();
         if (c) {
            _q.z("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         c = (boolean)0;
      }

      if (!F && !c && PerformanceModule.h()) {
         String var0 = "uniform";

         try {
            if (Q == null) {
               Q = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               H = ~PerformanceModule.R();
               _q.z("SingleColor", "Single Color Blocks active on {} terrain", U ? "Sodium " + p.S() : "vanilla");
            }

            int var1 = PerformanceModule.R();
            if (var1 != H) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(Q.slice(), var3);
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

               H = var1;
            }

            if (!U) {
               var0 = "vanilla-compile";
               F(RenderPipelines.SOLID_TERRAIN);
               F(RenderPipelines.CUTOUT_TERRAIN);
            } else {
               var0 = "sodium-compile";

               for (RenderPipeline var10 : T) {
                  in.v var4 = Y(var10);
                  p.L(var4.pipeline(), var4.source());
                  X.put(var10, var4);
                  _q.z("SingleColor", "Sodium pass {} draws through {}", var10.getLocation(), var4.pipeline().getLocation());
               }

               T.clear();

               for (in.v var11 : X.values()) {
                  p.L(var11.pipeline(), var11.source());
               }
            }

            q = (boolean)1;
         } catch (RuntimeException var7) {
            q(var0, var7);
         }
      } else {
         if (Q != null) {
            a();
         }
      }
   }

   static RenderPipeline t(RenderPipeline var0) {
      return !U && y() ? a.get(var0) : null;
   }

   static RenderPipeline w(RenderPipeline var0) {
      if (U && !c && !F && PerformanceModule.h() && !p.o(var0)) {
         in.v var1 = X.get(var0);
         if (var1 != null && q) {
            return var1.pipeline();
         }

         if (!T.contains(var0)) {
            T.add(var0);
         }

         return null;
      } else {
         return null;
      }
   }

   static boolean E(RenderPass var0, RenderPipeline var1) {
      if (Q != null && W(var1)) {
         var0.setUniform("LarpSingleColor", Q.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean W(RenderPipeline var0) {
      if (a.containsValue(var0)) {
         return true;
      }

      for (in.v var2 : X.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   private static RenderPipeline F(RenderPipeline var0) {
      return a.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(r("pipeline/performance/single_color/" + var0x.getLocation().getPath()))
               .withVertexShader(C)
               .withFragmentShader(C)
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));
            t(var0x, var1);
            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2x) -> var1.withShaderDefine(var1x, Float.parseFloat(var2x)));
            RenderPipeline var2 = var1.build();
            se.j(var2);
            return var2;
         }
      );
   }

   public static String S(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return p.W(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static in.v Y(RenderPipeline var0) {
      ShaderSource var1 = p7.o();
      String var2 = S(p.g(var1.getShader(p7.X(var0), ShaderType.VERTEX), p7.X(var0)), p.g(var1.getShader(K, ShaderType.VERTEX), K));
      String var3 = p.W(
         p.g(var1.getShader(p7.a(var0), ShaderType.FRAGMENT), p7.a(var0)),
         p.g(var1.getShader(K, ShaderType.FRAGMENT), K),
         ShaderType.FRAGMENT,
         "larp_single_block"
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "single_color/" + var0.getLocation().getPath()))
         .withVertexShader(K)
         .withFragmentShader(K)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));
      t(var0, var4);
      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var5 = p7.w((var3x, var4x) -> !var3x.equals(K) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new in.v(var4.build(), var5);
   }

   private static void t(RenderPipeline var0, Builder var1) {
      for (BindGroupLayout var3 : var0.getBindGroupLayouts()) {
         var1.withBindGroupLayout(var3);
      }

      var1.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpSingleColor", UniformType.UNIFORM_BUFFER).build());
      List var5 = p7.i(var0);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
      }

      List var7 = p7.A(var0);

      for (int var4 = 0; var4 < var7.size(); var4++) {
         var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
      }
   }

   private static void q(String var0, RuntimeException var1) {
      c = (boolean)1;
      q = (boolean)0;
      _q.o("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void a() {
      q = (boolean)0;
      if (Q != null) {
         Q.close();
         Q = null;
         _q.z("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static Identifier r(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private in() {
   }

   private record v(RenderPipeline pipeline, ShaderSource source) {
   }
}
