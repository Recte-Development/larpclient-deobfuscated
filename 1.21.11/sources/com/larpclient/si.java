package com.larpclient;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.TextureFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1011;
import net.minecraft.class_1044;

public final class si extends class_1044 {
   public static final int w = 4096;
   private final int V;
   private final int o;

   public si(String var1, List<class_1011> var2) {
      if (var2.isEmpty()) {
         throw new IllegalArgumentException("No wallpaper levels");
      }

      class_1011 var3 = (class_1011)var2.getFirst();
      this.V = var3.method_4307();
      this.o = var3.method_4323();
      GpuDevice var4 = RenderSystem.getDevice();
      this.field_56974 = var4.createTexture(() -> var1, 5, TextureFormat.RGBA8, this.V, this.o, 1, var2.size());
      this.field_60597 = var4.createTextureView(this.field_56974);
      CommandEncoder var5 = var4.createCommandEncoder();

      for (int var6 = 0; var6 < var2.size(); var6++) {
         var5.writeToTexture(
            this.field_56974,
            (class_1011)var2.get(var6),
            var6,
            0,
            0,
            0,
            ((class_1011)var2.get(var6)).method_4307(),
            ((class_1011)var2.get(var6)).method_4323(),
            0,
            0
         );
      }

      this.field_63613 = RenderSystem.getSamplerCache()
         .method_75293(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, true);
   }

   public int J() {
      return this.V;
   }

   public int l() {
      return this.o;
   }

   public static List<class_1011> u(class_1011 var0) {
      ArrayList var1 = new ArrayList();
      class_1011 var2 = var0;

      try {
         int var3 = Math.max(var0.method_4307(), var0.method_4323());
         if (var3 > 4096) {
            double var4 = 4096.0 / var3;
            int var6 = Math.max(1, (int)Math.round(var0.method_4307() * var4));
            int var7 = Math.max(1, (int)Math.round(var0.method_4323() * var4));
            class_1011 var8 = new class_1011(var6, var7, false);
            var1.add(var8);
            var0.method_4300(0, 0, var0.method_4307(), var0.method_4323(), var8);
            var0.close();
         } else {
            var1.add(var0);
         }

         var2 = null;
         class_1011 var10 = (class_1011)var1.getFirst();
         int var5 = h(var10.method_4307(), var10.method_4323());

         for (int var11 = 1; var11 < var5; var11++) {
            var1.add(ca.x((class_1011)var1.getLast()));
         }

         return var1;
      } catch (RuntimeException var9) {
         if (var2 != null) {
            var2.close();
         }

         y(var1);
         throw var9;
      }
   }

   public static int h(int var0, int var1) {
      return 32 - Integer.numberOfLeadingZeros(Math.max(1, Math.min(var0, var1)));
   }

   public static void y(List<class_1011> var0) {
      for (class_1011 var2 : var0) {
         var2.close();
      }

      var0.clear();
   }
}
