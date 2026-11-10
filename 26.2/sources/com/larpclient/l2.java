package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class l2 {
   private static final RenderPipeline C = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "menu_nebula"))
      .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "menu/nebula"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("MenuParameters", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final long W = System.nanoTime();
   private static final double[][] m = new double[140][4];
   private static TextureTarget b;
   private static GpuBuffer f;
   private static int g = -1;
   private static boolean y;
   private static long X;
   private static float w;
   private static float j;
   private static final Map<t2, Boolean> B;
   private static int Z;
   private static t2 P;
   private static t2 d;
   private static final jp M;
   private static final up I;

   private l2() {
   }

   public static void Z(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      s(var0, var1, var2, var3 ? 0.8 : 1.0, true);
   }

   private static void s(GuiGraphicsExtractor var0, int var1, int var2, double var3, boolean var5) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var6 = Minecraft.getInstance();
         double var7 = var5 ? (System.nanoTime() - W) / 1.0E9 : 0.0;
         if (g != wa.s) {
            C();
            g = wa.s;
            y = (boolean)0;
         }

         byte var9 = 0;
         if (!y) {
            try {
               Window var10 = var6.getWindow();
               int var11 = var10.getWidth();
               int var12 = var10.getHeight();
               double var13 = Math.min(1.0, 900.0 / Math.max(1, var11));
               int var15 = Math.max(1, (int)Math.round(var11 * var13));
               int var16 = Math.max(1, (int)Math.round(var12 * var13));
               if (b == null || b.width != var15 || b.height != var16) {
                  C();
                  ai.U(C);
                  b = mx.n("Larp website nebula", var15, var16, false, GpuFormat.RGBA8_UNORM);
                  f = RenderSystem.getDevice().createBuffer(() -> "Larp menu parameters", 136, 32L);
               }

               long var17 = System.nanoTime();
               if (X == 0L || var17 - X >= 33000000L) {
                  float var19 = var5 ? (float)(var6.mouseHandler.xpos() / Math.max(1, var10.getScreenWidth()) - 0.5) : 0.0F;
                  float var20 = var5 ? (float)(0.5 - var6.mouseHandler.ypos() / Math.max(1, var10.getScreenHeight())) : 0.0F;
                  if (var5) {
                     w = w + (var19 - w) * 0.025F;
                     j = j + (var20 - j) * 0.025F;
                  } else {
                     j = 0.0F;
                     w = 0.0F;
                  }

                  CommandEncoder var21 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var22 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var23 = Std140Builder.onStack(var22, 32)
                        .putVec4(new Vector4f(var15, var16, (float)var7, 0.0F))
                        .putVec4(new Vector4f(w, j, 0.0F, 0.0F))
                        .get();
                     var21.writeToBuffer(f.slice(), var23);
                  } catch (Throwable var28) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var26) {
                           var28.addSuppressed(var26);
                        }
                     }

                     throw var28;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  RenderPass var35 = var21.createRenderPass(() -> "Larp menu nebula", b.getColorTextureView(), Optional.empty());

                  try {
                     mx.i(var35, C);
                     var35.setUniform("MenuParameters", f.slice());
                     var35.draw(3, 1, 0, 0);
                  } catch (Throwable var27) {
                     if (var35 != null) {
                        try {
                           var35.close();
                        } catch (Throwable var25) {
                           var27.addSuppressed(var25);
                        }
                     }

                     throw var27;
                  }

                  if (var35 != null) {
                     var35.close();
                  }

                  X = var17;
               }

               var0.guiRenderState
                  .addGuiElement(
                     new BlitRenderState(
                        RenderPipelines.GUI_TEXTURED,
                        TextureSetup.singleTexture(b.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)),
                        new Matrix3x2f(var0.pose()),
                        0,
                        0,
                        var1,
                        var2,
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        uh.H(-1, var3),
                        null
                     )
                  );
               var9 = 1;
            } catch (RuntimeException var29) {
               y = (boolean)1;
               C();
               h.m("Menu", "Nebula unavailable; using dark blue fallback until resource reload", var29);
            }
         }

         if (var9 == 0) {
            kv.d(var0, 0, 0, var1, var2, uh.H(-16116958, var3), uh.H(-16118252, var3));
         }

         double var30 = Math.max(1, var6.getWindow().getGuiScale());
         int var31 = Math.min(140, Math.max(1, (int)Math.round(var1 * var2 * var30 * var30 / 10000.0)));

         for (int var32 = 0; var32 < var31; var32++) {
            double[] var14 = m[var32];
            double var33 = (0.2 + (1.0 + Math.sin(var7 * 0.6 + var14[3])) * 0.18) * 0.26;
            int var34 = Math.max(1, (int)Math.round(var14[2] * 2.0 / var30));
            kv.d(var0, (int)(var14[0] * var1), (int)(var14[1] * var2), var34, var34, uh.H(-4201985, var33 * var3));
         }
      }
   }

   public static double h() {
      return I.i();
   }

   public static double o() {
      return I.l();
   }

   public static void X(GuiGraphicsExtractor var0, int var1, int var2, t2 var3, boolean var4, boolean var5) {
      if (!(Minecraft.getInstance().gui.screen() instanceof hj)) {
         gr.l(var0, var1, var2);
         if (var4) {
            kv.d(var0, 0, 0, var1, var2, -2012607472);
         }
      } else {
         Minecraft var6 = Minecraft.getInstance();
         Window var7 = var6.getWindow();
         I.h(
            var6.mouseHandler.xpos() / Math.max(1, var7.getScreenWidth()) * 2.0 - 1.0,
            var6.mouseHandler.ypos() / Math.max(1, var7.getScreenHeight()) * 2.0 - 1.0,
            System.nanoTime(),
            var5
         );
         if (P != var3) {
            d = P;
            P = var3;
            M.F(d != null && var5 ? 0.0 : 1.0);
         }

         double var8 = var5 ? M.k(1.0, 260) : 1.0;
         if (var8 < 0.999 && d != null) {
            z(var0, d, 0, 0, var1, var2, 1.0, var5);
         }

         z(var0, var3, 0, 0, var1, var2, var8, var5);
         if (var8 >= 0.999) {
            d = null;
         }

         if (var4) {
            kv.d(var0, 0, 0, var1, var2, -2012607472);
         }
      }
   }

   public static void z(GuiGraphicsExtractor var0, t2 var1, int var2, int var3, int var4, int var5, double var6, boolean var8) {
      if (var4 > 0 && var5 > 0 && !(var6 <= 0.0)) {
         if (var1 == t2.K) {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var2, var3);
               if (var8) {
                  var0.pose().translate((float)(var4 * 0.0225 * (I.i() - 1.0)), (float)(var5 * 0.0225 * (I.l() - 1.0)));
                  var0.pose().scale(1.045F, 1.045F);
               }

               s(var0, var4, var5, var6, LarpClient.R().o().g().L);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            if (Z != wa.s) {
               Z = wa.s;
               B.clear();
            }

            Identifier var9 = Identifier.fromNamespaceAndPath("larp-client", "textures/menu/" + var1.B + ".png");
            boolean var10 = B.computeIfAbsent(var1, var2x -> {
               try {
                  Resource var3x = (Resource)Minecraft.getInstance().getResourceManager().getResource(var9).orElseThrow();

                  try (InputStream var4x = var3x.open()) {
                     NativeImage var5x = NativeImage.read(var4x);

                     try {
                        if (var5x.getWidth() != 1672 || var5x.getHeight() != 941) {
                           throw new IOException("Wallpaper dimensions changed");
                        }
                     } catch (Throwable var10x) {
                        if (var5x != null) {
                           try {
                              var5x.close();
                           } catch (Throwable var9x) {
                              var10x.addSuppressed(var9x);
                           }
                        }

                        throw var10x;
                     }

                     if (var5x != null) {
                        var5x.close();
                     }
                  }

                  return true;
               } catch (IOException | RuntimeException var12) {
                  h.g("Menu", "Missing/invalid {} artwork; using nebula fallback: {}", var1.z, var12.toString());
                  return false;
               }
            });
            if (!var10) {
               z(var0, t2.K, var2, var3, var4, var5, var6, var8);
            } else {
               try {
                  bb var11 = bb.cover(var4, var5, var8 ? 1.045 : 1.0, var8 ? I.i() : 0.0, var8 ? I.l() : 0.0);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED, var9, var2, var3, var11.u(), var11.v(), var4, var5, var11.width(), var11.height(), 1672, 941, uh.H(-1, var6)
                  );
               } catch (RuntimeException var14) {
                  B.put(var1, false);
                  h.m("Menu", "Wallpaper unavailable; using nebula fallback until reload", var14);
                  z(var0, t2.K, var2, var3, var4, var5, var6, var8);
               }
            }
         }
      }
   }

   public static void C() {
      gr.z();
      if (b != null) {
         b.destroyBuffers();
         b = null;
      }

      if (f != null) {
         f.close();
         f = null;
      }

      X = 0L;
   }

   static {
      Random var0 = new Random(10842L);

      for (double[] var4 : m) {
         var4[0] = var0.nextDouble();
         var4[1] = var0.nextDouble();
         var4[2] = 0.3 + var0.nextDouble() * 0.8;
         var4[3] = var0.nextDouble() * Math.PI * 2.0;
      }

      B = new EnumMap<>(t2.class);
      Z = -1;
      M = new jp(1.0);
      I = new up();
   }
}
