package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
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

public final class ua {
   private static final int c = 512;
   private static final int b = 8240;
   private static final RenderPipeline d = RenderPipeline.builder(new Snippet[0])
      .withLocation(f("pipeline/temple/droplet_surface"))
      .withVertexShader(f("temple/droplet_surface"))
      .withFragmentShader(f("temple/droplet_surface"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withUniform("DropletBatch", UniformType.UNIFORM_BUFFER).build()
      )
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline O = RenderPipeline.builder(new Snippet[0])
      .withLocation(f("pipeline/temple/droplet_composite"))
      .withVertexShader(f("post/fullscreen"))
      .withFragmentShader(f("temple/droplet_composite"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
            .withUniform("SceneColor", hj.G)
            .withUniform("SceneDepth", hj.G)
            .withUniform("DropletMask", hj.G)
            .withUniform("DropletDepth", hj.G)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static TextureTarget N;
   private static GpuBuffer Z;

   public static void X() {
      if (hr.f() != 0 && k.h()) {
         LevelRenderState var0 = k.F();
         if (var0 != null) {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (N == null || N.width != var1.width || N.height != var1.height) {
               P();
               N = hj.j("Larp volumetric water surface", var1.width, var1.height, true, GpuFormat.RGBA8_UNORM);
            }

            if (Z == null) {
               ea.z(d);
               ea.z(O);
               Z = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               mt.K("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            k.o();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(N.getColorTexture(), new Vector4f(), N.getDepthTexture(), 0.0);
            var3.copyTextureToTexture(var1.getDepthTexture(), N.getDepthTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(var4.viewRotationMatrix).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = ty.g();

            for (short var9 = 0; var9 < hr.f(); var9 += 512) {
               int var10 = Math.min(512, hr.f() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(hr.s(var14, var8) - var4.pos.x))
                        .putFloat((float)(hr.G(var14, var8) - var4.pos.y))
                        .putFloat((float)(hr.I(var14, var8) - var4.pos.z))
                        .putFloat((float)hr.f());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.width).putFloat(var1.height).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(Z.slice(), var12);
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
                  () -> "Larp volumetric surface", N.getColorTextureView(), Optional.empty(), N.getDepthTextureView(), OptionalDouble.empty()
               );

               try {
                  hj.P(var23, d);
                  var23.setUniform("LarpMaterial", k.W().slice());
                  var23.setUniform("DropletBatch", Z.slice());
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
               hj.P(var21, O);
               var21.setUniform("LarpMaterial", k.W().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               hj.u(var21, "SceneColor", k.y(), var22);
               hj.u(var21, "SceneDepth", k.D(), var24);
               hj.u(var21, "DropletMask", N.getColorTextureView(), var24);
               hj.u(var21, "DropletDepth", N.getDepthTextureView(), var24);
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

   public static void P() {
      if (N != null) {
         N.destroyBuffers();
         N = null;
      }

      if (Z != null) {
         Z.close();
         Z = null;
      }
   }

   private static Identifier f(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private ua() {
   }
}
