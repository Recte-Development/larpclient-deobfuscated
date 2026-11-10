package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.StartMain;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10789;
import net.minecraft.class_11658;
import net.minecraft.class_12075;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5636;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

public final class sm {
   private static final class_2960 v = class_2960.method_60655("larp-client", "distance_fog");
   private static final RenderPipeline e = e("distance_fog", false);
   private static final RenderPipeline w = e("distance_fog_layer", true);
   static final int L = 128;
   private static final Matrix4f U = new Matrix4f();
   private static boolean i;
   private static boolean J;
   private static boolean X;
   private static boolean r;
   private static boolean n;
   private static final GpuBuffer[] R = new GpuBuffer[2];
   private static GpuTexture b;
   private static GpuTextureView x;
   private static boolean a;
   private static int F = -1;
   private static String Q;
   private static String S;

   private static RenderPipeline e(String var0, boolean var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(class_2960.method_60655("larp-client", var0))
         .withVertexShader(class_2960.method_60655("larp-client", "post/fullscreen"))
         .withFragmentShader(class_2960.method_60655("larp-client", "post/distance_fog"))
         .withSampler("Depth")
         .withSampler("OpaqueDepth")
         .withUniform("DistanceFog", class_10789.field_60031)
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withColorWrite(true, var1)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   public static void O() {
      WorldRenderEvents.START_MAIN.register((StartMain)var0 -> a = (boolean)0);
      WorldRenderEvents.BEFORE_TRANSLUCENT.addPhaseOrdering(v, Event.DEFAULT_PHASE);
      WorldRenderEvents.BEFORE_TRANSLUCENT.register(v, sm::C);
      WorldRenderEvents.END_MAIN.addPhaseOrdering(v, Event.DEFAULT_PHASE);
      WorldRenderEvents.END_MAIN.register(v, sm::E);
   }

   public static void x(Matrix4f var0) {
      U.set(var0);
      i = (boolean)1;
   }

   public static boolean J(Matrix4f var0) {
      if (i) {
         var0.set(U);
      }

      return i;
   }

   private static void C(WorldRenderContext var0) {
      a = (boolean)0;
      if (!n && !wi.E()) {
         a = y(var0, sm.y.u);
      }
   }

   private static void E(WorldRenderContext var0) {
      sm.y var1 = a && x != null ? sm.y.L : sm.y.x;
      a = (boolean)0;
      if (y(var0, var1)) {
         String var2 = var1 == sm.y.L
            ? "before translucent terrain, then translucent surfaces by their own depth"
            : "once from the final depth ("
               + (wi.E() ? "Stream Proof Hide Everything" : (n ? "opaque depth copy failed" : "no draw before translucent terrain"))
               + ")";
         if (!var2.equals(S)) {
            S = var2;
            q8.G("Fog", "Composed {}", var2);
         }
      }
   }

   private static boolean y(WorldRenderContext var0, sm.y var1) {
      class_310 var2 = class_310.method_1551();
      FogModule var3 = FogModule.Z();
      if (var2.field_1687 != null && var3 != null && var3.g()) {
         class_12075 var4 = var0.worldState().field_63082;
         if (var4 == null || var4.field_63078 == null || !b9.G()) {
            J("camera state not extracted");
            return false;
         }

         if (!G(b9.B())) {
            J("camera in " + b9.B() + " fog");
            return false;
         }

         if (B()) {
            J("shader pack active");
            return false;
         }

         float var5 = var3.f(var2.method_61966().method_60637(false));
         if (var5 <= 0.001F) {
            J("faded underground");
            return false;
         }

         if (Q != null) {
            q8.v("Fog", "Resumed after {}", Q);
            Q = null;
         }

         if (F != t_.g) {
            X();
            n = (boolean)0;
            J = (boolean)0;
            F = t_.g;
         }

         if (J) {
            return false;
         }

         long var6 = c5.X();
         String var8 = "resources";

         try {
            if (R[0] == null) {
               f2.a(e);
               f2.a(w);
               q8.G("Fog", "Fog shader loaded; composed before translucent terrain and over translucent surfaces");
            }

            GpuBuffer var9 = R[var1.R];
            if (var9 == null) {
               var9 = R[var1.R] = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
            }

            class_276 var10 = var2.method_1522();
            if (var1 == sm.y.u) {
               var8 = "opaque depth copy";
               if (!p(var10)) {
                  return false;
               }
            }

            var8 = "parameters";
            if (!i && !r) {
               r = (boolean)1;
               q8.L("Fog", "World projection was not captured; using the unbobbed camera projection");
            }

            Matrix4f var11 = new Matrix4f((Matrix4fc)(i ? U : b9.v())).mul(b9.T()).invert();
            class_11658 var12 = var0.worldState();
            float var13 = var12.field_63087.field_63091;
            float var14 = -((float)Math.sin(var13));
            float var15 = (float)Math.cos(var13);
            float var16 = Math.clamp(var12.field_63085.field_63103, 0.0F, 1.0F);
            float var17 = L(-0.08F, 0.14F, var15) * (1.0F - var16 * 0.85F);
            Vector4fc var18 = b9.l();
            float[] var19 = L(var3, new Vector4f(var18.x(), var18.y(), var18.z(), var18.w()));
            float var20 = var2.field_1687.method_8615();
            float var21 = (float)Math.sin(Math.toRadians(var3.h.getValue()));
            MemoryStack var22 = MemoryStack.stackPush();

            try {
               ByteBuffer var23 = var22.malloc(128);
               var11.get(var23);
               var23.position(64);
               var23.putFloat((float)var4.field_63078.field_1351 - var20).putFloat(1.0F / var3.z.getValue().floatValue()).putFloat(0.0F).putFloat(var1.r);
               var23.putFloat(var3.k.getValue().floatValue()).putFloat(var3.F()).putFloat(1.0F * var5).putFloat(var21);
               var23.putFloat(var19[0]).putFloat(var19[1]).putFloat(var19[2]).putFloat(var3.C.getValue().floatValue());
               var23.putFloat(var14).putFloat(var15).putFloat(0.0F).putFloat(var17);
               var23.flip();
               RenderSystem.getDevice().createCommandEncoder().writeToBuffer(var9.slice(), var23);
            } catch (Throwable var38) {
               if (var22 != null) {
                  try {
                     var22.close();
                  } catch (Throwable var36) {
                     var38.addSuppressed(var36);
                  }
               }

               throw var38;
            }

            if (var22 != null) {
               var22.close();
            }

            var8 = "compose " + var1.l;
            class_276 var41 = wi.p(var10);
            RenderPass var42 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp distance fog", var41.method_71639(), OptionalInt.empty());

            try {
               yf.l(var42, var41 == var10 ? e : w);
               var42.setUniform("DistanceFog", var9.slice());
               class_12137 var24 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
               yf.R(var42, "Depth", var10.method_71640(), var24);
               yf.R(var42, "OpaqueDepth", var1 == sm.y.L ? x : var10.method_71640(), var24);
               var42.draw(0, 3);
            } catch (Throwable var37) {
               if (var42 != null) {
                  try {
                     var42.close();
                  } catch (Throwable var35) {
                     var37.addSuppressed(var35);
                  }
               }

               throw var37;
            }

            if (var42 != null) {
               var42.close();
            }

            h7.G();
            c5.V("Distance fog passes", 1L);
            return true;
         } catch (RuntimeException var39) {
            J = (boolean)1;

            try {
               X();
            } catch (RuntimeException var34) {
               var39.addSuppressed(var34);
            }

            q8.u("Fog", "DistanceFogRenderer stage=" + var8 + "; fog disabled until resource reload", var39);
            return false;
         } finally {
            c5.w("Distance fog", var6);
         }
      } else {
         Q = null;
         return false;
      }
   }

   private static boolean p(class_276 var0) {
      try {
         GpuTexture var1 = var0.method_30278();
         if (var1 == null) {
            return false;
         }

         if (b == null || b.getWidth(0) != var1.getWidth(0) || b.getHeight(0) != var1.getHeight(0) || b.getFormat() != var1.getFormat()) {
            o();
            b = RenderSystem.getDevice()
               .createTexture("Larp fog depth before translucent terrain", 13, var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
            x = RenderSystem.getDevice().createTextureView(b);
            q8.v("Fog", "Opaque depth copy allocated {}x{}", var1.getWidth(0), var1.getHeight(0));
         }

         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var1, b, 0, 0, 0, 0, 0, var1.getWidth(0), var1.getHeight(0));
         return true;
      } catch (RuntimeException var4) {
         n = (boolean)1;

         try {
            o();
         } catch (RuntimeException var3) {
            var4.addSuppressed(var3);
         }

         q8.u("Fog", "Depth copy before translucent terrain failed; fog is composed once from the final depth until resource reload", var4);
         return false;
      }
   }

   private static void o() {
      GpuTextureView var0 = x;
      GpuTexture var1 = b;
      x = null;
      b = null;

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

   static boolean G(class_5636 var0) {
      return var0 == class_5636.field_27888 || var0 == class_5636.field_60563;
   }

   private static void J(String var0) {
      if (!var0.equals(Q)) {
         Q = var0;
         q8.v("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] L(FogModule var0, Vector4f var1) {
      if (!var0.X.G("Custom") && var1 != null) {
         Integer var3 = wi.s() ? z() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.X.G("Custom") ? var0.R.getValue() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer z() {
      CustomSkyModule var0 = CustomSkyModule.M();
      if (CustomSkyModule.h()) {
         return var0.z() ? var0.Y().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.y();
      return var1 != null && var1.g() ? var1.u() : null;
   }

   static float L(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean B() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !X) {
            X = (boolean)1;
            q8.G("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            X = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!X) {
            X = (boolean)1;
            q8.L("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void X() {
      a = (boolean)0;

      try {
         for (int var0 = 0; var0 < R.length; var0++) {
            GpuBuffer var1 = R[var0];
            R[var0] = null;
            if (var1 != null) {
               var1.close();
            }
         }
      } finally {
         o();
      }
   }

   private sm() {
   }

   enum y {
      x(0, 0, "end of the main pass"),
      u(0, 0, "before translucent terrain"),
      L(1, 1, "translucent surfaces");

      final int r;
      final int R;
      final String l;

      y(int var3, int var4, String var5) {
         this.r = var3;
         this.R = var4;
         this.l = var5;
      }
   }
}
