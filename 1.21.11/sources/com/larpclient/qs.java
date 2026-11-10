package com.larpclient;

import com.larpclient.module.Module;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.OptionalInt;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterEntities;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeTranslucent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.EndMain;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_10859;
import net.minecraft.class_11658;
import net.minecraft.class_12075;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5636;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class qs {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> n = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> A = new IdentityHashMap<>();
   private static final RenderPipeline F = u("wet_reflection");
   private static final RenderPipeline C = u("underwater");
   public static final int S = 1088;
   private static final qs.z D = new qs.z();
   private static GpuBuffer j;
   private static boolean W;
   private static boolean H;
   private static boolean T;
   private static boolean K;
   private static boolean v;
   private static boolean t;
   private static int s = -1;
   private static Object P;
   private static class_11658 z;
   private static double J;
   private static double i;
   private static boolean w;

   public static void e() {
      if (!W) {
         W = (boolean)1;
         v = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         t = (boolean)(v && ih.x() ? 1 : 0);
         ta.E();
         if (t) {
            q8.G(
               "TerrainMaterials",
               "Sodium {} detected; Wet Ground and Snow Ground draw through a variant of Sodium's chunk program, other terrain materials keep Sodium output",
               ih.E()
            );
         } else if (v) {
            q8.L("TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices");
         }

         WorldRenderEvents.START_MAIN.register((StartMain)var0 -> b(var0.worldState()));
         WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)var0 -> u());
         WorldRenderEvents.BEFORE_TRANSLUCENT.register((BeforeTranslucent)var0 -> r());
         WorldRenderEvents.END_MAIN.register((EndMain)var0 -> {
            if (S()) {
               try {
                  eu.t();
               } catch (RuntimeException var2) {
                  N("volumetric-surface", var2);
               }
            }

            k();
         });
         ClientTickEvents.END_CLIENT_TICK.register(ex::o);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> c());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> c());
      }
   }

   public static boolean k() {
      return H || v;
   }

   public static boolean S() {
      return T && !k();
   }

   public static boolean M() {
      return H || v && (!t || ih.n() || ih.p());
   }

   public static boolean c() {
      return M() || v && (ih.m() || ih.e());
   }

   static boolean Y() {
      return !H && (!v || t && !ih.p() && !ih.m());
   }

   private static boolean f() {
      return T && !M();
   }

   private static boolean N() {
      return v ? !M() && (W(WetGroundModule.M()) || W(SnowGroundModule.R())) : !H;
   }

   static boolean A() {
      return t && j != null && f() && (W(WetGroundModule.M()) || W(SnowGroundModule.R()));
   }

   static int i() {
      return ((class_10859)j).field_57842;
   }

   private static boolean W(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean n() {
      return W(WavingFoliageModule.y()) || W(WetGroundModule.M()) || W(SnowGroundModule.R()) || W(RealisticWaterModule.C());
   }

   public static void w() {
      q8.v("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   static void j(int var0) {
      if (!v) {
         q8.G("SnowGround", "Subdivided drift surfaces; cells per top={}, maximum relief={} blocks", var0 >= 2 ? 9 : 4, 0.55F);
      } else if (t) {
         q8.G(
            "SnowGround",
            "Sodium {} chunk meshes carry snow tags; drift relief follows block corners without subdivision, maximum relief={} blocks",
            ih.E(),
            0.55F
         );
      } else {
         q8.L("SnowGround", "Sodium/Iris without a supported terrain hook; snow terrain keeps renderer output, snowfall still draws");
      }
   }

   public static void O() {
      j5.e();
      class_310 var0 = class_310.method_1551();
      int var1 = !W(WavingFoliageModule.y()) && !W(RealisticWaterModule.C()) && !W(SnowGroundModule.R()) ? 0 : 1;
      if (var0 != null && (var1 != 0 || w)) {
         w = (boolean)var1;
         var0.execute(() -> {
            if (var0.field_1687 != null) {
               var0.field_1769.method_3279();
            }
         });
      }
   }

   public static void B() {
      if (!n()) {
         W();
      }
   }

   private static void b(class_11658 var0) {
      z = var0;
      K = (boolean)0;
      class_310 var1 = class_310.method_1551();
      if (P != var1.field_1687) {
         W();
         P = var1.field_1687;
         J = qo.x();
         i = 0.0;
         j5.e();
      }

      if (s != t_.g) {
         W();
         H = (boolean)0;
         s = t_.g;
         n.clear();
         A.clear();
         ih.p();
         O();
      }

      if (n() && N() && var0.field_63082.field_63078 != null && b9.G()) {
         long var2 = c5.X();

         try {
            if (j == null) {
               j = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (t) {
                  f2.a(F);
                  q8.G("TerrainMaterials", "Sodium Wet Ground and Snow Ground material parameters and wet reflection shader ready");
               } else {
                  j(class_10799.field_64225);
                  j(class_10799.field_64227);
                  j(class_10799.field_56895);
                  l(class_10799.field_64225);
                  l(class_10799.field_64227);
                  l(class_10799.field_56895);
                  f2.a(F);
                  f2.a(C);
                  q8.G("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            a(var0);
            if (t) {
               ih.i(j5.N());
            }

            T = (boolean)1;
         } catch (RuntimeException var8) {
            N(t ? "sodium-prepare" : "prepare", var8);
         } finally {
            c5.w("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline l(RenderPipeline var0) {
      if (v) {
         return var0;
      } else {
         RenderPipeline var1 = ta.f(var0);
         if (var1 != null) {
            return var1;
         } else if (!H && n() && (var0 != class_10799.field_56895 || W(RealisticWaterModule.C()))) {
            return var0 != class_10799.field_64225 && var0 != class_10799.field_64227 && var0 != class_10799.field_56895
               ? var0
               : n.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == class_10799.field_56895 ? 1 : 0;
                     Builder var2 = f2.i(var0x)
                        .withLocation(u("pipeline/temple/" + var0x.getLocation().method_12832()))
                        .withVertexShader(u("temple/terrain"))
                        .withFragmentShader(u("temple/terrain"))
                        .withUniform("LarpMaterial", class_10789.field_60031);
                     if (var1x != 0) {
                        var2.withSampler("SceneColor").withSampler("SceneDepth");
                     }

                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var3 = var2.build();
                     f2.a(var3);
                     return var3;
                  }
               );
         } else {
            return A.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline j(RenderPipeline var0) {
      return A.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = f2.i(var0x)
               .withLocation(u("pipeline/temple/decode/" + var0x.getLocation().method_12832()))
               .withVertexShader(u("temple/terrain_decode"));
            RenderPipeline var2 = var1.build();
            f2.a(var2);
            return var2;
         }
      );
   }

   public static void e(RenderPass var0, RenderPipeline var1) {
      if (!ta.l(var0, var1)) {
         if (j != null && !H && n.containsValue(var1)) {
            var0.setUniform("LarpMaterial", j.slice());
            if (var1.getShaderDefines().comp_3104().contains("LARP_WATER") && D.U != null) {
               yf.R(var0, "SceneColor", D.U, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
               yf.R(var0, "SceneDepth", D.v, RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST));
            }
         }
      }
   }

   private static void a(class_11658 var0) {
      WavingFoliageModule var1 = WavingFoliageModule.y();
      RealisticWaterModule var2 = RealisticWaterModule.C();
      WetGroundModule var3 = WetGroundModule.M();
      SnowGroundModule var4 = SnowGroundModule.R();
      boolean var5 = W(var4);
      boolean var6 = W(var1);
      boolean var7 = W(var2);
      boolean var8 = W(var3);
      double var9 = qo.x();
      i = i + Math.clamp(var9 - J, 0.0, 0.1) * (var6 ? var1.Q.getValue() : 1.0);
      J = var9;
      float var11 = Math.clamp(var0.field_63085.field_63103, 0.0F, 1.0F);
      float var12 = var8 ? var3.f.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.q.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.r.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.A.getValue()) {
         var12 = Math.min(1.0F, var12 + var11 * 0.35F);
         var13 = Math.min(1.0F, var13 + var11 * 0.25F);
         var14 = Math.min(1.0F, var14 + var11 * 0.25F);
      }

      class_12075 var15 = var0.field_63082;
      Matrix4f var16 = new Matrix4f(b9.v()).mul(b9.T());
      Matrix4f var17 = new Matrix4f(var16).invert();
      float var18 = var0.field_63087.field_63091;
      float var19 = -((float)Math.sin(var18));
      float var20 = (float)Math.cos(var18);
      float var21 = Math.clamp(var20 * 1.8F + 0.15F, 0.0F, 1.0F) * (1.0F - var11 * 0.9F);
      int var22 = var0.field_63087.field_63097;
      MemoryStack var23 = MemoryStack.stackPush();

      try {
         ByteBuffer var24 = var23.malloc(1088);
         J(var24, (float)i, var6 ? var1.z.getValue().floatValue() : 0.0F, var6 ? var1.G.getValue().floatValue() / 100.0F : 0.0F, (float)var9);
         J(
            var24,
            var7 ? var2.W.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.Y.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.G.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         J(var24, var7 && var2.E.getValue() ? 1.0F : 0.0F, var7 && var2.V.getValue() ? 1.0F : 0.0F, var7 && var2.f.getValue() ? 1.0F : 0.0F, 0.0F);
         J(var24, var12, var13, var8 && var3.m.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.b.G("Flooded") ? 1.0F : 0.0F);
         J(var24, var8 ? 1.0F : 0.0F, var8 && var3.n.getValue() ? 1.0F : 0.0F, K ? 1.0F : 0.0F, b9.B() == class_5636.field_27886 ? 1.0F : 0.0F);
         J(var24, var19, var20, 0.0F, var21);
         J(var24, (var22 >> 16 & 0xFF) / 255.0F, (var22 >> 8 & 0xFF) / 255.0F, (var22 & 0xFF) / 255.0F, var11);
         J(var24, (float)(var15.field_63078.field_1352 % 8192.0), (float)var15.field_63078.field_1351, (float)(var15.field_63078.field_1350 % 8192.0), var14);
         J(
            var24,
            var5 ? var4.K.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.G.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.l.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.w() : 0.0F
         );
         var16.get(var24);
         var24.position(var24.position() + 64);
         var17.get(var24);
         var24.position(var24.position() + 64);
         if (var4 != null) {
            var4.D(var24, var15.field_63078, var9, var5);
         } else {
            for (int var25 = 0; var25 < 100; var25++) {
               var24.putLong(0L);
            }
         }

         J(var24, (float)var15.field_63078.field_1352, (float)var15.field_63078.field_1351, (float)var15.field_63078.field_1350, 0.0F);
         var24.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(j.slice(), var24);
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

   private static void r() {
      if (S() && W(RealisticWaterModule.C())) {
         try {
            D.H(class_310.method_1551().method_1522());
            K = (boolean)1;
            a(z);
         } catch (RuntimeException var1) {
            N("capture-water-scene", var1);
         }
      }
   }

   private static void u() {
      WetGroundModule var0 = WetGroundModule.M();
      if (f() && W(var0) && var0.n.getValue()) {
         try {
            class_276 var1 = class_310.method_1551().method_1522();
            D.H(var1);
            K = (boolean)1;
            a(z);
            p(F, var1);
         } catch (RuntimeException var2) {
            N("wet-reflection", var2);
         }
      }
   }

   private static void k() {
      RealisticWaterModule var0 = RealisticWaterModule.C();
      if (S() && W(var0) && var0.f.getValue() && b9.B() == class_5636.field_27886) {
         try {
            class_276 var1 = class_310.method_1551().method_1522();
            D.H(var1);
            K = (boolean)1;
            a(z);
            p(C, var1);
         } catch (RuntimeException var2) {
            N("underwater", var2);
         }
      }
   }

   private static void p(RenderPipeline var0, class_276 var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.method_71639(), OptionalInt.empty());

      try {
         yf.l(var3, var0);
         var3.setUniform("LarpMaterial", j.slice());
         yf.R(var3, "SceneColor", D.U, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
         yf.R(var3, "SceneDepth", D.v, RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST));
         var3.draw(0, 3);
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
      return j;
   }

   static GpuTextureView Q() {
      return D.U;
   }

   static GpuTextureView h() {
      return D.v;
   }

   static class_11658 y() {
      return z;
   }

   static void F() {
      D.H(class_310.method_1551().method_1522());
      K = (boolean)1;
      a(z);
   }

   private static RenderPipeline u(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(u("pipeline/temple/" + var0))
         .withVertexShader(u("post/fullscreen"))
         .withFragmentShader(u("temple/" + var0))
         .withUniform("LarpMaterial", class_10789.field_60031)
         .withSampler("SceneColor")
         .withSampler("SceneDepth")
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private static void J(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static class_2960 u(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private static void N(String var0, RuntimeException var1) {
      H = (boolean)1;
      W();
      O();
      q8.u("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void W() {
      K = (boolean)0;
      T = (boolean)0;
      if (j != null) {
         j.close();
         j = null;
      }

      D.K();
      eu.n();
   }

   public static void c() {
      ex.v();
      W();
      z = null;
      P = null;
      i = 0.0;
      J = qo.x();
   }

   private qs() {
   }

   private static final class z {
      private GpuTexture X;
      private GpuTexture s;
      private GpuTextureView U;
      private GpuTextureView v;

      private void H(class_276 var1) {
         if (var1.method_30278() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.X == null
            || this.X.getWidth(0) != var1.field_1482
            || this.X.getHeight(0) != var1.field_1481
            || this.X.getFormat() != var1.method_30277().getFormat()
            || this.s.getFormat() != var1.method_30278().getFormat()) {
            this.K();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.X = var2.createTexture("Larp material scene", var3, var1.method_30277().getFormat(), var1.field_1482, var1.field_1481, 1, 1);
            this.s = var2.createTexture("Larp material depth", var3, var1.method_30278().getFormat(), var1.field_1482, var1.field_1481, 1, 1);
            this.U = var2.createTextureView(this.X);
            this.v = var2.createTextureView(this.s);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.method_30277(), this.X, 0, 0, 0, 0, 0, var1.field_1482, var1.field_1481);
         var4.copyTextureToTexture(var1.method_30278(), this.s, 0, 0, 0, 0, 0, var1.field_1482, var1.field_1481);
      }

      private void K() {
         if (this.U != null) {
            this.U.close();
            this.U = null;
         }

         if (this.v != null) {
            this.v.close();
            this.v = null;
         }

         if (this.X != null) {
            this.X.close();
            this.X = null;
         }

         if (this.s != null) {
            this.s.close();
            this.s = null;
         }
      }
   }
}
