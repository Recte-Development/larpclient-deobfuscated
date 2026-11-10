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

public final class br {
   private static final RenderPipeline i = RenderPipeline.builder(new Snippet[0])
      .withLocation(x("world_color"))
      .withVertexShader(x("post/fullscreen"))
      .withFragmentShader(x("post/world_color"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Source", p7.X).withUniform("Grade", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final bk s = new bk();
   private static GpuBuffer n;
   private static double e = -100.0;
   private static int Q;
   private static int m = -1;
   private static boolean r;

   public static void C(int var0) {
      e = hz.Q();
      Q = var0;
   }

   public static void j() {
      Minecraft var0 = Minecraft.getInstance();
      WorldColorModule var1 = WorldColorModule.x();
      if (var0.level != null) {
         double var2 = Math.max(0.0, 1.0 - (hz.Q() - e) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (m != nl.q) {
               B();
               r = (boolean)0;
               m = nl.q;
            }

            if (!r) {
               long var5 = he.s();

               try {
                  if (n == null) {
                     se.j(i);
                     n = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     _q.z("Shader", "WorldColor shader loaded");
                  }

                  RenderTarget var7 = var0.gameRenderer.mainRenderTarget();
                  s.l(var7.width, var7.height, 1, bk.p.m);
                  nl.G(se.j, var7, s.B);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.E.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.p.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.Q.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.B.getValue().floatValue() : 1.0F);
                     q(var10, var4 != 0 ? var1.i.getValue() : -1, var4 != 0 ? var1.I.getValue().floatValue() : 0.0F);
                     q(var10, var4 != 0 ? var1.T.getValue() : -1, 0.0F);
                     q(var10, var4 != 0 ? var1.M.getValue() : -1, 0.0F);
                     q(var10, Q, (float)var2);
                     var10.putFloat(1.0F / var7.width).putFloat(1.0F / var7.height).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(n.slice(), var10);
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
                     p7.K(var26, i);
                     var26.setUniform("Grade", n.slice());
                     p7.t(var26, "Source", s.B.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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
                  r = (boolean)1;
                  B();
                  _q.o("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  he.T("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void q(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void B() {
      s.close();
      if (n != null) {
         n.close();
         n = null;
      }
   }

   public static void V() {
      e = -100.0;
      B();
   }

   private static Identifier x(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}
