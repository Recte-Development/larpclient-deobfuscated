package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.hi;
import com.larpclient.mh;
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
public abstract class q34a9aaef7485d0da extends Screen {
   protected q34a9aaef7485d0da(Component var1) {
      super(var1);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void a(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      LarpClient.A = true;
      if (!mh.j()) {
         Screen var3 = this;
         this.addRenderableWidget(Button.builder(Component.literal("Larp client"), var2x -> var2.gui.setScreen(hi.w(var3))).bounds(4, 4, 90, 20).build());
      } else {
         var2.execute(() -> {
            if (var2.gui.screen() instanceof TitleScreen) {
               if (mh.j()) {
                  var2.gui.setScreen(new mh());
               }
            }
         });
      }
   }
}
