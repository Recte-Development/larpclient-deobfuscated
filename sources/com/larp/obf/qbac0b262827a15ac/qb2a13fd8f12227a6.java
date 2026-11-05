package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient.h1;
import com.larpclient.uj;
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
public abstract class qb2a13fd8f12227a6 extends Screen {
   protected qb2a13fd8f12227a6(Component var1) {
      super(var1);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void v(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      LarpClient.h = true;
      if (!uj.l()) {
         Screen var3 = this;
         this.addRenderableWidget(Button.builder(Component.literal("Larp client"), var2x -> var2.gui.setScreen(h1.G(var3))).bounds(4, 4, 90, 20).build());
      } else {
         var2.execute(() -> {
            if (var2.gui.screen() instanceof TitleScreen) {
               if (uj.l()) {
                  var2.gui.setScreen(new uj());
               }
            }
         });
      }
   }
}
