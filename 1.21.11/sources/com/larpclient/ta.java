package com.larpclient;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_2960;
import org.lwjgl.system.MemoryStack;

public final class ta {
   private static final class_2960 W = o("performance/single_color");
   static final class_2960 C = o("performance/sodium_single_color");
   private static final String h = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> q = new IdentityHashMap<>();
   private static GpuBuffer A;
   private static int O;
   private static int U = -1;
   private static boolean k;
   private static boolean I;
   private static boolean Z;
   private static boolean o;

   static void E() {
      if (!k) {
         k = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         I = (boolean)(!var0.isModLoaded("iris") && !var0.isModLoaded("sodium") ? 0 : 1);
         if (I) {
            q8.G("SingleColor", "Iris or Sodium is loaded; Single Color Blocks keeps native terrain on 1.21.11");
         }

         WorldRenderEvents.START_MAIN.register((StartMain)var0x -> S());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> H());
      }
   }

   public static boolean D() {
      return !I && !Z;
   }

   static boolean N() {
      return o && !Z && PerformanceModule.w();
   }

   private static void S() {
      if (U != t_.g) {
         U = t_.g;
         q.clear();
         if (Z) {
            q8.G("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         Z = (boolean)0;
      }

      if (!I && !Z && PerformanceModule.w()) {
         String var0 = "uniform";

         try {
            if (A == null) {
               A = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               O = ~PerformanceModule.o();
               q8.G("SingleColor", "Single Color Blocks active on vanilla terrain");
            }

            int var1 = PerformanceModule.o();
            if (var1 != O) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(A.slice(), var3);
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

               O = var1;
            }

            var0 = "vanilla-compile";
            s(class_10799.field_64225);
            s(class_10799.field_64227);
            o = (boolean)1;
         } catch (RuntimeException var7) {
            Z(var0, var7);
         }
      } else {
         if (A != null) {
            H();
         }
      }
   }

   static RenderPipeline f(RenderPipeline var0) {
      return !N() ? null : q.get(var0);
   }

   static boolean l(RenderPass var0, RenderPipeline var1) {
      if (A != null && L(var1)) {
         var0.setUniform("LarpSingleColor", A.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean L(RenderPipeline var0) {
      return q.containsValue(var0);
   }

   private static RenderPipeline s(RenderPipeline var0) {
      return q.computeIfAbsent(
         var0,
         var0x -> {
            RenderPipeline var1 = f2.i(var0x)
               .withLocation(o("pipeline/performance/single_color/" + var0x.getLocation().method_12832()))
               .withVertexShader(W)
               .withFragmentShader(W)
               .withUniform("LarpSingleColor", class_10789.field_60031)
               .build();
            f2.a(var1);
            return var1;
         }
      );
   }

   public static String l(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return ih.v(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static void Z(String var0, RuntimeException var1) {
      Z = (boolean)1;
      o = (boolean)0;
      q8.u("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void H() {
      o = (boolean)0;
      if (A != null) {
         A.close();
         A = null;
         q8.G("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static class_2960 o(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private ta() {
   }
}
