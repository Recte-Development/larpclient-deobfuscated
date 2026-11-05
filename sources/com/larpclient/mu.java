package com.larpclient;

import com.larpclient.module.WorldColorModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class mu {
   private static final RenderPipeline v = RenderPipeline.builder(new Snippet[0])
      .withLocation(z("world_color"))
      .withVertexShader(z("post/fullscreen"))
      .withFragmentShader(z("post/world_color"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Source", of.t).withUniform("Grade", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final ld p = new ld();
   private static GpuBuffer o;
   private static double u = -100.0;
   private static int F;
   private static int N = -1;
   private static boolean d;

   public static void T(int var0) {
      u = ea.y();
      F = var0;
   }

   public static void J() {
      Minecraft var0 = Minecraft.getInstance();
      WorldColorModule var1 = WorldColorModule.S();
      if (var0.level != null) {
         double var2 = Math.max(0.0, 1.0 - (ea.y() - u) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (N != jk.v) {
               s();
               d = (boolean)0;
               N = jk.v;
            }

            if (!d) {
               long var5 = h_.U();

               try {
                  if (o == null) {
                     e4.U(v);
                     o = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     r4.V("Shader", "WorldColor shader loaded");
                  }

                  RenderTarget var7 = var0.gameRenderer.mainRenderTarget();
                  p.m(var7.width, var7.height, 1, ld.y.s);
                  jk.y(e4.g, var7, p.U);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.H.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.S.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.A.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.j.getValue().floatValue() : 1.0F);
                     N(var10, var4 != 0 ? var1.R.getValue() : -1, var4 != 0 ? var1.a.getValue().floatValue() : 0.0F);
                     N(var10, var4 != 0 ? var1.q.getValue() : -1, 0.0F);
                     N(var10, var4 != 0 ? var1.v.getValue() : -1, 0.0F);
                     N(var10, F, (float)var2);
                     var10.putFloat(1.0F / var7.width).putFloat(1.0F / var7.height).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(o.slice(), var10);
                  } catch (Throwable var22) {
                     if (var9 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var20) {
                           var22.addSuppressed(var20);
                        }
                     }

                     throw var22;
                  }

                  if (var9 != null) {
                     var9.close();
                  }

                  RenderPass var26 = var8.createRenderPass(() -> "Larp WorldColor grade + impact", var7.getColorTextureView(), Optional.empty());

                  try {
                     of.z(var26, v);
                     var26.setUniform("Grade", o.slice());
                     of.h(var26, "Source", p.U.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                     var26.draw(3, 1, 0, 0);
                  } catch (Throwable var21) {
                     if (var26 != null) {
                        try {
                           var26.close();
                        } catch (Throwable var19) {
                           var21.addSuppressed(var19);
                        }
                     }

                     throw var21;
                  }

                  if (var26 != null) {
                     var26.close();
                  }
               } catch (RuntimeException var23) {
                  d = (boolean)1;
                  s();
                  r4.r("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  h_.n("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void N(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void s() {
      p.close();
      if (o != null) {
         o.close();
         o = null;
      }
   }

   public static void f() {
      u = -100.0;
      s();
   }

   private static Identifier z(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}
