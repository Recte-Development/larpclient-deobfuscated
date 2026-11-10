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

public final class nr {
   private static nr.@Nullable y E;
   private static boolean V;

   public static RenderPass p(RenderPass var0, Supplier<String> var1, GpuTextureView var2, @Nullable GpuTextureView var3) {
      nr.y var4 = new nr.y(var0, var1, var2, var3);
      E = var4;
      return (RenderPass)Proxy.newProxyInstance(RenderPass.class.getClassLoader(), new Class[]{RenderPass.class}, var4);
   }

   public static boolean l() {
      nr.y var0 = E;
      if (var0 != null && var0.h != null) {
         var0.f();
         if (!V) {
            V = (boolean)1;
            _q.z("Render", "Main level pass splits around Larp commands; vanilla resumes on the same attachments");
         }

         return true;
      } else {
         return false;
      }
   }

   private nr() {
   }

   private static final class y implements InvocationHandler {
      private final Supplier<String> f;
      private final GpuTextureView v;
      private final @Nullable GpuTextureView R;
      private @Nullable RenderPass h;
      private boolean e;
      private Object @Nullable [] W;
      private Object @Nullable [] w;
      private Object @Nullable [] k;
      private @Nullable ByteBuffer L;
      private final Map<String, Object[]> B = new LinkedHashMap<>();
      private final Map<Integer, Object> g = new LinkedHashMap<>();
      private final ArrayDeque<Supplier<String>> t = new ArrayDeque<>();

      y(RenderPass var1, Supplier<String> var2, GpuTextureView var3, @Nullable GpuTextureView var4) {
         this.h = var1;
         this.f = var2;
         this.v = var3;
         this.R = var4;
      }

      void f() {
         RenderPass var1 = this.h;
         if (var1 != null) {
            this.h = null;

            for (int var2 = 0; var2 < this.t.size(); var2++) {
               var1.popDebugGroup();
            }

            var1.close();
         }
      }

      private RenderPass X() {
         RenderPass var1 = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.f, this.v, Optional.empty(), this.R, OptionalDouble.empty());
         this.h = var1;

         for (Supplier var3 : this.t.reversed()) {
            var1.pushDebugGroup(var3);
         }

         if (this.W != null) {
            var1.setPipeline((CompiledRenderPipeline)this.W[0]);
         }

         for (Object[] var5 : this.B.values()) {
            x(var1, var5);
         }

         this.g.forEach((var1x, var2) -> var1.setVertexBuffer(var1x, (GpuBufferSlice)var2));
         if (this.w != null) {
            var1.setIndexBuffer((GpuBuffer)this.w[0], (IndexType)this.w[1]);
         }

         if (this.k != null) {
            var1.enableScissor((Integer)this.k[0], (Integer)this.k[1], (Integer)this.k[2], (Integer)this.k[3]);
         }

         if (this.L != null) {
            var1.pushConstants(this.L.duplicate());
         }

         return var1;
      }

      private static void x(RenderPass var0, Object[] var1) {
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
               default -> "MainPass[" + (String)this.f.get() + "]";
            };
         } else if (var4.equals("close")) {
            if (this.e) {
               return null;
            }

            this.e = (boolean)1;
            if (nr.E == this) {
               nr.E = null;
            }

            RenderPass var9 = this.h;
            this.h = null;
            if (var9 != null) {
               var9.close();
            }

            return null;
         } else {
            RenderPass var5 = this.h != null ? this.h : this.X();

            Object var6;
            try {
               var6 = var2.invoke(var5, var3);
            } catch (InvocationTargetException var8) {
               throw var8.getCause();
            }

            this.J(var4, var3);
            return var6;
         }
      }

      private void J(String var1, Object @Nullable [] var2) {
         switch (var1) {
            case "setPipeline":
               this.W = (Object[])var2.clone();
               break;
            case "setUniform":
               this.B.put((String)var2[0], (Object[])var2.clone());
               break;
            case "setVertexBuffer":
               this.g.put((Integer)var2[0], var2[1]);
               break;
            case "setIndexBuffer":
               this.w = (Object[])var2.clone();
               break;
            case "enableScissor":
               this.k = (Object[])var2.clone();
               break;
            case "disableScissor":
               this.k = null;
               break;
            case "pushDebugGroup":
               this.t.push(E(var2[0]));
               break;
            case "popDebugGroup":
               if (!this.t.isEmpty()) {
                  this.t.pop();
               }
               break;
            case "pushConstants":
               ByteBuffer var5 = ((ByteBuffer)var2[0]).duplicate();
               ByteBuffer var6 = ByteBuffer.allocateDirect(var5.remaining()).order(var5.order());
               var6.put(var5).flip();
               this.L = var6;
         }
      }

      private static Supplier<String> E(Object var0) {
         return (Supplier<String>)var0;
      }
   }
}
