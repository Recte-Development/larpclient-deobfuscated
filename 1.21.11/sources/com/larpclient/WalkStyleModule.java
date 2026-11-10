package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.EmotesModule;
import com.larpclient.module.Module;
import net.minecraft.class_10055;
import net.minecraft.class_591;

public final class WalkStyleModule extends Module {
   private static WalkStyleModule T;
   private final com.larpclient.module.li E = this.addSetting(
      new com.larpclient.module.li(
         "Style",
         "Local walking choreography",
         "Natural",
         "Natural",
         "Swagger",
         "Robot",
         "Sneaky",
         "Hands in pockets",
         "Ninja run",
         "Zombie",
         "Bouncy",
         "Runway"
      )
   );
   private final com.larpclient.module.lo t = this.addSetting(
      new com.larpclient.module.lo("Strength", "Animation strength in percent", 100.0, 30.0, 150.0, 5.0)
   );
   private int l = Integer.MIN_VALUE;
   private boolean C;

   public WalkStyleModule() {
      super("Walk Style", "Walking animations with articulated knees and elbows", Category.RENDER, -1);
      T = this;
   }

   public static WalkStyleModule T() {
      return T;
   }

   @Override
   public void onTick() {
      int var4;
      label17: {
         this.l = this.isInGame() ? getMc().field_1724.method_5628() : Integer.MIN_VALUE;
         if (this.isInGame()) {
            var4 = getMc().field_1690.method_31044().method_31034();
            boolean var2 = false;
            byte var1 = var4;
            var4 = Integer.signum(var1);
            if ((var4 | -var4) >>> 31 == 0) {
               var4 = 1;
               break label17;
            }
         }

         var4 = 0;
      }

      this.C = (boolean)var4;
   }

   @Override
   public void onEnable() {
      this.onTick();
      q8.v("WalkStyle", "Local articulated walking activated");
   }

   @Override
   public void onDisable() {
      this.l = Integer.MIN_VALUE;
   }

   public float[] S(class_591 var1, class_10055 var2) {
      return this.isRuntimeActive()
            && this.C
            && var2.field_53528 == this.l
            && !var2.field_53413
            && !var2.field_53411
            && !var2.field_53412
            && !EmotesModule.Q(var2)
         ? M(this.E.getValue(), this.t.getValue().floatValue(), var1, var2)
         : null;
   }

   public static float[] M(String var0, float var1, class_591 var2, class_10055 var3) {
      float var4 = Math.min(1.0F, var3.field_53451) * var1 / 100.0F;
      if (var4 < 0.02F) {
         return null;
      }

      float var5 = var3.field_53450 * 0.6662F;
      float var6 = (float)Math.sin(var5);
      float var7 = (float)Math.cos(var5);
      float var8 = Math.max(0.0F, var6);
      float var9 = Math.max(0.0F, -var6);
      float var10 = 0.3F + Math.max(0.0F, -var7) * 0.4F;
      float var11 = 0.3F + Math.max(0.0F, var7) * 0.4F;
      float var12 = var8 * 1.1F;
      float var13 = var9 * 1.1F;
      switch (var0) {
         case "Swagger":
            var2.field_3391.field_3675 += var6 * 0.28F * var4;
            var2.field_3401.field_3654 = var7 * 1.25F * var4;
            var2.field_27433.field_3654 = -var7 * 1.25F * var4;
            var2.field_3401.field_3674 += 0.18F * var4;
            var2.field_27433.field_3674 -= 0.18F * var4;
            var2.field_3398.field_3674 += var6 * 0.08F * var4;
            var10 = 0.45F + Math.max(0.0F, -var7) * 0.4F;
            var11 = 0.45F + Math.max(0.0F, var7) * 0.4F;
            var12 = var8 * 0.9F;
            var13 = var9 * 0.9F;
            break;
         case "Robot":
            float var16 = Math.round(var7 * 2.0F) / 2.0F;
            var2.field_3401.field_3654 = var16 * 0.8F * var4;
            var2.field_27433.field_3654 = -var16 * 0.8F * var4;
            var2.field_3401.field_3674 = var2.field_27433.field_3674 = 0.0F;
            var2.field_3392.field_3654 = -var16 * 0.7F * var4;
            var2.field_3397.field_3654 = var16 * 0.7F * var4;
            var2.field_3398.field_3675 = var2.field_3398.field_3654 = 0.0F;
            var11 = 1.57F;
            var10 = 1.57F;
            var12 = Math.max(0.0F, var16) * 0.6F;
            var13 = Math.max(0.0F, -var16) * 0.6F;
            break;
         case "Sneaky":
            var2.field_3401.field_3654 = (-0.7F + var7 * 0.25F) * var4;
            var2.field_27433.field_3654 = (-0.7F - var7 * 0.25F) * var4;
            var2.field_3401.field_3674 -= 0.1F * var4;
            var2.field_27433.field_3674 += 0.1F * var4;
            var2.field_3391.field_3654 += 0.2F * var4;
            var2.field_3398.field_3654 += 0.15F * var4;
            var2.field_3392.field_3654 = var7 * 0.7F * var4;
            var2.field_3397.field_3654 = -var7 * 0.7F * var4;
            var11 = 1.5F;
            var10 = 1.5F;
            var12 = 0.7F + var8 * 0.5F;
            var13 = 0.7F + var9 * 0.5F;
            break;
         case "Hands in pockets":
            var2.field_3401.field_3654 = var2.field_27433.field_3654 = 0.2F * var4;
            var2.field_3401.field_3674 = 0.05F;
            var2.field_27433.field_3674 = -0.05F;
            var2.field_3392.field_3654 *= 0.75F;
            var2.field_3397.field_3654 *= 0.75F;
            var2.field_3398.field_3674 += var6 * 0.05F * var4;
            var11 = 0.9F;
            var10 = 0.9F;
            var12 = var8 * 0.6F;
            var13 = var9 * 0.6F;
            break;
         case "Ninja run":
            var2.field_3401.field_3654 = var2.field_27433.field_3654 = 1.25F * var4;
            var2.field_3401.field_3674 = 0.2F * var4;
            var2.field_27433.field_3674 = -0.2F * var4;
            var2.field_3391.field_3654 += 0.35F * var4;
            var2.field_3398.field_3654 -= 0.2F * var4;
            var2.field_3392.field_3654 = var7 * 1.6F * var4;
            var2.field_3397.field_3654 = -var7 * 1.6F * var4;
            var11 = 0.1F;
            var10 = 0.1F;
            var12 = var8 * 1.5F;
            var13 = var9 * 1.5F;
            break;
         case "Zombie":
            var2.field_3401.field_3654 = -1.45F + var6 * 0.06F;
            var2.field_27433.field_3654 = -1.45F - var6 * 0.06F;
            var2.field_3392.field_3654 *= 0.45F;
            var2.field_3397.field_3654 *= 0.45F;
            var2.field_3398.field_3674 += var6 * 0.12F * var4;
            var2.field_3391.field_3674 += var6 * 0.05F * var4;
            var11 = 0.15F;
            var10 = 0.15F;
            var12 = var8 * 0.3F;
            var13 = var9 * 0.3F;
            break;
         case "Bouncy":
            var2.field_3401.field_3654 = var7 * 1.3F * var4;
            var2.field_27433.field_3654 = -var7 * 1.3F * var4;
            var2.field_3392.field_3654 = var7 * 1.5F * var4;
            var2.field_3397.field_3654 = -var7 * 1.5F * var4;
            var2.field_3398.field_3654 = var2.field_3398.field_3654 + Math.abs(var6) * 0.12F * var4;
            var11 = 0.8F;
            var10 = 0.8F;
            var12 = var8 * 1.6F;
            var13 = var9 * 1.6F;
            break;
         case "Runway":
            var2.field_3391.field_3674 += var6 * 0.1F * var4;
            var2.field_3391.field_3675 += var6 * 0.12F * var4;
            var2.field_3401.field_3654 = var7 * 0.45F * var4;
            var2.field_27433.field_3654 = -var7 * 0.45F * var4;
            var2.field_3392.field_3675 = -0.18F * var4;
            var2.field_3397.field_3675 = 0.18F * var4;
            var2.field_3398.field_3674 -= var6 * 0.04F * var4;
            var11 = 0.25F;
            var10 = 0.25F;
            var12 = var8 * 0.7F;
            var13 = var9 * 0.7F;
      }

      return new float[]{-var10 * var4, -var11 * var4, var12 * var4, var13 * var4};
   }
}
