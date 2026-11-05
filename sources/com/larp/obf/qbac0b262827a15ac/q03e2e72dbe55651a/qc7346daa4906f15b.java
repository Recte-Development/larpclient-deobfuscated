package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.zv;
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
public abstract class qc7346daa4906f15b {
   @Shadow
   @Final
   private Minecraft minecraft;

   @ModifyVariable(
      method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private String i(String var1, Font var2, ItemStack var3, int var4, int var5, String var6) {
      var1 = zv.x().U(var3, var1);
      int var7 = var3.getCount();
      if (var7 > 1) {
         var1 = String.valueOf(var7);
      }

      return var1;
   }
}
