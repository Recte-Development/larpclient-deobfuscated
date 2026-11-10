package com.larpclient;

import com.larpclient.module.Module;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class w6 {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> r = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> B = new IdentityHashMap<>();
   private static final RenderPipeline m = s("wet_reflection");
   private static final RenderPipeline a = s("underwater");
   public static final int C = 1088;
   private static final w6.s f = new w6.s();
   private static GpuBuffer Y;
   private static boolean N;
   private static boolean X;
   private static boolean d;
   private static boolean x;
   private static boolean G;
   private static boolean w;
   private static int k = -1;
   private static Object v;
   private static LevelRenderState c;
   private static double o;
   private static double n;
   private static boolean T;

   public static void W() {
      if (!N) {
         N = (boolean)1;
         G = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         w = (boolean)(G && k_.r() ? 1 : 0);
         kx.I();
         if (w) {
            h.A("TerrainMaterials", "Sodium {} detected; Wet Ground draws through Sodium's chunk shader, other terrain materials keep Sodium output", k_.E());
         } else if (G) {
            h.g("TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0 -> p(var0.levelState()));
         LevelRenderEvents.AFTER_SOLID_FEATURES.register((AfterSolidFeatures)var0 -> z());
         LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> x());
         LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((AfterTranslucentTerrain)var0 -> {
            if (c()) {
               try {
                  tt.O();
               } catch (RuntimeException var2) {
                  q("volumetric-surface", var2);
               }
            }

            F();
         });
         ClientTickEvents.END_CLIENT_TICK.register(b9::p);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> e());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> e());
      }
   }

   public static boolean C() {
      return X || G;
   }

   public static boolean c() {
      return d && !C();
   }

   public static boolean B() {
      return X || G && (!w || k_.C());
   }

   private static boolean f() {
      return d && !B();
   }

   private static boolean O() {
      return G ? !B() && Q(WetGroundModule.h()) : !X;
   }

   private static boolean Q(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean z() {
      return Q(WavingFoliageModule.h()) || Q(WetGroundModule.h()) || Q(SnowGroundModule.C()) || Q(RealisticWaterModule.K());
   }

   public static void Q() {
      h.U("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   public static void N() {
      zx.b();
      Minecraft var0 = Minecraft.getInstance();
      int var1 = !Q(WavingFoliageModule.h()) && !Q(RealisticWaterModule.K()) && !Q(SnowGroundModule.C()) ? 0 : 1;
      if (var0 != null && (var1 != 0 || T)) {
         T = (boolean)var1;
         var0.execute(() -> {
            if (var0.level != null) {
               var0.levelExtractor.allChanged();
            }
         });
      }
   }

   public static void P() {
      if (!z()) {
         O();
      }
   }

   private static void p(LevelRenderState var0) {
      c = var0;
      x = (boolean)0;
      Minecraft var1 = Minecraft.getInstance();
      if (v != var1.level) {
         O();
         v = var1.level;
         o = cf.F();
         n = 0.0;
         zx.b();
      }

      if (k != wa.s) {
         O();
         X = (boolean)0;
         k = wa.s;
         r.clear();
         B.clear();
         k_.r();
         N();
      }

      if (z()
         && O()
         && var0.cameraRenderState.pos != null
         && var0.cameraRenderState.projectionMatrix != null
         && var0.cameraRenderState.viewRotationMatrix != null) {
         long var2 = zj.m();

         try {
            if (Y == null) {
               Y = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (w) {
                  ai.U(m);
                  h.A("TerrainMaterials", "Sodium Wet Ground material and wet reflection shaders ready");
               } else {
                  z(RenderPipelines.SOLID_TERRAIN);
                  z(RenderPipelines.CUTOUT_TERRAIN);
                  z(RenderPipelines.TRANSLUCENT_TERRAIN);
                  n(RenderPipelines.SOLID_TERRAIN);
                  n(RenderPipelines.CUTOUT_TERRAIN);
                  n(RenderPipelines.TRANSLUCENT_TERRAIN);
                  ai.U(m);
                  ai.U(a);
                  h.A("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            t(var0);
            if (w) {
               k_.g();
               k_.k();
            }

            d = (boolean)1;
         } catch (RuntimeException var8) {
            q(w ? "sodium-prepare" : "prepare", var8);
         } finally {
            zj.l("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline M(RenderPipeline var0) {
      k_.u();
      RenderPipeline var1 = kx.N(var0);
      if (var1 != null) {
         return var1;
      } else {
         return w && f() && Q(WetGroundModule.h()) ? k_.J(var0) : var0;
      }
   }

   public static RenderPipeline n(RenderPipeline var0) {
      if (G) {
         return var0;
      } else {
         RenderPipeline var1 = kx.o(var0);
         if (var1 != null) {
            return var1;
         } else if (!X && z() && (var0 != RenderPipelines.TRANSLUCENT_TERRAIN || Q(RealisticWaterModule.K()))) {
            return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT_TERRAIN
               ? var0
               : r.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == RenderPipelines.TRANSLUCENT_TERRAIN ? 1 : 0;
                     Builder var2 = RenderPipeline.builder(new Snippet[0])
                        .withLocation(U("pipeline/temple/" + var0x.getLocation().getPath()))
                        .withVertexShader(U("temple/terrain"))
                        .withFragmentShader(U("temple/terrain"))
                        .withCull(var0x.isCull())
                        .withPolygonMode(var0x.getPolygonMode())
                        .withPrimitiveTopology(var0x.getPrimitiveTopology())
                        .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

                     for (BindGroupLayout var4 : var0x.getBindGroupLayouts()) {
                        var2.withBindGroupLayout(var4);
                     }

                     com.mojang.blaze3d.pipeline.BindGroupLayout.Builder var7 = BindGroupLayout.builder()
                        .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER);
                     if (var1x != 0) {
                        var7.withSampler("SceneColor").withSampler("SceneDepth");
                     }

                     var2.withBindGroupLayout(var7.build());
                     List var8 = mx.G(var0x);

                     for (int var5 = 0; var5 < var8.size(); var5++) {
                        var2.withColorTargetState(var5, (ColorTargetState)var8.get(var5));
                     }

                     List var9 = mx.i(var0x);

                     for (int var6 = 0; var6 < var9.size(); var6++) {
                        var2.withVertexBinding(var6, (VertexFormat)var9.get(var6));
                     }

                     var0x.getShaderDefines().flags().forEach(var2::withShaderDefine);
                     var0x.getShaderDefines().values().forEach((var1xx, var2x) -> var2.withShaderDefine(var1xx, Float.parseFloat(var2x)));
                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var10 = var2.build();
                     ai.U(var10);
                     return var10;
                  }
               );
         } else {
            return B.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline z(RenderPipeline var0) {
      return B.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(U("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(U("temple/terrain_decode"))
               .withFragmentShader(mx.K(var0x))
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

            for (BindGroupLayout var3 : var0x.getBindGroupLayouts()) {
               var1.withBindGroupLayout(var3);
            }

            List var5 = mx.G(var0x);

            for (int var6 = 0; var6 < var5.size(); var6++) {
               var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
            }

            List var7 = mx.i(var0x);

            for (int var4 = 0; var4 < var7.size(); var4++) {
               var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
            }

            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
            RenderPipeline var8 = var1.build();
            ai.U(var8);
            return var8;
         }
      );
   }

   public static void C(RenderPass var0, RenderPipeline var1) {
      if (!kx.A(var0, var1)) {
         if (Y != null && !X && (r.containsValue(var1) || k_.k(var1))) {
            var0.setUniform("LarpMaterial", Y.slice());
            if (var1.getShaderDefines().flags().contains("LARP_WATER") && f.i != null) {
               mx.x(var0, "SceneColor", f.i, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               mx.x(var0, "SceneDepth", f.E, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            }
         }
      }
   }

   private static void t(LevelRenderState var0) {
      WavingFoliageModule var1 = WavingFoliageModule.h();
      RealisticWaterModule var2 = RealisticWaterModule.K();
      WetGroundModule var3 = WetGroundModule.h();
      SnowGroundModule var4 = SnowGroundModule.C();
      boolean var5 = Q(var4);
      boolean var6 = Q(var1);
      boolean var7 = Q(var2);
      boolean var8 = Q(var3);
      double var9 = cf.F();
      n = n + Math.clamp(var9 - o, 0.0, 0.1) * (var6 ? var1.G.getValue() : 1.0);
      o = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.B.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.Q.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.M.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.m.getValue()) {
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
      int var22 = var0.skyRenderState.skyColor;
      MemoryStack var23 = MemoryStack.stackPush();

      try {
         ByteBuffer var24 = var23.malloc(1088);
         P(var24, (float)n, var6 ? var1.v.getValue().floatValue() : 0.0F, var6 ? var1.t.getValue().floatValue() / 100.0F : 0.0F, (float)var9);
         P(
            var24,
            var7 ? var2.t.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.U.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.y.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         P(
            var24,
            var7 && var2.g.getValue() ? 1.0F : 0.0F,
            var7 && var2.M.getValue() ? 1.0F : 0.0F,
            var7 && var2.S.getValue() ? 1.0F : 0.0F,
            RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F
         );
         P(var24, var12, var13, var8 && var3.R.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.q.O("Flooded") ? 1.0F : 0.0F);
         P(var24, var8 ? 1.0F : 0.0F, var8 && var3.W.getValue() ? 1.0F : 0.0F, x ? 1.0F : 0.0F, var15.fogType == FogType.WATER ? 1.0F : 0.0F);
         P(var24, var19, var20, 0.0F, var21);
         P(var24, (var22 >> 16 & 0xFF) / 255.0F, (var22 >> 8 & 0xFF) / 255.0F, (var22 & 0xFF) / 255.0F, var11);
         P(var24, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         P(
            var24,
            var5 ? var4.H.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.t.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.l.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.j() : 0.0F
         );
         var16.get(var24);
         var24.position(var24.position() + 64);
         var17.get(var24);
         var24.position(var24.position() + 64);
         if (var4 != null) {
            var4.q(var24, var15.pos, var9, var5);
         } else {
            for (int var25 = 0; var25 < 100; var25++) {
               var24.putLong(0L);
            }
         }

         P(var24, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var24.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(Y.slice(), var24);
      } catch (Throwable var27) {
         if (var23 != null) {
            try {
               var23.close();
            } catch (Throwable var26) {
               var27.addSuppressed(var26);
            }
         }

         throw var27;
      }

      if (var23 != null) {
         var23.close();
      }
   }

   private static void x() {
      if (c() && Q(RealisticWaterModule.K())) {
         try {
            f.c(Minecraft.getInstance().gameRenderer.mainRenderTarget());
            x = (boolean)1;
            t(c);
         } catch (RuntimeException var1) {
            q("capture-water-scene", var1);
         }
      }
   }

   private static void z() {
      WetGroundModule var0 = WetGroundModule.h();
      if (f() && Q(var0) && var0.W.getValue()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            f.c(var1);
            x = (boolean)1;
            t(c);
            J(m, var1);
         } catch (RuntimeException var2) {
            q("wet-reflection", var2);
         }
      }
   }

   private static void F() {
      RealisticWaterModule var0 = RealisticWaterModule.K();
      if (c() && Q(var0) && var0.S.getValue() && c.cameraRenderState.fogType == FogType.WATER) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            f.c(var1);
            x = (boolean)1;
            t(c);
            J(a, var1);
         } catch (RuntimeException var2) {
            q("underwater", var2);
         }
      }
   }

   private static void J(RenderPipeline var0, RenderTarget var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorTextureView(), Optional.empty());

      try {
         mx.i(var3, var0);
         var3.setUniform("LarpMaterial", Y.slice());
         mx.x(var3, "SceneColor", f.i, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         mx.x(var3, "SceneDepth", f.E, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

   static GpuBuffer j() {
      return Y;
   }

   static GpuTextureView w() {
      return f.i;
   }

   static GpuTextureView h() {
      return f.E;
   }

   static LevelRenderState u() {
      return c;
   }

   static void c() {
      f.c(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      x = (boolean)1;
      t(c);
   }

   private static RenderPipeline s(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(U("pipeline/temple/" + var0))
         .withVertexShader(U("post/fullscreen"))
         .withFragmentShader(U("temple/" + var0))
         .withBindGroupLayout(
            BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withSampler("SceneColor").withSampler("SceneDepth").build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withCull(false)
         .build();
   }

   private static void P(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier U(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static void q(String var0, RuntimeException var1) {
      X = (boolean)1;
      O();
      N();
      h.m("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void O() {
      x = (boolean)0;
      d = (boolean)0;
      if (Y != null) {
         Y.close();
         Y = null;
      }

      f.q();
      tt.a();
   }

   public static void e() {
      b9.b();
      O();
      c = null;
      v = null;
      n = 0.0;
      o = cf.F();
   }

   private w6() {
   }

   private static final class s {
      private GpuTexture T;
      private GpuTexture x;
      private GpuTextureView i;
      private GpuTextureView E;

      private void c(RenderTarget var1) {
         if (var1.getDepthTexture() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.T == null
            || this.T.getWidth(0) != var1.width
            || this.T.getHeight(0) != var1.height
            || this.T.getFormat() != var1.getColorTexture().getFormat()
            || this.x.getFormat() != var1.getDepthTexture().getFormat()) {
            this.q();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.T = var2.createTexture("Larp material scene", var3, var1.getColorTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.x = var2.createTexture("Larp material depth", var3, var1.getDepthTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.i = var2.createTextureView(this.T);
            this.E = var2.createTextureView(this.x);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.getColorTexture(), this.T, 0, 0, 0, 0, 0, var1.width, var1.height);
         var4.copyTextureToTexture(var1.getDepthTexture(), this.x, 0, 0, 0, 0, 0, var1.width, var1.height);
      }

      private void q() {
         if (this.i != null) {
            this.i.close();
            this.i = null;
         }

         if (this.E != null) {
            this.E.close();
            this.E = null;
         }

         if (this.T != null) {
            this.T.close();
            this.T = null;
         }

         if (this.x != null) {
            this.x.close();
            this.x = null;
         }
      }
   }
}
