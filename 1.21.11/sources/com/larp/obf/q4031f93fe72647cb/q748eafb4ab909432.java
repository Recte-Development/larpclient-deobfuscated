package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.b9;
import com.larpclient.q8;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.class_4184;
import net.minecraft.class_761;
import net.minecraft.class_9779;
import net.minecraft.class_9922;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_761.class)
public class q748eafb4ab909432 {
   @Unique
   private static boolean p;

   @Inject(method = "method_22710", at = @At("HEAD"))
   private void K(
      class_9922 var1,
      class_9779 var2,
      boolean var3,
      class_4184 var4,
      Matrix4f var5,
      Matrix4f var6,
      Matrix4f var7,
      GpuBufferSlice var8,
      Vector4f var9,
      boolean var10,
      CallbackInfo var11
   ) {
      b9.T(var4, var5, var6, var9);
      if (!p) {
         p = true;
         q8.v("Render", "1.21.11 native world matrices and fog captured");
      }
   }
}
