package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public final class fx {
   public static void b(PoseStack var0, Quaternionfc var1) {
      var0.rotate(var1);
   }

   public static void Z(PoseStack var0, Matrix4fc var1) {
      var0.mulPose(var1);
   }

   private fx() {
   }
}
