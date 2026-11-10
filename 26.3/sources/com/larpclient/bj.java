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
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
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

public final class bj {
   private static final Identifier l = Identifier.fromNamespaceAndPath("larp-client", "distance_fog");
   private static final RenderPipeline p = h("distance_fog", 7);
   private static final RenderPipeline C = h("distance_fog_layer", 15);
   static final int D = 128;
   private static final Matrix4f B = new Matrix4f();
   private static boolean r;
   private static boolean d;
   private static boolean P;
   private static boolean v;
   private static boolean X;
   private static final GpuBuffer[] g = new GpuBuffer[2];
   private static GpuTexture F;
   private static GpuTextureView x;
   private static boolean A;
   private static int G = -1;
   private static String e;
   private static String V;

   private static RenderPipeline h(String var0, int var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/distance_fog"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Depth", p7.X)
               .withUniform("OpaqueDepth", p7.X)
               .withUniform("DistanceFog", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.DEFAULT.format(), var1))
         .withCull(false)
         .build();
   }

   public static void K() {
      LevelRenderEvents.START_MAIN.register((StartMain)var0 -> A = (boolean)0);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.addPhaseOrdering(l, Event.DEFAULT_PHASE);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(l, bj::U);
      LevelRenderEvents.END_MAIN.addPhaseOrdering(l, Event.DEFAULT_PHASE);
      LevelRenderEvents.END_MAIN.register(l, bj::e);
   }

   public static void v(Matrix4f var0) {
      B.set(var0);
      r = (boolean)1;
   }

   public static boolean f(Matrix4f var0) {
      if (r) {
         var0.set(B);
      }

      return r;
   }

   private static void U(LevelRenderContext var0) {
      A = (boolean)0;
      if (!X && !qc.w()) {
         A = m(var0, bj.r.b);
      }
   }

   private static void e(LevelRenderContext var0) {
      bj.r var1 = A && x != null ? bj.r.i : bj.r.S;
      A = (boolean)0;
      if (m(var0, var1)) {
         String var2 = var1 == bj.r.i
            ? "before translucent terrain, then translucent surfaces by their own depth"
            : "once from the final depth ("
               + (qc.w() ? "Stream Proof Hide Everything" : (X ? "opaque depth copy failed" : "no draw before translucent terrain"))
               + ")";
         if (!var2.equals(V)) {
            V = var2;
            _q.z("Fog", "Composed {}", var2);
         }
      }
   }

   private static boolean m(LevelRenderContext var0, bj.r var1) {
      Minecraft var2 = Minecraft.getInstance();
      FogModule var3 = FogModule.z();
      if (var2.level != null && var3 != null && var3.R()) {
         CameraRenderState var4 = var0.levelState().cameraRenderState;
         if (var4 == null || var4.pos == null || var4.viewRotationMatrix == null || var4.projectionMatrix == null) {
            m("camera state not extracted");
            return false;
         }

         if (!H(var4.fogType)) {
            m("camera in " + var4.fogType + " fog");
            return false;
         }

         if (m()) {
            m("shader pack active");
            return false;
         }

         float var5 = var3.T(var2.getDeltaTracker().getGameTimeDeltaPartialTick(false));
         if (var5 <= 0.001F) {
            m("faded underground");
            return false;
         }

         if (e != null) {
            _q.c("Fog", "Resumed after {}", e);
            e = null;
         }

         if (G != nl.q) {
            t();
            X = (boolean)0;
            d = (boolean)0;
            G = nl.q;
         }

         if (d) {
            return false;
         }

         long var6 = he.s();
         String var8 = "resources";

         try {
            if (g[0] == null) {
               se.j(p);
               se.j(C);
               _q.z("Fog", "Fog shader loaded; composed before translucent terrain and over translucent surfaces");
            }

            GpuBuffer var9 = g[var1.Z];
            if (var9 == null) {
               var9 = g[var1.Z] = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
            }

            RenderTarget var10 = var2.gameRenderer.mainRenderTarget();
            if (var1 == bj.r.b) {
               var8 = "opaque depth copy";
               if (!q(var10)) {
                  return false;
               }
            }

            var8 = "parameters";
            if (!r && !v) {
               v = (boolean)1;
               _q.R("Fog", "World projection was not captured; using the unbobbed camera projection");
            }

            Matrix4f var11 = new Matrix4f(r ? B : var4.projectionMatrix).mul(var4.viewRotationMatrix).invert();
            LevelRenderState var12 = var0.levelState();
            float var13 = var12.skyRenderState.sunAngle;
            float var14 = -((float)Math.sin(var13));
            float var15 = (float)Math.cos(var13);
            float var16 = Math.clamp(var12.weatherRenderState.intensity, 0.0F, 1.0F);
            float var17 = u(-0.08F, 0.14F, var15) * (1.0F - var16 * 0.85F);
            float[] var18 = m(var3, var4.fogData == null ? null : var4.fogData.color);
            float var19 = var2.level.getSeaLevel();
            float var20 = (float)Math.sin(Math.toRadians(var3.y.getValue()));
            MemoryStack var21 = MemoryStack.stackPush();

            try {
               ByteBuffer var22 = var21.malloc(128);
               var11.get(var22);
               var22.position(64);
               var22.putFloat((float)var4.pos.y - var19)
                  .putFloat(1.0F / var3.l.getValue().floatValue())
                  .putFloat(RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F)
                  .putFloat(var1.F);
               var22.putFloat(var3.N.getValue().floatValue()).putFloat(var3.n()).putFloat(1.0F * var5).putFloat(var20);
               var22.putFloat(var18[0]).putFloat(var18[1]).putFloat(var18[2]).putFloat(var3.g.getValue().floatValue());
               var22.putFloat(var14).putFloat(var15).putFloat(0.0F).putFloat(var17);
               var22.flip();
               RenderSystem.getDevice().createCommandEncoder().writeToBuffer(var9.slice(), var22);
            } catch (Throwable var37) {
               if (var21 != null) {
                  try {
                     var21.close();
                  } catch (Throwable var35) {
                     var37.addSuppressed(var35);
                  }
               }

               throw var37;
            }

            if (var21 != null) {
               var21.close();
            }

            var8 = "compose " + var1.c;
            RenderTarget var40 = qc.Y(var10);
            RenderPass var41 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp distance fog", var40.getColorTextureView(), Optional.empty());

            try {
               p7.K(var41, var40 == var10 ? p : C);
               var41.setUniform("DistanceFog", var9.slice());
               GpuSampler var23 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               p7.t(var41, "Depth", var10.getDepthTextureView(), var23);
               p7.t(var41, "OpaqueDepth", var1 == bj.r.i ? x : var10.getDepthTextureView(), var23);
               var41.draw(3, 1, 0, 0);
            } catch (Throwable var36) {
               if (var41 != null) {
                  try {
                     var41.close();
                  } catch (Throwable var34) {
                     var36.addSuppressed(var34);
                  }
               }

               throw var36;
            }

            if (var41 != null) {
               var41.close();
            }

            _s.g();
            he.Z("Distance fog passes", 1L);
            return true;
         } catch (RuntimeException var38) {
            d = (boolean)1;

            try {
               t();
            } catch (RuntimeException var33) {
               var38.addSuppressed(var33);
            }

            _q.o("Fog", "DistanceFogRenderer stage=" + var8 + "; fog disabled until resource reload", var38);
            return false;
         } finally {
            he.T("Distance fog", var6);
         }
      } else {
         e = null;
         return false;
      }
   }

   private static boolean q(RenderTarget var0) {
      try {
         GpuTexture var1 = var0.getDepthTexture();
         if (var1 == null) {
            return false;
         }

         if (F == null || F.getWidth(0) != var1.getWidth(0) || F.getHeight(0) != var1.getHeight(0) || F.getFormat() != var1.getFormat()) {
            q();
            F = RenderSystem.getDevice()
               .createTexture("Larp fog depth before translucent terrain", 13, var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
            x = RenderSystem.getDevice().createTextureView(F);
            _q.c("Fog", "Opaque depth copy allocated {}x{}", var1.getWidth(0), var1.getHeight(0));
         }

         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var1, F, 0, 0, 0, 0, 0, var1.getWidth(0), var1.getHeight(0));
         return true;
      } catch (RuntimeException var4) {
         X = (boolean)1;

         try {
            q();
         } catch (RuntimeException var3) {
            var4.addSuppressed(var3);
         }

         _q.o("Fog", "Depth copy before translucent terrain failed; fog is composed once from the final depth until resource reload", var4);
         return false;
      }
   }

   private static void q() {
      GpuTextureView var0 = x;
      GpuTexture var1 = F;
      x = null;
      F = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }
   }

   static boolean H(FogType var0) {
      return var0 == FogType.NONE || var0 == FogType.ATMOSPHERIC;
   }

   private static void m(String var0) {
      if (!var0.equals(e)) {
         e = var0;
         _q.c("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] m(FogModule var0, Vector4f var1) {
      if (!var0.d.w("Custom") && var1 != null) {
         Integer var3 = qc.t() ? T() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.d.w("Custom") ? var0.m.getValue() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer T() {
      CustomSkyModule var0 = CustomSkyModule.p();
      if (CustomSkyModule.C()) {
         return var0.c() ? var0.X().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.R();
      return var1 != null && var1.X() ? var1.g() : null;
   }

   static float u(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean m() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !P) {
            P = (boolean)1;
            _q.z("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            P = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!P) {
            P = (boolean)1;
            _q.R("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void t() {
      A = (boolean)0;

      try {
         for (int var0 = 0; var0 < g.length; var0++) {
            GpuBuffer var1 = g[var0];
            g[var0] = null;
            if (var1 != null) {
               var1.close();
            }
         }
      } finally {
         q();
      }
   }

   private bj() {
   }

   enum r {
      S(0, 0, "end of the main pass"),
      b(0, 0, "before translucent terrain"),
      i(1, 1, "translucent surfaces");

      final int F;
      final int Z;
      final String c;

      r(int var3, int var4, String var5) {
         this.F = var3;
         this.Z = var4;
         this.c = var5;
      }
   }
}
