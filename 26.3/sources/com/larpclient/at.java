package com.larpclient;

import com.larpclient.module.Module;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterSolidFeatures;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

public final class at {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> A = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> g = new IdentityHashMap<>();
   private static final RenderPipeline P = r("wet_reflection");
   private static final RenderPipeline H = r("underwater");
   public static final int N = 1088;
   private static final at.n c = new at.n();
   private static GpuBuffer U;
   private static boolean l;
   private static boolean k;
   private static boolean O;
   private static boolean j;
   private static boolean r;
   private static boolean S;
   private static int W = -1;
   private static Object t;
   private static LevelRenderState M;
   private static double e;
   private static double p;
   private static boolean J;

   public static void Q() {
      if (!l) {
         l = (boolean)1;
         r = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         S = (boolean)(r && com.larpclient.p.T() ? 1 : 0);
         in.w();
         if (S) {
            _q.z(
               "TerrainMaterials",
               "Sodium {} detected; Wet Ground draws through Sodium's chunk shader, other terrain materials keep Sodium output",
               com.larpclient.p.S()
            );
         } else if (r) {
            _q.R("TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0 -> r(var0.levelState()));
         LevelRenderEvents.AFTER_SOLID_FEATURES.register((AfterSolidFeatures)var0 -> y());
         LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> t());
         LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((AfterTranslucentTerrain)var0 -> {
            if (Y()) {
               try {
                  v_.Z();
               } catch (RuntimeException var2) {
                  A("volumetric-surface", var2);
               }
            }

            o();
         });
         ClientTickEvents.END_CLIENT_TICK.register(v0::M);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> g());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> g());
      }
   }

   public static boolean o() {
      return k || r;
   }

   public static boolean Y() {
      return O && !o();
   }

   public static boolean r() {
      return k || r && (!S || com.larpclient.p.h());
   }

   private static boolean i() {
      return O && !r();
   }

   private static boolean a() {
      return r ? !r() && C(WetGroundModule.C()) : !k;
   }

   private static boolean C(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean l() {
      return C(WavingFoliageModule.C()) || C(WetGroundModule.C()) || C(SnowGroundModule.q()) || C(RealisticWaterModule.g());
   }

   public static void E() {
      _q.c("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   public static void U() {
      hj.u();
      Minecraft var0 = Minecraft.getInstance();
      int var1 = !C(WavingFoliageModule.C()) && !C(RealisticWaterModule.g()) && !C(SnowGroundModule.q()) ? 0 : 1;
      if (var0 != null && (var1 != 0 || J)) {
         J = (boolean)var1;
         var0.execute(() -> {
            if (var0.level != null) {
               var0.levelExtractor.allChanged();
            }
         });
      }
   }

   public static void r() {
      if (!l()) {
         T();
      }
   }

   private static void r(LevelRenderState var0) {
      M = var0;
      j = (boolean)0;
      Minecraft var1 = Minecraft.getInstance();
      if (t != var1.level) {
         T();
         t = var1.level;
         e = hz.Q();
         p = 0.0;
         hj.u();
      }

      if (W != nl.q) {
         T();
         k = (boolean)0;
         W = nl.q;
         A.clear();
         g.clear();
         com.larpclient.p.l();
         U();
      }

      if (l()
         && a()
         && var0.cameraRenderState.pos != null
         && var0.cameraRenderState.projectionMatrix != null
         && var0.cameraRenderState.viewRotationMatrix != null) {
         long var2 = he.s();

         try {
            if (U == null) {
               U = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (S) {
                  se.j(P);
                  _q.z("TerrainMaterials", "Sodium Wet Ground material and wet reflection shaders ready");
               } else {
                  B(RenderPipelines.SOLID_TERRAIN);
                  B(RenderPipelines.CUTOUT_TERRAIN);
                  B(RenderPipelines.TRANSLUCENT_TERRAIN);
                  j(RenderPipelines.SOLID_TERRAIN);
                  j(RenderPipelines.CUTOUT_TERRAIN);
                  j(RenderPipelines.TRANSLUCENT_TERRAIN);
                  se.j(P);
                  se.j(H);
                  _q.z("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            Q(var0);
            if (S) {
               com.larpclient.p.N();
               com.larpclient.p.h();
            }

            O = (boolean)1;
         } catch (RuntimeException var8) {
            A(S ? "sodium-prepare" : "prepare", var8);
         } finally {
            he.T("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline s(RenderPipeline var0) {
      com.larpclient.p.u();
      RenderPipeline var1 = in.w(var0);
      if (var1 != null) {
         return var1;
      } else {
         return S && i() && C(WetGroundModule.C()) ? com.larpclient.p.n(var0) : var0;
      }
   }

   public static RenderPipeline j(RenderPipeline var0) {
      if (r) {
         return var0;
      } else {
         RenderPipeline var1 = in.t(var0);
         if (var1 != null) {
            return var1;
         } else if (!k && l() && (var0 != RenderPipelines.TRANSLUCENT_TERRAIN || C(RealisticWaterModule.g()))) {
            return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT_TERRAIN
               ? var0
               : A.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == RenderPipelines.TRANSLUCENT_TERRAIN ? 1 : 0;
                     Builder var2 = RenderPipeline.builder(new Snippet[0])
                        .withLocation(b("pipeline/temple/" + var0x.getLocation().getPath()))
                        .withVertexShader(b("temple/terrain"))
                        .withFragmentShader(b("temple/terrain"))
                        .withCull(var0x.isCull())
                        .withPolygonMode(var0x.getPolygonMode())
                        .withPrimitiveTopology(var0x.getPrimitiveTopology())
                        .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

                     for (BindGroupLayout var4 : var0x.getBindGroupLayouts()) {
                        var2.withBindGroupLayout(var4);
                     }

                     com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder var7 = BindGroupLayout.builder()
                        .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER);
                     if (var1x != 0) {
                        var7.withUniform("SceneColor", p7.X).withUniform("SceneDepth", p7.X);
                     }

                     var2.withBindGroupLayout(var7.build());
                     List var8 = p7.i(var0x);

                     for (int var5 = 0; var5 < var8.size(); var5++) {
                        var2.withColorTargetState(var5, (ColorTargetState)var8.get(var5));
                     }

                     List var9 = p7.A(var0x);

                     for (int var6 = 0; var6 < var9.size(); var6++) {
                        var2.withVertexBinding(var6, (VertexFormat)var9.get(var6));
                     }

                     var0x.getShaderDefines().flags().forEach(var2::withShaderDefine);
                     var0x.getShaderDefines().values().forEach((var1xx, var2x) -> var2.withShaderDefine(var1xx, Float.parseFloat(var2x)));
                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var10 = var2.build();
                     se.j(var10);
                     return var10;
                  }
               );
         } else {
            return g.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline B(RenderPipeline var0) {
      return g.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(b("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(b("temple/terrain_decode"))
               .withFragmentShader(p7.a(var0x))
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

            for (BindGroupLayout var3 : var0x.getBindGroupLayouts()) {
               var1.withBindGroupLayout(var3);
            }

            List var5 = p7.i(var0x);

            for (int var6 = 0; var6 < var5.size(); var6++) {
               var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
            }

            List var7 = p7.A(var0x);

            for (int var4 = 0; var4 < var7.size(); var4++) {
               var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
            }

            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
            RenderPipeline var8 = var1.build();
            se.j(var8);
            return var8;
         }
      );
   }

   public static void A(RenderPass var0, RenderPipeline var1) {
      if (!in.E(var0, var1)) {
         if (U != null && !k && (A.containsValue(var1) || com.larpclient.p.s(var1))) {
            var0.setUniform("LarpMaterial", U.slice());
            if (var1.getShaderDefines().flags().contains("LARP_WATER") && c.i != null) {
               p7.t(var0, "SceneColor", c.i, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               p7.t(var0, "SceneDepth", c.K, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            }
         }
      }
   }

   private static void Q(LevelRenderState var0) {
      WavingFoliageModule var1 = WavingFoliageModule.C();
      RealisticWaterModule var2 = RealisticWaterModule.g();
      WetGroundModule var3 = WetGroundModule.C();
      SnowGroundModule var4 = SnowGroundModule.q();
      boolean var5 = C(var4);
      boolean var6 = C(var1);
      boolean var7 = C(var2);
      boolean var8 = C(var3);
      double var9 = hz.Q();
      p = p + Math.clamp(var9 - e, 0.0, 0.1) * (var6 ? var1.i.getValue() : 1.0);
      e = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.p.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.h.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.q.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.C.getValue()) {
         var12 = Math.min(1.0F, var12 + var11 * 0.35F);
         var13 = Math.min(1.0F, var13 + var11 * 0.25F);
         var14 = Math.min(1.0F, var14 + var11 * 0.25F);
      }

      CameraRenderState var15 = var0.cameraRenderState;
      Matrix4f var16 = new Matrix4f(var15.projectionMatrix).mul(var15.viewRotationMatrix);
      Matrix4f var17 = new Matrix4f(var16).invert();
      float var18 = var0.skyRenderState.sunAngle;
      float var19 = -((float)Math.sin(var18));
      float var20 = (float)Math.cos(var18);
      float var21 = Math.clamp(var20 * 1.8F + 0.15F, 0.0F, 1.0F) * (1.0F - var11 * 0.9F);
      Vector3fc var22 = var0.skyRenderState.skyColor;
      int var23 = var22 == null ? 0 : ARGB.colorFromFloat(1.0F, var22.x(), var22.y(), var22.z());
      MemoryStack var24 = MemoryStack.stackPush();

      try {
         ByteBuffer var25 = var24.malloc(1088);
         o(var25, (float)p, var6 ? var1.u.getValue().floatValue() : 0.0F, var6 ? var1.L.getValue().floatValue() / 100.0F : 0.0F, (float)var9);
         o(
            var25,
            var7 ? var2.I.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.N.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.S.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         o(
            var25,
            var7 && var2.a.getValue() ? 1.0F : 0.0F,
            var7 && var2.e.getValue() ? 1.0F : 0.0F,
            var7 && var2.W.getValue() ? 1.0F : 0.0F,
            RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F
         );
         o(var25, var12, var13, var8 && var3.y.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.H.w("Flooded") ? 1.0F : 0.0F);
         o(var25, var8 ? 1.0F : 0.0F, var8 && var3.B.getValue() ? 1.0F : 0.0F, j ? 1.0F : 0.0F, var15.fogType == FogType.WATER ? 1.0F : 0.0F);
         o(var25, var19, var20, 0.0F, var21);
         o(var25, (var23 >> 16 & 0xFF) / 255.0F, (var23 >> 8 & 0xFF) / 255.0F, (var23 & 0xFF) / 255.0F, var11);
         o(var25, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         o(
            var25,
            var5 ? var4.p.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.L.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.q.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.q() : 0.0F
         );
         var16.get(var25);
         var25.position(var25.position() + 64);
         var17.get(var25);
         var25.position(var25.position() + 64);
         if (var4 != null) {
            var4.Y(var25, var15.pos, var9, var5);
         } else {
            for (int var26 = 0; var26 < 100; var26++) {
               var25.putLong(0L);
            }
         }

         o(var25, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var25.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(U.slice(), var25);
      } catch (Throwable var28) {
         if (var24 != null) {
            try {
               var24.close();
            } catch (Throwable var27) {
               var28.addSuppressed(var27);
            }
         }

         throw var28;
      }

      if (var24 != null) {
         var24.close();
      }
   }

   private static void t() {
      if (Y() && C(RealisticWaterModule.g())) {
         try {
            c.w(Minecraft.getInstance().gameRenderer.mainRenderTarget());
            j = (boolean)1;
            Q(M);
         } catch (RuntimeException var1) {
            A("capture-water-scene", var1);
         }
      }
   }

   private static void y() {
      WetGroundModule var0 = WetGroundModule.C();
      if (i() && C(var0) && var0.B.getValue()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            c.w(var1);
            j = (boolean)1;
            Q(M);
            X(P, var1);
         } catch (RuntimeException var2) {
            A("wet-reflection", var2);
         }
      }
   }

   private static void o() {
      RealisticWaterModule var0 = RealisticWaterModule.g();
      if (Y() && C(var0) && var0.W.getValue() && M.cameraRenderState.fogType == FogType.WATER) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            c.w(var1);
            j = (boolean)1;
            Q(M);
            X(H, var1);
         } catch (RuntimeException var2) {
            A("underwater", var2);
         }
      }
   }

   private static void X(RenderPipeline var0, RenderTarget var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorTextureView(), Optional.empty());

      try {
         p7.K(var3, var0);
         var3.setUniform("LarpMaterial", U.slice());
         p7.t(var3, "SceneColor", c.i, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         p7.t(var3, "SceneDepth", c.K, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         var3.draw(3, 1, 0, 0);
      } catch (Throwable var7) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (var3 != null) {
         var3.close();
      }
   }

   static GpuBuffer T() {
      return U;
   }

   static GpuTextureView H() {
      return c.i;
   }

   static GpuTextureView A() {
      return c.K;
   }

   static LevelRenderState J() {
      return M;
   }

   static void M() {
      c.w(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      j = (boolean)1;
      Q(M);
   }

   private static RenderPipeline r(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(b("pipeline/temple/" + var0))
         .withVertexShader(b("post/fullscreen"))
         .withFragmentShader(b("temple/" + var0))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
               .withUniform("SceneColor", p7.X)
               .withUniform("SceneDepth", p7.X)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withCull(false)
         .build();
   }

   private static void o(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier b(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static void A(String var0, RuntimeException var1) {
      k = (boolean)1;
      T();
      U();
      _q.o("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void T() {
      j = (boolean)0;
      O = (boolean)0;
      if (U != null) {
         U.close();
         U = null;
      }

      c.G();
      v_.U();
   }

   public static void g() {
      v0.Q();
      T();
      M = null;
      t = null;
      p = 0.0;
      e = hz.Q();
   }

   private at() {
   }

   private static final class n {
      private GpuTexture L;
      private GpuTexture b;
      private GpuTextureView i;
      private GpuTextureView K;

      private void w(RenderTarget var1) {
         if (var1.getDepthTexture() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.L == null
            || this.L.getWidth(0) != var1.width
            || this.L.getHeight(0) != var1.height
            || this.L.getFormat() != var1.getColorTexture().getFormat()
            || this.b.getFormat() != var1.getDepthTexture().getFormat()) {
            this.G();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.L = var2.createTexture("Larp material scene", var3, var1.getColorTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.b = var2.createTexture("Larp material depth", var3, var1.getDepthTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.i = var2.createTextureView(this.L);
            this.K = var2.createTextureView(this.b);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.getColorTexture(), this.L, 0, 0, 0, 0, 0, var1.width, var1.height);
         var4.copyTextureToTexture(var1.getDepthTexture(), this.b, 0, 0, 0, 0, 0, var1.width, var1.height);
      }

      private void G() {
         if (this.i != null) {
            this.i.close();
            this.i = null;
         }

         if (this.K != null) {
            this.K.close();
            this.K = null;
         }

         if (this.L != null) {
            this.L.close();
            this.L = null;
         }

         if (this.b != null) {
            this.b.close();
            this.b = null;
         }
      }
   }
}
