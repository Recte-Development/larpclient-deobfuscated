package com.larpclient;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class v1 {
   private static final RenderPipeline j = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "gui_aurora"))
      .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "gui/aurora"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("AuroraParameters", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   public static final int g = 112;
   private static final long z = System.nanoTime();
   private static TextureTarget h;
   private static GpuBuffer b;
   private static int s = -1;
   private static boolean U;
   private static long l;

   private v1() {
   }

   public static boolean f(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7, boolean var8, v1.i var9, double var10) {
      if (var1 > 0 && var2 > 0 && !(var3 <= 0.0)) {
         if (s != nl.q) {
            P();
            s = nl.q;
            U = (boolean)0;
         }

         if (U) {
            return false;
         }

         try {
            Window var12 = Minecraft.getInstance().getWindow();
            double var13 = Math.min(1.0, 640.0 / Math.max(1, var12.getWidth()));
            int var15 = Math.max(1, (int)Math.round(var12.getWidth() * var13));
            int var16 = Math.max(1, (int)Math.round(var12.getHeight() * var13));
            if (h == null || h.width != var15 || h.height != var16) {
               P();
               se.j(j);
               h = p7.m("Larp ClickGUI aurora", var15, var16, false, GpuFormat.RGBA8_UNORM);
               b = RenderSystem.getDevice().createBuffer(() -> "Larp aurora parameters", 136, 112L);
               _q.c("GUI", "Aurora backdrop target {}x{}", var15, var16);
            }

            long var17 = System.nanoTime();
            if (l == 0L || var17 - l >= 33000000L) {
               float var19 = var8 ? (float)((var17 - z) / 1.0E9 % 20000.0) : 0.0F;
               CommandEncoder var20 = RenderSystem.getDevice().createCommandEncoder();
               MemoryStack var21 = MemoryStack.stackPush();

               try {
                  Std140Builder var22 = Std140Builder.onStack(var21, 112);

                  for (Vector4f var26 : i(var15, var16, var19, var5, var6, var7, var9, var10)) {
                     var22.putVec4(var26);
                  }

                  var20.writeToBuffer(b.slice(), var22.get());
               } catch (Throwable var30) {
                  if (var21 != null) {
                     try {
                        var21.close();
                     } catch (Throwable var28) {
                        var30.addSuppressed(var28);
                     }
                  }

                  throw var30;
               }

               if (var21 != null) {
                  var21.close();
               }

               RenderPass var32 = var20.createRenderPass(() -> "Larp ClickGUI aurora", h.getColorTextureView(), Optional.empty());

               try {
                  p7.K(var32, j);
                  var32.setUniform("AuroraParameters", b.slice());
                  var32.draw(3, 1, 0, 0);
               } catch (Throwable var29) {
                  if (var32 != null) {
                     try {
                        var32.close();
                     } catch (Throwable var27) {
                        var29.addSuppressed(var27);
                     }
                  }

                  throw var29;
               }

               if (var32 != null) {
                  var32.close();
               }

               l = var17;
            }

            var0.guiRenderState
               .addGuiElement(
                  new BlitRenderState(
                     RenderPipelines.GUI_TEXTURED,
                     TextureSetup.singleTexture(h.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)),
                     new Matrix3x2f(var0.pose()),
                     0,
                     0,
                     var1,
                     var2,
                     0.0F,
                     1.0F,
                     1.0F,
                     0.0F,
                     vv.N(-1, var3),
                     var0.scissorStack.peek()
                  )
               );
            return true;
         } catch (RuntimeException var31) {
            U = (boolean)1;
            P();
            _q.o("GUI", var9 + " backdrop unavailable; using the plain dim until resource reload", var31);
            return false;
         }
      } else {
         return true;
      }
   }

   public static Vector4f[] z(int var0, int var1, float var2, int var3, int var4, boolean var5) {
      return i(var0, var1, var2, var3, var4, var5, v1.i.J, 0.55);
   }

   public static Vector4f[] i(int var0, int var1, float var2, int var3, int var4, boolean var5, v1.i var6, double var7) {
      float var9 = (float)Math.clamp(var7, 0.0, 1.0) * (var5 ? 0.73F : 1.0F);
      int var10 = vv.Q(var3, var4, 0.5);
      int var11 = vv.Q(var3, -1, 0.35);
      float var12 = (float)Math.clamp(Math.max(K(var3), K(var4)) / 0.5, 0.0, 1.0);
      float var13 = var5 ? 0.12F + 0.14F * var12 : 0.14F + 0.18F * var12;
      return new Vector4f[]{
         new Vector4f(var0, var1, var2, 0.0F),
         M(var3, 0.95F),
         M(var4, 0.85F),
         M(var10, 0.7F),
         M(var11, 0.55F),
         var5 ? new Vector4f(0.086F, 0.086F, 0.1F, var9) : new Vector4f(0.03F, 0.03F, 0.04F, var9),
         new Vector4f(var13, 0.6F, var6.ordinal(), 0.0F)
      };
   }

   static double K(int var0) {
      int var1 = var0 >> 16 & 0xFF;
      int var2 = var0 >> 8 & 0xFF;
      int var3 = var0 & 0xFF;
      return (Math.max(var1, Math.max(var2, var3)) - Math.min(var1, Math.min(var2, var3))) / 255.0;
   }

   private static Vector4f M(int var0, float var1) {
      return new Vector4f((var0 >> 16 & 0xFF) / 255.0F, (var0 >> 8 & 0xFF) / 255.0F, (var0 & 0xFF) / 255.0F, var1);
   }

   public static void P() {
      if (h != null) {
         h.destroyBuffers();
         h = null;
      }

      if (b != null) {
         b.close();
         b = null;
      }

      l = 0L;
   }

   public enum i {
      J,
      r,
      c;
   }
}
