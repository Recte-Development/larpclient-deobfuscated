package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient.de;
import com.larpclient.fj;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class qb27da2ca7471559b extends Screen {
   protected qb27da2ca7471559b(Component var1) {
      super(var1);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void A(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      LarpClient.W = true;
      if (!de.B()) {
         Screen var3 = this;
         this.addRenderableWidget(Button.builder(Component.literal("Larp client"), var2x -> var2.gui.setScreen(fj.I(var3))).bounds(4, 4, 90, 20).build());
      } else {
         var2.execute(() -> {
            if (var2.gui.screen() instanceof TitleScreen) {
               if (de.B()) {
                  var2.gui.setScreen(new de());
               }
            }
         });
      }
   }
}
