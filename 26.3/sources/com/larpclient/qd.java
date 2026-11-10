package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public final class qd {
   public static void i(PoseStack var0, Quaternionfc var1) {
      var0.rotate(var1);
   }

   public static void D(PoseStack var0, Matrix4fc var1) {
      var0.mulPose(var1);
   }

   private qd() {
   }
}
