package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public final class ui {
   private static ui.@Nullable t r;
   private static boolean C;

   public static RenderPass b(RenderPass var0, Supplier<String> var1, GpuTextureView var2, @Nullable GpuTextureView var3) {
      ui.t var4 = new ui.t(var0, var1, var2, var3);
      r = var4;
      return (RenderPass)Proxy.newProxyInstance(RenderPass.class.getClassLoader(), new Class[]{RenderPass.class}, var4);
   }

   public static boolean B() {
      ui.t var0 = r;
      if (var0 != null && var0.M != null) {
         var0.I();
         if (!C) {
            C = (boolean)1;
            mt.K("Render", "Main level pass splits around Larp commands; vanilla resumes on the same attachments");
         }

         return true;
      } else {
         return false;
      }
   }

   private ui() {
   }

   private static final class t implements InvocationHandler {
      private final Supplier<String> o;
      private final GpuTextureView y;
      private final @Nullable GpuTextureView h;
      private @Nullable RenderPass M;
      private boolean v;
      private Object @Nullable [] P;
      private Object @Nullable [] B;
      private Object @Nullable [] f;
      private @Nullable ByteBuffer s;
      private final Map<String, Object[]> k = new LinkedHashMap<>();
      private final Map<Integer, Object> Y = new LinkedHashMap<>();
      private final ArrayDeque<Supplier<String>> H = new ArrayDeque<>();

      t(RenderPass var1, Supplier<String> var2, GpuTextureView var3, @Nullable GpuTextureView var4) {
         this.M = var1;
         this.o = var2;
         this.y = var3;
         this.h = var4;
      }

      void I() {
         RenderPass var1 = this.M;
         if (var1 != null) {
            this.M = null;

            for (int var2 = 0; var2 < this.H.size(); var2++) {
               var1.popDebugGroup();
            }

            var1.close();
         }
      }

      private RenderPass N() {
         RenderPass var1 = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.o, this.y, Optional.empty(), this.h, OptionalDouble.empty());
         this.M = var1;

         for (Supplier var3 : this.H.reversed()) {
            var1.pushDebugGroup(var3);
         }

         if (this.P != null) {
            var1.setPipeline((CompiledRenderPipeline)this.P[0]);
         }

         for (Object[] var5 : this.k.values()) {
            B(var1, var5);
         }

         this.Y.forEach((var1x, var2) -> var1.setVertexBuffer(var1x, (GpuBufferSlice)var2));
         if (this.B != null) {
            var1.setIndexBuffer((GpuBuffer)this.B[0], (IndexType)this.B[1]);
         }

         if (this.f != null) {
            var1.enableScissor((Integer)this.f[0], (Integer)this.f[1], (Integer)this.f[2], (Integer)this.f[3]);
         }

         if (this.s != null) {
            var1.pushConstants(this.s.duplicate());
         }

         return var1;
      }

      private static void B(RenderPass var0, Object[] var1) {
         String var2 = (String)var1[0];
         if (var1.length == 3) {
            var0.setUniform(var2, (GpuTextureView)var1[1], (GpuSampler)var1[2]);
         } else if (var1[1] instanceof GpuBufferSlice var3) {
            var0.setUniform(var2, var3);
         } else {
            var0.setUniform(var2, (GpuBuffer)var1[1]);
         }
      }

      @Override
      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         String var4 = var2.getName();
         if (var2.getDeclaringClass() == Object.class) {
            return switch (var4) {
               case "equals" -> var1 == var3[0];
               case "hashCode" -> System.identityHashCode(var1);
               default -> "MainPass[" + (String)this.o.get() + "]";
            };
         } else if (var4.equals("close")) {
            if (this.v) {
               return null;
            }

            this.v = (boolean)1;
            if (ui.r == this) {
               ui.r = null;
            }

            RenderPass var9 = this.M;
            this.M = null;
            if (var9 != null) {
               var9.close();
            }

            return null;
         } else {
            RenderPass var5 = this.M != null ? this.M : this.N();

            Object var6;
            try {
               var6 = var2.invoke(var5, var3);
            } catch (InvocationTargetException var8) {
               throw var8.getCause();
            }

            this.C(var4, var3);
            return var6;
         }
      }

      private void C(String var1, Object @Nullable [] var2) {
         switch (var1) {
            case "setPipeline":
               this.P = (Object[])var2.clone();
               break;
            case "setUniform":
               this.k.put((String)var2[0], (Object[])var2.clone());
               break;
            case "setVertexBuffer":
               this.Y.put((Integer)var2[0], var2[1]);
               break;
            case "setIndexBuffer":
               this.B = (Object[])var2.clone();
               break;
            case "enableScissor":
               this.f = (Object[])var2.clone();
               break;
            case "disableScissor":
               this.f = null;
               break;
            case "pushDebugGroup":
               this.H.push(f(var2[0]));
               break;
            case "popDebugGroup":
               if (!this.H.isEmpty()) {
                  this.H.pop();
               }
               break;
            case "pushConstants":
               ByteBuffer var5 = ((ByteBuffer)var2[0]).duplicate();
               ByteBuffer var6 = ByteBuffer.allocateDirect(var5.remaining()).order(var5.order());
               var6.put(var5).flip();
               this.s = var6;
         }
      }

      private static Supplier<String> f(Object var0) {
         return (Supplier<String>)var0;
      }
   }
}
