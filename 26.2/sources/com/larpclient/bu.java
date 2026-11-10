package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
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

public final class bu {
   private static final Identifier n = Identifier.fromNamespaceAndPath("larp-client", "distance_fog");
   private static final RenderPipeline R = b("distance_fog", 7);
   private static final RenderPipeline H = b("distance_fog_layer", 15);
   static final int l = 128;
   private static final Matrix4f g = new Matrix4f();
   private static boolean a;
   private static boolean i;
   private static boolean I;
   private static boolean C;
   private static boolean W;
   private static final GpuBuffer[] N = new GpuBuffer[2];
   private static GpuTexture T;
   private static GpuTextureView z;
   private static boolean b;
   private static int t = -1;
   private static String e;
   private static String Q;

   private static RenderPipeline b(String var0, int var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/distance_fog"))
         .withBindGroupLayout(
            BindGroupLayout.builder().withSampler("Depth").withSampler("OpaqueDepth").withUniform("DistanceFog", UniformType.UNIFORM_BUFFER).build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.DEFAULT.format(), var1))
         .withCull(false)
         .build();
   }

   public static void r() {
      LevelRenderEvents.START_MAIN.register((StartMain)var0 -> b = (boolean)0);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.addPhaseOrdering(n, Event.DEFAULT_PHASE);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(n, bu::J);
      LevelRenderEvents.END_MAIN.addPhaseOrdering(n, Event.DEFAULT_PHASE);
      LevelRenderEvents.END_MAIN.register(n, bu::w);
   }

   public static void I(Matrix4f var0) {
      g.set(var0);
      a = (boolean)1;
   }

   public static boolean H(Matrix4f var0) {
      if (a) {
         var0.set(g);
      }

      return a;
   }

   private static void J(LevelRenderContext var0) {
      b = (boolean)0;
      if (!W && !p2.c()) {
         b = T(var0, bu.u.k);
      }
   }

   private static void w(LevelRenderContext var0) {
      bu.u var1 = b && z != null ? bu.u.t : bu.u.O;
      b = (boolean)0;
      if (T(var0, var1)) {
         String var2 = var1 == bu.u.t
            ? "before translucent terrain, then translucent surfaces by their own depth"
            : "once from the final depth ("
               + (p2.c() ? "Stream Proof Hide Everything" : (W ? "opaque depth copy failed" : "no draw before translucent terrain"))
               + ")";
         if (!var2.equals(Q)) {
            Q = var2;
            h.A("Fog", "Composed {}", var2);
         }
      }
   }

   private static boolean T(LevelRenderContext var0, bu.u var1) {
      Minecraft var2 = Minecraft.getInstance();
      FogModule var3 = FogModule.P();
      if (var2.level != null && var3 != null && var3.c()) {
         CameraRenderState var4 = var0.levelState().cameraRenderState;
         if (var4 == null || var4.pos == null || var4.viewRotationMatrix == null || var4.projectionMatrix == null) {
            Z("camera state not extracted");
            return false;
         }

         if (!R(var4.fogType)) {
            Z("camera in " + var4.fogType + " fog");
            return false;
         }

         if (s()) {
            Z("shader pack active");
            return false;
         }

         float var5 = var3.J(var2.getDeltaTracker().getGameTimeDeltaPartialTick(false));
         if (var5 <= 0.001F) {
            Z("faded underground");
            return false;
         }

         if (e != null) {
            h.U("Fog", "Resumed after {}", e);
            e = null;
         }

         if (t != wa.s) {
            D();
            W = (boolean)0;
            i = (boolean)0;
            t = wa.s;
         }

         if (i) {
            return false;
         }

         long var6 = zj.m();
         String var8 = "resources";

         try {
            if (N[0] == null) {
               ai.U(R);
               ai.U(H);
               h.A("Fog", "Fog shader loaded; composed before translucent terrain and over translucent surfaces");
            }

            GpuBuffer var9 = N[var1.y];
            if (var9 == null) {
               var9 = N[var1.y] = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
            }

            RenderTarget var10 = var2.gameRenderer.mainRenderTarget();
            if (var1 == bu.u.k) {
               var8 = "opaque depth copy";
               if (!M(var10)) {
                  return false;
               }
            }

            var8 = "parameters";
            if (!a && !C) {
               C = (boolean)1;
               h.g("Fog", "World projection was not captured; using the unbobbed camera projection");
            }

            Matrix4f var11 = new Matrix4f(a ? g : var4.projectionMatrix).mul(var4.viewRotationMatrix).invert();
            LevelRenderState var12 = var0.levelState();
            float var13 = var12.skyRenderState.sunAngle;
            float var14 = -((float)Math.sin(var13));
            float var15 = (float)Math.cos(var13);
            float var16 = Math.clamp(var12.weatherRenderState.intensity, 0.0F, 1.0F);
            float var17 = M(-0.08F, 0.14F, var15) * (1.0F - var16 * 0.85F);
            float[] var18 = e(var3, var4.fogData == null ? null : var4.fogData.color);
            float var19 = var2.level.getSeaLevel();
            float var20 = (float)Math.sin(Math.toRadians(var3.J.getValue()));
            MemoryStack var21 = MemoryStack.stackPush();

            try {
               ByteBuffer var22 = var21.malloc(128);
               var11.get(var22);
               var22.position(64);
               var22.putFloat((float)var4.pos.y - var19)
                  .putFloat(1.0F / var3.H.getValue().floatValue())
                  .putFloat(RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F)
                  .putFloat(var1.S);
               var22.putFloat(var3.V.getValue().floatValue()).putFloat(var3.o()).putFloat(1.0F * var5).putFloat(var20);
               var22.putFloat(var18[0]).putFloat(var18[1]).putFloat(var18[2]).putFloat(var3.p.getValue().floatValue());
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

            var8 = "compose " + var1.E;
            RenderTarget var40 = p2.Q(var10);
            RenderPass var41 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp distance fog", var40.getColorTextureView(), Optional.empty());

            try {
               mx.i(var41, var40 == var10 ? R : H);
               var41.setUniform("DistanceFog", var9.slice());
               GpuSampler var23 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               mx.x(var41, "Depth", var10.getDepthTextureView(), var23);
               mx.x(var41, "OpaqueDepth", var1 == bu.u.t ? z : var10.getDepthTextureView(), var23);
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

            so.K();
            zj.K("Distance fog passes", 1L);
            return true;
         } catch (RuntimeException var38) {
            i = (boolean)1;

            try {
               D();
            } catch (RuntimeException var33) {
               var38.addSuppressed(var33);
            }

            h.m("Fog", "DistanceFogRenderer stage=" + var8 + "; fog disabled until resource reload", var38);
            return false;
         } finally {
            zj.l("Distance fog", var6);
         }
      } else {
         e = null;
         return false;
      }
   }

   private static boolean M(RenderTarget var0) {
      try {
         GpuTexture var1 = var0.getDepthTexture();
         if (var1 == null) {
            return false;
         }

         if (T == null || T.getWidth(0) != var1.getWidth(0) || T.getHeight(0) != var1.getHeight(0) || T.getFormat() != var1.getFormat()) {
            m();
            T = RenderSystem.getDevice()
               .createTexture("Larp fog depth before translucent terrain", 13, var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
            z = RenderSystem.getDevice().createTextureView(T);
            h.U("Fog", "Opaque depth copy allocated {}x{}", var1.getWidth(0), var1.getHeight(0));
         }

         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var1, T, 0, 0, 0, 0, 0, var1.getWidth(0), var1.getHeight(0));
         return true;
      } catch (RuntimeException var4) {
         W = (boolean)1;

         try {
            m();
         } catch (RuntimeException var3) {
            var4.addSuppressed(var3);
         }

         h.m("Fog", "Depth copy before translucent terrain failed; fog is composed once from the final depth until resource reload", var4);
         return false;
      }
   }

   private static void m() {
      GpuTextureView var0 = z;
      GpuTexture var1 = T;
      z = null;
      T = null;

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

   static boolean R(FogType var0) {
      return var0 == FogType.NONE || var0 == FogType.ATMOSPHERIC;
   }

   private static void Z(String var0) {
      if (!var0.equals(e)) {
         e = var0;
         h.U("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] e(FogModule var0, Vector4f var1) {
      if (!var0.r.O("Custom") && var1 != null) {
         Integer var3 = p2.p() ? M() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.r.O("Custom") ? var0.g.getValue() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer M() {
      CustomSkyModule var0 = CustomSkyModule.K();
      if (CustomSkyModule.i()) {
         return var0.p() ? var0.m().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.t();
      return var1 != null && var1.O() ? var1.C() : null;
   }

   static float M(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean s() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !I) {
            I = (boolean)1;
            h.A("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            I = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!I) {
            I = (boolean)1;
            h.g("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void D() {
      b = (boolean)0;

      try {
         for (int var0 = 0; var0 < N.length; var0++) {
            GpuBuffer var1 = N[var0];
            N[var0] = null;
            if (var1 != null) {
               var1.close();
            }
         }
      } finally {
         m();
      }
   }

   private bu() {
   }

   enum u {
      O(0, 0, "end of the main pass"),
      k(0, 0, "before translucent terrain"),
      t(1, 1, "translucent surfaces");

      final int S;
      final int y;
      final String E;

      u(int var3, int var4, String var5) {
         this.S = var3;
         this.y = var4;
         this.E = var5;
      }
   }
}
