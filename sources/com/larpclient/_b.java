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

public final class _b {
   private static _b.@Nullable f w;
   private static boolean d;

   public static RenderPass r(RenderPass var0, Supplier<String> var1, GpuTextureView var2, @Nullable GpuTextureView var3) {
      _b.f var4 = new _b.f(var0, var1, var2, var3);
      w = var4;
      return (RenderPass)Proxy.newProxyInstance(RenderPass.class.getClassLoader(), new Class[]{RenderPass.class}, var4);
   }

   public static boolean j() {
      _b.f var0 = w;
      if (var0 != null && var0.i != null) {
         var0.O();
         if (!d) {
            d = (boolean)1;
            r4.V("Render", "Main level pass splits around Larp commands; vanilla resumes on the same attachments");
         }

         return true;
      } else {
         return false;
      }
   }

   private _b() {
   }

   private static final class f implements InvocationHandler {
      private final Supplier<String> H;
      private final GpuTextureView Q;
      private final @Nullable GpuTextureView s;
      private @Nullable RenderPass i;
      private boolean x;
      private Object @Nullable [] D;
      private Object @Nullable [] A;
      private Object @Nullable [] r;
      private @Nullable ByteBuffer a;
      private final Map<String, Object[]> u = new LinkedHashMap<>();
      private final Map<Integer, Object> B = new LinkedHashMap<>();
      private final ArrayDeque<Supplier<String>> F = new ArrayDeque<>();

      f(RenderPass var1, Supplier<String> var2, GpuTextureView var3, @Nullable GpuTextureView var4) {
         this.i = var1;
         this.H = var2;
         this.Q = var3;
         this.s = var4;
      }

      void O() {
         RenderPass var1 = this.i;
         if (var1 != null) {
            this.i = null;

            for (int var2 = 0; var2 < this.F.size(); var2++) {
               var1.popDebugGroup();
            }

            var1.close();
         }
      }

      private RenderPass W() {
         RenderPass var1 = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.H, this.Q, Optional.empty(), this.s, OptionalDouble.empty());
         this.i = var1;

         for (Supplier var3 : this.F.reversed()) {
            var1.pushDebugGroup(var3);
         }

         if (this.D != null) {
            var1.setPipeline((CompiledRenderPipeline)this.D[0]);
         }

         for (Object[] var5 : this.u.values()) {
            A(var1, var5);
         }

         this.B.forEach((var1x, var2) -> var1.setVertexBuffer(var1x, (GpuBufferSlice)var2));
         if (this.A != null) {
            var1.setIndexBuffer((GpuBuffer)this.A[0], (IndexType)this.A[1]);
         }

         if (this.r != null) {
            var1.enableScissor((Integer)this.r[0], (Integer)this.r[1], (Integer)this.r[2], (Integer)this.r[3]);
         }

         if (this.a != null) {
            var1.pushConstants(this.a.duplicate());
         }

         return var1;
      }

      private static void A(RenderPass var0, Object[] var1) {
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
               default -> "MainPass[" + (String)this.H.get() + "]";
            };
         } else if (var4.equals("close")) {
            if (this.x) {
               return null;
            }

            this.x = (boolean)1;
            if (_b.w == this) {
               _b.w = null;
            }

            RenderPass var9 = this.i;
            this.i = null;
            if (var9 != null) {
               var9.close();
            }

            return null;
         } else {
            RenderPass var5 = this.i != null ? this.i : this.W();

            Object var6;
            try {
               var6 = var2.invoke(var5, var3);
            } catch (InvocationTargetException var8) {
               throw var8.getCause();
            }

            this.W(var4, var3);
            return var6;
         }
      }

      private void W(String var1, Object @Nullable [] var2) {
         switch (var1) {
            case "setPipeline":
               this.D = (Object[])var2.clone();
               break;
            case "setUniform":
               this.u.put((String)var2[0], (Object[])var2.clone());
               break;
            case "setVertexBuffer":
               this.B.put((Integer)var2[0], var2[1]);
               break;
            case "setIndexBuffer":
               this.A = (Object[])var2.clone();
               break;
            case "enableScissor":
               this.r = (Object[])var2.clone();
               break;
            case "disableScissor":
               this.r = null;
               break;
            case "pushDebugGroup":
               this.F.push(G(var2[0]));
               break;
            case "popDebugGroup":
               if (!this.F.isEmpty()) {
                  this.F.pop();
               }
               break;
            case "pushConstants":
               ByteBuffer var5 = ((ByteBuffer)var2[0]).duplicate();
               ByteBuffer var6 = ByteBuffer.allocateDirect(var5.remaining()).order(var5.order());
               var6.put(var5).flip();
               this.a = var6;
         }
      }

      private static Supplier<String> G(Object var0) {
         return (Supplier<String>)var0;
      }
   }
}
