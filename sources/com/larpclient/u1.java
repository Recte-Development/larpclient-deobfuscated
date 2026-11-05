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

public final class u1 {
   private static final Identifier N = Identifier.fromNamespaceAndPath("larp-client", "distance_fog");
   private static final RenderPipeline L = K("distance_fog", 7);
   private static final RenderPipeline Z = K("distance_fog_layer", 15);
   static final int g = 128;
   private static final Matrix4f S = new Matrix4f();
   private static boolean t;
   private static boolean R;
   private static boolean l;
   private static boolean Q;
   private static GpuBuffer P;
   private static int o = -1;
   private static String s;

   private static RenderPipeline K(String var0, int var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/distance_fog"))
         .withBindGroupLayout(BindGroupLayout.builder().withUniform("Depth", hj.G).withUniform("DistanceFog", UniformType.UNIFORM_BUFFER).build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.DEFAULT.format(), var1))
         .withCull(false)
         .build();
   }

   public static void w() {
      LevelRenderEvents.END_MAIN.addPhaseOrdering(N, Event.DEFAULT_PHASE);
      LevelRenderEvents.END_MAIN.register(N, u1::V);
   }

   public static void Z(Matrix4f var0) {
      S.set(var0);
      t = (boolean)1;
   }

   private static void V(LevelRenderContext var0) {
      Minecraft var1 = Minecraft.getInstance();
      FogModule var2 = FogModule.U();
      if (var1.level != null && var2 != null && var2.i()) {
         CameraRenderState var3 = var0.levelState().cameraRenderState;
         if (var3 == null || var3.pos == null || var3.viewRotationMatrix == null || var3.projectionMatrix == null) {
            i("camera state not extracted");
         } else if (!J(var3.fogType)) {
            i("camera in " + var3.fogType + " fog");
         } else if (v()) {
            i("shader pack active");
         } else {
            float var4 = var2.r(var1.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            if (var4 <= 0.001F) {
               i("faded underground");
            } else {
               if (s != null) {
                  mt.U("Fog", "Resumed after {}", s);
                  s = null;
               }

               if (o != m5.k) {
                  I();
                  R = (boolean)0;
                  o = m5.k;
               }

               if (!R) {
                  long var5 = kx.G();
                  String var7 = "resources";

                  try {
                     if (P == null) {
                        ea.z(L);
                        ea.z(Z);
                        P = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
                        mt.K("Fog", "Fog shader loaded; composed at the end of the main pass");
                     }

                     var7 = "parameters";
                     if (!t && !Q) {
                        Q = (boolean)1;
                        mt.T("Fog", "World projection was not captured; using the unbobbed camera projection");
                     }

                     Matrix4f var8 = new Matrix4f(t ? S : var3.projectionMatrix).mul(var3.viewRotationMatrix).invert();
                     LevelRenderState var9 = var0.levelState();
                     float var10 = var9.skyRenderState.sunAngle;
                     float var11 = -((float)Math.sin(var10));
                     float var12 = (float)Math.cos(var10);
                     float var13 = Math.clamp(var9.weatherRenderState.intensity, 0.0F, 1.0F);
                     float var14 = x(-0.08F, 0.14F, var12) * (1.0F - var13 * 0.85F);
                     float[] var15 = D(var2, var3.fogData == null ? null : var3.fogData.color);
                     float var16 = var1.level.getSeaLevel();
                     float var17 = (float)Math.sin(Math.toRadians(var2.i.P()));
                     MemoryStack var18 = MemoryStack.stackPush();

                     try {
                        ByteBuffer var19 = var18.malloc(128);
                        var8.get(var19);
                        var19.position(64);
                        var19.putFloat((float)var3.pos.y - var16)
                           .putFloat(1.0F / var2.h.P().floatValue())
                           .putFloat(RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F)
                           .putFloat(0.0F);
                        var19.putFloat(var2.A.P().floatValue()).putFloat(var2.p()).putFloat(1.0F * var4).putFloat(var17);
                        var19.putFloat(var15[0]).putFloat(var15[1]).putFloat(var15[2]).putFloat(var2.f.P().floatValue());
                        var19.putFloat(var11).putFloat(var12).putFloat(0.0F).putFloat(var14);
                        var19.flip();
                        RenderSystem.getDevice().createCommandEncoder().writeToBuffer(P.slice(), var19);
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
                     RenderTarget var39 = zo.t(var38);
                     RenderPass var20 = RenderSystem.getDevice()
                        .createCommandEncoder()
                        .createRenderPass(() -> "Larp distance fog", var39.getColorTextureView(), Optional.empty());

                     try {
                        hj.P(var20, var39 == var38 ? L : Z);
                        var20.setUniform("DistanceFog", P.slice());
                        hj.u(var20, "Depth", var38.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

                     ex.w();
                     kx.U("Distance fog passes", 1L);
                  } catch (RuntimeException var36) {
                     R = (boolean)1;

                     try {
                        I();
                     } catch (RuntimeException var31) {
                        var36.addSuppressed(var31);
                     }

                     mt.P("Fog", "DistanceFogRenderer stage=" + var7 + "; fog disabled until resource reload", var36);
                  } finally {
                     kx.u("Distance fog", var5);
                  }
               }
            }
         }
      } else {
         s = null;
      }
   }

   static boolean J(FogType var0) {
      return var0 == FogType.NONE || var0 == FogType.ATMOSPHERIC;
   }

   private static void i(String var0) {
      if (!var0.equals(s)) {
         s = var0;
         mt.U("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] D(FogModule var0, Vector4f var1) {
      if (!var0.B.S("Custom") && var1 != null) {
         Integer var3 = zo.l() ? L() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.B.S("Custom") ? var0.P.P() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer L() {
      CustomSkyModule var0 = CustomSkyModule.Y();
      if (CustomSkyModule.a()) {
         return var0.k() ? var0.P().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.U();
      return var1 != null && var1.w() ? var1.e() : null;
   }

   static float x(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean v() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !l) {
            l = (boolean)1;
            mt.K("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            l = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!l) {
            l = (boolean)1;
            mt.T("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void I() {
      if (P != null) {
         GpuBuffer var0 = P;
         P = null;
         var0.close();
      }
   }

   private u1() {
   }
}
