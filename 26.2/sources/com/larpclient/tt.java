package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class tt {
   private static final int t = 512;
   private static final int I = 8240;
   private static final RenderPipeline b = RenderPipeline.builder(new Snippet[0])
      .withLocation(s("pipeline/temple/droplet_surface"))
      .withVertexShader(s("temple/droplet_surface"))
      .withFragmentShader(s("temple/droplet_surface"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withUniform("DropletBatch", UniformType.UNIFORM_BUFFER).build()
      )
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline P = RenderPipeline.builder(new Snippet[0])
      .withLocation(s("pipeline/temple/droplet_composite"))
      .withVertexShader(s("post/fullscreen"))
      .withFragmentShader(s("temple/droplet_composite"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
            .withSampler("SceneColor")
            .withSampler("SceneDepth")
            .withSampler("DropletMask")
            .withSampler("DropletDepth")
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static TextureTarget T;
   private static GpuBuffer n;

   public static void O() {
      if (b9.e() != 0 && w6.c()) {
         LevelRenderState var0 = w6.u();
         if (var0 != null) {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (T == null || T.width != var1.width || T.height != var1.height) {
               a();
               T = mx.n("Larp volumetric water surface", var1.width, var1.height, true, GpuFormat.RGBA8_UNORM);
            }

            if (n == null) {
               ai.U(b);
               ai.U(P);
               n = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               h.A("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            w6.c();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(T.getColorTexture(), new Vector4f(), T.getDepthTexture(), 0.0);
            var3.copyTextureToTexture(var1.getDepthTexture(), T.getDepthTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(var4.viewRotationMatrix).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = cf.l();

            for (short var9 = 0; var9 < b9.e(); var9 += 512) {
               int var10 = Math.min(512, b9.e() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(b9.l(var14, var8) - var4.pos.x))
                        .putFloat((float)(b9.B(var14, var8) - var4.pos.y))
                        .putFloat((float)(b9.I(var14, var8) - var4.pos.z))
                        .putFloat((float)b9.i());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.width).putFloat(var1.height).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(n.slice(), var12);
               } catch (Throwable var20) {
                  if (var11 != null) {
                     try {
                        var11.close();
                     } catch (Throwable var17) {
                        var20.addSuppressed(var17);
                     }
                  }

                  throw var20;
               }

               if (var11 != null) {
                  var11.close();
               }

               RenderPass var23 = var3.createRenderPass(
                  () -> "Larp volumetric surface", T.getColorTextureView(), Optional.empty(), T.getDepthTextureView(), OptionalDouble.empty()
               );

               try {
                  mx.i(var23, b);
                  var23.setUniform("LarpMaterial", w6.j().slice());
                  var23.setUniform("DropletBatch", n.slice());
                  var23.draw(6, var10, 0, 0);
               } catch (Throwable var19) {
                  if (var23 != null) {
                     try {
                        var23.close();
                     } catch (Throwable var16) {
                        var19.addSuppressed(var16);
                     }
                  }

                  throw var19;
               }

               if (var23 != null) {
                  var23.close();
               }
            }

            RenderPass var21 = var3.createRenderPass(() -> "Larp volumetric water composite", var1.getColorTextureView(), Optional.empty());

            try {
               mx.i(var21, P);
               var21.setUniform("LarpMaterial", w6.j().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               mx.x(var21, "SceneColor", w6.w(), var22);
               mx.x(var21, "SceneDepth", w6.h(), var24);
               mx.x(var21, "DropletMask", T.getColorTextureView(), var24);
               mx.x(var21, "DropletDepth", T.getDepthTextureView(), var24);
               var21.draw(3, 1, 0, 0);
            } catch (Throwable var18) {
               if (var21 != null) {
                  try {
                     var21.close();
                  } catch (Throwable var15) {
                     var18.addSuppressed(var15);
                  }
               }

               throw var18;
            }

            if (var21 != null) {
               var21.close();
            }
         }
      }
   }

   public static void a() {
      if (T != null) {
         T.destroyBuffers();
         T = null;
      }

      if (n != null) {
         n.close();
         n = null;
      }
   }

   private static Identifier s(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private tt() {
   }
}
