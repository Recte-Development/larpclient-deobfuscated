package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public final class uf {
   public static void q(PoseStack var0, Quaternionfc var1) {
      var0.mulPose(var1);
   }

   public static void o(PoseStack var0, Matrix4fc var1) {
      var0.mulPose(var1);
   }

   private uf() {
   }
}
