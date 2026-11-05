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

public final class eb {
   private static final RenderPipeline d = RenderPipeline.builder(new Snippet[0])
      .withLocation(f("world_color"))
      .withVertexShader(f("post/fullscreen"))
      .withFragmentShader(f("post/world_color"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Source", hj.G).withUniform("Grade", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final e4 i = new e4();
   private static GpuBuffer E;
   private static double x = -100.0;
   private static int g;
   private static int W = -1;
   private static boolean I;

   public static void C(int var0) {
      x = ty.K();
      g = var0;
   }

   public static void E() {
      Minecraft var0 = Minecraft.getInstance();
      WorldColorModule var1 = WorldColorModule.N();
      if (var0.level != null) {
         double var2 = Math.max(0.0, 1.0 - (ty.K() - x) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (W != m5.k) {
               f();
               I = (boolean)0;
               W = m5.k;
            }

            if (!I) {
               long var5 = kx.G();

               try {
                  if (E == null) {
                     ea.z(d);
                     E = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     mt.K("Shader", "WorldColor shader loaded");
                  }

                  RenderTarget var7 = var0.gameRenderer.mainRenderTarget();
                  i.e(var7.width, var7.height, 1, e4.o.c);
                  m5.l(ea.m, var7, i.b);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.w.P().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.A.P().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.Q.P().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.K.P().floatValue() : 1.0F);
                     k(var10, var4 != 0 ? var1.S.P() : -1, var4 != 0 ? var1.D.P().floatValue() : 0.0F);
                     k(var10, var4 != 0 ? var1.G.P() : -1, 0.0F);
                     k(var10, var4 != 0 ? var1.i.P() : -1, 0.0F);
                     k(var10, g, (float)var2);
                     var10.putFloat(1.0F / var7.width).putFloat(1.0F / var7.height).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(E.slice(), var10);
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
                     hj.P(var26, d);
                     var26.setUniform("Grade", E.slice());
                     hj.u(var26, "Source", i.b.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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
                  I = (boolean)1;
                  f();
                  mt.P("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  kx.u("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void k(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void f() {
      i.close();
      if (E != null) {
         E.close();
         E = null;
      }
   }

   public static void s() {
      x = -100.0;
      f();
   }

   private static Identifier f(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}
