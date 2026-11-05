package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class fo {
   private static final Identifier c = Identifier.fromNamespaceAndPath("larp-client", "distance_fog");
   private static final RenderPipeline v = G("distance_fog", 7);
   private static final RenderPipeline E = G("distance_fog_layer", 15);
   static final int i = 128;
   private static final Matrix4f q = new Matrix4f();
   private static boolean k;
   private static boolean D;
   private static boolean T;
   private static boolean V;
   private static GpuBuffer A;
   private static int p = -1;
   private static String b;

   private static RenderPipeline G(String var0, int var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/distance_fog"))
         .withBindGroupLayout(BindGroupLayout.builder().withUniform("Depth", of.t).withUniform("DistanceFog", UniformType.UNIFORM_BUFFER).build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.DEFAULT.format(), var1))
         .withCull(false)
         .build();
   }

   public static void B() {
      LevelRenderEvents.END_MAIN.addPhaseOrdering(c, Event.DEFAULT_PHASE);
      LevelRenderEvents.END_MAIN.register(c, fo::x);
   }

   public static void f(Matrix4f var0) {
      q.set(var0);
      k = (boolean)1;
   }

   private static void x(LevelRenderContext var0) {
      Minecraft var1 = Minecraft.getInstance();
      FogModule var2 = FogModule.A();
      if (var1.level != null && var2 != null && var2.p()) {
         CameraRenderState var3 = var0.levelState().cameraRenderState;
         if (var3 == null || var3.pos == null || var3.viewRotationMatrix == null || var3.projectionMatrix == null) {
            I("camera state not extracted");
         } else if (!X(var3.fogType)) {
            I("camera in " + var3.fogType + " fog");
         } else if (L()) {
            I("shader pack active");
         } else {
            float var4 = var2.T(var1.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            if (var4 <= 0.001F) {
               I("faded underground");
            } else {
               if (b != null) {
                  r4.g("Fog", "Resumed after {}", b);
                  b = null;
               }

               if (p != jk.v) {
                  v();
                  D = (boolean)0;
                  p = jk.v;
               }

               if (!D) {
                  long var5 = h_.U();
                  String var7 = "resources";

                  try {
                     if (A == null) {
                        e4.U(v);
                        e4.U(E);
                        A = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
                        r4.V("Fog", "Fog shader loaded; composed at the end of the main pass");
                     }

                     var7 = "parameters";
                     if (!k && !V) {
                        V = (boolean)1;
                        r4.m("Fog", "World projection was not captured; using the unbobbed camera projection");
                     }

                     Matrix4f var8 = new Matrix4f(k ? q : var3.projectionMatrix).mul(var3.viewRotationMatrix).invert();
                     LevelRenderState var9 = var0.levelState();
                     float var10 = var9.skyRenderState.sunAngle;
                     float var11 = -((float)Math.sin(var10));
                     float var12 = (float)Math.cos(var10);
                     float var13 = Math.clamp(var9.weatherRenderState.intensity, 0.0F, 1.0F);
                     float var14 = k(-0.08F, 0.14F, var12) * (1.0F - var13 * 0.85F);
                     float[] var15 = n(var2, var3.fogData == null ? null : var3.fogData.color);
                     float var16 = var1.level.getSeaLevel();
                     float var17 = (float)Math.sin(Math.toRadians(var2.I.getValue()));
                     MemoryStack var18 = MemoryStack.stackPush();

                     try {
                        ByteBuffer var19 = var18.malloc(128);
                        var8.get(var19);
                        var19.position(64);
                        var19.putFloat((float)var3.pos.y - var16)
                           .putFloat(1.0F / var2.f.getValue().floatValue())
                           .putFloat(RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F)
                           .putFloat(0.0F);
                        var19.putFloat(var2.H.getValue().floatValue()).putFloat(var2.f()).putFloat(1.0F * var4).putFloat(var17);
                        var19.putFloat(var15[0]).putFloat(var15[1]).putFloat(var15[2]).putFloat(var2.c.getValue().floatValue());
                        var19.putFloat(var11).putFloat(var12).putFloat(0.0F).putFloat(var14);
                        var19.flip();
                        RenderSystem.getDevice().createCommandEncoder().writeToBuffer(A.slice(), var19);
                     } catch (Throwable var35) {
                        if (var18 != null) {
                           try {
                              var18.close();
                           } catch (Throwable var33) {
                              var35.addSuppressed(var33);
                           }
                        }

                        throw var35;
                     }

                     if (var18 != null) {
                        var18.close();
                     }

                     var7 = "compose";
                     RenderTarget var38 = var1.gameRenderer.mainRenderTarget();
                     RenderTarget var39 = y8.m(var38);
                     RenderPass var20 = RenderSystem.getDevice()
                        .createCommandEncoder()
                        .createRenderPass(() -> "Larp distance fog", var39.getColorTextureView(), Optional.empty());

                     try {
                        of.z(var20, var39 == var38 ? v : E);
                        var20.setUniform("DistanceFog", A.slice());
                        of.h(var20, "Depth", var38.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                        var20.draw(3, 1, 0, 0);
                     } catch (Throwable var34) {
                        if (var20 != null) {
                           try {
                              var20.close();
                           } catch (Throwable var32) {
                              var34.addSuppressed(var32);
                           }
                        }

                        throw var34;
                     }

                     if (var20 != null) {
                        var20.close();
                     }

                     x1.N();
                     h_.A("Distance fog passes", 1L);
                  } catch (RuntimeException var36) {
                     D = (boolean)1;

                     try {
                        v();
                     } catch (RuntimeException var31) {
                        var36.addSuppressed(var31);
                     }

                     r4.r("Fog", "DistanceFogRenderer stage=" + var7 + "; fog disabled until resource reload", var36);
                  } finally {
                     h_.n("Distance fog", var5);
                  }
               }
            }
         }
      } else {
         b = null;
      }
   }

   static boolean X(FogType var0) {
      return var0 == FogType.NONE || var0 == FogType.ATMOSPHERIC;
   }

   private static void I(String var0) {
      if (!var0.equals(b)) {
         b = var0;
         r4.g("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] n(FogModule var0, Vector4f var1) {
      if (!var0.k.i("Custom") && var1 != null) {
         Integer var3 = y8.J() ? y() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.k.i("Custom") ? var0.d.getValue() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer y() {
      CustomSkyModule var0 = CustomSkyModule.Y();
      if (CustomSkyModule.i()) {
         return var0.y() ? var0.p().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.B();
      return var1 != null && var1.z() ? var1.L() : null;
   }

   static float k(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean L() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !T) {
            T = (boolean)1;
            r4.V("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            T = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!T) {
            T = (boolean)1;
            r4.m("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void v() {
      if (A != null) {
         GpuBuffer var0 = A;
         A = null;
         var0.close();
      }
   }

   private fo() {
   }
}
