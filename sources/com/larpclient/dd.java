package com.larpclient;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
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

public final class dd {
   private static final RenderPipeline B = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "menu_nebula"))
      .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "menu/nebula"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("MenuParameters", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final long o = System.nanoTime();
   private static final double[][] H = new double[140][4];
   private static TextureTarget J;
   private static GpuBuffer L;
   private static int q = -1;
   private static boolean X;
   private static long G;
   private static float e;
   private static float T;
   private static final Map<f7, Boolean> R;
   private static int W;
   private static f7 a;
   private static f7 l;
   private static final u7 h;
   private static final m3 g;

   private dd() {
   }

   public static void a(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      D(var0, var1, var2, var3 ? 0.8 : 1.0, true);
   }

   private static void D(GuiGraphicsExtractor var0, int var1, int var2, double var3, boolean var5) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var6 = Minecraft.getInstance();
         double var7 = var5 ? (System.nanoTime() - o) / 1.0E9 : 0.0;
         if (q != jk.v) {
            S();
            q = jk.v;
            X = (boolean)0;
         }

         byte var9 = 0;
         if (!X) {
            try {
               Window var10 = var6.getWindow();
               int var11 = var10.getWidth();
               int var12 = var10.getHeight();
               double var13 = Math.min(1.0, 900.0 / Math.max(1, var11));
               int var15 = Math.max(1, (int)Math.round(var11 * var13));
               int var16 = Math.max(1, (int)Math.round(var12 * var13));
               if (J == null || J.width != var15 || J.height != var16) {
                  S();
                  e4.U(B);
                  J = of.T("Larp website nebula", var15, var16, false, GpuFormat.RGBA8_UNORM);
                  L = RenderSystem.getDevice().createBuffer(() -> "Larp menu parameters", 136, 32L);
               }

               long var17 = System.nanoTime();
               if (G == 0L || var17 - G >= 33000000L) {
                  float var19 = var5 ? (float)(var6.mouseHandler.xpos() / Math.max(1, var10.getScreenWidth()) - 0.5) : 0.0F;
                  float var20 = var5 ? (float)(0.5 - var6.mouseHandler.ypos() / Math.max(1, var10.getScreenHeight())) : 0.0F;
                  if (var5) {
                     e = e + (var19 - e) * 0.025F;
                     T = T + (var20 - T) * 0.025F;
                  } else {
                     T = 0.0F;
                     e = 0.0F;
                  }

                  CommandEncoder var21 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var22 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var23 = Std140Builder.onStack(var22, 32)
                        .putVec4(new Vector4f(var15, var16, (float)var7, 0.0F))
                        .putVec4(new Vector4f(e, T, 0.0F, 0.0F))
                        .get();
                     var21.writeToBuffer(L.slice(), var23);
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

                  RenderPass var35 = var21.createRenderPass(() -> "Larp menu nebula", J.getColorTextureView(), Optional.empty());

                  try {
                     of.z(var35, B);
                     var35.setUniform("MenuParameters", L.slice());
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

                  G = var17;
               }

               var0.guiRenderState
                  .addGuiElement(
                     new BlitRenderState(
                        RenderPipelines.GUI_TEXTURED,
                        TextureSetup.singleTexture(J.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)),
                        new Matrix3x2f(var0.pose()),
                        0,
                        0,
                        var1,
                        var2,
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        oh.i(-1, var3),
                        null
                     )
                  );
               var9 = 1;
            } catch (RuntimeException var29) {
               X = (boolean)1;
               S();
               r4.r("Menu", "Nebula unavailable; using dark blue fallback until resource reload", var29);
            }
         }

         if (var9 == 0) {
            fr.Y(var0, 0, 0, var1, var2, oh.i(-16116958, var3), oh.i(-16118252, var3));
         }

         double var30 = Math.max(1, var6.getWindow().getGuiScale());
         int var31 = Math.min(140, Math.max(1, (int)Math.round(var1 * var2 * var30 * var30 / 10000.0)));

         for (int var32 = 0; var32 < var31; var32++) {
            double[] var14 = H[var32];
            double var33 = (0.2 + (1.0 + Math.sin(var7 * 0.6 + var14[3])) * 0.18) * 0.26;
            int var34 = Math.max(1, (int)Math.round(var14[2] * 2.0 / var30));
            fr.k(var0, (int)(var14[0] * var1), (int)(var14[1] * var2), var34, var34, oh.i(-4201985, var33 * var3));
         }
      }
   }

   public static double q() {
      return g.i();
   }

   public static double d() {
      return g.h();
   }

   public static void s(GuiGraphicsExtractor var0, int var1, int var2, f7 var3, boolean var4, boolean var5) {
      if (!(Minecraft.getInstance().gui.screen() instanceof pl)) {
         dm.r(var0, var1, var2);
         if (var4) {
            fr.k(var0, 0, 0, var1, var2, -2012607472);
         }
      } else {
         Minecraft var6 = Minecraft.getInstance();
         Window var7 = var6.getWindow();
         g.R(
            var6.mouseHandler.xpos() / Math.max(1, var7.getScreenWidth()) * 2.0 - 1.0,
            var6.mouseHandler.ypos() / Math.max(1, var7.getScreenHeight()) * 2.0 - 1.0,
            System.nanoTime(),
            var5
         );
         if (a != var3) {
            l = a;
            a = var3;
            h.c(l != null && var5 ? 0.0 : 1.0);
         }

         double var8 = var5 ? h.y(1.0, 260) : 1.0;
         if (var8 < 0.999 && l != null) {
            v(var0, l, 0, 0, var1, var2, 1.0, var5);
         }

         v(var0, var3, 0, 0, var1, var2, var8, var5);
         if (var8 >= 0.999) {
            l = null;
         }

         if (var4) {
            fr.k(var0, 0, 0, var1, var2, -2012607472);
         }
      }
   }

   public static void v(GuiGraphicsExtractor var0, f7 var1, int var2, int var3, int var4, int var5, double var6, boolean var8) {
      if (var4 > 0 && var5 > 0 && !(var6 <= 0.0)) {
         if (var1 == f7.o) {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var2, var3);
               if (var8) {
                  var0.pose().translate((float)(var4 * 0.0225 * (g.i() - 1.0)), (float)(var5 * 0.0225 * (g.h() - 1.0)));
                  var0.pose().scale(1.045F, 1.045F);
               }

               D(var0, var4, var5, var6, LarpClient.y().v().E().o);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            if (W != jk.v) {
               W = jk.v;
               R.clear();
            }

            Identifier var9 = Identifier.fromNamespaceAndPath("larp-client", "textures/menu/" + var1.e + ".png");
            boolean var10 = R.computeIfAbsent(var1, var2x -> {
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
                  r4.m("Menu", "Missing/invalid {} artwork; using nebula fallback: {}", var1.f, var12.toString());
                  return false;
               }
            });
            if (!var10) {
               v(var0, f7.o, var2, var3, var4, var5, var6, var8);
            } else {
               try {
                  pu var11 = pu.cover(var4, var5, var8 ? 1.045 : 1.0, var8 ? g.i() : 0.0, var8 ? g.h() : 0.0);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED, var9, var2, var3, var11.u(), var11.v(), var4, var5, var11.width(), var11.height(), 1672, 941, oh.i(-1, var6)
                  );
               } catch (RuntimeException var14) {
                  R.put(var1, false);
                  r4.r("Menu", "Wallpaper unavailable; using nebula fallback until reload", var14);
                  v(var0, f7.o, var2, var3, var4, var5, var6, var8);
               }
            }
         }
      }
   }

   public static void S() {
      dm.X();
      if (J != null) {
         J.destroyBuffers();
         J = null;
      }

      if (L != null) {
         L.close();
         L = null;
      }

      G = 0L;
   }

   static {
      Random var0 = new Random(10842L);

      for (double[] var4 : H) {
         var4[0] = var0.nextDouble();
         var4[1] = var0.nextDouble();
         var4[2] = 0.3 + var0.nextDouble() * 0.8;
         var4[3] = var0.nextDouble() * Math.PI * 2.0;
      }

      R = new EnumMap<>(f7.class);
      W = -1;
      h = new u7(1.0);
      g = new m3();
   }
}
