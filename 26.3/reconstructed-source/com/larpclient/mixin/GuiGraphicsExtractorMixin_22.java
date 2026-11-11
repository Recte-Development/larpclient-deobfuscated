package com.larpclient.mixin;

import com.larpclient.util.inventory.DispenserSpoofer;
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
public abstract class GuiGraphicsExtractorMixin_22 {
   @Shadow
   @Final
   private Minecraft minecraft;

   @ModifyVariable(
      method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private String p(String var1, Font var2, ItemStack var3, int var4, int var5, String var6) {
      var1 = DispenserSpoofer.getInstance().spoofName(var3, var1);
      int var7 = var3.getCount();
      if (var7 > 1) {
         var1 = String.valueOf(var7);
      }

      return var1;
   }
}
