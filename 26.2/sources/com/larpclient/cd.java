package com.larpclient;

import com.larpclient.module.WorldColorModule;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class cd {
   private static final RenderPipeline w = RenderPipeline.builder(new Snippet[0])
      .withLocation(e("world_color"))
      .withVertexShader(e("post/fullscreen"))
      .withFragmentShader(e("post/world_color"))
      .withBindGroupLayout(BindGroupLayout.builder().withSampler("Source").withUniform("Grade", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final bt r = new bt();
   private static GpuBuffer u;
   private static double Y = -100.0;
   private static int Z;
   private static int x = -1;
   private static boolean j;

   public static void X(int var0) {
      Y = cf.F();
      Z = var0;
   }

   public static void F() {
      Minecraft var0 = Minecraft.getInstance();
      WorldColorModule var1 = WorldColorModule.p();
      if (var0.level != null) {
         double var2 = Math.max(0.0, 1.0 - (cf.F() - Y) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (x != wa.s) {
               U();
               j = (boolean)0;
               x = wa.s;
            }

            if (!j) {
               long var5 = zj.m();

               try {
                  if (u == null) {
                     ai.U(w);
                     u = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     h.A("Shader", "WorldColor shader loaded");
                  }

                  RenderTarget var7 = var0.gameRenderer.mainRenderTarget();
                  r.V(var7.width, var7.height, 1, bt.p.S);
                  wa.Y(ai.C, var7, r.M);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.I.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.e.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.l.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.u.getValue().floatValue() : 1.0F);
                     S(var10, var4 != 0 ? var1.J.getValue() : -1, var4 != 0 ? var1.V.getValue().floatValue() : 0.0F);
                     S(var10, var4 != 0 ? var1.C.getValue() : -1, 0.0F);
                     S(var10, var4 != 0 ? var1.c.getValue() : -1, 0.0F);
                     S(var10, Z, (float)var2);
                     var10.putFloat(1.0F / var7.width).putFloat(1.0F / var7.height).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(u.slice(), var10);
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
                     mx.i(var26, w);
                     var26.setUniform("Grade", u.slice());
                     mx.x(var26, "Source", r.M.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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
                  j = (boolean)1;
                  U();
                  h.m("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  zj.l("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void S(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void U() {
      r.close();
      if (u != null) {
         u.close();
         u = null;
      }
   }

   public static void R() {
      Y = -100.0;
      U();
   }

   private static Identifier e(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}
