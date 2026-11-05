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

public final class y3 {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> g = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> d = new IdentityHashMap<>();
   private static final RenderPipeline w = t("wet_reflection");
   private static final RenderPipeline x = t("underwater");
   public static final int k = 1088;
   private static final y3.c e = new y3.c();
   private static GpuBuffer c;
   private static boolean m;
   private static boolean u;
   private static boolean D;
   private static boolean t;
   private static boolean X;
   private static boolean o;
   private static int G = -1;
   private static Object Z;
   private static LevelRenderState J;
   private static double O;
   private static double C;
   private static boolean r;

   public static void R() {
      if (!m) {
         m = (boolean)1;
         X = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         o = (boolean)(X && mk.o() ? 1 : 0);
         _2.s();
         if (o) {
            r4.V("TerrainMaterials", "Sodium {} detected; Wet Ground draws through Sodium's chunk shader, other terrain materials keep Sodium output", mk.Z());
         } else if (X) {
            r4.m("TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0 -> q(var0.levelState()));
         LevelRenderEvents.AFTER_SOLID_FEATURES.register((AfterSolidFeatures)var0 -> A());
         LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> m());
         LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((AfterTranslucentTerrain)var0 -> {
            if (n()) {
               try {
                  y.m();
               } catch (RuntimeException var2) {
                  t("volumetric-surface", var2);
               }
            }

            k();
         });
         ClientTickEvents.END_CLIENT_TICK.register(df::o);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> T());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> T());
      }
   }

   public static boolean S() {
      return u || X;
   }

   public static boolean n() {
      return D && !S();
   }

   public static boolean i() {
      return u || X && (!o || mk.v());
   }

   private static boolean t() {
      return D && !i();
   }

   private static boolean c() {
      return X ? !i() && y(WetGroundModule.Y()) : !u;
   }

   private static boolean y(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean U() {
      return y(WavingFoliageModule.P()) || y(WetGroundModule.Y()) || y(SnowGroundModule.x()) || y(RealisticWaterModule.L());
   }

   public static void G() {
      r4.g("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   public static void z() {
      er.K();
      Minecraft var0 = Minecraft.getInstance();
      int var1 = !y(WavingFoliageModule.P()) && !y(RealisticWaterModule.L()) && !y(SnowGroundModule.x()) ? 0 : 1;
      if (var0 != null && (var1 != 0 || r)) {
         r = (boolean)var1;
         var0.execute(() -> {
            if (var0.level != null) {
               var0.levelExtractor.allChanged();
            }
         });
      }
   }

   public static void J() {
      if (!U()) {
         p();
      }
   }

   private static void q(LevelRenderState var0) {
      J = var0;
      t = (boolean)0;
      Minecraft var1 = Minecraft.getInstance();
      if (Z != var1.level) {
         p();
         Z = var1.level;
         O = ea.y();
         C = 0.0;
         er.K();
      }

      if (G != jk.v) {
         p();
         u = (boolean)0;
         G = jk.v;
         g.clear();
         d.clear();
         mk.b();
         z();
      }

      if (U()
         && c()
         && var0.cameraRenderState.pos != null
         && var0.cameraRenderState.projectionMatrix != null
         && var0.cameraRenderState.viewRotationMatrix != null) {
         long var2 = h_.U();

         try {
            if (c == null) {
               c = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (o) {
                  e4.U(w);
                  r4.V("TerrainMaterials", "Sodium Wet Ground material and wet reflection shaders ready");
               } else {
                  T(RenderPipelines.SOLID_TERRAIN);
                  T(RenderPipelines.CUTOUT_TERRAIN);
                  T(RenderPipelines.TRANSLUCENT_TERRAIN);
                  a(RenderPipelines.SOLID_TERRAIN);
                  a(RenderPipelines.CUTOUT_TERRAIN);
                  a(RenderPipelines.TRANSLUCENT_TERRAIN);
                  e4.U(w);
                  e4.U(x);
                  r4.V("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            t(var0);
            if (o) {
               mk.s();
               mk.V();
            }

            D = (boolean)1;
         } catch (RuntimeException var8) {
            t(o ? "sodium-prepare" : "prepare", var8);
         } finally {
            h_.n("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline D(RenderPipeline var0) {
      mk.t();
      RenderPipeline var1 = _2.U(var0);
      if (var1 != null) {
         return var1;
      } else {
         return o && t() && y(WetGroundModule.Y()) ? mk.h(var0) : var0;
      }
   }

   public static RenderPipeline a(RenderPipeline var0) {
      if (X) {
         return var0;
      } else {
         RenderPipeline var1 = _2.J(var0);
         if (var1 != null) {
            return var1;
         } else if (!u && U() && (var0 != RenderPipelines.TRANSLUCENT_TERRAIN || y(RealisticWaterModule.L()))) {
            return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT_TERRAIN
               ? var0
               : g.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == RenderPipelines.TRANSLUCENT_TERRAIN ? 1 : 0;
                     Builder var2 = RenderPipeline.builder(new Snippet[0])
                        .withLocation(C("pipeline/temple/" + var0x.getLocation().getPath()))
                        .withVertexShader(C("temple/terrain"))
                        .withFragmentShader(C("temple/terrain"))
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
                        var7.withUniform("SceneColor", of.t).withUniform("SceneDepth", of.t);
                     }

                     var2.withBindGroupLayout(var7.build());
                     List var8 = of.J(var0x);

                     for (int var5 = 0; var5 < var8.size(); var5++) {
                        var2.withColorTargetState(var5, (ColorTargetState)var8.get(var5));
                     }

                     List var9 = of.m(var0x);

                     for (int var6 = 0; var6 < var9.size(); var6++) {
                        var2.withVertexBinding(var6, (VertexFormat)var9.get(var6));
                     }

                     var0x.getShaderDefines().flags().forEach(var2::withShaderDefine);
                     var0x.getShaderDefines().values().forEach((var1xx, var2x) -> var2.withShaderDefine(var1xx, Float.parseFloat(var2x)));
                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var10 = var2.build();
                     e4.U(var10);
                     return var10;
                  }
               );
         } else {
            return d.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline T(RenderPipeline var0) {
      return d.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(C("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(C("temple/terrain_decode"))
               .withFragmentShader(of.b(var0x))
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

            for (BindGroupLayout var3 : var0x.getBindGroupLayouts()) {
               var1.withBindGroupLayout(var3);
            }

            List var5 = of.J(var0x);

            for (int var6 = 0; var6 < var5.size(); var6++) {
               var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
            }

            List var7 = of.m(var0x);

            for (int var4 = 0; var4 < var7.size(); var4++) {
               var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
            }

            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
            RenderPipeline var8 = var1.build();
            e4.U(var8);
            return var8;
         }
      );
   }

   public static void g(RenderPass var0, RenderPipeline var1) {
      if (!_2.J(var0, var1)) {
         if (c != null && !u && (g.containsValue(var1) || mk.U(var1))) {
            var0.setUniform("LarpMaterial", c.slice());
            if (var1.getShaderDefines().flags().contains("LARP_WATER") && e.D != null) {
               of.h(var0, "SceneColor", e.D, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               of.h(var0, "SceneDepth", e.j, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            }
         }
      }
   }

   private static void t(LevelRenderState var0) {
      WavingFoliageModule var1 = WavingFoliageModule.P();
      RealisticWaterModule var2 = RealisticWaterModule.L();
      WetGroundModule var3 = WetGroundModule.Y();
      SnowGroundModule var4 = SnowGroundModule.x();
      boolean var5 = y(var4);
      boolean var6 = y(var1);
      boolean var7 = y(var2);
      boolean var8 = y(var3);
      double var9 = ea.y();
      C = C + Math.clamp(var9 - O, 0.0, 0.1) * (var6 ? var1.m.getValue() : 1.0);
      O = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.M.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.Q.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.D.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.N.getValue()) {
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
         w(var25, (float)C, var6 ? var1.Q.getValue().floatValue() : 0.0F, var6 ? var1.g.getValue().floatValue() / 100.0F : 0.0F, (float)var9);
         w(
            var25,
            var7 ? var2.h.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.q.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.F.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         w(
            var25,
            var7 && var2.i.getValue() ? 1.0F : 0.0F,
            var7 && var2.D.getValue() ? 1.0F : 0.0F,
            var7 && var2.u.getValue() ? 1.0F : 0.0F,
            RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F
         );
         w(var25, var12, var13, var8 && var3.C.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.S.i("Flooded") ? 1.0F : 0.0F);
         w(var25, var8 ? 1.0F : 0.0F, var8 && var3.B.getValue() ? 1.0F : 0.0F, t ? 1.0F : 0.0F, var15.fogType == FogType.WATER ? 1.0F : 0.0F);
         w(var25, var19, var20, 0.0F, var21);
         w(var25, (var23 >> 16 & 0xFF) / 255.0F, (var23 >> 8 & 0xFF) / 255.0F, (var23 & 0xFF) / 255.0F, var11);
         w(var25, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         w(
            var25,
            var5 ? var4.a.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.j.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.m.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.A() : 0.0F
         );
         var16.get(var25);
         var25.position(var25.position() + 64);
         var17.get(var25);
         var25.position(var25.position() + 64);
         if (var4 != null) {
            var4.z(var25, var15.pos, var9, var5);
         } else {
            for (int var26 = 0; var26 < 100; var26++) {
               var25.putLong(0L);
            }
         }

         w(var25, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var25.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(c.slice(), var25);
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

   private static void m() {
      if (n() && y(RealisticWaterModule.L())) {
         try {
            e.n(Minecraft.getInstance().gameRenderer.mainRenderTarget());
            t = (boolean)1;
            t(J);
         } catch (RuntimeException var1) {
            t("capture-water-scene", var1);
         }
      }
   }

   private static void A() {
      WetGroundModule var0 = WetGroundModule.Y();
      if (t() && y(var0) && var0.B.getValue()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            e.n(var1);
            t = (boolean)1;
            t(J);
            K(w, var1);
         } catch (RuntimeException var2) {
            t("wet-reflection", var2);
         }
      }
   }

   private static void k() {
      RealisticWaterModule var0 = RealisticWaterModule.L();
      if (n() && y(var0) && var0.u.getValue() && J.cameraRenderState.fogType == FogType.WATER) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            e.n(var1);
            t = (boolean)1;
            t(J);
            K(x, var1);
         } catch (RuntimeException var2) {
            t("underwater", var2);
         }
      }
   }

   private static void K(RenderPipeline var0, RenderTarget var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorTextureView(), Optional.empty());

      try {
         of.z(var3, var0);
         var3.setUniform("LarpMaterial", c.slice());
         of.h(var3, "SceneColor", e.D, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         of.h(var3, "SceneDepth", e.j, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

   static GpuBuffer x() {
      return c;
   }

   static GpuTextureView Z() {
      return e.D;
   }

   static GpuTextureView u() {
      return e.j;
   }

   static LevelRenderState b() {
      return J;
   }

   static void I() {
      e.n(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      t = (boolean)1;
      t(J);
   }

   private static RenderPipeline t(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(C("pipeline/temple/" + var0))
         .withVertexShader(C("post/fullscreen"))
         .withFragmentShader(C("temple/" + var0))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
               .withUniform("SceneColor", of.t)
               .withUniform("SceneDepth", of.t)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withCull(false)
         .build();
   }

   private static void w(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier C(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static void t(String var0, RuntimeException var1) {
      u = (boolean)1;
      p();
      z();
      r4.r("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void p() {
      t = (boolean)0;
      D = (boolean)0;
      if (c != null) {
         c.close();
         c = null;
      }

      e.E();
      y.Y();
   }

   public static void T() {
      df.o();
      p();
      J = null;
      Z = null;
      C = 0.0;
      O = ea.y();
   }

   private y3() {
   }

   private static final class c {
      private GpuTexture I;
      private GpuTexture T;
      private GpuTextureView D;
      private GpuTextureView j;

      private void n(RenderTarget var1) {
         if (var1.getDepthTexture() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.I == null
            || this.I.getWidth(0) != var1.width
            || this.I.getHeight(0) != var1.height
            || this.I.getFormat() != var1.getColorTexture().getFormat()
            || this.T.getFormat() != var1.getDepthTexture().getFormat()) {
            this.E();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.I = var2.createTexture("Larp material scene", var3, var1.getColorTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.T = var2.createTexture("Larp material depth", var3, var1.getDepthTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.D = var2.createTextureView(this.I);
            this.j = var2.createTextureView(this.T);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.getColorTexture(), this.I, 0, 0, 0, 0, 0, var1.width, var1.height);
         var4.copyTextureToTexture(var1.getDepthTexture(), this.T, 0, 0, 0, 0, 0, var1.width, var1.height);
      }

      private void E() {
         if (this.D != null) {
            this.D.close();
            this.D = null;
         }

         if (this.j != null) {
            this.j.close();
            this.j = null;
         }

         if (this.I != null) {
            this.I.close();
            this.I = null;
         }

         if (this.T != null) {
            this.T.close();
            this.T = null;
         }
      }
   }
}
