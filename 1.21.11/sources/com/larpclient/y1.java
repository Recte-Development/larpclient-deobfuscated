package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import net.minecraft.class_10789;
import net.minecraft.class_2960;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

public final class y1 {
   private static RenderPipeline c;
   private static RenderPipeline P;
   private static GpuBuffer u;
   private static int X = -1;
   private static int j = -1;
   private static boolean d;

   private y1() {
   }

   public static void t(int var0, float var1) {
      d = (boolean)0;
      if (!(var1 <= 0.0F) && (var0 & 16777215) != 16777215 && j != t_.g) {
         try {
            if (X != t_.g || u == null) {
               P();
               u = RenderSystem.getDevice().createBuffer(() -> "Larp complete lightmap tint", 136, 16L);
               X = t_.g;
            }

            Vector3fc var2 = v(new Vector3f(1.0F), var0, var1);
            MemoryStack var3 = MemoryStack.stackPush();

            try {
               ByteBuffer var4 = var3.malloc(16);
               var4.putFloat(var2.x()).putFloat(var2.y()).putFloat(var2.z()).putFloat(0.0F).flip();
               RenderSystem.getDevice().createCommandEncoder().writeToBuffer(u.slice(), var4);
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

            d = (boolean)1;
         } catch (RuntimeException var8) {
            J("prepare", var8);
         }
      }
   }

   public static RenderPipeline e(RenderPipeline var0, int var1, float var2) {
      if (d && !(var2 <= 0.0F) && (var1 & 16777215) != 16777215 && j != t_.g) {
         try {
            if (c == null) {
               RenderPipeline var3 = f2.i(var0)
                  .withLocation(class_2960.method_60655("larp-client", "lightmap_tint"))
                  .withFragmentShader(class_2960.method_60655("larp-client", "post/lightmap_tint"))
                  .withUniform("LarpLightTint", class_10789.field_60031)
                  .build();
               f2.a(var3);
               c = var3;
               P = var0;
               q8.v("WorldColor", "Native lightmap tint ready; sky, block, ambient and night vision");
            }

            return c;
         } catch (RuntimeException var4) {
            J("pipeline", var4);
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void v(RenderPass var0, RenderPipeline var1) {
      if (var1 == c && u != null) {
         try {
            var0.setUniform("LarpLightTint", u.slice());
         } catch (RuntimeException var4) {
            RenderPipeline var3 = P;
            J("uniform binding", var4);
            if (var3 != null) {
               var0.setPipeline(var3);
            }
         }
      }
   }

   private static void J(String var0, RuntimeException var1) {
      j = t_.g;

      try {
         P();
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }

      q8.u("WorldColor", "Lightmap stage=" + var0 + "; restored native lighting, retained settings; retry after resource reload", var1);
   }

   public static void P() {
      d = (boolean)0;
      P = null;
      c = null;
      GpuBuffer var0 = u;
      u = null;
      if (var0 != null) {
         var0.close();
      }
   }

   public static Vector3fc v(Vector3fc var0, int var1, float var2) {
      if (!(var2 <= 0.0F) && (var1 & 16777215) != 16777215) {
         float var3 = Math.clamp(var2, 0.0F, 1.0F);
         float var4 = 1.0F + ((var1 >>> 16 & 0xFF) / 255.0F - 1.0F) * var3;
         float var5 = 1.0F + ((var1 >>> 8 & 0xFF) / 255.0F - 1.0F) * var3;
         float var6 = 1.0F + ((var1 & 0xFF) / 255.0F - 1.0F) * var3;
         return u(var0, var4, var5, var6);
      } else {
         return var0;
      }
   }

   private static Vector3fc u(Vector3fc var0, float var1, float var2, float var3) {
      return new Vector3f(var0.x() * var1, var0.y() * var2, var0.z() * var3);
   }
}
