package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.p2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiGraphicsExtractor.class)
public abstract class qc9d930813c63ef6d {
   @Shadow
   @Final
   private Minecraft minecraft;

   @ModifyVariable(
      method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private String p(String var1, Font var2, ItemStack var3, int var4, int var5, String var6) {
      var1 = p2.p().A(var3, var1);
      int var7 = var3.getCount();
      if (var7 > 1) {
         var1 = String.valueOf(var7);
      }

      return var1;
   }
}
