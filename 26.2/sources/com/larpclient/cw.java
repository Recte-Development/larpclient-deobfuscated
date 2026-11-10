package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class cw {
   public static final Identifier X = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
   public static final float Q = 0.15F;
   public static final float H = 2.25F;
   public static final float L = 3.4F;
   public static final float u = 4.2F;
   public static final int T = 48;
   public static final float K = 50.0F;
   public static final float A = 64.0F;

   public static float a(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float s(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float B(float var0, float var1, float var2, float var3) {
      float var4 = F(var0);
      float var5 = F(var1);
      float var6 = F(var2);
      float var7 = F(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return z(var8, 0.09817477F, var7);
   }

   public static cw.c G(float var0, float var1, float var2, float var3, float var4) {
      float var5 = F(var1);
      float var6 = F(var2);
      float var7 = F(var3);
      float var8 = F(var4);
      float var9 = z(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = z(var9, -85.0F, var8);
      var10 = z(var10, -8.0F, var8);
      var11 = z(var11, -4.0F, var8);
      var12 = z(var12, -5.0F, var8);
      var13 = z(var13, 0.1F, var8);
      return new cw.c(
         l(var9) - (float)Math.cos(var0) * 0.2F * var13,
         l(var10) + (float)Math.sin(var0 - 0.6F) * 0.24F * var13,
         l(var11) - (float)(Math.sin(var0) + 0.125) * 0.8F * var13,
         l(var12) - (float)(Math.sin(var0 + 2.0F) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float b(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float u(int var0) {
      double var1 = var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static RenderType L(int var0) {
      return var0 >>> 24 == 255 ? RenderTypes.entityCutout(X) : RenderTypes.entityTranslucent(X);
   }

   private static float F(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float z(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float l(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private cw() {
   }

   public record c(float wingX, float wingY, float wingZ, float tipZ) {
   }

   public static final class d {
      private final ModelPart x;
      private final ModelPart t;
      private final ModelPart I;
      private final ModelPart Q;

      public d(ModelPart var1) {
         ModelPart var2 = var1.getChild("body");
         this.x = var2.getChild("left_wing");
         this.t = this.x.getChild("left_wing_tip");
         this.I = var2.getChild("right_wing");
         this.Q = this.I.getChild("right_wing_tip");
      }

      public static cw.d o() {
         return new cw.d(EnderDragonModel.createBodyLayer().bakeRoot());
      }

      public ModelPart M(int var1) {
         return var1 > 0 ? this.x : this.I;
      }

      public void F(cw.c var1) {
         this.x.setPos(0.0F, 0.0F, 0.0F);
         this.I.setPos(0.0F, 0.0F, 0.0F);
         this.x.setRotation(var1.wingX(), var1.wingY(), var1.wingZ());
         this.I.setRotation(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.t.setRotation(0.0F, 0.0F, var1.tipZ());
         this.Q.setRotation(0.0F, 0.0F, -var1.tipZ());
      }

      public static void Q(PoseStack var0, int var1, float var2, boolean var3) {
         var0.translate(var1 * 2.25F / 16.0F, cw.a(var3) / 16.0F, cw.s(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.scale(var4, var4, var4);
      }

      public void p(PoseStack var1, VertexConsumer var2, float var3, boolean var4, int var5, int var6) {
         for (byte var7 = -1; var7 <= 1; var7 += 2) {
            var1.pushPose();

            try {
               Q(var1, var7, var3, var4);
               this.M(var7).render(var1, var2, var5, OverlayTexture.NO_OVERLAY, var6);
            } finally {
               var1.popPose();
            }
         }
      }
   }
}
