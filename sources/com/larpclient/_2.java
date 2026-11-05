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

public final class _2 {
   private static final Identifier U = H("performance/single_color");
   static final Identifier v = H("performance/sodium_single_color");
   private static final String O = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> y = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, _2.e> V = new IdentityHashMap<>();
   private static final List<RenderPipeline> I = new ArrayList<>();
   private static GpuBuffer u;
   private static int Z;
   private static int s = -1;
   private static boolean L;
   private static boolean k;
   private static boolean P;
   private static boolean f;
   private static boolean d;

   static void s() {
      if (!L) {
         L = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         P = (boolean)(!var0.isModLoaded("iris") && (!var0.isModLoaded("sodium") || mk.o()) ? 0 : 1);
         k = (boolean)(var0.isModLoaded("sodium") && !P ? 1 : 0);
         if (P) {
            r4.V("SingleColor", "Iris or an unsupported Sodium is loaded; Single Color Blocks keeps native terrain");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0x -> Y());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> R());
      }
   }

   public static boolean o() {
      return !P && !f;
   }

   static boolean s() {
      return d && !f && PerformanceModule.A();
   }

   private static void Y() {
      if (s != jk.v) {
         s = jk.v;
         y.clear();
         V.clear();
         I.clear();
         if (f) {
            r4.V("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         f = (boolean)0;
      }

      if (!P && !f && PerformanceModule.A()) {
         String var0 = "uniform";

         try {
            if (u == null) {
               u = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               Z = ~PerformanceModule.G();
               r4.V("SingleColor", "Single Color Blocks active on {} terrain", k ? "Sodium " + mk.Z() : "vanilla");
            }

            int var1 = PerformanceModule.G();
            if (var1 != Z) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(u.slice(), var3);
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

               Z = var1;
            }

            if (!k) {
               var0 = "vanilla-compile";
               t(RenderPipelines.SOLID_TERRAIN);
               t(RenderPipelines.CUTOUT_TERRAIN);
            } else {
               var0 = "sodium-compile";

               for (RenderPipeline var10 : I) {
                  _2.e var4 = f(var10);
                  mk.t(var4.pipeline(), var4.source());
                  V.put(var10, var4);
                  r4.V("SingleColor", "Sodium pass {} draws through {}", var10.getLocation(), var4.pipeline().getLocation());
               }

               I.clear();

               for (_2.e var11 : V.values()) {
                  mk.t(var11.pipeline(), var11.source());
               }
            }

            d = (boolean)1;
         } catch (RuntimeException var7) {
            L(var0, var7);
         }
      } else {
         if (u != null) {
            R();
         }
      }
   }

   static RenderPipeline J(RenderPipeline var0) {
      return !k && s() ? y.get(var0) : null;
   }

   static RenderPipeline U(RenderPipeline var0) {
      if (k && !f && !P && PerformanceModule.A() && !mk.v(var0)) {
         _2.e var1 = V.get(var0);
         if (var1 != null && d) {
            return var1.pipeline();
         }

         if (!I.contains(var0)) {
            I.add(var0);
         }

         return null;
      } else {
         return null;
      }
   }

   static boolean J(RenderPass var0, RenderPipeline var1) {
      if (u != null && p(var1)) {
         var0.setUniform("LarpSingleColor", u.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean p(RenderPipeline var0) {
      if (y.containsValue(var0)) {
         return true;
      }

      for (_2.e var2 : V.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   private static RenderPipeline t(RenderPipeline var0) {
      return y.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(H("pipeline/performance/single_color/" + var0x.getLocation().getPath()))
               .withVertexShader(U)
               .withFragmentShader(U)
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));
            A(var0x, var1);
            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2x) -> var1.withShaderDefine(var1x, Float.parseFloat(var2x)));
            RenderPipeline var2 = var1.build();
            e4.U(var2);
            return var2;
         }
      );
   }

   public static String B(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return mk.V(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static _2.e f(RenderPipeline var0) {
      ShaderSource var1 = of.v();
      String var2 = B(mk.y(var1.getShader(of.C(var0), ShaderType.VERTEX), of.C(var0)), mk.y(var1.getShader(v, ShaderType.VERTEX), v));
      String var3 = mk.V(
         mk.y(var1.getShader(of.b(var0), ShaderType.FRAGMENT), of.b(var0)),
         mk.y(var1.getShader(v, ShaderType.FRAGMENT), v),
         ShaderType.FRAGMENT,
         "larp_single_block"
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "single_color/" + var0.getLocation().getPath()))
         .withVertexShader(v)
         .withFragmentShader(v)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));
      A(var0, var4);
      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var5 = of.A((var3x, var4x) -> !var3x.equals(v) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new _2.e(var4.build(), var5);
   }

   private static void A(RenderPipeline var0, Builder var1) {
      for (BindGroupLayout var3 : var0.getBindGroupLayouts()) {
         var1.withBindGroupLayout(var3);
      }

      var1.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpSingleColor", UniformType.UNIFORM_BUFFER).build());
      List var5 = of.J(var0);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
      }

      List var7 = of.m(var0);

      for (int var4 = 0; var4 < var7.size(); var4++) {
         var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
      }
   }

   private static void L(String var0, RuntimeException var1) {
      f = (boolean)1;
      d = (boolean)0;
      r4.r("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void R() {
      d = (boolean)0;
      if (u != null) {
         u.close();
         u = null;
         r4.V("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static Identifier H(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private _2() {
   }

   private record e(RenderPipeline pipeline, ShaderSource source) {
   }
}
