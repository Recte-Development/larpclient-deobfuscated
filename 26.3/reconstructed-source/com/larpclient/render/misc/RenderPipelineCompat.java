package com.larpclient.render.misc;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline.Pending;
import com.mojang.renderpearl.api.pipeline.ShaderSource.CachedIncludeSource;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class RenderPipelineCompat {
   public static final UniformType sAMPLER_UNIFORM_TYPE = UniformType.COMBINED_IMAGE_SAMPLER;
   private static final Map<RenderPipeline, CompiledRenderPipeline> compiledPipelines = new IdentityHashMap<>();

   public static void setSamplerUniform(RenderPass var0, String var1, GpuTextureView var2, GpuSampler var3) {
      var0.setUniform(var1, var2, var3);
   }

   public static CompiledRenderPipeline getCompiledPipeline(RenderPipeline var0) {
      CompiledRenderPipeline var1 = compiledPipelines.get(var0);
      return var1 != null && !var1.isClosed() ? var1 : RenderSystem.getCompiledPipeline(var0);
   }

   public static boolean compilePipeline(RenderPipeline var0, @Nullable ShaderSource var1) {
      if (var1 == null) {
         return RenderSystem.getCompiledPipelineNullable(var0) != null;
      }

      CompiledRenderPipeline var2 = ((Pending)RenderSystem.getDevice().compilePipeline(var0, var1, Runnable::run).join()).finishCompile();
      if (var2 == null) {
         return false;
      }

      CompiledRenderPipeline var3 = compiledPipelines.put(var0, var2);
      if (var3 != null && var3 != var2) {
         var3.close();
      }

      return true;
   }

   public static void setPipeline(RenderPass var0, RenderPipeline var1) {
      var0.setPipeline(getCompiledPipeline(var1));
   }

   public static Identifier getVertexShaderId(RenderPipeline var0) {
      return (Identifier)var0.getShaders().get(ShaderType.VERTEX);
   }

   public static Identifier getFragmentShaderId(RenderPipeline var0) {
      return (Identifier)var0.getShaders().get(ShaderType.FRAGMENT);
   }

   public static void drawImmediate(PreparedRenderType var0, ExecuteInfo var1) {
      RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      int var3 = var0.pipeline().getDepthStencilState() != null ? 1 : 0;
      RenderPass var4 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(
            () -> "Larp immediate " + var0.name(),
            var2.getColorTextureView(),
            Optional.empty(),
            var3 != 0 ? var2.getDepthTextureView() : null,
            OptionalDouble.empty()
         );

      try {
         var0.drawFromBuffer(var1, var4);
      } catch (Throwable var8) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (var4 != null) {
         var4.close();
      }
   }

   public static TextureTarget createTextureTarget(String var0, int var1, int var2, boolean var3, GpuFormat var4) {
      return new TextureTarget(var0, var1, var2, var4, var3 ? GpuFormat.D32_FLOAT : null);
   }

   public static ShaderSource getBaseShaderSource() {
      try {
         ShaderManager var0 = Minecraft.getInstance().getShaderManager();
         Field var1 = ShaderManager.class.getDeclaredField("postChains");
         var1.setAccessible(true);
         Object var2 = var1.get(var0);
         Field var3 = var2.getClass().getDeclaredField("configs");
         var3.setAccessible(true);
         return (ShaderSource)var3.get(var2);
      } catch (ReflectiveOperationException var4) {
         throw new IllegalStateException("26.3 shader sources are unavailable", var4);
      }
   }

   public static ShaderSource createShaderSource(final BiFunction<Identifier, ShaderType, String> var0) {
      final ShaderSource var1 = getBaseShaderSource();
      return new ShaderSource() {
         public @Nullable String getShader(Identifier var1x, ShaderType var2) {
            return (String)var0.apply(var1x, var2);
         }

         public @Nullable CachedIncludeSource getInclude(Identifier var1x) {
            return var1.getInclude(var1x);
         }

         public void close() {
         }
      };
   }

   public static List<ColorTargetState> getColorTargetStates(RenderPipeline var0) {
      return var0.getColorTargetStates();
   }

   public static List<VertexFormat> getVertexFormatBindings(RenderPipeline var0) {
      return var0.getVertexFormatBindings();
   }

   public static PreparedRenderType createTextureTarget(PreparedRenderType var0, RenderPipeline var1) {
      return new PreparedRenderType(var0.name(), var1, null, var0.dynamicTransforms(), var0.scissorState(), var0.textures());
   }

   public static RenderTarget getCompiledPipeline(PreparedRenderType var0) {
      return Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static String getBackendDescription() {
      return RenderSystem.getBackendDescription();
   }

   public static boolean getVertexShaderId(RenderTarget var0) {
      return var0.hasDepth();
   }

   private RenderPipelineCompat() {
   }
}
