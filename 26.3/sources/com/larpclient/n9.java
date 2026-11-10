package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class n9 {
   public static final Identifier c = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
   public static final float u = 0.15F;
   public static final float C = 2.25F;
   public static final float H = 3.4F;
   public static final float Z = 4.2F;
   public static final int j = 48;
   public static final float i = 50.0F;
   public static final float M = 64.0F;

   public static float V(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float G(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float L(float var0, float var1, float var2, float var3) {
      float var4 = J(var0);
      float var5 = J(var1);
      float var6 = J(var2);
      float var7 = J(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return L(var8, 0.09817477F, var7);
   }

   public static n9.d z(float var0, float var1, float var2, float var3, float var4) {
      float var5 = J(var1);
      float var6 = J(var2);
      float var7 = J(var3);
      float var8 = J(var4);
      float var9 = L(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = L(var9, -85.0F, var8);
      var10 = L(var10, -8.0F, var8);
      var11 = L(var11, -4.0F, var8);
      var12 = L(var12, -5.0F, var8);
      var13 = L(var13, 0.1F, var8);
      return new n9.d(
         x(var9) - (float)Math.cos(var0) * 0.2F * var13,
         x(var10) + (float)Math.sin(var0 - 0.6F) * 0.24F * var13,
         x(var11) - (float)(Math.sin(var0) + 0.125) * 0.8F * var13,
         x(var12) - (float)(Math.sin(var0 + 2.0F) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float L(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float w(int var0) {
      double var1 = var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static RenderType M(int var0) {
      return var0 >>> 24 == 255 ? RenderTypes.entityCutout(c) : RenderTypes.entityTranslucent(c);
   }

   private static float J(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float L(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float x(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private n9() {
   }

   public record d(float wingX, float wingY, float wingZ, float tipZ) {
   }

   public static final class j {
      private final ModelPart x;
      private final ModelPart b;
      private final ModelPart D;
      private final ModelPart K;

      public j(ModelPart var1) {
         ModelPart var2 = var1.getChild("body");
         this.x = var2.getChild("left_wing");
         this.b = this.x.getChild("left_wing_tip");
         this.D = var2.getChild("right_wing");
         this.K = this.D.getChild("right_wing_tip");
      }

      public static n9.j C() {
         return new n9.j(EnderDragonModel.createBodyLayer().bakeRoot());
      }

      public ModelPart R(int var1) {
         return var1 > 0 ? this.x : this.D;
      }

      public void Y(n9.d var1) {
         this.x.setPos(0.0F, 0.0F, 0.0F);
         this.D.setPos(0.0F, 0.0F, 0.0F);
         this.x.setRotation(var1.wingX(), var1.wingY(), var1.wingZ());
         this.D.setRotation(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.b.setRotation(0.0F, 0.0F, var1.tipZ());
         this.K.setRotation(0.0F, 0.0F, -var1.tipZ());
      }

      public static void L(PoseStack var0, int var1, float var2, boolean var3) {
         var0.translate(var1 * 2.25F / 16.0F, n9.V(var3) / 16.0F, n9.G(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.scale(var4, var4, var4);
      }

      public void d(PoseStack var1, VertexConsumer var2, float var3, boolean var4, int var5, int var6) {
         for (byte var7 = -1; var7 <= 1; var7 += 2) {
            var1.pushPose();

            try {
               L(var1, var7, var3, var4);
               this.R(var7).render(var1, var2, var5, OverlayTexture.NO_OVERLAY, var6);
            } finally {
               var1.popPose();
            }
         }
      }
   }
}
