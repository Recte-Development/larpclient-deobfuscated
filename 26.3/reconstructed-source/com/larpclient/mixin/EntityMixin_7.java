package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin_7 {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void N(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)this;
      Minecraft var7 = Minecraft.getInstance();
      if (ViewController.update() != ViewController.Owner.NONE && var6 == var7.player) {
         ViewController.rotate(var1, var3);
         var5.cancel();
      }
   }
}
