package com.larpclient;

import com.larpclient.module.EchoHandModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_6367;
import org.lwjgl.system.MemoryStack;

public final class jf {
   private static final RenderPipeline V = h("echo_hand", false);
   private static final RenderPipeline r = h("echo_hand", true);
   private static final RenderPipeline M = h("hand_flame", false);
   private static final RenderPipeline h = h("hand_flame", true);
   private static class_6367 W;
   private static class_6367 y;
   private static class_6367 G;
   private static class_6367 q;
   private static GpuBuffer u;
   private static int I = -1;
   private static boolean B;
   private static boolean d;
   private static boolean b;
   private static long c;
   private static Object Z;
   private static Object D;
   private static String O;
   private static double F;
   private static double J;
   private static double E;
   private static float g = 0.016666668F;
   private static final jf.w l = new jf.w();

   private static RenderPipeline h(String var0, boolean var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]);

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withSampler("Sampler" + var3);
      }

      var2.withUniform("EchoParameters", class_10789.field_60031);
      if (var1) {
         var2.withBlend(r(var0));
      }

      return var2.withLocation(F("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(F("core/echo_hand"))
         .withFragmentShader(F("core/" + var0))
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private static BlendFunction r(String var0) {
      return var0.equals("hand_flame") ? BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA : BlendFunction.LIGHTNING;
   }

   public static boolean W() {
      class_310 var0 = class_310.method_1551();
      EchoHandModule var1 = EchoHandModule.p();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.field_1687 != null
         && var0.field_1724 != null
         && var0.field_1690.method_31044().method_31034()
         && !var0.field_1690.field_1842
         && !var0.field_1724.method_6113()
         && !var0.field_1724.method_7325()
         && (var1.v() || var1.t.getValue())
         && (!var1.v() || !var1.a.getValue() || !var0.field_1724.method_6047().method_7960() || !var0.field_1724.method_6079().method_7960());
   }

   public static void l() {
      if (!W()) {
         o();
      } else if (Z != null && (Z != class_310.method_1551().field_1687 || D != class_310.method_1551().field_1724)) {
         o();
      }
   }

   public static boolean H() {
      if (I != t_.g) {
         o();
         I = t_.g;
         B = (boolean)0;
      }

      if (!W()) {
         o();
         return false;
      }

      if (B) {
         return false;
      }

      try {
         class_310 var0 = class_310.method_1551();
         EchoHandModule var1 = EchoHandModule.p();
         class_276 var2 = var0.method_1522();
         if (RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null) {
            int var3 = W != null
                  && W.field_1482 == var2.field_1482
                  && W.field_1481 == var2.field_1481
                  && Z == var0.field_1687
                  && D == var0.field_1724
                  && var1.e.getValue().equals(O)
                  && !(G(var0.field_1724.method_23317(), var0.field_1724.method_23318(), var0.field_1724.method_23321(), F, J, E) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               o();
               z(var2.field_1482, var2.field_1481);
               Z = var0.field_1687;
               D = var0.field_1724;
               O = var1.e.getValue();
            }

            F = var0.field_1724.method_23317();
            J = var0.field_1724.method_23318();
            E = var0.field_1724.method_23321();
            if (var0.method_1493() != b) {
               b = (boolean)(!b ? 1 : 0);
               q8.v("EchoHand", "Game {}; history keeps settling", b ? "paused" : "resumed");
            }

            float var4 = var0.field_1724.method_36454();
            float var5 = var0.field_1724.method_36455();
            float var6 = wp.R(var4);
            if (!Float.isNaN(var6)) {
               var4 = var6;
               var5 = wp.n();
            }

            l.E(var4, var5, var1.v() ? 1.0F : var1.b.getValue().floatValue());
            t_.L(f2.f, var2, W);
            W.method_29329(var2);
            d = (boolean)1;
            return true;
         } else {
            o();
            return false;
         }
      } catch (RuntimeException var7) {
         W("capture", var7);
         return false;
      }
   }

   public static void O() {
      if (d) {
         d = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.p();
            class_276 var1 = class_310.method_1551().method_1522();
            t_.L(f2.f, var1, y);
            y.method_29329(var1);
            long var2 = System.nanoTime();
            g = c == 0L ? 0.016666668F : (float)Math.clamp((var2 - c) / 1.0E9, 0.0, 0.1);
            c = var2;
            t(var0.v() ? M : V, q, var0, 0.0F);
            class_6367 var4 = G;
            G = q;
            q = var4;
            t(var0.v() ? h : r, var1, var0, 1.0F);
         } catch (RuntimeException var5) {
            W("history/composite", var5);
         }
      }
   }

   public static void x() {
      d = (boolean)0;
      o();
   }

   private static void z(int var0, int var1) {
      try {
         f2.a(V);
         f2.a(r);
         f2.a(M);
         f2.a(h);
         W = new class_6367("Larp EchoHand before", var0, var1, true);
         y = new class_6367("Larp EchoHand after", var0, var1, true);
         G = new class_6367("Larp EchoHand history", var0, var1, false);
         q = new class_6367("Larp EchoHand next", var0, var1, false);
         u = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(G.method_30277(), 0);
         var2.clearColorTexture(q.method_30277(), 0);
         q8.G("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         o();
         throw var3;
      }
   }

   private static void t(RenderPipeline var0, class_276 var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(W.field_1482)
            .putFloat(W.field_1481)
            .putFloat((var2.v() ? var2.D : var2.S).getValue().floatValue())
            .putFloat(var2.Q.getValue().floatValue());
         var6.putFloat(var2.m.getValue().floatValue()).putFloat(var2.b.getValue().floatValue()).putFloat(var3).putFloat((float)qo.x());
         var6.putFloat(l.d).putFloat(l.Z).putFloat(var2.A.getValue() ? 1.0F : 0.0F).putFloat(g);
         int var7 = var2.c();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.U.getValue().floatValue())
            .putFloat(var2.n.getValue().floatValue())
            .putFloat(var2.T.getValue().floatValue())
            .putFloat(var2.G.getValue().floatValue());
         var6.putFloat(var2.K.getValue().floatValue()).putFloat(var2.f.getValue().floatValue()).putFloat(var2.r.getValue().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(u.slice(), var6);
      } catch (Throwable var11) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (var5 != null) {
         var5.close();
      }

      RenderPass var12 = var4.createRenderPass(() -> "Larp EchoHand history/composite", var1.method_71639(), OptionalInt.empty());

      try {
         yf.l(var12, var0);
         var12.setUniform("EchoParameters", u.slice());
         class_12137 var13 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
         yf.R(var12, "Sampler0", y.method_71639(), var13);
         yf.R(var12, "Sampler1", W.method_71639(), var13);
         yf.R(var12, "Sampler2", G.method_71639(), var13);
         class_12137 var14 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
         yf.R(var12, "Sampler3", y.method_71640(), var14);
         yf.R(var12, "Sampler4", W.method_71640(), var14);
         var12.draw(0, 3);
      } catch (Throwable var10) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }
         }

         throw var10;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   private static void W(String var0, RuntimeException var1) {
      B = (boolean)1;
      o();
      q8.u("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void o() {
      class_6367[] var0 = new class_6367[]{W, y, G, q};
      q = null;
      G = null;
      y = null;
      W = null;
      GpuBuffer var1 = u;
      u = null;
      d = (boolean)0;
      D = null;
      Z = null;
      O = null;
      l.Z();
      c = 0L;
      RuntimeException var2 = null;

      for (class_6367 var6 : var0) {
         if (var6 != null) {
            try {
               var6.method_1238();
            } catch (RuntimeException var9) {
               if (var2 == null) {
                  var2 = var9;
               } else {
                  var2.addSuppressed(var9);
               }
            }
         }
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (RuntimeException var8) {
            if (var2 == null) {
               var2 = var8;
            } else {
               var2.addSuppressed(var8);
            }
         }
      }

      if (var2 != null) {
         q8.u("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double G(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static class_2960 F(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public static final class w {
      public float d;
      public float Z;
      private float w;
      private float t;
      private float n;
      private float O;
      private boolean T;

      public void E(float var1, float var2, float var3) {
         if (!this.T) {
            this.w = var1;
            this.t = var2;
            this.T = (boolean)1;
         } else {
            float var4 = class_3532.method_15393(var1 - this.w);
            float var5 = var2 - this.t;
            this.w = var1;
            this.t = var2;
            float var6 = class_3532.method_15363(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = class_3532.method_15363(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.n = (this.n + (var6 - this.d) * 0.18F) * 0.74F;
            this.O = (this.O + (var7 - this.Z) * 0.18F) * 0.74F;
            this.d = class_3532.method_15363((this.d + this.n) * 0.92F, -0.075F, 0.075F);
            this.Z = class_3532.method_15363((this.Z + this.O) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void Z() {
         this.T = (boolean)0;
         this.d = this.Z = this.w = this.t = this.n = this.O = 0.0F;
      }
   }
}
