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

public final class k {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> X = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> p = new IdentityHashMap<>();
   private static final RenderPipeline V = P("wet_reflection");
   private static final RenderPipeline j = P("underwater");
   public static final int d = 1088;
   private static final k.g I = new k.g();
   private static GpuBuffer Z;
   private static boolean P;
   private static boolean M;
   private static boolean z;
   private static boolean t;
   private static boolean G;
   private static boolean S;
   private static int K = -1;
   private static Object L;
   private static LevelRenderState U;
   private static double f;
   private static double B;
   private static boolean N;

   public static void i() {
      if (!P) {
         P = (boolean)1;
         G = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         S = (boolean)(G && w.t() ? 1 : 0);
         of.I();
         if (S) {
            mt.K("TerrainMaterials", "Sodium {} detected; Wet Ground draws through Sodium's chunk shader, other terrain materials keep Sodium output", w.q());
         } else if (G) {
            mt.T("TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0 -> k(var0.levelState()));
         LevelRenderEvents.AFTER_SOLID_FEATURES.register((AfterSolidFeatures)var0 -> X());
         LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> c());
         LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((AfterTranslucentTerrain)var0 -> {
            if (h()) {
               try {
                  ua.X();
               } catch (RuntimeException var2) {
                  T("volumetric-surface", var2);
               }
            }

            V();
         });
         ClientTickEvents.END_CLIENT_TICK.register(hr::t);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> H());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> H());
      }
   }

   public static boolean K() {
      return M || G;
   }

   public static boolean h() {
      return z && !K();
   }

   public static boolean g() {
      return M || G && (!S || w.p());
   }

   private static boolean w() {
      return z && !g();
   }

   private static boolean L() {
      return G ? !g() && a(WetGroundModule.n()) : !M;
   }

   private static boolean a(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean d() {
      return a(WavingFoliageModule.O()) || a(WetGroundModule.n()) || a(SnowGroundModule.d()) || a(RealisticWaterModule.G());
   }

   public static void g() {
      mt.U("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   public static void k() {
      em.C();
      Minecraft var0 = Minecraft.getInstance();
      int var1 = !a(WavingFoliageModule.O()) && !a(RealisticWaterModule.G()) && !a(SnowGroundModule.d()) ? 0 : 1;
      if (var0 != null && (var1 != 0 || N)) {
         N = (boolean)var1;
         var0.execute(() -> {
            if (var0.level != null) {
               var0.levelExtractor.allChanged();
            }
         });
      }
   }

   public static void w() {
      if (!d()) {
         p();
      }
   }

   private static void k(LevelRenderState var0) {
      U = var0;
      t = (boolean)0;
      Minecraft var1 = Minecraft.getInstance();
      if (L != var1.level) {
         p();
         L = var1.level;
         f = ty.K();
         B = 0.0;
         em.C();
      }

      if (K != m5.k) {
         p();
         M = (boolean)0;
         K = m5.k;
         X.clear();
         p.clear();
         w.E();
         k();
      }

      if (d()
         && L()
         && var0.cameraRenderState.pos != null
         && var0.cameraRenderState.projectionMatrix != null
         && var0.cameraRenderState.viewRotationMatrix != null) {
         long var2 = kx.G();

         try {
            if (Z == null) {
               Z = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (S) {
                  ea.z(V);
                  mt.K("TerrainMaterials", "Sodium Wet Ground material and wet reflection shaders ready");
               } else {
                  U(RenderPipelines.SOLID_TERRAIN);
                  U(RenderPipelines.CUTOUT_TERRAIN);
                  U(RenderPipelines.TRANSLUCENT_TERRAIN);
                  l(RenderPipelines.SOLID_TERRAIN);
                  l(RenderPipelines.CUTOUT_TERRAIN);
                  l(RenderPipelines.TRANSLUCENT_TERRAIN);
                  ea.z(V);
                  ea.z(j);
                  mt.K("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            j(var0);
            if (S) {
               w.x();
               w.c();
            }

            z = (boolean)1;
         } catch (RuntimeException var8) {
            T(S ? "sodium-prepare" : "prepare", var8);
         } finally {
            kx.u("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline c(RenderPipeline var0) {
      w.w();
      RenderPipeline var1 = of.N(var0);
      if (var1 != null) {
         return var1;
      } else {
         return S && w() && a(WetGroundModule.n()) ? w.C(var0) : var0;
      }
   }

   public static RenderPipeline l(RenderPipeline var0) {
      if (G) {
         return var0;
      } else {
         RenderPipeline var1 = of.y(var0);
         if (var1 != null) {
            return var1;
         } else if (!M && d() && (var0 != RenderPipelines.TRANSLUCENT_TERRAIN || a(RealisticWaterModule.G()))) {
            return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT_TERRAIN
               ? var0
               : X.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == RenderPipelines.TRANSLUCENT_TERRAIN ? 1 : 0;
                     Builder var2 = RenderPipeline.builder(new Snippet[0])
                        .withLocation(y("pipeline/temple/" + var0x.getLocation().getPath()))
                        .withVertexShader(y("temple/terrain"))
                        .withFragmentShader(y("temple/terrain"))
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
                        var7.withUniform("SceneColor", hj.G).withUniform("SceneDepth", hj.G);
                     }

                     var2.withBindGroupLayout(var7.build());
                     List var8 = hj.n(var0x);

                     for (int var5 = 0; var5 < var8.size(); var5++) {
                        var2.withColorTargetState(var5, (ColorTargetState)var8.get(var5));
                     }

                     List var9 = hj.P(var0x);

                     for (int var6 = 0; var6 < var9.size(); var6++) {
                        var2.withVertexBinding(var6, (VertexFormat)var9.get(var6));
                     }

                     var0x.getShaderDefines().flags().forEach(var2::withShaderDefine);
                     var0x.getShaderDefines().values().forEach((var1xx, var2x) -> var2.withShaderDefine(var1xx, Float.parseFloat(var2x)));
                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var10 = var2.build();
                     ea.z(var10);
                     return var10;
                  }
               );
         } else {
            return p.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline U(RenderPipeline var0) {
      return p.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(y("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(y("temple/terrain_decode"))
               .withFragmentShader(hj.q(var0x))
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

            for (BindGroupLayout var3 : var0x.getBindGroupLayouts()) {
               var1.withBindGroupLayout(var3);
            }

            List var5 = hj.n(var0x);

            for (int var6 = 0; var6 < var5.size(); var6++) {
               var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
            }

            List var7 = hj.P(var0x);

            for (int var4 = 0; var4 < var7.size(); var4++) {
               var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
            }

            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
            RenderPipeline var8 = var1.build();
            ea.z(var8);
            return var8;
         }
      );
   }

   public static void v(RenderPass var0, RenderPipeline var1) {
      if (!of.B(var0, var1)) {
         if (Z != null && !M && (X.containsValue(var1) || w.R(var1))) {
            var0.setUniform("LarpMaterial", Z.slice());
            if (var1.getShaderDefines().flags().contains("LARP_WATER") && I.j != null) {
               hj.u(var0, "SceneColor", I.j, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               hj.u(var0, "SceneDepth", I.A, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            }
         }
      }
   }

   private static void j(LevelRenderState var0) {
      WavingFoliageModule var1 = WavingFoliageModule.O();
      RealisticWaterModule var2 = RealisticWaterModule.G();
      WetGroundModule var3 = WetGroundModule.n();
      SnowGroundModule var4 = SnowGroundModule.d();
      boolean var5 = a(var4);
      boolean var6 = a(var1);
      boolean var7 = a(var2);
      boolean var8 = a(var3);
      double var9 = ty.K();
      B = B + Math.clamp(var9 - f, 0.0, 0.1) * (var6 ? var1.G.P() : 1.0);
      f = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.G.P().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.g.P().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.L.P().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.q.P()) {
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
         L(var25, (float)B, var6 ? var1.O.P().floatValue() : 0.0F, var6 ? var1.t.P().floatValue() / 100.0F : 0.0F, (float)var9);
         L(
            var25,
            var7 ? var2.J.P().floatValue() / 100.0F : 0.0F,
            var7 ? var2.F.P().floatValue() / 100.0F : 0.0F,
            var7 && var2.D.P() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         L(
            var25,
            var7 && var2.E.P() ? 1.0F : 0.0F,
            var7 && var2.H.P() ? 1.0F : 0.0F,
            var7 && var2.X.P() ? 1.0F : 0.0F,
            RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F
         );
         L(var25, var12, var13, var8 && var3.O.P() ? 1.0F + var11 : 0.0F, var8 && var3.S.S("Flooded") ? 1.0F : 0.0F);
         L(var25, var8 ? 1.0F : 0.0F, var8 && var3.M.P() ? 1.0F : 0.0F, t ? 1.0F : 0.0F, var15.fogType == FogType.WATER ? 1.0F : 0.0F);
         L(var25, var19, var20, 0.0F, var21);
         L(var25, (var23 >> 16 & 0xFF) / 255.0F, (var23 >> 8 & 0xFF) / 255.0F, (var23 & 0xFF) / 255.0F, var11);
         L(var25, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         L(
            var25,
            var5 ? var4.J.P().floatValue() / 100.0F : 0.0F,
            var5 ? var4.W.P().floatValue() / 100.0F : 0.0F,
            var5 ? var4.H.P().floatValue() / 100.0F : 0.0F,
            var5 ? var4.m() : 0.0F
         );
         var16.get(var25);
         var25.position(var25.position() + 64);
         var17.get(var25);
         var25.position(var25.position() + 64);
         if (var4 != null) {
            var4.u(var25, var15.pos, var9, var5);
         } else {
            for (int var26 = 0; var26 < 100; var26++) {
               var25.putLong(0L);
            }
         }

         L(var25, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var25.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(Z.slice(), var25);
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

   private static void c() {
      if (h() && a(RealisticWaterModule.G())) {
         try {
            I.M(Minecraft.getInstance().gameRenderer.mainRenderTarget());
            t = (boolean)1;
            j(U);
         } catch (RuntimeException var1) {
            T("capture-water-scene", var1);
         }
      }
   }

   private static void X() {
      WetGroundModule var0 = WetGroundModule.n();
      if (w() && a(var0) && var0.M.P()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            I.M(var1);
            t = (boolean)1;
            j(U);
            d(V, var1);
         } catch (RuntimeException var2) {
            T("wet-reflection", var2);
         }
      }
   }

   private static void V() {
      RealisticWaterModule var0 = RealisticWaterModule.G();
      if (h() && a(var0) && var0.X.P() && U.cameraRenderState.fogType == FogType.WATER) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            I.M(var1);
            t = (boolean)1;
            j(U);
            d(j, var1);
         } catch (RuntimeException var2) {
            T("underwater", var2);
         }
      }
   }

   private static void d(RenderPipeline var0, RenderTarget var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorTextureView(), Optional.empty());

      try {
         hj.P(var3, var0);
         var3.setUniform("LarpMaterial", Z.slice());
         hj.u(var3, "SceneColor", I.j, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         hj.u(var3, "SceneDepth", I.A, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

   static GpuBuffer W() {
      return Z;
   }

   static GpuTextureView y() {
      return I.j;
   }

   static GpuTextureView D() {
      return I.A;
   }

   static LevelRenderState F() {
      return U;
   }

   static void o() {
      I.M(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      t = (boolean)1;
      j(U);
   }

   private static RenderPipeline P(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(y("pipeline/temple/" + var0))
         .withVertexShader(y("post/fullscreen"))
         .withFragmentShader(y("temple/" + var0))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
               .withUniform("SceneColor", hj.G)
               .withUniform("SceneDepth", hj.G)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withCull(false)
         .build();
   }

   private static void L(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier y(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static void T(String var0, RuntimeException var1) {
      M = (boolean)1;
      p();
      k();
      mt.P("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void p() {
      t = (boolean)0;
      z = (boolean)0;
      if (Z != null) {
         Z.close();
         Z = null;
      }

      I.n();
      ua.P();
   }

   public static void H() {
      hr.E();
      p();
      U = null;
      L = null;
      B = 0.0;
      f = ty.K();
   }

   private k() {
   }

   private static final class g {
      private GpuTexture U;
      private GpuTexture k;
      private GpuTextureView j;
      private GpuTextureView A;

      private void M(RenderTarget var1) {
         if (var1.getDepthTexture() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.U == null
            || this.U.getWidth(0) != var1.width
            || this.U.getHeight(0) != var1.height
            || this.U.getFormat() != var1.getColorTexture().getFormat()
            || this.k.getFormat() != var1.getDepthTexture().getFormat()) {
            this.n();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.U = var2.createTexture("Larp material scene", var3, var1.getColorTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.k = var2.createTexture("Larp material depth", var3, var1.getDepthTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.j = var2.createTextureView(this.U);
            this.A = var2.createTextureView(this.k);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.getColorTexture(), this.U, 0, 0, 0, 0, 0, var1.width, var1.height);
         var4.copyTextureToTexture(var1.getDepthTexture(), this.k, 0, 0, 0, 0, 0, var1.width, var1.height);
      }

      private void n() {
         if (this.j != null) {
            this.j.close();
            this.j = null;
         }

         if (this.A != null) {
            this.A.close();
            this.A = null;
         }

         if (this.U != null) {
            this.U.close();
            this.U = null;
         }

         if (this.k != null) {
            this.k.close();
            this.k = null;
         }
      }
   }
}
