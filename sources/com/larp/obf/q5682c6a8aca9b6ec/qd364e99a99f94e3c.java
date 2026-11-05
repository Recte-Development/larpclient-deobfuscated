package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.pc;
import com.larpclient.rf;
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
public abstract class qd364e99a99f94e3c extends Screen {
   protected qd364e99a99f94e3c(Component var1) {
      super(var1);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void u(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      LarpClient.N = true;
      if (!rf.H()) {
         Screen var3 = this;
         this.addRenderableWidget(Button.builder(Component.literal("Larp client"), var2x -> var2.gui.setScreen(pc.o(var3))).bounds(4, 4, 90, 20).build());
      } else {
         var2.execute(() -> {
            if (var2.gui.screen() instanceof TitleScreen) {
               if (rf.H()) {
                  var2.gui.setScreen(new rf());
               }
            }
         });
      }
   }
}
