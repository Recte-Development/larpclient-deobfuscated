package com.larpclient.render.overlay;

import com.larpclient.util.misc.LarpLog;
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

public final class MainPassSplitter {
   private static MainPassSplitter.@Nullable RenderPassProxyHandler activeHandler;
   private static boolean loggedSplit;

   public static RenderPass wrap(RenderPass var0, Supplier<String> var1, GpuTextureView var2, @Nullable GpuTextureView var3) {
      MainPassSplitter.RenderPassProxyHandler var4 = new MainPassSplitter.RenderPassProxyHandler(var0, var1, var2, var3);
      activeHandler = var4;
      return (RenderPass)Proxy.newProxyInstance(RenderPass.class.getClassLoader(), new Class[]{RenderPass.class}, var4);
   }

   public static boolean suspendMainPass() {
      MainPassSplitter.RenderPassProxyHandler var0 = activeHandler;
      if (var0 != null && var0.pass != null) {
         var0.suspend();
         if (!loggedSplit) {
            loggedSplit = (boolean)1;
            LarpLog.info("Render", "Main level pass splits around Larp commands; vanilla resumes on the same attachments");
         }

         return true;
      } else {
         return false;
      }
   }

   private MainPassSplitter() {
   }

   private static final class RenderPassProxyHandler implements InvocationHandler {
      private final Supplier<String> label;
      private final GpuTextureView colorTarget;
      private final @Nullable GpuTextureView depthTarget;
      private @Nullable RenderPass pass;
      private boolean closed;
      private Object @Nullable [] pipelineArgs;
      private Object @Nullable [] indexBufferArgs;
      private Object @Nullable [] scissorArgs;
      private @Nullable ByteBuffer pushConstants;
      private final Map<String, Object[]> uniforms = new LinkedHashMap<>();
      private final Map<Integer, Object> vertexBuffers = new LinkedHashMap<>();
      private final ArrayDeque<Supplier<String>> debugGroups = new ArrayDeque<>();

      RenderPassProxyHandler(RenderPass var1, Supplier<String> var2, GpuTextureView var3, @Nullable GpuTextureView var4) {
         this.pass = var1;
         this.label = var2;
         this.colorTarget = var3;
         this.depthTarget = var4;
      }

      void suspend() {
         RenderPass var1 = this.pass;
         if (var1 != null) {
            this.pass = null;

            for (int var2 = 0; var2 < this.debugGroups.size(); var2++) {
               var1.popDebugGroup();
            }

            var1.close();
         }
      }

      private RenderPass reopenPass() {
         RenderPass var1 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(this.label, this.colorTarget, Optional.empty(), this.depthTarget, OptionalDouble.empty());
         this.pass = var1;

         for (Supplier var3 : this.debugGroups.reversed()) {
            var1.pushDebugGroup(var3);
         }

         if (this.pipelineArgs != null) {
            var1.setPipeline((CompiledRenderPipeline)this.pipelineArgs[0]);
         }

         for (Object[] var5 : this.uniforms.values()) {
            replayUniform(var1, var5);
         }

         this.vertexBuffers.forEach((var1x, var2) -> var1.setVertexBuffer(var1x, (GpuBufferSlice)var2));
         if (this.indexBufferArgs != null) {
            var1.setIndexBuffer((GpuBuffer)this.indexBufferArgs[0], (IndexType)this.indexBufferArgs[1]);
         }

         if (this.scissorArgs != null) {
            var1.enableScissor((Integer)this.scissorArgs[0], (Integer)this.scissorArgs[1], (Integer)this.scissorArgs[2], (Integer)this.scissorArgs[3]);
         }

         if (this.pushConstants != null) {
            var1.pushConstants(this.pushConstants.duplicate());
         }

         return var1;
      }

      private static void replayUniform(RenderPass var0, Object[] var1) {
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
               default -> "MainPass[" + (String)this.label.get() + "]";
            };
         } else if (var4.equals("close")) {
            if (this.closed) {
               return null;
            }

            this.closed = (boolean)1;
            if (MainPassSplitter.activeHandler == this) {
               MainPassSplitter.activeHandler = null;
            }

            RenderPass var9 = this.pass;
            this.pass = null;
            if (var9 != null) {
               var9.close();
            }

            return null;
         } else {
            RenderPass var5 = this.pass != null ? this.pass : this.reopenPass();

            Object var6;
            try {
               var6 = var2.invoke(var5, var3);
            } catch (InvocationTargetException var8) {
               throw var8.getCause();
            }

            this.trackState(var4, var3);
            return var6;
         }
      }

      private void trackState(String var1, Object @Nullable [] var2) {
         switch (var1) {
            case "setPipeline":
               this.pipelineArgs = (Object[])var2.clone();
               break;
            case "setUniform":
               this.uniforms.put((String)var2[0], (Object[])var2.clone());
               break;
            case "setVertexBuffer":
               this.vertexBuffers.put((Integer)var2[0], var2[1]);
               break;
            case "setIndexBuffer":
               this.indexBufferArgs = (Object[])var2.clone();
               break;
            case "enableScissor":
               this.scissorArgs = (Object[])var2.clone();
               break;
            case "disableScissor":
               this.scissorArgs = null;
               break;
            case "pushDebugGroup":
               this.debugGroups.push(asSupplier(var2[0]));
               break;
            case "popDebugGroup":
               if (!this.debugGroups.isEmpty()) {
                  this.debugGroups.pop();
               }
               break;
            case "pushConstants":
               ByteBuffer var5 = ((ByteBuffer)var2[0]).duplicate();
               ByteBuffer var6 = ByteBuffer.allocateDirect(var5.remaining()).order(var5.order());
               var6.put(var5).flip();
               this.pushConstants = var6;
         }
      }

      private static Supplier<String> asSupplier(Object var0) {
         return (Supplier<String>)var0;
      }
   }
}
