package com.larpclient;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.TextureFormat;
import java.io.IOException;
import net.minecraft.class_1011;
import net.minecraft.class_10537;
import net.minecraft.class_10539;
import net.minecraft.class_2960;
import net.minecraft.class_3300;

public class ca extends class_10537 {
   private final boolean v;

   public ca(class_2960 var1) {
      this(var1, true);
   }

   protected ca(class_2960 var1, boolean var2) {
      super(var1);
      this.v = var2;
   }

   public class_10539 method_65809(class_3300 var1) throws IOException {
      return class_10539.method_65871(var1, this.method_65859());
   }

   public void method_65857(class_10539 var1) {
      super.method_65857(var1);
      if (this.v) {
         AddressMode var2 = var1.method_65873() ? AddressMode.CLAMP_TO_EDGE : AddressMode.REPEAT;
         FilterMode var3 = var1.method_65872() ? FilterMode.LINEAR : FilterMode.NEAREST;
         this.field_63613 = RenderSystem.getSamplerCache().method_75293(var2, var2, var3, var3, true);
      }
   }

   protected void method_65856(class_1011 var1) {
      if (!this.v) {
         super.method_65856(var1);
      } else {
         GpuDevice var2 = RenderSystem.getDevice();
         this.close();
         int var3 = J(var1.method_4307(), var1.method_4323());
         class_2960 var4 = this.method_65859();
         this.field_56974 = var2.createTexture(() -> var4 + " (mipmapped)", 5, TextureFormat.RGBA8, var1.method_4307(), var1.method_4323(), 1, var3);
         this.field_60597 = var2.createTextureView(this.field_56974);
         CommandEncoder var5 = var2.createCommandEncoder();
         var5.writeToTexture(this.field_56974, var1, 0, 0, 0, 0, var1.method_4307(), var1.method_4323(), 0, 0);
         class_1011 var6 = var1;

         try {
            for (int var7 = 1; var7 < var3; var7++) {
               class_1011 var8 = x(var6);
               if (var6 != var1) {
                  var6.close();
               }

               var6 = var8;
               var5.writeToTexture(this.field_56974, var6, var7, 0, 0, 0, var6.method_4307(), var6.method_4323(), 0, 0);
            }
         } finally {
            if (var6 != var1) {
               var6.close();
            }
         }
      }
   }

   public static int J(int var0, int var1) {
      return 32 - Integer.numberOfLeadingZeros(Math.max(1, Math.max(var0, var1)));
   }

   static class_1011 x(class_1011 var0) {
      int var1 = Math.max(1, var0.method_4307() / 2);
      int var2 = Math.max(1, var0.method_4323() / 2);
      class_1011 var3 = new class_1011(var1, var2, false);
      int var4 = var0.method_4307() - 1;
      int var5 = var0.method_4323() - 1;

      for (int var6 = 0; var6 < var2; var6++) {
         for (int var7 = 0; var7 < var1; var7++) {
            long var8 = 0L;
            long[] var10 = new long[3];
            long[] var11 = new long[3];

            for (int var12 = 0; var12 < 2; var12++) {
               for (int var13 = 0; var13 < 2; var13++) {
                  int var14 = var0.method_61940(Math.min(var4, var7 * 2 + var13), Math.min(var5, var6 * 2 + var12));
                  int var15 = var14 >>> 24;
                  var8 += var15;

                  for (int var16 = 0; var16 < 3; var16++) {
                     int var17 = var14 >> 16 - 8 * var16 & 0xFF;
                     var10[var16] += (long)var17 * var15;
                     var11[var16] += var17;
                  }
               }
            }

            int var18 = (int)((var8 + 2L) / 4L) << 24;

            for (int var19 = 0; var19 < 3; var19++) {
               var18 |= (int)(var8 == 0L ? (var11[var19] + 2L) / 4L : (var10[var19] + var8 / 2L) / var8) << 16 - 8 * var19;
            }

            var3.method_61941(var7, var6, var18);
         }
      }

      return var3;
   }
}
