package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.GpuBuffer.MappedView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.minecraft.class_11228;
import net.minecraft.class_11233;
import net.minecraft.class_11234;
import net.minecraft.class_11235;
import net.minecraft.class_11236;
import net.minecraft.class_11237;
import net.minecraft.class_11238;
import net.minecraft.class_11246;
import net.minecraft.class_12075;
import net.minecraft.class_12137;
import net.minecraft.class_12160;
import net.minecraft.class_156;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_6367;
import net.minecraft.class_11246.class_11325;
import net.minecraft.class_12185.class_12186;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;

final class qz {
   private static final int d = 0;
   private static final double q = 1.0;
   private static final int l = 3;
   static final RenderPipeline w = s("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline v = s("streamproof_pack_interface", "Interface");
   static final RenderPipeline P = s("streamproof_opaque_blur", "Source");
   static final RenderPipeline k = G("streamproof_hand_cover", i(), "Scene", "Depth");
   static final RenderPipeline o = G("streamproof_sky", i(), "Sky", "Depth");
   private final class_11246 z = new class_11246();
   private class_11228 c;
   private class_6367 t;
   private class_6367 X;
   private class_6367 E;
   private class_6367 B;
   private class_6367 L;
   private class_6367 j;
   private boolean n;
   private GpuBufferSlice a;
   private long y = -1L;
   private final qz.a[] Q = new qz.a[3];
   private boolean T;
   private boolean Z;
   private boolean D;
   private boolean J;
   private long G = -1L;
   private long s = -1L;
   private int C;

   private static BlendFunction i() {
      return new BlendFunction(SourceFactor.ONE_MINUS_DST_ALPHA, DestFactor.ONE, SourceFactor.ONE_MINUS_DST_ALPHA, DestFactor.ONE);
   }

   class_11246 W() {
      return this.z;
   }

   boolean T(Object var1) {
      return var1 != null && var1 == this.c;
   }

   void d(GpuBufferSlice var1) {
      this.a = var1;
   }

   class_276 q() {
      return this.t;
   }

   boolean N() {
      return this.Z;
   }

   boolean D() {
      return this.C > 0;
   }

   boolean m(long var1) {
      return this.G == var1;
   }

   class_276 O() {
      return this.C > 0 ? this.X : null;
   }

   boolean G() {
      boolean[] var1 = new boolean[]{false};
      this.z.method_71060(var1x -> var1[0] = (boolean)1, class_11325.field_60315);
      if (!var1[0]) {
         this.z.method_71068(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.z.method_71064(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.z.method_71071(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void d(long var1) {
      if (this.G != var1) {
         class_276 var3 = class_310.method_1551().method_1522();
         this.X = m(this.X, "Larp Stream Proof effects", var3.field_1482, var3.field_1481, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.X.method_30277(), 0);
         this.G = var1;
         this.D = (boolean)0;
         this.J = (boolean)0;
      }
   }

   class_276 k(long var1) {
      this.d(var1);
      this.i();
      return this.X;
   }

   private void i() {
      if (!this.D) {
         this.X.method_29329(class_310.method_1551().method_1522());
         this.D = (boolean)1;
      }
   }

   class_276 v(long var1) {
      this.d(var1);
      class_276 var3 = class_310.method_1551().method_1522();
      this.E = m(this.E, "Larp Stream Proof sky", var3.field_1482, var3.field_1481, true);
      if (!this.J) {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.E.method_30277(), 0, this.E.method_30278(), 1.0);
         this.J = (boolean)1;
      }

      return this.E;
   }

   void H(long var1, boolean var3) {
      if (this.J && this.G == var1 && !this.Z) {
         class_276 var4 = class_310.method_1551().method_1522();
         this.i();
         Object var5 = var3 ? this.X : var4;

         try {
            this.q();
            class_12137 var6 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.X.method_71639(), OptionalInt.empty());

            try {
               yf.l(var7, o);
               yf.R(var7, "Sky", this.E.method_71639(), var6);
               yf.R(var7, "Depth", var5.method_71640(), var6);
               var7.draw(0, 3);
            } catch (Throwable var11) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var7 != null) {
               var7.close();
            }
         } catch (RuntimeException var12) {
            this.S("sky", var12);
         }
      }
   }

   void g(long var1) {
      if (this.G == var1 && !this.Z) {
         class_276 var3 = class_310.method_1551().method_1522();

         try {
            this.L = m(this.L, "Larp Stream Proof hand backdrop", var3.field_1482, var3.field_1481, false);
            t_.L(f2.f, var3, this.L);
         } catch (RuntimeException var5) {
            this.S("hand backdrop", var5);
         }
      }
   }

   void P(long var1) {
      if (this.G == var1 && !this.Z && this.L != null) {
         class_276 var3 = class_310.method_1551().method_1522();

         try {
            this.q();
            class_12137 var4 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.X.method_71639(), OptionalInt.empty());

            try {
               yf.l(var6, k);
               yf.R(var6, "Scene", this.L.method_71639(), var4);
               yf.R(var6, "Depth", var3.method_71640(), var4);
               var6.draw(0, 3);
            } catch (Throwable var10) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var6 != null) {
               var6.close();
            }

            var5.clearDepthTexture(var3.method_30278(), 1.0);
         } catch (RuntimeException var11) {
            this.S("hand cover", var11);
         }
      }
   }

   void m(long var1, Runnable var3) {
      if (this.G == var1 && !this.Z) {
         class_276 var4 = class_310.method_1551().method_1522();
         GpuTextureView var5 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var6 = RenderSystem.outputDepthTextureOverride;
         RenderSystem.outputColorTextureOverride = this.X.method_71639();
         RenderSystem.outputDepthTextureOverride = var4.method_71640();

         try {
            var3.run();
         } finally {
            RenderSystem.outputColorTextureOverride = var5;
            RenderSystem.outputDepthTextureOverride = var6;
         }
      } else {
         var3.run();
      }
   }

   void b(boolean var1) {
      this.n = var1;
   }

   class_276 I(class_276 var1) {
      return (class_276)(this.n && this.X != null ? this.X : var1);
   }

   void M(long var1) {
      this.d(var1);
      class_276 var3 = class_310.method_1551().method_1522();
      this.B = m(this.B, "Larp Stream Proof self depth", var3.field_1482, var3.field_1481, true);
      this.B.method_29329(var3);
      this.y = var1;
   }

   void h(long var1, Runnable var3) {
      if (this.y == var1 && this.B != null) {
         this.d(var1);
         GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
         class_276 var6 = t_.X;
         RenderSystem.outputColorTextureOverride = this.X.method_71639();
         RenderSystem.outputDepthTextureOverride = this.B.method_71640();
         t_.X = this.X;
         this.C++;

         try {
            var3.run();
         } finally {
            this.C--;
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            t_.X = var6;
         }
      } else {
         this.B(var1, var3);
      }
   }

   void B(long var1, Runnable var3) {
      this.d(var1);
      this.i();
      GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
      class_276 var6 = t_.X;
      RenderSystem.outputColorTextureOverride = this.X.method_71639();
      RenderSystem.outputDepthTextureOverride = this.X.method_71640();
      t_.X = this.X;
      this.C++;

      try {
         var3.run();
      } finally {
         this.C--;
         RenderSystem.outputColorTextureOverride = var4;
         RenderSystem.outputDepthTextureOverride = var5;
         t_.X = var6;
      }
   }

   void Y(Runnable var1) {
      if (this.C == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
         class_276 var4 = t_.X;
         int var5 = this.C;
         RenderSystem.outputColorTextureOverride = null;
         RenderSystem.outputDepthTextureOverride = null;
         t_.X = null;
         this.C = 0;

         try {
            var1.run();
         } finally {
            this.C = var5;
            RenderSystem.outputColorTextureOverride = var2;
            RenderSystem.outputDepthTextureOverride = var3;
            t_.X = var4;
         }
      }
   }

   boolean o(long var1, long var3, boolean var5, Consumer<class_276> var6, qz.j var7) {
      if (this.Z) {
         this.z.method_70926();
         return false;
      }

      class_276 var8 = class_310.method_1551().method_1522();
      String var9 = "resources";
      long var10 = c5.X();

      try {
         this.q();
         int var12 = var8.field_1482;
         int var13 = var8.field_1481;
         this.t = m(this.t, "Larp Stream Proof interface", var12, var13, true);
         this.j = m(this.j, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.t.method_30277(), 0, this.t.method_30278(), 1.0);
         var9 = "interface blur";
         var6.accept(this.t);
         var9 = "interface GUI";
         if (this.a == null) {
            throw new IllegalStateException("the vanilla GUI fog buffer was not captured this frame");
         }

         this.r().method_70890(this.a);
         this.r().method_70879();
         var9 = "compose";
         int var15 = var5 && this.G == var1 && this.X != null && this.X.field_1482 == var12 && this.X.field_1481 == var13 ? 1 : 0;
         class_12137 var16 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.j.method_71639(), OptionalInt.empty());

         try {
            yf.l(var17, var15 != 0 ? w : v);
            yf.R(var17, "Interface", this.t.method_71639(), var16);
            if (var15 != 0) {
               yf.R(var17, "Effects", this.X.method_71639(), var16);
            }

            var17.draw(0, 3);
         } catch (Throwable var26) {
            if (var17 != null) {
               try {
                  var17.close();
               } catch (Throwable var25) {
                  var26.addSuppressed(var25);
               }
            }

            throw var26;
         }

         if (var17 != null) {
            var17.close();
         }

         var9 = "read-back";
         return this.N(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.z.method_70926();
         this.S(var9, var27);
         return false;
      } finally {
         c5.w("Stream Proof compose", var10);
      }
   }

   void O() {
      this.z.method_70926();
   }

   void n(class_276 var1) {
      if (t_.i()) {
         this.l(var1, null);
      } else {
         for (int[] var3 : t_.m()) {
            this.l(var1, var3);
         }
      }
   }

   boolean J() {
      if (this.t != null && !this.Z) {
         this.l(this.t, null);
         return true;
      } else {
         return false;
      }
   }

   private void l(class_276 var1, int[] var2) {
      GpuTextureView var3 = t_.I();
      if (var3 != null) {
         this.q();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.method_71639(), OptionalInt.empty());

         try {
            yf.l(var4, P);
            yf.R(var4, "Source", var3, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
            if (var2 != null) {
               int var5 = class_310.method_1551().method_22683().method_4495();
               int var6 = var2[0];
               int var7 = var2[1];
               int var8 = var2[2];
               int var9 = var2[3];
               var4.enableScissor(
                  Math.max(0, var6 * var5),
                  Math.max(0, var1.field_1481 - (var7 + var9) * var5),
                  Math.max(0, Math.min(var8 * var5, var1.field_1482 - var6 * var5)),
                  Math.max(0, Math.min(var9 * var5, var1.field_1481 - var7 * var5))
               );
            }

            var4.draw(0, 3);
         } catch (Throwable var11) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (var4 != null) {
            var4.close();
         }
      }
   }

   void S(List<class_12186> var1, class_12075 var2, class_4587 var3, Matrix4f var4) {
      if (!this.Z && !var1.isEmpty()) {
         long var5 = c5.X();

         try {
            long var7 = class_156.method_658();
            class_12160 var9 = new class_12160();
            class_12160 var10 = new class_12160();

            for (class_12186 var12 : var1) {
               var12.method_75563().method_75531(var12.method_75562() ? var10 : var9, var12.method_75857(var7));
            }

            class_4598 var18 = class_310.method_1551().method_22940().method_23000();
            var9.method_75477(var3, var18, var2, var4);
            var18.method_37104();
            if (!var10.method_76659()) {
               RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.X.method_30278(), 1.0);
               var10.method_75477(var3, var18, var2, var4);
               var18.method_37104();
            }
         } catch (RuntimeException var16) {
            this.S("gizmo overlay", var16);
         } finally {
            c5.w("Stream Proof gizmos", var5);
         }
      }
   }

   private boolean N(long var1, int var3, int var4, qz.j var5) {
      qz.a var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.Q[var7] == null) {
            this.Q[var7] = new qz.a();
         }

         qz.a var8 = this.Q[var7];
         if (!var8.d) {
            if (var8.U == null || var8.R != var3 || var8.X != var4) {
               if (var8.U != null) {
                  var8.U.close();
               }

               var8.U = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.R = var3;
               var8.X = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         c5.V("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         qz.a var9 = var6;
         var9.d = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.j.method_30277(), var9.U, 0L, () -> {
            try {
               if (!var9.e && var1 > this.s) {
                  MappedView var7x = RenderSystem.getDevice().createCommandEncoder().mapBuffer(var9.U, true, false);

                  try {
                     var5.p(var1, var3, var4, var7x.data());
                  } catch (Throwable var16) {
                     if (var7x != null) {
                        try {
                           var7x.close();
                        } catch (Throwable var15) {
                           var16.addSuppressed(var15);
                        }
                     }

                     throw var16;
                  }

                  if (var7x != null) {
                     var7x.close();
                  }

                  this.s = var1;
               }
            } catch (RuntimeException var17) {
               q8.O("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.d = (boolean)0;
               if (var9.e && var9.U != null) {
                  var9.U.close();
                  var9.U = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private class_11228 r() {
      if (this.c == null) {
         class_310 var1 = class_310.method_1551();
         class_4598 var2 = var1.method_22940().method_23000();
         this.c = new class_11228(
            this.z,
            var2,
            var1.field_1773.method_72910(),
            var1.field_1773.method_72911(),
            List.of(
               new class_11235(var2, var1.method_1561()),
               new class_11238(var2),
               new class_11234(var2),
               new class_11233(var2, var1.method_72703()),
               new class_11237(var2, var1.method_72703()),
               new class_11236(var2)
            )
         );
         q8.v("StreamProof", "Interface GUI renderer created");
      }

      return this.c;
   }

   private void q() {
      if (!this.T) {
         for (RenderPipeline var4 : new RenderPipeline[]{w, v, P, k, o}) {
            f2.a(var4);
         }

         this.T = (boolean)1;
         q8.G("StreamProof", "Layer pipelines compiled");
      }
   }

   private void S(String var1, RuntimeException var2) {
      this.Z = (boolean)1;
      q8.u("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static class_6367 m(class_6367 var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.field_1482 == var2 && var0.field_1481 == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.method_1238();
      }

      return new class_6367(var1, var2, var3, var4);
   }

   void z() {
      this.z.method_70926();
      if (this.c != null) {
         this.c.close();
         this.c = null;
      }

      for (class_6367 var4 : new class_6367[]{this.t, this.X, this.E, this.B, this.L, this.j}) {
         if (var4 != null) {
            var4.method_1238();
         }
      }

      this.t = this.X = this.E = this.B = this.L = this.j = null;
      this.n = (boolean)0;
      this.a = null;
      this.y = -1L;
      this.D = this.J = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         qz.a var6 = this.Q[var5];
         if (var6 != null) {
            if (var6.d) {
               var6.e = (boolean)1;
            } else if (var6.U != null) {
               var6.U.close();
            }

            this.Q[var5] = null;
         }
      }

      this.G = -1L;
      this.s = -1L;
      this.C = 0;
      this.Z = (boolean)0;
      this.T = (boolean)0;
   }

   private static RenderPipeline s(String var0, String... var1) {
      return G(var0, null, var1);
   }

   private static RenderPipeline G(String var0, BlendFunction var1, String... var2) {
      Builder var3 = RenderPipeline.builder(new Snippet[0])
         .withLocation(class_2960.method_60655("larp-client", var0))
         .withVertexShader(class_2960.method_60655("larp-client", "post/fullscreen"))
         .withFragmentShader(class_2960.method_60655("larp-client", "post/" + var0));

      for (String var7 : var2) {
         var3.withSampler(var7);
      }

      if (var1 != null) {
         var3.withBlend(var1);
      } else {
         var3.withoutBlend();
      }

      return var3.withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private static final class a {
      GpuBuffer U;
      int R;
      int X;
      boolean d;
      boolean e;
   }

   interface j {
      void p(long var1, int var3, int var4, ByteBuffer var5);
   }
}
